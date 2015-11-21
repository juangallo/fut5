package com.ort.num172159_180968.fut5;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.media.Image;
import android.support.v4.content.res.TypedArrayUtils;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.ort.num172159_180968.fut5.model.persistance.DatabaseHelper;
import com.ort.num172159_180968.fut5.model.persistance.User;

import org.w3c.dom.Text;

import java.sql.Array;
import java.util.Arrays;

public class SelectTeamActivity extends AppCompatActivity {

    private ImageView player1;
    private ImageView player2;
    private ImageView player3;
    private ImageView player4;
    private ImageView player5;

    private TextView txtPlayer1;
    private TextView txtPlayer2;
    private TextView txtPlayer3;
    private TextView txtPlayer4;
    private TextView txtPlayer5;

    private String[] players;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_team);
        db = new DatabaseHelper(getApplicationContext());
        listenerPlayers();

        players = new String[6];
        players[0] = "";
        players[1] = "";
        players[2] = "";
        players[3] = "";
        players[4] = "";
        players[5] = "";


    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_select_team, menu);
        return true;
    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        db.closeDB();
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

        return super.onOptionsItemSelected(item);
    }


    private void listenerPlayers(){
        player1 = (ImageView) findViewById(R.id.player1);
        player2 = (ImageView) findViewById(R.id.player2);
        player3 = (ImageView) findViewById(R.id.player3);
        player4 = (ImageView) findViewById(R.id.player4);
        player5 = (ImageView) findViewById(R.id.player5);

        txtPlayer1 = (TextView) findViewById(R.id.txtPlayer1);
        txtPlayer2 = (TextView) findViewById(R.id.txtPlayer2);
        txtPlayer3 = (TextView) findViewById(R.id.txtPlayer3);
        txtPlayer4 = (TextView) findViewById(R.id.txtPlayer4);
        txtPlayer5 = (TextView) findViewById(R.id.txtPlayer5);



        player1.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), UserListActivity.class);
                startActivityForResult(intent, 1);
            }
        });

        player2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), UserListActivity.class);
                startActivityForResult(intent, 2);
            }
        });

        player3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), UserListActivity.class);
                startActivityForResult(intent, 3);
            }
        });

        player4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), UserListActivity.class);
                startActivityForResult(intent, 4);
            }
        });

        player5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), UserListActivity.class);
                startActivityForResult(intent, 5);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            String newUsername = data.getStringExtra("username");
            User newPlayer = db.getUser(newUsername);
            if (requestCode == 1) {
                setImageAndText(player1, txtPlayer1, newPlayer.getUsername(), newPlayer.getPhoto(), newUsername,1);
            }
            if (requestCode == 2) {
                setImageAndText(player2,txtPlayer2, newPlayer.getUsername(), newPlayer.getPhoto(), newUsername,2);
            }
            if (requestCode == 3) {
                setImageAndText(player3,txtPlayer3, newPlayer.getUsername(), newPlayer.getPhoto(), newUsername,3);
            }
            if (requestCode == 4) {
                setImageAndText(player4,txtPlayer4, newPlayer.getUsername(), newPlayer.getPhoto(), newUsername,4);
            }
            if (requestCode == 5) {
                setImageAndText(player5,txtPlayer5, newPlayer.getUsername(), newPlayer.getPhoto(), newUsername,5);
            }
        }
    }

    private void setImageAndText(ImageView image, TextView text, String name, String photo,String username,int code){

        int pos = getPosition(username);
        if(pos != 0) {
            players[pos] = players[code];
            ImageView imageChange = getImage(pos);
            TextView textChange = getTextPlayer(pos);

            textChange.setText(text.getText());
            Bitmap b = ((BitmapDrawable)image.getDrawable()).getBitmap();
            imageChange.setImageBitmap(b);

        }

        if (photo.isEmpty()) {
                image.setImageResource(R.drawable.soccerplayer);
        } else {
                byte[] decodedString = Base64.decode(photo, Base64.DEFAULT);
                Bitmap userImage = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                image.setImageBitmap(userImage);
        }
        text.setText(name);
        text.setTextColor(Color.BLACK);

        players[code] = username;

    }

    private int getPosition(String username){
        System.out.println("username en el position: " + username);
        for(int i = 0; i < 6; i++)
        {
            if(players[i].equals(username)){
                return i;
            }
        }
        return 0;

       /* for (String p : players) {
            if(p.equals(username)){
                return pos;
            }
            pos++;
        }*/

    }

    private ImageView getImage(int pos){

        switch (pos){
            case 1:
                return player1;
            case 2:
                return player2;
            case 3:
                return player3;
            case 4:
                return player4;
            case 5:
                return player5;
        }
        return null;
    }

    private TextView getTextPlayer(int pos){
        switch (pos){
            case 1:
                return txtPlayer1;
            case 2:
                return txtPlayer2;
            case 3:
                return txtPlayer3;
            case 4:
                return txtPlayer4;
            case 5:
                return txtPlayer5;
        }
        return null;
    }

}
