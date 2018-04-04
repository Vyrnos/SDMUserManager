package com.g81vdbvf.usermanager;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

import javax.crypto.SecretKey;

public class PantallaPresentacion extends AppCompatActivity {
    SecretKey key;
    public static final String MIS_PREFERENCIAS = "com.g81vdbvf.usermanager.login";

    String getRawKey() {
        if (key == null) {
            return null;
        }
        return Crypto.toHex(key.getEncoded());
    }

    public SecretKey deriveKey(String password, byte[] salt) {
        return Crypto.deriveKeyPbkdf2(salt, password);
    }

    public String encrypt(String plaintext, String password) {
        byte[] salt = Crypto.generateSalt();
        key = deriveKey(password, salt);
        Log.d("ENCRYPT: ", "Generated key: " + getRawKey());
        return Crypto.encrypt(plaintext, key, salt);
    }
    public String decrypt(String ciphertext, String password) {
        return Crypto.decryptPbkdf2(ciphertext, password);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_presentacion);

        List<Login> list = DatabaseInitializer.getLoginList(Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "user-database").fallbackToDestructiveMigration().allowMainThreadQueries().build());

        if(list.isEmpty()) {
            DatabaseInitializer.initializeLogin(Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "user-database").fallbackToDestructiveMigration().allowMainThreadQueries().build());
            list = DatabaseInitializer.getLoginList(Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "user-database").fallbackToDestructiveMigration().allowMainThreadQueries().build());
        }else{
            for (int i = 0; i<list.size(); i++)
            Log.v("ASDASDASDASD", "LA PASS ES: " + list.get(i).getPassword() + " Y EL USER: "+ list.get(i).getUsername());
        }

        SharedPreferences sprefs = getSharedPreferences(MIS_PREFERENCIAS, Context.MODE_PRIVATE);

        if(!sprefs.contains("PassBBDD")) {
            Log.v("Proceso de CIFRADO: ", "EL PASS ES: " + list.get(0).getPassword());
            key = deriveKey(list.get(0).getPassword(), Crypto.generateSalt());
            SharedPreferences.Editor editor = sprefs.edit();
            Log.v("PASS DE LA BBDD", getRawKey());
            editor.putString("PassBBDD", getRawKey());
            editor.commit();
        }

        if(sprefs.contains("PassBBDD")){
            SQLCipherDBHelper db = SQLCipherDBHelper.getInstance(this);

            Cursor cursor = db.getAllData();
            cursor.moveToFirst();
            int i = 0;
            while (!cursor.isAfterLast()){
                Log.v("LOSUSUARIOSDE-SQLCIPHER",i + " " + cursor.getString(1));
                cursor.moveToNext();
                i++;
            }
            if (i == 0){
                db.insertData("NombreEjemplo","M", "LocationEjemplo","user123","pass123","11/11/2011","pictureEjemplo");
                db.insertData("NombreEjemplo2","F", "LocationEjemplo2","user1234","pass1234","11/11/2012","pictureEjemplo2");
                db.insertData("NombreEjemplo3","F", "LocationEjemplo3","user1235","pass1235","11/11/2013","pictureEjemplo3");
                db.insertData("NombreEjemplo4","M", "LocationEjemplo4","user1236","pass1236","11/11/2014","pictureEjemplo4");
                db.insertData("NombreEjemplo5","M", "LocationEjemplo5","user1237","pass1237","11/11/2015","pictureEjemplo5");
            }
            cursor.close();
        }



        Thread reloj = new Thread(){
            public void run(){
                try {
                    sleep(3000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally {
                    Intent abrirPrincipal = new Intent("android.intent.action.LOGIN");
                    startActivity(abrirPrincipal);
                }
            }

        };
        reloj.start();
    }
}
