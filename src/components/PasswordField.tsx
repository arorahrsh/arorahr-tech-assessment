import React, { useState } from "react";

/* Given an input password string, this function validates the following conditions:
    - either the passowrd is more than 15 characters without any special restrictions, or
    - the password is between 8-15 chars with atleast 2 special characters.

   This function returns a string with an error message if the given password does not
   conform to these requirements. On the other hand if the password it valid, an empty
   string is returned.
*/
export function validatePassword (password: string): string {
  // regex pattern to check for atleast 2 special characters and 1 digit
  const regex = /^(?=.*[!@#$%^&*()\-_=+'/.\\,].*[!@#$%^&*()\-_=+'/.\\,])(?=.*\d).*$/
  if (password.length < 8) {
    return "Password must be atleast 8 characters"
  }
  else if (password.length > 15) {
    return ""
  } else if (!password.match(regex)) {
    return "Password must contain 2 special characters and one number"
  }
  return ""
}

function PasswordField() {
  const [passwordValue, setPasswordValue] = useState("")
  const [errorMessage, setErrorMessage] = useState("")

  const onInputChange = (e: React.ChangeEvent<HTMLInputElement>) => {
    const newPassword = e.currentTarget.value
    setPasswordValue(newPassword)
    const errorMessage = validatePassword(newPassword)
    setErrorMessage(errorMessage)
    console.log(newPassword)
  }

  return (
    <div>
      <label>
        Password
        <input type="password" name="password" onChange={onInputChange} />
      </label>
      <label className="errors">{errorMessage}</label>
    </div>
  );
}

export default PasswordField