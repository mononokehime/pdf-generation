package org.project.company.greenhomes.test.common;

import com.Ostermiller.util.CSVPrinter;
import org.apache.commons.math.random.RandomData;
import org.apache.commons.math.random.RandomDataImpl;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.project.company.greenhomes.common.enums.BeanNames;
import org.project.company.greenhomes.common.enums.PropertyAttributeNames;
import org.project.company.greenhomes.common.enums.WorkFlowStatus;
import org.project.company.greenhomes.common.util.DateFormatter;
import org.project.company.greenhomes.domain.entity.Measure;
import org.project.company.greenhomes.domain.entity.PropertyAddress;
import org.project.company.greenhomes.domain.entity.PropertyAttribute;
import org.project.company.greenhomes.service.PropertyService;
import org.project.company.greenhomes.test.common.db.DBUtil;
import org.project.company.greenhomes.test.common.spring.BaseSpringTestCase;

import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;
import java.util.*;

/**
 * Class to generate test data for land mark
 * Goes to the database and pulls out all the received records, that are property sales
 * Then iterates through and generate csv data based on those values. Random generators are used for the
 * various ratings and dates.
 *
 *
 */
public class GenerateLandmarkCSVFiles {

	private static List<Measure> measures;

	private static String getProperty (String key) throws IOException {

		Properties testProps = new Properties();
		testProps.load(GenerateLandmarkCSVFiles.class.getResourceAsStream("/test.properties"));
		return (String)testProps.get(key);
	}

	/**
	 * Run this class to generate the test data.
	 *
	 * @param args
	 * @throws IOException
	 * @throws ParseException
	 */
	public static void main (String[] args) throws IOException, ParseException {
		String testDir = getProperty("data.output.location");

		System.out.println("testDir:" + testDir);
		String fileName = testDir + "/landmark/" + System.currentTimeMillis() + "" +
				"EPC-Data_Batch5.csv";
		FileWriter fout = new FileWriter(fileName);
		String rFileName = testDir + "/landmark/" + System.currentTimeMillis() + "" +
				"EPC-Recommendations_Batch5.csv";
		CSVPrinter cSVPrinter = new CSVPrinter(fout);
		cSVPrinter.setAutoFlush(true);
		FileWriter rfout = new FileWriter(rFileName);
		CSVPrinter rCSVPrinter = new CSVPrinter(rfout);
		rCSVPrinter.setAutoFlush(true);
		String[] values = new String[9];
		values[0] = "RRN";
		values[1] = "Sequence";
		values[2] = "Improvement-Category";
		values[3] = "Improvement-Summary";
		values[4] = "Improvement-Heading";
		values[5] = "Improvement-Description";
		values[6] = "Typical-Saving";
		values[7] = "Energy-Performance-Rating";
		values[8] = "Environmental-Impact-Rating";
		rCSVPrinter.writeln(values);
		try {
			DBUtil.initaliseMockJNDI();
		} catch (Exception e) {
			e.printStackTrace();
		}
		// setAutowireMode(AUTOWIRE_NO);
		// create a new file input stream with the input file specified
		ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(BaseSpringTestCase.APPLICATION_CONTEXT);
		// get the list of valid sales
		//PropertyService propertyService = (PropertyService)ctx.getBean(BeanNames.PROPERTY_SALES_SERVICE.getValue());		

		PropertyService propertyService = (PropertyService)ctx.getBean(BeanNames.PROPERTY_SALES_SERVICE.getValue());
		// generate some epc results - create the criteria for finding address sales
		Map<String, Object> holder = new HashMap<String, Object>();
		holder.put("workFlowStatus", WorkFlowStatus.RECEIVED.getValue());
		List<PropertyAddress> results = propertyService.findByCriteria(holder);
		System.out.println("generate landmark data for:" + results.size() + " records");
		// now for each one create an epc
		String[] theValues = new String[22];
		theValues[0] = "RRN";
		theValues[1] = "INSPECTION_DATE";
		theValues[2] = "ADDR_LINE_1";
		theValues[3] = "ADDR_LINE_2";
		theValues[4] = "ADDR_LINE_3";
		theValues[5] = "POST_TOWN";
		theValues[6] = "POST_CODE";
		// generate current energy rating
		theValues[7] = "ENERGYRATINGCURRENT";
		// generate potential
		theValues[8] = "ENERGYRATINGPOTENTIAL";
		// env impact can ignore
		theValues[9] = "ENVIRONMENTALIMPACTCURRENT";
		theValues[10] = "ENVIRONMENTALIMPACTPOTENTIAL";
		// energy consumption can ignore
		theValues[11] = "ENERGYCONSUMPTIONCURRENT";
		theValues[12] = "ENERGYCONSUMPTIONPOTENTIAL";
		// co2 emissions
		theValues[13] = "CO2EMISSIONSCURRENT";
		theValues[14] = "CO2EMISSIONSPOTENTIAL";
		// floor area
		theValues[15] = "TOTALFLOORAREA";
		// lighting values
		theValues[16] = "LIGHTINGCOSTCURRENT";
		theValues[17] = "LIGHTINGCOSTPOTENTIAL";
		// heating
		theValues[18] = "HEATINGCOSTCURRENT";
		theValues[19] = "HEATINGCOSTPOTENTIAL";
		// hot water
		theValues[20] = "HOTWATERCOSTCURRENT";
		theValues[21] = "HOTWATERCOSTPOTENTIAL";
		cSVPrinter.writeln(theValues);
		//Date date = new Date();
		RandomData data = new RandomDataImpl();
		for (PropertyAddress address : results) {
			// rrn
			theValues[0] = UUID.randomUUID().toString().substring(0, 31);
			int daysToRoll = data.nextInt(1, 28);
			int monthsToRoll = data.nextInt(0, 11);
			Date date = getRandomDate(daysToRoll, monthsToRoll);
			theValues[1] = DateFormatter.getFormattedDateForLandMarkData(date);
			theValues[2] = address.getAddressLine2() + ", " + address.getAddressLine3();
			theValues[3] = "";
			theValues[4] = "";
			theValues[5] = address.getTown();
			theValues[6] = address.getPostcodeOutcode() + " " + address.getPostcodeIncode();
			// generate current energy rating
			int upperVal = data.nextInt(1, 50);
			if (upperVal == 1) {
				upperVal = 10;
			}
			int lowerVal = data.nextInt(1, upperVal);
			theValues[7] = lowerVal + "";
			// generate potential
			theValues[8] = upperVal + "";
			// env impact can ignore
			theValues[9] = "23";
			theValues[10] = "45";
			// energy consumption can ignore
			theValues[11] = "35";
			theValues[12] = "56";
			// co2 emissions
			theValues[13] = "8.9";
			theValues[14] = "6.2";
			// floor area
			theValues[15] = "23.54";
			// lighting values
			upperVal = data.nextInt(1, 50);
			if (upperVal == 1) {
				upperVal = 10;
			}
			lowerVal = data.nextInt(1, upperVal);
			theValues[17] = lowerVal + "";
			theValues[16] = upperVal + "";
			// heating
			upperVal = data.nextInt(1, 50);
			if (upperVal == 1) {
				upperVal = 10;
			}
			lowerVal = data.nextInt(1, upperVal);
			theValues[19] = lowerVal + "";
			theValues[18] = upperVal + "";
			// hot water

			upperVal = data.nextInt(1, 50);
			if (upperVal == 1) {
				upperVal = 10;
			}
			lowerVal = data.nextInt(1, upperVal);
			theValues[21] = lowerVal + "";
			;
			theValues[20] = upperVal + "";
			cSVPrinter.writeln(theValues);
			addRecommendations(propertyService, rCSVPrinter, theValues[0]);
		}
		cSVPrinter.flush();
		cSVPrinter.close();
		rCSVPrinter.flush();
		rCSVPrinter.close();
		System.out.println("finished");

	}

