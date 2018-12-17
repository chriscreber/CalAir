package Model;

import java.sql.*;
import java.util.ArrayList;

public class FlightDatabase {
    private static FlightDatabase flightDB = null;
    private Database db;

    private FlightDatabase() {
        db = Database.getInstance();
    }

    private FlightDatabase(int diff) {
        db = Database.getInstance(1);
    }

    public static FlightDatabase getInstance() {
        return flightDB == null ? (flightDB = new FlightDatabase()) : flightDB;
    }

    public static FlightDatabase getInstance(int diff) {
        return flightDB == null ? (flightDB = new FlightDatabase(1)) : flightDB;
    }

    public void insertFlight(Flight flight) {
        PreparedStatement pstmt = null;
        String sql = "INSERT INTO Flights(source, destination, depart, duration, price)"
                + "VALUES(?, ?, ?, ?, ?)";

        System.out.println("Inserting flight...");
        try {
            pstmt = db.getCon().prepareStatement(sql);
            pstmt.setString(1, flight.getSource());
            pstmt.setString(2, flight.getDestination());
            pstmt.setLong(3, flight.getDepartUnix());
            pstmt.setInt(4, flight.getDuration());
            pstmt.setFloat(5, flight.getPrice());
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.closeStatement(pstmt);
        }
    }

