package com.nucleustech.mymentor.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.nucleustech.mymentor.R;

/**
 * Created by ritwik.rai on 28/11/17.
 */

public class IntroActivity extends AppCompatActivity {

    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_first);
        mContext= IntroActivity.this;
    }

    public void onSignupClick(View view){

        startActivity(new Intent(mContext, OTPActivity.class));
        finish();
    }
}
