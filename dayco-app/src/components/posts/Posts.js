import React, { Component } from 'react';
import { Col, Row, Form, Button, Card, ListGroup, ListGroupItem, Badge } from 'react-bootstrap';

const Posts = ({id, title, thumbnail, content, author, modifiedDate}) => {
    return (
        <Card id={id}>
            <Card.Img variant="top" src={thumbnail} />
            <Card.Body>
                <Card.Title>{title}</Card.Title>
                <Card.Text>
                {content}
                </Card.Text>
            </Card.Body>
            <ListGroup className="list-group-flush">
                <ListGroupItem>
                    <Badge variant="dark"> Author:{author}</Badge>,  
                    <Badge variant="dark"> 변경 시간: {modifiedDate[0]}-{modifiedDate[1]}-{modifiedDate[2]} {modifiedDate[3]}:{modifiedDate[4]}:{modifiedDate[5]}</Badge>
                </ListGroupItem>
            </ListGroup>
            <Card.Body>
                <Card.Link href="#">Like</Card.Link>
                <Card.Link href="#">Comment</Card.Link>
            </Card.Body>
        </Card>
    );
};

export default Posts;