package com.ort.num172159_180968.fut5;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.Spinner;
import android.widget.TimePicker;

import com.ort.num172159_180968.fut5.controller.api.Fields;
import com.ort.num172159_180968.fut5.model.persistance.DatabaseHelper;
import com.ort.num172159_180968.fut5.model.persistance.Field;
import com.ort.num172159_180968.fut5.model.persistance.User;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ConfirmMatch extends AppCompatActivity {

    private Spinner comboBox;
    private DatePicker date;
    private TimePicker time;
    private Button btnSave;

    private DatabaseHelper db;
    private List<Field> fields = new ArrayList<>();

    private String[] playersLocal;
    private String[] playersVisitor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_match);

        db = new DatabaseHelper(getApplicationContext());
        fields = db.getAllFields();

        playersLocal = getIntent().getStringArrayExtra("playersLocal");
        playersVisitor = getIntent().getStringArrayExtra("playersVisitor");

        date = (DatePicker) findViewById(R.id.datePicker);
        time = (TimePicker) findViewById(R.id.timePicker);
        comboBox = (Spinner) findViewById(R.id.spinner);
        loadFields();

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



                //addMatch();
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
}
