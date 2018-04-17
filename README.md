# ims-java-lab

This lab's goal is to show a Java developer how to access an IMS database through the IMS Universal JDBC driver.



### Getting started
This lab is designed specifically for coding Java in an Eclipse IDE. It is meant to be instructor led where the laptops are already provided pre-configured for you but you should be able to run this on your own machine as well. If you get lost at any step, feel free to reference the solution provided in the `com.ibm.ims.lab.solutions.MyIMSJavaApplication.java` file

##### Pre-requisites
Software: 
* [Eclipse Neon or later](https://www.eclipse.org) 
* [Java 7 or later](https://java.com/en/)

Skills:
* Java programming - Beginner level
* SQL programming - Beginner level
* IMS - Beginner level 

##### Importing the Eclipse project
The ims-java-lab project is designed as an Eclipse project and will need to get imported into your Eclipse development environment.
1. Open up Eclipse
2. Accept the default workspace or specify your own
3. In the top menu, select File->Import
4. Choose the "Existing Projects into Workspace" option in the popup wizard and click Next
5. Click the "Browse" button next to the "Select root directory" radio and navigate to where you downloaded the ims-java-lab project.

##### Opening up the lab files
1. Once your project has been imported, you should have a **ims-java-lab** project in the **Project Explorer**. 
2. Expand out the following folders: **ims-java-lab->src->com.ibm.ims.lab**
3. Double click on the `MyIMSJavaApplication.java` file. The majority of your work will be done in this file.



### Writing a distributed Java application
The first part of the lab is to develop a distributed Java application. In this case when we say distributed, we're specifically talking about any non z/OS environment that supports Java. 

Connections to IMS resources on the mainframe from a distributed environment requires a TCP/IP connection through an IMS Connect TCP/IP gateway. For our distributed application, we will be connecting through a pre-configured IMS Connect that resides on a public demo system.

##### Exercise 1: Creating a Type-4 JDBC connection to an IMS database
The following information is required to connect to an IMS database from an external environment
1. Hostname/IP address of the IMS Connect
2. Port number for the IMS Connect
3. Username to authenticate against the system's resource access control facility (RACF)
4. Password to authenticate against RACF
5. The IMS Program specification block (PSB) name that the user will access

In `MyIMSJavaApplication.java`, go ahead and uncomment the line under Exercise 1 in the `main()` method by removing the comments ('//'). 

```java
createAnImsConnection(4).close();
```

Now navigate to the `createAnImsConnection()` method and create your connection underneath the Exercise 1 section.

To create your connection first create an IMSDataSource object. We will use an IMSDataSource to create our connection but you could also alternatively create it using the standard JDBC [DriverManager](https://www.ibm.com/support/knowledgecenter/en/SSEPH2_15.1.0/com.ibm.ims15.doc.apg/ims_odbjdbcconndrivermgr.htm) interface.

```java
IMSDataSource ds = new IMSDataSource();
```

Now use the appropriate setters on your IMSDataSource object to set the following parameters:
1. **host**: To be provided by the lab instructor
2. **port number**: To be provided by the lab instructor
3. **driver type**: 4
4. **user**: To be provided by the lab instructor
5. **password**: To be provided by the lab instructor
6. **database name**: PHIDPHO1 *<-- This is actually the PSB name*

Setter example for host:

```java
ds.setHost("zserveros.centers.ihost.com");
```

Once you've set all of the connection information, you can create a connection by calling the `getConnection()` method on your IMSDataSource object.

```java
connection = ds.getConnection();
```

You can now run your application by selecting **Run->Run As->Java Application** from the top Eclipse menu. The console should show the following output:

```
Apr 16, 2018 1:52:18 PM com.ibm.ims.drda.t4.T4ConnectionReply checkServerCompatibility
INFO: Server IMS Connect DDM level:  1
Apr 16, 2018 1:52:18 PM com.ibm.ims.drda.t4.T4ConnectionReply checkServerCompatibility
INFO: Client IMS Connect DDM level:  1
Apr 16, 2018 1:52:18 PM com.ibm.ims.drda.t4.T4ConnectionReply checkServerCompatibility
INFO: Server ODBM DDM level:  1 2 3 4 5 6
Apr 16, 2018 1:52:18 PM com.ibm.ims.drda.t4.T4ConnectionReply checkServerCompatibility
INFO: Client ODBM DDM level:  1 2 3 4 5 6 7
Apr 16, 2018 1:52:18 PM com.ibm.ims.drda.t4.T4ConnectionReply checkServerCompatibility
INFO: ODBM DDM level is backlevel with respect to the Universal driver client. Some functionality in the driver will be disabled. Suggest upgrading ODBM to latest service level.
Apr 16, 2018 1:52:18 PM com.ibm.ims.dli.PSBInternalFactory createPSB
INFO: IMS Universal Drivers build number: 14066
```

The output shows that we created a connection to the system and validated functional levels between the server and the client. 

Let's disable Excercise 1 before moving on by adding the comments back in the `main()` method

```java
//createAnImsConnection(4).close();
```

##### Exercise 2: Discovering the database metadata
Now that we have a connection to IMS, the next step is to discover what databases are available for access through the PSB (IVPDB1) defined in the connection from Exercise 1. This database metadata information would be stored in the IMS catalog which is IMS' trusted source for information. This information has been mapped to standard JDBC DatabaseMetadata discovery which many JDBC based tools use.

The following is a mapping of terms from IMS to the relational model that the JDBC interface uses:
* Program Control Block (PCB) == Schemas
* Database Segments == Database Tables
* Database Fields == Database Columns
* Database Records == Database Rows

Similar to Exercise 1, we will want to uncomment the following line in the `main()` method:

```java
displayMetadata();
```

Now navigate to the `displayMetadata()` implementation, you'll notice that we are first establishing a connection to the IMS system by taking advantage of the code we wrote in Exercise 1. 

Let's take that connection object and retrieve a queryable DatabaseMetaData object from that.

```java
DatabaseMetaData dbmd = connection.getMetaData();
```

The `DatabaseMetaData` class contains [several methods](https://docs.oracle.com/javase/7/docs/api/java/sql/DatabaseMetaData.html) for discovery which typically returns back a ResultSet object. Let's discover what PCBs are available by using the `getSchemas()` method. Remember that PCBs have a one to one mapping with schemas. The following code will show how to invoke the `getSchemas()` method and display the output.

```java
// Display IMS PCB information
ResultSet rs = dbmd.getSchemas("PHIDPHO1", null);
ResultSetMetaData rsmd = rs.getMetaData();
int colCount = rsmd.getColumnCount();
		
System.out.println("Displaying IMS PCB metadata");
while (rs.next()) {
for (int i = 1; i <= colCount; i++) {
  System.out.println(rsmd.getColumnName(i) + ": " + rs.getString(i));
}
```

In addition to using DatabaseMetaData for discovery of the database, we also used ResultSetMetaData above to identify information on the ResultSet returned by the getSchemas() call. We will be used ResultSetMetaData in most of the following exercises in order to display a readable output like the following:

```
TABLE_SCHEM: PCB01
TABLE_CATALOG: PHIDPHO1
PCB_PROCESSING_OPTIONS: AP
DBD_NAME: DHIDPHO1
DBD_TIMESTAMP: 1810711232054
```

We can dig even further into the database segments and fields with the following query. Use the same format as above to process the ResultSet object:

```java
// Display IMS segment information
rs = dbmd.getTables("PHIDPHO1", "PCB01", null, null);

// Display IMS field information
rs = dbmd.getColumns("PHIDPHO1", "PCB01", "A1111111", null);
```

You'll notice that there is a lack of field information for the phonebook database. Traditionally, additional metadata information would be stored in a COBOL copybook or a PL/I include file. It would be up to the IMS Database Administrator (DBA) and IMS System Programmer to incorporate this information into the IMS catalog. 

Since we don't have either available, we're going to take advantage of a little known [secret](https://imsinsiders.wordpress.com/2018/03/15/how-to-try-out-an-ims-catalog-without-an-actual-catalog-using-the-ims-jdbc-driver/). The way the IMS JDBC driver retrieves metadata from the IMS catalog is through the IMS GUR DL/I call which returns an XML representation of the requested resource. For now, we will proxy our local XML file instead of issuing a GUR for additional metadata. You should see the two files we will be referencing in the /src directory for both our PSB and DBD:
* PHIDPHO1.xml
* IVPDB1.xml

Let's modify our connection property in the `createAnImsConnection()` method to point to our local XML file:
```java
ds.setDatabaseName("xml://PHIDPHO1");
```

Now re-run the `DatabaseMetaData.getColumns()` method to view the additional fields. You should now see the following additional fields:
* LASTNAME
* FIRSTNAME
* EXTENTION
* ZIPCODE

That completes Exercise 2. Let's go ahead and disable the following line in the `main()` method by commenting it out:
```java
//displayMetadata();
```

##### Exercise 3: Querying the database
Now that we have a good understanding of what our database looks like. We can go ahead and start building queries against the database. Let's start by uncommenting the following line in the `main()` method.

```java
executeAndDisplaySqlQuery();
```

Let's now navigate to the `executeAndDisplaySqlQuery()` method and write our SQL SELECT statement to issue a read request against the database. 

An initial query has already been written `SELECT * FROM PCB01.A1111111`. This is based off of our database metadata discovery where we know the PSB PHIDPHO1 contains a PCB PCB01 which has a segment A111111 that contains fields related to a phonebook.

The way we would execute a read query is through the `Statement.executeQuery()` method. We can get a `Statement` object off of the `Connection`. The following code shows how to do that.

```java
Statement st = connection.createStatement();
ResultSet rs = st.executeQuery(sql);
```

You can process the `ResultSet` in a similar manner to what we did in Exercise 2. You should see output similar to the following:
```
LASTNAME: LAST1     
FIRSTNAME: FIRST1    
EXTENTION: 8-111-1111
ZIPCODE: D01/R01 
```


##### Exercise 4: Looking at how IMS breaks down SQL queries
The native query language for an IMS database is DL/I. In order for IMS to process SQL queries, those queries will need to be translated into the DL/I equivalent. Sometimes, it's useful for debugging or tuning purposes to look at how a SQL query is broken down. 

So where is this translation being done? In this case, the IMS JDBC driver handles all of the translation. It exposes the translation through the `Connection.nativeSql()` method

Let's take a look at the the translation for the previous SQL query with the following code snippet:
```java
String sql = "SELECT * FROM PCB01.A1111111";
System.out.println("DL/I translation for '" + sql + "' is:");
System.out.println(connection.nativeSQL(sql));
```

You should see the following output in your console:
```
DL/I translation for 'SELECT * FROM PCB01.A1111111' is:
GU   A1111111 

(LOOP)
GN   A1111111 

NOTE: GU/GN VALID only if not overruled by CONCUR_UPDATABLE ResultSet concurrency
```

The SQL SELECT query which can be considered a batch retrieve, is translated into a series of singleton DL/I calls. The first call is to a **GET UNIQUE** which retrieves the first record to match a qualifier. The IMS JDBC driver will then repeatedly call **GET NEXT** until it retrieves all records from the database that match the qualifier.

Feel free to come back to this exercise in order to look at the translation for the SQL statements in Exercise 5 and 6. For now, we'll go ahead and comment the following line in the `main()` method.

```java
//executeAndDisplaySqlQuery();
```


##### Exercise 5: Inserting a record into the database
Now that we have a base understanding of what the IMS database looks like and what data resides in that database, we'll go ahead and insert in a new phonebook record. Let's start off by uncommenting the following lines in the `main()` method
```java
executeASqlInsertOrUpdate();
executeAndDisplaySqlQuery();
```

Then navigate to the `executeASqlInsertOrUpdate()` method.

The database segment that we have been looking at is keyed off of the LASTNAME parameter, this means we will need a unique last name value from what's already in the database (which you should know from Exercise 3).

The format for a SQL INSERT statement can be found [here](https://www.w3schools.com/sql/sql_insert.asp). Similar to what we did for a SQL SELECT, we will be using a `Statement` object to issue the SQL statement. However instead of using the `executeQuery()` method which is for database reads, we will want to use the `executeUpdate()` method for database inserts, updates and deletes.

The following code snippet will insert a record into the database. Make sure to modify the values for the entry you want to add.
```java
sql = "INSERT INTO PCB01.A1111111 (LASTNAME, FIRSTNAME, EXTENTION, ZIPCODE) VALUES ('BAGGINS', 'FRODO', '123456A', '12345')";
Statement st = connection.createStatement();
System.out.println("Inserted " + st.executeUpdate(sql) + " record");
```		

Run the Java application and verify that your new record was inserted properly. You should see something like the following in your output:
```
Inserted 1 record

Displaying query results
LASTNAME: BAGGINS   
FIRSTNAME: FRODO     
EXTENTION: 123456A   
ZIPCODE: 12345     
```

What happens if you try to insert the same record again? We would expect an error as we can't have two records with the same unique key. Try running your application again. You should see the following error message
```
com.ibm.ims.drda.base.DrdaException: An error occurred processing the database DHIDPHO1. AIB return code: 0x900. AIB reason code: 0x0. AIB error code extension: 0x0. DBPCB status code: II.
 ```
 
You'll notice that the we get some AIB return and reason code in addition to a DBPCB status code. This error information is actually returned by the IMS database as a result of attempting to execute the translated DL/I query. Looking at the [IMS knowledge center](https://www.ibm.com/support/knowledgecenter/en/SSEPH2_15.1.0/com.ibm.ims15.doc.msgs/msgs/ii.htm), we can see that the II status code is returned on a DL/I ISRT call when a record already exists in the database.

Before moving on to the next exercise let's make sure we comment out any code we added to the `executeASqlInsertOrUpdate()` method.


##### Exercise 6: Updating a record in the database
Let's take the record we inserted in the previous exercise and update it using a SQL UPDATE statement. The format for a SQL UPDATE can be found [here](https://www.w3schools.com/sql/sql_update.asp).

We want to make sure only update the record we inserted earlier. This can be done by qualifying on the LASTNAME field which we know is a unique field. The following code snippet shows how to issue a SQL UPDATE query, make sure to modify the fields and qualifier as necessary.
```java
sql = "UPDATE PCB01.A1111111 SET FIRSTNAME='BILBO' WHERE LASTNAME='BAGGINS'";
Statement st = connection.createStatement();
System.out.println("Updated " + st.executeUpdate(sql) + " record(s)");
```

After running your application, you should see similar output in your console:
```
Updated 1 record(s)

Displaying query results
LASTNAME: BAGGINS   
FIRSTNAME: BILBO     
EXTENTION: 123456A   
ZIPCODE: 12345
```

This concludes the distributed portion of the lab. Make sure to clean up your application by going back into the `main()` method and commenting out the following lines:
```java
//executeASqlInsertOrUpdate();
//executeAndDisplaySqlQuery();
```


### Writing a native Java application
This covers lab exercises 7 and 8