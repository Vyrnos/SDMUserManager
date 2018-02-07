package com.g81vdbvf.usermanager;

import android.annotation.SuppressLint;
import android.arch.persistence.room.Room;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class InsertUser extends AppCompatActivity {

    Button insertCallAPI;
    EditText nationality, numUsers, regDate;
    CheckBox male, female;
    int inserted;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_user);

        nationality = findViewById(R.id.nacionality);
        numUsers = findViewById(R.id.numUsers);
        regDate = findViewById(R.id.regDate);

        male = findViewById(R.id.checkMale);
        female = findViewById(R.id.checkFemale);

        insertCallAPI = findViewById(R.id.insertCallAPI);
        insertCallAPI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inserted = 0;
                StringBuilder sb = new StringBuilder("https://randomuser.me/api/?inc=name,location,gender,picture,registered,login,nat");

                if(nationality.getText().length()>0) sb.append("&nat=").append(nationality.getText());
                if(numUsers.getText().length()>0) sb.append("&results=").append(numUsers.getText());

                if(male.isChecked()){
                    sb.append("&gender=male");
                    if(female.isChecked()) sb.append(",female");
                }else if(female.isChecked()) sb.append("&gender=female");

                String request = null;
                try {
                    request = new GetUrlContentTask().execute(sb.toString()).get();
                } catch (InterruptedException | ExecutionException e) {
                    e.printStackTrace();
                }

                Log.v("INSERT_USER", "REQUEST TO STRING IS "+ request);
                try {
                    JSONObject reader = new JSONObject(request);
                    JSONArray results = reader.getJSONArray("results");
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat format = new SimpleDateFormat("dd/MM/yyyy");
                    @SuppressLint("SimpleDateFormat") SimpleDateFormat apiFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
                    Date filterDate = new Date();
                    if(regDate.getText().toString().length() > 0) filterDate = format.parse(regDate.getText().toString());

                    for(int i = 0; i < results.length(); i++) {
                        JSONObject obj = results.getJSONObject(i);

                        JSONObject nombreJSON = obj.getJSONObject("name");
                        String nombre = nombreJSON.getString("title") + " " + nombreJSON.getString("first") + " " + nombreJSON.getString("last");

                        nombreJSON = obj.getJSONObject("location");
                        String localizacion = nombreJSON.getString("street") + " " + nombreJSON.getString("city") + " " + nombreJSON.getString("state") + " " + nombreJSON.getString("postcode");

                        nombreJSON = obj.getJSONObject("login");
                        String username = nombreJSON.getString("username");
                        String password = nombreJSON.getString("password");

                        nombreJSON = obj.getJSONObject("picture");
                        String image = nombreJSON.getString("large");

                        User user = new User(nombre, image, localizacion, username, password, obj.getString("gender").substring(0,1).toUpperCase(), format.format(apiFormat.parse(obj.getString("registered"))), obj.getString("nat"));

                        if(regDate.getText().toString().equals("") || regDate.getText().toString().length()>0 && apiFormat.parse(obj.getString("registered")).before(filterDate)){
                                DatabaseInitializer.addUserAsync(Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "user-database").fallbackToDestructiveMigration().build(), user);
                                inserted++;
                        }
                    }

                    Toast.makeText(InsertUser.this,"Se han insertado "+inserted+ " usuarios",Toast.LENGTH_LONG).show();
                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }

            }
        });

    }
    class GetUrlContentTask extends AsyncTask<String, Integer, String> {
        private String content = "";

        GetUrlContentTask(){

        }
        protected String doInBackground(String... urls) {

            try{
                URL url = new URL(urls[0]);
                HttpURLConnection connection = (HttpURLConnection) url.openConnection();
                try{
                    connection.setRequestMethod("GET");
                    connection.setDoInput(true);
                    connection.setDoOutput(false);
                    connection.setConnectTimeout(5000);
                    connection.setReadTimeout(5000);
                    connection.connect();
                    BufferedReader rd = new BufferedReader(new InputStreamReader(connection.getInputStream()));
                    String line;
                    while ((line = rd.readLine()) != null) {
                        content += line + "\n";
                    }

                }finally {
                    connection.disconnect();
                }

            }catch (Exception e) {
                e.printStackTrace();
            }
            return content;
        }
    }
}

