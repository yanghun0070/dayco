import React, { Component } from 'react';
import { Col, Row } from 'react-bootstrap';
import question from '../img/the-question-mark.png';
import Posts from './Posts';
import { getAllPosts } from '../../actions/posts';
import { connect } from 'react-redux';
import { withRouter } from "react-router";

class PostsList extends Component {

    /**
     * posts 추가 및 변경될 때, 호출
     * @see https://ko.reactjs.org/docs/react-component.html
     */
    componentDidMount() {
        this.props.getAllPosts();
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

export default withRouter(connect(mapStateToProps, {getAllPosts})(PostsList));
