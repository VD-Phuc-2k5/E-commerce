import Logo from "@/app/(auth)/_components/Logo/Logo";
import NavbarLabel from "@/app/(auth)/_components/NavbarLabel/NavbarLabel";
import NavLink from "@/app/(auth)/_components/NavLink/NavLink";
import styles from "./Navbar.module.scss";

export default function Navbar() {
  return (
    <nav className={styles["navbar"]}>
      <div className={styles["navbar-content"]}>
        <div className={styles["navbar-brand"]}>
          <Logo />
          <NavbarLabel />
        </div>
        <NavLink />
      </div>
    </nav>
  );
}
