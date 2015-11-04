package com.ort.num172159_180968.fut5;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageInstaller;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

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

import org.json.JSONException;

import java.sql.Array;
import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.List;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FacebookButton.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FacebookButton#newInstance} factory method to
 * create an instance of this fragment.
 */

public class FacebookButton extends Fragment {


    private AccessTokenTracker mTokenTracker;
    private ProfileTracker mProfileTracker;

    private CallbackManager mCallBackManager;

    private FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
            setupProfileTracker();



            /*GraphRequest graph = new GraphRequest();
            graph.setAccessToken(accessToken);
            graph.setGraphPath("/me/friends");
            graph.setParameters(null);
            graph.setHttpMethod(HttpMethod.GET);*/



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
        /*LoginButton loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");
        loginButton.setFragment(this);
        loginButton.registerCallback(mCallBackManager, mCallback);*/

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
        mCallBackManager.onActivityResult(requestCode,resultCode,data);
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
        //mButtonLogin.setReadPermissions("user_friends");
        //mButtonLogin.setReadPermissions("public_profile");

        mButtonLogin.setReadPermissions(permissions);
        //mButtonLogin.setPublishPermissions(permissions);
        mButtonLogin.registerCallback(mCallBackManager, mCallback);

    }

    private String constructWelcomeMessage(Profile profile) {
        StringBuffer stringBuffer = new StringBuffer();
        if (profile != null) {
            stringBuffer.append("Welcome " + profile.getName());
        }
        return stringBuffer.toString();
    }





}