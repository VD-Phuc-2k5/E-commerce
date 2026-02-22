import { ReactElement } from "react";
import { render, screen } from "@testing-library/react";
import SubmitButton from "@/app/(auth)/seller/signup/_components/SubmitButton/SubmitButton";
import { SellerSignupContext } from "@/contexts/SellerSignupContext";

const mockContext = {
  step: 3,
  phone: "+84123456789",
  nextStep: jest.fn(),
  prevStep: jest.fn(),
  setPhone: jest.fn()
};

const renderWithProvider = (ui: ReactElement) => {
  return render(
    <SellerSignupContext.Provider value={mockContext}>
      {ui}
    </SellerSignupContext.Provider>
  );
};

test("should render submit button with provided label.", () => {
  renderWithProvider(<SubmitButton label='Đăng ký' />);
  const label = screen.getByText(/đăng ký/i);
  expect(label).toBeInTheDocument();
});
