package data_access_object;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import database.JDBCUtil;

public class LoginDAO {

    public boolean authenticateUser(String username, String password) {

        try {
        	Connection connection = JDBCUtil.getConnection();

            String sql = "SELECT * FROM admin WHERE userName = ? AND password = ?";
            PreparedStatement statement = connection.prepareStatement(sql);
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                return true;
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        
        return false;
    }
}
