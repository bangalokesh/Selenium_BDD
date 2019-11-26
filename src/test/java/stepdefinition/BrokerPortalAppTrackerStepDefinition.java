package stepdefinition;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import managers.PageManager;
import pageclasses.BasePage;

public class BrokerPortalAppTrackerStepDefinition {

	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(BrokerPortalAppTrackerStepDefinition.class.getName());

	PageManager pm = new PageManager();
	BasePage bp = pm.getBasePage();

	@Given("Opening broker portal and validate App Tracker")
	public void brokerAppLogin() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Start Logging in to Broker App");
				pm.getBrokerPortalProcess().executeBrokerPortalAppTrackerValidation();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@And("Opening broker portal and validate Commission report")
	public void openingAppTracker() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Navigating to the App tracker page");
				pm.getBrokerPortalProcess().executeBrokerPortalCommissionReportValidation();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
