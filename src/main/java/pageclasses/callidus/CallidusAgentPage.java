package pageclasses.callidus;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Set;

import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.Status;

import enums.LocatorTypes;
import managers.PageManager;
import pageclasses.BasePage;
import pageobjects.CallidusObjRepo;
import utils.Dbconn;

public class CallidusAgentPage extends BasePage {

	PageManager pm;

	public CallidusAgentPage() {
		driver = getWebDriver();

	}

	public HashMap<String, String> goToCustTrxnHistoryPage() {

		HashMap<String, String> tempPeriod = new HashMap<String, String>();
		try {
			getElement(LocatorTypes.id, CallidusObjRepo.CallidusCustomerPage.transactionHistoryLink_id).click();
			wait(1);
			getElement(LocatorTypes.id, CallidusObjRepo.CallidusCommon.clearButton_id).click();
			getElement(LocatorTypes.id, CallidusObjRepo.CallidusCommon.searchbutton_id).click();
			wait(1);
			if (isElementPresent(LocatorTypes.xpath, CallidusObjRepo.CallidusCommon.tablerow1_xpath)) {

				Actions a = new Actions(driver);

				a.moveToElement(getElement(LocatorTypes.id, CallidusObjRepo.CallidusAgentPage.sortByPremPerdHeader_id))
						.build().perform();

				wait(1);
				getElement(LocatorTypes.xpath, CallidusObjRepo.CallidusAgentPage.sortByPremPeriodDropdownArrow_xpath)
						.click();

				getElement(LocatorTypes.xpath, CallidusObjRepo.CallidusAgentPage.sortByPremPeriodDescending_xpath)
						.click();
				wait(1);

				tempPeriod.put("premiumPeriod",
						getElement(LocatorTypes.xpath, CallidusObjRepo.CallidusCommon.tablerow1Column2_xpath)
								.getText());
				tempPeriod.put("processPeriod",
						getElement(LocatorTypes.xpath, CallidusObjRepo.CallidusCommon.tablerow1Column3_xpath)
								.getText());
				tempPeriod.put("RecordsPresent", "True");
			} else
				tempPeriod.put("RecordsPresent", "False");
		} catch (Exception e) {
			e.printStackTrace();
		}

		return tempPeriod;
	}

