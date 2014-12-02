package org.project.company.greenhomes.web.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.ModelAndView;
import org.project.company.greenhomes.common.enums.BatchTypes;
import org.project.company.greenhomes.common.readers.InputFileReader;
import org.project.company.greenhomes.common.util.FileIOExtras;
import org.project.company.greenhomes.common.util.WebApplicationContextHolder;
import org.project.company.greenhomes.domain.entity.PropertyAddress;
import org.project.company.greenhomes.domain.entity.UploadSummary;
import org.project.company.greenhomes.service.BatchSummaryAndScheduleService;
import org.project.company.greenhomes.service.PropertyService;
import org.project.company.greenhomes.service.pdf.PdfHandler;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.IOException;
import java.util.List;

@Controller
public class FileUploadController {

	/**
	 * the logger
	 */
	//private static final Logger log = LoggerFactory.getLogger(FileUploadController.class);

	//private ReloadableResourceBundleMessageSource messageSource;
	private BatchSummaryAndScheduleService batchSummaryService;
	private PropertyService propertyService;
	//private ReferenceDataService referenceDataService;
	private Boolean allowForcedUpload;
	private Integer numberOfUploadSummaries;
	private Integer errorsToDisplay;
	private String fileUploadLocationLandRegistry;
	private String fileUploadLocationLandMark;

	public void setFileUploadLocationLandMark (String fileUploadLocationLandMark) {
		this.fileUploadLocationLandMark = fileUploadLocationLandMark;
	}

	public void setErrorsToDisplay (Integer errorsToDisplay) {
		this.errorsToDisplay = errorsToDisplay;
	}

	/**
	 * Method that forwards on to the summary page
	 *
	 * @return
	 */
	@RequestMapping(value = "/file-admin/index", method = RequestMethod.GET)
	public ModelMap indexHandler () {

		// get the summaries for two types of upload
		//String results = referenceDataService.findApplicationPropertyByName(ApplicationPropertyNames.NUMBER_OF_UPLOAD_SUMMARIES.getValue()).getValue();
		List<UploadSummary> excelResults = batchSummaryService
				.findLastXSummariesByType(BatchTypes.PROPERTY_SALES, numberOfUploadSummaries);
		List<UploadSummary> epcResults = batchSummaryService
				.findLastXSummariesByType(BatchTypes.EPC_REPORT, numberOfUploadSummaries);
		ModelMap map = new ModelMap();
		map.addAttribute("excelResults", excelResults);
		map.addAttribute("epcResults", epcResults);
		return map;
	}

	/**
	 * For showing the errors, we need a start point as there can be hundreds
	 *
	 * @param processId
	 * @param startPoint
	 * @return
	 */
	@RequestMapping(value = "/file-admin/upload-errors", method = RequestMethod.GET)
	public ModelMap uploadErrors (@RequestParam("processId") long processId,
			@RequestParam("startPoint") int startPoint) {

		int endPoint = startPoint + errorsToDisplay;
		// get the summaries for two types of upload
		UploadSummary summary = batchSummaryService.findUploadSummaryById(processId);
		if (endPoint > summary.getErrorCount()) {
			endPoint = summary.getErrorCount();
		}
		List<PropertyAddress> results = propertyService
				.findNotValidAddressesByUploadId(processId, startPoint, endPoint);
		// we need the upload summary here for the total results.

		ModelMap map = new ModelMap();
		map.addAttribute("results", results);
		map.addAttribute("summary", summary);
		return map;
	}

	/**
	 * Method that forwards on to the upload page. More transparent to have it actually existing.
	 * Sets a session key to prevent re-posting.
	 *
	 * @return
	 */
	@RequestMapping(value = "/file-admin/land-registry-upload", method = RequestMethod.GET)
	public ModelAndView uploadLandRegistry (HttpSession session) {
		// put in a session key to prevent multiple uploads.
		long time = System.currentTimeMillis();
		session.setAttribute("time", time + "");
		return new ModelAndView("/file-admin/land-registry-upload");
	}

