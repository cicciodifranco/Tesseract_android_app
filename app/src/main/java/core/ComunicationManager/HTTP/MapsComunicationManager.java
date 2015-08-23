package core.ComunicationManager.HTTP;

import java.util.HashMap;

import core.Entity.Interface.Position_Interface;

/**
 * Created by kakashi on 04/07/15.
 */
public class MapsComunicationManager {

    private HTTP_Manager http_manager;
    private static MapsComunicationManager instance;

    private MapsComunicationManager(){

        http_manager= HTTP_Manager.getInstance();
    }

    public static MapsComunicationManager getInstance(){
        if(instance== null)
            instance= new MapsComunicationManager();
        return instance;
    }


    public String getToolboths(){

        String response = http_manager.doGET(HTTP_Manager.GET_TOOLBOTHS, new HashMap<String, String>());
        if(response.equals("401"))
            return null;
        return response;
    }

    public String getNearestToolboth(Position_Interface position){

        HashMap parameters = new HashMap<String, String>();
        parameters.put("latitude", position.getLatitude());
        parameters.put("longitude", position.getLongitude());
        String response = http_manager.doGET(HTTP_Manager.GET_TOOLBOTHS, parameters);
        if(response.equals("401"))
            return null;
        return response;
    }
}
