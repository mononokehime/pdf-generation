package org.project.company.greenhomes.service;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.project.company.greenhomes.common.enums.BatchTypes;
import org.project.company.greenhomes.common.enums.PropertyAttributeNames;
import org.project.company.greenhomes.common.enums.ReferenceDataTypes;
import org.project.company.greenhomes.common.enums.WorkFlowStatus;
import org.project.company.greenhomes.dao.LocalityDAO;
import org.project.company.greenhomes.domain.entity.*;
import org.project.company.greenhomes.domain.model.LocationData;
import org.project.company.greenhomes.domain.model.ReferenceData;
import org.project.company.greenhomes.exception.NoMatchingDataException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Class that holds values for reference data such as country, treatment and consumer segments
 * etc etc
 *
 *
 */
public class ReferenceDataServiceImpl implements ReferenceDataService {

	/**
	 * the logger
	 */
	private static final Logger log = LoggerFactory.getLogger(ReferenceDataServiceImpl.class);
	private LocalityDAO localityDAO;
	/*
	 * For reporting exceptions
	 */
	private ExceptionReportService exceptionReportService;
	private Map<String, List<ReferenceData>> estacs = new HashMap<String, List<ReferenceData>>();
	private Map<String, List<ReferenceData>> las = new HashMap<String, List<ReferenceData>>();
	private Map<ReferenceDataTypes, List<ReferenceData>> allData;

	public ReferenceDataServiceImpl (final LocalityDAO localityDAO,
			final ExceptionReportService exceptionReportService) {
		if (null == localityDAO) {
			throw new IllegalArgumentException("LocalityDAO cannot be null.");
		}
		if (null == exceptionReportService) {
			throw new IllegalArgumentException("exceptionReportService cannot be null.");
		}
		this.localityDAO = localityDAO;

		this.exceptionReportService = exceptionReportService;
	}

	/*
	 * (non-Javadoc)
	 * @see org.project.company.greenhomes.service.ReferenceDataService#getRolesMap()
	 */
	public Map<String, String> getRolesMap () {
		Map<String, String> roleMap = new HashMap<String, String>();
		roleMap.put("ROLE_SUPER_USER", "Super User");
		roleMap.put("ROLE_USER", "User");
		return roleMap;
	}

	/*
	 * (non-Javadoc)
	 * @see org.project.company.greenhomes.service.ReferenceDataService#findRatingsList()
	 */
	public List<ReferenceData> findRatingsList () {
		init();
		if (!allData.containsKey(ReferenceDataTypes.RATINGS)) {

			List<ReferenceData> ratings = new ArrayList<ReferenceData>();
			ReferenceData data = new ReferenceData();
			data.setReferenceDataKey("F,G");
			data.setLongName("F,G");
			data.setShortName("F,G");
			ratings.add(data);
			data = new ReferenceData();
			data.setReferenceDataKey("G");
			data.setLongName("G");
			data.setShortName("G");
			ratings.add(data);
			data = new ReferenceData();
			data.setReferenceDataKey("F");
			data.setLongName("F");
			data.setShortName("F");
			ratings.add(data);
			data = new ReferenceData();
			data.setReferenceDataKey("E");
			data.setLongName("E");
			data.setShortName("E");
			ratings.add(data);
			data = new ReferenceData();
			data.setReferenceDataKey("D");
			data.setLongName("D");
			data.setShortName("D");
			ratings.add(data);
			data = new ReferenceData();
			data.setReferenceDataKey("C");
			data.setLongName("C");
			data.setShortName("C");
			ratings.add(data);
			data = new ReferenceData();
			data.setReferenceDataKey("B");
			data.setLongName("B");
			data.setShortName("B");
			ratings.add(data);
			data = new ReferenceData();
			data.setReferenceDataKey("A");
			data.setLongName("A");
			data.setShortName("A");
			ratings.add(data);
			allData.put(ReferenceDataTypes.RATINGS, ratings);
		}
		return allData.get(ReferenceDataTypes.RATINGS);
	}

