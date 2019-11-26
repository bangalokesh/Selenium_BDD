package stepdefinition;

import cucumber.api.java.en.Given;
import cucumber.api.java.en.Then;
import managers.PageManager;
import pageclasses.BasePage;

public class M360MembershipStepDefinition {

	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(M360MembershipStepDefinition.class.getName());

	PageManager pm = new PageManager();
	BasePage bp = pm.getBasePage();

	@Given("I Login to MThreeSixty")
	public void m360Login() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Start Logging in to M360");
				pm.getM360LoginPage().login();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Given("I verify members")
	public void verifyMembers() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Verifying Members");
				pm.getM360memPage().validateMemberData();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("I do eligibility check")
	public void eligibilityCheck() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Starting Member Eligibility in M360");
				pm.getm360ElgInquiryPage().getMemberEligibilityDetails();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Given("I Enroll a member")
	public void enrollaMember() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Starting Member Enroll in M360");
				pm.getM360HomePage().navigateToMThreeSixty();
				pm.getm360EnrollmentProcess().executeTestsForEnrollment();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("I navigate to MemberPage")
	public void navigateMemPage() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Navigate to member page");
				pm.getM360memPage().navigateToMemberDetails();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("I verify member details")
	public void verifyMemberDetails() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Validate Member Details");
				pm.getm360EnrollmentProcess().executeTestMemberValidation();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("I verify application status")
	public void verifyApplicationStatus() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Validate Application Status");
				pm.getM360HomePage().navigateToMThreeSixty();
				pm.getm360MAEnrollPage().getApplicationStatusByAppID();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("I create OECFile")
	public void createOECFIle() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Creating OEC File");
				pm.getoecMemberEnrollmentPage().createOECRecordInDB();
				pm.getCreateFileFromLayout().generateOEC_CMS_File();
				pm.getCreateFileFromLayout().generateOEC_EH_File();
				pm.getCreateFileFromLayout().generateOEC_AON_File();
				pm.getCreateFileFromLayout().generateOEC_HPT_File();
				pm.getCreateFileFromLayout().generateOEC_CLR_2019_File();
				pm.getCreateFileFromLayout().generateOEC_CLR_2020_File();
				pm.getCreateFileFromLayout().generateOEC_EXT_File();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("I navigate to EligibilityPage")
	public void navigateMemEligibilityPage() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Navigate to member eligibility inquiry page");
				pm.getM360HomePage().navigateToEligInquiry();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("I clean up OEC table for next run")
	public void cleanUpOECFileTable() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Clean OEC File Table");
				pm.getoecMemberEnrollmentPage().deleteFromOECFileCreation();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("I verify letter details")
	public void verifyLetterDetails() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Validating Letters");
				pm.getm360EnrollmentProcess().executeTestLetterValidation();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("I create TRR File")
	public void createTRRFile() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Create TRR File");
				pm.createTRRFileFromTable().populateDataBase();
				pm.createTRRFileFromTable().createEnrollmentTRRFileFromLayout();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	/*
	 * @Given("I Change a member") public void changeaMember() { try { if
	 * (BasePage.isContinueExecution()) {
	 * logger.info("Starting Member Change in M360");
	 * pm.getM360HomePage().navigateToMThreeSixty();
	 * pm.getm360EnrollmentProcess().executeTestsForChange(); } } catch (Exception
	 * e) { e.printStackTrace(); } }
	 */

