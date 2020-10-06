import React from 'react'
import './styles.css'
import { FiLogIn } from 'react-icons/fi'
import { Link } from 'react-router-dom'

const Home = () => {
  return (
    <div id="page-home">
      <div className="content">
        <header>
          <h1>HUMAN RESOURCE MANAGEMENT</h1>
          <p>Seu gerenciador de recursos humanos.</p>
        </header>
        <main>
          <Link to="/create-pessoa">
            <span> <FiLogIn /> </span>
            <strong>Cadastro de Pessoas</strong>
          </Link>
        </main>

      </div>
    </div>
  )
}

export default Home