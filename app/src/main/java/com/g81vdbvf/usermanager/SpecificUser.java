package com.g81vdbvf.usermanager;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

public class SpecificUser extends AppCompatActivity {

    ImageView profilePic = findViewById(R.id.specific_profilePic);
    TextView nombre = findViewById(R.id.specific_nombre);
    TextView genero = findViewById(R.id.specific_genero);
    TextView registrado = findViewById(R.id.specific_registradoEn);
    ImageButton localizacion = findViewById(R.id.specific_location);
    TextView usuario = findViewById(R.id.specific_user);
    TextView password = findViewById(R.id.specific_pass);
    Button mod = findViewById(R.id.specific_mod);
    Button del = findViewById(R.id.specific_del);
    TextView nat = findViewById(R.id.specific_nation);
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_specific_user);

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
                String uri = "http://maps.google.com/maps?saddr=" + user.getLocation();
                Intent intent = new Intent(android.content.Intent.ACTION_VIEW, Uri.parse(uri));
                intent.setClassName("com.google.android.apps.maps", "com.google.android.maps.MapsActivity");
                startActivity(intent);
            }
        });
        nat.setText(user.getNationality());


        del.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DatabaseInitializer.deleteUserAsync(Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "user-database").fallbackToDestructiveMigration().build(), user);
            }
        });


    }
}
