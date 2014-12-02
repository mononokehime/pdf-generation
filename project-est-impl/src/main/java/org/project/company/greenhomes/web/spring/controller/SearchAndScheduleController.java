package org.project.company.greenhomes.web.spring.controller;

import com.lowagie.text.DocumentException;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.ModelMap;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.ModelAndView;
import org.project.company.greenhomes.common.util.DateFormatter;
import org.project.company.greenhomes.common.validation.SearchValidator;
import org.project.company.greenhomes.domain.entity.ExceptionReport;
import org.project.company.greenhomes.domain.entity.PropertyEPC;
import org.project.company.greenhomes.domain.entity.Schedule;
import org.project.company.greenhomes.domain.entity.User;
import org.project.company.greenhomes.domain.model.ReferenceData;
import org.project.company.greenhomes.domain.model.SearchParameters;
import org.project.company.greenhomes.exception.ApplicationException;
import org.project.company.greenhomes.exception.InvalidDataException;
import org.project.company.greenhomes.service.BatchSummaryAndScheduleService;
import org.project.company.greenhomes.service.ExceptionReportService;
import org.project.company.greenhomes.service.PropertyService;
import org.project.company.greenhomes.service.ReferenceDataService;
import org.project.company.greenhomes.service.pdf.PdfHandler;
import org.project.company.greenhomes.service.query.QueryEngineService;
import org.project.company.greenhomes.service.query.QueryHolder;
import org.project.company.greenhomes.service.query.QueryHolder.DBFieldNames;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.net.MalformedURLException;
import java.text.ParseException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Controller
@SessionAttributes("holder")
public class SearchAndScheduleController {
	/**
	 * the logger
	 */
	private static final Logger log = LoggerFactory.getLogger(SearchAndScheduleController.class);
	/*
	 * For validating
	 */
	private SearchValidator searchValidator;
	/*
	 * For queries
	 */
	private QueryEngineService queryEngineService;
	private ReferenceDataService referenceDataService;
	private BatchSummaryAndScheduleService batchSummaryAndScheduleService;
	private PdfHandler pdfLetterBuilder;
	private PropertyService propertyService;
	private ExceptionReportService exceptionReportService;
	private Integer summariesToDisplay;
	private Float resultsDisplaySize;

	/**
	 * Method that forwards on to the first search page
	 *
	 * @param searchParameters automatically populated if returning to this page
	 * @return
	 */
	@RequestMapping(value = "/search/index", method = RequestMethod.GET)
	public ModelMap indexHandler (@ModelAttribute("searchParameters") SearchParameters searchParameters) {
		return new ModelMap("searchParameters", searchParameters);
	}

	/**
	 * Method that forwards on to the history search page
	 *
	 * @param searchParameters automatically populated if returning to this page
	 * @return
	 */
	@RequestMapping(value = "/search/history-index", method = RequestMethod.GET)
	public ModelMap indexHistoryHandler (@ModelAttribute("searchParameters") SearchParameters searchParameters) {
		return new ModelMap("searchParameters", searchParameters);
	}

