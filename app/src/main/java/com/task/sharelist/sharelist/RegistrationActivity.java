package com.task.sharelist.sharelist;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.task.sharelist.sharelist.Model.User;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class RegistrationActivity extends AppCompatActivity implements View.OnClickListener {


    private FirebaseAuth firebaseAuth;
    private EditText regName, regPassword;
    private Button registerBtn;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        firebaseAuth = FirebaseAuth.getInstance();

        //if user already logged in
        if (firebaseAuth.getCurrentUser() != null)
        {
            finish();
            //go to profile page
            startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
        }

        //init views
        regName = (EditText) findViewById(R.id.regName);
        regPassword = (EditText) findViewById(R.id.regPassword);
        registerBtn = (Button) findViewById(R.id.registerBtn);
        progressDialog = new ProgressDialog(this);

        registerBtn.setOnClickListener(this);
    }

    private void registerUser() {
        String email = regName.getText().toString().trim();
        String password = regPassword.getText().toString().trim();

        //check text field
        if(TextUtils.isEmpty(email))
        {
            Toast.makeText(this, "Please Enter Email", Toast.LENGTH_LONG).show();
            return;
        }
        if(TextUtils.isEmpty(password))
        {
            Toast.makeText(this, "Please Enter Password", Toast.LENGTH_LONG).show();
            return;
        }

        //display progress dialog for registration
        progressDialog.setMessage("Registering...");
        progressDialog.show();

        //creating User
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>()
        {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task)
            {
                //check if successful
                if(task.isSuccessful())
                {
                    finish();
                    startActivity(new Intent(getApplicationContext(), ProfileActivity.class));
                }
                //display unsuccessful message
                else
                {
                    Toast.makeText(getApplicationContext(), "Registration Error", Toast.LENGTH_LONG).show();
                }
                progressDialog.dismiss();
            }
        });
    }

    @Override
    public void onClick(View view)
    {
        if (view == registerBtn)
        {
            registerUser();
        }
    }
}
