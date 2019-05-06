package com.krm.form;

import lombok.Data;

import javax.validation.constraints.NotBlank;

@Data
public class CreateAccountForm {

    @NotBlank(message = "First name is required.")
    private String firstName;

    @NotBlank(message = "Last name is required.")
    private String lastName;

    @NotBlank(message = "Username is required.")
    private String username;

    @NotBlank(message = "New password is required.")
    private String newPassword;

    @NotBlank(message = "Confirm password is required.")
    private String confirmPassword;

}
