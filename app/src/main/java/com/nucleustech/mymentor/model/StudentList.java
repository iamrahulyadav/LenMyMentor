package com.nucleustech.mymentor.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ritwik on 24/12/17.
 */

public class StudentList implements Serializable {

    /***
     * Students Array with Students Object
     * */
    public ArrayList<Student> studentsArrayList= new ArrayList<>();

    /**
     * Students Map saved according to its key--> email ID
     * */
    public HashMap<String,Student> studentArrayMap= new HashMap<>();
}
