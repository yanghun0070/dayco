import * as types from '../constants/types';
import * as API from '../services/http';
import * as Alert from '../actions/alert';
import axios from 'axios';
import { MAX_SHOW_POST_COMMENT } from "../constants";


/**
 * 게시글 목록 전체 조회 성공
 * @param {*} posts 
 */
export function getAllPostsSuccess(postsList) {
    return {
        type: types.posts.LIST_SUCCESS,
        postsList
    };
}

/**
 * 게시판 생성 성공
 */
export function createPostsSuccess(createdPosts) {
    return {
        type: types.posts.CREATE_SUCCESS,
        createdPosts
    };
}

/**
 * 게시글 목록 수정 성공
 */
export function editPostsSuccess(editedPosts) {
    return {
        type: types.posts.EDIT_SUCCESS,
        editedPosts
    };
}

/**
 * 게시글 목록 전체 조회 실패
 */
export function getAllPostsFail() {
    return {
        type: types.posts.LIST_FAIL
    };
}

/**
 * 게시글 목록 조회 실패
 */
export function getPostsFail() {
    return {
        type: types.posts.GET_FAIL
    };
}

/**
 * 게시글 목록 수정 실패
 */
export function editPostsFail() {
    return {
        type: types.posts.EDIT_FAIL
    }
}

/**
 * 게시글 생성 실패
 */
export function createPostsFail() {
    return {
        type: types.posts.CREATE_FAIL
    };
}

/**
 * 모든 게시글을 조회
 */
export function getAllPosts() {
    return dispatch => {
        return API.getAllPosts()
        .then(async(response) => {
            let result = response.data
            dispatch(getAllPostsSuccess(result)); 
        }).catch(function (error) {
            dispatch(getAllPostsFail());
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
        //상세 Modal 창에서 삭제할 경우, Hide 시킨다.
        dispatch({
            type: types.postsDetailModal.MODAL_HIDE
        })
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

// 게시물 상세 Modal 창을 숨긴다. 
export function dispatchHidePostsDetailModal() {
    return dispatch => {
        dispatch({
            type: types.postsDetailModal.MODAL_HIDE
        })
    };
}


export function getPostsWithCommentsAndLikeCnt(postsId) {
    return dispatch => {
        return API.getPosts(postsId)
        .then(async(response) => {
            // 게시물 정보를 전달한다.
            dispatch({
                type: types.posts.GET_SUCCESS,
                posts: response.data
            });
            dispatch({
                type: types.postsDetailModal.MODAL_SHOW
            })
        });
    }
}