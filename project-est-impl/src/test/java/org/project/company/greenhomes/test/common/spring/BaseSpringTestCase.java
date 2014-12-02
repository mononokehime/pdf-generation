package org.project.company.greenhomes.test.common.spring;

import org.springframework.test.annotation.AbstractAnnotationAwareTransactionalTests;

import java.io.IOException;
import java.util.Properties;

public class BaseSpringTestCase extends AbstractAnnotationAwareTransactionalTests {

	public static final String APPLICATION_CONTEXT = "config/application.context.xml";
	public static final String SPRING_MVC_CONTEXT = "config/springmvc.context.xml";
	private Properties properties;

	public BaseSpringTestCase () {
		//		try{
		//			DBUtil.initaliseMockJNDI();
		//		}
		//		catch(Exception e){
		//			fail("Couldn't initialise mock jndi" + e.getMessage());
		//		}
		setAutowireMode(AUTOWIRE_NO);

	}

	private void readProperties () {
		properties = new Properties();
		try {
			properties.load(BaseSpringTestCase.class.getResourceAsStream("/test.properties"));
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public String getProperty (String key) {
		if (null == properties) {
			readProperties();
		}
		return (String)properties.get(key);
	}
}
