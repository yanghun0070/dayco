import * as types from '../constants/types';
import * as API from '../services/http';
import * as Alert from '../actions/alert';
import Cookies from "js-cookie";

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
        userId
    };
}

/**
 * 회원가입 실패
 */
export function joinFail(message) {
    return {
        type: types.user.JOIN_FAIL,
        message
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

export function currentUserSuccess(currentUser) {
    return {
        type: types.user.CURRENT_USER_SUCCESS,
        currentUser
    }
}

export function currentUserFail() {
    return {
        type: types.user.CURRENT_USER_FAIL,
    }
}
/**
 * 로그인
 * @method login
 * @return {object}
 */
export function login(userId, password) {
    return dispatch => {
        return API.loginUser(userId, password)
            .then(async(response) => {
                var result = response.data.result
                dispatch(loginSuccess(result.username, result.token));
                dispatch(Alert.createAlert({
                    variant : 'success', 
                    message : '로그인 되셨습니다.'
                }));
            }).catch(function (error) {
                dispatch(loginFail());
                dispatch(Alert.createAlert({
                    variant : 'danger', 
                    message : '로그인 실패하였습니다.'
                }));
            })
    };
}

/**
 * 회원 가입
 * @method join
 * @param {string} userId
 * @param {String} email
 * @param {string} password
 * @param {string} confirmPassword
 */
export function join(userId, email, password, confirmPassword) {
    return dispatch => {
        return API.joinUser(userId, email, password, confirmPassword)
            .then(async(response) => {
                dispatch(joinSuccess(userId));
                dispatch(Alert.createAlert({
                    variant : 'success', 
                    message : '회원가입 되셨습니다.'
                }));
            }).catch(function (error) {
                dispatch(joinFail(error.message));
                dispatch(Alert.createAlert({
                    variant : 'danger', 
                    message : '회원가입 실패하였습니다.'
                }));
            })
    };
}

/**
 * 로그인 시에 현재 유저 정보를 조회
 */
export function getCurrentUser() {
    return dispatch => {
        const token = Cookies.get("token") ? Cookies.get("token") : null;
        if(token == null) {
            dispatch(currentUserFail());
        } else {
            return API.getCurrentUser()
            .then(async(response) => {
                dispatch(currentUserSuccess(response.data));
            }).catch(function (error) {
                dispatch(currentUserFail());
            });
        }  
    };
}

/**
 * 로그아웃 
 */
export function logout() {
    return dispatch => {
        dispatch(logoutSuccess())
    }
}