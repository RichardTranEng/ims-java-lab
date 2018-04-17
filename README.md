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
1. **host**: zserveros.centers.ihost.com
2. **port number**: 7013
3. **driver type**: 4
4. **user**: To be provided by the lab instructor
5. **password**: To be provided by the lab instructor
6. **database name**: DFSIVP1 *<-- This is actually the PSB name*

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
ResultSet rs = dbmd.getSchemas("DFSIVP1", null);
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
TBD
```

We can dig even further into the database segments and fields with the following query. Use the same format as above to process the ResultSet object:

```java
// Display IMS segment information
rs = dbmd.getTables("DFSIVP1", "PCB01", null, null);

// Display IMS field information
rs = dbmd.getColumns("DFSIVP1", "PCB01", "A1111111", null);
```

You'll notice that there is a lack of field information for the phonebook database. Traditionally, additional metadata information would be stored in a COBOL copybook or a PL/I include file. It would be up to the IMS Database Administrator (DBA) and IMS System Programmer to incorporate this information into the IMS catalog. 

Since we don't have either available, we're going to take advantage of a little known [secret](https://imsinsiders.wordpress.com/2018/03/15/how-to-try-out-an-ims-catalog-without-an-actual-catalog-using-the-ims-jdbc-driver/). The way the IMS JDBC driver retrieves metadata from the IMS catalog is through the IMS GUR DL/I call which returns an XML representation of the requested resource. For now, we will proxy our local XML file instead of issuing a GUR for additional metadata. You should see the two files we will be referencing in the /src directory for both our PSB and DBD:
* DFSIVP1.xml
* IVPDB1.xml

Let's modify our connection property in the `createAnImsConnection()` method to point to our local XML file:
```java
ds.setDatabaseName("xml://DFSIVP1");
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

##### Exercise 4: Looking at how IMS breaks down SQL queries

##### Exercise 5: Inserting a record into the database

##### Exercise 6: Updating a record in the database




### Writing a native Java application
This covers lab exercises 7 and 8