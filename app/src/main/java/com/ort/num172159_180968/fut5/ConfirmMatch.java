package com.ort.num172159_180968.fut5;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;
import android.widget.Toast;

import com.magnet.android.mms.MagnetMobileClient;
import com.magnet.android.mms.async.Call;
import com.ort.num172159_180968.fut5.controller.api.Match;
import com.ort.num172159_180968.fut5.controller.api.MatchFactory;
import com.ort.num172159_180968.fut5.controller.api.User;
import com.ort.num172159_180968.fut5.model.beans.AddMatchRequest;
import com.ort.num172159_180968.fut5.model.beans.AddMatchResult;
import com.ort.num172159_180968.fut5.model.persistance.DatabaseHelper;
import com.ort.num172159_180968.fut5.model.persistance.Field;


import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ConfirmMatch extends AppCompatActivity {

    private Spinner comboBox;
    private DatePicker date;
    private TimePicker time;
    private Button btnSave;

    private DatabaseHelper db;
    private List<Field> fields = new ArrayList<>();

    private String[] playersLocal;
    private String[] playersVisitor;

    private Match addMatch;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_match);

        session = new SessionManager(getApplicationContext());

        db = new DatabaseHelper(getApplicationContext());
        fields = db.getAllFields();

        playersLocal = getIntent().getStringArrayExtra("playersLocal");
        playersVisitor = getIntent().getStringArrayExtra("playersVisitor");

        date = (DatePicker) findViewById(R.id.datePicker);
        time = (TimePicker) findViewById(R.id.timePicker);
        comboBox = (Spinner) findViewById(R.id.spinner);
        loadFields();

        try {
            setUpAddMatch();
        } catch (Exception e) {
            e.printStackTrace();
        }

        btnSave = (Button) findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {

                String fieldName = comboBox.getSelectedItem().toString();
                Field field = db.getField(fieldName);
                System.out.println("Field Name: " + fieldName);

                int day = date.getDayOfMonth();
                int month = date.getMonth();
                int year = date.getYear();

                int hour = time.getCurrentHour();
                int minutes = time.getCurrentMinute();

                String dateMatch = "" + year + "/" + month + "/" + day + " " + hour + ":" + minutes + ":00";
                System.out.println("date: " + dateMatch);

                HashMap<String, String> user = session.getUserDetails();
                String username = user.get(SessionManager.KEY_USERNAME);


                AddMatchRequest body;
                List<Integer> local = new ArrayList<Integer>();
                List<Integer> visitor = new ArrayList<Integer>();

                for(int i = 1; i < 6; i++)
                {
                    com.ort.num172159_180968.fut5.model.persistance.User uLocal = db.getUser(playersLocal[i]);
                    local.add(uLocal.getUserId());
                    com.ort.num172159_180968.fut5.model.persistance.User uVisitor = db.getUser(playersVisitor[i]);
                    visitor.add(uVisitor.getUserId());
                }
                AddMatchRequest.AddMatchRequestBuilder builder = new AddMatchRequest.AddMatchRequestBuilder();
                builder.local(local);
                builder.visitor(visitor);

                body = builder.build();

                Call<AddMatchResult> callObjectResult = addMatch.addMatch(username, field.getFieldId() + "", dateMatch, body, null);
                try {
                    AddMatchResult match = callObjectResult.get();
                    Toast toast = Toast.makeText(getApplicationContext(), "Match created. Field " + field.getFieldName() + " Date " + dateMatch, Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER, 0, 0);
                    toast.show();
                    finish();
                    Intent intent = new Intent(getApplicationContext(),MainMenu.class);
                    startActivity(intent);

                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }

                //ver resultado del call object




            }
        });

    }


    @Override
    protected void onDestroy(){
        super.onDestroy();
        db.closeDB();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_confirm_match, menu);
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

    private void loadFields(){

        List<String> list = new ArrayList<String>();
        for (Field f : fields ){
            list.add(f.getFieldName());
        }

        ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(this,android.R.layout.simple_spinner_item, list);
        dataAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        comboBox.setAdapter(dataAdapter);

    }

    protected void setUpAddMatch() throws Exception {
        // Instantiate a controller
        MagnetMobileClient magnetClient = MagnetMobileClient.getInstance(this.getApplicationContext());
        MatchFactory controllerFactory = new MatchFactory(magnetClient);
        addMatch = controllerFactory.obtainInstance();
    }
}
