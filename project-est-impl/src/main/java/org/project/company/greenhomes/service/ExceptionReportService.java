package org.project.company.greenhomes.service;

import org.project.company.greenhomes.common.enums.BatchTypes;
import org.project.company.greenhomes.dao.ExceptionReportDAO;
import org.project.company.greenhomes.domain.entity.ExceptionReport;
import org.project.company.greenhomes.exception.ApplicationException;

import java.util.List;

/**
 * Class to control access to the exception report dao
 *
 *
 */
public interface ExceptionReportService {

	/**
	 * Calls the {@link ExceptionReportDAO} to insert a record of what went wrong
	 *
	 * @param e details of the exception
	 * @param e
	 * @throws ApplicationException
	 */
	void createExceptionReportRecord (final ExceptionReport e) throws ApplicationException;

	void processException (final String message, final Exception e, final BatchTypes type, final String processId);

	/**
	 * Calls the ExceptionReportDAO to get the lsit of exceptions by process id. Process id is often the
	 * property address id which is unique, but it could have had many exceptions along its journey.
	 * For example an address failure and a pdf failure, though usually will be just one.
	 *
	 * @param processId
	 * @return
	 */
	List<ExceptionReport> findExceptionReportsByProcessId (final String processId);

}
