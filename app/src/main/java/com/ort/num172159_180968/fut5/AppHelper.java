package com.ort.num172159_180968.fut5;

import android.content.Context;

import com.magnet.android.mms.MagnetMobileClient;
import com.magnet.android.mms.async.Call;
import com.ort.num172159_180968.fut5.controller.api.UserFactory;
import com.ort.num172159_180968.fut5.model.beans.UsersWithImagesResult;
import com.ort.num172159_180968.fut5.model.persistance.DatabaseHelper;
import com.ort.num172159_180968.fut5.model.persistance.User;

import java.util.List;
import java.util.concurrent.ExecutionException;

/**
 * Created by JuanMartin on 22-Nov-15.
 */
public class AppHelper {
    private Context context;
    private DatabaseHelper db;
    private User dbUser;
    private com.ort.num172159_180968.fut5.controller.api.User user;


    public AppHelper(Context context){
        this.context = context;
    }

    public void reloadUsers(){
        db = new DatabaseHelper(context);
        try {
            setUpUser();
            Call<List<UsersWithImagesResult>> callObject = user.getUsersWithImages(null);
            List<UsersWithImagesResult> users = callObject.get();
            for (UsersWithImagesResult u : users) {

                dbUser = new User(u.getUserId(), u.getUsername(), u.getFirstName(), u.getLastName(), u.getEmail(), u.getPhoto());
                if (db.existsUser(u.getUserId())) {
                    db.updateUser(dbUser);
                } else {
                    db.createUser(dbUser);
                }
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void setUpUser() throws Exception {
        // Instantiate a controller
        MagnetMobileClient magnetClient = MagnetMobileClient.getInstance(this.context);
        UserFactory controllerFactory = new UserFactory(magnetClient);
        user = controllerFactory.obtainInstance();
    }
}
