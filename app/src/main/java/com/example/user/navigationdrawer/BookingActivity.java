package com.example.user.navigationdrawer;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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


import java.util.Calendar;
import java.util.HashMap;

public class BookingActivity extends AppCompatActivity {

    private DatabaseReference mUser;
    private DatabaseReference mTime1;
    private DatabaseReference mTime2;
    private DatabaseReference mTime3;
    private DatabaseReference mCount1;
    private DatabaseReference mCount2;
    private DatabaseReference mCount3;
    private static final String TAG = "BookingActivity";
    public String lmt;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking);

        FirebaseAuth mAuth = FirebaseAuth.getInstance();

        FirebaseUser fu = mAuth.getCurrentUser();
        final User newUser = new User(fu);

        mUser = FirebaseDatabase.getInstance().getReference().child("Users");
        mTime1 = FirebaseDatabase.getInstance().getReference().child("3:30");
        mTime2 = FirebaseDatabase.getInstance().getReference().child("4:30");
        mTime3 = FirebaseDatabase.getInstance().getReference().child("5:30");
        mCount1 = FirebaseDatabase.getInstance().getReference().child("Count@3:30");
        mCount2 = FirebaseDatabase.getInstance().getReference().child("Count@4:30");
        mCount3 = FirebaseDatabase.getInstance().getReference().child("Count@5:30");

        findViewById(R.id.button1).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Update(mTime1,mCount1,newUser,"3:30");
            }
        });

        findViewById(R.id.button2).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Update(mTime2,mCount2,newUser,"4:30");
            }
        });

        findViewById(R.id.button3).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Update(mTime3,mCount3,newUser,"5:30");
            }
        });

    }


    public void Book(DatabaseReference mDatabase,User user,String s) {

        HashMap<String,String>datamap = new HashMap<>();


        if(user!=null) {
            datamap.put("Name", user.getUserName());
            datamap.put("Email", user.getUserEmail());
            datamap.put("Limit", user.setLimit("1"));
            datamap.put("Time",s);
        }

        mDatabase.push().setValue(datamap);
        mUser.child(user.getUid()).setValue(datamap);

    }

    public void Update(final DatabaseReference mDatabase, DatabaseReference mref, final User user, final String s) {

        Calendar cal= Calendar.getInstance();
        final Long currentHour = cal.getTimeInMillis();
        int start = 8;
        int end = 22;

        cal.set(Calendar.HOUR_OF_DAY, start);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        final Long startHour = cal.getTimeInMillis();

        cal.set(Calendar.HOUR_OF_DAY, end);
        cal.set(Calendar.MINUTE, 0);
        cal.set(Calendar.SECOND, 0);
        cal.set(Calendar.MILLISECOND, 0);

        final long endHour = cal.getTimeInMillis();
        getlimit(user);

        mref.runTransaction(new Transaction.Handler() {
            @NonNull
            @Override
            public Transaction.Result doTransaction(@NonNull MutableData mutableData) {

                Integer CurrentValue = mutableData.getValue(Integer.class);
                if(CurrentValue==null) {
                    return Transaction.success(mutableData);
                }
                else if(currentHour < startHour || currentHour > endHour) {

                    runOnUiThread(new Runnable() {
                        public void run() {
                            final Toast toast = Toast.makeText(BookingActivity.this,"Not yet time!",Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });

                }
                else if(CurrentValue<20  && lmt.equals("0")){
                    mutableData.setValue(CurrentValue + 1);
                    Book(mDatabase,user,s);
                    runOnUiThread(new Runnable() {
                        public void run() {
                            final Toast toast = Toast.makeText(BookingActivity.this,"Booked Successfully",Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
                    startActivity(new Intent(BookingActivity.this,PostBookActivity.class));
                }
                else{
                    runOnUiThread(new Runnable() {
                        public void run() {
                            final Toast toast = Toast.makeText(BookingActivity.this,"Maximum Limit Reached",Toast.LENGTH_SHORT);
                            toast.show();
                        }
                    });
                }
                return Transaction.success(mutableData);

            }

            @Override
            public void onComplete(@Nullable DatabaseError databaseError, boolean b, @Nullable DataSnapshot dataSnapshot) {

                Log.d(TAG, "Updating likes count transaction is completed");

            }
        });
    }

    public void getlimit(User user) {

        DatabaseReference mUser = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("Limit");

        mUser.addValueEventListener((new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String limit = dataSnapshot.getValue(String.class);
                lmt=limit;

            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        }));
    }

}

