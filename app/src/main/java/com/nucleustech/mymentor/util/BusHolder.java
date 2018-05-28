package com.nucleustech.mymentor.util;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by ritwik.rai on 24/12/17.
 */

public class BusHolder {

    private static EventBus eventBus;

    public static EventBus getInstnace() {
        if (eventBus == null) {
            eventBus = new EventBus();
        }
        return eventBus;
    }

    private BusHolder() {
    }
}