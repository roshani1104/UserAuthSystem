package com.auth.dto;

public class RegisterRequest {

    private final String firstName;
    private final String lastName;
    private final String email;
    private final String password;
    private final String confirmPassword;

    public RegisterRequest(String firstName, String lastName,
                           String email, String password, String confirmPassword) {
        this.firstName       = firstName;
        this.lastName        = lastName;
        this.email           = email;
        this.password        = password;
        this.confirmPassword = confirmPassword;
    }

    public String getFirstName()       { return firstName; }
    public String getLastName()        { return lastName; }
    public String getEmail()           { return email; }
    public String getPassword()        { return password; }
    public String getConfirmPassword() { return confirmPassword; }
}
