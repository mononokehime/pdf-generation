package org.project.company.greenhomes.web.listeners;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.support.WebApplicationContextUtils;
import org.springframework.web.context.support.WebApplicationObjectSupport;
import org.project.company.greenhomes.common.enums.BeanNames;
import org.project.company.greenhomes.domain.model.ReferenceData;
import org.project.company.greenhomes.exception.NoMatchingDataException;
import org.project.company.greenhomes.service.ReferenceDataService;

import javax.servlet.ServletContext;
import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import java.util.List;

/**
 * @author lorenzo.ansaloni - 4 Mar 2008
 */
public class ApplicationListener extends WebApplicationObjectSupport implements ServletContextListener {

	/**
	 * the logger
	 */
	private static final Logger log = LoggerFactory.getLogger(ApplicationListener.class);
	private ReferenceDataService referenceDataService;

	/**
	 * pre-load into the servletContext the values for several
	 * select menues
	 *
	 * @throws NoMatchingDataException
	 */
	@SuppressWarnings("unchecked")
	public void contextInitialized (ServletContextEvent sce) {
		ServletContext servletContext = sce.getServletContext();
		WebApplicationContext webContext = WebApplicationContextUtils.getWebApplicationContext(servletContext);

		this.referenceDataService = (ReferenceDataService)webContext
				.getBean(BeanNames.REFERENCE_DATA_SERVICE.getValue());

		if (null != referenceDataService) {
			if (log.isDebugEnabled()) {
				log.debug("going to find ref data******************************");
			}
			servletContext.setAttribute("ratingsList", referenceDataService.findRatingsList());

			// servletContext.setAttribute("treatmentsList", referenceDataService.findTreatmentsList());
			servletContext.setAttribute("countriesList", referenceDataService.findCountriesList());
			servletContext.setAttribute("consumerSegmentList", referenceDataService.findConsumerSegmentList());
			// finally iterate through and put in the list for estacs and country
			for (ReferenceData data : (List<ReferenceData>)servletContext.getAttribute("countriesList")) {
				try {
					servletContext.setAttribute("ESTACsList" + data.getReferenceDataKey(),
							referenceDataService.findESTACListByCountry(data.getReferenceDataKey()));
					servletContext.setAttribute("LAsList" + data.getReferenceDataKey(),
							referenceDataService.findLAListByCountry(data.getReferenceDataKey()));
				} catch (NoMatchingDataException e) {

					log.error("Unable to find ESTAC data for:" + data.getReferenceDataKey(), e);
				}
			}

		}

	}

	public void contextDestroyed (ServletContextEvent arg0) {

	}

}
