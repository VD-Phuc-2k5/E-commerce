"use client";

import styles from "./SubmitButton.module.scss";

type Props = {
  label: string;
  type?: "button" | "submit";
};

export default function SubmitButton({ label, type = "button" }: Props) {
  return (
    <button type={type} className={`${styles["submit-button"]}`}>
      {label}
    </button>
  );
}
