package com.dontwait.server.service;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.StringJoiner;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.jose.jws.MacAlgorithm;
import org.springframework.security.oauth2.jwt.JwsHeader;
import org.springframework.security.oauth2.jwt.Jwt;
import org.springframework.security.oauth2.jwt.JwtClaimsSet;
import org.springframework.security.oauth2.jwt.JwtDecoder;
import org.springframework.security.oauth2.jwt.JwtEncoder;
import org.springframework.security.oauth2.jwt.JwtEncoderParameters;
import org.springframework.security.oauth2.jwt.JwtException;
import org.springframework.security.oauth2.jwt.NimbusJwtDecoder;
import org.springframework.security.oauth2.jwt.NimbusJwtEncoder;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;

import com.dontwait.server.entity.User;
import com.nimbusds.jose.jwk.source.ImmutableSecret;

import javax.crypto.spec.SecretKeySpec;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class JwtService {

    private final JwtEncoder jwtEncoder;
    private final JwtDecoder jwtDecoder;

    private static final long ACCESS_TOKEN_EXPIRY_HOURS = 1;
    private static final long REFRESH_TOKEN_EXPIRY_DAYS = 30;
    private static final long REGISTER_TOKEN_EXPIRY_MINUTES = 15;

    public JwtService(@Value("${jwt.signerKey}") String signerKey) {
        // Build encoder
        SecretKeySpec secretKey = new SecretKeySpec(signerKey.getBytes(), "HmacSHA512");
        this.jwtEncoder = new NimbusJwtEncoder(new ImmutableSecret<>(secretKey));

        // Build decoder
        this.jwtDecoder = NimbusJwtDecoder
                .withSecretKey(secretKey)
                .macAlgorithm(MacAlgorithm.HS512)
                .build();
    }

    /**
     * Generate access token for authenticated user
     */
    public String generateAccessToken(User user) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("dontwait-ecommerce")
                .subject(user.getUserId().toString())
                .issuedAt(now)
                .expiresAt(now.plus(ACCESS_TOKEN_EXPIRY_HOURS, ChronoUnit.HOURS))
                .id(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .claim("type", "access")
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS512).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }

    private String buildScope(User user) {
        StringJoiner stringJoiner = new StringJoiner(" ");
        if(!CollectionUtils.isEmpty(user.getRoles()))
                user.getRoles().forEach(stringJoiner::add);
        return stringJoiner.toString();
    }

    /**
     * Generate refresh token for authenticated user
     */
    public String generateRefreshToken(User user) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("dontwait-ecommerce")
                .subject(user.getUserId().toString())
                .issuedAt(now)
                .expiresAt(now.plus(REFRESH_TOKEN_EXPIRY_DAYS, ChronoUnit.DAYS))
                .id(UUID.randomUUID().toString())
                .claim("scope", buildScope(user))
                .claim("type", "refresh")
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS512).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }

    /**
     * Generate short-lived register token after OTP verification (for new users)
     */
    public String generateRegisterToken(String phone, String userType) {
        Instant now = Instant.now();
        JwtClaimsSet claims = JwtClaimsSet.builder()
                .issuer("dontwait-ecommerce")
                .subject(phone)
                .issuedAt(now)
                .expiresAt(now.plus(REGISTER_TOKEN_EXPIRY_MINUTES, ChronoUnit.MINUTES))
                .id(UUID.randomUUID().toString())
                .claim("type", "register")
                .claim("userType", userType)
                .build();

        JwsHeader header = JwsHeader.with(MacAlgorithm.HS512).build();
        return jwtEncoder.encode(JwtEncoderParameters.from(header, claims)).getTokenValue();
    }

    /**
     * Verify and decode a JWT token. Returns Jwt if valid, null if invalid/expired.
     */
    public Jwt verifyToken(String token) {
        try {
            return jwtDecoder.decode(token);
        } catch (JwtException e) {
            log.warn("Invalid JWT token: {}", e.getMessage());
            return null;
        }
    }

    public String extractUserTypeFromRegisterToken(String registerToken) {
    Jwt jwt = verifyToken(registerToken);
    if (jwt == null) return null;
    String type = jwt.getClaimAsString("type");
    if (!"register".equals(type)) return null;
    return jwt.getClaimAsString("userType");
}
    /**
     * Extract phone from register token and validate it's a register-type token
     */
    public String extractPhoneFromRegisterToken(String registerToken) {
        Jwt jwt = verifyToken(registerToken);
        if (jwt == null) {
            return null;
        }
        String type = jwt.getClaimAsString("type");
        if (!"register".equals(type)) {
            return null;
        }
        return jwt.getSubject(); // phone number is the subject
    }
}
