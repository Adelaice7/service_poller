import {POLLER_SCHEDULE_TIME, pollService} from "./poller";
import './alerts';
import {createAlert} from "./alerts";

const axios = require('axios');
const $ = require("jquery");

const API_URL = window.location.href;
let services = [];

/**
 * Finds element in services array based on service ID.
 * @param serviceId the ID of the service to search for
 * @returns the element with the id attr matching serviceID
 */
function getServiceFromArray(serviceId) {
    return services.find(service => service["id"] == serviceId);
}

/**
 * Removes element in services array based on service ID.
 * @param serviceId the ID of the service to be removed
 */
function removeServiceFromArray(serviceId) {
    services = services.filter((s) => s["id"] !== serviceId);
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

    getServicesOfUser(currentUserId).then((res) => {
        services = res.data;
    }).catch((err) => {
        createAlert('Error occurred getting the services data!<br/>' + err);
    });

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
                    return '<button type="button" value="' + data['id'] + '" class="btn btn-warning service-edit-btn mx-2" ' +
                        'data-bs-toggle="modal" data-bs-target="#editServiceModal" data-bs-dismiss="modal">Edit</button>' +
                        '<button type="button" value="' + data['id'] + '" class="btn btn-danger service-remove-btn ml-2 mr-0" ' +
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
        $('#addCurrServiceBtn').on('click', () => {
            let service = {
                "name": $('#serviceNameInput').val(),
                "url": $('#serviceUrlInput').val()
            }

            addServiceToUser(currentUserId, service).then((res) => {
                // adding result service to the array
                const addedService = res.data;
                services.push(addedService);

                // polling service
                pollService(service["id"]).then(() => {
                    $('#servicesTable').DataTable().ajax.reload()
                }).catch((err) => {
                    createAlert('Could not poll added service!<br/>' + err, 'error');
                }).finally(() => {
                    createAlert('Successfully added service!', 'success');
                });

            }).catch((err) => {
                createAlert('Error occurred adding service!<br/>' + err, 'error');
            });
        });
    }

    /**
     * Edit service based on ID. Processes the form in the edit service modal dialog,
     * sends a request, then shows alert afterwards.
     * @param serviceId
     */
    function editService(serviceId) {
        const oldService = getServiceFromArray(serviceId);

        $('#serviceNameEditInput').val(oldService["name"]);
        $('#serviceUrlEditInput').val(oldService["url"]);

        $('#editCurrServiceBtn').on('click', (e) => {
            // do not send default form
            e.preventDefault();

            // creating updated service
            let updatedService = {
                "id": "",
                "name": "",
                "url": ""
            };

            updatedService["id"] = oldService["id"];
            updatedService["name"] = $('#serviceNameEditInput').val();
            updatedService["url"] = $('#serviceUrlEditInput').val();

            updateService(oldService["id"], updatedService).then((res) => {
                // adding result service to the array
                let savedService = res.data;
                removeService(oldService["id"]);
                services.push(savedService);

                // polling updated service
                pollService(savedService["id"]).then(() => {
                    $('#servicesTable').DataTable().ajax.reload()
                }).catch((err) => {
                    createAlert('Could not poll added service!<br/>' + err, 'error');
                }).finally(() => {
                    createAlert('Successfully edited service!', 'success');
                });

            }).catch((err) => {
                createAlert('Error occurred editing service!<br/>' + err, 'error');
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
            // do not send default form
            e.preventDefault();

            deleteService(serviceId).then(() => {

                // removing existing service from array
                let service = getServiceFromArray(serviceId);
                removeServiceFromArray(service["id"]);

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
        }, POLLER_SCHEDULE_TIME);
    }
});
