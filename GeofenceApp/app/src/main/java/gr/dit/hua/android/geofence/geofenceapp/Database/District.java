package gr.dit.hua.android.geofence.geofenceapp.Database;

//Το αντικείμενο District απόθηκευεται στην βάση δεδομένων
public class District {
    private String lat;
    private String lon;
    private String action;
    private String timestamp;

    public District(String lat, String lon, String action, String timestamp) {
        this.lat = lat;
        this.lon = lon;
        this.action = action;
        this.timestamp = timestamp;
    }

    public String getLat() {
        return lat;
    }

    public void setLat(String lat) {
        this.lat = lat;
    }

    public String getLon() {
        return lon;
    }

    public void setLon(String lon) {
        this.lon = lon;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }

    @Override
    public String toString() {
        return "District{" +
                "lat=" + lat +
                ", lon=" + lon +
                ", action='" + action + '\'' +
                ", timestamp='" + timestamp + '\'' +
                '}';
    }
}