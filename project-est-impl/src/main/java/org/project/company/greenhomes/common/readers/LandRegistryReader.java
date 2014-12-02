package org.project.company.greenhomes.common.readers;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.project.company.greenhomes.common.enums.BatchTypes;
import org.project.company.greenhomes.common.enums.WorkFlowStatus;
import org.project.company.greenhomes.common.filefilters.TextFileFilter;
import org.project.company.greenhomes.common.util.*;
import org.project.company.greenhomes.domain.entity.PropertyAddress;
import org.project.company.greenhomes.domain.entity.PropertySale;
import org.project.company.greenhomes.domain.entity.UploadSummary;
import org.project.company.greenhomes.exception.InvalidDataException;
import org.project.company.greenhomes.service.BatchSummaryAndScheduleService;
import org.project.company.greenhomes.service.MailService;
import org.project.company.greenhomes.service.PropertyService;
import org.project.company.greenhomes.service.ReferenceDataService;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.text.ParseException;
import java.util.Date;
import java.util.StringTokenizer;
import java.util.UUID;

public class LandRegistryReader implements InputFileReader {
	private static final int LAND_REGISTRY_TOKEN_COUNT = 15;
	private static final int LAND_REGISTRY_COUNTY_INDEX = 14;
	private static final int LAND_REGISTRY_DISTRICT_INDEX = 13;
	private static final int LAND_REGISTRY_TOWN_INDEX = 12;
	private static final int LAND_REGISTRY_LOCALITY_INDEX = 11;
	private static final int LAND_REGISTRY_ADDRESS_3_INDEX = 10;
	private static final int LAND_REGISTRY_ADDRESS_2_INDEX = 9;
	private static final int LAND_REGISTRY_ADDRESS_1_INDEX = 8;
	private static final int LAND_REGISTRY_POST_CODE_INDEX = 4;
	private static final int LAND_REGISTRY_SALE_DATE_INDEX = 3;
	/**
	 * the logger
	 */
	private static final Logger log = LoggerFactory.getLogger(LandRegistryReader.class);
	private ReloadableResourceBundleMessageSource messageSource;
	/*
	 * For creating/saving batch info
	 */
	private BatchSummaryAndScheduleService batchSummaryService;
	/*
	 * For sending upload success mail
	 */
	private MailService mailService;

	/*
	 * ref data calls
	 */
	private ReferenceDataService referenceDataService;
	/*
	 * 
	 */
	private PropertyService propertyService;
	private String fileUploadLocationLandRegistry;

	public void setFileUploadLocationLandRegistry (
			String fileUploadLocationLandRegistry) {
		this.fileUploadLocationLandRegistry = fileUploadLocationLandRegistry;
	}

