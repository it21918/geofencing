package gr.dit.hua.android.geofence.geofenceapp.Database;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

//Η κλάση αυτή χρσηιμοποιείται για την δημιουργία της βάσης δεδομένων και την εκτέλεση sql εντολών
public class DbHelper extends SQLiteOpenHelper {
    public static String DB_NAME = "Districts_db";
    public static String TABLE_NAME = "Districts";
    public static String FIELD_1 = "LAT";
    public static String FIELD_2 = "LON";
    public static String FIELD_3 = "ACTIONN";
    public static String FIELD_4 = "TIMESTAMP";

    //sql εντολή για την δημιουργία του πίνακα της βάσης δεδομένων
    private String SQL_QUERY = "CREATE TABLE "+TABLE_NAME+" ( " + FIELD_1 + " TEXT, " + FIELD_2 + " TEXT, " + FIELD_3 + " TEXT, " + FIELD_4 + " TEXT )" ;

    public DbHelper(@Nullable Context context) {
        super(context, DB_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_QUERY);       //δημιουργεί τον πίνακα
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME); //αν υπάρχει πινακας με όνομα TABLE_NAME τον διαγράφει
        onCreate(db);                                     //δημιουργεί καινούργιο πίνακα
    }

    //μέθοδος για την εισαγωγή των μεταβλητων στον πίνακα της βασης
    public long insertDistrict(District contact){
        ContentValues values = new ContentValues();
        values.put(FIELD_1,contact.getLat());
        values.put(FIELD_2,contact.getLon());
        values.put(FIELD_3,contact.getAction());
        values.put(FIELD_4,contact.getTimestamp());
        long id = this.getWritableDatabase().insert(TABLE_NAME,null,values);
        return id;
    }

    //Η μεθοδός αυτή επιστρέφει όλες τις πλειάδες του πίνακα TABLE_NAME
    public Cursor selectAll(){
        return this.getReadableDatabase().query(TABLE_NAME,null,null,null,null,null,null);
    }

    //Η μεθοδός αυτή επιστρέφει όλες τις πλειάδες του πίνακα TABLE_NAME όπου το κλειδί τους ισούται με το id
    public Cursor selectDistrictById(long id){
        return this.getReadableDatabase().query(TABLE_NAME,null,"rowid=?",new String[]{id+""},null,null,null);
    }
}
