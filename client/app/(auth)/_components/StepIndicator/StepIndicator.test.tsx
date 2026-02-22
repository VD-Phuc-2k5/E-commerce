import { ReactElement } from "react";
import { render, screen } from "@testing-library/react";
import StepIndicator from "./StepIndicator";
import { SellerSignupContext } from "@/contexts/SellerSignupContext";

type MockContextType = {
  step: number;
  phone: string;
  nextStep: jest.Mock;
  prevStep: jest.Mock;
  setPhone: jest.Mock;
};

const createMockContext = (
  override?: Partial<MockContextType>
): MockContextType => ({
  step: 1,
  phone: "",
  nextStep: jest.fn(),
  prevStep: jest.fn(),
  setPhone: jest.fn(),
  ...override
});

export const renderWithProvider = (
  ui: ReactElement,
  override?: Partial<MockContextType>
) => {
  const mockContext = createMockContext(override);

  return render(
    <SellerSignupContext.Provider value={mockContext}>
      {ui}
    </SellerSignupContext.Provider>
  );
};

describe("Step Indicator", () => {
  it("activates step 1 when step is 1", () => {
    renderWithProvider(<StepIndicator />, { step: 1 });
    expect(
      screen.getByText(/xác minh số điện thoại/i).parentElement
    ).toHaveClass("active");
  });

  it("activates step 1 and 2 when step is 2", () => {
    renderWithProvider(<StepIndicator />, { step: 2 });
    expect(
      screen.getByText(/xác minh số điện thoại/i).parentElement
    ).toHaveClass("active");

    expect(screen.getByText(/tạo mật khẩu/i).parentElement).toHaveClass(
      "active"
    );
  });

  it("activates all steps when step is 3", () => {
    renderWithProvider(<StepIndicator />, { step: 3 });

    expect(
      screen.getByText(/xác minh số điện thoại/i).parentElement
    ).toHaveClass("active");

    expect(screen.getByText(/tạo mật khẩu/i).parentElement).toHaveClass(
      "active"
    );

    expect(screen.getByText(/hoàn thành/i).parentElement).toHaveClass("active");
  });
});
