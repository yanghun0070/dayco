import * as types from '../constants/types';
import * as API from '../services/http';


export function increaseLikeCount(id) {
  return dispatch => {
    return API.increaseLikeCount(id)
               .then(async(response) => {
      dispatch({
        type: types.posts.LIKE_INCREASE_SUCCESS,
        id: response.data.id,
        likeCount: response.data.likeCount
      });
    }).catch(function (error) {
      dispatch({
        type: types.posts.INCREASE_FAIL
      })
    })
  }
}

export function dispatchPostsIncreaseLikeSuccess(id, likeCount) {
  return dispatch => {
    dispatch({
      type: types.posts.LIKE_INCREASE_SUCCESS,
      id: id,
      likeCount: likeCount 
    })
  }
}