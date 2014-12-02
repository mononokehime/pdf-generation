package org.project.company.greenhomes.web.spring.controller;

import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.mock.web.MockHttpServletResponse;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.mock.web.MockMultipartHttpServletRequest;
import org.springframework.test.annotation.ExpectedException;
import org.springframework.ui.ModelMap;
import org.springframework.web.servlet.ModelAndView;
import org.project.company.greenhomes.common.enums.BeanNames;
import org.project.company.greenhomes.domain.entity.UploadSummary;
import org.project.company.greenhomes.test.common.SqlPlusRunner;
import org.project.company.greenhomes.test.common.spring.BaseSpringTestCase;

import java.io.FileInputStream;
import java.util.List;

/**
 * Commented out tests are for future functionality.
 *
 * @author fmacder
 */
public class FileUploadControllerTest extends BaseSpringTestCase {

	private MockHttpServletRequest request;

	private FileUploadController controller;

	protected String[] getConfigLocations () {
		return new String[] { "classpath:" + APPLICATION_CONTEXT, SPRING_MVC_CONTEXT };
	}

	public void onSetUp () throws Exception {
		controller = (FileUploadController)applicationContext.getBean(BeanNames.FILE_UPLOAD_CONTROLLER.getValue());
		request = new MockHttpServletRequest();
		//	request.setMethod("POST");

		//deleteTestData();
		insertTestData();
	}

	@Override
	protected void onTearDown () throws Exception {
		// delete all the data
		//deleteData(getClass().getClassLoader().getResourceAsStream("partial.xml"));	
		deleteTestData();
	}

	/**
	 * Tests user can't re-post upload of file from land registry
	 *
	 * @throws Exception
	 */
	public void testFileUploadTimeWrong () throws Exception {
		//getClass().getClassLoader().get
		// get the test directory so we can copy the file
		String testDir = getProperty("data.output.location");
		String time = System.currentTimeMillis() + "";
		final FileInputStream fis = new FileInputStream(testDir + "/landregistry/new-land-reg-extract.txt");
		//final InputStream fis = getClass().getClassLoader().getResourceAsStream("land-registry.xls");
		// Mock request, response and MultipartFile
		MockMultipartHttpServletRequest request = new MockMultipartHttpServletRequest();
		request.setMethod("POST");
		MockMultipartFile f = new MockMultipartFile("doesntmatter.txt", "new-land-reg-extract.txt", "text/plain", fis);
		//		// Set some request Params and add MultipartFile to request
		request.addFile(f);
		request.setContentType("multipart/form-data");
		request.addHeader("Content-type", "multipart/form-data");

		request.getSession().setAttribute("time", time);
		time = (System.currentTimeMillis() + 500) + "";
		MockHttpServletResponse response = new MockHttpServletResponse();
		ModelAndView mav = controller.uploadLandRegistry(request, response, f, time);

		// now try again - session object should have been removed
		mav = controller.uploadLandRegistry(request, response, f, time);
		assertNotNull(mav);
		ModelMap map = mav.getModelMap();
		assertNotNull(map);
		String message = (String)map.get("message");
		assertNotNull(message);
		String messageVal = "The time value sent appears to be out of date. Please navigate back to this" +
				" page and start again.";
		System.out.println("message" + message);
		assertEquals(messageVal, message);

		assertNotNull(mav.getViewName());

		assertEquals("/file-admin/land-registry-upload", mav.getViewName());
	}

	/**
	 * Tests for get method
	 *
	 * @throws Exception
	 */
	public void testIndexPage () throws Exception {
		ModelMap map = controller.indexHandler();
		assertNotNull(map);

		List<UploadSummary> excelResults = (List<UploadSummary>)map.get("excelResults");
		assertNotNull(excelResults);
		assertEquals(15, excelResults.size());
		System.out.println("size:" + excelResults.size());
		List<UploadSummary> epcResults = (List<UploadSummary>)map.get("epcResults");
		assertNotNull(epcResults);
		assertEquals(8, epcResults.size());
	}

