package org.project.company.greenhomes.service;

import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.test.annotation.ExpectedException;
import org.project.company.greenhomes.common.enums.BeanNames;
import org.project.company.greenhomes.common.enums.RoleNames;
import org.project.company.greenhomes.domain.entity.User;
import org.project.company.greenhomes.domain.entity.UserRole;
import org.project.company.greenhomes.exception.DuplicateUserException;
import org.project.company.greenhomes.exception.InvalidDataException;
import org.project.company.greenhomes.test.common.SqlPlusRunner;
import org.project.company.greenhomes.test.common.spring.BaseSpringTestCase;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Commented out tests are for future functionality.
 *
 * @author fmacder
 */
public class UserServiceTest extends BaseSpringTestCase {

	private UserService userService;

	protected String[] getConfigLocations () {
		return new String[] { "classpath:" + APPLICATION_CONTEXT };
	}

	public void onSetUp () throws Exception {
		//DBUtil.insertData(getClass().getResourceAsStream("../../customer-data.xml"), BeanNames.CUSTOMER_DATA_SOURCE.getValue());
		userService = (UserService)applicationContext.getBean(BeanNames.USER_SERVICE.getValue());
		SqlPlusRunner.runSQLPlusFile("truncate.sql");
		SqlPlusRunner.runSQLPlusFile("user.sql");
	}

	@Override
	protected void onTearDown () throws Exception {
		// delete all the data
		//deleteData(getClass().getClassLoader().getResourceAsStream("partial.xml"));
		//SqlPlusRunner.runSQLPlusFile("truncate.sql");
	}

	/**
	 * Tests to see if user is returned
	 *
	 * @throws Exception
	 */
	public void testFindUsersByCriteriaOneResult () throws Exception {
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("userName", "%monono%");
		criteria.put("emailAddress", "%Developer8%");
		//criteria.put("emailAddress", model.getEmailAddress());
		List<User> models = userService.findUsersByCriteria(criteria);
		assertNotNull(models);
		// should be one
		assertEquals(1, models.size());
		//endTransaction();
	}

	/**
	 * Tests to see if user is returned
	 *
	 * @throws Exception
	 */
	public void testFindUsersByCriteriaMultipleCriteria () throws Exception {
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("userName", "%monono%");
		criteria.put("emailAddress", "%Developer5%");
		//criteria.put("emailAddress", model.getEmailAddress());
		List<User> models = userService.findUsersByCriteria(criteria);
		assertNotNull(models);
		// should be one
		assertEquals(6, models.size());
		//endTransaction();
	}

	/**
	 * Tests to see if users are returned
	 *
	 * @throws Exception
	 */
	public void testFindUsersByCriteriaMultipleResults () throws Exception {
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("userName", "%monono%");
		//criteria.put("emailAddress", model.getEmailAddress());
		List<User> models = userService.findUsersByCriteria(criteria);
		assertNotNull(models);
		// should be one
		assertEquals(7, models.size());
		//endTransaction();
	}

	/**
	 * Tests to see if user is returned
	 *
	 * @throws Exception
	 */
	@ExpectedException(UsernameNotFoundException.class)
	public void testFindUser () throws Exception {
		String userName = "ghdfghtytty";

		User user = (User)userService.loadUserByUsername(userName);
		// should throw exception
		assertNull("user returned", user);

		//endTransaction();
	}

	/**
	 * tests to see if a record can be created
	 */
	@ExpectedException(InvalidDataException.class)
	public void testInsertUserNoRoles () throws Exception {
		String emailAddress = "Developer11@est.org.uk";
		String userName = "freddy1";
		String firstName = "flinny";
		String familyName = "Bourne2";
		String password = "password";
		User user = new User();
		user.setActive(true);
		user.setUserName(userName);
		user.setEmailAddress(emailAddress);
		user.setFamilyName(familyName);
		user.setFirstName(firstName);
		user.setPassword(password);
		user = userService.createUser(user);
		assertNotNull("user returned", user);
		assertNotNull(user.getUserId());
	}

