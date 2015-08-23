package core.Entity.Interface;

/**
 * Created by kakashi on 8/2/15.
 */
public interface Toolboth_Interface {

    public void setId(int id);
    public int getId();
    public void setName(String name);
    public String getName();
    public void setCity(String city);
    public String getCity();
    public void setPosition(Position_Interface position);
    public Position_Interface getPosition();
    public void setLatitude(double latitude);
    public double getLatitude();
    public void setLongitude(double longitude);
    public double getLongitude();
}
