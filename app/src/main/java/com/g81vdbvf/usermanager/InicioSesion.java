package com.g81vdbvf.usermanager;

import android.arch.persistence.room.Room;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.provider.ContactsContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.util.List;

public class InicioSesion extends AppCompatActivity {

    EditText username, password;
    SharedPreferences sprefs;
    public static final String MIS_PREFERENCIAS = "com.g81vdbvf.usermanager.login";
    public static final String NOMBRE = "Username";
    public static final String CONTRA = "Password";
    boolean usedPreferences = false;

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
            usedPreferences = true;
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

}
