import React, { Component } from 'react';
import Header from './components/common/Header';
import Alerts from './components/common/Alerts';
import Login from './components/user/login/Login';
import SignUp from './components/user/signup/SignUp';
import Profile from './components/user/profile/Profile';
import PostsList from './components/posts/PostsList';
import PostsEditModal from './components/posts/PostEditModal';
import PostsDetailModal from './components/posts/PostsDetailModal';
import OAuth2RedirectHandler from './components/user/oauth/OAuth2RedirectHandler';
import { BrowserRouter as Router, Route, Switch } from 'react-router-dom';
import { Container } from 'react-bootstrap';
import { getCurrentUser } from './actions/user';
import {  dispatchCreatePostsSuccess, dispatchCreatePostsFail, 
  dispatchEditPostsSuccess, dispatchEditPostsFail,
  dispatchDeletePostsSuccess } from './actions/posts';
import { dispatchPostsIncreaseLikeSuccess } from './actions/postsLike';
import { API_BASE_URL } from './constants';
import SockJsClient from 'react-stomp';
import Cookies from "js-cookie";
import { connect } from 'react-redux';
import './App.css';
import 'bootstrap/dist/css/bootstrap.min.css';

class App extends Component {

  constructor() {
    super();
    this.clientRef = React.createRef()
  }

  componentDidMount() {
    this.props.getCurrentUser();
  }
  
  render() {
    const token = Cookies.get("token") ? Cookies.get("token") : null;
    const customHeaders = {
        "Authorization": token
    };
    return (
    <Router forceRefresh={true} >
      <div>
        <SockJsClient 
          ref={(el) => this.clientRef = el}
          url= {API_BASE_URL + "/dayco-websocket"}
          topics = {["/topic/posts", "/topic/posts/like"]}
          headers= {customHeaders}        
          subscribeHeaders={customHeaders}      
          //message 보냈을 때의 callback
          onMessage={(msg) => {
            console.log(msg)
            if(this.props.socketActionStatus === 'postsCreate') {
                this.props.dispatchCreatePostsSuccess(msg);
            } else if(this.props.socketActionStatus === 'postsEdit') {
                this.props.dispatchEditPostsSuccess(msg);
            } else if(this.props.socketActionStatus === 'postsDelete') {
                this.props.dispatchDeletePostsSuccess(msg.id, msg.author);
            } else if(this.props.socketActionStatus == 'postsLikeIncrease') {
              this.props.dispatchPostsIncreaseLikeSuccess(msg.id, msg.likeCount)
            }
          }}
          onConnectFailure={(error)=> console.log("Connect Fail : " + error)}
          onConnect={() => console.log("Connected to websocket")}
          onDisconnect={() => console.log("Disconnected from websocket")}
        /> 
        <Header />
        <Alerts />
        <PostsEditModal clientRef={this.clientRef} />
        <PostsDetailModal clientRef={this.clientRef} />
        <Container>
          <Switch>
            <Route path="/login" component={Login} />
            <Route path="/signup" component={SignUp}/>
            <Route path="/profile" render={() => <Profile email={(this.props.user.currentUser) ? this.props.user.currentUser.email : ''}/> } />
            <Route path="/home" component={() => <PostsList clientRef={this.clientRef} />} />
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
    user: state.user,
    socketActionStatus: state.socket.actionStatus,
	};
}
export default connect(mapStateToProps, {getCurrentUser,
  dispatchCreatePostsSuccess, dispatchEditPostsSuccess, dispatchDeletePostsSuccess,
  dispatchPostsIncreaseLikeSuccess
})(App);