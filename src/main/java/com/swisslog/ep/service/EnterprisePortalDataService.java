package com.swisslog.ep.service;

import java.util.Collection;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.stereotype.Service;

import com.swisslog.ep.businessobjects.EnterprisePortalData;

@Service
public class EnterprisePortalDataService{

	ConcurrentHashMap<String, EnterprisePortalData> epds = null;
	
	public EnterprisePortalDataService() {
		epds = new ConcurrentHashMap<>();
	}
	
	
	
	public Collection<EnterprisePortalData> allEnterprisePortalData() {
		return epds.values();
	}

	public EnterprisePortalData addEnterprisePortalData(EnterprisePortalData epd) {
		if (epds.put(epd.getMenuItemKey(), epd) != null)
		{
			return epd;
		}
		else 
		{
			return null;
		}
	}

	
	
}
