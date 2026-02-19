"use client";

import { ChangeEvent, useState, useMemo } from "react";
import EyeIcon from "@/auth/components/EyeIcon/EyeIcon";
import SubmitButton from "@/signup/components/SubmitButton/SubmitButton";
import EyeSlashIcon from "@/auth/components/EyeIcon/EyeSlashIcon";
import styles from "@/signup/components/CreatePwForm/Form.module.scss";

const passwordRules = {
  hasLowercase: /[a-z]/,
  hasUppercase: /[A-Z]/,
  hasLength: /^.{8,16}$/,
  allowedChars: /^[A-Za-z0-9!@#$%^&*()_+\-=[\]{};':"\\|,.<>/?]+$/
};

export default function Form() {
  const [showPassword, setShowPassword] = useState<boolean>(false);
  const [password, setPassword] = useState<string>("");

  // Memoized object that tracks password validation status
  // Stores boolean results of testing password against each rule
  // Used to dynamically highlight/activate password requirement items in UI
  const checks = useMemo(
    () => ({
      hasLowercase: passwordRules.hasLowercase.test(password),
      hasUppercase: passwordRules.hasUppercase.test(password),
      hasLength: passwordRules.hasLength.test(password),
      allowedChars: passwordRules.allowedChars.test(password)
    }),
    [password]
  );

  const onChange = (e: ChangeEvent<HTMLInputElement>) => {
    const password = e.target.value;
    setPassword(password);
  };

  return (
    <form className={styles["new-password-form"]}>
      <div className={styles["new-password-form-content"]}>
        <div className={styles["new-password-form-heading"]}>
          Bước cuối! Thiết lập mật khẩu để hoàn tất việc đăng ký.
        </div>

        <div className={styles["form-field"]}>
          <div className={styles["form-field-content"]}>
            <input
              onChange={onChange}
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
        <SubmitButton label='Đăng ký' />
      </div>
    </form>
  );
}
