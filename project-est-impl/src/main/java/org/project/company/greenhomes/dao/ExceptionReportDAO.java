package org.project.company.greenhomes.dao;

import org.project.company.greenhomes.domain.entity.ExceptionReport;

import java.util.List;

/**
 * DAO class for accessing the exception table, and writing exceptions
 *
 *
 */
public interface ExceptionReportDAO extends GenericDAO<ExceptionReport, Long> {

	/**
	 * Finds the details of an exception from the supplied process id, This could be
	 * a list as the same process id could through up a number of possible exceptions.
	 * Process id is likely to be property address id
	 *
	 * @param processId
	 * @return
	 */
	List<ExceptionReport> findExceptionReportsByProcessId (final String processId);

}
