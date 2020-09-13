import * as types from '../constants/types';
import * as API from '../services/http';
import * as Alert from '../actions/alert';

/**
 * 게시판 목록 전체 조회 성공
 * @param {*} posts 
 */
export function getAllPostsSuccess(postsList) {
    return {
        type: types.posts.LIST_SUCCESS,
        postsList
    };
}

/**
 * 게시판 목록 조회 성공
 */
export function getPostsSuccess() {
    return {
        type: types.posts.GET_SUCCESS
    };   
}

/**
 * 게시판 생성 성공
 */
export function createPostsSuccess(posts) {
    return {
        type: types.posts.CREATE_SUCCESS,
        posts
    };
}

/**
 * 게시판 목록 수정 성공
 */
export function editPostsSuccess(editPosts) {
    return {
        type: types.posts.EDIT_SUCCESS,
        editPosts
    };
}

/**
 * 게시판 목록 전체 조회 실패
 */
export function getAllPostsFail() {
    return {
        type: types.posts.LIST_FAIL
    };
}

/**
 * 게시판 목록 조회 실패
 */
export function getPostsFail() {
    return {
        type: types.posts.GET_FAIL
    };
}

/**
 * 게시판 목록 수정 실패
 */
export function editPostsFail() {
    return {
        type: types.posts.EDIT_FAIL
    }
}

/**
 * 게시판 생성 실패
 */
export function createPostsFail() {
    return {
        type: types.posts.CREATE_FAIL
    };
}

export function getAllPosts() {
    return dispatch => {
        return API.getAllPosts()
        .then(async(response) => {
            var result = response.data
            dispatch(getAllPostsSuccess(result));
        }).catch(function (error) {
            dispatch(getAllPostsFail());
        })
    }
}

export function getPosts(id) {
    return dispatch => {
        return API.getPosts(id)
        .then(async(response) => {
            var result = response.data
            dispatch(getPostsSuccess(result));
        }).catch(function (error) {
            dispatch(getPostsFail());
        })
    }
}

export function dispatchCreatePostsSuccess(data) {
    return dispatch => {
        dispatch(Alert.createAlert({
            variant : 'success', 
            message : data.author + ' 게시글 생성 성공하였습니다.'
        }));
        dispatch(createPostsSuccess(data));
        dispatch(hidePostsEditModal());
    }
}

export function dispatchCreatePostsFail() {
    return dispatch => {
        dispatch(createPostsFail());
    }
}

export function dispatchEditPostsSuccess(data) {
    return dispatch => {
        dispatch(editPostsSuccess(data));
        dispatch(Alert.createAlert({
            variant : 'success', 
            message : data.author + ' 게시글 수정 성공하였습니다.'
        }));
        dispatch(hidePostsEditModal());
    }
}

export function dispatchEditPostsFail() {
    return dispatch => {
        dispatch(editPostsFail());
    }
}

export function dispatchDeletePostsSuccess(id, author) {
    return dispatch => {
        dispatch({
            type: types.posts.DELETE_SUCCESS,
            deletePostsId: id
        });
        dispatch(hidePostsEditModal());
    }
}

export function dispatchDeletePostsFail() {
    return dispatch => {
        dispatch({
            type: types.posts.DELETE_FAIL
        });
        dispatch(Alert.createAlert({
            variant : 'danger', 
            message : '게시글 삭제 실패하였습니다.'
        }));
    }
}

export function createPosts(title, content) {
    return dispatch => {
        return API.createPosts(title, content)
        .then(async(response) => {
            dispatch(Alert.createAlert({
                variant : 'success', 
                message : response.data.author + ' 게시글 생성 성공하였습니다.'
            }));
            dispatch(createPostsSuccess(response.data));
            dispatch(hidePostsEditModal());
        }).catch(function (error) {
            dispatch(Alert.createAlert({
                variant : 'danger', 
                message : '게시글 생성 실패하였습니다.'
            }));
            dispatchCreatePostsFail();
        })
    }
}

export function editPosts(id, title, content, author) {
    return dispatch => {
        return API.editPosts(id, title, content, author)
            .then(async(response) => {
                dispatch(editPostsSuccess(response.data));
            dispatch(Alert.createAlert({
                variant : 'success', 
                message : response.data.author + ' 게시글 수정 성공하였습니다.'
            }));
            dispatch(hidePostsEditModal());
        }).catch(function (error) {
            dispatch(Alert.createAlert({
                variant : 'danger', 
                message : '게시글 수정 실패하였습니다.'
            }));
            dispatchEditPostsFail();
        })
    }
}

export function deletePosts(id, author) {
    return dispatch => {
        return API.deletePosts(id)
        .then(async(response) => {
            dispatch(Alert.createAlert({
                variant : 'success', 
                message : '게시글 삭제 성공하였습니다.'
            }));
            dispatch({
                type: types.posts.DELETE_SUCCESS,
                deletePostsId: id
            });
            dispatch(hidePostsEditModal());
        }).catch(function (error) {
            dispatchDeletePostsFail();
        })
    }
}

export function showPostsCreateModal() {
    return dispatch => {
        dispatch({
            type: types.postsEditModal.MODAL_CREATE
        })
    };
}

export function showPostsEditModal(id, title, content, author) {
    return dispatch => {
        dispatch({
            type: types.postsEditModal.MODAL_EDIT,
            id: id,
            title: title,
            content: content,
            author: author
        })
    };
}

export function showPostsDeleteModal(id, title, author) {
    return dispatch => {
        dispatch({
            type: types.postsEditModal.MODAL_DELETE,
            id: id,
            title: title,
            author: author
        })
    };
}
  
export function hidePostsEditModal() {
    return dispatch => {
        dispatch({ 
            type: types.postsEditModal.MODAL_HIDE 
        })
    };
}