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

export function createPosts(title, content, fileBase64, fileName) {
    const token = Cookies.get("token") ? Cookies.get("token") : null;
    return axios.post(API_BASE_URL + "/posts", {
        title: title,
        content: content,
        fileBase64: fileBase64,
        fileName: fileName,
    }, {
        headers: {
            'Content-type': 'application/json',
            'Authorization': token
        }
    });
}

export function editPosts(id, title, content, author, fileBase64, fileName) {
    const token = Cookies.get("token") ? Cookies.get("token") : null;
    return axios.put(API_BASE_URL + "/posts/" + id, {
         title: title,
         content: content,
         author: author,
         fileBase64: fileBase64,
         fileName: fileName
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

// Posts Id 값에 연관된 Posts 정보, 댓글, Like 건수를 조회한다.
export function getPosts(id) {
    const token = Cookies.get("token") ? Cookies.get("token") : null;
    return axios.get(API_BASE_URL + "/posts/" + id, {
        headers: {
            'Content-type': 'application/json',
            'Authorization': token
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

//Profile 변경한다.
export function changeProfile(fileBase64, fileName) {
    const token = Cookies.get("token") ? Cookies.get("token") : null;
    /**
     * application/x-www-form-urlencoded: &으로 분리되고,
     * "=" 기호로 값과 키를 연결하는 key-value tuple로 인코딩되는 값이다.
     * 영어 알파벳이 아닌 문자들은 percent encoded 으로 인코딩된다. 
     * 따라서, 이 content type은 바이너리 데이터에 사용하기에는 적절치 않다. 
     * (바이너리 데이터에는 use multipart/form-data 를 사용하는 것이 적절하다.)
     * 
     * query string 으로 던질 시에 http request header size 가 8kb 가 넘어가서 400 에러가 떨어진다.(Headers: 8.05 KB,  Body: 0B)
     * 톰캣에서 headers size(start line + header) 가 limit 제한이 8kb 라 http status 가 400  이 떨어진다 한다.
     * form-data 형식으로 변경해서 해결함 (Body: 7.61 KB, Headers: 542 B)
     * {@see https://datatracker.ietf.org/doc/html/rfc2616#section-4.1}
     * 
     * {@code POST /profile/change HTTP/1.1\r\n} 
     * {@code POST /profile/change?fileBase64=iVBORw0KGgo...] HTTP/1.1\r\n} 
     * 
     * 
     * @see https://developer.mozilla.org/ko/docs/Web/HTTP/Methods/POST
     * @see https://httpwg.org/specs/rfc7231.html#payload
     * @see https://stackoverflow.com/questions/5876809/do-http-post-methods-send-data-as-a-querystring/5876931
     */
    const formData = new FormData();
    formData.append("fileBase64", (fileBase64) ? fileBase64 : null); 
    formData.append("fileName", (fileName) ? fileName : null); 

    return axios.post(API_BASE_URL + "/profile/change", formData,
        {
            headers: {
                'Content-type': 'multipart/form-data',
                'Authorization': token
            }
        });
}