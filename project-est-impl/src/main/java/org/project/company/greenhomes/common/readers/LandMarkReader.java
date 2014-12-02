package org.project.company.greenhomes.common.readers;

import com.Ostermiller.util.CSVParser;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.project.company.greenhomes.common.enums.BatchTypes;
import org.project.company.greenhomes.common.enums.PropertyAttributeNames;
import org.project.company.greenhomes.common.enums.WorkFlowStatus;
import org.project.company.greenhomes.common.filefilters.CSVFileFilter;
import org.project.company.greenhomes.common.filefilters.LandMarkMeasuresFileFilter;
import org.project.company.greenhomes.common.util.DateFormatter;
import org.project.company.greenhomes.common.util.MathsHelper;
import org.project.company.greenhomes.domain.entity.*;
import org.project.company.greenhomes.exception.InvalidDataException;
import org.project.company.greenhomes.exception.NoMatchingDataException;
import org.project.company.greenhomes.service.BatchSummaryAndScheduleService;
import org.project.company.greenhomes.service.MailService;
import org.project.company.greenhomes.service.PropertyService;
import org.project.company.greenhomes.service.ReferenceDataService;

import java.io.File;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.Scanner;
import java.util.UUID;

/**
 * FIle to read the landmark files off the file system.
 *
 * @author FMacdermot
 */
public class LandMarkReader implements InputFileReader {
	private static final int LAND_MARK_RECOMMENDATIONS_TOKEN_COUNT = 9;
	private static final int LAND_MARK_DATA_TOKEN_COUNT = 22;
	private static final int LANDMARK_MEASURE_SAVING_INDEX = 6;
	private static final int LANDMARK_MEASURE_DESCRIPTION_INDEX = 5;
	private static final int LANDMARK_MEASURE_HEADING_INDEX = 4;
	private static final int LANDMARK_MEASURE_SUMMARY_INDEX = 3;
	private static final int LANDMARK_MEASURE_CATEGORY_INDEX = 2;
	private static final int LANDMARK_MEASURE_SORT_ORDER_INDEX = 1;
	private static final int LANDMARK_HOT_WATER_POTENTIAL_INDEX = 21;
	private static final int LANDMARK_HOT_WATER_CURRENT_INDEX = 20;
	private static final int LANDMARK_HEATING_CURRENT_INDEX = 18;
	private static final int LANDMARK_HEATING_POTENTIAL_INDEX = 19;
	private static final int LANDMARK_LIGHTING_POTENTIAL_INDEX = 17;
	private static final int LANDMARK_LIGHTING_CURRENT_INDEX = 16;
	private static final int LANDMARK_CO2_POTENTIAL_INDEX = 14;
	private static final int LANDMARK_CO2_CURRENT_INDEX = 13;
	private static final int LANDMARK_ENERGY_RATING_POTENTIAL = 8;
	private static final int LANDMARK_ENERGY_RATING_CURRENT = 7;
	private static final int LANDMARK_POST_CODE_INDEX = 6;
	private static final int LANDMARK_TOWN_INDEX = 5;
	private static final int LANDMARK_ADDRESS_3_INDEX = 4;
	private static final int LANDMARK_ADDRESS_2_INDEX = 3;
	private static final int LANDMARK_ADDRESS_1_INDEX = 2;
	private static final int LANDMARK_INSPECTION_DATE_INDEX = 1;
	private static final int LANDMARK_RRN_INDEX = 0;
	/**
	 * the logger
	 */
	private static final Logger log = LoggerFactory.getLogger(LandMarkReader.class);
	/*
	 * For creating/saving batch info
	 */
	private BatchSummaryAndScheduleService batchSummaryService;

	private ReloadableResourceBundleMessageSource messageSource;
	/*
	 * For sending upload success mail
	 */
	private MailService mailService;
	/*
	 * Whether to process the first line in the landmark files or not.
	 * First line would be columns
	 */
	private Boolean skipFirstLine;

	/*
	 * ref data calls
	 */
	private ReferenceDataService referenceDataService;
	/*
	 * 
	 */
	private PropertyService propertyService;
	private String fileUploadLocationLandMark;
	private Integer typicalSaving = 0;

	public void setFileUploadLocationLandMark (String fileUploadLocationLandMark) {
		this.fileUploadLocationLandMark = fileUploadLocationLandMark;
	}

