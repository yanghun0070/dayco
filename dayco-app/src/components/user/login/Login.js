import React, { Component } from 'react';
import {Col, Row, Form, Button, Card} from 'react-bootstrap';
import Alert from 'react-bootstrap/Alert';
import { login } from '../../../actions/user';
import {connect} from 'react-redux';
import { withRouter } from "react-router";

class Login extends Component {

	constructor(){
		super(...arguments);
		this.state ={
			requestId:'',
			requestPw:''
		};
		this.requestIdChange = this.requestIdChange.bind(this);
		this.requestPwChange = this.requestPwChange.bind(this);
    }

	requestIdChange(event){
		this.setState({requestId: event.target.value});
	}
	requestPwChange(event){
		this.setState({requestPw: event.target.value});
	}

	onLogin = () => {
		this.props.login(this.state.requestId, this.state.requestPw);
	}

	render(){
		return (
			<div>	
				<Row>
					<Col>&nbsp;</Col>
				</Row>
				<Row>
					<Col xs={3}></Col>
					<Col xs={6}>
						<Card>
							<Card.Body>
								<Form>
									<Form.Group as={Row}>
										<Form.Label column sm="4">ID</Form.Label>
										<Col sm="8">
										<Form.Control type="text" name="requestId" id="requestId" placeholder="ID 를 입력하세요."
											value={this.state.requestId} onChange={this.requestIdChange}/>
										</Col>
									</Form.Group>
									<Form.Group as={Row}>
										<Form.Label column sm="4">Password</Form.Label>
										<Col sm="8">
										<Form.Control type="password" name="password" id="requestPw" placeholder="password 를 입력하세요."
											value={this.state.requestPw} onChange={this.requestPwChange} />
										</Col>
									</Form.Group>
									<Form.Group as={Row}>
										<Col sm="2"></Col>
										<Col sm="8">
											<Button className="loginBtn" size="sm" block onClick={this.onLogin}>Log In</Button>
										</Col>
									</Form.Group>
								</Form>
								{(this.props.user.authenticated == true
								&& this.props.user.logon == true) ?
									<Alert color="success">
									로그인 되었습니다.
									</Alert>: ""
								}
								{(this.props.user.authenticated == true
								&& this.props.user.logon == false) ?
								<Alert  variant="danger">
								ID/PW 확인하세요.
								</Alert>
									: ""
								}
							</Card.Body>
						</Card>
					</Col>
					<Col xs="4"></Col>
				</Row>
			</div>
		);
	}
}

const mapStateToProps = (state, ownProps) => {
	return {
		user: state.user
	};
}

export default withRouter(connect(mapStateToProps, {login})(Login));