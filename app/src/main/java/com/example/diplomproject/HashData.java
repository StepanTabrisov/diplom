package com.example.diplomproject;

public class HashData {
    private String hashLogin;
    private String hashPassword;

    HashData(UserData data){

    }

    public String GetHashLogin() {
        return this.hashLogin;
    }

    public String GetHashPassword() {
        return this.hashPassword;
    }
}