    public ArrayList<Flight> getSoldOutFlights(){
        ArrayList<Flight> flightInfo = new ArrayList<Flight>();
        Statement stmt = null;
        String query = "SELECT * FROM Flights WHERE emptySeats = 0";
        ResultSet res = null;

        int flightNum = 0;
        String source = "";
        String destination = "";
        String status = "";
        long depart = 0;
        int duration = 0;
        int emptySeats = 0;
        float price = 0;

        try {
            System.out.println("Getting flights...");
            stmt = db.getCon().createStatement();
            res = stmt.executeQuery(query);



            while (res.next()) {

                flightNum = res.getInt("flightnum");
                source = res.getString("source");
                destination = res.getString("destination");
                status = res.getString("status");
                depart = res.getLong("depart");
                duration = res.getInt("duration");
                emptySeats = res.getInt("emptySeats");
                price = res.getFloat("price");
                flightInfo.add(new Flight(flightNum, source, destination,depart,duration,emptySeats,price,status));
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return flightInfo;

    }

    public ArrayList<Flight> getDelayedFlights(){
        ArrayList<Flight> flightInfo = new ArrayList<Flight>();
        Statement stmt = null;
        String query = "SELECT * FROM Flights WHERE status = 'Delayed' ";
        ResultSet res = null;

        int flightNum = 0;
        String source = "";
        String destination = "";
        String status = "";
        long depart = 0;
        int duration = 0;
        int emptySeats = 0;
        float price = 0;

        try {
            System.out.println("Getting flights...");
            stmt = db.getCon().createStatement();
            res = stmt.executeQuery(query);



            while (res.next()) {

                flightNum = res.getInt("flightnum");
                source = res.getString("source");
                destination = res.getString("destination");
                status = res.getString("status");
                depart = res.getLong("depart");
                duration = res.getInt("duration");
                emptySeats = res.getInt("emptySeats");
                price = res.getFloat("price");
                flightInfo.add(new Flight(flightNum, source, destination,depart,duration,emptySeats,price,status));
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return flightInfo;

    }

    public Flight getFlight(int flightNumber){
        PreparedStatement pstmt = null;
        String sql = "SELECT * FROM Flights WHERE flightnum = ?";
        ResultSet res = null;

        int flightNum = 0;
        String source = "";
        String destination = "";
        long depart = 0;
        int duration = 0;
        int emptySeats = 0;
        int price = 0;
        String status = "";


        try{
            pstmt = db.getCon().prepareStatement(sql);
            pstmt.setInt(1, flightNumber);
            res = pstmt.executeQuery();
            if (!res.next()) {
                System.out.println("Flight Does Not Exist");
                return null;
            }
            flightNum = res.getInt("flightnum");
            source = res.getString("source");
            destination = res.getString("destination");
            depart = res.getLong("depart");
            duration = res.getInt("duration");
            emptySeats = res.getInt("emptySeats");
            price = res.getInt("price");
            status = res.getString("status");


        }
        catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        return new Flight(flightNum, source, destination,depart,duration, emptySeats,price,status);
    }

    public float getEmptySeatProportion(String location) {
        PreparedStatement pstmt = null;
        String sql = "SELECT AVG(emptySeats) AS average "
                + "FROM Flights "
                + "WHERE source = ? OR destination = ?;";
        ResultSet res;
        float averageEmptySeats = 0;

        try {
            System.out.println("Getting average number of empty seats for flights to/from " + location + "...");
            pstmt = db.getCon().prepareStatement(sql);
            pstmt.setString(1, location);
            pstmt.setString(2, location);
            res = pstmt.executeQuery();
            averageEmptySeats = res.getFloat("average");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.closeStatement(pstmt);
        }

        return averageEmptySeats / 20;
    }

    public void editFlight(int flightNum,String status)
    {
        PreparedStatement pstmt = null;
        String sql = "UPDATE  Flights SET status = ? WHERE flightnum = ?";
        ResultSet res = null;

        try{
            pstmt = db.getCon().prepareStatement(sql);

            pstmt.setString(1, status);
            pstmt.setInt(2, flightNum);
            pstmt.executeUpdate();
        }
        catch (SQLException sqle) {
            sqle.printStackTrace();
        }

    }

    public void editSeats(int flightNum,int seats)
    {
        PreparedStatement pstmt = null;
        String sql = "UPDATE  Flights SET emptySeats = ? WHERE flightnum = ?";
        ResultSet res = null;

        try{
            pstmt = db.getCon().prepareStatement(sql);

            pstmt.setInt(1, seats -1);
            pstmt.setInt(2, flightNum);
            pstmt.executeUpdate();
        }
        catch (SQLException sqle) {
            sqle.printStackTrace();
        }

    }

    public float getPriceRecommendation(String location) {
        LocationDatabase locationDB = LocationDatabase.getInstance();

        float basePrice = locationDB.getBasePrice(location);
        float averageEmptySeats = getEmptySeatProportion(location);

        return basePrice - ((averageEmptySeats / 2) * basePrice);
    }

    public boolean planeIsAvailable(String destination, long depart) {
        PreparedStatement pstmt = null;
        String sql = "SELECT destination AS destination "
                + "FROM Flights "
                + "WHERE depart < ? AND (source = ? OR destination = ?) "
                + "ORDER BY depart DESC "
                + "LIMIT 1";
        ResultSet res;

        System.out.println("Checking that the prior flight was a return flight...");
        try {
            pstmt = db.getCon().prepareStatement(sql);
            pstmt.setLong(1, depart);
            pstmt.setString(2, destination);
            pstmt.setString(3, destination);
            res = pstmt.executeQuery();

            if (!res.next()) {
                return true;
            }
            System.out.println("There was a result...");

            System.out.println("destination: " + res.getString("destination"));
            if (res.getString("destination").equals("San Luis Obispo")) {
                return true;
            }
            System.out.println("It was not a return flight...");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.closeStatement(pstmt);
        }

        return false;
    }

    public boolean enoughTimeForReturn(String destination, long depart, int duration) {
        PreparedStatement pstmt = null;
        Connection con = db.getCon();
        ResultSet res;
        String home = "San Luis Obispo";
        int runwayBreak = 2400;                                                 // seconds in 40 minutes
        long nextFlightMin = depart + 2 * duration + runwayBreak;             // earliest the return flight can depart
        int conflictions = 0;

        String sql = "SELECT COUNT(*) AS conflictions "                         // number of flights at the given
                + "FROM Flights "                                               // destination that will be on the SLO
                + "WHERE source = ? AND destination = ? "                       // after the outgoing flight has left
                        + "AND ((depart + ?) > ? AND (depart + ?) < ?);";        // before the return flight can arrive

        try {
            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, home);
            pstmt.setString(2, destination);
            pstmt.setInt(3, 0);
            pstmt.setLong(4, depart);
            pstmt.setInt(5, 0);
            pstmt.setLong(6, nextFlightMin);
            res = pstmt.executeQuery();
            conflictions += res.getInt("conflictions");

            pstmt = con.prepareStatement(sql);
            pstmt.setString(1, destination);
            pstmt.setString(2, home);
            pstmt.setInt(3, duration);
            pstmt.setLong(4, depart);
            pstmt.setInt(5, duration);
            pstmt.setLong(6, nextFlightMin);
            res = pstmt.executeQuery();
            System.out.println(res.getInt("conflictions"));
            conflictions += res.getInt("conflictions");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.closeStatement(pstmt);
        }

        System.out.println("conflictions = " + conflictions);
        return conflictions == 0;
    }

    public boolean timeIsAvailable(long runwayTime) {
        String home = "San Luis Obispo";
        int runwayBreak = 40 * 60;
        PreparedStatement pstmt = null;
        String sqlOutbound = "SELECT COUNT(*) AS conflictions "
                + "FROM Flights "
                + "WHERE source = ? "
                + "AND (depart - ?) < ? AND (depart + ?) > ?;";
        String sqlInbound = "SELECT COUNT(*) AS conflictions "
                + "FROM Flights "
                + "WHERE destination = ? "
                + "AND ((depart + duration) - ?) < ? AND ((depart + duration) + ?) > ?;";
        ResultSet res;
        int conflictions = 0;

        try {
            System.out.println("Checking time conflictions for outgoing flights...");
            pstmt = db.getCon().prepareStatement(sqlOutbound);
            pstmt.setString(1, home);
            pstmt.setInt(2, runwayBreak);
            pstmt.setLong(3, runwayTime);
            pstmt.setInt(4, runwayBreak);
            pstmt.setLong(5, runwayTime);
            res = pstmt.executeQuery();
            conflictions += res.getInt("conflictions");

            System.out.println("Checking time conflictions for incoming flights...");
            pstmt = db.getCon().prepareStatement(sqlInbound);
            pstmt.setString(1, home);
            pstmt.setInt(2, runwayBreak);
            pstmt.setLong(3, runwayTime);
            pstmt.setInt(4, runwayBreak);
            pstmt.setLong(5, runwayTime);
            res = pstmt.executeQuery();
            conflictions += res.getInt("conflictions");
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.closeStatement(pstmt);
        }

        return !(conflictions > 0);
    }

    public long getNextDepart(Flight f) {
        PreparedStatement pstmt = null;
        ResultSet res;
        String sql = "SELECT depart AS depart "
                + "FROM Flights "
                + "WHERE destination = ? AND depart > ? "
                + "LIMIT 1";
        long nextDepart = Long.MAX_VALUE;
        System.out.println("outbound flight depart = " + f.getDepartUnix());

        try {
            pstmt = db.getCon().prepareStatement(sql);
            pstmt.setString(1, f.getDestination());
            pstmt.setLong(2, f.getDepartUnix());
            res = pstmt.executeQuery();

            if (res.next()) {
                nextDepart = res.getLong("depart");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.closeStatement(pstmt);
        }

        return nextDepart;
    }

    //Moved From Database.java
    //Flights

    public ArrayList<Flight> getFlights() {
        ArrayList<Flight> flightInfo = new ArrayList<Flight>();
        Statement stmt = null;
        String query = "SELECT * FROM Flights";
        ResultSet res = null;

        int flightNum = 0;
        String source = "";
        String destination = "";
        String status = "";
        long depart = 0;
        int duration = 0;
        int emptySeats = 0;
        float price = 0;

        try {
            System.out.println("Getting flights...");
            stmt = db.getCon().createStatement();
            res = stmt.executeQuery(query);



            while (res.next()) {

                flightNum = res.getInt("flightnum");
                source = res.getString("source");
                destination = res.getString("destination");
                status = res.getString("status");
                depart = res.getLong("depart");
                duration = res.getInt("duration");
                emptySeats = res.getInt("emptySeats");
                price = res.getFloat("price");

                flightInfo.add(new Flight(flightNum, source, destination,depart,duration,emptySeats,price, status));

            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        } finally {
            db.closeStatement(stmt);
        }

        return flightInfo;
    }

    public void insertFlight(int flightnum, String source,String destination, String status, long depart, int duration, int emptySeats, double price) {
        PreparedStatement pstmt = null;
        String sql = "INSERT INTO Flights(source, destination, depart, status, duration, emptySeats, price)"
                + "VALUES(?, ?, ?, ?, ?, ?, ?)";

        try {
            System.out.println("Inserting flight...");
            pstmt = db.getCon().prepareStatement(sql);

            pstmt.setString(1, source);
            pstmt.setString(2, destination);
            pstmt.setLong(3, depart);
            pstmt.setString(4, status);
            pstmt.setInt(5, duration);
            pstmt.setInt(6, emptySeats);
            pstmt.setDouble(7, price);

            pstmt.executeUpdate();
            System.out.println("Flight successfully inserted...");
        } catch (SQLException sqle) {
            db.closeStatement(pstmt);
            sqle.printStackTrace();
        }
    }

    public void updateFlight(int flightnum, Flight updated) {
        PreparedStatement pstmt = null;
        String sql = "UPDATE Flights SET flightnum = ?, source = ?, destination = ?, depart = ?, status = ?, duration = ?, emptySeats = ?, price = ? WHERE flightnum = ?";


        try {
            System.out.println("Updating flight " + flightnum + "...");
            pstmt = db.getCon().prepareStatement(sql);
            pstmt.setInt(1, updated.getFlightNum());
            pstmt.setString(2, updated.getSource());
            pstmt.setString(3, updated.getDestination());
            pstmt.setLong(4, updated.getDepartUnix());
            pstmt.setString(5, updated.getStatus());
            pstmt.setInt(6, updated.getDuration());
            pstmt.setInt(7, updated.getEmptySeats());
            pstmt.setDouble(8, updated.getPrice());
            pstmt.setInt(5, flightnum);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.closeStatement(pstmt);
        }
    }
}
