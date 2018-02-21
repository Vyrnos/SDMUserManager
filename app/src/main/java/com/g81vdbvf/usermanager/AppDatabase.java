package com.g81vdbvf.usermanager;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.RoomDatabase;

@Database(entities = {User.class, Login.class}, version = 5)
public abstract class AppDatabase extends RoomDatabase {

    public abstract UserDao userDao();

}