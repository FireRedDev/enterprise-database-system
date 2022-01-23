package at.jku.employeeonboardingsystem.jdbc;

import at.jku.employeeonboardingsystem.domain.Systemuser;
import at.jku.employeeonboardingsystem.domain.Targetsystemcredentials;
import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import org.bouncycastle.openssl.PasswordException;

public class TargetSystemJdbc {

    //Databaseexample: Connection connection2 = DriverManager.getConnection("jdbc:mysql://localhost:3306/test","test","test");
    public static void copyDatabaseData(String url, String username, String password) throws SQLException {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection2 = DriverManager.getConnection(url, username, password);
            Statement stmt2 = connection2.createStatement();
            String createTable =
                "CREATE TABLE TARGETSYSTEMCREDENTIALS " +
                "(id LONG, " +
                "USERNAME VARCHAR(255), " +
                "PASSWORD VARCHAR(255)," +
                "SYSTEMUSER_ID BIGINT," +
                "TARGETSYSTEM_ID LONG," +
                " PRIMARY KEY (SYSTEMUSER_ID))";
            try {
                stmt2.executeUpdate(createTable);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            /*     PreparedStatement stmt3 = connection2.prepareStatement(
                "INSERT IGNORE INTO TARGETSYSTEMCREDENTIALS(" + "ID,USERNAME,PASSWORD,SYSTEMUSER_ID,TARGETSYSTEM_ID) VALUES (?,?,?,?,?)"
            );
            ResultSet rs = stmt.executeQuery(getSystemCredentials);
            //if (checkPassword(rs.getString(3))) {
                System.out.println("in if ");
                while (rs.next()) {
                    System.out.println("in while");
                    Long id = rs.getLong(1);
                    stmt3.setLong(1, id);
                    String uname = (rs.getString(2));
                    stmt3.setString(2, uname);
                    String pword = rs.getString(3);
                    stmt3.setString(3, pword);
                    Long userId = rs.getLong(4);
                    stmt3.setLong(4, userId);
                    Long targetsystemId = rs.getLong(5);
                    stmt3.setLong(5, targetsystemId);
                    System.out.println("end of if ");

                    stmt3.executeUpdate();
                    System.out.println("sucessfully executedUpdate ");
                }
           /* } else {
                throw new PasswordException("Das Passwort ist nicht sicher genug");
            }*/
        } catch (SQLException | ClassNotFoundException e) { // | PasswordException e) {
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
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection2 = DriverManager.getConnection(url, username, password);
            String insert =
                "INSERT INTO TARGETSYSTEMCREDENTIALS(" + "ID,USERNAME,PASSWORD,SYSTEMUSER_ID,TARGETSYSTEM_ID) VALUES (?,?,?,?,?)";
            PreparedStatement stmt = connection2.prepareStatement(insert);
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
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void deleteFromDatabase(String url, String username, String password, Targetsystemcredentials credentials) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);
            Long id = credentials.getSystemuser().getId();
            String delete = "DELETE FROM TARGETSYSTEMCREDENTIALS WHERE ID =" + id;
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(delete);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public static void deleteFromDatabaseWithUser(String url, String username, String password, Systemuser user) {
        try {
            Class.forName("com.mysql.jdbc.Driver");
            Connection connection = DriverManager.getConnection(url, username, password);
            Long id = user.getId();
            String delete = "DELETE FROM TARGETSYSTEMCREDENTIALS WHERE ID =" + id;
            Statement stmt = connection.createStatement();
            stmt.executeUpdate(delete);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
