/******************************************************************************************************************
* File: AuthServices.java
* Course: 17655
* Project: Assignment A3
* Copyright: Copyright (c) 2018 Carnegie Mellon University
* Versions:
*	1.0 February 2018 - Initial write of assignment 3 (ajl).
*
* Description: This class provides the concrete implementation of the auth micro services. These services run
* in their own process (JVM).
*
* Parameters: None
*
* Internal Methods:
*  String createUser() - create a new user with the provided username and password
*  String auth() - authenticate a user, and if the username/password is correct, return a token
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

public class AuthServices extends UnicastRemoteObject implements AuthServicesAI
{ 
    // Set up the JDBC driver name and database URL
    static final String JDBC_CONNECTOR = "com.mysql.jdbc.Driver";  
    static final String DB_URL = Configuration.getJDBCConnection();

    // Set up the orderinfo database credentials
    static final String USER = "root";
    static final String PASS = Configuration.MYSQL_PASSWORD;

    // Do nothing constructor
    public AuthServices() throws RemoteException {}

    // Main service loop
    public static void main(String args[]) 
    { 	
    	// What we do is bind to rmiregistry, in this case localhost, port 1099. This is the default
    	// RMI port. Note that I use rebind rather than bind. This is better as it lets you start
    	// and restart without having to shut down the rmiregistry. 

        try 
        { 
            AuthServices obj = new AuthServices();

            Registry registry = Configuration.createRegistry();
            registry.bind("AuthServices", obj);

            String[] boundNames = registry.list();
            System.out.println("Registered services:");
            for (String name : boundNames) {
                System.out.println("\t" + name);
            }

        } catch (Exception e) {

            System.out.println("AuthServices binding err: " + e.getMessage()); 
            e.printStackTrace();
        } 

    } // main


    // Inplmentation of the abstract classes in AuthServicesAI happens here.

    // This method is used to create a user.
    // It queries the database to see if the user is in the database.
    // If the user is not in the database, it adds the user to the database.
    // Else, it returns an error message.

    public String createUser(String iusername, String ipassword) throws RemoteException, NotBoundException
    {
        Connection conn = null;
        Statement stmt = null;
        String ReturnString = "User Created";
        Registry loggingRegistry = LocateRegistry.getRegistry("ms_logging", 1096);
        LoggingServicesAI logger = (LoggingServicesAI) loggingRegistry.lookup("LoggingServices");

        try
        {
            logger.log(Level.INFO, "Method createUser() called.", "Null");
            Class.forName(JDBC_CONNECTOR);
            conn = DriverManager.getConnection(DB_URL, USER, PASS);
            stmt = conn.createStatement();

            String sql = "SELECT * FROM users WHERE username='" + iusername + "'";
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                ReturnString = "Error: User already exists";
                logger.log(Level.INFO, String.format("User %s already exists", iusername), "Null");
            } else {
                // * Insert the new user
                // Note: for simplicity, we store the password in plain text.
                // In a real system, we should hash the password before storing it in the database.
                sql = "INSERT INTO users (username, password) VALUES ('" + iusername + "', '" + ipassword + "')";
                stmt.executeUpdate(sql);
                logger.log(Level.INFO, String.format("Successfully create new user %s, using insert query: %s", iusername, sql), iusername);
            }

            rs.close();
            stmt.close();
            conn.close();
            stmt.close();
            conn.close();
        } catch (Exception e) {
            logger.log(Level.SEVERE, "Method createUser() exception. Error message: " + e.toString(), "Null");
            ReturnString = e.toString();
        }

        return ReturnString;
    }

    // This method is used to authenticate a user. 
    // It queries the database to see if the user is in the database. 
    // If the user is in the database and the password matches, it returns a token.
    // Else, it returns an error message.

    public String auth(String iusername, String ipassword) throws RemoteException, NotBoundException
    {
        Registry loggingRegistry = LocateRegistry.getRegistry("ms_logging", 1096);
        LoggingServicesAI logger = (LoggingServicesAI) loggingRegistry.lookup("LoggingServices");
      	// Local declarations

        Connection conn = null;		                 // connection to the orderinfo database
        Statement stmt = null;		                 // A Statement object is an interface that represents a SQL statement.
        String ReturnString = "User Authenticated.";	     // Return string. If everything works you get an 'OK' message
        							                 // if not you get an error string
        try
        {
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
            
            String sql = "SELECT * FROM users WHERE username='" + iusername + "'";
            ResultSet rs = stmt.executeQuery(sql);

            if (rs.next()) {
                String dbPassword = rs.getString("password");
                if (dbPassword.equals(ipassword)) {
                    // * Generate a token
                    // Note: For simplicity, we use username as token here.
                    // In a real system, we should generate a unique token and store it in the database.
                    ReturnString = "Token: " + iusername;
                    logger.log(Level.INFO, String.format("User %s authenticated.", iusername), "TODO");
                } else {
                    ReturnString = "Error: Incorrect password";
                    logger.log(Level.INFO, String.format("User %s failed to authenticate.", iusername), "TODO");
                }
            } else {
                ReturnString = "Error: User not found";
                logger.log(Level.INFO, String.format("User %s not found.", iusername), "TODO");
            }

            rs.close();

            // clean up the environment

            stmt.close();
            conn.close();
            stmt.close(); 
            conn.close();

        } catch(Exception e) {

            ReturnString = e.toString();
        } 
        
        return(ReturnString);

    } //auth

} // RetrieveServices