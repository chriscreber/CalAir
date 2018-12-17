package Model;

import java.sql.*;

public class Database {
    private static Database db = null;

    private static final String JDBC_DRIVER = "org.sqlite.JDBC";
    private static final String DB_URL = "jdbc:sqlite:Airline.db";
    private static final String DB_URL_TEST = "jdbc:sqlite:AirlineTest.db";

    private Connection con = null;

    private Database() {
        try {
            Class.forName(JDBC_DRIVER);

            System.out.println("Connecting to database...");
            con = DriverManager.getConnection(DB_URL);
            System.out.println("Successfully connected to database...");
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        } catch (SQLException se) {
            disconnect();
            se.printStackTrace();
        }
    }

    private Database(int diff) {
        try {
            Class.forName(JDBC_DRIVER);

            System.out.println("Connecting to database...");
            con = DriverManager.getConnection(DB_URL_TEST);
            System.out.println("Successfully connected to database...");
        } catch (ClassNotFoundException cnfe) {
            cnfe.printStackTrace();
        } catch (SQLException se) {
            disconnect();
            se.printStackTrace();
        }
    }

    public static Database getInstance() {
        return db == null ? (db = new Database()) : db;
    }

    public static Database getInstance(int i) {
        return db == null ? (db = new Database(1)) : db;
    }

    public Connection getCon() { return db.con; }


    public void closeStatement(Statement stmt) {
        try {
            if (stmt != null) {
                stmt.close();
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    public void disconnect() {
        try {
            if (con != null) {
                System.out.println("Disconnecting from database...");
                con.close();
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}