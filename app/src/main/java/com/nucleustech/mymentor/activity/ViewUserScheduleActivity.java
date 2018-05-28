package com.nucleustech.mymentor.activity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.nucleustech.mymentor.R;
import com.nucleustech.mymentor.model.Schedule;
import com.nucleustech.mymentor.volley.ServerResponseCallback;
import com.nucleustech.mymentor.volley.VolleyTaskManager;

import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Locale;

/**
 * Created by ritwik.rai on 24/12/17.
 */

public class ViewUserScheduleActivity extends AppCompatActivity implements ServerResponseCallback {

    private TextView tv_scheduleChat_dateTime;
    private Context mContext;
    VolleyTaskManager volleyTaskManager;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    int changedYear, changedMonth, changedDay, changedHour, changedMinute, changedSecond;
    private boolean isScheduleService = false;
    private Schedule schedule;
    private boolean isCancelMeetingSchedule = false;
    Calendar newDate;
    Calendar newTime;
    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat timeFormatter;

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
        setContentView(R.layout.activity_view_schedule);
        mContext = ViewUserScheduleActivity.this;
        dateFormatter = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
        timeFormatter = new SimpleDateFormat("hh:mm:ss", Locale.US);

        schedule = (Schedule) getIntent().getSerializableExtra("schedule");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("");
        tv_scheduleChat_dateTime = (TextView) findViewById(R.id.tv_scheduleChat_dateTime);

        volleyTaskManager = new VolleyTaskManager(mContext);

        Calendar newCalendar = Calendar.getInstance();

        datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                changedYear = year;
                changedMonth = monthOfYear + 1;
                changedDay = dayOfMonth;
                timePickerDialog.show();
                // tv_scheduleChat_dateTime.setText(dateFormatter.format(newDate.getTime()));

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


        timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        newTime = Calendar.getInstance();
                        newTime.set(changedYear, changedMonth, changedDay, hourOfDay, minute);
                        changedHour = hourOfDay;
                        changedMinute = minute;

                        //tv_scheduleChat_dateTime.setText(changedDay + "/" + changedMonth + "/" + changedYear + "    " + changedHour + ":" + changedMinute + ":" + "00");
                        tv_scheduleChat_dateTime.setText(dateFormatter.format(newDate.getTime()) + "   " + timeFormatter.format(newTime.getTime()));
                        tv_scheduleChat_dateTime.setClickable(false);

                        callScheduleChatService();
                    }
                }, newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE), true);
        tv_scheduleChat_dateTime.setText(schedule.scheduledDate + "   " + schedule.scheduledTime);

    }

    public void onRescheduleClick(View view) {
        setScheduleForAdminChat();
    }


    @Override
    public void onSuccess(JSONObject resultJsonObject) {

        if (isScheduleService) {
            isScheduleService = false;
            if (resultJsonObject.optString("code").trim().equalsIgnoreCase("200")) {
                Toast.makeText(mContext, "Chat Session Re-Scheduled.", Toast.LENGTH_LONG).show();
            } else {
                Toast.makeText(mContext, "Something went wrong please try again.", Toast.LENGTH_LONG).show();
            }
        } else if (isCancelMeetingSchedule) {
            isCancelMeetingSchedule = false;
            Log.e("isCancelMeetingSchedule", "isCancelMeetingSchedule: " + resultJsonObject);
            tv_scheduleChat_dateTime.setText("SCHEDULE MEET");
            // Meeting is cancalled call finish()
            startActivity(new Intent(mContext, ViewUserAllScheduleActivity.class));
            finish();
        }

    }

    @Override
    public void onError() {

    }

    private void setScheduleForAdminChat() {

        datePickerDialog.show();
    }

    private void callScheduleChatService() {

        HashMap<String, String> requestMap = new HashMap<>();
        requestMap.put("userID", "" + schedule.userID);
        requestMap.put("scheduleDate", "" + changedDay + "/" + changedMonth + "/" + changedYear);
        requestMap.put("scheduleTime", "" + changedHour + ":" + changedMinute);
        isScheduleService = true;
        volleyTaskManager.doPostAdminChatSchedule(requestMap, true);
    }

    /**
     * Cancel scheduled Chat
     */
    public void onCancelScheduleClick(View view) {

        final Dialog dialog = new Dialog(mContext);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        dialog.setContentView(R.layout.dialog_cancel_meeting);
        dialog.setCancelable(true);
        Button btn_submit = (Button) dialog.findViewById(R.id.btn_submit);
        final TextView tv_meetDateTime = (TextView) dialog.findViewById(R.id.tv_meetDateTime);

        btn_submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();
                HashMap<String, String> requestMap = new HashMap<>();
                requestMap.put("meetingID", "" + schedule.meetingId);
                isCancelMeetingSchedule = true;
                volleyTaskManager.doPostCancelSchedule(requestMap, true);
            }
        });
        dialog.show();

    }

}

