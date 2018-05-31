package com.task.sharelist.sharelist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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

public class RegistrationActivity extends AppCompatActivity {

    FirebaseDatabase database;
    DatabaseReference dbRef;

    EditText regName, regPassword;
    Button registerBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);

        database = FirebaseDatabase.getInstance();
        dbRef = database.getReference("Users");

        regName = (EditText) findViewById(R.id.regName);
        regPassword = (EditText) findViewById(R.id.regPassword);

        registerBtn = (Button) findViewById(R.id.registerBtn);

        registerBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final User user = new User( regName.getText().toString(), regPassword.getText().toString() );

                dbRef.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {
                        if(dataSnapshot.child(user.getUsername()).exists())
                            Toast.makeText(RegistrationActivity.this, "The Username Already Exists", Toast.LENGTH_SHORT).show();
                        else
                        {
                            dbRef.child(user.getUsername()).setValue(user);
                            Toast.makeText(RegistrationActivity.this, "Registered", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

    }
}
