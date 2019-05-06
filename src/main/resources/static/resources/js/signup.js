function signup() {

    var signupForm = {
        firstName: $('#txtFirstName').val(),
        lastName: $('#txtLastName').val(),
        username: $('#txtUsername').val(),
        newPassword: $('#txtNewPassword').val(),
        confirmPassword: $('#txtConfirmPassword').val()
    };

    $.post("/signup", signupForm, function (response) {

        window.location.reload();

    }).fail(function (response) {

        console.log(response.responseJSON.message);

    });
}