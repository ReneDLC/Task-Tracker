package com.task.sharelist.sharelist;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.content.Intent;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.task.sharelist.sharelist.Model.Task;

import java.util.ArrayList;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private Button logoutBtn;
    private TextView welcomeText;
    private FirebaseAuth firebaseAuth;
    private FloatingActionButton addFAB;
    private RecyclerView recyclerView;
    ArrayList<Task> taskList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        firebaseAuth = FirebaseAuth.getInstance();

        //if user not logged in, return null, go back to login page
        if (firebaseAuth.getCurrentUser() == null) {
            finish();
            startActivity(new Intent(getApplicationContext(), ShareList.class));
        }

        // get current user
        FirebaseUser user = firebaseAuth.getCurrentUser();

        // init
        welcomeText = (TextView) findViewById(R.id.welcomeText);
        logoutBtn = (Button) findViewById(R.id.logout);
        addFAB = (FloatingActionButton) findViewById(R.id.floatingAdd);
        recyclerView = (RecyclerView) findViewById(R.id.taskRecycleView);
        //display user email in welcomeText
        welcomeText.setText("Welcome " + user.getEmail());
        //add listener to button
        logoutBtn.setOnClickListener(this);
        addFAB.setOnClickListener(this);
    }





    @Override
    public void onClick(View view) {
        if (view == logoutBtn) {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(getApplicationContext(), ShareList.class));
        }
        if (view == addFAB) {
            finish();
            startActivity(new Intent(getApplicationContext(), AddTask.class));
        }

    }
}


