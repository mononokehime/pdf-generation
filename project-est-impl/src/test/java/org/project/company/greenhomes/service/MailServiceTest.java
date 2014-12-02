package org.project.company.greenhomes.service;

import org.project.company.greenhomes.common.enums.BeanNames;
import org.project.company.greenhomes.domain.entity.UploadSummary;
import org.project.company.greenhomes.test.common.spring.BaseSpringTestCase;

import java.util.Date;

/**
 * @author fmacder
 */
public class MailServiceTest extends BaseSpringTestCase {

	private MailService service;

	protected String[] getConfigLocations () {
		return new String[] { "classpath:" + APPLICATION_CONTEXT };
	}

	public void onSetUp () throws Exception {
		service = (MailService)applicationContext.getBean(BeanNames.MAIL_SERVICE.getValue());

	}

	@Override
	protected void onTearDown () throws Exception {
		// delete all the data
		//	deleteTestData();
	}

	/**
	 * functional mail test
	 */
	public void testSendExcelSuccessEmail () throws Exception {
		UploadSummary summary = new UploadSummary();
		summary.setEndTime(new Date());
		summary.setNumberOfRows(20);
		service.sendLandRegistrySuccessEmail(summary);
	}

}
