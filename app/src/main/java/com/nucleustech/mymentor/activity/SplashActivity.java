package com.nucleustech.mymentor.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.nucleustech.mymentor.R;
import com.nucleustech.mymentor.model.UserClass;
import com.nucleustech.mymentor.util.Util;

/**
 * Created by ritwik on 28/11/17.
 */

public class SplashActivity extends AppCompatActivity {

    private FirebaseAuth mFirebaseAuth;

    private FirebaseUser mFirebaseUser;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_splash);
        new SplashTimerTask().execute();

    }

    /**
     * The Splash Timer. duration ---> 2345ms
     *
     * @params {@link AsyncTask}
     */
    private class SplashTimerTask extends AsyncTask<Void, Void, Void> {

        private ProgressDialog mProgressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            mProgressDialog = new ProgressDialog(SplashActivity.this);
            mProgressDialog.setCancelable(false);
            mProgressDialog.setMessage("Loading! Please wait...");
            mProgressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... params) {

            try {
                Thread.sleep(2345);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            if (mProgressDialog.isShowing())
                mProgressDialog.dismiss();
            verifyUserLogin();
        }
    }

    /**
     * Open IntroActivity.
     */
    private void openIntroActivity() {

        Intent intent = new Intent(SplashActivity.this, IntroActivity.class);
        startActivity(intent);
        finish();
    }

    /**
     * Open LoginActivity.
     */
   /* public void openLoginActivity() {

        Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();

    }*/

    /**
     * Verify user is logged in
     */
    private void verifyUserLogin() {

        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            openIntroActivity();
        } else {

            UserClass userClass = Util.fetchUserClass(SplashActivity.this);
            if (userClass == null)
                userClass = new UserClass();
            userClass.displayName = "" + mFirebaseUser.getDisplayName();
            userClass.profileUrl = "" + mFirebaseUser.getPhotoUrl();
            userClass.setEmail("" + mFirebaseUser.getEmail());
            userClass.firebaseId="" + mFirebaseUser.getUid();
            Util.saveUserClass(SplashActivity.this, userClass);

            if (userClass.getEmail().equalsIgnoreCase("ritwikrai04@gmail.com") || userClass.getEmail().equalsIgnoreCase("nucleustech01@gmail.com")||
                    userClass.getEmail().equalsIgnoreCase("shiv@mohanoverseas.in")) {
                Intent intent = new Intent(SplashActivity.this, SelectStudentActivity.class);

                intent.putExtra("name", "" + mFirebaseUser.getDisplayName());
                startActivity(intent);
                finish();
            }
        }
    }

}