	private static void addRecommendations (PropertyService propertyService, CSVPrinter printer, String rrn)
			throws IOException {
		// add the low cost measures
		int sequence = 1;
		RandomData data = new RandomDataImpl();
		int numOfResults = data.nextInt(1, 15);
		//System.out.println("numOfResults lo"+numOfResults);
		for (Measure measure : getDbPropertyMeasures(propertyService, 1, numOfResults)) {
			String[] values = new String[9];
			values[0] = rrn;
			values[1] = sequence + "";
			values[2] = measure.getCategory() + "";
			values[3] = measure.getSummary();
			values[4] = measure.getHeading();
			values[5] = measure.getDescription();
			// typical saving
			values[6] = data.nextInt(0, 400) + "";
			// energy performance rating
			values[7] = data.nextInt(0, 100) + "";
			// env impact
			values[8] = data.nextInt(0, 100) + "";

			printer.writeln(values);
			sequence++;
		}
		sequence = 1;
		numOfResults = data.nextInt(1, 8);
		//System.out.println("high lo"+numOfResults);
		for (Measure measure : getDbPropertyMeasures(propertyService, 2, numOfResults)) {
			String[] values = new String[9];
			values[0] = rrn;
			values[1] = sequence + "";
			values[2] = measure.getCategory() + "";
			values[3] = measure.getSummary();
			values[4] = measure.getHeading();
			values[5] = measure.getDescription();
			// typical saving
			values[6] = data.nextInt(0, 400) + "";
			// energy performance rating
			values[7] = data.nextInt(0, 100) + "";
			// env impact
			values[8] = data.nextInt(0, 100) + "";
			printer.writeln(values);
			sequence++;
		}
		sequence = 1;
		numOfResults = data.nextInt(1, 10);
		//System.out.println("numOfResults other"+numOfResults);
		for (Measure measure : getDbPropertyMeasures(propertyService, 3, numOfResults)) {
			String[] values = new String[9];
			values[0] = rrn;
			values[1] = sequence + "";
			values[2] = measure.getCategory() + "";
			values[3] = measure.getSummary();
			values[4] = measure.getHeading();
			values[5] = measure.getDescription();
			// typical saving
			values[6] = data.nextInt(0, 400) + "";
			// energy performance rating
			values[7] = data.nextInt(0, 100) + "";
			// env impact
			values[8] = data.nextInt(0, 100) + "";
			printer.writeln(values);
			sequence++;
		}
	}

