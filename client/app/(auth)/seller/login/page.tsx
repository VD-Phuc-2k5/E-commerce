import Hero from "@/app/(auth)/seller/login/_components/Hero/Hero";
import Form from "@/app/(auth)/seller/login/_components/Form/Form";
import styles from "@/app/(auth)/seller/main.module.scss";

export default function SignupPage() {
  return (
    <section>
      <div className={styles["seller-login"]}>
        <Hero />
        <Form />
      </div>
    </section>
  );
}
