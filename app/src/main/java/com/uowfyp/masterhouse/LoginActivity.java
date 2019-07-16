package com.uowfyp.masterhouse;

import android.app.ProgressDialog;
import android.content.Intent;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class LoginActivity extends AppCompatActivity {

    Button btnRegister, btnLogin;
    EditText txtEmail, txtPwd;
    FirebaseAuth auth;
    ProgressDialog progressDialog;
    FirebaseAuth.AuthStateListener mAuthListener;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

//        getSupportActionBar().hide();

        btnRegister = (Button)findViewById(R.id.btnRegister);
        btnLogin = (Button)findViewById(R.id.btnLogin);
        txtEmail = (EditText)findViewById(R.id.etxtEmail);
        txtPwd = (EditText)findViewById(R.id.etxtPwd);

        progressDialog = new ProgressDialog(LoginActivity.this);

        auth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener(){
            @Override
            public  void  onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth){
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if(user!=null){
                    Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

        btnRegister.setOnClickListener(new View.OnClickListener() { // go to register page
            @Override
            public void onClick(View v) {
                startActivity(new Intent(LoginActivity.this, RegisterActivity.class));
            }
        });

        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String loginEmail = txtEmail.getText().toString();
                String loginPassword = txtPwd.getText().toString();

                if(validateEmail()&& validatePassword()){
                    progressDialog.setMessage("Logging in ...");
                    progressDialog.show();
                    auth.signInWithEmailAndPassword(loginEmail, loginPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            //loading.setVisibility(View.GONE);
                            if (task.isSuccessful()){
                                FirebaseUser user = auth.getCurrentUser();
                                startActivity(new Intent(LoginActivity.this, MainActivity.class));
                                finish();
                            }else {
                                txtEmail.setError(task.getException().getLocalizedMessage());
                                txtPwd.setError("");
                            }
                            progressDialog.dismiss();
                         }
                    });
                }

            }
        });

    }

    private boolean validateEmail(){
        String inputEmail = txtEmail.getText().toString();
        if(inputEmail.isEmpty() ){
            txtEmail.setError("Please Enter Email");
            return false;
        }else{
            txtEmail.setError(null);
            return true;
        }
    }
    private boolean validatePassword(){
        String inputPassword = txtPwd.getText().toString();
        if(inputPassword.isEmpty() ){
            txtPwd.setError("Please Enter Password");
            return false;
        }else if(inputPassword.length()<6){
            txtPwd.setError("Please Enter At Least 6 Character");
            return false;
        }else{
            txtPwd.setError(null);
            return true;
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
        auth.addAuthStateListener(mAuthListener);   // check user was logged or not
    }
}
