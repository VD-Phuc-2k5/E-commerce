import styles from "./SuccessIcon.module.scss";

export default function SuccessIcon({ size = "1rem" }) {
  return (
    <div className={styles["icon-wrap"]}>
      <svg
        fill='none'
        viewBox='0 0 16 16'
        style={{ height: size, width: size }}>
        <path
          fill='none'
          stroke='#6C0'
          d='M8 15A7 7 0 108 1a7 7 0 000 14z'
          clipRule='evenodd'></path>
        <path
          stroke='none'
          fill='#6C0'
          fillRule='evenodd'
          d='M11.621 6.406l-3.98 4.059c-.266.266-.719.244-1.012-.049-.293-.293-.314-.746-.048-1.012l3.98-4.059c.266-.266.719-.245 1.012.048.293.293.314.747.048 1.013z'
          clipRule='evenodd'></path>
        <path
          stroke='none'
          fill='#6C0'
          fillRule='evenodd'
          d='M3.803 7.997l2.81 2.532c.267.267.72.245 1.013-.048.293-.293.315-.746.048-1.012l-2.81-2.532c-.267-.267-.72-.245-1.013.048-.293.293-.314.746-.048 1.012z'
          clipRule='evenodd'></path>
      </svg>
    </div>
  );
}
