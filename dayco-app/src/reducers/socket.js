import initialState from '../constants/initialState';
import * as types from '../constants/types';


/**
 * The socket reducer is responsible
 * Web Socket 사용 시, 한 곳에서 메시지를 처리하기 때문에 상태 별로 구분지어서 상태 분리
 * 
 * @param {*} state 
 * @param {*} action 
 */
export function socket(state = initialState.socket, action) {
    switch (action.type) {
        case types.actionStatus.SOCKET_POST_CREATE:
            return Object.assign({}, state, {
                actionStatus: 'postsCreate'
            });
        case types.actionStatus.SOCKET_POST_EDIT:
            return Object.assign({}, state, {
                actionStatus: 'postsEdit'
            });
        case types.actionStatus.SOCKET_POST_DELETE:
            return Object.assign({}, state, {
                actionStatus: 'postsDelete'
            });
        case types.actionStatus.SOCKET_POST_LIKE_GET:
            return Object.assign({}, state, {
                actionStatus: 'postsLikeGet'
            });
        case types.actionStatus.SOCKET_POST_LIKE_INCREASE:
            return Object.assign({}, state, {
                actionStatus: 'postsLikeIncrease'
            });
        case types.actionStatus.SOCKET_POST_LIKE_DECREASE:
            return Object.assign({}, state, {
                actionStatus: 'postsLikeDecrease'
            });
        default:
            return state;
    }
}