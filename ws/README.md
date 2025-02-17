# Project Overview

This project consists of a combination of Java and JavaScript files, likely implementing a web service with client-server communication.

## Project Structure

```
/Logger.js          # Handles logging functionality in JavaScript
/OrdersUI.java      # Java-based UI handling orders
/REST.js            # Implements REST API in JavaScript
/Server.js          # Server-side logic in JavaScript
/WSClientAPI.java   # Java client API for WebSocket communication
```

## Installation & Setup

1. Ensure you have the required dependencies installed:
   - Node.js for JavaScript files (`Server.js`, `REST.js`, `Logger.js`)
   - Java Development Kit (JDK) for Java files (`OrdersUI.java`, `WSClientAPI.java`)

2. To run the server:
   ```sh
   node Server.js
   ```

3. To use the Java client:
   - Compile the Java files:
     ```sh
     javac OrdersUI.java WSClientAPI.java
     ```
   - Run the Java client:
     ```sh
     java OrdersUI
     ```

## Features

- `Server.js`: Manages server-side logic and communication.
- `REST.js`: Provides RESTful API endpoints.
- `Logger.js`: Handles logging functionality.
- `OrdersUI.java`: Handles order-related UI operations.
- `WSClientAPI.java`: Implements WebSocket communication for real-time data exchange.

## Contribution

Feel free to contribute by submitting issues or pull requests.

## License

This project is licensed under [MIT License](LICENSE).
