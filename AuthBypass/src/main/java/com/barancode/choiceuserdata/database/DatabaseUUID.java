package main.java.com.barancode.choiceuserdata.database;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.UUID;

import main.java.com.barancode.authbypass.Main;

public class DatabaseUUID {
	
	public static UUID getUUID(String username){
		String s = getValue(username, "username", "uuid", 0).replaceAll("(\\w{8})(\\w{4})(\\w{4})(\\w{4})(\\w{12})", "$1-$2-$3-$4-$5");
		if (!s.equals("")){
			return UUID.fromString(s);
		} else {
			return null;
		}
	}
	
	private static String getValue(String valueGiven, String typeGiven, String typeWanted, int tries){
		if (typeGiven.equals("uuid")) valueGiven = valueGiven.replaceAll("-", "");
		if (Main.db.con != null) {
			Statement statement = null;
			ResultSet rs = null;
			try {
				statement = Main.db.con.createStatement();
				rs = statement.executeQuery("SELECT * FROM players WHERE " + typeGiven + "=\"" + valueGiven + "\" ORDER BY id DESC;");
				if (rs.next()){
					return rs.getString(typeWanted);
				}
				return "";
			} catch (Exception e){
				e.printStackTrace();
				if (tries < 1){
					Main.db.reconnect();
					return getValue(valueGiven, typeGiven, typeWanted, tries + 1);
				} else {
					System.out.println("Attempted to re-connect but failed");
				}
			} finally {
				try {
					if (rs != null) rs.close();
					if (statement != null) statement.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
			}
		} else {
			if (tries < 1){
				Main.db.reconnect();
				return getValue(valueGiven, typeGiven, typeWanted, tries + 1);
			} else {
				System.out.println("Attempted to re-connect but failed");
			}
		}
		return "";
    }
}
