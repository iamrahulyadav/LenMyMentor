package com.nucleustech.mymentor.util;

import com.nucleustech.mymentor.model.Schedule;

/**
 * Created by Ritwik Rai on 24/12/17.
 */

public interface ScheduleListener {

    void onRescheduleClick(Schedule schedule);

    void onCancelClick(Schedule schedule);
}
