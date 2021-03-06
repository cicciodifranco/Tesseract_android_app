package core.Entity.ConcreteEntity;

import java.util.LinkedList;

import core.Entity.Interface.Car_Interface;

/**
 * Created by francesco on 06/06/15.
 */
public class Car implements Car_Interface {

    private String registration_number;
    private LinkedList <Route> routes;

    public Car(String registration_number){
        this.registration_number=registration_number;
    }

    public void setRegistration_number(String registration_number){
        this.registration_number=registration_number;
    }

    public String getRegistration_number(){
        return this.registration_number;
    }


}
