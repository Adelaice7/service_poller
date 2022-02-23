import {createAlert} from "./alerts";

const axios = require('axios');
const $ = require("jquery");

const API_URL = window.location.href;

export function pollAllServices() {
    return axios.get(API_URL + 'pollServices')
        .then(() => {
            $('#servicesTable').DataTable().ajax.reload();
        }).catch((err) => {
            createAlert('Could not poll services<br/>' + err)
        }).finally(() => {
            $('#servicesTable').DataTable().ajax.reload();
        });
}

export function pollService(serviceId) {
    return axios.post(API_URL + 'pollService/' + serviceId);
}

/**
 * Polls
 */
$(document).ready(() => {
    pollAllServices();
    setInterval(pollAllServices, 60000);
});
