package stepdefinition;

import java.util.HashMap;
import java.util.List;

import EnrollmentFiles.CreateFileFromLayout;
import cucumber.api.java.en.Given;
import managers.PageManager;
import pageclasses.BasePage;
import utils.Const;
import utils.MySqlSAPDB;

public class SAPStepDefinition {
	
	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(M360MembershipStepDefinition.class.getName());

	PageManager pm = new PageManager();
	BasePage bp = pm.getBasePage();
	MySqlSAPDB mySqlDB = new MySqlSAPDB();
	CreateFileFromLayout createFile = new CreateFileFromLayout();
	

	@Given("I Login to SAP")
	public void sapLogin() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Start Logging in to SAP");
				
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Given("I read the file and validate in DB")
	public void I_read_the_file_and_validate_in_DB() {
		if(BasePage.isContinueExecution()) {
			logger.info("Start Logging file test in java");
			try {
			List<HashMap<String, String>> a = createFile.readFileFromLayout(Const.SAPConfigProperties, Const.SAPDataFile, ";");
			logger.info("Read the file");
				for (HashMap<String,String> row: a) {
					String q = Const.SAPDBQuery2 + "'" + row.get("ZSRC_SYS_CD") + "';";
					HashMap<String, String> dbMap = mySqlDB.getResultSet(q);
					logger.info("Read the data from db");
					boolean b = bp.compareHashMaps(row, dbMap);
					if (b)
						logger.info("Data in file and table matched");
					else
						logger.info("Data in file and table did not match");
				}
			} catch (Exception e){
				e.printStackTrace();
			}
		}
	}

}
