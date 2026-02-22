import Link from "next/link";
import styles from "./NavLink.module.scss";

export default function NavLink() {
  // TO DO: Add this link in the future
  return (
    <Link href='#' className={styles["nav-link"]}>
      Bạn cần giúp đỡ?
    </Link>
  );
}
