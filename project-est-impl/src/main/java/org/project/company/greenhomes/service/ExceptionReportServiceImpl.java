package org.project.company.greenhomes.service;

import org.hibernate.HibernateException;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.project.company.greenhomes.common.enums.BatchTypes;
import org.project.company.greenhomes.dao.ExceptionReportDAO;
import org.project.company.greenhomes.domain.entity.ExceptionReport;
import org.project.company.greenhomes.exception.ApplicationException;
import org.project.company.greenhomes.exception.ExceptionHelper;

import java.util.List;

public class ExceptionReportServiceImpl implements ExceptionReportService {
	/**
	 * the logger
	 */
	private static final Logger log = LoggerFactory.getLogger(ExceptionReportServiceImpl.class);
	private ExceptionReportDAO exceptionReportDAO;

	public ExceptionReportServiceImpl (final ExceptionReportDAO exceptionReportDAO) {
		if (null == exceptionReportDAO) {
			throw new IllegalArgumentException("exceptionReportDAO cannot be null.");
		}
		this.exceptionReportDAO = exceptionReportDAO;
	}

	/*
	 * (non-Javadoc)
	 * @see org.project.company.greenhomes.service.ExceptionReportService#createExceptionReportRecord(org.project.company.greenhomes.domain.entity.ExceptionReport)
	 */
	public void createExceptionReportRecord (final ExceptionReport report) throws ApplicationException {
		try {
			exceptionReportDAO.save(report);
		} catch (HibernateException e) {
			// want to catch this here to prevent failure along the line
			throw new ApplicationException("Unable to access the exception table.", e);
		}

	}

	public void processException (final String message, final Exception e, final BatchTypes type,
			final String processId) {
		//create an exception
		if (log.isDebugEnabled()) {
			log.debug("Logging exception before insert", e);
		}

		ExceptionReport report = new ExceptionReport();
		report.setMessage(message);
		report.setFunctionalArea(type.getValue());
		report.setProcessId(processId);
		String exception = ExceptionHelper.exceptionToString(e);
		if (exception.length() > 2048) {
			report.setException(exception.substring(0, 2047));
		} else {
			report.setException(exception);
		}

		// now save it, catch any exception here as this should not prevent any further 
		// processing
		try {
			createExceptionReportRecord(report);
		} catch (ApplicationException e1) {
			// just log and continue here
			log.error("eek", e1);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.project.company.greenhomes.service.ExceptionReportService#findExceptionReportsByProcessId(java.lang.String)
	 */
	public List<ExceptionReport> findExceptionReportsByProcessId (final String processId) {
		return exceptionReportDAO.findExceptionReportsByProcessId(processId);
	}

}
