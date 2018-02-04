package com.g81vdbvf.usermanager;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class InsertUser extends AppCompatActivity {

    Button insertCallAPI;
    EditText nacionality, numUsers, regDate;
    CheckBox male, female;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_user);

        nacionality = findViewById(R.id.nacionality);
        numUsers = findViewById(R.id.numUsers);
        regDate = findViewById(R.id.regDate);

        male = findViewById(R.id.checkMale);
        female = findViewById(R.id.checkFemale);

        StringBuilder sb = new StringBuilder("https://randomuser.me/api/?inc=gender,registered,nat");
        if(nacionality.getText().length()>0) sb.append("&nat="+nacionality.getText());
        if(numUsers.getText().length()>0) sb.append("&results="+nacionality.getText());
        if(regDate.getText().length()>0) sb.append("&registered="+nacionality.getText());

        insertCallAPI = (Button) findViewById(R.id.insertCallAPI);
        insertCallAPI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    URL url = new URL("https://randomuser.me/api/");

                    HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
                    try {
                        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream()));
                        StringBuilder stringBuilder = new StringBuilder();
                        String line;
                        while ((line = bufferedReader.readLine()) != null) {
                            stringBuilder.append(line).append("\n");
                        }
                        bufferedReader.close();

                    }
                    finally{
                        urlConnection.disconnect();
                    }
                }
                catch(Exception e) {
                    Log.e("ERROR", e.getMessage(), e);
                }
            }
        });

    }
}
