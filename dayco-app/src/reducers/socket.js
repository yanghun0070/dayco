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
        case types.socket.SOCKET_USED:
            return Object.assign({}, state, {
                isSocket: true
            });
        case types.socket.SOCKET_NOT_USED:
            return Object.assign({}, state, {
                isSocket: false
            });
        default:
            return state;
    }
}