package com.g81vdbvf.usermanager;

import android.arch.persistence.room.Room;
import android.support.v4.app.NavUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

public class ModUser extends AppCompatActivity {
    EditText name, user, pass;
    Spinner nat;
    Button save;
    Button cancel;
    User u;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mod_user);

        nat = findViewById(R.id.mod_nationality);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.nationalities, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        nat.setAdapter(adapter);

        name = findViewById(R.id.mod_nombre);
        user = findViewById(R.id.mod_user);
        pass = findViewById(R.id.mod_pass);

        u = (User) getIntent().getSerializableExtra("User");
        int index = getIntent().getIntExtra("Nat",0);


        name.setText(u.getName());
        user.setText(u.getUsername());
        pass.setText(u.getPassword());
        nat.setSelection(index);

        cancel = findViewById(R.id.mod_cancel);
        save = findViewById(R.id.mod_ok);

        cancel.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                NavUtils.navigateUpFromSameTask(ModUser.this);
            }
        });

        save.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                if(name.getText().toString().length()>0) u.setName(name.getText().toString());
                if(user.getText().toString().length()>0) u.setUsername(user.getText().toString());
                if(pass.getText().toString().length()>0) u.setPassword(pass.getText().toString());
                if(!nat.getSelectedItem().toString().equals("Nacionalidad")) u.setNationality(nat.getSelectedItem().toString().substring(0, 2));

                DatabaseInitializer.updateUser(Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "user-database").fallbackToDestructiveMigration().allowMainThreadQueries().build(), u);
                Toast.makeText(ModUser.this,"Usuario modificado con éxito",Toast.LENGTH_SHORT).show();
                NavUtils.navigateUpFromSameTask(ModUser.this);
            }
        });


    }
}
