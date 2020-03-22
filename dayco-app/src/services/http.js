import axios from 'axios';
import Cookie from "js-cookie";

export function joinUser(userId, password, confirmPassword) {
    return axios.post("http://localhost:8000/api/user/join",{
        headers: {
            'Content-type': 'application/json'
        },
        userId: userId,
        password: password,
        confirmPassword: confirmPassword
    });
}

export function loginUser(userId, password) {
    return axios.post("http://localhost:8000/api/user/login",{
        headers: {
            'Content-type': 'application/json'
         },
        username : userId,
        password : password
    });
}
