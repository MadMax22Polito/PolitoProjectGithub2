package com.example.jaumenartbaron.politoproject;

// user: jaume@gmail.com password:asdqwe

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class MainActivity extends Activity implements View.OnClickListener {


    private Button buttonRegister;
    private EditText editTextUserName;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private TextView textViewSignin;

    private ProgressDialog progressDialog;

    private FirebaseAuth firebaseAuth;

    FirebaseDatabase firebaseInstance = FirebaseDatabase.getInstance();
    private DatabaseReference userref = firebaseInstance.getReference("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

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

        buttonRegister = (Button) findViewById(R.id.button3);
        editTextUserName = (EditText) findViewById(R.id.editTextUserName);
        editTextEmail = (EditText) findViewById(R.id.editTextEmail);
        editTextPassword = (EditText) findViewById(R.id.editTextPassword);
        textViewSignin = (TextView) findViewById(R.id.textViewSigin);

        buttonRegister.setOnClickListener(this);
        textViewSignin.setOnClickListener(this);


    }

    private void registerUser()
    {
        final String name = editTextUserName.getText().toString().trim();
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();

        if(TextUtils.isEmpty(name))
        {
            //password is empty
            Toast.makeText(this, "Please enter user name", Toast.LENGTH_SHORT).show();
            //stopping the function execution further
            return;
        }
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
        progressDialog.setMessage("Creating user...");
        progressDialog.show();

        //Creates user in firebase console, attach listener to track the completion of the registration
        firebaseAuth.createUserWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                       if(task.isSuccessful())
                       {
                           progressDialog.dismiss();
                           //user is registered successfully and logged in
                           Toast.makeText(MainActivity.this, "Registered successfully", Toast.LENGTH_SHORT).show();

                               String userkey=userref.push().getKey();
                               userref.child(userkey).child("email").setValue(email);
                               userref.child(userkey).child("name").setValue(name);

                               //profile activity here
                               finish();
                               startActivity(new Intent(getApplicationContext(),ProfileActivity.class));

                       }
                       else
                       {
                           progressDialog.dismiss();
                           Log.i("errr",task.toString());
                           //could not register
                           Toast.makeText(MainActivity.this, "Could not register", Toast.LENGTH_SHORT).show();
                       }
                    }
                });
    }

    @Override
    public void onClick(View view)
    {
        if(view == buttonRegister)
        {
            registerUser();
        }
        if(view == textViewSignin)
        {
            //will open login activity
            startActivity(new Intent(this, LoginActivity.class));
        }

    }
}
