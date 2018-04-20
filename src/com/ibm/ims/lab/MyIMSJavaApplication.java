package com.ibm.ims.lab;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.Statement;

import com.ibm.ims.dli.IMSConnectionSpec;
import com.ibm.ims.dli.IMSConnectionSpecFactory;
import com.ibm.ims.dli.PCB;
import com.ibm.ims.dli.PSB;
import com.ibm.ims.dli.PSBFactory;
import com.ibm.ims.dli.Path;
import com.ibm.ims.dli.SSAList;
import com.ibm.ims.dli.tm.Application;
import com.ibm.ims.dli.tm.ApplicationFactory;
import com.ibm.ims.dli.tm.Transaction;

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
						
			// Exercise 7 - Establishing a distributed IMS DL/I Connection
			//createAnImsDliConnection(4).close();
						
			// Exercise 8 - Read all records with GU and GN DL/I calls
			//readAllRecordsWithDliGuGnCalls();
			
			// Exercise 9 - Read a specific record with a DL/I GU call and a qualification
			//readASpecificRecordWithDliGu();

			// Exercise 10 - Update a specific record with a DL/I GHU and REPL call
			//updateASpecificRecordWithDliGhuRepl();
			//readASpecificRecordWithDliGu();
			
			// Exercise 11 - Writing a native IMS application
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
			// Exercise 2: Change the connection to use a local XML file PHIPHO1.xml
			
		} else if (driverType == 2) {
			// A Type-2 JDBC connection is used for local access on the mainframe
			// Exercise 11: Retrieve a Type-2 JDBC connection and set it to the connection object
			
		} else {
			throw new Exception("Invalid driver type specified: " + driverType);
		}
		
		
		return connection;
	}
	
	private static void displayMetadata() throws Exception {
		Connection connection = createAnImsConnection(4);
		
		// Exercise 2 - Use the JDBC DatabaseMetadata interface to print out 
		// database metadata information taken from the IMS catalog
		
		connection.commit();
		connection.close();
	}

	private static void executeAndDisplaySqlQuery() throws Exception {
		Connection connection = createAnImsConnection(4);
		
		// Exercise 3 - Issue a SQL SELECT statement and display it's output
		String sql = "SELECT * FROM PCB01.A1111111";
		
		connection.commit();
		connection.close();
	}

	private static void displayDliTranslationForSqlQuery() throws Exception {
		Connection connection = createAnImsConnection(4);
		
		// Exercise 4 - Use the Connection.nativeSql(String) method to display
		// the DL/I equivalent for a sql query
		String sql = "SELECT * FROM PCB01.A1111111";
		
		connection.commit();
		connection.close();
	}

	private static void executeASqlInsertOrUpdate() throws Exception {
		String sql = null;
		Connection connection = createAnImsConnection(4);
		
		// Exercise 5 - Issue a SQL INSERT
		//sql = "INSERT INTO PCB01.A1111111 (LASTNAME, FIRSTNAME, EXTENTION, ZIPCODE) VALUES ('REPLACE', 'REPLACE', 'REPLACE', 'REPLACE')";
		
		
		// Exercise 6 - Issue a SQL UPDATE
		//sql = "UPDATE PCB01.A1111111 SET FIRSTNAME='REPLACE' WHERE LASTNAME='REPLACE'";
		
		connection.commit();
		connection.close();
	}
	
	private static PSB createAnImsDliConnection(int driverType) throws Exception {
		PSB psb = null;
		
		if (driverType == 4) {
			// Exercise 7: Create a distributed DL/I connection and a PSB object
			// Define your connection properties
			IMSConnectionSpec imsConnSpec = IMSConnectionSpecFactory.createIMSConnectionSpec();
			
			// Create your PSB object
			psb = PSBFactory.createPSB(imsConnSpec);
		} else if (driverType == 2) {
			IMSConnectionSpec imsConnSpec = IMSConnectionSpecFactory.createIMSConnectionSpec();
			
			psb = PSBFactory.createPSB(imsConnSpec);
		} else {
			throw new Exception("Invalid driver type specified: " + driverType);
		}
		
		return psb;
	}
	
	private static void readAllRecordsWithDliGuGnCalls() throws Exception {
		PSB psb = createAnImsDliConnection(4);
		
		// Exercise 8 - Read from the database using GU/GN calls
		// Prepare and issue the GU call
		
		psb.commit();
		psb.close();
	}
	
	private static void readASpecificRecordWithDliGu() throws Exception {
		PSB psb = createAnImsDliConnection(4);
		
		// Exercise 9 - Read a specific record with a DL/I GU call and a qualification
		// Prepare and issue the GU call
		
		psb.commit();
		psb.close();
	}
	
	private static void updateASpecificRecordWithDliGhuRepl() throws Exception {
		PSB psb = createAnImsDliConnection(4);
		
		// Exercise 10 - Position on a specific record with a DL/I GHU call and a qualification
		// Prepare and issue the GHU call

		psb.commit();
		psb.close();
	}
	
	private static void executeNativeApplication() throws Exception {
		// Exercise 11 - Write a native IMS JBP application
		Connection connection = createAnImsConnection(2);
		
		// Start the unit of work
		Application app = ApplicationFactory.createApplication();
        Transaction transaction = app.getTransaction();
        
		// Do some work by displaying your updated record
		String sql = "SELECT * FROM PCB01.A1111111 WHERE LASTNAME='REPLACE'";
		
		// Commit your unit of work and cleanup your code
		connection.close();
		transaction.commit();
		app.end();
	}
}
