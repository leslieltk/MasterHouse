package com.uowfyp.masterhouse;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.Calendar;

public class CreatePostActivity extends AppCompatActivity {

    EditText etxtTitle, etxtDescription, etxtSalary;
    Spinner spinnerLocation, spinnerType;
    Button btnAdd;
    Post addPost;
    //    FirebaseAuth auth;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        etxtTitle = (EditText)findViewById(R.id.newpostTitle);
        etxtDescription = (EditText)findViewById(R.id.etxtDescription);
        spinnerLocation = (Spinner)findViewById(R.id.spinnerLocation);
        spinnerType = (Spinner)findViewById(R.id.spinnerType);
        etxtSalary = (EditText)findViewById(R.id.etxtSalary);

        btnAdd = (Button)findViewById(R.id.btnAdd);
        addPost = new Post();


        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();
        final DatabaseReference dbReff = FirebaseDatabase.getInstance().getReference("Posts");
        final String date = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addPost.setTitle(etxtTitle.getText().toString());
                addPost.setDescription(etxtDescription.getText().toString());
                addPost.setLocation(spinnerLocation.getSelectedItem().toString());
                addPost.setSalary(etxtSalary.getText().toString());
                addPost.setType(spinnerType.getSelectedItem().toString());
                addPost.setUid(uid);
                addPost.setPostDate(date);
                dbReff.push().setValue(addPost).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(CreatePostActivity.this,"Post created",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(CreatePostActivity.this, MainActivity.class));
                            finish();
                        }
                    }
                });
            }
        });

    }
}
