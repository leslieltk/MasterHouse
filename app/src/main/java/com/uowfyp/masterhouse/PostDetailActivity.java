package com.uowfyp.masterhouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PostDetailActivity extends AppCompatActivity {

    private static final String TAG = "PostDetailActivity";

    String postKey = null;
    DatabaseReference postReff, userReff;
    TextView tvTitle, tvDesc, tvcategory, tvlocation, tvsalary, tvdate;
    Button btnApply, btnlike, btnPostSetting;
    FirebaseAuth auth;
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    String currentTime = dateFormat.format(calendar.getTime());
    String time, s;
    MissionPost missionPost = new MissionPost();
    Date d1, d2;
    boolean isbelong;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        postKey = getIntent().getExtras().getString("postKey");  //get the missionPost key from other activity

        auth = FirebaseAuth.getInstance();
        postReff = FirebaseDatabase.getInstance().getReference("Posts").child(postKey);
        userReff = FirebaseDatabase.getInstance().getReference("Users").child(auth.getCurrentUser().getUid());

        tvTitle = (TextView)findViewById(R.id.postTitle);
        tvDesc = (TextView)findViewById(R.id.tvDesc);
        btnApply = (Button)findViewById(R.id.postApply);
        btnlike = (Button)findViewById(R.id.btnlike);
        tvcategory = (TextView)findViewById(R.id.tvcategory);
        tvlocation = (TextView)findViewById(R.id.tvlocation);
        tvsalary = (TextView)findViewById(R.id.tvsalary);
        tvdate = (TextView)findViewById(R.id.tvdate);
        btnPostSetting = (Button)findViewById(R.id.btnPostSetting);

        userReff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                checkUserLiked(dataSnapshot);
                if (dataSnapshot.child("posts").hasChild(postKey)){
                    isbelong = true;

                }else if (dataSnapshot.child("applied").hasChild(postKey)){
                    btnApply.setText("Cancel");
                    btnApply.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            cancelApply();
                        }
                    });
                }else {
                    isbelong = false;
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });


        postReff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if (dataSnapshot.exists()){
                missionPost = dataSnapshot.getValue(MissionPost.class);
                try {
                    d1 = dateFormat.parse(dataSnapshot.child("date").getValue().toString());
                    d2 = dateFormat.parse(currentTime);
                    long diff = d1.getTime() - d2.getTime();
                    long diffSeconds = diff / 1000;
                    long diffMinutes = diff / (60 * 1000);
                    long diffHours = diff / (60 * 60 * 1000);
                    s = String.valueOf(diffHours);
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                postDerailDisplay(dataSnapshot);
                enableSetting(missionPost.getUid());
                countApplicant(dataSnapshot);
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnPostSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(PostDetailActivity.this, CreateMissionActivity.class);
                intent.putExtra("key", missionPost.getKey());
                startActivity(intent);
            }
        });

        btnlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeClicked();
            }
        });
    };


    private void likeClicked() {
        if (btnlike.getText().equals("notlike")) {
            userReff.child("likes").child(postKey).setValue(true);
            postReff.child("likes").child(auth.getCurrentUser().getUid()).setValue(true);
            btnlike.setBackgroundResource(R.drawable.ic_favorite_red_600_24dp);
            btnlike.setText("likes");
            Toast.makeText(PostDetailActivity.this, "Like", Toast.LENGTH_SHORT).show();
        } else if (btnlike.getText().equals("like")) {
            userReff.child("likes").child(postKey).removeValue();
            postReff.child("likes").child(auth.getCurrentUser().getUid()).removeValue();
            btnlike.setBackgroundResource(R.drawable.ic_favorite_border_red_600_24dp);
            btnlike.setText("notlike");
            Toast.makeText(PostDetailActivity.this, "Unlike", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkUserLiked(DataSnapshot dataSnapshot){
        if(dataSnapshot.exists()){
            if (dataSnapshot.child("likes").hasChild(postKey)) {
                btnlike.setBackgroundResource(R.drawable.ic_favorite_red_600_24dp);
                btnlike.setText("like");
            }

        }
    }

    private  void  countApplicant(DataSnapshot dataSnapshot){
        if (isbelong == true){
            long temp = dataSnapshot.child("applied").getChildrenCount();
            String text = String.valueOf(temp);
            btnApply.setText(text + " Applicant");
        }
    }

    private void applyClicked(){
        postReff.child("applicant").child(auth.getCurrentUser().getUid()).setValue(currentTime);
        userReff.child("applied").child(postKey).setValue(currentTime);
        Toast.makeText(PostDetailActivity.this, "Apply a Mission", Toast.LENGTH_SHORT).show();
    }

    private void cancelApply(){
        new AlertDialog.Builder(PostDetailActivity.this)
                .setTitle("Confirm!")
                .setMessage("Do you really want to Cancel this Mission Post?")
                .setIcon(android.R.drawable.ic_dialog_alert)
                .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                    public void onClick(DialogInterface dialog, int whichButton) {
                        postReff.child("applicant").child(auth.getCurrentUser().getUid()).removeValue();
                        userReff.child("applied").child(postKey).removeValue();

                    }})
                .setNegativeButton(android.R.string.no, null).show();

    }

    private void checkApply(){

    }


    private void postDerailDisplay(DataSnapshot dataSnapshot){
        tvTitle.setText(missionPost.getTitle());
        tvDesc.setText(dataSnapshot.child("description").getValue().toString());
        tvcategory.setText(missionPost.getCategory());
        tvlocation.setText(missionPost.getLocation());
        tvsalary.setText("$" + missionPost.getPrice());
        tvdate.setText(missionPost.getDate());

    }

    private void enableSetting(String postUid){
        if(postUid.equals(auth.getCurrentUser().getUid().toString())){
            btnPostSetting.setVisibility(View.VISIBLE);
            btnPostSetting.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(PostDetailActivity.this, EditMissionActivity.class);
                    intent.putExtra("postKey", postKey);
                    startActivity(intent);
                }
            });
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
