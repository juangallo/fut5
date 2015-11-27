package com.ort.num172159_180968.fut5;

import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.ort.num172159_180968.fut5.controller.api.Match;
import com.ort.num172159_180968.fut5.model.persistance.User;

import java.util.ArrayList;
import java.util.List;

public class TabMatches extends AppCompatActivity {

    private List<Match> matches = new ArrayList<>();
    String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_matches);

        type = getIntent().getStringExtra("type");

        //dependiendo el tipo es que lista de partidos cargo

        //obtener partidos

        populateListView();
        registerClickCallback();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tab_matches, menu);
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

        return super.onOptionsItemSelected(item);
    }

    private void registerClickCallback() {
        ListView list = (ListView)findViewById(R.id.lstMatches);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                //ir al detalle del partido

                //User clickedUser = users.get(position);
                //Toast.makeText(UserListActivity.this, clickedUser.getUsername(), Toast.LENGTH_LONG).show();
                //Intent intent = getIntent();
                //intent.putExtra("username", clickedUser.getUsername());
                //setResult(RESULT_OK, intent);
                //finish();
            }
        });
    }

    private void populateListView() {
        ArrayAdapter<Match> adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.lstMatches);
        list.setAdapter(adapter);
    }

    private class MyListAdapter extends ArrayAdapter<Match> {
        public MyListAdapter(){
            super(TabMatches.this, R.layout.match_view, matches);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //make sure we have a view to work with (may be null)
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.match_view, parent, false);
            }

            //Find the car to work with
            Match matchRes = matches.get(position);

            // /Fill the view
            //missing the image view

            TextView matchField = (TextView)itemView.findViewById(R.id.item_txtField);
            matchField.setText("Cancha 1");

            TextView matchDate = (TextView)itemView.findViewById(R.id.item_txtDate);
            matchDate.setText("2015-12-12");



           /* Bitmap image = null;

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
            }*/
            return itemView;
        }
    }
}
