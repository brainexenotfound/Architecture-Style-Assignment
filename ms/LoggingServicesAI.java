/******************************************************************************************************************
* File: LoggingServicesAI.java
* Course: 17655
* Project: Assignment A3
* Copyright: Copyright (c) 2018 Carnegie Mellon University
* Versions:
*	1.0 February 2018 - Initial write of assignment 3 (ajl).
*
* Description: This class provides the abstract interface for the logging micro service, LoggingServices.
* The implementation of these abstract interfaces can be found in the LoggingService.java class.
* This logging service is utilizing the Java RMI remote procedure calls.  
* Parameters: None
*
* Internal Methods:
*  void log(Level level, String message, String userId) - logging method for microservices to call the logging service with
*
* External Dependencies: None
******************************************************************************************************************/

import java.rmi.*;
import java.util.logging.Level;

		
public interface LoggingServicesAI extends java.rmi.Remote
{
	/*******************************************************
	* Sends log data from microservice to the LoggingService 
	* microservice
	*******************************************************/

	void log(Level level, String message, String userId) throws RemoteException;

}
