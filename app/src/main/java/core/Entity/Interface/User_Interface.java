package core.Entity.Interface;

import java.util.LinkedList;

import core.Entity.Vehicle;

/**
 * Created by francesco on 06/06/15.
 */
public interface User_Interface {

    public void setName(String name);
    public String getName();

    public void setSurname(String surname);
    public String getSurname();

    public void setAge(int age);
    public int getAge();

    public void setGender(String gender);
    public String getGender();

    public void setVehicles(LinkedList<Vehicle> vehicles);
    public LinkedList<Vehicle> getVehicles();
    public void addVehicle(Vehicle vehicle);
    public void removeVehicle (Vehicle vehicle);

}
