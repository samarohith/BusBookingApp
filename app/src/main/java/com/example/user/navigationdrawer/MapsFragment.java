package com.example.user.navigationdrawer;

import android.app.Fragment;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

public class MapsFragment extends Fragment {

    View myView;
    Button button;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        myView = inflater.inflate(R.layout.maps_layout, container, false);

        View.OnClickListener listnr = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               startActivity(new Intent(getActivity(),MapsActivity.class));
            }
        };

        button = myView.findViewById(R.id.buttonCheck);
        button.setOnClickListener(listnr);

        return myView;
    }
}
