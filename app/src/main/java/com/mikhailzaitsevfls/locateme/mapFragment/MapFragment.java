package com.mikhailzaitsevfls.locateme.mapFragment;

import android.app.PendingIntent;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.Circle;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.mikhailzaitsevfls.locateme.model.Group;
import com.mikhailzaitsevfls.locateme.R;
import com.mikhailzaitsevfls.locateme.checking.permission.CheckPermission;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MapFragment extends Fragment implements OnMapReadyCallback, MapFragmentInterface {

    private MapFragmentPresenterInterface presenter;
    private Unbinder unbinder;

    @BindView(R.id.fragment_map_change_radius)
    public SeekBar seekBar;

    @BindView(R.id.fragment_map_save_button)
    public Button saveButton;

    @BindView(R.id.fragment_map_mapview)
    public MapView mapView;

    private float zCircleParam = 0.1f;

    private GeofencingClient geofencingClient;

    public static MapFragment newInstance() {
        return new MapFragment();
    }
    private MapFragment() {}


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setRetainInstance(true);
        geofencingClient = LocationServices.getGeofencingClient(Objects.requireNonNull(getContext()));
        init();
    }


    private void init(){
        this.presenter = new MapFragmentPresenter();
        presenter.attachView(this);
        presenter.isReady();
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        presenter.detachView();
        unbinder.unbind();
    }

    @Override
    public void onResume() {
        super.onResume();
        saveButton = Objects.requireNonNull(getView()).findViewById(R.id.fragment_map_save_button);
        saveButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        unbinder = ButterKnife.bind(this,view);
        return view;
    }


    @OnClick(R.id.fragment_map_save_button)
    public void onClick(View v) {
        seekBar.setVisibility(View.GONE);
        saveButton.setVisibility(View.INVISIBLE);

        presenter.saveCircleChanges(presenter.getCircleArrayList());
    }

    @Override
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        MapView mapView = view.findViewById(R.id.fragment_map_mapview);
        if (mapView != null){
            mapView.onCreate(null);
            mapView.onResume();
            mapView.getMapAsync(this);
        }
    }


    @Override
    public void setMyLocationEnabled(boolean isMyLocationEnabled){
        if (isMyLocationEnabled){
            presenter.getGoogleMap().setMyLocationEnabled(true);
        }else {
            presenter.getGoogleMap().setMyLocationEnabled(false);
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        presenter.setGoogleMap(googleMap);
        setMyLocationEnabled(CheckPermission.checkPermission(getActivity()));

        MapsInitializer.initialize(Objects.requireNonNull(getContext()));
        googleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        presenter.initMap();
    }


    @Override
    public Circle drawCircle(Group group, int i) {
        zCircleParam += 0.1f;
        Circle circle = presenter.getGoogleMap().addCircle(new CircleOptions()
                .center(new LatLng(group.getLatitude(),
                        group.getLongitude()))
                .radius(group.getRadius())
                .zIndex(zCircleParam)
                .clickable(true));
        circle.setTag(i);
        drawMarker(circle, group);

        presenter.getGoogleMap().setOnCircleClickListener(circle1 -> {
            seekBar.setVisibility(View.VISIBLE);
            seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
                @Override
                public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                    if (i != 0) {
                        circle.setRadius(i * 50);
                    } else {
                        circle.setRadius(50);
                    }
                }

                @Override
                public void onStartTrackingTouch(SeekBar seekBar) {
                    if (seekBar.getProgress() != 0) {
                        circle.setRadius(seekBar.getProgress() * 50);
                    } else {
                        circle.setRadius(50);
                    }
                }

                @Override
                public void onStopTrackingTouch(SeekBar seekBar) {
                    if (seekBar.getProgress() != 0) {
                        circle.setRadius(seekBar.getProgress() * 50);
                    } else {
                        circle.setRadius(50);
                    }
                    saveButton.setVisibility(View.VISIBLE);
                }
            });
            seekBar.setProgress((int) circle.getRadius() / 50);
        });
        return circle;
    }


    private void drawMarker(final Circle circle, final Group group) {
        Marker marker = presenter.getGoogleMap()
                .addMarker(new MarkerOptions()
                        .position(circle.getCenter())
                        .draggable(true)
                        .title(group.getGroupName()));
        marker.setTag(circle);

        presenter.getGoogleMap().setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {

            @Override
            public void onMarkerDragStart(Marker marker) {
            }

            @Override
            public void onMarkerDrag(Marker marker) {
                Circle circle1 = (Circle) marker.getTag();
                Objects.requireNonNull(circle1).setCenter(marker.getPosition());
            }

            @Override
            public void onMarkerDragEnd(Marker marker) {
                Circle circle1 = (Circle) marker.getTag();
                Objects.requireNonNull(circle1).setCenter(marker.getPosition());
                saveButton.setVisibility(View.VISIBLE);
            }
        });
    }


    public Geofence drawGeofence(Group group){
        return new Geofence.Builder()
                .setRequestId(String.valueOf(group.getGroupId()))
                .setCircularRegion(group.getLatitude(),group.getLongitude(),group.getRadius())
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .setLoiteringDelay(1000)
                .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_DWELL | Geofence.GEOFENCE_TRANSITION_EXIT)
                .build();
    }


    @Override
    public GeofencingClient getGeofencingClient() {
        return geofencingClient;
    }


    @Override
    public PendingIntent createPendingIntent() {
        Intent intent = new Intent(getContext(), GeofenceBroadcastReceiver.class);
        return PendingIntent.getBroadcast(getContext(),0,intent,PendingIntent.FLAG_UPDATE_CURRENT);
    }

    @Override
    public void dataHasBeenChanged(){
        presenter.dataHasBeenChanged();
    }
}