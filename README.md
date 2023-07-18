# Technical Assessment (Hrshikesh Arora)

This project implements a React single page application for a password reset functionality. It allows the user to enter their password, validates it against the requirements and then allows the user to save their new password if the validation passes and resets the page. Some screenshots of the application are shown below.

| Default application view | Invalid password (less than 8 chars) |
| --- | --- |
| ![default](./screenshots/01-Default.PNG) | ![invalid-1](./screenshots/02-Invalid.PNG) |

| Invalid password (missing special characters) | Valid password |
| --- | --- |
| ![invalid-2](./screenshots/03-Invalid.PNG) | ![valid](./screenshots/04-Valid.PNG) |

## Running the application

1. Clone this source repository into your chosen directory either by downloading this project as a zip or using the command below.

```
git clone https://github.com/arorahrsh/arorahr-tech-assessment.git
```

2. Change the current directory to be the cloned repository and install the project dependencies using `npm` (Note: if NodeJs aka npm is not installed, make sure this exists first).

```
cd arorahr-tech-assessment
npm install
```

3. Once the project dependencies are installed, run the application using the command below which will launch the app on a URL like `http://localhost:5173/` (please refer to the command line output for the correct URL).

```
npm run dev
```

### Running the units

4. Follow steps 1-2 above and then run the following command. Refer to the command line output for results.

```
npm run test
```

## Design considerations

- The application consists of two main components. The `App` component represents a single page application for the password reset functionality and allows submitting the password. However, the actual entering of the input and password validation is encapsulated in the `PasswordField` component. This segregation allows for a better separation of concern and clear responsibilities of each of these components. Furthermore, developing the password input as a standalone component promotes reuse of this component in the future. For example, if this were a larger application, the password field component could easily be reused in multiple forms e.g. Login, Sign up and Reset Password.
- React Bootstrap was used to style this application to prevent having to reinvent the wheel by adding boilerplate CSS code to this application.
- While the `PasswordField` component stores the password and validation logic, this is continuously passed back to the `App` parent component through the use of a callback prop. This allows the parent to enable the "Save password" button when the entered password is confirmed as being valid. Designing the button this way gives the user visual feedback when the form is ready to be saved. In addition, this button logic could easily be extended in future to implement saving of this new password to a database.

## Assumptions

- While most real-world applications require a new password to be entered twice to confirm password matching, I have not included this functionality in this POC since this feature was not explicitly mentioned in the assessment requirements.