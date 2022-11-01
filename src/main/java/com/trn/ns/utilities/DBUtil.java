package com.trn.ns.utilities;

import static com.trn.ns.test.configs.Configurations.TEST_PROPERTIES;

import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class DBUtil{

	private static Connection con;

	private static final String Driver = "com.microsoft.sqlserver.jdbc.SQLServerDriver";

	/*
	 * Builds the string URL to connect to the database
	 * Supports integrated security and standard SQL Server Authentication
	 * @return a string of the URL to connect to the North Star SQL Database
	 */
	private static String getDBURL() {
		StringBuilder dbURLBuilder = new StringBuilder("jdbc:sqlserver://" + TEST_PROPERTIES.get("nsHostName") + ":" + TEST_PROPERTIES.get("dbPort") + ";");
		dbURLBuilder.append("DatabaseName=" + TEST_PROPERTIES.get("dbName") + ";");
		if(Boolean.parseBoolean(TEST_PROPERTIES.get("isDBIntegrated"))) {
			dbURLBuilder.append("integratedSecurity=true;");
		} else {
			dbURLBuilder.append("user=" + TEST_PROPERTIES.get("dbUserName") + ";");
			dbURLBuilder.append("password=" + TEST_PROPERTIES.get("dbPassword") + ";");
		}
		return dbURLBuilder.toString();
	}


	/**
	 * to load the database base driver
	 * @return a database connection
	 * @throws SQLException throws an exception if an error occurs
	 */
	private static  Connection loadDriver() throws SQLException {
		try {
			Class.forName(Driver);
		} catch (ClassNotFoundException ex) {
			System.out.println(ex.getMessage());
		}
		con = DriverManager.getConnection(getDBURL());
		return con;
	}

	/**
	 * to get a result set of a query
	 * @param query custom query
	 * @return a result set of custom query
	 * @throws SQLException throws an exception if an error occurs
	 */
	public static ResultSet getResultSet(String query) throws SQLException {
		con = loadDriver();
		ResultSet rs;
		PreparedStatement st = con.prepareStatement(query);
		rs = st.executeQuery();
		return rs;
	}

	/**
	 * to run an update query such as update, delete
	 * @param query custom query
	 * @throws SQLException throws an exception if an error occurs
	 */
	public static void runQuery(String query) throws SQLException {
		con = loadDriver();
		PreparedStatement st = con.prepareStatement(query);
		st.executeUpdate();
		con.close();

	}

	public static void closeDBConnection() throws SQLException {

		con.close();
	}


	public static int getResultFromFunction(String instanceID,String batchMachineID) throws SQLException {
		con = loadDriver();
		CallableStatement  st = con.prepareCall("{? = call dbo.GetMachineDisplayElementCount(?,?)}");
		st.registerOutParameter(1, java.sql.Types.INTEGER);
		st.setString(2,instanceID);
		st.setString(3,batchMachineID);
		st.execute();
		int output = st.getInt(1);
		st.close();
		return output;


	}

	private static String getDBURL(String dbName) {
		StringBuilder dbURLBuilder = new StringBuilder("jdbc:sqlserver://" + TEST_PROPERTIES.get("nsHostName") + ":" + TEST_PROPERTIES.get("dbPort") + ";");
		dbURLBuilder.append("DatabaseName=" +dbName+ ";");
		if(Boolean.parseBoolean(TEST_PROPERTIES.get("isDBIntegrated"))) {
			dbURLBuilder.append("integratedSecurity=true;");
		} else {
			dbURLBuilder.append("user=" + TEST_PROPERTIES.get("dbUserName") + ";");
			dbURLBuilder.append("password=" + TEST_PROPERTIES.get("dbPassword") + ";");
		}
		return dbURLBuilder.toString();
	}


	/**
	 * to load the database base driver
	 * @return a database connection
	 * @throws SQLException throws an exception if an error occurs
	 */
	private static  Connection loadDriver(String dbName) throws SQLException {
		try {
			Class.forName(Driver);
		} catch (ClassNotFoundException ex) {
			System.out.println(ex.getMessage());
		}
		con = DriverManager.getConnection(getDBURL(dbName));
		return con;
	}

	/**
	 * to get a result set of a query
	 * @param query custom query
	 * @return a result set of custom query
	 * @throws SQLException throws an exception if an error occurs
	 */
	public static ResultSet getResultSet(String query,String dbName) throws SQLException {
		con = loadDriver(dbName);
		ResultSet rs;
		PreparedStatement st = con.prepareStatement(query);
		rs = st.executeQuery();

		return rs;
	}

	/**
	 * to run an update query such as update, delete
	 * @param query custom query
	 * @throws SQLException throws an exception if an error occurs
	 */
	public static void runQuery(String query, String dbName) throws SQLException {
		con = loadDriver(dbName);
		PreparedStatement st = con.prepareStatement(query);
		st.executeUpdate();
		con.close();

	}

	public static void closeDBConnection(String dbName) throws SQLException {

		con.close();
	}


	public static int getResultFromFunctionNsUsageAnalyticsDB(String instanceID,String batchMachineID, String dbName) throws SQLException {
		Connection con = loadDriver(dbName);
		CallableStatement  st = con.prepareCall("{? = call dbo.GetMachineDisplayElementCount(?,?)}");
		st.registerOutParameter(1, java.sql.Types.INTEGER);
		st.setString(2,instanceID);
		st.setString(3,batchMachineID);
		st.execute();
		int output = st.getInt(1);
		st.close();
		return output;


	}

	public boolean verifyColumnPresence(String dbName,String table, String column) throws SQLException {
		
		boolean status = false;
		con = loadDriver(dbName);
		DatabaseMetaData metadata = con.getMetaData();		
		ResultSet resultSet;
				resultSet = metadata.getColumns(null, null, table, column);
		if(resultSet.next())
		    status = true;
		
		return status;
		
	}




}
