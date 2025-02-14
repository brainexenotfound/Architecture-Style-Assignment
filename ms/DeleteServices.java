/******************************************************************************************************************
* File: DeleteServices.java
* Course: 17655
* Project: Assignment A3
* Copyright: Copyright (c) 2018 Carnegie Mellon University
* Versions:
*	1.0 February 2018 - Initial write of assignment 3 (ajl).
*
* Description: This class provides the concrete implementation of the delete micro services. These services run
* in their own process (JVM).
*
* Parameters: None
*
* Internal Methods:
*  String deleteOrder(String id) - deletes the order associated with the order id
*
* External Dependencies: 
*	- rmiregistry must be running to start this server
*	= MySQL
	- orderinfo database 
******************************************************************************************************************/
import java.rmi.NotBoundException;
import java.rmi.RemoteException; 
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.LocateRegistry;
import java.rmi.registry.Registry;
import java.sql.*;
import java.util.logging.Level;

public class DeleteServices extends UnicastRemoteObject implements DeleteServicesAI
{ 
    // Set up the JDBC driver name and database URL
    static final String JDBC_CONNECTOR = "com.mysql.jdbc.Driver";  
    static final String DB_URL = Configuration.getJDBCConnection();

    // Set up the orderinfo database credentials
    static final String USER = "root";
    static final String PASS = Configuration.MYSQL_PASSWORD;

    // Do nothing constructor
    public DeleteServices() throws RemoteException {}

    // Main service loop
    public static void main(String args[]) 
    { 	
    	// What we do is bind to rmiregistry, in this case localhost, port 1097. This is the default
    	// RMI port. Note that I use rebind rather than bind. This is better as it lets you start
    	// and restart without having to shut down the rmiregistry. 

        try 
        { 
            DeleteServices obj = new DeleteServices();

            Registry registry = Configuration.createRegistry();
            registry.rebind("DeleteServices", obj);

            String[] boundNames = registry.list();
            System.out.println("Registered services:");
            for (String name : boundNames) {
                System.out.println("\t" + name);
            }

        } catch (Exception e) {

            System.out.println("DeleteServices binding err: " + e.getMessage()); 
            e.printStackTrace();
        } 

    } // main


    // Inplmentation of the abstract classes in DeleteServicesAI happens here.

    // This method will delete the order in the orderinfo database corresponding to the id
    // provided in the argument.

    public String deleteOrder(String orderid, String authToken) throws RemoteException, NotBoundException
    {
        Registry loggingRegistry = LocateRegistry.getRegistry("ms_logging", 1096);
        LoggingServicesAI logger = (LoggingServicesAI) loggingRegistry.lookup("LoggingServices");
        String username = TokenVerification.verifyToken(authToken);
        if (username == null) {
            logger.log(Level.INFO, "Invalid token: %s", authToken);
            return "Invalid token";
        }
      	// Local declarations

        Connection conn = null;		// connection to the orderinfo database
        Statement stmt = null;		// A Statement object is an interface that represents a SQL statement.
        String ReturnString = "[";	// Return string. If everything works you get an ordered pair of data
        							// if not you get an error string


        try
        {
            logger.log(Level.INFO, String.format("Method deleteOrder() called with ID:%s.", orderid), "TODO");

            // Here we load and initialize the JDBC connector. Essentially a static class
            // that is used to provide access to the database from inside this class.

            Class.forName(JDBC_CONNECTOR);

            //Open the connection to the orderinfo database

            //System.out.println("Connecting to database...");
            conn = DriverManager.getConnection(DB_URL,USER,PASS);

            // Here we create the query Execute a query. Not that the Statement class is part
            // of the Java.rmi.* package that enables you to submit SQL queries to the database
            // that we are connected to (via JDBC in this case).

            // System.out.println("Creating statement...");
            stmt = conn.createStatement();
            
            String sql;
            sql = "DELETE FROM orders where order_id=" + orderid;
            // execute the update
            stmt.executeUpdate(sql);
            //ResultSet rs = stmt.executeQuery(sql);

            logger.log(Level.INFO, String.format("Successfully deleted order ID:%s, using insert query: %s", orderid, sql), "TODO");

            // Extract data from result set. Note there should only be one for this method.
            // I used a while loop should there every be a case where there might be multiple
            // orders for a single ID.

            //Clean-up environment

            stmt.close();
            conn.close();
            stmt.close(); 
            conn.close();

        } catch(Exception e) {
            logger.log(Level.SEVERE, "method deleteOrder() exception. Error message: " + e.toString(), "TODO");
            ReturnString = e.toString();

        } 

        return(ReturnString);

    } //delete order by id

} // DeleteServices