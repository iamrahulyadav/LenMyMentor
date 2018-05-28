package com.nucleustech.mymentor.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nucleustech.mymentor.R;
import com.nucleustech.mymentor.adapter.ScheduleAdapter;
import com.nucleustech.mymentor.interfaces.OnChatClickListener;
import com.nucleustech.mymentor.model.Schedule;
import com.nucleustech.mymentor.model.Student;
import com.nucleustech.mymentor.model.StudentList;
import com.nucleustech.mymentor.model.UserClass;
import com.nucleustech.mymentor.util.AlertDialogCallBack;
import com.nucleustech.mymentor.util.Util;
import com.nucleustech.mymentor.volley.ServerResponseCallback;
import com.nucleustech.mymentor.volley.VolleyTaskManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;

/**
 * Created by ritwik.rai on 24/12/17.
 */

public class ViewUserAllScheduleActivity extends AppCompatActivity implements ServerResponseCallback, OnChatClickListener {

    private TextView tv_scheduleChat_dateTime;
    private Context mContext;
    VolleyTaskManager volleyTaskManager;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    int changedYear, changedMonth, changedDay, changedHour, changedMinute, changedSecond;
    private boolean isScheduleService = false;
    private ArrayList<Schedule> scheduleArrayList;
    private ListView lv_schedules;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /* @Override
     public boolean onCreateOptionsMenu(Menu menu) {
         // Inflate the menu; this adds items to the action bar if it is present.
         getMenuInflater().inflate(R.menu.schedule_menu, menu);
         return true;
     }*/
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_all_schedule);
        mContext = ViewUserAllScheduleActivity.this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Schedules");
        tv_scheduleChat_dateTime = (TextView) findViewById(R.id.tv_scheduleChat_dateTime);
        lv_schedules = (ListView) findViewById(R.id.lv_schedules);

        volleyTaskManager = new VolleyTaskManager(mContext);

        Calendar newCalendar = Calendar.getInstance();
        UserClass userClass = Util.fetchUserClass(mContext);

        volleyTaskManager.doPostFetchSchedules(new HashMap<String, String>(), true);
    }


    @Override
    public void onSuccess(JSONObject resultJsonObject) {


        if (resultJsonObject.optString("code").equalsIgnoreCase("200")) {
            try {
                scheduleArrayList = new ArrayList<>();
                JSONArray schedules = resultJsonObject.optJSONArray("schedules");

                for (int i = 0; i < schedules.length(); i++) {
                    JSONObject userSchedule = schedules.optJSONObject(i);
                    Schedule schedule = new Schedule();
                    schedule.userID = userSchedule.optString("userID");
                    schedule.name = userSchedule.optString("name");
                    schedule.scheduledDate = userSchedule.optString("scheduleDate");
                    schedule.scheduledTime = userSchedule.optString("scheduleTime");
                    schedule.meetingId = userSchedule.optString("meetingID");
                    scheduleArrayList.add(schedule);
                }

                // Load Adapter
                ScheduleAdapter scheduleAdapter = new ScheduleAdapter(mContext, this, scheduleArrayList, ViewUserAllScheduleActivity.this);
                lv_schedules.setAdapter(scheduleAdapter);
                lv_schedules.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {

                        Schedule schedule = scheduleArrayList.get(i);
                        Intent intent = new Intent(mContext, ViewUserScheduleActivity.class);
                        intent.putExtra("schedule", schedule);
                        startActivity(intent);
                        finish();
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (resultJsonObject.optString("code").equalsIgnoreCase("400")) {
            Util.showCallBackMessageWithOkCallback(mContext, "No schedules found.", new AlertDialogCallBack() {
                @Override
                public void onSubmit() {
                    finish();
                }

                @Override
                public void onCancel() {

                }
            });
        }

    }

    @Override
    public void onError() {

    }


    @Override
    public void onChatClick(int position) {

        StudentList studentList = Util.fetchStudentList(mContext);
        Student selectedStudent = null;

        for (Student student : studentList.studentsArrayList) {
            if (student.userID.trim().equalsIgnoreCase("" + scheduleArrayList.get(position).userID.trim())) {
                selectedStudent = student;
                break;
            }
        }

        if (selectedStudent != null) {
            Intent intent = new Intent(mContext, StudentChatActivity.class);
            intent.putExtra("name", selectedStudent.name);
            intent.putExtra("email", selectedStudent.emailId);
            intent.putExtra("studentId", selectedStudent.userID);
            intent.putExtra("firebaseId", selectedStudent.studentFirebaseId);
            startActivity(intent);
            finish();
        }

    }

}
