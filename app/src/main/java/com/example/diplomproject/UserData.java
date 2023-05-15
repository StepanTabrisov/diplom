package com.example.diplomproject;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import java.util.Objects;

public class UserData implements Parcelable {

    private int id;
    private String name;
    private String login;
    private String email;
    private String password;

    UserData(){
        this.name = "";
        this.login = "";
        this.email = "";
        this.password = "";
    }

    protected UserData(Parcel in) {
        id = in.readInt();
        name = in.readString();
        login = in.readString();
        email = in.readString();
        password = in.readString();
    }

    public static final Creator<UserData> CREATOR = new Creator<UserData>() {
        @Override
        public UserData createFromParcel(Parcel in) {
            String pName = in.readString();
            String pLogin = in.readString();
            String pEmail = in.readString();
            String pPassword = in.readString();
            return new UserData(pName, pLogin, pEmail, pPassword);
        }

        @Override
        public UserData[] newArray(int size) {
            return new UserData[size];
        }
    };

    @Override
    public String toString() {
        return "UserData{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", login='" + login + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                '}';
    }

    UserData(String name, String login, String email, String password){
        this.name = name;
        this.login = login;
        this.email = email;
        this.password = password;
    }

    UserData(String name, String login, String email, String password, String acceptPassword){

    }

    public int getId() {
        return this.id;
    }

    public void setId(int id) {
        this.id = id;
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

    public static boolean EqualsPassword(String password, String acceptPassword) {
        if(Objects.equals(password, acceptPassword)) return true;
        return false;
    }

    public static void CheckPassword(String password) {

    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(this.name);
        dest.writeString(this.login);
        dest.writeString(this.email);
        dest.writeString(this.password);
    }
}
