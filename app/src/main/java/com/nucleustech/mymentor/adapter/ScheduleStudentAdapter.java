package com.nucleustech.mymentor.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.nucleustech.mymentor.R;
import com.nucleustech.mymentor.activity.StudentChatActivity;
import com.nucleustech.mymentor.model.Schedule;
import com.nucleustech.mymentor.model.Student;
import com.nucleustech.mymentor.model.StudentList;
import com.nucleustech.mymentor.util.Util;

import java.util.ArrayList;

/**
 * Created by ritwik on 24/12/17.
 */

public class ScheduleStudentAdapter extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Schedule> courses = new ArrayList<>();


    public ScheduleStudentAdapter(Context mContext, ArrayList<Schedule> cityMap) {
        this.mContext = mContext;
        this.courses = cityMap;
        mInflater = LayoutInflater.from(mContext);

    }

    @Override
    public int getCount() {
        return courses.size();
    }

    @Override
    public Object getItem(int position) {
        return courses.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View hView = convertView;
        if (convertView == null) {
            hView = mInflater.inflate(R.layout.item_view_schedules, null);
            ScheduleStudentAdapter.ViewHolder holder = new ScheduleStudentAdapter.ViewHolder();
            holder.tv_scheduleChat_dateTime = (TextView) hView.findViewById(R.id.tv_scheduleChat_dateTime);
            holder.tv_reschedule = (TextView) hView.findViewById(R.id.tv_reschedule);
            holder.tv_studentName = (TextView) hView.findViewById(R.id.tv_studentName);
            hView.setTag(holder);
        }

        ScheduleStudentAdapter.ViewHolder holder = (ScheduleStudentAdapter.ViewHolder) hView.getTag();
        holder.tv_scheduleChat_dateTime.setText(courses.get(position).scheduledDate + "   " + courses.get(position).scheduledTime);
        holder.tv_studentName.setText(courses.get(position).name);


        return hView;
    }


    class ViewHolder {
        TextView tv_scheduleChat_dateTime, tv_reschedule, tv_cancel, tv_studentName;


    }


}
