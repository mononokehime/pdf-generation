package org.project.company.greenhomes.service.query;

import org.project.company.greenhomes.domain.entity.PropertyEPC;

import java.util.List;

public interface QueryEngineService {

	/**
	 * Finds the property results by the criteria set. Note that it is the caller
	 * responsibility to set the max number of results to return and also
	 * the scrolling start point. The scroll is done here rather than later on as we need
	 * the total result details too.
	 *
	 * @param holder
	 * @return
	 */
	QueryHolder findPropertiesForPDFsByCriteria (QueryHolder holder);

	List<PropertyEPC> findPropertyEPCListByPropertyKey (List<String> list);

}
