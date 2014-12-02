package org.project.company.greenhomes.service.query;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.project.company.greenhomes.common.enums.ReferenceDataTypes;
import org.project.company.greenhomes.dao.PropertyDAO;
import org.project.company.greenhomes.domain.entity.Centre;
import org.project.company.greenhomes.domain.entity.PropertyEPC;
import org.project.company.greenhomes.exception.NoMatchingDataException;
import org.project.company.greenhomes.service.ReferenceDataService;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

public class QueryEngineServiceImpl implements QueryEngineService {

	/**
	 * the logger
	 */
	private static final Logger log = LoggerFactory.getLogger(QueryEngineServiceImpl.class);
	private PropertyDAO propertyDAO;
	private ReferenceDataService referenceDataService;
	public QueryEngineServiceImpl (final ReferenceDataService referenceDataService,
			final PropertyDAO propertyDAO) {
		if (null == referenceDataService) {
			throw new IllegalArgumentException("referenceDataService cannot be null.");
		}
		if (null == propertyDAO) {
			throw new IllegalArgumentException("propertyDAO cannot be null.");
		}
		this.referenceDataService = referenceDataService;
		this.propertyDAO = propertyDAO;
	}

	/**
	 * Not to be used for paging use findPropertyEPCListByPropertyKey and pass the list of required keys
	 * the holder already has a totla list of keys
	 */
	public QueryHolder findPropertiesForPDFsByCriteria (QueryHolder holder) {
		// this is going to be a big one as needs to amalgamate two result sets
		// this can be done in a group function for the common elements
		// but when there are EPC specific things, then we need to filter those results
		// the holder contains all fields though
		// first of all get the generic results - note that these are
		// epc results - needs to be separate because we need total results
		Collection<String> tempList = propertyDAO.findPropertiesForPDFsByCriteria(holder.getGenericQuery());
		// this collection is actually a hashset to ensure uniqueness
		// this is the list of address keys.
		List<String> list = new ArrayList<String>(tempList);
		holder.setPropertyKeys(list);
		if (holder.getPropertyKeys().size() < 1) {
			return holder;
		}
		// this is the initial search page and paging is done in a different
		// method below (findPropertyEPCListByPropertyKey)
		// all we need to do is return either results required for page one
		// or all the results returned if that is smaller
		int startPoint = 0;
		float endPoint = holder.getResultsPerPage();
		if (endPoint > holder.getPropertyKeys().size()) {
			endPoint = holder.getPropertyKeys().size();
		}
		list = holder.getPropertyKeys().subList(startPoint, (int)endPoint);
		holder.setResults(findPropertyEPCListByPropertyKey(list));
		return holder;
	}

	public List<PropertyEPC> findPropertyEPCListByPropertyKey (List<String> list) {
		List<PropertyEPC> results = propertyDAO.findPropertyEPCListByPropertyKeyLazyFalse(list);
		// now we need to populate extra the extra data like
		// ESTAC name, country
		// iterate through the results
		for (PropertyEPC result : results) {
			// first get the country name
			try {
				result.setCountryName(referenceDataService
						.findPropertyShortNameByTypeAndKey(ReferenceDataTypes.COUNTRIES, result.getCountry()));
			} catch (NoMatchingDataException e) {
				// this is not good, but should not bring the whole query down
				log.error("error", e);
			}
			// now get the estac name
			try {
				result.setESTACName(referenceDataService
						.findPropertyShortNameByTypeAndKey(ReferenceDataTypes.ALL_ESTACS, result.getESTAC() + ""));
			} catch (NoMatchingDataException e) {
				// this is not good, but should not bring the whole query down
				//there seems to be a slight hole in the data for estacs by country
				// so try to find on the key itself
				Centre centre = referenceDataService.findCentreFromId(result.getESTAC());
				if (null == centre) {
					log.error("Unable to find centre details aswell", e);
				} else {
					result.setESTACName(centre.getCentreName());
					// and put in to ref data so populated in future.
					referenceDataService.addMissingESTAC(centre, result.getCountry());
				}

			}
			// now get the local authority name
			try {
				result.setLocalAuthorityName(referenceDataService
						.findPropertyShortNameByTypeAndKey(ReferenceDataTypes.ALL_LAS, result.getLocalAuthority()));
			} catch (NoMatchingDataException e) {
				// this is not good, but should not bring the whole query down
				log.error("error", e);
			}
			try {

				result.setSaleDate(
						propertyDAO.findPropertySaleDateForAddressKey(result.getAddressKey(), result.getScheduleId()));
			} catch (NoMatchingDataException e) {
				// this is not good, but should not bring the whole query down
				log.error("error", e);
			}

		}
		return results;
	}

}
