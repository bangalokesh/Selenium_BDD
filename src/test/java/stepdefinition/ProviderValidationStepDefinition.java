package stepdefinition;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import managers.PageManager;
import pageclasses.BasePage;

public class ProviderValidationStepDefinition {

	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(ProviderValidationStepDefinition.class.getName());

	PageManager pm = new PageManager();
	BasePage bp = pm.getBasePage();

	@Given("I Validate MDH Provider Data")
	public void validateMDHProvider() {
		try {
			if (BasePage.isContinueExecution()) {
				pm.getproviderProcess().executeMDH_Validation();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("I Validate MPP Provider Data")
	public void validateMPPProvider() {
		try {
			if (BasePage.isContinueExecution()) {
				pm.getproviderProcess().executeMPP_Validation();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@And("I Validate MPM Provider Data")
	public void validateMPMProvider() {
		try {
			if (BasePage.isContinueExecution()) {
				pm.getproviderProcess().executeMPM_Validation();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("I Validate MHT Provider Data")
	public void validateMHTProvider() {
		try {
			if (BasePage.isContinueExecution()) {
				pm.getproviderProcess().executeMHT_Validation();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@And("I Validate MMH Provider Data")
	public void validateMMHProvider() {
		try {
			if (BasePage.isContinueExecution()) {
				pm.getproviderProcess().executeMMH_Validation();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("I Validate MPH Provider Data")
	public void validateMPHProvider() {
		try {
			if (BasePage.isContinueExecution()) {
				pm.getproviderProcess().executeMPH_Validation();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
