package com.uowfyp.masterhouse;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterActivity extends AppCompatActivity {

    EditText etxtEmail, etxtPwd, etxtFirstname, etxtLastname, etxtPhone;
    Button btnNext;
    ProgressBar loading;
    FirebaseAuth auth;
    //FirebaseAuth.AuthStateListener authStateListener;
    DatabaseReference dbReff;
    ProgressDialog progressDialog;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        etxtFirstname = (EditText)findViewById(R.id.etxtFirstname);
        etxtLastname = (EditText)findViewById(R.id.etxtLastname);
        etxtPhone = (EditText)findViewById(R.id.etxtPhone);
        etxtEmail = (EditText)findViewById(R.id.etxtEmail2);
        etxtPwd = (EditText)findViewById(R.id.etxtPwd2);
        btnNext = (Button)findViewById(R.id.btnNext);
        loading = (ProgressBar)findViewById(R.id.loading2);

        user = new User();

        auth = FirebaseAuth.getInstance();
        dbReff = FirebaseDatabase.getInstance().getReference();

        btnNext.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                if (vaildateEmail() && vaildatePassword() && validateFirstName() && validateLastName() && validatePhone()) {

                    final String email = etxtEmail.getText().toString().trim();
                    final String password = etxtPwd.getText().toString().trim();
                    final String firstName = etxtFirstname.getText().toString().trim();
                    final String lastName = etxtLastname.getText().toString().trim();
                    final String phoneNum = etxtPhone.getText().toString().trim();

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

                                FirebaseDatabase.getInstance().getReference("Users").child(auth.getCurrentUser().getUid()).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()){
                                            Toast.makeText(RegisterActivity.this, "Register Successfully!", Toast.LENGTH_LONG).show();
                                            FirebaseAuth.getInstance().signOut();
                                            startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
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
        String inputEmail = etxtEmail.getText().toString();
        if(inputEmail.isEmpty() ){
            etxtEmail.setError("Enter a Email");
            return false;
        }else{
            etxtEmail.setError(null);
            return true;
        }
    }
    private boolean vaildatePassword(){
        String inputPassword = etxtPwd.getText().toString();
        if(inputPassword.isEmpty() ){
            etxtPwd.setError("Please Enter Password");
            return false;
        }else if(inputPassword.length()<6){
            etxtPwd.setError("Enter at least 6 character for your password");
            return false;
        }else{
            etxtPwd.setError(null);
            return true;
        }
    }

    private boolean validateFirstName(){
        String inputFirstName = etxtFirstname.getText().toString();
        if (inputFirstName.isEmpty()){
            etxtFirstname.setError("Enter first name");
            return false;
        }else {
            etxtFirstname.setError(null);
            return true;
        }
    }

    private boolean validateLastName(){
        String inputLastName = etxtLastname.getText().toString();
        if (inputLastName.isEmpty()){
            etxtLastname.setError("Enter last name");
            return false;
        }else {
            etxtLastname.setError(null);
            return true;
        }
    }

    private boolean validatePhone(){
        String inputPhone = etxtPhone.getText().toString();
        if (inputPhone.isEmpty()){
            etxtPhone.setError("Enter phone number");
            return false;
        }else if(etxtPhone.length() != 8){
            etxtPhone.setError("Enter phone number with 8 number");
            return false;
        }else {
            etxtPhone.setError(null);
            return true;
        }
    }

    private void showProcessDialog() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Registering ...");
        progressDialog.show();
    }

}
