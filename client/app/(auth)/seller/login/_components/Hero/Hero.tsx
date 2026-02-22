import Image from "next/image";
import styles from "./Hero.module.scss";

export default function Hero() {
  return (
    <div className={styles["hero-section"]}>
      <div className={styles["hero-section-content"]}>
        <div className={styles["hero-section-heading"]}>
          Bán hàng chuyên nghiệp
        </div>
        <div className={styles["hero-section-description"]}>
          Quản lý shop của bạn một cách hiệu quả hơn trên Shopee với Shopee -
          Kênh Người bán
        </div>
        <Image
          className={styles["hero-section-thumb"]}
          src='/images/seller-login.png'
          width={400}
          height={200}
          alt='seller-login.png'
        />
      </div>
    </div>
  );
}