	/*
	 * (non-Javadoc)
	 * @see org.project.company.greenhomes.common.util.PropertySalesReader#loadPropertyData(java.io.File)
	 */
	public UploadSummary processFile (File fileHandle) throws IOException, InvalidDataException {

		// this is the scanner for the data document
		Scanner scanner = null;
		// this is the scanner for the recommendations document
		Scanner measureScanner = null;
		// the summary that is saved to upload summary table and also mailed out to administrator
		UploadSummary summary = null;
		// the file object that represents the recommendations csv
		File measuresFile = null;
		// the folder where the uploads live
		File landMarkUploadDirectory = null;
		long start = System.currentTimeMillis();
		int errors = 0;
		try {
			//  we need to init with some batch summary info
			summary = new UploadSummary();
			// load up the data file
			scanner = new Scanner(fileHandle);
			// we also need the measures/recommendations file as well.
			LandMarkMeasuresFileFilter filter = new LandMarkMeasuresFileFilter();
			landMarkUploadDirectory = new File(fileUploadLocationLandMark);
			File[] filesInDir = landMarkUploadDirectory.listFiles(filter);
			// there should be only one, if not fail
			if (null == filesInDir || filesInDir.length != 1) {
				throw new IOException(
						"Measures files are either null or greater than one. Only one can be handled at a time.");
			}
			// get the actual file
			measuresFile = filesInDir[0];
			// and load the measures in to a scanner
			measureScanner = new Scanner(measuresFile);
			// set the upload type
			summary.setUploadType(BatchTypes.EPC_REPORT.getValue());
			// we need to time all this so find the time

			summary.setStartTime(new Date(start));
			// save details up to now
			summary = batchSummaryService.save(summary);
			// keep a record of the number of errors

			// when debugging keep record start time
			long recordStartTime = 0;
			// when debugging need the end time
			long recordEndTime = 0;
			// skip a line if column headings are enabled
			if (skipFirstLine) {
				scanner.nextLine();
				measureScanner.nextLine();
			}
			// check the number of tokens is valid

			// iterate through each line of the data csv
			while (scanner.hasNextLine()) {
				recordStartTime = System.currentTimeMillis();
				// process the line and create the epc
				PropertyEPC epc = processLine(scanner.nextLine(), measureScanner);
				epc.setUploadId(summary.getUploadSummaryId());
				// add up the errors
				errors += savePropertyEPC(epc);
				recordEndTime = System.currentTimeMillis();
				if (log.isDebugEnabled()) {
					log.debug("full loop = " + (recordEndTime - recordStartTime) + " ms");
				}
			}

		} finally {
			long finish = System.currentTimeMillis();
			if (log.isDebugEnabled()) {
				log.debug("time taken = " + (finish - start));
			}
			// end time
			summary.setEndTime(new Date(finish));
			// error count
			summary.setErrorCount(errors);
			// finally get the number of records for that batch
			Integer numberOfRows = batchSummaryService.findNumberOfResultsByUploadId(summary.getUploadSummaryId());
			summary.setNumberOfRows(numberOfRows.intValue());
			// finally, update the schedule info
			batchSummaryService.save(summary);
			// close off any scanners
			if (null != scanner) {
				scanner.close();
			}
			if (null != measureScanner) {
				measureScanner.close();
			}
		}
		return summary;
	}

	/**
	 * Reads the file from the file system and processes. Only runs if a system property
	 * <code>PROCESS_INPUTS</code> exists.
	 */
	public void readFileFromFileSystem () {

		// check to see if the server is set up to run the input jobs

		String runMe = System.getProperty("PROCESS_INPUTS");
		long startTime = System.currentTimeMillis();
		if (log.isInfoEnabled()) {
			log.info("Running land mark job = " + runMe);
		}

		if (StringUtils.isEmpty(runMe) || runMe.equalsIgnoreCase("false")) {
			if (log.isInfoEnabled()) {
				log.info("No need to run landmark job as no system property available.");
			}
			return;
		}
		// get the file directory
		File folderToRead = new File(fileUploadLocationLandMark);

		// need to scan that directory for any files
		CSVFileFilter filter = new CSVFileFilter();
		File[] filesInDir = folderToRead.listFiles(filter);
		if (log.isDebugEnabled()) {
			log.debug("found " + filesInDir.length + " files.");
		}
		if (filesInDir.length > 1) {
			// too many files, we can only do one at a time,
			String message = "Too many/ not enough landmark data files in directory. Only one can be processed at a time. Please contact a system administrator and ask them to remove the extra files.";
			log.error(message);
			mailService.sendLandMarkFailEmail(message);
		}
		// if there are no files then just stop.
		if (filesInDir.length < 1) {
			return;
		}
		UploadSummary summary = null;
		// iterate through and process them
		File fileToProcess = filesInDir[0];
		// correct file name etc etc so now read it
		try {
			long startFileProcess = System.currentTimeMillis();
			summary = processFile(fileToProcess);
			long endFileProcess = System.currentTimeMillis();
			if (log.isInfoEnabled()) {
				log.info("Time taken to process file:" + MathsHelper
						.getTimeTakenFromMillis((endFileProcess - startFileProcess)));
			}
			summary.setFileRenameSucceeded(moveFilesToDirectory(folderToRead.listFiles(), "completed"));
			mailService.sendLandMarkSuccessEmail(summary);
		} catch (IOException e) {
			Object args[] = new Object[] { "landmark" };
			String message = messageSource.getMessage("upload.failure.invalid.format", args, null);
			log.error(message, e);
			mailService.sendLandMarkFailEmail(message);
			moveFilesToDirectory(folderToRead.listFiles(), "errors");
			return;
		} catch (InvalidDataException e) {
			Object args[] = new Object[] { "landmark" };
			String message = messageSource.getMessage("upload.failure.incorrect.columns", args, null)
					+ ". Here is the error message:" + e.getMessage();
			log.error(message, e);
			mailService.sendLandMarkFailEmail(message);
			moveFilesToDirectory(folderToRead.listFiles(), "errors");
			return;
		} finally {
			if (log.isInfoEnabled()) {
				long endTime = System.currentTimeMillis();
				log.info("Time taken to run landmark job:" + MathsHelper.getTimeTakenFromMillis((endTime - startTime)));
			}
		}
	}

