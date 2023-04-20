package com.example.diplomproject;

public class UserData {

    private String name;
    private String login;
    private String email;
    private String password;
    //private String acceptPassword;

    UserData(){
        this.name = "";
        this.login = "";
        this.email = "";
        this.password = "";
        //this.acceptPassword = "";
    }

    UserData(String name, String login, String email, String password){
        this.name = name;
        this.login = login;
        this.email = email;
        this.password = password;
    }

    UserData(String name, String login, String email, String password, String acceptPassword){

    }

    public void SetName(String name){
        this.name = name;
    }

    public String GetName(){
        return this.name;
    }

    public void SetLogin(String login){
        this.login = login;
    }

    public String GetLogin(){
        return this.login;
    }

    public void SetEmail(String email){
        this.email = email;
    }

    public String GetEmail(){
        return this.email;
    }

    public void SetPassword(String password){
        this.password = password;
    }

    public String GetPassword(){
        return this.password;
    }

    public static void EqualsPassword(String password, String acceptPassword) {

    }

    public static void CheckPassword(String password) {

    }
}
