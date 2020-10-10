import React, { Component } from 'react';
import { connect } from 'react-redux';
import { withRouter } from "react-router";
import { Card, ListGroup, ListGroupItem, Badge, DropdownButton, DropdownItem, Button } from 'react-bootstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faCoffee, faThumbsUp, faThumbtack, faRainbow, faRemoveFormat,
  faAngleDown, faComment } from '@fortawesome/free-solid-svg-icons'
import { showPostsEditModal, showPostsDeleteModal } from '../../actions/posts';
import { getPostsLikeAddCount, increaseLikeCount, dispatchBeforeIncreaseLike } from '../../actions/postsLike';
import Cookies from "js-cookie";
import Moment from "react-moment";

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
  
    increaseLikeCnt = () => {
        if(this.props.isSocket === true) {
            this.props.dispatchBeforeIncreaseLike();
            let jsonStr = JSON.stringify({
            type: "increase",
            postsId: this.props.id
            })
            this.sendMessage("/app/posts/like", jsonStr);
        } else {
            this.props.increaseLikeCount(this.props.id);
        }
    };
 
    componentDidMount() {
        this.props.getPostsLikeAddCount(this.props.id);
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
        return (
            <Card>
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
                            변경 시간: 
                            <Moment date={this.props.modifiedDate} format="YYYY-MM-DD HH:mm:ssZ"/>
                        </Badge>
                    </ListGroupItem>
                </ListGroup>
                <Card.Body>
                  <small className="text-muted">
                    <FontAwesomeIcon icon={faThumbsUp}
                                     style={{color: "blue"}}
                                     onClick={() => { this.increaseLikeCnt(this.props.id) }} />&nbsp;
                    <Badge pill variant="dark">{this.props.likeCount}</Badge>
                    &nbsp;&nbsp;
                    <FontAwesomeIcon icon={faComment} />
                  </small>
                </Card.Body>
            </Card>
        );
    };
}

const mapStateToProps = (state) => {
	return {
		posts: state.posts,
        isSocket: state.socket.isSocket
	};
}

export default withRouter(connect(mapStateToProps, {showPostsEditModal, showPostsDeleteModal,
    getPostsLikeAddCount, increaseLikeCount, dispatchBeforeIncreaseLike})(Posts));
  