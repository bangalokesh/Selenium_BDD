package stepdefinition;

import cucumber.api.java.en.Given;
import managers.PageManager;
import pageclasses.BasePage;

public class SAPStepDefinition {
	
	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(M360MembershipStepDefinition.class.getName());

	PageManager pm = new PageManager();
	BasePage bp = pm.getBasePage();

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

}
