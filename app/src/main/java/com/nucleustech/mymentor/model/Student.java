package com.nucleustech.mymentor.model;

import java.io.Serializable;

/**
 * Created by ritwik on 09/12/17.
 */

public class Student implements Serializable {
    public String name;
    public String emailId;
    public String mobile;
    public String studentFirebaseId;
    public String userID;
    public int unreadMsgCount=0;
    public String profileImgURL;
}
