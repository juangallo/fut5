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
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.fourmob.datetimepicker.date.DatePickerDialog;
import com.magnet.android.mms.MagnetMobileClient;
import com.magnet.android.mms.async.Call;
import com.ort.num172159_180968.fut5.controller.api.Match;
import com.ort.num172159_180968.fut5.controller.api.MatchFactory;
import com.ort.num172159_180968.fut5.controller.api.User;
import com.ort.num172159_180968.fut5.model.beans.AddMatchRequest;
import com.ort.num172159_180968.fut5.model.beans.AddMatchResult;
import com.ort.num172159_180968.fut5.model.persistance.DatabaseHelper;
import com.ort.num172159_180968.fut5.model.persistance.Field;
import com.sleepbot.datetimepicker.time.RadialPickerLayout;
import com.sleepbot.datetimepicker.time.TimePickerDialog;


import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class ConfirmMatch extends AppCompatActivity implements DatePickerDialog.OnDateSetListener, TimePickerDialog.OnTimeSetListener {

    private Spinner comboBox;
    //private DatePicker date;
    //private TimePicker time;
    private Button btnSave;

    private DatabaseHelper db;
    private List<Field> fields = new ArrayList<>();

    private String[] playersLocal;
    private String[] playersVisitor;

    private Match addMatch;
    SessionManager session;
    private int day;
    private int month;
    private int year;
    private int hour;
    private int minute;

    public static final String DATEPICKER_TAG = "datepicker";
    public static final String TIMEPICKER_TAG = "timepicker";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_match);

        session = new SessionManager(getApplicationContext());
        Calendar c = Calendar.getInstance();
        editDateTime(c.get(Calendar.DATE), c.get(Calendar.MONTH) + 1, c.get(Calendar.YEAR), 21, 00);
        updateCalendarIcon();
        ((TextView)findViewById(R.id.txtDate)).setText(getDateString());
        db = new DatabaseHelper(getApplicationContext());
        fields = db.getAllFields();

        playersLocal = getIntent().getStringArrayExtra("playersLocal");
        playersVisitor = getIntent().getStringArrayExtra("playersVisitor");

        //date = (DatePicker) findViewById(R.id.datePicker);
        //time = (TimePicker) findViewById(R.id.timePicker);
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

                String dateMatch = getDateString();//"" + year + "/" + month + "/" + day + " " + hour + ":" + minute + ":00";
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

        final Calendar calendar = Calendar.getInstance();

        final DatePickerDialog datePickerDialog = DatePickerDialog.newInstance(this, calendar.get(Calendar.YEAR), calendar.get(Calendar.MONTH), calendar.get(Calendar.DAY_OF_MONTH), isVibrate());
        final TimePickerDialog timePickerDialog = TimePickerDialog.newInstance(this, calendar.get(Calendar.HOUR_OF_DAY) ,calendar.get(Calendar.MINUTE), false, false);

        findViewById(R.id.imgCalendarDay).setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                datePickerDialog.setVibrate(isVibrate());
                datePickerDialog.setYearRange(1985, 2028);
                datePickerDialog.setCloseOnSingleTapDay(isCloseOnSingleTapDay());
                datePickerDialog.show(getSupportFragmentManager(), DATEPICKER_TAG);
            }
        });

        findViewById(R.id.imgTime).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                timePickerDialog.setVibrate(isVibrate());
                timePickerDialog.setCloseOnSingleTapMinute(isCloseOnSingleTapMinute());
                timePickerDialog.show(getSupportFragmentManager(), TIMEPICKER_TAG);
            }
        });

        if (savedInstanceState != null) {
            DatePickerDialog dpd = (DatePickerDialog) getSupportFragmentManager().findFragmentByTag(DATEPICKER_TAG);
            if (dpd != null) {
                dpd.setOnDateSetListener(this);
            }

            TimePickerDialog tpd = (TimePickerDialog) getSupportFragmentManager().findFragmentByTag(TIMEPICKER_TAG);
            if (tpd != null) {
                tpd.setOnTimeSetListener(this);
            }
        }

    }

    private String getDateString() {
        String res = "";
        res += year + "/";
        if (month < 10){
            res += "0";
        }
        res += month + "/";
        if (day < 10) {
            res += "0";
        }
        res += day + " ";
        if (hour < 10) {
            res += "0";
        }
        res += hour + ":";
        if (minute < 10) {
            res += "0";
        }
        res += minute + ":00";
        return res;
    }

    private void editDateTime(int date, int month, int year, int hour, int minute) {
        this.day = date;
        this.month = month;
        this.year = year;
        this.hour = hour;
        this.minute = minute;
    }

    private boolean isVibrate() {
        return false;//((CheckBox) findViewById(R.id.checkBoxVibrate)).isChecked();
    }

    private boolean isCloseOnSingleTapDay() {
        return false;//((CheckBox) findViewById(R.id.checkBoxCloseOnSingleTapDay)).isChecked();
    }

    private boolean isCloseOnSingleTapMinute() {
        return false;//((CheckBox) findViewById(R.id.checkBoxCloseOnSingleTapMinute)).isChecked();
    }

    @Override
    public void onDateSet(DatePickerDialog datePickerDialog, int year, int month, int day) {
        Toast.makeText(ConfirmMatch.this, "new date:" + year + "-" + month + "-" + day, Toast.LENGTH_LONG).show();
        this.day = day;
        this.month = month + 1;
        this.year = year;
        updateCalendarIcon();
        ((TextView)findViewById(R.id.txtDate)).setText(getDateString());
    }

    @Override
    public void onTimeSet(RadialPickerLayout view, int hourOfDay, int minute) {
        Toast.makeText(ConfirmMatch.this, "new time:" + hourOfDay + "-" + minute, Toast.LENGTH_LONG).show();
        this.hour = hourOfDay;
        this.minute = minute;
        ((TextView)findViewById(R.id.txtDate)).setText(getDateString());
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

    private void updateCalendarIcon() {
        ImageView view = (ImageView)findViewById(R.id.imgCalendarDay);
        switch (day) {
            case 1: view.setImageResource(R.drawable.calendar1);
                break;
            case 2: view.setImageResource(R.drawable.calendar2);
                break;
            case 3: view.setImageResource(R.drawable.calendar3);
                break;
            case 4: view.setImageResource(R.drawable.calendar4);
                break;
            case 5: view.setImageResource(R.drawable.calendar5);
                break;
            case 6: view.setImageResource(R.drawable.calendar6);
                break;
            case 7: view.setImageResource(R.drawable.calendar7);
                break;
            case 8: view.setImageResource(R.drawable.calendar8);
                break;
            case 9: view.setImageResource(R.drawable.calendar9);
                break;
            case 10: view.setImageResource(R.drawable.calendar10);
                break;
            case 11: view.setImageResource(R.drawable.calendar11);
                break;
            case 12: view.setImageResource(R.drawable.calendar12);
                break;
            case 13: view.setImageResource(R.drawable.calendar13);
                break;
            case 14: view.setImageResource(R.drawable.calendar14);
                break;
            case 15: view.setImageResource(R.drawable.calendar15);
                break;
            case 16: view.setImageResource(R.drawable.calendar16);
                break;
            case 17: view.setImageResource(R.drawable.calendar17);
                break;
            case 18: view.setImageResource(R.drawable.calendar18);
                break;
            case 19: view.setImageResource(R.drawable.calendar19);
                break;
            case 20: view.setImageResource(R.drawable.calendar20);
                break;
            case 21: view.setImageResource(R.drawable.calendar21);
                break;
            case 22: view.setImageResource(R.drawable.calendar22);
                break;
            case 23: view.setImageResource(R.drawable.calendar23);
                break;
            case 24: view.setImageResource(R.drawable.calendar24);
                break;
            case 25: view.setImageResource(R.drawable.calendar25);
                break;
            case 26: view.setImageResource(R.drawable.calendar26);
                break;
            case 27: view.setImageResource(R.drawable.calendar27);
                break;
            case 28: view.setImageResource(R.drawable.calendar28);
                break;
            case 29: view.setImageResource(R.drawable.calendar29);
                break;
            case 30: view.setImageResource(R.drawable.calendar30);
                break;
            case 31: view.setImageResource(R.drawable.calendar31);
                break;


        }
    }
}
