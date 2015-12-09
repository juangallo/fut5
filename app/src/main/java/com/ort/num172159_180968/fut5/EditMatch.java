package com.ort.num172159_180968.fut5;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

public class EditMatch extends AppCompatActivity {

    ImageView imgLocal;
    ImageView imgVisitor;

    ListView listLocal;
    ListView listVisitor;

    String creatorName;
    Long date;
    Integer idMatch;
    String[] players;

    private String[] playersLocal = new String[5]; //{"Ubuntu", "Android", "iOS", "Windows", "Mac OSX"};
    private String[] playersVisitor = new String[5]; //{"Ubuntu", "Android", "iOS", "Windows", "Mac OSX"};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_match);

        imgLocal = (ImageView) findViewById(R.id.imgLocal);
        imgLocal.setImageResource(R.drawable.notepad1);

        imgVisitor = (ImageView) findViewById(R.id.imgVisitor);
        imgVisitor.setImageResource(R.drawable.notepad1);

        listLocal = (ListView) findViewById(R.id.listLocal);
        listVisitor = (ListView) findViewById(R.id.listVisitor);

        creatorName = getIntent().getStringExtra("username");
        date = getIntent().getLongExtra("date", 0);
        idMatch = getIntent().getIntExtra("idMatch", 0);
        players = getIntent().getStringArrayExtra("players");

        for(int i = 0; i < 5; i++)
        {
            playersLocal[i] = players[i];
        }
        for(int i = 5; i < 10; i++)
        {
            playersVisitor[i-5] = players[i];
        }


        populateListView();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_match, menu);
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

    private void populateListView() {
        ArrayAdapter<String> adapterLocal = new MyListAdapter(true,playersLocal);
        listLocal.setAdapter(adapterLocal);

        ArrayAdapter<String> adapterVisitor = new MyListAdapter(false,playersVisitor);
        listVisitor.setAdapter(adapterVisitor);

    }

    private class MyListAdapter extends ArrayAdapter<String> {
        boolean isLocal;
        String[] players;

        public MyListAdapter(boolean local,String[] playersTeam){
            super(EditMatch.this, R.layout.content_players_list,playersTeam);
            isLocal = local;
            players = playersTeam;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //make sure we have a view to work with (may be null)
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.content_players_list, parent, false);
            }

            //Find the car to work with
            String playerName = players[position];

            TextView txtPlayer = (TextView)itemView.findViewById(R.id.txtPlayer);
            txtPlayer.setText(playerName);

            ImageView imgPlayer = (ImageView) itemView.findViewById(R.id.imgPlayer);
            if(isLocal) {
                imgPlayer.setImageResource(R.drawable.soccerplayer);
            } else {
                imgPlayer.setImageResource(R.drawable.soccervisit);
            }

            return itemView;
        }
    }
}
