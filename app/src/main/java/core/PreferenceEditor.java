package core;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.Preference;
import android.util.Log;

import java.util.Map;
import java.util.Observable;
import java.util.Set;

import javax.sql.StatementEventListener;

/**
 * Created by kakashi on 04/07/15.
 */
class Preferences{

    public static final String FILE = "main_settings";
    public static final String LOGGED = "logged";
    public static final String USER_ID="user_id";
    public static final String IDENTITY_PROVIDER = "identity_provider";
    public static final String TOKEN = "access_token";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String NAME = "name";
    public static final String SURNAME = "surname";
    public static final String BIRTHDAY = "birthday";
    public static final String GENDER = "gender";
    public static final String FISCAL_CODE = "fiscal_code";
    public static final String CAR = "car";
    public static final String ROUTES = "routes";
    public static final String TRANSACTIONS = "transactions";
    public static final String ROUTE_INCOMPLETE = "route_incomplete";
    public static final String SELECTED_CAR = "selected_car";
    public static final String NOTIFICATION_COUNT = "notification_count";
}

public class PreferenceEditor extends Observable {

    private String TAG="Preference editor";
    private Map <String, ?> preferences;
    private static PreferenceEditor instance;
    private SharedPreferences mSharedPreferences;
    private SharedPreferences.Editor editor;
    private String accessToken="";

    private PreferenceEditor(Context context){

        mSharedPreferences = context.getSharedPreferences(Preferences.FILE, context.MODE_PRIVATE);
        preferences = mSharedPreferences.getAll();

    }
    public static void init(Context context){

            instance = new PreferenceEditor(context);
    }

    public static PreferenceEditor getInstance(){
        return instance;
    }

    public Map<String, ?> getAll(){
        preferences = mSharedPreferences.getAll();
        return preferences;
    }


    public void setLogged(boolean logged){
        editor = mSharedPreferences.edit();
        editor.putBoolean(Preferences.LOGGED, logged);
        editor.commit();
    }

    public boolean isLogged(){
        return mSharedPreferences.getBoolean(Preferences.LOGGED, false);
    }

    public void storeId(String id){

        editor=mSharedPreferences.edit();
        editor.putString(Preferences.USER_ID, id);
        editor.commit();
    }

    public String getId(){
        return mSharedPreferences.getString(Preferences.USER_ID, null);

    }
    public void setIdentityProvider(String identityProvider){
        editor=mSharedPreferences.edit();
        editor.putString(Preferences.IDENTITY_PROVIDER, identityProvider);
        editor.commit();
    }

    public void storePassword(String password){
        editor=mSharedPreferences.edit();
        editor.putString(Preferences.PASSWORD, password);
        editor.commit();
    }

    public String getPassword(){
        return  mSharedPreferences.getString(Preferences.PASSWORD, "");
    }

    public String getIdentityProvider(){
        return mSharedPreferences.getString(Preferences.IDENTITY_PROVIDER, null);
    }

    public void setAccessToken(String accessToken){
        this.accessToken=accessToken;
        Log.i(TAG , accessToken);
        editor=mSharedPreferences.edit();
        editor.putString(Preferences.TOKEN, accessToken);
        editor.commit();
    }

    public String getAccessToken(){
        if(this.accessToken.equals(""))
            this.accessToken=mSharedPreferences.getString(Preferences.TOKEN, "");
        Log.i(TAG, accessToken);
        return this.accessToken;
    }

    public void setEmail(String email){
        editor = mSharedPreferences.edit();
        editor.putString(Preferences.EMAIL, email);
        editor.commit();
    }
    public String getEmail(){
        return mSharedPreferences.getString(Preferences.EMAIL, null);
    }

    public void setName(String name){
        editor = mSharedPreferences.edit();
        editor.putString(Preferences.NAME, name);
        editor.commit();
    }

    public String getName(){
        return mSharedPreferences.getString(Preferences.NAME, null);
    }

    public void setSurname(String surname){
        editor=mSharedPreferences.edit();
        editor.putString(Preferences.SURNAME, surname);
        editor.commit();
    }

    public String getSurname(){
        return mSharedPreferences.getString(Preferences.SURNAME, null);
    }

    public void setBirthday(String birthday){
        editor=mSharedPreferences.edit();
        editor.putString(Preferences.BIRTHDAY, birthday);
        editor.commit();
    }
    public String getBirthday(){
        return mSharedPreferences.getString(Preferences.BIRTHDAY, null);
    }

    public void setGender(String gender){
        editor=mSharedPreferences.edit();
        editor.putString(Preferences.GENDER, gender);
        editor.commit();
    }

    public String getGender(){
        return mSharedPreferences.getString(Preferences.GENDER, null);
    }

    public void setFiscalCode(String fiscalCode){
        editor=mSharedPreferences.edit();
        editor.putString(Preferences.FISCAL_CODE, fiscalCode);
        editor.commit();
    }

    public String getFiscalCode(){
        return mSharedPreferences.getString(Preferences.FISCAL_CODE, null);
    }

    public void setCars(String car){
        Set<String> cars = mSharedPreferences.getStringSet(Preferences.CAR, null);

        if(cars!=null && !cars.contains(car)) {
            cars.add(car);
            editor = mSharedPreferences.edit();
            editor.putStringSet(Preferences.CAR, cars);
            editor.commit();
        }

    }

    public Set <String> getCars(){
        return mSharedPreferences.getStringSet(Preferences.CAR, null);
    }

    public void setRoutes(String routes){
        editor=mSharedPreferences.edit();
        editor.putString(Preferences.ROUTES, routes);
        editor.commit();
    }

    public String getRoutes(){
        return mSharedPreferences.getString(Preferences.ROUTES, null);
    }


    public void setTransactions(String transactions){
        editor=mSharedPreferences.edit();
        editor.putString(Preferences.TRANSACTIONS, transactions);
        editor.commit();
    }
    public String getTransactions(){
        return mSharedPreferences.getString(Preferences.TRANSACTIONS, null);
    }

    public boolean getRouteState(){
        return mSharedPreferences.getBoolean(Preferences.ROUTE_INCOMPLETE, false);

    }

    public void setRouteState(boolean state){
        editor = mSharedPreferences.edit();
        editor.putBoolean(Preferences.ROUTE_INCOMPLETE, state);
        editor.commit();
    }

    public String getSelectedCar(){
        return mSharedPreferences.getString(Preferences.SELECTED_CAR, null);
    }

    public void setSelectedCar(String selectedCar){
        editor= mSharedPreferences.edit();
        editor.putString(Preferences.SELECTED_CAR, selectedCar);
        editor.commit();
    }

    public int getNotificationCount(){
        int count = mSharedPreferences.getInt(Preferences.NOTIFICATION_COUNT, 0);
        count++;
        editor=mSharedPreferences.edit();
        editor.putInt(Preferences.NOTIFICATION_COUNT, count);
        editor.commit();
        return count;
    }
}


