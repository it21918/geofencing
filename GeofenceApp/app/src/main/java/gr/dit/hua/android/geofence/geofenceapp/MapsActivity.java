package gr.dit.hua.android.geofence.geofenceapp;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CircleOptions;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import java.util.List;

import gr.dit.hua.android.geofence.geofenceapp.databinding.ActivityMapsBinding;


public class MapsActivity extends FragmentActivity implements OnMapReadyCallback  {

    private static final int GEOFENCING_FINE_LOCATION_PERMISSION_CODE = 999;
    private static final float GEOFENCE_RADIUS = 200;
    private static final int ENABLE_MYLOCATION_FINE_LOCATION_PERMISSION_CODE = 111;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;
    GeofencingClient geofencingClient;
    private List<Geofence> geofenceList = new ArrayList<>();

    private PendingIntent geofencePendingIntent;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        geofencingClient = LocationServices.getGeofencingClient(MapsActivity.this);

    }

    //Η συνάρτηση αυτή χρησιμοποιείται για να προσθέτουμε goefences.
    private void addGeofences() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) { //ελέγχουμε αν δεν έχουμε αδεια στην τοποθεσία του χρήστη
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, GEOFENCING_FINE_LOCATION_PERMISSION_CODE); //σε περίπτωση που δεν έχουμε ζήταμε από τον χρήστη να μας την δώσει
            return;
        }

        geofencingClient.addGeofences(getGeofencingRequest(), getGeofencePendingIntent())
                .addOnSuccessListener(MapsActivity.this, unused -> Log.d("GeofencingClient", "Geofence succesfully added!"))
                .addOnFailureListener(MapsActivity.this, unused -> Log.d("GeofencingClient", "Geofence addition failed!"));
    }

    private GeofencingRequest getGeofencingRequest() {
        GeofencingRequest.Builder builder = new GeofencingRequest.Builder();
        builder.setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER);
        builder.addGeofences(geofenceList);
        return builder.build();
    }


    private PendingIntent getGeofencePendingIntent() { //ο broadcastReceiver χειρίζεται τα tranzitions από τα geofences
        if (geofencePendingIntent != null) { //αν υπάρχει ήδη το prndingintent δεν δημιουργουμε καινουργιο
            return geofencePendingIntent;
        }
        Intent intent = new Intent(this, GeofenceBroadcastReceiver.class);

        //χρησιμοποιούμε το FLAG_UPDATE_CURRENT για να έχουμε το ίδιο pendingIntent όταν καλούμε την addGeofences()
        geofencePendingIntent = PendingIntent.getBroadcast(this,0, intent, PendingIntent.FLAG_UPDATE_CURRENT);
        return geofencePendingIntent;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case GEOFENCING_FINE_LOCATION_PERMISSION_CODE:                   //γνωρίζουμε ότι έχουμε ζητήσει άδεια για το fine location
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) { //οπότε ελέγχουμε η άδεια έχει δοθεί
                    addGeofences();                                         //και επιστρέφουμε στην addGeofence() και εκτελείται ο κώδικας κάτω από το if(ActivityCompat...)
                }
                break;
            case ENABLE_MYLOCATION_FINE_LOCATION_PERMISSION_CODE:
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    enableMyLocation();
                }
                break;
        }
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.setOnMapLongClickListener(latLng -> {  //στο σημείο που ο χρήστης κάνει long click προσθέτουμε το geofence στην λίστα με τα geofences
            geofenceList.add(new Geofence.Builder()
                    .setCircularRegion(latLng.latitude,latLng.longitude,GEOFENCE_RADIUS) //σετάρουμε τις συνταταγμένες του και την ακτίνα του
                    .setExpirationDuration(Geofence.NEVER_EXPIRE)                        //σετάρουμε την διάρκεια του geofence
                    .setTransitionTypes(Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_EXIT) //σεταρουμε ποια ανελαγή μας ενδιαφέρει
                    .setRequestId("ID")
                    .build());

            CircleOptions circleOptions = new CircleOptions(); //δημιουργούμε το geofence στον χαρτη με μορφή ενός κύκλου
            circleOptions.radius(GEOFENCE_RADIUS);
            circleOptions.center(latLng);
            mMap.addCircle(circleOptions);

            addGeofences(); //την καλούμε για να προσθέσουμε το geofence
        });

        enableMyLocation();
    }


    private void enableMyLocation(){
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(MapsActivity.this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, ENABLE_MYLOCATION_FINE_LOCATION_PERMISSION_CODE);
            return;
        }
        mMap.setMyLocationEnabled(true);
    }

}