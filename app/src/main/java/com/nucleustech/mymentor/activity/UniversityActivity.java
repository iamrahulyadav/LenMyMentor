package com.nucleustech.mymentor.activity;

import android.app.Dialog;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.method.LinkMovementMethod;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nucleustech.mymentor.R;
import com.nucleustech.mymentor.adapter.CourseAdapter;
import com.nucleustech.mymentor.model.CertificateCourse;
import com.nucleustech.mymentor.model.Course;
import com.nucleustech.mymentor.model.University;
import com.nucleustech.mymentor.util.AlertDialogCallBack;
import com.nucleustech.mymentor.util.Util;
import com.nucleustech.mymentor.volley.ServerResponseCallback;
import com.nucleustech.mymentor.volley.VolleyTaskManager;

import org.json.JSONArray;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ritwik.rai on 23/12/17.
 */

public class UniversityActivity extends AppCompatActivity implements ServerResponseCallback {

    private VolleyTaskManager volleyTaskManager;
    private String universityID = "";
    private Context mContext;
    private University university;
    private ArrayList<Course> allCourses = new ArrayList<>();
    private ListView lv_courses, lv_undergradcourses, lv_postgradcourses;
    private TextView tv_aboutUs;
    private TextView tv_collegeName, tv_collegeAddress;
    private ArrayList<Course> underGradCourses = new ArrayList<>();
    private ArrayList<Course> postGradCourses = new ArrayList<>();
    private LinearLayout llAboutUS, llUnderGrad, llPostGrad;
    private TextView tv_menu_postgrad, tv_menu_undergrad, tv_menu_aboutus;
    private View v_line_aboutme, v_line_undergrad, v_line_postgrad;
    private TextView tv_requestInfo;
    private boolean isFetchUniversity = false;
    private boolean isSuggestUniversity = false;
    private TextView tv_underGradDetails,tv_postGradDetails;

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_university_profile);
        mContext = UniversityActivity.this;


        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(" ");


        universityID = getIntent().getStringExtra("universityID");

        lv_courses = (ListView) findViewById(R.id.lv_courses);
        lv_undergradcourses = (ListView) findViewById(R.id.lv_undergradcourses);
        lv_postgradcourses = (ListView) findViewById(R.id.lv_postgradcourses);
        tv_aboutUs = (TextView) findViewById(R.id.tv_aboutUs);
        tv_collegeName = (TextView) findViewById(R.id.tv_collegeName);
        tv_collegeAddress = (TextView) findViewById(R.id.tv_collegeAddress);


        llAboutUS = (LinearLayout) findViewById(R.id.llAboutUS);
        llUnderGrad = (LinearLayout) findViewById(R.id.llUnderGrad);
        llPostGrad = (LinearLayout) findViewById(R.id.llPostGrad);
        volleyTaskManager = new VolleyTaskManager(mContext);

        tv_menu_aboutus = (TextView) findViewById(R.id.tv_menu_aboutus);
        tv_menu_undergrad = (TextView) findViewById(R.id.tv_menu_undergrad);
        tv_menu_postgrad = (TextView) findViewById(R.id.tv_menu_postgrad);

        v_line_aboutme = (View) findViewById(R.id.v_line_aboutme);
        v_line_undergrad = (View) findViewById(R.id.v_line_undergrad);
        v_line_postgrad = (View) findViewById(R.id.v_line_postgrad);

        tv_requestInfo = (TextView) findViewById(R.id.tv_requestInfo);

        tv_underGradDetails= (TextView)findViewById(R.id.tv_underGradDetails);
        tv_postGradDetails= (TextView)findViewById(R.id.tv_postGradDetails);
        //tv_aboutUs.setText(getResources().getString(R.string.test_hyperlink));

        if (universityID.length() > 0) {


            HashMap<String, String> requestMap = new HashMap<>();
            requestMap.put("uid", "" + universityID);
            isFetchUniversity = true;
            volleyTaskManager.doPostFetchUniversity(requestMap, true);
        } else {
            Util.showCallBackMessageWithOkCallback(mContext, "Please reopen the University page again.", new AlertDialogCallBack() {
                @Override
                public void onSubmit() {
                    finish();
                }

                @Override
                public void onCancel() {

                }
            });
        }
    }

    @Override
    public void onSuccess(JSONObject resultJsonObject) {
        if (isFetchUniversity) {
            isFetchUniversity = false;
            if (resultJsonObject.optString("code").trim().equalsIgnoreCase("200")) {
                JSONObject universityJsonObject = resultJsonObject.optJSONObject("university");

                University university = new University();
                university.universityId = universityJsonObject.optString("universityID");
                university.universityName = universityJsonObject.optString("universityName");
                university.about = universityJsonObject.optString("universityAbout");
                university.address = universityJsonObject.optString("universityAddress");

                JSONArray degreesOffered = universityJsonObject.optJSONArray("degreesOffered");
                ArrayList<CertificateCourse> degrees = new ArrayList<>();
                allCourses = new ArrayList<>();

                for (int i = 0; i < degreesOffered.length(); i++) {
                    JSONObject degreesJsonObject = degreesOffered.optJSONObject(i);
                    CertificateCourse degree = new CertificateCourse();
                    degree.certificateName = degreesJsonObject.optString("degreeType");
                    degree.certificateDetails = degreesJsonObject.optString("degreeDetail");

                    if(i==0)
                        university.underGradDetails=degree.certificateDetails;
                    else if(i==1)
                        university.postGradDetails=degree.certificateDetails;

                    JSONArray coursesOffered = degreesJsonObject.optJSONArray("coursesOffered");
                    Log.e("coursesOffered", "coursesOffered: " + coursesOffered);

                    Log.e("coursesOffered", "coursesOffered: " + degreesJsonObject.optString("coursesOffered"));
                    ArrayList<Course> courses = new ArrayList<>();
                    for (int j = 0; j < coursesOffered.length(); j++) {
                        JSONObject coursesJson = coursesOffered.optJSONObject(j);
                        Course course = new Course();
                        course.courseName = coursesJson.optString("courseName");
                        course.courseDetails = coursesJson.optString("courseDetail");
                        courses.add(course);
                        allCourses.add(course);
                        if (i == 0) {
                            underGradCourses.add(course);
                        } else if (i == 1) {
                            postGradCourses.add(course);
                        }
                    }
                    degree.courses = courses;
                    degrees.add(degree);

                }
                university.certificateCourses = degrees;

                this.university = university;


                loadView();
            }
        } else if (isSuggestUniversity) {
            isSuggestUniversity = false;
            if (resultJsonObject.optString("code").equalsIgnoreCase("200")) {
                Util.showCallBackMessageWithOkCallback(mContext, "University suggested successfully to " + Util.fetchUserClass(mContext).selectedStudent.name, new AlertDialogCallBack() {
                    @Override
                    public void onSubmit() {
                        finish();
                    }

                    @Override
                    public void onCancel() {

                    }
                });
            }
        }

    }


    @Override
    public void onError() {

    }

    private void loadView() {


        tv_aboutUs.setText(university.about);
        tv_aboutUs.setMovementMethod(LinkMovementMethod.getInstance());
        tv_collegeAddress.setText(university.address);
        tv_collegeName.setText(university.universityName);
        tv_underGradDetails.setText(university.underGradDetails);
        tv_postGradDetails.setText(university.postGradDetails);
        CourseAdapter courseAdapter = new CourseAdapter(mContext, allCourses);
        lv_courses.setAdapter(courseAdapter);
        lv_courses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Course course = allCourses.get(i);

                final Dialog dialog = new Dialog(mContext);
                dialog.setContentView(R.layout.dialog_course_details);
                dialog.setCancelable(true);
                TextView tv_courseName = (TextView) dialog.findViewById(R.id.tv_courseName);
                TextView tv_courseDetails = (TextView) dialog.findViewById(R.id.tv_courseDetails);

                tv_courseDetails.setText(course.courseDetails);
                tv_courseName.setText(course.courseName);
                dialog.show();
            }
        });

        CourseAdapter courseAdapterUnderGrad = new CourseAdapter(mContext, underGradCourses);

        lv_undergradcourses.setAdapter(courseAdapterUnderGrad);
        lv_undergradcourses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Course course = underGradCourses.get(i);

                final Dialog dialog = new Dialog(mContext);
                dialog.setContentView(R.layout.dialog_course_details);
                dialog.setCancelable(true);
                TextView tv_courseName = (TextView) dialog.findViewById(R.id.tv_courseName);
                TextView tv_courseDetails = (TextView) dialog.findViewById(R.id.tv_courseDetails);

                tv_courseDetails.setText(course.courseDetails);
                tv_courseName.setText(course.courseName);
                dialog.show();
            }
        });

        CourseAdapter courseAdapterPostGrad = new CourseAdapter(mContext, postGradCourses);

        lv_postgradcourses.setAdapter(courseAdapterPostGrad);
        lv_postgradcourses.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Course course = postGradCourses.get(i);

                final Dialog dialog = new Dialog(mContext);
                dialog.setContentView(R.layout.dialog_course_details);
                dialog.setCancelable(true);
                TextView tv_courseName = (TextView) dialog.findViewById(R.id.tv_courseName);
                TextView tv_courseDetails = (TextView) dialog.findViewById(R.id.tv_courseDetails);

                tv_courseDetails.setText(course.courseDetails);
                tv_courseName.setText(course.courseName);
                dialog.show();
            }
        });


        tv_menu_aboutus.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                v_line_aboutme.setVisibility(View.VISIBLE);
                v_line_undergrad.setVisibility(View.GONE);
                v_line_postgrad.setVisibility(View.GONE);
                llAboutUS.setVisibility(View.VISIBLE);
                llUnderGrad.setVisibility(View.GONE);
                llPostGrad.setVisibility(View.GONE);

            }
        });
        tv_menu_undergrad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                v_line_aboutme.setVisibility(View.GONE);
                v_line_undergrad.setVisibility(View.VISIBLE);
                v_line_postgrad.setVisibility(View.GONE);
                llAboutUS.setVisibility(View.GONE);
                llUnderGrad.setVisibility(View.VISIBLE);
                llPostGrad.setVisibility(View.GONE);
            }
        });
        tv_menu_postgrad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                v_line_aboutme.setVisibility(View.GONE);
                v_line_undergrad.setVisibility(View.GONE);
                v_line_postgrad.setVisibility(View.VISIBLE);
                llAboutUS.setVisibility(View.GONE);
                llUnderGrad.setVisibility(View.GONE);
                llPostGrad.setVisibility(View.VISIBLE);
            }
        });

        tv_requestInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if(Util.fetchUserClass(mContext).selectedStudent!=null) {
                    HashMap<String, String> requestMap = new HashMap<>();
                    requestMap.put("userID", "" + Util.fetchUserClass(mContext).selectedStudent.userID);
                    requestMap.put("universityID", "" + universityID);
                    isSuggestUniversity = true;
                    volleyTaskManager.doPostSuggestUniversity(requestMap, true);
                }
            }
        });
    }


}
