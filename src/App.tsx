import { useState } from 'react'
import westpacLogo from './assets/westpac.svg'
import './App.css'
import PasswordField from './components/PasswordField'

function App() {
  const [count, setCount] = useState(0)

  return (
    <div className="App">
      <div>
          <img src={westpacLogo} className="logo" alt="Westpac logo" />
          <h3>Reset Password</h3>
          <PasswordField />
      </div>
      <div className="card">
        <button onClick={() => setCount((count) => count + 1)}>
          count is {count}
        </button>
        <p>
          Edit <code>src/App.tsx</code> and save to test HMR
        </p>
      </div>
      <p className="read-the-docs">
        Click on the Vite and React logos to learn more
      </p>
    </div>
  )
}

export default App