	/**
	 * Performs the actual search adn returns to the search result jsp
	 *
	 * @param searchParameters populated when posted from jsp
	 * @param result           any errors
	 * @param request
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/search/search", method = RequestMethod.POST)
	public ModelAndView search (@ModelAttribute("searchParameters") SearchParameters searchParameters,
			BindingResult result, HttpServletRequest request) throws ParseException {
		//
		searchValidator.validate(searchParameters, result);
		if (result.hasErrors()) {
			ModelAndView modelView = new ModelAndView("/search/index");
			modelView.addObject("searchParameters", searchParameters);
			return modelView;
		}
		ModelAndView modelView = new ModelAndView("/search/results");
		modelView.addObject("searchParameters", searchParameters);
		QueryHolder holder = populateSearchCriteria(searchParameters);
		holder = queryEngineService.findPropertiesForPDFsByCriteria(holder);
		// set the search criteria so it they can be amended later
		holder.setSearchParameters(searchParameters);
		// finally need to get template details for scheduling;
		List<ReferenceData> templates = referenceDataService.findAllTemplates();
		modelView.addObject("templates", templates);
		modelView.addObject("holder", holder);
		request.getSession().setAttribute("holder", holder);
		return modelView;
	}

	/**
	 * Looks at the schedule history based on parameters provided.
	 *
	 * @param searchParameters
	 * @param result
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/search/search-history", method = RequestMethod.POST)
	public ModelAndView searchHistory (@ModelAttribute("searchParameters") SearchParameters searchParameters,
			BindingResult result, HttpServletRequest request) throws ParseException {
		//
		searchValidator.validate(searchParameters, result);
		if (result.hasErrors()) {
			ModelAndView modelView = new ModelAndView("/search/history-index");
			modelView.addObject("searchParameters", searchParameters);
			return modelView;
		}
		ModelAndView modelView = new ModelAndView("/search/history-results");
		modelView.addObject("searchParameters", searchParameters);
		QueryHolder holder = populateSearchCriteria(searchParameters);
		holder = queryEngineService.findPropertiesForPDFsByCriteria(holder);
		// set the search criteria so it they can be amended later
		holder.setSearchParameters(searchParameters);
		modelView.addObject("holder", holder);
		request.getSession().setAttribute("holder", holder);
		return modelView;
	}

	/**
	 * Creates a schedule with the given parameters and sends a redirect afterwards.
	 * page
	 *
	 * @param request
	 */
	@RequestMapping(value = "/search/schedule", method = RequestMethod.POST)
	public ModelAndView schedule (@RequestParam("templateId") String templateId,
			@RequestParam("startDate") String startDate, HttpServletRequest request, HttpSession session) {
		Date theDate = null;
		try {
			theDate = DateFormatter.getFormattedDate(startDate);
		} catch (ParseException e) {
			ModelAndView modelView = new ModelAndView("/search/results");
			// send back if this fails
			List<ReferenceData> templates = referenceDataService.findAllTemplates();
			modelView.addObject("templates", templates);
			// get the results set based on the page given
			QueryHolder holder = (QueryHolder)request.getSession().getAttribute("holder");
			modelView.addObject("holder", holder);
			modelView.addObject("message", "Please enter the date in format dd-mm-yyyy");
			return modelView;
		}

		// format the date
		// we need to validate the date
		ModelAndView modelView = new ModelAndView("redirect:/search/scheduled-summary.do");
		User user = (User)SecurityContextHolder.getContext().getAuthentication().getPrincipal();
		// get the holder out as this has the search parameters
		QueryHolder holder = (QueryHolder)session.getAttribute("holder");
		// this is the list of ids that require scheduling
		List<String> ids = holder.getPropertyKeys();

		Schedule schedule = new Schedule();
		schedule.setRequestDate(new Date());
		schedule.setScheduleById(user.getUserId());
		schedule.setStartDate(theDate);
		schedule.setTemplateId(templateId);
		schedule.setTemplateVersion("V1");
		try {
			batchSummaryAndScheduleService.createScheduleAndUpdateProperties(ids, schedule);

		} catch (InvalidDataException e) {
			String message = "An error occured creating the schedule" +
					" and it will not run. Please ask sys admin to check the server logs for reasons why.";
			log.error(message, e);
			//modelView.addObject("message", message);
			session.setAttribute("message", message);
		}
		session.setAttribute("schedule", schedule);
		return modelView;
	}

