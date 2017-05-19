package com.example.jaumenartbaron.politoproject;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends Activity implements View.OnClickListener {

    private Button buttonSignIn;

    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignup;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);


        //Initialize firebaseAuth object
        firebaseAuth = FirebaseAuth.getInstance();

        //user is already logged in
        if(firebaseAuth.getCurrentUser()!=null)
        {
            //profile activity here
            finish();
            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));

        }

        progressDialog = new ProgressDialog(this);

        buttonSignIn = (Button) findViewById(R.id.buttonSignIn);

        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        textViewSignup = (TextView) findViewById(R.id.textViewSignUp);

        buttonSignIn.setOnClickListener(this);
        textViewSignup.setOnClickListener(this);
    }

    private void userLogin()
    {

        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();


        if(TextUtils.isEmpty(email))
        {
            //email is empty
            Toast.makeText(this, "Please enter email", Toast.LENGTH_SHORT).show();
            //stopping the function execution further
            return;
        }
        if(TextUtils.isEmpty(password))
        {
            //password is empty
            Toast.makeText(this, "Please enter password", Toast.LENGTH_SHORT).show();
            //stopping the function execution further
            return;
        }
        //if validation is
        //we will register user
        progressDialog.setMessage("Registering user...");
        progressDialog.show();

        firebaseAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {

                        if(task.isSuccessful())
                        {
                            progressDialog.dismiss();
                            finish();
                            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                        }
                        else
                        {
                            progressDialog.dismiss();
                            //could not register
                            Toast.makeText(LoginActivity.this, "Incorrect email or password, please try again", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }

    @Override
    public void onClick(View v) {
        if(v==buttonSignIn)
        {
            userLogin();
        }
        if(v==textViewSignup)
        {
            finish();//close this activity
            startActivity(new Intent(this,MainActivity.class));//go bac to main activity to sign up
        }
    }
}
