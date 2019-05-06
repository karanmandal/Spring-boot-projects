function login() {

    var loginForm = {
        "username": $('#txtUsername').val(),
        "password": $('#txtPassword').val()
    };

    $.post("/signin", loginForm, function (response) {

        window.location.reload();

    }).fail(function (response) {

        console.log(response.responseJSON.message);

    });
}