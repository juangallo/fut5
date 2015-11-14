package com.ort.num172159_180968.fut5;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.magnet.android.mms.MagnetMobileClient;
import com.magnet.android.mms.async.Call;
import com.ort.num172159_180968.fut5.controller.api.User;
import com.ort.num172159_180968.fut5.controller.api.UserFactory;
import com.ort.num172159_180968.fut5.controller.api.UserLogIn;
import com.ort.num172159_180968.fut5.controller.api.UserLogInFactory;

import java.util.concurrent.ExecutionException;

public class LogIn extends AppCompatActivity {
    private UserLogIn userLogIn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_log_in);

        try {
            setUp();
        } catch (Exception e) {
            e.printStackTrace();
        }


        btnOkAction();
        btnLogInAction();
        btnRegisterAction();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_log_in, menu);
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

    public void btnOkAction(){
        Button btnNext = (Button)findViewById(R.id.btnNext);
        btnNext.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MainMenu.class);
                startActivityForResult(intent, 0);
            }
        });
    }

    public void btnLogInAction(){
        Button btnLogIn = (Button)findViewById(R.id.btnOk);
        btnLogIn.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                if (LogIn()) {
                    Intent intent = new Intent(v.getContext(), MainMenu.class);
                    startActivityForResult(intent, 0);
                } else {
                    Toast.makeText(getApplicationContext(), "Username and password don't match",
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    public boolean LogIn(){
        boolean ret = false;
        String username = ((EditText)findViewById(R.id.txtUser)).getText().toString();
        String password = ((EditText)findViewById(R.id.txtPassword)).getText().toString();
        Call<String> callObject = userLogIn.logIn(username, password, null);
        if (!callObject.equals(null)){
            try {
                String logIn = callObject.get();
                ret = Boolean.parseBoolean(logIn);
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ExecutionException e) {
                e.printStackTrace();
            }
        }
        return ret;
    }

    public void btnRegisterAction(){
        Button btnRegister = (Button)findViewById(R.id.btnRegister);
        btnRegister.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Register.class);
                startActivityForResult(intent, 0);
            }
        });
    }

    protected void setUp() throws Exception {
        // Instantiate a controller
        MagnetMobileClient magnetClient = MagnetMobileClient.getInstance(this.getApplicationContext());
        UserLogInFactory controllerFactory = new UserLogInFactory(magnetClient);
        userLogIn = controllerFactory.obtainInstance();
    }
}
