package core.Services;

import android.app.Service;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import com.pubnub.api.Callback;
import com.pubnub.api.Pubnub;
import com.pubnub.api.PubnubError;
import com.tesseract.tesseract.Main.Mediator;


import java.util.Map;

import java.util.StringTokenizer;

import core.Entity.ConcreteEntity.Toolboth;
import core.PreferenceEditor;
import core.TesseractCreator;

public class Communication_service extends Service {
    private static final String TAG = "Communication service";
    private final IBinder mBinder = new LocalBinder();
    private Pubnub pubnub;
    private PreferenceEditor preferenceEditor;
    private Mediator mediator;

    public Communication_service() {


    }

    @Override
    public IBinder onBind(Intent intent) {

        if(preferenceEditor==null)
            preferenceEditor=PreferenceEditor.getInstance();

        if(mediator==null)
            mediator=Mediator.getInstance();

        if(pubnub == null && preferenceEditor!=null)
            pubnub = new Pubnub("Tesseract" , "sub-c-4b1096ca-6cea-11e5-b4b4-0619f8945a4f");

        if(preferenceEditor.getId()!=null)
            try {
                pubnub.subscribe(""+preferenceEditor.getId(), new Callback() {

                            @Override
                            public void connectCallback(String channel, Object message) {
                                Log.i("pubnub", "connect: " + message.toString());
                            }

                            @Override
                            public void disconnectCallback(String channel, Object message) {
                                Log.i("pubnub", "disconnect: " + message.toString());
                            }

                            public void reconnectCallback(String channel, Object message) {
                                Log.i("pubnub", "reconnect: " + message.toString());
                            }

                            @Override
                            public void successCallback(String channel, Object message) {
                                //message arrived from server

                                try {
                                    StringTokenizer stringTokenizer = new StringTokenizer((String )message, ";");
                                    String title  = stringTokenizer.nextToken();
                                    String strMsg = stringTokenizer.nextToken();

                                    Map<String, Toolboth> map = TesseractCreator.getInstance().getToolbothMap();
                                    if (map != null) {
                                        for (String key : map.keySet()) {
                                            if (strMsg.equals("" + map.get(key).getId())) {
                                                strMsg = "Your car have just crossed the gate of " + map.get(key).getName();
                                                break;
                                            }
                                        }
                                        mediator.messageArrived(title, strMsg);
                                    }
                                }
                                catch (Exception e){
                                    Log.e(TAG, "error while parsing message");
                                }

                                Log.i("pubnub", "success: " + message.toString());
                            }

                            @Override
                            public void errorCallback(String channel, PubnubError error) {
                                Log.i("pubnub", "error: " + error.toString());
                            }
                        }
                );
            }catch(Exception e){

            }




        return mBinder;
    }

    public class LocalBinder extends Binder {

        public Communication_service getService() {

            return Communication_service.this;
        }
    }
}
