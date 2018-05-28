package com.nucleustech.mymentor.activity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.nucleustech.mymentor.R;
import com.nucleustech.mymentor.adapter.CircleTransform;
import com.nucleustech.mymentor.adapter.SuggestedCollegesAdapter;
import com.nucleustech.mymentor.model.University;
import com.nucleustech.mymentor.model.UserClass;
import com.nucleustech.mymentor.util.FileOpen;
import com.nucleustech.mymentor.util.Util;
import com.nucleustech.mymentor.volley.ServerResponseCallback;
import com.nucleustech.mymentor.volley.VolleyTaskManager;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.HashMap;

/**
 * Created by ritwik.rai on 09/12/17.
 */

public class StudentProfileActivity extends AppCompatActivity implements ServerResponseCallback {

    private Context mContext;
    private VolleyTaskManager volleyTaskManager;
    TextView name, email, tv_phone, tv_aboutMe, tv_toeflScore, tv_greScore;
    ImageView image;
    private boolean isFetchProfileService = false;

    TextView tv_scheduleChat_dateTime;
    private ProgressDialog pDialog;
    private RecyclerView recyclerAppliedUniversities, recyclerSuggestedUniversities;
    private ArrayList<University> suggestedUniversities;
    private ArrayList<University> interestedUniversities;
    private String uplodedCVUrl = "";
    private String studentName = "";
    private String studentEmail = "";
    private String studentFirebaseID = "";
    private String studentID = "";

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            finish();
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_student_profile);

        mContext = StudentProfileActivity.this;


        studentName = getIntent().getStringExtra("name");

        studentEmail = getIntent().getStringExtra("email");
        studentFirebaseID = getIntent().getStringExtra("firebaseId");
        studentID = getIntent().getStringExtra("studentId");

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("" + studentName);

        name = (TextView) findViewById(R.id.name);
        email = (TextView) findViewById(R.id.email);
        tv_phone = (TextView) findViewById(R.id.tv_phone);
        image = (ImageView) findViewById(R.id.iv_userProfile);
        tv_aboutMe = (TextView) findViewById(R.id.tv_aboutMe);
        tv_greScore = (TextView) findViewById(R.id.tv_greScore);
        tv_toeflScore = (TextView) findViewById(R.id.tv_toeflScore);
        tv_scheduleChat_dateTime = (TextView) findViewById(R.id.tv_scheduleChat_dateTime);
        recyclerSuggestedUniversities = (RecyclerView) findViewById(R.id.recyclerSuggestedUniversities);
        recyclerAppliedUniversities = (RecyclerView) findViewById(R.id.recyclerAppliedUniversities);

        volleyTaskManager = new VolleyTaskManager(mContext);


        name.setText("" + studentName);
        email.setText("" + studentEmail);
        tv_phone.setText("");
        //Glide.with(image.getContext()).load(userClass.profileUrl).centerCrop().transform(new CircleTransform(image.getContext())).override(50, 50).into(image);
        HashMap<String, String> hashMap = new HashMap<>();
        hashMap.put("userID", "" + studentID);

        isFetchProfileService = true;
        volleyTaskManager.doPostFetchProfile(hashMap, true);


    }


    public void onCallClick(View view) {
        String phoneNumber = tv_phone.getText().toString().trim();

        if (!TextUtils.isEmpty(phoneNumber)) {
            Intent intent = new Intent(Intent.ACTION_DIAL);
            intent.setData(Uri.parse("tel:" + phoneNumber));
            startActivity(intent);
        }

    }

    @Override
    public void onSuccess(JSONObject resultJsonObject) {

        if (isFetchProfileService) {
            isFetchProfileService = false;
            Log.e("FetchProfile", "Response: " + resultJsonObject);
            if (resultJsonObject.optString("code").trim().equalsIgnoreCase("200")) {

                JSONObject jsonObject = resultJsonObject.optJSONObject("userData");
                String greScore = "" + jsonObject.optString("greScore");
                String toeflScore = "" + jsonObject.optString("toeflScore");
                uplodedCVUrl = "" + jsonObject.optString("uploadedCV");
                String profileImgURL = "" + jsonObject.optString("profileImgURL");
                String mobileNumber=""+jsonObject.optString("mobile");

                String aboutMeText = "" + jsonObject.optString("aboutMe");
                if (aboutMeText != null && !(aboutMeText.equalsIgnoreCase("null")))
                    tv_aboutMe.setText(aboutMeText);

                if (!greScore.trim().isEmpty() && greScore.length() > 0) {
                    tv_greScore.setText(greScore);
                    tv_greScore.setClickable(false);

                } else if (greScore.trim().isEmpty() && !(greScore.length() > 0)) {
                    tv_greScore.setText("NA");
                    tv_greScore.setClickable(true);
                    tv_greScore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onEditTestScoresClick(view);
                        }
                    });
                }
                if (!toeflScore.trim().isEmpty() && toeflScore.length() > 0) {
                    tv_toeflScore.setText(toeflScore);
                    tv_toeflScore.setClickable(false);
                } else if (toeflScore.trim().isEmpty() && !(toeflScore.length() > 0)) {
                    tv_toeflScore.setText("NA");
                    tv_toeflScore.setClickable(true);
                    tv_toeflScore.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            onEditTestScoresClick(view);
                        }
                    });
                }
                    tv_phone.setText(""+mobileNumber);
                Glide.with(image.getContext()).load(profileImgURL).centerCrop().transform(new CircleTransform(image.getContext())).override(50, 50).into(image);
                suggestedUniversities = new ArrayList<>();
                JSONArray suggestedUniversitiesArray = jsonObject.optJSONArray("suggestedUniversities");
                if (suggestedUniversitiesArray != null && suggestedUniversitiesArray.length() > 0) {
                    for (int i = 0; i < suggestedUniversitiesArray.length(); i++) {
                        JSONObject suggestedUniversitiesJsonObject = suggestedUniversitiesArray.optJSONObject(i);
                        University university = new University();
                        university.universityId = suggestedUniversitiesJsonObject.optString("universityID");
                        university.universityName = suggestedUniversitiesJsonObject.optString("universityName");
                        university.address = suggestedUniversitiesJsonObject.optString("universityAddress");
                        suggestedUniversities.add(university);
                    }
                }
                // SET SUGGESTED UNIVERSITY ADAPTER
                SuggestedCollegesAdapter suggestedCollegesAdapter = new SuggestedCollegesAdapter(mContext, suggestedUniversities);
                LinearLayoutManager llm = new LinearLayoutManager(mContext);
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerSuggestedUniversities.setLayoutManager(llm);
                //list.setAdapter( adapter );
                recyclerSuggestedUniversities.setAdapter(suggestedCollegesAdapter);

                suggestedCollegesAdapter.setOnItemClickListener(new SuggestedCollegesAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, University obj, int position) {
                        //Toast.makeText(mContext, "Position: " + position, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(mContext, UniversityActivity.class);
                        intent.putExtra("universityID", obj.universityId);
                        startActivity(intent);
                    }
                });

                //============================
                interestedUniversities = new ArrayList<>();
                JSONArray interestedUniversitiesArray = jsonObject.optJSONArray("universitiesApplied");
                Log.e("interested", "interestedUniversitiesArraysize: " + interestedUniversitiesArray.length());
                if (interestedUniversitiesArray != null && interestedUniversitiesArray.length() > 0) {
                    for (int i = 0; i < interestedUniversitiesArray.length(); i++) {
                        JSONObject interestedUniversitiesJsonObject = interestedUniversitiesArray.optJSONObject(i);
                        University university = new University();
                        university.universityId = interestedUniversitiesJsonObject.optString("universityID");
                        university.universityName = interestedUniversitiesJsonObject.optString("universityName");
                        university.address = interestedUniversitiesJsonObject.optString("universityAddress");
                        interestedUniversities.add(university);
                    }
                }
                // SET SUGGESTED UNIVERSITY ADAPTER
                SuggestedCollegesAdapter interestedUniversitiesAdapter = new SuggestedCollegesAdapter(mContext, interestedUniversities);
                LinearLayoutManager llmInterestedUniversities = new LinearLayoutManager(mContext);
                llmInterestedUniversities.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerAppliedUniversities.setLayoutManager(llmInterestedUniversities);
                //list.setAdapter( adapter );
                recyclerAppliedUniversities.setAdapter(interestedUniversitiesAdapter);

                interestedUniversitiesAdapter.setOnItemClickListener(new SuggestedCollegesAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, University obj, int position) {
                        Toast.makeText(mContext, "Position: " + position, Toast.LENGTH_SHORT).show();
                        Intent intent = new Intent(mContext, UniversityActivity.class);
                        intent.putExtra("universityID", obj.universityId);
                        startActivity(intent);
                    }
                });


            }
        }
    }


    @Override
    public void onError() {

    }


    public void onEditAboutMeClick(View view) {


    }

    private void updateUser(UserClass userClass) {


    }

    public void onEditTestScoresClick(View view) {


    }

    //Download Previous uploaded cv
    public void onDownloadResumeClick(View view) {
        if (uplodedCVUrl.trim().length() > 0) {
            //Some data could not be loaded please check your internet and try again.
            new DownloadFileFromURL().execute(uplodedCVUrl);
        } else {
            Toast.makeText(mContext, "Resume not uploaded by the student.", Toast.LENGTH_LONG).show();
        }

    }

    //Upload a new cv
    public void onUploadResumeClick(View view) {


    }

    /**
     * Reschedule Chat Session
     */
    public void onRescheduleClick(View view) {

    }

    /**
     * Cancel scheduled Chat
     */
    public void onCancelScheduleClick(View view) {


    }

    /**
     * Background Async Task to download file
     */
    class DownloadFileFromURL extends AsyncTask<String, String, String> {

        /**
         * Before starting background thread
         * Show Progress Bar Dialog
         */
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            pDialog = new ProgressDialog(mContext);
            pDialog.setMessage("Downloading file. Please wait...");
            pDialog.setIndeterminate(false);
            pDialog.setMax(100);
            pDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            pDialog.setCancelable(true);
            if (!pDialog.isShowing())
                pDialog.show();
        }

        /**
         * Downloading file in background thread
         */
        @Override
        protected String doInBackground(String... f_url) {
            int count;
            try {
                URL url = new URL(f_url[0]);
                URLConnection conection = url.openConnection();
                conection.connect();
                // getting file length
                int lenghtOfFile = conection.getContentLength();

                // input stream to read file - with 8k buffer
                InputStream input = new BufferedInputStream(url.openStream(), 8192);

                // Output stream to write file
                OutputStream output = new FileOutputStream("/sdcard/" + getFileNameFromUrl(uplodedCVUrl));

                byte data[] = new byte[1024];

                long total = 0;

                while ((count = input.read(data)) != -1) {
                    total += count;
                    // publishing the progress....
                    // After this onProgressUpdate will be called
                    publishProgress("" + (int) ((total * 100) / lenghtOfFile));

                    // writing data to file
                    output.write(data, 0, count);
                }

                // flushing output
                output.flush();

                // closing streams
                output.close();
                input.close();

            } catch (Exception e) {
                Log.e("Error: ", e.getMessage());
            }

            return null;
        }

        /**
         * Updating progress bar
         */
        protected void onProgressUpdate(String... progress) {
            // setting progress percentage
            pDialog.setProgress(Integer.parseInt(progress[0]));
        }

        /**
         * After completing background task
         * Dismiss the progress dialog
         **/
        @Override
        protected void onPostExecute(String file_url) {
            // dismiss the dialog after the file was downloaded
            if (pDialog.isShowing())
                pDialog.dismiss();

            // Displaying downloaded image into image view
            // Reading image path from sdcard
            String imagePath = Environment.getExternalStorageDirectory().toString() + "/" + getFileNameFromUrl(uplodedCVUrl);
            // setting downloaded into image view
            //my_image.setImageDrawable(Drawable.createFromPath(imagePath));
            Log.e("Stored Path", "StoredPath: " + imagePath);
            if (imagePath != null && imagePath.length() > 0) {
                File file = new File(imagePath);
                try {
                    FileOpen.openFile(mContext,file);
                } catch (IOException e) {
                    e.printStackTrace();
                    Toast.makeText(mContext, "No handler for this type of file.", Toast.LENGTH_LONG).show();
                }
            }
        }

    }

    private String getFileNameFromUrl(String url) {
        Log.e("FileName", "Filename URl: " + url);
        String fileName = url.substring(url.lastIndexOf('/') + 1);
        Log.e("FIlename", "filename:" + fileName);
        return fileName;

    }

}
