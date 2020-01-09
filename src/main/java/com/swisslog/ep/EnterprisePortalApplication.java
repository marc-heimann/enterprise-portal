package com.swisslog.ep;

import java.lang.Thread.UncaughtExceptionHandler;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;

@SpringBootApplication(exclude = { SecurityAutoConfiguration.class })
public class EnterprisePortalApplication implements UncaughtExceptionHandler{
	
	public EnterprisePortalApplication() {
		Thread.currentThread().setUncaughtExceptionHandler(this);
	}
	
	public static void main(String[] args) {
		SpringApplication.run(EnterprisePortalApplication.class, args);
	}

	@Override
	public void uncaughtException(Thread arg0, Throwable arg1) {
		System.out.println(arg1);
		arg1.printStackTrace();
		
	}

}
