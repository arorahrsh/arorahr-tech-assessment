import { render, screen, fireEvent } from '@testing-library/react'
import '@testing-library/jest-dom'
import PasswordField from './PasswordField'

it("password is invalid if under 8 characters", () => {
  const { getByTestId } = render(<PasswordField onSubmit={() => {}} />)
  const input = screen.getByLabelText("Password")
  fireEvent.change(input, { target: { value: 'test' } })
  const errorMessage = getByTestId('error')
  expect(errorMessage).toHaveTextContent('Password must be atleast 8 characters')
})

it("password is invalid if between 8-15 characters but missing 2 special characters", () => {
  const { getByTestId } = render(<PasswordField onSubmit={() => {}} />)
  const input = screen.getByLabelText("Password")
  fireEvent.change(input, { target: { value: 'testtest2' } })
  const errorMessage = getByTestId('error')
  expect(errorMessage).toHaveTextContent('Password must contain 2 special characters and one number')
})

it("password is invalid if between 8-15 characters but missing atleast 1 number", () => {
  const { getByTestId } = render(<PasswordField onSubmit={() => {}} />)
  const input = screen.getByLabelText("Password")
  fireEvent.change(input, { target: { value: 'testtest!!' } })
  const errorMessage = getByTestId('error')
  expect(errorMessage).toHaveTextContent('Password must contain 2 special characters and one number')
})

it("password is valid if greater than 15 characters", () => {
  const { getByTestId } = render(<PasswordField onSubmit={() => {}} />)
  const input = screen.getByLabelText("Password")
  fireEvent.change(input, { target: { value: 'testtesttesttest' } })
  const errorMessage = getByTestId('error')
  expect(errorMessage).toHaveTextContent('')
})
