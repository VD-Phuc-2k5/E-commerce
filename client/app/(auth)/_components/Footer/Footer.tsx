"use client";

import Image from "next/image";
import styles from "./Footer.module.scss";

const steps = [
  {
    number: "01",
    heading: "Đăng ký tài khoản Shopee",
    description:
      "Tại trang Shopee, nhấn Đăng Ký để tạo tài khoản. Sau đó, nhập Số điện thoại và Email tại trang Tài Khoản Của Tôi để xác minh tài khoản."
  },
  {
    number: "02",
    heading: "Cài đặt thông tin cửa hàng",
    description:
      "Đi đến Kênh Người Bán, đặt tên Shop và thiết lập địa chỉ lấy hàng của bạn."
  },
  {
    number: "03",
    heading: "Cài đặt vận chuyển",
    description: "Thiết lập phương thức vận chuyển cho Shop và nhấn Hoàn tất."
  },
  {
    number: "04",
    heading: "Đăng bán sản phẩm",
    description:
      "Chọn Thêm Sản Phẩm, sau đó điền chi tiết thông tin và nhấn Lưu & Hiển thị để hoàn tất."
  }
];

export default function Footer() {
  return (
    <div className={styles.footer}>
      <div className={styles["footer-title"]}>
        CÁC BƯỚC MỞ CỬA HÀNG TRÊN SHOPEE
      </div>
      <section className={styles["footer-content"]}>
        {steps.map((step) => (
          <div key={step.number} className={styles["footer-content-item"]}>
            <div className={styles["footer-content-item-nth"]}>
              {step.number}
            </div>
            <div className={styles["footer-content-item-heading"]}>
              {step.heading}
            </div>
            <div className={styles["footer-content-item-description"]}>
              {step.description}
            </div>
          </div>
        ))}
        <Image
          className={styles["footer-overlay"]}
          src='/images/signup-overlay.png'
          width={1366}
          height={167}
          alt='overlay'
        />
      </section>
    </div>
  );
}
