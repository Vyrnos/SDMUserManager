package com.g81vdbvf.usermanager;

import android.annotation.SuppressLint;
import android.app.NotificationManager;
import android.arch.persistence.room.Room;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v4.app.NavUtils;
import android.support.v4.app.NotificationCompat;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
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
import java.util.concurrent.ExecutionException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class InsertUser extends BaseActivity {

    Button insertCallAPI;
    EditText numUsers, regDate;
    Spinner nationality;
    CheckBox male, female;
    int inserted;
    boolean validDate = true;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        nationality = findViewById(R.id.nacionality);

        // Create an ArrayAdapter using the string array and a default spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.nationalities, android.R.layout.simple_spinner_item);
        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        // Apply the adapter to the spinner
        nationality.setAdapter(adapter);

        numUsers = findViewById(R.id.numUsers);
        regDate = findViewById(R.id.regDate);

        regDate.addTextChangedListener(new TextValidator(regDate) {
            @Override public void validate(TextView textView, String text) {
                String PATTERN = "^(0?[1-9]|[12]\\d|3[01])[/](0?[1-9]|1[0-2])[/](19|20)\\d{2}$";
                Pattern pattern = Pattern.compile(PATTERN);
                Matcher matcher = pattern.matcher(text);
                if(!matcher.matches()){
                    textView.setError("Fecha invalida");
                    validDate = false;
                }else{
                    validDate = true;
                }
            }
        });

        male = findViewById(R.id.checkMale);
        female = findViewById(R.id.checkFemale);

        insertCallAPI = findViewById(R.id.insertCallAPI);


        insertCallAPI.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inserted = 0;
                StringBuilder sb = new StringBuilder("https://randomuser.me/api/?inc=name,location,gender,picture,registered,login,nat");

                if(!nationality.getSelectedItem().toString().equals("Nacionalidad")) {
                    sb.append("&nat=").append(nationality.getSelectedItem().toString().substring(0, 2));
                }
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
                        char [] fFirst = nombreJSON.getString("first").toCharArray();
                        fFirst[0] = Character.toUpperCase(fFirst[0]);
                        char [] fLast = nombreJSON.getString("last").toCharArray();
                        fLast[0] = Character.toUpperCase(fLast[0]);
                        String nombre = new String(fFirst) + " " + new String(fLast);

                        nombreJSON = obj.getJSONObject("location");
                        String localizacion = nombreJSON.getString("street") + " " + nombreJSON.getString("city") + " " + nombreJSON.getString("state") + " " + nombreJSON.getString("postcode");

                        nombreJSON = obj.getJSONObject("login");
                        String username = nombreJSON.getString("username");
                        String password = nombreJSON.getString("password");

                        nombreJSON = obj.getJSONObject("picture");
                        String image = nombreJSON.getString("large");

                        User user = new User(nombre, image, localizacion, username, password, obj.getString("gender").substring(0,1).toUpperCase(), format.format(apiFormat.parse(obj.getString("registered"))), obj.getString("nat"));
                        if(validDate)
                            if(regDate.getText().toString().equals("") || regDate.getText().toString().length()>0 && apiFormat.parse(obj.getString("registered")).before(filterDate)){
                                DatabaseInitializer.addUserAsync(Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "user-database").fallbackToDestructiveMigration().build(), user);
                                inserted++;
                            }
                    }

                    Toast.makeText(InsertUser.this,"Se han insertado "+inserted+ " usuarios",Toast.LENGTH_SHORT).show();
                    NotificationCompat.Builder mBuilder = new NotificationCompat.Builder(InsertUser.this)
                                    .setSmallIcon(R.drawable.ic_supervisor_account_white_36dp)
                                    .setContentTitle("User Manager")
                                    .setContentText("Se han insertado "+inserted+" usuarios");

                    NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
                    assert notificationManager != null;
                    notificationManager.notify(1, mBuilder.build());

                    NavUtils.navigateUpFromSameTask(InsertUser.this);

                } catch (JSONException | ParseException e) {
                    e.printStackTrace();
                }

            }
        });



    }

    @Override
    int getContentViewId() {
        return R.layout.activity_insert_user;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.navigation_insert;
    }

    static class GetUrlContentTask extends AsyncTask<String, Integer, String> {
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

