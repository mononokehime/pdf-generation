package org.project.company.greenhomes.web.spring.logger;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;
import org.project.company.greenhomes.exception.ExceptionHelper;

import javax.servlet.http.HttpServletRequest;

/**
 * Custom implementation of the {@link SimpleMappingExceptionResolver} to make sure things get
 * logged as they can get lost in the default version. Also allows for greater flexibility later
 * if we want some custom behaviour
 *
 *
 */
public class SpringMVCLogger extends SimpleMappingExceptionResolver {
	/**
	 * the logger
	 */
	private static final Logger log = LoggerFactory.getLogger(SpringMVCLogger.class);

	/*
	 * override default implementation for logging
	 * @see org.springframework.web.servlet.handler.SimpleMappingExceptionResolver#logException(java.lang.Exception, javax.servlet.http.HttpServletRequest)
	 */
	@Override
	protected void logException (Exception ex, HttpServletRequest request) {

		request.setAttribute("exceptionStack", ExceptionHelper.exceptionToString(ex));
		log.error("*********************\nCatching an error in Spring MVC and here are the details:", ex);
		//super.logException(ex, request);
	}

}
