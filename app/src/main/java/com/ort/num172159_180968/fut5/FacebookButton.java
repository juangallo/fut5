package com.ort.num172159_180968.fut5;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.AccessToken;
import com.facebook.AccessTokenTracker;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.GraphRequest;
import com.facebook.GraphResponse;
import com.facebook.HttpMethod;
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginManager;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;
import com.magnet.android.mms.MagnetMobileClient;
import com.magnet.android.mms.async.Call;
import com.ort.num172159_180968.fut5.controller.api.User;
import com.ort.num172159_180968.fut5.controller.api.UserExists;
import com.ort.num172159_180968.fut5.controller.api.UserExistsFactory;
import com.ort.num172159_180968.fut5.controller.api.UserFactory;
import com.ort.num172159_180968.fut5.model.beans.AddUserImageRequest;
import com.ort.num172159_180968.fut5.model.beans.UserResult;
//import com.squareup.picasso.Picasso;

import org.json.JSONException;
import org.json.JSONObject;


import java.io.BufferedInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FacebookButton.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FacebookButton#newInstance} factory method to
 * create an instance of this fragment.
 */

public class FacebookButton extends Fragment {

    private String user_email;
    private UserExists userExists;
    private User userRegister;

    private AccessTokenTracker mTokenTracker;
    private ProfileTracker mProfileTracker;

    private CallbackManager mCallBackManager;

    private SessionManager session;

    private FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();

            GraphRequest request = GraphRequest.newMeRequest(AccessToken.getCurrentAccessToken(),
                    new GraphRequest.GraphJSONObjectCallback() {
                        public void onCompleted(JSONObject object, GraphResponse response) {
                            try {
                                user_email = object.getString("email");
                                System.out.println("email: " + user_email);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                        }
                    }
            );
            Bundle parameters = new Bundle();
            parameters.putString("fields", "email");
            request.setParameters(parameters);
            request.executeAsync();

            setupProfileTracker();

        }

        @Override
        public void onCancel() {

        }