	private Boolean moveFilesToDirectory (File[] filesInDir, String directoryName) {
		File folderToRead = new File(fileUploadLocationLandMark);

		for (File file : filesInDir) {
			if (file.isDirectory()) {
				continue;
			}
			File newDirectory = null;
			String filePrefix = "";
			try {
				filePrefix = DateFormatter.getFormattedStringForFileRename(new Date());
			} catch (ParseException e) {
				filePrefix = System.currentTimeMillis() + "";
			}
			newDirectory = new File(
					folderToRead + file.separator + directoryName + file.separator + filePrefix + file.getName());
			if (log.isDebugEnabled()) {
				log.debug("renaming" + file.getAbsolutePath() + ", to:" + newDirectory.getAbsolutePath());
			}
			if (!file.renameTo(newDirectory)) {
				// if the file re-name failed, then we need to break out and get them manually renamed
				// so send message in email.
				return false;
			}

		}
		return true;
	}

	private Integer savePropertyEPC (PropertyEPC propertyEPC) {
		int errors = 0;
		// get the ESTAC and country, region, address key info
		//long startTime = System.currentTimeMillis();
		PropertyAddress add = (PropertyAddress)propertyEPC;
		add = referenceDataService.populateLocationData(add);
		// set up a transaction for this
		// check to see if record is valid too
		add = propertyService.insertPropertyAddress(add);
		if (!add.getWorkFlowStatus().equals(WorkFlowStatus.RECEIVED.getValue())) {
			// do a number rather than boolean so we can increment number of errors
			errors = 1;
		}
		add = null;
		return errors;
	}