	/*
	 * (non-Javadoc)
	 * @see org.project.company.greenhomes.service.ReferenceDataService#findCountriesList()
	 */
	public List<ReferenceData> findCountriesList () {
		init();
		if (!allData.containsKey(ReferenceDataTypes.COUNTRIES)) {

			List<ReferenceData> countries = new ArrayList<ReferenceData>();
			ReferenceData data = new ReferenceData();
			data.setReferenceDataKey("064");
			data.setLongName("England");
			data.setShortName("England");
			countries.add(data);
			data = new ReferenceData();
			data.setReferenceDataKey("220");
			data.setLongName("Wales");
			data.setShortName("Wales");
			countries.add(data);
			data = new ReferenceData();
			data.setReferenceDataKey("179");
			data.setLongName("Scotland");
			data.setShortName("Scotland");
			countries.add(data);
			allData.put(ReferenceDataTypes.COUNTRIES, countries);
		}
		return allData.get(ReferenceDataTypes.COUNTRIES);
	}

	/*
	 * (non-Javadoc)
	 * @see org.project.company.greenhomes.service.ReferenceDataService#findAllTemplates()
	 */
	public List<ReferenceData> findAllTemplates () {
		init();
		if (!allData.containsKey(ReferenceDataTypes.TEMPLATES)) {

			List<ReferenceData> list = new ArrayList<ReferenceData>();
			ReferenceData data = new ReferenceData();
			data.setReferenceDataKey("pdfTemplateA");
			data.setLongName("pdfTemplateA");
			data.setShortName("pdfTemplateA");
			list.add(data);
			allData.put(ReferenceDataTypes.TEMPLATES, list);
		}
		return allData.get(ReferenceDataTypes.TEMPLATES);
	}

