package Model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ReservationDatabase {
    private static ReservationDatabase reservationDB = null;
    private Database db = null;

    private ReservationDatabase() {
        db = Database.getInstance();
    }

    private ReservationDatabase(int diff) {
        db = Database.getInstance(1);
    }

    public static ReservationDatabase getInstance() {
        return reservationDB == null ? (reservationDB = new ReservationDatabase()) : reservationDB;
    }

    public static ReservationDatabase getInstance(int diff) {
        return reservationDB == null ? (reservationDB = new ReservationDatabase(1)) : reservationDB;
    }

    public boolean checkStatus(int confirmationNumber)
    {
        PreparedStatement pstmt = null;
        ResultSet res = null;

        String sql = "SELECT * FROM Reservations WHERE confirmation = ?";

        try {
            pstmt = db.getCon().prepareStatement(sql);
            pstmt.setString(1, Integer.toString(confirmationNumber));
            res = pstmt.executeQuery();

            if(res.getBoolean("checkedIn"))
                return false;

            sql = "UPDATE Reservations SET checkedIn = 1 WHERE confirmation = ?";
            pstmt = db.getCon().prepareStatement(sql);
            pstmt.setInt(1, confirmationNumber);
            pstmt.executeUpdate();
            System.out.println();

        }
        catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        return true;
    }
    public boolean checkReservation(int confirmationNumber)
    {
        PreparedStatement pstmt = null;
        ResultSet res = null;

        String sql = "SELECT * FROM Reservations WHERE confirmation = ?";

        try {
            System.out.println("Checking confirmation number: " + confirmationNumber + "...");
            pstmt = db.getCon().prepareStatement(sql);
            pstmt.setInt(1, confirmationNumber);
            res = pstmt.executeQuery();
            System.out.println();

            if(!res.isBeforeFirst()) {
                return false;
            }

        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }


        return  true;
    }

    public void setBags(int confirmationNumber, int bags)
    {
        PreparedStatement pstmt = null;
        ResultSet res = null;

        String sql = "UPDATE Reservations SET bags = ? WHERE confirmation = ?";
        try {
            System.out.println("Inserting Bags: " + bags + "...");
            pstmt = db.getCon().prepareStatement(sql);
            pstmt.setString(1, Integer.toString(bags));
            pstmt.setString(2, Integer.toString(confirmationNumber));
            pstmt.executeUpdate();
            System.out.println();

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }

    public int lastCheckinNumber(){
        Statement stmt = null;
        String sql = "SELECT * FROM Reservations WHERE id != 0";
        ResultSet res = null;
        int count = 1;
        int id = 1;

        try {
            System.out.println("Getting Reservation ids...");
            stmt = db.getCon().createStatement();
            res = stmt.executeQuery(sql);

            while (res.next()) {
                count++;
                id = res.getInt("id");
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return count;

    }

    public void insertReservation(User user, Flight depart, Flight returnflight, int confirmationNumber)
    {
        PreparedStatement pstmt = null;
        String sql = "INSERT INTO Reservations (username, departFlight, returnFlight, confirmation)"
                + "VALUES(?, ?, ?, ?)";

        System.out.println("Inserting Reservations...");

        Reservation newReservation = new Reservation(1,user.getUsername(),depart.getFlightNum(),returnflight.getFlightNum(),confirmationNumber,false,0);

        try {
            pstmt = db.getCon().prepareStatement(sql);
            pstmt.setString(1, newReservation.getUsername());
            pstmt.setInt(2, newReservation.getDepartFlight());
            pstmt.setInt(3, newReservation.getReturnFlight());
            pstmt.setInt(4, confirmationNumber);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        } finally {
            db.closeStatement(pstmt);
        }
    }

}
