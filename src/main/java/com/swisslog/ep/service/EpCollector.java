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

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;
import com.swisslog.ep.businessobjects.EnterprisePortalData;
import com.swisslog.ep.listeners.ServiceListener;
import com.swisslog.ep.misc.Helper;

import io.kubernetes.client.models.V1Service;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * A simple example of how to use the Java API from an application outside a kubernetes cluster
 *
 * <p>Easiest way to run this: mvn exec:java
 * -Dexec.mainClass="io.kubernetes.client.examples.KubeConfigFileClientExample"
 *
 * <p>From inside $REPO_DIR/examples
 */
@Service
public class EpCollector implements Runnable{
	private final boolean inCluster = Helper.inCluster;
	private List<ServiceListener> listeners = new ArrayList<ServiceListener>();
	int menuNewHash;
	int menuOldHash;
	String ip = "localhost";
	String port = "80";
	String adress;
	OkHttpClient client = new OkHttpClient();
	
	@Autowired
	EnterprisePortalDataService eds;
	
	@Autowired
	KubeServiceDetector ksd;
	
	public EpCollector(ServiceListener listener) {
		System.out.println("EPCollector started!");
		Thread t = new Thread(this);
		t.start();
		
		addListener(listener);
	}
	public void addListener(ServiceListener toAdd) {
        listeners.add(toAdd);
    }
	
  public Response run(String url) throws IOException {
	  Request request = new Request.Builder()
		      .url(url)
		      .build();

		  try (Response response = client.newCall(request).execute()) {
		    return response;
		  }
		}
  public boolean checkPage(String apiLink) {
	  boolean available = false;
	  try {
		  Response response = run(apiLink);
		  System.out.println("connection in checkpage: "+response+", apilink: "+apiLink);
		  if (response.isSuccessful()) {
		      available = true;
		  }
	  }catch(Exception e){
		  System.out.println("exception: "+e+" , while checking: "+apiLink);
		  return false;
	  }
	  return available;
  }
  
  public String getScreenFile(String wellKnownLink) {
	  try {
		  HttpURLConnection connection = (HttpURLConnection) new URL(wellKnownLink).openConnection();
		  connection.setRequestMethod("GET");
		  connection.setConnectTimeout(5000);
		  String jsonReply;
		  if(connection.getResponseCode()==201 || connection.getResponseCode()==200)
		      {
		          InputStream response = connection.getInputStream();
		          jsonReply = convertStreamToString(response);
		          return jsonReply;
		      }
	  }
	  catch(Exception e){
		  System.out.println("exception: "+e+" , while checking: "+wellKnownLink);
		  return null;
	  }
	  return null;
  }
  
  public ArrayList<ArrayList<String>> getWellKnowns(List<V1Service> serviceList) {
	  List<String> instances = new ArrayList<String>();
	  String ServiceName = "example";
	  String wellKnown ;
	  String wellKnownFolder = "/.well-known/enterprise-portal-screens";
	  ArrayList<ArrayList<String>> wellKnowns = new ArrayList<ArrayList<String>>();
	  for(V1Service activeService : serviceList) {
		  boolean instanceDuplication = false;
		  //System.out.println("activeService: "+activeService);
		  String instance = activeService.getSpec().getSelector().get("app.kubernetes.io/instance");
		  System.out.println("instances = "+instances);
		  if(!instances.isEmpty()){
			  if(!ContainsAllNulls(instances)) {
				  for(String savedInstance : instances){
					  if(savedInstance.equals(instance)){
						  instanceDuplication = true;
						  break;
					  }
				  }
			  }
		  }
		  
		  if(instanceDuplication == false){
			  instances.add(instance);
			  try {
				  if(inCluster) {
					  ip = activeService.getMetadata().getName();
					  ip += ".";
					  ip += activeService.getMetadata().getNamespace();
					  port = activeService.getSpec().getPorts().get(0).getPort().toString();
					  ServiceName = activeService.getMetadata().getName();
					  System.out.println(ip + ":" + port + " ("+ServiceName+")");
					  adress = "http://" + ip;
				  }
				  else {
					  ip = "10.49.145.110";
					  port = activeService.getSpec().getPorts().get(0).getNodePort().toString();
					  ServiceName = activeService.getMetadata().getName();
					  System.out.println(ip + ":" + port + " ("+ServiceName+")");
					  adress = "http://" + ip + ":" + port;
				  }
				  
				  
				  wellKnown = adress + wellKnownFolder;
				  if(checkPage(wellKnown)) {
					  System.out.println("EPS.json: "+wellKnown);
					  ArrayList<String> data = new ArrayList<>();
					  data.add(adress);
					  data.add(wellKnown);
					  wellKnowns.add(data);
				  }
				  else {
					  System.out.println("No EPScreen for Service: "+ServiceName+" found!");
				  }
			  }
			  catch(Exception e) {
				  System.out.println("epcollector, while checking wellknowns, Exception: "+e);
			  }
			}
	  }
	  System.out.println("wellKnowns: "+wellKnowns+ ", instances: "+instances);
	  return wellKnowns;
  }
  
