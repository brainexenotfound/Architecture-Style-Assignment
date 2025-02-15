/******************************************************************************************************************
* File: LoggingServices.java
* Course: 17655
* Project: Assignment A3
* Copyright: Copyright (c) 2018 Carnegie Mellon University
* Versions:
*	1.0 February 2018 - Initial write of assignment 3 (ajl).
*
* Description: This class provides the concrete implementation of the logging micro services. 
* This service runs with the Java RMI library.
* Parameters: None
*
* Internal Methods:
*  void log(Level level, String message, String userId) - logging method for microservices to call the logging service with
*
* External Dependencies: 
*	- rmiregistry must be running to start this server
*   - this service needs to start before any of the other services
******************************************************************************************************************/
import java.io.IOException;
import java.rmi.RemoteException; 
import java.rmi.server.UnicastRemoteObject;
import java.rmi.registry.Registry;
import java.util.logging.*;

public class LoggingServices extends UnicastRemoteObject implements LoggingServicesAI
{ 

    // Concrete implementation of logger using class to identify logger.
    private static final Logger logger = Logger.getLogger(LoggingServices.class.getName());
    
    // constructor
    public LoggingServices() throws RemoteException {
        super();
        try {
            // This block creates a FileHandler instance that will write to the 
            // specified log.txt file in the usr/logging directory with option to append. 
            // NOTE: The file is written to usr/logging directory and not the project
            // working directory usr/app.
            // REF: https://docs.oracle.com/javase/8/docs/api/java/util/logging/FileHandler.html#FileHandler-java.lang.String-boolean-
            FileHandler fileHandler = new FileHandler("/usr/logging/log.txt", true);
            fileHandler.setFormatter(new SimpleFormatter());
            logger.addHandler(fileHandler);
            // Level set to ALL to capture all messages.
            logger.setLevel(Level.ALL);
            logger.info("LoggingServices successfully started.");
        } catch (IOException e) {
            System.err.println("Failed to start logger: " + e.getMessage());
        }
    }

    // Main service loop
    public static void main(String[] args) {
        
        try {            
            // instantiating a LoggingServices object
            LoggingServices obj = new LoggingServices();

            // instantiating a Registry object and binding LoggingServices
            Registry registry = Configuration.createRegistry();
            registry.bind("LoggingServices", obj);

            System.out.println("Logger service started on port 1096, waiting for clients...");

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


