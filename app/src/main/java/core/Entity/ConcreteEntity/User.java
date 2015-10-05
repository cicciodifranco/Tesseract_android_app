package core.Entity.ConcreteEntity;

import java.util.LinkedList;


import core.Entity.Interface.Car_Interface;
import core.Entity.Interface.Route_Interface;
import core.Entity.Interface.Transaction_Interface;
import core.Entity.Interface.User_Interface;

/**
 * Created by francesco on 06/06/15.
 */
public class User implements User_Interface{
    private int id;
    private String email, name, surname, gender, birthday, fiscalCode;
    private Car selectedCar;
    private LinkedList<Car_Interface> cars;
    private LinkedList<Transaction_Interface> transactions;
    private LinkedList<Route_Interface> routes;
    private String password;

    public User(int id, String email, String name, String surname, String birthday, String gender, String fiscalCode){
        this.id=id;
        this.email=email;
        this.name=name;
        this.surname=surname;
        this.birthday=birthday;
        this.gender=gender;
        this.fiscalCode=fiscalCode;


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
    public void setEmail(String email){
        this.email=email;
    }
    @Override
    public String getEmail(){
        return this.email;
    }

    @Override
    public String getPassword() {
        return  this.password;
    }

    @Override
    public void setPassword(String password) {
        this.password=password;
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
    public void setBirthday(String birthday){
        this.birthday=birthday;
    }

    @Override
    public String getBirthday(){
        return this.birthday;
    }

    @Override
    public void setGender(String gender){

            this.gender=gender;
    }
    @Override
    public String getGender(){
        return this.gender;
    }

    @Override
    public void setFiscalCode(String fiscalCode){
        this.fiscalCode=fiscalCode;
    }
    @Override
    public String getFiscalCode(){
        return this.fiscalCode;
    }

    public void setRoutes(LinkedList<Route_Interface> routes){
        this.routes=routes;
    }
    public void addRoute(Route route){
        if(this.routes==null)
            routes=new LinkedList<Route_Interface>();
        routes.addLast(route);
    }
    public LinkedList<Route_Interface> getRoutes(){
        return this.routes;
    }

    public void setSelectedCar(Car car){
        this.selectedCar=car;
    }

    public Car_Interface getSelectedCar(){
        return this.selectedCar;
    }

    @Override
    public void setCars(LinkedList<Car_Interface> cars){
        this.cars=cars;
    }

    @Override
    public LinkedList<Car_Interface> getCars(){
        return this.cars;
    }

    @Override
    public void addCar(Car car){
        if(cars==null)
            cars = new LinkedList<Car_Interface>();
        this.cars.addLast(car);
    }

    @Override
    public void removeCar (Car car){
        if(this.cars !=null)
            this.cars.remove(car);
    }

    public void setTransactions(LinkedList<Transaction_Interface> transactions){
        this.transactions=transactions;
    }
    public LinkedList<Transaction_Interface> getTransactions(){
        return this.transactions;
    }
    public void addTransaction(Transaction_Interface transaction){
        if(this.transactions==null)
            this.transactions=new LinkedList<Transaction_Interface>();
        this.transactions.addLast(transaction);
        transaction.setUser(this);
    }
    public void removeTransaction(Transaction_Interface transaction){
        if(this.transactions!=null)
            this.transactions.remove(transaction);

    }

}