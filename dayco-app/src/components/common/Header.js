import React, {Component} from 'react';
import {Navbar, Image } from 'react-bootstrap';
import {connect} from 'react-redux';
import { withRouter } from "react-router";
import { logout } from '../../actions/user';
import { showPostsCreateModal } from '../../actions/posts';
import login from '../img/login.png';
import signup from '../img/signup.png';
import photo from '../img/photo.png';
import home from '../img/home.png';
import { LinkContainer } from "react-router-bootstrap";

class Header extends Component {

    onLogout = () => {
		this.props.logout();
    }
    
    handleClick = () => {
        this.props.showPostsCreateModal();
    };
    
    render() {
        return (
            <Navbar bg="primary" variant="dark" expand="lg">
                <Navbar.Brand href="#home">Dayco</Navbar.Brand>
                <Navbar.Toggle aria-controls="basic-navbar-nav" />
                {   
                (this.props.user.authenticated === true
                && this.props.user.logon === true
                && this.props.user.currentUser != null) ? 
                <Navbar.Collapse className="justify-content-end">
                    <LinkContainer to="/home">
                        <Navbar.Brand>
                            <Image src={home} alt="home" width={30} height={30}/>
                        </Navbar.Brand>
                    </LinkContainer>
                    <Navbar.Brand href="#" onClick={this.handleClick}>
                        <Image src={photo} alt="postsedit" width={30} height={30}/>
                    </Navbar.Brand>
                    <Navbar.Text>
                    {this.props.user.currentUser.picture ? (
                                        <Image src={this.props.user.currentUser.picture} alt={this.props.user.currentUser.name}
                                        width={26} height={26}
                                        roundedCircle/>
                                    ) : (
                                        <div className="text-avatar">
                                            <span>{this.props.user.currentUser.name}</span>
                                        </div>
                                    )}  
                    &nbsp;<a href="#" onClick={this.onLogout}>{this.props.user.currentUser.userId}</a>
                    </Navbar.Text>
                </Navbar.Collapse> :
                <Navbar.Collapse className="justify-content-end">
                    <Navbar>
                        <LinkContainer to="/home">
                            <Navbar.Brand>
                                <Image src={home} alt="home" width={30} height={30}/>
                            </Navbar.Brand>
                        </LinkContainer>
                        <LinkContainer to="/login">
                            <Navbar.Brand>
                                <Image src={login} alt="login" roundedCircle width={30} height={30}/>
                            </Navbar.Brand>
                        </LinkContainer>
                        <LinkContainer to="/signup">
                            <Navbar.Brand>
                                <Image src={signup} alt="signup" rounded width={25} height={20}/>
                            </Navbar.Brand>
                        </LinkContainer>
                    </Navbar>
                </Navbar.Collapse>
                }
            </Navbar>
        );
    }
}

const mapStateToProps = (state, ownProps) => {
	return {
        user: state.user
	};
}

export default withRouter(connect(mapStateToProps, {logout, showPostsCreateModal})(Header));