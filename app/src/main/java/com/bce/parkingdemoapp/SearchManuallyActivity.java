package com.bce.parkingdemoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

public class SearchManuallyActivity extends AppCompatActivity {

    Button search,callOwner;
    EditText vehicleNo;
    TextView ownerInfo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_manually);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        search = findViewById(R.id.searchVehicleButton);
        vehicleNo = findViewById(R.id.vehicleNoEditText);
        ownerInfo = findViewById(R.id.ownerInfoTextView);
        callOwner = findViewById(R.id.callOwnerButton);


        search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final String vehicleNoUserEntered = vehicleNo.getText().toString();

                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference("users");

                Query checkUser = reference.orderByChild("vehicle").equalTo(vehicleNoUserEntered);

                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if(snapshot.exists()){
                            String vehicleNofromDB = snapshot.child(vehicleNoUserEntered).child("vehicle").getValue(String.class);
                            assert vehicleNofromDB != null;
                            if(vehicleNofromDB.equals(vehicleNoUserEntered)){
                                String name = snapshot.child(vehicleNoUserEntered).child("username").getValue(String.class);
                                final String phone = snapshot.child(vehicleNoUserEntered).child("phone").getValue(String.class);

                                ownerInfo.setText(name);
                                callOwner.setText("call : " + phone);
                                callOwner.setVisibility(View.VISIBLE);
                                callOwner.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent callIntent = new Intent(Intent.ACTION_DIAL,Uri.fromParts("tel",phone,null));
                                        startActivity(callIntent);
                                    }
                                });
                            } else {
                                Toast.makeText(SearchManuallyActivity.this, "No Vehicle Found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SearchManuallyActivity.this, "No Vehicle Found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }
        });
    }
    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}
