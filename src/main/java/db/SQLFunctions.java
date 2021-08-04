package db;

import java.sql.PreparedStatement;
import java.sql.SQLException;

/**
 * Contains all the SQL functions required for this program.
 */
public class SQLFunctions {
    public static PreparedStatement createUser() throws SQLException, NullPointerException {
        String query = "INSERT INTO Users(uid, bal) VALUES (?, 10)";
        return SQLConn.getConnection().prepareStatement(query);
    }

    public static PreparedStatement getUserInfo() throws SQLException, NullPointerException {
        String query = "SELECT bal FROM Users WHERE uid = ?";
        return SQLConn.getConnection().prepareStatement(query);
    }

    public static PreparedStatement getTop10Users() throws SQLException, NullPointerException {
        String query = "SELECT uid, bal FROM Users ORDER BY bal DESC LIMIT 10";
        return SQLConn.getConnection().prepareStatement(query);
    }

    public static PreparedStatement getTotalBalance() throws SQLException, NullPointerException {
        String query = "SELECT SUM(bal) FROM Users";
        return SQLConn.getConnection().prepareStatement(query);
    }

    public static PreparedStatement insertRequest() throws SQLException, NullPointerException {
        String query = "INSERT INTO Requests(uid, gtype, gdetail, amount) VALUES (?, ?, ?, ?)";
        return SQLConn.getConnection().prepareStatement(query);
    }

    public static PreparedStatement updateBalancePlus() throws SQLException, NullPointerException {
        String query = "UPDATE Users SET bal = bal + ? WHERE uid = ?";
        return SQLConn.getConnection().prepareStatement(query);
    }

    public static PreparedStatement updateBalanceMinus() throws SQLException, NullPointerException {
        String query = "UPDATE Users SET bal = bal - ? WHERE uid = ?";
        return SQLConn.getConnection().prepareStatement(query);
    }

    public static PreparedStatement getRequests() throws SQLException, NullPointerException {
        String query = "SELECT uid, gtype, gdetail, amount FROM Requests WHERE request_datetime >= ?";
        return SQLConn.getConnection().prepareStatement(query);
    }

    public static PreparedStatement insertFinalLog() throws SQLException, NullPointerException {
        String query = "INSERT INTO FinalLogs(uid, amount) VALUES (?, ?)";
        return SQLConn.getConnection().prepareStatement(query);
    }
}
