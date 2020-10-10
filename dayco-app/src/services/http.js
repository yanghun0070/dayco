import axios from 'axios';
import Cookies from "js-cookie";
import { API_BASE_URL } from '../constants';


export function joinUser(userId, email, password, confirmPassword) {
    return axios.post(API_BASE_URL + "/auth/signup",{
        headers: {
            'Content-type': 'application/json'
        },
        userId: userId,
        email: email,
        password: password
    });
}

export function loginUser(userId, password) {
    return axios.post(API_BASE_URL + "/auth/signin",{
        headers: {
            'Content-type': 'application/json'
         },
        userId : userId,
        password : password
    });
}

export function getCurrentUser() {
    const token = Cookies.get("token") ? Cookies.get("token") : null;
    return axios.get(API_BASE_URL + "/user/me", {
        headers: {
            'Content-type': 'application/json',
            'Authorization': token
         }
    });
}

export function createPosts(title, content) {
    const token = Cookies.get("token") ? Cookies.get("token") : null;
    return axios.post(API_BASE_URL + "/posts", {
        title: title,
        content: content
    }, {
        headers: {
            'Content-type': 'application/json',
            'Authorization': token
        }
    });
}

export function editPosts(id, title, content, author) {
    const token = Cookies.get("token") ? Cookies.get("token") : null;
    return axios.put(API_BASE_URL + "/posts/" + id, {
         title: title,
         content: content,
         author: author
    }, {
        headers: {
            'Content-type': 'application/json',
            'Authorization': token
         }
    });
}

export function deletePosts(id) {
    const token = Cookies.get("token") ? Cookies.get("token") : null;
    return axios.delete(API_BASE_URL + "/posts/" + id, {
       headers: {
           'Content-type': 'application/json',
           'Authorization': token
        }
   });
}

export function getPosts(id) {
    const token = Cookies.get("token") ? Cookies.get("token") : null;
    return axios.get(API_BASE_URL + "/posts", {
        headers: {
            'Content-type': 'application/json',
            'Authorization': token
         },
         params: {
             id: id
         }
    })
}

export function getAllPosts() {
    const token = Cookies.get("token") ? Cookies.get("token") : null;
    return axios.get(API_BASE_URL + "/posts/all", {
        headers: {
            'Content-type': 'application/json',
            'Authorization': token
         }
    })
}

export function getPostsLikeCount(postsId) {
    const token = Cookies.get("token") ? Cookies.get("token") : null;
    return axios.get(API_BASE_URL + "/posts/likes/" + postsId, {
        headers: {
            'Content-type': 'application/json',
            'Authorization': token
        }
    });
}

export function increaseLikeCount(postsId) {
    const token = Cookies.get("token") ? Cookies.get("token") : null;
    return axios.post(API_BASE_URL + "/posts/like/increase/" + postsId, null,
    {
        headers: {
            'Content-type': 'application/json',
            'Authorization': token
        }
    });
}

// 게시글 댓글을 페이징 처리로 조회한다.
export function getPageOfComments(postsId, page, rowNum) {
    const token = Cookies.get("token") ? Cookies.get("token") : null;
    return axios.get(API_BASE_URL + "/posts/comments/page", {
        headers: {
            'Content-type': 'application/json',
            'Authorization': token
         },
         params: {
            postsId: postsId,
            page: page,
            rowNum: rowNum
         }
    })
} 

// 전체 게시글 댓글 페이징 처리로 조회한다. 
export function getPageOfCommentsForPostsIds(postsIds, page, rowNum) {
    const token = Cookies.get("token") ? Cookies.get("token") : null;
    return axios.get(API_BASE_URL + "/posts/comments", {
        headers: {
            'Authorization': token
         },
         params: {
            postsIds: postsIds.join(','),
            page: page,
            rowNum: rowNum
         }
    });
}

// 게시글 댓글을 생성한다.
export function createPostsComment(postsId, comment) {
    const token = Cookies.get("token") ? Cookies.get("token") : null;
    return axios.post(API_BASE_URL + "/posts/comment", null,{
        headers: {
            'Content-type': 'application/json',
            'Authorization': token
        },
        params: {
            postsId: postsId,
            comment: comment
        }
    });
}

// 게시글 댓글을 수정시킨다.
export function editPostsComment(commentId, comment) {
    const token = Cookies.get("token") ? Cookies.get("token") : null;
    return axios.put(API_BASE_URL + "/posts/comment/" + commentId, {
        comment: comment
    }, {
        headers: {
            'Content-type': 'application/json',
            'Authorization': token
        },
        params: {
            comment: comment
        }
    });
}

// 게시글 댓글을 삭제시킨다.
export function deletePostsComment(commentId) {
    const token = Cookies.get("token") ? Cookies.get("token") : null;
    return axios.delete(API_BASE_URL + "/posts/comment/" + commentId, {
        headers: {
            'Content-type': 'application/json',
            'Authorization': token
         }
    });
}

// Posts Id 값에 연관된 Posts 정보, 댓글, Like 건수를 조회한다.
export function getDetailPostsAndCommentsAndLikeCnt(postsId, page, rowNum) {
    const token = Cookies.get("token") ? Cookies.get("token") : null;

    //Posts Id 값에 연관된 Posts 정보를 조회한다.
    const requestPosts = axios.get(API_BASE_URL + "/posts/" + postsId, {
        headers: {
            'Content-type': 'application/json',
            'Authorization': token
         }
    })

    //Posts Id 값에 연관된 Posts 댓글 정보를 조회한다.
    const requestPostsComments = getPageOfComments(postsId, page, rowNum);

    //Posts Id 값에 연관된 Posts Like 건수를 조회한다.
    const requestPostsLikeCount = getPostsLikeCount(postsId);
    return axios.all([requestPosts, requestPostsComments, requestPostsLikeCount]);
}
