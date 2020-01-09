/*
Copyright 2019 The Kubernetes Authors.
Licensed under the Apache License, Version 2.0 (the "License");
you may not use this file except in compliance with the License.
You may obtain a copy of the License at
    http://www.apache.org/licenses/LICENSE-2.0
Unless required by applicable law or agreed to in writing, software
distributed under the License is distributed on an "AS IS" BASIS,
WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
See the License for the specific language governing permissions and
limitations under the License.
*/

package com.swisslog.ep.service;

import java.net.InetAddress;
import java.net.InetSocketAddress;

import javax.annotation.PostConstruct;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.swisslog.ep.listeners.ServiceListenerImpl;

/**
 * A simple example of how to use the Java API from an application outside a kubernetes cluster
 *
 * <p>Easiest way to run this: mvn exec:java
 * -Dexec.mainClass="io.kubernetes.client.examples.KubeConfigFileClientExample"
 *
 * <p>From inside $REPO_DIR/examples
 */
@Service
public class WebSocketDriver{
	
	@Autowired
	EPWebSocketServer wss;
	
	@Autowired
	ServiceListenerImpl listener;
	
	@Autowired
	EpCollector collector;
	
    public WebSocketDriver() {
    	System.out.println("WSDriver started!");        
    }
    
    @PostConstruct
    public void start() {
    	try {
    		wss.start();
    		wss.addListener(listener);
    		collector.addListener(listener);
    		System.out.println("WSDriver started fully!"); 
    	}
    	catch(Exception e) {
    		System.out.println("wss = "+wss);
    		System.out.println("listener = "+listener);
            System.out.println("collector = "+collector);
    		System.out.println("Exception in driver: "+e);
    	}
    }

}