	/**
	 * Handles each line of the data csv. This contains the main epc data. For each line we extract the epc data
	 * and then use the rrn to look in the recommendation file and extract the lines using Scanner and process
	 * them.
	 *
	 * @param line
	 * @param measureScanner
	 * @return
	 * @throws InvalidDataException
	 */
	private PropertyEPC processLine (String line, Scanner measureScanner) throws InvalidDataException {

		PropertyEPC propertyEPC = new PropertyEPC(UUID.randomUUID());
		String[][] values = CSVParser.parse(line);
		if (values[0].length != LAND_MARK_DATA_TOKEN_COUNT) {
			throw new InvalidDataException("The land mark data file does not have the " +
					"expected number of fields of " + LAND_MARK_DATA_TOKEN_COUNT + ". " +
					"It has: " + values[0].length);
		}
		for (int y = 0; y < values.length; y++) {
			{
				for (int j = 0; j < values[y].length; j++) {
					String value = StringUtils.trim(values[y][j]);
					// ignore 2
					// i can't think of a better way other than just doing it the grunt way
					// we don't  have any column identifiers
					// so nothing to link the number to the setter unless we do some kind of mapping
					// which has it's own problems
					switch (j) {
					// first value is the date#
					case LANDMARK_RRN_INDEX:
						propertyEPC.setRrn(value);
						break;
					case LANDMARK_INSPECTION_DATE_INDEX:
						try {
							//2008-09-25 00:00:00 // the date value
							Date parsed = DateFormatter.getFormattedDateFromLandMark(value);
							propertyEPC.setInspectionDate(parsed);
						} catch (ParseException pe) {
							log.error("ERROR: Cannot parse \"" + value + "\", default to today.");
							// set to today then
							propertyEPC.setInspectionDate(new Date());
						}
						break;
					case LANDMARK_ADDRESS_1_INDEX:
						// if the address has number in two, then break in to two bits,
						if (value.indexOf(",") != -1) {
							propertyEPC.setAddressLine2(value.substring(0, value.indexOf(",")));
							propertyEPC.setAddressLine3(value.substring(value.indexOf(",") + 1, value.length()).trim());
						} else {
							propertyEPC.setAddressLine1(value);
						}
						break;
					case LANDMARK_ADDRESS_2_INDEX:
						if (value.indexOf(",") != -1) {
							propertyEPC.setAddressLine2(value.substring(0, value.indexOf(",")));
							propertyEPC.setAddressLine3(value.substring(value.indexOf(",") + 1, value.length()).trim());
						} else {
							// if not an empty value
							if (StringUtils.isNotBlank(value)) {
								propertyEPC.setAddressLine2(value);
							}
						}
						break;
					case LANDMARK_ADDRESS_3_INDEX:
						// not used
						break;
					case LANDMARK_TOWN_INDEX:
						// this is the town
						// the town needs to be split upper and lower case, with first letter only upper
						value = StringUtils.capitalize(value);
						propertyEPC.setTown(value);
						break;
					case LANDMARK_POST_CODE_INDEX:

						//  post code we need to split in to two. We can't be sure of how long the first
						// part will be but the second is always three.
						String outcode = "";
						String incode = "";
						if (StringUtils.isNotBlank(value)) {
							// we can just split
							String[] pc = StringUtils.split(value, " ");
							if (pc.length == 2) {
								incode = pc[1];
								outcode = pc[0];
								propertyEPC.setPostcodeIncode(StringUtils.trim(incode));
								propertyEPC.setPostcodeOutcode(StringUtils.trim(outcode));
							} else {
								log.error("Unable to create post code for:" + value);
							}
						}
						break;
					case LANDMARK_ENERGY_RATING_CURRENT:
						// energy rating
						propertyEPC.setEnergyRatingCurrent(new Integer(value));
						break;
					case LANDMARK_ENERGY_RATING_POTENTIAL:
						// potential energy rating
						propertyEPC.setEnergyRatingPotential(new Integer(value));
						break;
					case LANDMARK_CO2_CURRENT_INDEX:
						// co2 current use
						propertyEPC.addPropertyAddressAttribute(
								createAttribute(PropertyAttributeNames.CO2_EMISSIONS_COST_CURRENT.getValue(), value));
						break;
					case LANDMARK_CO2_POTENTIAL_INDEX:
						// co2 potential use
						propertyEPC.addPropertyAddressAttribute(
								createAttribute(PropertyAttributeNames.CO2_EMISSIONS_COST_POTENTIAL.getValue(), value));
						break;
					case LANDMARK_LIGHTING_CURRENT_INDEX:
						// lighting current use
						propertyEPC.addPropertyAddressAttribute(
								createAttribute(PropertyAttributeNames.LIGHTING_COST_CURRENT.getValue(), value));
						break;
					case LANDMARK_LIGHTING_POTENTIAL_INDEX:
						propertyEPC.addPropertyAddressAttribute(
								createAttribute(PropertyAttributeNames.LIGHTING_COST_POTENTIAL.getValue(), value));
						break;
					case LANDMARK_HEATING_CURRENT_INDEX:
						propertyEPC.addPropertyAddressAttribute(
								createAttribute(PropertyAttributeNames.HEATING_COST_CURRENT.getValue(), value));
						break;
					case LANDMARK_HEATING_POTENTIAL_INDEX:
						propertyEPC.addPropertyAddressAttribute(
								createAttribute(PropertyAttributeNames.HEATING_COST_POTENTIAL.getValue(), value));
						break;
					case LANDMARK_HOT_WATER_CURRENT_INDEX:
						propertyEPC.addPropertyAddressAttribute(
								createAttribute(PropertyAttributeNames.HOT_WATER_COST_CURRENT.getValue(), value));
						break;
					case LANDMARK_HOT_WATER_POTENTIAL_INDEX:
						propertyEPC.addPropertyAddressAttribute(
								createAttribute(PropertyAttributeNames.HOT_WATER_COST_POTENTIAL.getValue(), value));
						break;
					default:
						break;
					}
				}
			}
			// now we've extracted that information we need the measures
			// now we need to get the whole line from the other file
			//Scanner measureLineScanner = null;
			typicalSaving = 0;

			while (measureScanner.findInLine(propertyEPC.getRrn()) != null) {
				// then we have a match and so can advance on
				propertyEPC.addPropertyMeasure(processLandmarkMeasureLine(measureScanner.nextLine()));
			}
			propertyEPC.addPropertyAddressAttribute(
					createAttribute(PropertyAttributeNames.TYPICAL_SAVING.getValue(), typicalSaving + ""));
		}
		return propertyEPC;

	}

