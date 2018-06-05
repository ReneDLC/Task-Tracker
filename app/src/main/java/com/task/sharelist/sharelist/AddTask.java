package com.task.sharelist.sharelist;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.task.sharelist.sharelist.Model.Task;


import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class AddTask extends AppCompatActivity implements View.OnClickListener{

    private FirebaseDatabase database;
    private DatabaseReference mReference;
    private DatabaseReference mRef;
    private FirebaseAuth firebaseAuth;
    private Button saveTaskBtn;
    private Button cancelTaskBtn;
    private Button deleteBtn;
    private EditText titleText;
    private EditText descText;
    private TextView taskDate;
    private ProgressDialog progressDialog;
    private DatePickerDialog.OnDateSetListener mDateSetListener;
    private String key;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_task);

        firebaseAuth = FirebaseAuth.getInstance();

        if(firebaseAuth.getCurrentUser() == null)
        {
            finish();
            startActivity(new Intent(getApplicationContext(), ShareList.class));
        }

        FirebaseUser user = firebaseAuth.getCurrentUser();

        database = FirebaseDatabase.getInstance();
        mReference = database.getReference("Task List");
        mRef = mReference.child(user.getUid());
        taskDate = (TextView) findViewById(R.id.taskDate);
        saveTaskBtn = (Button) findViewById(R.id.saveTaskBtn);
        cancelTaskBtn = (Button) findViewById(R.id.cancelBtn);
        deleteBtn = (Button) findViewById(R.id.deleteBtn);
        titleText = (EditText) findViewById(R.id.taskTitle);
        descText = (EditText) findViewById(R.id.taskDesc);
        taskDate = (TextView) findViewById(R.id.taskDate);
        progressDialog = new ProgressDialog(this);

        taskDate.setOnClickListener(this);
        saveTaskBtn.setOnClickListener(this);
        cancelTaskBtn.setOnClickListener(this);
        deleteBtn.setOnClickListener(this);

        if(getIntent().getExtras() != null)
        {
            Bundle extras = getIntent().getExtras();
            Task task = (Task)extras.get("task");
            if(task != null)
            {
                key = task.getKey();
                titleText.setText(task.getTitle());
                descText.setText(task.getDescription());
                taskDate.setText(task.getDate());
            }
        }

        mDateSetListener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth)
            {
                month = month + 1;
                String date = month + "/" + dayOfMonth + "/" + year;
                taskDate.setText(date);
            }
        };

    }

    void saveTask()
    {
        if (key == null)
        {
            key = mRef.push().getKey();
        }

        Task task = new Task();
        task.setTitle(titleText.getText().toString());
        task.setDescription(descText.getText().toString());
        task.setDate(taskDate.getText().toString());
        task.setKey(key);

        Map<String, Object> childUpdates = new HashMap<>();
        childUpdates.put(key, task.toFirebaseObject());
        mRef.updateChildren(childUpdates, new DatabaseReference.CompletionListener() {
            @Override
            public void onComplete (DatabaseError databaseError, DatabaseReference databaseReference)
            {
                if (databaseError == null)
                {
                    finish();
                }
            }
        });
    }

    void deleteTask()
    {
        mRef.child(key).removeValue();

    }

    public void onClick(View view)
    {
        if(view == saveTaskBtn)
        {
            progressDialog.setMessage("Saving Task");
            saveTask();
            progressDialog.dismiss();
            finish();
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }

        if(view == cancelTaskBtn)
        {
            finish();
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }

        if(view == taskDate)
        {
            Calendar cal = Calendar.getInstance();
            int year = cal.get(Calendar.YEAR);
            int month = cal.get(Calendar.MONTH);
            int day = cal.get(Calendar.DAY_OF_MONTH);

            DatePickerDialog dialog = new DatePickerDialog(
                    AddTask.this,
                    android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                    mDateSetListener,
                    year,month,day

            );
            dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            dialog.show();
        }

        if(view == deleteBtn)
        {
            deleteTask();
            finish();
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));

        }
    }
}
