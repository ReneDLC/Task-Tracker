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
    RecycleAdapter adapter;
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
        taskList = new ArrayList<>();
        addFAB = (FloatingActionButton) findViewById(R.id.floatingAdd);
        recyclerView = (RecyclerView) findViewById(R.id.taskRecyclerView);
        LinearLayoutManager llm = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(llm);
        adapter = new RecycleAdapter();
        recyclerView.setAdapter(adapter);

        adapter.notifyDataSetChanged();

        //display user email in welcomeText
        welcomeText.setText("Welcome " + user.getEmail());
        //add listener to button
        logoutBtn.setOnClickListener(this);
        addFAB.setOnClickListener(this);
    }

    @Override
    protected void onResume()
    {
        super.onResume();
        FirebaseUser user = firebaseAuth.getCurrentUser();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        database.getReference("Task List").child(user.getUid()).addListenerForSingleValueEvent(
                new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        taskList.clear();
                        Log.w("TaskApp", "getUser:onCancelled " + dataSnapshot.toString());
                        Log.w("TaskApp", "count = " + String.valueOf(dataSnapshot.getChildrenCount()) + " values " + dataSnapshot.getKey());
                        for (DataSnapshot data : dataSnapshot.getChildren())
                        {
                            Task task = data.getValue(Task.class);
                            task.setKey(data.getKey());
                            taskList.add(task);
                        }
                        adapter.notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                        Log.w("TaskApp", "getUser:onCancelled", databaseError.toException());
                    }
                }
        );
    }

    private class RecycleAdapter extends RecyclerView.Adapter {
        @Override
        public int getItemCount()
        {
            return taskList.size();
        }

        @Override
        public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.task_item, parent, false);
            SimpleItemViewHolder pvh = new SimpleItemViewHolder(v);
            return pvh;
        }

        @Override
        public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
            SimpleItemViewHolder viewHolder = (SimpleItemViewHolder) holder;
            viewHolder.position = position;
            Task task = taskList.get(position);
            ((SimpleItemViewHolder) holder).title.setText(task.getTitle());
        }

        public final  class SimpleItemViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
            TextView title;
            public int position;
            public SimpleItemViewHolder(View itemView) {
                super(itemView);
                itemView.setOnClickListener(this);
                title = (TextView) itemView.findViewById(R.id.titleView);
            }

            @Override
            public void onClick(View view) {
                Intent newIntent = new Intent(getApplicationContext(), AddTask.class);
                newIntent.putExtra("task", taskList.get(position));
                ProfileActivity.this.startActivity(newIntent);
            }
        }
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


