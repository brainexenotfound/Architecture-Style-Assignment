
/******************************************************************************************************************
* File:REST.js
* Course: 17655
* Project: Assignment A3
* Copyright: Copyright (c) 2018 Carnegie Mellon University
* Versions:
*   1.0 February 2018 - Initial write of assignment 3 for 2018 architectures course(ajl).
*
* Description: This module provides the restful webservices for the Server.js Node server. This module contains GET,
* and POST services.  
*
* Parameters: 
*   router - this is the URL from the client
*   connection - this is the connection to the database
*   md5 - This is the md5 hashing/parser... included by convention, but not really used 
*
* Internal Methods: 
*   router.get("/"... - returns the system version information
*   router.get("/orders"... - returns a listing of everything in the ws_orderinfo database
*   router.get("/orders/:order_id"... - returns the data associated with order_id
*   router.post("/order?"... - adds the new customer data into the ws_orderinfo database
*
* External Dependencies: mysql
*
******************************************************************************************************************/

var mysql   = require("mysql");     //Database
const logger = require("./Logger.js");

function REST_ROUTER(router, connection) {
    var self = this;
    self.sessions = {};
    self.handleRoutes(router, connection);
}

// Here is where we define the routes. Essentially a route is a path taken through the code dependent upon the 
// contents of the URL

