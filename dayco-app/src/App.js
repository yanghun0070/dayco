import React, { Component } from 'react';
import Header from './components/common/Header';
import Login from './components/user/login/Login';
import SignUp from './components/user/signup/SignUp';
import PostsList from './components/posts/PostsList';
import PostsEdit from './components/posts/PostsEdit';
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
  }

  componentDidMount() {
    this.props.getCurrentUser();
  }

  render() {
    return (
    <Router forceRefresh={true} >
      <div>
        <Header />
        <Container>
          <Switch>
            <Route path="/login" component={Login} />
            <Route path="/signup" component={SignUp}/>
            <Route path="/home" component={PostsList} />
            <Route path="/posts/edit" component={PostsEdit} />
            <Route path="/oauth2/redirect" component={OAuth2RedirectHandler}/>
          </Switch>
        </Container>
      </div>
    </Router>
    );
  }
}

const mapStateToProps = (state) => {
	return {
		user: state.user
	};
}


export default connect(mapStateToProps, {getCurrentUser})(App);