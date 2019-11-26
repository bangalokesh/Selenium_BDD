package pageclasses.guidingCare;

import java.io.IOException;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Random;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import enums.LocatorTypes;
import managers.PageManager;
import pageclasses.BasePage;
import pageobjects.GuidingCareObjRepo;
import utils.Dbconn;
import utils.DbconnProd;

public class GuidingCarePreAuthorization extends BasePage {

	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(GuidingCarePreAuthorization.class.getName());
	PageManager pm = new PageManager();
	Dbconn db = new Dbconn();
	DbconnProd dbp = new DbconnProd();

	public void preauthorization_InPatient() {
		try {
			boolean flag = false;
			navigateToInPatient();
			flag = preAuth_EligibilityVerification("Pre-Service");

			if (flag) {
				String benefitNetwork = getBenefitNetworkName();
				flag = preAuth_ProviderDetails(benefitNetwork);

				if (!flag) {
					return;
				}

				flag = preAuth_AuthBasicDetails();

				if (!flag) {
					return;
				}

				flag = preAuth_DiagnosisAndServiceCodes();

				if (!flag) {
					return;
				}
				flag = preAuth_returnDueDate();

				if (flag)
					reportPass("PreAuthorization creation is successful PatientType:" + testData.get("PatientType")
							+ " ,AuthType:" + testData.get("PreAuthType") + "and ActivityType:"
							+ testData.get("ActivityType"));
				else
					reportFail("PreAuthorization creation failed");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void preauthorization_OutPatient() {
		try {
			boolean flag = false;
			navigateToOutPatient();
			flag = preAuth_EligibilityVerification("OutPatient");

			if (flag) {
				String benefitNetwork = getBenefitNetworkName();
				flag = preAuth_ProviderDetails_OutPatient(benefitNetwork);

				if (!flag) {
					return;
				}

				flag = preAuth_AuthBasicDetails_OutPatient();

				if (!flag) {
					return;
				}

				flag = preAuth_DiagnosisAndServiceCodes();

				if (!flag) {
					return;
				}
				flag = preAuth_returnDueDate();

				if (flag)
					reportPass("PreAuthorization creation is successful PatientType:" + testData.get("PatientType")
							+ " ,AuthType:" + testData.get("PreAuthType") + "and ActivityType:"
							+ testData.get("ActivityType"));
				else
					reportFail("PreAuthorization creation failed");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public String getBenefitNetworkName() {

		String network = getElement(LocatorTypes.xpath, GuidingCareObjRepo.PreAuthorization.network_xpath).getText();

		switch (network) {
		case "MA Blue Advantage P3 HMO":
			network = "MA BlueAdvantage P3 HMO";
			break;

		case "MA Blue Advantage HMO":
			network = "MA BlueAdvantage HMO";
			break;
		}

		return network;
	}

	public String getProviderName(String benefitNetwork) {
		HashMap<String, String> resultSet = new HashMap<String, String>();
		String provName = null;
		String query = "select top 1  PROV_LAST_NAME from [ProviderMasterETL].[Altruista].[contract] c,\r\n"
				+ "[ProviderMasterETL].[Altruista].[provider] p\r\n" + "where p.PROVIDER_ID = c.PROVIDER_ID\r\n"
				+ "and c.BENEFIT_NETWORK = '" + benefitNetwork + "'\r\n" + "and PROV_LAST_NAME != '' order by newid()";

		resultSet = dbp.getResultSetProd(query);

		provName = resultSet.get("PROV_LAST_NAME");
		return provName;
	}

	public void navigateToInPatient() {

		try {
			wait(2);
			elementVisibleWait(LocatorTypes.id, GuidingCareObjRepo.GuidingCareLandingPage.inPatient_id, 10);
			elementClickableWait(LocatorTypes.id, GuidingCareObjRepo.GuidingCareLandingPage.inPatient_id, 10);
			WebElement inPatient = getElement(LocatorTypes.id, GuidingCareObjRepo.GuidingCareLandingPage.inPatient_id);
			JavascriptExecutor ex = (JavascriptExecutor) driver;
			ex.executeScript("arguments[0].click();", inPatient);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void navigateToOutPatient() {

		try {
			wait(2);
			elementVisibleWait(LocatorTypes.id, GuidingCareObjRepo.GuidingCareLandingPage.outPatient_id, 5);
			elementClickableWait(LocatorTypes.id, GuidingCareObjRepo.GuidingCareLandingPage.outPatient_id, 5);
			WebElement outPatient = getElement(LocatorTypes.id,
					GuidingCareObjRepo.GuidingCareLandingPage.outPatient_id);
			JavascriptExecutor ex = (JavascriptExecutor) driver;
			ex.executeScript("arguments[0].click();", outPatient);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public boolean preAuth_EligibilityVerification(String dropdownToBe) {
		boolean flag = true;
		try {
			wait(3);
			if (!isElementPresent(LocatorTypes.xpath,
					GuidingCareObjRepo.PreAuthorization.authTypeDropdownArrowButton_xpath)) {

				if (isElementPresent(LocatorTypes.xpath,
						GuidingCareObjRepo.PreAuthorization.authTypeNoRecordsFound_xpath)) {

					if (getElement(LocatorTypes.xpath, GuidingCareObjRepo.PreAuthorization.authTypeNoRecordsFound_xpath)
							.getText().equals("No records found")) {
						reportFail("Eligibility information is not present");
						return false;
					}
				}
			}

			WebElement dropdownArrow = getElement(LocatorTypes.xpath,
					GuidingCareObjRepo.PreAuthorization.authTypeDropdownArrowButton_xpath);
			WebElement dropdownText = getElement(LocatorTypes.xpath,
					GuidingCareObjRepo.PreAuthorization.authTypeDropdownSelectedText_xpath);

			selectDropdownValue(dropdownToBe, dropdownArrow, dropdownText);

			if (!(getElement(LocatorTypes.xpath, GuidingCareObjRepo.PreAuthorization.authTypeDropdownSelectedText_xpath)
					.getText().equals(dropdownToBe))) {
				reportFail("No options are found in AuthType dropdown");
				flag = false;
				return flag;
			}

			getElement(LocatorTypes.id, GuidingCareObjRepo.PreAuthorization.saveAndNext_id).click();

		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	public boolean preAuth_ProviderDetails(String benefitNetwork) { // Inpatient
		try {

			Actions a = new Actions(driver);
			String provNPI = null;
			String provNPI2 = null;
			if (!testData.containsKey("ClaimType")) {// fetch NPI from Altruista table based on member program name
				provNPI = getProviderName(benefitNetwork);
				provNPI2 = getProviderName(benefitNetwork);
			} else { // code to select NPI values from claims table
				provNPI = testData.get("BillingProviderNPI").toString(); // facilityProviderNPI
				provNPI2 = testData.get("AttendingProviderNPI"); // AdmittingProviderNPI
			}
			elementVisibleWait(LocatorTypes.xpath, GuidingCareObjRepo.PreAuthorization.facilityProvNPIDropdown_xpath,
					10);
			elementClickableWait(LocatorTypes.xpath, GuidingCareObjRepo.PreAuthorization.facilityProvNPIDropdown_xpath,
					10);
			Select facilityDropdown = new Select(
					getElement(LocatorTypes.xpath, GuidingCareObjRepo.PreAuthorization.facilityProvNPIDropdown_xpath));
			facilityDropdown.selectByValue("NPI");
			elementVisibleWait(LocatorTypes.xpath, GuidingCareObjRepo.PreAuthorization.facilityProvNameText_xpath, 10);
			elementClickableWait(LocatorTypes.xpath, GuidingCareObjRepo.PreAuthorization.facilityProvNameText_xpath,
					10);
			wait(3);
			WebElement facilityProvName = getElement(LocatorTypes.xpath,
					GuidingCareObjRepo.PreAuthorization.facilityProvNameText_xpath);
			wait(2);
			a.sendKeys(facilityProvName, provNPI).sendKeys(Keys.ARROW_DOWN).pause(6000).sendKeys(Keys.ENTER).build()
					.perform();

			elementVisibleWait(LocatorTypes.xpath, GuidingCareObjRepo.PreAuthorization.facilityFax_xpath, 10);
			getElement(LocatorTypes.xpath, GuidingCareObjRepo.PreAuthorization.facilityFax_xpath)
					.sendKeys("1111111111");
			elementVisibleWait(LocatorTypes.xpath, GuidingCareObjRepo.PreAuthorization.admittingProvNPIDropdown_xpath,
					10);
			elementClickableWait(LocatorTypes.xpath, GuidingCareObjRepo.PreAuthorization.admittingProvNPIDropdown_xpath,
					10);
			Select admittingDropdown = new Select(
					getElement(LocatorTypes.xpath, GuidingCareObjRepo.PreAuthorization.admittingProvNPIDropdown_xpath));
			admittingDropdown.selectByValue("NPI");
			elementVisibleWait(LocatorTypes.xpath, GuidingCareObjRepo.PreAuthorization.admittingProvNameText_xpath, 10);
			elementClickableWait(LocatorTypes.xpath, GuidingCareObjRepo.PreAuthorization.admittingProvNameText_xpath,
					10);
			wait(3);
			WebElement admittingProvName = getElement(LocatorTypes.xpath,
					GuidingCareObjRepo.PreAuthorization.admittingProvNameText_xpath);
			wait(1);
			a.sendKeys(admittingProvName, provNPI2).sendKeys(Keys.ARROW_DOWN).pause(5000).sendKeys(Keys.ENTER).build()
					.perform();

			elementVisibleWait(LocatorTypes.xpath, GuidingCareObjRepo.PreAuthorization.admittingFax_xpath, 10);
			getElement(LocatorTypes.xpath, GuidingCareObjRepo.PreAuthorization.admittingFax_xpath)
					.sendKeys("1111111111");

			getElement(LocatorTypes.id, GuidingCareObjRepo.PreAuthorization.saveAndNext_id).click();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	public boolean preAuth_ProviderDetails_OutPatient(String benefitNetwork) { // outpatient
		try {

			Actions a = new Actions(driver);
			String provNPI = null;
			String provNPI2 = null;
			if (!testData.containsKey("ClaimType")) // PreAuth Claim
			{
				provNPI = getProviderName(benefitNetwork);
				provNPI2 = getProviderName(benefitNetwork);
			} else {
				provNPI = testData.get("BillingProviderNPI").toString();
				if (testData.get("RenderingProviderNPI").isEmpty()) {
					provNPI2 = testData.get("BillingProviderNPI");
				} else {
					provNPI2 = testData.get("RenderingProviderNPI");
				}
			}

			elementVisibleWait(LocatorTypes.xpath, GuidingCareObjRepo.PreAuthorization.facilityProvNPIDropdown_xpath,
					10);
			elementClickableWait(LocatorTypes.xpath, GuidingCareObjRepo.PreAuthorization.facilityProvNPIDropdown_xpath,
					10);
			Select facilityDropdown = new Select(
					getElement(LocatorTypes.xpath, GuidingCareObjRepo.PreAuthorization.facilityProvNPIDropdown_xpath));
			facilityDropdown.selectByValue("NPI");
			elementVisibleWait(LocatorTypes.xpath, GuidingCareObjRepo.PreAuthorization.facilityProvNameText_xpath, 10);
			elementClickableWait(LocatorTypes.xpath, GuidingCareObjRepo.PreAuthorization.facilityProvNameText_xpath,
					10);
			wait(3);
			WebElement facilityProvName = getElement(LocatorTypes.xpath,
					GuidingCareObjRepo.PreAuthorization.facilityProvNameText_xpath);
			wait(2);
			a.sendKeys(facilityProvName, provNPI).sendKeys(Keys.ARROW_DOWN).pause(5000).sendKeys(Keys.ENTER).build()
					.perform();

			elementVisibleWait(LocatorTypes.xpath, GuidingCareObjRepo.PreAuthorization.facilityFax_xpath, 10);
			getElement(LocatorTypes.xpath, GuidingCareObjRepo.PreAuthorization.facilityFax_xpath)
					.sendKeys("1111111111");

//			String provName2 = getProviderName(benefitNetwork);
//			elementVisibleWait(LocatorTypes.xpath, GuidingCareObjRepo.PreAuthorization.referredbyProvNameText_xpath, 10);
//			elementClickableWait(LocatorTypes.xpath, GuidingCareObjRepo.PreAuthorization.referredbyProvNameText_xpath, 10);
//			wait(3);
//			WebElement referredByProvName = getElement(LocatorTypes.xpath, GuidingCareObjRepo.PreAuthorization.referredbyProvNameText_xpath);
//			wait(1);
			// a.sendKeys(referredByProvName,provName2).sendKeys(Keys.ARROW_DOWN).pause(5000).sendKeys(Keys.ENTER).build().perform();

			elementVisibleWait(LocatorTypes.xpath, GuidingCareObjRepo.PreAuthorization.referredbyFax_xpath, 10);
			getElement(LocatorTypes.xpath, GuidingCareObjRepo.PreAuthorization.referredbyFax_xpath)
					.sendKeys("1111111111");

			elementVisibleWait(LocatorTypes.xpath, GuidingCareObjRepo.PreAuthorization.referredToProvNameText_xpath,
					10);
			elementClickableWait(LocatorTypes.xpath, GuidingCareObjRepo.PreAuthorization.referredToProvNameText_xpath,
					10);

			Select referredToDropdown = new Select(getElement(LocatorTypes.xpath,
					GuidingCareObjRepo.PreAuthorization.referredToProvNPIDropdown_xpath));
			referredToDropdown.selectByValue("NPI");

			wait(3);
			WebElement referredToProvName = getElement(LocatorTypes.xpath,
					GuidingCareObjRepo.PreAuthorization.referredToProvNameText_xpath);
			wait(1);
			a.sendKeys(referredToProvName, provNPI2).sendKeys(Keys.ARROW_DOWN).pause(5000).sendKeys(Keys.ENTER).build()
					.perform();

			elementVisibleWait(LocatorTypes.xpath, GuidingCareObjRepo.PreAuthorization.referredToFax_xpath, 10);
			getElement(LocatorTypes.xpath, GuidingCareObjRepo.PreAuthorization.referredToFax_xpath)
					.sendKeys("1111111111");

			getElement(LocatorTypes.id, GuidingCareObjRepo.PreAuthorization.saveAndNext_id).click();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean preAuth_AuthBasicDetails() {
		try {
			String notificationDate = null;
			String admissionDate = null;

			if (!testData.containsKey("ClaimType")) {
				notificationDate = getDateBeforeDaysTimeStamp(1);
				admissionDate = getTimeStamp();
			} else {
				String query = "SELECT CPTCodeID,UnitOrBasisforMeasurementCode,DateTimeFromPeriod,DateTimeToPeriod FROM [test_data_claimline] cl where TestCaseId = '"
						+ testData.get("TestCaseID") + "' and UnitOrBasisforMeasurementCode in ('UN','MJ')";

				List<HashMap<String, String>> claimLinelist = db.getListOfHashMapsFromResultSet(query);

				for (HashMap<String, String> row : claimLinelist) { // code to get least DateTimePeriod from claimline
																	// table
					if (notificationDate == null) {
						notificationDate = getDateMMDDYYYYFROMMDDYYYY(row.get("DateTimeFromPeriod"));
					} else if (notificationDate
							.compareTo(getDateMMDDYYYYFROMMDDYYYY(row.get("DateTimeFromPeriod"))) < 0) {
						notificationDate = getDateMMDDYYYYFROMMDDYYYY(row.get("DateTimeFromPeriod"));
					}
				}

				admissionDate = notificationDate + " 00:00:00";
				notificationDate = getDateBeforeDays(5, notificationDate) + " 00:00:00"; // notificationDate is (line
																							// 357) DateTimePeriod date
																							// - 5 days

			}

			getElement(LocatorTypes.id, GuidingCareObjRepo.PreAuthorization.notificationDate_id)
					.sendKeys(notificationDate);
			getElement(LocatorTypes.id, GuidingCareObjRepo.PreAuthorization.admissionDate_id).sendKeys(admissionDate); // DateTimePeriod
																														// date

			WebElement dropdownArrowButton = getElement(LocatorTypes.xpath,
					GuidingCareObjRepo.PreAuthorization.auth_priorityDropdown_xpath);
			WebElement dropdownText = getElement(LocatorTypes.xpath,
					GuidingCareObjRepo.PreAuthorization.auth_priorityText_xpath);

			selectDropdownValue(testData.get("PreAuthType"), dropdownArrowButton, dropdownText);

			elementVisibleWait(LocatorTypes.xpath, GuidingCareObjRepo.PreAuthorization.saveAndNextAuthBasics_xpath, 5);
			getElement(LocatorTypes.xpath, GuidingCareObjRepo.PreAuthorization.saveAndNextAuthBasics_xpath).click();

			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean preAuth_AuthBasicDetails_OutPatient() {
		String notificationDate = null;
		try {
			if (!testData.containsKey("ClaimType")) {
				notificationDate = getDateBeforeDaysTimeStamp(1);
				// String admissionDate = getTimeStamp();
			} else {
				String query = "SELECT CPTCodeID,UnitOrBasisforMeasurementCode,DateTimeFromPeriod,DateTimeToPeriod FROM [test_data_claimline] cl where TestCaseId = '"
						+ testData.get("TestCaseID") + "' and UnitOrBasisforMeasurementCode in ('UN','MJ')";

				List<HashMap<String, String>> claimLinelist = db.getListOfHashMapsFromResultSet(query);

				for (HashMap<String, String> row : claimLinelist) {
					if (notificationDate == null) {
						notificationDate = getDateMMDDYYYYFROMMDDYYYY(row.get("DateTimeFromPeriod"));
					} else if (notificationDate
							.compareTo(getDateMMDDYYYYFROMMDDYYYY(row.get("DateTimeFromPeriod"))) < 0) {
						notificationDate = getDateMMDDYYYYFROMMDDYYYY(row.get("DateTimeFromPeriod"));
					}
				}

				// String admissionDate = notificationDate+ " 00:00:00" ;
				notificationDate = getDateBeforeDays(5, notificationDate) + " 00:00:00";

			}

			getElement(LocatorTypes.id, GuidingCareObjRepo.PreAuthorization.notificationDate_id)
					.sendKeys(notificationDate);

			WebElement dropdownArrowButton = getElement(LocatorTypes.xpath,
					GuidingCareObjRepo.PreAuthorization.auth_priorityDropdown_xpath);
			WebElement dropdownText = getElement(LocatorTypes.xpath,
					GuidingCareObjRepo.PreAuthorization.auth_priorityText_xpath);

			selectDropdownValue(testData.get("PreAuthType"), dropdownArrowButton, dropdownText);

			elementVisibleWait(LocatorTypes.xpath, GuidingCareObjRepo.PreAuthorization.saveAndNextAuthBasics_xpath, 5);
			getElement(LocatorTypes.xpath, GuidingCareObjRepo.PreAuthorization.saveAndNextAuthBasics_xpath).click();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean preAuth_DiagnosisAndServiceCodes() {
		try {
			Actions a = new Actions(driver);
			wait(1);
			WebElement diagnosis = getElement(LocatorTypes.xpath,
					GuidingCareObjRepo.PreAuthorization.diagnosisDescription_xpath);
			a.sendKeys(diagnosis, "LUNG").sendKeys(Keys.ARROW_DOWN).pause(3000).sendKeys(Keys.ENTER).build().perform();
			wait(1);

			WebElement procedure = getElement(LocatorTypes.xpath,
					GuidingCareObjRepo.PreAuthorization.procedureDescription_xpath);
			a.sendKeys(procedure, "LUNG").sendKeys(Keys.ARROW_DOWN).pause(3000).sendKeys(Keys.ENTER).build().perform();
			wait(1);
			getElement(LocatorTypes.xpath, GuidingCareObjRepo.PreAuthorization.fromDate_xpath).sendKeys(CurrentDate());
			getElement(LocatorTypes.xpath, GuidingCareObjRepo.PreAuthorization.toDate_xpath)
					.sendKeys(getDateAfterDaysInMMddYYYYformat(2).toString());
			wait(3);
			a.sendKeys(Keys.TAB);

			getElement(LocatorTypes.xpath,
					GuidingCareObjRepo.PreAuthorization.saveAndNextDiagnosisAndServiceCodes_xpath).click();
			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	public boolean preAuth_returnDueDate() {

		String duedate = null;
		String preAuthID = null;
		String status = null;

		try {
			wait(2);
			getElement(LocatorTypes.xpath, GuidingCareObjRepo.PreAuthorization.saveAndNextFinacialDetails_xpath)
					.click();
			wait(2);
			if (testData.get("PatientType").equals("In Patient")) {
				getElement(LocatorTypes.xpath, GuidingCareObjRepo.PreAuthorization.saveAndNextAdditionalDetails_xpath)
						.click();
			} else if (testData.get("PatientType").equals("Out Patient")) {
				getElement(LocatorTypes.xpath,
						GuidingCareObjRepo.PreAuthorization.saveAndNextAdditionalDetails_outpatient_xpath).click();
			}
			wait(6);
			getElement(LocatorTypes.xpath, GuidingCareObjRepo.PreAuthorization.saveButton_xpath).click();

			wait(5);
			duedate = getElement(LocatorTypes.id, GuidingCareObjRepo.PreAuthorization.dueDate_id).getText();
			preAuthID = getElement(LocatorTypes.id, GuidingCareObjRepo.PreAuthorization.preAuthID_id).getText()
					.toString().substring(1, 10);

			testData.put("PreAuth_ID", preAuthID);

			System.out.println(duedate + "  " + preAuthID);

			getElement(LocatorTypes.id, GuidingCareObjRepo.PreAuthorization.saveAndDecide_id).click();

			elementVisibleWait(LocatorTypes.xpath, GuidingCareObjRepo.PreAuthorization.decisionStatus_xpath, 10);

			status = getElement(LocatorTypes.xpath, GuidingCareObjRepo.PreAuthorization.decisionStatus_xpath).getText();

			insertPreAuthToDB(preAuthID, duedate, status);

			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	public void insertPreAuthToDB(String preAuthID, String dueDate, String status) {
		String query = "UPDATE [dbo].[guidingCare_Preauthorization_Activity]\r\n" + "           SET [PreAuth_ID] = '"
				+ preAuthID + "'\r\n" + "           ,[Status] = '" + status + "'\r\n"
				+ "           ,[PreAuth_DueDate] = '" + dueDate + "' where ID = " + testData.get("ID") + "\r\n";

		db.sqlUpdate(query);

	}

	public void createPreauthForClaims() {
		try {
			boolean flag = false;
			String claims[] = { "Standard", "Expedited", "Continuity of Services" };// Random select from PreAuth Types
			int rnd = new Random().nextInt(claims.length);
			String preAuthType = claims[rnd];
			testData.put("PreAuthType", preAuthType);

			String codes_InPatient[] = { "21", "31", "51", "61" }; // In Patient codes

			boolean inPatient_flag = Arrays.asList(codes_InPatient)
					.contains(testData.get("HealthCareServiceLocationInformation")); // verify if input code is one of
																						// codes_InPatient

			if (testData.get("ClaimType").equals("I") && inPatient_flag) { // InPatient
				navigateToInPatient();
				preAuth_EligibilityVerification("Pre-Service");
				preAuth_ProviderDetails("");
				preAuth_AuthBasicDetails();
			} else { // OutPatient
				navigateToOutPatient();
				preAuth_EligibilityVerification("OutPatient");
				preAuth_ProviderDetails_OutPatient("");
				preAuth_AuthBasicDetails_OutPatient();

			}

			preAuth_DiagnosisAndServicesCodes_Claims();

			flag = preAuth_SaveAndApprove();

			closePreAuth();

			if (flag)
				reportPass("PreAuthorization creation is successful");
			else
				reportFail("PreAuthorization creation failed");
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void closePreAuth() {

		try {
			getElement(LocatorTypes.className, GuidingCareObjRepo.PreAuthorization.preAuthEntryLink_class).click();

			elementVisibleWait(LocatorTypes.xpath,
					GuidingCareObjRepo.PreAuthorization.applicationStatusDropdownArrow_xpath, 10);
			elementClickableWait(LocatorTypes.xpath,
					GuidingCareObjRepo.PreAuthorization.applicationStatusDropdownArrow_xpath, 10);

			WebElement applicationStatusDropdownArrow = getElement(LocatorTypes.xpath,
					GuidingCareObjRepo.PreAuthorization.applicationStatusDropdownArrow_xpath);
			WebElement applicationStatusCurrentText = getElement(LocatorTypes.xpath,
					GuidingCareObjRepo.PreAuthorization.applicationStatusText_xpath);

			selectDropdownValue("Closed", applicationStatusDropdownArrow, applicationStatusCurrentText);

			getElement(LocatorTypes.xpath, GuidingCareObjRepo.PreAuthorization.saveFinal_xpath);
			reportPass("Pre Auth Closed");
		} catch (IOException e) {
			e.printStackTrace();
		} catch (SQLException e) {
			e.printStackTrace();
		}

	}

	private void preAuth_DiagnosisAndServicesCodes_Claims() {
		try {
			Actions a = new Actions(driver);
			List<String> diagnosisCodes = new ArrayList<String>(); // Retrieve diagnosis codes data from list
			diagnosisCodes.add(testData.get("HealthCareCodeInformationPrimary")); // Diagnosis codes from claims table
			if (!testData.get("HealthCareCodeInformationSecondary").isEmpty()
					&& !testData.get("HealthCareCodeInformationSecondary").equals("0")) {
				String diagnosisCodesSecondary[] = testData.get("HealthCareCodeInformationSecondary").split(":");
				for (int i = 0; i < diagnosisCodesSecondary.length; i++) {
					diagnosisCodes.add(diagnosisCodesSecondary[i]);
				}
			}

			for (int diaCode = 0; diaCode < diagnosisCodes.size(); diaCode++) { // enter diagnosis on GuidingCare
																				// application

				if (diaCode != 0) {
					getElement(LocatorTypes.xpath, GuidingCareObjRepo.PreAuthorization.diagnosis_xpath1 + diaCode
							+ GuidingCareObjRepo.PreAuthorization.diagnosisAddButton_xpath2).click();
				}

				wait(1);
				WebElement diagnosis = getElement(LocatorTypes.xpath,
						GuidingCareObjRepo.PreAuthorization.diagnosis_xpath1 + (diaCode + 1)
								+ GuidingCareObjRepo.PreAuthorization.diagnosisCode_xpath2);
				a.moveToElement(diagnosis).click().sendKeys(diagnosis, diagnosisCodes.get(diaCode))
						.sendKeys(Keys.ARROW_DOWN).pause(2000).sendKeys(Keys.ENTER).build().perform();
				wait(1);

			}

			// Procedure codes is from claimsline table
			String query = "SELECT distinct CPTCodeID,UnitOrBasisforMeasurementCode,DateTimeFromPeriod,DateTimeToPeriod FROM [test_data_claimline] where TestCaseId = '"
					+ testData.get("TestCaseID") + "' and UnitOrBasisforMeasurementCode in ('UN','MJ')";

			List<HashMap<String, String>> claimLinelist = db.getListOfHashMapsFromResultSet(query);
			int procCode = 0;
			for (HashMap<String, String> row : claimLinelist) {
				if (procCode != 0) {
					getElement(LocatorTypes.xpath, GuidingCareObjRepo.PreAuthorization.procedure_xpath1 + procCode
							+ GuidingCareObjRepo.PreAuthorization.procedureAddButton_xpath2).click();
				}
				String procCode_modifier[] = row.get("CPTCodeID").split(":"); // CPTCode Format:
																				// HC:CPTCode:Modifier:Modifier
				WebElement procedure = null;

				for (int i = 0; i < procCode_modifier.length; i++) {
					if (i == 0) {
						procedure = getElement(LocatorTypes.xpath, GuidingCareObjRepo.PreAuthorization.procedure_xpath1
								+ (procCode + 1) + GuidingCareObjRepo.PreAuthorization.procedureCode_xpath2);
						a.sendKeys(procedure, procCode_modifier[i]).sendKeys(Keys.ARROW_DOWN).pause(3000)
								.sendKeys(Keys.ENTER).build().perform();
						wait(1);
					}
					if (i == 1) { // Enter modifier
						WebElement modifier = getElement(LocatorTypes.xpath,
								GuidingCareObjRepo.PreAuthorization.procedure_xpath1 + (procCode + 1)
										+ GuidingCareObjRepo.PreAuthorization.prodecureModifier_xpath2);
						a.sendKeys(modifier, procCode_modifier[i]).sendKeys(Keys.ARROW_DOWN).pause(2000)
								.sendKeys(Keys.ENTER).build().perform();
						break;
					}
				}
				WebElement unitTypeDropdown = getElement(LocatorTypes.xpath,
						GuidingCareObjRepo.PreAuthorization.procedure_xpath1 + (procCode + 1)
								+ GuidingCareObjRepo.PreAuthorization.procedureUnitTypeDropdownArrow_xpath);
				WebElement unitTypeCurrentText = getElement(LocatorTypes.xpath,
						GuidingCareObjRepo.PreAuthorization.procedure_xpath1 + (procCode + 1)
								+ GuidingCareObjRepo.PreAuthorization.procedureUnitTypeCurrentText_xpath);
				String unitType_toBeValue = null;

				switch (row.get("UnitOrBasisforMeasurementCode")) {
				case "UN":
					unitType_toBeValue = "Units";
					break;
				case "MJ":
					unitType_toBeValue = "Minutes";
					break;
				}

				selectDropdownValue(unitType_toBeValue, unitTypeDropdown, unitTypeCurrentText);

				getElement(LocatorTypes.xpath,
						GuidingCareObjRepo.PreAuthorization.procedure_xpath1 + (procCode + 1)
								+ GuidingCareObjRepo.PreAuthorization.procedureFromDate_xpath)
										.sendKeys(getDateMMDDYYYYFROMMDDYYYY(row.get("DateTimeFromPeriod")));
				if (row.get("DateTimeToPeriod").isEmpty()) {
					getElement(LocatorTypes.xpath,
							GuidingCareObjRepo.PreAuthorization.procedure_xpath1 + (procCode + 1)
									+ GuidingCareObjRepo.PreAuthorization.procedureToDate_xpath2)
											.sendKeys(getDateAfterDays(15,
													getDateMMDDYYYYFROMMDDYYYY(row.get("DateTimeFromPeriod"))));
				} else {
					getElement(LocatorTypes.xpath,
							GuidingCareObjRepo.PreAuthorization.procedure_xpath1 + (procCode + 1)
									+ GuidingCareObjRepo.PreAuthorization.procedureToDate_xpath2)
											.sendKeys(getDateAfterDays(15,
													getDateMMDDYYYYFROMMDDYYYY(row.get("DateTimeToPeriod"))));
				}
				wait(3);
				getElement(LocatorTypes.xpath, GuidingCareObjRepo.PreAuthorization.procedure_xpath1 + (procCode + 1)
						+ GuidingCareObjRepo.PreAuthorization.requestedDays_xpath2).sendKeys("15");
				procCode++;
			}

			getElement(LocatorTypes.xpath,
					GuidingCareObjRepo.PreAuthorization.saveAndNextDiagnosisAndServiceCodes_xpath).click();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean preAuth_SaveAndApprove() {
		String preAuthID = null;

		try {
			wait(2);
			getElement(LocatorTypes.xpath, GuidingCareObjRepo.PreAuthorization.saveAndNextFinacialDetails_xpath)
					.click();
			wait(2);
			if (isElementPresent(LocatorTypes.xpath,
					GuidingCareObjRepo.PreAuthorization.saveAndNextAdditionalDetails_xpath)) {
				getElement(LocatorTypes.xpath, GuidingCareObjRepo.PreAuthorization.saveAndNextAdditionalDetails_xpath)
						.click();
			} else {
				getElement(LocatorTypes.xpath,
						GuidingCareObjRepo.PreAuthorization.saveAndNextAdditionalDetails_outpatient_xpath).click();
			}
			wait(6);
			getElement(LocatorTypes.xpath, GuidingCareObjRepo.PreAuthorization.saveButton_xpath).click();

			wait(5);
			preAuthID = getElement(LocatorTypes.id, GuidingCareObjRepo.PreAuthorization.preAuthID_id).getText()
					.toString().substring(1, 10);

			testData.put("PreAuth_ID", preAuthID);
			getElement(LocatorTypes.id, GuidingCareObjRepo.PreAuthorization.saveAndDecide_id).click();

			approvePreAuth();

			return true;

		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}

	}

	public void approvePreAuth() {
		try {
			int rowNum = 0;
			while (isElementPresent(LocatorTypes.xpath,
					GuidingCareObjRepo.GuidingCarePreAuthDecisions.decisionStatusDropdown_xpath1 + rowNum
							+ GuidingCareObjRepo.GuidingCarePreAuthDecisions.decisionStatusEdit_xpath2)) {
				getElement(LocatorTypes.xpath,
						GuidingCareObjRepo.GuidingCarePreAuthDecisions.decisionStatusDropdown_xpath1 + rowNum
								+ GuidingCareObjRepo.GuidingCarePreAuthDecisions.decisionStatusEdit_xpath2).click();

				Select statusdropDown = new Select(getElement(LocatorTypes.xpath,
						GuidingCareObjRepo.GuidingCarePreAuthDecisions.decisionStatusDropdown_xpath1 + rowNum
								+ GuidingCareObjRepo.GuidingCarePreAuthDecisions.descisionStatusDropdown_xpath2));
				statusdropDown.selectByVisibleText("Approved");

				Select statusCode = new Select(getElement(LocatorTypes.xpath,
						GuidingCareObjRepo.GuidingCarePreAuthDecisions.decisionStatusDropdown_xpath1 + rowNum
								+ GuidingCareObjRepo.GuidingCarePreAuthDecisions.decisionStatusCodeDropdown_xpath2));
				statusCode.selectByIndex(1);// selectByVisibleText("Approved-Medical Necessity Met");
				elementVisibleWait(
						LocatorTypes.xpath, GuidingCareObjRepo.GuidingCarePreAuthDecisions.decisionStatusDropdown_xpath1
								+ rowNum + GuidingCareObjRepo.GuidingCarePreAuthDecisions.decisionApprovedDays_xpath2,
						10);
				getElement(LocatorTypes.xpath,
						GuidingCareObjRepo.GuidingCarePreAuthDecisions.decisionStatusDropdown_xpath1 + rowNum
								+ GuidingCareObjRepo.GuidingCarePreAuthDecisions.decisionApprovedDays_xpath2)
										.sendKeys("15");

				getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCarePreAuthDecisions.saveButton_xpath).click();

				rowNum++;
			}

			updatePreAuthNumberToClaimsTable();
			reportPass("Pre Auth Approved");
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void updatePreAuthNumberToClaimsTable() {
		try {
			String query = "update [test_data_readytoclaims] set PreAuthNumber='" + testData.get("PreAuth_ID")
					+ "' where testcaseid ='" + testData.get("TestCaseID") + "';";
			db.sqlUpdate(query);
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
