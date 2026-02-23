"use client";

import * as yup from "yup";
import Link from "next/link";
import { useForm } from "react-hook-form";
import { yupResolver } from "@hookform/resolvers/yup";
import { useSellerContext } from "@/contexts/SellerSignupContext";
import SuccessIcon from "@/app/(auth)/_components/SuccessIcon/SuccessIcon";
import SubmitButton from "@/app/(auth)/seller/signup/_components/SubmitButton/SubmitButton";
import { PHONE_REGEX } from "@/utils/constants";
import styles from "@/app/(auth)/seller/main.module.scss";

const INVALID_PHONE_MESSAGES = {
  required: "Số điện thoại không được để trống.",
  invalidFormat: "Số điện thoại không hợp lệ."
};

const schema = yup.object({
  phone: yup
    .string()
    .required(INVALID_PHONE_MESSAGES.required)
    .matches(PHONE_REGEX, INVALID_PHONE_MESSAGES.invalidFormat)
    .min(10, INVALID_PHONE_MESSAGES.invalidFormat)
});

type schemaType = yup.InferType<typeof schema>;

const formatPhoneVN = (phone: string) => {
  return `+84${phone.slice(1)}`;
};

export default function SignupForm() {
  const { nextStep, setPhone } = useSellerContext();
  const {
    register,
    handleSubmit,
    formState: { errors, isValid }
  } = useForm({
    resolver: yupResolver(schema)
  });

  const onSubmit = (data: schemaType) => {
    // TO DO: Call API to submit form data to server
    setPhone(formatPhoneVN(data.phone));
    nextStep();
  };

  return (
    <div className={styles["form"]}>
      <div className={styles["form__title"]}>
        <div>
          <div>Đăng ký</div>
        </div>
      </div>
      <div className={styles["form__content"]}>
        <form onSubmit={handleSubmit(onSubmit)}>
          <div className={styles["form-field"]}>
            <div className={styles["form-field__content"]}>
              <input
                className={styles["form-field__control"]}
                placeholder='Số điện thoại'
                {...register("phone")}
                maxLength={10}
              />
              {isValid && <SuccessIcon data-testid='success-icon' />}
            </div>
            <p className={styles["form-field__error"]}>
              {errors.phone?.message}
            </p>
          </div>
          <SubmitButton label='Tiếp theo' type='submit' />
        </form>
      </div>
      <div className={styles["form__footer"]}>
        <div>
          Bạn đã có tài khoản?
          <Link href='/seller/login'>Đăng nhập</Link>
        </div>
      </div>
    </div>
  );
}
