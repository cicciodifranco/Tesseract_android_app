package core.Entity;

import core.Entity.Interface.Position_Interface;

/**
 * Created by kakashi on 8/3/15.
 */
public class Position implements Position_Interface {

    public double latitude, longitude;

    public Position(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;

    }
    public double getLatitude(){
        return this.latitude;
    }
    public double getLongitude(){
        return this.longitude;
    }
}