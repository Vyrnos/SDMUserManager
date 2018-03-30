package com.g81vdbvf.usermanager;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
        Log.v("Proceso de CIFRADO: ", "EL PASS ES: " + list.get(0).getPassword());
        key = deriveKey(list.get(0).getPassword(), Crypto.generateSalt());

        SharedPreferences sprefs = getSharedPreferences(MIS_PREFERENCIAS, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sprefs.edit();

        editor.putString("PassBBDD", key.toString());

        //HACER OPERACIONES CON BBDD de SQLCipherDBHElper aqui!!!


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
