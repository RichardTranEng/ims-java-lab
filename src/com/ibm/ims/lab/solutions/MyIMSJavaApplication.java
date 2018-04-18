package com.ibm.ims.lab.solutions;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import com.ibm.ims.dli.tm.Application;
import com.ibm.ims.dli.tm.ApplicationFactory;
import com.ibm.ims.dli.tm.Transaction;
import com.ibm.ims.jdbc.IMSDataSource;

public class MyIMSJavaApplication {
	public static void main(String[] args) {
		try {
			// Exercise 1 - Establishing a distributed IMS connection
			//createAnImsConnection(4).close();
			
			// Exercise 2 - Doing JDBC metadata discovery
			//displayMetadata();
			
			// Exercise 3 - Querying a database with a SQL Select
			//executeAndDisplaySqlQuery();
			
			// Exercise 4 - Looking at the DL/I translation of the SQL query
			//displayDliTranslationForSqlQuery();
			
			// Exercise 5 - Insert a record into the database with a SQL INSERT
			// Exercise 6 - Updating the database with a SQL UPDATE and validate contents
			//executeASqlInsertOrUpdate();
			//executeAndDisplaySqlQuery();
						
			
			// Exercise 7 - Writing a native IMS application
			//executeNativeApplication();
			
			
		} catch (Exception e) {
			System.out.println("Abnormal error occurred: " + e.getMessage());
			e.printStackTrace();
		}
	}

	private static Connection createAnImsConnection(int driverType) throws Exception{
		Connection connection = null;
		
		if (driverType == 4) {
			// A Type-4 JDBC connection is used for distributed access over TCP/IP.
			// Exercise 1: Retrieve a Type-4 JDBC connection and set it to the connection object
			//IMSDataSource ds = new IMSDataSource();
			//ds.setHost("sample.host.name");
			//ds.setPortNumber(5555);
			//ds.setDriverType(driverType);
			//ds.setUser("myUser");
			//ds.setPassword("myPass");
			//ds.setDatabaseName("PHIDPHO1");
			//connection = ds.getConnection();
			
			
			// Exercise 2: Change the connection to use a local XML file PHIPHO1.xml
			//IMSDataSource ds = new IMSDataSource();
			//ds.setHost("sample.host.name");
			//ds.setPortNumber(5555);
			//ds.setDriverType(driverType);
			//ds.setUser("myUser");
			//ds.setPassword("myPass");
			//ds.setDatabaseName("xml://PHIDPHO1");
			//connection = ds.getConnection();
			
		} else if (driverType == 2) {
			// A Type-2 JDBC connection is used for local access on the mainframe
			// Exercise 7: Retrieve a Type-2 JDBC connection and set it to the connection object
			//IMSDataSource ds = new IMSDataSource();
			//ds.setDriverType(driverType);
			//ds.setUser("myUser");
			//ds.setPassword("myPass");
			//ds.setDatabaseName("xml://PHIDPHO1");
			//connection = ds.getConnection();
			
		} else {
			throw new Exception("Invalid driver type specified: " + driverType);
		}
		
		
		return connection;
	}
	
	private static void displayMetadata() throws Exception {
		Connection connection = createAnImsConnection(4);
		
		// Exercise 2 - Use the JDBC DatabaseMetadata interface to print out 
		// database metadata information taken from the IMS catalog
		DatabaseMetaData dbmd = connection.getMetaData();
		
		// Display IMS PCB information
		ResultSet rs = dbmd.getSchemas("PHIDPHO1", null);
		ResultSetMetaData rsmd = rs.getMetaData();
		int colCount = rsmd.getColumnCount();
		
		System.out.println("Displaying IMS PCB metadata");
		while (rs.next()) {
			for (int i = 1; i <= colCount; i++) {
				System.out.println(rsmd.getColumnName(i) + ": " + rs.getString(i));
			}
			System.out.println();
		}
		
		// Display IMS segment information
		rs = dbmd.getTables("PHIDPHO1", "PCB01", null, null);
		rsmd = rs.getMetaData();
		colCount = rsmd.getColumnCount();
		
		System.out.println("\nDisplaying IMS Segment metadata");
		while (rs.next()) {
			for (int i = 1; i <= colCount; i++) {
				System.out.println(rsmd.getColumnName(i) + ": " + rs.getString(i));
			}
			System.out.println();
		}
		
		// Display IMS field information
		rs = dbmd.getColumns("PHIDPHO1", "PCB01", "A1111111", null);
		rsmd = rs.getMetaData();
		colCount = rsmd.getColumnCount();
		
		System.out.println("\nDisplaying IMS Field metadata");
		while (rs.next()) {
			for (int i = 1; i <= colCount; i++) {
				System.out.println(rsmd.getColumnName(i) + ": " + rs.getString(i));
			}
			System.out.println();
		}
		
		connection.commit();
		connection.close();
	}

