/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package criotam;

import java.io.IOException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;

/**
 *
 * @author AVINASH
 */
@ServerEndpoint("/bot_gateway")
public class BotGateway {

    private static Map<String, Session> map = new ConcurrentHashMap<>();
    
    @OnMessage
    public String onMessage(String message, Session session) throws IOException {
        
        System.out.println("message:" + message);
        
        if(message.contains("admin")){
            map.put("admin", session);
        }else if(message.contains("fork_lift")){
            map.put("fork_lift", session);
        }else{        
            if(session == map.get("admin")){
                System.out.println("admin:" + message);
                if(map.get("fork_lift")!=null){
                    if(map.get("fork_lift").isOpen()){
                        map.get("fork_lift").getBasicRemote().sendText(message);
                    }
                }
            }else if(session == map.get("fork_lift")){
                System.out.println("fork_lift:"+ message);
                if(map.get("admin")!=null){
                    if(map.get("admin").isOpen()){
                        map.get("admin").getBasicRemote().sendText(message);
                    }
                }
            }
        }
        
        return null;
    }

    @OnOpen
    public void onOpen(Session session) {
    }

    @OnError
    public void onError(Throwable t) {
    }

    @OnClose
    public void onClose() {
    }
    
}
