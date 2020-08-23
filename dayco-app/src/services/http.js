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

export function createPosts(title, content) {
    const token = Cookies.get("token") ? Cookies.get("token") : null;
    return axios.post(API_BASE_URL + "/posts", {
        title: title,
        content: content
    }, {
        headers: {
            'Content-type': 'application/json',
            'Authorization': token
        }
    });
}

export function editPosts(id, title, content) {
    const token = Cookies.get("token") ? Cookies.get("token") : null;
    return axios.put(API_BASE_URL + "/posts", {
         id: id,
         title: title,
         content: content
    }, {
        headers: {
            'Content-type': 'application/json',
            'Authorization': token
         }
    });
}

export function getPosts(id) {
    const token = Cookies.get("token") ? Cookies.get("token") : null;
    return axios.get(API_BASE_URL + "/posts", {
        headers: {
            'Content-type': 'application/json',
            'Authorization': token
         },
         params: {
             id: id
         }
    })
}

export function getAllPosts() {
    const token = Cookies.get("token") ? Cookies.get("token") : null;
    return axios.get(API_BASE_URL + "/posts/all", {
        headers: {
            'Content-type': 'application/json',
            'Authorization': token
         }
    })
}