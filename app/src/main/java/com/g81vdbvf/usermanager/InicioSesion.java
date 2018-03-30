package com.g81vdbvf.usermanager;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

import javax.crypto.SecretKey;

public class InicioSesion extends AppCompatActivity {

    private static final String TAG = "INICIO SESION:";

    EditText username, password;
    SharedPreferences sprefs;
    public static final String MIS_PREFERENCIAS = "com.g81vdbvf.usermanager.login";
    public static final String NOMBRE = "Username";
    public static final String CONTRA = "Password";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inicio_sesion);

        username = findViewById(R.id.log_usr);
        password = findViewById(R.id.log_pass);
        sprefs = getSharedPreferences(MIS_PREFERENCIAS, Context.MODE_PRIVATE);


        if(sprefs.contains(NOMBRE)){
            username.setText(sprefs.getString(NOMBRE,""));
        }
        if(sprefs.contains(CONTRA)){
            password.setText(sprefs.getString(CONTRA,""));
        }
    }

    public void login(View view) {
        List<Login> list = DatabaseInitializer.getLoginList(Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "user-database").fallbackToDestructiveMigration().allowMainThreadQueries().build());

        String usr = username.getText().toString();
        String pass = password.getText().toString();



        String hashed = DatabaseInitializer.bin2hex(DatabaseInitializer.getHash(pass));
        SharedPreferences.Editor editor = sprefs.edit();

        if(usr.equals(list.get(0).getUsername()) && pass.equals(sprefs.getString(CONTRA,""))){
            editor.putString(NOMBRE,usr);
            editor.putString(CONTRA,pass);
            editor.commit();

            Intent intent = new Intent("android.intent.action.PRINCIPAL");
            startActivity(intent);
            Toast.makeText(InicioSesion.this,"¡ Has entrado mediante Shared Preference !",Toast.LENGTH_LONG).show();
        }else{
            if(usr.equals(list.get(0).getUsername()) && hashed.equals(list.get(0).getPassword())){
                editor.putString(NOMBRE,usr);
                editor.putString(CONTRA,hashed);
                editor.commit();

                Intent intent = new Intent("android.intent.action.PRINCIPAL");
                startActivity(intent);
                Toast.makeText(InicioSesion.this,"¡ Bienvenido " + usr + " !",Toast.LENGTH_LONG).show();
            }else{
                username.setError("Usuario o contraseña incorrectos");
                password.setError("Usuario o contraseña incorrectos");
                Toast.makeText(InicioSesion.this,"Usuario o contraseña incorrectos",Toast.LENGTH_SHORT).show();
            }
        }
    }

    /* ESTO NO SABEMOS AHORA MISMO DONDE VA (PAG 9 de 18 en PDF de ejercicios M3) */
    SecretKey key;
    String
    getRawKey() {
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
        Log.d(TAG, "Generated key: " + getRawKey());
        return Crypto.encrypt(plaintext, key, salt);
    }
    public String decrypt(String ciphertext, String password) {
        return Crypto.decryptPbkdf2(ciphertext, password);
    }

}
