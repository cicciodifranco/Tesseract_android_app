package core.Entity.Interface;

/**
 * Created by francesco on 06/06/15.
 */
public interface Route_Interface {
    public void setStartingPos(float initLat, float initLon);

    public Position_Interface getStartingPos();

    public void setFinalPos(float finalLat, float finalLon);

    public Position_Interface getFinalPosition();

    public void setPrice(float price);

    public float getPrice();


}
