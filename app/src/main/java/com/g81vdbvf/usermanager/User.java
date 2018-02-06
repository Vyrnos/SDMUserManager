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

    public String getGender() {
        return gender;
    }

    public void setGender(String gender) {
        this.gender = gender;
    }

    public String getRegistered() {
        return registered;
    }

    public void setRegistered(String registered) {
        this.registered = registered;
    }

    public int getUid() {

        return uid;
    }

    public void setUid(int uid) {
        this.uid = uid;
    }
}
