const fs = require("fs");
const path = require("path");

const logFilePath = path.join(__dirname, "logs.txt");

/**
 * Appends a log entry to logs.txt
 *
 * @param {string} username - The name of the user performing the action
 * @param {string} action - The action performed
 * @param {boolean} success - Whether the operation was successful
 */
function logAction(username, action, success) {
    const timestamp = new Date().toISOString();
    const logLine = `${timestamp}, user=${username}, action=${action}, success=${success}\n`;
    fs.appendFile(logFilePath, logLine, (err) => {
        if (err) {
            console.error("LOG ERROR:", err);
        } else {
            console.log("Log entry appended to:", logFilePath);
        }
    });
}

module.exports = {
    logAction
};
