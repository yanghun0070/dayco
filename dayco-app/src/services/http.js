import axios from 'axios';
import Cookie from "js-cookie";

export function joinUser(userId, password, confirmPassword) {
    return axios.post("http://localhost:9999/auth/signup",{
        headers: {
            'Content-type': 'application/json'
        },
        username: userId,
        password: password
    });
}

export function loginUser(userId, password) {
    return axios.post("http://localhost:9999/auth/signin",{
        headers: {
            'Content-type': 'application/json'
         },
        username : userId,
        password : password
    });
}
