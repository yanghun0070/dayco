import React, { Component } from 'react';
import { Form, Button,  Modal } from 'react-bootstrap';
import { connect } from 'react-redux';
import { withRouter } from "react-router";
import { createPosts, editPosts, deletePosts, hidePostsEditModal,
     dispatchCreatePostsSuccess, dispatchCreatePostsFail,
     dispatchEditPostsSuccess, dispatchEditPostsFail,
     dispatchDeletePostsSuccess } from '../../actions/posts';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faSmileBeam, faSms } from '@fortawesome/free-solid-svg-icons'
import { API_BASE_URL } from '../../constants';
import SockJsClient from 'react-stomp';
import Cookies from "js-cookie";

class PostsEditModal extends Component {

    constructor(){
        super();
        this.state = {
            requestId: -1,
            requestTitle: '',
            requestContent: ''
        }
        this.clientRef = React.createRef()
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

    createPosts = () => {
        if(this.props.isSocket === true) {
            let jsonStr = JSON.stringify({
                type: "create", 
                title: this.state.requestTitle, 
                content: this.state.requestContent
            })
            this.sendMessage("/app/dayco-websocket", jsonStr);   
        } else {
            this.props.createPosts(this.state.requestTitle, this.state.requestContent);
        }
    }

    editPosts = () => {
        if(this.props.isSocket === true) {
            this.props.editPosts(this.props.id, this.state.requestTitle, this.state.requestContent, this.props.author)
        } else {
            let jsonStr = JSON.stringify({
                type: "edit", 
                postsId: this.props.id,
                title: this.state.requestTitle, 
                content: this.state.requestContent
            })
            this.sendMessage("/app/dayco-websocket", jsonStr);        
        }
    }

    deletePosts = () => {
        if(this.props.isSocket === true) {
            this.props.deletePosts(this.props.id, this.props.author);
        } else {
            let jsonStr = JSON.stringify({
                type: "delete", 
                postsId: this.props.id
            })
            this.sendMessage("/app/dayco-websocket", jsonStr);   
        }
    }

    sendMessage = (topic, jsonStr) => {
        const token = Cookies.get("token") ? Cookies.get("token") : null;
        const customHeaders = {
            "Authorization": token
        };
        this.clientRef.sendMessage(topic,
            jsonStr,
            customHeaders)
    }

    render(){
        const token = Cookies.get("token") ? Cookies.get("token") : null;
        const customHeaders = {
            "Authorization": token
        };
        return(
            <div>
                <SockJsClient 
                    ref={(el) => this.clientRef = el}
                    url= {API_BASE_URL + "/dayco-websocket"}
                    topics = {["/topic/posts"]}
                    headers= {customHeaders}        
                    subscribeHeaders={customHeaders}      
                    //message 보냈을 때의 callback
                    onMessage={(msg) => {
                        console.log(msg)
                        if(this.props.status === 'create') {
                            this.props.dispatchCreatePostsSuccess(msg);
                        } else if(this.props.status === 'edit') {
                            this.props.dispatchEditPostsSuccess(msg);
                        } else {
                            this.props.dispatchDeletePostsSuccess(msg.id, msg.author);
                        }
                    }}
                    onConnectFailure={(error)=> console.log("Connect Fail : " + error)}
                    onConnect={() => console.log("Connected to websocket")}
                    onDisconnect={() => console.log("Disconnected from websocket")}
                /> 
                <Modal show={this.props.isShowPostsEditModal}>
                    <Form>
                        <Form.Group>
                            <Modal.Header>
                            <Modal.Title>
                            <Form.Label>Posts  </Form.Label>
                            <Form.Text>
                            {
                            (this.props.status === 'delete') ?
                            (this.props.title + ' 삭제처리 하시겠습니까?') : ('')
                            }
                            </Form.Text>
                            </Modal.Title>
                            </Modal.Header>
                            {
                            (this.props.status !== 'delete') ?
                            <Modal.Body>
                                <Form.Control type="text" name="requestTitle" id="requestTitle"
                                    onChange={this.requestTitleChange}
                                    placeholder={(this.props.status === 'edit') ? (this.props.title) : ("제목을 입력하세요.")} />
                                <Form.Label>내용</Form.Label>
                                <Form.Control as="textarea" rows="3" name="requestContent" id="requestContent"
                                    onChange={this.requestContentChange} 
                                    placeholder={(this.props.status === 'edit') ? (this.props.content) : ("")}/>
                            </Modal.Body>
                            : ('')
                            }
                            <Modal.Footer>
                            <Button variant="secondary" onClick={() => this.hideModal()}>
                                Close
                            </Button>
                            {
                            (this.props.status === 'create') ? 
                            (<Button variant="primary" onClick={this.createPosts}>
                                등록 <FontAwesomeIcon icon={faSms} />
                            </Button>)
                            :('')
                            }
                            {
                            (this.props.status === 'edit') ?
                            (<Button variant="warning" onClick={this.editPosts}>
                                수정 <FontAwesomeIcon icon={faSmileBeam} />
                            </Button>)
                            : ('')
                            }
                            {
                            (this.props.status === 'delete') ?
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
        isSocket: state.isSocket
	};
}

export default withRouter(connect(mapStateToProps, {createPosts, editPosts, deletePosts,
    hidePostsEditModal,
    dispatchCreatePostsSuccess, dispatchEditPostsSuccess, dispatchDeletePostsSuccess})(PostsEditModal));
