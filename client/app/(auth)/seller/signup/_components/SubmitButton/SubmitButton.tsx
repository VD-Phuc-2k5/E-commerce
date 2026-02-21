"use client";

import { useSellerContext } from "@/contexts/SellerSignupContext";
import styles from "@/signup/components/SubmitButton/SubmitButton.module.scss";

type Props = {
  label: string;
  type?: "button" | "submit";
};

export default function SubmitButton({ label, type = "button" }: Props) {
  const { step } = useSellerContext();
  return (
    <button
      type={type}
      className={`${styles["submit-button"]} ${step === 3 && styles["not-allowed"]}`}>
      {label}
    </button>
  );
}
