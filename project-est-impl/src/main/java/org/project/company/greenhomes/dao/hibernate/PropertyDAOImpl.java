package org.project.company.greenhomes.dao.hibernate;

import org.apache.commons.lang.StringUtils;
import org.hibernate.HibernateException;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.DataAccessException;
import org.springframework.jdbc.core.JdbcTemplate;
import org.project.company.greenhomes.common.enums.BatchTypes;
import org.project.company.greenhomes.common.enums.PropertyAttributeNames;
import org.project.company.greenhomes.common.enums.WorkFlowStatus;
import org.project.company.greenhomes.common.util.MathsHelper;
import org.project.company.greenhomes.dao.PropertyDAO;
import org.project.company.greenhomes.dao.mapper.PropertyAddressMapper;
import org.project.company.greenhomes.domain.entity.*;
import org.project.company.greenhomes.exception.NoMatchingDataException;
import org.project.company.greenhomes.service.query.QueryHolder.DBFieldNames;

import java.io.Serializable;
import java.util.*;

public class PropertyDAOImpl extends GenericDAOImpl<PropertyAddress, String> implements PropertyDAO, Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * the logger
	 */
	private static final Logger log = LoggerFactory.getLogger(PropertyDAOImpl.class);

	private static final String findPropertyEPCByScheduleId = "from PropertyEPC where scheduleId = ? ";
	private static final String MEASURES_BY_SUMMARY_HEADING_SQL = "from Measure where summary = ? and heading = ? ";

	private int batchSize;
	private JdbcTemplate jdbcTemplate;

	public void setBatchSize (int batchSize) {
		this.batchSize = batchSize;
	}

	/*
	 * (non-Javadoc)
	 * @see org.project.company.greenhomes.dao.PropertySalesDAO#insertPropertySales(java.util.List)
	 */
	public List<ExceptionReport> insertPropertySales (List<PropertySale> sales) {
		if (log.isDebugEnabled()) {
			log.debug("going to insert [" + sales.size() + "] records");
		}
		// initialise exception report 
		List<ExceptionReport> reports = new ArrayList<ExceptionReport>();
		int i = 0;

		for (PropertySale sale : sales) {
			try {
				getHibernateTemplate().save(sale);
				i++;
			} catch (HibernateException e) {
				ExceptionReport report = new ExceptionReport();
				String message = "Unable to save the sale on upload " + sale.getUploadId();
				report.setMessage(message);
				report.setException(e.getMessage());
				report.setFunctionalArea(BatchTypes.PROPERTY_SALES.getValue());
				reports.add(report);
				// need to log an error and continue - these errors
				// need to go in an exception report
				log.error("error", e);
			}

			if (i % batchSize == 0) { //, same as the JDBC batch size
				//flush a batch of inserts and release memory:
				getHibernateTemplate().flush();
				getHibernateTemplate().clear();
			}
		}
		return reports;
	}

	private Integer findExistingRecordCount (Map<String, Object> crit) {
		return findByCriteria(crit).size();
	}

	@SuppressWarnings("unchecked")
	public ExceptionReport insertPropertyAddress (PropertyAddress address) {
		if (log.isDebugEnabled()) {
			log.debug("going to insert [" + address + "] ");
		}
		ExceptionReport report = null;
		// check to see if the record for the same date exists. 
		// if it does stop the insert.
		// would be nice to get the db to do this but the required data is across two tables.
		Map<String, Object> crit = new HashMap<String, Object>();
		crit.put("addressKey", address.getAddressKey());
		if (address instanceof PropertySale) {
			PropertySale sale = (PropertySale)address;
			crit.put("saleDate", sale.getSaleDate());
			if (log.isDebugEnabled()) {
				log.debug("saleDate [" + sale.getSaleDate() + "] ");
			}

		}
		if (address instanceof PropertyEPC) {
			PropertyEPC sale = (PropertyEPC)address;
			// the rrn is unique to time
			crit.put("rrn", sale.getRrn());
		}
		//		--- need to check if addresskey and received combo exist
		//		--- if they do then find later one and mark earlier one as duplicate
		//		-- can be only one received at a time
		// if empty then we can continue		
		//	Integer existingCount = findExistingRecordCount(crit);

		if (findExistingRecordCount(crit) > 0) {

			address.setWorkFlowStatus(WorkFlowStatus.DUPLICATE.getValue());
		} else {
			// there is an additional case where property has been sold in quick succession
			// and so there could be two marked received, so need to make only one - in this case
			// mark the earlier one as superceded
			crit = new HashMap<String, Object>();
			DetachedCriteria query = DetachedCriteria.forClass(PropertyAddress.class);
			if (address instanceof PropertySale) {
				query.add(Expression.isNotNull("saleDate"));
			}
			if (address instanceof PropertyEPC) {
				query.add(Expression.isNotNull("inspectionDate"));
			}

			query.add(Expression.eq("workFlowStatus", WorkFlowStatus.RECEIVED.getValue()));
			query.add(Expression.eq("addressKey", address.getAddressKey()));
			List<PropertyAddress> addresses = getHibernateTemplate().findByCriteria(query);
			if (!addresses.isEmpty()) {
				if (address instanceof PropertySale) {
					PropertySale newSale = (PropertySale)address;
					for (PropertyAddress oldAd : addresses) {
						PropertySale sale = (PropertySale)oldAd;
						if (sale.getSaleDate().after(newSale.getSaleDate())) {
							address.setWorkFlowStatus(WorkFlowStatus.DUPLICATE.getValue());
						} else {
							// otherwise update old sale
							sale.setWorkFlowStatus(WorkFlowStatus.DUPLICATE.getValue());
							getHibernateTemplate().merge(sale);
						}
					}
				}
				if (address instanceof PropertyEPC) {
					PropertyEPC newSale = (PropertyEPC)address;
					for (PropertyAddress oldAd : addresses) {
						PropertyEPC sale = (PropertyEPC)oldAd;
						if (sale.getInspectionDate().after(newSale.getInspectionDate())) {
							address.setWorkFlowStatus(WorkFlowStatus.DUPLICATE.getValue());
						} else {
							// otherwise update old sale
							sale.setWorkFlowStatus(WorkFlowStatus.DUPLICATE.getValue());
							getHibernateTemplate().merge(sale);
						}
					}
				}
			}
		}

		try {
			getHibernateTemplate().save(address);
		} catch (HibernateException e) {
			// runtime, log and move on
			log.error("error", e);
			//			report = new ExceptionReport();
			//			String message = "Unable to save the sale on upload "+address.getUploadId();
			//			report.setMessage(message);
			//			report.setException(e.getMessage());
			//			if (address instanceof PropertySale) {
			//				report.setFunctionalArea(BatchTypes.PROPERTY_SALES.getValue());
			//			}
			//			if (address instanceof PropertyEPC) {
			//				report.setFunctionalArea(BatchTypes.EPC_REPORT.getValue());
			//			}
			//			report.setProcessId(address.getPropertyAddressId());
			//			// need to log an error and continue - these errors
			//			// need to go in an exception report
			//log.error("error",e);
		}
		return report;
	}

	public List<ExceptionReport> insertPropertyEPC (List<PropertyEPC> sales) {
		if (log.isDebugEnabled()) {
			log.debug("going to insert [" + sales.size() + "] records");
		}
		// initialise exception report 
		List<ExceptionReport> reports = new ArrayList<ExceptionReport>();
		int i = 0;
		for (PropertyEPC sale : sales) {
			try {
				getHibernateTemplate().save(sale);
			} catch (HibernateException e) {
				ExceptionReport report = new ExceptionReport();
				String message = "Unable to save the sale on upload " + sale.getUploadId();
				report.setMessage(message);
				report.setException(e.getMessage());
				report.setFunctionalArea(BatchTypes.PROPERTY_SALES.getValue());
				reports.add(report);
				// need to log an error and continue - these errors
				// need to go in an exception report
				log.error("error", e);
			}

			if (i % batchSize == 0) { //, same as the JDBC batch size
				//flush a batch of inserts and release memory:
				getHibernateTemplate().flush();
				getHibernateTemplate().clear();
			}
		}
		return reports;
	}

	/*
	 * (non-Javadoc)
	 * @see org.project.company.greenhomes.dao.PropertySalesDAO#findPropertyResultsByCriteria(org.project.company.greenhomes.service.query.QueryHolder)
	 */
	@SuppressWarnings("unchecked")
	public List<PropertyAddress> findPropertyResultsByCriteria (Map<String, Object> holder, Integer startPoint,
			Integer maxNumberOfResults) {
		DetachedCriteria query = DetachedCriteria.forClass(PropertyAddress.class);

		// convert the values in the holder search parameters to criteria
		Set<String> set = holder.keySet();
		for (String obj : set) {
			// need to strip any bits before the . off. May seem weird but this method
			// is unlikely to be used much and if it is may need a rethink.
			// type safety more useful for the pdf like search
			String param = obj;
			if (StringUtils.countMatches(obj, ".") > 0) {
				param = StringUtils.substringAfter(obj, ".");
			}

			query.add(Expression.eq(param, holder.get(obj)));
		}
		query.addOrder(Order.desc("propertyAddressId"));
		List<PropertyAddress> results = getHibernateTemplate().findByCriteria(query, startPoint, maxNumberOfResults);
		return results;
	}

	/*
	 * (non-Javadoc)
	 * @see org.project.company.greenhomes.dao.PropertyDAO#findPropertyEPCListByAddressKey(java.util.List)
	 */
	public List<PropertyEPC> findPropertyEPCListByPropertyKeyLazyFalse (final List<String> array) {
		List<PropertyEPC> results = new ArrayList<PropertyEPC>();
		for (String str : array) {
			PropertyEPC epc = findPropertyEPCById(str);
			results.add(epc);
		}
		return results;
	}

	/*
	 * (non-Javadoc)
	 * @see org.project.company.greenhomes.dao.PropertyDAO#deleteAllPropertySales(java.util.List)
	 */
	public void deleteAllPropertySales (List<PropertySale> list) {
		for (PropertySale sale : list) {
			delete(sale);
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.project.company.greenhomes.dao.PropertyDAO#deleteAllPropertyEPC(java.util.List)
	 */
	public void deleteAllPropertyEPC (List<PropertyEPC> list) {

		for (PropertyEPC sale : list) {
			getHibernateTemplate().delete(sale);
		}

	}

	/*
	 * (non-Javadoc)
	 * @see org.project.company.greenhomes.dao.PropertyDAO#findPropertiesForPDFsByCriteria(java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	public Collection<String> findPropertiesForPDFsByCriteria (final Map<DBFieldNames, Object> criteria)
			throws DataAccessException {
		StringBuilder epc = new StringBuilder();
		// the same address key can have multiple epcs as they could have done many reports to improve the ra
		//rating of the house
		StringBuilder sales = new StringBuilder();
		List<Object> params = new ArrayList<Object>();
		// we need the distinct property address key when there are two matches.
		// really this result is the union of a query on each sales/epc table
		// originally this was an iteration but it was nasty and fiddley to maintain so 
		// just doing it the old fashioned grunt way
		epc.append("select epc.PROPERTY_ADDRESS_ID from property_epc epc, " +
				"property_address pa ");
		sales.append("select unique ADDRESS_KEY from property_sale sale, " +
				"property_address pa ");
		// property attributes is only required if segmentation is searched on
		if (criteria.containsKey(DBFieldNames.SEGMENTATION_VALUE)) {
			epc.append(", property_address_attribute paa ");
		}
		// now add the parts which are always required, i.e. the joins
		epc.append(" where pa.PROPERTY_ADDRESS_ID = epc.PROPERTY_ADDRESS_ID ");
		sales.append(" where pa.PROPERTY_ADDRESS_ID = sale.PROPERTY_ADDRESS_ID ");
		//required if we have segmentation
		if (criteria.containsKey(DBFieldNames.SEGMENTATION_VALUE)) {
			// don't need land registry segmentation search as will be in the
			// epc search
			String value = (String)criteria.get(DBFieldNames.SEGMENTATION_VALUE);
			epc.append(" and pa.PROPERTY_ADDRESS_ID = paa.PROPERTY_ADDRESS_ID ");
			// lets do segmentation while we are here!
			epc.append(" and " + DBFieldNames.SEGMENTATION.getValue() + " = '" +
					"" + PropertyAttributeNames.estsegmentdescription.getValue() + "' ");
			epc.append(" and " + DBFieldNames.SEGMENTATION_VALUE.getValue() + " = '" + value + "' ");
		}

		// now go through the parameters and see what we get!
		// do the date part where we could have a range, or simply a to or from
		if (criteria.containsKey(DBFieldNames.TO_DATE)) {
			// then set the value to be less than this
			sales.append(" and sale.SALE_DATE  <= ? ");
			params.add(criteria.get(DBFieldNames.TO_DATE));
		}
		// same for from date
		if (criteria.containsKey(DBFieldNames.FROM_DATE)) {
			// then set the value to be greater than this
			sales.append(" and sale.SALE_DATE  >= ? ");
			params.add(criteria.get(DBFieldNames.FROM_DATE));
		}
		// next move on to the rating
		if (criteria.containsKey(DBFieldNames.EPC_RATING)) {
			// this is likely to be an array of values
			String ratings[] = StringUtils.split(criteria.get(DBFieldNames.EPC_RATING).toString(), ',');
			// need to get the between values in here
			epc.append(" and epc.ENERGY_RATING_CURRENT between  " +
							MathsHelper.getLowestRatingValue(ratings) + " and " +
							MathsHelper.getHighestRatingValue(ratings)
			);
		}
		// now add country
		if (criteria.containsKey(DBFieldNames.COUNTRY)) {

			// then set the value to be greater than this
			sales.append(" and pa.COUNTRY = '" + criteria.get(DBFieldNames.COUNTRY) + "' ");
			epc.append(" and pa.COUNTRY = '" + criteria.get(DBFieldNames.COUNTRY) + "' ");
		}
		// now add estac
		if (criteria.containsKey(DBFieldNames.ESTAC)) {
			// then set the value to be greater than this
			sales.append(" and pa.ESTAC = " + criteria.get(DBFieldNames.ESTAC) + " ");
			epc.append(" and pa.ESTAC = " + criteria.get(DBFieldNames.ESTAC) + " ");
		}
		// now add local authority
		if (criteria.containsKey(DBFieldNames.LOCAL_AUTHORITY)) {
			// then set the value to be greater than this
			sales.append(" and pa.LOCAL_AUTHORITY =  '" + criteria.get(DBFieldNames.LOCAL_AUTHORITY) + "' ");
			epc.append(" and pa.LOCAL_AUTHORITY =  '" + criteria.get(DBFieldNames.LOCAL_AUTHORITY) + "' ");
		}
		if (criteria.containsKey(DBFieldNames.SCHEDULE_ID)) {
			// then set the value to be greater than this
			sales.append(" and pa.SCHEDULE_ID =  '" + criteria.get(DBFieldNames.SCHEDULE_ID) + "' ");
			epc.append(" and pa.SCHEDULE_ID =  '" + criteria.get(DBFieldNames.SCHEDULE_ID) + "' ");
		}
		// now add the status
		if (criteria.containsKey(DBFieldNames.WORKFLOW)) {
			// then set the value to be greater than this
			sales.append(" and pa.WORK_FLOW_STATUS = '" + criteria.get(DBFieldNames.WORKFLOW) + "' ");
			epc.append(" and pa.WORK_FLOW_STATUS =  '" + criteria.get(DBFieldNames.WORKFLOW) + "' ");
		}
		// now we can put them together for an 'in' statement to make it one
		epc.append(" and pa.ADDRESS_KEY in ( " + sales.toString() + " )");
		if (log.isDebugEnabled()) {
			log.debug("sql:" + epc.toString());
		}
		return jdbcTemplate.queryForList(epc.toString(), params.toArray(), String.class);
	}

	public void setJdbcTemplate (JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public PropertyEPC findPropertyEPCById (final String pk) {

		PropertyEPC result = (PropertyEPC)getHibernateTemplate().load(PropertyEPC.class, pk);
		// init the lazy sets
		if (!result.getPropertyAddressAttributeSet().isEmpty()) {
			result.setPropertyAddressAttributeSet(
					new LinkedHashSet<PropertyAttribute>(result.getPropertyAddressAttributeSet()));
		}
		if (!result.getMeasuresSet().isEmpty()) {
			result.setMeasuresSet(new LinkedHashSet<PropertyMeasure>(result.getMeasuresSet()));
		}
		return result;
	}

	/**
	 * Uses jdbctemplate to update property address and set work flow status, based on address key
	 * and scheduled status. Tried hibernate template but was v slow for updating 10000+ records
	 */
	public void updatePropertyAddressWorkflow (final String addressId, final WorkFlowStatus newStatus,
			final WorkFlowStatus oldStatus) {
		// jdbc as this is 
		Object[] params = new Object[2];
		params[0] = newStatus.getValue();
		params[1] = addressId;
		String sql =
				"update property_address a set a.WORK_FLOW_STATUS = ? where a.ADDRESS_KEY = ? and a.WORK_FLOW_STATUS = '"
						+ oldStatus.getValue() + "'";
		jdbcTemplate.update(sql, params);
	}

	@SuppressWarnings("unchecked")
	public List<PropertyEPC> findPropertyEPCByScheduleId (Long scheduleId) {
		List<PropertyEPC> epcs = getHibernateTemplate().find(findPropertyEPCByScheduleId, scheduleId);
		//		// init all the associated collections - don't want this to be default behaviour
		for (PropertyEPC result : epcs) {
			if (!result.getPropertyAddressAttributeSet().isEmpty()) {
				result.setPropertyAddressAttributeSet(
						new LinkedHashSet<PropertyAttribute>(result.getPropertyAddressAttributeSet()));
			}
			if (!result.getMeasuresSet().isEmpty()) {
				result.setMeasuresSet(new LinkedHashSet<PropertyMeasure>(result.getMeasuresSet()));
			}
		}
		return epcs;
	}

	public void updatePropertiesToSchedule (List<String> ids, Long scheduleId) {

		// can only do 100 at a time without an error
		while (ids.size() > 100) {
			// we need to break up in to chunks
			// how many
			// do sublist
			updatePropertiesToScheduled(ids.subList(0, 99), scheduleId);
			ids.subList(0, 99).clear();
		}
		// do the last remaining ones
		updatePropertiesToScheduled(ids, scheduleId);
	}

	@SuppressWarnings("unchecked")
	private void updatePropertiesToScheduled (List<String> ids, Long scheduleId) {

		// this was taking ages to complete with 28000 records
		// and so changed to just pure sql - seems to be far quicker.
		// we are not using second level cache for propertyaddress 
		// let's get the property address ids from these values
		StringBuilder sb = new StringBuilder();
		for (Iterator<String> i = ids.iterator(); i.hasNext(); ) {
			sb.append("'");
			sb.append(i.next());
			sb.append("'");
			if (i.hasNext()) {
				sb.append(",");
			}

		}
		String select = "select address_key from GREEN_HOMES.PROPERTY_ADDRESS " +
				"where property_address_id " +
				" in (" + sb.toString() + ")";
		List<String> results = jdbcTemplate.queryForList(select, String.class);
		sb = new StringBuilder();
		for (Iterator<String> i = results.iterator(); i.hasNext(); ) {
			sb.append("'");
			sb.append(i.next());
			sb.append("'");
			if (i.hasNext()) {
				sb.append(",");
			}

		}

		String sql = "update GREEN_HOMES.PROPERTY_ADDRESS set " +
				"WORK_FLOW_STATUS = '" + WorkFlowStatus.SCHEDULED.getValue() + "', " +
				"SCHEDULE_ID = " + scheduleId + " " +
				"where address_key in " +
				"(" + sb.toString() + ") and " +
				"WORK_FLOW_STATUS = '" + WorkFlowStatus.RECEIVED.getValue() + "'";

		jdbcTemplate.execute(sql);
	}

	/*
	 * (non-Javadoc)
	 * @see org.project.company.greenhomes.dao.PropertyDAO#findNotValidAddressesByUploadId(java.lang.Long, java.lang.Integer, java.lang.Integer)
	 */
	@SuppressWarnings("unchecked")
	public List<PropertyAddress> findNotValidAddressesByUploadId (final Long processId, final Integer startPoint,
			final Integer endPoint) {
		// this is very complex!
		String sql = "select  * from (select a.*, rownum rnum from " +
				"(select * from GREEN_HOMES.PROPERTY_ADDRESS ps " +
				"where ps.UPLOAD_ID = " + processId
				+ " and (ps.WORK_FLOW_STATUS != 'received' and ps.WORK_FLOW_STATUS != 'scheduled'  and ps.WORK_FLOW_STATUS != 'completed') "
				+
				"order by ps.WORK_FLOW_STATUS, rowid ) " +
				"a where rownum <= " + endPoint + ") where rnum >= " + startPoint;

		return jdbcTemplate.query(sql, new PropertyAddressMapper());
	}

	public Integer findNumberOfResultsforProcessId (Long processId) {
		String sql = "from PropertyAddress where uploadId = ? ";
		return getHibernateTemplate().find(sql, processId).size();
	}

	@SuppressWarnings("unchecked")
	public List<PropertyAddress> findByCriteria (Map<String, Object> crit) {
		DetachedCriteria query = DetachedCriteria.forClass(PropertyAddress.class);
		// convert the values in the holder search parameters to criteria
		Set<String> set = crit.keySet();
		for (String obj : set) {
			query.add(Expression.eq(obj, crit.get(obj)));
		}
		return getHibernateTemplate().findByCriteria(query);
	}

	/*
	 * (non-Javadoc)
	 * @see org.project.company.greenhomes.dao.PropertyDAO#findMeasureByHeadingSummary(java.lang.String, java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<Measure> findMeasureByHeadingSummary (String summary, String heading) {
		Object[] params = new Object[2];
		params[0] = summary;
		params[1] = heading;
		return getHibernateTemplate().find(MEASURES_BY_SUMMARY_HEADING_SQL, params);
	}

	/*
	 * (non-Javadoc)
	 * @see org.project.company.greenhomes.dao.PropertyDAO#findAllMeasures()
	 */
	@SuppressWarnings("unchecked")
	public List<Measure> findAllMeasures () {

		return getSession().createCriteria(Measure.class).list();
	}

	@SuppressWarnings("unchecked")
	public Date findPropertySaleDateForAddressKey (String addressKey, Long scheduleId) throws NoMatchingDataException {
		DetachedCriteria query = DetachedCriteria.forClass(PropertySale.class);
		// convert the values in the holder search parameters to criteria
		query.add(Expression.isNotNull("saleDate"));

		if (null != scheduleId) {
			query.add(Expression.eq("scheduleId", scheduleId));
		} else {
			query.add(Expression.eq("workFlowStatus", WorkFlowStatus.RECEIVED.getValue()));
		}

		query.add(Expression.eq("addressKey", addressKey));
		List<PropertySale> results = getHibernateTemplate().findByCriteria(query);
		if (results.size() != 1) {
			// should not happen
			throw new NoMatchingDataException("Unable to find single related sale date " +
					"for addressKey:" + addressKey + ", and scheduleId" + scheduleId + ", results " +
					"returned: " + results.size());
		}
		PropertySale sale = results.iterator().next();
		return sale.getSaleDate();
	}
}
