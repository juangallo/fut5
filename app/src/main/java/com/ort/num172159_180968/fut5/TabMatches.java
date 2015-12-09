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
import android.widget.Toast;

import com.magnet.android.mms.MagnetMobileClient;
import com.magnet.android.mms.async.Call;
import com.ort.num172159_180968.fut5.controller.api.Match;
import com.ort.num172159_180968.fut5.controller.api.MatchFactory;
import com.ort.num172159_180968.fut5.model.beans.MatchesResult;
import com.ort.num172159_180968.fut5.model.beans.Team;
import com.ort.num172159_180968.fut5.model.persistance.DatabaseHelper;
import com.ort.num172159_180968.fut5.model.persistance.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class TabMatches extends AppCompatActivity {

    private List<MatchesResult> matches = new ArrayList<>();
    String type;

    private Match getMatch;

    private SessionManager session;
    private DatabaseHelper db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tab_matches);

        db = new DatabaseHelper(getApplicationContext());
        type = getIntent().getStringExtra("type");
        System.out.println("type: " + type);

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        String username = user.get(SessionManager.KEY_USERNAME);

        try {
            setUpGetMatch();
        } catch (Exception e) {
            e.printStackTrace();
        }


        if(type.equals("finished")){
            getMatches(username,"true","false");
        }
        if(type.equals("next")){
            getMatches(username,"true","true");
        }
        if(type.equals("others")){
            getMatches(username,"false","false");
        }

        populateListView();
        registerClickCallback();

    }

    @Override
    public void onBackPressed() {
        this.getParent().onBackPressed();
    }




    private void getMatches(String username, String mine, String next){
        Call<List<MatchesResult>> callObject = getMatch.getMatches(username,mine,next,null);
        try {
            matches = callObject.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_tab_matches, menu);
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

    private void registerClickCallback() {
        ListView list = (ListView) findViewById(R.id.lstMatches);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View viewClicked, int position, long id) {
                //ir al detalle del partido

                MatchesResult clickedMatch = matches.get(position);

                String[] players = new String[10];
                int i = 0;
                for (Team t : clickedMatch.getTeams()){
                    players[i] = t.getUsername();
                    i++;
                }


                //Toast.makeText(TabMatches.this, clickedMatch.getMatchId(), Toast.LENGTH_LONG).show();
                Intent intent = new Intent(getApplicationContext(),EditMatch.class);
                intent.putExtra("username", clickedMatch.getCreatedByUser().getUsername());
                intent.putExtra("date",clickedMatch.getMatchDate());
                intent.putExtra("idMatch",clickedMatch.getMatchId());
                intent.putExtra("players",players);
                setResult(RESULT_OK, intent);
                startActivityForResult(intent,position);
                //finish();
            }
        });
    }

    private void populateListView() {
        ArrayAdapter<MatchesResult> adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.lstMatches);
        list.setAdapter(adapter);
    }

    protected void setUpGetMatch() throws Exception {
        // Instantiate a controller
        MagnetMobileClient magnetClient = MagnetMobileClient.getInstance(this.getApplicationContext());
        MatchFactory controllerFactory = new MatchFactory(magnetClient);
        getMatch = controllerFactory.obtainInstance();
    }

    private class MyListAdapter extends ArrayAdapter<MatchesResult> {
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
            MatchesResult matchRes = matches.get(position);

            TextView matchField = (TextView)itemView.findViewById(R.id.item_txtField);
            matchField.setText(db.getField(matchRes.getMatchField().getFieldId()).getFieldName());

            Date date = new Date();
            date.setTime(matchRes.getMatchDate());

            TextView matchDate = (TextView)itemView.findViewById(R.id.item_txtDate);
            matchDate.setText("Date: " + date);


            TextView matchCreator = (TextView)itemView.findViewById(R.id.item_txtCreator);
            matchCreator.setText(matchRes.getCreatedByUser().getFirstName());

            ImageView matchView = (ImageView) itemView.findViewById(R.id.item_icon);
            matchView.setImageResource(R.drawable.maracanaa);

            return itemView;
        }
    }


}
