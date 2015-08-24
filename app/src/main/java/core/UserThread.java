package core;

import android.util.JsonReader;

import org.json.JSONObject;

import java.util.Objects;

import core.ComunicationManager.HTTP.UserComunicationManager;
import core.Entity.Interface.User_Interface;
import core.Entity.User;

/**
 * Created by kakashi on 24/08/15.
 */
public class UserThread implements Runnable {

    public static final int STORE_USER = 0;
    public static final int GET_USER = 1;
    public static final int STORE_ROUTE = 2;
    public static final int GET_ROUTE = 3;
    public static final int STORE_CAR = 4;
    public static final int GET_CARS = 5;
    public static final int STORE_TRANSACTION = 6;
    public static final int GET_TRANSACTIONS = 7;

    private static UserCreator instance;
    private static PreferenceEditor editor;
    private UserComunicationManager userComunicationManager;
    private User mUser;
    private int actionSelected;
    private UserThreadListner listner;

    public UserThread(int action, UserThreadListner listner){
        this.actionSelected=action;
        this.listner=listner;
    }
    public void setUser(User user){
        this.mUser=user;
    }
    public void setAction(int action){
        this.actionSelected=action;
    }
    public int getActionSelected(){
        return this.actionSelected;
    }
    @Override
    public void run() {
        switch (actionSelected){
            case STORE_USER : {
                if(storeUser(mUser))
                    listner.OnActionFinished(true, null, this);
                else listner.OnActionFinished(false, null, this);
            }
            case GET_USER : {
                if(getUser())
                    listner.OnActionFinished(true,mUser,this);
                else listner.OnActionFinished(false,null,this);
            }
            default: return;

        }
    }

    private boolean getUser(){
        String response = userComunicationManager.getUserInfo();
        if(response!=null){
            try {
                JSONObject jsonObject = new JSONObject(response);
                mUser.setId(Integer.parseInt(jsonObject.getString("id")));
                mUser.setEmail(jsonObject.getString("email"));
                mUser.setName(jsonObject.getString("name"));
                mUser.setSurname(jsonObject.getString("surname"));
                mUser.setBirthday(jsonObject.getString("birthday"));
                mUser.setGender(jsonObject.getString("gender"));
                mUser.setFiscalCode(jsonObject.getString("fiscal_code"));
                return true;

            }
            catch(Exception e){
                return false;
            }
        }
        return false;
    }
    private boolean storeUser(User user){

        if(userComunicationManager.storeUserInfo(user)){
            editor.storeId(user.getId());
            editor.setEmail(user.getEmail());
            editor.setName(user.getName());
            editor.setSurname(user.getSurname());
            editor.setBirthday(user.getBirthday());
            editor.setGender(user.getGender());
            editor.setFiscalCode(user.getFiscalCode());
            return true;
        }

        return false;
    }
    public interface UserThreadListner{
        public void OnActionFinished(boolean result, Object data, UserThread thread);

    }
}
