package com.project.back_end.DTO;

public class Login {

    private String identifier; // Can be email or username

    private String password;

    public Login() {
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

}
