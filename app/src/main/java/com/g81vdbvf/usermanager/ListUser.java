package com.g81vdbvf.usermanager;

import android.arch.persistence.room.Room;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.List;

public class ListUser extends BaseActivity {

    public ListView list;
    List<User> ul;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        list = findViewById(R.id.lv);

        ul = DatabaseInitializer.getUserList(Room.databaseBuilder(getApplicationContext(), AppDatabase.class, "user-database").allowMainThreadQueries().build());
        UsersAdapter arrayAdapter = new UsersAdapter(this, ul);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                User u = (User) list.getItemAtPosition(position);
                Intent in = new Intent("android.intent.action.SPECIFICUSER");
                in.putExtra("User", u);
                startActivity(in);
            }
        });
        list.setAdapter(arrayAdapter);
    }

    @Override
    int getContentViewId() {
        return R.layout.activity_list_user;
    }

    @Override
    int getNavigationMenuItemId() {
        return R.id.navigation_list;
    }
}
