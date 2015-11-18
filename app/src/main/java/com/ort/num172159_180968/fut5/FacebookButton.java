package com.ort.num172159_180968.fut5;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
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
import com.ort.num172159_180968.fut5.model.beans.UserResult;

import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Array;
import java.sql.SQLOutput;
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

                Intent intent = new Intent(getActivity(),MainMenu.class);
                if(currentProfile != null) {
                    intent.putExtra("id_fb", currentProfile.getId());
                    intent.putExtra("user_name", currentProfile.getFirstName());
                    intent.putExtra("last_name", currentProfile.getLastName());
                    intent.putExtra("profile",currentProfile);
                }

                addFacebookUser(currentProfile);
                session.createLoginSession(currentProfile.getId(),user_email);

                startActivity(intent);
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

        /*List<String> permissions_publish = new ArrayList<>();
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
                Call<UserResult> callObjectResult = userRegister.addUser(username, null, profile.getFirstName(), profile.getLastName(), email, "", null);
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


}