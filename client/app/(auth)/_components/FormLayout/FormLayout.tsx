import { ReactNode } from "react";
import StepIndicator from "@/app/(auth)/_components/StepIndicator/StepIndicator";
import PrevButton from "@/app/(auth)/_components/PrevButton/PrevButton";
import FormHeading from "@/app/(auth)/_components/FormLayout/FormHeading";
import styles from "./FormLayout.module.scss";

type Props = {
  children: ReactNode;
  title: string;
};

export default function FormLayout({ children, title }: Props) {
  return (
    <section>
      <div className={styles["verify-container"]}>
        <div className={styles["verify-content"]}>
          <StepIndicator />
          <div className={styles["verify-card"]}>
            {/* Heading */}
            <div className={styles["verify-card-heading"]}>
              <div className={styles["verify-card-heading-wrap"]}>
                <PrevButton />
                <FormHeading title={title} />
              </div>
            </div>
            {/* Form */}
            {children}
          </div>
        </div>
      </div>
    </section>
  );
}
