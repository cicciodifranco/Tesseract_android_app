package core;

import core.ComunicationManager.HTTP.HTTP_Manager;
import core.ComunicationManager.HTTP.MapsComunicationManager;

/**
 * Created by kakashi on 8/24/15.
 */
public class TesseractThread implements Runnable {


    public static final int GET_TOOLBOTHS= 1;

    private TessearctThreadListner listner;
    private MapsComunicationManager mapsComunicationManager;


    public TesseractThread(int action, TessearctThreadListner listner){
        this.listner=listner;
        this.mapsComunicationManager=MapsComunicationManager.getInstance();

    }

    @Override
    public void run() {

    }


    public interface TessearctThreadListner{
        public void onActionCompleted(boolean result, Object data, TesseractThread thread);
    }
}
