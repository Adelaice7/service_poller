// import {createAlert} from "./alerts";
// import {pollService} from "./poller";

import {pollService} from "./poller";
import './alerts';
import {createAlert} from "./alerts";

const axios = require('axios');
const $ = require("jquery");

const API_URL = window.location.href;
let services = [...new Set()];

function Service(id, name, url, timestamp, status) {
    this.id = id;
    this.name = name;
    this.url = url;
    this.timestamp = timestamp;
    this.status = status;
}

/* Requests to the REST API */
function getServicesOfUser(userId) {
    return axios.get(API_URL + 'users/' + userId + '/services');
}

function getServiceById(id) {
    return axios.get(API_URL + 'users/services/' + id);
}

function addServiceToUser(userId, service) {
    return axios.post(API_URL + 'users/' + userId + '/services/add', service);
}

function updateService(serviceId, updatedService) {
    return axios.put(API_URL + 'users/services/' + serviceId, updatedService);
}

function deleteService(serviceId) {
    return axios.delete(API_URL + 'users/services/' + serviceId);
}

/* -------------- Render services -------------- */

$(document).ready(() => {
    if (currentUserId === null || currentUserId === undefined) {
        console.error('The user ID does not exist!');
    }

    /* Using an ajax request, it queries the services belonging to current user based on user ID,
    * then shows them in a table. */
    const servicesTable = $('#servicesTable').DataTable({
        ajax: {
            url: API_URL + 'users/' + currentUserId + '/services',
            dataSrc: ""
        },
        columns: [
            {data: 'name'},
            {data: 'url'},
            {data: 'timestamp'},
            {data: 'status'},
            {
                data: null,
                render: (data) => {
                    const service = new Service(data['id'], data['name'], data['url'], data['timestamp'], data['status']);
                    services[data['id']] = service;
                    return '<button type="button" value="' + service.id + '" class="btn btn-warning service-edit-btn mx-2" ' +
                        'data-bs-toggle="modal" data-bs-target="#editServiceModal" data-bs-dismiss="modal">Edit</button>' +
                        '<button type="button" value="' + service.id + '" class="btn btn-danger service-remove-btn ml-2 mr-0" ' +
                        'data-bs-toggle="modal" data-bs-target="#removeServiceModal" data-bs-dismiss="modal">Remove</button>'
                }
            },
        ],
        "bLengthChange": false,
        "bInfo": false,
        "bPaginate": false
    });

    /* Reload services table */
    reloadServiceTable();

    /* -------------------- on click events on buttons -------------------- */
    const servicesTableBody = $('#servicesTable tbody');

    servicesTableBody.on('click', '.service-edit-btn', function () {
        const serviceId = this.value;
        editService(serviceId);
    });

    servicesTableBody.on('click', '.service-remove-btn', function () {
        const serviceId = this.value;
        removeService(serviceId);
    });

    $('#addBtn').on('click', () => {
        addService();
    });

    /**
     * Adds new service. Processes the form in the add service modal dialog,
     * sends a request with the data, and then shows alert afterwards.
     */
    function addService() {
        $('#addCurrServiceBtn').on('click', (e) => {
            let service = {
                "name": $('#serviceNameInput').val(),
                "url": $('#serviceUrlInput').val()
            }

            addServiceToUser(currentUserId, service).then((res) => {
                createAlert('Successfully added service!', 'success');
                pollService(service.id).then((res) => {
                    $('#servicesTable').DataTable().ajax.reload()
                }).catch((err) => {
                    createAlert('Could not poll added service!<br/>' + err, 'error');
                });
            }).catch((err) => {
                createAlert('Error occurred adding service!<br/>' + err, 'error');
                $('#servicesTable').DataTable().ajax.reload()
            });
        });
    }

    /**
     * Edit service based on ID. Processes the form in the edit service modal dialog,
     * sends a request, then shows alert afterwards.
     * @param serviceId
     */
    function editService(serviceId) {
        const oldService = services[serviceId];

        $('#serviceNameEditInput').val(oldService.name);
        $('#serviceUrlEditInput').val(oldService.url);

        $('#editCurrServiceBtn').on('click', (e) => {
            e.preventDefault();
            $('#editServiceForm').serialize();

            let updatedService = {
                'id': oldService.id,
                'name': $('#serviceNameEditInput').val(),
                'url': $('#serviceUrlEditInput').val()
            };

            updateService(oldService.id, updatedService).then((resp) => {
                createAlert('Successfully edited service!', 'success');
            }).catch((err) => {
                createAlert('Error occurred editing service!<br/>' + err, 'error');
            }).finally(() => {
                $('#servicesTable').DataTable().ajax.reload();
            });
        });
    }

    /**
     * Deletes a service upon clicking the remove button,
     * sends a request, then shows alert afterwards.
     * @param serviceId the ID of the service to remove.
     */
    function removeService(serviceId) {
        $('#rmCurrServiceBtn').on('click', (e) => {
            deleteService(serviceId).then((response) => {
                // remove from array
                services.filter(service => service !== serviceId);
                createAlert('Successfully removed service!', 'success');
            }).catch((err) => {
                createAlert('Error occurred removing service!<br/>' + err, 'error');
            }).finally(() => {
                $('#servicesTable').DataTable().ajax.reload();
            });
        });
    }

    /**
     * Reloads the table containing the services to query the data using an AJAX call,
     * every 2 minutes.
     */
    function reloadServiceTable() {
        setInterval(() => {
            $('#servicesTable').DataTable().ajax.reload();
        }, 60000);
    }
});
