package com.example.user.navigationdrawer;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

/**
 * Created by user on 12/31/15.
 */
public class SecondFragment extends Fragment{

    View myView;
    public String lmt ;
    DatabaseReference mLimit;
    TextView txt;
    FirebaseAuth mAuth;
    FirebaseUser user;

    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.second_layout, container, false);

        View.OnClickListener listnr = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               // startActivity(new Intent(getActivity(), BookingCodeActivity.class));
                getlimit();
                StartIt();
            }
        };

        txt = myView.findViewById(R.id.textViewClick);
        txt.setOnClickListener(listnr);


        return myView;
    }

    public void StartIt() {

        if(lmt != null) {
            if (lmt.equals("1")) {
                startActivity(new Intent(getActivity(), BookingCodeActivity.class));

            } else {
                Toast.makeText(getActivity(), "No Booking History found", Toast.LENGTH_SHORT).show();
            }
        }
    }

    public void getlimit() {

        mAuth = FirebaseAuth.getInstance();
        user = mAuth.getCurrentUser();

        if(user!=null) {
            mLimit = FirebaseDatabase.getInstance().getReference().child("Users").child(user.getUid()).child("Limit");
            if(mLimit==null) {
                Toast.makeText(getActivity(), "No Booking History found", Toast.LENGTH_SHORT).show();

            }
            else {
                mLimit.addValueEventListener((new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {

                        lmt = dataSnapshot.getValue(String.class);

                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                }));
            }

        }
    }

}
