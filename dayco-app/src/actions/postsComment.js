import * as types from '../constants/types';
import * as API from '../services/http';
import { MAX_SHOW_POST_COMMENT } from "../constants";


/**
 * 페이징에 있는 댓글 목록을 조회한다.
 * @param {*} postsId 댓글 ID
 * @param {*} page 현재 페이지
 * @param {*} rowNum 보여질 건수
 */
export function getPageOfComments(postsId, page, rowNum) {
    return dispatch => {
      return API.getPageOfComments(postsId, page, rowNum)
                 .then(async(response) => {
        dispatch({
          type: types.posts.PAGING_COMMENT_FROM_DETAIL_SUCCESS,
          postsId: postsId,
          pageOfComments: response.data
        })
      }).catch(function (error) {
        dispatch({
          type: types.posts.PAGING_COMMENT_FROM_DETAIL_FAIL
        })
      })
    }  
}

/**
 * 게시글 댓글 생성
 * @param {*} postsId Posts ID
 * @param {*} comment 댓글
 */
export function createPostsComment(postsId, comment) {
    return dispatch => { 
        return API.createPostsComment(postsId, comment)
        .then(async(response) => {
            // 새로 등록했을 경우, 첫페이지로 이동
            API.getPageOfComments(postsId, 0, MAX_SHOW_POST_COMMENT)
                 .then(async(response) => {
                    dispatch({
                        type: types.posts.PAGING_COMMENT_SUCCESS,
                        postsId: postsId,
                        pageOfComments: response.data
                      })
                 });
        }).catch(function (error) {
            dispatch({ 
                type: types.posts.CREATE_COMMENT_FAIL
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
                type: types.posts.EDIT_COMMENT_SUCCESS, 
                editedPostsComment : response.data
            })
        }).catch(function (error) {
            dispatch({
                type: types.posts.EDIT_COMMENT_FAIL
              })
        })
    }
}

/**
 * 게시글 댓글 삭제
 * @param {*} postsId Posts ID
 * @param {*} commentId 댓글 ID
 */
export function deletePostsComment(postsId, commentId) {
    return dispatch => { 
        return API.deletePostsComment(commentId)
        .then(async(response) => {
            API.getPageOfComments(postsId, 0, MAX_SHOW_POST_COMMENT)
                 .then(async(response) => {
                    dispatch({
                        type: types.posts.PAGING_COMMENT_SUCCESS,
                        postsId: postsId,
                        pageOfComments: response.data
                      })
                 });
        }).catch(function (error) {
            dispatch({
                type: types.posts.DELETE_COMMENT_FAIL
              })
        })
    }
}
