package org.project.company.greenhomes.common.util;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.security.authentication.ProviderManager;
import org.project.company.greenhomes.test.common.spring.BaseSpringTestCase;
import org.project.company.greenhomes.web.spring.controller.UserManagementController;

public class WebApplicationContextHolderTest extends BaseSpringTestCase {

	private MockHttpServletRequest request;
	private MockHttpServletResponse response;
	private UserManagementController controller;

	protected String[] getConfigLocations () {
		return new String[] { "classpath:" + APPLICATION_CONTEXT, SPRING_MVC_CONTEXT };
	}

	public void onSetUp () throws Exception {

	}

	public void testFindAuthenticationManager () {

		String[] name = applicationContext.getBeanDefinitionNames();
		for (int i = 0; i < name.length; i++) {
			System.out.println("name:" + name[i]);
		}
		ProviderManager authenticationManager = (ProviderManager)applicationContext.getBean("_authenticationManager");
		assertNotNull(authenticationManager);
	}

	@Override
	protected void onTearDown () throws Exception {
		// delete all the data
		//deleteData(getClass().getClassLoader().getResourceAsStream("partial.xml"));	
	}

}
