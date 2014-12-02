package org.project.company.greenhomes.service;

import org.project.company.greenhomes.common.enums.WorkFlowStatus;
import org.project.company.greenhomes.domain.entity.Measure;
import org.project.company.greenhomes.domain.entity.PropertyAddress;
import org.project.company.greenhomes.domain.entity.PropertyEPC;
import org.project.company.greenhomes.domain.entity.PropertySale;
import org.project.company.greenhomes.exception.InvalidDataException;
import org.project.company.greenhomes.exception.NoMatchingDataException;

import java.util.List;
import java.util.Map;

public interface PropertyService {

	List<PropertyAddress> findByCriteria (Map<String, Object> holder);

	/**
	 * Passes the supplied list of property sales on to the property dao
	 * and creates an exception report if required.
	 *
	 * @param sales
	 */
	void insertPropertySales (List<PropertySale> sales);

	/**
	 * Passes the supplied property sale on to the property dao
	 * and creates an exception report if required.
	 *
	 * @param sales
	 */
	PropertyAddress insertPropertyAddress (PropertyAddress sale);

	/**
	 * Passes the supplied list of property epc on to the property dao
	 * and creates an exception report if required.
	 *
	 * @param sales
	 */
	void insertPropertyEPC (List<PropertyEPC> sales);

	/**
	 * deletes all property sales from all main table and child tables.
	 *
	 * @param list
	 */
	void deleteAllPropertySales (List<PropertySale> list);

	/**
	 * deletes all property epcs from all main table and child tables.
	 *
	 * @param list
	 */
	void deleteAllPropertyEPC (List<PropertyEPC> list);

	List<PropertyAddress> findPropertyResultsByCriteria (Map<String, Object> holder, Integer startPoint,
			Integer maxNumberOfResults);

	PropertyEPC findPropertyEPCById (final String pk);

	/**
	 * Note that this update works on the address id
	 *
	 * @param addressId
	 * @param status
	 */
	void updatePropertyAddressWorkflow (final String addressId, final WorkFlowStatus newStatus,
			final WorkFlowStatus oldStatus);

	List<PropertyEPC> findPropertyEPCByScheduleId (final Long scheduleId);

	void updatePropertiesToSchedule (final List<String> ids, final Long scheduleId);

	List<PropertyAddress> findNotValidAddressesByUploadId (final Long processId, final Integer startPoint,
			final Integer numberToReturn);

	Integer findNumberOfResultsforProcessId (final Long processId);

	/**
	 * When EPC report comes in we need to match the measures summary/heading against
	 * what we have in our database so that the text can be replaced with EST specific text
	 * rather than the default. This accesses the PropertyDAO to get the data. Could arguably be in the
	 * ReferenceDataService.
	 *
	 * @param summary
	 * @param heading
	 * @return
	 */
	Measure findMeasureByHeadingSummary (final String summary, final String heading)
			throws InvalidDataException, NoMatchingDataException;

	/**
	 * This method only used for generating test data
	 *
	 * @return
	 */
	List<Measure> findAllMeasures ();

}
