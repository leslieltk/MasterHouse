package com.uowfyp.masterhouse;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
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

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

public class PostDetailActivity extends AppCompatActivity {

    private static final String TAG = "PostDetailActivity";

    String postId = null;
    DatabaseReference postReff, userReff;
    TextView tvTitle, tvDesc;
    Button btnApply, btnlike;
    FirebaseAuth auth;
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd,HH:mm:ss");
    String currentTime = dateFormat.format(calendar.getTime());

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        postId = getIntent().getExtras().getString("key");  //get the post key from other activity

        auth = FirebaseAuth.getInstance();
        postReff = FirebaseDatabase.getInstance().getReference("Posts").child(postId);
        userReff = FirebaseDatabase.getInstance().getReference("Users").child(auth.getCurrentUser().getUid());

        tvTitle = (TextView)findViewById(R.id.postTitle);
        tvDesc = (TextView)findViewById(R.id.tvDesc);
        btnApply = (Button)findViewById(R.id.postApply);
        btnlike = (Button)findViewById(R.id.btnlike);

        postReff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                postDerailDisplay(dataSnapshot);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        userReff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                checkUserLiked(dataSnapshot);
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnlike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                likeClicked();
            }
        });

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                applyClicked();
            }
        });
    };


    private void likeClicked() {
        if (btnlike.getText().equals("notlike")) {
            userReff.child("likes").child(postId).setValue(true);
            postReff.child("likes").child(auth.getCurrentUser().getUid()).setValue(true);
            btnlike.setBackgroundResource(R.drawable.ic_favorite_red_600_24dp);
            btnlike.setText("likes");
            Toast.makeText(PostDetailActivity.this, "Like", Toast.LENGTH_SHORT).show();
        } else if (btnlike.getText().equals("like")) {
            userReff.child("likes").child(postId).removeValue();
            postReff.child("likes").child(auth.getCurrentUser().getUid()).removeValue();
            btnlike.setBackgroundResource(R.drawable.ic_favorite_border_red_600_24dp);
            btnlike.setText("notlike");
            Toast.makeText(PostDetailActivity.this, "Unlike", Toast.LENGTH_SHORT).show();
        }
    }

    private void checkUserLiked(DataSnapshot dataSnapshot){
        if(dataSnapshot.exists()){
            if (dataSnapshot.child("likes").hasChild(postId)) {
                btnlike.setBackgroundResource(R.drawable.ic_favorite_red_600_24dp);
                btnlike.setText("like");
            }
        }
    }

    private void applyClicked(){
        postReff.child("applicant").child(auth.getCurrentUser().getUid()).setValue(currentTime);
        userReff.child("applied").child(postId).setValue(currentTime);
        Toast.makeText(PostDetailActivity.this, "Apply a Mission", Toast.LENGTH_SHORT).show();
    }


    private void postDerailDisplay(DataSnapshot dataSnapshot){
        tvTitle.setText(dataSnapshot.child("title").getValue().toString());
        tvDesc.setText(dataSnapshot.child("description").getValue().toString());
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        finish();
    }
}
