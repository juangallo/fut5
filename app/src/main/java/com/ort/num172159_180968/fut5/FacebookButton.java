package com.ort.num172159_180968.fut5;

import android.app.Activity;
import android.content.Intent;
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
import com.facebook.Profile;
import com.facebook.ProfileTracker;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.facebook.login.widget.ProfilePictureView;


/**
 * A simple {@link Fragment} subclass.
 * Activities that contain this fragment must implement the
 * {@link FacebookButton.OnFragmentInteractionListener} interface
 * to handle interaction events.
 * Use the {@link FacebookButton#newInstance} factory method to
 * create an instance of this fragment.
 */

public class FacebookButton extends Fragment {

    private ProfilePictureView mProfilePicture;

    private TextView mTextDetails;
    private ImageButton mImgProfile;

    private AccessTokenTracker mTokenTracker;
    private ProfileTracker mProfileTracker;

    private CallbackManager mCallBackManager;

    private FacebookCallback<LoginResult> mCallback = new FacebookCallback<LoginResult>() {
        @Override
        public void onSuccess(LoginResult loginResult) {
            AccessToken accessToken = loginResult.getAccessToken();
            Profile profile = Profile.getCurrentProfile();
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
        /*super.onViewCreated(view, savedInstanceState);
        LoginButton loginButton = (LoginButton) view.findViewById(R.id.login_button);
        loginButton.setReadPermissions("user_friends");
        loginButton.setFragment(this);
        loginButton.registerCallback(mCallBackManager, mCallback);*/

        setupLoginButton(view);
        //setupTextDetails(view);
        //setupProfilePicture(view);

    }

    @Override
    public void onResume() {
        super.onResume();
        Profile profile = Profile.getCurrentProfile();
        /*mTextDetails.setText(constructWelcomeMessage(profile));
        if(profile != null){
            mProfilePicture.setProfileId(profile.getId());
        }*/
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

    /*private void setupTextDetails(View view) {
        mTextDetails = (TextView) view.findViewById(R.id.txtLogin);
    }

    private void setupProfilePicture(View view){
        mProfilePicture = (ProfilePictureView) view.findViewById(R.id.profilePicture);

    }*/

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

                /*mTextDetails.setText(constructWelcomeMessage(currentProfile));
                if(currentProfile != null){
                    mProfilePicture.setProfileId(currentProfile.getId());
                } else {
                    mProfilePicture.setProfileId(null);
                }*/

            }
        };
    }

    private void setupLoginButton(View view) {
        LoginButton mButtonLogin = (LoginButton) view.findViewById(R.id.login_button);
        mButtonLogin.setFragment(this);
        //mButtonLogin.setReadPermissions("user_friends");
        mButtonLogin.setReadPermissions("public_profile");
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