package org.project.company.greenhomes.test.common;

import java.io.File;

/**
 * Runs the SQL*Plus file. The following pre-requisites apply
 * <UL>
 * <LI>You will need an Oracle client with SQL*Plus</LI>
 * <LI>The location of the SQL*Plus scripts should be found as per the
 * script_location property in the sqlplus.properties file on the application
 * classpath</LI>
 * <LI>The database user is the connection.user property in
 * connection.properties, also on the classpath</LI>
 * <LI>The database password is the connection.password property in
 * connection.properties</LI>
 * <LI>The database SID is the connection.service property in
 * connection.properties</LI>
 * <LI>The file runsqlplus.sql needs to be in the same directory as the
 * SQL*Plus scripts</LI>
 * </UL>
 *
 * @author Phil Zoio
 */
public class SqlPlusRunner {

	public SqlPlusRunner () {
		super();
	}

	public static int runSQLPlusFile (String fileName) {

		String scriptLocation = PropertyUtils.getProperty("test.properties", "script.location");
		String user = PropertyUtils.getProperty("test.properties", "user.db.user");
		String password = PropertyUtils.getProperty("test.properties", "user.db.password");
		String service = PropertyUtils.getProperty("test.properties", "connection.service");
		//String exe = "sqlplus phil/phil@HOME @runsqlplus " + fileName;

		File sqlPlusWrapper = new File(scriptLocation, "runsqlplus.sql");
		if (!sqlPlusWrapper.exists()) {
			throw new IllegalStateException("Sql plus wrapper " + sqlPlusWrapper + " does not exist");
		}

		StringBuffer exeBuffer = new StringBuffer("sqlplus ").append(user.trim()).append("/").append(password.trim())
				.append("@").append(service);
		exeBuffer.append(" @runsqlplus ").append(fileName);

		return CommandRunner.runCommand(new File(scriptLocation), exeBuffer.toString());

	}
}