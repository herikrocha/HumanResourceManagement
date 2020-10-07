import React, { useState, ChangeEvent, FormEvent } from 'react'
import './styles.css'
import { Link, useHistory } from 'react-router-dom'
import { FiArrowLeft } from 'react-icons/fi'
import api from '../../services/api'
 
const CreatePessoa = () => {

  const [formData, setFormData] = useState({
    nome: '',
    email: '',
    cpf: '',
    dataNascimento: '',
    sexo: '0',
    nacionalidade: '',
    naturalidade: '',
  })
  const history = useHistory()

  function handleInputChange(event: ChangeEvent<HTMLInputElement>) {
    const { name, value } = event.target

    setFormData({ ...formData, [name]: value })
  }
  
  async function handleSubmit(event: FormEvent) {
    event.preventDefault() 

    const { nome, email, cpf, dataNascimento, sexo, nacionalidade, naturalidade } = formData
    const data = {
      nome: nome,
      email: email,
      cpf: cpf,
      dataNascimento: dataNascimento,
      sexo: sexo,
      nacionalidade: nacionalidade,
      naturalidade: naturalidade
    }
    
    const json = JSON.stringify(data)
    console.log(json)

    await api.post('pessoas', json).then(res => {
      console.log(res)
      alert('Pessoa cadastrada com sucesso!')
      history.push('/')
    }).catch(err => {
      if(err.response) {
        console.log(err)
        alert(err.response.data)
      }
    })
  }

  return (
    <div id="page-create-pessoa">
      <header>
        <Link to="/">
          <FiArrowLeft />
          Voltar para home
        </Link>
      </header>

      <form onSubmit={handleSubmit}>
        <h1>Cadastro de Pessoas </h1>

        <fieldset>
          <legend>
            <h2>Dados</h2>
          </legend>
          
          <div className="field">
            <label htmlFor="name">Nome</label>
            <input 
              type="text"
              name="nome"
              id="nome"
              onChange={handleInputChange}
            />
          </div>
          <div className="field">
            <label htmlFor="email">E-mail</label>
            <input 
              type="text"
              name="email"
              id="email"
              onChange={handleInputChange}
            />
          </div>
          <div className="field-group">
            <div className="field">
              <label htmlFor="name">CPF</label>
              <input 
                type="text"
                name="cpf"
                id="cpf"
                onChange={handleInputChange}
              />
            </div>  
            <div className="field">
              <label htmlFor="dataNascimento">Data de Nascimento</label>
              <input 
                type="Date"
                name="dataNascimento"
                id="dataNascimento"
                onChange={handleInputChange}
              />

            </div>
          </div>
          <div className="field-group">
            <div className="field">
              <label htmlFor="sexo">Sexo</label>
              <select 
                name="sexo" 
                id="sexo" 
                >
                <option value="0"></option>
                <option value="F">Feminino</option>
                <option value="M">Masculino</option>
              </select>
            </div>
            <div className="field">
              <label htmlFor="nacionalidade">Nacionalidade</label>
              <input 
                type="text"
                name="nacionalidade"
                id="nacionalidade"
                onChange={handleInputChange}
              />
            </div>
            <div className="field">
              <label htmlFor="naturalidade">Naturalidade</label>
              <input 
                type="text"
                name="naturalidade"
                id="naturalidade"
                onChange={handleInputChange}
              />
            </div>

          </div>
        </fieldset>
        
        <button type="submit">Cadastrar Pessoa</button>

      </form>
    </div>
  )
}

export default CreatePessoa