package org.project.company.greenhomes.service.pdf;

import com.lowagie.text.DocumentException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.project.company.greenhomes.common.enums.BatchTypes;
import org.project.company.greenhomes.common.enums.WorkFlowStatus;
import org.project.company.greenhomes.common.util.MathsHelper;
import org.project.company.greenhomes.domain.entity.PropertyAddress;
import org.project.company.greenhomes.domain.entity.PropertyEPC;
import org.project.company.greenhomes.domain.entity.Schedule;
import org.project.company.greenhomes.exception.ApplicationException;
import org.project.company.greenhomes.exception.InvalidDataException;
import org.project.company.greenhomes.service.BatchSummaryAndScheduleService;
import org.project.company.greenhomes.service.ExceptionReportService;
import org.project.company.greenhomes.service.MailService;
import org.project.company.greenhomes.service.PropertyService;
import org.project.company.greenhomes.service.pdf.templates.PdfLetterTemplate;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.security.InvalidParameterException;
import java.util.List;
import java.util.Map;

/**
 * Class that builds and writes all the pdf documents to the file system for
 * sending over to the print house, and also saves them in the db for later use.
 * The template is selected from the template map based on the value passed in
 * when the schedule was created.
 *
 *
 */
public class PdfHandler {

	/**
	 * the logger
	 */
	private static final Logger log = LoggerFactory.getLogger(PdfHandler.class);
	/*
	 * Map of templates
	 */
	private Map<String, PdfLetterTemplate> templates;
	private PropertyService propertyService;
	private ExceptionReportService exceptionReportService;
	private BatchSummaryAndScheduleService batchSummaryAndScheduleService;
	private MailService mailService;
	//private FTPConnector ftpConnector;
	private String pdfOutputDirectory;

	/**
	 * Runs the ftp job and send all files over to hestia. After successful ftp the files are deleted
	 * First checks to see there is something in the pdf directory. The record information can be
	 * picked up from the file name which is in schedule id, address key format. Emails are sent to
	 * the admin person giving any reasons for failure. Once finished th files are moved to the completed
	 * subfolder.
	 */
	public void runFtpToHestia () {
		//void		long startTime = System.currentTimeMillis();
		//		File pdfFolder = new File(pdfOutputDirectory);
		//		//Map<String, TransferError> errors = null;
		//		try {
		//			// check to see if the server is set up to run the input jobs
		//			String runMe = System.getProperty("RUN_FTP");
		//			if (log.isDebugEnabled()) {
		//				log.debug("runMe= " + runMe);
		//			}
		//			if (StringUtils.isEmpty(runMe) || runMe.equalsIgnoreCase("false")) {
		//				if (log.isInfoEnabled()) {
		//					log.info("No need to run pftp job as no system property available.");
		//				}
		//				return;
		//			}
		//
		//			String addressKey = null;
		//			try {
		//			//	errors = ftpConnector.uploadBinaryFiles(pdfOutputDirectory);
		//				// any errors should be marked as failures
		//				File[] fileList = pdfFolder.listFiles(new PdfFileFilter());
		//				//annoyingly new style for failed
		//
		//				for (int i = 0; i < fileList.length; i++) {
		//					File file = fileList[i];
		////					WorkFlowStatus newStatus = errors.containsKey(file.getName()) ?
		////							WorkFlowStatus.FTP_FAILED :
		////							WorkFlowStatus.FTP_SUCCESS;
		////					addressKey = file.getName().substring(file.getName().indexOf("_") + 1,
		////							file.getName().indexOf(PdfFileFilter.extension));
		////					propertyService.updatePropertyAddressWorkflow(addressKey, newStatus, WorkFlowStatus.COMPLETED);
		////					if (!moveFileToDirectory(file, "completed")) {
		////						errors.put(file.getName(),
		////								new TransferError(file.getName(), "Unable to move from file system."));
		////					}
		//				}
		//				if (fileList.length > 0) {
		//				//	mailService.sendFtpCompletedEmail(formatErrorMessagesForEmail(errors));
		//				}
		//				fileList = null;
		//
		////			} catch (IOException e) {
		////				processFtpException(e);
		////			} //catch (LoginException e) {
		//			//	processFtpException(e);
		//			//}
		////			catch (Exception e) {
		////				processFtpException(e);
		////			}
		//		} finally {
		//			if (log.isDebugEnabled()) {
		//				long endTime = System.currentTimeMillis();
		//				log.debug("Time taken to run ftp job:" + MathsHelper.getTimeTakenFromMillis((endTime - startTime)));
		//			}
		//		}
	}

