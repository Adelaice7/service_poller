import axios from 'axios';

document.addEventListener("DOMContentLoaded", function (event) {
    const USERS_REST_API_ENDPOINT = window.location.href + '/users';

    function getUsers() {
        return axios.get(USERS_REST_API_ENDPOINT);
    }

    function getUser(id) {
        return axios.get(USERS_REST_API_ENDPOINT + '/' + id);
    }

    function addUser(user) {
        return axios.post(USERS_REST_API_ENDPOINT + '/add', {
            user: user
        });
    }

    function updateUser(userId, updatedUser) {
        return axios.put(USERS_REST_API_ENDPOINT + '/' + userId, {
            user: updatedUser
        });
    }

    function deleteUser(id) {
        return axios.delete(USERS_REST_API_ENDPOINT + '/' + id);
    }

    function deleteAllUsers() {
        return axios.delete(USERS_REST_API_ENDPOINT + '/deleteAll');
    }
});