REST_ROUTER.prototype.handleRoutes = function(router, connection) {
    let self = this;
    // GET with no specifier - returns system version information
    // req paramdter is the request object
    // res parameter is the response object
    
    function generateToken() {
        return (Math.random().toString(36).substr(2) + Date.now().toString(36));
    }

    router.post("/accout", function (req, res) {
        var username = req.body.username;
        var password = req.body.password;

        var SQL = "INSERT INTO accounts (username, password) VALUES (?, ?)";
        SQL = mysql.format(SQL, [username, password]);
        connection.query(SQL, function (err, _) {
            if (err) {
                logger.logAction(username, "CREATE_ACCOUNT", false);
                return res.json({ error: true, message: "Error: " + err });
            }
            let token = generateToken();
            self.sessions[token] = username; // Store session
            res.setHeader("token", token);
            logger.logAction(username, "CREATE_ACCOUNT", true);
            res.json({ error: false, message: "Account created successfully", token: token });
        });
    });

    router.get("/login", function(req, res) {
        var username = req.query.username;
        var password = req.query.password;

        var SQL = "SELECT COUNT(1) as count FROM accounts WHERE username = ? AND password = ?";
        SQL = mysql.format(SQL,[username, password]);
        connection.query(SQL, [username, password], function (err, rows) {
            if (err) {
                logger.logAction(username, "LOGIN", false);
                return res.json({ error: true, message: "Error executing MySQL query", sql: SQL });
            }

            if (rows[0].count === 1) {
                let token = generateToken();
                self.sessions[token] = username; // Store session
                res.setHeader("token", token);
                logger.logAction(username, "LOGIN", true);
                res.json({ error: false, message: "Login successfully", "token": token });
            } else {
                logger.logAction(username, "LOGIN", false);
                res.status(401).json({ error: true, message: "Invalid credentials", query: SQL, result: rows });
            }
        });
    });

    router.get("/", function(req, res) {
        let username = req.query.username || "anonymous";
        logger.logAction(username, "GET_VERSION", true);
        res.json({ "Message": "Orders Webservices Server Version 1.0" });
    });
    
    router.get("/orders", function(req, res) {

    // GET for /orders specifier - returns all orders currently stored in the database
    // req paramdter is the request object
    // res parameter is the response object
  
    let token = req.headers.token;
        if (!token || !self.sessions[token]) {
            logger.logAction("anonymous", "UNAUTHORIZED GET_ALL_ORDERS", false);
            return res.status(401).json({ error: true, message: "Unauthorized" });
        }
        const username = self.sessions[token];
        console.log("Getting all database entries...");
        var query = "SELECT * FROM ??";
        var table = ["orders"];
        query = mysql.format(query, table);
        connection.query(query, function(err, rows) {
            if (err) {
                logger.logAction(username, "GET_ALL_ORDERS", false);
                res.json({ "Error": true, "Message": "Error executing MySQL query" });
            } else {
                logger.logAction(username, "GET_ALL_ORDERS", true);
                res.json({ "Error": false, "Message": "Success", "Orders": rows });
            }
        });
    });

    // GET for /orders/order id specifier - returns the order for the provided order ID
    // req paramdter is the request object
    // res parameter is the response object

    router.get("/orders/:order_id", function(req, res) {
        let token = req.headers.token;
        if (!token || !self.sessions[token]) {
            logger.logAction("anonymous", "UNAUTHORIZED GET_ORDER_BY_ID", false);
            return res.status(401).json({ error: true, message: "Unauthorized" });
        }
        const username = self.sessions[token];
        console.log("Getting order ID: ", req.params.order_id);
        var query = "SELECT * FROM ?? WHERE ??=?";
        var table = ["orders", "order_id", req.params.order_id];
        query = mysql.format(query, table);
        connection.query(query, function(err, rows) {
            if (err) {
                logger.logAction(username, "GET_ORDER_BY_ID", false);
                res.json({ "Error": true, "Message": "Error executing MySQL query" });
            } else {
                logger.logAction(username, "GET_ORDER_BY_ID", true);
                res.json({ "Error": false, "Message": "Success", "Users": rows });
            }
        });
    });

    // POST for /orders?order_date&first_name&last_name&address&phone - adds order
    // req paramdter is the request object - note to get parameters (eg. stuff afer the '?') you must use req.body.param
    // res parameter is the response object 

    router.post("/orders", function(req, res) {
        let token = req.headers.token;
        if (!token || !self.sessions[token]) {
            logger.logAction("anonymous", "UNAUTHORIZED CREATE_ORDER", false);
            return res.status(401).json({ error: true, message: "Unauthorized" });
        }
        const username = self.sessions[token];
        console.log("Adding to orders table ", req.body.order_date, ",", req.body.first_name, ",", req.body.last_name, ",", req.body.address, ",", req.body.phone);
        var query = "INSERT INTO ??(??,??,??,??,??) VALUES (?,?,?,?,?)";
        var table = ["orders", "order_date", "first_name", "last_name", "address", "phone",
                     req.body.order_date, req.body.first_name, req.body.last_name, req.body.address, req.body.phone];
        query = mysql.format(query, table);
        connection.query(query, function(err, rows) {
            if (err) {
                logger.logAction(username, "CREATE_ORDER", false);
                res.json({ "Error": true, "Message": "Error executing MySQL query" });
            } else {
                logger.logAction(username, "CREATE_ORDER", true);
                res.json({ "Error": false, "Message": "User Added !" });
            }
        });
    });

    router.delete("/orders/:order_id", function(req, res) {
        let token = req.headers.token;
        if (!token || !self.sessions[token]) {
            logger.logAction("anonymous", "UNAUTHORIZED DELETE_ORDER", false);
            return res.status(401).json({ error: true, message: "Unauthorized" });
        }
        const username = self.sessions[token];
        console.log("Deleting order ID: ", req.params.order_id);
        var query = "DELETE FROM ?? WHERE ??=?";
        var table = ["orders", "order_id", req.params.order_id];
        query = mysql.format(query, table);
        connection.query(query, function(err, rows) {
            if (err) {
                logger.logAction(username, "DELETE_ORDER", false);
                res.json({ "Error": true, "Message": "Error executing MySQL query" });
            } else {
                logger.logAction(username, "DELETE_ORDER", true);
                res.json({ "Error": false, "Message": "Success" });
            }
        });
    });

}

// The next line just makes this module available... think of it as a kind package statement in Java

module.exports = REST_ROUTER;