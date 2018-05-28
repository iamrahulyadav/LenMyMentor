package com.nucleustech.mymentor.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import com.nucleustech.mymentor.R;
import com.nucleustech.mymentor.adapter.WalkthroughAdapter;
import com.nucleustech.mymentor.model.UserClass;
import com.nucleustech.mymentor.util.Util;

/**
 * Created by ritwik.rai on 28/11/17.
 */

public class WalkthroughActivity extends AppCompatActivity {

    ViewPager myPager;
    private Context mContext;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_walkthrough);
        mContext = WalkthroughActivity.this;

        WalkthroughAdapter adapter = new WalkthroughAdapter();
        myPager = (ViewPager) findViewById(R.id.viewpager_layout);

        myPager.setAdapter(adapter);
        myPager.setCurrentItem(0);

    }

    public void jumpToNextPage(View view) {

        myPager.setCurrentItem(myPager.getCurrentItem() + 1, true);
        //Toast.makeText(mContext, "Current Item: " + myPager.getCurrentItem(), Toast.LENGTH_SHORT).show();

        if (myPager.getCurrentItem() == 3 && count != 1) {
            //Toast.makeText(mContext, "Move to the next Activity.", Toast.LENGTH_SHORT).show();
            //Move to the next Activity
            count = 1;


        } else if (myPager.getCurrentItem() == 3 && count == 1) {
            gotoLandingScreen();
        }

    }

    public void jumpToPreviousPage(View view) {

        myPager.setCurrentItem(myPager.getCurrentItem() - 1, true);
    }


    public void onSkipClicked(View view) {
        gotoLandingScreen();

    }
    private void gotoLandingScreen(){
        UserClass userClass = Util.fetchUserClass(mContext);
        if (userClass.getEmail().equalsIgnoreCase("ritwikrai04@gmail.com") || userClass.getEmail().equalsIgnoreCase("nucleustech01@gmail.com")||
                userClass.getEmail().equalsIgnoreCase("shiv@mohanoverseas.in")) {

            Intent intent = new Intent(mContext, SelectStudentActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();

        }
    }
}
