package com.ort.num172159_180968.fut5;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
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
import java.util.List;
import java.util.concurrent.ExecutionException;

public class UserListActivity extends AppCompatActivity {
    //private User user;
    DatabaseHelper db;
    private List<User> users = new ArrayList<>();
    //private List<UsernameImage> images = new ArrayList<>();

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
        local = getIntent().getBooleanExtra("local",true);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
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
        populateListView();
        registerClickCallback();
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        db.closeDB();
    }


    /*protected void setUp() throws Exception {
        // Instantiate a controller
        MagnetMobileClient magnetClient = MagnetMobileClient.getInstance(this.getApplicationContext());
        UserFactory controllerFactory = new UserFactory(magnetClient);
        user = controllerFactory.obtainInstance();
    }*/

    /*private void callWebServiceGetUsers(){
        Call<List<UserResult>> callObject = user.getUsers("", null);
        if (!callObject.equals(null)) {
            try {

                users = callObject.get();

                if(!local) {

                    int pos = -1;
                    for (int i = 0; i < 6; i++) {
                        System.out.println("Los locales " + playersLocal[i]);
                        int aux = 0;
                        for (UserResult u : users) {
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


            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
    }*/

    /*private Bitmap callWebServiceGetUserImage(String username){
        System.out.println(username);
        Call<String> callObject = user.getUserImage(username, null);
        Bitmap ret = null;
        if (!callObject.equals(null)) {
            try {
                String image = callObject.get();
                if (!image.equals("-1")) {
                    byte[] decodedString = Base64.decode(image, Base64.DEFAULT);
                    ret = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }*/

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

            TextView nameText = (TextView)itemView.findViewById(R.id.item_txtName);
            nameText.setText(userRes.getFirstName());

            TextView lastNameText = (TextView)itemView.findViewById(R.id.item_txtLastName);
            lastNameText.setText(userRes.getLastName());

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
