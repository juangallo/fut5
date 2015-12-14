package com.ort.num172159_180968.fut5;

import android.app.Dialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.NumberPicker;
import android.widget.Spinner;
import android.widget.TextClock;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.CallbackManager;
import com.facebook.login.LoginManager;
import com.facebook.share.ShareApi;
import com.facebook.share.model.ShareLinkContent;
import com.facebook.share.model.SharePhoto;
import com.facebook.share.model.SharePhotoContent;
import com.facebook.share.widget.ShareDialog;
import com.magnet.android.mms.MagnetMobileClient;
import com.magnet.android.mms.async.Call;
import com.ort.num172159_180968.fut5.controller.api.Statistic;
import com.ort.num172159_180968.fut5.controller.api.StatisticFactory;
import com.ort.num172159_180968.fut5.controller.api.UserStatistics;
import com.ort.num172159_180968.fut5.controller.api.UserStatisticsFactory;
import com.ort.num172159_180968.fut5.model.beans.AddMatchStadisticDetailResult;
import com.ort.num172159_180968.fut5.model.beans.MatchGoalsResult;
import com.ort.num172159_180968.fut5.model.beans.MatchStadisticDetailResult;
import com.ort.num172159_180968.fut5.model.beans.SaveMatchGoalsResult;
import com.ort.num172159_180968.fut5.model.persistance.DatabaseHelper;
import com.ort.num172159_180968.fut5.model.persistance.User;

import org.w3c.dom.Text;

import java.sql.Time;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.TimeZone;
import java.util.concurrent.ExecutionException;

public class EditMatch extends AppCompatActivity {

    ImageView imgLocal;
    ImageView imgVisitor;

    ListView listLocal;
    ListView listVisitor;

    TextView fieldMatch;
    TextView dateMatch;
    TextView createdByName;
    TextView goalsLocal;
    TextView goalsVisitor;
    TextView txtLocalTeam;
    TextView txtVisitorTeam;

    Button btnChange;
    Button btnEdit;
    Button btnShare;
    NumberPicker pickerLocal;
    NumberPicker pickerVisitor;

    NumberPicker pickerGoalsPlayer;
    NumberPicker pickerPannas;
    CheckBox yellowCard;
    CheckBox redCard;

    String type;
    String creatorName;
    String fieldName;
    Long date;
    Integer idMatch;
    String[] players;

    Statistic statistic;
    UserStatistics userStatistics;

    private SessionManager session;
    DatabaseHelper db;
    private int color;

