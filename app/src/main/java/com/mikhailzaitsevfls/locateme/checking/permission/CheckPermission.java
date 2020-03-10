package com.mikhailzaitsevfls.locateme.checking.permission;

import android.Manifest;
import android.app.Activity;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.PermissionChecker;

import static androidx.core.content.PermissionChecker.PERMISSION_GRANTED;
import static androidx.core.content.PermissionChecker.checkSelfPermission;

public class CheckPermission {


    public static void requirePermission(Activity activity,int PERMISSION_NUMBER){
        ActivityCompat.requestPermissions(
                activity,
                new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                PERMISSION_NUMBER);
    }

    public static boolean checkPermission(Activity activity){
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M ) {
            return (checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PermissionChecker.PERMISSION_GRANTED)
                    && (checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION) == PermissionChecker.PERMISSION_GRANTED);
        } else
            return (PERMISSION_GRANTED == ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION))
                    && (PERMISSION_GRANTED == ContextCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_COARSE_LOCATION));
    }


    private CheckPermission(){}
}