	private Boolean moveFileToDirectory (File file, String dirName) {
		File newDirectory = new File(pdfOutputDirectory + file.separator + dirName, file.getName());
		return file.renameTo(newDirectory);
	}

	private void processFtpException (Throwable e) {
		long errorCode = System.currentTimeMillis();
		log.error("Unable to read/write. Error code:" + errorCode, e);
		String message = "The ftp service was unable to complete. Please contact the system administrator " +
				" with the following error code: " + errorCode;
		mailService.sendFtpCompletedEmail(message);
		log.error("Unable to read/write. Error code:" + errorCode, e);
	}

	//	private String formatErrorMessagesForEmail (Map<String, TransferError> errors) {
	//		StringBuilder sb = new StringBuilder();
	//		if (errors.size() > 0) {
	//			sb.append("The ftp transfer had ");
	//			sb.append(errors.size());
	//			sb.append(" problems. Below are the file names with the reason.\n");
	//			TransferError error = null;
	//			for (String key : errors.keySet()) {
	//				error = errors.get(key);
	//				sb.append("\nfile name:" + error.getFileName());
	//				sb.append(", reason:" + error.getReasonCode());
	//			}
	//			sb.append("\n\nTo avoid duplicate postings, please move the files from the pdf output directory ("
	//					+ pdfOutputDirectory + ").");
	//		} else {
	//			sb.append("The ftp transfer had no reported problems.");
	//		}
	//		return sb.toString();
	//	}

	/**
	 * Method that runs the jobs. Called by the timer service. Will run if property
	 * <code>RUN_PDF</code> is set
	 */
	public void runPdfPrinting () {
		long startTime = System.currentTimeMillis();
		try {
			// check to see if the server is set up to run the input jobs
			String runMe = System.getProperty("RUN_PDF");
			if (log.isDebugEnabled()) {
				log.debug("runMe= " + runMe);
			}
			if (StringUtils.isEmpty(runMe) || runMe.equalsIgnoreCase("false")) {
				if (log.isInfoEnabled()) {
					log.info("No need to run pdf job as no system property available.");
				}
				return;
			}
			// first of all we need to get the scheduled pdfs.
			// find any schedules meant to run before the end of t
			// the times are set to run at 11.30.
			List<Schedule> schedules = batchSummaryAndScheduleService.findSchedulesToRun();
			// now just loop through and run them all!
			for (Schedule schedule : schedules) {
				// get all the properties for this id. we want the epc version as that
				// has all the information on.
				List<PropertyEPC> epcs = propertyService.findPropertyEPCByScheduleId(schedule.getScheduleId());
				createLetters(epcs, schedule.getTemplateId() + "");
				// now mark the schedule as completed
				String message = "finished";
				batchSummaryAndScheduleService.markScheduleComplete(schedule.getScheduleId(), message);
			}
		} finally {
			if (log.isDebugEnabled()) {
				long endTime = System.currentTimeMillis();
				log.debug("Time taken to run pdf generation job:" + MathsHelper
						.getTimeTakenFromMillis((endTime - startTime)));
			}
		}
	}

