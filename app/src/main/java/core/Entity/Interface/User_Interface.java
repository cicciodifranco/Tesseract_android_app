package core.Entity.Interface;

import java.util.LinkedList;
import java.util.List;

import core.Entity.Car;
import core.Entity.Transaction;

/**
 * Created by francesco on 06/06/15.
 */
public interface User_Interface {

    public void setEmail(String email);
    public String getEmail();

    public void setName(String name);
    public String getName();

    public void setSurname(String surname);
    public String getSurname();

    public void setBirthday(String birthday);
    public String getBirthday();

    public void setGender(String gender);
    public String getGender();

    public void setFiscalCode(String fiscalCode);
    public String getFiscalCode();

    public void setCars(LinkedList<Car_Interface> cars);
    public LinkedList<Car_Interface> getCars();
    public void addCar(Car car);
    public void removeCar (Car car);

    public void setTransactions(LinkedList<Transaction_Interface> transactions);
    public LinkedList<Transaction_Interface> getTransactions();
    public void addTransaction(Transaction_Interface transaction);
    public void removeTransaction(Transaction_Interface transaction);
}
