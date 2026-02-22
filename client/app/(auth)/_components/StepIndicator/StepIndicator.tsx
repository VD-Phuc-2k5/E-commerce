"use client";

import { useSellerContext } from "@/contexts/SellerSignupContext";
import styles from "./StepIndicator.module.scss";

export default function StepIndicator() {
  const { step } = useSellerContext();

  const getStepClass = (targetStep: number) =>
    `${styles["verify-phone-step"]} ${step >= targetStep ? styles["active"] : ""}`;

  const getArrowClass = (targetStep: number) =>
    `${styles["verify-phone-step-arrow"]} ${step >= targetStep ? styles["active"] : ""}`;

  return (
    <div className={styles["verify-phone-steps"]}>
      {/* Step 1 */}
      <div className={getStepClass(1)}>
        <div>1</div>
        <p>Xác minh số điện thoại</p>
      </div>
      <div className={getArrowClass(2)} />

      {/* Step 2 */}
      <div className={getStepClass(2)}>
        <div>2</div>
        <p>Tạo mật khẩu</p>
      </div>
      <div className={getArrowClass(3)} />

      {/* Step 3 */}
      <div className={getStepClass(3)}>
        <div>
          <svg viewBox='0 0 15 15'>
            <path d='M6.5 13.6c-.2 0-.5-.1-.7-.2l-5.5-4.8c-.4-.4-.5-1-.1-1.4s1-.5 1.4-.1l4.7 4 6.8-9.4c.3-.4.9-.5 1.4-.2.4.3.5 1 .2 1.4l-7.4 10.3c-.2.2-.4.4-.7.4z' />
          </svg>
        </div>
        <p>Hoàn thành</p>
      </div>
    </div>
  );
}
