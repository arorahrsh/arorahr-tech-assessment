import { useState } from 'react'
import westpacLogo from './assets/westpac.svg'
import './App.css'
import Button from 'react-bootstrap/Button'
import Container from 'react-bootstrap/Container'
import Form from 'react-bootstrap/Form'
import Row from 'react-bootstrap/Row'
import PasswordField from './components/PasswordField'

function App() {
  const [isValid, setIsValid] = useState(false)
  const [password, setPassword] = useState("")

  const getFormData = (password: string, isValid: boolean) => {
    setPassword(password)
    setIsValid(isValid)
  }

  return (
    <div className="App">
      <Container>
        <Row className="justify-content-md-center">
          <img src={westpacLogo} className="logo" alt="Westpac" />
          <h3 className="text-center">Reset Password</h3>
        </Row>
        <Row className="row-md-auto">
          <Form noValidate validated={isValid}>
            <PasswordField onSubmit={getFormData} />
            <Button type="submit">Save password</Button>
          </Form>
        </Row>
      </Container>
    </div>
  )
}

export default App
