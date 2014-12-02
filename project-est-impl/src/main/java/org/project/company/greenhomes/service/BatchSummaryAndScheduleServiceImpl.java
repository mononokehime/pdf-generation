package org.project.company.greenhomes.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.project.company.greenhomes.common.enums.BatchTypes;
import org.project.company.greenhomes.dao.BatchSummaryAndScheduleDAO;
import org.project.company.greenhomes.domain.entity.Schedule;
import org.project.company.greenhomes.domain.entity.UploadSummary;
import org.project.company.greenhomes.domain.entity.User;
import org.project.company.greenhomes.exception.InvalidDataException;

import java.util.List;

public class BatchSummaryAndScheduleServiceImpl implements BatchSummaryAndScheduleService {

	/**
	 * the logger
	 */
	private static final Logger log = LoggerFactory.getLogger(BatchSummaryAndScheduleServiceImpl.class);
	private BatchSummaryAndScheduleDAO batchSummaryDAO;
	private PropertyService propertyService;
	private UserService userService;

	public BatchSummaryAndScheduleServiceImpl (final PropertyService propertyService,
			final UserService userService, final BatchSummaryAndScheduleDAO batchSummaryDAO) {
		if (null == propertyService) {
			throw new IllegalArgumentException("propertyService cannot be null.");
		}
		if (null == userService) {
			throw new IllegalArgumentException("userService cannot be null.");
		}
		if (null == batchSummaryDAO) {
			throw new IllegalArgumentException("batchSummaryDAO cannot be null.");
		}
		this.propertyService = propertyService;
		this.userService = userService;
		this.batchSummaryDAO = batchSummaryDAO;
	}

	public UploadSummary save (UploadSummary entity) {
		try {
			return batchSummaryDAO.save(entity);
		} catch (org.hibernate.exception.DataException e) {
			String message = "Could not save update!" + entity;
			log.error(message, e);
		}
		return entity;

	}

	public List<UploadSummary> findLastXSummariesByType (BatchTypes batchType, Integer resultsToReturn) {
		// we should get number of results related to each upload
		//	List<UploadSummary> results = batchSummaryDAO.findLastXSummariesByType(batchType, resultsToReturn);

		return batchSummaryDAO.findLastXSummariesByType(batchType, resultsToReturn);
	}

	/*
	 * (non-Javadoc)
	 * @see org.project.company.greenhomes.service.BatchSummaryService#findNumberOfResultsByUploadId(java.lang.Long)
	 */
	public Integer findNumberOfResultsByUploadId (Long uploadId) {
		return batchSummaryDAO.findNumberOfResultsByUploadId(uploadId);
	}

	/*
	 * (non-Javadoc)
	 * @see org.project.company.greenhomes.service.BatchSummaryAndScheduleService#findSchedulesToRun()
	 */
	public List<Schedule> findSchedulesToRun () {
		return batchSummaryDAO.findSchedulesToRun();
	}

	public void markScheduleComplete (Long scheduleId, String summary) {

		batchSummaryDAO.markScheduleComplete(scheduleId, summary);
	}

	/*
	 * (non-Javadoc)
	 * @see org.project.company.greenhomes.service.BatchSummaryAndScheduleService#createScheduleAndUpdateProperties(java.util.List, org.project.company.greenhomes.domain.entity.Schedule)
	 */
	public void createScheduleAndUpdateProperties (List<String> ids, Schedule schedule) throws InvalidDataException {
		// first of all create a new schedule
		schedule = batchSummaryDAO.createNewSchedule(schedule);
		// now update the ids. There could be lots here.
		if (log.isDebugEnabled()) {
			log.debug("going to update [" + ids.size() + "] records");
		}
		propertyService.updatePropertiesToSchedule(ids, schedule.getScheduleId());

	}

	public Schedule findScheduleByid (final Long scheduleId) {
		Schedule schedule = batchSummaryDAO.findScheduleByid(scheduleId);
		//populate the name of who did it
		User user = userService.findUserByUserid(schedule.getScheduleById());
		schedule.setScheduledByName(
				user.getFirstName() + " " + user.getFamilyName() + " (" + user.getUsername() + ").");
		return schedule;
	}

	/*
	 * (non-Javadoc)
	 * @see org.project.company.greenhomes.service.BatchSummaryAndScheduleService#findLastXSchedules(java.lang.Integer)
	 */
	public List<Schedule> findLastXSchedules (Integer resultsToReturn) {
		// populate the user names
		List<Schedule> schedules = batchSummaryDAO.findLastXSchedules(resultsToReturn);
		for (Schedule schedule : schedules) {
			User user = userService.findUserByUserid(schedule.getScheduleById());
			schedule.setScheduledByName(
					user.getFirstName() + " " + user.getFamilyName() + " (" + user.getUsername() + ").");

		}
		return schedules;
	}

	public UploadSummary findUploadSummaryById (long id) {

		return batchSummaryDAO.findUploadSummaryById(id);
	}

}
