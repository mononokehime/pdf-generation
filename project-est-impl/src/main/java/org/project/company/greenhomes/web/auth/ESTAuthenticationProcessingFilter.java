package org.project.company.greenhomes.web.auth;

//public class ESTAuthenticationProcessingFilter extends AuthenticationProcessingFilter{
//	private ReloadableResourceBundleMessageSource messageSource;
//	/**
//	 * the logger
//	 */
//	//private static final Logger log = LoggerFactory.getLogger(ESTAuthenticationProcessingFilter.class);
//	protected void onPreAuthentication(HttpServletRequest request, HttpServletResponse response) throws
//			AuthenticationException,
//	IOException{
//		// don't do anything at moment. Why help them?
//		super.onPreAuthentication(request, response);
//	}
//
//	public void onUnsuccessfulAuthentication(HttpServletRequest request,
//			HttpServletResponse response, AuthenticationException failed) throws IOException{
//		// put in a message
//
//		request.getSession().setAttribute("loginMessage", messageSource.getMessage("invalid.login.credentials", null, null));
//		super.onUnsuccessfulAuthentication(request, response, failed);
//	}
//	public void setMessageSource(ReloadableResourceBundleMessageSource messageSource) {
//		this.messageSource = messageSource;
//	}
//}
