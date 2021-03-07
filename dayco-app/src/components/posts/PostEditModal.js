import React, { Component } from 'react';
import { Form, Button,  Modal, Card } from 'react-bootstrap';
import { connect } from 'react-redux';
import { withRouter } from "react-router";
import { createPosts, editPosts, deletePosts, 
    hidePostsEditModal } from '../../actions/posts';
import question from '../img/the-question-mark.png';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faSmileBeam, faSms } from '@fortawesome/free-solid-svg-icons'
import { POSTS_CREATED, POSTS_DELETED, POSTS_EDITED,
    POSTS_CREATE_MODAL, POSTS_EDIT_MODAL, POSTS_DELETE_MODAL } from "../../constants";
import Cookies from "js-cookie";

class PostsEditModal extends Component {

    constructor(){
        super();
        this.state = {
            requestId: -1,
            requestTitle: '',
            requestContent: '',
            requestFileBase64: '',
            requestFileName: ''
        }
        this.imageUploader = React.createRef()
        this.uploadedImage = React.createRef()

        this.requestTitleChange = this.requestTitleChange.bind(this);
        this.requestContentChange = this.requestContentChange.bind(this);
    }
    
    hideModal() {
        this.props.hidePostsEditModal();
    }

    requestTitleChange(event){
		this.setState({requestTitle: event.target.value});
    }
    
	requestContentChange(event){
		this.setState({requestContent: event.target.value});
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

    createPosts = () => {
        if(this.props.isSocket === true) {
            this.sendMessage("/app/posts",
                JSON.stringify({
                    status: POSTS_CREATED, 
                    title: this.state.requestTitle, 
                    content: this.state.requestContent,                
                    fileBase64 :this.state.requestFileBase64,
                    fileName : this.state.requestFileName
                }));
            this.props.hidePostsEditModal();
        } else {
            this.props.createPosts(this.state.requestTitle, 
                this.state.requestContent,
                this.state.requestFileBase64,
                this.state.requestFileName
                );
        }
    }

    editPosts = () => {
        if(this.props.isSocket === true) {
            this.sendMessage("/app/posts", JSON.stringify({
                status: POSTS_EDITED, 
                id: this.props.id, 
                title: this.state.requestTitle, 
                content: this.state.requestContent,
                fileBase64 :this.state.requestFileBase64,
                fileName : this.state.requestFileName
            }));
            this.props.hidePostsEditModal(); 
        } else {
            this.props.editPosts(this.props.id, 
                this.state.requestTitle, 
                this.state.requestContent, 
                this.props.author,
                this.state.requestFileBase64,
                this.state.requestFileName
            )
        }
    }

    deletePosts = () => {
        if(this.props.isSocket === true) {
            this.sendMessage("/app/posts", JSON.stringify({
                status: POSTS_DELETED, 
                id: this.props.id
            }));
            this.props.hidePostsEditModal();
        } else {
            this.props.deletePosts(this.props.id, this.props.author);
        }
    }

    sendMessage = (topic, jsonStr) => {
        const token = Cookies.get("token") ? Cookies.get("token") : null;
        const customHeaders = {
            "Authorization": token
        };
        this.props.clientRef.sendMessage(topic,
            jsonStr,
            customHeaders)
    }

    render(){
        return(
            <div>
                <Modal show={this.props.isShowPostsEditModal}>
                    <Form>
                        <Form.Group>
                            <Modal.Header>
                            <Modal.Title>
                            <Form.Label>Posts  </Form.Label>
                            <Form.Text>
                            {
                            (this.props.status === POSTS_DELETE_MODAL) ?
                            (this.props.title + ' 삭제처리 하시겠습니까?') : ('')
                            }
                            </Form.Text>
                            </Modal.Title>
                            </Modal.Header>
                            {
                            (this.props.status !== POSTS_DELETE_MODAL) ?
                            <Modal.Body>
                                <Form.Group  controlId="formImage">
                                <Form.Control
                                            type="file"
                                            accept="image/*"
                                            onChange={this.handleImageUpload}
                                            ref={this.imageUploader}
                                            style={{display: "none"}}/>
                                        <div
                                            style={{
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
                                <Form.Control type="text" 
                                    name="requestTitle" id="requestTitle"
                                    onChange={this.requestTitleChange}
                                    placeholder={
                                    (this.props.status === POSTS_EDIT_MODAL) ? 
                                        (this.props.title) : ("제목을 입력하세요.")
                                     } />
                                <Form.Label>내용</Form.Label>
                                <Form.Control as="textarea" rows="3" 
                                    name="requestContent" id="requestContent"
                                    onChange={this.requestContentChange} 
                                    placeholder={
                                    (this.props.status === POSTS_EDIT_MODAL) ? 
                                        (this.props.content) : ("")
                                    }/>
                            </Modal.Body>
                            : ('')
                            }
                            <Modal.Footer>
                            <Button variant="secondary" onClick={() => this.hideModal()}>
                                Close
                            </Button>
                            {
                            (this.props.status === POSTS_CREATE_MODAL) ? 
                            (<Button variant="primary" onClick={this.createPosts}>
                                등록 <FontAwesomeIcon icon={faSms} />
                            </Button>)
                            :('')
                            }
                            {
                            (this.props.status === POSTS_EDIT_MODAL) ?
                            (<Button variant="warning" onClick={this.editPosts}>
                                수정 <FontAwesomeIcon icon={faSmileBeam} />
                            </Button>)
                            : ('')
                            }
                            {
                            (this.props.status === POSTS_DELETE_MODAL) ?
                            (<Button variant="outline-dark" onClick={this.deletePosts}>
                                삭제 <FontAwesomeIcon icon={faSmileBeam} />
                            </Button>)
                             : ('')
                            }
                            </Modal.Footer>
                        </Form.Group>
                    </Form>
                </Modal>
            </div>
        )
    }
}

const mapStateToProps = (state) => {
	return {
        posts: state.posts,
        isShowPostsEditModal: state.postsEditModal.isShow,
        status: state.postsEditModal.status,
        id: state.postsEditModal.id,
        title : state.postsEditModal.title,
        content: state.postsEditModal.content,
        author: state.postsEditModal.author,
        isSocket: state.socket.isSocket
	};
}

export default withRouter(connect(mapStateToProps, {createPosts, editPosts, deletePosts,
    hidePostsEditModal})(PostsEditModal));
