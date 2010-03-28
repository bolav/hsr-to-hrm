package no.ikke.sportstracker.data;

import de.saring.polarviewer.data.*;


public class GPSSample extends ExerciseSample {
    private float longitude;
    private float latitude;
    
    public float getLongitude () {
        return longitude;
    }
    
    public void setLongitude (float longitude) {
        this.longitude = longitude;
    }
    
    public float getLatitude () {
        return latitude;
    }
    
    public void setLatitude (float latitude) {
        this.latitude = latitude;
    }
}