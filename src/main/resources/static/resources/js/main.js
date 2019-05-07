var WEB_SOCKET;

var MESSAGE_TYPE = Object.freeze(
    {
        NORMAL: "NORMAL",
        WELCOME: "WELCOME",
        SENT_NOTIFICATION: "SENT_NOTIFICATION",
        RECEIVED_NOTIFICATION: "RECEIVED_NOTIFICATION",
        READ_NOTIFICATION: "READ_NOTIFICATION"
    }
);

var MESSAGE_CONSTANT = Object.freeze(
    {
        MESSAGE_ID: "MESSAGE_ID",
        RECEIVER: "RECEIVER",
        MESSAGE_TEXT: "MESSAGE_TEXT",
        SENDER: "SENDER",
        SENDER_ID: "SENDER_ID",
        SENDER_NAME: "SENDER_NAME",
        MESSAGE_DATE: "MESSAGE_DATE"
    }
);

function appendToConsole(message) {
    $('#txtConsole').append(message + "\n");
}

$(document).ready(function () {
    WEB_SOCKET = new WebSocket('ws://localhost:8080/stream');

    WEB_SOCKET.onmessage = function (messagePacket) {
        messagePacket = JSON.parse(messagePacket.data);

        var message = messagePacket.message;

        switch (messagePacket.type) {
            case MESSAGE_TYPE.WELCOME:
                handleWelcomeMessage(message);
                break;

            case MESSAGE_TYPE.NORMAL:
                handleNormalMessage(message);
                break;

            case MESSAGE_TYPE.RECEIVED_NOTIFICATION:
                handleReceivedNotification(message);
                break;

            case MESSAGE_TYPE.SENT_NOTIFICATION:
                handleSentNotification(message);
                break;
        }
    }

    WEB_SOCKET.onopen = function (data) {
        appendToConsole("Connection open!");
    }
});

function handleReceivedNotification(message) {
    appendToConsole("Message received notification: " + JSON.stringify(message, 0, 4));
}

function handleSentNotification(message) {
    appendToConsole("Message sent notification: " + JSON.stringify(message, 0, 4));
}

function handleNormalMessage(messageData) {
    appendToConsole("Message receive: " + JSON.stringify(messageData, 0, 4));

    sendNotification(MESSAGE_TYPE.RECEIVED_NOTIFICATION, messageData);
}

function handleWelcomeMessage(message) {
    $('#txtUser').text(message.fullName + " (" + message.id + ")");
    appendToConsole("Welcome message:\n" + JSON.stringify(message, 0, 4));
}

function sendNotification(notificationType, messageData) {

    var message = {};
    message[MESSAGE_CONSTANT.MESSAGE_ID] = messageData.MESSAGE_ID;
    message[MESSAGE_CONSTANT.RECEIVER] = messageData.SENDER;

    var messagePacket = {
        "type": notificationType,
        "message": message
    }

    WEB_SOCKET.send(JSON.stringify(messagePacket));
}

function sendNormalMessage() {

    var message = {};
    message[MESSAGE_CONSTANT.MESSAGE_ID] = uniqueId();
    message[MESSAGE_CONSTANT.RECEIVER] = $('#txtFriendsUsername').val();
    message[MESSAGE_CONSTANT.MESSAGE_TEXT] = $('#txtMessage').val();

    var messagePacket = {
        "type": MESSAGE_TYPE.NORMAL,
        "message": message
    }

    WEB_SOCKET.send(JSON.stringify(messagePacket));
}

function sendFriendRequest() {

    var username = $('#txtFriendsUsername').val();

    $.post("/send-request/" + username, function (response) {

        console.log(response);

    }).fail(function (response) {

        console.log(response.responseJSON.message);

    });

}

function uniqueId() {
    return ([1e7] + 1e3 + 4e3 + 8e3 + 1e11).replace(/[018]/g, function (c) {
        return (c ^ crypto.getRandomValues(new Uint8Array(1))[0] & 15 >> c / 4).toString(16);
    });
}

function logout() {
    window.location = "/signout";
}