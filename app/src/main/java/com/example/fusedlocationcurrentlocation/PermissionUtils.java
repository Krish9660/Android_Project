package com.example.fusedlocationcurrentlocation;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.LocationManager;
import android.provider.Settings;

import androidx.appcompat.app.AlertDialog;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class PermissionUtils {
//   LocationManager locationManager;
    public void requestAccessFineLocationPermission(Activity activity,int requestId){
        ActivityCompat.requestPermissions(activity,new String[] {Manifest.permission.ACCESS_FINE_LOCATION},requestId);

    }
    public boolean isAccessFineLocationGranted(Context context){
        if(ActivityCompat.checkSelfPermission(context, Manifest.permission.ACCESS_FINE_LOCATION)== PackageManager.PERMISSION_GRANTED){
            return true;
        }
        else {
            return false;
        }
    }
    public boolean isLocationEnabled(Context context){
       LocationManager locationManager=(LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
       boolean gpsEnabled=locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
       return gpsEnabled;
    }
    public void showGPSNotEnabledDialog(Context context){
        new AlertDialog.Builder(context).setTitle("GPS enable permission").setMessage("you need to enable your GPS to access your location")
                .setPositiveButton("Enable now", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        context.startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }

        }).create().show();
    }
    }

