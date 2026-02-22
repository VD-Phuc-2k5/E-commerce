"use client";

import { Fragment } from "react";
import Hero from "@/app/(auth)/seller/signup/_components/Hero/Hero";
import SignupForm from "@/app/(auth)/seller/signup/_components/SignupForm/Form";
import VerifyPhoneForm from "@/app/(auth)/seller/signup/_components/VerifyPhoneForm/Form";
import CreatePwForm from "@/app/(auth)/seller/signup/_components/CreatePwForm/Form";
import SuccessCard from "@/app/(auth)/seller/signup/_components/SuccessCard/SuccessCard";
import FormLayout from "@/app/(auth)/_components/FormLayout/FormLayout";
import { useSellerContext } from "@/contexts/SellerSignupContext";
import styles from "@/app/(auth)/seller/main.module.scss";

export default function LoginPage() {
  const { step } = useSellerContext();

  // Verify phone number
  if (step === 1) {
    return (
      <FormLayout title='Nhập mã xác nhận'>
        <VerifyPhoneForm />
      </FormLayout>
    );
  }

  // Create new password
  if (step === 2) {
    return (
      <FormLayout title='Thiết Lập Mật Khẩu'>
        <CreatePwForm />
      </FormLayout>
    );
  }

  // Complete
  if (step === 3) {
    return (
      <FormLayout title='Đăng ký thành công!'>
        <SuccessCard />
      </FormLayout>
    );
  }

  //default
  return (
    <Fragment>
      <section className={styles["seller-register"]}>
        <div className={styles["seller-register__container"]}>
          <Hero />
          <SignupForm />
        </div>
      </section>
    </Fragment>
  );
}
