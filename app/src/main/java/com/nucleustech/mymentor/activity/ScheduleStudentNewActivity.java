package com.nucleustech.mymentor.activity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.nucleustech.mymentor.R;
import com.nucleustech.mymentor.util.AlertDialogCallBack;
import com.nucleustech.mymentor.util.Util;
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

public class ScheduleStudentNewActivity extends AppCompatActivity implements ServerResponseCallback {

    private TextView tv_scheduleChat_dateTime;
    private Context mContext;
    VolleyTaskManager volleyTaskManager;
    DatePickerDialog datePickerDialog;
    TimePickerDialog timePickerDialog;
    int changedYear, changedMonth, changedDay, changedHour, changedMinute, changedSecond;
    private boolean isScheduleService = false;
    Calendar newDate;
    Calendar newTime;
    private SimpleDateFormat dateFormatter;
    private SimpleDateFormat timeFormatter;
    private String studentID="";

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_new_schedule);
        mContext = ScheduleStudentNewActivity.this;
        dateFormatter = new SimpleDateFormat("yyyy/MM/dd", Locale.US);
        timeFormatter= new SimpleDateFormat("hh:mm:ss",Locale.US);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Scheduled Meet");
        studentID= getIntent().getStringExtra("studentID");
        tv_scheduleChat_dateTime = (TextView) findViewById(R.id.tv_scheduleChat_dateTime);

        volleyTaskManager = new VolleyTaskManager(mContext);

        Calendar newCalendar = Calendar.getInstance();
       tv_scheduleChat_dateTime.setText("Schedule New Meet");
        datePickerDialog = new DatePickerDialog(mContext, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth);
                changedYear = year;
                changedMonth = monthOfYear+1;
                changedDay = dayOfMonth;
                timePickerDialog.show();
                // tv_scheduleChat_dateTime.setText(dateFormatter.format(newDate.getTime()));

            }

        }, newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


        timePickerDialog = new TimePickerDialog(this,
                new TimePickerDialog.OnTimeSetListener() {

                    @Override
                    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
                        newTime= Calendar.getInstance();
                        newTime.set(changedYear,changedMonth,changedDay,hourOfDay,minute);
                        changedHour = hourOfDay;
                        changedMinute = minute;

                        //tv_scheduleChat_dateTime.setText(changedDay + "/" + changedMonth + "/" + changedYear + "    " + changedHour + ":" + changedMinute + ":" + "00");
                        tv_scheduleChat_dateTime.setText(dateFormatter.format(newDate.getTime())+"   "+timeFormatter.format(newTime.getTime() ));
                        tv_scheduleChat_dateTime.setClickable(false);
                        callScheduleChatService();
                    }
                }, newCalendar.get(Calendar.HOUR_OF_DAY), newCalendar.get(Calendar.MINUTE), true);

    }

    public void onRescheduleClick(View view) {
        setScheduleForAdminChat();
    }


    @Override
    public void onSuccess(JSONObject resultJsonObject) {

        if (isScheduleService) {
            isScheduleService = false;
            if (resultJsonObject.optString("code").trim().equalsIgnoreCase("200")) {
                //Toast.makeText(mContext, "Chat Session Scheduled.", Toast.LENGTH_LONG).show();
                Util.showCallBackMessageWithOkCallback(mContext, "Chat Session Scheduled.", new AlertDialogCallBack() {
                    @Override
                    public void onSubmit() {
                        finish();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            } else {
                Toast.makeText(mContext, "Something went wrong please try again.", Toast.LENGTH_LONG).show();
            }
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
        requestMap.put("userID", "" +studentID);
        requestMap.put("scheduleDate", "" + changedDay + "/" + changedMonth + "/" + changedYear);
        requestMap.put("scheduleTime", "" + changedHour + ":" + changedMinute);
        isScheduleService = true;
        volleyTaskManager.doPostAdminChatSchedule(requestMap, true);
    }

}
