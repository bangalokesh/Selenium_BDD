package utils;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Types;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;

import managers.PageManager;
import managers.PropertyFileReader;
import pageclasses.BasePage;

public class Dbconn {

	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Dbconn.class.getName());
	private static Connection con;
	PageManager pm = new PageManager();
	BasePage bp = pm.getBasePage();

	public static Connection getConnection() {
		String DBServer = "md-vlc-d01.corp.net.bcbsaz.com;databaseName=VelocityTestAutomation;integratedSecurity=true;authenticationScheme=NativeAuthentication;";
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

	public static Connection getConnectionTwo() {
		String DBServer = "mu-prf-d01.corp.net.bcbsaz.com;databaseName=ProviderMasterETL;integratedSecurity=true;authenticationScheme=NativeAuthentication;";
		String userId = PropertyFileReader.getDbUserId().replace("\"", "");
		userId = "CORP" + "\\" + userId;
		String password = BasePage.decodeString(PropertyFileReader.getDbPassword().replace("\"", ""));
		String db = "jdbc:sqlserver://" + DBServer;
		try {
			con = DriverManager.getConnection(db, userId, password);
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return con;
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
					if (res.getObject(i) != null && !res.getObject(i).toString().contains("None")
							&& !res.getObject(i).toString().contains("none")) {
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
				val = res.getString("TestDataTableName").trim();
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

	public void updateReadyToClaims(String colName, String value) {
		Connection con = getConnection();
		try {
			String query = "UPDATE VelocityTestAutomation.dbo.test_data_readytoclaims SET " + colName + " = '" + value
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
				val = r.getString(colName).trim();
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

	public String selectPCPDetails(String query) {
		Connection con = getConnectionTwo();
		String npm = null;
		try {
			PreparedStatement preparedStmt = con.prepareStatement(query);
			ResultSet r = preparedStmt.executeQuery();
			while (r.next()) {
				for (int i = 1; i <= 5; i++) {
					if (r.getObject(i) != null && !r.getObject(i).toString().contains("None")
							&& !r.getObject(i).toString().contains("none")) {
						npm = r.getObject(i).toString().trim();
					}
				}

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
		return npm;
	}

	public List<HashMap<String, String>> getListOfHashMapsFromResultSet(String query) {
		ResultSet resultSet;
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			con = Dbconn.getConnection();
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

	public boolean checkExistingRecordReadyToSell(String colName, String colValue) {
		ResultSet resultSet;
		boolean flag = true;

		con = Dbconn.getConnection();
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

	public List<String> getListFromQuery(String query) {
		List<String> list = new ArrayList<String>();
		ResultSet res = null;

		try {
			con = getConnection();
			res = con.createStatement().executeQuery(query);

			while (res.next()) {
				list.add(res.getString(1));
			}

		} catch (Exception e) {
			e.printStackTrace();
			try {
				BasePage.reportFail("getTableName: " + e.getMessage());
			} catch (IOException e1) {
				e1.printStackTrace();
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

	public HashMap<String, String> getResultSetTwo(String selectQuery) {
		HashMap<String, String> row = new HashMap<String, String>();
		try {
			con = getConnectionTwo();
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
				return row;
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

	public String selectPlanDetails(String colName, String value1, String value2) {
		Connection con = getConnection();
		String val = null;
		try {
			String query = "select " + colName + " from VelocityTestAutomation.dbo.product_details WHERE PlanID"
					+ " = '" + value1 + "' and PBPID = '" + value2 + "';";
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

	public List<HashMap<String, String>> getListOfHashMapsFromProviderDB(String query) {
		ResultSet resultSet;
		List<HashMap<String, String>> list = new ArrayList<HashMap<String, String>>();
		try {
			con = Dbconn.getConnectionTwo();
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

	public HashMap<String, String> selectPCPDetails(String zipCode, String plan) {
		HashMap<String, String> list = new HashMap<String, String>();
		try {
			String query = "SELECT DISTINCT " + "       prov.ProvNPIID AS NPI,"
					+ "       hs.MasterCd AS PRIMARY_SPECIALTY," + "       bn.Program as BN_PROGRAM,"
					+ "       bn.BenefitNetwork as BN_Benefit_Network," + "      l.LocAddrZipCd AS ZIP "
					+ "FROM [Profisee].[data].[tProvider] prov "
					+ "INNER JOIN [Profisee].[SPF].[PracticeParticipant] pp " + "       ON pp.ProvFK = prov.Code "
					+ "INNER JOIN [Profisee].[data].[tPractice] pract " + "       ON pract.Code = pp.PractcFK "
					+ "INNER JOIN [profisee].[SPF].[PracticeParticipantProgramContract] pppc "
					+ "       ON pppc.PractcPrtcptFK = PP.PractcPrtcptPK "
					+ "INNER JOIN ProviderMasterHub.HUB.vProgram pg " + "       ON pg.ProgPK = pppc.ProgFK "
					+ "INNER JOIN [Profisee].[data].[tDG_ProviderType] pt " + "       ON pt.Code = prov.ProvTypeFK "
					+ "INNER JOIN ProviderMasterHub.HUB.vDegreeType dt " + "       ON dt.DegTypePK = prov.DegTypeFK "
					+ "INNER JOIN [Profisee].[SPF].[ProviderLocation] pl "
					+ "       ON pl.PractcPrtcptFK = PP.PractcPrtcptPK "
					+ "INNER JOIN Profisee.data.tDG_LocationType lt " + "       ON lt.ID = pl.LocTypeFK "
					+ "INNER JOIN [Profisee].[data].[tLocation] l " + "       ON l.Code = pl.LocFK "
					+ "INNER JOIN [Profisee].[SPF].[ProviderSpecialty] ps " + "      ON ps.ProvFK = PROV.CODE "
					+ "INNER JOIN [Profisee].[data].[vDG_HealthcareSpecialty] hs  "
					+ "       ON hs.Code = ps.HlthcrSpcltyFK "
					+ "INNER JOIN [Profisee].[SPF].[ProviderTelephoneNumber] ptn "
					+ "       ON ptn.PractcPrtcptFK = PP.PractcPrtcptPK AND ptn.PhnNumTypeFK = pl.LocTypeFK "
					+ "INNER JOIN [Profisee].[data].[tBenefitNetwork] bn " + "       ON bn.Program = pg.ProgCd "
					+ "WHERE hs.MasterCd in ('Internal Medicine','Family Medicine') " + "and" + " l.LocAddrZipCd like '"
					+ zipCode + "" + "%'" + " and" + " bn.Program = '" + plan + "' ORDER BY prov.ProvNPIID" + "";

			list = getResultSetTwo(query);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return list;
	}

	public HashMap<String, String> returnSecretKeySalt(HashMap<String, String> temp) {
		ResultSet resultSet;
		try {
			con = Dbconn.getConnection();
			String query = "SELECT [SecretKey],[Salt] FROM [VelocityTestAutomation].[dbo].[automation_users] "
					+ "  WHERE [AppName] = '" + temp.get("AppName") + "' " + "	AND [ENVIRONMENT] = '"
					+ temp.get("Environment") + "'" + "	AND [USERID] = '" + temp.get("UserID") + "'";
			resultSet = con.createStatement().executeQuery(query);
			while (resultSet.next()) {
				temp.put("secretKey", resultSet.getString(1).trim());
				temp.put("salt", resultSet.getString(2).trim());
			}
			if (con != null) {
				resultSet.close();
				con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return temp;
	}

	public void insertEncryptDataToDB(Map<String, String> encryptData) {
		try {
			con = Dbconn.getConnection();
			String insertQuery = "INSERT INTO [VelocityTestAutomation].[dbo].[automation_users](Environment,AppName,UserID,SecretKey,Salt) VALUES ('"
					+ encryptData.get("Environment") + "','" + encryptData.get("AppName") + "','"
					+ encryptData.get("UserId") + "','" + encryptData.get("secretKey") + "','" + encryptData.get("salt")
					+ "');";

			String updateQuery = "UPDATE [VelocityTestAutomation].[dbo].[automation_users] SET [SecretKey] ='"
					+ encryptData.get("secretKey") + "',[Salt] ='" + encryptData.get("salt") + "'"
					+ " WHERE [AppName] = '" + encryptData.get("AppName") + "' " + " AND [ENVIRONMENT] = '"
					+ encryptData.get("Environment") + "'" + " AND [USERID] = '" + encryptData.get("UserId") + "'";

			if (checkExistingRecordInAutomationUsers(encryptData) == true) {
				con.createStatement().executeUpdate(updateQuery);
			} else {
				con.createStatement().execute(insertQuery);
			}

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public boolean checkExistingRecordInAutomationUsers(Map<String, String> temp) {
		ResultSet resultSet;
		boolean flag = true;
		con = Dbconn.getConnection();
		try {

			String query = " SELECT count(USERID) as count FROM [VelocityTestAutomation].[dbo].[automation_users] "
					+ "  WHERE [AppName] = '" + temp.get("AppName") + "' " + "	AND [ENVIRONMENT] = '"
					+ temp.get("Environment") + "'" + "	AND [USERID] = '" + temp.get("UserId") + "'";
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

	public double[] returnAgentAgencyCommissionDB(HashMap<String, String> temp) {
		con = getConnection();
		double[] commission = new double[2];
		ResultSet resultSet;

		String query = "  select Agent_CommissionAmt,Agency_CommissionAmt from [dbo].[commission_details] "
				+ "where Year = " + temp.get("Year") + " and Cycle =" + temp.get("CmsCycle") + " and PlanID = '"
				+ temp.get("PlanID") + "' and contractType = '" + temp.get("contractType") + "';";

		try {
			resultSet = con.createStatement().executeQuery(query);

			while (resultSet.next()) {
				commission[0] = resultSet.getDouble(1);
				commission[1] = resultSet.getDouble(2);
			}
			if (con != null) {
				resultSet.close();
				con.close();
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return commission;
	}

	public boolean checkExistingRecordInTable(String tableName, String colName, String colValue) {
		ResultSet resultSet;
		boolean flag = true;

		con = Dbconn.getConnection();
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

	public boolean checkForExistingRecord(String query) {
		ResultSet resultSet;
		boolean flag = true;

		con = Dbconn.getConnection();
		try {
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

	public void insert_HRP_ProviderSupplierDetailsToDB(HashMap<String, String> tempDetails) {

		try {
			con = getConnection();
			String insertQuery = "INSERT INTO  [VelocityTestAutomation].[dbo].[hrp_pgr_provider_supplier_details]\r\n"
					+ "           ([CATEGORY]\r\n" + "           ,[NAME]\r\n" + "           ,[TAX_ID]\r\n"
					+ "           ,[NPI]\r\n" + "           ,[ADDRESS]\r\n" + "           ,[SUITE]\r\n"
					+ "           ,[CITY]\r\n" + "           ,[ZIPCODE]\r\n" + "           ,[STATE]\r\n"
					+ "           ,[COUNTY]\r\n" + "           ,[PRIMARYSPECIALITY]\r\n"
					+ "           ,[Network],[PRIMARYINDICATOR],[PHONENUMBER])" + "     VALUES\r\n" + " ('"
					+ tempDetails.get("CATEGORY") + "'\r\n" + "           ,'" + tempDetails.get("NAME") + "'\r\n"
					+ "           ,'" + tempDetails.get("TaxID") + "'\r\n" + "           ,'" + tempDetails.get("NPI")
					+ "'\r\n" + "           ,'" + tempDetails.get("Address") + "'\r\n" + "           ,?"
					+ "           ,'" + tempDetails.get("City") + "'\r\n" + "           ,'" + tempDetails.get("ZipCode")
					+ "'\r\n" + "           ,'" + tempDetails.get("State") + "'\r\n" + "           ,'"
					+ tempDetails.get("County") + "'\r\n" + "           ,'" + tempDetails.get("PrimarySpeciality")
					+ "'\r\n" + "           ,'" + tempDetails.get("BenefitNetwork") + "',"
					+ tempDetails.get("PrimaryIndicator") + ",?)";
			PreparedStatement ps = con.prepareStatement(insertQuery);

			if (tempDetails.get("Suite").isEmpty()) {
				ps.setNull(1, Types.NULL);
			} else
				ps.setString(1, tempDetails.get("Suite"));

			if (tempDetails.get("PrimaryIndicator").equals("1") || tempDetails.get("PhoneNumber").isEmpty()) {
				ps.setNull(2, Types.NULL);
			} else {
				ps.setString(2, tempDetails.get("PhoneNumber"));
			}
			ps.executeUpdate();

		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

	public String returnRecordFromTablePRFDB(String tableName, String returnColName, String colName, String colValue) {
		ResultSet resultSet;
		String value = null;
		con = Dbconn.getConnectionTwo();
		try {
			String query = " SELECT " + returnColName + "  FROM " + tableName + "  WHERE " + colName + "='" + colValue
					+ "'";
			resultSet = con.createStatement().executeQuery(query);
			while (resultSet.next()) {
				value = resultSet.getString(1);
				break;
			}
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return value;
	}

	public boolean rowMatchedHRPPGR(HashMap<String, String> tempHashMap) {
		boolean flag = false;
		con = getConnection();
		String benefitNetworkValidation = null;
		String phoneNumberValidation = null; // We are validating phone numbers only for Non PCP
		String[] address = tempHashMap.get("ADDRESS").toUpperCase().split(" ");
		// MedicareAdvantage HMO

		if (tempHashMap.get("Network").equals("MedicareAdvantage HMO")) {
			String[] temp = tempHashMap.get("BENEFITNETWORK_HRP").split(",");
			benefitNetworkValidation = "upper(Network) like '%" + temp[0] + "%' and upper(Network) like '%" + temp[1]
					+ "%'	";
		} else {
			benefitNetworkValidation = "upper(Network) like '%" + tempHashMap.get("BENEFITNETWORK_HRP") + "%';";
		}

		if (tempHashMap.get("PrimaryIndicator").equals("1") || tempHashMap.get("PhoneNumber").isEmpty()) // include
																											// condition
																											// for
																											// phoneNumber
																											// is null
		{
			phoneNumberValidation = "";
		} else {
			phoneNumberValidation = "PHONENUMBER like '%" + tempHashMap.get("PhoneNumber") + "%';";
		}

		String selectQueryHRPVDB = null;

		if (tempHashMap.get("SUITE").isEmpty()) {

			selectQueryHRPVDB = "select * from [dbo].[hrp_pgr_provider_supplier_details] h1\r\n"
					+ "join [dbo].[stateMapping] s1 on upper(h1.STATE) = upper(s1.[STATE NAME]) \r\n"
					+ "where CATEGORY = '" + tempHashMap.get("CATEGORY_HRP") + "' and\r\n" + "upper(NAME) = '"
					+ tempHashMap.get("NAME") + "' and \r\n" + "upper(TAX_ID) = '" + tempHashMap.get("TAX_ID")
					+ "' and \r\n" + "upper(NPI) = '" + tempHashMap.get("NPI") + "' and \r\n" + "upper(ADDRESS) like '"
					+ address[0] + "%' and "
					// + tempHashMap.get("ADDRESS").toUpperCase() + "' and \r\n"
					// + "upper(SUITE) is null and \r\n"
					+ "upper(CITY) = '" + tempHashMap.get("CITY").toUpperCase() + "' and \r\n" + "upper(ZIPCODE) = '"
					+ tempHashMap.get("ZIP") + "' and \r\n" + "upper(s1.[STATE CODE]) = '"
					+ tempHashMap.get("ST").toUpperCase() + "' and \r\n" +
					// "upper(COUNTY) = '"+tempHashMap.get("CNTY").toUpperCase()+"' and \r\n" +
					"upper(PRIMARYSPECIALITY)  = '" + tempHashMap.get("PRIMARY_SPECIALTY").toUpperCase() + "' and\r\n"
					+ benefitNetworkValidation + phoneNumberValidation;
		} else {
			selectQueryHRPVDB = "select * from [dbo].[hrp_pgr_provider_supplier_details] h1\r\n"
					+ "join [dbo].[stateMapping] s1 on upper(h1.STATE) = upper(s1.[STATE NAME]) \r\n"
					+ "where CATEGORY = '" + tempHashMap.get("CATEGORY_HRP") + "' and\r\n" + "upper(NAME) = '"
					+ tempHashMap.get("NAME") + "' and \r\n" + "upper(TAX_ID) = '" + tempHashMap.get("TAX_ID")
					+ "' and \r\n" + "upper(NPI) = '" + tempHashMap.get("NPI") + "' and \r\n" + "upper(ADDRESS) like '"
					+ address[0] + "%' and "
					// + "upper(SUITE) = "+ tempHashMap.get("SUITE").toUpperCase() +" and \r\n"
					+ "upper(CITY) = '" + tempHashMap.get("CITY").toUpperCase() + "' and \r\n" + "upper(ZIPCODE) = '"
					+ tempHashMap.get("ZIP") + "' and \r\n" + "upper(s1.[STATE CODE]) = '"
					+ tempHashMap.get("ST").toUpperCase() + "' and \r\n" +
					// "upper(COUNTY) = '"+tempHashMap.get("CNTY").toUpperCase()+"' and \r\n" +
					"upper(PRIMARYSPECIALITY)  = '" + tempHashMap.get("PRIMARY_SPECIALTY").toUpperCase() + "' and\r\n"
					+ benefitNetworkValidation + phoneNumberValidation;
		}

		PreparedStatement ps;
		try {
			ps = con.prepareStatement(selectQueryHRPVDB);

			ResultSet res = ps.executeQuery();
			if (res.next()) {
				flag = true;
			} else
				flag = false;
		} catch (SQLException e) {
			e.printStackTrace();
		}
		return flag;
	}

	public void insert_HRP_Non_PCP_Supplier_Screen_Data_To_DB(HashMap<String, String> tempDetails) {

		try {
			con = getConnection();
			String insertQuery = "INSERT INTO  [VelocityTestAutomation].[dbo].[hrp_non_pcp_supplier_screen_data]\r\n"
					+ "           ([TAX_ID]\r\n" + "           ,[NPI]\r\n" + "           ,[ADDRESS]\r\n"
					+ "           ,[SUITE]\r\n" + "           ,[CITY]\r\n" + "           ,[ZIPCODE]\r\n"
					+ "           ,[STATE]\r\n"
					+ "           ,[COUNTY], SUPPLIER_ID,SAME_ADDRESS_COUNTER,ADDRESS_LINK,PHONENUMBER) "
					+ "     VALUES\r\n" + " ('" + tempDetails.get("TaxID") + "'\r\n" + "           ,'"
					+ tempDetails.get("SUPPL_NPI") + "'\r\n" + "           ,'" + tempDetails.get("Address") + "'\r\n"
					+ "           ,?" + "           ,'" + tempDetails.get("City") + "'\r\n" + "           ,'"
					+ tempDetails.get("ZipCode") + "'\r\n" + "           ,'" + tempDetails.get("State") + "'\r\n"
					+ "           ,'" + tempDetails.get("County") + "'\r\n,'" + tempDetails.get("SUPPL_ID") + "',"
					+ tempDetails.get("SameAddressCounter") + ",'" + tempDetails.get("AddressLinkName") + "',?)";
			PreparedStatement ps = con.prepareStatement(insertQuery);

			if (tempDetails.get("Suite").isEmpty() || tempDetails.get("Suite").equalsIgnoreCase("null")) {
				ps.setNull(1, Types.NULL);
			} else
				ps.setString(1, tempDetails.get("Suite"));

			if (tempDetails.get("PhoneNumber").isEmpty()) {
				ps.setNull(2, Types.NULL);
			} else {
				ps.setString(2, tempDetails.get("PhoneNumber"));
			}
			ps.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
		}
	}

}
