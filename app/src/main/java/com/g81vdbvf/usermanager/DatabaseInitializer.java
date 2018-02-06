package com.g81vdbvf.usermanager;

/**
 * Created by Vlad on 2/6/2018.
 */

import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.util.Log;

import com.g81vdbvf.usermanager.AppDatabase;
import com.g81vdbvf.usermanager.User;

import java.util.List;
import java.util.concurrent.ExecutionException;

public class DatabaseInitializer {

    private static final String TAG = DatabaseInitializer.class.getName();

    public static void populateAsync(@NonNull final AppDatabase db) {
        PopulateDbAsync task = new PopulateDbAsync(db);
        task.execute();
    }

    public static void addUserAsync(@NonNull final AppDatabase db, User... users){
        AddUserAsync task = new AddUserAsync(db);
        task.execute(users);
    }

    public static List<User> getUserList(@NonNull final AppDatabase db){
        return db.userDao().getAll();
    }

    public static void deleteAll(@NonNull final AppDatabase db){
        db.userDao().deleteAll();
    }

    private static User addUser(final AppDatabase db, User user) {
        db.userDao().insertAll(user);
        return user;
    }

    private static void populateWithTestData(AppDatabase db) {
        User user = new User();
        user.setGender("male");
        user.setRegistered("null");
        addUser(db, user);

        List<User> userList = db.userDao().getAll();
        Log.d(DatabaseInitializer.TAG, "Rows Count: " + userList.size());
    }

    private static class PopulateDbAsync extends AsyncTask<Void, Void, Void> {

        private final AppDatabase mDb;

        PopulateDbAsync(AppDatabase db) {
            mDb = db;
        }

        @Override
        protected Void doInBackground(final Void... params) {
            populateWithTestData(mDb);
            return null;
        }
    }

    private static class AddUserAsync extends AsyncTask<User, Void, Void> {

        private final AppDatabase mDb;

        AddUserAsync(AppDatabase db) {
            mDb = db;
        }

        @Override
        protected Void doInBackground(final User... user) {
            for(int i = 0; i < user.length; i++) addUser(mDb,user[i]);
            return null;
        }
    }
}