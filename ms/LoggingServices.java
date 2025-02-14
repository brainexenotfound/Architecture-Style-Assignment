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
import java.rmi.registry.Registry;
import java.util.logging.*;
import java.sql.*;

public class LoggingServices extends UnicastRemoteObject implements LoggingServicesAI
{ 

    private static final Logger logger = Logger.getLogger(LoggingServices.class.getName());
    
    // constructor
    public LoggingServices() throws RemoteException {
        super();
        try {
            FileHandler fileHandler = new FileHandler("/usr/logging/remote_app.log", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            logger.setLevel(Level.ALL);
            logger.info("Here we go again.");
        } catch (IOException e) {
            System.err.println("Failed to start logger: " + e.getMessage());
        }
    }

    public static void main(String[] args) {
        try {            
            LoggingServices obj = new LoggingServices();

            Registry registry = Configuration.createRegistry();
            registry.bind("LoggingServices", obj);

            System.out.println("Logger service started on port 1097, waiting for clients...");

            String[] boundNames = registry.list();
            System.out.println("Registered services:");
            for (String name : boundNames) {
                System.out.println("\t" + name);
            }
        } catch (Exception e) {
            System.err.println("Logger server failed: " + e.getMessage());
            e.printStackTrace();
        }
    }

    @Override
    public void log(Level level, String message, String userId) {
        message += "user: " + userId;
        logger.log(level, message);
    }
}