	@Then("I verify Member ID Card")
	public void VerifyMemberIDCard() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Verify Member ID Card");
				pm.getVerifyMemberIDCard().Member_ID_Validation();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Given("I validate Migrated Member Data in M360")
	public void validateMigratedMemData() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Validate Migrated Member Data in M360");
				pm.getm360EnrollmentProcess().validateMemberDataMigration();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("I verify Disenrollment")
	public void VerifyDisenrollment() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Verify Disenrollment");
				pm.getM360memPage().validateDisenrollment();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("I verify name change")
	public void VerifyNameChange() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Verify Name Change");
				pm.getM360memPage().validateNameChange();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("I verify beneficiaryID change")
	public void verifyBeneficaryChange() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Verify Beneficiary ID change");
				pm.getM360memPage().validateBeneficiaryIDChange();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("I validate PGR MDH in M360")
	public void validatePGR_MDH_inM360() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Validate PGR MDH Data in M360");
				pm.getM360HomePage().navigateToMThreeSixty();
				pm.getm360EnrollmentProcess().validatePGRToM360PCPProcess("MDH");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("I validate PGR MPP in M360")
	public void validatePGR_MPP_inM360() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Validate PGR MPP Data in M360");
				pm.getM360HomePage().navigateToMThreeSixty();
				pm.getm360EnrollmentProcess().validatePGRToM360PCPProcess("MPP");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("I validate PGR MHT in M360")
	public void validatePGR_MHP_inM360() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Validate PGR MHT Data in M360");
				pm.getM360HomePage().navigateToMThreeSixty();
				pm.getm360EnrollmentProcess().validatePGRToM360PCPProcess("MHT");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("I validate PGR MMH in M360")
	public void validatePGR_MHH_inM360() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Validate PGR MMH Data in M360");
				pm.getM360HomePage().navigateToMThreeSixty();
				pm.getm360EnrollmentProcess().validatePGRToM360PCPProcess("MMH");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("I validate PGR MPH in M360")
	public void validatePGR_MPH_inM360() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Validate PGR MPH Data in M360");
				pm.getM360HomePage().navigateToMThreeSixty();
				pm.getm360EnrollmentProcess().validatePGRToM360PCPProcess("MPH");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("I validate PGR MPM in M360")
	public void validatePGR_MPM_inM360() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Validate PGR MPM Data in M360");
				pm.getM360HomePage().navigateToMThreeSixty();
				pm.getm360EnrollmentProcess().validatePGRToM360PCPProcess("MPM");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("I validate COMPLETED ENROLLMENT REQUEST AND TO CONFIRM ENROLLMENT")
	public void validateMemLetterCompletedEnrollmentRequest() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Validate COMPLETED ENROLLMENT REQUEST AND TO CONFIRM ENROLLMENT");
				pm.getm360EnrollmentProcess().validateCompletedEnrollmentRequest();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("I validate NOTICE FOR DISENROLLMENT DUE TO CONFIRMATION OF OOA")
	public void validateMemLetterNoticeForDisenrollmentOOAStatus() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Validate NOTICE FOR DISENROLLMENT DUE TO CONFIRMATION OF OOA");
				pm.getm360EnrollmentProcess().validateNoticeForDisenrollmentOOAStatus();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("I Create Modification TRR File")
	public void createDisEnrollmentFile() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Creating Modification TRR File");
				pm.createTRRFileFromTable().createTRRDisenrollmentFile();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("I validate CONFIRM VOLUNTARY DISENROLLMENT FOLLOWING RECEIPT OF TRR")
	public void validateMemLetterConfirmVoluntaryDisenrollmentFollowingTRR() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Validate CONFIRM VOLUNTARY DISENROLLMENT FOLLOWING RECEIPT OF TRR");
				pm.getm360EnrollmentProcess().validateConfirmVoluntaryDisenrollmentFollowingTRR();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("I validate NOTIFICATION OF PLAN PREMIUM AMOUNT DUE FOR REINSTATEMENT")
	public void validateMemLetterNotificationPlanPremiumAmountDueForReinstatement() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Validate NOTIFICATION OF PLAN PREMIUM AMOUNT DUE FOR REINSTATEMENT");
				pm.getm360EnrollmentProcess().validateNotificationPlanPremiumAmountDueForReinstatement();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("I validate DISEMROLL DUE TO LOSS OF PART A-B")
	public void validateDisemrollDueToLossOfPartAB() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Validate DISEMROLL DUE TO LOSS OF PART A-B");
				pm.getm360EnrollmentProcess().validateDisemrollDueToLossOfPartAB();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("I validate MA MODEL NOTICE TO RESERARCH POTENTIAL OUT OF AREA STATUS")
	public void validateMemLetterMAModelNoticeResearchPotentialOutOfAreaStatus() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Validate MA MODEL NOTICE TO RESERARCH POTENTIAL OUT OF AREA STATUS");
				pm.getm360EnrollmentProcess().validateMAModelNoticeResearchPotentialOutOfAreaStatus();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("I validate MODEL NOTICE OF DISENROLLMENT DUE TO DEATH")
	public void validateMemLetterModelNoticeDisenrollmentDueToDeath() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Validate MODEL NOTICE OF DISENROLLMENT DUE TO DEATH");
				pm.getm360EnrollmentProcess().validateModelNoticeDisenrollmentDueToDeath();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("I close the member page")
	public void closeMemberPage() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Closing the member page...");
				pm.getM360memPage().closeMemberPage();
				BasePage.closeBrowser();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Then("I update CVT_MemberData table with MedicareID")
	public void updateCVT_MemberDataMedicareID() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Updating the MedicareID in CVT_MemberData table...");
				pm.getm360EnrollmentProcess().updateCVT_MemberDataMedicareID();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Given("I create 834 Enrollment File")
	public void I_create_834_Enrollment_File() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Creating 834 Enrollment File...");
				//pm.getEnrollmentFile().createEnrollmentFile();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	@Given("I create 270 Eligibility Check File")
	public void I_create_270_Eligibility_Check_File() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Creating 270 Eligibility Check File...");
				pm.getEligibilityCheckFile().createEligibilityFile();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
}
