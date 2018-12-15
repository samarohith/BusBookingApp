package com.example.user.navigationdrawer;

import android.app.Fragment;
import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.Calendar;


public class FirstFragment extends Fragment {


    View myView;
    private DatabaseReference mUser;
    private DatabaseReference mDay;
    private DatabaseReference mMonth;
    private DatabaseReference mYear;
    private DatabaseReference mCount1;
    private DatabaseReference mCount2;

    FirebaseAuth mAuth;


    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        mAuth = FirebaseAuth.getInstance();

        mUser = FirebaseDatabase.getInstance().getReference().child("Users");
        mDay = FirebaseDatabase.getInstance().getReference().child("ThisDay");
        mMonth = FirebaseDatabase.getInstance().getReference().child("ThisMonth");
        mYear = FirebaseDatabase.getInstance().getReference().child("ThisYear");
        mCount1 = FirebaseDatabase.getInstance().getReference().child("Count@3:30");
        mCount2 = FirebaseDatabase.getInstance().getReference().child("Count@5:30");

    }



        @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.first_layout, container, false);

            FirebaseUser fu = mAuth.getCurrentUser();
            final User newUser = new User(fu);

        View.OnClickListener listnr = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), BookingActivity.class);
                startActivity(i);
                adjustTimeline(newUser);
            }
        };

        Button btn = myView.findViewById(R.id.nextButton);
        btn.setOnClickListener(listnr);

        return myView;
    }



    public void adjustDay(final User user) {

        Calendar cal = Calendar.getInstance();

        int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH);
        final String currdayOfMonth = String.valueOf(dayOfMonth);

        mDay.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String day = dataSnapshot.getValue().toString();
                if(!day.equals(currdayOfMonth)){

                    mDay.setValue(currdayOfMonth);
                    mCount1.setValue(0);
                    mCount2.setValue(0);
                    user.setLimit("0");
                    mUser.child(user.getUid()).child("Limit").setValue("0");
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(),"Failed to update Day",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void adjustMonth() {

        Calendar cal = Calendar.getInstance();

        int Month = cal.get(Calendar.MONTH)+1;
        final String currMonth = String.valueOf(Month);

        mMonth.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String month = dataSnapshot.getValue().toString();
                if(!month.equals(currMonth)){

                    mMonth.setValue(currMonth);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

                Toast.makeText(getActivity(),"Failed to update Month",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void adjustYear() {

        Calendar cal = Calendar.getInstance();

        int Year = cal.get(Calendar.YEAR);
        final String currYear = String.valueOf(Year);

        mYear.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                String year = dataSnapshot.getValue().toString();
                if(!year.equals(currYear)){

                    mYear.setValue(currYear);

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
                Toast.makeText(getActivity(),"Failed to update Year",Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void adjustTimeline(User user){

        adjustDay(user);
        adjustMonth();
        adjustYear();
    }



}



