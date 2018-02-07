package com.g81vdbvf.usermanager;

import android.arch.persistence.room.ColumnInfo;
import android.arch.persistence.room.Entity;
import android.arch.persistence.room.PrimaryKey;

/**
 * Created by Vlad on 2/6/2018.
 */

@Entity(tableName = "user")
public class User {
    @PrimaryKey(autoGenerate = true)
    private int uid;

    @ColumnInfo(name = "gender")
    private String gender;

    @ColumnInfo(name = "registered")
    private String registered;

    @ColumnInfo(name= "nationality")
    private String nationality;

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

    @Override
    public String toString() {
        return  "ID: " + uid +
                "\nGender: " + gender +
                "\nRegistered: " + registered +
                "\nNationality: " + nationality;
    }
}
