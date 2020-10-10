import * as types from '../constants/types';
import * as API from '../services/http';

export function getPageOfComments(postsId, page, rowNum) {
    return dispatch => {
      return API.getPageOfComments(postsId, page, rowNum)
                 .then(async(response) => {
        dispatch({
          type: types.postsComment.PAGING_LIST_SUCCESS,
          pageOfComments: response.data
        })
      }).catch(function (error) {
        dispatch({
          type: types.postsComment.PAGING_LIST_FAIL
        })
      })
    }
}
 
export function getPageOfCommentsForPostsIds(postsIds, page, rowNum) {
    return dispatch => { 
        return API.getPageOfCommentsForPostsIds(postsIds, page, rowNum)
                    .then(async(response) => {
            dispatch({ 
                type: types.postsComment.PAGING_LIST_SUCCESS,
                pageOfComments: response.data
            })
        }).catch(function (error) {
            dispatch({
                type: types.postsComment.PAGING_LIST_FAIL
            })
        })
    }
}

export function createPostsComment(postsId, comment) {
    return dispatch => { 
        return API.createPostsComment(postsId, comment)
        .then(async(response) => {
            dispatch({
                type: types.postsComment.CREATE_SUCCESS,
                postsComment: response.data
                })
        }).catch(function (error) {
            dispatch({
                type: types.postsComment.CREATE_FAIL
                })
        })
    }
}

/**
 * 게시글 댓글 수정
 * @param {*} commentId 댓글 ID
 * @param {*} comment 댓글 
 */
export function editPostsComment(commentId, comment) {
    return dispatch => { 
        return API.editPostsComment(commentId, comment)
        .then(async(response) => {
            dispatch({
                type: types.postsComment.EDIT_SUCCESS,
                postsComment: response.data
              })
        }).catch(function (error) {
            dispatch({
                type: types.postsComment.EDIT_FAIL
              })
        })
    }
}

/**
 * 게시글 댓글 삭제
 * @param {*} commentId 댓글 ID
 */
export function deletePostsComment(commentId) {
    return dispatch => { 
        return API.deletePostsComment(commentId)
        .then(async(response) => {
            console.log(commentId)
            dispatch({
                type: types.postsComment.DELETE_SUCCESS,
                postsCommentId: commentId
              })
        }).catch(function (error) {
            dispatch({
                type: types.postsComment.DELETE_FAIL
              })
        })
    }
}

/**
 * 다음 페이징에 있는 댓글 목록을 조회한다.
 * @param {*} postsId 댓글 ID
 * @param {*} page 현재 페이지
 * @param {*} rowNum 보여질 건수
 */
export function getNextPageOfComments(postsId, page, rowNum) {
    return dispatch => {
        return API.getPageOfComments(postsId, page, rowNum * 2)
                   .then(async(response) => {
          dispatch({
            type: types.postsComment.PAGING_DETAIL_LIST_SUCCESS,
            pageOfComments: response.data
          })
        }).catch(function (error) {
          dispatch({
            type: types.postsComment.PAGING_DETAIL_LIST_FAIL
          })
        })
    }
}