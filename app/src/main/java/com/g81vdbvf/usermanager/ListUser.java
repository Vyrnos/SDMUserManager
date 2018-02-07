package com.g81vdbvf.usermanager;

import android.arch.persistence.room.Room;
import android.content.res.Configuration;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ListView;

import java.util.List;

public class ListUser extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_user);

        final ListView list = findViewById(R.id.lv);

        List<User> ul = DatabaseInitializer.getUserList(Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "user-database").allowMainThreadQueries().build());
        UsersAdapter arrayAdapter = new UsersAdapter(this, ul);

        list.setAdapter(arrayAdapter);
    }
}
