package com.uowfyp.masterhouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
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
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class EditMissionActivity extends AppCompatActivity {

    EditText etTitle, etDescription, etPrice;
    TextView tvTile;
    Spinner spinnerLocation, spinnerCategory, spinnerPriceType;
    Button btnUpdate, btnDelete, btnStartDate, btnStartTime, btnEndDate, btnEndTime;
    MissionPost missionPost;
    String postKey;
    DatabaseReference postReff;
    String[] categoryArr, locationArr, priceTypeArr;
    String title, desc, category, location, priceType, price, startDate, startTime, endDate, endTime;
    String tempStarDate, tempEndDate, tempStartTime, tempEndTime;
    int year, month, day,hour, min, rhour, rmin, syear, smonth, sday, checkhour, checkmin;
    Calendar calendar = Calendar.getInstance();
    DatePickerDialog.OnDateSetListener dateListener1, dateListener2;
    TimePickerDialog.OnTimeSetListener timeListener1, timeListener2;
    SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy");
    SimpleDateFormat timeFormat = new SimpleDateFormat("HH:mm");
    boolean checkTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_post);

        Resources res = getResources();
        categoryArr = res.getStringArray(R.array.category);
        locationArr = res.getStringArray(R.array.location);
        priceTypeArr = res.getStringArray(R.array.priceType);

        postKey = getIntent().getExtras().getString("postKey");

        etTitle = (EditText)findViewById(R.id.etTitle);
        etDescription = (EditText)findViewById(R.id.etDescription);
        spinnerLocation = (Spinner)findViewById(R.id.spinnerLocation);
        spinnerCategory = (Spinner)findViewById(R.id.spinnerCategory);
        spinnerPriceType = (Spinner)findViewById(R.id.spinnerPriceType);
        etPrice = (EditText)findViewById(R.id.etPrice);
        tvTile = (TextView)findViewById(R.id.tvTitle);
        btnStartDate = (Button)findViewById(R.id.btnStartDate);
        btnEndDate = (Button)findViewById(R.id.btnEndDate);
        btnStartTime = (Button)findViewById(R.id.btnStartTime);
        btnEndTime = (Button)findViewById(R.id.btnEndTime);

        tvTile.setText("Misiion Post Edited");

        btnDelete = (Button)findViewById(R.id.btnDelete);
        btnUpdate = (Button)findViewById(R.id.btnSumbit);
        btnUpdate.setText("Update");
        missionPost = new MissionPost();
        btnDelete.setVisibility(View.VISIBLE);


        postReff = FirebaseDatabase.getInstance().getReference("Posts").child(postKey);

        postReff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()) {
                    missionPost = dataSnapshot.getValue(MissionPost.class);
                    etTitle.setText(missionPost.getTitle());
                    etDescription.setText(missionPost.getDescription());

                    for (int i = 0; i < categoryArr.length; i++) {

                        if (categoryArr[i].equals(missionPost.getCategory())) {
                            spinnerCategory.setSelection(i);
                        }
                    }
                    for (int i = 0; i < locationArr.length; i++) {

                        if (locationArr[i].equals(missionPost.getLocation())) {
                            spinnerLocation.setSelection(i);
                        }
                    }
                    for (int i = 0; i < priceTypeArr.length; i++) {

                        if (priceTypeArr[i].equals(missionPost.getPriceType())) {
                            spinnerPriceType.setSelection(i);
                        }
                    }
                    etPrice.setText(missionPost.getPrice());

                }

                btnStartDate.setText(missionPost.getStartDate());
                btnStartTime.setText(missionPost.getStartTime());
                btnEndDate.setText(missionPost.getEndDate());
                btnEndTime.setText(missionPost.getEndTime());

                tempStarDate = missionPost.getStartDate();
                tempEndDate = missionPost.getEndDate();
                tempStartTime = missionPost.getStartTime();
                tempEndTime = missionPost.getEndTime();

                startDate = tempStarDate;
                startTime = tempStartTime;
                endDate = tempEndDate;
                endTime = tempEndTime;
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnStartDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                perDate(tempStarDate);
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
                perDate(tempEndDate);
                dateSelect(dateListener2);
            }
        });

        dateListener2 = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int year, int month, int day) {

                btnStartDate.setError(null);
                if (syear > year) {
                    btnEndDate.setError("");
                    Toast.makeText(EditMissionActivity.this, "Plesae input a valid date", Toast.LENGTH_SHORT).show();
                } else if (smonth > month) {
                    btnEndDate.setError("");
                    Toast.makeText(EditMissionActivity.this, "Plesae input a valid date", Toast.LENGTH_SHORT).show();
                } else if (sday > day) {
                    btnEndDate.setError("");
                    Toast.makeText(EditMissionActivity.this, "Plesae input a valid date", Toast.LENGTH_SHORT).show();
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
                perTime(tempStartTime);
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
                    smin ="" +min;
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
                perTime(tempEndTime);
                timeSelect(timeListener2);
            }
        });

        timeListener2 = new TimePickerDialog.OnTimeSetListener() {
            @Override
            public void onTimeSet(TimePicker timePicker, int hour, int min) {

                if (checkTime == true){
                    if (checkhour > hour){
                        btnEndTime.setError("");
                        Toast.makeText(EditMissionActivity.this, "Plesae input a valid Time", Toast.LENGTH_SHORT).show();
                    }else if (checkmin > min){
                        btnEndTime.setError("");
                        Toast.makeText(EditMissionActivity.this, "Plesae input a valid Time", Toast.LENGTH_SHORT).show();
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

        btnUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                boolean check = true;
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
                    postReff.child("title").setValue(title);
                    postReff.child("category").setValue(category);
                    postReff.child("description").setValue(desc);
                    postReff.child("location").setValue(location);
                    postReff.child("price").setValue(price);
                    postReff.child("priceType").setValue(priceType);
                    postReff.child("startDate").setValue(startDate);
                    postReff.child("startTime").setValue(startTime);
                    postReff.child("endDate").setValue(endDate);
                    postReff.child("endTime").setValue(endTime);

                    Intent intent = new Intent(EditMissionActivity.this, PostDetailActivity.class);
                    intent.putExtra("postKey", postKey);
                    startActivity(intent);
                    finish();
                }

            }
        });

        btnDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new AlertDialog.Builder(EditMissionActivity.this)
                        .setTitle("Confirm")
                        .setMessage("Do you really want to Deleter this Mission Post?")
                        .setIcon(android.R.drawable.ic_dialog_alert)
                        .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                            public void onClick(DialogInterface dialog, int whichButton) {
                                postReff.removeValue();
                                Intent intent1 = new Intent(EditMissionActivity.this, MainActivity.class);
                                startActivity(intent1);
                                finish();
                            }})
                        .setNegativeButton(android.R.string.no, null).show();

            }
        });
    }

    public void dateSelect(DatePickerDialog.OnDateSetListener dateListener){
        DatePickerDialog dialog = new DatePickerDialog(
                EditMissionActivity.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                dateListener,
                year,month,day);
        dialog.show();
    }

    public void timeSelect(TimePickerDialog.OnTimeSetListener timeListener){
        TimePickerDialog dialog = new TimePickerDialog(
                EditMissionActivity.this,
                android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                timeListener,
                rhour,rmin,true);
        dialog.show();
    }

    public void perDate(String date){
        try {
            Date d1 = dateFormat.parse(date);
            calendar.setTime(d1);
            year = calendar.get(Calendar.YEAR);
            month = calendar.get(Calendar.MONTH);
            day = calendar.get(Calendar.DAY_OF_MONTH);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }

    public void perTime(String time){
        try {
            Date d1 = timeFormat.parse(time);
            calendar.setTime(d1);
            rhour = calendar.get(Calendar.HOUR_OF_DAY);
            rmin = calendar.get(Calendar.MINUTE);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}