	/*
	 * (non-Javadoc)
	 * @see org.project.company.greenhomes.service.ReferenceDataService#findLAListByCountry()
	 */
	public List<ReferenceData> findLAListByCountry (String countryKey) throws NoMatchingDataException {
		init();
		if (null == las) {
			las = new HashMap<String, List<ReferenceData>>();
		}

		if (las.containsKey(countryKey)) {
			return las.get(countryKey);
		} else {
			List<ReferenceData> allLAs = allData.get(ReferenceDataTypes.ALL_LAS);
			if (null == allLAs) {
				allLAs = new ArrayList<ReferenceData>();
			}
			List<ReferenceData> results =
					localityDAO.findLAListByCountry(countryKey);
			allLAs.addAll(results);
			allData.put(ReferenceDataTypes.ALL_LAS, allLAs);
			las.put(countryKey, results);
			return results;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.project.company.greenhomes.service.ReferenceDataService#findESTACListByCountry()
	 */
	public List<ReferenceData> findESTACListByCountry (String countryKey) throws NoMatchingDataException {
		init();
		if (null == estacs) {
			estacs = new HashMap<String, List<ReferenceData>>();
		}
		if (estacs.containsKey(countryKey)) {
			return estacs.get(countryKey);
		} else {

			List<ReferenceData> allESTACs = allData.get(ReferenceDataTypes.ALL_ESTACS);
			if (null == allESTACs) {
				allESTACs = new ArrayList<ReferenceData>();
			}
			List<ReferenceData> results =
					localityDAO.findESTACListByCountry(countryKey);
			allESTACs.addAll(results);
			allData.put(ReferenceDataTypes.ALL_ESTACS, allESTACs);
			estacs.put(countryKey, results);
			return results;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.project.company.greenhomes.service.ReferenceDataService#findConsumerSegmentList()
	 */
	public List<ReferenceData> findConsumerSegmentList () {
		init();
		if (!allData.containsKey(ReferenceDataTypes.CONSUMER_SEGMENTS)) {
			List<ReferenceData> consumerSegments = new ArrayList<ReferenceData>();
			ReferenceData data = new ReferenceData();
			data.setReferenceDataKey("");
			data.setLongName("--");
			data.setShortName("--");
			consumerSegments.add(data);
			List<String> results =
					localityDAO.findSegmentationList();
			for (String result : results) {
				data = new ReferenceData();
				data.setLongName(result);
				data.setReferenceDataKey(result);
				data.setShortName(result);
				consumerSegments.add(data);
			}
			allData.put(ReferenceDataTypes.CONSUMER_SEGMENTS, consumerSegments);
		}
		return allData.get(ReferenceDataTypes.CONSUMER_SEGMENTS);
	}

	public String findPropertyShortNameByTypeAndKey (ReferenceDataTypes key, String id) throws NoMatchingDataException {
		// first of all get the reference data type
		init();
		if (allData.containsKey(key)) {

			List<ReferenceData> data = allData.get(key);
			// now iterate through and get the short name for 
			// that key
			for (ReferenceData ref : data) {
				//log.debug("ref key:"+ref.getReferenceDataKey()+", and id:"+id);
				if (ref.getReferenceDataKey().equals(id)) {
					return ref.getShortName();
				}
			}
		}
		// possible it has not been initialised yet...so
		throw new NoMatchingDataException("Unable to find any data for the" +
				" reference data type :" + key.getValue() + " and id:" + id);
	}

	private void init () {
		if (null == allData) {
			allData = new HashMap<ReferenceDataTypes, List<ReferenceData>>();
			findRatingsList();
			findCountriesList();
			findConsumerSegmentList();
			// do the call for estacs and las by country
			for (ReferenceData data : findCountriesList()) {
				try {
					findESTACListByCountry(data.getReferenceDataKey());
					findLAListByCountry(data.getReferenceDataKey());
				} catch (NoMatchingDataException e) {

					log.error("Unable to find ESTAC data for:" + data.getReferenceDataKey(), e);
				}
			}
		}

	}

	private String cleanValue (final String str) {
		if (StringUtils.isNotBlank(str)) {
			return str;
		} else {
			return "";
		}
	}

	public PropertyAddress populateLocationData (final PropertyAddress address) {

		Object[] postcode = new Object[5];
		postcode[0] = address.getPostcodeOutcode();
		postcode[1] = address.getPostcodeIncode();
		postcode[2] = cleanValue(address.getAddressLine1());
		postcode[3] = cleanValue(address.getAddressLine2());
		postcode[4] = cleanValue(address.getAddressLine3());
		LocationData data = null;

		try {
			// // annoyingly we need the address key which we can only get with the other 
			//bits of address information 
			data = localityDAO.findLocationData(postcode);
			address.setCountry(data.getRegion());
			address.setLocalAuthority(data.getLocalAuthority());
			address.setESTAC(data.getCentreCode());

			// the segmentation data 
			// we only need this if an epc
			if (address instanceof PropertyEPC) {
				if (StringUtils.isNotBlank(data.getEstSegmentDescription())) {
					PropertyAttribute attr = new PropertyAttribute();
					attr.setName(PropertyAttributeNames.estsegmentdescription.getValue());
					attr.setValue(data.getEstSegmentDescription());
					address.addPropertyAddressAttribute(attr);
				}
			}
			// set work flow status 1, which is beginning point
			address.setWorkFlowStatus(WorkFlowStatus.RECEIVED.getValue());
			// get the address as a different transaction do this last so the rest of the data can
			// be set

			address.setAddressKey(localityDAO.findAddressKey(postcode));
			return address;
		} catch (NoMatchingDataException e) {
			// mark as invalid
			address.setWorkFlowStatus(WorkFlowStatus.INVALID.getValue());
			String message = "Unable to find address key or estac for post code:" + postcode[0] +
					postcode[1] + " and upload id:" + address.getUploadId();
			BatchTypes batchType = null;
			if (address instanceof PropertySale) {
				batchType = BatchTypes.PROPERTY_SALES;
			}
			if (address instanceof PropertyEPC) {
				batchType = BatchTypes.EPC_REPORT;
			}
			exceptionReportService.processException(message, e, batchType, address.getPropertyAddressId());
			return address;
		} catch (IllegalArgumentException e) {
			address.setWorkFlowStatus(WorkFlowStatus.INVALID.getValue());
			String message = "Post code data not correct and upload id:" + address.getUploadId();
			;
			exceptionReportService
					.processException(message, e, BatchTypes.PROPERTY_SALES, address.getPropertyAddressId());
			return address;
		}
	}

	public void addMissingESTAC (Centre centre, String country) {
		// the data has already been init'd so we are jsut adding
		List<ReferenceData> allESTACs = allData.get(ReferenceDataTypes.ALL_ESTACS);
		ReferenceData data = new ReferenceData();
		data.setReferenceDataKey(centre.getCentreCode() + "");
		data.setShortName(centre.getCentreName());
		data.setLongName(centre.getCentreName());
		allESTACs.add(data);
		// now get the ones by country
		List<ReferenceData> list = estacs.get(country);
		list.add(data);
	}

	public List<FormattedAddressView> findAddressesByPostCode (Object[] postcode) {
		return localityDAO.findAddressesByPostCode(postcode);
	}

	public Centre findCentreFromPostcode (Object[] postcode) {

		return localityDAO.findCentreFromPostcode(postcode);
	}

	public Centre findCentreFromId (final Integer centreCode) {
		return localityDAO.findCentreFromId(centreCode);
	}

}
