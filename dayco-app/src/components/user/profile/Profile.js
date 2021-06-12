import React, { Component } from 'react';
import '../profile/Profile.css';
import {connect} from 'react-redux';
import { withRouter } from "react-router";
import question from '../../img/the-question-mark.png';
import { Form, Col, Row, Button, Card } from 'react-bootstrap';
import { changeProfile } from '../../../actions/profile';

class Profile extends Component {
    
    constructor() {
        super(...arguments);
        this.state = {
            requestFileBase64: '',
            requestFileName: ''
		};
        this.imageUploader = React.createRef()
        this.uploadedImage = React.createRef()
    }

    handleImageUpload = e => {
        const [file] = e.target.files;
        if (file) {
            const originalFileName = file.name
            const reader = new FileReader();
            const { current } = this.uploadedImage;
            current.file = file;
            reader.onload = e => {
                current.src = e.target.result;
                const dataIndex = current.src.indexOf(',') + 1
                const fileBase64 = current.src.substring(
                            dataIndex,
                            current.src.length)
                this.setState({
                    requestFileBase64: fileBase64,
                    requestFileName: originalFileName
                })
            };
            reader.readAsDataURL(file);
        }
    };
    
    //Profile 변경한다.
    changeProfile = () => {
        this.props.changeProfile(
            this.state.requestFileBase64,
            this.state.requestFileName
        )
    }

    render(){
		return (
            <div>
                <Row>
                    <Col xs={3}></Col>
                    <Col xs={6}>
                        <Card>
                            <Card.Body>
                                <Form>
                                    <Form.Group  controlId="formImage">
                                       <Form.Control
                                            type="file"
                                            accept="image/*"
                                            onChange={this.handleImageUpload}
                                            ref={this.imageUploader}
                                            style={{display: "none"}}/>
                                        <div
                                            style={{
                                            height: "500px",
                                            width: "500px",
                                            border: "1px dashed black"}}
                                            onClick={() => this.imageUploader.current.click()}>
                                            <Card.Img
                                                variant="top" 
                                                ref={this.uploadedImage}
                                                style={{
                                                    width: "100%",
                                                    height: "100%",
                                                    position: "acsolute"
                                                }}
                                                src={question}/>
                                        </div>
                                        이미지를 Upload 하세요.
                                    </Form.Group>
                                    
                                </Form>
                                <Button variant="primary" onClick={this.changeProfile}>Change</Button>
                            </Card.Body>
                        </Card>  
                    </Col>
                    <Col xs={3}></Col>
                </Row>
            </div>
        )
    };
}

const mapStateToProps = (state) => {
	return {
        user: state.user
	};
}

export default withRouter(connect(mapStateToProps, {changeProfile})(Profile));