	/**
	 * This method loops through the addresses provided and calls createLetter for each one.
	 * When an exception occurs we continue with the write but log the exception
	 *
	 * @param addresses
	 * @param template
	 */
	private void createLetters (List<PropertyEPC> addresses, String template) {
		if (null == addresses || null == template) {
			String message = "address list is " + addresses + ", and template is " + template + ", " +
					" both must be non-null.";
			throw new InvalidParameterException(message);
		}
		WorkFlowStatus newStatus = null;
		for (PropertyAddress address : addresses) {
			try {
				// we need to populate the centre info
				createPdf(address, template);
				newStatus = WorkFlowStatus.COMPLETED;

			} catch (FileNotFoundException e) {
				newStatus = WorkFlowStatus.FAILED;
				String message =
						"Failed entry on Schedule id:" + address.getScheduleId() + ", with address key:" + address
								.getAddressKey();
				//exceptionReportService.processException(message, e, BatchTypes.PDF_GENERATION, address.getPropertyAddressId());
				log.error(message, e);
			} catch (DocumentException e) {
				newStatus = WorkFlowStatus.FAILED;
				String message =
						"Failed entry on Schedule id:" + address.getScheduleId() + ", with address key:" + address
								.getAddressKey();
				//exceptionReportService.processException(message, e, BatchTypes.PDF_GENERATION, address.getPropertyAddressId());
				log.error(message, e);
			} catch (MalformedURLException e) {
				newStatus = WorkFlowStatus.FAILED;
				String message =
						"Failed entry on Schedule id:" + address.getScheduleId() + ", with address key:" + address
								.getAddressKey();
				exceptionReportService
						.processException(message, e, BatchTypes.PDF_GENERATION, address.getPropertyAddressId());
				log.error(message, e);
			} catch (IOException e) {
				newStatus = WorkFlowStatus.FAILED;
				String message =
						"Failed entry on Schedule id:" + address.getScheduleId() + ", with address key:" + address
								.getAddressKey();
				exceptionReportService
						.processException(message, e, BatchTypes.PDF_GENERATION, address.getPropertyAddressId());
				log.error(message, e);
			} catch (InvalidDataException e) {
				newStatus = WorkFlowStatus.FAILED;
				String message =
						"Failed entry on Schedule id:" + address.getScheduleId() + ", with address key:" + address
								.getAddressKey();
				exceptionReportService
						.processException(message, e, BatchTypes.PDF_GENERATION, address.getPropertyAddressId());
				log.error(message, e);
			} catch (Exception e) {
				newStatus = WorkFlowStatus.FAILED;
				String message =
						"Failed entry on Schedule id:" + address.getScheduleId() + ", with address key:" + address
								.getAddressKey();
				exceptionReportService
						.processException(message, e, BatchTypes.PDF_GENERATION, address.getPropertyAddressId());
				log.error(message, e);
			} finally {
				propertyService
						.updatePropertyAddressWorkflow(address.getAddressKey(), newStatus, WorkFlowStatus.SCHEDULED);
			}
		}
	}

	/**
	 * Calling this method will generate a full PDF based on the template key provided
	 * and calls the required methods in the correct order
	 *
	 * @param address
	 * @param templateKey
	 * @throws DocumentException
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws InvalidDataException
	 * @throws ApplicationException
	 */
	private void createPdf (final PropertyAddress address, final String templateKey)
			throws DocumentException, MalformedURLException, IOException, InvalidDataException, ApplicationException {
		final Schedule schedule = batchSummaryAndScheduleService.findScheduleByid(address.getScheduleId());
		// first need to figure out the template
		// for now we only have one, but when there are more will need to change
		// the logic here.
		final PdfLetterTemplate template = templates.get(templateKey);

		if (null == template) {
			String message = "Invalid template value set, cannot find template labelled: " + templateKey;
			throw new InvalidParameterException(message);
		}
		template.generate(address, schedule);
	}

	/**
	 * Method used to generate an in memory pdf for the front end
	 *
	 * @param address
	 * @return
	 * @throws DocumentException
	 * @throws MalformedURLException
	 * @throws IOException
	 * @throws InvalidDataException
	 * @throws ApplicationException
	 */
	public ByteArrayOutputStream createPdfInMemory (PropertyAddress address)
			throws DocumentException, MalformedURLException, IOException, InvalidDataException, ApplicationException {
		// we have the schedule id and so need the template
		final Schedule schedule = batchSummaryAndScheduleService.findScheduleByid(address.getScheduleId());
		PdfLetterTemplate template = templates.get(schedule.getTemplateId());
		return template.generateInMemory(address, schedule);
	}

	public void setExceptionReportService (
			ExceptionReportService exceptionReportService) {
		this.exceptionReportService = exceptionReportService;
	}

	public void setPropertyService (PropertyService propertyService) {
		this.propertyService = propertyService;
	}

	public void setTemplates (Map<String, PdfLetterTemplate> templates) {
		this.templates = templates;
	}

	public void setBatchSummaryAndScheduleService (
			BatchSummaryAndScheduleService batchSummaryAndScheduleService) {
		this.batchSummaryAndScheduleService = batchSummaryAndScheduleService;
	}

	public void setPdfOutputDirectory (String pdfOutputDirectory) {
		this.pdfOutputDirectory = pdfOutputDirectory;
	}

	//	public void setFtpConnector (FTPConnector ftpConnector) {
	//		this.ftpConnector = ftpConnector;
	//	}

	public void setMailService (MailService mailService) {
		this.mailService = mailService;
	}
}
