package org.project.company.greenhomes.service;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.project.company.greenhomes.common.enums.WorkFlowStatus;
import org.project.company.greenhomes.dao.PropertyDAO;
import org.project.company.greenhomes.domain.entity.*;
import org.project.company.greenhomes.exception.ApplicationException;
import org.project.company.greenhomes.exception.InvalidDataException;
import org.project.company.greenhomes.exception.NoMatchingDataException;

import java.util.List;
import java.util.Map;

public class PropertyServiceImpl implements PropertyService {

	/**
	 * the logger
	 */
	private static final Logger log = LoggerFactory.getLogger(PropertyServiceImpl.class);
	private PropertyDAO propertyDAO;
	private ExceptionReportService exceptionReportService;
	private ReferenceDataService referenceDataService;

	public PropertyServiceImpl (final ReferenceDataService referenceDataService,
			final ExceptionReportService exceptionReportService, final PropertyDAO propertyDAO) {
		if (null == referenceDataService) {
			throw new IllegalArgumentException("referenceDataService cannot be null.");
		}
		if (null == exceptionReportService) {
			throw new IllegalArgumentException("exceptionReportService cannot be null.");
		}
		if (null == propertyDAO) {
			throw new IllegalArgumentException("propertyDAO cannot be null.");
		}
		this.referenceDataService = referenceDataService;
		this.exceptionReportService = exceptionReportService;
		this.propertyDAO = propertyDAO;
	}

	/*
	 * (non-Javadoc)
	 * @see org.project.company.greenhomes.service.PropertyService#insertPropertySales(java.util.List)
	 */
	public void insertPropertySales (List<PropertySale> sales) {

		List<ExceptionReport> reports = propertyDAO.insertPropertySales(sales);
		// iterate through exception reports
		for (ExceptionReport report : reports) {
			// processing
			try {
				exceptionReportService.createExceptionReportRecord(report);
			} catch (ApplicationException e1) {
				// just log and continue here
				log.error("error", e1);
			}
		}
	}

	public PropertyAddress insertPropertyAddress (PropertyAddress sale) {
		ExceptionReport report = propertyDAO.insertPropertyAddress(sale);
		if (null != report) {
			try {
				exceptionReportService.createExceptionReportRecord(report);
			} catch (ApplicationException e1) {
				// just log and continue here
				// means it hasn't gone in for some reason
				sale.setWorkFlowStatus(WorkFlowStatus.FAILED.getValue());
				log.error("failed to save:" + sale, e1);
			}
		}
		return sale;
	}

