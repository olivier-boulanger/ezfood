package connect;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class AccueilBdd {
	
	Connection con = ConnectSGBD.getConnect();
	
	public boolean verifPasswd(String encryptedData) {
		ResultSet nb = null;
		boolean verifPass = false;
		try {
			Statement stmt=con.createStatement();
			nb=stmt.executeQuery("select id from personnel where motdepasse ='" + encryptedData + "'");
			if (nb.next()) {
				verifPass = true;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return verifPass; 
	}

}
