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

import java.io.FileReader;
import java.util.List;

import org.springframework.stereotype.Service;

import com.swisslog.ep.misc.Helper;

import io.kubernetes.client.ApiClient;
import io.kubernetes.client.Configuration;
import io.kubernetes.client.apis.CoreV1Api;
import io.kubernetes.client.models.V1Service;
import io.kubernetes.client.models.V1ServiceList;
import io.kubernetes.client.util.ClientBuilder;
import io.kubernetes.client.util.KubeConfig;

/**
 * A simple example of how to use the Java API from an application outside a kubernetes cluster
 *
 * <p>Easiest way to run this: mvn exec:java
 * -Dexec.mainClass="io.kubernetes.client.examples.KubeConfigFileClientExample"
 *
 * <p>From inside $REPO_DIR/examples
 */
@Service
public class KubeServiceDetector {
	private final boolean inCluster = Helper.inCluster;
	public KubeServiceDetector() {
		System.out.println("KubeServiceDetector started!");
	}
  public List<V1Service> getServices() {
	  	
	  	// file path to your KubeConfig
	    String kubeConfigPath = "C:/Users/g7grobm/.kube/config";
	  	//String kubeConfigPath = "/var/kube/config";
	    String namespace = "sl-application";
	    V1ServiceList serviceList = new V1ServiceList();
	    ApiClient client = new ApiClient();
	    try {
	    	if(inCluster) {
	    		// loading the in-cluster config, including:
	    	    //   1. service-account CA
	    	    //   2. service-account bearer-token
	    	    //   3. service-account namespace
	    	    //   4. master endpoints(ip, port) from pre-set environment variables
	    	    client = ClientBuilder.cluster().build();
	    	}
	    	else {
	    		// loading the out-of-cluster config, a kubeconfig from file-system
			    client = ClientBuilder.kubeconfig(KubeConfig.loadKubeConfig(new FileReader(kubeConfigPath))).build();
	    	}
		    // set the global default api-client to the in-cluster one from above
		    Configuration.setDefaultApiClient(client);
	    }
	    catch(Exception e) {
	    	System.out.println("Exceptionn during startup: "+e);
	    }
	    

	    // the CoreV1Api loads default api-client from global configuration.
	    CoreV1Api api = new CoreV1Api();

	    // invokes the CoreV1Api client
	    try {
	    	serviceList = api.listNamespacedService(namespace, null, null, null, null, null, null, null, null, null);
	    }
	    catch(Exception e) {
	    	System.out.println("Exceptionn during listing: "+e);
	    }
	    
	    return serviceList.getItems();
  }
}