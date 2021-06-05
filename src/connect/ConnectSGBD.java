package connect;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class ConnectSGBD {
	
	private static final String url = "jdbc:mysql://localhost/easyfood";
	private static final String user ="root";
	private static final String password ="";
	private static Connection connection;
	
	public static Connection getConnect() {
		if (connection == null) {
			try {
				Class.forName("com.mysql.cj.jdbc.Driver");  
				connection =DriverManager.getConnection(url,user,password); 
			} catch (Exception e) {
				System.out.println(e);
			}
		}
		return connection;
	}

	public PreparedStatement prepareStatement(String string) {
		// TODO Auto-generated method stub
		return null;
	}

	public void close() {
		try {
			connection.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	

}
