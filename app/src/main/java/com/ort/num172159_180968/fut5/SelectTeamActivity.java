package com.ort.num172159_180968.fut5;

import android.app.AlertDialog;
import android.content.DialogInterface;
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
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

    private DatabaseHelper db;
    private ImageButton btnNext;

    private Boolean local;

    private String[] playersLocal;
    private String[] playersVisitor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_team);
        db = new DatabaseHelper(getApplicationContext());
        listenerPlayers();

        local = true;
        btnNext = (ImageButton)findViewById(R.id.imgbtnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                if(local){
                    if(completeTeam(playersLocal)){
                        createDialog(local);
                    } else {
                        Toast.makeText(getApplicationContext(), "Local Team Incomplete",
                                Toast.LENGTH_SHORT).show();
                    }
                } else {
                    if(completeTeam(playersVisitor)){
                        createDialog(local);
                    } else {
                        Toast.makeText(getApplicationContext(), "Visitor Team Incomplete",
                                Toast.LENGTH_SHORT).show();
                    }
                }

            }
        });

        playersLocal = new String[6];
        playersLocal[0] = "";
        playersLocal[1] = "";
        playersLocal[2] = "";
        playersLocal[3] = "";
        playersLocal[4] = "";
        playersLocal[5] = "";

        playersVisitor = new String[6];
        playersVisitor[0] = "";
        playersVisitor[1] = "";
        playersVisitor[2] = "";
        playersVisitor[3] = "";
        playersVisitor[4] = "";
        playersVisitor[5] = "";
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
                intent.putExtra("playersLocal",playersLocal);
                intent.putExtra("local",local);
                startActivityForResult(intent, 1);
            }
        });

        player2.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), UserListActivity.class);
                intent.putExtra("playersLocal",playersLocal);
                intent.putExtra("local",local);
                startActivityForResult(intent, 2);
            }
        });

        player3.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), UserListActivity.class);
                intent.putExtra("playersLocal",playersLocal);
                intent.putExtra("local",local);
                startActivityForResult(intent, 3);
            }
        });

        player4.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), UserListActivity.class);
                intent.putExtra("playersLocal",playersLocal);
                intent.putExtra("local",local);
                startActivityForResult(intent, 4);
            }
        });

        player5.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), UserListActivity.class);
                intent.putExtra("playersLocal",playersLocal);
                intent.putExtra("local",local);
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
            String[] players;
            if(local){
                players = playersLocal;
            } else {
                players = playersVisitor;
            }
            if (requestCode == 1) {
                setImageAndText(player1, txtPlayer1, newPlayer.getUsername(), newPlayer.getPhoto(), newUsername, 1, players);
            }
            if (requestCode == 2) {
                setImageAndText(player2,txtPlayer2, newPlayer.getUsername(), newPlayer.getPhoto(), newUsername, 2, players);
            }
            if (requestCode == 3) {
                setImageAndText(player3,txtPlayer3, newPlayer.getUsername(), newPlayer.getPhoto(), newUsername, 3, players);
            }
            if (requestCode == 4) {
                setImageAndText(player4,txtPlayer4, newPlayer.getUsername(), newPlayer.getPhoto(), newUsername, 4, players);
            }
            if (requestCode == 5) {
                setImageAndText(player5,txtPlayer5, newPlayer.getUsername(), newPlayer.getPhoto(), newUsername, 5, players);
            }
        }
    }

    private void setImageAndText(ImageView image, TextView text, String name, String photo,String username,int code,String[] players){

        int pos = getPosition(username,players);
        if(pos != 0) {
            players[pos] = players[code];
            ImageView imageChange = getImage(pos);
            TextView textChange = getTextPlayer(pos);

            textChange.setText(text.getText());
            Bitmap b = ((BitmapDrawable)image.getDrawable()).getBitmap();
            imageChange.setImageBitmap(b);

        }

        if (photo.isEmpty()) {
            if(local) {
                image.setImageResource(R.drawable.soccerplayer);
            } else {
                image.setImageResource(R.drawable.soccervisit);
            }
        } else {
                byte[] decodedString = Base64.decode(photo, Base64.DEFAULT);
                Bitmap userImage = BitmapFactory.decodeByteArray(decodedString, 0, decodedString.length);
                image.setImageBitmap(userImage);
        }
        text.setText(name);
        text.setTextColor(Color.BLACK);

        players[code] = username;

    }

    private int getPosition(String username,String[] players){
        System.out.println("username en el position: " + username);
        for(int i = 0; i < 6; i++)
        {
            if(players[i].equals(username)){
                return i;
            }
        }
        return 0;

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

    private void cleanPlayers(){
        player1.setImageBitmap(null);
        player1.setImageResource(R.drawable.unknown);
        player2.setImageBitmap(null);
        player2.setImageResource(R.drawable.unknown);
        player3.setImageBitmap(null);
        player3.setImageResource(R.drawable.unknown);
        player4.setImageBitmap(null);
        player4.setImageResource(R.drawable.unknown);
        player5.setImageBitmap(null);
        player5.setImageResource(R.drawable.unknown);

        txtPlayer1.setText(null);
        txtPlayer2.setText(null);
        txtPlayer3.setText(null);
        txtPlayer4.setText(null);
        txtPlayer5.setText(null);

    }

    private void createDialog(final Boolean localTeam){
        AlertDialog.Builder alertDialog = new AlertDialog.Builder(this);

        if(localTeam) {
            alertDialog.setTitle("Confirm Local Team");
        } else {
            alertDialog.setTitle("Confirm Visitor Team");
        }
        alertDialog.setMessage("Are you sure you want to confirm the team?");

        alertDialog.setIcon(R.drawable.checkmark);

        alertDialog.setPositiveButton("YES", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {

                if (localTeam) {
                    cleanPlayers();
                    local = false;
                    TextView text = (TextView) findViewById(R.id.lblLocalTeam);
                    text.setText("VISITOR TEAM");
                } else {
                    //guardar equipos y salir
                }
            }
        });
        alertDialog.setNegativeButton("NO", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        alertDialog.show();
    }

    private boolean completeTeam(String[] players){
        boolean ret = true;
        for(int i = 1; i < 6; i++)
        {
            if(players[i].equals("")){
                ret = false;
            }
        }
        return ret;
    }
}
