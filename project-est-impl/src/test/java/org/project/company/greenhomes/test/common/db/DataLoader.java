package org.project.company.greenhomes.test.common.db;

import org.project.company.greenhomes.test.common.SqlPlusRunner;

public class DataLoader {

	public static void main (String[] args) {
		SqlPlusRunner.runSQLPlusFile("truncate.sql");
		SqlPlusRunner.runSQLPlusFile("user.sql");
		//		SqlPlusRunner.runSQLPlusFile("upload_summary.sql");
		//		SqlPlusRunner.runSQLPlusFile("property_address.sql");
		//		SqlPlusRunner.runSQLPlusFile("property_address_attr.sql");
		//		SqlPlusRunner.runSQLPlusFile("property_epc.sql");
		//		SqlPlusRunner.runSQLPlusFile("property_sale.sql");
		//		SqlPlusRunner.runSQLPlusFile("property_measures.sql");
		//		SqlPlusRunner.runSQLPlusFile("schedule.sql");
	}
}
