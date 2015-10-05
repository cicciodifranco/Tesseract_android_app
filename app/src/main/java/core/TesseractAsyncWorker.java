package core;

import android.content.Intent;
import android.os.AsyncTask;

import core.CommunicationManager.HTTP.MapsCommunicationManager;

/**
 * Created by kakashi on 10/4/15.
 */
public class TesseractAsyncWorker extends AsyncTask<Integer, Integer, String> {
    private TesseractListener listener;
    private MapsCommunicationManager mapsCommunicationManager;
    private int action;
    public TesseractAsyncWorker (TesseractListener tesseractListener){
        this.listener = tesseractListener;
    }
    @Override
    protected String doInBackground(Integer... params) {
        return getToolboths();
    }

    @Override
    protected void onPostExecute(String result) {
        listener.OnActionFinished(result);
    }


    private String getToolboths(){
        return mapsCommunicationManager.getToolboths();

    }

    public interface TesseractListener{

        void OnActionFinished(String response);
    }
}
