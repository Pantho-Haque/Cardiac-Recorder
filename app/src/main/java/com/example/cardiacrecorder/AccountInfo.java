package com.example.cardiacrecorder;

public class AccountInfo {
    String name,email;

    public AccountInfo() {
    }

    public AccountInfo(String name, String email) {
        this.name = name;
        this.email = email;

    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

}
