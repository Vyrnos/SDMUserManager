package com.g81vdbvf.usermanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

public class PantallaPresentacion extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_presentacion);

        Thread reloj = new Thread(){
            public void run(){
                try {
                    sleep(3000);
                }catch(InterruptedException e){
                    e.printStackTrace();
                }finally {
                    Intent abrirPrincipal = new Intent("android.intent.action.PRINCIPAL");
                    startActivity(abrirPrincipal);
                }
            }

        };
        reloj.start();
    }
}
