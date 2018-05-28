package com.nucleustech.mymentor.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.nucleustech.mymentor.R;
import com.nucleustech.mymentor.model.User;
import com.nucleustech.mymentor.model.UserClass;
import com.nucleustech.mymentor.util.AlertDialogCallBack;
import com.nucleustech.mymentor.util.Util;
import com.nucleustech.mymentor.volley.ServerResponseCallback;
import com.nucleustech.mymentor.volley.VolleyTaskManager;

import org.json.JSONObject;

import java.util.HashMap;

/**
 * Created by ritwik.rai on 28/11/17.
 */

public class SigninActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener, ServerResponseCallback {

    private Context mContext;

    //Firebase and GoogleApiClient
    private GoogleApiClient mGoogleApiClient;
    private FirebaseAuth mFirebaseAuth;
    private static final int RC_SIGN_IN = 9001;
    private String TAG = getClass().getSimpleName();

    private ProgressDialog mProgressDialog;
    private VolleyTaskManager volleyTaskManager;
    private String phoneNumber = "";
    /*private FirebaseAuth.AuthStateListener mAuthListener;
    CallbackManager mCallbackManager;
    LoginButton mFacebookSignInButton;*/

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        mContext = SigninActivity.this;

        //Google signin manager options
        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this, this)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();
        // Facebook Login
        /*FacebookSdk.sdkInitialize(getApplicationContext());
        AppEventsLogger.activateApp(this);
        mFacebookSignInButton = (LoginButton) findViewById(R.id.login_button);
        mCallbackManager = CallbackManager.Factory.create();
        mFacebookSignInButton.setReadPermissions("email");*/
        /*mFacebookSignInButton.registerCallback(mCallbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {

                Log.e(TAG, "Hello" + loginResult.getAccessToken().getToken());
                //  Toast.makeText(OldMainActivity.this, "Token:"+loginResult.getAccessToken(), Toast.LENGTH_SHORT).show();

                handleFacebookAccessToken(loginResult.getAccessToken());
            }

            @Override
            public void onCancel() {

            }

            @Override
            public void onError(FacebookException error) {

            }
        });


        mAuthListener = new FirebaseAuth.AuthStateListener() {


            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {

                FirebaseUser user = firebaseAuth.getCurrentUser();

                if (user != null) {
                    String name = user.getDisplayName();
                    Toast.makeText(mContext, "" + user.getDisplayName(), Toast.LENGTH_LONG).show();
                } else {
                    Toast.makeText(mContext, "something went wrong", Toast.LENGTH_LONG).show();
                }


            }
        };*/


        // Initialize FirebaseAuth
        mFirebaseAuth = FirebaseAuth.getInstance();

        mProgressDialog = new ProgressDialog(mContext);
        mProgressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        mProgressDialog.setCancelable(false);
        mProgressDialog.setMessage("Loading...");

        phoneNumber = getIntent().getStringExtra("phone");

        volleyTaskManager = new VolleyTaskManager(mContext);
    }


    public void onGoogleClick(View v) {
        mFirebaseAuth.signOut();
        //startActivity(new Intent(mContext, WalkthroughActivity.class));

        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);

    }


    public void onFacebookClick(View v) {
        //mFacebookSignInButton.performClick();
        Toast.makeText(mContext, "Not Available for Admin App.", Toast.LENGTH_LONG).show();

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            if (result.isSuccess()) {
                Toast.makeText(mContext, "Google Account Verified.", Toast.LENGTH_SHORT).show();
                GoogleSignInAccount account = result.getSignInAccount();

                showProgressDialog();
                firebaseAuthWithGoogle(account);
            } else {
                Log.e("Signin", "Google Sign In failed.");
                Toast.makeText(mContext, "Google Sign In failed.", Toast.LENGTH_SHORT).show();
            }
        } /*else {
            //Pass the activity result back to the Facebook SDK
            mCallbackManager.onActivityResult(requestCode, resultCode, data);
        }*/
    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {
        Log.e("SignIn", "onConnectionFailed:" + connectionResult);
        Util.initToast(this, "Google Play Services error.");
        hideProgressDialog();
    }

   /* private void handleFacebookAccessToken(AccessToken token) {
        Log.e(TAG, "handleFacebookAccessToken:" + token);

        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if (task.isSuccessful()) {
                            Log.e(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(mContext, "Facebook Account Verified.", Toast.LENGTH_SHORT).show();
                            mDatabase = FirebaseDatabase.getInstance().getReference();
                            addUserToDatabase(FirebaseAuth.getInstance().getCurrentUser());
                            HashMap<String, String> requestMap = new HashMap<>();
                            requestMap.put("emailID", "" + mFirebaseAuth.getCurrentUser().getEmail());
                            requestMap.put("mobile", "" + phoneNumber);
                            requestMap.put("name", "" + mFirebaseAuth.getCurrentUser().getDisplayName());
                            requestMap.put("fid", mFirebaseAuth.getCurrentUser().getUid());
                            Log.e("firebaseId", "U Id: " + mFirebaseAuth.getCurrentUser().getUid());
                            volleyTaskManager.doRegistration(requestMap, false);
                        } else {
                            Toast.makeText(mContext, "Facebook Sign In failed.", Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }*/

    private GoogleSignInAccount acct;

    private void firebaseAuthWithGoogle(final GoogleSignInAccount acct) {

        this.acct = acct;
        AuthCredential credential = GoogleAuthProvider.getCredential(acct.getIdToken(), null);
        mFirebaseAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Log.e(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        if (!task.isSuccessful()) {
                            hideProgressDialog();
                            Log.e(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(mContext, "Authentication failed", Toast.LENGTH_SHORT).show();
                            // Util.initToast(mContext, "Authentication failed");
                        } else {
                            mDatabase = FirebaseDatabase.getInstance().getReference();
                            addUserToDatabase(FirebaseAuth.getInstance().getCurrentUser());
                            String instanceId = FirebaseInstanceId.getInstance().getToken();
                            HashMap<String, String> requestMap = new HashMap<>();
                            requestMap.put("emailID", "" + acct.getEmail());
                            requestMap.put("mobile", "" + Util.fetchUserClass(mContext).getPhone());
                            requestMap.put("name", "" + acct.getDisplayName());
                            requestMap.put("fid", mFirebaseAuth.getCurrentUser().getUid());
                            requestMap.put("deviceToken",""+instanceId);
                            Log.e("firebaseId", "U Id: " + mFirebaseAuth.getCurrentUser().getUid());
                            volleyTaskManager.doRegistration(requestMap, true);
                            //Toast.makeText(mContext,"Logging in! Please wait...",Toast.LENGTH_SHORT).show();

                        }
                    }
                });
    }

    public void showProgressDialog() {
        if (!mProgressDialog.isShowing())
            mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog.isShowing())
            mProgressDialog.dismiss();
    }

    @Override
    public void onSuccess(JSONObject resultJsonObject) {

        hideProgressDialog();

        if (resultJsonObject.optString("code").equalsIgnoreCase("200")) {
            UserClass userClass = Util.fetchUserClass(mContext);
            if (userClass == null)
                userClass = new UserClass();

            userClass.displayName = "" + mFirebaseAuth.getCurrentUser().getDisplayName();
            userClass.profileUrl = "" + mFirebaseAuth.getCurrentUser().getPhotoUrl();
            userClass.setEmail("" + mFirebaseAuth.getCurrentUser().getEmail());
            userClass.firebaseId = "" + mFirebaseAuth.getCurrentUser().getUid();
            userClass.setUserId(resultJsonObject.optJSONObject("userData").optString("userID"));
            Util.saveUserClass(mContext, userClass);

            if (userClass.getEmail().equalsIgnoreCase("ritwikrai04@gmail.com") || userClass.getEmail().equalsIgnoreCase("nucleustech01@gmail.com") ||
                    userClass.getEmail().equalsIgnoreCase("shiv@mohanoverseas.in")) {

                Intent intent = new Intent(mContext, SelectStudentActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();

            } else {

                mFirebaseAuth.signOut();
                Auth.GoogleSignInApi.signOut(mGoogleApiClient);

                Util.showMessageWithOk(SigninActivity.this, "Please Signin using admin credentials.");
            }


        }

       else if (resultJsonObject.optString("code").equalsIgnoreCase("201")) {

            Util.showCallBackMessageWithOkCallback(mContext, "Please use the correct phone and email.", new AlertDialogCallBack() {
                @Override
                public void onSubmit() {
                    mFirebaseAuth.signOut();
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                    Intent intent= new Intent(mContext, OTPActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onCancel() {

                }
            });
        }
        else if (resultJsonObject.optString("code").equalsIgnoreCase("202")) {

            Util.showCallBackMessageWithOkCallback(mContext, "Please use the correct phone and email.", new AlertDialogCallBack() {
                @Override
                public void onSubmit() {
                    mFirebaseAuth.signOut();
                    Auth.GoogleSignInApi.signOut(mGoogleApiClient);
                    Intent intent= new Intent(mContext, OTPActivity.class);
                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
                    startActivity(intent);
                    finish();
                }

                @Override
                public void onCancel() {

                }
            });


        }


        else {
            Toast.makeText(mContext, "Something went wrong please try again.", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onError() {

    }

    private DatabaseReference mDatabase;

    private void addUserToDatabase(FirebaseUser firebaseUser) {
        User user = new User(
                firebaseUser.getDisplayName(),
                firebaseUser.getEmail(),
                firebaseUser.getUid(),
                firebaseUser.getPhotoUrl() == null ? "" : firebaseUser.getPhotoUrl().toString()
        );

        mDatabase.child("users")
                .child(user.getUid()).setValue(user);

        String instanceId = FirebaseInstanceId.getInstance().getToken();
        if (instanceId != null) {
            mDatabase.child("users")
                    .child(firebaseUser.getUid())
                    .child("instanceId")
                    .setValue(instanceId);
        }
    }
}
