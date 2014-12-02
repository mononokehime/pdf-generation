package org.project.company.greenhomes.dao.hibernate;

import org.apache.commons.lang.ArrayUtils;
import org.apache.commons.lang.StringUtils;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.hibernate.criterion.MatchMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.orm.hibernate3.support.HibernateDaoSupport;
import org.project.company.greenhomes.common.util.RegularExpressionChecker;
import org.project.company.greenhomes.dao.LocalityDAO;
import org.project.company.greenhomes.dao.mapper.ReferenceDataMapper;
import org.project.company.greenhomes.domain.entity.Centre;
import org.project.company.greenhomes.domain.entity.Experian;
import org.project.company.greenhomes.domain.entity.FormattedAddressView;
import org.project.company.greenhomes.domain.model.ESTACData;
import org.project.company.greenhomes.domain.model.LocationData;
import org.project.company.greenhomes.domain.model.ReferenceData;
import org.project.company.greenhomes.exception.NoMatchingDataException;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;

/**
 * Implementation of {@link LocalityDAO}
 *
 *
 */
public class LocalityDAOImpl extends HibernateDaoSupport implements LocalityDAO {

	/**
	 * the logger
	 */
	private static final Logger log = LoggerFactory.getLogger(LocalityDAOImpl.class);
	private final static String FIND_ESTAC_LIST_BY_COUNTRY = "SELECT DISTINCT sc.centre_name,sc.centre_code " +
			"FROM sense.centres sc, sense.centre_la scl, ref_data.postcode_data pd WHERE sc.obsolete = 'N' " +
			"AND sc.cen_typ_centre_type_id = 1 AND sc.centre_code = scl.cen_centre_code " +
			"AND pd.oscty || pd.oslaua = scl.centre_la_id AND pd.ctry = ? order by sc.centre_name asc";
	private final static String FIND_LA_LIST_BY_COUNTRY = "select distinct la.name, la.COUNTY || la.DISTRICT from " +
			"ref_data.LOCAL_AUTHORITIES la, ref_data.postcode_data pd " +
			"where la.DISTRICT = pd.OSLAUA and la.COUNTY = pd.OSCTY and pd.CTRY = ? order by la.name";
	private final static String centreSql = "select a.region, a.centre_code, a.CENTRE_LA_ID " +
			"from  sense.D1_GET_CEN_REG_FOR_POSTCODE a  " +
			"where a.outcode = ? and a.incode = ?    ";
	private JdbcTemplate jdbcTemplate;

	/*
	 * (non-Javadoc)
	 * @see org.project.company.greenhomes.dao.LocalityDAO#findESTACListByCountry(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<ReferenceData> findESTACListByCountry (String countryCode) {
		Object[] country = new Object[1];
		country[0] = countryCode;
		return getJdbcTemplate().query(FIND_ESTAC_LIST_BY_COUNTRY, country, new ReferenceDataMapper());
	}

	/*
	 * (non-Javadoc)
	 * @see org.project.company.greenhomes.dao.LocalityDAO#findLAListByCountry(java.lang.String)
	 */
	@SuppressWarnings("unchecked")
	public List<ReferenceData> findLAListByCountry (String countryCode) {
		Object[] country = new Object[1];
		country[0] = countryCode;
		return getJdbcTemplate().query(FIND_LA_LIST_BY_COUNTRY, country, new ReferenceDataMapper());
	}

	/*
	 * (non-Javadoc)
	 * @see org.project.company.greenhomes.dao.LocalityDAO#findSegmentationList()
	 */
	@SuppressWarnings("unchecked")
	public List<String> findSegmentationList () {
		return getJdbcTemplate().queryForList(
				"select distinct ESTSEGMENTDESCRIPTION from ref_data.experian order by ESTSEGMENTDESCRIPTION asc",
				String.class);
	}

