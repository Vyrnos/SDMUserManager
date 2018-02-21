package com.g81vdbvf.usermanager;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import java.util.List;

public class PantallaPresentacion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_presentacion);

        List<Login> list = DatabaseInitializer.getLoginList(Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "user-database").fallbackToDestructiveMigration().allowMainThreadQueries().build());

        if(list.isEmpty()) {
            DatabaseInitializer.initializeLogin(Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "user-database").fallbackToDestructiveMigration().allowMainThreadQueries().build());
        }else{
            for (int i = 0; i<list.size(); i++)
            Log.v("ASDASDASDASD", "LA PASS ES: " + list.get(i).getPassword() + " Y EL USER: "+ list.get(i).getUsername());
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
