package core.Entity;

import core.Entity.Interface.Route_Interface;
import core.Entity.Interface.Transaction_Interface;
import core.Entity.Interface.User_Interface;

/**
 * Created by kakashi on 01/07/15.
 */
public class Transaction implements Transaction_Interface{

    private User user;
    private Route route;
    private String paymentMethod;
    private float price;

    public Transaction(){

    }
    @Override
    public void setUser(User_Interface user) {
        this.user=(User)user;
    }

    @Override
    public User_Interface getUser() {
        return this.user;
    }

    @Override
    public void setRoute(Route_Interface route) {

        this.route= (Route)route;
    }

    @Override
    public Route_Interface getRoute() {
        return this.route;
    }

    @Override
    public void setPaymentMethod(String paymentMethod) {
        this.paymentMethod=paymentMethod;
    }

    @Override
    public String getPaymentMethod() {
        return this.paymentMethod;
    }

    @Override
    public void setPrice(float price) {
        this.price=price;
    }

    @Override
    public float getPrice() {
        return this.price;
    }
}
