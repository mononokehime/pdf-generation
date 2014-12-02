package org.project.company.greenhomes.dao.hibernate;

import org.hibernate.HibernateException;
import org.hibernate.criterion.DetachedCriteria;
import org.hibernate.criterion.Expression;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.dao.support.DataAccessUtils;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.project.company.greenhomes.dao.UserDAO;
import org.project.company.greenhomes.domain.entity.User;
import org.project.company.greenhomes.domain.entity.UserRole;

import java.io.Serializable;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;

public class UserDAOImpl extends GenericDAOImpl<User, Long> implements UserDAO, Serializable {
	/**
	 *
	 */
	private static final long serialVersionUID = 1L;
	/**
	 * the logger
	 */
	private static final Logger log = LoggerFactory.getLogger(UserDAOImpl.class);
	private JdbcTemplate jdbcTemplate;

	/*
	 * (non-Javadoc)
	 * @see org.project.company.greenhomes.dao.UserDAO#loadUserByUsername(java.lang.String)
	 */
	public User loadUserByUsername (String userName)
			throws UsernameNotFoundException {
		if (log.isDebugEnabled()) {
			log.debug("looking for user [" + userName + "]");
		}
		try {
			return (User)DataAccessUtils
					.uniqueResult(getHibernateTemplate().find("from User user where userName = ?  ", userName));
		} catch (HibernateException e) {
			log.error("Error from hibernate", e);
			throw new UsernameNotFoundException("No user returned with username [" + userName + "]");
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.project.company.greenhomes.dao.UserDAO#saveOrUpdateUser(org.project.company.greenhomes.domain.entity.User)
	 */
	public User saveOrUpdateUser (User user) {
		if (null == user.getUserId()) {
			getHibernateTemplate().save(user);
		} else {
			getHibernateTemplate().merge(user);
		}
		return user;
	}

	/*
	 * (non-Javadoc)
	 * @see org.project.company.greenhomes.dao.UserDAO#findUsersByCriteria(java.util.Map)
	 */
	@SuppressWarnings("unchecked")
	public List<User> findUsersByCriteria (Map<String, Object> criteria) {

		//getHibernateTemplate( ).findByCriteria(criteria)
		StringBuilder sb = new StringBuilder();
		sb.append(" from User where ");
		// loop through the criteria and set up the parameters before calling
		// do iteration for this so we can control the builder more effectively
		// at the same time build an object array to put in as
		// parameter values
		Set<String> set = criteria.keySet();
		Object[] values = new Object[criteria.size()];

		Iterator<String> itr = set.iterator();
		int counter = 0;
		while (itr.hasNext()) {
			String key = itr.next();
			Object value = (Object)criteria.get(key);
			values[counter] = value;
			sb.append(" " + key + " like ? ");
			if (itr.hasNext()) {
				sb.append(" and ");
			}
			counter++;
		}
		return getHibernateTemplate().find(
				sb.toString(), values);

	}

	@SuppressWarnings("unchecked")
	public User findUserForUpdate (Long id) {
		DetachedCriteria criteria = DetachedCriteria.forClass(User.class);
		// don't want to use load as then the update becomes more of a drag with instances
		// easier to create a new one and merge it with the existing.
		criteria.add(Expression.eq("userId", id));
		List<User> users = getHibernateTemplate().findByCriteria(criteria);
		if (users.isEmpty()) {
			throw new UsernameNotFoundException("No user returned with id [" + id + "]");
		}
		return users.iterator().next();
	}

	public void deleteUserRole (UserRole role) {

		jdbcTemplate.execute("delete from USERS_ROLE where user_role_id = " + role.getUserRoleId() + "");
	}

	public void setJdbcTemplate (JdbcTemplate jdbcTemplate) {
		this.jdbcTemplate = jdbcTemplate;
	}

}
