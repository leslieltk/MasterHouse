package com.uowfyp.masterhouse;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class ProfileActivity extends AppCompatActivity {

    TextView tvEmail, tvName, tvDate;
    Button btnlogout;
    ProgressBar loading;
    FirebaseAuth auth;
    FirebaseAuth.AuthStateListener authStateListener;
    DatabaseReference dbreff;
    User user;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile);

//        View rootView = inflater.inflate(R.layout.activity_profile, container, false);
//        auth = FirebaseAuth.getInstance();a
//        tvEmail = (TextView)rootView.findViewById(R.id.tvemail);
//        tvName = (TextView)rootView.findViewById(R.id.tvname);
//        btnlogout = (Button)rootView.findViewById(R.id.btnlogout);
//        loading = (ProgressBar)rootView.findViewById(R.id.loading2);
//        tvDate  = (TextView)rootView.findViewById(R.id.tvdate);

        auth = FirebaseAuth.getInstance();
        tvEmail = (TextView)findViewById(R.id.tvemail);
        tvName = (TextView)findViewById(R.id.tvname);
        btnlogout = (Button)findViewById(R.id.btnlogout);
        loading = (ProgressBar)findViewById(R.id.loading2);
        tvDate  = (TextView)findViewById(R.id.tvdate);
        user = new User();

        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        //textView.setText(getString(R.string.section_format, getArguments().getInt(ARG_SECTION_NUMBER)));

        FirebaseUser authuser = FirebaseAuth.getInstance().getCurrentUser();
        if (authuser != null){
            loading.setVisibility(View.VISIBLE);
            String email = authuser.getEmail();
            String uid = authuser.getUid();
            dbreff = FirebaseDatabase.getInstance().getReference("Users").child(uid);
            dbreff.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String firstName = dataSnapshot.child("firstName").getValue().toString();
                    String lastName = dataSnapshot.child("lastName").getValue().toString();
                    firstName = firstName.toUpperCase();
                    lastName = lastName.toUpperCase();
                    tvName.setText(firstName +" "+lastName);
                    tvName.setTextSize(20);
                    loading.setVisibility(View.GONE);

                }
                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
            boolean emailVerified = authuser.isEmailVerified();
            tvEmail.setText(email);

        }

        btnlogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                startActivity(new Intent(ProfileActivity.this, LoginActivity.class));
                finish();
            }
        });
    }
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    startActivity(new Intent(ProfileActivity.this, HomeActivity.class));
                    return true;
                case R.id.navigation_dashboard:
                    return true;
                case R.id.navigation_notifications:
                    return true;
            }
            return false;
        }
    };
}
