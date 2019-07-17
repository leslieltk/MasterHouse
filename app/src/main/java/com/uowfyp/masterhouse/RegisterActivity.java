package com.uowfyp.masterhouse;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {

    EditText etxtEmail, etxtPwd, etxtFirstname, etxtLastname, etxtPhone, etUsername;
    Spinner spinnerGender;
    Button btnNext, btnbirthday;
    ProgressBar loading;
    FirebaseAuth auth;
    //FirebaseAuth.AuthStateListener authStateListener;
    DatabaseReference dbReff;
    ProgressDialog progressDialog;
    User user;
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    final String currentTime = dateFormat.format(calendar.getTime());
    DatePickerDialog.OnDateSetListener listener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etxtFirstname = (EditText)findViewById(R.id.etxtFirstname);
        etxtLastname = (EditText)findViewById(R.id.etxtLastname);
        etxtPhone = (EditText)findViewById(R.id.etxtPhone);
        etxtEmail = (EditText)findViewById(R.id.etxtEmail2);
        etxtPwd = (EditText)findViewById(R.id.etxtPwd2);
        btnNext = (Button)findViewById(R.id.btnRegister);
        loading = (ProgressBar)findViewById(R.id.loading2);
        etUsername = (EditText)findViewById(R.id.etUsername);
        spinnerGender = (Spinner)findViewById(R.id.spinnerGender);
        btnbirthday = (Button)findViewById(R.id.btnbirthday);

        btnbirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                int year = calendar.get(calendar.YEAR);
                int month = calendar.get(calendar.MONTH);
                int day = calendar.get(calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        RegisterActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        listener,
                        year,month,day);
                dialog.show();
            }
        });


        user = new User();

        auth = FirebaseAuth.getInstance();
        dbReff = FirebaseDatabase.getInstance().getReference();

        btnNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (validateFirstName() && validateLastName() && vaildateUsername()  && validatePhone() &&  vaildateEmail() && vaildatePassword() ) {

                    final String email = etxtEmail.getText().toString().trim();
                    final String password = etxtPwd.getText().toString().trim();
                    final String firstName = etxtFirstname.getText().toString().trim();
                    final String lastName = etxtLastname.getText().toString().trim();
                    final String phoneNum = etxtPhone.getText().toString().trim();
                    final String username = etUsername.getText().toString().trim();
                    final String gender = spinnerGender.getSelectedItem().toString();

                    auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                    // store the email and password to firebase
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {

                            if (task.isSuccessful()) {
                                showProcessDialog();
                                user.setEmail(email);
                                user.setFirstName(firstName);
                                user.setLastName(lastName);
                                user.setPhone(phoneNum);
                                user.setUsername(username);
                                user.setCreateDate(currentTime);
                                user.setGender(gender);

                                FirebaseDatabase.getInstance().getReference("Users").child(auth.getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(RegisterActivity.this, "Register Successfully!", Toast.LENGTH_LONG).show();
                                            FirebaseAuth.getInstance().signOut();
                                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                            finish();
                                        }else{

                                        }
                                    }
                                });

                            } else {
                                Toast.makeText(RegisterActivity.this, task.getException().getMessage(), Toast.LENGTH_LONG).show();
                            }
                        }
                    });
                }
            }
        });
    }

    private boolean vaildateEmail(){
        String inputEmail = etxtEmail.getText().toString().trim();
        if(inputEmail.isEmpty() ){
            etxtEmail.setError("Please input a Email");
            return false;
        }else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(inputEmail).matches()){
            etxtEmail.setError("Please input a valid Email");
            return false;
        }else{
            etxtEmail.setError(null);
            return true;
        }
    }
    private boolean vaildatePassword(){
        String inputPassword = etxtPwd.getText().toString().trim();
        if(inputPassword.isEmpty() ){
            etxtPwd.setError("Please Enter Password");
            return false;
        }else if(inputPassword.length()<6){
            etxtPwd.setError("Enter at least 6 character for your password");
            return false;
        }else {
            etxtPwd.setError(null);
            return true;
        }
    }

    private boolean validateFirstName(){
        String inputFirstName = etxtFirstname.getText().toString().trim();
        if (inputFirstName.isEmpty()){
            etxtFirstname.setError("Please input your first name");
            return false;
        }else {
            etxtFirstname.setError(null);
            return true;
        }
    }

    private boolean validateLastName(){
        String inputLastName = etxtLastname.getText().toString().trim();
        if (inputLastName.isEmpty()){
            etxtLastname.setError("Please input your last name");
            return false;
        }else {
            etxtLastname.setError(null);
            return true;
        }
    }

    private boolean validatePhone(){
        String inputPhone = etxtPhone.getText().toString().trim();
        if (inputPhone.isEmpty()){
            etxtPhone.setError("Please input phone number");
            return false;
        }else if(etxtPhone.length() != 8){
            etxtPhone.setError("Please input phone number within 8 number");
            return false;
        }else {
            etxtPhone.setError(null);
            return true;
        }
    }

    private boolean vaildateUsername(){
        String inputUser = etUsername.getText().toString().trim().trim();
        if (inputUser.length() < 3 || inputUser.isEmpty()){
            etUsername.setError("Please input at lease 3 character of Username");
            return false;
        }else {
            return true;
        }
    }

    private void showProcessDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering ...");
        progressDialog.show();
    }

}
