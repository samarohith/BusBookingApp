package com.example.user.navigationdrawer;


import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.HashMap;

public class OneTimeActivity extends AppCompatActivity {

    EditText editText;
    String displayName;
    private DatabaseReference mUser;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);





       /* if (mSharedPreferences.getBoolean("isfirstTime", true)) {
            mEditor.putBoolean("isFirstTime",false);
            mEditor.apply();
        }else{
            startActivity(new Intent(this, MainActivity.class));
            overridePendingTransition(0, 0);
            finish();
        }*/

        setContentView(R.layout.activity_one_time);

        mAuth = FirebaseAuth.getInstance();
        editText = findViewById(R.id.editTextDisplayName);

        mUser = FirebaseDatabase.getInstance().getReference().child("Users");

        loadUserInformation();



        findViewById(R.id.buttonSave).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveUserInformation();
            }
        });

        findViewById(R.id.buttonNext).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String displayName = editText.getText().toString();
                if (displayName.isEmpty()) {
                    editText.setError("Name required");
                    editText.requestFocus();
                    return;
                }
                final FirebaseUser user = mAuth.getCurrentUser();
                if (user.getEmail().equals("samarohith5@gmail.com")) {
                    finish();
                    startActivity(new Intent(getApplicationContext(), NewActivity.class));
                } else {
                    finish();
                    startActivity(new Intent(getApplicationContext(), MainActivity.class));
                }
            }

        });

    }

   /* private boolean first_time_check() {

        SharedPreferences mSharedPreferences = getSharedPreferences("yourPrefsFileName", Context.MODE_PRIVATE);
        SharedPreferences.Editor mEditor = mSharedPreferences.edit();
        String first=mSharedPreferences.getString("first",null);
        if(first!=null){
            Intent i= new Intent(this,MainActivity.class);
            startActivity(i);
            }
            SharedPreferences.Editor editor=mSharedPreferences.edit();
            editor.putString("first","loggedin");
            editor.commit();
            return false;
    }
*/

    private void loadUserInformation() {
        final FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {


            if (user.getDisplayName() != null) {
                editText.setText(user.getDisplayName());
            }
        }
    }


    private void saveUserInformation() {

         displayName = editText.getText().toString();

        if (displayName.isEmpty()) {
            editText.setError("Name required");
            editText.requestFocus();
            return;
        }

        FirebaseUser user = mAuth.getCurrentUser();

        if (user != null) {

            final User newUser = new User(user);
            UserProfileChangeRequest profile = new UserProfileChangeRequest.Builder()
                    .setDisplayName(displayName)
                    .build();
            Log.d("Rohith","DisplayName:" + displayName);

            newUser.setLimit("0");

            HashMap<String, String> datamap = new HashMap<>();

            datamap.put("Name", newUser.getUserName());
            datamap.put("Email", newUser.getUserEmail());
            datamap.put("Limit", newUser.getLimit());

            String uid = user.getUid();
            mUser.child(uid).setValue(datamap);

            user.updateProfile(profile)
                    .addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if (task.isSuccessful()) {
                                Toast.makeText(OneTimeActivity.this, "Profile Updated", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
        }

    }
}

