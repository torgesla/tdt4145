package tdt4145;
import java.sql.*;
public class Driver {
	static ResultSet result = null;
	
	public static  void Write(String query){
		try {
			Connection myconn = DriverManager.getConnection("jdbc:mysql://localhost:3306/treningslogg",
					"torgesla", "torgeslapw" );
			Statement statement = myconn.createStatement();
		    statement.execute(query);
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	public static ResultSet Read(String query) {
		try {
			Connection myconn = DriverManager.getConnection("jdbc:mysql://localhost:3306/treningslogg",
					"torgesla", "torgeslapw" );
			Statement statement = myconn.createStatement();
			result = statement.executeQuery(query);
		} catch (Exception e) {
		}
		return result;
	}
	public static void PrintTable(String table) throws SQLException {
		String query = "select* from "+table;
		ResultSet result = Read(query);
		int columns = result.getMetaData().getColumnCount();
		while(result.next()) {
			String string = "";
			for (int i = 1; i < columns+1; i++) {
				string+=result.getString(i)+",";
			}
			System.out.println(string);
		}	
	}
	public static void PrintSet(ResultSet result) throws SQLException {
		String table = result.getMetaData().getTableName(1);
		PrintTable(table);
	}
}