  public ArrayList<ArrayList<String>> getEPScreens(ArrayList<ArrayList<String>> wellKnowns) {
	  ArrayList<ArrayList<String>> menus = new ArrayList<ArrayList<String>>();
	  Gson g = new Gson();
	  for(int i = 0; i < wellKnowns.size(); i++) {
		  try {
			  ArrayList<String> menu = new ArrayList<String>();
			  System.out.println("wellKnowns: "+wellKnowns);
			  //todo check page
			  if(checkPage(wellKnowns.get(i).get(1))) {
				  String responseString = getScreenFile(wellKnowns.get(i).get(1));
				  String dataAdress = "http://ui.swisslog.net";
				  System.out.println("responseString: "+responseString);
				  EnterprisePortalData result = null;
				  try {
					  result = eds.addEnterprisePortalData(g.fromJson(responseString, EnterprisePortalData.class));
				  }catch(Exception e) {
					  System.out.println("Exception: "+e+", while trying gson");
				  }
				  if(result != null && result.getType().equals("enterprise-portal")) {
					  System.out.println("result: "+result);
					  menu.add("<button class=\"collapsible\">"+result.getMenuItemKey()+"</button>");
					  for(int j = 0; j < result.getScreens().length; j++) {
						  System.out.println("screens: "+result.getScreens());
						  String url = dataAdress + result.getScreens()[j].getUrl();
						  menu.add("<a href=\""+url+"\" target=\"contentFrame\" onclick=\"linkClicked(event);return false;\">"+result.getScreens()[j].getMenuItemKey()+"</a>");
					  }
					  menus.add(menu);
					  System.out.println("menu: "+menu);
				  }
				  
			  }
		  }
		  catch(Exception e) {
			  System.out.println("Exception: "+e+", while trying checkWellKnowns");
		  }
	  }
	  
	  if(menus.size() > 0) {
		  for(int i = 0; i < menus.size();i++) {
			  menuNewHash = String.join(", ", menus.get(i)).hashCode();
		  }
	  }
	  else {
		  menuNewHash = 0;
	  }
	  
	  System.out.println("newhash: "+menuNewHash+", oldhash: "+menuOldHash);
	  if(menuOldHash != menuNewHash){
		  menuOldHash = menuNewHash;
		  String send = " ";
		  if(menuNewHash != 0) {
			  send = g.toJson(menus);
		  }
		  for (ServiceListener hl : listeners)
	            hl.newServices(send);
	  }
	  return menus;
  }
  
  public Boolean ContainsAllNulls(List<String> instances)
  {
      if(instances != null)
      {
          for(String a : instances)
              if(a != null) return false;
      }

      return true;
  }
  
  private String convertStreamToString(InputStream is) {

	  BufferedReader reader = new BufferedReader(new InputStreamReader(is));
	  StringBuilder sb = new StringBuilder();

	  String line = null;
	  try {
	      while ((line = reader.readLine()) != null) {
	          sb.append(line + "\n");
	      }
	  } catch (IOException e) {
	      e.printStackTrace();
	  } finally {
	      try {
	          is.close();
	      } catch (IOException e) {
	          e.printStackTrace();
	      }
	  }
	  return sb.toString();
  }

  @Override
  public void run() {
	  
	  while(true) {
		  try
		  {
		      Thread.sleep(10000);
		  }
		  catch(InterruptedException ex)
		  {
			  System.out.println("exception: "+ex+" while sleep");
			  Thread.currentThread().interrupt();
		  }
		  List <V1Service> services= ksd.getServices();
		  ArrayList<ArrayList<String>> wellKnowns = getWellKnowns(services);
		  ArrayList<ArrayList<String>> ePScreens = getEPScreens(wellKnowns);
		  System.out.println("EPscreens: "+ePScreens);
		  String secs = "time:" + System.currentTimeMillis();
		  for (ServiceListener hl : listeners)
	            hl.broadcast(secs);
	  }
  }
  
}