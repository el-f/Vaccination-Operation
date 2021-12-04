package project;

import java.sql.*;

public class JDBC {
    public static void main(String[] args) {
        String url = "jdbc:mysql://localhost:3306/vac_operation?useUnicode"
                + "=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC";
        String user = "root";
        String password = "";

        try {
            Connection connection = DriverManager.getConnection(url, user, password);
            Statement myStatement = connection.createStatement();

            ResultSet myResult = myStatement.executeQuery("select citizen_id, worker_id from vaccination");

            while (myResult.next()) {
                System.out.println(myResult.getString("citizen_id"));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
