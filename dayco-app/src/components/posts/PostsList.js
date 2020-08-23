import React, { Component } from 'react';
import { Col, Row, Form, Button, Card, ListGroup, ListGroupItem } from 'react-bootstrap';
import question from '../img/the-question-mark.png';
import Posts from './Posts';
import { getAllPosts, createPosts } from '../../actions/posts';
import { connect } from 'react-redux';
import { withRouter } from "react-router";

class PostsList extends Component {

    constructor() {
        super(...arguments);
    }

    /**
     * Reactjs Component Guide 문서
     * https://ko.reactjs.org/docs/react-component.html
     */
    componentDidMount() {
        this.props.getAllPosts();
    }

    componentWillReceiveProps(nextProps) {
        
    }

    render(){
  
        let postsInfosHtml = this.props.posts.list.map((info) => {
            return <Posts 
                key={info.id}
                id={info.id}
                title={info.title} 
                thumbnail={question}
                author={info.author}
                content={info.content}
                modifiedDate={info.modifiedDate}
                />;
        })

        return (
            <div>
                <Row>
					<Col xs={3}></Col>
                    <Col xs={6}>
                        {postsInfosHtml}
                    </Col>
                    <Col xs={3}></Col>
                </Row>
            </div>
        );
    }
}

const mapStateToProps = (state) => {
	return {
		posts: state.posts
	};
}

export default withRouter(connect(mapStateToProps, {getAllPosts, createPosts})(PostsList));
