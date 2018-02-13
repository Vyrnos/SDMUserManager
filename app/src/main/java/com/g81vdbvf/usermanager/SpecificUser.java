package com.g81vdbvf.usermanager;

import android.app.Activity;
import android.arch.persistence.room.Room;
import android.content.Intent;
import android.net.Uri;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

public class SpecificUser extends AppCompatActivity {

    ImageView profilePic;
    TextView nombre;
    TextView genero;
    TextView registrado;
    ImageButton localizacion;
    TextView usuario ;
    TextView password;
    Button mod;
    Button del;
    TextView nat;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_user);

        profilePic = findViewById(R.id.specific_profilePic);
        nombre = findViewById(R.id.specific_nombre);
        genero = findViewById(R.id.specific_genero);
        registrado = findViewById(R.id.specific_registradoEn);
        localizacion = findViewById(R.id.specific_location);
        usuario = findViewById(R.id.specific_user);
        password = findViewById(R.id.specific_pass);
        mod = findViewById(R.id.specific_mod);
        del = findViewById(R.id.specific_del);
        nat = findViewById(R.id.specific_nation);

        user = (User) getIntent().getSerializableExtra("User");

        new UsersAdapter.DownloadImageTask(profilePic).execute(user.getImage());
        nombre.setText(user.getName());
        genero.setText(user.getGender());
        registrado.setText(user.getRegistered());
        usuario.setText(user.getUsername());
        password.setText(user.getPassword());
        localizacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Uri uri = Uri.parse("geo:0,0?q=" + Uri.encode(user.getLocation()));
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, uri);
                intent.setPackage("com.google.android.apps.maps");
                SpecificUser.this.startActivity(intent);
            }
        });
        nat.setText(user.getNationality());


        del.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DatabaseInitializer.deleteUserAsync(Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "user-database").fallbackToDestructiveMigration().build(), user);
                Toast.makeText(SpecificUser.this,"Se ha borrado el usuario con éxito",Toast.LENGTH_LONG).show();
                NavUtils.navigateUpFromSameTask(SpecificUser.this);
            }
        });


    }
}