        @Override
        public void onError(FacebookException e) {
        }
    };

    public FacebookButton() {
        //LoginManager.getInstance().logOut();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());

        session = new SessionManager(getActivity().getApplicationContext());

        mCallBackManager = CallbackManager.Factory.create();
        setupTokenTracker();
        setupProfileTracker();

        mTokenTracker.startTracking();
        mProfileTracker.startTracking();

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_facebook_button, container, false);

    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        setupLoginButton(view);

    }

    @Override
    public void onResume() {
        super.onResume();
        FacebookSdk.sdkInitialize(getActivity().getApplicationContext());

        Profile profile = Profile.getCurrentProfile();

        if(profile != null) {
            Intent intent = new Intent(getActivity(),MainMenu.class);
            intent.putExtra("id_fb", profile.getId());
            intent.putExtra("user_name", profile.getFirstName());
            intent.putExtra("last_name", profile.getLastName());
            //intent.putExtra("profile",profile);
            startActivity(intent);

        }
    }

    @Override
    public void onStop() {
        super.onStop();
        mTokenTracker.stopTracking();
        mProfileTracker.stopTracking();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        mCallBackManager.onActivityResult(requestCode, resultCode, data);
    }

    private void setupTokenTracker() {
        mTokenTracker = new AccessTokenTracker() {
            @Override
            protected void onCurrentAccessTokenChanged(AccessToken oldAccessToken, AccessToken currentAccessToken) {
                //Log.d("VIVZ", "" + currentAccessToken);
            }
        };
    }

    private void setupProfileTracker() {
        mProfileTracker = new ProfileTracker() {
            @Override
            protected void onCurrentProfileChanged(Profile oldProfile, Profile currentProfile) {
                //Log.d("VIVZ", "" + currentProfile);


                if(currentProfile != null) {

                    Intent intent = new Intent(getContext(),MainMenu.class);
                    intent.putExtra("id_fb", currentProfile.getId());
                    intent.putExtra("user_name", currentProfile.getFirstName());
                    intent.putExtra("last_name", currentProfile.getLastName());
                    intent.putExtra("profile", currentProfile);

                    addFacebookUser(currentProfile);
                    session.createLoginSession(currentProfile.getId(), user_email);

                    startActivity(intent);
                }

            }
        };
    }

    private void setupLoginButton(View view) {
        LoginButton mButtonLogin = (LoginButton) view.findViewById(R.id.login_button);
        mButtonLogin.setFragment(this);
        List<String> permissions = new ArrayList<>();
        permissions.add("user_friends");
        permissions.add("public_profile");
        permissions.add("email");

        mButtonLogin.setReadPermissions(permissions);

        /*ListWeather<String> permissions_publish = new ArrayList<>();
        permissions_publish.add("publish_actions");
        mButtonLogin.setPublishPermissions(permissions_publish);*/

        mButtonLogin.registerCallback(mCallBackManager, mCallback);

    }

    private String constructWelcomeMessage(Profile profile) {
        StringBuffer stringBuffer = new StringBuffer();
        if (profile != null) {
            stringBuffer.append("Welcome " + profile.getName());
        }
        return stringBuffer.toString();
    }


    private void addFacebookUser(Profile profile){

        try {
            setUpExistsUser();
            setUpRegisterUser();
        } catch (Exception e) {
            e.printStackTrace();
        }

        String username = profile.getId();
        String email = user_email;

        Call<String> callObject = userExists.userExists(username, null);
        try {
            String result = callObject.get();
            System.out.println(result);
            if (result.equals("false")) {
                System.out.println("datos " + username + profile.getFirstName() + profile.getLastName() + email);
                Call<UserResult> callObjectResult = userRegister.addUser(username, null, profile.getFirstName(), profile.getLastName(), email, "", null);

                //para subir la foto pasar a otro metodo
                //Uri uri = profile.getProfilePictureUri(512, 512);
                //System.out.println("foto en uri: " + profile.getProfilePictureUri(512, 512));

                //Bitmap bitmap = null;

                    //URL imgUrl = new URL(profile.getProfilePictureUri(512, 512).toString());
                   /* URL imgUrl = new URL("https://graph.facebook.com/" + profile.getId() + "/picture?type=large");
                    HttpURLConnection.setFollowRedirects(true);
                    bitmap = BitmapFactory.decodeStream(imgUrl.openConnection().getInputStream());*/


                    //URL imgUrl = new URL("https://graph.facebook.com/" + profile.getId() + "/picture?type=large");

               /* Uri uri = profile.getProfilePictureUri(512,512);
                System.out.println("uriel: " + uri);
                    ImageView i = new ImageView(this.getActivity().getApplicationContext());
                    i.setImageURI(uri);

                    bitmap = ((BitmapDrawable)i.getDrawable()).getBitmap();*/

               /* try {
                    Uri uri = profile.getProfilePictureUri(512, 512);
                    URL url = new URL(uri.toString());
                    HttpURLConnection ucon = (HttpURLConnection) url.openConnection();
                    ucon.setInstanceFollowRedirects(false);
                    URL secondURL = new URL(ucon.getHeaderField("Location"));
                    System.out.println("url para gallo: " + secondURL);
                } catch (MalformedURLException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }*/

                /*ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                bitmap.compress(Bitmap.CompressFormat.PNG, 100, byteArrayOutputStream);
                byte[] byteArray = byteArrayOutputStream .toByteArray();

                System.out.println("foto en byte: " + byteArray);

                String encoded = Base64.encodeToString(byteArray, Base64.DEFAULT);
                System.out.println("foto en string: " + encoded);

                AddUserImageRequest.AddUserImageRequestBuilder builder = new AddUserImageRequest.AddUserImageRequestBuilder();
                builder.image(encoded);
                AddUserImageRequest body = builder.build();
                userRegister.addUserImage(username, body, null);*/

                //fin

                /*ProfilePictureView faceBookProfilePictureView = new ProfilePictureView(getContext());
                faceBookProfilePictureView.setProfileId(profile.getId());
                Thread.sleep(1000);
                ImageView profileImageView = ((ImageView)faceBookProfilePictureView.getChildAt(0));
                Bitmap bitmap  = ((BitmapDrawable)profileImageView.getDrawable()).getBitmap();
                System.out.println("bitmaptoString: " + BitMapToString(bitmap));*/

                UserResult user = callObjectResult.get();
                Toast toast = Toast.makeText(getContext(), "User: " + user.getUsername() + " added correctly.", Toast.LENGTH_SHORT);
                toast.setGravity(Gravity.CENTER,0,0);
                toast.show();
                Intent intent = new Intent(getContext(), LogIn.class);
                startActivityForResult(intent, 0);
            }
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
    }

    protected void setUpExistsUser() throws Exception {
        // Instantiate a controller
        MagnetMobileClient magnetClient = MagnetMobileClient.getInstance(getContext());
        UserExistsFactory controllerFactory = new UserExistsFactory(magnetClient);
        userExists = controllerFactory.obtainInstance();
    }

    protected void setUpRegisterUser() throws Exception {
        // Instantiate a controller
        MagnetMobileClient magnetClient = MagnetMobileClient.getInstance(getContext());
        UserFactory controllerFactory = new UserFactory(magnetClient);
        userRegister = controllerFactory.obtainInstance();
    }

    private byte[] fromUriToByte(Uri uri){

        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        FileInputStream fis;
        try {
            fis = new FileInputStream(new File(uri.getPath()));
            byte[] buf = new byte[1024];
            int n;
            while (-1 != (n = fis.read(buf)))
                baos.write(buf, 0, n);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return baos.toByteArray();
    }
    public String BitMapToString(Bitmap bitmap){
        if(bitmap != null) {
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
            byte[] b = baos.toByteArray();
            String temp = Base64.encodeToString(b, Base64.DEFAULT);
            return temp;
        }else{
            return "";
        }
    }

}