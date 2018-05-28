package com.nucleustech.mymentor.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.design.widget.Snackbar;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.storage.FirebaseStorage;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nucleustech.mymentor.R;
import com.nucleustech.mymentor.adapter.CircleTransform;
import com.nucleustech.mymentor.adapter.FriendsListAdapter;
import com.nucleustech.mymentor.model.Student;
import com.nucleustech.mymentor.model.StudentList;
import com.nucleustech.mymentor.model.UserClass;
import com.nucleustech.mymentor.model.UserModel;
import com.nucleustech.mymentor.util.BusHolder;
import com.nucleustech.mymentor.util.DataSetUpdatedEvent;
import com.nucleustech.mymentor.util.Util;
import com.nucleustech.mymentor.volley.ServerResponseCallback;
import com.nucleustech.mymentor.volley.VolleyTaskManager;

import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;
import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * @author ritwik.rai
 */
public class SelectStudentActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener, ServerResponseCallback {

    ImageView iv;

    private static final int IMAGE_GALLERY_REQUEST = 1;
    private static final int IMAGE_CAMERA_REQUEST = 2;
    private static final int PLACE_PICKER_REQUEST = 3;

    static final String TAG = SelectStudentActivity.class.getSimpleName();
    static final String CHAT_REFERENCE = "chatmodel";

    //Firebase and GoogleApiClient
    private FirebaseAuth mFirebaseAuth;
    private FirebaseUser mFirebaseUser;
    private GoogleApiClient mGoogleApiClient;
    private DatabaseReference mFirebaseDatabaseReference;
    FirebaseStorage storage = FirebaseStorage.getInstance();

    //CLass Model
    private UserModel userModel;

