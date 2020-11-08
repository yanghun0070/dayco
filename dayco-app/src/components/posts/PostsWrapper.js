import React, { Component } from 'react';
import { Card, Button } from 'react-bootstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faAngleDown } from '@fortawesome/free-solid-svg-icons'
import { showPostsEditModal, showPostsDeleteModal, getPostsWithCommentsAndLikeCnt } from '../../actions/posts';
import Posts from '../posts/Posts'; 
import { increaseLikeCount } from '../../actions/postsLike';
import PostsComments from '../posts/PostsComments';
import { connect } from 'react-redux';
import { withRouter } from "react-router";
import Cookies from "js-cookie";
import { MAX_SHOW_POST_COMMENT } from "../../constants";

class PostsWrapper extends Component {

  constructor(){
    super(...arguments);
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

  getPostsWithCommentsAndLikeCnt = () => { 
    this.props.getPostsWithCommentsAndLikeCnt(this.props.id);
  }

  render(){
      return (
          <Card id={this.props.id}>
            <Posts 
              id={this.props.id}
              author={this.props.author}
              title={this.props.title}
              content={this.props.content}
              thumbnail={this.props.thumbnail}
              likeCount={(this.props.postsLike) ? this.props.postsLike.likeCount : 0 }
              modifiedDate={this.props.modifiedDate}
              clientRef={this.props.clientRef}
              /> 
              {/* 댓글 최신 순으로 5개 이상 일 경우, 다른 창에서 보여지도록 수정한다. */}
              <PostsComments 
                postsId={this.props.id} 
                postsComments={this.props.postsComments}/>
              {/* 댓글이 5개 이상일 경우, '더보기' 버튼을 보여준다. */}
              <Button variant="secondary" 
                size="lg"  
                onClick={() => {this.getPostsWithCommentsAndLikeCnt()}}
                block>
                <FontAwesomeIcon icon={faAngleDown}  /> 더보기
              </Button>
          </Card>
      )};
}

const mapStateToProps = (state) => {
	return {
    isSocket: state.socket.isSocket
	};
}

export default withRouter(connect(mapStateToProps, {showPostsEditModal, showPostsDeleteModal,
  getPostsWithCommentsAndLikeCnt,
  increaseLikeCount})(PostsWrapper));
