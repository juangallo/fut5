package com.ort.num172159_180968.fut5;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.magnet.android.mms.MagnetMobileClient;
import com.magnet.android.mms.async.Call;
import com.ort.num172159_180968.fut5.controller.api.User;
import com.ort.num172159_180968.fut5.controller.api.UserFactory;
import com.ort.num172159_180968.fut5.model.beans.UserResult;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class UserListActivity extends AppCompatActivity {
    private User user;
    private List<UserResult> users = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        try {
            setUp();
            callWebService();
        } catch (Exception e) {
            e.printStackTrace();
        }
        populateListView();
        registerClickCallback();
    }


    protected void setUp() throws Exception {
        // Instantiate a controller
        MagnetMobileClient magnetClient = MagnetMobileClient.getInstance(this.getApplicationContext());
        UserFactory controllerFactory = new UserFactory(magnetClient);
        user = controllerFactory.obtainInstance();
    }

    private void callWebService(){
        Call<List<UserResult>> callObject = user.getUsers("", null);
        if (!callObject.equals(null)) {
            try {
                users = callObject.get();
                for(UserResult u : users) {
                    System.out.println(u.getUsername() + " " + u.getLastName());
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }

    private void populateListView() {
        ArrayAdapter<UserResult> adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.lstUsers);
        list.setAdapter(adapter);

    }

    private void registerClickCallback() {
        ListView list = (ListView)findViewById(R.id.lstUsers);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                UserResult clickedUser = users.get(position);
                Toast.makeText(UserListActivity.this, clickedUser.getUsername(), Toast.LENGTH_LONG).show();
            }
        });
    }

    private class MyListAdapter extends ArrayAdapter<UserResult> {
        public MyListAdapter(){
            super(UserListActivity.this, R.layout.item_view, users);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //make sure we have a view to work with (may be null)
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.item_view, parent, false);
            }
            //Find the car to work with
            UserResult userRes = users.get(position);
            //Fill the view
            //missing the image view
            TextView userNameText = (TextView)itemView.findViewById(R.id.item_txtUsername);
            userNameText.setText(userRes.getUsername());

            TextView nameText = (TextView)itemView.findViewById(R.id.item_txtName);
            nameText.setText(userRes.getFirstName());

            TextView lastNameText = (TextView)itemView.findViewById(R.id.item_txtLastName);
            lastNameText.setText(userRes.getLastName());
            return itemView;
        }
    }
}
