/******************************************************************************************************************
* File: RetrieveServices.java
* Course: 17655
* Project: Assignment A3
* Copyright: Copyright (c) 2018 Carnegie Mellon University
* Versions:
*	1.0 February 2018 - Initial write of assignment 3 (ajl).
*
* Description: This class provides the concrete implementation of the retrieve micro services. These services run
* in their own process (JVM).
*
* Parameters: None
*
* Internal Methods:
*  String retrieveOrders() - gets and returns all the orders in the orderinfo database
*  String retrieveOrders(String id) - gets and returns the order associated with the order id
*
* External Dependencies: 
*	- rmiregistry must be running to start this server
*	= MySQL
	- orderinfo database 
******************************************************************************************************************/
import java.io.IOException;
import java.rmi.RemoteException; 
import java.rmi.server.UnicastRemoteObject;
// import java.rmi.registry.Registry;
import java.util.logging.*;
// import java.sql.*;

public class LoggingServices extends UnicastRemoteObject implements LoggingServicesAI
{ 
    // Set up the JDBC driver name and database URL
    // static final String JDBC_CONNECTOR = "com.mysql.jdbc.Driver";  
    // static final String DB_URL = Configuration.getJDBCConnection();

    // // Set up the orderinfo database credentials
    // static final String USER = "root";
    // static final String PASS = Configuration.MYSQL_PASSWORD;

    private static final Logger logger = Logger.getLogger(LoggingServices.class.getName());
    
    // constructor
    protected LoggingServices() throws RemoteException {
        super();
        try {
            FileHandler fileHandler = new FileHandler("/usr/app/remote_app.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            logger.setLevel(Level.ALL);
        } catch (IOException e) {
            System.err.println("Failed to start logger: " + e.getMessage());
        }
    }

    @Override
    public void log(Level level, String message) {
        logger.log(level, message);
    }
}


