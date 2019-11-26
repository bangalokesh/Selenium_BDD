package stepdefinition;

import cucumber.api.java.en.Given;
import managers.PageManager;
import pageclasses.BasePage;

public class FileValidationCreation {

	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(GuidingCareStepDefinition.class.getName());
	PageManager pm = new PageManager();
	BasePage bp = pm.getBasePage();

	@Given("I have 835 file I validate it")
	public void I_have_835_file_I_validate_it() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Validating 835 Claims File");
				pm.getClaim835FileValidation().claimFileValidation();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Given("I create professional claim file")
	public void I_create_professional_claim_file() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Creating 837 Professional Claims File");
				pm.getClaimFileCreationProf().create837ClaimsFile();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Given("I create institutional claim file")
	public void I_create_institutional_claim_file() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Creating 837 Institutional Claims File");
				pm.getClaimFileCreationInst().create837ClaimsFile();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
