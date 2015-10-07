package core;

import android.util.Log;

import java.net.UnknownServiceException;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.Observable;

import core.CommunicationManager.HTTP.UserCommunicationManager;
import core.Entity.ConcreteEntity.Car;
import core.Entity.ConcreteEntity.Toolboth;
import core.Entity.Interface.Route_Interface;
import core.Entity.Interface.User_Interface;
import core.Entity.ConcreteEntity.Route;
import core.Entity.ConcreteEntity.Transaction;
import core.Entity.ConcreteEntity.User;

/**
 * Created by kakashi on 01/07/15.
 */


public class UserCreator extends Observable implements UserAsyncWorker.AsyncWorkerListener {

    private static User mUser;
    private static UserCreator instance;
    private static PreferenceEditor editor;
    private UserCommunicationManager userCommunicationManager;
    private User_Interface user;
    private static UserAsyncWorker userAsyncWorker;



    private String  TAG = "UserCreator";

    private CreatorListener creatorListener;

    private UserCreator(){
        editor = PreferenceEditor.getInstance();
        //userCommunicationManager  = UserCommunicationManager.getInstance();
        if(editor.isLogged()) {
            userAsyncWorker = new UserAsyncWorker(mUser, this);
            userAsyncWorker.execute(UserAsyncWorker.GET_ALL_INFO);
        }


    }


    public void setCreatorListener(CreatorListener mCreatorListener){
        creatorListener = mCreatorListener;
    }
    public void removeListener(){
        creatorListener= null;
    }
    public static UserCreator getInstance(){
        if(instance==null);
            instance=new UserCreator();
        return instance;
    }

    public static User userFactory(){
        if(mUser==null){
            if(editor.isLogged()){
                mUser = new User(editor.getId(),editor.getEmail(), editor.getName(),
                                editor.getSurname(), editor.getBirthday(),
                                editor.getGender(), editor.getFiscalCode());
                if(PreferenceEditor.getInstance().getSelectedCar()!=null)
                    mUser.setSelectedCar(new Car(PreferenceEditor.getInstance().getSelectedCar()));
            }
        }
        return mUser;

    }
    public static User userFactory(String id, String email, String name, String surname, String birthday, String gender, String fiscalCode){

        mUser= new User(id, email, name, surname, birthday, gender, fiscalCode);
        userAsyncWorker = new UserAsyncWorker(mUser, instance);
        userAsyncWorker.execute(UserAsyncWorker.STORE_USER);

        return mUser;
    }
    public static void storeUser(){
        userAsyncWorker = new UserAsyncWorker(mUser, instance);
        userAsyncWorker.execute(UserAsyncWorker.STORE_USER);
    }

    public void login(String username, String password){
        userAsyncWorker = new UserAsyncWorker(username+";"+password, this);
        userAsyncWorker.execute(UserAsyncWorker.LOGIN);
    }

    public static void register(String email, String name, String surname, String birthday, String gender, String fiscalCode, String password){
        mUser= new User("", email, name, surname, birthday, gender, fiscalCode);
        mUser.setPassword(password);
        userAsyncWorker = new UserAsyncWorker(mUser, instance);
        userAsyncWorker.execute(UserAsyncWorker.REGISTER);

    }
    public static Car carFactory(String registration_number) {
        Car car;
        if (editor.isLogged()) {
            car = new Car(registration_number);
            mUser.addCar(car);
            mUser.setSelectedCar(car);
            PreferenceEditor.getInstance().setSelectedCar(registration_number);
            userAsyncWorker = new UserAsyncWorker(car, instance);
            userAsyncWorker.execute(UserAsyncWorker.STORE_CAR);
            return car;
        }
        else return null;
    }



    public void startRoute(Toolboth toolboth, Car car){

        if(mUser!= null && toolboth!= null && car != null){
            Route route = new Route(0, toolboth, car);
            mUser.addRoute(route);


            userAsyncWorker = new UserAsyncWorker(route, this);
            userAsyncWorker.execute(UserAsyncWorker.START_ROUTE);

        }

    }
    public void endRoute(Toolboth endToolboth){
        if(mUser!=null){
            mUser.getRoutes().getLast().setFinalToolboth(endToolboth);
            if(endToolboth.getId()==mUser.getRoutes().getLast().getStartingToolboth().getId()) {
                Log.i(TAG, "start toolboth is equal than final tollboth ");
                return;
            }
            UserAsyncWorker userAsyncWorker = new UserAsyncWorker(mUser.getRoutes().getLast(), this);
            userAsyncWorker.execute(UserAsyncWorker.END_ROUTE);
        }
    }

    public boolean storeTransaction(Transaction transaction){
        if(mUser!=null){
            if(userCommunicationManager.addTransaction(transaction)) {
                mUser.addTransaction(transaction);
                return true;
            }
        }
        return true;
    }

    public void storeAccessToken(String accessToken){
        Log.i(TAG, accessToken);
        editor.setAccessToken(accessToken);
        if(userCommunicationManager ==null)
            userCommunicationManager = UserCommunicationManager.getInstance();

    }

    public Car searchCar(String registration_number){

        Iterator iterator = mUser.getCars().iterator();
        while(iterator.hasNext()){
            Car car = (Car)iterator.next();
            if(car.getRegistration_number().equals(registration_number))
                return car;
        }
        return null;
    }

    @Override
    public void update() {
        if(creatorListener!= null)
            creatorListener.update();
    }

    @Override
    public void OnActionsFinished(int action, boolean result) {


        if(creatorListener!= null){
            creatorListener.OnActionsFinished(action, result);
            return;
        }
        Log.i(TAG, "listener null");
    }

    public interface CreatorListener{

        void OnActionsFinished(int action, boolean result);
        void update();
    }
}
