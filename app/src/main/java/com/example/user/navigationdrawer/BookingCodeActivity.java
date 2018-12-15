package com.example.user.navigationdrawer;

import android.annotation.TargetApi;
import android.content.Intent;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.MutableData;
import com.google.firebase.database.Transaction;
import com.google.firebase.database.ValueEventListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Calendar;
import java.util.Date;

public class BookingCodeActivity extends AppCompatActivity {

    private TextView mTextView;
    DatabaseReference mCount;
    String output;


    @TargetApi(Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_code);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        FirebaseUser fu = mAuth.getCurrentUser();
        final User newUser = new User(fu);

        mTextView = findViewById(R.id.textViewDay);

        mCount = FirebaseDatabase.getInstance().getReference().child("Code");

        DateFormat df =new SimpleDateFormat("dd/MM/yyyy");
        Date today=Calendar.getInstance().getTime();
        String reportdate=df.format(today);
       // LocalDate today = LocalDate.now( ZoneId.of( "Asia/Kolkata" ) ) ;
        //output = today.toString() ;
        mTextView.setText(reportdate);

        findViewById(R.id.buttonDone).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(BookingCodeActivity.this,"You have a ride to catch",Toast.LENGTH_SHORT).show();
            }
        });


              postTime(newUser);


    }

    public void postTime(User user) {

        DatabaseReference mUser = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("Time");

        mUser.addValueEventListener((new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String time = dataSnapshot.getValue(String.class);
                mTextView = findViewById(R.id.textViewTime);
                mTextView.setText(time);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }));
    }

}
