package com.nucleustech.mymentor.notification;

import android.content.Context;
import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.text.TextUtils;
import android.util.Log;

import com.google.firebase.messaging.FirebaseMessagingService;
import com.google.firebase.messaging.RemoteMessage;
import com.nucleustech.mymentor.activity.SplashActivity;
import com.nucleustech.mymentor.model.Student;
import com.nucleustech.mymentor.model.StudentList;
import com.nucleustech.mymentor.util.BusHolder;
import com.nucleustech.mymentor.util.DataSetUpdatedEvent;
import com.nucleustech.mymentor.util.Util;

import java.util.ArrayList;
import java.util.Map;

/*
 * Created by ritwik on 3/13/2017.
 */

public class MessagingService extends FirebaseMessagingService {

    private static final String TAG = MessagingService.class.getSimpleName();

    private NotificationUtils notificationUtils;

    @Override
    public void onMessageReceived(RemoteMessage remoteMessage) {
        Log.e(TAG, "From: " + remoteMessage.getFrom());
        Log.e(TAG, "Data: " + remoteMessage.getData().toString());
        //Log.e(TAG, "Message Notification Body: " + remoteMessage.getNotification().getBody());
        if (remoteMessage == null)
            return;

        // Check if message contains a notification payload.
        if (remoteMessage.getNotification() != null) {
            Log.e(TAG, "Notification Body: " + remoteMessage.getNotification().getBody());
            handleNotification(remoteMessage.getNotification().getBody());
        }

        // Check if message contains a data payload.
        if (remoteMessage.getData().size() > 0) {
            Log.e(TAG, "Data Payload: " + remoteMessage.getData().toString());
            Map<String, String> data = remoteMessage.getData();
            handleDataMessage(data);

        }
    }

    private void handleNotification(String message) {
        if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
            // app is in foreground, broadcast the push message
            Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
            pushNotification.putExtra("message", message);
            LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

            // play notification sound
            NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
            notificationUtils.playNotificationSound();
        } else {
            // If the app is in background, firebase itself handles the notification
        }
    }

    private void handleDataMessage(Map<String, String> json) {
        Log.e(TAG, "push json: " + json.toString());

        try {
            //JSONObject data = json.getJSONObject("data");

            String title = json.get("title");
            String message = json.get("message");
            String imageUrl = json.get("icon");
            String timestamp = "5767";//json.getString("timestamp");
            //JSONObject payload = json.getJSONObject("payload");
            String email = json.get("email");

            Log.e(TAG, "title: " + title);
            Log.e(TAG, "message: " + message);
            //Log.e(TAG, "payload: " + payload.toString());
            Log.e(TAG, "imageUrl: " + imageUrl);
            Log.e(TAG, "timestamp: " + timestamp);

            if (!email.isEmpty()) {
                updateStudentList(email);
            }


            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                // app is in foreground, broadcast the push message
                Log.e("In Background","Not In Background");
                Intent pushNotification = new Intent(Config.PUSH_NOTIFICATION);
                pushNotification.putExtra("message", message);
                LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification);

                // play notification sound
                NotificationUtils notificationUtils = new NotificationUtils(getApplicationContext());
                notificationUtils.playNotificationSound();
            } else {
                Log.e("Not in background","In background: "+message);
                // app is in background, show the notification in notification tray
                Intent resultIntent = new Intent(getApplicationContext(), SplashActivity.class);
                resultIntent.putExtra("message", message);

                // check for image attachment
                if (TextUtils.isEmpty(imageUrl)) {
                    Log.e("ImageURL","Empty");
                    showNotificationMessage(getApplicationContext(), title, message, timestamp, resultIntent);
                } else {
                    // image is present, show notification with image
                    Log.e("ImageURL","Not Empty");
                    showNotificationMessageWithBigImage(getApplicationContext(), title, message, timestamp, resultIntent, imageUrl);
                }
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception: " + e.getMessage());
            e.printStackTrace();
        }
    }

    private void updateStudentList(String email) {

        // Enter student at first

        StudentList studentList = Util.fetchStudentList(getApplicationContext());

        if (studentList != null) {

            ArrayList<Student> studentArrayList = studentList.studentsArrayList;

            for (int i = 0; i < studentArrayList.size(); i++) {
                if (studentArrayList.get(i).emailId.trim().equalsIgnoreCase("" + email)) {
                    Student student = studentArrayList.get(i);
                    int unreadCount = student.unreadMsgCount;
                    Log.e("Unread Message", "Before: " + student.unreadMsgCount);
                    student.unreadMsgCount = unreadCount + 1;
                    Log.e("Unread Message", "After Increent: " + student.unreadMsgCount);

                    //Remove the item
                    studentArrayList.remove(i);
                    Log.e("Size: ", "Remove Size: " + studentArrayList.size());
                    //Push the item at Top
                    studentArrayList.add(0,student);
                    Log.e("Size: ", "Added Size: " + studentArrayList.size());
                }


            }
            // Replace the old list with New
            studentList.studentsArrayList = studentArrayList;
            Log.e("Size: ", "Added Size: " + studentList.studentsArrayList.size());
            Util.saveStudentList(getApplicationContext(), studentList);
            if (!NotificationUtils.isAppIsInBackground(getApplicationContext())) {
                BusHolder.getInstnace().post(new DataSetUpdatedEvent(""));
            }

        }
    }

    /**
     * Showing notification with text only
     */
    private void showNotificationMessage(Context context, String title, String message, String timeStamp, Intent intent) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent);
    }

    /**
     * Showing notification with text and image
     */
    private void showNotificationMessageWithBigImage(Context context, String title, String message, String timeStamp, Intent intent, String imageUrl) {
        notificationUtils = new NotificationUtils(context);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        notificationUtils.showNotificationMessage(title, message, timeStamp, intent, imageUrl);
    }
}