	/**
	 * Tests for get method
	 *
	 * @throws Exception
	 */
	public void testUploadResultsErrors () throws Exception {

		//	final FileInputStream fis = new FileInputStream("C:/dev/workspace/GreenHomes/docs/land-registry/land-registry.xls");
		// Mock request, response and MultipartFile
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");
		MockMultipartFile multipartFile = null;
		//		// Set some request Params and add MultipartFile to request

		ModelAndView mav = controller.uploadLandRegistry(request.getSession());
		assertNotNull(mav);
		assertNotNull(mav.getViewName());
		assertEquals("/file-admin/land-registry-upload", mav.getViewName());
		assertNotNull(request.getSession().getAttribute("time"));
	}

	/**
	 * Tests user can't re-post upload of file from land registry
	 *
	 * @throws Exception
	 */
	public void testFileUploadRepost () throws Exception {

		// get the test directory so we can copy the file
		String testDir = getProperty("data.output.location");
		final FileInputStream fis = new FileInputStream(testDir + "/landregistry/new-land-reg-extract.txt");

		// Mock request, response and MultipartFile
		MockMultipartHttpServletRequest request = new MockMultipartHttpServletRequest();
		request.setMethod("POST");
		MockMultipartFile f = new MockMultipartFile("doesntmatter.txt", "new-land-reg-extract.txt", "text/plain", fis);
		//		// Set some request Params and add MultipartFile to request
		request.addFile(f);
		request.setContentType("multipart/form-data");
		request.addHeader("Content-type", "multipart/form-data");
		String time = System.currentTimeMillis() + "";
		request.getSession().setAttribute("time", time);
		MockHttpServletResponse response = new MockHttpServletResponse();
		ModelAndView mav = controller.uploadLandRegistry(request, response, f, time);

		// now try again - session object should have been removed
		mav = controller.uploadLandRegistry(request, response, f, time);
		assertNotNull(mav);
		ModelMap map = mav.getModelMap();
		assertNotNull(map);
		String message = (String)map.get("message");
		assertNotNull(message);
		String messageVal =
				"It appears that you have re-posted the same data or have used the back button. Please navigate back to this"
						+
						" page and start again.";
		assertEquals(messageVal, message);
		//System.out.println("message"+message);		
		assertNotNull(mav.getViewName());
		System.out.println("mav.getViewName()" + mav.getViewName());
		assertEquals("/file-admin/land-registry-upload", mav.getViewName());
	}

	/**
	 * Tests for successful upload of excel from land registry
	 *
	 * @throws Exception
	 */
	public void testFileUpload () throws Exception {

		// get the test directory so we can copy the file
		String testDir = getProperty("data.output.location");
		final FileInputStream fis = new FileInputStream(testDir + "/landregistry/new-land-reg-extract.txt");
		MockMultipartHttpServletRequest request = new MockMultipartHttpServletRequest();
		request.setMethod("POST");
		MockMultipartFile f = new MockMultipartFile("doesntmatter.txt", "new-land-reg-extract.txt", "text/plain", fis);
		//		// Set some request Params and add MultipartFile to request
		request.addFile(f);
		request.setContentType("multipart/form-data");
		request.addHeader("Content-type", "multipart/form-data");
		String time = System.currentTimeMillis() + "";
		request.getSession().setAttribute("time", time);
		MockHttpServletResponse response = new MockHttpServletResponse();
		ModelAndView mav = controller.uploadLandRegistry(request, response, f, time);
		assertNotNull(mav);
		ModelMap map = mav.getModelMap();
		assertNotNull(map);
		String message = (String)map.get("message");
		assertNotNull(message);
		System.out.println("message" + message);
		assertNotNull(mav.getViewName());
		System.out.println("mav.getViewName()" + mav.getViewName());
		assertEquals("redirect:/file-admin/land-registry-upload-success.do", mav.getViewName());
	}

