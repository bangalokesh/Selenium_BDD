package pageclasses.callidus;

import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.sql.Statement;

import managers.PageManager;
import pageclasses.BasePage;
import utils.Dbconn;

public class CallidusHomePage extends BasePage {

	PageManager pm = new PageManager();

	Dbconn dbconn;

	public CallidusHomePage() {

	}

	public boolean callidusValidateCustomerAgent_AccessDB() {

		dbconn = new Dbconn();
		boolean flag1 = false, flag2 = false;
		String status = null;
		try {
			if (!pm.getCallidusCustomerPage().searchCustomer()) {
				dbconn.updateReadyToEnroll("CalidusValStatus", "CustNotFound");
				return false;
			}
			flag1 = pm.getCallidusCustomerPage().validateCustomerDemographics();
			flag2 = pm.getCallidusAgentPage().validateAgentAgencyDetails();

			if (flag1 == true && flag2 == true) {
				status = "PASSED";
			} else
				status = "FAILED";
			if (!dbconn.checkExistingRecordReadyToSell("MEDICAREID", testData.get("MEDICAREID")))
				pm.getCallidusHomePage().insertReadyToSell(status);
			else
				pm.getCallidusHomePage().updateReadyToSell(status);
			dbconn.updateReadyToEnroll("CalidusValStatus", status);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;

	}

	public boolean callidusValidateCustomerAgent() {

		dbconn = new Dbconn();
		boolean flag1 = false, flag2 = false;
		String status = null;
		try {
			if (!pm.getCallidusCustomerPage().searchCustomer()) {
				dbconn.updateReadyToEnroll("CalidusValStatus", "CustNotFound");
				return false;
			}
			flag1 = pm.getCallidusCustomerPage().validateCustomerDemographics();
			flag2 = pm.getCallidusAgentPage().validateAgentAgencyDetails();

			if (flag1 == true && flag2 == true) {
				status = "PASSED";
			} else
				status = "FAILED";
			if (!dbconn.checkExistingRecordReadyToSell("MEDICAREID", testData.get("MEDICAREID")))
				pm.getCallidusHomePage().insertReadyToSell(status);
			else
				pm.getCallidusHomePage().updateReadyToSell(status);
			dbconn.updateReadyToEnroll("CalidusValStatus", status);

		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public void insertReadyToSell(String Status) {

		int flag;
		Connection con = Dbconn.getConnection();
		Statement statement = null;

		try {
			String query = "insert into VelocityTestAutomation.[dbo].[test_data_readytosell]"
					+ "  (TCID,MEDICAREID,RUNMODE,AGENTID,AGENCYID,READYTOENROLL_ID,AGENTCOMMISSION_CAL,AGENCYCOMMISSION_CAL,DRIVERTYPE,STATUS,APPNAME) "
					+ "  VALUES (" + getTcid() + ",'" + testData.get("MEDICAREID") + "','Y','"
					+ testData.get("AgentTIN") + "','" + testData.get("AgencyTIN") + "'," + testData.get("ID") + ","
					+ testData.get("AgentCommission") + "," + testData.get("AgencyCommission") + ",'"
					+ testData.get("DRIVERTYPE") + "','" + Status + "','CALLIDUS')";
			statement = con.createStatement();
			flag = statement.executeUpdate(query);

			if (flag == 1) {
				reportPass("Test Results are inserted into ReadyToSell Table");
			} else {
				reportFail("Insert Test Results into ReadyToSell Table is Failed");
			}

		} catch (SQLException | IOException e) {
			e.printStackTrace();
			try {
				reportFail("Insert Test Results into ReadyToSell Table is Failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} finally {
			try {
				if (null != con) {
					statement.close();
					con.close();
				}
			} catch (SQLException s) {
				s.printStackTrace();
			}

		}
	}

	public void updateReadyToSell(String Status) {
		int flag;
		Connection con = Dbconn.getConnection();
		Statement statement = null;

		try {
			String query = "update VelocityTestAutomation.[dbo].[test_data_readytosell]" + " set TCID =" + getTcid()
					+ " , RUNMODE = 'Y'" + " , AGENTID = '" + testData.get("AgentTIN") + "' , AGENCYID = '"
					+ testData.get("AgencyTIN") + "' , READYTOENROLL_ID =" + testData.get("ID")
					+ " , AGENTCOMMISSION_CAL=" + testData.get("AgentCommission") + " , AGENCYCOMMISSION_CAL="
					+ testData.get("AgencyCommission") + " , DRIVERTYPE='" + testData.get("DRIVERTYPE") + "' , STATUS='"
					+ Status + "'" + " ,APPNAME='CALLIDUS' where MEDICAREID ='" + testData.get("MEDICAREID") + "'";

			statement = con.createStatement();
			flag = statement.executeUpdate(query);

			if (flag == 1) {
				reportPass("Test Results are updated to ReadyToSell Table");
			} else {
				reportFail("Update Test Results to ReadyToSell Table is Failed");
			}

		} catch (SQLException | IOException e) {
			e.printStackTrace();
			try {
				reportFail("Update Test Results to ReadyToSell Table is Failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} finally {
			try {
				if (null != con) {
					statement.close();
					con.close();
				}
			} catch (SQLException s) {
				s.printStackTrace();
			}

		}
	}

}
