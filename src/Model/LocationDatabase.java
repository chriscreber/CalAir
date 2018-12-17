package Model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class LocationDatabase {
    private static LocationDatabase locationDB = null;
    private Database db;

    private LocationDatabase() {
        db = Database.getInstance();
    }

    private LocationDatabase(int diff) {
        db = Database.getInstance(1);
    }

    public static LocationDatabase getInstance() {
        return locationDB == null ? (locationDB = new LocationDatabase()) : locationDB;
    }

    public static LocationDatabase getInstance(int diff) {
        return locationDB == null ? (locationDB = new LocationDatabase(1)) : locationDB;
    }

    public ArrayList<String> getLocations() {
        ArrayList<String> locationNames = new ArrayList<>();
        Statement stmt = null;
        String sql = "SELECT name FROM Locations;";
        ResultSet res;

        try {
            System.out.println("Getting location names...");
            stmt = db.getCon().createStatement();
            res = stmt.executeQuery(sql);

            while (res.next()) {
                locationNames.add(res.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.closeStatement(stmt);
        }

        return locationNames;
    }

    public ArrayList<String> getOtherLocations() {
        ArrayList<String> locationNames = new ArrayList<>();
        Statement stmt = null;
        String sql = "SELECT name FROM Locations WHERE name != 'San Luis Obispo';";
        ResultSet res;

        try {
            System.out.println("Getting location names...");
            stmt = db.getCon().createStatement();
            res = stmt.executeQuery(sql);

            while (res.next()) {
                locationNames.add(res.getString("name"));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.closeStatement(stmt);
        }

        for (String name : locationNames) {
            System.out.println(name);
        }

        return locationNames;
    }

    public float getBasePrice(String location) {
        PreparedStatement pstmt = null;
        String sql = "SELECT basePrice AS basePrice "
                + "FROM Locations "
                + "WHERE name = ?;";
        ResultSet res;
        float basePrice = 0;

        try {
            System.out.println("Getting base price for location " + location + "...");
            pstmt = db.getCon().prepareStatement(sql);
            pstmt.setString(1, location);
            res = pstmt.executeQuery();
            basePrice = res.getFloat("basePrice");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.closeStatement(pstmt);
        }

        return basePrice;
    }

    public int getMeanDuration(String location) {
        PreparedStatement pstmt = null;
        String sql = "SELECT meanDuration AS meanDuration "
                + "FROM Locations "
                + "WHERE name = ?";
        ResultSet res;
        int meanDuration = 0;

        try {
            System.out.println("Getting mean duration for flights to/from " + location);
            pstmt = db.getCon().prepareStatement(sql);
            pstmt.setString(1, location);
            res = pstmt.executeQuery();
            meanDuration = res.getInt("meanDuration");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.closeStatement(pstmt);
        }

        return meanDuration;
    }

    public void updatePrice(String location, float price) {
        PreparedStatement pstmt = null;
        String sql = "UPDATE Locations SET basePrice = ? WHERE name = ?";


        try {
            System.out.println("Updating price for " + location + "...");
            pstmt = db.getCon().prepareStatement(sql);
            pstmt.setFloat(1, price);
            pstmt.setString(2, location);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.closeStatement(pstmt);
        }
    }
}
