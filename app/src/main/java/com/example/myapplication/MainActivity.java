package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class MainActivity extends AppCompatActivity {
    private EditText Name;
    private EditText Password;
    private Button Login;
    private int counter=5;
    private Button userRegistration;
    private FirebaseAuth firebaseAuth;
    private ProgressDialog progressDialog;
    private TextView forgotPassword;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Name=(EditText)findViewById(R.id.etName);
        Password=(EditText)findViewById(R.id.etPassword);
        Login=(Button)findViewById(R.id.btnLogin);
        userRegistration=(Button)findViewById(R.id.btnRegister);
        forgotPassword=(TextView)findViewById(R.id.tvForgotPassword);
        firebaseAuth=FirebaseAuth.getInstance();
        progressDialog=new ProgressDialog(this);
        FirebaseUser user= firebaseAuth.getCurrentUser();

        if(user != null)
        {
            finish();
            startActivity(new Intent(MainActivity.this,SecondActivity.class));
        }
        Login.setOnClickListener(new View.OnClickListener()
        {
            @Override
                    public void onClick(View view)
            {
                validate(Name.getText().toString(),Password.getText().toString());
            }

        });


        userRegistration.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view)
            {
                startActivity(new Intent(MainActivity.this,ActivityRegistration.class));
            }
        });
        forgotPassword.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this,PasswordActivity.class));
            }
        });
    }
    private void validate(String userName, String userPassword)
    {
        progressDialog.setMessage("Please wait a mooment while we load");
        progressDialog.show();
            firebaseAuth.signInWithEmailAndPassword(userName,userPassword).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                @Override
                public void onComplete(@NonNull Task<AuthResult> task) {
                        if(task.isSuccessful())
                        {
                            progressDialog.dismiss();
                            checkEmailverification();
                            //Toast.makeText(MainActivity.this,"Login Successfull",Toast.LENGTH_SHORT).show();
                        }
                        else
                        {
                            progressDialog.dismiss();
                            Toast.makeText(MainActivity.this,"Login Failed",Toast.LENGTH_SHORT).show();

                        }
                }
            });
    }
    private void checkEmailverification()
    {
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        Boolean emailflag=firebaseUser.isEmailVerified();
        startActivity(new Intent(MainActivity.this,SecondActivity.class));
        /*if(emailflag)
        {
            finish();
            startActivity(new Intent(MainActivity.this,SecondActivity.class));
        }
        else
        {
            Toast.makeText(this,"Verify Your Email",Toast.LENGTH_SHORT).show();
            firebaseAuth.signOut();
        }*/
    }

}
