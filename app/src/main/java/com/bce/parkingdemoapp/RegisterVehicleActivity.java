package com.bce.parkingdemoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class RegisterVehicleActivity extends AppCompatActivity {

    EditText name,vehicleNo,phoneNo;
    Button register;
    FirebaseDatabase database = FirebaseDatabase.getInstance();
    DatabaseReference reference = database.getReference().child("users");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register_vehicle);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        name = findViewById(R.id.fullnameEditText);
        vehicleNo = findViewById(R.id.registerVehicleNoEditText);
        phoneNo = findViewById(R.id.phoneNoEditText);
        register = findViewById(R.id.registerButton);



        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String userName = name.getText().toString();
                String userVehicleNo = vehicleNo.getText().toString();
                String userPhoneNo = phoneNo.getText().toString();

                User user = new User(userName,userVehicleNo,userPhoneNo);

                reference.child(userVehicleNo).setValue(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()){
                            Toast.makeText(RegisterVehicleActivity.this, "Vehicle Added Successfully", Toast.LENGTH_SHORT).show();
                            finish();
                        } else {
                            Toast.makeText(RegisterVehicleActivity.this, "Error! Can't Register Now.", Toast.LENGTH_SHORT).show();
                            finish();
                        }
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