package org.project.company.greenhomes.service;

import org.project.company.greenhomes.common.enums.BatchTypes;
import org.project.company.greenhomes.domain.entity.Schedule;
import org.project.company.greenhomes.domain.entity.UploadSummary;
import org.project.company.greenhomes.exception.InvalidDataException;

import java.util.List;

public interface BatchSummaryAndScheduleService {

	UploadSummary findUploadSummaryById (long id);

	UploadSummary save (UploadSummary entity);

	List<UploadSummary> findLastXSummariesByType (BatchTypes batchType, Integer resultsToReturn);

	/**
	 * Method to find out from BatchSummaryDAO table how many rows have a specified
	 * upload id
	 *
	 * @param uploadId
	 * @return
	 */
	Integer findNumberOfResultsByUploadId (Long uploadSummaryId);

	/**
	 * Returns all the schedules that have a start date of today. Done by rolling the date forward to tomorrow
	 * and returning everything that is less than tomorrow. Calls batchandscheduledao
	 *
	 * @return
	 */
	List<Schedule> findSchedulesToRun ();

	/**
	 * Marks all the propertyaddress entities in PROPERTY_ADDRESS as completed
	 *
	 * @param scheduleId
	 * @param summary
	 */
	void markScheduleComplete (Long scheduleId, String summary);

	/**
	 * Calls the schedule dao and creates a new schedule, then updates the
	 * property entities to status "scheduled" and updates the schedule id
	 *
	 * @param ids
	 */
	void createScheduleAndUpdateProperties (List<String> ids, Schedule schedule) throws InvalidDataException;

	Schedule findScheduleByid (final Long scheduleId);

	/**
	 * Finds the last x schedules by calling the BatchSummaryAndScheduleDAO. Note the the scheduler name
	 * is also populated by calling UserService
	 *
	 * @param resultsToReturn
	 * @return
	 */
	List<Schedule> findLastXSchedules (Integer resultsToReturn);

}
