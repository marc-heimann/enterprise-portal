package com.swisslog.ep.businessobjects;

import java.io.Serializable;

import org.springframework.hateoas.ResourceSupport;

public class EnterprisePortalData extends ResourceSupport implements Serializable {
	
	/**
	 * 
	 */
	private static final long serialVersionUID = -292184466587480029L;
	
	String type;
	String menuItemKey;
	EnterprisePortalScreen[] screens;
	
	public String getType() {
		return type;
	}
	public void setType(String type) {
		this.type = type;
	}
	
	public String getMenuItemKey() {
		return menuItemKey;
	}
	public void setMenuItemKey(String menuItemKey) {
		this.menuItemKey = menuItemKey;
	}
	
	public EnterprisePortalScreen[] getScreens() {
		return screens;
	}
	public void setScreens(EnterprisePortalScreen[] screen) {
		if (screen == null)
			screens = null;		
		this.screens = screen;
	}
}
