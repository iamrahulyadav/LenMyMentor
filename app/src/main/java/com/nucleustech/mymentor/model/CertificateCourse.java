package com.nucleustech.mymentor.model;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * Created by ritwik.rai on 24/12/17.
 */

public class CertificateCourse implements Serializable {

    public String certificateName="";
    public String  certificateDetails="";
    public ArrayList<Course> courses= new ArrayList<>();
}
