package org.project.company.greenhomes.web.spring.controller;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.project.company.greenhomes.common.enums.BeanNames;
import org.project.company.greenhomes.test.common.spring.BaseSpringTestCase;

import java.io.File;

/**
 * Commented out tests are for future functionality.
 *
 * @author fmacder
 */
public class TemplateControllerTest extends BaseSpringTestCase {

	private MockHttpServletRequest request;
	private MockHttpServletResponse response;
	private TemplateController controller;

	protected String[] getConfigLocations () {
		return new String[] { "classpath:" + APPLICATION_CONTEXT, SPRING_MVC_CONTEXT };
	}

	public void onSetUp () throws Exception {
		controller = (TemplateController)applicationContext.getBean(BeanNames.TEMPLATE_CONTROLLER.getValue());
		request = new MockHttpServletRequest();
		request.setMethod("GET");
		response = new MockHttpServletResponse();
		//deleteTestData();

	}

	@Override
	protected void onTearDown () throws Exception {
		// delete all the data
		//deleteData(getClass().getClassLoader().getResourceAsStream("partial.xml"));	
		//deleteTestData();
	}

	/**
	 * @throws Exception
	 */
	public void testTemplateDisplay () throws Exception {

		String testDir = getProperty("data.output.location") + File.separator;

		String template = "pdfTemplateA";
		request.addParameter("template", template);
		controller.setPdfTemplateDirectory(testDir);
		controller.templateDisplay(response, template);

		String contentType = response.getContentType();
		assertEquals("application/pdf", contentType);
		System.out.println("reponse" + response.getContentLength());

	}
}