    //Views UI
    private RecyclerView recyclerViewStudentList;
    private VolleyTaskManager volleyTaskManager;
    private Context mContext;
    private FriendsListAdapter adapter;
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_student);

        mContext = SelectStudentActivity.this;

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        TextView tv_profile = (TextView) toolbar.findViewById(R.id.tv_profile);
        tv_profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(mContext, ProfileActivity.class));
            }
        });


        ImageLoader imageLoader = ImageLoader.getInstance();
        if (!imageLoader.isInited()) {
            imageLoader.init(ImageLoaderConfiguration.createDefault(this));
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        View headerView = navigationView.getHeaderView(0);
        String name = Util.fetchUserClass(SelectStudentActivity.this).displayName;
        String url = Util.fetchUserClass(SelectStudentActivity.this).profileUrl;

        iv = (ImageView) headerView.findViewById(R.id.imageView_profile);
        TextView tv_navName = headerView.findViewById(R.id.tv_navName);

        tv_navName.setText("" + name);

        Glide.with(iv.getContext()).load(url).centerCrop().transform(new CircleTransform(iv.getContext())).override(50, 50).into(iv);

        navigationView.setNavigationItemSelectedListener(this);

        volleyTaskManager = new VolleyTaskManager(mContext);
        if (!Util.verificaConexao(this)) {
            Util.initToast(this, "You do not have an internet connection.");
            finish();
        } else {
            bindViews();
            verifyUserLogin();

        }
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else if (!doubleBackToExitPressedOnce) {
            this.doubleBackToExitPressedOnce = true;
            Toast.makeText(this, "Please click BACK again to exit.", Toast.LENGTH_SHORT).show();

            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    doubleBackToExitPressedOnce = false;
                }
            }, 2000);
        } else {
            super.onBackPressed();
            return;
        }
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Main Activity
            Intent intent = new Intent(mContext, SelectStudentActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);

        } else if (id == R.id.nav_gallery) {

            if (Util.fetchUserClass(mContext).selectedStudent != null)
                startActivity(new Intent(mContext, ViewUserAllScheduleActivity.class));
            else
                Toast.makeText(mContext, "You should chat first.", Toast.LENGTH_SHORT).show();

        } else if (id == R.id.nav_slideshow) {

            startActivity(new Intent(mContext, AllUniversitiesActivity.class));

        } else if (id == R.id.nav_manage) {

            startActivity(new Intent(mContext, ProfileActivity.class));

        } else if (id == R.id.nav_share) {

            signOut();

        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    /**
     * Verify user is logged in
     */
    private void verifyUserLogin() {
        mFirebaseAuth = FirebaseAuth.getInstance();
        mFirebaseUser = mFirebaseAuth.getCurrentUser();
        if (mFirebaseUser == null) {
            startActivity(new Intent(this, SigninActivity.class));
            finish();
        } else {
            userModel = new UserModel(mFirebaseUser.getDisplayName(), mFirebaseUser.getPhotoUrl().toString(), mFirebaseUser.getUid());
            volleyTaskManager.doGetStudentList(new HashMap<String, String>(), true);

        }
    }

    /**
     * Link views with Java API
     */
    private void bindViews() {
        recyclerViewStudentList = (RecyclerView) findViewById(R.id.recyclerViewStudentList);
    }

    /**
     * Sign Out no login
     */
    private void signOut() {
        mFirebaseAuth.signOut();
        //Auth.GoogleSignInApi.signOut(mGoogleApiClient);
        startActivity(new Intent(this, SigninActivity.class));
        finish();
    }


    @Override
    public void onSuccess(JSONObject resultJsonObject) {

        if (resultJsonObject.optString("code").trim().equalsIgnoreCase("200")) {
            try {
                JSONArray studentJsonArray = resultJsonObject.optJSONArray("students");
                ArrayList<Student> students = new ArrayList<>();
                HashMap<String, Student> studentArrayMap = new HashMap<>();
                for (int i = 0; i < studentJsonArray.length(); i++) {
                    JSONObject studentJsonObj = studentJsonArray.optJSONObject(i);
                    Student student = new Student();
                    student.name = studentJsonObj.optString("name");
                    student.emailId = studentJsonObj.optString("emailID");
                    student.mobile = studentJsonObj.optString("mobile");
                    student.userID = studentJsonObj.optString("userID");
                    student.studentFirebaseId = studentJsonObj.optString("fid");
                    student.profileImgURL = studentJsonObj.optString("profileImgURL");
                    students.add(student);
                    studentArrayMap.put("" + student.emailId, student);
                }

                StudentList studentList = Util.fetchStudentList(mContext);
                if (studentList == null) {
                    //First Time Student List Fetch
                    studentList = new StudentList();
                    studentList.studentsArrayList = students;
                    studentList.studentArrayMap = studentArrayMap;
                    Util.saveStudentList(mContext, studentList);
                } else if (studentList != null) {
                    // Not the First Time
                    Log.e("NotFirstTime", "Not the First Time");

                    ArrayList<Student> oldList = studentList.studentsArrayList;
                    ArrayList<Student> newList = students;
                    ArrayList<Student> commonList = new ArrayList<>();
                    ArrayList<Student> newAddedStudentsList = new ArrayList<>();
                    for (Student student1 : oldList) {
                        Log.e("Unreadcount", "Unreadcount: " + student1.unreadMsgCount);
                        for (Student student2 : newList) {
                            if (student1.emailId.equalsIgnoreCase(student2.emailId)) {
                                //This gives the common elements
                                commonList.add(student1);
                            }
                        }
                    }


                    ArrayList<Integer> results = new ArrayList<>();

                    // Loop arrayList2 items
                    for (Student person2 : newList) {
                        // Loop arrayList1 items
                        boolean found = false;
                        for (Student person1 : commonList) {
                            if (person2.emailId.equalsIgnoreCase(person1.emailId)) {
                                found = true;
                            }
                        }
                        if (!found) {
                            newAddedStudentsList.add(person2);
                        }
                    }
                   /* //Now get New Elements
                    for(Student student1:newList){
                        for(Student student2:commonList){
                            if(!(student1.emailId.trim().equalsIgnoreCase(student2.emailId.trim()))){
                                // This gives new elements that are added
                                newAddedStudentsList.add(student1);
                            }
                        }
                    }*/
                    Log.e("New Files", "Common Files Size: " + commonList.size());
                    Log.e("New Files", "New Files Size: " + newAddedStudentsList.size());

                    //Now Final List is
                    ArrayList<Student> finalList = new ArrayList<>(commonList);
                    finalList.addAll(newAddedStudentsList);
                    Log.e("Final List", "Final List: " + finalList);
                    Log.e("Final List", "Final List Size: " + finalList.size());

                    studentList.studentsArrayList = finalList;
                    Util.saveStudentList(mContext, studentList);
                }

                //Set filter for Search
                final ArrayList<Student> studentNewList= studentList.studentsArrayList;
                ImageButton ib_filter = (ImageButton) toolbar.findViewById(R.id.ib_filter);
                ib_filter.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent=new Intent(mContext, SearchViewActivity.class);
                        intent.putExtra("students",studentNewList);
                        startActivity(intent);
                    }
                });

                // Set Students List View and Adapter
                adapter = new FriendsListAdapter(mContext, studentList.studentsArrayList);
                LinearLayoutManager llm = new LinearLayoutManager(mContext);
                llm.setOrientation(LinearLayoutManager.VERTICAL);
                recyclerViewStudentList.setLayoutManager(llm);
                //list.setAdapter( adapter );
                recyclerViewStudentList.setAdapter(adapter);

                adapter.setOnItemClickListener(new FriendsListAdapter.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, Student obj, int position) {
                        //Toast.makeText(mContext, "Position: " + position, Toast.LENGTH_SHORT).show();

                        UserClass userClass = Util.fetchUserClass(mContext);
                        if (userClass == null)
                            userClass = new UserClass();

                        userClass.selectedStudent = obj;
                        Util.saveUserClass(mContext, userClass);
                        StudentList studentList1 = Util.fetchStudentList(mContext);
                        if (studentList1.studentsArrayList.get(position).unreadMsgCount > 0) {
                            studentList1.studentsArrayList.get(position).unreadMsgCount = 0;
                            Util.saveStudentList(mContext, studentList1);
                        }

                        Intent intent = new Intent(mContext, StudentChatActivity.class);
                        intent.putExtra("name", obj.name);
                        intent.putExtra("email", obj.emailId);
                        intent.putExtra("studentId", obj.userID);
                        intent.putExtra("firebaseId", obj.studentFirebaseId);
                        startActivity(intent);
                    }
                });

            } catch (Exception e) {
                e.printStackTrace();
                Util.showMessageWithOk(SelectStudentActivity.this, "Something went wrong. Please try again later.");

            }
        }


    }

    @Override
    public void onError() {

    }

    @Override
    protected void onResume() {
        super.onResume();
        BusHolder.getInstnace().register(this);
    }

    @Override
    protected void onPause() {
        super.onPause();
        BusHolder.getInstnace().unregister(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onDatasetUpdated(DataSetUpdatedEvent event) {
        //Update RecyclerView
        Toast.makeText(mContext, "asfjskfjklsjdf", Toast.LENGTH_SHORT).show();
        StudentList studentList = Util.fetchStudentList(mContext);
        adapter = new FriendsListAdapter(mContext, studentList.studentsArrayList);
        recyclerViewStudentList.setAdapter(adapter);
        LinearLayoutManager llm = new LinearLayoutManager(mContext);
        llm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerViewStudentList.setLayoutManager(llm);
        //list.setAdapter( adapter );
        recyclerViewStudentList.setAdapter(adapter);

        adapter.setOnItemClickListener(new FriendsListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, Student obj, int position) {
                //Toast.makeText(mContext, "Position: " + position, Toast.LENGTH_SHORT).show();

                UserClass userClass = Util.fetchUserClass(mContext);
                if (userClass == null)
                    userClass = new UserClass();

                userClass.selectedStudent = obj;
                Util.saveUserClass(mContext, userClass);
                StudentList studentList1 = Util.fetchStudentList(mContext);
                if (studentList1.studentsArrayList.get(position).unreadMsgCount > 0) {
                    studentList1.studentsArrayList.get(position).unreadMsgCount = 0;
                    Util.saveStudentList(mContext, studentList1);
                }

                Intent intent = new Intent(mContext, StudentChatActivity.class);
                intent.putExtra("name", obj.name);
                intent.putExtra("email", obj.emailId);
                intent.putExtra("firebaseId", obj.studentFirebaseId);

                startActivity(intent);
            }
        });
        adapter.notifyDataSetChanged();

    }
}
