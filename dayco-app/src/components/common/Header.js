import React, {Component} from 'react';
import {Navbar, Nav, Image} from 'react-bootstrap';
import {connect} from 'react-redux';
import { withRouter } from "react-router";

class Header extends Component {
    render() {
        return (
            <Navbar bg="primary" variant="dark" expand="lg">
                <Navbar.Brand href="#home">Dayco</Navbar.Brand>
                <Navbar.Toggle aria-controls="basic-navbar-nav" />
                <Navbar.Collapse id="basic-navbar-nav">
                    <Nav className="mr-auto">
                    <Nav.Link href="/signup">회원가입</Nav.Link>
                    <Nav.Link href="/login">로그인</Nav.Link>
                    </Nav>
                </Navbar.Collapse>
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
                &nbsp;<a href="#logout">{this.props.user.currentUser.userId}</a>
                </Navbar.Text>
                </Navbar.Collapse> : ""
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
export default withRouter(connect(mapStateToProps)(Header));