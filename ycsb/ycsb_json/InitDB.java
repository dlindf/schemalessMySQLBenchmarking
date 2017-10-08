import java.sql.*;


public class InitDB {
	static final String user = "root";
	static final String password = "root";
	static final String port = "3306";
	static final String host = "mysql";
	static final String dbName = "ycsb";
	static final String jdbcDriver = "com.mysql.jdbc.Driver";


	public static void main(String[] args) {
		try {
			Class.forName(jdbcDriver);
			Connection conn = openConnection(host);
			if(databaseExists(conn)) runStatement(conn, "DROP DATABASE " + dbName);
			runStatement(conn, "CREATE DATABASE " + dbName);
			conn.setCatalog(dbName);
			runStatement(conn, "CREATE TABLE usertable ( "
				+ "YCSB_KEY VARCHAR(255) PRIMARY KEY, "
				+ "FIELDS JSON NOT NULL)");
			conn.close();
		}
		catch(Exception e) {
			e.printStackTrace();
		}
	}

	private static Connection openConnection(String host) throws Exception {
		Connection conn = null;
		String dbURL = "jdbc:mysql://" + host + ":" + port + "/?autoReconnect=true&useSSL=false";
		conn = DriverManager.getConnection(dbURL, user, password);
		System.out.println("connected to the database");
		return conn;
	}

	private static boolean databaseExists(Connection conn) throws Exception {
		ResultSet res = conn.getMetaData().getCatalogs();
		boolean retVal = false;
		while(res.next()) {
			if(dbName.equals(res.getString(1))) retVal = true;
		}
		res.close();
		return retVal;
	}

	private static void runStatement(Connection conn, String query) throws Exception {
		Statement stmt = null;
		stmt = conn.createStatement();
		stmt.executeUpdate(query);
		stmt.close();
	}

}
