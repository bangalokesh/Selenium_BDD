package stepdefinition;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Then;
import cucumber.api.java.en.When;
import managers.PageManager;
import pageclasses.BasePage;

public class M360BillingStepDefinition {

	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(M360BillingStepDefinition.class.getName());

	PageManager pm = new PageManager();
	BasePage bp = pm.getBasePage();

	@When("Navigate to the Billing page")
	public void navigateBillingTab() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Navigate to billing page");
				pm.getM360HomePage().navigateToMThreeSixty();
				pm.getM360HomePage().navigateToM360BillingHome();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@And("Navigate to the Invoice page")
	public void navigateToInvoiceTab() {
		try {

			if (BasePage.isContinueExecution()) {
				logger.info("Navigate to billing page");
				pm.getM360BillingInvoicePage().navigateToInvoiceTab();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@And("Search Invoice for member")
	public void searchInvoice() {
		try {

			if (BasePage.isContinueExecution()) {
				logger.info("Search Invoice Details");
				pm.getM360BillingInvoicePage().searchMedicareId();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@And("Navigate to the Member payment tab")
	public void navigateMemberPaymentTab() {
		try {

			if (BasePage.isContinueExecution()) {
				logger.info("Navigate to billing page");
				pm.getM360MemberPaymentsPage().navigateToMemberPaymentDetails();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("validate the Member payments details")
	public void validateMemPayment() {
		try {

			if (BasePage.isContinueExecution()) {
				logger.info("Navigate to member payment tab");
				pm.getM360BillingMemPaymentProcess().executeMemberPaymentsValidation();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("I create Payment File")
	public void createBillFromTable() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Create Bill From Table");
				pm.getCreateBillFromTable().createPaymentFile();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("Verify and get Invoice Details for member")
	public void getInvoicedetails() {
		try {

			if (BasePage.isContinueExecution()) {
				logger.info("Get Invoice Details");
				pm.getM360BillingMemPaymentProcess().executeBillingInvoice();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("I Search and verify open Invoice Details for each Member")
	public void verifyOpenInvoices() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Get open invoice details");
				pm.getM360BillingMemPaymentProcess().executeBillingInvoiceValidation();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("I Search and verify Unapplied Cash Invoice Details for each Member")
	public void verifyOpenUnappliedCashInvoices() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Get open Unapplied Cash invoice details");
				pm.getM360BillingMemPaymentProcess().executeBillingInvoiceUnappliedCashValidation();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

}
