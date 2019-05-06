var webSocket;

$(document).ready(function () {
    webSocket = new WebSocket('ws://localhost:8080/stream');

    webSocket.onmessage = function (data) {
        console.log(data);
    }

    webSocket.onopen = function (data) {
        console.log("Connection open");
        console.log(data);
    }
});

function receiveError(data) {
}

function sendMessage() {

    var message = {
        "message": $('#txtMessage').val(),
        "receiver": $('#txtFriendsUsername').val()
    }

    webSocket.send(JSON.stringify(message));
}

function sendFriendRequest() {

    var username = $('#txtFriendsUsername').val();

    $.post("/send-request/" + username, function (response) {

        console.log(response);

    }).fail(function (response) {

        console.log(response.responseJSON.message);

    });

}

function logout() {
    window.location = "/signout";
}