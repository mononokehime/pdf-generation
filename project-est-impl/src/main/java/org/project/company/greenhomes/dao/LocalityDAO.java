package org.project.company.greenhomes.dao;

import org.project.company.greenhomes.domain.entity.Centre;
import org.project.company.greenhomes.domain.entity.FormattedAddressView;
import org.project.company.greenhomes.domain.model.LocationData;
import org.project.company.greenhomes.domain.model.ReferenceData;
import org.project.company.greenhomes.exception.NoMatchingDataException;

import java.util.List;

/**
 * DAO class for accessing the locality related tables spread over different instances.
 *
 *
 */
public interface LocalityDAO {

	/**
	 * Returns a list of EEACs from from sense.CENTRES where CEN_TYP_CENTRE_TYPE_ID
	 * where type is 1. May need re-visiting to make the number of centres fewer
	 *
	 * @return
	 */
	List<ReferenceData> findESTACListByCountry (final String countryCode);

	/**
	 * Gets a list of local authorities by country from ref_data.LOCAL_AUTHORITIES la, ref_data.postcode_data
	 *
	 * @param countryCode
	 * @return
	 */
	List<ReferenceData> findLAListByCountry (final String countryCode);

	/**
	 * Gets a complete list of segmentation data from ref_data.experian
	 *
	 * @return
	 */
	List<String> findSegmentationList ();

	LocationData findLocationData (final Object[] postcode) throws NoMatchingDataException;

	/**
	 * Queries ref_data.FORMATTED_ADDRESS_VIEW by building up a dynamic hql query from
	 * the parameters given.
	 *
	 * @param postcode
	 * @return
	 * @throws NoMatchingDataException
	 */
	String findAddressKey (final Object[] postcode) throws NoMatchingDataException;

	/**
	 * Queries ref_data.FORMATTED_ADDRESS_VIEW but only with the post code
	 *
	 * @param postcode
	 * @return
	 */
	List<FormattedAddressView> findAddressesByPostCode (final Object[] postcode);

	/**
	 * Gets an ESTAC when it is not available as part of the main finder. Queries sense.D1_GET_CEN_REG_FOR_POSTCODE
	 * Only used when user is looking at errors as the centres are usually populated with all the other
	 * lcoality information
	 *
	 * @param postcode
	 * @return
	 */
	Centre findCentreFromPostcode (final Object[] postcode);

	Centre findCentreFromId (final Integer centreCode);
}
