package stepdefinition;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import managers.PageManager;
import pageclasses.BasePage;

public class CallidusStepDefinition {

	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(CallidusStepDefinition.class.getName());

	PageManager pm = new PageManager();
	BasePage bp = pm.getBasePage();

	@Given("I am on Callidus Login Screen")
	public void callidusLogin() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Start Logging in to Callidus Application");
				pm.getCallidusLoginPage().login();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@When("I search for Customer")
	public void searchForCustomer() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Searching for Customer in Callidus Application");
				pm.getCallidusCustomerPage().searchCustomer();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("I validate Customer demographics")
	public void i_validate_Customer_demographics() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Validating Customer Demographics");
				pm.getCallidusCustomerPage().validateCustomerDemographics();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("I validate Customer Agent Details")
	public void i_validate_Customer_agent_details() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Start Logging in to Callidus Application");
				pm.getm360EnrollmentProcess().executeTestsForCallidus();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("I validate Customer Agent Details accessDB")
	public void i_validate_Customer_agent_details_accessDB() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Start Logging in to Callidus Application");
				pm.getm360EnrollmentProcess().executeTestsForCallidusOnAccessDB();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
