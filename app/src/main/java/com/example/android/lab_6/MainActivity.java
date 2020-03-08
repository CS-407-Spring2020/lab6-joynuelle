package com.example.android.lab_6;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Bundle;

import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.maps.model.PolylineOptions;

import java.security.Permissions;


public class MainActivity extends FragmentActivity {
    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 12;
    private FusedLocationProviderClient mFusedLocationProviderClient;
    private final LatLng mDestinationLatLng = new LatLng(43.075232, -89.404188);
    private GoogleMap mMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_map);
        mapFragment.getMapAsync(googleMap -> {
            mMap = googleMap;
            googleMap.addMarker(new MarkerOptions()
                    .position(mDestinationLatLng)
                    .title("Destination"));
            displayMyLocation();
        });
        mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        if (requestCode == PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {
            if (grantResults.length > 0
                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                displayMyLocation();
            }
        }
    }

    private void displayMyLocation() {
        // Check if permission granted
        int permission = ActivityCompat.checkSelfPermission(this.getApplicationContext(), android.Manifest.permission.ACCESS_FINE_LOCATION);
        // if not ask for it
        if (permission == PackageManager.PERMISSION_DENIED) {
            ActivityCompat.requestPermissions(this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
        // if permission granted, display marker at curr location
        else {
            mFusedLocationProviderClient.getLastLocation()
                    .addOnCompleteListener(this, task -> {
                        Location mLastKnownLocation = task.getResult();
                        if (task.isSuccessful() && mLastKnownLocation != null) {
                            mMap.addPolyline(new PolylineOptions().add(
                                    new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude()),
                                    mDestinationLatLng));

                            LatLng mCDestinationLatLng = new LatLng(mLastKnownLocation.getLatitude(), mLastKnownLocation.getLongitude());

                            SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.fragment_map);
                            mapFragment.getMapAsync(googleMap -> {
                                mMap = googleMap;
                                googleMap.addMarker(new MarkerOptions()
                                        .position(mCDestinationLatLng)
                                        .title("Current"));
                            });
                            mFusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);


                        }
                    });
        }

    }
}



