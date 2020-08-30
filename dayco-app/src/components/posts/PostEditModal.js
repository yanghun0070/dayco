import React, { Component } from 'react';
import { Form, Button,  Modal } from 'react-bootstrap';
import { connect } from 'react-redux';
import { withRouter } from "react-router";
import { createPosts, editPosts, hidePostsEditModal } from '../../actions/posts';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faSmileBeam, faSms } from '@fortawesome/free-solid-svg-icons'

class PostsEditModal extends Component {

    constructor(){
        super();
        this.state = {
            requestId: -1,
            requestTitle: '',
            requestContent: ''
        }
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
        this.props.createPosts(this.state.requestTitle, this.state.requestContent);
    }

    editPosts = () => {
        this.props.editPosts(this.props.id, this.state.requestTitle, this.state.requestContent, this.props.author)
    }

    render(){
        return(
            <div>
                <Modal show={this.props.isShowPostsEditModal}>
                    <Form>
                        <Form.Group>
                            <Modal.Header>
                            <Modal.Title>
                            <Form.Label>Posts </Form.Label>
                            </Modal.Title>
                            </Modal.Header>
                            <Modal.Body>
                                <Form.Control type="text" name="requestTitle" id="requestTitle"
                                    onChange={this.requestTitleChange}
                                    placeholder={(this.props.status === 'edit') ? (this.props.title) : ("제목을 입력하세요.")} />
                                <Form.Label>내용</Form.Label>
                                <Form.Control as="textarea" rows="3" name="requestContent" id="requestContent"
                                    onChange={this.requestContentChange} 
                                    placeholder={(this.props.status === 'edit') ? (this.props.content) : ("")}/>
                            </Modal.Body>
                            <Modal.Footer>
                            <Button variant="secondary" onClick={() => this.hideModal()}>
                                Close
                            </Button>
                            {
                            (this.props.status === 'create') ? 
                            (<Button variant="primary" onClick={this.createPosts}>
                                등록 <FontAwesomeIcon icon={faSms} />
                            </Button>)
                            :
                            (<Button variant="outline-dark" onClick={this.editPosts}>
                                수정 <FontAwesomeIcon icon={faSmileBeam} />
                            </Button>)
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
        author: state.postsEditModal.author
	};
}

export default withRouter(connect(mapStateToProps, {createPosts, editPosts, hidePostsEditModal})(PostsEditModal));
