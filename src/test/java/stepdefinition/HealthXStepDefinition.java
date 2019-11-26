package stepdefinition;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import managers.PageManager;
import pageclasses.BasePage;

public class HealthXStepDefinition {

	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(HealthXStepDefinition.class.getName());

	PageManager pm = new PageManager();
	BasePage bp = pm.getBasePage();
	
	@Given("I Launch the HealthX Provider Portal")
	public void launchHealthXProviderPortal() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Launching HealthX Provider Portal");
				pm.getHealthXLoginPage().launchProviderPortalPage();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Given("I Launch the HealthX Member Portal")
	public void launchHealthXMemberPortal() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Launching HealthX Member Portal");
				pm.getHealthXLoginPage().launchMemberPortalPage();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Given("I Launch the HealthX Admin Portal")
	public void launchHealthXAdminPortal() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Launching HealthX Admin Portal");
				pm.getHealthXLoginPage().launchAdminPortalPage();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Given("I login the HealthX Admin Portal")
	public void loginToHealthXAdminPortal() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Logging into HealthX Admin Portal");
				pm.getHealthXLoginPage().loginAsAdmin();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Then("I validate the Member data")
	public void validateMemberPortalData() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Validating the Member Portal data...");
				pm.getHealthXProcess().executeTestsForMember(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Then("I validate the Provider data")
	public void validateProviderPortalData() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Validating the Provider Portal data...");
				pm.getHealthXProcess().executeTestsForProvider(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
