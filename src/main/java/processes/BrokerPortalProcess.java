package processes;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import managers.PageManager;
import pageclasses.BasePage;

import utils.Dbconn;

public class BrokerPortalProcess extends BasePage{
	
	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(BrokerPortalProcess.class.getName());
	Dbconn db = new Dbconn();
	PageManager pm = new PageManager();
	
	public BrokerPortalProcess() {
		driver = getWebDriver();
	}

	public void executeBrokerPortalAppTrackerValidation() {
		try {
			String query = "select AgentUserID, AgentID from test_data_readytoenroll join agent on test_data_readytoenroll.AgentID = agent.AgentTIN where test_data_readytoenroll.AgentID = '830316';";
			List<HashMap<String,String>> list = db.getListOfHashMapsFromResultSet(query);
			HashMap<String, String> temptestData = new HashMap<String, String>();
			   for(HashMap<String,String> row:list) {
				   for (String key : row.keySet()) {
							temptestData.put(key, row.get(key));
						}
//				setrowTestData(temptestData);
	
				pm.getBrokerLoginPage().login(temptestData.get("AgentUserID"));
				pm.getApplicationTrackerPage().applicationTrackerPage();
				pm.getApplicationTrackerPage().validateAppStatus(temptestData.get("AgentID"));
				
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Test Failed in Brokerportalprocess executeBorkerPortalAppTrackerValidation method");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} 
	}
	public void executeBrokerPortalCommissionReportValidation() {
		try {
			String query = "select AgentUserID, AgentID from test_data_readytoenroll join agent on test_data_readytoenroll.AgentID = agent.AgentTIN where test_data_readytoenroll.AgentID = '830316';";
			List<HashMap<String,String>> list = db.getListOfHashMapsFromResultSet(query);
			HashMap<String, String> temptestData = new HashMap<String, String>();
			   for(HashMap<String,String> row:list) {
				   for (String key : row.keySet()) {
							temptestData.put(key, row.get(key));
						}
//				setrowTestData(temptestData);
				pm.getBrokerLoginPage().login(temptestData.get("AgentUserID"));
				pm.getCommissionReportPage().navigateCommissionReportPage();
				pm.getCommissionReportPage().validateMedicareCommissionReport();
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Test Failed in BrokerPortalProcess executeBorkerPortalCommissionReportValidation method");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} 
	}
}
