import React, { Component } from 'react';
import { join } from '../../../actions/user';
import { Col, Row, Form, Button, Card } from 'react-bootstrap';
import Alert from 'react-bootstrap/Alert';
import { connect } from 'react-redux';
import { withRouter } from "react-router";

class SignUp extends Component {

	constructor(){
		super(...arguments);
		this.state = {
			requestId:'',
			requestPw:'',
			requestPwConfirm: '',
			requestEmail: ''
		};
	}

	requestIdChange = id => {
		this.setState({requestId : id});
	}
	requestPwChange = pw => {
		this.setState({requestPw : pw});
	}
	requestEmailChange = email => {
		this.setState({requestEmail: email})
	}
	requestPwConfirmChange = pwConfirm => {
		this.setState({requestPwConfirm : pwConfirm});
	}

	onSignup = () => {
		this.props.join(this.state.requestId, this.state.requestEmail, this.state.requestPw, this.state.requestPwConfirm);
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
											onChange={e => this.requestIdChange(e.target.value)} value={this.state.requestId}/>
									</Col>
								</Form.Group>
								<Form.Group as={Row}>
									<Form.Label column sm="4">Email</Form.Label>
									<Col sm="8">
										<Form.Control type="text" name="requestEmail" id="requestEmail" placeholder="Email 를 입력하세요."
											onChange={e => this.requestEmailChange(e.target.value)} value={this.state.requestEmail}/>
									</Col>
								</Form.Group>
								<Form.Group as={Row}>
									<Form.Label column sm="4">Password</Form.Label>
									<Col sm="8">
										<Form.Control type="password" name="password" id="requestPw" placeholder="password 를 입력하세요."
											onChange={e => this.requestPwChange(e.target.value)} value={this.state.requestPw}/>
									</Col>
								</Form.Group>
								<Form.Group as={Row}>
									<Form.Label column sm="4">Password 확인</Form.Label>
									<Col sm="8">
										<Form.Control type="password" name="password" id="requestPwConfirm" placeholder="password 를 입력하세요."
											onChange={e => this.requestPwConfirmChange(e.target.value)} value={this.state.requestPwConfirm}/>
									</Col>
								</Form.Group>
								<Form.Group as={Row}>
									<Col sm="2"></Col>
									<Col sm="8">
										<Button className="joinBtn" size="sm" block onClick={this.onSignup}>Sign Up</Button>
									</Col>
								</Form.Group>
							</Form>
							{(this.props.user.authenticated == true && 
								this.props.user.joined == true) ?
								<Alert variant="success">
									회원가입 되었습니다.
								</Alert>
								: ""
							}
							{(this.props.user.authenticated == true && 
							this.props.user.joined == false) ?
								<Alert variant="success">
									로그인 되었습니다.
								</Alert>
							: ""
							}
							{(this.props.user.authenticated == false && 
							this.props.user.joined == false) ?
								<Alert variant="danger">
									ID / PW 확인하세요.
								</Alert>
							: ""
							}
						</Card.Body>
					</Card>
					</Col>
					<Col xs={3}></Col>
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

export default withRouter(connect(mapStateToProps, {join})(SignUp));
