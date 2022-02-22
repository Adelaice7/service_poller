import $ from 'jquery';

const axios = require('axios');

const API_URL = window.location.href;
let services = [];

function Service(id, name, url, timestamp, status) {
    this.id = id;
    this.name = name;
    this.url = url;
    this.timestamp = timestamp;
    this.status = status;
}

$(document).ready(() => {

    function reloadServiceTable() {
        setInterval(() => {
            servicesTable.ajax.reload();
        }, 60000);
    }
    /* -------------- Render services -------------- */

    const servicesTable = $('#servicesTable').DataTable({
        ajax: {
            url: API_URL + 'users/' + 1 + '/services',
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

    reloadServiceTable();

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


    function addService() {
        $('#addCurrServiceBtn').on('click', (e) => {
            let service = {
                "name": $('#serviceNameInput').val(),
                "url": $('#serviceUrlInput').val()
            }

            addServiceToUser(1, service).then((res) => {
                $('.messages').empty().prepend('<div class="alert alert-success">Successfully added service!</div>');
            }).catch((err) => {
                $('.messages').empty().prepend('<div class="alert alert-danger">Error occurred adding service!</div>');
            }).finally(() => {
                $('#addServiceModal').modal('toggle');
                servicesTable.ajax.reload();
            });
        })
    }

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
                $('.messages').empty().prepend('<div class="alert alert-success">Successfully edited service!</div>');
            }).catch((err) => {
                $('.messages').empty().prepend('<div class="alert alert-danger">Error occurred edited service!</div>');
            }).finally(() => {
                $('#addServiceModal').modal('toggle');
                servicesTable.ajax.reload();
            });
        });
    }

    function removeService(serviceId) {
        $('#rmCurrServiceBtn').on('click', (e) => {
            deleteService(serviceId).then((response) => {
                $('.messages').empty().prepend('<div class="alert alert-success">Successfully removed service!</div>');
            }).catch((err) => {
                $('.messages').empty().prepend('<div class="alert alert-danger">Error occurred removing service!</div>');
            }).finally(() => {
                $('#addServiceModal').modal('toggle');
                servicesTable.ajax.reload();
            });
        });
    }


});

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
