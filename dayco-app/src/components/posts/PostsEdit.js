import React, { Component } from 'react';
import { Col, Row, Form, Button, Card, ListGroup, ListGroupItem, Modal } from 'react-bootstrap';
import { connect } from 'react-redux';
import { withRouter } from "react-router";
import PostsList from './PostsList';
import { createPosts, editPosts } from '../../actions/posts';

class PostsEdit extends Component {

    constructor(){
        super();
        this.state = {
            showHide : true,
            requestId: -1,
            requestTitle: '',
            requestContent: '',
            isCreatePosts : false
        }
        this.requestTitleChange = this.requestTitleChange.bind(this);
		this.requestContentChange = this.requestContentChange.bind(this);
    }

    handleModalShowHide() {
        this.setState({ showHide: false })
    }

    requestTitleChange(event){
		this.setState({requestTitle: event.target.value});
	}
	requestContentChange(event){
		this.setState({requestContent: event.target.value});
    }
    
    createPosts = () => {
		this.props.createPosts(this.state.requestTitle, this.state.requestContent)
		.then(() => {
            this.setState({ showHide: false })
            this.setState({ isCreatePosts : true })
		});
    }

    editPosts = () => {
        this.props.editPosts(this.state.requestId, this.state.requestTitle, this.state.requestContent)
    }

    render(){
        return(
            <div>
                <Modal show={this.state.showHide}>
                    <Form>
                        <Form.Group>
                            <Modal.Header closeButton onClick={() => this.handleModalShowHide()}>
                            <Modal.Title>
                            <Form.Label>Posts </Form.Label>
                            </Modal.Title>
                            </Modal.Header>
                            <Modal.Body>
                                <Form.Control type="text" name="requestTitle" id="requestTitle"
                                    value={this.state.requestTitle} onChange={this.requestTitleChange}
                                    placeholder="제목을 입력하세요." />
                                <Form.Label>내용</Form.Label>
                                <Form.Control as="textarea" rows="3" name="requestContent" id="requestContent"
                                    value={this.state.requestContent} onChange={this.requestContentChange} />
                            </Modal.Body>
                            <Modal.Footer>
                            <Button variant="secondary" onClick={() => this.handleModalShowHide()}>
                                Close
                            </Button>
                            <Button variant="primary" onClick={this.createPosts}>
                                등록
                            </Button>
                            </Modal.Footer>
                        </Form.Group>
                    </Form>
                </Modal>
            
                <PostsList posts={this.props.posts} isCreate={this.state.isCreatePosts} />
            </div>
        )
    }
}

const mapStateToProps = (state) => {
	return {
		posts: state.posts
	};
}

export default withRouter(connect(mapStateToProps, {createPosts, editPosts})(PostsEdit));