	/*
	 * (non-Javadoc)
	 * @see org.project.company.greenhomes.dao.LocalityDAO#findLocationData(java.lang.Object[])
	 */
	@SuppressWarnings("unchecked")
	public LocationData findLocationData (final Object[] address) throws NoMatchingDataException {
		if (address == null) {
			throw new IllegalArgumentException("Argument Object[] postcode cannot be null");
		}
		if (address.length != 5) {
			throw new IllegalArgumentException(
					"Argument Object[] postcode size must be 5 [incode, outcode, addr1, addr2, addr3]. Current size = "
							+ address.length);
		}

		LocationData data = new LocationData();
		// we need to break this up as some addresses do not have complete data
		// first of all get the centre code, region and local authority

		// only need postcode data here
		Object[] postcode = ArrayUtils.subarray(address, 0, 2);
		// put the mapper in there two to save on creating more code
		// anonymous class!!
		List<ESTACData> obj = (List<ESTACData>)getJdbcTemplate().query(centreSql, postcode, new RowMapper() {
			public Object mapRow (ResultSet rs, int rowNum) throws SQLException {
				ESTACData data = new ESTACData(rs.getString("region"), rs.getInt("centre_code"),
						rs.getString("CENTRE_LA_ID"));
				return data;
			}
		});
		// could also do this way: - or just use mapper like with other ways
		//		private static final class ActorMapper implements RowMapper {
		//
		//		    public Object mapRow(ResultSet rs, int rowNum) throws SQLException {
		//		        Actor actor = new Actor();
		//		        actor.setFirstName(rs.getString("first_name"));
		//		        actor.setSurname(rs.getString("surname"));
		//		        return actor;
		//		    }
		//		}

		// there should be only one
		if (obj.isEmpty()) {
			// then we are doomed
			String message = "No estac found for outcode [" + postcode[0] + "] and incode [" + postcode[1]
					+ "]. Inserted but marked invalid.";
			throw new NoMatchingDataException(message);
		}
		ESTACData est = obj.get(0);
		data.setRegion(est.getRegion());
		data.setCentreCode(est.getCentreCode());
		data.setLocalAuthority(est.getLocalAuthority());
		// finally we need the segmentation data  - this is not complete for all post codes
		//
		String sql = "from Experian where postCodeOutcode = ? and postCodeIncode = ?";
		List<Experian> res = getHibernateTemplate().find(sql, postcode);
		if (res.isEmpty()) {
			//just log as not the end of the world
			log.warn("No segmentation data for post code:" + postcode[0] + postcode[1]);
		} else {
			data.setEstSegmentDescription(res.get(0).getESTSegmentDescription());
		}
		return data;
	}

	/*
	 * (non-Javadoc)
	 * @see org.project.company.greenhomes.dao.LocalityDAO#findAddressKey(java.lang.Object[])
	 */
	@SuppressWarnings("unchecked")
	public String findAddressKey (final Object[] postcode) throws NoMatchingDataException {

		// there are three more potential address matchind fields
		DetachedCriteria query = buildAddressToQuery(postcode, false);
		Boolean containsInvertedComma = RegularExpressionChecker.containsInvertedComma((String)postcode[4]);

		List<FormattedAddressView> res = getHibernateTemplate().findByCriteria(query);
		if (res.size() == 1) {
			return res.get(0).getAddressKey();
		}

		if (res.isEmpty()) {

			// most of the time this does not fail, but it tends to go wrong for addresses like
			// st. pauls street where the post code data table has the full stop, but the supplied 
			//data does not, so try  match
			if (containsInvertedComma) {
				query = buildAddressToQuery(postcode, true);
			}
			res = getHibernateTemplate().findByCriteria(query);
			// try again!
			if (res.size() == 1) {
				return res.get(0).getAddressKey();
			}

		}
		String addressDetails = "postCodeOutcode:" + postcode[0] + ", postCodeIncode" + postcode[1] + ", " +
				"address1:" + postcode[2] + ", address2" + postcode[3] + ", address3" + postcode[4];
		if (res.size() > 1) {
			// too many results!
			throw new NoMatchingDataException("Too many results for address" + addressDetails);
		}
		throw new NoMatchingDataException("Unable to find address key for address" + addressDetails);
	}

