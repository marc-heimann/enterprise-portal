package com.swisslog.ep.listeners;

//An interface to be implemented by everyone
//interested in "Hello" events
public interface ServiceListener {
 void newServices(String send);
 void broadcast(String send);
}
