package org.project.company.greenhomes.dao;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.project.company.greenhomes.domain.entity.User;
import org.project.company.greenhomes.domain.entity.UserRole;

import java.util.List;
import java.util.Map;

/**
 * DAO class for accessing the users table
 *
 *
 */
public interface UserDAO extends GenericDAO<User, Long> {

	/**
	 * Implementation for Spring Security
	 *
	 * @param pUsername
	 * @return
	 * @throws UsernameNotFoundException
	 */
	User loadUserByUsername (String pUsername)
			throws UsernameNotFoundException;

	/**
	 * Saves or update the user. If the user has a user id, then merge is used
	 *
	 * @param user
	 * @return
	 */
	User saveOrUpdateUser (User user);

	/**
	 * Find the user by criteria. The key should be the field to search and the Object the criteria
	 *
	 * @param criteria
	 * @return
	 */
	List<User> findUsersByCriteria (Map<String, Object> criteria);

	User findUserForUpdate (Long id);

	void deleteUserRole (UserRole role);

}
