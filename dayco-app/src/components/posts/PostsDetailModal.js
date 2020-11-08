import React, { Component } from 'react';
import { Form, Modal, Row, Col, Button} from 'react-bootstrap';
import { faAngleRight, faAngleLeft } from '@fortawesome/free-solid-svg-icons';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { connect } from 'react-redux';
import { withRouter } from "react-router";
import Posts from '../posts/Posts';
import PostsComments from '../posts/PostsComments';
import question from '../img/the-question-mark.png';
import { dispatchHidePostsDetailModal } from '../../actions/posts';
import { getPageOfComments } from '../../actions/postsComment';

class PostsDetailModal extends Component {

    constructor() {
        super(...arguments);
    };

    getNextPageOfComments = () => {
        this.props.getPageOfComments(
            this.props.posts.id, 
            this.props.posts.pageOfComments.number + 1, 
            this.props.posts.pageOfComments.size
            );
    }

    getPrevPageOfComments = () => {
        if(this.props.posts.pageOfComments.number == 0) {
            alert("현재 페이지가 처음페이지 입니다.")
            return true;
        }
        this.props.getPageOfComments(
            this.props.posts.id, 
            this.props.posts.pageOfComments.number - 1, 
            this.props.posts.pageOfComments.size
            );
    }

    render(){
        console.log(this.props.posts)
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
                                            likeCount={(this.props.posts.postsLike) ? 
                                                this.props.posts.postsLike.likeCount : 0 }
                                            modifiedDate={this.props.posts.modifiedDate}
                                            clientRef={this.props.clientRef}/>
                                    </Form.Group>
                                </Col>
                                <Col>
                                    <Form.Group>
                                        <PostsComments 
                                            postsId={this.props.posts.id} 
                                            postsComments={(this.props.posts.postsComments) ? 
                                            this.props.posts.postsComments: []}/>

                                        <Row>
                                            <Col>
                                                <Button 
                                                    style={{float: "right"}}
                                                    variant="warning" 
                                                    size="sm"  
                                                    onClick={() => {this.getPrevPageOfComments()}}>
                                                    <FontAwesomeIcon icon={faAngleLeft}  />  Prev
                                                </Button>
                                            </Col>
                                            <Col>
                                            <Button 
                                                    style={{float: "left"}}
                                                    variant="warning" 
                                                    size="sm"  
                                                    onClick={() => {this.getNextPageOfComments()}}>
                                                    Next <FontAwesomeIcon icon={faAngleRight}  /> 
                                                </Button>
                                            </Col>
                                        </Row>
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
        posts : state.posts.detail
	};
}

export default withRouter(connect(mapStateToProps, {dispatchHidePostsDetailModal,
    getPageOfComments})(PostsDetailModal));
 