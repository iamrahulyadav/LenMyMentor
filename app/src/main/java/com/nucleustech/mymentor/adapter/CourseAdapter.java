package com.nucleustech.mymentor.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nucleustech.mymentor.R;
import com.nucleustech.mymentor.model.Course;

import java.util.ArrayList;

/**
 * Created by ritwik on 24/12/17.
 */

public class CourseAdapter  extends BaseAdapter {

    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Course> courses = new ArrayList<>();


    public CourseAdapter(Context mContext, ArrayList<Course>  cityMap) {
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
            hView = mInflater.inflate(R.layout.item_courses, null);
            ViewHolder holder = new ViewHolder();
            holder.tv_courseName = (TextView) hView.findViewById(R.id.tv_courseName);
            hView.setTag(holder);
        }

        ViewHolder holder = (ViewHolder) hView.getTag();
        holder.tv_courseName.setText(courses.get( position).courseName);

        return hView;
    }


    class ViewHolder {
        TextView tv_courseName;

    }
}
