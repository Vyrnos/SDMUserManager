package com.g81vdbvf.usermanager;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

public class PantallaPrincipal extends AppCompatActivity {

    Button list, insert;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pantalla_principal);

        list=(Button) findViewById(R.id.buttonList);
        insert=(Button) findViewById(R.id.buttonInsert);

        insert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent abrirInsert = new Intent("android.intent.action.INSERTUSER");
                startActivity(abrirInsert);
            }
        });

        list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent abrirList = new Intent("android.intent.action.LISTUSER");
                startActivity(abrirList);
            }
        });
    }
}