	private static List<Measure> getDbPropertyMeasures (PropertyService propertyService,
			int category, int numberOfResults) {
		if (null == measures) {
			measures = propertyService.findAllMeasures();
		}

		// turn in to list by category
		List<Measure> low = new ArrayList<Measure>();
		List<Measure> high = new ArrayList<Measure>();
		List<Measure> other = new ArrayList<Measure>();
		for (Measure measure : measures) {
			int id = measure.getCategory();
			switch (id) {
			case 1:
				low.add(measure);
				break;
			case 2:
				high.add(measure);
				break;
			case 3:
				other.add(measure);
				break;
			}
		}
		RandomData data = new RandomDataImpl();
		Set<Integer> set = new HashSet<Integer>();

		List<Measure> results = new ArrayList<Measure>();
		switch (category) {
		case 1:
			while (set.size() < numberOfResults) {
				set.add(data.nextInt(0, low.size() - 1));
			}
			// now iterate through the set and get those elements out
			for (Integer index : set) {
				results.add(low.get(index));
			}
			break;
		case 2:
			while (set.size() < numberOfResults) {
				set.add(data.nextInt(0, high.size() - 1));
			}
			for (Integer index : set) {
				results.add(high.get(index));
			}
			break;
		case 3:
			while (set.size() < numberOfResults) {
				set.add(data.nextInt(0, other.size() - 1));
			}
			for (Integer index : set) {
				results.add(other.get(index));
			}
			break;
		}
		return results;
	}

	private static Date getRandomDate (int daysToRoll, int monthsToRoll) {
		Date date = new Date();
		Calendar cal = new GregorianCalendar();
		cal.setTime(date);
		cal.set(Calendar.HOUR, 0);
		cal.set(Calendar.MINUTE, 00);
		cal.set(Calendar.SECOND, 00);
		cal.roll(Calendar.DATE, -daysToRoll);
		cal.roll(Calendar.MONTH, -monthsToRoll);

		return cal.getTime();
	}

	private static Set<PropertyAttribute> createPropertyAttributes () {
		Set<PropertyAttribute> attrs = new HashSet<PropertyAttribute>();
		PropertyAttribute e = new PropertyAttribute();
		e.setName(PropertyAttributeNames.HEATING_COST_CURRENT.getValue());
		e.setValue("422");
		attrs.add(e);
		e = new PropertyAttribute();
		e.setName(PropertyAttributeNames.HEATING_COST_POTENTIAL.getValue());
		e.setValue("400");
		attrs.add(e);
		e = new PropertyAttribute();
		e.setName(PropertyAttributeNames.LIGHTING_COST_CURRENT.getValue());
		e.setValue("88");
		attrs.add(e);
		e = new PropertyAttribute();
		e.setName(PropertyAttributeNames.LIGHTING_COST_POTENTIAL.getValue());
		e.setValue("33");
		attrs.add(e);
		e = new PropertyAttribute();
		e.setName(PropertyAttributeNames.HOT_WATER_COST_CURRENT.getValue());
		e.setValue("167");
		attrs.add(e);
		e = new PropertyAttribute();
		e.setName(PropertyAttributeNames.HOT_WATER_COST_POTENTIAL.getValue());
		e.setValue("101");
		attrs.add(e);
		e = new PropertyAttribute();
		e.setName(PropertyAttributeNames.CO2_EMISSIONS_COST_CURRENT.getValue());
		e.setValue("4.4");
		attrs.add(e);
		e = new PropertyAttribute();
		e.setName(PropertyAttributeNames.CO2_EMISSIONS_COST_POTENTIAL.getValue());
		e.setValue("3.2");
		attrs.add(e);
		e = new PropertyAttribute();
		e.setName(PropertyAttributeNames.TYPICAL_SAVING.getValue());
		e.setValue("175");
		attrs.add(e);
		return attrs;
	}
	//	private static void addRecommendations(CSVPrinter rCSVPrinter, String rrn) throws IOException
	//	{
	//		RandomData data = new RandomDataImpl();
	//
	//		int sequence = 1;
	//		// we need some random genuine measures
	//		// lets get all of the measures
	//		for (TestMeasure measure: createPropertyMeasures())
	//		{
	//			String[] values = new String[9];
	//			values[0] = rrn;
	//			values[1] = sequence+"";
	//			values[2] = measure.getCategoryId()+"";
	//			values[3] = measure.getSummary();
	//			values[4] = measure.getHeading();
	//			values[5] =measure.getDescription();
	//			// typical saving
	//			values[6] = data.nextInt(0, 400)+"";
	//			// energy performance rating
	//			values[7] = data.nextInt(0, 100)+"";
	//			// env impact
	//			values[8] = data.nextInt(0, 100)+"";
	//
	//			rCSVPrinter.writeln(values);
	//			sequence++;
	//		}
	//
	//		System.out.println("finished recomendations");
	//	}
}
