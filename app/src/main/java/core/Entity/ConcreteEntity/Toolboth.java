package core.Entity.ConcreteEntity;

import core.Entity.Interface.Position_Interface;
import core.Entity.Interface.Toolboth_Interface;

/**
 * Created by kakashi on 8/2/15.
 */
public class Toolboth implements Toolboth_Interface {

    private int id;
    private String name, city;
    private Position position;

    public Toolboth(int id, String name, String city, double latitude, double longitude){
        this.id=id;
        this.name=name;
        this.city=city;
        this.position=new Position(latitude, longitude);
    }

    @Override
    public void setId(int id){
        this.id=id;
    }

    @Override
    public int getId(){
        return this.id;
    }

    @Override
    public void setName(String name) {
        this.name=name;
    }

    @Override
    public String getName() {
        return this.name;
    }

    @Override
    public void setCity(String city) {
        this.city=city;
    }

    @Override
    public String getCity() {
        return this.city;
    }

    @Override
    public void setPosition(Position_Interface position){
        this.position=(Position)position;
    }

    @Override
    public Position_Interface getPosition(){
        return this.position;
    }
    @Override
    public void setLatitude(double latitude) {
        this.position.latitude=latitude;
    }

    @Override
    public double getLatitude() {
        return this.position.latitude;
    }

    @Override
    public void setLongitude(double longitude) {
        this.position.longitude=longitude;
    }

    @Override
    public double getLongitude() {
        return this.position.longitude;
    }
}
