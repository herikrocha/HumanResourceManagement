import Home from './pages/Home'
import CreatePessoa from './pages/CreatePessoa'
import React from 'react';
import { Route, BrowserRouter } from 'react-router-dom';


const Routes = () => {
  return (
     <BrowserRouter>
      <Route component={Home} path="/" exact />  
      <Route component={CreatePessoa} path="/create-pessoa" />  
    </BrowserRouter>
  );
}

export default Routes;