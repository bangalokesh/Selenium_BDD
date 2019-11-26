package pageclasses.m360membership;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.aventstack.extentreports.Status;

import enums.LocatorTypes;
import managers.PageManager;
import pageclasses.BasePage;
import pageobjects.M360MembershipObjRepo;
import utils.Dbconn;

public class M360MAEnrollPage extends BasePage {

	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(M360MAEnrollPage.class.getName());
	Dbconn db = new Dbconn();
	PageManager pm = new PageManager();

	public M360MAEnrollPage() {
		driver = getWebDriver();
	}

	public void navigateMANewMemApl() {
		try {
			getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MAEnroll.maNewMemApl_xpath).click();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("clicking on MA - New Member Appl failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void eligibilityCheck() {
		try {
			String date = testData.get("CoverageDate");
			String lName = testData.get("MemberLName");
			if ((lName.isEmpty() || lName.contains("None"))) {
				getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.enrolDOB_name)
						.sendKeys(getDateInMMDDYYYY(testData.get("DOB").trim()));
			} else {
				getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.enrolLName_name)
						.sendKeys(testData.get("MemberLName").trim());
			}
			getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.medCareId_name)
					.sendKeys(testData.get("MedCareID"));
			if (date.isEmpty() || date.contains("NULL")) {
				getElement(LocatorTypes.id, M360MembershipObjRepo.M360MAEnroll.coverageDate_id)
						.sendKeys(getNextMonth());
			} else {
				getElement(LocatorTypes.id, M360MembershipObjRepo.M360MAEnroll.coverageDate_id)
						.sendKeys(getDateInMMDDYYYY(testData.get("CoverageDate")));
			}
			getElement(LocatorTypes.id, M360MembershipObjRepo.M360MAEnroll.appDate_id).click();
			if (waitForAlert())
				driver.switchTo().alert().accept();
			getElement(LocatorTypes.id, M360MembershipObjRepo.M360MAEnroll.appDate_id)
					.sendKeys(getApplicationDate() + Keys.ENTER);

			reportPass("Eligibility Check successful");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Enter Eligibility Check failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	private String getApplicationDate() {
		if (!testData.get("CoverageDate").isEmpty())
			return getDateBeforeDays(2, getDateInMMDDYYYY(testData.get("CoverageDate")));
		else
			return CurrentDate();
	}

	public void enterPersonalInfo() {
		try {
			getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.zipcode_name).clear();
			getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.zipcode_name)
					.sendKeys(testData.get("ZipCode") + Keys.TAB);
			wait(5);
			String city = getElement(LocatorTypes.id, M360MembershipObjRepo.M360MAEnroll.city_id).getText().trim();

			HashMap<String, String> addressData = db.getResultSet("select * from [dbo].[address_details] "
					+ "where [ZipCode] = '" + testData.get("ZipCode") + "' and [City] = '" + city + "';");

