import { ReactElement } from "react";
import { render, screen, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import SignUpForm from "@/app/(auth)/seller/signup/_components/SignupForm/Form";
import { SellerSignupContext } from "@/contexts/SellerSignupContext";

const user = userEvent.setup();
const mockContext = {
  step: 0,
  phone: "",
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

describe("Seller SignUp Form", () => {
  it("Shows validation error when submitting empty form.", async () => {
    renderWithProvider(<SignUpForm />);
    const submitBtn = screen.getByRole("button", {
      name: /tiếp theo/i
    });

    expect(submitBtn).toBeInTheDocument();

    await user.click(submitBtn);
    expect(
      screen.getByText(/số điện thoại không được để trống./i)
    ).toBeInTheDocument();
  });

  it("Shows validation error when submitting invalid phone number.", async () => {
    renderWithProvider(<SignUpForm />);
    const submitBtn = screen.getByRole("button", {
      name: /tiếp theo/i
    });
    const phoneTextbox = screen.getByPlaceholderText(/số điện thoại/i);
    expect(submitBtn).toBeInTheDocument();
    expect(phoneTextbox).toBeInTheDocument();

    await user.type(phoneTextbox, "abc1234567");
    await user.click(submitBtn);
    expect(
      screen.getByText(/số điện thoại không hợp lệ./i)
    ).toBeInTheDocument();
  });

  it("Submits successfully with valid phone number.", async () => {
    renderWithProvider(<SignUpForm />);
    const submitBtn = screen.getByRole("button", {
      name: /tiếp theo/i
    });
    const phoneTextbox = screen.getByPlaceholderText(/số điện thoại/i);

    expect(submitBtn).toBeInTheDocument();
    expect(phoneTextbox).toBeInTheDocument();

    await user.type(phoneTextbox, "0123456789");
    const icon = await screen.findByTestId("success-icon");
    expect(icon).toBeInTheDocument();

    await user.click(submitBtn);
    await waitFor(() => {
      expect(mockContext.setPhone).toHaveBeenCalledWith("+84123456789");
      expect(mockContext.nextStep).toHaveBeenCalledTimes(1);
    });
  });
});
