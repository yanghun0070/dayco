import React, { Component } from 'react';
import { Card, ListGroup, ListGroupItem, Badge, DropdownButton, DropdownItem } from 'react-bootstrap';
import { FontAwesomeIcon } from '@fortawesome/react-fontawesome'
import { faCoffee, faRainbow, faRemoveFormat } from '@fortawesome/free-solid-svg-icons'
import { showPostsEditModal, showPostsDeleteModal, deletePosts } from '../../actions/posts';
import { connect } from 'react-redux';
import { withRouter } from "react-router";

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

    render(){
        const modifiedDateStr = new Date(this.props.modifiedDate);
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
                    <Card.Link href="#">Like</Card.Link>
                    <Card.Link href="#">Comment</Card.Link>
                </Card.Body>
            </Card>
        )};
}

const mapStateToProps = (state) => {
	return {
		posts: state.posts
	};
}


export default withRouter(connect(mapStateToProps, {showPostsEditModal, showPostsDeleteModal, deletePosts})(Posts));
