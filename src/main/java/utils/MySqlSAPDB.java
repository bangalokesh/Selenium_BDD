package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MySqlSAPDB {

	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(MySqlSAPDB.class.getName());
	private static Connection con;

	public static Connection getConnection() {
		String host = "sapdb.cmdlqet8zbdj.us-east-1.rds.amazonaws.com";
		String port = "3306";
		String databasename = "saptest";
		String userid = "admin";
		String password = "SapTest123";

		try {
			String db = "jdbc:mysql://" + host + ":" + port + "/" + databasename+ "?autoReconnect=true&useSSL=false"; 
			con = DriverManager.getConnection(db, userid, password);
		} catch (SQLException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return con;
	}


	public List<HashMap<String, String>> getListOfHashMapsFromResultSet(String query) {
		ResultSet resultSet;
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			con = MySqlSAPDB.getConnection();
			resultSet = con.createStatement().executeQuery(query);
			ResultSetMetaData rsmd = resultSet.getMetaData();
			int columnCount = rsmd.getColumnCount();
			while (resultSet.next()) {
				HashMap<String, String> temptestData = new HashMap<String, String>();
				for (int i = 1; i <= columnCount; i++) {
					if (resultSet.getObject(i) != null && !resultSet.getObject(i).toString().contains("None")
							&& !resultSet.getObject(i).toString().contains("none")) {
						if (!rsmd.getColumnLabel(i).isEmpty() && rsmd.getColumnLabel(i) != null)
							temptestData.put(rsmd.getColumnLabel(i), resultSet.getObject(i).toString().trim());
						else
							temptestData.put(rsmd.getColumnName(i), resultSet.getObject(i).toString().trim());
					} else {
						if (!rsmd.getColumnLabel(i).isEmpty() && rsmd.getColumnLabel(i) != null)
							temptestData.put(rsmd.getColumnLabel(i), "");
						else
							temptestData.put(rsmd.getColumnName(i), "");
					}
				}
				list.add(temptestData);
			}
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return list;
	}

	
	public HashMap<String, String> convertResultSettoHashMap(ResultSet rs) {
		HashMap<String, String> row = new HashMap<String, String>();
		try {
			while (rs.next()) {
				ResultSetMetaData md = rs.getMetaData();
				int columns = md.getColumnCount();
				for (int i = 1; i <= columns; ++i) {
					row.put(md.getColumnName(i), rs.getObject(i).toString().trim());
				}
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return row;
	}

	public HashMap<String, String> getResultSet(String selectQuery) {
		HashMap<String, String> row = new HashMap<String, String>();
		try {
			con = getConnection();
			ResultSet rs = con.createStatement().executeQuery(selectQuery);
			while (rs.next()) {
				ResultSetMetaData md = rs.getMetaData();
				int columns = md.getColumnCount();
				for (int i = 1; i <= columns; ++i) {
					if (rs.getObject(i) != null && !rs.getObject(i).toString().contains("None")
							&& !rs.getObject(i).toString().contains("none")) {
						if (!md.getColumnLabel(i).isEmpty() && md.getColumnLabel(i) != null)
							row.put(md.getColumnLabel(i), rs.getObject(i).toString().trim());
						else
							row.put(md.getColumnName(i), rs.getObject(i).toString().trim());
					} else {
						if (!md.getColumnLabel(i).isEmpty() && md.getColumnLabel(i) != null)
							row.put(md.getColumnLabel(i), "");
						else
							row.put(md.getColumnName(i), "");
					}
				}
				break;
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return row;
	}

}
