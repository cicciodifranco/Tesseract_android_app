package core.ComunicationManager.HTTP;

import java.util.HashMap;
import core.Entity.Car;
import core.Entity.Route;
import core.Entity.Transaction;
import core.Entity.User;
import core.PreferenceEditor;

 /**
 * Created by kakashi on 01/07/15.
 */
public class UserComunicationManager {

    private HTTP_Manager httpManager;
    private PreferenceEditor editor;
    private static UserComunicationManager instance;

    private UserComunicationManager(){
        httpManager= HTTP_Manager.getInstance();
        editor= PreferenceEditor.getInstance();

    }
    public static UserComunicationManager getInstance(){
        if(instance==null)
            instance=new UserComunicationManager();
        return instance;
    }
    public String getUserInfo(){

        HashMap parameters = new HashMap<String, String>();
        parameters.put("email", editor.getEmail());
        String response = httpManager.doGET(HTTP_Manager.GET_USER_INFO, parameters);
        if(response.equals("401"))
            return null;
        return response;
    }
    public boolean steUserInfo(User user){
        HashMap parameters = new HashMap<String, String>();
        parameters.put("email", user.getEmail());
        parameters.put("name", user.getName());
        parameters.put("surname", user.getSurname());
        parameters.put("birthday", user.getBirthday());
        parameters.put("gender", user.getGender());
        parameters.put("fiscal_code", user.getFiscalCode());

        String response = httpManager.doPOST(HTTP_Manager.SET_USER_INFO, parameters);
        if(response.equals("200"))
            return true;
        else return false;
    }
     public boolean addCar(Car car){
         HashMap parameters = new HashMap<String, String>();
         parameters.put("registration_number", car.getRegistration_number());
         String response=httpManager.doPOST(HTTP_Manager.ADD_CAR, parameters);
         if(response.equals("200"))
             return true;
         return false;
     }

     public String getCars(){
         String response=httpManager.doPOST(HTTP_Manager.ADD_CAR, new HashMap<String, String>());
         if(response.equals("401"))
             return null;
         return response;
     }

     public boolean addRoute(Route route){
        HashMap parameters = new HashMap<String, String>();
        parameters.put("email", editor.getEmail());
        parameters.put("start", route.getStartingToolboth().getId());
        parameters.put("end", route.getFinalToolboth().getId());
        for(int i=0; i<route.getCheckPoints().length;i++){
            parameters.put("checkpoint"+i, route.getCheckPoints()[i]);
        }
        String response = httpManager.doPOST(HTTP_Manager.ADD_ROUTE, parameters);

        if(response.equals("200"))
            return true;
        return false;
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
        String response = httpManager.doPOST(HTTP_Manager.ADD_TRANSACTION, parameters);
        if(response.equals("200"))
            return true;
        return false;
    }

    public String getTransactions(){

        String response=httpManager.doGET(HTTP_Manager.GET_TRANSACTIONS, new HashMap<String, String>());
        if(response.equals("401"))
            return null;
        return response;
    }
























}
