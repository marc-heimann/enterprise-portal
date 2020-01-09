package com.swisslog.ep.service;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Component;

import com.swisslog.ep.listeners.WebSocketListener;
import com.swisslog.ep.misc.Helper;

@Component
@Scope("singleton")
public class EPWebSocketServer extends WebSocketServer{
	private List<WebSocketListener> listeners = new ArrayList<WebSocketListener>();
	private Set<WebSocket> conns = null;
	
	public EPWebSocketServer() {
		super(Helper.getSocketAddress());
		System.out.println("new wss: "+this);
		if(conns == null) {
			conns = new HashSet<>();
		}
		
	}
	
	public void addListener(WebSocketListener toAdd) {
        listeners.add(toAdd);
    }
	
	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		System.out.println("new Client!!!!!");
		conns.add(conn);
		System.out.println("on Socket created for listeners: "+listeners);
		for (WebSocketListener hl : listeners)
            hl.onSocketCreated(conn);
	    System.out.println("client added: "+conn);
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		conns.remove(conn);
		System.out.println("client removed: "+conn+ " code: "+code+" reason: "+reason);
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		System.out.println("message recieved: "+message);
		
	}

	@Override
	public void onError(WebSocket conn, Exception ex) {
		if (conn != null) {
	        conns.remove(conn);
	        // do some thing if required
	    }
	    System.out.println("ERROR from " + conn.getRemoteSocketAddress().getAddress().getHostAddress());
		
	}

	@Override
	public void onStart() {
		// TODO Auto-generated method stub
		
	}
	
	public void broadcast(String message){
		System.out.println("Broadcast to Conns: "+conns);
		  for (WebSocket sock : conns) {
			  System.out.println("Broadcast: "+message+" to client:"+sock);
	          sock.send(message);
	      }
	  }

}
