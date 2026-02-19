import Link from "next/link";
import SuccessIcon from "@/auth/components/SuccessIcon/SuccessIcon";
import SubmitButton from "@/signup/components/SubmitButton/SubmitButton";
import styles from "@/app/(auth)/seller/main.module.scss";

export default function SignupForm() {
  return (
    <div className={styles["form"]}>
      <div className={styles["form__title"]}>
        <div>
          <div>Đăng ký</div>
        </div>
      </div>
      <div className={styles["form__content"]}>
        <form>
          <div className={styles["form-field"]}>
            <div className={styles["form-field__content"]}>
              <input
                className={styles["form-field__control"]}
                placeholder='Số điện thoại'
              />
              <SuccessIcon />
            </div>
            <p className={styles["form-field__error"]}></p>
          </div>
          <SubmitButton label='Tiếp theo' />
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
