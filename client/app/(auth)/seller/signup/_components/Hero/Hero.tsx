import { CSSProperties } from "react";
import styles from "./Hero.module.scss";

export default function SellerHero() {
  return (
    <div className={styles["seller-hero"]}>
      <div className={styles["seller-hero__content"]}>
        <p className={styles["seller-hero__brand"]}>Shopee Việt Nam</p>

        <p className={styles["seller-hero__title"]}>
          Trở thành Người bán ngay hôm nay
        </p>

        <div className={styles["seller-hero__feature"]}>
          <div
            className={styles["seller-hero__icon"]}
            style={
              {
                "--x": "48.679245283018865%",
                "--y": "1.6366612111292962%",
                "--size": "3780.5555555555557% 948.6111111111111%"
              } as CSSProperties
            }></div>
          <p className={styles["seller-hero__text"]}>
            Nền tảng thương mại điện tử hàng đầu Đông Nam Á và Đài Loan
          </p>
        </div>

        <div className={styles["seller-hero__feature"]}>
          <div
            className={styles["seller-hero__icon"]}
            style={
              {
                "--x": "36.22641509433962%",
                "--y": "1.6366612111292962%",
                "--size": "3780.5555555555557% 948.6111111111111%"
              } as CSSProperties
            }></div>
          <p className={styles["seller-hero__text"]}>
            Phát triển trở thành thương hiệu toàn cầu
          </p>
        </div>

        <div className={styles["seller-hero__feature"]}>
          <div
            className={styles["seller-hero__icon"]}
            style={
              {
                "--x": "16.150943396226417%",
                "--y": "1.594896331738437%",
                "--size": "3780.5555555555557% 1219.642857142857%"
              } as CSSProperties
            }></div>
          <p className={styles["seller-hero__text"]}>
            Dẫn đầu lượng người dùng trên ứng dụng mua sắm tại Việt Nam
          </p>
        </div>
      </div>
    </div>
  );
}
