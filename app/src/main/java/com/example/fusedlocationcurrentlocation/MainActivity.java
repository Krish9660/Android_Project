package com.example.fusedlocationcurrentlocation;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;

public class MainActivity extends AppCompatActivity {
    PermissionUtils permissionUtils;
    int Location_Permission_Code = 101;
    FusedLocationProviderClient fusedLocationProviderClient;
    LocationRequest locationRequest;
    TextView textLat, textLon;
    Context context;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        textLat = (TextView) findViewById(R.id.txt);
        textLon = (TextView) findViewById(R.id.txt1);
        permissionUtils=new PermissionUtils();
        locationRequest=new LocationRequest();
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
    }

    @Override
    protected void onStart() {
        super.onStart();

    }

    @SuppressLint("MissingPermission")
    private void setUpLocationListner() {
        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest.setInterval(2000).setFastestInterval(2000).setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
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

            fusedLocationProviderClient.requestLocationUpdates(locationRequest, new LocationCallback() {
                @Override
                public void onLocationResult(@NonNull LocationResult locationResult) {
                    super.onLocationResult(locationResult);
                    for (Location location : locationResult.getLocations()) {
                        double lattitude = location.getLatitude();
                        double longitude = location.getLongitude();
                        textLat.setText(String.valueOf(lattitude));
                        textLon.setText(String.valueOf(longitude));
                        Log.d("MainActivity", "lat:" + lattitude + " lon:" + longitude);


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
                    Toast.makeText(this, "permission not Granted", Toast.LENGTH_LONG).show();
                }

            }
        }
    }
