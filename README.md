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

### Logging
WS server operations are logged to a file named `logs.txt`. This file is generated in the server’s working directory inside the Docker container.

**Location:**
The log file is located at:
`/usr/app/ws/logs.txt`
within the container.

**Accessing `logs.txt`:**

  Run the following command from your project directory:
  ```bash
  docker-compose -f stack-ws.yml exec server cat /usr/app/ws/logs.txt