	/*
	 * (non-Javadoc)
	 * @see org.project.company.greenhomes.common.util.PropertySalesReader#loadPropertyData(java.io.File)
	 */
	public UploadSummary processFile (File fileHandle) throws IOException, InvalidDataException {

		//  we need to init with some batch summary info
		UploadSummary summary = new UploadSummary();
		// could be replaced by Scanner later, as in the landmark one
		BufferedReader reader = null;
		try {
			summary.setUploadType(BatchTypes.PROPERTY_SALES.getValue());
			long start = System.currentTimeMillis();
			summary.setStartTime(new Date(start));
			summary = batchSummaryService.save(summary);
			if (log.isDebugEnabled()) {
				log.debug("summary.getUploadSummaryId()" + summary.getUploadSummaryId());
			}
			String line = null;
			reader = new BufferedReader(new FileReader(fileHandle));
			int lineNumber = 1;
			int errors = 0;
			while ((line = reader.readLine()) != null) {
				StringTokenizer st = new StringTokenizer(line, ",");
				// needs to be 14 tokens to process

				if (st.countTokens() != LAND_REGISTRY_TOKEN_COUNT) {
					summary.setErrorCount(0);
					summary.setNumberOfRows(0);
					throw new InvalidDataException("The land registry file does not have the " +
							"expected number of fields of " + LAND_REGISTRY_TOKEN_COUNT);
				}

				PropertySale sale = processLine(st);
				sale.setUploadId(summary.getUploadSummaryId());
				// now we need to save the data
				errors += savePropertySale(sale);
				lineNumber++;
			}
			summary.setErrorCount(errors);
			// finally get the number of records for that batch
			Integer numberOfRows = batchSummaryService.findNumberOfResultsByUploadId(summary.getUploadSummaryId());
			summary.setNumberOfRows(numberOfRows.intValue());
			// finally, update the schedule info

		} finally {
			long finish = System.currentTimeMillis();
			summary.setEndTime(new Date(finish));
			batchSummaryService.save(summary);
			if (null != reader) {
				try {
					reader.close();
				} catch (IOException e) {
					log.error("Unable to close reader", e);
				}
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
		if (log.isInfoEnabled()) {
			log.info("Running land registry job = " + runMe);
		}
		if (StringUtils.isEmpty(runMe) || runMe.equalsIgnoreCase("false")) {
			log.info("No need to run land registry job as no system property available.");
			return;
		}
		long startTime = System.currentTimeMillis();
		try {
			// get the file directory

			File folderToRead = new File(fileUploadLocationLandRegistry);
			if (FileIOExtras.isAccessible(folderToRead)) {
				// need to scan that directory for any files
				// and then move them to prevent any other access
				TextFileFilter filter = new TextFileFilter();
				// need to scan that directory for any files
				// and then move them to prevent any other access
				File[] filesInDir = folderToRead.listFiles(filter);
				if (log.isDebugEnabled()) {
					log.debug("found " + filesInDir.length + " files.");
				}
				// once all this is done we need to rename the file and move it in
				// to the read directory - this only happens one time
				File newDirectory = new File(folderToRead + folderToRead.separator + "completed");
				UploadSummary summary = null;
				File newFile = null;
				// iterate through and process them
				for (File file : filesInDir) {
					// correct file name etc etc so now read it
					try {
						summary = processFile(file);
						// once this is done, then move the file and rename
						newFile = FileIOExtras.buildNewFileName(file, newDirectory.getAbsolutePath());
						//boolean rename = file.renameTo(newFile);
						summary.setFileRenameSucceeded(moveFileToDirectory(file, "completed"));
						if (log.isInfoEnabled()) {
							log.info(file.getAbsolutePath() + " has been renamed to " + newFile.getAbsolutePath()
									+ ", success:" + summary.getFileRenameSucceeded());
						}
						mailService.sendLandRegistrySuccessEmail(summary);
						//moveFileToDirectory(file, "complete");
					} catch (IOException e) {
						Object args[] = new Object[] { "land registry" };
						String message = messageSource
								.getMessage("upload.failure.invalid.format.landregistry", args, null);
						log.error(message, e);
						mailService.sendLandRegistryFailEmail(message);
						moveFileToDirectory(file, "errors");
						// skip to the next one
						continue;
					} catch (InvalidDataException e) {
						Object args[] = new Object[] { "land registry" };
						String message = messageSource.getMessage("upload.failure.incorrect.columns", args, null)
								+ " Here is the error message:" + e.getMessage();
						log.error(message, e);
						mailService.sendLandRegistryFailEmail(message);
						moveFileToDirectory(file, "errors");
						continue;
					}
				}
			} else {
				String message = "Unable to run job: because folder exists" + folderToRead.exists() + ", can read:"
						+ folderToRead.canRead() + ", can write:" + folderToRead.canWrite();
				log.error(message);
				mailService.sendLandRegistryFailEmail(message);
			}
		} finally {
			if (log.isInfoEnabled()) {
				long endTime = System.currentTimeMillis();
				log.info("Time taken to run land registry job:" + MathsHelper
						.getTimeTakenFromMillis((endTime - startTime)));
			}
		}
	}

	private Boolean moveFileToDirectory (File file, String dirName) {
		String filePrefix = "";
		try {
			filePrefix = DateFormatter.getFormattedStringForFileRename(new Date());
		} catch (ParseException e) {
			filePrefix = System.currentTimeMillis() + "";
		}
		File newDirectory = new File(fileUploadLocationLandRegistry + file.separator + dirName,
				filePrefix + file.getName());
		return file.renameTo(newDirectory);
	}

	private Integer savePropertySale (PropertySale propertySale) {
		int errors = 0;
		// get the ESTAC and country, region, address key info
		PropertyAddress add = (PropertyAddress)propertySale;
		add = referenceDataService.populateLocationData(add);

		add = propertyService.insertPropertyAddress(add);
		if (!add.getWorkFlowStatus().equals(WorkFlowStatus.RECEIVED.getValue())) {
			errors = 1;
		}
		add = null;
		return errors;
	}

	/**
	 * Method to process each column of the excel sheet
	 * This is more performant than using the more common user driven model, as this uses
	 * event driven model where each row/cell is processed, rather than just taking in the whole document
	 * to memory. Hence we have to process each column in order.
	 */
	private PropertySale processLine (StringTokenizer st) {

		int columnCount = 0;
		// the format of the csv will never change
		//1 is some long code
		// 2 is the price
		// 5, 6 and 7 and 14 can be skipped
		PropertySale propertySale = new PropertySale(UUID.randomUUID());
		while (st.hasMoreTokens()) {
			columnCount++;
			//
			String value = st.nextToken();
			// data surrounded by stoopid " so remove them then trim
			value = StringUtils.remove(value, "\"");
			value = value.trim();
			switch (columnCount) {
			case LAND_REGISTRY_SALE_DATE_INDEX:
				try {
					//2008-09-25 00:00:00 // the date value
					Date parsed = DateFormatter.getFormattedDateFromLandReg(value);
					propertySale.setSaleDate(parsed);
				} catch (ParseException pe) {

					log.error("ERROR: Cannot parse \"" + value + "\". Default to today.");
					propertySale.setSaleDate(new Date());
				}
				break;
			case LAND_REGISTRY_POST_CODE_INDEX:
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
						propertySale.setPostcodeIncode(StringUtils.trim(incode));
						propertySale.setPostcodeOutcode(StringUtils.trim(outcode));
					} else {
						log.error("Unable to create post code for:" + value);
					}
				}
				break;
			// flat number or address 1
			case LAND_REGISTRY_ADDRESS_1_INDEX:
				// check to see if digit and if is, put in trailing space to help match
				if (RegularExpressionChecker.isStMatch(value)) {
					// add the . to the st
					value = RegularExpressionChecker.replaceStValues(value);
				}
				// some of these addresses have lots of white space
				value = AnotherStringHelper.rebuildStringWithNoBigSpaces(value);
				propertySale.setAddressLine1(value);
				break;
			// house number or address 2
			case LAND_REGISTRY_ADDRESS_2_INDEX:
				// check to see if digit and if is, put in trailing space to help match
				if (RegularExpressionChecker.isStMatch(value)) {
					// add the . to the st
					value = RegularExpressionChecker.replaceStValues(value);
				}
				// some of these addresses have lots of white space
				value = AnotherStringHelper.rebuildStringWithNoBigSpaces(value);
				propertySale.setAddressLine2(value);
				break;
			// road or address 3
			case LAND_REGISTRY_ADDRESS_3_INDEX:
				// address line 3 is also the street name
				if (RegularExpressionChecker.isStMatch(value)) {
					// add the . to the st
					value = RegularExpressionChecker.replaceStValues(value);
				}
				propertySale.setAddressLine3(value);
				break;
			case LAND_REGISTRY_LOCALITY_INDEX:
				propertySale.setLocality(value);
				break;
			case LAND_REGISTRY_TOWN_INDEX:
				propertySale.setTown(value);
				break;
			case LAND_REGISTRY_DISTRICT_INDEX:
				propertySale.setDistrict(value);
				break;
			case LAND_REGISTRY_COUNTY_INDEX:
				propertySale.setCounty(value);
				break;
			}
		}
		return propertySale;
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

	public void setMessageSource (ReloadableResourceBundleMessageSource messageSource) {
		this.messageSource = messageSource;
	}

}
