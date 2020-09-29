import initialState from '../constants/initialState';
import * as types from '../constants/types';

/**
 * Like, Comment 처리
 * @param {*} state
 * @param {*} action
 */
export function postsEtc(state = initialState.postsEtc, action) {
  switch (action.type) {
    case types.postsLikeCount.GET_SUCCESS:
      const { like } = action;
      let duplicatedLike = state.likes.filter(l => (l.id == like.id))
      /**
       * 중복되는 Like가 있을 경우, 하나로 통일 시킨다.
       * 중복되는 Like가 없을 경우, 추가시킨다.
       */
      const changedLikes = (duplicatedLike.length > 0) ? state.likes.map((l) => {
        if(l.id === like.id) {
            return Object.assign(like, {
              id: like.id,
              likeCount: likeCount
            });
        }
        return like;
      }) : [
        like, ...state.likes
      ]
      return Object.assign({}, state, {likes: [...changedLikes]});
    case types.postsLikeCount.INCREASE_SUCCESS:
      /**
       * Like 건수 목록 중에 변경됬을 경우,
       * 해당 항목만 변경시킨다.
       */
      const { id, likeCount } = action;
      const editLikesList = state.likes.map((like) => {
        if(id === like.id) {
            return Object.assign(like, {
              id: like.id,
              likeCount: likeCount
            });
        }
        return like;
      });
      return Object.assign({}, state, {
        likes: [...editLikesList]
      });
    case types.postsLikeCount.DECREASE_SUCCESS:
      return state;
    case types.postsLikeCount.INCREASE_BEFORE:
      return Object.assign({}, state, {
        status: 'increment'
      });
    case types.postsLikeCount.DECREASE_BEFORE:
      return Object.assign({}, state, {
        status: 'decrement'
      });
    case types.postsLikeCount.GET_FAIL:
      return state;
    case types.postsLikeCount.INCREASE_FAIL:
      return state;
    case types.postsLikeCount.DECREASE_FAIL:
      return state;
    default:
      return state;
  }
}
