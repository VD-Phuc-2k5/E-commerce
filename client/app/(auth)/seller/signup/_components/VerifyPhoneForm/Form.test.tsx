import { ReactElement } from "react";
import { render, screen, waitFor, act } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import VerifyPhoneForm from "@/app/(auth)/seller/signup/_components/VerifyPhoneForm/Form";
import { SellerSignupContext } from "@/contexts/SellerSignupContext";

const user = userEvent.setup({ advanceTimers: jest.advanceTimersByTime });
const mockContext = {
  step: 1,
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

describe("Verify Phone Form", () => {
  it("Shows validation error when submitting empty form.", async () => {
    renderWithProvider(<VerifyPhoneForm />);
    expect(screen.getByText("(+84) 123 456 789")).toBeInTheDocument();

    const submitBtn = screen.getByRole("button", {
      name: /kế tiếp/i
    });
    expect(submitBtn).toBeInTheDocument();

    await user.click(submitBtn);
    expect(
      screen.getByText(/mã otp không được để trống./i)
    ).toBeInTheDocument();
  });

  it("Shows validation error when submitting invalid otp.", async () => {
    renderWithProvider(<VerifyPhoneForm />);
    expect(screen.getByText("(+84) 123 456 789")).toBeInTheDocument();

    const submitBtn = screen.getByRole("button", {
      name: /kế tiếp/i
    });
    const otpTextbox = screen.getByTestId("otp-textbox");

    expect(submitBtn).toBeInTheDocument();
    expect(otpTextbox).toBeInTheDocument();

    await user.type(otpTextbox, "abc123");
    await user.click(submitBtn);
    expect(screen.getByText(/mã otp không hợp lệ./i)).toBeInTheDocument();
  });

  it("Submits successfully with valid otp.", async () => {
    renderWithProvider(<VerifyPhoneForm />);
    expect(screen.getByText("(+84) 123 456 789")).toBeInTheDocument();

    const submitBtn = screen.getByRole("button", {
      name: /kế tiếp/i
    });
    const otpTextbox = screen.getByTestId("otp-textbox");

    expect(submitBtn).toBeInTheDocument();
    expect(otpTextbox).toBeInTheDocument();

    await user.type(otpTextbox, "123456");
    await user.click(submitBtn);
    await waitFor(() => {
      expect(mockContext.nextStep).toHaveBeenCalledTimes(1);
    });
  });
});

describe("Countdown", () => {
  beforeEach(() => {
    jest.useFakeTimers();
  });

  afterEach(() => {
    jest.runOnlyPendingTimers();
    jest.useRealTimers();
  });

  it("shows initial time.", () => {
    renderWithProvider(<VerifyPhoneForm />);
    expect(screen.getByTestId("countdown")).toHaveTextContent("05 : 00");
  });

  it("decreases correctly after 9 seconds", () => {
    renderWithProvider(<VerifyPhoneForm />);
    act(() => {
      jest.advanceTimersByTime(9000);
    });
    expect(screen.getByTestId("countdown")).toHaveTextContent("04 : 51");
  });

  it("clears interval when reaching zero", () => {
    const clearSpy = jest.spyOn(globalThis, "clearInterval");
    renderWithProvider(<VerifyPhoneForm />);

    act(() => {
      jest.advanceTimersByTime(300000);
    });

    expect(screen.queryByTestId("countdown")).not.toBeInTheDocument();
    expect(clearSpy).toHaveBeenCalled();
  });
});