	private PropertyMeasure processLandmarkMeasureLine (String newLine) throws InvalidDataException {

		// the problem is here is text delimited files with 
		// inverted commas to escape.

		String[][] mvalues = CSVParser.parse(newLine);
		if (mvalues[0].length != LAND_MARK_RECOMMENDATIONS_TOKEN_COUNT) {
			throw new InvalidDataException("The land mark recommendations file does not have the " +
					"expected number of fields of " + LAND_MARK_RECOMMENDATIONS_TOKEN_COUNT + ". " +
					"It has: " + mvalues[0].length);
		}
		PropertyMeasure measure = new PropertyMeasure();
		String summary = null;
		String heading = null;
		String description = null;
		Measure theMeasure = null;
		// Display the parsed data
		for (int i = 0; i < mvalues.length; i++) {
			for (int j = 0; j < mvalues[i].length; j++) {
				String value = mvalues[i][j];
				switch (j) {
				case LANDMARK_MEASURE_SORT_ORDER_INDEX:
					// the sequence or order
					measure.setSortOder(new Integer(value));
					break;
				case LANDMARK_MEASURE_CATEGORY_INDEX:
					// the category of measure, i.e. low, high or other 1,2,3 in that order
					measure.setCategoryId(new Integer(value));
					break;
				case LANDMARK_MEASURE_SUMMARY_INDEX:
					// the summary
					summary = value;
					break;
				case LANDMARK_MEASURE_HEADING_INDEX:
					// the heading
					heading = value;
					break;
				case LANDMARK_MEASURE_DESCRIPTION_INDEX:
					// the description - just in case no match is found
					description = value;
					break;
				case LANDMARK_MEASURE_SAVING_INDEX:
					// this is the typical saving
					// add this to running total as long as it is not category 3
					measure.setTypicalSaving(new Integer(value));
					if (measure.getCategoryId().intValue() != 3) {
						typicalSaving += measure.getTypicalSaving();
					}
					break;
				default:
					break;
				}
			}
		}

		// now we need to find a match for the measure from the db and use the required values 
		// for the pdf! BIt of a palaver I know.
		try {
			theMeasure = propertyService.findMeasureByHeadingSummary(summary, heading);
			measure.setHeading(theMeasure.getEstHeading());
			measure.setDescription(theMeasure.getEstDescription());
		} catch (InvalidDataException e) {
			log.error(
					"Unable to find measure match as heading/summary is null. Will attempt to use the value that is not null.",
					e);
			// one has value so we can use that
			if (!StringUtils.isEmpty(summary)) {
				measure.setHeading(summary);
			}
			if (!StringUtils.isEmpty(heading)) {
				measure.setHeading(heading);
			}
			measure.setDescription(description);

		} catch (NoMatchingDataException e) {
			log.error("No measure match, defaulting to supplied epc measures.", e);
			measure.setHeading(heading);
			measure.setDescription(description);
		}
		return measure;

	}

	/**
	 * Creates a {@link PropertyAttribute} based on name value provided.
	 *
	 * @param name
	 * @param value
	 * @return
	 */
	private PropertyAttribute createAttribute (String name, String value) {
		PropertyAttribute attr = new PropertyAttribute();
		attr.setName(name);
		attr.setValue(value);
		return attr;
	}

	public void setBatchSummaryService (
			BatchSummaryAndScheduleService batchSummaryService) {
		this.batchSummaryService = batchSummaryService;
	}

	public void setMailService (MailService mailService) {
		this.mailService = mailService;
	}

	public void setReferenceDataService (ReferenceDataService referenceDataService) {
		this.referenceDataService = referenceDataService;
	}

	public void setPropertyService (PropertyService propertyService) {
		this.propertyService = propertyService;
	}

	public void setSkipFirstLine (Boolean skipFirstLine) {
		this.skipFirstLine = skipFirstLine;
	}

	public void setMessageSource (ReloadableResourceBundleMessageSource messageSource) {
		this.messageSource = messageSource;
	}

	//	public void setSkipFirstLine(Boolean skipFirstLine) {
	//		this.skipFirstLine = skipFirstLine;
	//	}

}
