package database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class JDBCUtil {
	
	public static Connection getConnection() {
		
		String url = "jdbc:mysql://localhost:3306/umi_izakaya";
		String userName = "root";
		String password = "";
		
		Connection con = null;
		
		try {
			Class.forName("com.mysql.cj.jdbc.Driver");
			con = DriverManager.getConnection(url, userName, password);
			System.out.println("Connect successfully!");
		} catch (ClassNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		return con;
    }

    public static void closeConnection(Connection connection) {
    	try {
    		if(connection != null) {
    			connection.close();
    		}
    	} catch(Exception e) {
    		e.printStackTrace();
    	}
    }
}
