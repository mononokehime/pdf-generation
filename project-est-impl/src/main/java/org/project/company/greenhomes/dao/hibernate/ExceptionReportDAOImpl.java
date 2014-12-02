package org.project.company.greenhomes.dao.hibernate;

import org.project.company.greenhomes.dao.ExceptionReportDAO;
import org.project.company.greenhomes.domain.entity.ExceptionReport;

import java.io.Serializable;
import java.util.List;

public class ExceptionReportDAOImpl extends GenericDAOImpl<ExceptionReport, Long>
		implements ExceptionReportDAO, Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * the logger
	 */
	//private static final Logger log = LoggerFactory.getLogger(ExceptionReportDAOImpl.class);
	private static final String EXCEPTION_FROM_PROCESS_ID_SQL = "from ExceptionReport where processId = ?";

	/*
	 * (non-Javadoc)
	 * @see org.project.company.greenhomes.dao.ExceptionReportDAO#findExceptionReportByProcessId(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<ExceptionReport> findExceptionReportsByProcessId (String processId) {
		return getHibernateTemplate().find(EXCEPTION_FROM_PROCESS_ID_SQL, processId);
	}

}