			if (!addressData.get("Address1").isEmpty()) {
				int randomNum = getRandomNumber(101, 1999);
				getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.primaryAdd_name).clear();
				getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.primaryAdd_name)
						.sendKeys(randomNum + " " + addressData.get("Address1"));
			}
			if (!addressData.get("Address2").isEmpty()) {
				getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.primaryAdd2_name).clear();
				getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.primaryAdd2_name)
						.sendKeys(addressData.get("Address2"));
			}
			if (!addressData.get("Address3").isEmpty()) {
				getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.primaryAdd3_name).clear();
				getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.primaryAdd3_name)
						.sendKeys(addressData.get("Address3"));
			}
			if (!addressData.get("HomePhone").isEmpty()) {
				getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.homePhone_name).clear();
				getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.homePhone_name)
						.sendKeys(addressData.get("HomePhone"));
			}
			if (!addressData.get("WorkPhone").isEmpty()) {
				getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.workPhone_name).clear();
				getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.workPhone_name)
						.sendKeys(addressData.get("WorkPhone"));
			}
			if (!addressData.get("CellPhone").isEmpty()) {
				getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.cellPhone_name).clear();
				getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.cellPhone_name)
						.sendKeys(addressData.get("CellPhone"));
			}
			if (!addressData.get("Fax").isEmpty()) {
				getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.faxPhone_name).clear();
				getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.faxPhone_name)
						.sendKeys(addressData.get("Fax"));
			}
			if (!addressData.get("Email").isEmpty()) {
				getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.email_name).clear();
				getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.email_name)
						.sendKeys(addressData.get("Email"));
			}
			String updateAddressID = "update [dbo].[test_data_readytoenroll] set [AddressID] = "
					+ Integer.parseInt(addressData.get("ID")) + " WHERE ID = " + Integer.parseInt(testData.get("ID"));
			db.sqlUpdate(updateAddressID);

			reportPass("Entered Personal info successfully");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Enter Personal Info failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void enterEnrollInfo() {
		try {
			getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MAEnroll.enrollingProdQuestMark_xpath).click();
			String parentHandle = driver.getWindowHandle();
			Set<String> winHandles = driver.getWindowHandles();
			for (String winHandle : winHandles) {
				if (driver.getTitle().contains("Product Search"))
					break;
				else
					driver.switchTo().window(winHandle);
			}
			getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.planID_name)
					.sendKeys(testData.get("ProductPlanID"));
			getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.pbpID_name)
					.sendKeys(testData.get("ProductPBPID"));
			getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.segmentID_name)
					.sendKeys(testData.get("ProductSegmentID"));
			getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.prodSearch_name).click();
			List<WebElement> result = getElements(LocatorTypes.xpath,
					M360MembershipObjRepo.M360MAEnroll.prodSearchResult_xpath);
			result.get(0).click();
			getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.prdSubmit_name).click();
			driver.switchTo().window(parentHandle);
			reportPass("Entered Enroll/Enrolling In info successfully");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Enter Enroll/Enrolling In Info failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void enterPCPDetails() {
		try {
			String planId = testData.get("ProductPlanID");
			String pbpId = testData.get("ProductPBPID");
			String segmentId = testData.get("ProductSegmentID");
			String query = "select PlanType from [dbo].[product_details] " + "where PlanID = '" + planId + " ' "
					+ "and PBPID = '" + pbpId + " ' " + "and PBPSegmentID = '" + segmentId + " ' ";
			List<String> list = db.getListFromQuery(query);
			String planType = list.get(0);
			System.out.println("plantype-" + planType);
			/*** If Plantype is PPO then exit and do not enter PCP Details ***/
			if (planType.equalsIgnoreCase("PPO")) {
				return;
			}

			String pcpnpi = testData.get("PCPNPI").trim();
			StringBuilder zip = new StringBuilder(testData.get("ZipCode").trim());
			StringBuilder zipcode = zip.deleteCharAt(3).deleteCharAt(3);
			String expectedZip = zipcode.toString();
			String expectedPlan = testData.get("ProductPlanID").trim();
			String expectedPBP = testData.get("ProductPBPID").trim();
			String designation = db.selectPlanDetails("Designation", expectedPlan, expectedPBP);

			getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.pcpQuestMark_name).click();
			String parentHandle = driver.getWindowHandle();
			Set<String> winHandles = driver.getWindowHandles();
			for (String winHandle : winHandles) {
				if (driver.getTitle().contains("PCP Search"))
					break;
				else
					driver.switchTo().window(winHandle);
			}
			if (pcpnpi.isEmpty() || pcpnpi.contains("NULL")) {
				HashMap<String, String> pcp = db.selectPCPDetails(expectedZip, designation);
				String npi = pcp.get("NPI");
				getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.searchOfficeCode_name)
						.sendKeys(String.valueOf(npi));
				getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.officeSearch_name).click();
				List<WebElement> result = getElements(LocatorTypes.xpath,
						M360MembershipObjRepo.M360MAEnroll.selectPCP_xpath);
				if (result.size() > 0) {
					result.get(0).click();
					db.updateReadyToEnroll("PCPNPI", npi);
				} else {
					selectPCPFromApplication();
				}
			} else {
				getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.searchOfficeCode_name)
						.sendKeys(pcpnpi);
				getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.officeSearch_name).click();
				List<WebElement> result = getElements(LocatorTypes.xpath,
						M360MembershipObjRepo.M360MAEnroll.selectPCP_xpath);
				if (result.size() > 0) {
					result.get(0).click();
				} else {
					selectPCPFromApplication();
				}
			}
			String npi = getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MAEnroll.npiID_xpath).getText()
					.trim();
			String doctorName = getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MAEnroll.pcpDoctor_xpath)
					.getText().trim();
			db.updateReadyToEnroll("PCPNPI", npi);
			db.updateReadyToEnroll("PCPDoctorName", doctorName);
			getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.pcpSubmit_name).click();
			driver.switchTo().window(parentHandle);
			reportPass("Entered PCP info successfully");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Enter PCP Info failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void enterPCPInfo() {
		try {
			getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.pcpQuestMark_name).click();
			String parentHandle = driver.getWindowHandle();
			Set<String> winHandles = driver.getWindowHandles();
			for (String winHandle : winHandles) {
				if (driver.getTitle().contains("PCP Search"))
					break;
				else
					driver.switchTo().window(winHandle);
			}
			String expectedLocationID = testData.get("PCPLocationID");
			String expectedDoctor = testData.get("PCPDoctorName");
			int tableRows = driver.findElements(By.xpath("//table[@id='data']/tbody/tr")).size();
			for (int i = 1; i <= tableRows; i++) {
				if (driver.findElements(By.xpath("//table[@id='data']/tbody/tr[" + i + "]")).size() != 0
						&& expectedLocationID.contains(
								driver.findElement(By.xpath("//*[@id='data']/tbody/tr[" + i + "]/td[3]")).getText())
						&& expectedDoctor.contains(
								driver.findElement(By.xpath("//*[@id='data']/tbody/tr[" + i + "]/td[4]")).getText())) {
					driver.findElement(By.xpath("//*[@id='data']/tbody/tr[" + i + "]/td[1]/input")).click();
				}
			}
			getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.pcpSubmit_name).click();
			driver.switchTo().window(parentHandle);
			reportPass("Entered PCP info successfully");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Enter PCP Info failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void enterAttestation() {
		try {
			Select s = new Select(
					getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MAEnroll.electionType_xpath));
			s.selectByVisibleText(testData.get("ElectionType"));
			reportPass("Entered Attestation of Eligibility for an Enrollment Period successfully");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Enter Attestation of Eligibility for an Enrollment Period failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void appSignature() {
		try {

			getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.appReceivedDate_name)
					.sendKeys(getApplicationDate() + Keys.TAB);
			getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.appSign_name)
					.sendKeys(getApplicationDate() + Keys.TAB);
			reportPass("Entered Application Signature and Authorized Representative successfully");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Enter Application Signature and Authorized Representative failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void enterBrokerDetails() {
		try {
			getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.brokerCommAgency_name).click();
			String parentHandle = driver.getWindowHandle();
			Set<String> winHandles = driver.getWindowHandles();
			for (String winHandle : winHandles) {
				if (driver.getTitle().contains("Agency Search"))
					break;
				else
					driver.switchTo().window(winHandle);
			}
			if (!testData.get("AgencyID").isEmpty() || !testData.get("AgentID").isEmpty()) {
				getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.agencyID_name)
						.sendKeys(testData.get("AgencyID"));
				getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.agencySearch_name).click();
				List<WebElement> result = getElements(LocatorTypes.xpath,
						M360MembershipObjRepo.M360MAEnroll.agencySearchResult_xpath);
				if (result.size() > 0) {
					result.get(0).click();
				} else {
					String agentID = selectDefaultAgencyAndAgent();
					testData.replace("AgentID", agentID);
					db.updateReadyToEnroll("AgentID", agentID);
				}
			} else {
				String agentID = selectDefaultAgencyAndAgent();
				testData.replace("AgentID", agentID);
				db.updateReadyToEnroll("AgentID", agentID);
			}
			getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.brokerCommAgencySubmit_name).click();
			driver.switchTo().window(parentHandle);
			WebElement e = getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MAEnroll.brokerAgentType_xpath);
			Select s = new Select(e);
			s.selectByVisibleText(testData.get("AgentType").toUpperCase());
			getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.brokerDate_name)
					.sendKeys(getApplicationDate() + Keys.TAB);
			reportPass("Entered Broker details successfully");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Enter Broker Info failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void validateApplication() {
		try {
			getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MAEnroll.validateApplication_xpath).click();
			driver.switchTo().alert().accept();
			reportPass("Validate application successfully");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Validate application failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void updateApplication() {
		try {
			getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MAEnroll.updateApplication_xpath).click();
			driver.switchTo().alert().accept();
			forceApprove();
			reportPass("Update application successful");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Update application failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void processEnrollOnline() {
		try {
			String enrollType = testData.get("EnrollmentType");
			String applicationType = testData.get("ApplicationType");
			String applicationTypeUI = "";
			if (enrollType.equalsIgnoreCase("Online") && applicationType.equalsIgnoreCase("NMA")) {
				navigateMANewMemApl();
				eligibilityCheck();
				enterPersonalInfo();
				applicationTypeUI = getElement(LocatorTypes.xpath,
						M360MembershipObjRepo.M360MAEnroll.applicationType_xpath).getText();
				if (applicationTypeUI.contains("NMA")) {
					enterEnrollInfo();
				} else {
					cmaEnterEnrollInfo();
				}
				enterPCPDetails();
				enterAttestation();
				appSignature();
				enterBrokerDetails();
				boolean match = enterAgencyAndAgentID();
				if (match) {
					validateApplication();
					updateApplication();
					captureAfterEnrollmentDetails();
					reportPass("Process Enroll successfully");
				}
			}
		} catch (Exception e) {
			if (getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MemberPage.pageCannotDisplay_xpath).getText()
					.equalsIgnoreCase("The page cannot be displayed because an internal server error has occurred.")
					|| getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MemberPage.unExpectedResponse).getText()
							.equals("Unexpected Response")
					|| getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MemberPage.unExpectedResponse).getText()
							.contains("The following session is not valid")) {
				driver.quit();
			}
			e.printStackTrace();
			try {
				reportFail("Process Enroll failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void captureAfterEnrollmentDetails() {
		try {
			String prodName = null;
			String appDate = getElement(LocatorTypes.id, M360MembershipObjRepo.M360MAEnroll.appDate_id)
					.getAttribute("value").trim();
			String covDate = getElement(LocatorTypes.id, M360MembershipObjRepo.M360MAEnroll.coverageDate_id)
					.getAttribute("value").trim();
			String appID = getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.applicationID_name)
					.getAttribute("value").trim();
			String appStatus = getElement(LocatorTypes.xpath,
					M360MembershipObjRepo.M360MAEnroll.applicationStatus_xpath).getText().trim();
			String applicationType = getElement(LocatorTypes.xpath,
					M360MembershipObjRepo.M360MAEnroll.applicationType_xpath).getText().trim();
			if (applicationType.equalsIgnoreCase("NMA")) {
				prodName = getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MAEnroll.productName_xpath)
						.getAttribute("value").trim();
			} else if (applicationType.equalsIgnoreCase("CMA")) {
				prodName = getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MAEnroll.cmaProductName_xpath)
						.getAttribute("value").trim();
			}

			String hicNum = getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.HICNum_name)
					.getAttribute("value").trim();
			String DOB = getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.enrolDOB_name)
					.getAttribute("value").trim();
			String ssn = getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.ssn_name)
					.getAttribute("value").trim();
			String memberID = getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.memberID_name)
					.getAttribute("value").trim();
			String gender = getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MAEnroll.gender_xpath).getText()
					.trim();
			String paymentOption = getElement(LocatorTypes.xpath,
					M360MembershipObjRepo.M360MAEnroll.paymentOption_xpath).getText().trim();
			String lisStartDate = getElement(LocatorTypes.id, M360MembershipObjRepo.M360MAEnroll.lisStartDate_id)
					.getAttribute("value").trim();
			String lisEndDate = getElement(LocatorTypes.id, M360MembershipObjRepo.M360MAEnroll.lisEndDate_id)
					.getAttribute("value").trim();
			String lisCopay = getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MAEnroll.lisCopay_xpath)
					.getText().trim();
			String lisPercent = getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MAEnroll.lisPercent_xpath)
					.getText().trim();
			db.updateReadyToEnroll("ApplicationDate", appDate);
			db.updateReadyToEnroll("CoverageDate", covDate);
			db.updateReadyToEnroll("ApplicationID", appID);
			db.updateReadyToEnroll("ApplicationStatus", appStatus);
			db.updateReadyToEnroll("ProductPlanName", prodName);
			db.updateReadyToEnroll("HICNumber", hicNum);
			db.updateReadyToEnroll("DOB", DOB);
			db.updateReadyToEnroll("MemberSSN", ssn);
			db.updateReadyToEnroll("MemberID", memberID);
			db.updateReadyToEnroll("Gender", gender);
			db.updateReadyToEnroll("MAPDPaymentOption", paymentOption);
			db.updateReadyToEnroll("LISStartDate", lisStartDate);
			db.updateReadyToEnroll("LISEndDate", lisEndDate);
			db.updateReadyToEnroll("LISCopay", lisCopay);
			db.updateReadyToEnroll("LISPercent", lisPercent);
			testData.put("ApplicationDate", appDate);
			testData.put("CoverageDate", covDate);
			testData.put("ApplicationID", appID);
			testData.put("ApplicationStatus", appStatus);
			testData.put("ProductPlanName", prodName);
			testData.put("ApplicationDate", hicNum);
			testData.put("DOB", DOB);
			testData.put("ApplicationStatus", ssn);
			testData.put("ProductPlanName", memberID);
			testData.put("ApplicationStatus", gender);
			testData.put("MAPDPaymentOption", paymentOption);
			testData.put("LISStartDate", lisStartDate);
			testData.put("LISEndDate", lisEndDate);
			testData.put("LISCopay", lisCopay);
			testData.put("LISPercent", lisPercent);
			reportPass("Captured after Enrollment details successfully");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Capture after Enrollment details failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public String getApplicationStatusByAppID() {
		try {
			navigateMANewMemApl();
			getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MAEnroll.appIDSearch)
					.sendKeys(testData.get("ApplicationID"));
			getElement(LocatorTypes.id, M360MembershipObjRepo.M360MAEnroll.goButton).click();
			String status = getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.currentStatus_name)
					.getAttribute("value");
			return status;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void forceApprove() {
		try {
			String status = getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.currentStatus_name)
					.getAttribute("value");
			if (status.equalsIgnoreCase("ELGWARNING")) {
				WebElement e = getElement(LocatorTypes.xpath,
						M360MembershipObjRepo.M360MAEnroll.applicationStatus_select);
				Select s = new Select(e);
				s.selectByValue("FORCEDAPPR");
				getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MAEnroll.updateApplication_xpath).click();
				driver.switchTo().alert().accept();
			}
			reportPass("Force Approve successful");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Force Approve failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void selectPCPFromApplication() {
		try {
			getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.pcpReset_name).click();
			getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.officeSearch_name).click();
			getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MAEnroll.npiFirstRadioBtn_xpath).click();
			String npi = getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MAEnroll.npiFirstRadioBtn_xpath)
					.getText();
			db.updateReadyToEnroll("PCPNPI", npi);
			reportPass("Selected first available PCP successfully");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("First available PCP selection failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void navigateMAContractChange() {
		try {
			getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360CMA.maContractChange_xpath).click();
			reportPass("Clicked on MA - Contract Chg Form successfully");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Clicking on MA - Contract Chg Form failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	// Priya
	public void cmaEnterEnrollInfo() {
		try {
			getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360CMA.CMAenrollingProdQuestMark_xpath).click();
			String parentHandle = driver.getWindowHandle();
			Set<String> winHandles = driver.getWindowHandles();
			for (String winHandle : winHandles) {
				if (driver.getTitle().contains("Product Search"))
					break;
				else
					driver.switchTo().window(winHandle);
			}
			getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.planID_name)
					.sendKeys(testData.get("ProductPlanID").trim());
			getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.pbpID_name)
					.sendKeys(testData.get("ProductPBPID").trim());
			getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.segmentID_name)
					.sendKeys(testData.get("ProductSegmentID").trim());
			getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.prodSearch_name).click();
			List<WebElement> result = getElements(LocatorTypes.xpath,
					M360MembershipObjRepo.M360MAEnroll.prodSearchResult_xpath);
			result.get(0).click();
			getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.prdSubmit_name).click();
			driver.switchTo().window(parentHandle);
			reportPass("CMA - Entered Enroll/Enrolling In info successfully");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("CMA - Enter Enroll/Enrolling In Info failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	/*
	 * public void processCMAOnline() { try { String enrollType =
	 * testData.get("EnrollmentType"); String applicationType =
	 * testData.get("ApplicationType"); if (enrollType.equalsIgnoreCase("Online") &&
	 * applicationType.equalsIgnoreCase("CMA")) { navigateMAContractChange();
	 * eligibilityCheck(); enterPersonalInfo(); cmaEnterEnrollInfo();
	 * enterPCPDetails(); enterAttestation(); appSignature(); boolean match =
	 * enterAgencyAndAgentID(); if (match) { validateApplication();
	 * updateApplication(); captureAfterEnrollmentDetails();
	 * reportPass("Process Enroll successfully"); } } } catch (Exception e) {
	 * e.printStackTrace(); try { reportFail("Process Change failed"); } catch
	 * (IOException e1) { e1.printStackTrace(); } } }
	 */

	public String selectDefaultAgencyAndAgent() {
		String agentID = null;
		try {
			HashMap<String, String> agentDetails = db.getResultSet(
					"select APM.AgentTIN,APM.AgencyTIN,APM.PRoductID,PD.PlanID,PD.PBPID from test_data_readytoenroll RE "
							+ "inner join  product_details PD on RE.ProductPlanID = PD.PlanID and RE.ProductPBPID = PD.PBPID "
							+ "inner join agency_product_mapping APM on APM.ProductID = PD.PRoductID "
							+ "where RE.MedCareID = '" + testData.get("MedCareID") + "'");
			String agencyID = agentDetails.get("AgencyTIN");
			agentID = agentDetails.get("AgentTIN");
			getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.agencyID_name).clear();
			getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.agencyID_name).sendKeys(agencyID);
			getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.agencySearch_name).click();
			List<WebElement> result = getElements(LocatorTypes.xpath,
					M360MembershipObjRepo.M360MAEnroll.agencySearchResult_xpath);
			if (result.size() > 0) {
				result.get(0).click();
				db.updateReadyToEnroll("AgencyID", agencyID);
			}

			reportPass("Agency selected successfully");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Agency selection failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return agentID;
	}

	public boolean enterAgencyAndAgentID() {
		boolean match = false;
		try {

			List<WebElement> help = getElements(LocatorTypes.name,
					M360MembershipObjRepo.M360MAEnroll.brokerCommAgency_name);
			System.out.println(help.size());
			help.get(1).click();
			String parentHandle = driver.getWindowHandle();
			Set<String> winHandles = driver.getWindowHandles();
			for (String winHandle : winHandles) {
				if (driver.getTitle().contains("Agency Search"))
					break;
				else
					driver.switchTo().window(winHandle);
			}
			getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.agencyID_name)
					.sendKeys(testData.get("AgencyID"));
			getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.agentField_name)
					.sendKeys(testData.get("AgentID"));
			WebElement e = getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.agentType_name);
			Select s = new Select(e);
			s.selectByVisibleText(testData.get("AgentType").toUpperCase());
			getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.agencySearch_name).click();
			List<WebElement> result = getElements(LocatorTypes.xpath,
					M360MembershipObjRepo.M360MAEnroll.agencySearchResult_xpath);
			if (result.size() > 0) {
				result.get(0).click();
				getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.brokerCommAgencySubmit_name).click();
			} else {
				driver.close();
			}
			driver.switchTo().window(parentHandle);
			if (isAlertPresent())
				driver.switchTo().alert().accept();
			getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.brokerDate_name)
					.sendKeys(CurrentDate() + Keys.ENTER);
			match = validateAgentAndAgencyDetails();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Agency and Agent combination returned no results. enterAgencyAndAgentID - failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return match;
	}

	public boolean validateAgentAndAgencyDetails() {
		boolean match = false;
		try {
			System.out.println(testData.get("AgencyID"));
			System.out.println(testData.get("AgentID"));
			String agencyID = getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.agencyIDCheck_name)
					.getAttribute("value").trim();
			String agentID = getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MAEnroll.agentIDCheck_xpath)
					.getAttribute("value").trim();
			if (agencyID.contains(testData.get("AgencyID").trim())
					&& agentID.contains(testData.get("AgentID").trim())) {
				match = true;
				reportPass("Agency and Agent selected successfully");
			} else {
				if (!agencyID.contains(testData.get("AgencyID"))) {
					enterAgencyDetails();
				}
				if (!agentID.contains(testData.get("AgentID"))) {
					enterAgentDetails();
				}
			}
			agencyID = getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.agencyIDCheck_name)
					.getAttribute("value").trim();
			agentID = getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MAEnroll.agentIDCheck_xpath)
					.getAttribute("value").trim();
			if (agencyID.contains(testData.get("AgencyID").trim())
					&& agentID.contains(testData.get("AgentID").trim())) {
				match = true;
				reportPass("Agency and Agent selected successfully");
			} else {
				match = false;
				reportFail("Agency and Agent match failed");
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				match = false;
				reportFail("Agency selection failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return match;
	}

	public void enterAgencyDetails() {
		try {
			elementClickableWait(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.brokerCommAgency_name, 15);
			getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.brokerCommAgency_name).click();
			String parentHandle = driver.getWindowHandle();
			Set<String> winHandles = driver.getWindowHandles();
			for (String winHandle : winHandles) {
				if (driver.getTitle().contains("Agency Search"))
					break;
				else
					driver.switchTo().window(winHandle);
			}
			getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.agencyID_name)
					.sendKeys(testData.get("AgencyID"));
			getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.agencySearch_name).click();
			List<WebElement> result = getElements(LocatorTypes.xpath,
					M360MembershipObjRepo.M360MAEnroll.agencySearchResult_xpath);
			if (result.size() > 0)
				result.get(0).click();
			getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.brokerCommAgencySubmit_name).click();
			driver.switchTo().window(parentHandle);
			if (isAlertPresent())
				driver.switchTo().alert().accept();
			reportPass("Agency selected successfully");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Agency selection failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void enterAgentDetails() {
		try {
			wait(2);
			getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MAEnroll.brokerAgent_xpath).click();
			getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MAEnroll.searchAgentID_xpath)
					.sendKeys(testData.get("AgentID") + Keys.ENTER);
			wait(1);
			if (isAlertPresent())
				driver.switchTo().alert().accept();
			reportPass("Agent selection Passed");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Agent selection failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void eligibilityCheckForMember() {
		try {
			HashMap<String, String> memberData = db.getResultSet("select top 1 * from [dbo].[test_data_readytoenroll] "
					+ "where RunMode = 'Y' and CMSEnrollmentStatus = 'CMSAPPRV';");

			String date = memberData.get("CoverageDate");

			getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.enrolLName_name)
					.sendKeys(memberData.get("MemberLName").trim());

			getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.medCareId_name)
					.sendKeys(memberData.get("MedCareID"));

			if (date.isEmpty() || date.contains("NULL")) {
				getElement(LocatorTypes.id, M360MembershipObjRepo.M360MAEnroll.coverageDate_id)
						.sendKeys(getNextMonth());
			} else {
				getElement(LocatorTypes.id, M360MembershipObjRepo.M360MAEnroll.coverageDate_id)
						.sendKeys(getDateInMMDDYYYY(memberData.get("CoverageDate")));
			}

			getElement(LocatorTypes.id, M360MembershipObjRepo.M360MAEnroll.appDate_id).click();

			if (waitForAlert())
				driver.switchTo().alert().accept();
			getElement(LocatorTypes.id, M360MembershipObjRepo.M360MAEnroll.appDate_id)
					.sendKeys(CurrentDate() + Keys.ENTER);

			getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.zipcode_name).clear();
			getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.zipcode_name)
					.sendKeys(memberData.get("ZipCode") + Keys.TAB);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void retrievePlanInformation(String planID, String pbpID, String segment) {
		try {
			getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360CMA.CMAenrollingProdQuestMark_xpath).click();
			String parentHandle = driver.getWindowHandle();
			Set<String> winHandles = driver.getWindowHandles();
			for (String winHandle : winHandles) {
				if (driver.getTitle().contains("Product Search"))
					break;
				else
					driver.switchTo().window(winHandle);
			}
			getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.pcpReset_name).click();

			getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.planID_name).sendKeys(planID);
			getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.pbpID_name).sendKeys(pbpID);
			getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.segmentID_name).sendKeys(segment);

			getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.prodSearch_name).click();
			List<WebElement> result = getElements(LocatorTypes.xpath,
					M360MembershipObjRepo.M360MAEnroll.prodSearchResult_xpath);

			result.get(0).click();

			getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.prdSubmit_name).click();
			driver.switchTo().window(parentHandle);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void openPCPWindow() {
		getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.pcpQuestMark_name).click();
		Set<String> winHandles = driver.getWindowHandles();
		for (String winHandle : winHandles) {
			if (driver.getTitle().contains("PCP Search"))
				break;
			else
				driver.switchTo().window(winHandle);
		}
	}

	public void validate_PGR_M360_PCP_Data(Map<String, String> dbValues) {
		try {
			String officeCode = "";
			String doctorName = "";
			String address = "";
			String city = "";
			String state = "";
			String zip = "";

			getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.pcpReset_name).click();
			getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.searchOfficeCode_name).clear();
			getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.searchOfficeCode_name)
					.sendKeys(dbValues.get("NPI"));

			getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.LocationId).clear();
			getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.LocationId)
					.sendKeys(dbValues.get("ProvLoc"));

			getElement(LocatorTypes.name, M360MembershipObjRepo.M360MAEnroll.officeSearch_name).click();

			int tableRows = driver.findElements(By.xpath("//table[@id='data']/tbody/tr")).size();
			if (tableRows < 1) {
				test.log(Status.FAIL, "Unable to find NPI " + dbValues.get("NPI") + " on the M360 Website"
						+ " and Location: " + dbValues.get("ProvLoc"));
				db.updateDBTestData("UPDATE [VelocityTestAutomation].[dbo].[pgr_m360_data]\r\n"
						+ "SET STATUS = 'NPI Location Not Found' \r\n" + "WHERE \r\n" + "ID = "
						+ Integer.parseInt(dbValues.get("ID")));

				return;
			}

			HashMap<String, String> M360_PCP = new LinkedHashMap<String, String>();
			for (int i = 1; i <= tableRows; i++) {
				officeCode = driver.findElement(By.xpath("//*[@id='data']/tbody/tr[" + i + "]/td[2]")).getText();
				doctorName = driver.findElement(By.xpath("//*[@id='data']/tbody/tr[" + i + "]/td[4]")).getText();
				address = driver.findElement(By.xpath("//*[@id='data']/tbody/tr[" + i + "]/td[6]")).getText();
				city = driver.findElement(By.xpath("//*[@id='data']/tbody/tr[" + i + "]/td[7]")).getText();
				state = driver.findElement(By.xpath("//*[@id='data']/tbody/tr[" + i + "]/td[8]")).getText();
				zip = driver.findElement(By.xpath("//*[@id='data']/tbody/tr[" + i + "]/td[9]")).getText();

				M360_PCP.put("officeCode", officeCode.trim());
				M360_PCP.put("doctorName", doctorName.trim());
				M360_PCP.put("address", address.trim());
				M360_PCP.put("city", city.trim());
				M360_PCP.put("state", state.trim());
				M360_PCP.put("zip", zip.trim());

				HashMap<String, String> PGR_Data = new HashMap<String, String>();
				PGR_Data.put("officeCode", dbValues.get("NPI").trim());
				PGR_Data.put("doctorName", dbValues.get("FIRST_NAME").trim() + " " + dbValues.get("LAST_NAME").trim());

				if (!dbValues.get("SUITE").trim().isEmpty()) {
					PGR_Data.put("address", dbValues.get("ADDRESS").trim() + " " + dbValues.get("SUITE").trim());
				} else {
					PGR_Data.put("address", dbValues.get("ADDRESS").trim());
				}

				PGR_Data.put("city", dbValues.get("CITY").trim());
				PGR_Data.put("state", dbValues.get("ST").trim());
				PGR_Data.put("zip", dbValues.get("ZIP").trim());

				if (compareHashMaps(PGR_Data, M360_PCP)) {
					reportPass("NPI Matched: - " + PGR_Data);
					db.updateDBTestData(
							"UPDATE [VelocityTestAutomation].[dbo].[pgr_m360_data]\r\n" + "SET STATUS = 'PASSED' \r\n"
									+ "WHERE \r\n" + "ID = " + Integer.parseInt(dbValues.get("ID")));
				} else {
					reportFail("NPI Not Matched: - Database NP = " + PGR_Data + "\n Screen NPI = " + M360_PCP);
					db.updateDBTestData(
							"UPDATE [VelocityTestAutomation].[dbo].[pgr_m360_data]\r\n" + "SET STATUS = 'FAILED' \r\n"
									+ "WHERE \r\n" + "ID = " + Integer.parseInt(dbValues.get("ID")));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			if (getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MemberPage.pageCannotDisplay_xpath).getText()
					.equalsIgnoreCase("The page cannot be displayed because an internal server error has occurred.")
					|| getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MemberPage.unExpectedResponse).getText()
							.equals("Unexpected Response")
					|| getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MemberPage.unExpectedResponse).getText()
							.contains("The following session is not valid")) {
				driver.quit();
			}
		}
	}
}