package com.uowfyp.masterhouse;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
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

public class CreateMissionActivity extends AppCompatActivity {

    EditText etTitle, etDescription, etPrice;
    Spinner spinnerLocation, spinnerCategory, spinnerPriceType;
    Button btnSumbit;
    MissionPost newMissionPost;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        etTitle = (EditText)findViewById(R.id.etTitle);
        etDescription = (EditText)findViewById(R.id.etDescription);
        spinnerLocation = (Spinner)findViewById(R.id.spinnerLocation);
        spinnerCategory = (Spinner)findViewById(R.id.spinnerCategory);
        spinnerPriceType = (Spinner)findViewById(R.id.spinnerPriceType);
        etPrice = (EditText)findViewById(R.id.etPrice);

        btnSumbit = (Button)findViewById(R.id.btnSumbit);
        newMissionPost = new MissionPost();


        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();
        final DatabaseReference dbReff = FirebaseDatabase.getInstance().getReference("Posts");
        final String date = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());

        btnSumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                newMissionPost.setTitle(etTitle.getText().toString());
                newMissionPost.setDescription(etDescription.getText().toString());
                newMissionPost.setCategory(spinnerCategory.getSelectedItem().toString());
                newMissionPost.setLocation(spinnerLocation.getSelectedItem().toString());
                newMissionPost.setPrice(etPrice.getText().toString());
                newMissionPost.setPriceType(spinnerPriceType.getSelectedItem().toString());

                newMissionPost.setUid(uid);
                newMissionPost.setDate(date);
                dbReff.push().setValue(newMissionPost).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(CreateMissionActivity.this,"MissionPost created",Toast.LENGTH_LONG).show();
                            startActivity(new Intent(CreateMissionActivity.this, MainActivity.class));
                            finish();
                        }
                    }
                });
            }
        });

    }
}
