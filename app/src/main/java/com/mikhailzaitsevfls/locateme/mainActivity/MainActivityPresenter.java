package com.mikhailzaitsevfls.locateme.mainActivity;

import android.annotation.SuppressLint;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;

import com.mikhailzaitsevfls.locateme.checking.db.CheckLocalDbChanges;
import com.mikhailzaitsevfls.locateme.interfaces.PresenterInterfaceWithSecondCallBack;
import com.mikhailzaitsevfls.locateme.interfaces.PresenterInterfaceWithSecondDetach;
import com.mikhailzaitsevfls.locateme.model.Db;
import com.mikhailzaitsevfls.locateme.checking.internet.CheckInternetConnection;
import com.mikhailzaitsevfls.locateme.noInternetActivity.NoInternetActivity;
import com.mikhailzaitsevfls.locateme.permissionActivity.PermissionActivity;

import java.util.Objects;

import static android.content.Context.LOCATION_SERVICE;


public class MainActivityPresenter implements MainActivityPresenterInterface,
        PresenterInterfaceWithSecondCallBack, PresenterInterfaceWithSecondDetach {

    private MainActivityInterface activity;


    private LocationManager locationManager;
    private LocationListener locationListener;

    @Override
    public void attachView(MainActivityInterface activity) {
        this.activity = activity;
    }

    @Override
    public void detachView() {
        CheckInternetConnection.cancelChecking(this);
        Db.newInstance().deleteDb();
        removeLocationListener();
        activity = null;
    }

    @Override
    public void detachViewNotFromParent() {
        activity.startNewActivity(NoInternetActivity.class);
        //////////////////////////////////////////////////////////////////////
        Db.newInstance().deleteDb();
        removeLocationListener();
        ////////////////////////////////////////////////////////////////////////
    }

    private void removeLocationListener(){
        if (locationListener != null){
            locationManager.removeUpdates(locationListener);
            locationListener = null;
        }
    }

    @Override
    public void isReady() {
        Db.newInstance();
        startLocationChangesListener();
    }

    @Override
    public boolean isDetached() {
        return activity == null;
    }


    @Override
    public void startCheckingDbChanges(){
        CheckLocalDbChanges.startChecking(this);
    }


    @Override
    public void stopCheckingDbChanges(){
        CheckLocalDbChanges.cancelChecking();
    }

    @Override
    public void startCheckingInternet() {
        CheckInternetConnection.startChecking(this, true);
    }

    @Override
    public void stopCheckingInternet() {
        CheckInternetConnection.cancelChecking(this);
    }


    private void startLocationChangesListener() {
        locationManager = (LocationManager) activity.getSysService(LOCATION_SERVICE);
        locationListener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                Db.newInstance().setLocation(location);
                activity.makeToast("Location Changed");
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {
            }

            @Override
            public void onProviderEnabled(String provider) {
            }

            @Override
            public void onProviderDisabled(String provider) {
            }
        };
        if (activity.checkPermission()){
            Objects.requireNonNull(locationManager).requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, locationListener);
        }else {
            activity.requirePermission(this);
        }
    }



   /* public void sendNotification(String whatGeofences){
        NotificationCompat.Builder builder = activity.getNotificationBuilder(whatGeofences,"chanel_id");

        Notification notification = builder.build();

        NotificationManager notificationManager =
                (NotificationManager) Service.getSystemService(Context.NOTIFICATION_SERVICE);
        Objects.requireNonNull(notificationManager).notify(1, notification);
    }*/

    @SuppressLint("MissingPermission")//permission checked in startLocationChangesListener()
    @Override
    public void callBack(Boolean bool) {
        if (bool){
            Objects.requireNonNull(locationManager).requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 1, locationListener);
        }else {
            activity.startNewActivity(PermissionActivity.class);
        }
    }

    @Override
    public void secondCallBack(Boolean ifDataChangedTrue_IfOnlineChangedFalse){
        if (ifDataChangedTrue_IfOnlineChangedFalse){
            activity.dataHasBeenChanged();
        }else {
            activity.onlineHasBeenChanged();
        }
    }
}

