package com.nucleustech.mymentor.util;

/**
 * Created by ritwik.rai on 24/12/17.
 */

public class DataSetUpdatedEvent {
    //It is better to use database and share the key of record of database.
    //But for simplicity, I share the dataset directly.
    String dataset;

    public DataSetUpdatedEvent(String dataset) {
        this.dataset = dataset;
    }
}