package com.example.fusedlocationcurrentlocation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity {
    PermissionUtils permissionUtils;
    int Location_Permission_Code = 10101001;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationReq;
    TextView textLat, textLon;
    Context context;
    Button btn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textLat = (TextView) findViewById(R.id.txt);
        textLon = (TextView) findViewById(R.id.txt1);
        btn=(Button)findViewById(R.id.btn);
        permissionUtils=new PermissionUtils();
        locationReq=new LocationRequest();
//        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        if (permissionUtils.isAccessFineLocationGranted(this)) {
            if (permissionUtils.isLocationEnabled(this)) {
                setUpLocationListner();
            } else {
                permissionUtils.showGPSNotEnabledDialog(this);
            }
        } else {
            permissionUtils.requestAccessFineLocationPermission(this, Location_Permission_Code);
        }
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(MainActivity.this,MainActivity2.class);
                startActivity(intent);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @SuppressLint("MissingPermission")
    private void setUpLocationListner() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationReq.setInterval(2000).setFastestInterval(2000).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }

            fusedLocationProviderClient.requestLocationUpdates(locationReq, new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    for (Location location : locationResult.getLocations()) {
                        double la = location.getLatitude();
                        double lo = location.getLongitude();
                        textLat.setText(String.valueOf(la));
                        textLon.setText(String.valueOf(lo));
                        Log.d("MainActivity", "lat:" + la + " lon:" + lo);


                    }
                }
            }, Looper.myLooper());
        }



        @Override
        public void onRequestPermissionsResult ( int requestCode, @NonNull String[] permissions,
        @NonNull int[] grantResults){
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if (requestCode == Location_Permission_Code) {
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (permissionUtils.isLocationEnabled(this)) {
                        setUpLocationListner();
                    } else {
                        permissionUtils.showGPSNotEnabledDialog(this);
                    }

                } else {
                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show();
                }

            }
        }
    }
