package com.nucleustech.mymentor.activity;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.nucleustech.mymentor.R;
import com.nucleustech.mymentor.adapter.CircleTransform;
import com.nucleustech.mymentor.model.UserClass;
import com.nucleustech.mymentor.util.Util;
import com.nucleustech.mymentor.volley.ServerResponseCallback;
import com.nucleustech.mymentor.volley.VolleyTaskManager;

import org.json.JSONObject;

/**
 * Created by ritwik.rai on 09/12/17.
 */

public class ProfileActivity extends AppCompatActivity implements ServerResponseCallback {

    private Context mContext;
    private VolleyTaskManager volleyTaskManager;
    TextView name, email, tv_phone;
    ImageView image;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);

        mContext = ProfileActivity.this;

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Profile");

        name = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.email);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        image=(ImageView)findViewById(R.id.image) ;

        /*volleyTaskManager= new VolleyTaskManager(mContext);

        volleyTaskManager*/
        UserClass userClass = Util.fetchUserClass(mContext);
        if (userClass != null) {
            name.setText("" + userClass.displayName);
            email.setText("" + userClass.getEmail());
            tv_phone.setText("" + userClass.getPhone());

            Glide.with(image.getContext()).load(userClass.profileUrl).centerCrop().transform(new CircleTransform(image.getContext())).override(50, 50).into(image);

        }
    }

    @Override
    public void onSuccess(JSONObject resultJsonObject) {

    }

    @Override
    public void onError() {

    }
}
