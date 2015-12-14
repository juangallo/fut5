package com.ort.num172159_180968.fut5;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;

import com.facebook.login.LoginManager;
import com.facebook.share.ShareApi;
import com.facebook.share.model.ShareLinkContent;
import com.flask.colorpicker.ColorPickerView;
import com.flask.colorpicker.OnColorSelectedListener;
import com.flask.colorpicker.builder.ColorPickerClickListener;
import com.flask.colorpicker.builder.ColorPickerDialogBuilder;
import com.magnet.android.mms.MagnetMobileClient;
import com.magnet.android.mms.async.Call;
import com.ort.num172159_180968.fut5.controller.api.Fields;
import com.ort.num172159_180968.fut5.controller.api.FieldsFactory;
import com.ort.num172159_180968.fut5.model.beans.FieldsResult;
import com.ort.num172159_180968.fut5.model.persistance.DatabaseHelper;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class MainMenu extends AppCompatActivity {

    private String id_facebook;
    private String user_name;
    private String last_name;
    private Fields field;
    private DatabaseHelper db;
    SessionManager session;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_menu);

        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        String username = user.get(SessionManager.KEY_USERNAME);

        changeBackgroundColor();
        System.out.println("username en el shared preferences menu: " + username);



        id_facebook = getIntent().getStringExtra("id_fb");
        user_name = getIntent().getStringExtra("user_name");
        last_name = getIntent().getStringExtra("last_name");

        new AsyncCaller().execute();

        ImageButton btnMaps = (ImageButton)findViewById(R.id.btnMaps);
        btnMaps.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), MapsActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        ImageButton btnSelect = (ImageButton)findViewById(R.id.btnNew);
        btnSelect.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), SelectTeamActivity.class);
                startActivityForResult(intent, 0);
            }
        });

        ImageButton btnUser = (ImageButton)findViewById(R.id.btnUser);
        btnUser.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Profile.class);
                intent.putExtra("id_fb", id_facebook);
                intent.putExtra("user_name", user_name);
                intent.putExtra("last_name", last_name);
                startActivityForResult(intent, 0);
            }
        });

        ImageButton btnStatistics = (ImageButton)findViewById(R.id.btnStadistics);
        btnStatistics.setOnClickListener(new View.OnClickListener() {

            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Statistics.class);
                startActivityForResult(intent, 0);
            }
        });

        ImageButton btnViewMatches = (ImageButton) findViewById(R.id.btnSearch);
        btnViewMatches.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(v.getContext(), ViewMatches.class);
                startActivityForResult(intent,0);
            }
        });

        ImageButton btnNotifications = (ImageButton) findViewById(R.id.btnNotification);
        btnNotifications.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Intent intent = new Intent(v.getContext(), Notification.class);
                startActivityForResult(intent,0);
            }
        });
        /*if(id_facebook != null){
            share_facebook();
        }*/
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main_menu, menu);
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
        if(id == R.id.action_logout){
            session.logoutUser();
            finish();
            //return true;
        }
        if (id == R.id.app_color) {
            final Context context = MainMenu.this;

            ColorPickerDialogBuilder
                    .with(context)
                    .setTitle("Choose color")
                    .initialColor(Color.WHITE)
                    .wheelType(ColorPickerView.WHEEL_TYPE.FLOWER)
                    .density(12)
                    .setOnColorSelectedListener(new OnColorSelectedListener() {
                        @Override
                        public void onColorSelected(int selectedColor) {
                            System.out.println(selectedColor);
                            session.createColor(selectedColor);
                        }
                    })
                    .setPositiveButton("Ok", new ColorPickerClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int selectedColor, Integer[] allColors) {
                            changeBackgroundColor();
                            if (allColors != null) {
                                StringBuilder sb = null;

                                for (Integer color : allColors) {
                                    if (color == null)
                                        continue;
                                    if (sb == null)
                                        sb = new StringBuilder("Color List:");
                                    sb.append("\r\n#" + Integer.toHexString(color).toUpperCase());
                                }

                            }
                        }
                    })
                    .setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                        }
                    })
                    .build()
                    .show();
            changeBackgroundColor();
        }

        return super.onOptionsItemSelected(item);
    }


    private void changeBackgroundColor() {
        HashMap<String, Integer> color = session.getColorDetail();
        int colorId = color.get(SessionManager.KEY_COLOR);
        System.out.println("colorId: " + colorId);
        ImageButton button = (ImageButton)findViewById(R.id.btnMaps);
        button.setColorFilter(colorId);
        button = (ImageButton)findViewById(R.id.btnNew);
        button.setColorFilter(colorId);
        button = (ImageButton)findViewById(R.id.btnNotification);
        button.setColorFilter(colorId);
        button = (ImageButton)findViewById(R.id.btnSearch);
        button.setColorFilter(colorId);
        button = (ImageButton)findViewById(R.id.btnStadistics);
        button.setColorFilter(colorId);
        button = (ImageButton)findViewById(R.id.btnUser);
        button.setColorFilter(colorId);

    }

    @Override
    protected void onDestroy(){
        super.onDestroy();
        db.closeDB();
    }

    public void share_facebook (){
        List<String> permissions_publish = new ArrayList<>();
        permissions_publish.add("publish_actions");
        LoginManager.getInstance().logInWithPublishPermissions(this, permissions_publish);
        System.out.println("estoy en el share");

        ShareLinkContent content = new ShareLinkContent.Builder()
                .setContentUrl(Uri.parse("https://developers.facebook.com"))
                .setContentDescription("Desde Fut5")
                .build();

        /*Bitmap image = BitmapFactory.decodeResource(getResources(), R.drawable.unknown);
        SharePhoto photo = new SharePhoto.Builder()
                .setBitmap(image)
                .build();
        SharePhotoContent content = new SharePhotoContent.Builder()
                .addPhoto(photo)
                .build();*/

        ShareApi.share(content, null);

    }

    protected void setUpField() throws Exception {
        // Instantiate a controller
        MagnetMobileClient magnetClient = MagnetMobileClient.getInstance(this.getApplicationContext());
        FieldsFactory controllerFactory = new FieldsFactory(magnetClient);
        field = controllerFactory.obtainInstance();
    }

    private class AsyncCaller extends AsyncTask<Void, Void, Void>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            try {
                db = new DatabaseHelper(getApplicationContext());
                setUpField();
            } catch (Exception e) {
                e.printStackTrace();
            }
            //this method will be running on UI thread
        }
        @Override
        protected Void doInBackground(Void... params) {
            try {
                AppHelper helper = new AppHelper(getApplicationContext());
                if (db.getAllUser().isEmpty() || helper.needsUserReload()) {
                    helper.reloadUsers();
                }

                Call<List<FieldsResult>> callObjectFields = field.getFields(null);
                List<FieldsResult> fields = callObjectFields.get();
                for (FieldsResult f : fields) {
                    com.ort.num172159_180968.fut5.model.persistance.Field fieldDb = new
                            com.ort.num172159_180968.fut5.model.persistance.Field(f.getFieldId(), f.getFieldName(), f.getFieldLat(),f.getFieldLon());
                    if (!db.existsField(f.getFieldId())) {
                        System.out.println(f.getFieldName());
                        long field1 = db.createField(fieldDb);
                        System.out.println(field1);
                    }else {
                        System.out.println("field already exists");
                        db.updateFields(fieldDb);
                    }
                }

            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
            //this method will be running on background thread so don't update UI frome here
            //do your long running http tasks here,you dont want to pass argument and u can access the parent class' variable url over here


            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);

            //this method will be running on UI thread
            db.closeDB();
        }

    }

}
