package com.nucleustech.mymentor.adapter;

import android.content.Context;
import android.os.Parcelable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.nucleustech.mymentor.R;

/**
 * Created by ritwik.rai on 28/11/17.
 */

public class WalkthroughAdapter extends PagerAdapter {

    public int getCount() {
        return 4;
    }

    public Object instantiateItem(ViewGroup collection, int position) {
        LayoutInflater inflater = (LayoutInflater) collection.getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        int resId = 0;
        switch (position) {
            case 0:
                resId = R.layout.viewpager_view_first;
                break;
            case 1:
                resId = R.layout.viewpager_view_second;
                break;
            case 2:
                resId = R.layout.viewpager_view_third;
                break;
            case 3:
                resId = R.layout.viewpager_view_fourth;
                break;

        }
        View view = inflater.inflate(resId, null);
        ((ViewPager) collection).addView(view, 0);

        return view;
    }

    @Override
    public void destroyItem(ViewGroup arg0, int arg1, Object arg2) {
        ((ViewPager) arg0).removeView((View) arg2);
    }

    @Override
    public boolean isViewFromObject(View arg0, Object arg1) {
        return arg0 == ((View) arg1);
    }

    //public boolean isViewFromObject(ViewGroup arg0, Object arg1) {
    // return arg0 == ((View) arg1);
    //}
    @Override
    public Parcelable saveState() {
        return null;
    }
}