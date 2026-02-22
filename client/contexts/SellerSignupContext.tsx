"use client";

import { createContext, useContext, useState } from "react";

interface Props {
  children: React.ReactNode;
}

type SellerSignupValue = {
  step: number;
  phone: string;
  nextStep: () => void;
  prevStep: () => void;
  setPhone: (phone: string) => void;
};

const SellerSignupContext = createContext<SellerSignupValue | null>(null);

const SellerSignupProvider = ({ children }: Props) => {
  const [step, setStep] = useState(0);
  const [phone, setPhone] = useState("");

  const nextStep = () => {
    setStep((currStep) => Math.min(currStep + 1, 3));
  };

  const prevStep = () => {
    setStep((currStep) => Math.max(currStep - 1, 0));
  };

  return (
    <SellerSignupContext.Provider
      value={{ step, phone, nextStep, prevStep, setPhone }}>
      {children}
    </SellerSignupContext.Provider>
  );
};

const useSellerContext = () => {
  const context = useContext(SellerSignupContext);
  if (!context) {
    throw new Error(
      "useSellerContext must be used within SellerSignupProvider"
    );
  }
  return context;
};

export { SellerSignupProvider, SellerSignupContext, useSellerContext };
