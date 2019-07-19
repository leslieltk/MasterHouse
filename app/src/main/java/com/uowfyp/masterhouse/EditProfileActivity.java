package com.uowfyp.masterhouse;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class EditProfileActivity extends AppCompatActivity {

    FirebaseUser firebaseUser = FirebaseAuth.getInstance().getCurrentUser();
    DatabaseReference userreff;
    EditText etEmail, etPwd, etFirstname, etLastname, etPhone, etUsername;
    Spinner spinnerGender;
    Button btnNext, btnbirthday;
    TextView title;
    User user;
    String[] gender;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        Resources res = getResources();
        gender = res.getStringArray(R.array.gender);

        etFirstname = (EditText)findViewById(R.id.etFirstname);
        etLastname = (EditText)findViewById(R.id.etLastname);
        etPhone = (EditText)findViewById(R.id.etxtPhone);
        etEmail = (EditText)findViewById(R.id.etxtEmail2);
        etPwd = (EditText)findViewById(R.id.etPwd2);
        btnNext = (Button)findViewById(R.id.btnRegister);
        etUsername = (EditText)findViewById(R.id.etUsername);
        spinnerGender = (Spinner)findViewById(R.id.spinnerGender);
        btnbirthday = (Button)findViewById(R.id.btnbirthday);
        title = (TextView)findViewById(R.id.register_title);
        user = new User();

        title.setText("Profile Edited Page");
        btnNext.setText("SAVE");

        FirebaseUser authuser = FirebaseAuth.getInstance().getCurrentUser();
        if (authuser != null) {
            String uid = authuser.getUid();
            userreff = FirebaseDatabase.getInstance().getReference("Users").child(uid);

            userreff.addListenerForSingleValueEvent(new ValueEventListener() {
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    user = dataSnapshot.getValue(User.class);

                    etFirstname.setText(user.getFirstName());
                    etLastname.setText(user.getLastName());
                    etUsername.setText(user.getUsername());
                    for (int i = 0; i<gender.length; i++){
                        if(gender[i].equals(user.getGender())){
                            spinnerGender.setSelection(i);
                        }
                    }
                    btnbirthday.setText(user.getBirthday());
                    btnbirthday.setClickable(false);
                    etPhone.setText(user.getPhone());
                    etEmail.setText(user.getEmail());
                    etPwd.setVisibility(View.GONE);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });
        }
    }
}
