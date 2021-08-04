package logic;

import db.SQLFunctions;
import model.Request;
import net.dv8tion.jda.internal.utils.tuple.ImmutablePair;
import net.dv8tion.jda.internal.utils.tuple.Pair;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 * Contains the logic to perform SQL queries.
 */
public class GamesMethod {
    public static boolean createUser(String userId) {
        try {
            PreparedStatement stmt = SQLFunctions.createUser();
            stmt.setString(1, userId);
            int update = stmt.executeUpdate();

            stmt.close();
            return update == 1;

        } catch (SQLException ex) {
            System.err.println("Error in performing function createUser()");
            System.err.println("SQLException: " + ex.getMessage());
            return false;
        }
    }

    public static int getBalance(String userId) {
        try {
            PreparedStatement stmt = SQLFunctions.getUserInfo();
            stmt.setString(1, userId);
            ResultSet resultSet = stmt.executeQuery();

            boolean found = resultSet.next();
            int ret = -1;
            if (found) {
                ret = resultSet.getInt("bal");
            }

            stmt.close();
            return ret;

        } catch (SQLException ex) {
            System.err.println("Error in performing function getBalance()");
            System.err.println("SQLException: " + ex.getMessage());
            return -1;
        }
    }

    public static List<Pair<String, Integer>> getTop10Users() {
        try {
            PreparedStatement stmt = SQLFunctions.getTop10Users();
            ResultSet resultSet = stmt.executeQuery();

            List<Pair<String, Integer>> uaList = new ArrayList<>();
            while(resultSet.next()) {
                uaList.add(ImmutablePair.of(resultSet.getString("uid"), resultSet.getInt("bal")));
            }
            stmt.close();
            return uaList;

        } catch (SQLException ex) {
            System.err.println("Error in performing function getUserInfo()");
            System.err.println("SQLException: " + ex.getMessage());
            return null;
        }
    }

    public static int getTotalBalance() {
        try {
            PreparedStatement stmt = SQLFunctions.getTotalBalance();
            ResultSet resultSet = stmt.executeQuery();

            boolean found = resultSet.next();
            int ret = -1;
            if (found) {
                ret = resultSet.getInt(1);
            }

            stmt.close();
            return ret;

        } catch (SQLException ex) {
            System.err.println("Error in performing function getTotalBalance()");
            System.err.println("SQLException: " + ex.getMessage());
            return -1;
        }
    }

    public static boolean insertRequest(Request req) {
        try {
            PreparedStatement stmt = SQLFunctions.insertRequest();
            stmt.setString(1, req.getUserId());
            stmt.setInt(2, req.getGtype());
            stmt.setInt(3, req.getGdetail());
            stmt.setInt(4, req.getAmount());
            int update = stmt.executeUpdate();
            stmt.close();

            if (update == 1) {
                stmt = SQLFunctions.updateBalanceMinus();
                stmt.setInt(1, req.getAmount());
                stmt.setString(2, req.getUserId());
                update = stmt.executeUpdate();
            }

            return update == 1;

        } catch (SQLException ex) {
            System.err.println("Error in performing function insertRequest()");
            System.err.println("SQLException: " + ex.getMessage());
            return false;
        }
    }

    public static List<Request> getRequests(ZonedDateTime start) {
        Timestamp timestamp_start = Timestamp.from(start.toInstant());
        try {
            PreparedStatement stmt = SQLFunctions.getRequests();
            stmt.setTimestamp(1, timestamp_start);
            ResultSet resultSet = stmt.executeQuery();

            List<Request> uaList = new ArrayList<>();
            while(resultSet.next()) {
                uaList.add(new Request(resultSet.getString("uid"),resultSet.getInt("gtype"), resultSet.getInt("gdetail"), resultSet.getInt("amount")));
            }
            stmt.close();

            return uaList;

        } catch (SQLException ex) {
            System.err.println("Error in performing function getRequests()");
            System.err.println("SQLException: " + ex.getMessage());
            return null;
        }
    }

    public static void insertFinalLog(List<Pair<String, Integer>> toUpdate) {
        while (toUpdate.size() > 0) {
            Pair<String, Integer> i = toUpdate.remove(0);

            try {
                PreparedStatement stmt = SQLFunctions.insertFinalLog();
                stmt.setString(1, i.getLeft());
                stmt.setInt(2, i.getRight());
                int update = stmt.executeUpdate();
                stmt.close();

                if (update == 1) {
                    if (i.getRight() >= 0) {
                        stmt = SQLFunctions.updateBalancePlus();
                    } else {
                        stmt = SQLFunctions.updateBalanceMinus();
                    }

                    stmt.setInt(1, i.getRight());
                    stmt.setString(2, i.getLeft());
                    update = stmt.executeUpdate();
                }

            } catch (SQLException ex) {
                System.err.println("Error in performing function insertFinalLog()");
                System.err.println("SQLException: " + ex.getMessage());
            }
        }
    }

}
