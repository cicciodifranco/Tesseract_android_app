package core;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.LinkedList;

import core.CommunicationManager.HTTP.UserCommunicationManager;
import core.Entity.ConcreteEntity.Car;
import core.Entity.ConcreteEntity.Route;
import core.Entity.ConcreteEntity.Transaction;
import core.Entity.ConcreteEntity.User;
import core.Entity.Interface.Car_Interface;


/**
 * Created by kakashi on 9/25/15.
 */
public class UserAsyncWorker extends AsyncTask <Integer, Integer, Integer> {

    public static final int STORE_USER = 0;
    public static final int GET_USER = 1;
    public static final int START_ROUTE = 2;
    public static final int END_ROUTE = 3;
    public static final int GET_ROUTES = 4;
    public static final int STORE_CAR = 5;
    public static final int GET_CARS = 6;
    public static final int STORE_TRANSACTION = 7;
    public static final int GET_TRANSACTIONS = 8;
    public static final int GET_ALL_INFO = 9;
    public static final int REGISTER = 10;
    private Object data;
    private PreferenceEditor editor = PreferenceEditor.getInstance();
    private UserCommunicationManager userCommunicationManager = UserCommunicationManager.getInstance();
    private AsyncWorkerListener listener;
    private  int action;

    public UserAsyncWorker(Object data, AsyncWorkerListener listener) {

        this.listener = listener;
        this.data = data;

    }
    public void setEntity(Object data){
        this.data=data;
    }
    @Override
    protected Integer doInBackground(Integer... action) {
        this.action=action[0];
        switch (action[0]) {

            case STORE_USER: {
                if (data instanceof User) {
                    if (storeUser((User) data))
                        return 0;
                }
                break;
            }

            case GET_USER: {
                if (data instanceof User) {
                    if (getUser((User) data))
                        return 0;
                }
                break;
            }

            case START_ROUTE: {
                if (data instanceof Route) {
                    if (startRoute((Route) data))
                        return 0;
                }

                break;
            }

            case END_ROUTE: {
                if (data instanceof Route) {
                    if (endRoute((Route) data))
                        return 0;
                }
                break;
            }

            case GET_ROUTES: {
                if (data instanceof User) {
                    if (getRoutes((User) data))
                        return 0;
                }
                break;
            }

            case STORE_CAR: {
                if (data instanceof Car) {
                    if (storeCar((Car) data))
                        return 0;
                }
                break;
            }

            case GET_CARS: {
                if (data instanceof User) {
                    if (getCars((User) data))
                        return 0;
                }
                break;
            }

            case STORE_TRANSACTION: {
                if (data instanceof Transaction) {
                    if (storeTransactions((Transaction) data))
                        return 0;
                }
                break;
            }

            case GET_TRANSACTIONS: {
                if (data instanceof User) {
                    if (getTransactions((User) data))
                        return 0;
                }
                break;
            }

            case GET_ALL_INFO: {
                if (data instanceof User) {
                    if (getAllInfo((User) data))
                        return 0;
                }
                break;
            }

            case REGISTER: {
                if(data instanceof User)
                    if(register((User)data))
                        return 0;
                break;
            }

            default:
                break;

        }
        return 1;
    }

    @Override
    protected void onPostExecute(Integer result) {
        if(result==0)
            listener.OnActionsFinished(action, true);
        else
            listener.OnActionsFinished(action, false);
    }

    private synchronized boolean getUser(User mUser){
        String response = userCommunicationManager.getUserInfo();
        if(response!=null){
            try {
                String id, email, name, surname, birthday, gender, fiscal_code;
                JSONObject jsonObject = new JSONObject(response);

                //trying to get all attributes
                id=jsonObject.getString("id");
                email=jsonObject.getString("email");
                name=jsonObject.getString("name");
                surname=jsonObject.getString("surname");
                birthday=jsonObject.getString("birthday");
                gender=jsonObject.getString("gender");
                fiscal_code=jsonObject.getString("fiscal_code");

                //setting attributes
                mUser.setId(Integer.parseInt(id));
                mUser.setEmail(email);
                mUser.setName(name);
                mUser.setSurname(surname);
                mUser.setBirthday(birthday);
                mUser.setGender(gender);
                mUser.setFiscalCode(fiscal_code);
                return true;

            }
            catch(Exception e){
                return false;
            }
        }
        return false;
    }
    private synchronized boolean storeUser(User user){

        editor.storeId(user.getId());
        editor.setEmail(user.getEmail());
        editor.setName(user.getName());
        editor.setSurname(user.getSurname());
        editor.setBirthday(user.getBirthday());
        editor.setGender(user.getGender());
        editor.setFiscalCode(user.getFiscalCode());

        if(userCommunicationManager.storeUserInfo(user)){

            return true;
        }

        return false;
    }


    private synchronized boolean startRoute(Route route){
        String response = userCommunicationManager.startRoute(route);
        if(response!=null){
            try {
                JSONObject jsonObject = new JSONObject(response);
                route.setId(jsonObject.getInt("id_route"));
                return true;

            } catch (JSONException e) {
                e.printStackTrace();

            }

        }
        return false;
    }

    private synchronized boolean endRoute(Route route){

        String response = userCommunicationManager.endRoute(route);
        if(response!= null)
            return true;
        return false;
    }

    private synchronized boolean getRoutes(User mUser){

        String response = userCommunicationManager.getRoutes();
        if(response!=null){
            try {
                JSONArray jsonArray = new JSONArray(response);
                for(int i=0; i<jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int id = jsonObject.getInt("id_route");
                    int start_toolboth_id = jsonObject.getInt("start_toolboth_id");
                    int end_toolboth_id = jsonObject.getInt("end_toolboth_id");
                    String registration_number = jsonObject.getString("registration_number");
                    String date = jsonObject.getString("date");
                    Car car = UserCreator.getInstance().searchCar(registration_number);
                    if (car != null) {
                        mUser.addRoute(new Route(id, TesseractCreator.getInstance().getToolboth(start_toolboth_id), TesseractCreator.getInstance().getToolboth(end_toolboth_id), car));
                        return true;
                    }
                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return false;
    }

    private synchronized boolean storeCar(Car car){
        if(userCommunicationManager.storeCar(car)){
            editor.setCars(car.getRegistration_number());
            return true;
        }
        return false;
    }

    private synchronized boolean getCars(User mUser){
        String response = userCommunicationManager.getCars();
        if(response!=null){
            try {
                JSONArray json = new JSONArray(response);

                if(json.length()>0) {
                    LinkedList<Car_Interface> cars =  new LinkedList<>();
                    for (int i = 0; i < json.length(); i++) {
                        cars.add(new Car(json.getJSONObject(i).getString("registration_number")));
                    }
                    mUser.setCars(cars);
                    return true;
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return false;
    }

    public synchronized boolean storeTransactions(Transaction transaction){
        return false;
    }

    public synchronized boolean getTransactions(User mUser){
        return false;
    }

    private synchronized  boolean getAllInfo(User mUser){

        return false;
    }

    public boolean register(User user){
        if(userCommunicationManager.register(user))
            return true;
        return false;
    }


    public interface AsyncWorkerListener{

        void update();
        void OnActionsFinished(int action, boolean result);
    }
}
