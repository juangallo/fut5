package com.ort.num172159_180968.fut5;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.magnet.android.mms.MagnetMobileClient;
import com.magnet.android.mms.async.Call;
import com.ort.num172159_180968.fut5.controller.api.UserExists;
import com.ort.num172159_180968.fut5.controller.api.UserExistsFactory;

import java.util.concurrent.ExecutionException;

public class Register extends AppCompatActivity {

    Button btnSave;
    private UserExists userExists;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        btnSaveClick();
        try {
            setUpExistsUser();
        } catch (Exception e) {
            e.printStackTrace();
        }

    }

    protected void setUpExistsUser() throws Exception {
        // Instantiate a controller
        MagnetMobileClient magnetClient = MagnetMobileClient.getInstance(this.getApplicationContext());
        UserExistsFactory controllerFactory = new UserExistsFactory(magnetClient);
        userExists = controllerFactory.obtainInstance();
    }

    public void btnSaveClick(){

        btnSave = (Button)findViewById(R.id.btnSave);
        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String name = ((EditText)findViewById(R.id.txtPersonName)).getText().toString();
                String lastname = ((EditText)findViewById(R.id.txtLastNameRegister)).getText().toString();
                String email = ((EditText)findViewById(R.id.txtEmailAddress)).getText().toString();
                String username = ((EditText)findViewById(R.id.txtUserNameRegister)).getText().toString();
                String password = ((EditText)findViewById(R.id.txtPasswordRegister)).getText().toString();

                Call<String> callObject = userExists.userExists(username, null);
                try {
                    String result = callObject.get();

                    if (result.equals("false")) {
                        Toast toast = Toast.makeText(getApplicationContext(), "Send user.", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                    } else {
                        Toast toast = Toast.makeText(getApplicationContext(), "Username already in use.", Toast.LENGTH_SHORT);
                        toast.setGravity(Gravity.CENTER,0,0);
                        toast.show();
                    }
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } catch (ExecutionException e) {
                    e.printStackTrace();
                }




                //si esta ok guardarlo y mandarlo al login para que entre

                

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_register, menu);
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
}
