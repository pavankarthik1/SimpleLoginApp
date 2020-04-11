package com.example.myapplication;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class ActivityRegistration extends AppCompatActivity {
    private EditText userName,userPassword,UserEmail,userAge;
    private Button regButton;
    private TextView userLogin;
    private FirebaseAuth firebaseAuth;
    private ImageView userProfilepic;
    String name,password,email,age;
    private LocationManager locationManager;
    private LocationListener locationListener;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_registration);
        setupUIViews();
        firebaseAuth=FirebaseAuth.getInstance();
        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(validate()){
                    //Data ni database lo upload cheyali
                    String user_email=UserEmail.getText().toString();
                    String user_password=userPassword.getText().toString();
                    firebaseAuth.createUserWithEmailAndPassword(user_email,user_password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
                        @Override
                        public void onComplete(@NonNull Task<AuthResult> task) {
                            if(task.isSuccessful()) {
                                //sendEmailVerifiaction();
                                sendUserData();
                                Toast.makeText(ActivityRegistration.this,"Uploaded Completly",Toast.LENGTH_SHORT).show();
                                firebaseAuth.signOut();
                                finish();
                                startActivity(new Intent(ActivityRegistration.this,MainActivity.class));
                            }
                            else
                            {
                                Toast.makeText(ActivityRegistration.this,"Registration Failed",Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
        userLogin.setOnClickListener(new View.OnClickListener()
        {
            @Override
                    public void onClick(View view)
            {
                startActivity(new Intent(ActivityRegistration.this,MainActivity.class));
            }
        });
    }
    private void setupUIViews()
    {
        userName=(EditText)findViewById(R.id.etUserName);
        userPassword=(EditText)findViewById((R.id.etUserPassword));
        UserEmail=(EditText)findViewById((R.id.etUserEmail));
        regButton=(Button)findViewById((R.id.btnRegister));
        userLogin=(TextView)findViewById((R.id.tvUserLogin));
        userAge=(EditText)findViewById(R.id.etAge);
        userProfilepic=(ImageView)findViewById((R.id.ivProfile));

    }
    private Boolean validate()
    {
        Boolean result=false;
         name=userName.getText().toString();
         password=userPassword.getText().toString();
         email=UserEmail.getText().toString();
         age=userAge.getText().toString();
        if(name.isEmpty() || password.isEmpty() || email.isEmpty() || age.isEmpty())
            Toast.makeText(this, "Please Enter All details", Toast.LENGTH_SHORT).show();
        else
            result=true;
        return result;
    }
    private void sendEmailVerifiaction()
    {
        FirebaseUser firebaseUser=FirebaseAuth.getInstance().getCurrentUser();
        if(firebaseUser!=null)
        {
            firebaseUser.sendEmailVerification().addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    if(task.isSuccessful())
                    {
                        sendUserData();
                        Toast.makeText(ActivityRegistration.this,"Uploaded Completly",Toast.LENGTH_SHORT).show();
                        firebaseAuth.signOut();
                        finish();
                        startActivity(new Intent(ActivityRegistration.this,MainActivity.class));
                    }
                    else
                    {
                        Toast.makeText(ActivityRegistration.this, "Verification Mail not ben sent", Toast.LENGTH_SHORT).show();
                    }
                }
            });
        }
    }
    private void sendUserData()
    {
        FirebaseDatabase firebaseDatabase=FirebaseDatabase.getInstance();
        DatabaseReference myref=firebaseDatabase.getReference(firebaseAuth.getUid());
        UserProfile userProfile=new UserProfile(age,email,name);
        myref.setValue(userProfile);

    }

}