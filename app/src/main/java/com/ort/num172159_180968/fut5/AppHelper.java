package com.ort.num172159_180968.fut5;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Matrix;
import android.util.Base64;

import com.magnet.android.mms.MagnetMobileClient;
import com.magnet.android.mms.async.Call;
import com.ort.num172159_180968.fut5.controller.api.UserFactory;
import com.ort.num172159_180968.fut5.model.beans.UsersWithImagesResult;
import com.ort.num172159_180968.fut5.model.persistance.DatabaseHelper;
import com.ort.num172159_180968.fut5.model.persistance.User;

import java.io.ByteArrayOutputStream;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.ExecutionException;

public class AppHelper {
    private Context context;
    private DatabaseHelper db;
    private User dbUser;
    private com.ort.num172159_180968.fut5.controller.api.User user;
    private SessionManager session;


    public AppHelper(Context context){
        this.context = context;
        session = new SessionManager(context);
    }

    public boolean needsUserReload() {
        boolean res;
        try {
            HashMap<String, String> userSession = session.getUserDetails();
            String username = userSession.get(SessionManager.KEY_USERNAME);
            setUpUser();
            Call<String> callObject = user.getNeedsUserReload(username, null);
            if (callObject != null) {
                res = Boolean.parseBoolean(callObject.get());
            } else {
                res = false;
            }
        } catch (Exception e) {
            res = false;
        }
        System.out.println("res:::" + res);
        return res;
    }

    public void reloadUsers(){
        db = new DatabaseHelper(context);
        System.out.println("in reload users");
        try {
            setUpUser();
            HashMap<String, String> userSession = session.getUserDetails();
            String username = userSession.get(SessionManager.KEY_USERNAME);
            Call<List<UsersWithImagesResult>> callObject = user.getUsersWithImages(username, null);
            List<UsersWithImagesResult> users = callObject.get();
            db.deleteAllUsers();
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
        db.closeDB();
    }

    private void setUpUser() throws Exception {
        // Instantiate a controller
        MagnetMobileClient magnetClient = MagnetMobileClient.getInstance(this.context);
        UserFactory controllerFactory = new UserFactory(magnetClient);
        user = controllerFactory.obtainInstance();
    }

    public Bitmap getResizedBitmap(Bitmap bm, int newHeight, int newWidth) {
        int width = bm.getWidth();
        int height = bm.getHeight();
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;
        Matrix matrix = new Matrix();
        // RESIZE THE BIT MAP
        matrix.postScale(scaleWidth, scaleHeight);
        // RECREATE THE NEW BITMAP
        return Bitmap.createBitmap(bm, 0, 0, width, height, matrix, false);
    }

    public static int calculateInSampleSize(
            BitmapFactory.Options options, int reqWidth, int reqHeight) {
        // Raw height and width of image
        final int height = options.outHeight;
        final int width = options.outWidth;
        int inSampleSize = 1;

        if (height > reqHeight || width > reqWidth) {

            final int halfHeight = height / 2;
            final int halfWidth = width / 2;

            // Calculate the largest inSampleSize value that is a power of 2 and keeps both
            // height and width larger than the requested height and width.
            while ((halfHeight / inSampleSize) > reqHeight
                    && (halfWidth / inSampleSize) > reqWidth) {
                inSampleSize *= 2;
            }
        }
        return inSampleSize;
    }

    public static Bitmap decodeSampledBitmapFromByte(byte[] bytes, int reqWidth, int reqHeight) {

        // First decode with inJustDecodeBounds=true to check dimensions
        final BitmapFactory.Options options = new BitmapFactory.Options();
        options.inJustDecodeBounds = true;
        BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);

        // Calculate inSampleSize
        options.inSampleSize = calculateInSampleSize(options, reqWidth, reqHeight);

        // Decode bitmap with inSampleSize set
        options.inJustDecodeBounds = false;
        return BitmapFactory.decodeByteArray(bytes, 0, bytes.length, options);
    }

    public String BitMapToString(Bitmap bitmap){
        if(bitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            return Base64.encodeToString(b, Base64.DEFAULT);
        }else{
            return "";
        }
    }
}
