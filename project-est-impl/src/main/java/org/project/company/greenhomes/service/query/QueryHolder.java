package org.project.company.greenhomes.service.query;

import org.apache.commons.lang.StringUtils;
import org.project.company.greenhomes.domain.entity.PropertyEPC;
import org.project.company.greenhomes.domain.model.SearchParameters;

import java.io.Serializable;
import java.util.List;
import java.util.Map;

public class QueryHolder implements Serializable {
	/**
	 * We need to maintain this so the search can be editted
	 */
	private SearchParameters searchParameters;
	/**
	 * The number of results to return
	 */
	private List<PropertyEPC> results;
	/**
	 * The property address ids to search on
	 * This value gives us the total number of results for a query
	 */
	private List<String> propertyKeys;
	/**
	 * Map to build up generic query. The map contains the field to search on
	 */
	private Map<DBFieldNames, Object> genericQuery;
	/**
	 * Used for paging. Indicates the current page
	 */
	private Float currentPage = new Float(0);
	/**
	 * The maximum number of results to display on a page
	 */
	private Float resultsPerPage;
	/**
	 * Useful method that build a search string for the jsp based on the values in
	 * searchParameters
	 */
	private String searchString;
	/**
	 * The total number of results in the propertyAddressIds expressed as a
	 * int so the jsp can call this method for display
	 */
	private Float totalResults;
	/**
	 * Total number of pages
	 */
	private Integer totalPages;

	/**
	 * Constructor that forces the max number of results to be set on creation
	 *
	 * @param maxNumberOfResults
	 */
	public QueryHolder (Float resultsPerPage) {
		this.resultsPerPage = resultsPerPage;
	}

	public List<PropertyEPC> getResults () {
		return results;
	}

	public void setResults (List<PropertyEPC> results) {
		this.results = results;
	}

	public String toString () {
		StringBuilder sb = new StringBuilder();
		sb.append("maxNumberOfResults:" + resultsPerPage);
		sb.append("currentPage:" + currentPage);
		if (null != propertyKeys) {
			sb.append("addressKeys:" + propertyKeys.size());
		}
		if (null != results) {
			sb.append("results:" + results.size());
		}
		return sb.toString();
	}

	public Map<DBFieldNames, Object> getGenericQuery () {
		return genericQuery;
	}

	public void setGenericQuery (Map<DBFieldNames, Object> genericQuery) {
		this.genericQuery = genericQuery;
	}

	public Float getCurrentPage () {
		return currentPage;
	}

	public void setCurrentPage (Float currentPage) {
		this.currentPage = currentPage;
	}

	public SearchParameters getSearchParameters () {
		return searchParameters;
	}

	public void setSearchParameters (SearchParameters searchParameters) {
		this.searchParameters = searchParameters;
	}

	/**
	 * Builds up a query string for jsp usage populated by the search parameters
	 *
	 * @return
	 */
	public String getSearchString () {

		if (null == this.searchParameters) {
			return null;
		}
		StringBuilder sb = new StringBuilder();
		sb.append("?");
		if (StringUtils.isNotBlank(searchParameters.getConsumerSegment())) {
			sb.append("consumerSegment=" + searchParameters.getConsumerSegment() + "&");
		}
		if (StringUtils.isNotBlank(searchParameters.getCountry())) {
			sb.append("country=" + searchParameters.getCountry() + "&");
		}
		if (StringUtils.isNotBlank(searchParameters.getESTAC())) {
			sb.append("ESTAC=" + searchParameters.getESTAC() + "&");
		}
		if (StringUtils.isNotBlank(searchParameters.getFromDate())) {
			sb.append("fromDate=" + searchParameters.getFromDate() + "&");
		}
		if (StringUtils.isNotBlank(searchParameters.getRating())) {
			sb.append("rating=" + searchParameters.getRating() + "&");
		}
		if (StringUtils.isNotBlank(searchParameters.getToDate())) {
			sb.append("toDate=" + searchParameters.getToDate() + "&");
		}
		if (StringUtils.isNotBlank(searchParameters.getTreatment())) {
			sb.append("treatment=" + searchParameters.getTreatment() + "&");
		}
		if (StringUtils.isNotBlank(searchParameters.getWorkflowStatus())) {
			sb.append("workflowStatus=" + searchParameters.getWorkflowStatus() + "&");
		}
		// no real way of knowing when the first last parameter
		// is being called, so strip off the final & also need to check to make sure it exists
		// in case an empty search param was put in
		// first convert to String
		searchString = sb.toString();
		if (searchString.indexOf("&") > -1) {
			searchString = searchString.substring(0, searchString.lastIndexOf("&"));
		}
		return searchString;
	}

	public void setSearchString (String searchString) {
		this.searchString = searchString;
	}

	public Float getTotalResults () {
		if (null == propertyKeys || propertyKeys.isEmpty()) {
			return (float)0;
		}
		return (float)propertyKeys.size();
	}

	public void setTotalResults (Float totalResults) {
		this.totalResults = totalResults;
	}

	public Integer getTotalPages () {
		return (int)Math.ceil(getTotalResults() / getResultsPerPage());

	}

	public void setTotalPages (Integer totalPages) {
		this.totalPages = totalPages;
	}

	public List<String> getPropertyKeys () {
		return propertyKeys;
	}

	public void setPropertyKeys (List<String> propertyKeys) {
		this.propertyKeys = propertyKeys;
	}

	public Float getResultsPerPage () {
		return resultsPerPage;
	}

	public void setResultsPerPage (Float resultsPerPage) {
		this.resultsPerPage = resultsPerPage;
	}

	/*
	 * Use to keep the field names constant for the database columns
	 * These are used to build up the sql query
	 */
	public enum DBFieldNames {
		/**
		 * Value for form date field name
		 */
		TO_DATE("TO_DATE"),
		/**
		 * Value for form post code field name
		 */
		POSTCODE_INCODE("pa.postcode_incode"),
		/**
		 * Value for form post code field name
		 */
		POSTCODE_OUTCODE("pa.postcode_outcode"),
		/**
		 * Value for form country field name
		 */
		COUNTRY("pa.COUNTRY"),
		/**
		 * Value for form ESTAC field name
		 */
		ESTAC("pa.ESTAC"),
		/**
		 * Value for work flow
		 */
		WORKFLOW("pa.WORK_FLOW_STATUS"),
		/**
		 * Value for form segment field name
		 */
		SEGMENTATION("paa.NAME"),
		/**
		 * Value for form segment field value
		 */
		SEGMENTATION_VALUE("paa.VALUE"),
		/**
		 * Value for form segment field value
		 */
		SCHEDULE_ID("SCHEDULE_ID"),
		//	/**
		//	 * Value for form treatment field name
		//	 */
		//	TREATMENT ("epc.TREATMENT"),
		/**
		 * Value for form epc rating field name
		 */
		EPC_RATING("epc.ENERGY_RATING_CURRENT"),
		/**
		 * Value for form local authority  field name
		 */
		LOCAL_AUTHORITY("pa.LOCAL_AUTHORITY"),
		/**
		 * Value for form date field name
		 */
		FROM_DATE("FROM_DATE");
		/**
		 * value variable
		 */
		private final String value;

		DBFieldNames (String serviceName) {
			value = serviceName;
		}

		/**
		 * @return the value
		 */
		public String getValue () {
			return value;
		}
	}

}
