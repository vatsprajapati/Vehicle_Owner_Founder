package com.bce.parkingdemoapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.coordinatorlayout.widget.CoordinatorLayout;

import android.app.ActionBar;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.snackbar.Snackbar;

import java.util.Objects;

public class MainActivity extends AppCompatActivity {

    Button searchManually;
    Button searchUsingCamera;
    FloatingActionButton addUser;
    ConstraintLayout layout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        searchManually = findViewById(R.id.searchManuallyButton);
        searchUsingCamera = findViewById(R.id.searchUsingCamera);
        addUser = findViewById(R.id.floatingActionButtonAddUser);
        layout = findViewById(R.id.layoutMain);

        searchUsingCamera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SearchUsingCameraActivity.class);
                startActivity(intent);
            }
        });
        searchManually.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, SearchManuallyActivity.class);
                startActivity(intent);
            }
        });
        addUser.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(MainActivity.this, RegisterVehicleActivity.class);
                startActivity(intent);
            }
        });
    }
}