	private static void executeAndDisplaySqlQuery() throws Exception {
		Connection connection = createAnImsConnection(4);
		
		// Exercise 3 - Issue a SQL SELECT statement and display it's output
		String sql = "SELECT * FROM PCB01.A1111111";
		
		Statement st = connection.createStatement();
		ResultSet rs = st.executeQuery(sql);
		ResultSetMetaData rsmd = rs.getMetaData();
		int colCount = rsmd.getColumnCount();
		
		System.out.println("\nDisplaying query results");
		while (rs.next()) {
			for (int i = 1; i <= colCount; i++) {
				System.out.println(rsmd.getColumnName(i) + ": " + rs.getString(i));
			}
			System.out.println();
		}
		
		connection.commit();
		connection.close();
	}

	private static void displayDliTranslationForSqlQuery() throws Exception {
		Connection connection = createAnImsConnection(4);
		
		// Exercise 4 - Use the Connection.nativeSql(String) method to display
		// the DL/I equivalent for a sql query
		String sql = "SELECT * FROM PCB01.A1111111";
		System.out.println("DL/I translation for '" + sql + "' is:");
		System.out.println(connection.nativeSQL(sql));
		
		connection.close();
	}

	private static void executeASqlInsertOrUpdate() throws Exception {
		String sql = null;
		Connection connection = createAnImsConnection(4);
		
		// Exercise 5 - Issue a SQL INSERT
		//sql = "INSERT INTO PCB01.A1111111 (LASTNAME, FIRSTNAME, EXTENTION, ZIPCODE) VALUES ('BAGGINS', 'FRODO', '123456A', '12345')";
		//Statement st = connection.createStatement();
		//System.out.println("Inserted " + st.executeUpdate(sql) + " record");
		
		// Exercise 6 - Issue a SQL UPDATE
		//sql = "UPDATE PCB01.A1111111 SET FIRSTNAME='BILBO' WHERE LASTNAME='BAGGINS'";
		//Statement st = connection.createStatement();
		//System.out.println("Updated " + st.executeUpdate(sql) + " record(s)");
		
		connection.commit();
		connection.close();
	}
	
	private static void executeNativeApplication() throws Exception {
		// Exercise 7 - Write a native IMS JBP application
		Connection connection = createAnImsConnection(2);
		
		// Start the unit of work
		Application app = ApplicationFactory.createApplication();
        Transaction transaction = app.getTransaction();
        
		// Do some work by displaying your inserted record
		//String sql = "SELECT * FROM PCB01.A1111111 WHERE LASTNAME='BAGGINS'";
		
		//Statement st = connection.createStatement();
		//ResultSet rs = st.executeQuery(sql);
		//ResultSetMetaData rsmd = rs.getMetaData();
		//int colCount = rsmd.getColumnCount();
		
		//System.out.println("\nDisplaying query results");
		//while (rs.next()) {
		//	for (int i = 1; i <= colCount; i++) {
		//		System.out.println(rsmd.getColumnName(i) + ": " + rs.getString(i));
		//	}
		//	System.out.println();
		//}
		
		// Commit your unit of work and cleanup your code
		connection.close();
		transaction.commit();
		app.end();
	}
}