	private DetachedCriteria buildAddressToQuery (final Object[] postcode, Boolean invertedComma) {
		//apologies for this method. Matching addresses is a bugger
		DetachedCriteria query = DetachedCriteria.forClass(FormattedAddressView.class);
		// add the post code
		query.add(Expression.eq("postCodeOutcode", postcode[0]));
		query.add(Expression.eq("postCodeIncode", postcode[1]));
		// if the first one is empty then is a street name and number only
		boolean firstOneEmpty = false;
		if (StringUtils.isNotBlank(postcode[2] + "")) {
			// need to lower case
			// we need to tidy data up for search			
			String add = (String)postcode[2];
			query.add(Expression.ilike("address", add.toLowerCase(), MatchMode.ANYWHERE));
		} else {
			firstOneEmpty = true;
		}
		if (invertedComma) {
			// get rid of the inverted commas
			String val = (String)postcode[4];
			postcode[4] = val.replace("'", "");
		}
		if (firstOneEmpty) {

			// put address together for search as this one means it is probably a house numebr and road
			// but there are exceptions! netherly TN8 7HE, so in this case find out if
			//any are not numbers, but remember it could be a flat number so pick an arbitrary
			// number such as 3 letters, which means they should not be joined
			// a street name/number would be 13 house street which means we need the full match
			// if it is split, then 131 would come out as the same as 13 as we need to put
			// like in there too nethley%

			int nonDigits = RegularExpressionChecker.numberOfNonDigits(postcode[3] + "");
			if (nonDigits > 3) {
				// keep the apart as probably name of house
				String add = (String)postcode[3];
				query.add(Expression.ilike("address", add.toLowerCase(), MatchMode.ANYWHERE));
				add = (String)postcode[4];
				query.add(Expression.ilike("address", add.toLowerCase(), MatchMode.ANYWHERE));
			} else {
				String add = (String)postcode[3];
				String add1 = (String)postcode[4];
				query.add(Expression.ilike("address", add.toLowerCase() + " " + add1.toLowerCase(), MatchMode.START));
			}
		} else {
			if (StringUtils.isNotBlank(postcode[3] + "")) {
				String add = (String)postcode[3];
				query.add(Expression.ilike("address", add.toLowerCase(), MatchMode.ANYWHERE));
			}
			if (StringUtils.isNotBlank(postcode[4] + "")) {
				String add = (String)postcode[4];
				query.add(Expression.ilike("address", add.toLowerCase(), MatchMode.ANYWHERE));
			}
		}
		return query;
	}

	/*
	 * (non-Javadoc)
	 * @see org.project.company.greenhomes.dao.LocalityDAO#findAddressesByPostCode(java.lang.Object[])
	 */
	@SuppressWarnings("unchecked")
	public List<FormattedAddressView> findAddressesByPostCode (Object[] postcode) {
		if (postcode == null) {
			throw new IllegalArgumentException("Argument Object[] postcode cannot be null");
		}
		if (postcode.length != 2) {
			throw new IllegalArgumentException(
					"Argument Object[] postcode size must be 2 [incode, outcode]. Current size = " + postcode.length);
		}
		String sql = "from FormattedAddressView where postCodeOutcode = ? and postCodeIncode = ? ";
		return getHibernateTemplate().find(sql, postcode);
	}

	/*
	 * (non-Javadoc)
	 * @see org.project.company.greenhomes.dao.LocalityDAO#findCentreFromPostcode(java.lang.Object[])
	 */
	public Centre findCentreFromPostcode (Object[] postcode) {
		if (postcode == null) {
			throw new IllegalArgumentException("Argument Object[] postcode cannot be null");
		}
		if (postcode.length != 2) {
			throw new IllegalArgumentException(
					"Argument Object[] postcode size must be 2 [incode, outcode]. Current size = " + postcode.length);
		}
		String sql = "select gcr.centre_code from sense.D1_GET_CEN_REG_FOR_POSTCODE gcr " +
				"where gcr.outcode = ? and gcr.incode = ?  ";

		Integer str = getJdbcTemplate().queryForInt(sql, postcode);
		if (null != str) {
			// then go and get the centre details
			return (Centre)getHibernateTemplate().get(Centre.class, str);
		}
		return null;
	}

	public JdbcTemplate getJdbcTemplate () {
		return jdbcTemplate;
	}

	public void setJdbcTemplate (JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

	public Centre findCentreFromId (final Integer centreCode) {
		return (Centre)getHibernateTemplate().get(Centre.class, centreCode);
	}
}
