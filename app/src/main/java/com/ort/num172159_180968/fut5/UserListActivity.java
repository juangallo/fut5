package com.ort.num172159_180968.fut5;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.magnet.android.mms.MagnetMobileClient;
import com.magnet.android.mms.async.Call;
//import com.ort.num172159_180968.fut5.controller.api.User;
import com.ort.num172159_180968.fut5.controller.api.UserFactory;
import com.ort.num172159_180968.fut5.model.beans.UserResult;
import com.ort.num172159_180968.fut5.model.persistance.DatabaseHelper;
import com.ort.num172159_180968.fut5.model.persistance.User;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class UserListActivity extends AppCompatActivity {
    DatabaseHelper db;
    private List<User> users = new ArrayList<>();
    private SessionManager session;

    private String[] playersLocal;
    private Boolean local;
    private AppHelper helper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        db = new DatabaseHelper(getApplicationContext());
        setContentView(R.layout.activity_user_list);
        helper = new AppHelper(getApplicationContext());
        playersLocal = getIntent().getStringArrayExtra("playersLocal");
        local = getIntent().getBooleanExtra("local", true);
        session = new SessionManager(getApplicationContext());
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        refreshUsers();

        populateListView();
        registerClickCallback();

    }

    private void updateUsers() {
        AppHelper helper = new AppHelper(getApplicationContext());
        helper.reloadUsers();

        refreshUsers();

        populateListView();
    }


    private void refreshUsers(){
        users = db.getAllUser();
        if(!local) {
            int pos = -1;
            for (int i = 0; i < 6; i++) {
                System.out.println("Los locales " + playersLocal[i]);
                int aux = 0;
                for (User u : users) {
                    if (playersLocal[i].equals(u.getUsername())) {
                        pos = aux;
                    }
                    aux++;
                }
                if (pos != -1) {
                    users.remove(pos);
                }
                pos = 0;
            }
        }
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        db.closeDB();
    }

    private void populateListView() {
        ArrayAdapter<User> adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.lstUsers);
        list.setAdapter(adapter);
    }

    private void registerClickCallback() {
        ListView list = (ListView)findViewById(R.id.lstUsers);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                User clickedUser = users.get(position);
                //Toast.makeText(UserListActivity.this, clickedUser.getUsername(), Toast.LENGTH_LONG).show();
                Intent intent = getIntent();
                intent.putExtra("username", clickedUser.getUsername());
                setResult(RESULT_OK, intent);
                finish();
            }
        });
    }

    private class MyListAdapter extends ArrayAdapter<User> {
        public MyListAdapter(){
            super(UserListActivity.this, R.layout.item_view, users);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            HashMap<String, Integer> color = session.getColorDetail();
            int colorId = color.get(SessionManager.KEY_COLOR);

            //make sure we have a view to work with (may be null)
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.item_view, parent, false);
            }

            //Find the car to work with
            User userRes = users.get(position);
            //Fill the view
            //missing the image view
            TextView userNameText = (TextView)itemView.findViewById(R.id.item_txtUsername);
            userNameText.setText(userRes.getUsername());
            userNameText.setTextColor(colorId);

            TextView nameText = (TextView)itemView.findViewById(R.id.item_txtName);
            nameText.setText(userRes.getFirstName());
            nameText.setTextColor(colorId);

            TextView lastNameText = (TextView)itemView.findViewById(R.id.item_txtLastName);
            lastNameText.setText(userRes.getLastName());
            lastNameText.setTextColor(colorId);

            Bitmap image = null;
            /*
            boolean found = false;
            for(UsernameImage im: images){
                if (im.username.equals(userRes.getUsername())){
                    found = true;
                    image = im.image;
                }
            }
            if (!found) {
                image = callWebServiceGetUserImage(userRes.getUsername());
                UsernameImage usernameImage = new UsernameImage(userRes.getUsername(), image);
                images.add(usernameImage);
            }*/
            String imageString = userRes.getPhoto();
            byte[] decodedString = Base64.decode(imageString, Base64.DEFAULT);

            image = helper.decodeSampledBitmapFromByte(decodedString, 80, 80);

            ImageView userImageView = (ImageView) itemView.findViewById(R.id.item_icon);
            if (image != null) {
                userImageView.setImageBitmap(image);
            } else {
                if(local) {
                    userImageView.setImageResource(R.drawable.soccerplayer);
                }else {
                    userImageView.setImageResource(R.drawable.soccervisit);
                }
            }
            return itemView;
        }
    }

    /*private Bitmap getUserImage(String username){
        Bitmap image = null;
        for(UsernameImage im: images){
            if (im.username.equals(username)){
                image = im.image;
            }
        }
        System.out.println("image: " + image);
        return image;

    }*/

    public String BitMapToString(Bitmap bitmap){
        if(bitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            String temp = Base64.encodeToString(b, Base64.DEFAULT);
            return temp;
        }else{
            return "";
        }
    }

    /*private class UsernameImage {
        public String username;
        public Bitmap image;

        public UsernameImage(String username, Bitmap image) {
            this.username = username;
            this.image = image;
        }
    }*/
}
