package org.project.company.greenhomes.web.auth;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

public class ESTLogoutHandler implements LogoutHandler {

	private static final Logger log = LoggerFactory.getLogger(ESTLogoutHandler.class);

	/**
	 * Over-ride the default behaviour so we can add a message if required when user disables
	 * their own account
	 */
	public void logout (HttpServletRequest arg0, HttpServletResponse arg1,
			Authentication arg2) {
		// this logs them out.
		String message = (String)arg0.getSession().getAttribute("message");
		//log.debug("session message is:"+message);
		arg0.getSession().invalidate();
		//arg0.setAttribute("message", message);
		arg0.getSession().setAttribute("loginMessage", message);
	}

}
