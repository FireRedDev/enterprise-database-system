package at.jku.employeeonboardingsystem.jdbc;

import java.sql.*;

public class TargetSystemJdbc {

    public static void copyDatabaseData() throws SQLException {
        try {
            Class.forName("org.h2.Driver");
            Connection connection = DriverManager.getConnection("jdbc:h2:mem:employeeonboardingsystem", "employeeonboardingsystem", "");
            Connection connection2 = DriverManager.getConnection("jdbc:h2:mem:mydb", "sa", "");
            System.out.println(connection);
            System.out.println(connection2);
            Statement stmt = connection.createStatement();
            Statement stmt2 = connection2.createStatement();
            String getSystemCredentials = "SELECT * FROM TARGETSYSTEMCREDENTIALS";
            String createTable =
                "CREATE TABLE TARGETSYSTEMCREDENTIALS " +
                "(id LONG, " +
                " USERNAME VARCHAR(255), " +
                "PASSWORD VARCHAR2 (255)," +
                "SYSTEMUSER_ID BIGINT," +
                "TARGETSYSTEM_ID LONG," +
                " PRIMARY KEY ( SYSTEMUSER_ID))";
            stmt2.executeUpdate(createTable);
            PreparedStatement stmt3 = connection2.prepareStatement(
                "INSERT INTO TARGETSYSTEMCREDENTIALS(" + "ID,USERNAME,PASSWORD,SYSTEMUSER_ID,TARGETSYSTEM_ID) VALUES (?,?,?,?,?)"
            );
            ResultSet rs = stmt.executeQuery(getSystemCredentials);
            while (rs.next()) {
                Long id = rs.getLong(1);
                stmt3.setLong(1, id);
                String username = (rs.getString(2));
                stmt3.setString(2, username);
                String password = rs.getString(3);
                stmt3.setString(3, password);
                Long userId = rs.getLong(4);
                stmt3.setLong(4, userId);
                Long targetsystemId = rs.getLong(5);
                stmt3.setLong(5, targetsystemId);

                stmt3.executeUpdate();
            }
        } catch (SQLException | ClassNotFoundException e) {
            System.out.println(e);
        }
    }
}
