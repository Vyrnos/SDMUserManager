package com.g81vdbvf.usermanager;

import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ExecutionException;

public class InsertUser extends AppCompatActivity {

    Button insertCallAPI;
    EditText nationality, numUsers, regDate;
    CheckBox male, female;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_insert_user);

        nationality = (EditText) findViewById(R.id.nacionality);
        numUsers = (EditText) findViewById(R.id.numUsers);
        //regDate = findViewById(R.id.regDate);

        male = (CheckBox) findViewById(R.id.checkMale);
        female = (CheckBox) findViewById(R.id.checkFemale);

        insertCallAPI = (Button) findViewById(R.id.insertCallAPI);
        insertCallAPI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                StringBuilder sb = new StringBuilder("https://randomuser.me/api/?inc=gender,registered,nat");

                if(nationality.getText().length()>0) sb.append("&nat="+nationality.getText());
                if(numUsers.getText().length()>0) sb.append("&results="+numUsers.getText());

                if(male.isChecked()){
                    sb.append("&gender=male");
                    if(female.isChecked()) sb.append(",female");
                }else if(female.isChecked()) sb.append("&gender=female");

                String request = null;
                try {
                    request = new GetUrlContentTask().execute(sb.toString()).get();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                Log.v("INSERT_USER", "REQUEST TO STRING IS "+ request);
                try {
                    JSONObject reader = new JSONObject(request);
                    JSONArray results = reader.getJSONArray("results");
                    ArrayList<HashMap<String,String>> contactList = new ArrayList<>();
                    for(int i = 0; i < results.length(); i++) {
                        JSONObject obj = results.getJSONObject(i);
                        String gender = obj.getString("gender");
                        String registered = obj.getString("registered");
                        HashMap<String, String> contact = new HashMap<>();
                        contact.put("gender",gender);
                        contact.put("registered",registered);
                        contactList.add(contact);
                    }
                    for(int i = 0; i < contactList.size(); i++){
                        Log.v("CONTACTOSSSS","El contacto es: GENERO: " + contactList.get(i).get("gender") + " REGISTRADO EN: "+ contactList.get(i).get("registered"));
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }
}

class GetUrlContentTask extends AsyncTask<String, Integer, String> {
    String content = "";

    public GetUrlContentTask(){

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