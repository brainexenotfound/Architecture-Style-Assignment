/******************************************************************************************************************
* File: CreateServices.java
* Course: 17655
* Project: Assignment A3
* Copyright: Copyright (c) 2018 Carnegie Mellon University
* Versions:
*	1.0 February 2018 - Initial write of assignment 3 (ajl).
*
* Description: This class provides the concrete implementation of the create micro services. These services run
* in their own process (JVM).
*
* Parameters: None
*
* Internal Methods:
*  String newOrder() - creates an order in the ms_orderinfo database from the supplied parameters.
*
* External Dependencies: 
*	- rmiregistry must be running to start this server
*	= MySQL
	- orderinfo database 
    - logging
******************************************************************************************************************/
import java.rmi.NotBoundException;
import java.rmi.RemoteException; 
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.*;
import java.util.logging.Level;

public class CreateServices extends UnicastRemoteObject implements CreateServicesAI
{ 
    // Set up the JDBC driver name and database URL
    static final String JDBC_CONNECTOR = "com.mysql.jdbc.Driver";  
    static final String DB_URL = Configuration.getJDBCConnection();

    // Set up the orderinfo database credentials
    static final String USER = "root";
    static final String PASS = Configuration.MYSQL_PASSWORD;

    // Do nothing constructor
    public CreateServices() throws RemoteException {}

    // Main service loop
    public static void main(String args[]) 
    { 	
    	// What we do is bind to rmiregistry, in this case localhost, port 1099. This is the default
    	// RMI port. Note that I use rebind rather than bind. This is better as it lets you start
    	// and restart without having to shut down the rmiregistry. 

        try 
        { 
            CreateServices obj = new CreateServices();

            Registry registry = Configuration.createRegistry();
            registry.bind("CreateServices", obj);

            String[] boundNames = registry.list();
            System.out.println("Registered services:");
            for (String name : boundNames) {
                System.out.println("\t" + name);
            }
            // Bind this object instance to the name RetrieveServices in the rmiregistry 
            // Naming.rebind("//" + Configuration.getRemoteHost() + ":1099/CreateServices", obj); 

        } catch (Exception e) {

            System.out.println("CreateServices binding err: " + e.getMessage()); 
            e.printStackTrace();
        } 

    } // main


    // Inplmentation of the abstract classes in RetrieveServicesAI happens here.

    // This method add the entry into the ms_orderinfo database

    public String newOrder(String idate, String ifirst, String ilast, String iaddress, String iphone, String authToken) throws RemoteException, NotBoundException
    {
        // Here we create a loggingRegistry object using registry parameters.
        // We look up the LoggingServices from the registry and create an instance of the
        // logging service abstract interface.
        Registry loggingRegistry = LocateRegistry.getRegistry("ms_logging", 1096);
        LoggingServicesAI logger = (LoggingServicesAI) loggingRegistry.lookup("LoggingServices");
        String username = TokenVerification.verifyToken(authToken);
        if (username == null) {
            logger.log(Level.INFO, "Invalid token: %s", authToken);
            return "Invalid token";
        }
      	// Local declarations

        Connection conn = null;		                 // connection to the orderinfo database
        Statement stmt = null;		                 // A Statement object is an interface that represents a SQL statement.
        String ReturnString = "Order Created";	     // Return string. If everything works you get an 'OK' message
        							                 // if not you get an error string

        try
        {
            // Logging the call made to the CreateOrder microservice
            logger.log(Level.INFO, "Method newOrder() called.", username);

            // Here we load and initialize the JDBC connector. Essentially a static class
            // that is used to provide access to the database from inside this class.
            Class.forName(JDBC_CONNECTOR);

            //Open the connection to the orderinfo database

            //System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            // Here we create the queery Execute a query. Not that the Statement class is part
            // of the Java.rmi.* package that enables you to submit SQL queries to the database
            // that we are connected to (via JDBC in this case).

            stmt = conn.createStatement();
            
            String sql = "INSERT INTO orders(order_date, first_name, last_name, address, phone) VALUES (\""+idate+"\",\""+ifirst+"\",\""+ilast+"\",\""+iaddress+"\",\""+iphone+"\")";

            // execute the update
            stmt.executeUpdate(sql);
            
            // Logging the response from the CreateOrder microservice 
            logger.log(Level.INFO, String.format("Successfully created new order, using insert query: %s", sql), username);

            // clean up the environment

            stmt.close();
            conn.close();
            stmt.close(); 
            conn.close();

        } catch(Exception e) {
            // Logging the error encountered by the CreateOrder microservice
            logger.log(Level.SEVERE, "Method newOrder() exception. Error message: " + e.toString(), username);
            ReturnString = e.toString();
        } 
        
        return(ReturnString);

    } //retrieve all orders

} // RetrieveServices