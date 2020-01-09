package com.swisslog.ep.controllers;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.hateoas.Link;
import org.springframework.hateoas.Resources;
import org.springframework.hateoas.config.EnableHypermediaSupport;
import org.springframework.hateoas.config.EnableHypermediaSupport.HypermediaType;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.swisslog.ep.businessobjects.EnterprisePortalData;
import com.swisslog.ep.service.EnterprisePortalDataService;

import io.swagger.annotations.Api;

@Api
@RestController
@RequestMapping(value = "/v1")
@EnableHypermediaSupport(type = HypermediaType.HAL)
public class EnterprisePortalDataController {

	@Autowired
	EnterprisePortalDataService epDataService;

	@GetMapping(value = "/epds", produces = {"application/hal+json"})
	public Resources<EnterprisePortalData> getEnterprisePortalData() {		
			
		final Collection<EnterprisePortalData> epds = epDataService.allEnterprisePortalData();
		    
		for (final EnterprisePortalData epd : epds) {
			Link selfLink = linkTo(methodOn(EnterprisePortalDataController.class).getEnterprisePortalData()).withSelfRel();
			if (!epd.hasLink(selfLink.getRel()))
				epd.add(selfLink);
		}
			  
		Link link = linkTo(methodOn(EnterprisePortalDataController.class).getEnterprisePortalData()).withSelfRel();
		return new Resources<>(epds, link);
	}
}
