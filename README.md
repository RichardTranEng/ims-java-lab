# ims-java-lab

This lab's goal is to show a Java developer how to access an IMS database through the IMS Universal JDBC driver.



### Getting started

##### Pre-requisites
This lab was designed to run in an Eclipse IDE running JDK 7 or higher. 
* [Eclipse IDE](https://www.eclipse.org) 
* [Java](https://java.com/en/)

##### Importing the Eclipse project
1. Open up Eclipse
2. Accept the default workspace or specify your own
3. In the top menu, select File->Import
4. Choose the "Existing Projects into Workspace" option in the popup wizard and click Next
5. Click the "Browse" button next to the "Select root directory" radio and navigate to where you downloaded the ims-java-lab project.

##### Opening up the lab files
1. Once your project has been imported, you should have a **ims-java-lab** project in the **Project Explorer**. 
2. Expand out the following folders: **ims-java-lab->src->com.ibm.ims.lab**
3. Double click on the **MyIMSJavaApplication.java** file. The majority of your work will be done in this file.



### Writing a distributed Java application
Include an intro TBD

##### Exercise 1: Creating a Type-4 JDBC connection to an IMS database
A Type-4 JDBC connection is required in order to connect to an IMS database from a distributed (non z/OS environment). All external connections into IMS go through **IMS Connect** which is the TCP/IP gateway into IMS. The following information is required to connect to an IMS database from an external environment
1. Hostname/IP address of the IMS Connect server
2. Port number for the IMS Connect server
3. Username to authenticate against the system's resource access control facility (RACF)
4. Password to authenticate against RACF
5. The IMS Program specification block (PSB) name that the user will access

In **MyIMSJavaApplication.java**, go ahead and uncomment the line under Exercise 1 in the `main()` method by removing the comments ('//'). 

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

##### Exercise 3: Querying the database

##### Exercise 4: Looking at how IMS breaks down SQL queries

##### Exercise 5: Inserting a record into the database

##### Exercise 6: Updating a record in the database




### Writing a native Java application
This covers lab exercises 7 and 8