"use client";

import { useEffect, useState, useCallback, useRef } from "react";
import { useRouter } from "next/navigation";
import SuccessIcon from "@/app/(auth)/_components/SuccessIcon/SuccessIcon";
import styles from "./SuccessCard.module.scss";

const REDIRECT_SECONDS = 5;

export default function SuccessCard() {
  const router = useRouter();
  const [countdown, setCountdown] = useState(REDIRECT_SECONDS);
  const intervalRef = useRef<NodeJS.Timeout | null>(null);

  const navigateToShopee = useCallback(() => {
    if (intervalRef.current) {
      clearInterval(intervalRef.current);
    }
    router.push("/");
  }, [router]);

  useEffect(() => {
    intervalRef.current = setInterval(() => {
      setCountdown((prev) => {
        if (prev <= 1) {
          clearInterval(intervalRef.current!);
          router.push("/");
          return 0;
        }
        return prev - 1;
      });
    }, 1000);

    return () => {
      if (intervalRef.current) {
        clearInterval(intervalRef.current);
      }
    };
  }, [router]);

  return (
    <div className={styles["success-card-body"]}>
      <SuccessIcon size='3.5rem' />
      <p>Bạn đã tạo thành công tài khoản Shopee</p>
      <p data-testid='countdown'>
        Bạn sẽ được chuyển hướng đến Shopee trong {countdown}s
      </p>
      <div>
        <button data-testid='navigate-btn' onClick={navigateToShopee}>
          Quay lại Shopee
        </button>
      </div>
    </div>
  );
}
