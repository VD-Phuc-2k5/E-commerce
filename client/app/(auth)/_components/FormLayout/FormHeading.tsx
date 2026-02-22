"use client";

import { useSellerContext } from "@/contexts/SellerSignupContext";
import styles from "./FormLayout.module.scss";

type Props = {
  title: string;
};

export default function FormHeading({ title }: Props) {
  const { step } = useSellerContext();
  return (
    <div
      className={`${styles["verify-card-heading-label"]} ${step === 3 && styles["no-padding"]}`}>
      <div>{title}</div>
    </div>
  );
}