	/**
	 * Tests to see if user is returned
	 *
	 * @throws Exception
	 */
	public void testFindUsersByCriteria () throws Exception {
		Map<String, Object> criteria = new HashMap<String, Object>();
		criteria.put("userName", "mononoke2");
		//criteria.put("emailAddress", model.getEmailAddress());
		List<User> models = userService.findUsersByCriteria(criteria);
		assertNotNull(models);
		// should be one
		assertEquals(1, models.size());
		//endTransaction();
	}

	/**
	 * tests to see if a record can be created
	 */
	public void testUpdateuserRoles () throws Exception {
		String emailAddress = "Developer5@est.org.uk";
		String userName = "mononoke3";
		String firstName = "Jason2";
		String familyName = "Bourne";
		String password = "password";
		Long userId = (long)69;
		User user = new User();
		user.setUserId(userId);
		user.setActive(true);
		user.setUserName(userName);
		user.setEmailAddress(emailAddress);
		user.setFamilyName(familyName);
		user.setFirstName(firstName);
		user.setPassword(password);

		UserRole role = new UserRole();
		role.setRoleName(RoleNames.ROLE_USER.getValue());
		user.addUserRole(role);
		user = userService.updateUser(user, false);
		assertNotNull("user returned", user);
		assertNotNull(user.getUserId());
		assertNotNull(user.getUserRoleSet());
		assertEquals(1, user.getUserRoleSet().size());
		assertEquals(1, user.getAuthorities().size());
		//	assertEquals(RoleNames.ROLE_SUPER_USER.getValue(),user.getUserRoleSet().iterator().next().getRoleName());
	}

	/**
	 * Tests to see if user is returned
	 *
	 * @throws Exception
	 */
	@ExpectedException(DuplicateUserException.class)
	public void testCreateDuplicateUser () throws Exception {
		String userName = "mononoke3";
		User user = new User();
		user.setUserName(userName);
		user.setFamilyName("sdfsd");
		user.setFirstName("sdfsd");
		user.setEmailAddress("sdfgd@sdfs.com");
		user.setActive(true);
		userService.createUser(user);

	}

	/**
	 * Tests to see if exception thrown
	 *
	 * @throws Exception
	 */
	@ExpectedException(UsernameNotFoundException.class)
	public void testLoadByUsernameNoResult () throws Exception {
		String userName = "archibacl";
		User user = (User)userService.loadUserByUsername(userName);
		assertNotNull(user);
	}

	/**
	 * tests to see if a record can be created
	 */
	public void testAddUserRoles () throws Exception {
		String emailAddress = "Developer5@est.org.uk";
		String userName = "mononoke9";
		String firstName = "Jason2";
		String familyName = "Bourne";
		String password = "password";
		//Long userId = (long)69;
		User user = new User();
		//user.setUserId(userId);
		user.setActive(true);
		user.setUserName(userName);
		user.setEmailAddress(emailAddress);
		user.setFamilyName(familyName);
		user.setFirstName(firstName);
		user.setPassword(password);

		UserRole role = new UserRole();
		role.setRoleName(RoleNames.ROLE_USER.getValue());
		user.addUserRole(role);
		role = new UserRole();
		role.setRoleName(RoleNames.ROLE_SUPER_USER.getValue());
		user.addUserRole(role);
		user = userService.createUser(user);
		assertNotNull("user returned", user);
		assertNotNull(user.getUserId());
		assertNotNull(user.getUserRoleSet());
		assertEquals(2, user.getUserRoleSet().size());
		assertEquals(2, user.getAuthorities().size());
		//	assertEquals(RoleNames.ROLE_SUPER_USER.getValue(),user.getUserRoleSet().iterator().next().getRoleName());
	}

	/**
	 * tests to see if a null pointer is returned
	 */
	@ExpectedException(NullPointerException.class)
	public void testInsertNullUser () throws Exception {
		User user = userService.createUser(null);
		assertNull("user returned", user);
	}

}
