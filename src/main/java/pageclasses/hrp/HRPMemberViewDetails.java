package pageclasses.hrp;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.aventstack.extentreports.Status;

import enums.LocatorTypes;
import io.appium.java_client.windows.WindowsDriver;
import pageclasses.BasePage;
import pageobjects.HRPObjRepo;

public class HRPMemberViewDetails extends BasePage {

	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(HRPMemberViewDetails.class.getName());

	public HRPMemberViewDetails(WindowsDriver<WebElement> winDriver) {

	}

	public HashMap<String, String> getGeneralDetails() {
		HashMap<String, String> genericDetails = new HashMap<String, String>();
		try {
			String accountName = getWinElement(LocatorTypes.id, HRPObjRepo.HRPMemberViewDetails.accountName_id)
					.getText();
			String primaryName = getWinElement(LocatorTypes.id, HRPObjRepo.HRPMemberViewDetails.primaryName_id)
					.getText();
			String activeDate = getWinElement(LocatorTypes.id, HRPObjRepo.HRPMemberViewDetails.activeDate_id).getText();
			activeDate = getDateInYYMMDD(activeDate);
			activeDate = getDateStringInSqlFormat(activeDate);
			List<WebElement> result = getWinElements(LocatorTypes.className,
					HRPObjRepo.HRPMemberViewDetails.general_className);
			String accID = result.get(1).getText();
			String[] accountID = accID.split("-");
			String planID = accountID[0];
			String memberID = result.get(3).getText();
			String effectiveDate = result.get(2).getText();
			effectiveDate = getDateInYYMMDD(effectiveDate);
			effectiveDate = getDateStringInSqlFormat(effectiveDate);
			String dob = result.get(9).getText().trim();
			String finalDob = getHRPDateInYYYYMMDD(dob);

			genericDetails.put("accountName", accountName);
			genericDetails.put("primaryName", primaryName);
			genericDetails.put("activeDate", activeDate);
			genericDetails.put("planID", planID);
			genericDetails.put("memberID", memberID);
			genericDetails.put("effectiveDate", effectiveDate);
			genericDetails.put("finalDob", finalDob);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Retrieval of General details failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return genericDetails;
	}

	public boolean validateGeneralDetails() {
		boolean flag = true;
		try {
			HashMap<String, String> generalDetails = getGeneralDetails();
			String fullName;
			String primaryName = generalDetails.get("primaryName");
			String plan = testData.get("ProductPlanName");
			//String[] planName = plan.split(" ");
			//String accountName = planName[0].trim();
			if (generalDetails.get("accountName").toUpperCase().trim().contains(plan)) {
				test.log(Status.FAIL, "Plan Name " + testData.get("ProductPlanName") + " is not equal to "
						+ generalDetails.get("accountName"));
				flag = false;
			}

			if (testData.get("middleInitial").trim().isEmpty()) {
				fullName = testData.get("firstname").trim() + " " + testData.get("lastname").trim();
			} else {
				fullName = testData.get("firstname").trim() + " " + testData.get("middleInitial").trim() + " "
						+ testData.get("lastname").trim();
			}

			if(primaryName.contains(".")) {
				String[] primaryNameSplit = primaryName.split("\\.");
				primaryName = primaryNameSplit[1].trim();
			}
			
			if (!primaryName.trim().equalsIgnoreCase(fullName)) {
				test.log(Status.FAIL,
						"Member name " + fullName + " is not equal to " + primaryName);
				flag = false;
			}

			if (!generalDetails.get("planID").trim().equalsIgnoreCase(testData.get("PlanID").trim())) {
				test.log(Status.FAIL,
						"Plan ID " + testData.get("PlanID") + " is not equal to " + generalDetails.get("planID"));
				flag = false;
			}

			StringBuilder str = new StringBuilder(testData.get("SupplementalID").trim());
			StringBuilder SupplementalID = str.deleteCharAt(0).deleteCharAt(0).deleteCharAt(0);
			if (!generalDetails.get("memberID").trim().equalsIgnoreCase(SupplementalID.toString().trim())) {
				test.log(Status.FAIL,
						"Member ID " + SupplementalID + " is not equal to " + generalDetails.get("memberID"));
				flag = false;
			}

			if (!generalDetails.get("effectiveDate").trim().equalsIgnoreCase(testData.get("CoverageDate"))) {
				test.log(Status.FAIL, "Effective Date " + testData.get("CoverageDate") + " is not equal to "
						+ generalDetails.get("effectiveDate"));
				flag = false;
			}

			if (!generalDetails.get("finalDob").trim().equalsIgnoreCase(testData.get("birthdate"))) {
				test.log(Status.FAIL, "Active Date " + testData.get("birthdate") + " is not equal to "
						+ generalDetails.get("finalDob"));
				flag = false;
			}

			if (flag == true) {
				reportPass("General details verified successfully");
			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			try {
				reportFail("Validation of PCP Details Failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}

	public void editMember() {
		try {
			getWinElement(LocatorTypes.name, HRPObjRepo.HRPMemberViewDetails.editMember_name).click();
			Set<String> winHandles = winDriver.getWindowHandles();
			for (String winHandle : winHandles) {
				if (winDriver.getTitle().equalsIgnoreCase("Specify As of Date"))
					break;
				else
					winDriver.switchTo().window(winHandle);
			}
			getWinElement(LocatorTypes.name, HRPObjRepo.HRPMemberSpecifyAsOFDate.changeEffectiveDate_name).click();
			getWinElement(LocatorTypes.name, HRPObjRepo.HRPMemberSpecifyAsOFDate.changeEffectiveDate_name)
					.sendKeys(Keys.TAB + testData.get("ChangeEffectiveDate"));
			getWinElement(LocatorTypes.name, HRPObjRepo.HRPMemberSpecifyAsOFDate.changeProcessClaimDate_name).click();
			getWinElement(LocatorTypes.name, HRPObjRepo.HRPMemberSpecifyAsOFDate.reasonCode_name)
					.sendKeys(Keys.TAB + "Active");
			getWinElement(LocatorTypes.name, HRPObjRepo.HRPMemberSpecifyAsOFDate.comment_name)
					.sendKeys(Keys.TAB + "testing change");
			getWinElement(LocatorTypes.name, HRPObjRepo.HRPMemberSpecifyAsOFDate.submit_name).click();
			reportPass("Edit Started");

			wait(10);
			winHandles = winDriver.getWindowHandles();
			for (String winHandle : winHandles) {
				if (winDriver.getTitle().contains("Edit Subs")) {
					System.out.println(winDriver.getTitle());
					break;
				} else {
					System.out.println(winDriver.getTitle());
					winDriver.switchTo().window(winHandle);
				}
			}
			getWinElement(LocatorTypes.name, HRPObjRepo.HRPMemberEditSuscriptionBankAccounts.bankAccountsTab_name)
					.click();
			getWinElement(LocatorTypes.name, HRPObjRepo.HRPMemberEditSuscriptionBankAccounts.paymentType_name)
					.sendKeys(Keys.TAB + "Check");
			getWinElement(LocatorTypes.name, HRPObjRepo.HRPTobBar.save_name).click();
			reportPass("Edit Member Completed");
			getWinElement(LocatorTypes.name, HRPObjRepo.HRPMemberSpecifyAsOFDate.submit_name).click();

			winHandles = winDriver.getWindowHandles();
			for (String winHandle : winHandles) {
				winDriver.switchTo().window(winHandle);
				getWinElement(LocatorTypes.name, HRPObjRepo.HRPMemberReviewRequired.no_name).click();
				break;
			}

			wait(15);
			winHandles = winDriver.getWindowHandles();
			for (String winHandle : winHandles) {
				if (!winDriver.getTitle().equals("Edit Subscription")) {
					winDriver.switchTo().window(winHandle);
					break;
				} else {
					getWinElement(LocatorTypes.name, HRPObjRepo.HRPMemberReviewRequired.yes_name).click();
				}
			}

			getWinElement(LocatorTypes.name, HRPObjRepo.HRPTobBar.close_name).click();
			getWinElement(LocatorTypes.name, HRPObjRepo.HRPMemberReviewRequired.yes_name).click();

			Actions action = new Actions(winDriver);
			action.sendKeys(Keys.ALT + "Home");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public HashMap<String, String> getContactDetails() {
		HashMap<String, String> contactDetails = new HashMap<String, String>();
		try {
			getWinElement(LocatorTypes.name, HRPObjRepo.HRPSupplierViewDetails.contactTab_name).click();
			List<WebElement> result = getWinElements(LocatorTypes.className,
					HRPObjRepo.HRPContactDetails.address_className);
			String zipCode = result.get(6).getText();
			String city = result.get(7).getText();
			String address = result.get(10).getText();
			List<WebElement> stateClass = getWinElements(LocatorTypes.className,
					HRPObjRepo.HRPContactDetails.state_className);
			String state = stateClass.get(19).getText();

			contactDetails.put("zipCode", zipCode);
			contactDetails.put("city", city);
			contactDetails.put("address", address);
			contactDetails.put("state", state);

		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Retrieval of Contact details failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return contactDetails;
	}

	public boolean validateContactDetails() {
		boolean flag = true;
		try {
			HashMap<String, String> addressDetails = getContactDetails();
			if (!addressDetails.get("address").trim().equalsIgnoreCase(testData.get("Address1").trim())) {
				test.log(Status.FAIL,
						"Address " + testData.get("Address1") + " is not equal to " + addressDetails.get("address"));
				flag = false;
			}
			if (!addressDetails.get("state").trim().equalsIgnoreCase(testData.get("StateCode").trim())) {
				test.log(Status.FAIL,
						"State " + testData.get("StateCode") + " is not equal to " + addressDetails.get("state"));
				flag = false;
			}

			if (flag == true) {
				reportPass("Contact details verified successfully");
			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			try {
				reportFail("Failure in validating contact details -- validateContactDetails ");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}

	public HashMap<String, String> getBenefitDetails() {
		HashMap<String, String> benefitDetails = new HashMap<String, String>();
		try {
			getWinElement(LocatorTypes.name, HRPObjRepo.HRPBenefitPlanDetails.benefitTab_name).click();
			String planText = getWinElement(LocatorTypes.name, HRPObjRepo.HRPBenefitPlanDetails.benefitPlan_name)
					.getText();
			String[] plan = planText.split("[(]");
			String[] pbp = plan[1].split("-");
			String pbpId = pbp[0].trim();
			pbpId = pbpId.replaceAll("[)]", "").trim();

			benefitDetails.put("pbpId", pbpId);

		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Retrieval of Benefit details failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return benefitDetails;
	}

	public boolean validateBenefitPlanDetails() {
		boolean flag = true;
		try {
			HashMap<String, String> planDetails = getBenefitDetails();

			if (!planDetails.get("pbpId").trim().equalsIgnoreCase(testData.get("PBPID").trim())) {
				test.log(Status.FAIL,
						"PBPID " + testData.get("PBPID") + " is not equal to " + planDetails.get("pbpId"));
				flag = false;
			}
			if (flag == true) {
				reportPass("PBP details verified successfully");
			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			try {
				reportFail("Failure in validating PBP details -- validateBenefitPlanDetails ");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}

	public HashMap<String, String> getMedicareDetails() {
		HashMap<String, String> medDetails = new HashMap<String, String>();
		try {
			getWinElement(LocatorTypes.name, HRPObjRepo.HRPMedicareDetails.MBI_name).click();
			List<WebElement> result = getWinElements(LocatorTypes.className,
					HRPObjRepo.HRPMedicareDetails.medicare_className);
			String medicareID = result.get(1).getText();
			getWinElement(LocatorTypes.name, HRPObjRepo.HRPMedicareDetails.partAStartDate_name).click();
			getWinElement(LocatorTypes.name, HRPObjRepo.HRPMedicareDetails.partBStartDate_name).click();
			getWinElement(LocatorTypes.name, HRPObjRepo.HRPMedicareDetails.partDStartDate_name).click();
			List<WebElement> dates = getWinElements(LocatorTypes.className,
					HRPObjRepo.HRPMedicareDetails.startDates_className);
			String prtADate = dates.get(7).getText().trim();
			String partAStartDate = getHRPDateInYYYYMMDD(prtADate);
			String prtBDate = dates.get(9).getText().trim();
			String partBStartDate = getHRPDateInYYYYMMDD(prtBDate);
			String prtDDate = dates.get(10).getText().trim();
			String partDStartDate = getHRPDateInYYYYMMDD(prtDDate);
			medDetails.put("medicareID", medicareID);
			medDetails.put("partAStartDate", partAStartDate);
			medDetails.put("partBStartDate", partBStartDate);
			medDetails.put("partDStartDate", partDStartDate);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Retrieval of Medicare details failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return medDetails;
	}

	public boolean validateMedicareDetails() {
		boolean flag = true;
		try {
			getWinElement(LocatorTypes.name, HRPObjRepo.HRPMedicareDetails.medicareTab_name).click();
			if (isWinElementPresent(LocatorTypes.name, HRPObjRepo.HRPMedicareDetails.MBI_name)) {
				HashMap<String, String> medicareDetails = getMedicareDetails();

				if (!medicareDetails.get("medicareID").trim().equalsIgnoreCase(testData.get("MEDICAREID").trim())) {
					test.log(Status.FAIL, "PBPID " + testData.get("MEDICAREID") + " is not equal to "
							+ medicareDetails.get("medicareID"));
					flag = false;
				}
				if (!medicareDetails.get("partAStartDate").trim()
						.equalsIgnoreCase(testData.get("PartAStartDate").trim())) {
					test.log(Status.FAIL, "Part A start date " + testData.get("PartAStartDate") + " is not equal to "
							+ medicareDetails.get("partAStartDate"));
					flag = false;
				}
				if (!medicareDetails.get("partBStartDate").trim()
						.equalsIgnoreCase(testData.get("PartBStartDate").trim())) {
					test.log(Status.FAIL, "Part B start date " + testData.get("PartBStartDate") + " is not equal to "
							+ medicareDetails.get("partBStartDate"));
					flag = false;
				}
				if (!medicareDetails.get("partDStartDate").trim()
						.equalsIgnoreCase(testData.get("PartDStartDate").trim())) {
					test.log(Status.FAIL, "Part D start date " + testData.get("PartDStartDate") + " is not equal to "
							+ medicareDetails.get("partDStartDate"));
					flag = false;
				}
				if (flag == true) {
					reportPass("Medicare details verified successfully");
				}
			} else {
				flag = false;
				reportFail("Failure in validating Medicare details -- validateMedicareDetails ");
			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			try {
				reportFail("Failure in validating Medicare details -- validateMedicareDetails ");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}

	public HashMap<String, String> getProviderChoiceDetails() {
		HashMap<String, String> pcDetails = new HashMap<String, String>();
		try {
			getWinElement(LocatorTypes.name, HRPObjRepo.HRPProviderChoiceDetails.providerChoiceTab_name).click();
			String effDate = getWinElement(LocatorTypes.name,
					HRPObjRepo.HRPProviderChoiceDetails.providerEffectiveDate_name + "0").getText();
			effDate = getDateInYYMMDD(effDate);
			effDate = getDateStringInSqlFormat(effDate);

			String name = getWinElement(LocatorTypes.name, HRPObjRepo.HRPProviderChoiceDetails.providerName_name + "0")
					.getText();
			String[] pname = name.split(",");
			String providerName = pname[1] + " " + pname[0];

			String npi = getWinElement(LocatorTypes.name, HRPObjRepo.HRPProviderChoiceDetails.NPI_name + "0").getText();
			String[] npinum = npi.split(" ");
			String finalnpi = npinum[1].trim();

			pcDetails.put("effDate", effDate);
			pcDetails.put("providerName", providerName);
			pcDetails.put("finalnpi", finalnpi);

		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Retrieval of Provider Choice details failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return pcDetails;
	}

	public boolean validateProviderChoiceDetails() {
		boolean flag = true;
		try {
			HashMap<String, String> providerDetails = getProviderChoiceDetails();

			if (!providerDetails.get("effDate").trim().equalsIgnoreCase(testData.get("PCPStartDate").trim())) {
				test.log(Status.FAIL, "PCP start date " + testData.get("PCPStartDate") + " is not equal to "
						+ providerDetails.get("effDate"));
				flag = false;
			}

			if (!providerDetails.get("providerName").trim().equalsIgnoreCase(testData.get("PCPDoctorName").trim())) {
				test.log(Status.FAIL, "PCP doctor name " + testData.get("PCPDoctorName") + " is not equal to "
						+ providerDetails.get("providerName"));
				flag = false;
			}

			if (!providerDetails.get("finalnpi").trim().equalsIgnoreCase(testData.get("PCPNPI").trim())) {
				test.log(Status.FAIL, "PCP doctor name " + testData.get("PCPNPI") + " is not equal to "
						+ providerDetails.get("finalnpi"));
				flag = false;
			}
			if (flag == true) {
				reportPass("Provider details verified successfully");
			}

		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			try {
				reportFail("Failure in validating Provider details -- viewProviderChoiceDetails ");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}
}
