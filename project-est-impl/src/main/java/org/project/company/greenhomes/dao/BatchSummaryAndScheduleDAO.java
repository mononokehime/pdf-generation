package org.project.company.greenhomes.dao;

import org.project.company.greenhomes.common.enums.BatchTypes;
import org.project.company.greenhomes.domain.entity.Schedule;
import org.project.company.greenhomes.domain.entity.UploadSummary;
import org.project.company.greenhomes.exception.InvalidDataException;

import java.util.List;

/**
 * DAO class for accessing the SCHEDULES and UPLOAD_SUMMARY table
 *
 *
 */
public interface BatchSummaryAndScheduleDAO extends GenericDAO<UploadSummary, Long> {

	/**
	 * Uses the jdbcTemplate to return a list of the last x number of
	 * upload summaries.
	 *
	 * @param batchType
	 * @param resultsToReturn
	 * @return
	 */
	List<UploadSummary> findLastXSummariesByType (BatchTypes batchType, Integer resultsToReturn);

	/**
	 * Method to find out from PROPERTY_SALES table how many rows have a specified
	 * upload id uses projections to get row count
	 *
	 * @param uploadId
	 * @return
	 */
	Integer findNumberOfResultsByUploadId (Long uploadId);

	/**
	 * Returns all the schedules that have a start date of today. Done by rolling the date forward to tomorrow
	 * and returning everything that is less than tomorrow.
	 *
	 * @return
	 */
	List<Schedule> findSchedulesToRun ();

	/**
	 * Marks the schedule complete on the schedules table. Sets the date to date now.
	 *
	 * @param scheduleId
	 * @param summary
	 */
	void markScheduleComplete (Long scheduleId, String summary);

	/**
	 * Creates a new schedule that will run in the evening specified date. Update SUMMARY table.
	 * Start date request date, scheduled by and template id cannot be null
	 *
	 * @param schedule
	 * @return
	 * @throws InvalidDataException when one of values is null
	 */
	Schedule createNewSchedule (Schedule schedule) throws InvalidDataException;

	/**
	 * Return a schedule from by id
	 *
	 * @param scheduleId
	 * @return
	 */
	Schedule findScheduleByid (final Long scheduleId);

	/**
	 * Uses DetachedCriteria to find the last x number of schedules. X is set in the application
	 * properties file and we access the summary table.
	 *
	 * @param resultsToReturn
	 * @return
	 */
	List<Schedule> findLastXSchedules (Integer resultsToReturn);

	UploadSummary findUploadSummaryById (final Long id);
}
