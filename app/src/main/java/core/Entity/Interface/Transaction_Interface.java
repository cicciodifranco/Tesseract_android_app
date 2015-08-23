package core.Entity.Interface;

/**
 * Created by kakashi on 01/07/15.
 */
public interface Transaction_Interface {

    public void setUser(User_Interface user);
    public User_Interface getUser();
    public void setRoute(Route_Interface route);
    public Route_Interface getRoute();
    public void setPaymentMethod(String paymentMethod);
    public String getPaymentMethod();
    public void setPrice(float price);
    public float getPrice();


}
