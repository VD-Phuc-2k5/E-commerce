"use client";

import * as yup from "yup";
import { useState, useMemo } from "react";
import { yupResolver } from "@hookform/resolvers/yup";
import { useForm } from "react-hook-form";
import EyeIcon from "@/app/(auth)/_components/EyeIcon/EyeIcon";
import EyeSlashIcon from "@/app/(auth)/_components/EyeIcon/EyeSlashIcon";
import SubmitButton from "@/app/(auth)/seller/signup/_components/SubmitButton/SubmitButton";
import { useSellerContext } from "@/contexts/SellerSignupContext";
import styles from "./Form.module.scss";

const INVALID_PASSWORD_MESSAGES = {
  required: "Mật khẩu không được để trống.",
  invalidFormat: "Mật khẩu không hợp lệ."
};

const passwordRules = {
  hasLowercase: /[a-z]/,
  hasUppercase: /[A-Z]/,
  allowedChars: /^[A-Za-z0-9!@#$%^&*()_+\-=[\]{}|,.?]+$/
};

const schema = yup.object({
  password: yup
    .string()
    .required(INVALID_PASSWORD_MESSAGES.required)
    .min(8, INVALID_PASSWORD_MESSAGES.invalidFormat)
    .max(16, INVALID_PASSWORD_MESSAGES.invalidFormat)
    .matches(
      passwordRules.hasLowercase,
      INVALID_PASSWORD_MESSAGES.invalidFormat
    )
    .matches(
      passwordRules.hasUppercase,
      INVALID_PASSWORD_MESSAGES.invalidFormat
    )
    .matches(
      passwordRules.allowedChars,
      INVALID_PASSWORD_MESSAGES.invalidFormat
    )
});

type schemaType = yup.InferType<typeof schema>;

export default function Form() {
  const [showPassword, setShowPassword] = useState<boolean>(false);
  const { nextStep } = useSellerContext();
  const {
    register,
    handleSubmit,
    watch,
    formState: { errors }
  } = useForm<schemaType>({
    resolver: yupResolver(schema),
    defaultValues: {
      password: ""
    }
  });

  const password = watch("password") ?? "";

  // Memoized object that tracks password validation status
  // Stores boolean results of testing password against each rule
  // Used to dynamically highlight/activate password requirement items in UI
  const checks = useMemo(
    () => ({
      hasLowercase: passwordRules.hasLowercase.test(password),
      hasUppercase: passwordRules.hasUppercase.test(password),
      hasLength: 8 <= password.length && password.length <= 16,
      allowedChars: passwordRules.allowedChars.test(password)
    }),
    [password]
  );

  const onSubmit = (data: schemaType) => {
    // TO DO: Call API to submit form data to server
    nextStep();
  };

  return (
    <form
      onSubmit={handleSubmit(onSubmit)}
      className={styles["new-password-form"]}>
      <div className={styles["new-password-form-content"]}>
        <div className={styles["new-password-form-heading"]}>
          Bước cuối! Thiết lập mật khẩu để hoàn tất việc đăng ký.
        </div>

        <div className={styles["form-field"]}>
          <div className={styles["form-field-content"]}>
            <input
              {...register("password")}
              type={showPassword ? "text" : "password"}
              className={styles["form-field-control"]}
              placeholder='Mật khẩu'
            />
            <button
              type='button'
              aria-label='toggle password visibility'
              onClick={() => {
                setShowPassword((prev) => !prev);
              }}>
              {showPassword ? <EyeIcon /> : <EyeSlashIcon />}
            </button>
          </div>

          <p className={styles["form-field__error"]}>
            {errors.password?.message}
          </p>

          <ul className={styles["password-rules"]}>
            <li className={`${checks.hasLowercase ? styles.active : ""}`}>
              Ít nhất một kí tự viết thường
            </li>
            <li className={`${checks.hasUppercase ? styles.active : ""}`}>
              Ít nhất một kí tự viết hoa
            </li>
            <li className={`${checks.hasLength ? styles.active : ""}`}>
              8 - 16 kí tự
            </li>
            <li className={`${checks.allowedChars ? styles.active : ""}`}>
              Chỉ các chữ cái, số và kí tự phổ biến mới có thể được sử dụng
            </li>
          </ul>
        </div>
        <SubmitButton label='Đăng ký' type='submit' />
      </div>
    </form>
  );
}
