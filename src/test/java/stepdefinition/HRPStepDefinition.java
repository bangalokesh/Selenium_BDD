package stepdefinition;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import managers.PageManager;
import pageclasses.BasePage;

public class HRPStepDefinition {

	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(HRPStepDefinition.class.getName());

	PageManager pm = new PageManager();
	BasePage bp = pm.getBasePage();

	@Given("I am on HRP Login Screen")
	public void hrpLogin() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Start Logging in to HRP");
				pm.getLoginPage().hrpLogin();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@When("I search for member in HRP")
	public void memberSearch() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Searching for Member in HRP");
				pm.getHrpHomePage().navigateMemberSearch();
				pm.getHrpMemSearchPage().searchMember();
				pm.getHrpMemSearchPage().viewMember();
				// pm.getHrpMemViewDetails().viewGeneralDetails();
				pm.getHrpMemViewDetails().editMember();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@When("I search for provider")
	public void providerSearch() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Searching for Provider in HRP");

				pm.getHrpHomePage().searchProvider();
				pm.getHRPProviderSearchPage().validateProviders();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@When("Validate provider, supplier in HRP for PGR MDH program data")
	public void providerSupplier_MDH() {
		try {
			if (BasePage.isContinueExecution()) {
				pm.getHRPPGRProviderProcess().validateHRP_PGR("MDH");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@When("Validate provider, supplier in HRP for PGR MMH program data")
	public void providerSupplier_MMH() {
		try {
			if (BasePage.isContinueExecution()) {
				pm.getHRPPGRProviderProcess().validateHRP_PGR("MMH");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@When("Validate provider, supplier in HRP for PGR MPH program data")
	public void providerSupplier_MPH() {
		try {
			if (BasePage.isContinueExecution()) {
				pm.getHRPPGRProviderProcess().validateHRP_PGR("MPH");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@When("Validate provider, supplier in HRP for PGR MPP program data")
	public void providerSupplier_MPP() {
		try {
			if (BasePage.isContinueExecution()) {
				pm.getHRPPGRProviderProcess().validateHRP_PGR("MPP");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@When("Validate provider, supplier in HRP for PGR MPM program data")
	public void providerSupplier_MPM() {
		try {
			if (BasePage.isContinueExecution()) {
				pm.getHRPPGRProviderProcess().validateHRP_PGR("MPM");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@When("I search for supplier")
	public void supplierSearch() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Searching for Supplier in HRP");
				pm.getHrpHomePage().searchSupplier();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("I edit the member in HRP")
	public void memberEdit() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Editing Member in HRP");
				pm.getHrpMemViewDetails().editMember();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("I validate the member in HRP")
	public void validateMember() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Searching for Member in HRP");
				pm.getHrpHomePage().navigateMemberSearch();
				pm.getHRPMemberValidationProcess().validateHRPMember();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("validationPGROnExistingHRPData MPM")
	public void validationOnExistingData() {
		try {
			if (BasePage.isContinueExecution()) {
				pm.getHRPPGRProviderProcess().validationProcessOnExitsingHRPData("MDH");
				pm.getHRPPGRProviderProcess().validationProcessOnExitsingHRPData("MPM");

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("Create a service request")
	public void createServiceRequest() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("create service request");
				pm.getHRPCreateServiceRequestProcess().createServiceRequests();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("I Validate Claims")
	public void validateHRPClaims() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Validate Claims");
				pm.getHRPClaimsValidationProcess().validateClaims();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Then("I Clear HRP Workbasket")
	public void createHRPWorkbasket() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("create HRP Workbasket");
				pm.getHRPWorkbasketProcess().clearWorkBasket();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
}
