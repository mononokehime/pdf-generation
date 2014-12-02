package org.project.company.greenhomes.service.query;

import org.project.company.greenhomes.domain.model.SearchParameters;
import org.project.company.greenhomes.test.common.BaseTestCase;

import java.util.ArrayList;
import java.util.List;

/**
 * Class to test the QueryHolder functions ok.
 *
 *
 */
public class QueryHolderTest extends BaseTestCase {

	public QueryHolderTest (String testName) {
		super(testName);
	}

	/**
	 * tests to see if search string are generated in the expected way
	 */
	public void testCreateSearchString () throws Exception {
		QueryHolder holder = new QueryHolder((float)20);
		SearchParameters params = new SearchParameters();
		params.setRating("F,G");
		params.setWorkflowStatus("0");
		params.setESTAC("72");
		params.setCountry("064");
		holder.setSearchParameters(params);

		assertNotNull(holder);
		assertNotNull(holder.getSearchString());
		String result = "?country=064&ESTAC=72&rating=F,G&workflowStatus=0";
		assertEquals(result, holder.getSearchString());
	}

	/**
	 * tests to see if no search string are generated from null search parameters
	 */
	public void testNullSearchString () throws Exception {
		QueryHolder holder = new QueryHolder((float)20);
		SearchParameters params = null;
		holder.setSearchParameters(params);
		assertNotNull(holder);
		assertNull(holder.getSearchString());
	}

	/**
	 * tests to see if no search string are generated from empty search parameters
	 */
	public void testEmptySearchString () throws Exception {
		QueryHolder holder = new QueryHolder((float)20);
		SearchParameters params = new SearchParameters();
		holder.setSearchParameters(params);
		assertNotNull(holder);
		assertNotNull(holder.getSearchString());
		String result = "?";
		assertEquals(result, holder.getSearchString());
	}

	/**
	 * tests to see if no search string are generated from empty search parameters
	 */
	public void testTotalPagesCalc () throws Exception {
		QueryHolder holder = new QueryHolder((float)5);
		List<String> propertyAddressIds = new ArrayList<String>(40);
		for (int i = 0; i < 40; i++) {
			propertyAddressIds.add(i + "");
		}
		holder.setPropertyKeys(propertyAddressIds);
		//return (int)Math.ceil(getTotalResults() / getMaxNumberOfResults());
		//holder.setTotalResults(40);
		assertEquals(8 + "", holder.getTotalPages() + "");

	}

	/**
	 * tests to see if no search string are generated from empty search parameters
	 */
	public void testTotalPagesNonStandardCalc () throws Exception {
		QueryHolder holder = new QueryHolder((float)5);
		List<String> propertyAddressIds = new ArrayList<String>(43);
		for (int i = 0; i < 43; i++) {
			propertyAddressIds.add(i + "");
		}
		holder.setPropertyKeys(propertyAddressIds);

		assertEquals(9 + "", holder.getTotalPages() + "");

	}

}
