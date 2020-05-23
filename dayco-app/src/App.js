import React, { Component } from 'react';
import Header from './components/common/Header';
import Login from './components/user/login/Login';
import SignUp from './components/user/signup/SignUp';
import OAuth2RedirectHandler from './components/user/oauth/OAuth2RedirectHandler';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import { Container } from 'react-bootstrap';
import { getCurrentUser } from './actions/user';
import { connect } from 'react-redux';
import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';

class App extends Component {

  constructor(props) {
    super(props);
    this.state = {
      authenticated: false,
      currentUser: null
    }
  }

  componentDidMount() {
      this.props.getCurrentUser();
  }

  render() {
    return (
    <Router>
      <div>
        <Header />
        <Container>
          <Switch>
            <Route path="/login" component={Login}/>
            <Route path="/signup" component={SignUp}/>
            <Route path="/oauth2/redirect" component={OAuth2RedirectHandler}/>
          </Switch>
        </Container>
      </div>
    </Router>
    );
  }
}



export default connect(null, {getCurrentUser})(App);