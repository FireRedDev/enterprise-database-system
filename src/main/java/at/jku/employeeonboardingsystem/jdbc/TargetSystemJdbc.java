package at.jku.employeeonboardingsystem.jdbc;

import at.jku.employeeonboardingsystem.domain.Department;
import at.jku.employeeonboardingsystem.domain.Systemuser;
import at.jku.employeeonboardingsystem.domain.Targetsystem;
import at.jku.employeeonboardingsystem.domain.Targetsystemcredentials;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.bouncycastle.openssl.PasswordException;

public class TargetSystemJdbc {

    //Database 1:jdbc:mysql://localhost:3306/test?useSSL=false test test
    //Database 2: jdbc:mysql://localhost:3306/sys?useSSL=false test test
    //Database for Docker: jdbc:mysql://host.docker.internal/test?useSSL=false
    public static void copyDatabaseData(String url, String username, String password) throws SQLException {
        try {
            Connection connection2 = DriverManager.getConnection(url, username, password);
            Statement stmt2 = connection2.createStatement();
            String createTable =
                "CREATE TABLE IF NOT EXISTS TARGETSYSTEMCREDENTIALS " +
                "(id LONG, " +
                "USERNAME VARCHAR(255), " +
                "PASSWORD VARCHAR(255)," +
                "SYSTEMUSER_ID BIGINT," +
                "TARGETSYSTEM_ID LONG," +
                " PRIMARY KEY (SYSTEMUSER_ID))";
            try {
                stmt2.executeUpdate(createTable);
            } catch (SQLException e) {
                System.out.println("Table targetsystemcredentials already exists");
            }
            connection2.close();
        } catch (SQLException e) { // | PasswordException e) {
            System.out.println(e);
        }
    }

    public static boolean checkPassword(String pw) {
        boolean capitalFlag = false;
        boolean lowerCaseFlag = false;
        boolean numberFlag = false;
        char ch;
        for (int i = 0; i < pw.length(); i++) {
            ch = pw.charAt(i);
            if (Character.isDigit(ch)) {
                numberFlag = true;
            } else if (Character.isUpperCase(ch)) {
                capitalFlag = true;
            } else if (Character.isLowerCase(ch)) {
                lowerCaseFlag = true;
            }
            if (numberFlag && capitalFlag && lowerCaseFlag && pw.length() >= 6) {
                return true;
            }
        }
        return false;
    }

    public static void insertIntoDatabase(String url, String username, String password, Targetsystemcredentials credentials)
        throws SQLException {
        try {
            Connection connection2 = DriverManager.getConnection(url, username, password);
            String insert =
                "INSERT INTO TARGETSYSTEMCREDENTIALS(" + "ID,USERNAME,PASSWORD,SYSTEMUSER_ID,TARGETSYSTEM_ID) VALUES (?,?,?,?,?)";
            PreparedStatement stmt = connection2.prepareStatement(insert);
            if (checkPassword(credentials.getPassword())) {
                Long id = credentials.getSystemuser().getId();
                stmt.setLong(1, id);
                String usernameTc = credentials.getUsername();
                stmt.setString(2, usernameTc);
                String passwordTc = credentials.getPassword();
                stmt.setString(3, passwordTc);
                Long userId = credentials.getSystemuser().getId();
                stmt.setLong(4, userId);
                Long targetSystemId = credentials.getTargetsystem().getId();
                stmt.setLong(5, targetSystemId);
                stmt.executeUpdate();
            }

            connection2.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void deleteFromDatabase(String url, String username, String password, Targetsystemcredentials credentials) {
        try {
            Connection connection = DriverManager.getConnection(url, username, password);
            Long id = credentials.getSystemuser().getId();
            String delete = "DELETE FROM TARGETSYSTEMCREDENTIALS WHERE ID =" + id;
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(delete);

            connection.close();
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void deleteFromDatabaseWithUser(Department d, Systemuser user) {
        for (Targetsystem t : d.getTargetsystems()) {
            if (t != null && t.getType().equals("db")) {
                try {
                    Connection con = DriverManager.getConnection(t.getUrl(), t.getUsername(), t.getPassword());
                    Long id = user.getId();
                    String delete = "DELETE FROM TARGETSYSTEMCREDENTIALS WHERE ID =" + id;
                    Statement stmt = con.createStatement();
                    stmt.executeUpdate(delete);
                    con.close();
                } catch (Exception ex) {
                    ex.printStackTrace();
                }
            }
        }
    }
}
