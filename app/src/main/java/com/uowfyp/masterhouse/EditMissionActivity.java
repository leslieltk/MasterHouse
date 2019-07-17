package com.uowfyp.masterhouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditMissionActivity extends AppCompatActivity {

    EditText etTitle, etDescription, etPrice;
    TextView tvTile;
    Spinner spinnerLocation, spinnerCategory, spinnerPriceType;
    Button btnUpdate, btnDelete;
    MissionPost missionPost;
    String postKey;
    DatabaseReference postReff;
    String[] categoryArr, locationArr, priceTypeArr;
    String title, desc, category, location, priceType, price;
    int c;

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
                if (dataSnapshot.exists()){
                missionPost = dataSnapshot.getValue(MissionPost.class);
                etTitle.setText(missionPost.getTitle());
                etDescription.setText(missionPost.getDescription());

                for(int i = 0; i < categoryArr.length ; i++){

                    if(categoryArr[i].equals(missionPost.getCategory())){
                        spinnerCategory.setSelection(i);
                    }
                }
                for(int i = 0; i < locationArr.length ; i++){

                    if(locationArr[i].equals(missionPost.getLocation())){
                        spinnerLocation.setSelection(i);
                    }
                }
                for(int i = 0; i < priceTypeArr.length ; i++){

                    if(priceTypeArr[i].equals(missionPost.getPriceType())){
                        spinnerPriceType.setSelection(i);
                    }
                }

                etPrice.setText(missionPost.getPrice());

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

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
                    postReff.child("pricrType").setValue(priceType);

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
}
