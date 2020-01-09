package com.swisslog.ep.listeners;

import org.java_websocket.WebSocket;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.swisslog.ep.service.EPWebSocketServer;

@Service
public class ServiceListenerImpl implements ServiceListener, WebSocketListener{
	
	private String menu = "";
	
	@Autowired
	EPWebSocketServer wss;
	
	@Override
	public void newServices(String send) {
		System.out.println("Recieved Event: "+send);
		menu = send;
		wss.broadcast(menu);
	}
	
	@Override
	public void broadcast(String send) {
		wss.broadcast(send);
	}
	
	public String getMenu() {
		return menu;
	}

	public void setMenu(String menu) {
		this.menu = menu;
	}


	@Override
	public void onSocketCreated(WebSocket socket) {
		System.out.println("Send menu: "+menu+" to new socket: "+socket);
		socket.send(menu);		
	}
}
