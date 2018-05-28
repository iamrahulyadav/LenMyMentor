package com.nucleustech.mymentor.notification;

import android.util.Log;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.nucleustech.mymentor.model.UserClass;
import com.nucleustech.mymentor.util.Util;

/*
 * Created by ritwik.rai on 3/13/2017.
 */

public class InstanceIdService extends FirebaseInstanceIdService {

    @Override
    public void onTokenRefresh() {
        super.onTokenRefresh();
        String instanceId = FirebaseInstanceId.getInstance().getToken();
        Log.e("@@@@", "onTokenRefresh: " + instanceId);
        UserClass userClass= Util.fetchUserClass(getApplicationContext());
        if(userClass==null)
            userClass= new UserClass();
        userClass.firebaseInstanceId= instanceId;
        Util.saveUserClass(getApplicationContext(),userClass);

        FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            FirebaseDatabase.getInstance().getReference()
                    .child("users")
                    .child(firebaseUser.getUid())
                    .child("instanceId")
                    .setValue(instanceId);
        }

      /*  FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
        if (firebaseUser != null) {
            FirebaseDatabase.getInstance().getReference()
                    .child("users")
                    .child(firebaseUser.getUid())
                    .child("instanceId")
                    .setValue(instanceId);
        }*/
    }
}
