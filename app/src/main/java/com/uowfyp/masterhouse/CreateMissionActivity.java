package com.uowfyp.masterhouse;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
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
    Button btnSumbit, btnStartDate, btnStartTime, btnEndDate, btnEndTime;
    MissionPost newMissionPost;
    FirebaseAuth auth;
    String title, desc, category, location, priceType, price, startDate, startTime, endDate, endTime;
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm:ss");
    final String currentDate = dateFormat.format(calendar.getTime());
    final String currentTime = timeFormat.format(calendar.getTime());
    DatePickerDialog.OnDateSetListener dateListener1, dateListener2;
    TimePickerDialog.OnTimeSetListener timeListener1, timeListener2;
    int year, month, day, rhour, rmin, syear, smonth, sday, checkhour, checkmin;
    boolean checkTime;

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
        btnStartDate = (Button)findViewById(R.id.btnStartDate);
        btnEndDate = (Button)findViewById(R.id.btnEndDate);
        btnStartTime = (Button)findViewById(R.id.btnStartTime);
        btnEndTime = (Button)findViewById(R.id.btnEndTime);


        btnSumbit = (Button)findViewById(R.id.btnSumbit);
        newMissionPost = new MissionPost();

        year = calendar.get(calendar.YEAR);
        month = calendar.get(calendar.MONTH);
        day = calendar.get(calendar.DAY_OF_MONTH);


        final FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        final String uid = user.getUid();
        final DatabaseReference dbReff = FirebaseDatabase.getInstance().getReference("Posts");
        final DatabaseReference userReff = FirebaseDatabase.getInstance().getReference("Users");
//        final String date = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
        DatabaseReference pushedPostRef = dbReff.push();
        final String postId = pushedPostRef.getKey();

        btnStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateSelect(dateListener1);
            }
        });

        dateListener1 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {
                btnStartDate.setError(null);
                syear = year;
                smonth = month;
                sday = day;
                startDate = day + "/" + month + "/" + year;
                btnStartDate.setText(startDate);

            }
        };

        btnEndDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dateSelect(dateListener2);
            }
        });

        dateListener2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                    btnStartDate.setError(null);
                    if (syear > year) {
                        btnEndDate.setError("");
                        Toast.makeText(CreateMissionActivity.this, "Plesae input a valid date", Toast.LENGTH_SHORT).show();
                    } else if (smonth > month) {
                        btnEndDate.setError("");
                        Toast.makeText(CreateMissionActivity.this, "Plesae input a valid date", Toast.LENGTH_SHORT).show();
                    } else if (sday > day) {
                        btnEndDate.setError("");
                        Toast.makeText(CreateMissionActivity.this, "Plesae input a valid date", Toast.LENGTH_SHORT).show();
                    } else {
                        btnEndDate.setError(null);
                        endDate = day + "/" + month + "/" + year;
                        btnEndDate.setText(endDate);
                    }

                    if (syear == year && sday == day && smonth == month) {
                        checkTime = true;
                    }else {
                        checkTime = false;
                    }




            }
        };

        btnStartTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeSelect(timeListener1);
            }
        });

        timeListener1 = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int min) {
                checkhour = hour;
                checkmin = min;
                String smin = null;
                if (min < 10)
                {
                    smin  = "0" +min;
                }else {
                    smin ="" +smin;
                }
                startTime = hour + ":" + smin;
                btnStartTime.setText(startTime);
                rhour = hour;
                rmin = min;
            }
        };

        btnEndTime.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timeSelect(timeListener2);
            }
        });

        timeListener2 = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int min) {

                if (checkTime == true){
                    if (checkhour > hour){
                        btnEndTime.setError("");
                        Toast.makeText(CreateMissionActivity.this, "Plesae input a valid Time", Toast.LENGTH_SHORT).show();
                    }else if (checkmin > min){
                        btnEndTime.setError("");
                        Toast.makeText(CreateMissionActivity.this, "Plesae input a valid Time", Toast.LENGTH_SHORT).show();
                    }else {
                        btnEndTime.setError(null);
                        endTime = hour + ":" + min;
                        btnEndTime.setText(endTime);
                    }
                }else {
                    btnEndTime.setError(null);
                    String smin = null;
                    if (min < 10)
                    {
                        smin  = "0" +min;
                    }else {
                        smin ="" +min;
                    }
                    endTime = hour + ":" + smin;
                    btnEndTime.setText(endTime);
                }


                rhour = hour;
                rmin = min;
            }
        };


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
                }else if(startDate.isEmpty() || startTime.isEmpty() || endDate.isEmpty() || endTime.isEmpty()){
                    Toast.makeText(CreateMissionActivity.this, "Plesae input Date and Time", Toast.LENGTH_SHORT).show();
                }
                else {

                newMissionPost.setTitle(etTitle.getText().toString());
                newMissionPost.setDescription(etDescription.getText().toString());
                newMissionPost.setCategory(spinnerCategory.getSelectedItem().toString());
                newMissionPost.setLocation(spinnerLocation.getSelectedItem().toString());
                newMissionPost.setPrice(etPrice.getText().toString());
                newMissionPost.setPriceType(spinnerPriceType.getSelectedItem().toString());
                newMissionPost.setStartDate(startDate);
                newMissionPost.setStartTime(startTime);
                newMissionPost.setEndDate(endDate);
                newMissionPost.setEndTime(endTime);
                newMissionPost.setUid(uid);
                newMissionPost.setCreateDate(currentDate);
                newMissionPost.setCreateTime(currentTime);

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

    public void dateSelect(DatePickerDialog.OnDateSetListener dateListener){
        DatePickerDialog dialog = new DatePickerDialog(
                CreateMissionActivity.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                dateListener,
                year,month,day);
        dialog.show();
    }

    public void timeSelect(TimePickerDialog.OnTimeSetListener timeListener){
        TimePickerDialog dialog = new TimePickerDialog(
                CreateMissionActivity.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                timeListener,
                rhour,rmin,true);
        dialog.show();
    }
}
