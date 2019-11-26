package stepdefinition;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import managers.PageManager;
import pageclasses.BasePage;

public class GuidingCareStepDefinition {

	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(GuidingCareStepDefinition.class.getName());
	PageManager pm = new PageManager();
	BasePage bp = pm.getBasePage();

	@Given("I am on Guiding Care Login Page")
	public void I_am_on_GuidingCareLoginPage() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Start Logging in to Guiding Care Application");
				pm.getGuidingCareLoginPage().login();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Given("Guiding Care Login Page as UMIntake")
	public void I_am_on_GuidingCareLoginPageUMIntake() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Start Logging in to Guiding Care Application as UMIntake");
				pm.getGuidingCareLoginPage().loginAsUMIntake();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Given("Guiding Care Login Page as UMSupervisor")
	public void I_am_on_GuidingCareLoginPageUMSupervisor() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Start Logging in to Guiding Care Application as UMSupervisor");
				pm.getGuidingCareLoginPage().loginAsUMSupervisor();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Given("Guiding Care Login Page as PhysicianReviewer")
	public void I_am_on_GuidingCareLoginPagePhysicianReveiwer() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Start Logging in to Guiding Care Application as PhysicianReviewer");
				pm.getGuidingCareLoginPage().loginAsPhysicianReviewer();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Given("Guiding Care Login Page as UMClinicalReviewer")
	public void I_am_on_GuidingCareLoginPageUMClinicalReviewer() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Start Logging in to Guiding Care Application as UMClinicalReviewer");
				pm.getGuidingCareLoginPage().loginAsUMClinicalReviewer();
				;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Given("Guiding Care Login Page as UMTech")
	public void I_am_on_GuidingCareLoginPageUMTech() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Start Logging in to Guiding Care Application as UMTech");
				pm.getGuidingCareLoginPage().loginAsUMTech();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("I validate Member Details")
	public void I_validate_Member_Details() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Start member validation in Guiding Care Application");
				pm.getGuidingCareProviderProcess().validateMemberDataInGuidingCare();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("I validate MA BlueAdvantage HMO provider details")
	public void validate_MA_BlueAdvantage_HMO_ProviderDetails() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Validating Guiding Care Provider MA BlueAdvantage HMO Information");
				pm.getGuidingCarePage().navigateToProviderPage();
				pm.getGuidingCareProviderProcess().executeGC_ProviderMA_BlueAdvantage_HMO();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("I validate MA BlueAdvantage P3 HMO provider details")
	public void validate_MA_BlueAdvantage_P3_HMO_ProviderDetails() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Validating Guiding Care Provider MA BlueAdvantage P3 HMO Information");
				pm.getGuidingCarePage().navigateToProviderPage();
				pm.getGuidingCareProviderProcess().executeGC_ProviderMA_BlueAdvantage_P3_HMO();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("I validate MA BlueJourney PPO Maricopa Pima details")
	public void validate_MA_BlueJourney_PPO_Maricopa_Pima() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Validating Guiding Care Provider MA BlueJourney PPO Maricopa Pima");
				pm.getGuidingCarePage().navigateToProviderPage();
				pm.getGuidingCareProviderProcess().executeGC_ProviderMA_BlueJourney_PPO_Maricopa_Pima();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("I validate MA BluePathway P3 HMO details")
	public void validate_MA_BluePathway_P3_HMO() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Validating Guiding Care Provider MA BluePathway P3 HMO");
				pm.getGuidingCarePage().navigateToProviderPage();
				pm.getGuidingCareProviderProcess().executeGC_ProviderMA_BluePathway_P3_HMO();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("I validate MA BluePathway Maricopa HMO details")
	public void validate_MA_BluePathway_Maricopa_HMO() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Validating Guiding Care Provider MA BluePathway Maricopa HMO");
				pm.getGuidingCarePage().navigateToProviderPage();
				pm.getGuidingCareProviderProcess().executeGC_ProviderMA_BluePathway_Maricopa_HMO();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("I create preAuthorization activity")
	public void I_create_preAuthorization() {
		try {
			if (BasePage.isContinueExecution()) {
				pm.getGuidingCareProviderProcess().createPreauthorizationActivity();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("Decisions on preauthorization activities")
	public void decision_on_preauthorization_activities() {
		try {
			if (BasePage.isContinueExecution()) {
				pm.getGuidingCareProviderProcess().decisionPreAuthorizationActivity();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("Create preauthorization for claims")
	public void create_preauthorization_claims() {
		try {
			if (BasePage.isContinueExecution()) {
				pm.getGuidingCareProviderProcess().createPreAuthorizationForClaims();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("I validate Complaint Details")
	public void validateComplaintDetails() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Validating Complaint Details");
				pm.getGuidingCareProviderProcess().validateComplaints("H0302");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("Guiding Care Logout")
	public void Guiding_Care_Logout() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Logging out from Guiding Care");
				pm.getGuidingCarePage().logout();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
