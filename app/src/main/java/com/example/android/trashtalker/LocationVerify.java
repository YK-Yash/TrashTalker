package com.example.android.trashtalker;

import android.content.pm.PackageManager;
import android.location.Location;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

public class LocationVerify extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {

    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 1000;
    public Location mLastLocation;
    private GoogleApiClient mGoogleApiClient;
    private TextView lblLocation;
    public Button proc, ret;
    int MY_PERMISSIONS_REQUEST_READ_LOCATION;
    double latitude, longitude;

    FirebaseDatabase db;
    DatabaseReference worker_ref, dustbin_ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_location_verify);

        db = FirebaseDatabase.getInstance();
        worker_ref = db.getReferenceFromUrl("https://myapplication-d680d.firebaseio.com/Workers/Pyarelal");
        dustbin_ref = db.getReferenceFromUrl("https://myapplication-d680d.firebaseio.com/Dustbins/D1");


        if (ContextCompat.checkSelfPermission(LocationVerify.this,
                android.Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(LocationVerify.this,
                    new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION},
                    MY_PERMISSIONS_REQUEST_READ_LOCATION);

            Handler handler = new Handler();
            handler.postDelayed(new Runnable() {
                @Override
                public void run() {

                    displayLocation();

                }
            }, 5000);

        }

        if (checkPlayServices()) {

            buildGoogleApiClient();
        }

    }

    private void displayLocation() {

        mLastLocation = LocationServices.FusedLocationApi
                .getLastLocation(mGoogleApiClient);

        if (mLastLocation != null) {
            latitude = mLastLocation.getLatitude();
            longitude = mLastLocation.getLongitude();

            //Toast.makeText(getBaseContext(), "Lat : "+latitude+" Long : "+longitude, Toast.LENGTH_LONG).show();

            dustbin_ref.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    float dv = dataSnapshot.getValue(Float.class);
                    if(dv<20){
                        worker_ref.setValue(1);
                        double dist = calculateDistanceInKilometer(12.97078, 79.15749, latitude, longitude);
                        if(dist<1) {
                            Toast.makeText(getBaseContext(), "Thank you", Toast.LENGTH_LONG).show();
                            worker_ref.setValue("cleared");
                        }
                        else{
                            Toast.makeText(getBaseContext(), "You are no where near the bin!", Toast.LENGTH_LONG).show();
                        }
                        finish();

                    }

                    else{
                        Toast.makeText(getBaseContext(), "You have not cleared the garbage", Toast.LENGTH_LONG).show();
                        finish();
                    }

                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });

        } else {

            Toast.makeText(getBaseContext(), "No location", Toast.LENGTH_LONG).show();
        }
    }

    protected synchronized void buildGoogleApiClient() {
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .addApi(LocationServices.API).build();
    }

    private boolean checkPlayServices() {
        int resultCode = GooglePlayServicesUtil
                .isGooglePlayServicesAvailable(this);
        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {
                GooglePlayServicesUtil.getErrorDialog(resultCode, this,
                        PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Toast.makeText(getApplicationContext(),
                        "This device is not supported.", Toast.LENGTH_LONG)
                        .show();
                finish();
            }
            return false;
        }
        return true;
    }

    @Override
    protected void onStart() {
        super.onStart();
        if (mGoogleApiClient != null) {
            mGoogleApiClient.connect();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();

        checkPlayServices();
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {
        displayLocation();
    }

    @Override
    public void onConnectionSuspended(int i) {
        mGoogleApiClient.connect();

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {


    }

    public final static double AVERAGE_RADIUS_OF_EARTH_KM = 6371;
    public double calculateDistanceInKilometer(double userLat, double userLng,
                                            double venueLat, double venueLng) {

        double latDistance = Math.toRadians(userLat - venueLat);
        double lngDistance = Math.toRadians(userLng - venueLng);

        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(userLat)) * Math.cos(Math.toRadians(venueLat))
                * Math.sin(lngDistance / 2) * Math.sin(lngDistance / 2);

        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));

//        return (int) (Math.round(AVERAGE_RADIUS_OF_EARTH_KM * c));
        return AVERAGE_RADIUS_OF_EARTH_KM*c;
    }

}
