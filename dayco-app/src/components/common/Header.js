import React, {Component} from 'react';
import {Navbar, Nav} from 'react-bootstrap';

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
                </Navbar>
        );
    }
}

export default Header;
