const $ = require("jquery");

export function createAlert(message, type) {
    let messagesBox = $('.messages');
    messagesBox.empty();

    if (type === 'success') {
        messagesBox.prepend('<div class="alert alert-success">' + message + '</div>')
    } else if (type === 'error') {
        messagesBox.prepend('<div class="alert alert-danger">' + message + '</div>')
    }

    $('.alert').fadeTo(2000, 500).slideUp(500);
}
