package org.project.company.greenhomes.dao.hibernate;

import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.Projections;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.project.company.greenhomes.common.enums.BatchTypes;
import org.project.company.greenhomes.common.util.DateFormatter;
import org.project.company.greenhomes.dao.BatchSummaryAndScheduleDAO;
import org.project.company.greenhomes.domain.entity.PropertyAddress;
import org.project.company.greenhomes.domain.entity.Schedule;
import org.project.company.greenhomes.domain.entity.UploadSummary;
import org.project.company.greenhomes.exception.InvalidDataException;

import java.io.Serializable;
import java.security.InvalidParameterException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class BatchSummaryAndScheduleDAOImpl extends GenericDAOImpl<UploadSummary, Long>
		implements BatchSummaryAndScheduleDAO, Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * Query from oracle for top n results. Simply doing rownum fails. http://www.oracle.com/technology/oramag/oracle/06-Sep/056asktom.html
	 */
	private final String LAST_X_SUMMARY_SQL = "select UPLOAD_SUMMARY_ID from " +
			"(select * from GREEN_HOMES.UPLOAD_SUMMARY where UPLOAD_TYPE = ? " +
			"order by START_TIME desc) " +
			"where rownum <= ? ";
	private final String FIND_SCHEDULE_TO_RUN_SQL = "from Schedule where startDate < ? and endDate is null";
	/**
	 * the logger
	 */
	//private static final Logger log = LoggerFactory.getLogger(BatchSummaryDAOImpl.class);

	private JdbcTemplate jdbcTemplate;

	public void setJdbcTemplate (JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	/*
	 * (non-Javadoc)
	 * @see org.project.company.greenhomes.dao.BatchSummaryDAO#findLastXSummariesByType(org.project.company.greenhomes.common.enums.BatchTypes, java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	public List<UploadSummary> findLastXSummariesByType (BatchTypes batchType, Integer resultsToReturn) {
		if (null == batchType || null == resultsToReturn || resultsToReturn < 1) {
			String message = "batchType = " + batchType + ", resultsToReturn:" + resultsToReturn + " " +
					" both parameters must have a value.";
			throw new InvalidParameterException(message);
		}
		Object[] params = new Object[2];
		params[0] = batchType.getValue();
		params[1] = resultsToReturn;
		List<Long> results = jdbcTemplate.queryForList(LAST_X_SUMMARY_SQL, params, Long.class);
		if (results.isEmpty()) {
			// return an empty list
			return new ArrayList<UploadSummary>();
		}
		// now load the values up
		//List<UploadSummary> summaries = new ArrayList<UploadSummary>();
		DetachedCriteria query = DetachedCriteria.forClass(UploadSummary.class);
		query.add(Expression.in("uploadSummaryId", results));
		query.addOrder(Order.desc("startTime"));
		return getHibernateTemplate().findByCriteria(query);
	}

	/*
	 * (non-Javadoc)
	 * @see org.project.company.greenhomes.dao.BatchSummaryDAO#findNumberOfResultsByUploadId(java.lang.Long)
	 */
	public Integer findNumberOfResultsByUploadId (Long uploadId) {
		if (null == uploadId || uploadId < 1) {
			String message = "uploadId = " + uploadId +
					" parameter must have a value.";
			throw new InvalidParameterException(message);
		}
		DetachedCriteria query = DetachedCriteria.forClass(PropertyAddress.class);
		query.add(Expression.eq("uploadId", uploadId));
		query.setProjection(Projections.rowCount());
		return DataAccessUtils.intResult(getHibernateTemplate().findByCriteria(query));
	}

	/*
	 * (non-Javadoc)
	 * @see org.project.company.greenhomes.dao.BatchSummaryAndScheduleDAO#findSchedulesToRun()
	 */
	@SuppressWarnings("unchecked")
	public List<Schedule> findSchedulesToRun () {

		Date date = DateFormatter.getDateForScheduleSearch();
		return getHibernateTemplate().find(FIND_SCHEDULE_TO_RUN_SQL, date);
	}

	/*
	 * (non-Javadoc)
	 * @see org.project.company.greenhomes.dao.BatchSummaryAndScheduleDAO#markScheduleComplete(java.lang.Long, java.lang.String)
	 */
	public void markScheduleComplete (Long scheduleId, String summary) {
		// get the schedule
		Schedule result = (Schedule)getHibernateTemplate().load(Schedule.class, scheduleId);
		result.setEndDate(new Date());
		result.setScheduleSummary(summary);
		getHibernateTemplate().saveOrUpdate(result);
		getHibernateTemplate().flush();
	}

	/*
	 * (non-Javadoc)
	 * @see org.project.company.greenhomes.dao.BatchSummaryAndScheduleDAO#createNewSchedule(org.project.company.greenhomes.domain.entity.Schedule)
	 */
	public Schedule createNewSchedule (Schedule schedule) throws InvalidDataException {
		if (null == schedule || null == schedule.getRequestDate() || null == schedule.getScheduleById()
				|| null == schedule.getTemplateId() || null == schedule.getStartDate()) {
			String messages = "One of the required values is null:" + schedule;
			throw new InvalidDataException(messages);
		}
		getHibernateTemplate().saveOrUpdate(schedule);
		return schedule;
	}

	/*
	 * (non-Javadoc)
	 * @see org.project.company.greenhomes.dao.BatchSummaryAndScheduleDAO#findScheduleByid(java.lang.Long)
	 */
	public Schedule findScheduleByid (final Long scheduleId) {

		return (Schedule)getHibernateTemplate().get(Schedule.class, scheduleId);
	}

	/*
	 * (non-Javadoc)
	 * @see org.project.company.greenhomes.dao.BatchSummaryAndScheduleDAO#findLastXSchedules(java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	public List<Schedule> findLastXSchedules (Integer resultsToReturn) {
		DetachedCriteria query = DetachedCriteria.forClass(Schedule.class);
		query.addOrder(Order.desc("scheduleId"));
		return getHibernateTemplate().findByCriteria(query, 0, resultsToReturn);
	}

	public UploadSummary findUploadSummaryById (Long id) {
		return (UploadSummary)getHibernateTemplate().get(UploadSummary.class, id);
	}

}
