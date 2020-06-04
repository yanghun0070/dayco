import Cookies from 'js-cookie';
import initialState from '../constants/initialState';
import * as types from '../constants/types';

/**
 * The user reducer is responsible
 * @method user
 * @module letters/reducers
 * @param  {object} [state=initialState.user] state
 * @param  {object} action                    redux action
 * @return {object}                           next state
 */
export function user(state = initialState.user, action) {
    switch (action.type) {
        case types.user.LOGIN_SUCCESS:
            const { username, token } = action;
            Cookies.set("token", "Bearer " + token);
            return Object.assign({}, state, {
                authenticated: true,
                logon: true,
                token: token
            });
        case types.user.LOGOUT_SUCCESS:
            Cookies.remove("token");
            return initialState.user;
        case  types.user.JOIN_SUCCESS:
            return Object.assign({}, state, {
                authenticated: true,
                joined: true,
                logon: false,
                id: action.userId
            });
        case types.user.JOIN_FAIL:
            const { message } = action;
            return Object.assign({}, state, {
                authenticated: false,
                joined: false,
                message: message
            });
        case types.user.LOGIN_FAIL:
            return Object.assign({}, state, {
                authenticated: true,
                logon: false
            });
        case types.user.CURRENT_USER_SUCCESS:
            const { currentUser } = action;
            return Object.assign({}, state, {
                authenticated: true,
                logon: true,
                currentUser: currentUser
            });
        default:
            return state;
    }
}
