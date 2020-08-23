import * as types from '../constants/types';
import * as API from '../services/http';

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
export function editPostsSuccess() {
    return {
        type: types.posts.EDIT_SUCCESS
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

export function createPosts(title, content) {
    return dispatch => {
        return API.createPosts(title, content)
        .then(async(response) => {
            dispatch(createPostsSuccess(response.data));
        }).catch(function (error) {
            dispatch(createPostsFail());
        })
    }
}

export function editPosts(id, title, content) {
    return dispatch => {
        return API.editPosts(id, title, content)
        .then(async(response) => {
            dispatch(editPostsSuccess());
        }).catch(function (error) {
            dispatch(editPostsFail());
        })
    }
}