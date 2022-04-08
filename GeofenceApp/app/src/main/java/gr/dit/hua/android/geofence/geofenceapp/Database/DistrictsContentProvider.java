package gr.dit.hua.android.geofence.geofenceapp.Database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.net.Uri;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class DistrictsContentProvider extends ContentProvider {
    private static UriMatcher uriMatcher;
    private DbHelper dbHelper;
    private static final String AUTHORITY = "gr.dit.hua.android.geofence.geofenceapp.districtscontentprovider";
    public static final String CONTENT_URI = "content://"+AUTHORITY;
    static{
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(AUTHORITY,"districts",1);
        uriMatcher.addURI(AUTHORITY,"districts/#",2);
    }

    @Override
    public boolean onCreate() {
        dbHelper = new DbHelper(getContext());
        if (dbHelper.getWritableDatabase() != null) {
            return true;
        }
        return false;
    }

    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        Cursor result = null;
        switch (uriMatcher.match(uri)){
            case 1:                            //αν το url = gr.dit.hua.android.geofence.geofenceapp.districtscontentprovider/districts
                result = dbHelper.selectAll(); //εκτελει την μεθοδο selectAll όπου επιστρέφονται όλες οι πλειάδες του πίνακα
                break;
            case 2:                            //αν το url = gr.dit.hua.android.geofence.geofenceapp.districtscontentprovider/districts/123
                result = dbHelper.selectDistrictById(Integer.parseInt(uri.getLastPathSegment())); //επιστρέφει την πλειάδα με id = 123
                break;
        }
        return result;
    }

    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        return null;
    }

    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        Uri result = null;
        switch(uriMatcher.match(uri)){
            case 1: //αν το uri = r.dit.hua.android.geofence.geofenceapp.districtscontentprovider/districts
                District district = new District(values.getAsString("LAT"),values.getAsString("LON"),values.getAsString("ACTION"),values.getAsString("TIMESTAMP") ); //Δημιουργούμε το αντικείμενο District
                long id = dbHelper.insertDistrict(district); //εισάγουμε το αντικείμενο district στην βάση μέσω της μεθοδου της κλάσης DbHelper. Η μέθοδος αυτή επιστρέφει ένα κλέιδί
                result = Uri.parse(AUTHORITY+"/districts/"+id);
                break;
        }
        return result;
    }

    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }

    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        return 0;
    }
}
