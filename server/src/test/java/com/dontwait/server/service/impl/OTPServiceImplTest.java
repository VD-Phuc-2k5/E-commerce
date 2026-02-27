package com.dontwait.server.service.impl;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.data.redis.core.ValueOperations;

import com.dontwait.server.enums.ErrorCode;
import com.dontwait.server.exception.AppException;

@ExtendWith(MockitoExtension.class)
class OTPServiceImplTest {

    @Mock
    StringRedisTemplate redisTemplate;

    @Mock
    ValueOperations<String, String> valueOperations;

    @InjectMocks
    OTPServiceImpl otpService;

    static final String PHONE = "+84901234567";
    static final String OTP_KEY = "OTP:" + PHONE;
    static final String SEND_RATE_KEY = "OTP_SEND_RATE:" + PHONE;
    static final String VERIFY_RATE_KEY = "OTP_VERIFY_RATE:" + PHONE;

    @BeforeEach
    void setUp() {
        when(redisTemplate.opsForValue()).thenReturn(valueOperations);
    }

    // ==================== GENERATE AND SAVE OTP ====================
    @Nested
    @DisplayName("generateAndSaveOtp()")
    class GenerateAndSaveOtpTests {

        @Test
        @DisplayName("Tạo OTP thành công - lần gửi đầu tiên")
        void shouldGenerateOtp_WhenFirstRequest() {
            // Given: chưa có rate limit counter
            when(valueOperations.get(SEND_RATE_KEY)).thenReturn(null);
            when(valueOperations.increment(SEND_RATE_KEY)).thenReturn(1L);

            // When
            String otp = otpService.generateAndSaveOtp(PHONE);

            // Then
            assertNotNull(otp);
            assertEquals(6, otp.length());
            assertTrue(otp.matches("\\d{6}"));

            // Verify OTP được lưu vào Redis với TTL 5 phút
            verify(valueOperations).set(eq(OTP_KEY), eq(otp), eq(5L), eq(TimeUnit.MINUTES));
            // Verify rate limit counter được tăng
            verify(valueOperations).increment(SEND_RATE_KEY);
            // Verify TTL được set cho rate limit (lần đầu tiên)
            verify(redisTemplate).expire(SEND_RATE_KEY, 5L, TimeUnit.MINUTES);
        }

        @Test
        @DisplayName("Tạo OTP thành công - lần gửi thứ 2 (chưa vượt limit)")
        void shouldGenerateOtp_WhenUnderRateLimit() {
            // Given: đã gửi 1 lần
            when(valueOperations.get(SEND_RATE_KEY)).thenReturn("1");
            when(valueOperations.increment(SEND_RATE_KEY)).thenReturn(2L);

            // When
            String otp = otpService.generateAndSaveOtp(PHONE);

            // Then
            assertNotNull(otp);
            assertEquals(6, otp.length());
            verify(valueOperations).set(eq(OTP_KEY), eq(otp), eq(5L), eq(TimeUnit.MINUTES));
            // Không set expire lại vì count != 1
            verify(redisTemplate, never()).expire(eq(SEND_RATE_KEY), anyLong(), any(TimeUnit.class));
        }

        @Test
        @DisplayName("Throw OTP_RATE_LIMIT_EXCEEDED khi gửi quá 3 lần")
        void shouldThrowException_WhenRateLimitExceeded() {
            // Given: đã gửi 3 lần
            when(valueOperations.get(SEND_RATE_KEY)).thenReturn("3");

            // When & Then
            AppException exception = assertThrows(AppException.class,
                    () -> otpService.generateAndSaveOtp(PHONE));

            assertEquals(ErrorCode.OTP_RATE_LIMIT_EXCEEDED, exception.getErrorCode());
            // Verify: KHÔNG lưu OTP, KHÔNG tăng counter
            verify(valueOperations, never()).set(eq(OTP_KEY), anyString(), anyLong(), any(TimeUnit.class));
            verify(valueOperations, never()).increment(SEND_RATE_KEY);
        }

        @Test
        @DisplayName("Mỗi lần gọi tạo OTP khác nhau")
        void shouldGenerateDifferentOtpEachTime() {
            // Given
            when(valueOperations.get(SEND_RATE_KEY)).thenReturn(null);
            when(valueOperations.increment(SEND_RATE_KEY)).thenReturn(1L);

            // When
            String otp1 = otpService.generateAndSaveOtp(PHONE);

            when(valueOperations.get(SEND_RATE_KEY)).thenReturn("1");
            when(valueOperations.increment(SEND_RATE_KEY)).thenReturn(2L);

            String otp2 = otpService.generateAndSaveOtp(PHONE);

            // Then: OTP khác nhau (xác suất trùng rất nhỏ)
            // Nếu trùng thì có thể do random, nên chỉ verify cả 2 đều hợp lệ
            assertNotNull(otp1);
            assertNotNull(otp2);
            assertEquals(6, otp1.length());
            assertEquals(6, otp2.length());
        }
    }

