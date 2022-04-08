package gr.dit.hua.android.geofence.geofenceapp;

import android.content.BroadcastReceiver;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingEvent;

import java.text.SimpleDateFormat;
import java.util.Date;

import gr.dit.hua.android.geofence.geofenceapp.Database.DbHelper;
import gr.dit.hua.android.geofence.geofenceapp.Database.DistrictsContentProvider;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "GeofenceBroadcastReceiv";

    @Override
    public void onReceive(Context context, Intent intent) {
        // TODO: This method is called when the BroadcastReceiver is receiving

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        if (geofencingEvent.hasError()) {
            Log.d(TAG, "onReceive: Error receiving geofence event...");
            return;
        }

        int transitionType = geofencingEvent.getGeofenceTransition(); //επιστρέφει έναν ακεραιο που προσδιορίζει τον τύπο του transition

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); //σεταρουμε το format της ημερομηνίας
        String timestamp  = dateFormat.format(new Date());

        ContentResolver resolver = context.getContentResolver(); //Χρησιμοποιείστε ώστε να επιλέξουμε τον σώστο contentProvider μέσω του content uri

        ContentValues contentValues = new ContentValues(); //Στον contentValues αποθηκεύουμε όλες τις μεταβλητές που μας ενδιαφέρουν να αποθηκεύσουμε στην βάση
        contentValues.put("LAT", geofencingEvent.getTriggeringLocation().getLatitude());
        contentValues.put("LON",geofencingEvent.getTriggeringLocation().getLongitude());
        contentValues.put("TIMESTAMP", timestamp);

        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                Toast.makeText(context, "GEOFENCE_TRANSITION_ENTER", Toast.LENGTH_SHORT).show();
                contentValues.put("ACTION", "GEOFENCE_TRANSITION_ENTER");                                     //Με τον resolver αποθηκέυουμε τις τιμές του contentValues στην βάση
                resolver.insert(Uri.parse(DistrictsContentProvider.CONTENT_URI+"/districts"),contentValues);  //ως ορισμα παίρνει απο την κλάση DistrictsContentProvider την μεταβλητη CONTENT_URI και προσθέτουμε το /districts για να γίνει το case1 της insert
                break;
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                Toast.makeText(context, "GEOFENCE_TRANSITION_EXIT", Toast.LENGTH_SHORT).show();
                contentValues.put("ACTION", "GEOFENCE_TRANSITION_EXIT");
                resolver.insert(Uri.parse(DistrictsContentProvider.CONTENT_URI+"/districts"),contentValues);
                break;
        }



    }

}