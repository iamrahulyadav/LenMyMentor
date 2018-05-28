package com.nucleustech.mymentor.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.nucleustech.mymentor.R;
import com.nucleustech.mymentor.adapter.GridListAdapter;
import com.nucleustech.mymentor.constant.FilterType;
import com.nucleustech.mymentor.model.Student;
import com.nucleustech.mymentor.model.StudentList;
import com.nucleustech.mymentor.model.UserClass;
import com.nucleustech.mymentor.util.Util;

import java.util.ArrayList;

/**
 * Created by ritwik.rai on 08/02/17.
 */
public class SearchViewActivity extends AppCompatActivity implements View.OnClickListener {
    private Context context;
    private GridListAdapter adapter;
    private ArrayList<Student> arrayList;
   // private RadioGroup searchViaRadioGroup, filterByRadioGroup;
    private EditText searchEditText;
    //private TextView searchViaLabel, filterByLabel;

    /*  Filter Type to identify the type of Filter  */
    private FilterType filterType;

    /*  boolean variable for Filtering */
    private boolean isSearchWithPrefix = false;
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
        setContentView(R.layout.list_view_fragment);
        context=SearchViewActivity.this;
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Search");
        arrayList =(ArrayList<Student>) getIntent().getSerializableExtra("students");
        loadListView();
        findViews();
        implementEvents();
    }




    //Bind all Views
    private void findViews() {
        filterType = FilterType.NAME;
        /*searchViaRadioGroup = (RadioGroup) findViewById(R.id.search_via_radio_group);
        filterByRadioGroup = (RadioGroup) view.findViewById(R.id.filter_type_radio_group);*/
        searchEditText = (EditText) findViewById(R.id.search_text);

        /*searchViaLabel = (TextView) view.findViewById(R.id.search_via_label);
        filterByLabel = (TextView) view.findViewById(R.id.filter_by_label);*/
    }


    private void loadListView() {
        ListView listView = (ListView)findViewById(R.id.list_view);

        adapter = new GridListAdapter(context, arrayList, true);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l) {
                //Toast.makeText(context,"Item: "+arrayList.get(position).name,Toast.LENGTH_SHORT).show();
                UserClass userClass = Util.fetchUserClass(context);
                if (userClass == null)
                    userClass = new UserClass();

                userClass.selectedStudent = arrayList.get(position);
                Util.saveUserClass(context, userClass);
                StudentList studentList1 = Util.fetchStudentList(context);
                for (Student student:studentList1.studentsArrayList){
                    if((student.emailId.equalsIgnoreCase(arrayList.get(position).emailId ))&& (studentList1.studentsArrayList.get(position).unreadMsgCount > 0)){
                        studentList1.studentsArrayList.get(position).unreadMsgCount = 0;
                        Util.saveStudentList(context, studentList1);
                    }
                }

                Intent intent = new Intent(context, StudentChatActivity.class);
                intent.putExtra("name", arrayList.get(position).name);
                intent.putExtra("email", arrayList.get(position).emailId);
                intent.putExtra("studentId", arrayList.get(position).userID);
                intent.putExtra("firebaseId", arrayList.get(position).studentFirebaseId);
                startActivity(intent);
            }
        });
    }

    private void implementEvents() {
        /*filterByRadioGroup.setOnCheckedChangeListener(this);
        searchViaRadioGroup.setOnCheckedChangeListener(this);
        searchViaLabel.setOnClickListener(this);
        filterByLabel.setOnClickListener(this);*/

        searchEditText.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                //On text changed in Edit text start filtering the list
                adapter.filter(filterType, charSequence.toString(), isSearchWithPrefix);
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }



    @Override
    public void onClick(View view) {
        /*switch (view.getId()) {
            case R.id.search_via_label:
                //show hide the radio group
                if (searchViaRadioGroup.isShown()) {
                    searchViaLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.up_dropdown, 0);
                    searchViaRadioGroup.setVisibility(View.GONE);
                } else {
                    searchViaLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_dropdown, 0);
                    searchViaRadioGroup.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.filter_by_label:
                //show hide the radio group
                if (filterByRadioGroup.isShown()) {
                    filterByLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.up_dropdown, 0);
                    filterByRadioGroup.setVisibility(View.GONE);
                } else {
                    filterByLabel.setCompoundDrawablesWithIntrinsicBounds(0, 0, R.drawable.down_dropdown, 0);
                    filterByRadioGroup.setVisibility(View.VISIBLE);
                }
                break;
        }*/
    }

}
