import React, { Component } from 'react';
import { Card, ListGroup, ListGroupItem, Badge, DropdownButton, DropdownItem } from 'react-bootstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faCoffee, faThumbsUp, faThumbtack, faRainbow, faRemoveFormat, faComment } from '@fortawesome/free-solid-svg-icons'
import { showPostsEditModal, showPostsDeleteModal } from '../../actions/posts';
import { getPostsLikeCount, increaseLikeCount,
  dispatchBeforeIncreaseLike, dispatchBeforeDecreaseLike, dispatchBeforeGetLike } from '../../actions/postsLike';
import { connect } from 'react-redux';
import { withRouter } from "react-router";
import Cookies from "js-cookie";

class Posts extends Component {

  constructor(){
      super(...arguments);
      this.handleSelect = this.handleSelect.bind(this);
  }

  handleSelect(e) {
      if(e === 'edit') {
          this.props.showPostsEditModal(this.props.id, this.props.title, this.props.content, this.props.author);
      } else if(e === 'delete') {
          this.props.showPostsDeleteModal(this.props.id, this.props.title, this.props.author);
      }
  }

  increaseLikeCnt = (postId) => {
    if(this.props.isSocket === true) {
      this.props.dispatchBeforeIncreaseLike();
      let jsonStr = JSON.stringify({
        type: "increase",
        postsId: postId
      })
      this.sendMessage("/app/posts/like", jsonStr);
    } else {
      this.props.increaseLikeCount(postId);
    }
  };

  componentDidMount() {
    this.props.getPostsLikeCount(this.props.id);
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
        const modifiedDateStr = new Date(this.props.modifiedDate);

        /**
         * Like ID 가 일치할 경우, 일치하는 건수를 보여준다.
         * 없을 경우, 0 으로 보여준다.
         */
        const likes = this.props.postsEtc.likes;
        const filteredLike = likes.filter(like => (like.id == this.props.id));
        const likeCount = (filteredLike.length > 0) ? filteredLike[0].likeCount : 0;
        return (
            <Card id={this.props.id}>
                <Card.Header className="text-right">
                    <DropdownButton size="sm" title={<FontAwesomeIcon icon={faCoffee} />}
                        onSelect={this.handleSelect}>
                        <DropdownItem eventKey='edit'
                        ><FontAwesomeIcon icon={faRainbow} /> Edit</DropdownItem>
                        <DropdownItem eventKey='delete'
                        ><FontAwesomeIcon icon={faRemoveFormat} /> Delete</DropdownItem>
                    </DropdownButton>
                </Card.Header>
                <Card.Img variant="top" src={this.props.thumbnail} />
                <Card.Body>
                    <Card.Title>{this.props.title}</Card.Title>
                    <Card.Text>
                    {this.props.content}
                    </Card.Text>
                </Card.Body>
                <ListGroup className="list-group-flush">
                    <ListGroupItem>
                        <Badge variant="dark"> Author:{this.props.author}</Badge>,  
                        <Badge variant="dark">
                            변경 시간: {modifiedDateStr.toUTCString()}
                        </Badge>
                    </ListGroupItem>
                </ListGroup>
                <Card.Body>
                  <small className="text-muted">
                    <FontAwesomeIcon icon={faThumbsUp}
                                     style={{color: "blue"}}
                                     onClick={() => { this.increaseLikeCnt(this.props.id) }} />&nbsp;
                    <Badge pill variant="dark">{likeCount}</Badge>
                    &nbsp;&nbsp;
                    <FontAwesomeIcon icon={faComment} />
                  </small>
                </Card.Body>
            </Card>
        )};
}

const mapStateToProps = (state) => {
	return {
		posts: state.posts,
    postsEtc: state.postsEtc,
    isSocket: state.socket.isSocket
	};
}

export default withRouter(connect(mapStateToProps, {showPostsEditModal, showPostsDeleteModal,
  getPostsLikeCount, increaseLikeCount,
  dispatchBeforeIncreaseLike, dispatchBeforeDecreaseLike, dispatchBeforeGetLike})(Posts));
