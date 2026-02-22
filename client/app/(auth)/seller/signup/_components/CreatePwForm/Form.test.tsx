import { ReactElement } from "react";
import { render, screen, waitFor } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import CreatePwForm from "@/app/(auth)/seller/signup/_components/CreatePwForm/Form";
import { SellerSignupContext } from "@/contexts/SellerSignupContext";

const user = userEvent.setup();
const mockContext = {
  step: 2,
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

describe("Create Password Form", () => {
  it("Shows validation error when submitting empty form.", async () => {
    renderWithProvider(<CreatePwForm />);
    const submitBtn = screen.getByRole("button", { name: /đăng ký/i });
    expect(submitBtn).toBeInTheDocument();

    await user.click(submitBtn);
    expect(
      screen.getByText(/mật khẩu không được để trống./i)
    ).toBeInTheDocument();
  });

  it("Shows validation error when submitting invalid password.", async () => {
    renderWithProvider(<CreatePwForm />);
    const submitBtn = screen.getByRole("button", { name: /đăng ký/i });
    const passwordTextbox = screen.getByPlaceholderText(/mật khẩu/i);

    expect(submitBtn).toBeInTheDocument();
    expect(passwordTextbox).toBeInTheDocument();

    await user.type(passwordTextbox, "User@");
    await user.click(submitBtn);

    expect(screen.getByText(/mật khẩu không hợp lệ./i)).toBeInTheDocument();
  });

  it("Submits successfully with valid password.", async () => {
    renderWithProvider(<CreatePwForm />);
    const submitBtn = screen.getByRole("button", { name: /đăng ký/i });
    const passwordTextbox = screen.getByPlaceholderText(/mật khẩu/i);

    expect(submitBtn).toBeInTheDocument();
    expect(passwordTextbox).toBeInTheDocument();

    await user.type(passwordTextbox, "User@1234");
    await user.click(submitBtn);

    await waitFor(() => {
      expect(mockContext.nextStep).toHaveBeenCalledTimes(1);
    });
  });

  it("toggles password visibility when clicking button", async () => {
    renderWithProvider(<CreatePwForm />);
    const toggleBtn = screen.getByRole("button", { name: /toggle password/i });
    const passwordTextbox = screen.getByPlaceholderText(/mật khẩu/i);

    expect(toggleBtn).toBeInTheDocument();
    expect(passwordTextbox).toHaveAttribute("type", "password");
    expect(screen.queryByTestId("eye-slash-icon")).toBeInTheDocument();

    await user.click(toggleBtn);
    expect(passwordTextbox).toHaveAttribute("type", "text");
    expect(screen.queryByTestId("eye-icon")).toBeInTheDocument();

    await user.click(toggleBtn);
    expect(passwordTextbox).toHaveAttribute("type", "password");
    expect(screen.queryByTestId("eye-slash-icon")).toBeInTheDocument();
  });
});
