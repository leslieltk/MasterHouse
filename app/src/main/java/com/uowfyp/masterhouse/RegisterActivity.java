package com.uowfyp.masterhouse;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.content.res.Resources;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class RegisterActivity extends AppCompatActivity {

    EditText etEmail, etPwd, etFirstname, etLastname, etPhone, etUsername;
    Spinner spinnerGender;
    Button btnNext, btnbirthday;
    ProgressBar loading;
    FirebaseAuth auth;
    DatabaseReference dbReff;
    ProgressDialog progressDialog;
    User user;
    Calendar calendar = Calendar.getInstance();
    SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
    final String currentTime = dateFormat.format(calendar.getTime());
    DatePickerDialog.OnDateSetListener listener;
    String date;
    int year, month;
    TextView tvBirthday;
    boolean checkYear;
    Resources res;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etFirstname = (EditText)findViewById(R.id.etFirstname);
        etLastname = (EditText)findViewById(R.id.etLastname);
        etPhone = (EditText)findViewById(R.id.etxtPhone);
        etEmail = (EditText)findViewById(R.id.etxtEmail2);
        etPwd = (EditText)findViewById(R.id.etxtPwd2);
        btnNext = (Button)findViewById(R.id.btnRegister);
        loading = (ProgressBar)findViewById(R.id.loading2);
        etUsername = (EditText)findViewById(R.id.etUsername);
        spinnerGender = (Spinner)findViewById(R.id.spinnerGender);
        btnbirthday = (Button)findViewById(R.id.btnbirthday);
        tvBirthday = (TextView)findViewById(R.id.tvBirthday);
        res = getResources();


        btnbirthday.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                year = calendar.get(calendar.YEAR);
                month = calendar.get(calendar.MONTH);
                int day = calendar.get(calendar.DAY_OF_MONTH);

                DatePickerDialog dialog = new DatePickerDialog(
                        RegisterActivity.this,
                        android.R.style.Theme_Holo_Light_Dialog_MinWidth,
                        listener,
                        year,month,day);
                dialog.show();
            }
        });

        listener = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int birthyear, int birthmonth, int day) {
                 date = day + "/" + month + "/" + birthyear;
                 btnbirthday.setText(date);
                 if (year - birthyear < 16){
                    btnbirthday.setError("");
                    checkYear = false;
                    Toast.makeText(RegisterActivity.this, "Your need over 16 year old before to use the application", Toast.LENGTH_SHORT).show();
                 }else {
                     btnbirthday.setError(null);
                     checkYear = true;
                 }
            }
        };


        user = new User();

        auth = FirebaseAuth.getInstance();
        dbReff = FirebaseDatabase.getInstance().getReference();




        btnNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (date == null){
                    btnbirthday.setError("Please input your Birthday");
                    Toast.makeText(RegisterActivity.this, "Please input your Birthday", Toast.LENGTH_SHORT).show();
                }
                if (validateFirstName() && validateLastName() && vaildateUsername()  && validatePhone() &&  vaildateEmail() && vaildatePassword() && checkYear) {
                    final String email = etEmail.getText().toString().trim();
                    final String password = etPwd.getText().toString().trim();
                    final String firstName = etFirstname.getText().toString().trim();
                    final String lastName = etLastname.getText().toString().trim();
                    final String phoneNum = etPhone.getText().toString().trim();
                    final String username = etUsername.getText().toString().trim();
                    final String gender = spinnerGender.getSelectedItem().toString();
                    final String birhday = date;

                    new AlertDialog.Builder(RegisterActivity.this)
                            .setTitle("Confirm Your Information")
                            .setMessage("Full Name: " + firstName +" " + lastName +"\n\n"
                                    + "Username: " + username +"\n\n"
                                    + "Gender: " + gender +"\n\n"
                                    + "Birthday: " + birhday +"\n\n"
                                    + "Phone Number: " + phoneNum +"\n\n"
                                    + "Email Address: " + email +"\n\n"

                            )
                            .setIcon(res.getDrawable(R.drawable.ic_error_outline))
                            .setPositiveButton(android.R.string.yes, new DialogInterface.OnClickListener() {

                                public void onClick(DialogInterface dialog, int whichButton) {
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
                                                user.setBirhtdate(birhday);

                                                FirebaseDatabase.getInstance().getReference("Users").child(auth.getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                                    @Override
                                                    public void onComplete(@NonNull Task<Void> task) {
                                                        if (task.isSuccessful()){
                                                            Toast.makeText(RegisterActivity.this, "Register Successfully!", Toast.LENGTH_LONG).show();
                                                            FirebaseUser user = auth.getCurrentUser();
                                                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                                    .setDisplayName(username)
                                                                    .build();
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
                                }})
                            .setNegativeButton(android.R.string.no, null).show();

                }
            }
        });
    }

    private boolean vaildateEmail(){
        String inputEmail = etEmail.getText().toString().trim();
        if(inputEmail.isEmpty() ){
            etEmail.setError("Please input a Email");
            return false;
        }else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(inputEmail).matches()){
            etEmail.setError("Please input a valid Email");
            return false;
        }else{
            etEmail.setError(null);
            return true;
        }
    }
    private boolean vaildatePassword(){
        String inputPassword = etPwd.getText().toString().trim();
        if(inputPassword.isEmpty() ){
            etPwd.setError("Please Enter Password");
            return false;
        }else if(inputPassword.length()<6){
            etPwd.setError("Enter at least 6 character for your password");
            return false;
        }else {
            etPwd.setError(null);
            return true;
        }
    }

    private boolean validateFirstName(){
        String inputFirstName = etFirstname.getText().toString().trim();
        if (inputFirstName.isEmpty()){
            etFirstname.setError("Please input your first name");
            return false;
        }else {
            etFirstname.setError(null);
            return true;
        }
    }

    private boolean validateLastName(){
        String inputLastName = etLastname.getText().toString().trim();
        if (inputLastName.isEmpty()){
            etLastname.setError("Please input your last name");
            return false;
        }else {
            etLastname.setError(null);
            return true;
        }
    }

    private boolean validatePhone(){
        String inputPhone = etPhone.getText().toString().trim();
        if (inputPhone.isEmpty()){
            etPhone.setError("Please input phone number");
            return false;
        }else if(etPhone.length() != 8){
            etPhone.setError("Please input phone number within 8 number");
            return false;
        }else {
            etPhone.setError(null);
            return true;
        }
    }

    private boolean vaildateUsername(){
        String inputUser = etUsername.getText().toString().trim();
        if (inputUser.length() < 4 || inputUser.isEmpty()){
            etUsername.setError("Please input at lease 4 character of Username");
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
