package core.Entity;

import java.util.LinkedList;
import core.Entity.Interface.User_Interface;

/**
 * Created by francesco on 06/06/15.
 */
public class User implements User_Interface{

    private String name, surname, gender, city;
    private int age;
    private LinkedList<Vehicle> vehicles;

    public User(String name, String surname, int age, String gender, String city){

        this.name=name;
        this.surname=surname;
        this.age=age;
        this.gender=gender;
        this.city=city;
        vehicles= new LinkedList<Vehicle>();
    }

    @Override
    public void setName(String name){
        this.name=name;
    }

    @Override
    public String getName(){
        return this.name;
    }

    @Override
    public void setSurname(String surname){
        this.surname=surname;
    }

    @Override
    public String getSurname(){
        return this.surname;
    }

    @Override
    public void setAge(int age){
        this.age=age;
    }

    @Override
    public int getAge(){
        return this.age;
    }

    @Override
    public void setGender(String gender){
        if(gender.length()==1)
            this.gender=gender;
    }

    @Override
    public String getGender(){
        return this.gender;
    }

    @Override
    public void setVehicles(LinkedList<Vehicle> vehicles){
        this.vehicles=vehicles;
    }

    @Override
    public LinkedList<Vehicle> getVehicles(){
        return this.vehicles;
    }

    @Override
    public void addVehicle(Vehicle vehicle){
        this.vehicles.add(vehicle);
    }

    @Override
    public void removeVehicle (Vehicle vehicle){
        this.vehicles.remove(vehicle);
    }

}