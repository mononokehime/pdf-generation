package org.project.company.greenhomes.service;

import org.project.company.greenhomes.common.enums.ReferenceDataTypes;
import org.project.company.greenhomes.domain.entity.Centre;
import org.project.company.greenhomes.domain.entity.FormattedAddressView;
import org.project.company.greenhomes.domain.entity.PropertyAddress;
import org.project.company.greenhomes.domain.model.ReferenceData;
import org.project.company.greenhomes.exception.NoMatchingDataException;

import java.util.List;
import java.util.Map;

/**
 * Service class for the controllers that returns static data
 *
 *
 */
public interface ReferenceDataService {

	/**
	 * Returns a list of roles in the system
	 *
	 * @return
	 */
	Map<String, String> getRolesMap ();

	//LocationData findLocationData(Object[] postcode)throws NoMatchingDataException;

	/**
	 * Adds all the required location data to the property sale
	 *
	 * @param sale
	 */
	PropertyAddress populateLocationData (PropertyAddress address);

	List<FormattedAddressView> findAddressesByPostCode (Object[] postcode);

	//
	//	/**
	//	 * Returns the country and ESTAC that a post code is part of. Calls {@link LocalityDAO}.
	//	 * @param postcode
	//	 * @return
	//	 * @throws NoMatchingDataException
	//	 * @throws ApplicationException
	//	 */
	//	SenseLocalityData findCountryAndESTAC(Object[] postcode) throws NoMatchingDataException, ApplicationException;
	//	/**
	//	 * Finds the local authority code for a given post code. Calls {@link LocalityDAO}.
	//	 * @param postcode
	//	 * @return
	//	 * @throws NoMatchingDataException
	//	 * @throws ApplicationException
	//	 */
	//	String findLocalAuthorityCodeByPostcode(Object[] postcode) throws NoMatchingDataException, ApplicationException;
	//
	//	/**
	//	 * Returns all the Consumer segment data for a given post code. Calls {@link LocalityDAO}.
	//	 * @param postcode
	//	 * @return
	//	 * @throws NoMatchingDataException
	//	 * @throws ApplicationException
	//	 */
	//	SegmentationData findSegmentationDescription(Object[] postcode)throws NoMatchingDataException, ApplicationException;
	//
	//	/**
	//	 * Returns the address key. Calls {@link LocalityDAO}.
	//	 * @param postcode
	//	 * @return
	//	 * @throws NoMatchingDataException
	//	 * @throws ApplicationException
	//	 */
	//	LocationData findLocationDetails(PropertyAddress address)throws NoMatchingDataException, ApplicationException;
	//

	Centre findCentreFromPostcode (Object[] postcode);

	/**
	 * Returns a list of {@list ReferenceData} objects for epc ratings, currently hard coded G-A
	 *
	 * @return
	 */
	List<ReferenceData> findRatingsList ();
	//	/**
	//	 * Returns a list of {@list ReferenceData} objects for epc treatments, currently hard coded
	//	 * @return
	//	 */
	//	List<ReferenceData> findTreatmentsList();

	/**
	 * Returns a list of {@list ReferenceData} objects for countries, currently hard coded
	 * but the values are from on ref_data.countries
	 *
	 * @return
	 */
	List<ReferenceData> findCountriesList ();

	/**
	 * Returns a list of {@list ReferenceData} objects for templates, currently hard coded
	 *
	 * @return
	 */
	List<ReferenceData> findAllTemplates ();

	/**
	 * Returns a list of {@list ReferenceData} objects for estacs from
	 * sense.D1_GET_CEN_REG_OLD
	 *
	 * @return
	 */
	List<ReferenceData> findESTACListByCountry (final String countryKey) throws NoMatchingDataException;

	List<ReferenceData> findLAListByCountry (final String countryKey) throws NoMatchingDataException;

	/**
	 * Returns a list of {@list ReferenceData} objects for consumer segments, currently hard coded
	 * but the values are from on ref_data.countries
	 *
	 * @return
	 */
	List<ReferenceData> findConsumerSegmentList ();

	/**
	 * Finds the short name for the given {@link ReferenceDataTypes} and key combo
	 * For most of the reference data it is stored in a big mapped keyed on {@link ReferenceDataTypes}
	 * for easy access
	 *
	 * @param key
	 * @param id
	 * @return
	 * @throws NoMatchingDataException
	 */
	String findPropertyShortNameByTypeAndKey (final ReferenceDataTypes key, final String id)
			throws NoMatchingDataException;

	Centre findCentreFromId (final Integer centreCode);

	void addMissingESTAC (final Centre centre, final String country);

}
