import axios from 'axios'

const api = axios.create({
  baseURL: process.env.NODE_ENV === 'development' ? 'http://localhost:8080/api' : 'https://api-hrm-server.herokuapp.com/api' ,
  auth: {
    username: 'admin',
    password: 'admin'
  },
  headers: {
    'Content-Type': 'application/json'           
  },
})

export default api
