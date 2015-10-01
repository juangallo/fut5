package com.ort.num172159_180968.fut5;

import android.app.Application;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.util.Base64;
import android.util.Log;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/**
 * Created by JuanMartin on 9/27/2015.
 */
public class MyApplication  extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        printHasKey();
    }

    public void printHasKey() {
        try {
            PackageInfo info = getPackageManager().getPackageInfo(
                    "com.ort.num172159_180968.fut5",
                    PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                Log.d("JMG:", Base64.encodeToString(md.digest(), Base64.DEFAULT));
            }
        } catch (PackageManager.NameNotFoundException e) {

        } catch (NoSuchAlgorithmException e) {

        }
    }
}
