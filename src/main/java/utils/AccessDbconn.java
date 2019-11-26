package utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import managers.PageManager;
import pageclasses.BasePage;

public class AccessDbconn {

	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(AccessDbconn.class.getName());
	private static Connection con;
	PageManager pm = new PageManager();
	BasePage bp = pm.getBasePage();

	public Connection getConnection() {
		Connection connection = null;
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			String msAccDB = Const.AccessDBPath;
			String dbURL = "jdbc:ucanaccess://" + msAccDB;
			connection = DriverManager.getConnection(dbURL);
		} catch (Exception cnfex) {
			System.out.println("Problem in loading or " + "registering MS Access JDBC driver");
			cnfex.printStackTrace();
		}
		return connection;
	}

	public int sqlSelectTestID(String selQuery) throws SQLException {
		int val = 0;
		try {
			con = getConnection();
			ResultSet res = con.createStatement().executeQuery(selQuery);
			while (res.next()) {
				val = res.getInt("ID");
				if (val != 0)
					bp.setTcid(res.getInt("ID"));
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return val;
	}

	public void sqlUpdate(String updateQuery) {
		try {
			con = getConnection();
			PreparedStatement preparedStmt = con.prepareStatement(updateQuery);
			preparedStmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}

	public void updateTestResult(int tcid, HashMap<String, String> testData) {
		ObjectMapper mapper = new ObjectMapper();
		con = getConnection();
		String json = null;
		try {
			json = mapper.writeValueAsString(testData);
		} catch (JsonProcessingException e1) {
			e1.printStackTrace();
		}
		try {
			String updateQuery = "UPDATE VelocityTestAutomation.dbo.test_case SET TestData = '" + json + "' WHERE ID = "
					+ tcid + ";";
			PreparedStatement preparedStmt = con.prepareStatement(updateQuery);
			preparedStmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}

	public String sqlgetField(String selQuery) {
		String val = null;
		try {
			con = getConnection();
			ResultSet res = con.createStatement().executeQuery(selQuery);
			while (res.next()) {
				val = res.getString("WorkStreamID").trim();
				break;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return val;
	}

	public HashMap<String, String> selectTestDataFromTable(int tcid) {
		HashMap<String, String> testData = new HashMap<String, String>();
		String query = "SELECT VelocityTestAutomation.dbo.test_case.WorkStreamID FROM VelocityTestAutomation.dbo.test_case WHERE ID = "
				+ tcid + ";";
		String workStreamID = sqlgetField(query);
		String tableName = getTableName(Integer.parseInt(workStreamID));
		con = getConnection();
		String selQuery = "SELECT * FROM " + tableName + " where TCID = " + tcid + ";";
		try {
			ResultSet res = con.createStatement().executeQuery(selQuery);
			ResultSetMetaData rsmd = res.getMetaData();
			int columnCount = rsmd.getColumnCount();
			while (res.next()) {
				for (int i = 1; i <= columnCount; i++) {
					if (res.getObject(i) != null) {
						if (!res.getObject(i).toString().contains("None"))
							testData.put(rsmd.getColumnName(i), res.getObject(i).toString().trim());
					}
				}
				break;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return testData;
	}

	public String getTableName(int workStreamID) {
		String tableName = null;
		try {
			con = getConnection();
			String selQuery = "SELECT * FROM VelocityTestAutomation.dbo.workstream_detail WHERE ID = " + workStreamID
					+ ";";
			ResultSet res = con.createStatement().executeQuery(selQuery);
			String val = null;
			while (res.next()) {
				val = res.getString("TestDataTableName");
				break;
			}
			tableName = ("VelocityTestAutomation.dbo." + val).trim();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				BasePage.reportFail("getTableName: " + e.getMessage());
			} catch (IOException e1) {
				e1.printStackTrace();
			} finally {
				if (con != null)
					try {
						con.close();
					} catch (SQLException e1) {
						e1.printStackTrace();
					}
			}
		}
		return tableName;
	}

	public void updateDBTestData(String query) {
		Connection con = getConnection();
		try {
			PreparedStatement preparedStmt = con.prepareStatement(query);
			preparedStmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}

	public void updateReadyToEnroll(String colName, String value) {
		Connection con = getConnection();
		try {
			String query = "UPDATE VelocityTestAutomation.dbo.test_data_readytoenroll SET " + colName + " = '" + value
					+ "' WHERE ID = " + BasePage.getTestDataid() + ";";
			PreparedStatement preparedStmt = con.prepareStatement(query);
			preparedStmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}

	public void updateReadyToBill(String colName, String value) {
		Connection con = getConnection();
		try {
			String query = "UPDATE VelocityTestAutomation.dbo.test_data_readytobill SET " + colName + " = '" + value
					+ "' WHERE ID = " + BasePage.getTestDataid() + ";";
			PreparedStatement preparedStmt = con.prepareStatement(query);
			preparedStmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}

	public void updateProvider(String colName, String value, int iD) {
		Connection con = getConnection();
		try {
			String query = "UPDATE VelocityTestAutomation.dbo.BCBSAZProv_ActiveOnly SET " + colName + " = '" + value
					+ "' WHERE ID = " + iD + ";";
			logger.info(query);
			PreparedStatement preparedStmt = con.prepareStatement(query);
			preparedStmt.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
	}

	public String selectAddressDetails(String colName, String colWhere, String value) {
		Connection con = getConnection();
		String val = null;
		try {
			String query = "select " + colName + " from VelocityTestAutomation.dbo.address_details WHERE " + colWhere
					+ " = '" + value + "';";
			PreparedStatement preparedStmt = con.prepareStatement(query);
			ResultSet r = preparedStmt.executeQuery();
			while (r.next()) {
				val = r.getString(colName);
				break;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return val;
	}

	public List<HashMap<String, String>> getListOfHashMapsFromResultSet(String query) {
		ResultSet resultSet;
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			con = getConnection();
			resultSet = con.createStatement().executeQuery(query);
			ResultSetMetaData rsmd = resultSet.getMetaData();
			int columnCount = rsmd.getColumnCount();
			while (resultSet.next()) {
				HashMap<String, String> temptestData = new HashMap<String, String>();
				for (int i = 1; i <= columnCount; i++) {
					if (resultSet.getObject(i) != null && !resultSet.getObject(i).toString().contains("None") && !resultSet.getObject(i).toString().contains("none")) {
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

	public boolean checkExistingRecordReadyToSell(String colName, String colValue) {
		ResultSet resultSet;
		boolean flag = true;

		con = getConnection();
		try {

			String query = "select count(ID) as count from VelocityTestAutomation.[dbo].[test_data_readytosell] where "
					+ colName + "= '" + colValue + "'";
			resultSet = con.createStatement().executeQuery(query);

			while (resultSet.next()) {

				if (resultSet.getInt("count") == 0) {
					flag = false;
				} else {
					flag = true;
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return flag;
	}

	public ResultSet executeSelectQuery(String query) {
		ResultSet res = null;
		try {
			con = getConnection();
			res = con.createStatement().executeQuery(query);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				BasePage.reportFail("getTableName: " + e.getMessage());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return res;
	}

	public HashMap<String, String> convertResultSettoHashMap(ResultSet rs) {
		HashMap<String, String> row = new HashMap<String, String>();
		try {
			while (rs.next()) {
				ResultSetMetaData md = rs.getMetaData();
				int columns = md.getColumnCount();
				for (int i = 1; i <= columns; ++i) {
					row.put(md.getColumnName(i), rs.getObject(i).toString());
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
					if (rs.getObject(i) != null && !rs.getObject(i).toString().contains("None") && !rs.getObject(i).toString().contains("none")) {
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

	public String selectPlanDetails(String colName, String colWhere, String value) {
		Connection con = getConnection();
		String val = null;
		try {
			String query = "select " + colName + " from VelocityTestAutomation.dbo.product_details WHERE " + colWhere
					+ " = '" + value + "';";
			PreparedStatement preparedStmt = con.prepareStatement(query);
			ResultSet r = preparedStmt.executeQuery();
			while (r.next()) {
				val = r.getString(colName);
			}
			if (con != null)
				con.close();
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return val;
	}

	public boolean checkExistingRecordInTable(String tableName, String colName, String colValue) {
		ResultSet resultSet;
		boolean flag = true;

		con = getConnection();
		try {

			String query = " SELECT count(0) as count FROM [VelocityTestAutomation].[dbo]." + tableName + "  WHERE "
					+ colName + "='" + colValue + "'";
			resultSet = con.createStatement().executeQuery(query);

			while (resultSet.next()) {

				if (resultSet.getInt("count") == 0) {
					flag = false;
				} else {
					flag = true;
				}
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}

		return flag;
	}

	public void executeQuery(String query) {
		try {
			con = getConnection();
			con.createStatement().execute(query);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				BasePage.reportFail("getTableName: " + e.getMessage());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public boolean isResultSetEmpty(String query) {
		try {
			con = getConnection();
			ResultSet res = con.createStatement().executeQuery(query);
			if (res.next()) {
				return true;
			} else
				return false;
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			if (con != null)
				try {
					con.close();
				} catch (SQLException e) {
					e.printStackTrace();
				}
		}
		return false;
	}

}
