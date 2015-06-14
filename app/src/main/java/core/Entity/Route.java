package core.Entity;
import core.Entity.Interface.Position_Interface;
import core.Entity.Interface.Route_Interface;

/**
 * Created by francesco on 06/06/15.
 */

class Position implements Position_Interface{

    public float latitude, longitude;

    public Position(float latitude, float longitude) {
        this.latitude = latitude;
        this.longitude = longitude;

    }
}
public class Route implements Route_Interface{

    private Position starting_position, final_position;
    private float price;
    public Route (float initLat, float initLon, float finalLat,float finalLon, float price){

        this.starting_position= new Position(initLat, initLon);
        this.final_position=new Position(finalLat, finalLon);
        this.price=price;

    }

    public void setStartingPos(float initLat, float initLon){

        this.starting_position.latitude=initLat;
        this.starting_position.longitude=initLon;

    }

    public Position getStartingPos(){
        return this.starting_position;
    }

    public void setFinalPos(float finalLat, float finalLon){
        this.final_position.latitude=finalLat;
        this.final_position.longitude=finalLon;
    }

    public Position getFinalPosition(){
        return this.final_position;
    }

    public void setPrice(float price){
        this.price=price;
    }

    public float getPrice(){
        return this.price;
    }

}