	/**
	 * Dummy method to handle redirects from the schedule
	 *
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/search/scheduled-summary")
	public ModelAndView scheduleRedirect (HttpSession session) {
		ModelAndView modelView = new ModelAndView("/search/scheduled-summary");
		// should be able to get objects from session
		String message = (String)session.getAttribute("message");

		Schedule schedule = (Schedule)session.getAttribute("schedule");

		modelView.addObject("message", message);
		modelView.addObject("schedule", schedule);
		return modelView;
	}

	/**
	 * Shows the schedule history
	 *
	 * @param session
	 * @return
	 */
	@RequestMapping(value = "/search/schedule-history")
	public ModelAndView scheduleHistory (HttpSession session) {
		ModelAndView modelView = new ModelAndView("/search/schedule-history");
		if (null != session.getAttribute("schedule")) {
			session.removeAttribute("schedule");
		}
		//Integer summariesToDisplay = new Integer(referenceDataService.findApplicationPropertyByName(ApplicationPropertyNames.SUMMARY_RESULTS_DISPLAY.getValue()).getValue());
		List<Schedule> schedules = batchSummaryAndScheduleService.findLastXSchedules(summariesToDisplay);
		modelView.addObject("schedules", schedules);
		return modelView;
	}

	/**
	 * Shows the schedule details
	 *
	 * @param session
	 * @param scheduleId
	 * @return
	 * @throws ParseException
	 */
	@RequestMapping(value = "/search/schedule-detail")
	public ModelAndView scheduleDetail (HttpSession session, @RequestParam("scheduleId") Long scheduleId)
			throws ParseException {

		Schedule schedule = null;
		ModelAndView modelView = new ModelAndView("/search/schedule-detail");
		schedule = batchSummaryAndScheduleService.findScheduleByid(scheduleId);
		modelView.addObject("schedule", schedule);
		// add to session for paging.
		session.setAttribute("schedule", schedule);
		// now get the results for that schedule
		SearchParameters searchParameters = new SearchParameters();
		searchParameters.setScheduleId(scheduleId);
		QueryHolder holder = populateSearchCriteria(searchParameters);
		holder = queryEngineService.findPropertiesForPDFsByCriteria(holder);
		holder.setSearchParameters(searchParameters);
		holder.setCurrentPage(0f);
		modelView.addObject("holder", holder);
		// add to session for paging.
		//session.removeAttribute("holder");
		session.setAttribute("holder", holder);
		return modelView;
	}

	/**
	 * Shows the schedule details exception
	 * page
	 *
	 * @param session
	 * @throws ParseException
	 */
	@RequestMapping(value = "/search/schedule-detail-exception")
	public ModelAndView scheduleExceptionDetail (HttpSession session, @RequestParam("processId") String processId) {
		ModelAndView modelView = new ModelAndView("/search/schedule-detail-exception");
		// get the schedule detail itself
		List<ExceptionReport> exceptions = exceptionReportService.findExceptionReportsByProcessId(processId);
		session.setAttribute("exceptions", exceptions);
		return modelView;
	}

	/**
	 * Allows user to page through schedule detail results
	 *
	 * @param page
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/search/schedule-pager")
	public ModelAndView schedulePager (@RequestParam("page") float page, HttpServletRequest request) {
		ModelAndView modelView = new ModelAndView("/search/schedule-detail");
		// check the value is a valid numerical value
		if (page < 1) {
			page = 1;
		}
		// get the results set based on the page given
		QueryHolder holder = (QueryHolder)request.getSession().getAttribute("holder");

		// make sure the requested page is not greater than the total pages
		if (page > holder.getTotalPages()) {
			page = holder.getTotalPages();
		}
		// because the index actually start at 0, we need to take 1 away
		holder.setCurrentPage(page - 1);
		// we already have the total list of keys in an array, so no point searching again
		// where to start the paging
		float startPoint = (page - 1) * holder.getResultsPerPage();
		// where to cut the paging off
		float endPoint = page * holder.getResultsPerPage();
		if (page == holder.getTotalPages()) {
			// if this is the case, then we just need the end point to be the 
			// last result.	    
			endPoint = holder.getTotalResults();
		}
		List<String> list = holder.getPropertyKeys().subList((int)startPoint, (int)endPoint);

		//List<String> list =
		holder.setResults(queryEngineService.findPropertyEPCListByPropertyKey(list));
		modelView.addObject("holder", holder);
		return modelView;
	}

	/**
	 * Open up the pdf to users
	 * page
	 *
	 * @param response
	 * @throws ApplicationException
	 * @throws InvalidDataException
	 * @throws IOException
	 * @throws DocumentException
	 * @throws MalformedURLException
	 */
	@RequestMapping(value = "/search/view-pdf")
	public void viewPDF (HttpServletResponse response, @RequestParam("propertyAddressId") String propertyAddressId)
			throws MalformedURLException, DocumentException, IOException, InvalidDataException, ApplicationException {
		PropertyEPC epc = propertyService.findPropertyEPCById(propertyAddressId);
		ByteArrayOutputStream stream = pdfLetterBuilder.createPdfInMemory(epc);
		response.setContentType("application/pdf");
		response.setHeader("Content-disposition", "attachment; filename=\"" + epc.getRrn() + ".pdf\"");
		response.setContentLength(stream.size());
		ServletOutputStream out = response.getOutputStream();
		stream.writeTo(out);
		out.flush();
		stream.close();
	}

