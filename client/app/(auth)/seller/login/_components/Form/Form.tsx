"use client";

import * as yup from "yup";
import Link from "next/link";
import { useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import {
  PHONE_REGEX,
  EMAIL_REGEX,
  PASSWORD_RULES,
  INVALID_USERNAME_MESSAGES,
  INVALID_PASSWORD_MESSAGES
} from "@/utils/constants";
import styles from "@/app/(auth)/seller/main.module.scss";

const schema = yup.object({
  username: yup
    .string()
    .required(INVALID_USERNAME_MESSAGES.required)
    .test(
      "phone-or-email",
      INVALID_USERNAME_MESSAGES.invalidFormat,
      (value) => {
        if (!value) return false;
        const isPhone =
          PHONE_REGEX.test(value) && yup.string().length(10).isValidSync(value);
        const isEmail = EMAIL_REGEX.test(value);
        return isPhone || isEmail;
      }
    ),
  password: yup
    .string()
    .required(INVALID_PASSWORD_MESSAGES.required)
    .min(8, INVALID_PASSWORD_MESSAGES.invalidFormat)
    .max(16, INVALID_PASSWORD_MESSAGES.invalidFormat)
    .matches(
      PASSWORD_RULES.hasLowercase,
      INVALID_PASSWORD_MESSAGES.invalidFormat
    )
    .matches(
      PASSWORD_RULES.hasUppercase,
      INVALID_PASSWORD_MESSAGES.invalidFormat
    )
    .matches(
      PASSWORD_RULES.allowedChars,
      INVALID_PASSWORD_MESSAGES.invalidFormat
    )
});

type schemaType = yup.InferType<typeof schema>;

export default function Form() {
  const {
    register,
    handleSubmit,
    formState: { errors }
  } = useForm({
    resolver: yupResolver(schema)
  });

  const onsubmit = (data: schemaType) => {
    // TO DO: Call API to submit form data to server
  };

  return (
    <div className={styles["form"]}>
      <div className={styles["form__title"]}>
        <div>
          <div>Đăng nhập</div>
        </div>
      </div>
      <div className={styles["form__content"]}>
        <form onSubmit={handleSubmit(onsubmit)}>
          <div className={styles["form-field"]}>
            <div className={styles["form-field__content"]}>
              <input
                {...register("username")}
                className={styles["form-field__control"]}
                placeholder='Email/Số điện thoại'
              />
            </div>
            <p className={styles["form-field__error"]}>
              {errors.username?.message}
            </p>
          </div>
          <div className={styles["form-field"]}>
            <div className={styles["form-field__content"]}>
              <input
                {...register("password")}
                className={styles["form-field__control"]}
                type='password'
                placeholder='Mật khẩu'
              />
            </div>
            <p className={styles["form-field__error"]}>
              {errors.password?.message}
            </p>
          </div>

          {/* Forgot password link */}
          {/* TO DO: Add this link in the future */}
          <div className={styles["form__content__forgotPW"]}>
            <Link href='#'>Quên mật khẩu</Link>
          </div>

          <button type='submit' className={styles["form__content__button"]}>
            Đăng nhập
          </button>
        </form>
      </div>
      <div className={styles["form__footer"]}>
        <div>
          Bạn mới biết đến Shopee?
          <Link href='/seller/signup'>Đăng ký</Link>
        </div>
      </div>
    </div>
  );
}
