package core.CommunicationManager.WebSocket;

/**
 * Created by kakashi on 8/3/15.
 */

import android.util.Log;

import com.pubnub.api.*;

import java.util.Observable;

public class PubNub extends Observable {

    Pubnub pubnub = new Pubnub("demo", "sub-c-1c18a78e-ef4c-11e4-831e-0619f8945a4f");

    public PubNub(String channel){
        try {
            pubnub.subscribe(channel, new Callback() {

                        @Override
                        public void connectCallback(String channel, Object message) {
                            System.out.println("SUBSCRIBE : CONNECT on channel:" + channel
                                    + " : " + message.getClass() + " : "
                                    + message.toString());

                            Log.i("pubnub", "connect: "+message.toString());

                        }

                        @Override
                        public void disconnectCallback(String channel, Object message) {
                            System.out.println("SUBSCRIBE : DISCONNECT on channel:" + channel
                                    + " : " + message.getClass() + " : "
                                    + message.toString());

                            Log.i("pubnub", "disconnect: " + message.toString());

                        }

                        public void reconnectCallback(String channel, Object message) {
                            System.out.println("SUBSCRIBE : RECONNECT on channel:" + channel
                                    + " : " + message.getClass() + " : "
                                    + message.toString());

                            Log.i("pubnub", "reconnect: " + message.toString());
                        }

                        @Override
                        public void successCallback(String channel, Object message) {
                            System.out.println("SUBSCRIBE : " + channel + " : "
                                    + message.getClass() + " : " + message.toString());
                            Log.i("pubnub", "success: " + message.toString());
                        }

                        @Override
                        public void errorCallback(String channel, PubnubError error) {
                            System.out.println("SUBSCRIBE : ERROR on channel " + channel
                                    + " : " + error.toString());

                            Log.i("pubnub", "error: " + error.toString());
                        }
                    }
            );
        }catch(Exception e){

        }
    }
}
