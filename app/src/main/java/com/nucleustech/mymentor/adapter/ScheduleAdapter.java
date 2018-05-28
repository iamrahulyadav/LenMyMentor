package com.nucleustech.mymentor.adapter;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.nucleustech.mymentor.R;
import com.nucleustech.mymentor.activity.StudentChatActivity;
import com.nucleustech.mymentor.interfaces.OnChatClickListener;
import com.nucleustech.mymentor.model.Schedule;
import com.nucleustech.mymentor.model.Student;
import com.nucleustech.mymentor.model.StudentList;
import com.nucleustech.mymentor.util.Util;

import java.util.ArrayList;

/**
 * Created by ritwik on 24/12/17.
 */

public class ScheduleAdapter extends BaseAdapter {

    private Context mContext;
    private Activity mActivity;
    private LayoutInflater mInflater;
    private ArrayList<Schedule> courses = new ArrayList<>();
    private OnChatClickListener mOnChatClickListener;


    public ScheduleAdapter(Context mContext, OnChatClickListener mOnChatClickListener, ArrayList<Schedule> cityMap, Activity mActivity) {
        this.mContext = mContext;
        this.courses = cityMap;
        this.mActivity = mActivity;
        this.mOnChatClickListener = mOnChatClickListener;
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
    public View getView(final int position, View convertView, ViewGroup parent) {

        View hView = convertView;
        if (convertView == null) {
            hView = mInflater.inflate(R.layout.item_view_schedules_with_chat, null);
            ScheduleAdapter.ViewHolder holder = new ScheduleAdapter.ViewHolder();
            holder.tv_scheduleChat_dateTime = (TextView) hView.findViewById(R.id.tv_scheduleChat_dateTime);
            holder.tv_reschedule = (TextView) hView.findViewById(R.id.tv_reschedule);
            holder.tv_studentName = (TextView) hView.findViewById(R.id.tv_studentName);
            holder.iv_student_chat = (ImageView) hView.findViewById(R.id.iv_student_chat);
            hView.setTag(holder);
        }

        ScheduleAdapter.ViewHolder holder = (ScheduleAdapter.ViewHolder) hView.getTag();
        holder.tv_scheduleChat_dateTime.setText(courses.get(position).scheduledDate + "   " + courses.get(position).scheduledTime);
        holder.tv_studentName.setText(courses.get(position).name);

        holder.tv_studentName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                mOnChatClickListener.onChatClick(position);
            }
        });
        return hView;
    }


    class ViewHolder {
        TextView tv_scheduleChat_dateTime, tv_reschedule, tv_studentName;
        ImageView iv_student_chat;


    }


}
