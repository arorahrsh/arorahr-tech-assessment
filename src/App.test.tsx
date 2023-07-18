import { render, screen, fireEvent } from '@testing-library/react'
import '@testing-library/jest-dom'
import App from './App'

it("display contains the reset password text", () => {
  const { getByText } = render(<App />)
  const heading = getByText(/Reset password/i)
  expect(heading).toBeVisible()
})

it("App contains the PasswordField component", () => {
  const { getByTestId } = render(<App />);
  const parent = getByTestId('parent');
  const child = getByTestId('child');
  expect(parent).toContainElement(child);
})

it("save password button is disabled by default", () => {
  const { getByText } = render(<App />)
  const button = getByText(/Save password/i)
  expect(button).toHaveAttribute('disabled')
})

it("save password button is enabled when the stored password is valid", () => {
  const { getByText } = render(<App />)
  const input = screen.getByLabelText("Password")
  fireEvent.change(input, { target: { value: 'testtest@123!' } })
  const button = getByText(/Save password/i)
  expect(button).not.toHaveAttribute('disabled')
})
