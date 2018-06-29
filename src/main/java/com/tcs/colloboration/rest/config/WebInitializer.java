package com.tcs.colloboration.rest.config;

import java.nio.charset.StandardCharsets;

import javax.servlet.Filter;
import javax.servlet.ServletRegistration;

import org.springframework.web.filter.CharacterEncodingFilter;
import org.springframework.web.servlet.support.AbstractAnnotationConfigDispatcherServletInitializer;

import com.tcs.colloboration.config.ApplicationContextConfig;

public class WebInitializer extends AbstractAnnotationConfigDispatcherServletInitializer {
	
	protected void customizeRegistration(ServletRegistration.Dynamic registration) 
	{
		System.out.println("customizeRegistration");
		registration.setInitParameter("dispatchOptionsRequest", "true");
		registration.setAsyncSupported(true);
	}


	protected Class<?>[] getRootConfigClasses() 
	{	
		return new Class[] {WebResolver.class,ApplicationContextConfig.class};
	}

	
	protected Class<?>[] getServletConfigClasses() {
		// TODO Auto-generated method stub
		return null;
	}

	
	protected String[] getServletMappings() 
	{
		return new String[] {"/"};
	}
	
	
	protected Filter[] getServletFilters()
	{
		CharacterEncodingFilter encodingFilter=new CharacterEncodingFilter();
		encodingFilter.setEncoding(StandardCharsets.UTF_8.name());
		return new Filter[] {encodingFilter};
	}
}
