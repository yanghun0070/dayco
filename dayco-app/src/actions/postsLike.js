import * as types from '../constants/types';
import * as API from '../services/http';

export function getPostsLikeCount(id) {
  return dispatch => {
    return API.getPostsLikeCount(id)
               .then(async(response) => {
      dispatch({
        type: types.postsLikeCount.GET_SUCCESS,
        like: response.data
      })
    }).catch(function (error) {
      dispatch({
        type: types.postsLikeCount.GET_FAIL
      })
    })
  }
}

export function increaseLikeCount(id) {
  return dispatch => {
    return API.increaseLikeCount(id)
               .then(async(response) => {
      dispatch({
        type: types.postsLikeCount.INCREASE_SUCCESS,
        id: response.data.id,
        likeCount: response.data.likeCount
      });
    }).catch(function (error) {
      dispatch({
        type: types.postsLikeCount.INCREASE_FAIL
      })
    })
  }
}

export function dispatchPostsIncreaseLikeSuccess(id, likeCount) {
  return dispatch => {
    dispatch({
      type: types.postsLikeCount.INCREASE_SUCCESS,
      id: id,
      likeCount: likeCount
    })
  }
}

export function dispatchBeforeIncreaseLike() {
  return dispatch => {
    dispatch({
      type: types.actionStatus.SOCKET_POST_LIKE_INCREASE
    })
  }
}

export function dispatchBeforeDecreaseLike() {
  return dispatch => {
    dispatch({
      type: types.actionStatus.SOCKET_POST_LIKE_DECREASE
    })
  }
}

export function dispatchBeforeGetLike() {
  return dispatch => {
    dispatch({
      type: types.actionStatus.SOCKET_POST_LIKE_GET
    })
  }
}