import React, { Component } from 'react';
import { ListGroup, ListGroupItem, Image, InputGroup, FormControl, Button, Card, Badge } from 'react-bootstrap';
import { connect } from 'react-redux';
import { withRouter } from "react-router";
import { createPostsComment, editPostsComment, deletePostsComment } from '../../actions/postsComment';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faSmileWink, faReply, faRemoveFormat, faStop } from '@fortawesome/free-solid-svg-icons'

class PostsComment extends Component {
    
    constructor(){
        super(...arguments);
        this.state = {
            requestEditComment: '',
            isEditShow: false
		};
        this.requestEditCommentChange = this.requestEditCommentChange.bind(this);

    }
    
    requestEditCommentChange(event) {
		this.setState({
            requestEditComment: event.target.value
        });
    }

    // 수정 댓글을 보여준다.
    showEditComment = () => {
        this.setState({
            isEditShow: true
        });
    }

    //댓글을 수정한다.
    editComment = () => {
        this.props.editPostsComment(
            this.props.id,
            this.state.requestEditComment
        );
        this.setState({
            isEditShow: false
        });
    }

    // 댓글을 삭제한다.
    deleteComment = () => {
        this.props.deletePostsComment(
            this.props.id
        )
    }

    //수정하는 것을 취소한다.
    cancelEditComment = () => {
        this.setState({
            isEditShow: false,
            requestEditComment: ''
        });
    }

    render(){
        return (
            <ListGroup.Item
                id={this.props.id}
                key={this.props.id}>  
                {/* 수정 버튼 클릭 시에, 자신의 작성한 글을 수정 및 취소 할 수 있다. */}
                {(
                this.props.user.authenticated === true
                    && this.props.user.logon === true
                    && this.props.user.currentUser != null
                    && this.props.user.currentUser.userId == this.props.author
                && this.state.isEditShow == true) ?
                ( 
                <InputGroup>
                    {(this.props.picture) ?
                    (<Image src={this.props.picture}
                        width={26} height={26} roundedCircle />):
                    (<FontAwesomeIcon icon={faSmileWink} size="lg" color="Violet"/>)
                    }
                    <span><strong>{this.props.author}</strong></span>
                    &nbsp;
                    <FormControl
                        onChange={this.requestEditCommentChange}
                        placeholder={this.props.comment}
                        aria-label="comment"
                        size="sm"
                    />
                    {
                    (this.props.user.authenticated === true
                        && this.props.user.logon === true
                        && this.props.user.currentUser != null
                        && this.props.user.currentUser.userId == this.props.author) ? 
                    (
                    <FontAwesomeIcon onClick={this.cancelEditComment} icon={faStop} pull="right" /> 
                    ): ('')}
                    {
                    (this.props.user.authenticated === true
                        && this.props.user.logon === true
                        && this.props.user.currentUser != null
                        && this.props.user.currentUser.userId == this.props.author) ? 
                    (
                    <FontAwesomeIcon onClick={this.editComment} icon={faReply} pull="right" /> 
                    ): ('')}
                </InputGroup>
                )
                :
                ( 
                <div>
                    {(this.props.picture) ?
                    (<Image src={this.props.picture}
                        width={26} height={26} roundedCircle />):
                    (<FontAwesomeIcon icon={faSmileWink} size="lg" color="MediumSeaGreen"/>)
                    }
                    <span><strong>{this.props.author}</strong></span>
                    &nbsp;
                    {this.props.comment}
                    {/* 자신의 작성한 글을 수정, 삭제 할 수 있다. */}
                    {
                    (this.props.user.authenticated === true
                    && this.props.user.logon === true
                    && this.props.user.currentUser != null
                    && this.props.user.currentUser.userId == this.props.author) ? 
                    (
                        <FontAwesomeIcon onClick={this.showEditComment} icon={faReply} pull="right" /> 
                    )
                    : ('')
                    } 
                    {
                    (this.props.user.authenticated === true
                    && this.props.user.logon === true
                    && this.props.user.currentUser != null
                    && this.props.user.currentUser.userId == this.props.author) ? 
                    (
                        <FontAwesomeIcon onClick={this.deleteComment} icon={faRemoveFormat} pull="right" /> 
                    )
                    : ('')
                    }  
                </div>
                )
                }
            </ListGroup.Item>);

    }
}

const mapStateToProps = (state, ownProps) => {
	return {
        user: state.user
    };
}

export default withRouter(connect(mapStateToProps,
     {createPostsComment, editPostsComment, deletePostsComment})(PostsComment));
  