	/**
	 * Method that handles the land registry file upload. All it does is copy it to the specified
	 * directory which is defined in the application.properties files with the key
	 * <code>file.upload.location.land.registry</code>. It rejects the file if it does not end with a
	 * <code>.txt</code> extension and sends back to originating page with a message. This could be improved with a
	 * plug in validator if time. User is then redirected to the success page with a message.
	 *
	 * @param request
	 * @param response
	 * @param f
	 * @return
	 */
	@RequestMapping(value = "/file-admin/land-registry-upload", method = RequestMethod.POST)
	public ModelAndView uploadLandRegistry (HttpServletRequest request, HttpServletResponse response,
			@RequestParam("file") MultipartFile f, @RequestParam("time") String time) {
		// this validation could arguably be moved to a validator class.
		HttpSession session = request.getSession();
		if (null != session.getAttribute("time")) {
			// check value with request
			String timeFromSession = (String)session.getAttribute("time");
			if (!timeFromSession.equals(time)) {
				String message = "The time value sent appears to be out of date. Please navigate back to this" +
						" page and start again.";
				ModelAndView mav = new ModelAndView("/file-admin/land-registry-upload");
				mav.addObject("message", message);
				return mav;
			}
			session.removeAttribute("time");
		} else {
			String message =
					"It appears that you have re-posted the same data or have used the back button. Please navigate back to this"
							+
							" page and start again.";
			ModelAndView mav = new ModelAndView("/file-admin/land-registry-upload");
			mav.addObject("message", message);
			return mav;
		}
		if (null == f) {
			String message = "No file provided.";
			ModelAndView mav = new ModelAndView("/file-admin/land-registry-upload");
			mav.addObject("message", message);
			long newTime = System.currentTimeMillis();
			session.setAttribute("time", newTime + "");
			return mav;
		}
		String fileName = f.getOriginalFilename();

		if (fileName.indexOf(".txt") < 0) {
			String message = "Incorrect file format.";
			ModelAndView mav = new ModelAndView("/file-admin/land-registry-upload");
			mav.addObject("message", message);
			long newTime = System.currentTimeMillis();
			session.setAttribute("time", newTime + "");
			return mav;
		}

		// process the file by saving to file system. The job will take care of the rest

		if (FileIOExtras.writeLandRegistryFileToSystem(f, fileName, fileUploadLocationLandRegistry)) {
			//log.debug("success");
			String message = "File uploaded successfully. It will be processed this evening" +
					" and the results will be available tomorrow morning.";
			ModelAndView mav = new ModelAndView("redirect:/file-admin/land-registry-upload-success.do");
			mav.addObject("message", message);
			f = null;
			return mav;
		}

		String message = "File did not upload. The most likely cause is the destination directory" +
				" does not exist or is full. Please ask the system administrator to check.";
		ModelAndView mav = new ModelAndView("redirect:/file-admin/land-registry-upload-success.do");
		mav.addObject("message", message);
		f = null;
		return mav;
	}

	/**
	 * Redirect method to prevent multiple postings of the data.
	 *
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/file-admin/land-registry-upload-success", method = RequestMethod.GET)
	public ModelAndView handlelandRegistryRedirect () {
		return new ModelAndView("/file-admin/land-registry-upload-success");
	}

	/**
	 * Method that forwards on to the upload page. More transparent to have it actually existing.
	 * Sets a session key to prevent re-posting.
	 *
	 * @return
	 */
	@RequestMapping(value = "/file-admin/landmark-upload", method = RequestMethod.GET)
	public ModelAndView uploadLandmark (HttpSession session) {
		// put in a session key to prevent multiple uploads.
		long time = System.currentTimeMillis();
		session.setAttribute("time", time + "");
		return new ModelAndView("/file-admin/landmark-upload");
	}