	/**
	 * Allows user to page through results
	 *
	 * @param page
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/search/pager")
	public ModelAndView pager (@RequestParam("page") float page, HttpServletRequest request) {
		ModelAndView modelView = new ModelAndView("/search/results");
		// check the value is a valid numerical value
		if (page < 1) {
			page = 1;
		}

		// get the results set based on the page given
		QueryHolder holder = (QueryHolder)request.getSession().getAttribute("holder");

		// make sure the requested page is not greater than the total pages
		if (page > holder.getTotalPages()) {
			page = holder.getTotalPages();
		}
		// because the index actually start at 0, we need to take 1 away
		holder.setCurrentPage(page - 1);
		// we already have the total list of keys in an array, so no point searching again
		// where to start the paging
		float startPoint = (page - 1) * holder.getResultsPerPage();
		// where to cut the paging off
		float endPoint = page * holder.getResultsPerPage();
		if (page == holder.getTotalPages()) {
			// if this is the case, then we just need the end point to be the 
			// last result.	    
			endPoint = holder.getTotalResults();
		}
		List<String> list = holder.getPropertyKeys().subList((int)startPoint, (int)endPoint);

		//List<String> list =
		holder.setResults(queryEngineService.findPropertyEPCListByPropertyKey(list));
		//	holder = queryEngineService.findPropertiesForPDFsByCriteria(holder);
		// set the search criteria so it they can be amended later
		// finally need to get template details for scheduling;
		List<ReferenceData> templates = referenceDataService.findAllTemplates();
		modelView.addObject("templates", templates);
		modelView.addObject("holder", holder);
		return modelView;
	}

	/**
	 * Allows user to page through history results
	 *
	 * @param page
	 * @param request
	 * @return
	 */
	@RequestMapping(value = "/search/pager-history")
	public ModelAndView pagerHistory (@RequestParam("page") float page, HttpServletRequest request) {
		ModelAndView modelView = new ModelAndView("/search/history-results");
		// check the value is a valid numerical value
		if (page < 1) {
			page = 1;
		}
		// get the results set based on the page given
		QueryHolder holder = (QueryHolder)request.getSession().getAttribute("holder");
		// because the index actually start at 0, we need to take 1 away
		// make sure the requested page is not greater than the total pages
		if (page > holder.getTotalPages()) {
			page = holder.getTotalPages();
		}
		holder.setCurrentPage(page - 1);
		// we already have the total list of keys in an array, so no point searching again
		// where to start the paging
		float startPoint = (page - 1) * holder.getResultsPerPage();
		// where to cut the paging off
		float endPoint = page * holder.getResultsPerPage();
		if (page == holder.getTotalPages()) {
			// if this is the case, then we just need the end point to be the 
			// last result.	    
			endPoint = holder.getTotalResults();
		}
		List<String> list = holder.getPropertyKeys().subList((int)startPoint, (int)endPoint);
		holder.setResults(queryEngineService.findPropertyEPCListByPropertyKey(list));
		modelView.addObject("holder", holder);
		return modelView;
	}

