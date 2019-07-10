package com.uowfyp.masterhouse;

import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.EditText;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.uowfyp.masterhouse.R;

public class EditProfileActivity extends AppCompatActivity {
    FirebaseAuth auth;
    DatabaseReference userreff;
    EditText fname, lname;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);

        fname = (EditText)findViewById(R.id.editfName);
        lname = (EditText)findViewById(R.id.editlName);

        FirebaseUser authuser = FirebaseAuth.getInstance().getCurrentUser();
        if (authuser != null) {
            String uid = authuser.getUid();
            userreff = FirebaseDatabase.getInstance().getReference("Users").child(uid);

            userreff.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String firstName = dataSnapshot.child("firstName").getValue().toString();
                    String lastName = dataSnapshot.child("lastName").getValue().toString();

                    fname.setText(firstName);
                    lname.setText(lastName);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
