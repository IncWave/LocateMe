package com.mikhailzaitsevfls.locateme.mapFragment;

import android.app.PendingIntent;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.maps.model.Circle;
import com.mikhailzaitsevfls.locateme.model.Group;

public interface MapFragmentInterface {

    void setMyLocationEnabled(boolean isMyLocationEnabled);

    Circle drawCircle(Group group, int i);

    GeofencingClient getGeofencingClient();

    Geofence drawGeofence(Group group);

    PendingIntent createPendingIntent();

    void dataHasBeenChanged();
}
