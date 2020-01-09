package com.swisslog.ep.businessobjects;

import java.io.Serializable;

import org.springframework.hateoas.ResourceSupport;

public class EnterprisePortalScreen extends ResourceSupport implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -292184466587480029L;
	
	String name;
	String menuItemKey;
	String URL;
	
	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	
	public String getMenuItemKey() {
		return menuItemKey;
	}
	public void setMenuItemKey(String menuItemKey) {
		this.menuItemKey = menuItemKey;
	}
	
	public String getUrl() {
		return URL;
	}
	public void setUrl(String URL) {
		this.URL = URL;
	}
	
}
