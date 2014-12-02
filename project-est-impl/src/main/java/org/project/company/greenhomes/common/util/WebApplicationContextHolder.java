package org.project.company.greenhomes.common.util;

import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.security.authentication.AuthenticationManager;
import org.project.company.greenhomes.common.enums.BeanNames;
import org.project.company.greenhomes.common.readers.LandMarkReader;
import org.project.company.greenhomes.common.readers.LandRegistryReader;
import org.project.company.greenhomes.service.UserService;
import org.project.company.greenhomes.service.pdf.PdfHandler;

/**
 * Allows access to Spring enabled beans without the class itself being
 * Spring enabled
 * Consequently, initialize this bean with the applicationContext and make it
 * static.
 *
 * @author fmacdermot
 * @since 29-Oct-2008
 */
public class WebApplicationContextHolder implements ApplicationContextAware {
	/**
	 * application context reference
	 */
	private static ApplicationContext myApplicationContext;

	/**
	 * Getter for the ApplicationContext.
	 *
	 * @return The web application context previously set by
	 * {@link #setApplicationContext(ApplicationContext)}
	 */
	public static ApplicationContext getApplicationContext () {
		return myApplicationContext;
	}

	/**
	 * Setter for the ApplicationContext. Called by Spring.
	 */
	public void setApplicationContext (ApplicationContext applicationContext) {
		myApplicationContext = applicationContext;
	}

	/**
	 * Returns the user service from the application context
	 */
	public static UserService getUserService () {
		return (UserService)myApplicationContext.getBean(BeanNames.USER_SERVICE.getValue());
	}

	/**
	 * Returns the land mark reader from the application context
	 */
	public static LandMarkReader getLandMarkReader () {
		return (LandMarkReader)myApplicationContext.getBean(BeanNames.LAND_MARK_READER.getValue());
	}

	/**
	 * Returns the land registry reader from the application context
	 */
	public static LandRegistryReader getLandRegistryReader () {
		return (LandRegistryReader)myApplicationContext.getBean(BeanNames.LAND_REGISTRY_READER.getValue());
	}

	/**
	 * Returns the pdf writer from the application context
	 */
	public static PdfHandler getPdfLetterBuilder () {
		return (PdfHandler)myApplicationContext.getBean(BeanNames.PDF_LETTER_BUILDER.getValue());
	}

	public static AuthenticationManager getAuthenticationManager () {
		return (AuthenticationManager)myApplicationContext.getBean("_" + BeanNames.AUTHENTICATION_MANAGER.getValue());
	}
	//	public static TokenBasedRememberMeServices getRememberMeService(){
	//		return (TokenBasedRememberMeServices)myApplicationContext.getBean(BeanNames.REMEMBERME_SERVICE.getValue());
	//	}

}