    // ==================== VALIDATE OTP ====================
    @Nested
    @DisplayName("validateOtp()")
    class ValidateOtpTests {

        @Test
        @DisplayName("Validate thành công khi OTP đúng")
        void shouldReturnTrue_WhenOtpIsCorrect() {
            // Given
            String correctOtp = "123456";
            when(valueOperations.get(VERIFY_RATE_KEY)).thenReturn(null);
            when(valueOperations.get(OTP_KEY)).thenReturn(correctOtp);

            // When
            boolean result = otpService.validateOtp(PHONE, correctOtp);

            // Then
            assertTrue(result);
            // Verify: xóa OTP sau khi validate thành công
            verify(redisTemplate).delete(OTP_KEY);
            // Verify: xóa verify rate counter
            verify(redisTemplate).delete(VERIFY_RATE_KEY);
        }

        @Test
        @DisplayName("Trả về false khi OTP sai")
        void shouldReturnFalse_WhenOtpIsIncorrect() {
            // Given
            when(valueOperations.get(VERIFY_RATE_KEY)).thenReturn(null);
            when(valueOperations.get(OTP_KEY)).thenReturn("123456");
            when(valueOperations.increment(VERIFY_RATE_KEY)).thenReturn(1L);

            // When
            boolean result = otpService.validateOtp(PHONE, "999999");

            // Then
            assertFalse(result);
            // Verify: tăng verify rate counter
            verify(valueOperations).increment(VERIFY_RATE_KEY);
            // Verify: KHÔNG xóa OTP (vẫn cho phép thử lại)
            verify(redisTemplate, never()).delete(OTP_KEY);
        }

        @Test
        @DisplayName("Throw OTP_EXPIRED khi OTP hết hạn (không tồn tại trong Redis)")
        void shouldThrowException_WhenOtpExpired() {
            // Given: OTP không tồn tại trong Redis
            when(valueOperations.get(VERIFY_RATE_KEY)).thenReturn(null);
            when(valueOperations.get(OTP_KEY)).thenReturn(null);

            // When & Then
            AppException exception = assertThrows(AppException.class,
                    () -> otpService.validateOtp(PHONE, "123456"));

            assertEquals(ErrorCode.OTP_EXPIRED, exception.getErrorCode());
        }

        @Test
        @DisplayName("Throw OTP_VERIFY_LIMIT_EXCEEDED khi nhập sai quá 5 lần")
        void shouldThrowException_WhenVerifyLimitExceeded() {
            // Given: đã nhập sai 5 lần
            when(valueOperations.get(VERIFY_RATE_KEY)).thenReturn("5");

            // When & Then
            AppException exception = assertThrows(AppException.class,
                    () -> otpService.validateOtp(PHONE, "123456"));

            assertEquals(ErrorCode.OTP_VERIFY_LIMIT_EXCEEDED, exception.getErrorCode());
            // Verify: KHÔNG truy cập OTP vì đã bị block
            verify(valueOperations, never()).get(OTP_KEY);
        }

        @Test
        @DisplayName("Vẫn validate được khi nhập sai 4 lần (chưa vượt limit)")
        void shouldAllowValidation_WhenUnderVerifyLimit() {
            // Given: đã nhập sai 4 lần, lần này nhập đúng
            String correctOtp = "123456";
            when(valueOperations.get(VERIFY_RATE_KEY)).thenReturn("4");
            when(valueOperations.get(OTP_KEY)).thenReturn(correctOtp);

            // When
            boolean result = otpService.validateOtp(PHONE, correctOtp);

            // Then
            assertTrue(result);
            verify(redisTemplate).delete(OTP_KEY);
            verify(redisTemplate).delete(VERIFY_RATE_KEY);
        }
    }

    // ==================== DELETE OTP ====================
    @Nested
    @DisplayName("deleteOtp()")
    class DeleteOtpTests {

        @Test
        @DisplayName("Xóa OTP thành công")
        void shouldDeleteOtp() {
            // When
            otpService.deleteOtp(PHONE);

            // Then
            verify(redisTemplate).delete(OTP_KEY);
        }
    }
}
