package com.g81vdbvf.usermanager;

import android.os.AsyncTask;
import android.support.annotation.NonNull;

import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.List;

class DatabaseInitializer {

    static void addUserAsync(@NonNull final AppDatabase db, User... users){
        AddUserAsync task = new AddUserAsync(db);
        task.execute(users);
    }

    static List<User> getUserList(@NonNull final AppDatabase db){
        return db.userDao().getAll();
    }

    static List<Login> getLoginList(@NonNull final AppDatabase db){
        return db.userDao().getAllLogin();
    }


    private static void addLogin(@NonNull final AppDatabase db, Login login) {
        db.userDao().insertAll(login);
    }

    static void initializeLogin(@NonNull final AppDatabase db){
        Login cred = new Login();
        cred.setUsername("asd");
        cred.setPassword("A665A45920422F9D417E4867EFDC4FB8A04A1F3FFF1FA07E998E86F7F7A27AE3"); //la pass es 123
        addLogin(db, cred);
    }

    static Login getCredentials(@NonNull final AppDatabase db, String user, String password){
        return db.userDao().findByUserAndPassword(user,password);
    }

    static void deleteAllLogin(@NonNull final AppDatabase db){
        db.userDao().deleteAllLogin();
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

    static byte[] getHash(String password) {
        MessageDigest digest=null;
        try {
            digest = MessageDigest.getInstance("SHA-256");
        } catch (NoSuchAlgorithmException e1) {
            e1.printStackTrace();
        }
        digest.reset();
        return digest.digest(password.getBytes());
    }
    static String bin2hex(byte[] data) {
        return String.format("%0" + (data.length*2) + "X", new BigInteger(1, data));
    }

}
