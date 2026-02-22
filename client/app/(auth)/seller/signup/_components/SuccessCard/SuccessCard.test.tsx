const pushMock = jest.fn();
jest.mock("next/navigation", () => ({
  useRouter: () => ({
    push: pushMock
  })
}));

import { ReactElement } from "react";
import { render, screen, act } from "@testing-library/react";
import userEvent from "@testing-library/user-event";
import SuccessCard from "@/app/(auth)/seller/signup/_components/SuccessCard/SuccessCard";
import { SellerSignupContext } from "@/contexts/SellerSignupContext";

const user = userEvent.setup({ advanceTimers: jest.advanceTimersByTime });
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

describe("Countdown", () => {
  beforeEach(() => {
    jest.useFakeTimers();
  });

  afterEach(() => {
    jest.runOnlyPendingTimers();
    jest.useRealTimers();
  });

  it("shows initial time.", () => {
    renderWithProvider(<SuccessCard />);
    expect(screen.getByTestId("countdown")).toHaveTextContent(/5s/i);
  });

  it("decreases correctly after 2 seconds", () => {
    renderWithProvider(<SuccessCard />);
    act(() => {
      jest.advanceTimersByTime(2000);
    });
    expect(screen.getByTestId("countdown")).toHaveTextContent(/3s/i);
  });

  it("clears interval when reaching zero", () => {
    const clearSpy = jest.spyOn(globalThis, "clearInterval");
    renderWithProvider(<SuccessCard />);
    act(() => {
      jest.advanceTimersByTime(5000);
    });
    expect(pushMock).toHaveBeenCalledTimes(1);
    expect(clearSpy).toHaveBeenCalled();
  });
});

describe("Navigate button", () => {
  it("clears interval when navigate button is clicked.", async () => {
    const clearSpy = jest.spyOn(globalThis, "clearInterval");
    renderWithProvider(<SuccessCard />);
    expect(screen.getByTestId("countdown")).toHaveTextContent(/5s/i);

    const navigateBtn = screen.getByTestId("navigate-btn");
    expect(navigateBtn).toBeInTheDocument();

    await user.click(navigateBtn);
    expect(pushMock).toHaveBeenCalledTimes(1);
    expect(clearSpy).toHaveBeenCalled();
  });
});
