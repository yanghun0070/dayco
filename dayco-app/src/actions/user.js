import * as types from '../constants/types';
import * as API from '../services/http';

/**
 * 로그인 성공
 * @method loginSuccess
 * @param  {string}     userId  userId
 * @param  {string}     token JWT token
 * @return {object}
 */
export function loginSuccess(userId, token) {
    return {
        type: types.user.LOGIN_SUCCESS,
        userId,
        token
    };
}

/**
 * 로그아웃 성공
 * @method logoutSuccess
 * @return {object}
 */
export function logoutSuccess() {
    return {
        type: types.user.LOGOUT_SUCCESS
    };
}

/**
 * 회원가입 성공
 * @param {userId} userId
 */
export function joinSuccess(userId) {
    return {
        type: types.user.JOIN_SUCCESS,
        userId,

    };
}

/**
 * 회원가입 실패
 */
export function joinFail() {
    return {
        type: types.user.JOIN_FAIL
    };
}

/**
 * 로그인 실패
 */
export function loginFail() {
    return {
        type: types.user.LOGIN_FAIL
    };
}

/**
 * 로그인
 * @method login
 * @return {object}
 */
export function login(userId, password) {
    return dispatch => {
        return  API.loginUser(userId, password)
            .then(async(response) => {
                dispatch(loginSuccess(response.data.userId, response.data.token));
            }).catch(function (error) {
                dispatch(loginFail());
            })
    };
}

/**
 * 회원 가입
 * @method join
 * @param {string} userId
 * @param {string} password
 * @param {string} confirmPassword
 */
export function join(userId, password, confirmPassword) {
    return dispatch => {
        return API.joinUser(userId, password, confirmPassword)
            .then(async() => {
                dispatch(joinSuccess(userId));
            }).catch(function (error) {
                dispatch(joinFail());
            })
    };
}
