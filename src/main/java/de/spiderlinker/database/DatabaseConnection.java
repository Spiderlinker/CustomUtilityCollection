package de.spiderlinker.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public abstract class DatabaseConnection {

    private static String databaseURL;
    private static String username;
    private static String password;
    private static String databaseDriver;

    // Pool mit Datenbankverbindungen
    private static Connection connection;

    private DatabaseConnection() {
        // Single instance
    }

    /**
     * Set up the connection to a database
     *
     * @param databaseDriver Driver of the database
     * @param databaseURL    Database URL
     * @param username       Username to login to the database
     * @param password       password of the given username
     */
    public static void setupConnection(String databaseDriver, String databaseURL, String username, String password) {
        DatabaseConnection.databaseDriver = databaseDriver;
        DatabaseConnection.databaseURL = databaseURL;
        DatabaseConnection.username = username;
        DatabaseConnection.password = password;
    }

    /**
     * Liefert eine neue Connection zu der Datenbank.
     *
     * @return eine neue Connection zu der Datenbank
     * @throws SQLException Falls
     */
    public static Connection getConnection() throws SQLException {
        if (isConnectionClosed()) {
            establishConnection();
        }
        return connection;
    }

    private static void establishConnection() throws SQLException {

    	// Prüfen, ob die Datenbankverbindung initialisiert worden ist
        if (databaseDriver == null || databaseURL == null) {
            throw new SQLException("DatabaseConnection is not setup. Please call #setupConnection() to intialize the database connection!");
        }

        try {
            Class.forName(databaseDriver);
            connection = DriverManager.getConnection(databaseURL, username, password);
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    /**
     * Gibt an, ob die Verbindung zur Datenbank getrennt wurde bzw. nicht aufgebaut
     * ist
     *
     * @return true, falls keine Verbindung besteht; andernfalls false
     * @throws SQLException Fehler beim Prüfen, ob Datenbank geschlossen
     */
    public static boolean isConnectionClosed() throws SQLException {
        return connection == null || connection.isClosed();
    }

    /**
     * Schließt alle Connections (den ConnectionPool) zu der Datenbank
     *
     * @throws SQLException Fehler beim Schließen der Connections
     */
    public static void closeConnection() throws SQLException {
        if (connection != null) {
            connection.close();
            connection = null;
        }
    }

}
