package core.Entity;

import core.Entity.Interface.Vehicle_Interface;

/**
 * Created by francesco on 06/06/15.
 */
public class Vehicle implements Vehicle_Interface {

    private String registration_number;

    public Vehicle(String registration_number){
        this.registration_number=registration_number;
    }

    public void setRegistration_number(String registration_number){
        this.registration_number=registration_number;
    }

    public String getRegistration_number(){
        return this.registration_number;
    }

}
