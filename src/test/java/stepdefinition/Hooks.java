package stepdefinition;

import org.apache.log4j.PropertyConfigurator;

import cucumber.api.Scenario;
import cucumber.api.java.After;
import cucumber.api.java.Before;
import managers.PageManager;
import pageclasses.BasePage;
import utils.Const;
import utils.Dbconn;

public class Hooks {
	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(Hooks.class.getName());
	
	
	Dbconn db = new Dbconn();
	PageManager pm = new PageManager();
	
	@SuppressWarnings("static-access")
	@Before
	public void initialization(Scenario sc) {
		try {
			BasePage bp = pm.getBasePage();
			loadLog4j();
			String [] scName = sc.getName().split("~");
			String scenarioName = scName[0].trim();
			String testName = scName[1].trim();
			logger.info(scenarioName + "~" + testName);
			int tcid = 1;//db.sqlSelectTestID("select ID from VelocityTestAutomation.dbo.test_case where AzureStoryID = '" + scenarioName + "' AND AzureTestID = '" + testName + "' AND RunMode = 'Y';");
			//logger.info("TCID = " + tcid);
			if(tcid==0) {
				bp.setContinueExecution(false); 
			} else {
				bp.setContinueExecution(true);
				bp.setTest(scenarioName, testName);
				//bp.setTestData(db.selectTestDataFromTable(tcid));
				//bp.invokeDriver();
			}
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	@SuppressWarnings("static-access")
	@After
	public void closure(Scenario sc) {
		String status = null;
		BasePage bp = pm.getBasePage();
		try {
			int id = bp.getTcid();
			if(bp.test!= null) {
				status = bp.test.getStatus()+"";
				if(Const.TESTPLANID!=null && Const.TESTSUITEID!=null) {
					String [] scName = sc.getName().split("~");
					int testName = Integer.parseInt(scName[1].trim());
					//bp.updateTestResult("Completed", "Test Failed", "Failed", testName);
				}
			}
			if(Const.TESTPLANID!=null && Const.TESTSUITEID!=null) {
				String [] scName = sc.getName().split("~");
				int testName = Integer.parseInt(scName[1].trim());
				//bp.updateTestResult("Completed", "Test Failed", "Passed", testName);
			}
			String updateQuery = "update VelocityTestAutomation.dbo.test_case set Status = '"+ status + "', RunName = '" + bp.getRunName() + "', AzureRunNum = '" + bp.getRunId() + "' where id = '" + id + "'";
			//db.sqlUpdate(updateQuery);
			//db.updateTestResult(id, bp.getTestData());
			
		} catch(Exception e) {
			e.printStackTrace();
		} finally {
			bp.flushTest();
			bp.closeDrivers();
		}
	}
	
	public void loadLog4j() {
		String log4jConfigPath = Const.LOG4JPROPERTIES;
		PropertyConfigurator.configure(log4jConfigPath);
		logger.info("loadedlog4j");
	}
}
