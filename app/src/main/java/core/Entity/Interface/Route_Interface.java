package core.Entity.Interface;

import core.Entity.Toolboth;
import core.PreferenceEditor;

/**
 * Created by francesco on 06/06/15.
 */
public interface Route_Interface {

    public void setId(int id);

    public int getId();

    public void setStartingToolboth(Toolboth_Interface toolboth);

    public Toolboth_Interface getStartingToolboth();

    public void setFinalToolboth(Toolboth_Interface toolboth);

    public Toolboth_Interface getFinalToolboth();

    public void setStartingPosition(double latitude, double longitude);

    public Position_Interface getStartingPosition();

    public void setFinalPosition(double latitude, double longitude);

    public void addCheckPoint(double check_point_lat, double check_point_lon);

    public Position_Interface[] getCheckPoints();

    public Position_Interface getFinalPosition();

    public void setCar(Car_Interface car);

    public Car_Interface getCar();

    public void setPrice(float price);

    public float getPrice();

    public void setTransaction(Transaction_Interface transaction);

    public Transaction_Interface getTransaction();


}
