import React, { Component } from 'react';
import { ListGroup, ListGroupItem, Image, InputGroup, FormControl, Button, Card } from 'react-bootstrap';
import { connect } from 'react-redux';
import { withRouter } from "react-router";
import { createPostsComment } from '../../actions/postsComment';
import PostsComment from './PostsComment';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome';
import { faSmileWink } from '@fortawesome/free-solid-svg-icons';

class PostsComments extends Component {
    
    constructor(){
        super(...arguments);
        this.state = {
            requestCreateComment: ''
        }
        this.requestCreateCommentChange = this.requestCreateCommentChange.bind(this);
    }
    
    requestCreateCommentChange(event) {
		this.setState({requestCreateComment: event.target.value});
    }

    // 댓글을 등록시킨다.
    createComment = () => {
        this.props.createPostsComment(
            this.props.postsId, 
            this.state.requestCreateComment)
        //초기화 시킨다.
        this.setState({requestCreateComment: ''});
    }

    render(){
        let postsCommentsHtml = this.props.postsComments.map((comment) => {
            return (<ListGroup.Item
                        id={comment.commentId}
                        key={comment.commentId}>  
                        <PostsComment id={comment.commentId} 
                            postsId={comment.postsId}
                            key={comment.commentId} 
                            author={comment.author.userId}
                            comment={comment.comment}
                            picture={comment.author.picture}
                            createTime={comment.createTime}
                            modifiedTime={comment.modifiedTime}
                            />
                    </ListGroup.Item>);
        });

        return (
            <div>
                <input type="hidden" value={this.props.postsCommentStatus}/>
                <ListGroup>
                    {postsCommentsHtml}
                </ListGroup>
                <ListGroup horizontal="sm" className="my-2">
                {   
                (this.props.user.authenticated === true
                && this.props.user.logon === true
                && this.props.user.currentUser != null) ? 
                    <InputGroup className="mb-3">
                        <InputGroup.Prepend>
                            <InputGroup.Text>
                                {(this.props.user.currentUser.picture) ?
                                (<Image src={this.props.user.currentUser.picture}
                                    width={26} height={26} roundedCircle />):
                                (<FontAwesomeIcon icon={faSmileWink} size="lg" color="Violet"/>)
                                }
                            </InputGroup.Text>
                        </InputGroup.Prepend>
                        <FormControl
                            value={this.state.requestCreateComment}
                            onChange={this.requestCreateCommentChange}
                            placeholder="댓글을 입력하세요."
                            aria-label="comment"
                        />
                        <InputGroup.Append>
                            <Button variant="dark" onClick={this.createComment}>등록</Button>
                        </InputGroup.Append>
                    </InputGroup>
                :''
                }
                </ListGroup>
                
            </div>
        );
        
    }
}

const mapStateToProps = (state, ownProps) => {
	return {
        user: state.user,
        //부모 props 배열로 전달되면 rendering 이 안되므로, 상태를 바로 갱신 시켜주기 위해 status 사용
        postsCommentStatus: state.postsComment.status, 
	};
}

export default withRouter(connect(mapStateToProps, {createPostsComment})(PostsComments));
