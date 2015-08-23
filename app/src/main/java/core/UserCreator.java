package core;

import java.util.Observable;

import core.ComunicationManager.HTTP.UserComunicationManager;
import core.Entity.Interface.User_Interface;
import core.Entity.Route;
import core.Entity.Transaction;
import core.Entity.User;

/**
 * Created by kakashi on 01/07/15.
 */


public class UserCreator extends Observable{

    private static User mUser;
    private static UserCreator instance;
    private static PreferenceEditor editor;
    private UserComunicationManager userComunicationManager;
    private User_Interface user;


    private UserCreator(){
        editor = PreferenceEditor.getInstance();
        userComunicationManager  = UserComunicationManager.getInstance();

    }

    public static UserCreator getInstance(){
        if(instance==null);
            instance=new UserCreator();
        return instance;
    }

    public User userFactory(){
        if(mUser==null){
            if(editor.isLogged()){
                mUser= new User(editor.getId(),editor.getEmail(), editor.getName(),
                                editor.getSurname(), editor.getBirthday(),
                                editor.getGender(), editor.getFiscalCode());
            }
        }
        return mUser;

    }
    public User userFactory(int id, String email, String name, String surname, String birthday, String gender, String fiscalCode){
        if(editor.isLogged()){
            mUser=new User(id,email,name,surname,birthday,gender,fiscalCode);
            storeUser(mUser);

        }
        return mUser;
    }


    public boolean storeUser(User user){
        if(userComunicationManager.steUserInfo(user)){
            editor.storeId(user.getId());
            editor.setEmail(user.getEmail());
            editor.setName(user.getName());
            editor.setSurname(user.getSurname());
            editor.setBirthday(user.getBirthday());
            editor.setGender(user.getGender());
            editor.setFiscalCode(user.getFiscalCode());

            setChanged();
            notifyObservers();
            return true;
        }

        return false;
    }

    public boolean storeRoute(Route route){
        if(mUser!= null){
            if(userComunicationManager.addRoute(route)){
                //mUser.getCars().
                return true;
            }
        }
        return false;
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

    public boolean storeAccessToken(String accessToken){
        return false;
    }



}
