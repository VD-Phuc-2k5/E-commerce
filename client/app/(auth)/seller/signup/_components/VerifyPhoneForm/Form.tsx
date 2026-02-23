"use client";

import * as yup from "yup";
import { useState, useEffect, useRef } from "react";
import { yupResolver } from "@hookform/resolvers/yup";
import { useForm } from "react-hook-form";
import { useSellerContext } from "@/contexts/SellerSignupContext";
import SubmitButton from "@/app/(auth)/seller/signup/_components/SubmitButton/SubmitButton";
import styles from "./Form.module.scss";

const RESEND_OTP_COUNTDOWN = 300; // 5 minutes

const INVALID_OTP_MESSAGES = {
  required: "Mã OTP không được để trống.",
  invalidFormat: "Mã OTP không hợp lệ."
};

const schema = yup.object({
  otp: yup
    .string()
    .required(INVALID_OTP_MESSAGES.required)
    .matches(/^[0-9]{6}$/, INVALID_OTP_MESSAGES.invalidFormat)
});

type schemaType = yup.InferType<typeof schema>;

const displayPhone = (phone: string) => {
  const local = phone.slice(3);
  return `(+84) ${local.slice(0, 3)} ${local.slice(3, 6)} ${local.slice(6, 9)}`;
};

const displayResendOtpTime = (countdown: number) => {
  const minute = Math.trunc(countdown / 60)
    .toString()
    .padStart(2, "0");
  const second = (countdown % 60).toString().padStart(2, "0");
  return `${minute} : ${second}`;
};

export default function Form() {
  const [countdown, setCountdown] = useState(RESEND_OTP_COUNTDOWN);
  const { nextStep, phone } = useSellerContext();
  const intervalRef = useRef<NodeJS.Timeout | null>(null);
  const {
    register,
    handleSubmit,
    formState: { errors }
  } = useForm({
    resolver: yupResolver(schema)
  });

  useEffect(() => {
    intervalRef.current = setInterval(() => {
      setCountdown((prev) => Math.max(0, prev - 1));
    }, 1000);

    return () => {
      if (intervalRef.current) clearInterval(intervalRef.current);
    };
  }, []);

  useEffect(() => {
    if (countdown === 0 && intervalRef.current) {
      clearInterval(intervalRef.current);
    }
  }, [countdown]);

  const onSubmit = (data: schemaType) => {
    // Clear Interval
    if (intervalRef.current) clearInterval(intervalRef.current);
    // TO DO: Call API to submit form data to server
    nextStep();
  };

  return (
    <form onSubmit={handleSubmit(onSubmit)} className={styles["verify-form"]}>
      <div className={styles["verify-form-content"]}>
        <div className={styles["verify-form-heading"]}>
          Mã xác minh của bạn sẽ được gửi bằng tin nhắn đến
        </div>
        <div className={styles["verify-form-phone-number"]}>
          <div>{displayPhone(phone)}</div>
        </div>
        <div className={styles["verify-form-group"]}>
          <div className={styles["verify-form-control"]}>
            <input
              type='tel'
              autoComplete='one-time-code'
              maxLength={6}
              data-testid='otp-textbox'
              {...register("otp")}
            />
            <div className={styles["verify-form-lines"]}>
              <div></div>
              <div></div>
              <div></div>
              <div></div>
              <div></div>
              <div></div>
            </div>
          </div>
        </div>
        {/* Error Message */}
        <div className={styles["verify-form-error"]}>{errors.otp?.message}</div>

        {countdown <= 0 ? (
          <div className={styles["verify-form-resend"]}>
            Bạn vẫn chưa nhận được?
            <button>Gửi lại</button>
          </div>
        ) : (
          <div className={styles["verify-form-resend"]}>
            Mã OTP sẽ được gửi lại sau:
            <button
              type='button'
              style={{ cursor: "auto" }}
              data-testid='countdown'>
              {displayResendOtpTime(countdown)}
            </button>
          </div>
        )}

        <SubmitButton label='Kế tiếp' type='submit' />
      </div>
    </form>
  );
}
