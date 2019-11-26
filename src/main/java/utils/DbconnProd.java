package utils;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.HashMap;

import managers.PageManager;
import managers.PropertyFileReader;
import pageclasses.BasePage;

public class DbconnProd {

	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Dbconn.class.getName());
	private static Connection con;
	PageManager pm = new PageManager();
	BasePage bp = pm.getBasePage();

	public static Connection getConnectionProd() {
		String DBServer = "mp-prf-d01.corp.net.bcbsaz.com;databaseName=ProviderMasterETL;integratedSecurity=true;authenticationScheme=NativeAuthentication;";
		String userId = PropertyFileReader.getDbUserId().replace("\"", "");
		userId = "CORP" + "\\" + userId;
		String password = "";
		String db = "jdbc:sqlserver://" + DBServer;
		try {
			con = DriverManager.getConnection(db, userId, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
	}

	public HashMap<String, String> getResultSetProd(String selectQuery) {
		HashMap<String, String> row = new HashMap<String, String>();
		try {
			con = getConnectionProd();
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
