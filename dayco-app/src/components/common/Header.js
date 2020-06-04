import React, {Component} from 'react';
import {Navbar, Nav, Image} from 'react-bootstrap';
import {connect} from 'react-redux';
import { withRouter } from "react-router";
import { logout } from '../../actions/user';
import login from '../img/login.png';
import signup from '../img/signup.png';

class Header extends Component {

    onLogout = () => {
		this.props.logout();
    }
    
    render() {
        return (
            <Navbar bg="primary" variant="dark" expand="lg">
                <Navbar.Brand href="#home">Dayco</Navbar.Brand>
                <Navbar.Toggle aria-controls="basic-navbar-nav" />
                {
                (this.props.user.authenticated == true
                && this.props.user.logon == true
                && this.props.user.currentUser != null) ? 
                <Navbar.Collapse className="justify-content-end">
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
                &nbsp;<a onClick={this.onLogout}>{this.props.user.currentUser.userId}</a>
                </Navbar.Text>
                </Navbar.Collapse> :
                <Navbar.Collapse className="justify-content-end">
                    <Navbar>
                        <Navbar.Brand href="/login">
                            <Image src={login} alt="login" roundedCircle width={30} height={30}/>
                        </Navbar.Brand>
                        <Navbar.Brand href="/signup">
                            <Image src={signup} alt="signup" rounded width={25} height={20}/>
                        </Navbar.Brand>
                    </Navbar>
                </Navbar.Collapse>
                }
            </Navbar>
        );
    }
}

const mapStateToProps = (state, ownProps) => {
    console.log("mapStateToProps")
    console.log(state)
	return {
		user: state.user
	};
}
export default withRouter(connect(mapStateToProps, {logout})(Header));