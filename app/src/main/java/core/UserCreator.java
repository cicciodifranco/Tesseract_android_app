package core;

import java.util.LinkedList;
import java.util.Objects;
import java.util.Observable;

import core.ComunicationManager.HTTP.UserComunicationManager;
import core.Entity.Interface.Route_Interface;
import core.Entity.Interface.User_Interface;
import core.Entity.Route;
import core.Entity.Transaction;
import core.Entity.User;

/**
 * Created by kakashi on 01/07/15.
 */


public class UserCreator extends Observable implements UserThread.UserThreadListner{

    private static User mUser;
    private static UserCreator instance;
    private static PreferenceEditor editor;
    private UserComunicationManager userComunicationManager;
    private User_Interface user;
    private UserThread userThread ;
    private Thread thread;


    private UserCreator(){
        editor = PreferenceEditor.getInstance();
        userComunicationManager  = UserComunicationManager.getInstance();
        userThread = new UserThread(-1, this);
        userThread.setUser(mUser);
        thread = new Thread(userThread);

    }

    public static UserCreator getInstance(){
        if(instance==null);
            instance=new UserCreator();
        return instance;
    }

    public User userFactory(){
        if(mUser==null){
            if(editor.isLogged()){
                mUser = new User(editor.getId(),editor.getEmail(), editor.getName(),
                                editor.getSurname(), editor.getBirthday(),
                                editor.getGender(), editor.getFiscalCode());
            }
        }
        return mUser;

    }
    public User userFactory(int id, String email, String name, String surname, String birthday, String gender, String fiscalCode){

        if(editor.isLogged()){
            userThread.setAction(UserThread.STORE_USER);
            mUser.setId(id);
            mUser.setEmail(email);
            mUser.setName(name);
            mUser.setSurname(surname);
            mUser.setBirthday(birthday);
            mUser.setGender(gender);
            mUser.setFiscalCode(fiscalCode);
            thread.start();
        }
        return mUser;
    }

    public void storeRoute(Route route){
        if(mUser!= null){
            userThread.setAction(UserThread.STORE_ROUTE);
            mUser.addRoute(route);
            thread.start();
        }

    }

    public boolean storeTransaction(Transaction transaction){
        if(mUser!=null){
            if(userComunicationManager.addTransaction(transaction)) {
                mUser.addTransaction(transaction);
                return true;
            }
        }
        return true;
    }

    public void storeAccessToken(String accessToken){
        editor.setAccessToken(accessToken);

    }


    @Override
    public void OnActionFinished(boolean result, Object data, UserThread thread) {
        if(result){
            switch (thread.getActionSelected()){
                case UserThread.GET_USER :
                    if(data!=null)
                        mUser = (User) data;
                    break;
                case UserThread.GET_ROUTE:
                    if(data!=null)
                        mUser.setRoutes((LinkedList<Route_Interface>)data);
                    break;
                default : return;
            }
            setChanged();
            notifyObservers();

        }
    }


}
