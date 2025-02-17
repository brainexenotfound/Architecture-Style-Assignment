# Project Overview

## Web Service
This project consists of a combination of Java and JavaScript files, likely implementing a web service with client-server communication.

### Project Structure

```
/Logger.js          # Handles logging functionality in JavaScript
/OrdersUI.java      # Java-based UI handling orders
/REST.js            # Implements REST API in JavaScript
/Server.js          # Server-side logic in JavaScript
/WSClientAPI.java   # Java client API for WebSocket communication
```

### Installation & Setup
Please refer to the INSTALL.md

### Features

- `Server.js`: Manages server-side logic and communication.
- `REST.js`: Provides RESTful API endpoints.
- `Logger.js`: Handles logging functionality.
- `OrdersUI.java`: Handles order-related UI operations.
- `WSClientAPI.java`: Implements WebSocket communication for real-time data exchange.

### Usage
This section describes how to interact with the Orders Database User Interface using Docker Compose.

To start the client UI, use the following command:

```sh
$ docker-compose -f ws.yml exec client java OrdersUI
```

Upon launching, the system presents an authentication menu:
```
Orders Database User Interface:

Select an Option: 

1: Sign up.
2: Log in.
3: Exit.
```

To log in, select 2 and enter your credentials:
```
Enter your username: evan
Enter your password: huang
```
Once authenticated, you will be presented with order management options.

After logging in, you can interact with the Orders Database:
```
Orders Database User Interface: 

Select an Option: 

1: Retrieve all orders in the order database.
2: Retrieve an order by ID.
3: Add a new order to the order database.
4: Delete an order from the order database.
X: Exit
```

To view all orders, select 1:
```
>>>> 1
Retrieving All Orders::
{"Error":false,"Message":"Success","Orders":[]}
```
If no orders exist, the Orders array will be empty.
To retrieve a specific order, select 2:
```
>>>> 2
Enter the order ID: 12
{"Error":false,"Message":"Success","Users":[]}

Press enter to continue...
```
If the order ID exists, the response will include order details.

To exit the application, select X.

## Microservices

This project contains multiple Java-based microservices for handling authentication, order management, and logging.

### Project Structure

```
/AuthServices.java           # Handles authentication logic
/AuthServicesAI.java         # AI-enhanced authentication services
/CreateServices.java         # Service for creating resources
/CreateServicesAI.java       # AI-enhanced creation services
/DeleteServices.java         # Handles resource deletion
/DeleteServicesAI.java       # AI-enhanced deletion services
/RetrieveServices.java       # Retrieves stored data
/RetrieveServicesAI.java     # AI-enhanced retrieval services
/LoggingServices.java        # Handles logging
/LoggingServicesAI.java      # AI-enhanced logging services
/MSClientAPI.java            # Client API for microservices communication
/OrdersUI.java               # UI logic for order management
/TokenVerification.java      # Manages token verification and security
/Configuration-tmplt.java    # Configuration template for services
/MySqlConstants-tmplt.java   # MySQL database configuration template
/registry.properties         # Service registry properties
/start-ms.sh                 # Shell script to start microservices
```

### Installation & Setup
Please refer to the INSTALL.md

### Features

- **Microservices-based** architecture for authentication and order management.
- **AI-Enhanced Services**: Some services include AI-based optimizations.
- **Logging & Monitoring** to track authentication and service operations.

### Usage
When attaching to the client serice, the following main menu will show up:
```
Orders Database User Interface: 

Select an Option: 

1: Retrieve all orders in the order database.
2: Retrieve an order by ID.
3: Add a new order to the order database.
4: Delete an order in the order database.
8: Login.
9: Signup for a new account.
X: Exit
```

If you are a new user, you must first create a new account by pressing 9.
You will be prompted for a user name and then a password.

After successfully creating a new account, you will be taken back to the main menu.
Creating a new account does not log you in, so you must now press 8.

This will prompt you for your username and password. If successful, you will be authenticated.

Now you can properly use options 1 through 4.

Pressing 1 will retrieve all orders in the database.

Pressing 2 will prompt you for an order id. After entering an ID, you will receive all of the orders associated with that ID.

Pressing 3 will prompt you for first name, last name, address, and phone. Then, it will ask you to press 'y' to create this order. After this, the order will be saved in the database.

Pressing 4 will prompt you for an order id. After entering an ID, the order associated with that ID will be deleted from the database.