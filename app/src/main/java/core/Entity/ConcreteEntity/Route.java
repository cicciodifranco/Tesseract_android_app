package core.Entity.ConcreteEntity;
import core.Entity.Interface.Car_Interface;
import core.Entity.Interface.Route_Interface;
import core.Entity.Interface.Toolboth_Interface;
import core.Entity.Interface.Transaction_Interface;

/**
 * Created by francesco on 06/06/15.
 */


public class Route implements Route_Interface{

    private int id;
    private Position starting_position, final_position, checkpoints[];
    private Toolboth starting_toolboth, final_toolboth;
    private Car car;
    private float price;
    private String date;

    int index=0;

    public Route (){
        this(0, null, null);
    }
    public Route(int id, Toolboth start_toolboth, Car car){

        this(id, start_toolboth.getLatitude(), start_toolboth.getLongitude(), car);

        this.starting_toolboth=start_toolboth;

    }
    public Route(int id, Toolboth start_toolboth, Toolboth final_toolboth,Car car){

        this(id, start_toolboth.getLatitude(), start_toolboth.getLongitude(), final_toolboth.getLatitude(), final_toolboth.getLongitude(), car);
        this.starting_toolboth=start_toolboth;
        this.final_toolboth=final_toolboth;
    }
    public Route (int id, double initLat, double initLon, Car car){

        this(id, initLat, initLon, 0, 0, car);

    }
    public Route (int id, double initLat, double initLon, double finalLat,double finalLon, Car car){

        this(id, initLat, initLon, finalLat, finalLon, car,  0);

    }
    public Route (int id, double initLat, double initLon, double finalLat,double finalLon, Car car, float price){


        this(id, initLat, initLon, finalLat, finalLon, car, price, "");
    }

    public Route (int id, double initLat, double initLon, double finalLat,double finalLon, Car car, float price, String date){

        this.id=id;
        this.starting_position= new Position(initLat, initLon);
        this.final_position=new Position(finalLat, finalLon);
        this.checkpoints = new Position[20];
        this.price=price;
        this.car=car;
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
    public void setStartingToolboth(Toolboth_Interface toolboth){

        this.starting_toolboth=(Toolboth)toolboth;

    }

    @Override
    public Toolboth getStartingToolboth(){
        return this.starting_toolboth;
    }

    @Override
    public void setFinalToolboth(Toolboth_Interface toolboth){
        this.final_toolboth=(Toolboth)toolboth;
    }

    @Override
    public Toolboth getFinalToolboth(){
        return this.final_toolboth;
    }

    @Override
    public void setStartingPosition(double latitude, double longitude){
        this.starting_position.latitude=latitude;
        this.starting_position.longitude=longitude;
    }

    @Override
    public Position getStartingPosition(){
        return this.starting_position;
    }

    @Override
    public void setFinalPosition(double latitude, double longitude){
        this.final_position.latitude=latitude;
        this.final_position.longitude=longitude;

    }

    @Override
    public Position getFinalPosition(){
        return this.final_position;
    }

    @Override
    public void addCheckPoint(double check_point_lat, double check_point_lon){

        this.checkpoints[index]=new Position(check_point_lat,check_point_lon);

    }

    @Override
    public Position[] getCheckPoints(){
        return this.checkpoints;
    }

    @Override
    public void setCar(Car_Interface car){
        this.car=(Car)car;
    }

    @Override
    public Car getCar(){
        return this.car;
    }

    @Override
    public void setPrice(float price){
        this.price=price;
    }

    @Override
    public float getPrice(){
        return this.price;
    }

    @Override
    public void setTransaction(Transaction_Interface transaction) {
        
    }

    @Override
    public Transaction_Interface getTransaction() {
        return null;
    }

    @Override
    public void setDate(String date){

        this.date=date;
    }

    @Override
    public String getDate(){
        return this.date;
    }
}
