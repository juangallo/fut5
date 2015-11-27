package com.ort.num172159_180968.fut5;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.magnet.android.mms.MagnetMobileClient;
import com.magnet.android.mms.async.Call;
import com.ort.num172159_180968.fut5.controller.api.UserFactory;
import com.ort.num172159_180968.fut5.controller.api.UserNotification;
import com.ort.num172159_180968.fut5.controller.api.UserNotificationFactory;
import com.ort.num172159_180968.fut5.model.beans.NotificationsResult;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Notification extends AppCompatActivity {

    private List<NotificationsResult> notifications;
    private UserNotification userNotification;
    private String username;
    private SessionManager session;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        session = new SessionManager(getApplicationContext());
        HashMap<String, String> user = session.getUserDetails();
        username = user.get(SessionManager.KEY_USERNAME);
        try {
            setUpUserNotification();
            Call<List<NotificationsResult>> callObject = userNotification.getNotifications(username, null);
            if (callObject != null) {
                notifications = callObject.get();
            } else {
                notifications = new ArrayList<>();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        setContentView(R.layout.activity_notification);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        populateListView();
    }


    private void populateListView() {
        ArrayAdapter<NotificationsResult> adapter = new MyListAdapter();
        ListView list = (ListView) findViewById(R.id.lstNotifications);
        list.setAdapter(adapter);
    }

    private class MyListAdapter extends ArrayAdapter<NotificationsResult> {
        public MyListAdapter(){
            super(Notification.this, R.layout.notification_item, notifications);
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            //make sure we have a view to work with (may be null)
            View itemView = convertView;
            if (itemView == null) {
                itemView = getLayoutInflater().inflate(R.layout.notification_item, parent, false);
            }

            //Find the car to work with
            NotificationsResult notificationRes = notifications.get(position);
            //Fill the view
            //missing the image view
            TextView notificationText = (TextView)itemView.findViewById(R.id.txtNotification);
            notificationText.setText(notificationRes.getNotificationText());

            //Bitmap image = null;

            //String imageString = userRes.getPhoto();
            //byte[] decodedString = Base64.decode(imageString, Base64.DEFAULT);

            //image = helper.decodeSampledBitmapFromByte(decodedString, 80, 80);
            int notificationImageId = getPhotoIdFromNotificationType(notificationRes.getNotificationType());
            ImageView notificationImage = (ImageView) itemView.findViewById(R.id.imgNotificationIcon);
            if (notificationImageId != 0) {
                notificationImage.setImageResource(notificationImageId);
            } else {
                notificationImage.setImageResource(R.drawable.alertcolor);
            }
            return itemView;
        }
    }

    protected void setUpUserNotification() throws Exception {
        // Instantiate a controller
        MagnetMobileClient magnetClient = MagnetMobileClient.getInstance(this.getApplicationContext());
        UserNotificationFactory controllerFactory = new UserNotificationFactory(magnetClient);
        userNotification = controllerFactory.obtainInstance();
    }

    private int getPhotoIdFromNotificationType(String notificationType) {
        int res = 0;
        switch(notificationType) {
            case "NEWMATCH":
                res = R.drawable.newmatch;
                break;
            case "MATCHCOMING":
                res = R.drawable.matchcoming;
                break;
            default:
                res = 0;
        }
        return res;
    }
}