	/*
	 * (non-Javadoc)
	 * @see org.project.company.greenhomes.service.PropertyService#insertPropertyEPC(java.util.List)
	 */
	public void insertPropertyEPC (List<PropertyEPC> sales) {

		List<ExceptionReport> reports = propertyDAO.insertPropertyEPC(sales);
		// iterate through exception reports
		for (ExceptionReport report : reports) {
			// processing
			try {
				exceptionReportService.createExceptionReportRecord(report);
			} catch (ApplicationException e1) {
				// just log and continue here
				log.error("error", e1);
			}
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.project.company.greenhomes.service.PropertyService#deleteAllPropertyEPC(java.util.List)
	 */
	public void deleteAllPropertyEPC (List<PropertyEPC> list) {
		propertyDAO.deleteAllPropertyEPC(list);

	}

	/*
	 * (non-Javadoc)
	 * @see org.project.company.greenhomes.service.PropertyService#deleteAllPropertySales(java.util.List)
	 */
	public void deleteAllPropertySales (List<PropertySale> list) {
		propertyDAO.deleteAllPropertySales(list);

	}

	public List<PropertyAddress> findPropertyResultsByCriteria (Map<String, Object> holder, Integer startPoint,
			Integer maxNumberOfResults) {
		return propertyDAO.findPropertyResultsByCriteria(holder, startPoint, maxNumberOfResults);
	}

	public void updatePropertyAddressWorkflow (final String addressId, final WorkFlowStatus newStatus,
			final WorkFlowStatus oldStatus) {
		propertyDAO.updatePropertyAddressWorkflow(addressId, newStatus, oldStatus);

	}

	/*
	 * (non-Javadoc)
	 * @see org.project.company.greenhomes.service.PropertyService#findPropertyEPCByScheduleId(java.lang.Long)
	 */
	public List<PropertyEPC> findPropertyEPCByScheduleId (Long scheduleId) {
		List<PropertyEPC> results = propertyDAO.findPropertyEPCByScheduleId(scheduleId);
		// now we need to iterate through and populate the centres info
		for (PropertyEPC epc : results) {
			// add the centre information
			Centre centre = referenceDataService.findCentreFromId(epc.getESTAC());
			epc.setCentre(centre);
		}
		return results;

	}

	public void updatePropertiesToSchedule (List<String> ids, Long scheduleId) {
		propertyDAO.updatePropertiesToSchedule(ids, scheduleId);
	}

	public List<PropertyAddress> findNotValidAddressesByUploadId (final Long processId, final Integer startPoint,
			final Integer numberToReturn) {
		List<PropertyAddress> results = propertyDAO
				.findNotValidAddressesByUploadId(processId, startPoint, numberToReturn);

		// now go through each one and find out what bit of data is invalid.
		// we need an address and an estac
		Object postcode[] = null;
		for (PropertyAddress address : results) {
			postcode = new Object[2];
			if (StringUtils.isNotEmpty(address.getPostcodeOutcode()) &&
					StringUtils.isNotEmpty(address.getPostcodeIncode())) {
				// find us some data!
				postcode[0] = address.getPostcodeOutcode();
				postcode[1] = address.getPostcodeIncode();
				// no point getting address info if duplicate
				if (address.getWorkFlowStatus().equals(WorkFlowStatus.INVALID.getValue())) {
					address.setPossibleAddressMatches(referenceDataService.findAddressesByPostCode(postcode));
				}

				// the rest of the data should be populated, and if not then not really an issue.
				// except for the centre which we can add on
				if (StringUtils.isEmpty(address.getESTAC() + "")) {
					// if empty then failed to get a centre so try to get one
					address.setCentre(referenceDataService.findCentreFromPostcode(postcode));
				} else {
					// get the centre information
					address.setCentre(referenceDataService.findCentreFromId(address.getESTAC()));
				}
			}
		}
		return results;
	}

	public Integer findNumberOfResultsforProcessId (Long processId) {
		return propertyDAO.findNumberOfResultsforProcessId(processId);
	}

	public PropertyEPC findPropertyEPCById (String pk) {
		PropertyEPC epc = propertyDAO.findPropertyEPCById(pk);
		// add the centre information

		Centre centre = referenceDataService.findCentreFromId(epc.getESTAC());
		//log.debug("centre"+centre.getCentreName());
		epc.setCentre(centre);
		return epc;
	}

	public List<PropertyAddress> findByCriteria (Map<String, Object> holder) {
		return propertyDAO.findByCriteria(holder);
	}

	/*
	 * (non-Javadoc)
	 * @see org.project.company.greenhomes.service.PropertyService#findMeasureByHeadingSummary(java.lang.String, java.lang.String)
	 */
	public Measure findMeasureByHeadingSummary (String summary, String heading)
			throws InvalidDataException, NoMatchingDataException {
		if (StringUtils.isBlank(summary) || StringUtils.isBlank(heading)) {
			String message = "summary is [" + summary + "] and heading is [" + heading + "]. Both must have value.";
			throw new InvalidDataException(message);
		}
		List<Measure> measures = propertyDAO.findMeasureByHeadingSummary(summary, heading);
		// if there is more than one then there is no proper match so throw an exception
		if (measures.size() == 1) {
			return measures.get(0);
		}
		String message =
				"Results returned: " + measures.size() + ", on data summary is [" + summary + "] and heading is ["
						+ heading + "]. Should be only one";
		throw new NoMatchingDataException(message);
	}

	/*
	 * (non-Javadoc)
	 * @see org.project.company.greenhomes.service.PropertyService#findAllMeasures()
	 */
	public List<Measure> findAllMeasures () {
		return propertyDAO.findAllMeasures();
	}

}
