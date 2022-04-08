package com.example.mygeofencemap;

import androidx.fragment.app.FragmentActivity;
import android.content.ContentResolver;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;

import com.example.mygeofencemap.databinding.ActivityMapsBinding;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private static final String AUTHORITY = "gr.dit.hua.android.geofence.geofenceapp.districtscontentprovider";
    public static final String CONTENT_URI = "content://" + AUTHORITY;
    private GoogleMap mMap;
    private ActivityMapsBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        binding = ActivityMapsBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;

        mMap.clear();
        LatLng latLng ;

        //ο contentRosolver επικοινωνεί με τον provider ως client. Ουσιαστικά δεχεται τα δεδομένα που ζητάει απο τον provider
        ContentResolver resolver = this.getContentResolver();
        Cursor cursor = resolver.query(Uri.parse(CONTENT_URI + "/districts"), null, null, null, null);  //επιστρέφεται ενας κερσορας με τα δεδομενα της βασης
        if (cursor.moveToFirst()) {
            do {
                 latLng = new LatLng(cursor.getDouble(0), cursor.getDouble(1));
                 mMap.addMarker(new MarkerOptions().position(latLng).title("action: " + cursor.getString(2) + " timestamp: " + cursor.getString(3)));
            } while (cursor.moveToNext());
        }
    }

}