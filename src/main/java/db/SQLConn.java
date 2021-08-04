package db;

import config.Controller;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.List;

public class SQLConn {

    static Connection getConnection() {
        try {
            Class.forName("com.mysql.cj.jdbc.Driver");
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        Connection conn = null;

        try {
            List<String> dbtoken = Controller.getDbToken();
            conn = DriverManager.getConnection(dbtoken.get(0), dbtoken.get(1), dbtoken.get(2));

        } catch (FileNotFoundException e) {
            System.err.println("Unable to retrieve dbtoken file");
        } catch (SQLException ex) {
            System.out.println("SQLException: " + ex.getMessage());
            System.out.println("SQLState: " + ex.getSQLState());
            System.out.println("VendorError: " + ex.getErrorCode());
        }

        return conn;
    }
}
