import React, { Component } from 'react';
import { Card, Button } from 'react-bootstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faAngleDown } from '@fortawesome/free-solid-svg-icons'
import { showPostsEditModal, showPostsDeleteModal, getDetailPostsAndCommentsAndLikeCnt } from '../../actions/posts';
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

  getDetailPostsAndCommentsAndLikeCnt = () => { 
    this.props.getDetailPostsAndCommentsAndLikeCnt(this.props.id, 0, MAX_SHOW_POST_COMMENT);
  }

  render(){
      /**
       * Like ID 가 일치할 경우, 일치하는 건수를 보여준다.
       * 없을 경우, 0 으로 보여준다.
       */
      const likes = this.props.postsEtc.likes;
      const filteredLike = likes.filter(like => (like.id == this.props.id));
      const likeCount = (filteredLike.length > 0) ? filteredLike[0].likeCount : 0;

      /**
       * Posts ID 에 해당하는 댓글만 조회한다.
       */
      const postsComments = this.props.postsComments.filter(postsComment =>
        (postsComment.postsId == this.props.id)
      );

      return (
          <Card id={this.props.id}>
            <Posts 
              id={this.props.id}
              author={this.props.author}
              title={this.props.title}
              content={this.props.content}
              thumbnail={this.props.thumbnail}
              likeCount={likeCount}
              modifiedDate={this.props.modifiedDate}
              clientRef={this.props.clientRef}
              /> 
              {/* 댓글 최신 순으로 5개 이상 일 경우, 다른 창에서 보여지도록 수정한다. */}
              <PostsComments 
                postsId={this.props.id} 
                postsComments={postsComments}/>
              {/* 댓글이 5개 이상일 경우, '더보기' 버튼을 보여준다. */}
              <Button variant="secondary" 
                size="lg"  
                onClick={() => {this.getDetailPostsAndCommentsAndLikeCnt()}}
                block>
                <FontAwesomeIcon icon={faAngleDown}  /> 더보기
              </Button>
          </Card>
      )};
}

const mapStateToProps = (state) => {
	return {
    postsEtc: state.postsEtc,
    postsComments: state.postsComment.comments,
    isSocket: state.socket.isSocket
	};
}

export default withRouter(connect(mapStateToProps, {showPostsEditModal, showPostsDeleteModal,
  getDetailPostsAndCommentsAndLikeCnt,
  increaseLikeCount})(PostsWrapper));
