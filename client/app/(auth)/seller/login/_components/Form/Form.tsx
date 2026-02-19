import Link from "next/link";
import styles from "@/app/(auth)/seller/main.module.scss";

export default function Form() {
  return (
    <div className={styles["form"]}>
      <div className={styles["form__title"]}>
        <div>
          <div>Đăng nhập</div>
        </div>
      </div>
      <div className={styles["form__content"]}>
        <form>
          <div className={styles["form-field"]}>
            <div className={styles["form-field__content"]}>
              <input
                className={styles["form-field__control"]}
                placeholder='Email/Số điện thoại'
              />
            </div>
            <p className={styles["form-field__error"]}></p>
          </div>
          <div className={styles["form-field"]}>
            <div className={styles["form-field__content"]}>
              <input
                className={styles["form-field__control"]}
                type='password'
                placeholder='Mật khẩu'
              />
            </div>
            <p className={styles["form-field__error"]}></p>
          </div>

          {/* Forgot password link */}
          {/* TO DO: Add this link in the future */}
          <div className={styles["form__content__forgotPW"]}>
            <Link href='#'>Quên mật khẩu</Link>
          </div>

          <button className={styles["form__content__button"]}>Đăng nhập</button>
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
