/******************************************************************************************************************
* File: DeleteServicesAI.java
* Course: 17655
* Project: Assignment A3
* Copyright: Copyright (c) 2018 Carnegie Mellon University
* Versions:
*	1.0 February 2018 - Initial write of assignment 3 (ajl).
*
* Description: This class provides the abstract interface for the delete micro service, DeleteServices.
* The implementation of these abstract interfaces can be found in the DeleteServices.java class.
* The micro services are partitioned as Create, Delete, Update, Delete (CRUD) service packages. Each service 
* is its own process (eg. executing in a separate JVM). It would be a good practice to follow this convention
* when adding and modifying services. Note that services can be duplicated and differentiated by IP
* and/or port# they are hosted on.
*
* Parameters: None
*
* Internal Methods:
*  String deleteOrder(String id) - deletes the order associated with the given id
*
* External Dependencies: None
******************************************************************************************************************/

import java.rmi.*;
		
public interface DeleteServicesAI extends java.rmi.Remote
{
	/*******************************************************
	* Deletes an existing new order from the provided id.
	* Returns an OK message or an error string.
	*******************************************************/

	String deleteOrder(String id) throws RemoteException;

}