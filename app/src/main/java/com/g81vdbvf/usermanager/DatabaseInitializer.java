package com.g81vdbvf.usermanager;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.util.List;

class DatabaseInitializer {

    static void addUserAsync(@NonNull final AppDatabase db, User... users){
        AddUserAsync task = new AddUserAsync(db);
        task.execute(users);
    }

    static List<User> getUserList(@NonNull final AppDatabase db){
        return db.userDao().getAll();
    }

    static void deleteAll(@NonNull final AppDatabase db){
        db.userDao().deleteAll();
    }

    private static void addUser(final AppDatabase db, User user) {
        db.userDao().insertAll(user);
    }

    static void deleteUser(final AppDatabase db, User user) {
        db.userDao().delete(user);
    }

    static void updateUser(final AppDatabase db, User user) {
        db.userDao().update(user);
    }

    private static class AddUserAsync extends AsyncTask<User, Void, Void> {

        private final AppDatabase mDb;

        AddUserAsync(AppDatabase db) {
            mDb = db;
        }

        @Override
        protected Void doInBackground(final User... user) {
            for (User anUser : user) addUser(mDb, anUser);
            return null;
        }
    }

}
