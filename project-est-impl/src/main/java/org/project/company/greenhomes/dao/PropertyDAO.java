package org.project.company.greenhomes.dao;

import org.project.company.greenhomes.common.enums.WorkFlowStatus;
import org.project.company.greenhomes.domain.entity.*;
import org.project.company.greenhomes.exception.NoMatchingDataException;
import org.project.company.greenhomes.service.query.QueryHolder.DBFieldNames;

import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * DAO class for accessing the property_sales table
 *
 *
 */
public interface PropertyDAO extends GenericDAO<PropertyAddress, String> {
	/**
	 * Inserts data relating to property sale in to the property_address/
	 * property_address_attribute/property_sales table.
	 *
	 * @param sale
	 * @return exception to save to the db for reporting later
	 */
	ExceptionReport insertPropertyAddress (PropertyAddress sale);

	/**
	 * Inserts data relating to property sales in to the property_address/
	 * property_address_attribute/property_sales table.
	 *
	 * @param sales
	 * @return list of exceptions to save to the db for reporting later
	 */
	List<ExceptionReport> insertPropertySales (List<PropertySale> sales);

	/**
	 * Inserts data relating to received epc reports in to the property_address/
	 * property_address_attribute/property_epc table
	 *
	 * @param sales
	 * @return list of exceptions to save to the db for reporting later
	 */
	List<ExceptionReport> insertPropertyEPC (List<PropertyEPC> sales);

	/**
	 * Finds the property address results by the criteria specified in the holder from the
	 * property_address table. Can do scrolling sets but caller must set a start point
	 * and also max results. Not sure if there is a use for this.
	 *
	 * @param holder
	 * @return
	 */
	List<PropertyAddress> findPropertyResultsByCriteria (Map<String, Object> holder, Integer startPoint,
			Integer maxNumberOfResults);

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

	/**
	 * Returns the complete number of results that fit the criteria, populated with property address id
	 *
	 * @param criteria
	 * @return
	 */
	Collection<String> findPropertiesForPDFsByCriteria (final Map<DBFieldNames, Object> criteria);

	/**
	 * Returns a populated list of {@llink PropertyEPC} based on the list of
	 * propertyaddressids given. Note that collections have been init'd
	 *
	 * @param array
	 * @return
	 */
	List<PropertyEPC> findPropertyEPCListByPropertyKeyLazyFalse (final List<String> array);

	PropertyEPC findPropertyEPCById (final String pk);

	void updatePropertyAddressWorkflow (final String addressId, final WorkFlowStatus newStatus,
			final WorkFlowStatus oldStatus);

	List<PropertyEPC> findPropertyEPCByScheduleId (final Long scheduleId);

	void updatePropertiesToSchedule (List<String> ids, Long scheduleId);

	/**
	 * Gets all the property addresses that are errors. That is, not received, scheduled or completed
	 *
	 * @param processId
	 * @param startPoint
	 * @param numberToReturn
	 * @return
	 */
	List<PropertyAddress> findNotValidAddressesByUploadId (final Long processId, final Integer startPoint,
			final Integer numberToReturn);

	Integer findNumberOfResultsforProcessId (Long processId);

	List<PropertyAddress> findByCriteria (Map<String, Object> holder);

	/**
	 * Accesses the MEASURES table to get the measure by the parameters set
	 *
	 * @param summary this is not the est summary
	 * @param heading this is not the est summary
	 * @return
	 */
	List<Measure> findMeasureByHeadingSummary (final String summary, final String heading);

	/**
	 * This method only used for generating test data
	 *
	 * @return
	 */
	List<Measure> findAllMeasures ();

	Date findPropertySaleDateForAddressKey (String addressKey, Long scheduleId) throws NoMatchingDataException;

}
