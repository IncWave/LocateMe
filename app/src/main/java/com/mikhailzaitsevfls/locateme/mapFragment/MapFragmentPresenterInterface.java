package com.mikhailzaitsevfls.locateme.mapFragment;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.Circle;
import com.mikhailzaitsevfls.locateme.interfaces.PresenterInterface;
import com.mikhailzaitsevfls.locateme.model.Group;

import java.util.ArrayList;

public interface MapFragmentPresenterInterface extends PresenterInterface {

    void dataHasBeenChanged();

    void attachView(MapFragmentInterface context);

    void saveCircleChanges(ArrayList<Circle> circles);

    ArrayList<Circle> getCircleArrayList();

    void clearGoogleMap();

    void setGoogleMap(GoogleMap googleMap);


    GoogleMap getGoogleMap();

    void initMap();

    ArrayList<Group> getGroupsArrayList();
}
