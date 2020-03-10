package com.mikhailzaitsevfls.locateme.mapFragment;

import android.app.PendingIntent;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.mikhailzaitsevfls.locateme.interfaces.PresenterInterfaceWithCallback;
import com.mikhailzaitsevfls.locateme.model.Db;
import com.mikhailzaitsevfls.locateme.model.Group;
import com.mikhailzaitsevfls.locateme.R;

import java.util.ArrayList;

public class MapFragmentPresenter implements MapFragmentPresenterInterface, PresenterInterfaceWithCallback {

    private MapFragmentInterface context;

    private GoogleMap googleMap;

    private ArrayList <Circle> circleArrayList;
    private ArrayList <Geofence> geofenceArrayList;
    private ArrayList<Group> groupArrayList;

    private PendingIntent pendingIntent;

    ////////////////////////////////////////////////////////////////////////permission----

    @Override
    public ArrayList<Group> getGroupsArrayList() {
        return groupArrayList;
    }

////////////////////////////////////////////////////////////////////////----permission

    @Override
    public void initMap(){
        initMapWithGeofencings();
        initMapWithMarkersAndCircles();
    }

    @Override
    public void dataHasBeenChanged(){
        refreshData();
        initMap();
    }

    private void initMapWithGeofencings(){
        if (pendingIntent!=null){
            context.getGeofencingClient().removeGeofences(pendingIntent);
        }
        geofenceArrayList = new ArrayList<>();
        for (int i = 0; i < getGroupsArrayList().size();i++) {
            geofenceArrayList.add(context.drawGeofence(getGroupsArrayList().get(i)));
        }
        context.getGeofencingClient().addGeofences(getGeofencingRequest(), getGeofencePendingIntent());
    }

    private void initMapWithMarkersAndCircles(){
        if (googleMap != null){
            googleMap.clear();
        }
        Circle circle;
        circleArrayList = new ArrayList<>();
        for (int i = 0; i < groupArrayList.size(); i++) {
            circle = context.drawCircle(groupArrayList.get(i), i);
            circle.setStrokeColor(R.color.colorAccent);
            circle.setStrokeWidth(9);
            circleArrayList.add(circle);
        }
    }

    private GeofencingRequest getGeofencingRequest(){
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_EXIT);
        builder.addGeofences(geofenceArrayList);
        return builder.build();
    }

    private PendingIntent getGeofencePendingIntent(){
        if (pendingIntent != null){
            return pendingIntent;
        }
        pendingIntent = context.createPendingIntent();
        return pendingIntent;
    }


    @Override
    public void attachView(MapFragmentInterface context) {
        this.context = context;
    }

    @Override
    public void detachView() {
        clearGoogleMap();
        this.context = null;
    }

    @Override
    public void isReady() {
        refreshData();
    }

    private void refreshData(){
        groupArrayList = Db.newInstance().getGroups();
    }

    @Override
    public boolean isDetached() {
        return context == null;
    }

    @Override
    public void saveCircleChanges(ArrayList<Circle> circles) {
        Db.newInstance().saveCirclesChanges(circles);
        dataHasBeenChanged();
    }

    @Override
    public ArrayList<Circle> getCircleArrayList() {
        return circleArrayList;
    }

    @Override
    public void clearGoogleMap() {
        if(googleMap != null){
            googleMap.clear();
        }
    }

    @Override
    public void setGoogleMap(GoogleMap googleMap) {
        this.googleMap = googleMap;
    }

    @Override
    public GoogleMap getGoogleMap() {
        return googleMap;
    }

    @Override
    public void callBack(Boolean bool) {
        //callback from permission checking
        if (bool){
            context.setMyLocationEnabled(true);
        }else {
            //dosomestuff
        }
    }
}
