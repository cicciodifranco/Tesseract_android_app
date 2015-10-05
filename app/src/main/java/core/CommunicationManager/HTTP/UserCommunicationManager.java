package core.CommunicationManager.HTTP;

import com.google.android.gms.nearby.messages.Strategy;

import java.util.HashMap;
import core.Entity.ConcreteEntity.Car;
import core.Entity.ConcreteEntity.Route;
import core.Entity.ConcreteEntity.Transaction;
import core.Entity.ConcreteEntity.User;
import core.PreferenceEditor;

 /**
 * Created by kakashi on 01/07/15.
 */
public class UserCommunicationManager {

    private HTTP_Manager httpManager;
    private PreferenceEditor editor;
    private static UserCommunicationManager instance;

    private UserCommunicationManager(){
        httpManager= HTTP_Manager.getInstance();
        httpManager.init(PreferenceEditor.getInstance().getAccessToken(), "facebook");
        editor= PreferenceEditor.getInstance();

    }
    public static UserCommunicationManager getInstance(){
        if(instance==null)
            instance=new UserCommunicationManager();
        return instance;
    }
    public String getUserInfo(){

        HashMap parameters = new HashMap<String, String>();
        parameters.put("id", editor.getId());
        String response = httpManager.doGET(HTTP_Manager.GET_USER_INFO, parameters);
        if(response.equals("401"))
            return null;
        return response;
    }
    public boolean storeUserInfo(User user){
        HashMap parameters = new HashMap<String, String>();
        parameters.put("id",""+user.getId());
        parameters.put("email", user.getEmail());
        parameters.put("name", user.getName());
        parameters.put("surname", user.getSurname());
        parameters.put("birthday", user.getBirthday());
        parameters.put("gender", user.getGender());
        parameters.put("fiscal_code", user.getFiscalCode());

        String response = httpManager.doGET(HTTP_Manager.SET_USER_INFO, parameters);
        if(response.equals("401"))
            return false;
        else return true;
    }

     public boolean register(User user){
         HashMap parameters = new HashMap<String, String>();
         parameters.put("name", user.getName());
         parameters.put("surname", user.getSurname());
         parameters.put("email", user.getEmail());
         parameters.put("password", user.getPassword());

         String response = httpManager.doGET(HTTP_Manager.REGISTER, parameters);
         if(response.equals("401"))
             return false;
         else return true;


     }

     public boolean storeCar(Car car){
         HashMap parameters = new HashMap<String, String>();
         parameters.put("registration_number", car.getRegistration_number());
         String response=httpManager.doGET(HTTP_Manager.ADD_CAR, parameters);
         if(response.equals("401"))
             return false;
         return true;
     }

     public String getCars(){
         String response=httpManager.doGET(HTTP_Manager.ADD_CAR, new HashMap<String, String>());
         if(response.equals("401"))
             return null;
         return response;
     }

     public boolean storeRoute(Route route){
        HashMap parameters = new HashMap<String, String>();
        parameters.put("start_toolboth", route.getStartingToolboth().getId());
        parameters.put("end_toolboth", route.getFinalToolboth().getId());
        parameters.put("registration_number", route.getCar().getRegistration_number());

         /*for(int i=0; i<route.getCheckPoints().length;i++){
            parameters.put("checkpoint"+i, route.getCheckPoints()[i]);
        }
        */
        String response = httpManager.doGET(HTTP_Manager.ADD_ROUTE, parameters);

        if(response.equals("401"))
            return false;
        return true;
    }

    public String startRoute(Route route){
        HashMap parameters = new HashMap<String, String>();
        parameters.put("start_toolboth", route.getStartingToolboth().getId());
        parameters.put("registration_number", route.getCar().getRegistration_number());

        String response = httpManager.doGET(HTTP_Manager.START_ROUTE, parameters);
        if(response.equals("401"))
            return null;
        else return response;

    }
    public String endRoute(Route route){
        HashMap parameters = new HashMap<String, String>();
        parameters.put("end_toolboth", route.getFinalToolboth().getId());
        parameters.put("id_route", route.getId());
        String response = httpManager.doGET(HTTP_Manager.END_ROUTE, parameters);
        if(response.equals("401"))
            return null;
        else return response;
    }

    public String getRoutes(){
        String response = httpManager.doGET(HTTP_Manager.GET_ROUTES, new HashMap<String, String>());
        if(response.equals("401"))
            return null;
        return response;
    }

    public boolean addTransaction(Transaction transaction){
        HashMap parameters = new HashMap<String, String>();
        parameters.put("route_id", ""+transaction.getRoute().getId());
        parameters.put("price", ""+transaction.getPrice());
        parameters.put("payment_method", transaction.getPaymentMethod());
        String response = httpManager.doGET(HTTP_Manager.ADD_TRANSACTION, parameters);
        if(response.equals("401"))
            return false;
        return true;
    }

    public String getTransactions(){

        String response=httpManager.doGET(HTTP_Manager.GET_TRANSACTIONS, new HashMap<String, String>());
        if(response.equals("401"))
            return null;
        return response;
    }
}
