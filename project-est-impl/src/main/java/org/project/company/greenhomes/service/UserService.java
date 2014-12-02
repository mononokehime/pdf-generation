package org.project.company.greenhomes.service;

import org.springframework.security.core.userdetails.UserDetailsService;
import org.project.company.greenhomes.domain.entity.User;
import org.project.company.greenhomes.exception.DuplicateUserException;
import org.project.company.greenhomes.exception.InvalidDataException;
import org.project.company.greenhomes.exception.PasswordDoesNotMatchException;

import java.util.List;
import java.util.Map;

public interface UserService extends UserDetailsService {

	/**
	 * Creates a new user with an encrypted password based on details supplied. Inserts
	 * record in to user and user_roles tables. Note that no validation is done here. Expected at front end
	 *
	 * @param user the details to save
	 * @return the user populated with a pk
	 */
	User createUser (User user) throws DuplicateUserException, InvalidDataException;

	/**
	 * @param user
	 * @param updatePassword whether or not password has been changed in which case we need encryption
	 * @return
	 * @throws PasswordDoesNotMatchException when old and new password do not match
	 */
	User updateUser (User user, Boolean updatePassword) throws PasswordDoesNotMatchException;

	User updateUserStatus (User user);

	/**
	 * Returns the populated user for the id given
	 *
	 * @param userId
	 * @return
	 */
	User findUserByUserid (final Long userId);

	List<User> findUsersByCriteria (final Map<String, Object> criteria);

	User updatePassword (User user);

}