	/**
	 * Tests for successful upload of excel from land registry
	 *
	 * @throws Exception
	 */
	public void testFileUploadBadFormat () throws Exception {

		// get the test directory so we can copy the file
		String testDir = getProperty("data.output.location");
		final FileInputStream fis = new FileInputStream(testDir + "/landregistry/original-land-reg-extract.xls");
		// Mock request, response and MultipartFile
		MockMultipartHttpServletRequest request = new MockMultipartHttpServletRequest();
		request.setMethod("POST");
		MockMultipartFile f = new MockMultipartFile("doesntmatter.xls", "doesntmatter.xls", "text/plain", fis);
		//		// Set some request Params and add MultipartFile to request
		request.addFile(f);
		request.setContentType("multipart/form-data");
		request.addHeader("Content-type", "multipart/form-data");
		String time = System.currentTimeMillis() + "";
		request.getSession().setAttribute("time", time);
		MockHttpServletResponse response = new MockHttpServletResponse();
		ModelAndView mav = controller.uploadLandRegistry(request, response, f, time);

		assertNotNull(mav);
		assertNotNull(mav.getViewName());
		assertEquals("/file-admin/land-registry-upload", mav.getViewName());
		ModelMap map = mav.getModelMap();
		assertNotNull(map);
		String summary = (String)map.get("message");
		assertNotNull(summary);
	}

	/**
	 * Tests for failure upload of excel from land registry when null
	 *
	 * @throws Exception
	 */
	@ExpectedException(IllegalArgumentException.class)
	public void testFileUploadNull () throws Exception {

		//	final FileInputStream fis = new FileInputStream("C:/dev/workspace/GreenHomes/docs/land-registry/land-registry.xls");
		// Mock request, response and MultipartFile
		MockMultipartHttpServletRequest request = new MockMultipartHttpServletRequest();
		request.setMethod("POST");
		MockMultipartFile multipartFile = null;
		//		// Set some request Params and add MultipartFile to request
		request.addFile(multipartFile);
		request.setContentType("multipart/form-data");
		request.addHeader("Content-type", "multipart/form-data");
		String time = System.currentTimeMillis() + "";
		request.getSession().setAttribute("time", time);
		MockHttpServletResponse response = new MockHttpServletResponse();
		ModelAndView mav = controller.uploadLandRegistry(request, response, null, time);
		assertNotNull(mav);
		assertNotNull(mav.getViewName());
		assertEquals("/file-admin/land-registry-upload", mav.getViewName());
	}

	/**
	 * Tests for get method
	 *
	 * @throws Exception
	 */
	public void testFileUploadGet () throws Exception {

		//	final FileInputStream fis = new FileInputStream("C:/dev/workspace/GreenHomes/docs/land-registry/land-registry.xls");
		// Mock request, response and MultipartFile
		MockHttpServletRequest request = new MockHttpServletRequest();
		request.setMethod("GET");
		MockMultipartFile multipartFile = null;
		//		// Set some request Params and add MultipartFile to request

		ModelAndView mav = controller.uploadLandRegistry(request.getSession());
		assertNotNull(mav);
		assertNotNull(mav.getViewName());
		assertEquals("/file-admin/land-registry-upload", mav.getViewName());
		assertNotNull(request.getSession().getAttribute("time"));
	}

	private void insertTestData () {

		SqlPlusRunner.runSQLPlusFile("truncate.sql");
		SqlPlusRunner.runSQLPlusFile("user.sql");
		SqlPlusRunner.runSQLPlusFile("upload_summary.sql");
		SqlPlusRunner.runSQLPlusFile("property_address.sql");
		SqlPlusRunner.runSQLPlusFile("property_address_attr.sql");
		SqlPlusRunner.runSQLPlusFile("property_epc.sql");
		SqlPlusRunner.runSQLPlusFile("property_sale.sql");
		SqlPlusRunner.runSQLPlusFile("dev_application_properties.SQL");
	}

	private void deleteTestData () {
		SqlPlusRunner.runSQLPlusFile("truncate.sql");
	}
}