	public HashMap<String, String> returnAgentDetails() {

		HashMap<String, String> tempAgentDetails = new HashMap<String, String>();
		pm = new PageManager();

		try {
			HashMap<String, String> tempPeriod = goToCustTrxnHistoryPage();
			if (tempPeriod.get("RecordsPresent").equals("False")) {
				tempAgentDetails.put("AgentAgencyFlag", "NoRecords");
				return tempAgentDetails;
			}
			tempAgentDetails.put("AgentAgencyFlag", "True");
			getElement(LocatorTypes.name, CallidusObjRepo.CallidusAgentPage.premiumPeriodDropdown_name)
					.sendKeys(tempPeriod.get("premiumPeriod"));
			getElement(LocatorTypes.name, CallidusObjRepo.CallidusAgentPage.processPeriodDropdown_name)
					.sendKeys(tempPeriod.get("processPeriod"));
			(new Select(getElement(LocatorTypes.name, CallidusObjRepo.CallidusAgentPage.BrokerType_name)))
					.selectByVisibleText("Writing Agent");

			getElement(LocatorTypes.id, CallidusObjRepo.CallidusCommon.searchbutton_id).click();

			String parentWindow = driver.getWindowHandle();
			WebDriverWait w = new WebDriverWait(driver, 20);
			wait(2);

			if (!isElementPresent(LocatorTypes.xpath, CallidusObjRepo.CallidusCommon.tablerow1Column5_xpath)) {
				tempAgentDetails.put("Status", "False");
				return tempAgentDetails;
			}
			w.until(ExpectedConditions.elementToBeClickable(
					getElement(LocatorTypes.xpath, CallidusObjRepo.CallidusCommon.tablerow1Column5_xpath)));

			getElement(LocatorTypes.xpath, CallidusObjRepo.CallidusCommon.tablerow1Column5_xpath).click();
			Set<String> winHandles = driver.getWindowHandles();

			for (String winHandle : winHandles) {
				if (!winHandle.equalsIgnoreCase(parentWindow)) {
					driver.switchTo().window(winHandle);
				}
			}

			String[] agentName = getElement(LocatorTypes.xpath, CallidusObjRepo.CallidusAgentPage.name_xpath).getText()
					.split(",");

			tempAgentDetails.put("AgentName", (agentName[1] + " " + agentName[0]).trim());
			tempAgentDetails.put("AgentPhone",
					getElement(LocatorTypes.xpath, CallidusObjRepo.CallidusAgentPage.phone_xpath).getText());
			tempAgentDetails.put("AgentEmail",
					getElement(LocatorTypes.xpath, CallidusObjRepo.CallidusAgentPage.email_xpath).getText()
							.toUpperCase());
			tempAgentDetails.put("AgencyTIN",
					getElement(LocatorTypes.xpath, CallidusObjRepo.CallidusAgentPage.TIN_xpath).getText());

			String[] date = testData.get("effectivemonth").split("-");

			if (Integer.parseInt(date[1]) <= 7) {
				tempAgentDetails.put("Year", tempPeriod.get("premiumPeriod").substring(3));
			} else {
				tempAgentDetails.put("Year",
						String.valueOf((Integer.parseInt(tempPeriod.get("premiumPeriod").substring(3)) + 1)));
			}

			driver.close();
			driver.switchTo().window(parentWindow);

			elementClickableWait(LocatorTypes.xpath, CallidusObjRepo.CallidusCommon.tablerow1_xpath, 10);
			wait(1);
			getElement(LocatorTypes.xpath, CallidusObjRepo.CallidusCommon.tablerow1_xpath).click();

			testData.put("contractType",
					getElement(LocatorTypes.xpath, CallidusObjRepo.CallidusAgentPage.contractType_xpath).getText());

			tempAgentDetails.put("contractType", testData.get("contractType"));
			tempAgentDetails.put("commissionAmount",
					getElement(LocatorTypes.xpath, CallidusObjRepo.CallidusAgentPage.commission_xpath).getText()
							.replace("$", ""));
			tempAgentDetails.put("ProducerType",
					getElement(LocatorTypes.xpath, CallidusObjRepo.CallidusAgentPage.producerType_xpath).getText());

		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return tempAgentDetails;
	}

	public String returncmsCycle() {

		String cmsCycle = null;
		try {
			getElement(LocatorTypes.id, CallidusObjRepo.CallidusCustomerPage.cmsCycleYear_id).click();

			getElement(LocatorTypes.xpath, CallidusObjRepo.CallidusCMSCycleYear.mbi_xpath)
					.sendKeys(testData.get("MEDICAREID"));
			getElement(LocatorTypes.id, CallidusObjRepo.CallidusCommon.searchbutton_id).click();
			wait(1);
			cmsCycle = getElement(LocatorTypes.xpath, CallidusObjRepo.CallidusCommon.tablerow1Column6_xpath).getText();

		} catch (Exception e) {

			e.printStackTrace();
		}

		return cmsCycle;
	}

	public HashMap<String, String> returnAgencyDetails() {

		HashMap<String, String> tempAgencyDetails = new HashMap<String, String>();

		try {
			HashMap<String, String> tempPeriod = goToCustTrxnHistoryPage();

			if (testData.get("contractType").equalsIgnoreCase("Renewal")) {
				tempPeriod.put("premiumPeriod", "01/" + tempPeriod.get("premiumPeriod").substring(3));
				tempPeriod.put("processPeriod", "01/" + tempPeriod.get("processPeriod").substring(3));
			}

			wait(2);
			getElement(LocatorTypes.name, CallidusObjRepo.CallidusAgentPage.premiumPeriodDropdown_name)
					.sendKeys(tempPeriod.get("premiumPeriod"));
			getElement(LocatorTypes.name, CallidusObjRepo.CallidusAgentPage.processPeriodDropdown_name)
					.sendKeys(tempPeriod.get("processPeriod"));
			(new Select(getElement(LocatorTypes.name, CallidusObjRepo.CallidusAgentPage.BrokerType_name)))
					.selectByVisibleText("Hierarchy");

			getElement(LocatorTypes.id, CallidusObjRepo.CallidusCommon.searchbutton_id).click();

			String parentWindow = driver.getWindowHandle();
			WebDriverWait w = new WebDriverWait(driver, 20);
			wait(2);
			w.until(ExpectedConditions.elementToBeClickable(
					getElement(LocatorTypes.xpath, CallidusObjRepo.CallidusCommon.tablerow1Column5_xpath)));

			getElement(LocatorTypes.xpath, CallidusObjRepo.CallidusCommon.tablerow1Column5_xpath).click();
			Set<String> winHandles = driver.getWindowHandles();

			for (String winHandle : winHandles) {
				if (!winHandle.equalsIgnoreCase(parentWindow)) {
					driver.switchTo().window(winHandle);
				}
			}

			tempAgencyDetails.put("AgencyName",
					getElement(LocatorTypes.xpath, CallidusObjRepo.CallidusAgentPage.name_xpath).getText());
			tempAgencyDetails.put("AgencyPhone",
					getElement(LocatorTypes.xpath, CallidusObjRepo.CallidusAgentPage.phone_xpath).getText());
			tempAgencyDetails.put("AgencyEmail",
					getElement(LocatorTypes.xpath, CallidusObjRepo.CallidusAgentPage.email_xpath).getText());
			tempAgencyDetails.put("AgencyTIN",
					getElement(LocatorTypes.xpath, CallidusObjRepo.CallidusAgentPage.TIN_xpath).getText());

			driver.close();
			driver.switchTo().window(parentWindow);

			elementClickableWait(LocatorTypes.xpath, CallidusObjRepo.CallidusCommon.tablerow1_xpath, 10);
			wait(1);
			getElement(LocatorTypes.xpath, CallidusObjRepo.CallidusCommon.tablerow1_xpath).click();

			tempAgencyDetails.put("commissionAmount",
					getElement(LocatorTypes.xpath, CallidusObjRepo.CallidusAgentPage.commission_xpath).getText()
							.replace("$", ""));
		} catch (Exception e1) {
			e1.printStackTrace();
		}
		return tempAgencyDetails;
	}

	public boolean validateAgentAgencyDetails() {

		HashMap<String, String> tmpAgentDetails, tmpAgencyDetails = null;

		boolean flagAgent = false, flagAgency = true;
		boolean flag_agencyValidation = false;
		double[] commAmtDB = new double[2];
		boolean check = false;

		try {
			tmpAgentDetails = returnAgentDetails();

			if (tmpAgentDetails.get("AgentAgencyFlag").equals("NoRecords")) {
				check = checkRecordForBeforeICMImplementation();
				return check;
			}

			if (!(tmpAgentDetails.get("ProducerType").equalsIgnoreCase("Firm")
					|| tmpAgentDetails.get("ProducerType").equalsIgnoreCase("House"))) {
				tmpAgencyDetails = returnAgencyDetails();
				testData.put("AgencyCommission", tmpAgencyDetails.get("commissionAmount"));
				flag_agencyValidation = true;
			}

			commAmtDB = getCommission(tmpAgentDetails);

			flagAgent = validateAgentDetails(tmpAgentDetails, commAmtDB);
			if (flag_agencyValidation) {
				flagAgency = validateAgencyDetails(tmpAgencyDetails, commAmtDB);
			}

			if (flagAgent && flagAgency)
				flagAgent = true;
			else
				flagAgent = false;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return flagAgent;
	}

	public boolean checkRecordForBeforeICMImplementation() {
		String expDate = null;
		boolean flag = false;

		try {
			getWinElement(LocatorTypes.id, CallidusObjRepo.CallidusCustomerPage.producerOwnershipLink_id).click();
			getWinElement(LocatorTypes.id, CallidusObjRepo.CallidusCommon.searchbutton_id).click();

			expDate = getWinElement(LocatorTypes.xpath, CallidusObjRepo.CallidusProducerOwnershipPage.exppirationDate)
					.getText();

			Date dob = new SimpleDateFormat("MM/dd/yyyy").parse(expDate);

			if (dob.before(new SimpleDateFormat("MM/dd/yyyy").parse("03/31/2018"))) {
				flag = true;
			} else {
				reportFail("No Records are present in Callidus for Agent Agency validation for Customer :"
						+ testData.get("SupplementalID"));
				flag = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return flag;

	}

	private double[] getCommission(HashMap<String, String> tmpAgentDetails) {
		HashMap<String, String> inputCommission = new HashMap<String, String>();
		Dbconn dbconn = new Dbconn();
		int cmsCycle = 0;
		double[] commAmtDB = new double[2];

		cmsCycle = getCalculatedCmsCycle(tmpAgentDetails);
		inputCommission.put("Year", tmpAgentDetails.get("Year"));
		inputCommission.put("CmsCycle", Integer.toString(cmsCycle));
		inputCommission.put("contractType", tmpAgentDetails.get("contractType"));
		inputCommission.put("PlanID", testData.get("ContarctNumber"));
		testData.put("AgentCommission", tmpAgentDetails.get("commissionAmount"));
		commAmtDB = dbconn.returnAgentAgencyCommissionDB(inputCommission);

		return commAmtDB;
	}

	private int getCalculatedCmsCycle(HashMap<String, String> tmpAgentDetails) {

		int cmsCycle = 0;

		if (tmpAgentDetails.get("contractType").equalsIgnoreCase("New")) {
			cmsCycle = Integer.parseInt(returncmsCycle());
			if (cmsCycle == 1)
				cmsCycle = 1;
			else if (cmsCycle > 1)
				cmsCycle = 2;
		} else if (tmpAgentDetails.get("contractType").equalsIgnoreCase("Renewal"))
			cmsCycle = 0;
		return 0;
	}

	private boolean validateAgencyDetails(HashMap<String, String> tmpAgencyDetails, double[] commAmtDB) {

		boolean flagAgency = true;

		try {
			if (!testData.get("AgencyName").trim().equalsIgnoreCase(tmpAgencyDetails.get("AgencyName").trim())) {
				test.log(Status.FAIL, "AgencyName " + testData.get("AgencyName") + "is not equal to screen value "
						+ tmpAgencyDetails.get("AgencyName"));
				flagAgency = false;
			}

			if (!testData.get("AgencyPhone").equalsIgnoreCase(tmpAgencyDetails.get("AgencyPhone"))) {
				test.log(Status.FAIL, "AgencyPhone " + testData.get("AgencyPhone") + "is not equal to screen value "
						+ tmpAgencyDetails.get("AgencyPhone"));
				flagAgency = false;
			}

			if (!testData.get("AgencyEmail").trim().equalsIgnoreCase(tmpAgencyDetails.get("AgencyEmail").trim())) {
				test.log(Status.FAIL, "AgencyEmail " + testData.get("AgencyEmail") + "is not equal to screen value "
						+ tmpAgencyDetails.get("AgencyEmail"));
				flagAgency = false;
			}

			if (!testData.get("AgencyTIN").equalsIgnoreCase(tmpAgencyDetails.get("AgencyTIN"))) {
				test.log(Status.FAIL, "AgencyTIN " + testData.get("AgencyTIN") + "is not equal to screen value "
						+ tmpAgencyDetails.get("AgencyTIN"));
				flagAgency = false;
			}

			if (!(commAmtDB[1] == Double.parseDouble(tmpAgencyDetails.get("commissionAmount")))) {
				test.log(Status.FAIL, " Calculated commAmt " + commAmtDB[0] + " is not equal to screen value"
						+ tmpAgencyDetails.get("commissionAmount"));
				flagAgency = false;
			}

			if (flagAgency == true)
				reportPass("Callidus Agency Verified" + tmpAgencyDetails);
			else
				reportFail("Callidus Agency Verification failed DB values" + testData + "On Screen" + tmpAgencyDetails);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return flagAgency;
	}

	private boolean validateAgentDetails(HashMap<String, String> tmpAgentDetails, double[] commAmtDB) {

		boolean flagAgent = true;

		try {
			if (!testData.get("AgentName").trim().equalsIgnoreCase(tmpAgentDetails.get("AgentName").trim())) {
				test.log(Status.FAIL, "AgentName " + testData.get("AgentName") + "is not equal to screen value "
						+ tmpAgentDetails.get("AgentName"));
				flagAgent = false;
			}

			if (!testData.get("AgentPhone").equalsIgnoreCase(tmpAgentDetails.get("AgentPhone"))) {
				test.log(Status.FAIL, "AgentPhone " + testData.get("AgentPhone") + "is not equal to screen value "
						+ tmpAgentDetails.get("AgentPhone"));
				flagAgent = false;
			}

			if (!testData.get("AgentEmail").trim().equalsIgnoreCase(tmpAgentDetails.get("AgentEmail").trim())) {
				test.log(Status.FAIL, "AgentEmail " + testData.get("AgentEmail") + "is not equal to screen value "
						+ tmpAgentDetails.get("AgentEmail"));
				flagAgent = false;
			}

			if (!testData.get("AgentTIN").equalsIgnoreCase(tmpAgentDetails.get("AgentTIN"))) {
				test.log(Status.FAIL, "AgentTIN " + testData.get("AgentTIN") + "is not equal to screen value "
						+ tmpAgentDetails.get("AgentTIN"));
				flagAgent = false;
			}

			if (!(commAmtDB[0] == Double.parseDouble(tmpAgentDetails.get("commissionAmount")))) {
				test.log(Status.FAIL, " Calculated commAmt " + commAmtDB[0] + " is not equal to "
						+ tmpAgentDetails.get("commissionAmount"));
				flagAgent = false;
			}

			if (flagAgent == true)
				reportPass("Callidus Agent Verified" + tmpAgentDetails);
			else
				reportFail("Callidus Agent Verification failed DB values" + testData + "On Screen" + tmpAgentDetails);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return flagAgent;
	}

	public boolean validateAgentAgencyDetails_accessDB() {

		HashMap<String, String> tmpAgentDetails, tmpAgencyDetails = null;

		boolean flagAgent = false, flagAgency = true;
		boolean flag_agencyValidation = false;
		double[] commAmtDB = new double[2];
		boolean check = false;

		try {
			tmpAgentDetails = returnAgentDetails();

			if (tmpAgentDetails.get("AgentAgencyFlag").equals("NoRecords")) {
				check = checkRecordForBeforeICMImplementation();
				return check;
			}

			if (!(tmpAgentDetails.get("ProducerType").equalsIgnoreCase("Firm")
					|| tmpAgentDetails.get("ProducerType").equalsIgnoreCase("House"))) {
				tmpAgencyDetails = returnAgencyDetails();
				testData.put("AgencyCommission", tmpAgencyDetails.get("commissionAmount"));
				flag_agencyValidation = true;
			}

			commAmtDB = getCommission(tmpAgentDetails);

			flagAgent = validateAgentDetails_accessDB(tmpAgentDetails, commAmtDB);
			if (flag_agencyValidation) {
				flagAgency = validateAgencyDetails_accessDB(tmpAgencyDetails, commAmtDB);
			}

			if (flagAgent && flagAgency)
				flagAgent = true;
			else
				flagAgent = false;

		} catch (Exception e) {
			e.printStackTrace();
		}

		return flagAgent;
	}

	private boolean validateAgencyDetails_accessDB(HashMap<String, String> tmpAgencyDetails, double[] commAmtDB) {

		boolean flagAgency = true;

		try {
			if (!testData.get("AgencyName").trim().equalsIgnoreCase(tmpAgencyDetails.get("AgencyName").trim())) {
				test.log(Status.FAIL, "AgencyName " + testData.get("AgencyName") + "is not equal to screen value "
						+ tmpAgencyDetails.get("AgencyName"));
				flagAgency = false;
			}

			if (!testData.get("AgencyTIN").equalsIgnoreCase(tmpAgencyDetails.get("AgencyTIN"))) {
				test.log(Status.FAIL, "AgencyTIN " + testData.get("AgencyTIN") + "is not equal to screen value "
						+ tmpAgencyDetails.get("AgencyTIN"));
				flagAgency = false;
			}

			if (!(commAmtDB[1] == Double.parseDouble(tmpAgencyDetails.get("commissionAmount")))) {
				test.log(Status.FAIL, " Calculated commAmt " + commAmtDB[0] + " is not equal to screen value"
						+ tmpAgencyDetails.get("commissionAmount"));
				flagAgency = false;
			}

			if (flagAgency == true)
				reportPass("Callidus Agency Verified" + tmpAgencyDetails);
			else
				reportFail("Callidus Agency Verification failed DB values" + testData + "On Screen" + tmpAgencyDetails);
		} catch (NumberFormatException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return flagAgency;
	}

	private boolean validateAgentDetails_accessDB(HashMap<String, String> tmpAgentDetails, double[] commAmtDB) {

		boolean flagAgent = true;

		try {
			if (!testData.get("AgentName").trim().equalsIgnoreCase(tmpAgentDetails.get("AgentName").trim())) {
				test.log(Status.FAIL, "AgentName " + testData.get("AgentName") + "is not equal to screen value "
						+ tmpAgentDetails.get("AgentName"));
				flagAgent = false;
			}

			if (!testData.get("AgentTIN").equalsIgnoreCase(tmpAgentDetails.get("AgentTIN"))) {
				test.log(Status.FAIL, "AgentTIN " + testData.get("AgentTIN") + "is not equal to screen value "
						+ tmpAgentDetails.get("AgentTIN"));
				flagAgent = false;
			}

			if (!(commAmtDB[0] == Double.parseDouble(tmpAgentDetails.get("commissionAmount")))) {
				test.log(Status.FAIL, " Calculated commAmt " + commAmtDB[0] + " is not equal to "
						+ tmpAgentDetails.get("commissionAmount"));
				flagAgent = false;
			}

			if (flagAgent == true)
				reportPass("Callidus Agent Verified" + tmpAgentDetails);
			else
				reportFail("Callidus Agent Verification failed DB values" + testData + "On Screen" + tmpAgentDetails);
		} catch (Exception e) {
			e.printStackTrace();
		}

		return flagAgent;
	}

}
