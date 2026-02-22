import { ReactElement } from "react";
import { render, screen } from "@testing-library/react";
import PrevButton from "./PrevButton";
import { SellerSignupContext } from "@/contexts/SellerSignupContext";
import userEvent from "@testing-library/user-event";

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

describe("PrevButton", () => {
  const user = userEvent.setup();

  it("should not render when step is 3", () => {
    renderWithProvider(<PrevButton />, { step: 3 });
    const button = screen.queryByRole("button");
    expect(button).toBeNull();
  });

  it("should call prevStep when button is clicked", async () => {
    const mockPrevStep = jest.fn();
    renderWithProvider(<PrevButton />, { step: 1, prevStep: mockPrevStep });
    const button = screen.getByRole("button");
    await user.click(button);
    expect(mockPrevStep).toHaveBeenCalledTimes(1);
  });
});
