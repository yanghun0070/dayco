import React, { Component } from 'react';
import { Col, Row } from 'react-bootstrap';
import question from '../img/the-question-mark.png';
import PostsWrapper from './PostsWrapper';
import { getAllPosts } from '../../actions/posts';
import { connect } from 'react-redux';
import { withRouter } from "react-router";

class PostsList extends Component {

    /**
     * posts 추가 및 변경될 때, 호출
     * @todo 추후, 전체 Posts 가 아닌 페이징 처리로 수정
     * @see https://ko.reactjs.org/docs/react-component.html
     */
    componentDidMount() {
        this.props.getAllPosts();
    }

    render(){
        let postsInfosHtml = this.props.posts.list.map((info) => {
            
            return <PostsWrapper 
                key={info.id}
                id={info.id}
                title={info.title} 
                thumbnail={question}
                author={info.author}
                content={info.content}
                modifiedDate={info.modifiedDate}
                clientRef={this.props.clientRef}
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
