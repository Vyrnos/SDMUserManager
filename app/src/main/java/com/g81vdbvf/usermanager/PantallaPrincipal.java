package com.g81vdbvf.usermanager;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

public class PantallaPrincipal extends BaseActivity {
    Button list, insert, borrar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        list= findViewById(R.id.buttonList);
        insert= findViewById(R.id.buttonInsert);
        borrar = findViewById(R.id.buttonBorrar);

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

        borrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseInitializer.deleteAll(Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "user-database").allowMainThreadQueries().build());
                Toast.makeText(PantallaPrincipal.this ,"Usuarios borrados con éxito",Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    int getContentViewId() {
        return R.layout.activity_pantalla_principal;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.navigation_home;
    }
}
