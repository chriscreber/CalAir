package Model;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;

public class UserDatabase {
    private static UserDatabase userDB = null;
    private Database db = null;

    private UserDatabase() {
        db = Database.getInstance();
    }

    private UserDatabase(int diff) {
        db = Database.getInstance(1);
    }

    public static UserDatabase getInstance() {
        return userDB == null ? (userDB = new UserDatabase()) : userDB;
    }

    public static UserDatabase getInstance(int diff) {
        return userDB == null ? (userDB = new UserDatabase(1)) : userDB;
    }

    public void insertUser(String username, String password, String firstName,
                           String lastName, String email, long birthDate,
                           int role) {
        PreparedStatement pstmt = null;
        String sql = "INSERT INTO Users(username, password, firstname, lastname, email, birthDate, role)"
                + "VALUES (?, ?, ?, ?, ?, ?, ?);";

        try {
            System.out.println("Inserting user:" + username + "...");
            pstmt = db.getCon().prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);
            pstmt.setString(3, firstName);
            pstmt.setString(4, lastName);
            pstmt.setString(5, email);
            pstmt.setLong(6, birthDate);
            pstmt.setInt(7, role);
            pstmt.executeUpdate();
            System.out.println("User successfully inserted...");
            db.closeStatement(pstmt);
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public boolean usernameExists(String username) {
        PreparedStatement pstmt = null;
        String sql = "SELECT COUNT(*) AS total FROM Users WHERE username = ?;";
        ResultSet res = null;
        int total = 0;
        System.out.println(username);

        try {
            System.out.println("Checking username: " + username + "...");
            pstmt = db.getCon().prepareStatement(sql);
            pstmt.setString(1, username);
            res = pstmt.executeQuery();
            System.out.println();

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return total > 0;
    }

    public User getUser(String username)
    {

        PreparedStatement pstmt = null;
        String sql = "SELECT * FROM Users WHERE username = ?";
        ResultSet res = null;

        String firstName = "";
        String lastName = "";
        String email = "";
        String day = "";
        String month = "";
        String year = "";
        long bday = 0;
        int role = 0;
        LocalDate date = null;

        try{
            pstmt = db.getCon().prepareStatement(sql);
            pstmt.setString(1, username);
            res = pstmt.executeQuery();
            if (!res.next()) {
                System.out.println("User Does Not Exist");
                return null;
            }
            firstName = res.getString("firstName");
            lastName = res.getString("lastName");
            email = res.getString("email");
            role = res.getInt("role");
            bday = res.getLong("birthDate");

        }
        catch (SQLException sqle) {
            sqle.printStackTrace();
        }
        date = DateConverter.getInstance().timestampToDateTime(bday).toLocalDate();
        return new User(username, firstName, lastName,email,date, role);

    }

    public void editUser(User user,String username)
    {
        PreparedStatement pstmt = null;
        String sql = "UPDATE  Users SET firstName = ?, lastName = ?, email = ?, birthDate = ?, role = ?,username = ? WHERE username = ?";
        ResultSet res = null;

        try{
            pstmt = db.getCon().prepareStatement(sql);

            pstmt.setString(1, user.getFirstName());
            pstmt.setString(2, user.getLastName());
            pstmt.setString(3, user.getEmail());
            pstmt.setLong(4, DateConverter.getInstance().dateToTimestamp(user.getBirthDate()));
            pstmt.setInt(5,user.getRole());
            pstmt.setString(6,user.getUsername());
            pstmt.setString(7,username);
            pstmt.executeUpdate();


        }
        catch (SQLException sqle) {
            sqle.printStackTrace();
        }

    }

    public void deleteUserEntry(String username)
    {
        PreparedStatement pstmt = null;
        String sql = "DELETE FROM Users WHERE username = ?";
        ResultSet res = null;

        try {
            pstmt = db.getCon().prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.executeUpdate();
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    public User login(String username, String password)
    {
        PreparedStatement pstmt = null;
        String sql = "SELECT * FROM Users WHERE username = ? AND password = ?";
        ResultSet res = null;
        String firstName = "";
        String lastName = "";
        String email = "";
        LocalDate bday = null;
        int role = 0;

        try {
            System.out.println("Verifying login credentials...");

            pstmt = db.getCon().prepareStatement(sql);
            pstmt.setString(1, username);
            pstmt.setString(2, password);

            res = pstmt.executeQuery();

            if (!res.next()) {
                System.out.println("Invalid login");
                return null;
            }

            firstName = res.getString("firstName");
            lastName = res.getString("lastName");

            role = res.getInt("role");
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        return new User(username, firstName, lastName,email,bday, role);
    }

    public void disconnect() {
        db.disconnect();
    }
}
