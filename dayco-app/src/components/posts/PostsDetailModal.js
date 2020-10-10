import React, { Component } from 'react';
import { Form, Modal, Row, Col, Button} from 'react-bootstrap';
import { faAngleDown } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { connect } from 'react-redux';
import { withRouter } from "react-router";
import Posts from '../posts/Posts';
import PostsComments from '../posts/PostsComments';
import question from '../img/the-question-mark.png';
import { dispatchHidePostsDetailModal } from '../../actions/posts';
import { getNextPageOfComments } from '../../actions/postsComment';

class PostsDetailModal extends Component {

    constructor() {
        super(...arguments);
    };

    getNextPageOfComments = () => {
        this.props.getNextPageOfComments(
            this.props.posts.id, this.props.postsCommentPage, this.props.postsCommentRowNum);
    }

    render(){
        return (
            <div>
                <Modal size="xl" show={this.props.isShow}
                    onHide={() => this.props.dispatchHidePostsDetailModal()}>
                    <Modal.Header closeButton>
                        <Modal.Title>
                            <Form.Label>Posts Detail </Form.Label>
                        </Modal.Title>
                    </Modal.Header>
                    <Modal.Body>
                        <Form>
                            <Row>
                                <Col xs={6}>
                                    <Form.Group>
                                        <Posts 
                                            id={this.props.posts.id}
                                            author={this.props.posts.author}
                                            title={this.props.posts.title}
                                            content={this.props.posts.content}
                                            thumbnail={question}
                                            likeCount={this.props.postsLike.likeCount}
                                            modifiedDate={this.props.posts.modifiedDate}
                                            clientRef={this.props.clientRef}/>
                                    </Form.Group>
                                </Col>
                                <Col>
                                    <Form.Group>
                                        <PostsComments 
                                            postsId={this.props.posts.id} 
                                            postsComments={this.props.postsComments}/>
                                        <Button variant="warning" 
                                            size="lg"  
                                            onClick={() => {this.getNextPageOfComments()}}
                                            block>
                                            <FontAwesomeIcon icon={faAngleDown}  /> 더보기
                                        </Button>
                                    </Form.Group>
                                </Col>
                            </Row>
                        </Form>
                    </Modal.Body>
                </Modal>
            </div>
        )
    };
}

const mapStateToProps = (state) => {
	return {
        user : state.user,
        isShow : state.postsDetailModal.isShow,
        posts : state.posts.detail,
        postsComments : state.postsComment.detail.comments,
        postsCommentPage : state.postsComment.detail.page,
        postsCommentRowNum : state.postsComment.detail.rowNum,
        postsLike : state.postsEtc.detail
	};
}

export default withRouter(connect(mapStateToProps, {dispatchHidePostsDetailModal,
    getNextPageOfComments})(PostsDetailModal));
 