	private QueryHolder populateSearchCriteria (SearchParameters searchParameters) throws ParseException {

		QueryHolder holder = new QueryHolder(resultsDisplaySize);
		Map<DBFieldNames, Object> query = new HashMap<DBFieldNames, Object>();
		if (null != searchParameters.getScheduleId()) {
			query.put(DBFieldNames.SCHEDULE_ID, searchParameters.getScheduleId());
		}
		if (StringUtils.isNotBlank(searchParameters.getConsumerSegment())) {
			query.put(DBFieldNames.SEGMENTATION_VALUE, searchParameters.getConsumerSegment());
		}
		if (StringUtils.isNotBlank(searchParameters.getCountry())) {
			query.put(DBFieldNames.COUNTRY, searchParameters.getCountry());
		}
		if (StringUtils.isNotBlank(searchParameters.getESTAC())) {
			query.put(DBFieldNames.ESTAC, searchParameters.getESTAC());
		}
		if (StringUtils.isNotBlank(searchParameters.getRating())) {
			query.put(DBFieldNames.EPC_RATING, searchParameters.getRating());
		}
		if (StringUtils.isNotBlank(searchParameters.getWorkflowStatus())) {
			query.put(DBFieldNames.WORKFLOW, searchParameters.getWorkflowStatus());
		}
		if (StringUtils.isNotBlank(searchParameters.getLocalAuthority())) {
			query.put(DBFieldNames.LOCAL_AUTHORITY, searchParameters.getLocalAuthority());
		}
		// now check for date parameters
		if (StringUtils.isNotBlank(searchParameters.getFromDate())) {
			query.put(DBFieldNames.FROM_DATE, DateFormatter.getFormattedDate(searchParameters.getFromDate()));
		}
		if (StringUtils.isNotBlank(searchParameters.getToDate())) {
			query.put(DBFieldNames.TO_DATE, DateFormatter.getFormattedDate(searchParameters.getToDate()));
		}
		holder.setGenericQuery(query);
		return holder;
	}

	// the following are put in to test exception handling
	//    @RequestMapping(value="/search/null")
	//    public ModelMap killMe() throws NullPointerException {
	//    	throw new NullPointerException("aaa");
	//    }
	//    @RequestMapping(value="/search/parse")
	//    public ModelMap killMe1() throws ParseException {
	//    	throw new ParseException("eeek",1);
	//    }
	//    @RequestMapping(value="/search/generic")
	//    public ModelMap killMe2() throws Exception  {
	//    	throw new Exception("eeek");
	//    }

	public void setQueryEngineService (QueryEngineService queryEngineService) {
		this.queryEngineService = queryEngineService;
	}

	public void setResultsDisplaySize (Float resultsDisplaySize) {
		this.resultsDisplaySize = resultsDisplaySize;
	}

	public void setSummariesToDisplay (Integer summariesToDisplay) {
		this.summariesToDisplay = summariesToDisplay;
	}

	public void setExceptionReportService (
			ExceptionReportService exceptionReportService) {
		this.exceptionReportService = exceptionReportService;
	}

	public void setPropertyService (PropertyService propertyService) {
		this.propertyService = propertyService;
	}

	public void setSearchValidator (SearchValidator searchValidator) {
		this.searchValidator = searchValidator;
	}

	public void setReferenceDataService (ReferenceDataService referenceDataService) {
		this.referenceDataService = referenceDataService;
	}

	public void setBatchSummaryAndScheduleService (
			BatchSummaryAndScheduleService batchSummaryAndScheduleService) {
		this.batchSummaryAndScheduleService = batchSummaryAndScheduleService;
	}

	public void setPdfLetterBuilder (PdfHandler pdfLetterBuilder) {
		this.pdfLetterBuilder = pdfLetterBuilder;
	}

}
