/******************************************************************************************************************
* File: AuthServicesAI.java
* Course: 17655
* Project: Assignment A3
* Copyright: Copyright (c) 2018 Carnegie Mellon University
* Versions:
*	1.0 February 2018 - Initial write of assignment 3 (ajl).
*
* Description: This class provides the abstract interface for the authentication micro service, AuthServices.
* The implementation of these abstract interfaces can be found in the AuthServices.java class.
*
* Parameters: None
*
* Internal Methods:
*  String createUser() - create a new user with the provided username and password
*  String auth() - authenticate a user, and if the username/password is correct, return a token
*
* External Dependencies: None
******************************************************************************************************************/

import java.rmi.*;
		
public interface AuthServicesAI extends java.rmi.Remote
{
	/*******************************************************
	 * Create a new user with the provided username and password. 
	 * If the username is not already taken, the user is created.
	 * Otherwise, an error message is returned.
	********************************************************/

	String createUser(String Username, String Password) throws RemoteException;

	/*******************************************************
	* Authenticates a user with the provided username and password. 
	* If the password is correct, a token is returned. 
	* Otherwise, an error message is returned.
	*******************************************************/

	String auth(String Username, String Password) throws RemoteException;

}