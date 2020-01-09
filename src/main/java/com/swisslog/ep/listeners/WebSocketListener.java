package com.swisslog.ep.listeners;

import org.java_websocket.WebSocket;

//An interface to be implemented by everyone
//interested in "Hello" events
public interface WebSocketListener {
 void onSocketCreated(WebSocket socket);
}
