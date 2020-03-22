import React from 'react';
import Header from './components/common/Header';
import Login from './components/user/login/Login';
import SignUp from './components/user/signup/SignUp';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import { Container } from 'react-bootstrap';
import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';

function App() {
  return (
    <Router>
      <div>
        <Header />
        <Container>
          <Switch>
            <Route path="/login" component={Login}/>
            <Route path="/signup" component={SignUp}/>
          </Switch>
        </Container>
      </div>
    </Router>
  );
}

export default App;
