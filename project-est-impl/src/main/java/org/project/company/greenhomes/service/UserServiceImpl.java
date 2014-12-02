package org.project.company.greenhomes.service;

import org.springframework.dao.DataAccessException;
import org.springframework.security.authentication.encoding.ShaPasswordEncoder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.project.company.greenhomes.dao.UserDAO;
import org.project.company.greenhomes.domain.entity.User;
import org.project.company.greenhomes.domain.entity.UserRole;
import org.project.company.greenhomes.exception.DuplicateUserException;
import org.project.company.greenhomes.exception.InvalidDataException;
import org.project.company.greenhomes.exception.PasswordDoesNotMatchException;

import java.util.Date;
import java.util.List;
import java.util.Map;

public class UserServiceImpl implements UserService {

	private UserDAO userDAO;

	/**
	 * @param userDAO
	 */
	public UserServiceImpl (final UserDAO userDAO) {
		if (null == userDAO) {
			throw new IllegalArgumentException("userDAO cannot be null.");
		}
		this.userDAO = userDAO;
	}

	/**
	 * the logger
	 */
	//private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);
	
	/*
	 * (non-Javadoc)
	 * @see org.springframework.security.userdetails.UserDetailsService#loadUserByUsername(java.lang.String)
	 */
	public UserDetails loadUserByUsername (String pUsername)
			throws UsernameNotFoundException, DataAccessException {
		User user = userDAO.loadUserByUsername(pUsername);
		if (null == user) {
			throw new UsernameNotFoundException("No user returned with username [" + pUsername + "]");
		}
		return user;
	}

	/*
	 * (non-Javadoc)
	 * @see org.project.company.greenhomes.service.UserService#createUser(org.project.company.greenhomes.domain.entity.User)
	 */
	public User createUser (User user) throws DuplicateUserException, InvalidDataException {
		// validation been done at lower level
		// check to see if user exists
		try {
			User myUser = (User)loadUserByUsername(user.getUserName());
			throw new DuplicateUserException("The user with name " + user.getUserName() + " already exists.");
		} catch (UsernameNotFoundException e) {
			// not usual to use an exception in the normal flow but what are the choices?
			// we need to know if user name already exists. So re-use the finder method
			// which has to throw that exception as part of the interface. The alternative
			// is to write another method
			ShaPasswordEncoder sha = new ShaPasswordEncoder();
			String pwd = sha.encodePassword(user.getUserName(), null);
			user.setPassword(pwd);
			user.setCreateDate(new Date());
			// need to have roles too
			if (null == user.getUserRoleSet() || user.getUserRoleSet().isEmpty()) {
				throw new InvalidDataException("No user roles set");
			}
			user = userDAO.save(user);
			return user;
		}
	}

	/*
	 * (non-Javadoc)
	 * @see org.project.company.greenhomes.service.UserService#updateUser(org.project.company.greenhomes.domain.entity.User, java.lang.Boolean)
	 */
	public User updateUser (User user, Boolean updatePassword) throws PasswordDoesNotMatchException {
		// first of all get the old user

		User oldUser = userDAO.findUserForUpdate(user.getUserId());
		// just get the old values from it
		user.setCreateDate(oldUser.getCreateDate());
		user.setActive(oldUser.getActive());
		if (updatePassword) {
			ShaPasswordEncoder sha = new ShaPasswordEncoder();
			String pwd = sha.encodePassword(user.getPasswordModel().getOldPassword(), null);
			if (!pwd.equals(oldUser.getPassword())) {
				throw new PasswordDoesNotMatchException("Password supplied does not match old password");
			}
			// now set the new password!		
			oldUser.setPassword(sha.encodePassword(user.getPasswordModel().getNewPassword(), null));
			return userDAO.saveOrUpdateUser(oldUser);
		} else {
			user.setPassword(oldUser.getPassword());
			for (UserRole role : oldUser.getUserRoleSet()) {
				userDAO.deleteUserRole(role);
			}
			return userDAO.saveOrUpdateUser(user);
		}

	}

	public User findUserByUserid (Long userId) {
		User user = userDAO.findById(userId, false);
		user.getUserRoleSet().size();
		return user;
	}

	public List<User> findUsersByCriteria (Map<String, Object> criteria) {
		return userDAO.findUsersByCriteria(criteria);
	}

	public User updatePassword (User user) {
		User oldUser = userDAO.findById(user.getUserId(), false);
		// do it this way so we don't need to fart about populating all the fields.
		oldUser.setPassword(user.getPassword());
		// save it
		return userDAO.saveOrUpdateUser(user);
	}

	public User updateUserStatus (User user) {
		User oldUser = userDAO.findUserForUpdate(user.getUserId());
		// just get the old values from it		
		oldUser.setActive(user.getActive());
		return userDAO.saveOrUpdateUser(oldUser);
	}

}
