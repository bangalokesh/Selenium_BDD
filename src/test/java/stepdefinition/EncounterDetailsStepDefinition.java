package stepdefinition;

import cucumber.api.java.en.Then;
import managers.PageManager;
import pageclasses.BasePage;

public class EncounterDetailsStepDefinition {
	
	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(EncounterDetailsStepDefinition.class.getName());

	PageManager pm = new PageManager();
	BasePage bp = pm.getBasePage();

	@Then("I navigate to Encounter Details")
	public void navigateToEncounterDetails() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Navigate to Encounter Details");
				pm.getEncountersHomePage().navigateToEncounters();
				pm.getEncountersHomePage().navigateToEncounterDetails();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Then("I validate Encounter Details")
	public void validateEncounterDetails() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Validating Encounter Details");
				pm.getEncounterDetailsProcess().executeEncounterDetails();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