	/**
	 * Method that handles the landmark file upload. All it does is copy it to the specified
	 * directory which is defined in the application.properties files with the key
	 * <code>file.upload.location.landmark</code>. It rejects the file if it does not end with a
	 * <code>.txt</code> extension and sends back to originating page with a message. This could be improved with a
	 * plug in validator if time. User is then redirected to the success page with a message.
	 *
	 * @param request
	 * @param response
	 * @param f
	 * @return
	 */
	@RequestMapping(value = "/file-admin/landmark-upload", method = RequestMethod.POST)
	public ModelAndView uploadLandMark (HttpServletRequest request, HttpServletResponse response,
			@RequestParam("file") MultipartFile f, @RequestParam("time") String time) {
		// this validation could arguably be moved to a validator class.
		HttpSession session = request.getSession();
		if (null != session.getAttribute("time")) {
			// check value with request
			String timeFromSession = (String)session.getAttribute("time");
			if (!timeFromSession.equals(time)) {
				String message = "The time value sent appears to be out of date. Please navigate back to this" +
						" page and start again.";
				ModelAndView mav = new ModelAndView("/file-admin/landmark-upload");
				mav.addObject("message", message);
				return mav;
			}
			session.removeAttribute("time");
		} else {
			String message =
					"It appears that you have re-posted the same data or have used the back button. Please navigate back to this"
							+
							" page and start again.";
			ModelAndView mav = new ModelAndView("/file-admin/landmark-upload");
			mav.addObject("message", message);
			return mav;
		}
		if (null == f) {
			String message = "No file provided.";
			ModelAndView mav = new ModelAndView("/file-admin/landmark-upload");
			mav.addObject("message", message);
			long newTime = System.currentTimeMillis();
			session.setAttribute("time", newTime + "");
			return mav;
		}
		String fileName = f.getOriginalFilename();

		if (fileName.indexOf(".csv") < 0) {
			String message = "Incorrect file format.";
			ModelAndView mav = new ModelAndView("/file-admin/landmark-upload");
			mav.addObject("message", message);
			long newTime = System.currentTimeMillis();
			session.setAttribute("time", newTime + "");
			return mav;
		}

		// process the file by saving to file system. The job will take care of the rest

		if (FileIOExtras.writeLandRegistryFileToSystem(f, fileName, fileUploadLocationLandMark)) {
			//log.debug("success");
			String message = "File uploaded successfully. It will be processed this evening" +
					" and the results will be available tomorrow morning.";
			ModelAndView mav = new ModelAndView("redirect:/file-admin/landmark-upload-success.do");
			mav.addObject("message", message);
			f = null;
			return mav;
		}

		String message = "File did not upload. The most likely cause is the destination directory" +
				" does not exist or is full. Please ask the system administrator to check.";
		ModelAndView mav = new ModelAndView("redirect:/file-admin/landmark-upload-success.do");
		mav.addObject("message", message);
		f = null;
		return mav;
	}

	/**
	 * Secret squirrel method to force processing of landmark file
	 *
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/file-admin/force-landmark")
	public ModelAndView forceLandMark (HttpServletResponse response) throws IOException {
		if (!allowForcedUpload) {
			return new ModelAndView("redirect:/errors/not-allowed.jsp");
		}

		InputFileReader reader = WebApplicationContextHolder.getLandMarkReader();
		reader.readFileFromFileSystem();
		return new ModelAndView("redirect:/file-admin/index.do");
	}

	/**
	 * Secret squirrel method to force processing of land registry file
	 *
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/file-admin/force-land-registry")
	public ModelAndView forceLandRegistry (HttpServletResponse response) throws IOException {
		if (!allowForcedUpload) {
			return new ModelAndView("redirect:/errors/not-allowed.jsp");
		}
		InputFileReader reader = WebApplicationContextHolder.getLandRegistryReader();
		reader.readFileFromFileSystem();
		return new ModelAndView("redirect:/file-admin/index.do");
	}

	/**
	 * Secret squirrel method to force processing of pdf files
	 *
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/file-admin/force-pdf")
	public ModelAndView forcePdf (HttpServletResponse response) throws IOException {
		if (!allowForcedUpload) {
			return new ModelAndView("redirect:/errors/not-allowed.jsp");
		}
		PdfHandler reader = WebApplicationContextHolder.getPdfLetterBuilder();
		reader.runPdfPrinting();
		return new ModelAndView("redirect:/file-admin/index.do");
	}

	/**
	 * Secret squirrel method to force processing of pdf files
	 *
	 * @return
	 * @throws IOException
	 */
	@RequestMapping(value = "/file-admin/force-ftp")
	public ModelAndView forceFtp (HttpServletResponse response) throws IOException {
		if (!allowForcedUpload) {
			return new ModelAndView("redirect:/errors/not-allowed.jsp");
		}
		PdfHandler reader = WebApplicationContextHolder.getPdfLetterBuilder();
		reader.runFtpToHestia();
		return new ModelAndView("redirect:/file-admin/index.do");
	}

	/**
	 * Redirect method to prevent multiple postings of the data.
	 *
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/file-admin/landmark-upload-success", method = RequestMethod.GET)
	public ModelAndView handleLandMarkRedirect () {
		return new ModelAndView("/file-admin/landmark-upload-success");
	}

	public void setPropertyService (PropertyService propertyService) {
		this.propertyService = propertyService;
	}

	public void setBatchSummaryService (BatchSummaryAndScheduleService batchSummaryService) {
		this.batchSummaryService = batchSummaryService;
	}

	public void setNumberOfUploadSummaries (Integer numberOfUploadSummaries) {
		this.numberOfUploadSummaries = numberOfUploadSummaries;
	}

	public void setFileUploadLocationLandRegistry (
			String fileUploadLocationLandRegistry) {
		this.fileUploadLocationLandRegistry = fileUploadLocationLandRegistry;
	}

	public void setAllowForcedUpload (Boolean allowForcedUpload) {
		this.allowForcedUpload = allowForcedUpload;
	}

}
