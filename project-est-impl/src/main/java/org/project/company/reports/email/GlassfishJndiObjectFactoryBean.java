package org.project.company.reports.email;

import com.sun.enterprise.deployment.MailConfiguration;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jndi.JndiObjectFactoryBean;
import org.springframework.util.Assert;

import javax.naming.NamingException;

/**
 * This class is just a fix for Glassfish.
 * If the application is deployed on a different application server, this class simply has no effect
 * and revert its behaviour back to the class org.springframework.jndi.JndiObjectFactoryBean that
 * is extending.
 * If we define a JavaMail Session on Glassfish (available through JNDI), when the application do
 * the lookup, fetches an object of class com.sun.enterprise.deployment.MailConfiguration instead
 * of a javax.mail.Session. Without this fix, that result in the following stack trace:<br /><br />
 * org.springframework.beans.TypeMismatchException: Failed to convert property value of type [com.sun.enterprise.deployment.MailConfiguration] to required type [javax.mail.Session] for property 'session'; nested exception is java.lang.IllegalArgumentException: Cannot convert value of type [com.sun.enterprise.deployment.MailConfiguration] to required type [javax.mail.Session] for property 'session': no matching editors or conversion strategy found
 *
 * @author lorenzo.ansaloni - 12 Mar 2008
 */
public class GlassfishJndiObjectFactoryBean extends JndiObjectFactoryBean {

	private static final Logger log = LoggerFactory.getLogger(GlassfishJndiObjectFactoryBean.class);

	/**
	 * Overwrite the method of the parent class
	 *
	 * @return a JNDI object
	 */
	protected Object lookup (String jndiName, Class requiredType) throws NamingException {
		Assert.notNull(jndiName, "'jndiName' must not be null");
		String jndiNameToUse = convertJndiName(jndiName);
		Object jndiObject = getJndiTemplate().lookup(jndiNameToUse, requiredType);
		if (jndiObject instanceof MailConfiguration) {
			MailConfiguration mailConfig = (MailConfiguration)jndiObject;
			java.util.Properties props = mailConfig.getMailProperties();
			javax.mail.Session session = javax.mail.Session.getInstance(props);
			jndiObject = session;
		} else {
			log.warn(
					"WARNING: the JNDI object we got back is not an instance of com.sun.enterprise.deployment.MailConfiguration. We probably don't need to use "
							+ getClass().getName() + " anymore");
			log.warn("JNDI object class: " + jndiObject.getClass().getName());
		}
		if (log.isDebugEnabled()) {
			log.debug("Located object with JNDI name [" + jndiNameToUse + "]");
		}
		return jndiObject;
	}

	public void finalize () throws Throwable {
		super.finalize();
	}

}