    private String[] playersLocal = new String[5];
    private Boolean[] playersLocalDetail = new Boolean[5];
    private String[] playersVisitor = new String[5];
    private Boolean[] playersVisitorDetail = new Boolean[5];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_match);

        db = new DatabaseHelper(getApplicationContext());
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        String username = user.get(SessionManager.KEY_USERNAME);

        HashMap<String, Integer> colorSession = session.getColorDetail();
        color = colorSession.get(SessionManager.KEY_COLOR);

        txtLocalTeam = (TextView) findViewById(R.id.txtLocalTeam);
        txtLocalTeam.setTextColor(color);
        txtVisitorTeam = (TextView) findViewById(R.id.txtVisitorTeam);
        txtVisitorTeam.setTextColor(color);

        btnEdit = (Button) findViewById(R.id.btnEdit);
        setResult();
        btnShare = (Button) findViewById(R.id.btnShareFb);
        btnShare.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                share_facebok();
            }
        });
        btnChange = (Button) findViewById(R.id.btnChange);
        update_match();

        imgLocal = (ImageView) findViewById(R.id.imgLocal);
        imgLocal.setImageResource(R.drawable.notepad1);

        imgVisitor = (ImageView) findViewById(R.id.imgVisitor);
        imgVisitor.setImageResource(R.drawable.notepad1);

        listLocal = (ListView) findViewById(R.id.listLocal);
        listVisitor = (ListView) findViewById(R.id.listVisitor);

        getStringFromIntent();

        setMatchData();

        goalsLocal = (TextView) findViewById(R.id.txtGoalsLocal);
        goalsVisitor = (TextView) findViewById(R.id.txtGoalsVisitor);

        for(int i = 0; i < 5; i++)
        {
            playersLocal[i] = players[i];
            playersLocalDetail[i] = false;
            playersVisitorDetail[i] = false;
        }
        for(int i = 5; i < 10; i++)
        {
            playersVisitor[i-5] = players[i];
        }

        populateListView();

        setUpServices();

        if(type.equals("finished")){
            btnChange.setVisibility(View.INVISIBLE);
            if(!username.equals(creatorName)){
                btnEdit.setVisibility(View.INVISIBLE);
            } else {
                registerClickCallback(true);
                registerClickCallback(false);
            }
                try {
                    Call<MatchGoalsResult> callObject = statistic.getMatchGoals(idMatch + "", null);
                    MatchGoalsResult matchGoalsResult = callObject.get();
                    if(matchGoalsResult.getMatchStadisticsId() != 0) {
                        int localGoals = matchGoalsResult.getTeam1Goals();
                        goalsLocal.setText(localGoals + "");
                        int visitorGoals = matchGoalsResult.getTeam2Goals();
                        goalsVisitor.setText(visitorGoals + "");
                        btnEdit.setVisibility(View.INVISIBLE);
                    } else {
                        goalsLocal.setText("");
                        goalsVisitor.setText("");
                    }

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }


            if(session.isFacebook()){
                btnShare.setVisibility(View.VISIBLE);
            } else {
                btnShare.setVisibility(View.INVISIBLE);

            }
        }

        if(type.equals("next")){
            btnEdit.setVisibility(View.INVISIBLE);
            btnShare.setVisibility(View.INVISIBLE);
            if(!username.equals(creatorName)) {
                btnChange.setVisibility(View.INVISIBLE);
            } else {
                btnChange.setVisibility(View.INVISIBLE);
            }
            goalsLocal.setText("");
            goalsVisitor.setText("");
        }

        if(type.equals("others")){
            btnEdit.setVisibility(View.INVISIBLE);
            btnShare.setVisibility(View.INVISIBLE);
            btnChange.setVisibility(View.INVISIBLE);
            try {
                Call<MatchGoalsResult> callObject = statistic.getMatchGoals(idMatch + "", null);
                MatchGoalsResult matchGoalsResult = callObject.get();
                int localGoals = matchGoalsResult.getTeam1Goals();
                goalsLocal.setText(localGoals + "");
                int visitorGoals = matchGoalsResult.getTeam2Goals();
                goalsVisitor.setText(visitorGoals + "");
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        db.closeDB();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_match, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void getStringFromIntent(){
        creatorName = getIntent().getStringExtra("username");
        fieldName = getIntent().getStringExtra("field");
        date = getIntent().getLongExtra("date", 0);
        idMatch = getIntent().getIntExtra("idMatch", 0);
        players = getIntent().getStringArrayExtra("players");
        type = getIntent().getStringExtra("type");
    }

    public void setMatchData(){
        createdByName = (TextView) findViewById(R.id.txtCreatedByName);
        createdByName.setText(creatorName);
        fieldMatch = (TextView) findViewById(R.id.txtFieldName);
        fieldMatch.setText(fieldName);
        dateMatch = (TextView) findViewById(R.id.txtDateMatch);

        Date d = new Date();
        d.setTime(date);
        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String formattedDate = df.format(d.getTime());

        dateMatch.setText(formattedDate);
    }

    public void setResult(){

        btnEdit.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                final Dialog dialog = new Dialog(v.getContext());
                dialog.setContentView(R.layout.content_set_result);

                TextView txtLocal = (TextView) dialog.findViewById(R.id.txtLocal);
                txtLocal.setTextColor(color);
                TextView txtVisitor = (TextView) dialog.findViewById(R.id.txtVisitor);
                txtVisitor.setTextColor(color);

                pickerLocal = (NumberPicker) dialog.findViewById(R.id.pickerLocal);
                pickerLocal.setMinValue(0);
                pickerLocal.setMaxValue(20);
                pickerVisitor = (NumberPicker) dialog.findViewById(R.id.pickerVisitor);
                pickerVisitor.setMinValue(0);
                pickerVisitor.setMaxValue(20);

                Button btnSaveResult = (Button) dialog.findViewById(R.id.btnSaveResult);
                btnSaveResult.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        try {
                            Call<SaveMatchGoalsResult> callObject = statistic.saveMatchGoals(idMatch + "", pickerLocal.getValue() + "", pickerVisitor.getValue() + "", null);
                            int result = callObject.get().getMatchStadisticsId();
                            if (result == 0) {
                                Toast.makeText(getApplicationContext(), "Problem saving match result", Toast.LENGTH_LONG).show();
                            } else {
                                btnEdit.setVisibility(View.INVISIBLE);
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                        goalsLocal.setText(pickerLocal.getValue() + "");
                        goalsVisitor.setText(pickerVisitor.getValue() + "");
                        dialog.dismiss();
                    }
                });

                dialog.show();
            }
        });
    }

    public void update_match(){
        btnChange.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v) {
                final Dialog dialog = new Dialog(v.getContext());
                dialog.setContentView(R.layout.content_update_match);

                dialog.show();
            }
        });
    }

    public void share_facebok (){
        List<String> permissions_publish = new ArrayList<>();
        permissions_publish.add("publish_actions");
        LoginManager.getInstance().logInWithPublishPermissions(this, permissions_publish);
        System.out.println("estoy en el share");

        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("https://developers.facebook.com"))
                .setContentDescription("Match: " + dateMatch.getText() + ", Field: " + fieldMatch.getText() + ", Result: " + goalsLocal.getText() + "-" + goalsVisitor.getText())
                .build();

        /*Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.soccerfield);
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(image)
                .build();
        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();*/



        ShareApi.share(content, null);



    }

    private void setUpServices(){
        try {
            setUpAddStatistics();
            setUpAddUserStatistics();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    protected void setUpAddStatistics() throws Exception {
        // Instantiate a controller
        MagnetMobileClient magnetClient = MagnetMobileClient.getInstance(this.getApplicationContext());
        StatisticFactory controllerFactory = new StatisticFactory(magnetClient);
        statistic = controllerFactory.obtainInstance();
    }

    protected void setUpAddUserStatistics() throws Exception {
        // Instantiate a controller
        MagnetMobileClient magnetClient = MagnetMobileClient.getInstance(this.getApplicationContext());
        UserStatisticsFactory controllerFactory = new UserStatisticsFactory(magnetClient);
        userStatistics = controllerFactory.obtainInstance();
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
        Boolean[] playersDetail;

        public MyListAdapter(boolean local,String[] playersTeam){
            super(EditMatch.this, R.layout.content_players_list, playersTeam);
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
                playersDetail = playersLocalDetail;
            } else {
                imgPlayer.setImageResource(R.drawable.soccervisit);
                playersDetail = playersVisitorDetail;
            }

            //ver si tiene estadistica y poner el tick
            if(type.equals("finished")){
                System.out.println("valor en el players detail: " + isLocal + ":" + position + ":" + playersDetail[position]);
                if (!playersDetail[position]) {
                    try {
                        User clickedUser = db.getUser(players[position]);
                        Call<MatchStadisticDetailResult> callObject = userStatistics.getMatchStadisticDetail(idMatch + "", clickedUser.getUserId() + "", null);
                        int result = callObject.get().getMatchStadisticsDetailId();
                        if (result == 0) {
                            //Toast.makeText(getApplicationContext(), "Problem saving match result", Toast.LENGTH_LONG).show();
                        } else {
                            //Toast.makeText(getApplicationContext(), "Data Saved", Toast.LENGTH_SHORT).show();
                            ImageView imgTick = (ImageView) itemView.findViewById(R.id.imgTick);
                            imgTick.setImageResource(R.drawable.checkmark);
                            playersDetail[position] = true;
                        }
                    }catch(Exception e){
                        e.printStackTrace();
                    }
                }
            }

            return itemView;
        }
    }

    private void registerClickCallback(final boolean local) {

        ListView list;
        if(local){
             list = (ListView)findViewById(R.id.listLocal);
        } else {
             list = (ListView)findViewById(R.id.listVisitor);
        }
        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View viewClicked, final int position, long id) {
                final Dialog dialog = new Dialog(viewClicked.getContext());
                dialog.setContentView(R.layout.content_user_statistics);

                pickerGoalsPlayer = (NumberPicker) dialog.findViewById(R.id.pickerGoalsPlayer);
                pickerGoalsPlayer.setMinValue(0);
                pickerGoalsPlayer.setMaxValue(10);
                pickerPannas = (NumberPicker) dialog.findViewById(R.id.pickerPannas);
                pickerPannas.setMinValue(0);
                pickerPannas.setMaxValue(10);
                yellowCard = (CheckBox) dialog.findViewById(R.id.checkYellow);
                redCard = (CheckBox) dialog.findViewById(R.id.checkRed);

                Button btnSaveUserStatistics = (Button) dialog.findViewById(R.id.btnSaveUserStatistics);
                btnSaveUserStatistics.setOnClickListener(new View.OnClickListener() {
                    public void onClick(View v) {
                        try {
                            User clickedUser;
                            if(local){
                                clickedUser = db.getUser(playersLocal[position]);
                            } else {
                                clickedUser = db.getUser(playersVisitor[position]);
                            }

                            Call<AddMatchStadisticDetailResult> callObject = userStatistics.addMatchStadisticDetail(idMatch + "" ,clickedUser.getUserId() + "" ,pickerGoalsPlayer.getValue() + "" ,yellowCard.isChecked() + "" ,redCard.isChecked() + "",pickerPannas.getValue() + "" ,null);
                            int result = callObject.get().getMatchStadisticsDetailId();
                            if (result == 0) {
                                Toast.makeText(getApplicationContext(), "Problem saving match result", Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getApplicationContext(), "Data Saved", Toast.LENGTH_SHORT).show();
                                ImageView imgTick = (ImageView)viewClicked.findViewById(R.id.imgTick);
                                imgTick.setImageResource(R.drawable.checkmark);
                                if(local){
                                    playersLocalDetail[position] = true;
                                } else {
                                    playersVisitorDetail[position] = true;
                                }
                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        dialog.dismiss();
                    }
                });
                ImageView img = (ImageView)viewClicked.findViewById(R.id.imgTick);
                if(img.getDrawable() != null){
                    Toast.makeText(getApplicationContext(), "With data", Toast.LENGTH_SHORT).show();
                } else {
                    dialog.show();
                }

            }
        });
    }
}
