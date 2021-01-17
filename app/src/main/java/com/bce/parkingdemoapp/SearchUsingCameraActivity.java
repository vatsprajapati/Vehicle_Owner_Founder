package com.bce.parkingdemoapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.ml.vision.FirebaseVision;
import com.google.firebase.ml.vision.common.FirebaseVisionImage;
import com.google.firebase.ml.vision.text.FirebaseVisionText;
import com.google.firebase.ml.vision.text.FirebaseVisionTextDetector;

import java.util.List;

public class SearchUsingCameraActivity extends AppCompatActivity {

    private TextView retrievedInfo,vehicleNoPlateDetected;
    private ImageView capturedImage;
    private Button takeImage, getVehicleInfo, callOwner;
    FirebaseDatabase database;
    DatabaseReference reference;

    static final int REQUEST_IMAGE_CAPTURE = 1;
    private Bitmap imageBitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_using_camera);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        retrievedInfo = findViewById(R.id.retrievedInfoTextView);
        capturedImage = findViewById(R.id.capturedImageView);
        takeImage = findViewById(R.id.takeImageButton);
        getVehicleInfo = findViewById(R.id.getVehicleInfoButton);
        callOwner = findViewById(R.id.callOwnerButton);
        vehicleNoPlateDetected = findViewById(R.id.vehiclePlateDetectedTextView);

        takeImage.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View view) {
                Log.i("SearchUsingCameraActivity", "Take Image");
                dispatchTakePictureIntent();
                retrievedInfo.setText("");
            }
        });

        getVehicleInfo.setOnClickListener(new View.OnClickListener() {
            @SuppressLint("LongLogTag")
            @Override
            public void onClick(View view) {
                Log.i("SearchUsingCameraActivity", "getVehicleInfoCLicked");
                detectTextFromImage();
            }
        });
    }

    @SuppressLint("LongLogTag")
    private void dispatchTakePictureIntent() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        try {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        } catch (ActivityNotFoundException e) {
            Log.i("SearchUsingCameraActivity", e + "Error");
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            imageBitmap = (Bitmap) extras.get("data");
            capturedImage.setImageBitmap(imageBitmap);
        }
    }

    private void detectTextFromImage() {
        FirebaseVisionImage image = FirebaseVisionImage.fromBitmap(imageBitmap);
        FirebaseVisionTextDetector detector = FirebaseVision.getInstance().getVisionTextDetector();
        detector.detectInImage(image).addOnSuccessListener(new OnSuccessListener<FirebaseVisionText>() {
            @SuppressLint("LongLogTag")
            @Override
            public void onSuccess(FirebaseVisionText firebaseVisionText) {
                displayTextFromImage(firebaseVisionText);
                Log.i("SearchUsingCameraActivity", "detect Text from Image Successfull");
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(SearchUsingCameraActivity.this, e.getMessage(), Toast.LENGTH_LONG);
            }
        });
    }

    @SuppressLint("LongLogTag")
    private void displayTextFromImage(FirebaseVisionText firebaseVisionText) {
        List<FirebaseVisionText.Block> blockList = firebaseVisionText.getBlocks();
        if (blockList.size() == 0) {
            Log.i("SearchUsingCameraActivity", "no Text found from image");
            Toast.makeText(this, "no text found", Toast.LENGTH_SHORT).show();

        } else {

            for (FirebaseVisionText.Block block : firebaseVisionText.getBlocks()) {
                String text = block.getText().trim();
                final String noSpaceText = text.replaceAll("\\s", "");
                vehicleNoPlateDetected.setText(noSpaceText);

                //final String vehicleNoUserEntered = vehicleNo.getText().toString();

                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                DatabaseReference reference = database.getReference("users");

                Query checkUser = reference.orderByChild("vehicle").equalTo(noSpaceText);

                checkUser.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            String vehicleNofromDB = snapshot.child(noSpaceText).child("vehicle").getValue(String.class);
                            assert vehicleNofromDB != null;
                            if (vehicleNofromDB.equals(noSpaceText)) {
                                String name = snapshot.child(noSpaceText).child("username").getValue(String.class);
                                final String phone = snapshot.child(noSpaceText).child("phone").getValue(String.class);

                                retrievedInfo.setText(name);
                                callOwner.setText("CALL : " + phone);
                                callOwner.setVisibility(View.VISIBLE);
                                callOwner.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View view) {
                                        Intent callIntent = new Intent(Intent.ACTION_DIAL, Uri.fromParts("tel", phone, null));
                                        startActivity(callIntent);
                                    }
                                });
                            } else {
                                Toast.makeText(SearchUsingCameraActivity.this, "No Vehicle Found", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(SearchUsingCameraActivity.this, "No Vehicle Found", Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                    }
                });
            }

            Log.i("SearchUsingCameraActivity", "shown Text Successfully");
            //retrievedInfo.setText(noSpaceText);
        }
    }

    @Override
    public boolean onSupportNavigateUp() {
        finish();
        return true;
    }
}