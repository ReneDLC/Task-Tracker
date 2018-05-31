package com.task.sharelist.sharelist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.content.Intent;


import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class ProfileActivity extends AppCompatActivity implements View.OnClickListener {

    private Button logoutBtn;
    private TextView userEmail;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);
        firebaseAuth = FirebaseAuth.getInstance();

        //if user not logged in, return null, go back to login page
        if(firebaseAuth.getCurrentUser() == null)
        {
            finish();
            startActivity(new Intent(getApplicationContext(), ShareList.class));
        }

        //get current user
        FirebaseUser user = firebaseAuth.getCurrentUser();

        userEmail = (TextView) findViewById(R.id.textEmail);
        logoutBtn = (Button) findViewById(R.id.logout);

        //display user email in text
        userEmail.setText("Welcome " + user.getEmail());

        //add listener to button
        logoutBtn.setOnClickListener(this);
    }
    @Override
    public void onClick(View view)
    {
        if(view == logoutBtn)
        {
            firebaseAuth.signOut();
            finish();
            startActivity(new Intent(getApplicationContext(), ShareList.class));
        }

    }

}
