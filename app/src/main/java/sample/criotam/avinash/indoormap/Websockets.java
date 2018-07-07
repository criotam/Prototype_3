package sample.criotam.avinash.indoormap;

import android.content.Context;
import android.os.Build;
import android.util.Log;
import android.widget.Toast;

import org.java_websocket.client.WebSocketClient;
import org.java_websocket.drafts.Draft_17;
import org.java_websocket.handshake.ServerHandshake;

import java.net.URI;
import java.net.URISyntaxException;

public class Websockets {

    private WebSocketClient mWebSocketClient;

    Context context;

    public Websockets(Context context){
        this.context = context;
    }

    public void connectWebSocket() {

        URI uri;
        try {
            uri = new URI("ws://"+"192.168.1.4"+":8080/BotControllerGateway/bot_gateway");
        } catch (URISyntaxException e) {
            e.printStackTrace();
            return;
        }

        mWebSocketClient = new WebSocketClient(uri, new Draft_17()) {
            @Override
            public void onOpen(ServerHandshake serverHandshake) {
                Toast.makeText(context, "Connected",Toast.LENGTH_LONG).show();
                Log.d("Websocket", "Opened");
                mWebSocketClient.send("admin");
            }

            @Override
            public void onMessage(String s) {
                final String message = s;
                Log.d("Message", ""+message);
            }

            @Override
            public void onClose(int i, String s, boolean b) {
                Log.i("Websocket", "Closed " + s);
                mWebSocketClient = null;
                Toast.makeText(context, "DisConnected",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onError(Exception e) {
                Log.i("Websocket", "Error " + e.getMessage());
            }
        };
        mWebSocketClient.connect();
    }


    public WebSocketClient getWebsocketClient(){
        if(mWebSocketClient!=null){
            return mWebSocketClient;
        }

        return null;
    }

}
