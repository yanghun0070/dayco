import axios from 'axios';
import Cookies from "js-cookie";
import { API_BASE_URL } from '../constants';


export function joinUser(userId, email, password, confirmPassword) {
    return axios.post(API_BASE_URL + "/auth/signup",{
        headers: {
            'Content-type': 'application/json'
        },
        userId: userId,
        email: email,
        password: password
    });
}

export function loginUser(userId, password) {
    return axios.post(API_BASE_URL + "/auth/signin",{
        headers: {
            'Content-type': 'application/json'
         },
        userId : userId,
        password : password
    });
}

export function getCurrentUser() {
    const token = Cookies.get("token") ? Cookies.get("token") : null;
    return axios.get(API_BASE_URL + "/user/me", {
        headers: {
            'Content-type': 'application/json',
            'Authorization': token
         }
    });
}
