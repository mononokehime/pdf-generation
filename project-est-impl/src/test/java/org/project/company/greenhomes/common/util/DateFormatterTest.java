package org.project.company.greenhomes.common.util;

import junit.framework.TestCase;
import org.springframework.test.annotation.ExpectedException;

import java.text.ParseException;
import java.util.Date;

public class DateFormatterTest extends TestCase {

	public void onSetUp () throws Exception {
		//	controller = (ExcelPropertySalesReader)applicationContext.getBean(BeanNames.EXCEL_PROPERTY_SALES_READER.getValue());

	}

	@ExpectedException(ParseException.class)
	public void testGetFormattedDateFromLandRegNull () {
		String dateStr = "sdfsd";
		try {
			Date date = DateFormatter.getFormattedDateFromLandReg(dateStr);
			//System.out.println("date:"+date);
			//assertNotNull(date);
			//assertEquals("Tue Jul 15 12:00:00 BST 2008", date.toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void testGetFormattedDateFromLandMark () {
		String dateStr = "01-DEC-08 09.00";
		try {
			Date date = DateFormatter.getFormattedDateFromLandMark(dateStr);
			//System.out.println("date:"+date);
			assertNotNull(date);
			assertEquals("Mon Dec 01 00:00:00 GMT 2008", date.toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void testGetFormattedDateForSchedule () {
		String dateStr = "06-02-2009";
		try {
			Date date = DateFormatter.getFormattedDate(dateStr);
			//System.out.println("date:"+date);
			assertNotNull(date);
			assertEquals("Fri Feb 06 00:00:00 GMT 2009", date.toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void testGetFormattedDateFromLandReg () {
		String dateStr = "2008-07-15 13:33:00";
		try {
			Date date = DateFormatter.getFormattedDateFromLandReg(dateStr);
			//System.out.println("date:"+date);
			assertNotNull(date);
			assertEquals("Tue Jul 15 12:00:00 BST 2008", date.toString());
		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
