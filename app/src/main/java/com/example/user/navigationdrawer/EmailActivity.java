package com.example.user.navigationdrawer;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class EmailActivity extends AppCompatActivity {

    public FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_email);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser user = mAuth.getCurrentUser();

        if(user!=null) {
            user.sendEmailVerification();
        }



        findViewById(R.id.DoneButton).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                StartActivity();
            }
        });

    }

    public void StartActivity() {

        final FirebaseUser user = mAuth.getCurrentUser();
        user.reload();


        if(user!=null) {
            if (user.isEmailVerified()) {
                finish();
                startActivity(new Intent(this, OneTimeActivity.class));
            }
            else {
                Toast.makeText(EmailActivity.this,"Check Your Email",Toast.LENGTH_SHORT).show();
            }
        }


    }
}
