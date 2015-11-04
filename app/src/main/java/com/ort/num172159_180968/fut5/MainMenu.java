package com.ort.num172159_180968.fut5;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.facebook.AccessToken;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.login.LoginManager;
import com.facebook.login.widget.ProfilePictureView;

import org.json.JSONException;

public class MainMenu extends AppCompatActivity {

    private String id_facebook;
    private String user_name;
    private String last_name;
    private Profile profile;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        id_facebook = getIntent().getStringExtra("id_fb");
        user_name = getIntent().getStringExtra("user_name");
        last_name = getIntent().getStringExtra("last_name");

        ImageButton btnMaps = (ImageButton)findViewById(R.id.btnMaps);
        btnMaps.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MapsActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        ImageButton btnSelect = (ImageButton)findViewById(R.id.btnNew);
        btnSelect.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SelectTeamActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        ImageButton btnUser = (ImageButton)findViewById(R.id.btnUser);
        btnUser.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Perfil.class);
                intent.putExtra("id_fb", id_facebook);
                intent.putExtra("user_name", user_name);
                intent.putExtra("last_name", last_name);
                startActivityForResult(intent, 0);
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }
        if(id == R.id.action_logout){
            LoginManager.getInstance().logOut();
            System.out.println("boton log out");
            Intent intent = new Intent(this, LogIn.class);
            startActivity(intent);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }




}
