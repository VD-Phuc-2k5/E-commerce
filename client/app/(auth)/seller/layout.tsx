import type { Metadata } from "next";
import Navbar from "@/app/(auth)/_components/Navbar/Navbar";
import Footer from "@/app/(auth)/_components/Footer/Footer";

export const metadata: Metadata = {
  title: "Login to Your Account | EShoping Store",
  description:
    "Sign in to your EShoping account to manage orders, track purchases, and enjoy a secure and personalized shopping experience.",
  keywords: [
    "EShoping login",
    "user login",
    "sign in account",
    "e-commerce login",
    "secure login"
  ]
};

export default function AuthSellerLayout({
  children
}: Readonly<{
  children: React.ReactNode;
}>) {
  return (
    <div>
      <Navbar />
      {children}
      <Footer />
    </div>
  );
}
