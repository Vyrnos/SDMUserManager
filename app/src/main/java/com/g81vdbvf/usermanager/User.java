package com.g81vdbvf.usermanager;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

@Entity(tableName = "user")
public class User {
    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name= "name")
    private String name;

    @ColumnInfo(name = "image")
    private String image;

    @ColumnInfo(name = "location")
    private String location;

    @ColumnInfo(name = "username")
    private String username;

    @ColumnInfo(name = "password")
    private String password;

    @ColumnInfo(name = "gender")
    private String gender;

    @ColumnInfo(name = "registered")
    private String registered;

    @ColumnInfo(name= "nationality")
    private String nationality;

    public User(){

    }

    public User(String name, String image, String location, String username, String password, String gender, String registered, String nationality) {
        this.name = name;
        this.image = image;
        this.location = location;
        this.username = username;
        this.password = password;
        this.gender = gender;
        this.registered = registered;
        this.nationality = nationality;
    }

    String getNationality() {
        return nationality;
    }

    void setNationality(String nationality) {
        this.nationality = nationality;
    }

    String getGender() {
        return gender;
    }

    void setGender(String gender) {
        this.gender = gender;
    }

    String getRegistered() {
        return registered;
    }

    void setRegistered(String registered) {
        this.registered = registered;
    }

    int getUid() {

        return uid;
    }

    void setUid(int uid) {
        this.uid = uid;
    }

    String getName() {
        return name;
    }

    void setName(String name) {
        this.name = name;
    }

    String getImage() {
        return image;
    }

    void setImage(String image) {
        this.image = image;
    }

    String getLocation() {
        return location;
    }

    void setLocation(String location) {
        this.location = location;
    }

    String getUsername() {
        return username;
    }

    void setUsername(String username) {
        this.username = username;
    }

    String getPassword() {
        return password;
    }

    void setPassword(String password) {
        this.password = password;
    }

    @Override
    public String toString() {
        return "User{" +
                "uid=" + uid +
                ", name='" + name + '\'' +
                ", image=" + image +
                ", location='" + location + '\'' +
                ", username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", gender='" + gender + '\'' +
                ", registered='" + registered + '\'' +
                ", nationality='" + nationality + '\'' +
                '}';
    }
}
