package core;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import core.Entity.ConcreteEntity.Toolboth;

/**
 * Created by kakashi on 04/07/15.
 */
public class TesseractCreator  implements TesseractAsyncWorker.TesseractListener{

    private static TesseractCreator instance;
    private Map<String, Toolboth> toolbothMap;
    private static TesseractAsyncWorker tesseractAsyncWorker;
    private Toolboth toolboth [] = new Toolboth[10];
    private TesseractCreatorListener tesseractCreatorListener;




    private TesseractCreator(){

        toolbothMap = new HashMap<>();
    }

    public static TesseractCreator getInstance(){
        if(instance==null) {
            instance = new TesseractCreator();

        }
        return instance;
    }
    public static void fetchToolboth(){
        tesseractAsyncWorker = new TesseractAsyncWorker(instance);
        tesseractAsyncWorker.execute();
    }
    public void setTesseractCreatorListener(TesseractCreatorListener listener){
        this.tesseractCreatorListener = listener;
    }
    public Toolboth getToolboth(String name){

        return toolbothMap.get(name);
    }

    public Toolboth getToolboth(int id){

        return toolboth[id];
    }

    public Map<String, Toolboth> getToolbothMap(){
        if(toolbothMap == null)
            fetchToolboth();
        return toolbothMap;
    }




    @Override
    public synchronized void OnActionFinished(String response) {
        if(response!=null)
            try {
                toolbothMap= new HashMap<>();

                JSONArray jsonArray = new JSONArray(response);
                for(int i=0; i<jsonArray.length(); i++){
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    int id=jsonObject.getInt("toolboth_id");
                    String name=jsonObject.getString("name"),
                            city = jsonObject.getString("city");
                    Double latitude = jsonObject.getDouble("latitude")
                            , longitude = jsonObject.getDouble("longitude");
                    Toolboth t = new Toolboth(id, name, city, latitude, longitude);
                    toolbothMap.put(name, t);
                    toolboth[t.getId()]=t;

                }
                if(tesseractCreatorListener!=null)
                    tesseractCreatorListener.OnActionFinished();

            } catch (JSONException e) {
                e.printStackTrace();
            }
    }


    public interface TesseractCreatorListener{
        void OnActionFinished();
    }
}
