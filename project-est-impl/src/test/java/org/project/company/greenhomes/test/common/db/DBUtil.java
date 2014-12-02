package org.project.company.greenhomes.test.common.db;

import org.apache.commons.dbcp.BasicDataSource;
import org.dbunit.dataset.IDataSet;
import org.dbunit.dataset.xml.FlatXmlDataSet;
import org.springframework.mock.jndi.SimpleNamingContextBuilder;

import javax.mail.Session;
import javax.naming.NamingException;
import javax.sql.DataSource;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class DBUtil {

	private static IDataSet getDataSet (InputStream stream) throws Exception {
		return new FlatXmlDataSet(stream);
	}

	//
	//    private static IDatabaseConnection getConnection(String dsName) throws ClassNotFoundException, SQLException, DatabaseUnitException
	//    {
	//    	//org.apache.commons.dbcp.BasicDataSource
	//
	//    	BasicDataSource source = (BasicDataSource)WebApplicationContextHolder.getApplicationContext().getBean(dsName);
	//
	//        Connection jdbcConnection = source.getConnection();
	//        IDatabaseConnection connection = new DatabaseConnection(jdbcConnection);
	//        return connection;
	//    }
	//    private static IDatabaseConnection getStandaloneConnection(String dsName) throws ClassNotFoundException, SQLException, DatabaseUnitException
	//    {
	//    	//org.apache.commons.dbcp.BasicDataSource
	//    	ClassPathXmlApplicationContext ctx = new ClassPathXmlApplicationContext(BaseSpringTestCase.APPLICATION_CONTEXT);
	//    	BasicDataSource source = (BasicDataSource)WebApplicationContextHolder.getApplicationContext().getBean(dsName);
	//    	// System.out.println("here source"+source);
	//        Connection jdbcConnection = source.getConnection();
	//
	//        IDatabaseConnection connection = null;
	//        //System.out.println("here 1"+jdbcConnection);
	//        try
	//        {
	//        	connection = new DatabaseConnection(jdbcConnection, "GREEN_HOMES");
	//        //	System.out.println("here 1 connection"+connection);
	//        }
	//        catch (Exception e)
	//        {
	//        	e.printStackTrace();
	//        }
	//        return connection;
	//    }
	//
	//    public static void insertData(InputStream stream, String dsName) throws Exception
	//    {
	//        // initialize your database connection here
	//        IDatabaseConnection connection = getConnection(dsName);
	//        // initialize your dataset here
	//        IDataSet dataSet = getDataSet(stream);
	//        try
	//        {
	//        	InsertIdentityOperation.CLEAN_INSERT.execute(connection, dataSet);
	//        }
	//        finally
	//        {
	//            connection.close();
	//        }
	//    }
	//    public static void deleteData(InputStream stream, String dsName) throws Exception
	//    {
	//        // initialize your database connection here
	//        IDatabaseConnection connection = getConnection(dsName);
	//        // initialize your dataset here
	//        IDataSet dataSet = getDataSet(stream);
	//        try
	//        {
	//        	DatabaseOperation.DELETE.execute(connection, dataSet);
	//        }
	//        finally
	//        {
	//            connection.close();
	//        }
	//    }
	//
	/*
     * Initialises the MockJNDI for the tests, this method MUST be called before
     * the Spring Context is initialised. Calling the method more than once will
     * have no additional effect
     * 
     * Values are read from test.properties
     * 
     */

	public static void initaliseMockJNDI () throws NamingException, IOException {

		//only need to run this once
		if (SimpleNamingContextBuilder.getCurrentContextBuilder() == null) {

			Properties testProps = new Properties();
			testProps.load(DBUtil.class.getResourceAsStream("/test.properties"));

			final SimpleNamingContextBuilder builder = new SimpleNamingContextBuilder();

			builder.bind(testProps.getProperty("user.db.jndi.name"), buildDataSource("user", testProps));
			Properties mailProps = new Properties();
			System.out.println("hello");
			mailProps.load(DBUtil.class.getResourceAsStream("/mail.properties"));
			System.out.println("props" + mailProps);
			System.out.println("mail:" + Session.getInstance(mailProps));
			builder.bind(testProps.getProperty("mail.jndi.name"), Session.getInstance(mailProps));
			builder.activate();

		}
	}

	private static DataSource buildDataSource (String prefix, Properties testProps) {
		BasicDataSource ds = new BasicDataSource();
		ds.setDriverClassName(testProps.getProperty(prefix + ".db.classname"));
		ds.setUrl(testProps.getProperty(prefix + ".db.url"));

		ds.setUsername(testProps.getProperty(prefix + ".db.user"));
		ds.setPassword(testProps.getProperty(prefix + ".db.password"));
		ds.setMaxActive(Integer.valueOf(testProps.getProperty(prefix + ".db.maxactive")));
		ds.setMinIdle(Integer.valueOf(testProps.getProperty(prefix + ".db.minidle")));

		return ds;
	}

	public static void main (String[] args) {

		//		try{
		//			DBUtil.initaliseMockJNDI();
		//		}
		//		catch(Exception e){
		//			e.printStackTrace();
		//		}
		//
		//		try {
		//
		//
		//			exportDatabase();
		//			//exportWolDatabase();
		//        //    populateDatabase();
		//
		//        } catch (SQLException e) {
		//			// TODO Auto-generated catch block
		//			e.printStackTrace();
		//		}
		//		System.out.println("finished");
	}
	//	private static void populateDatabase() throws SQLException
	//	{
	//        // database connection
	//		IDatabaseConnection connection = null;
	//        try {
	//        	insertData("c:/partial.xml") ;
	//        	//exportDatabase();
	////	        // full database export
	////	        IDataSet fullDataSet = connection.createDataSet();
	////	        FlatXmlDataSet.write(fullDataSet, new FileOutputStream("full.xml"));
	//		}
	////        catch (ClassNotFoundException e) {
	////			// TODO Auto-generated catch block
	////			e.printStackTrace();
	////		} catch (SQLException e) {
	////			// TODO Auto-generated catch block
	////			e.printStackTrace();
	////		} catch (DataSetException e) {
	////			// TODO Auto-generated catch block
	////			e.printStackTrace();
	////		} catch (FileNotFoundException e) {
	////			// TODO Auto-generated catch block
	////			e.printStackTrace();
	////		} catch (IOException e) {
	////			// TODO Auto-generated catch block
	////			e.printStackTrace();
	////		}
	//		catch (Exception e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		}
	//
	//
	//
	////        // dependent tables database export: export table X and all tables that
	////        // have a PK which is a FK on X, in the right order for insertion
	////        String[] depTableNames =
	////          TablesDependencyHelper.getAllDependentTables( connection, "X" );
	////        IDataSet depDataset = connection.createDataSet( depTableNames );
	////        FlatXmlDataSet.write(depDataSet, new FileOutputStream("dependents.xml"));
	//	}
	//
	//    protected static IDatabaseConnection getConnection() throws ClassNotFoundException, SQLException
	//    {
	//		Class driverClass = Class.forName("oracle.jdbc.driver.OracleDriver");
	//        Connection jdbcConnection = DriverManager.getConnection(
	//                "jdbc:oracle:thin:@dev001.test.local:1521:dev", "GREEN_HOMES", "GREEN_HOMES");
	//        System.out.println("connection"+jdbcConnection);
	//
	//         IDatabaseConnection connection = null;
	//		try {
	//			System.out.println("here");
	//			connection = new DatabaseConnection(jdbcConnection);
	//			System.out.println("here now");
	//		} catch (DatabaseUnitException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		}
	//        return connection;
	//    }
	//
	//    protected static void insertData(String fileName) throws Exception
	//    {
	//        // initialize your database connection here
	//        IDatabaseConnection connection = getConnection();
	//        // initialize your dataset here
	//        IDataSet dataSet = getDataSet(fileName);
	//        try
	//        {
	//        	System.out.println("putting in data"+dataSet.toString());
	//        	InsertIdentityOperation.CLEAN_INSERT.execute(connection, dataSet);
	//        }
	//        finally
	//        {
	//            connection.close();
	//        }
	//    }
	//    protected static IDataSet getDataSet(String fileName) throws Exception
	//    {
	//        return new FlatXmlDataSet(new FileInputStream(fileName));
	//    }
	//	private static void exportDatabase() throws SQLException
	//	{
	//        // database connection
	//		IDatabaseConnection connection = null;
	//        try {
	//        	connection = getStandaloneConnection(BeanNames.USER_DATA_SOURCE.getValue());
	//	        System.out.println("connection, now query"+connection);
	//
	//	        // partial database export
	//	        QueryDataSet partialDataSet = new QueryDataSet(connection);
	//	        System.out.println("data set 1");
	//	        partialDataSet.addTable("PROPERTY_ADDRESS", "SELECT * FROM PROPERTY_ADDRESS");
	//	        System.out.println("data set2");
	//	        partialDataSet.addTable("PROPERTY_ADDRESS_ATTRIBUTE", "SELECT * FROM PROPERTY_ADDRESS_ATTRIBUTE");
	//	        System.out.println("data set3");
	//	        partialDataSet.addTable("PROPERTY_SALE", "SELECT * FROM PROPERTY_SALE");
	//	      //  partialDataSet.addTable("ApplicationContent", "SELECT * FROM dbo.ApplicationContent");
	//	        //partialDataSet.addTable("BAR");
	//	        FlatXmlDataSet.write(partialDataSet, new FileOutputStream("c://partial.xml"));
	//
	////	        // full database export
	////	        IDataSet fullDataSet = connection.createDataSet();
	////	        FlatXmlDataSet.write(fullDataSet, new FileOutputStream("full.xml"));
	//		} catch (ClassNotFoundException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		} catch (SQLException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		} catch (DataSetException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		} catch (FileNotFoundException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		} catch (IOException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		} catch (DatabaseUnitException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		}
	//		catch (Exception e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		}
	//		finally
	//		{
	//			connection.close();
	//		}
	//
	//
	////        // dependent tables database export: export table X and all tables that
	////        // have a PK which is a FK on X, in the right order for insertion
	////        String[] depTableNames =
	////          TablesDependencyHelper.getAllDependentTables( connection, "X" );
	////        IDataSet depDataset = connection.createDataSet( depTableNames );
	////        FlatXmlDataSet.write(depDataSet, new FileOutputStream("dependents.xml"));
	//	}
	//	private static void exportWolDatabase() throws SQLException
	//	{
	//        // database connection
	//		IDatabaseConnection connection = null;
	//        try {
	////			Class driverClass = Class.forName("net.sourceforge.jtds.jdbc.Driver");
	////	        Connection jdbcConnection = DriverManager.getConnection(
	////	                "jdbc:jtds:sqlserver://127.0.0.1:1433/dev1", "sa", "acl");
	//	        connection = getConnection();
	//
	//	        // partial database export
	//	        QueryDataSet partialDataSet = new QueryDataSet(connection);
	//	        partialDataSet.addTable("ApplicationContent");
	////	        partialDataSet.addTable("offers");
	////	        partialDataSet.addTable("component_types");
	////	        partialDataSet.addTable("customer_types");
	////	        partialDataSet.addTable("promo_advice_categories");
	////	        partialDataSet.addTable("promo_review_categories");
	////	        partialDataSet.addTable("promo_customer_type");
	////	        partialDataSet.addTable("promo_types");
	////	        partialDataSet.addTable("promos");
	//
	//	      //  partialDataSet.addTable("ApplicationContent", "SELECT * FROM dbo.ApplicationContent");
	//	        //partialDataSet.addTable("BAR");
	//	        FlatXmlDataSet.write(partialDataSet, new FileOutputStream("c://partial.xml"));
	//
	////	        // full database export
	////	        IDataSet fullDataSet = connection.createDataSet();
	////	        FlatXmlDataSet.write(fullDataSet, new FileOutputStream("full.xml"));
	//		} catch (ClassNotFoundException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		} catch (SQLException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		} catch (DataSetException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		} catch (FileNotFoundException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		} catch (IOException e) {
	//			// TODO Auto-generated catch block
	//			e.printStackTrace();
	//		}
	//		finally
	//		{
	//			connection.close();
	//		}
	//	}
}
