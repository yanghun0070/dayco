import initialState from '../constants/initialState';
import * as types from '../constants/types';


/**
 * The posts reducer is responsible
 * @param {*} state 
 * @param {*} action 
 */
export function posts(state = initialState.posts, action) {
    switch (action.type) {
        case types.posts.LIST_SUCCESS:
            const { postsList } = action;
            return Object.assign({}, state, {list: [...postsList]});
        case types.posts.CREATE_SUCCESS:
            const { posts } = action; 
            state.list.unshift(posts);
            return state;
        case types.posts.LIST_FAIL:
            return Object.assign({}, state, null);
        default:
            return state;
    }
}
