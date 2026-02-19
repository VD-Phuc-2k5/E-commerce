import SubmitButton from "@/signup/components/SubmitButton/SubmitButton";
import styles from "@/signup/components/VerifyPhoneForm/Form.module.scss";

export default function Form() {
  return (
    <form className={styles["verify-form"]}>
      <div className={styles["verify-form-content"]}>
        <div className={styles["verify-form-heading"]}>
          Mã xác minh của bạn sẽ được gửi bằng tin nhắn đến
        </div>
        <div className={styles["verify-form-phone-number"]}>
          <div>(+84) 396 597 866</div>
        </div>
        <div className={styles["verify-form-group"]}>
          <div className={styles["verify-form-control"]}>
            <input
              type='tel'
              autoComplete='one-time-code'
              maxLength={6}
              onChange={(e) => {
                const input = e.target;
                input.value = e.target.value.replace(/\D/g, "");
              }}
            />
            <div className={styles["verify-form-lines"]}>
              <div></div>
              <div></div>
              <div></div>
              <div></div>
              <div></div>
              <div></div>
            </div>
          </div>
        </div>
        {/* Error Message */}
        <div className={styles["verify-form-error"]}></div>

        <div className={styles["verify-form-resend"]}>
          Bạn vẫn chưa nhận được?
          <button>Gửi lại</button>
        </div>

        <SubmitButton label='Kế tiếp' />
      </div>
    </form>
  );
}
