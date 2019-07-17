package com.uowfyp.masterhouse;

import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CreateMissionActivity extends AppCompatActivity {

    EditText etTitle, etDescription, etPrice;
    Spinner spinnerLocation, spinnerCategory, spinnerPriceType;
    Button btnSumbit;
    MissionPost newMissionPost;
    FirebaseAuth auth;
    String title, desc, category, location, priceType, price;
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    final String currentTime = dateFormat.format(calendar.getTime());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        auth = FirebaseAuth.getInstance();

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
        final DatabaseReference userReff = FirebaseDatabase.getInstance().getReference("Users");
//        final String date = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        DatabaseReference pushedPostRef = dbReff.push();
        final String postId = pushedPostRef.getKey();



        btnSumbit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                title = etTitle.getText().toString();
                category = spinnerCategory.getSelectedItem().toString();
                desc = etDescription.getText().toString();
                location = spinnerLocation.getSelectedItem().toString();
                price = etPrice.getText().toString();
                priceType = spinnerPriceType.getSelectedItem().toString();

                if(title.isEmpty()){
                    etTitle.setError("Please input a title");
                }else if (category.equals("Please Select")){
                    ((TextView)spinnerCategory.getSelectedView()).setError("");
                }else if (desc.isEmpty()){
                    etDescription.setError("please input some description about this mission");
                }else if (location.equals("Please Select")){
                    ((TextView)spinnerLocation.getSelectedView()).setError("");
                }else if (price.isEmpty()){
                    etPrice.setError("Please input a price");
                }else {

                newMissionPost.setTitle(etTitle.getText().toString());
                newMissionPost.setDescription(etDescription.getText().toString());
                newMissionPost.setCategory(spinnerCategory.getSelectedItem().toString());
                newMissionPost.setLocation(spinnerLocation.getSelectedItem().toString());
                newMissionPost.setPrice(etPrice.getText().toString());
                newMissionPost.setPriceType(spinnerPriceType.getSelectedItem().toString());

                newMissionPost.setUid(uid);
                newMissionPost.setDate(currentTime);

                userReff.child(auth.getCurrentUser().getUid()).child("posts").child(postId).setValue(true);

                dbReff.child(postId).setValue(newMissionPost).addOnCompleteListener(new OnCompleteListener<Void>() {
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
            }
        });

    }
}
