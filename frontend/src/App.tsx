import { Link, Outlet } from '@tanstack/react-router'

import './App.css'

function App() {
  return (
      <>
        <nav
            className={"site-nav"}
            aria-label="Main navigation"
        >
          <Link
              to="/"
              activeOptions={{ exact: true }}
              activeProps={{ className: "active" }}
          >
            Dashboard
          </Link>

          <Link
              to="/challenges"
              activeProps={{ className: "active" }}
          >
            Challenges
          </Link>
        </nav>

        <Outlet />
      </>
  )
}

export default App