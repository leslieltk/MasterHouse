package com.uowfyp.masterhouse;

import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class PostDetailActivity extends AppCompatActivity {

    String postId = null;
    DatabaseReference dbReff;
    TextView tvTitle, tvDesc;
    ArrayList<Post> list;
    Button btnApply;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        postId = getIntent().getExtras().getString("key"); //get the post key from homeactivity

        dbReff = FirebaseDatabase.getInstance().getReference("Posts").child(postId);
        tvTitle = (TextView)findViewById(R.id.postTitle);
        tvDesc = (TextView)findViewById(R.id.tvDesc);
        btnApply = (Button)findViewById(R.id.postApply);

        dbReff.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                tvTitle.setText(dataSnapshot.child("title").getValue().toString());
                tvDesc.setText(dataSnapshot.child("description").getValue().toString());
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        btnApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(PostDetailActivity.this, "Apply a Mission", Toast.LENGTH_LONG).show();
            }
        });
    }
}
