package pageclasses.m360billing;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import com.aventstack.extentreports.Status;

import enums.LocatorTypes;
import managers.PageManager;
import pageclasses.BasePage;
import pageobjects.M360BillingObjRepo;
import utils.Dbconn;

public class M360BillingInvoicePage extends BasePage {
	// private static DecimalFormat df = new DecimalFormat("00.00");
	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(M360BillingInvoicePage.class.getName());
	PageManager pm = new PageManager();
	Dbconn db = new Dbconn();

	public M360BillingInvoicePage() {
		driver = getWebDriver();
	}

	public void navigateToInvoiceTab() {
		try {
			getElement(LocatorTypes.xpath, M360BillingObjRepo.M360BillingInvoice.invoicesTab_xpath).click();

		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("M360MemberPage navigateToInvoiceTab failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public boolean searchMedicareId() {
		boolean flag = true;
		try {
			getElement(LocatorTypes.name, M360BillingObjRepo.M360BillingInvoice.BillResetSearch_name).click();
			getElement(LocatorTypes.xpath, M360BillingObjRepo.M360BillingInvoice.medicareid_xpath)
					.sendKeys(testData.get("MedCareID").trim());
			getElement(LocatorTypes.id, M360BillingObjRepo.M360BillingInvoice.goButton_id).click();
			reportPass("M360MemberPage getMemberDetails Passed");
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			try {
				reportFail("M360MemberPage getMemberDetails failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}

	public void getInvoiceList() {
		try {
			List<WebElement> invoiceList = getElements(LocatorTypes.xpath,
					M360BillingObjRepo.M360BillingInvoice.invoicelist_xpath);
			if (invoiceList.size() < 3) {
				reportFail("No invoice found for this member");
			} else {
				for (int i = 2; i < invoiceList.size(); i++) {
					Actions a = new Actions(driver);
					a.doubleClick(getElement(LocatorTypes.xpath,
							M360BillingObjRepo.M360BillingInvoice.invoicelist_xpath + "[" + i + "]/td[1]")).build()
							.perform();
					getBillingInvoiceDetails();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("No more invoices");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

	}

	public HashMap<String, String> getBillingInvoiceDetails() {
		HashMap<String, String> testDataMap = new HashMap<String, String>();
		try {
			testDataMap.put("memberId",
					getElement(LocatorTypes.xpath, M360BillingObjRepo.M360BillingInvoice.memberId_xpath).getText());
			testDataMap.put("medicareId",
					getElement(LocatorTypes.xpath, M360BillingObjRepo.M360BillingInvoice.medicareidValue_xpath)
							.getText());
			testDataMap.put("membername",
					getElement(LocatorTypes.xpath, M360BillingObjRepo.M360BillingInvoice.memberName_xpath).getText());

			String[] memName = testDataMap.get("membername").split(" ");
			testDataMap.put("memLastName", memName[1]);
			boolean flag = true;
			if (!testDataMap.get("medicareId").trim().equalsIgnoreCase(testData.get("MedCareID").trim())) {
				test.log(Status.FAIL, testDataMap.get("medicareId") + "is not equal to " + testData.get("MedicareID"));
				flag = false;
			}

			if (!testDataMap.get("memLastName").trim().equalsIgnoreCase(testData.get("MemberLName").trim())) {
				test.log(Status.FAIL,
						testDataMap.get("memLastName") + "is not equal to " + testData.get("MemberLName"));
				flag = false;
			}

			if (!testDataMap.get("memberId").trim().equalsIgnoreCase(testData.get("MemberID").trim())) {
				test.log(Status.FAIL, testDataMap.get("memberId") + "is not equal to " + testData.get("MemberID"));
				flag = false;
			}

			if (flag) {
				reportPass("Invoice details Verified " + testDataMap);
			} else {

				reportFail("Invoice details Not Matched, screen data: " + testDataMap
						+ " did not match invoice data in tables: " + testData);
			}

			testDataMap.put("memberTotalDue",
					getElement(LocatorTypes.xpath, M360BillingObjRepo.M360BillingInvoice.memberTotalDue_xpath)
							.getText());
			testDataMap.put("invoiceNbr",
					getElement(LocatorTypes.xpath, M360BillingObjRepo.M360BillingInvoice.invoiceNbr_xpath).getText());
			testDataMap.put("invoiceType",
					getElement(LocatorTypes.xpath, M360BillingObjRepo.M360BillingInvoice.invoiceType_xpath).getText());
			testDataMap.put("invoiceDueDate",
					getElement(LocatorTypes.xpath, M360BillingObjRepo.M360BillingInvoice.invoiceDueDate_xpath)
							.getText());

			testDataMap.put("invoiceBillThruDate",
					getElement(LocatorTypes.xpath, M360BillingObjRepo.M360BillingInvoice.invoiceBillThruDate_xpath)
							.getText());
			testDataMap.put("invoiceStatus",
					getElement(LocatorTypes.xpath, M360BillingObjRepo.M360BillingInvoice.invoiceStatus_xpath)
							.getText());
			testDataMap.put("invoiceAmt",
					getElement(LocatorTypes.xpath, M360BillingObjRepo.M360BillingInvoice.invoiceAmt_xpath).getText());
			testDataMap.put("invoiceAdjustmentAmt",
					getElement(LocatorTypes.xpath, M360BillingObjRepo.M360BillingInvoice.invoiceAdjustmentAmt_xpath)
							.getText());
			testDataMap.put("invoicePaymentAmt",
					getElement(LocatorTypes.xpath, M360BillingObjRepo.M360BillingInvoice.invoicePaymentAmt_xpath)
							.getText());
			testDataMap.put("invoiceFinalAmt",
					getElement(LocatorTypes.xpath, M360BillingObjRepo.M360BillingInvoice.invoiceFinalAmt_xpath)
							.getText());
			testDataMap.put("invoiceFrequency",
					getElement(LocatorTypes.xpath, M360BillingObjRepo.M360BillingInvoice.invoiceFrequency_xpath)
							.getText());
			logger.info(testDataMap);

			String squery = "Select [MedicareID] from [dbo].[test_data_readytobill] where [MedicareID] = '"
					+ testData.get("MedCareID").trim() + "' AND InvoiceNumber = '"
					+ Integer.parseInt(testDataMap.get("invoiceNbr").trim()) + "';";
			if (db.isResultSetEmpty(squery) == false) {
				String insertQuery = "INSERT INTO VelocityTestAutomation.dbo.test_data_readytobill\r\n"
						+ "([TCID],[DriverType],[MedicareID],[MemberID],[SupplementalID],[MemberLName],[InvoiceNumber], [InvoiceStatus] ,[InvoiceAmount], [TotalDue], [InvoiceDueDate], [PaymentAmount], [InvoiceType], [AdjustmentAmount], [Frequency], [BillThruDate])\r\n"
						+ "VALUES (" + testData.get("TCID").trim() + ", 'CHROME', '"
						+ testDataMap.get("medicareId").trim() + "', '" + testDataMap.get("memberId").trim() + "', '"
						+ testData.get("SupplementalID").trim() + "', '" + testDataMap.get("memLastName").trim() + "', "
						+ Integer.parseInt(testDataMap.get("invoiceNbr").trim()) + ", '"
						+ testDataMap.get("invoiceStatus").trim() + "', '" + testDataMap.get("invoiceAmt").trim()
						+ "', '" + testDataMap.get("memberTotalDue").trim() + "', '"
						+ getDateMMDDYYYYInSqlFormat(testDataMap.get("invoiceDueDate").trim()) + "', '"
						+ testDataMap.get("invoicePaymentAmt").trim() + "', '"
						+ testDataMap.get("invoiceType").trim().substring(0, 2) + "', '"
						+ testDataMap.get("invoiceAdjustmentAmt").trim() + "', '"
						+ testDataMap.get("invoiceFrequency").trim() + "', '"
						+ getDateMMDDYYYYInSqlFormat(testDataMap.get("invoiceBillThruDate").trim()) + "');";
				db.sqlUpdate(insertQuery);
			}

		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("M360BillingInvoicePage getBillingInvoiceDetails failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return testDataMap;
	}

	public boolean validateInvoiceHeaderDetails(boolean dateCheck) {
		boolean flag = false;
		boolean flag1 = false;
		boolean flag2 = false;
		String invoiceAmountDB;
		double finalAmt = 0;
		try {
			String invoiceAmount = getElement(LocatorTypes.xpath,
					M360BillingObjRepo.M360BillingInvoice.invoiceAmt_xpath).getText();
			if (dateCheck) {
				invoiceAmountDB = testData.get("2019TotalPremium");
			} else {
				invoiceAmountDB = testData.get("2020TotalPremium");
			}
			if (invoiceAmount.trim().equals(invoiceAmountDB.trim())) {
				test.log(Status.PASS, invoiceAmount + "is equal to DB value " + invoiceAmountDB);
				flag1 = true;
			} else {
				test.log(Status.FAIL, invoiceAmount + "is not equal to DB value " + invoiceAmountDB);
				flag1 = false;
			}
			String paymentAmt = getElement(LocatorTypes.xpath,
					M360BillingObjRepo.M360BillingInvoice.invoicePaymentAmt_xpath).getText();
			String finalAmountUI = getElement(LocatorTypes.xpath,
					M360BillingObjRepo.M360BillingInvoice.invoiceFinalAmt_xpath).getText();

			if (Double.parseDouble(paymentAmt) < 0) {
				finalAmt = Double.parseDouble(invoiceAmount) + Double.parseDouble(paymentAmt);
			} else {
				finalAmt = Double.parseDouble(invoiceAmount);
			}
			// validating final amount invoice header details
			if (Double.parseDouble(finalAmountUI) == (finalAmt)) {
				reportPass("Final Amount matches with screen");
				flag2 = true;
			} else {
				reportFail("Final Amount does not matches with screen");
				flag2 = false;
			}
			if (flag1 == true && flag2 == true) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("M360BillingInvoicePage validateInvoiceHeaderDetails failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}

	public boolean getOpenInvoiceDetails() {
		boolean flag = false;
		boolean invoiceDetailsFlag = false;
		boolean headerDetailsFlag = false;
		boolean summaryFlag = false;
		int totalOpenInvoices = 0;
		boolean year2019 = false;
		try {
			/*
			 * Date coverageDate = new
			 * SimpleDateFormat("yyyy-MM-dd").parse(testData.get("CoverageDate")); Date
			 * checkDate = new SimpleDateFormat("yyyy-MM-dd").parse("2019-08-01"); if
			 * (coverageDate.before(checkDate)) { year2019 = true; }
			 */
			List<WebElement> invoiceList = getElements(LocatorTypes.xpath,
					M360BillingObjRepo.M360BillingInvoice.invoicelist_xpath);
			if (invoiceList.size() < 3) {
				reportFail("No invoice found for this member");
			} else {
				double totalPaymentAmt = 0;
				for (int i = 2; i < invoiceList.size(); i++) {
					if (getElement(LocatorTypes.xpath,
							M360BillingObjRepo.M360BillingInvoice.invoicelist_xpath + "[" + i + "]/td[6]").getText()
									.trim().equalsIgnoreCase("Open")) {
						totalOpenInvoices = totalOpenInvoices + 1;
						getElement(LocatorTypes.xpath,
								M360BillingObjRepo.M360BillingInvoice.invoicelist_xpath + "[" + i + "]/td[1]").click();
						invoiceDetailsFlag = getInvoiceDetails(year2019);
						headerDetailsFlag = validateInvoiceHeaderDetails(year2019);
						String paymentAmt = getElement(LocatorTypes.xpath,
								M360BillingObjRepo.M360BillingInvoice.invoicePaymentAmt_xpath).getText();
						totalPaymentAmt = totalPaymentAmt + Double.parseDouble(paymentAmt);
					} else {
						test.log(Status.INFO, testData.get("MedCareID") + " not Open invoice");
					}
				}
				if (invoiceDetailsFlag) {
					summaryFlag = validateMemberSummary(totalOpenInvoices, year2019, totalPaymentAmt);
				}
				if (invoiceDetailsFlag == true && headerDetailsFlag == true && summaryFlag == true) {
					flag = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("No more invoices");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}

	public boolean validateMemberSummary(int totalOpenInvoices, boolean dateCheck, double totalPaymentAmt) {
		boolean flag = false;
		boolean flag1 = false;
		boolean flag2 = false;
		boolean flag3 = false;
		try {
			double memberTotalDue;
			double totalInvoiceAmtinDB;
			if (dateCheck) {
				totalInvoiceAmtinDB = Double.parseDouble(testData.get("2019TotalPremium"));
			} else {
				totalInvoiceAmtinDB = Double.parseDouble(testData.get("2020TotalPremium"));
			}
			double summaryInvoice = totalInvoiceAmtinDB * totalOpenInvoices;
			String memberInvoiceUI = getElement(LocatorTypes.xpath,
					M360BillingObjRepo.M360BillingInvoice.memberInvoice_xpath).getText();
			if (Double.parseDouble(memberInvoiceUI) == (summaryInvoice)) {
				reportPass("Member summary Invoice matches from DB " + summaryInvoice + " to UI " + memberInvoiceUI);
				flag1 = true;
			} else {
				reportFail(
						"Member summary Invoice doesnot match from DB " + summaryInvoice + " to UI " + memberInvoiceUI);
				flag1 = false;
			}
			String memberPaymentAmt = getElement(LocatorTypes.xpath,
					M360BillingObjRepo.M360BillingInvoice.memberPayments_xpath).getText();
			String memberTotalDueUI = getElement(LocatorTypes.xpath,
					M360BillingObjRepo.M360BillingInvoice.memberTotalDue_xpath).getText().trim();

			// validating member total due
			if (Double.parseDouble(memberPaymentAmt) < 0) {
				memberTotalDue = Double.parseDouble(memberInvoiceUI) + Double.parseDouble(memberPaymentAmt);
			} else {
				memberTotalDue = Double.parseDouble(memberInvoiceUI);
			}
			if (Double.parseDouble(memberTotalDueUI) == (memberTotalDue)) {
				reportPass("Member Total Due amount matches with DB");
				flag2 = true;
			} else {
				reportFail("Member Total Due amount does not matches with DB");
				flag2 = false;
			}

			// validating member Payment amount
			if (Double.parseDouble(memberPaymentAmt) == totalPaymentAmt) {
				reportPass("Member Payment amount matches with UI");
				flag3 = true;
			} else {
				reportFail("Member Payment amount does not matches with UI");
				flag3 = false;
			}
			if (flag1 == true && flag2 == true && flag3 == true) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("M360BillingInvoicePage validateMemberSummary failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}

	public boolean getInvoiceDetails(boolean dateCheck) {
		boolean flag = false;
		String partD2019 = "2019PartDPremium";
		String partD2020 = "2020PartDPremium";
		String percent = testData.get("LISPercent").trim();
		if (!percent.isEmpty()) {
			partD2019 = "2019PartDLIS" + percent;
			partD2020 = "2020PartDLIS" + percent;
		}
		String LISAmountPartDinDB;
		String LISAmountPartCinDB;
		try {
			List<WebElement> invoiceDetailsList = getElements(LocatorTypes.xpath,
					M360BillingObjRepo.M360BillingInvoice.invoiceDetailslist_xpath);
			for (int i = 2; i < invoiceDetailsList.size(); i++) {
				getElement(LocatorTypes.xpath,
						M360BillingObjRepo.M360BillingInvoice.invoiceDetailslist_xpath + "[" + i + "]/td[3]").click();
				String detailAmount = getElement(LocatorTypes.xpath,
						M360BillingObjRepo.M360BillingInvoice.invoiceDetailsDetailAmt_xpath).getText().trim();
				String description = getElement(LocatorTypes.xpath,
						M360BillingObjRepo.M360BillingInvoice.invoiceDetailslist_xpath + "[" + i + "]/td[3]").getText()
								.trim();
				if (description.equalsIgnoreCase("Part D Premium")) {
					if (dateCheck) {
						LISAmountPartDinDB = testData.get(partD2019).trim();
					} else {
						LISAmountPartDinDB = testData.get(partD2020).trim();
					}
					if (LISAmountPartDinDB.equals(detailAmount)) {
						reportPass("Part D detail amount matches with DB");
						flag = true;
					} else {
						reportFail("Part D detail amount does not matches with DB");
						flag = false;
					}
				}
				if (description.equalsIgnoreCase("Part C Premium")) {
					if (dateCheck) {
						LISAmountPartCinDB = testData.get("2019PartCPremium").trim();
					} else {
						LISAmountPartCinDB = testData.get("2020PartCPremium").trim();
					}
					if (LISAmountPartCinDB.equals(detailAmount)) {
						reportPass("Part C detail amount matches with DB");
						flag = true;
					} else {
						reportFail("Part C detail amount does not matches with DB");
						flag = false;
					}
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("No more invoices");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}

	public boolean searchMedCareAndInvoiceType() {
		boolean flag = true;
		try {
			getElement(LocatorTypes.name, M360BillingObjRepo.M360BillingInvoice.BillResetSearch_name).click();
			getElement(LocatorTypes.xpath, M360BillingObjRepo.M360BillingInvoice.medicareid_xpath)
					.sendKeys(testData.get("MedCareID").trim());
			WebElement invoiceEle = getElement(LocatorTypes.xpath,
					M360BillingObjRepo.M360BillingInvoice.dropDownInvoiceTypeSearch_xpath);
			Select invoiceDropDown = new Select(invoiceEle);
			invoiceDropDown.selectByVisibleText("Unapplied Cash - MBR");
			getElement(LocatorTypes.id, M360BillingObjRepo.M360BillingInvoice.goButton_id).click();
			reportPass("M360MemberPage getMemberDetails Passed");
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			try {
				reportFail("M360MemberPage getMemberDetails failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}

	public boolean getUnAppliedInvoiceDetails() {
		boolean flag = false;
		String invoiceNbr = null;
		String unAppliedfinalAmount = null;
		boolean invoiceHeaderFlag = false;
		boolean invoiceAdujustmentHeaderFlag = false;
		try {
			List<WebElement> invoiceList = getElements(LocatorTypes.xpath,
					M360BillingObjRepo.M360BillingInvoice.invoicelist_xpath);
			if (invoiceList.size() < 3) {
				reportFail("No invoice found for this member");
			} else {
				double totalUnAppliedPaymentAmt = 0;
				for (int i = 2; i < invoiceList.size(); i++) {

					if (getElement(LocatorTypes.xpath,
							M360BillingObjRepo.M360BillingInvoice.invoicelist_xpath + "[" + i + "]/td[2]").getText()
									.trim().equalsIgnoreCase("UM - Unapplied Cash - MBR")
							&& getElement(LocatorTypes.xpath,
									M360BillingObjRepo.M360BillingInvoice.invoicelist_xpath + "[" + i + "]/td[6]")
											.getText().trim().equalsIgnoreCase("Open")) {
						getElement(LocatorTypes.xpath,
								M360BillingObjRepo.M360BillingInvoice.invoicelist_xpath + "[" + i + "]/td[1]").click();
						unAppliedfinalAmount = getElement(LocatorTypes.xpath,
								M360BillingObjRepo.M360BillingInvoice.invoicelist_xpath + "[" + i + "]/td[7]")
										.getText();
						unAppliedfinalAmount = unAppliedfinalAmount.substring(1, unAppliedfinalAmount.length());
						totalUnAppliedPaymentAmt = totalUnAppliedPaymentAmt + Double.parseDouble(unAppliedfinalAmount);
						invoiceNbr = getElement(LocatorTypes.xpath,
								M360BillingObjRepo.M360BillingInvoice.invoiceNbr_xpath).getText();
						invoiceHeaderFlag = validateUnAppliedInvoiceHeaderDetails();
						if (testData.get("RefundIndicator").equalsIgnoreCase("Y")) {
							reportPass("Refund Indicator is yes");
							String bankCode = getAdjustmentInvoiceDetails();
							invoiceAdujustmentHeaderFlag = validateAdjustUnAppliedInvoiceAmount(bankCode, invoiceNbr,
									unAppliedfinalAmount);
						}
					} else {
						test.log(Status.INFO, testData.get("MedCareID") + " not Open invoice");
					}
				}
				if (invoiceAdujustmentHeaderFlag == true && invoiceHeaderFlag == true
						&& invoiceAdujustmentHeaderFlag == true) {
					flag = true;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("No more invoices");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}

	public boolean validateUnAppliedInvoiceHeaderDetails() {
		boolean flag = false;
		boolean flag1 = false;
		double finalAmt = 0;
		try {
			String invoiceAmount = getElement(LocatorTypes.xpath,
					M360BillingObjRepo.M360BillingInvoice.invoiceAmt_xpath).getText();
			String paymentAmt = getElement(LocatorTypes.xpath,
					M360BillingObjRepo.M360BillingInvoice.invoicePaymentAmt_xpath).getText();
			String finalAmountUI = getElement(LocatorTypes.xpath,
					M360BillingObjRepo.M360BillingInvoice.invoiceFinalAmt_xpath).getText();
			if (Double.parseDouble(paymentAmt) < 0) {
				if (Double.parseDouble(paymentAmt) < Double.parseDouble(invoiceAmount)) {
					reportPass("Payment Amount is Over paid");
					finalAmt = Double.parseDouble(invoiceAmount) + Double.parseDouble(paymentAmt);
				}
			} else {
				finalAmt = Double.parseDouble(invoiceAmount);
			}
			// validating final amount invoice header details
			if (Double.parseDouble(finalAmountUI) == (finalAmt)) {
				reportPass("Final Amount matches with screen");
				flag1 = true;
			} else {
				reportFail("Final Amount does not matches with screen");
				flag1 = false;
			}
			if (flag1 == true) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("M360BillingInvoicePage validateInvoiceHeaderDetails failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}

	public boolean validateUnAppliedMemberSummary(int totalOpenInvoices, boolean dateCheck, double totalPaymentAmt) {
		boolean flag = false;
		boolean flag1 = false;
		boolean flag2 = false;
		try {
			double memberTotalDue;
			String memberInvoiceUI = getElement(LocatorTypes.xpath,
					M360BillingObjRepo.M360BillingInvoice.memberInvoice_xpath).getText();
			String memberPaymentAmt = getElement(LocatorTypes.xpath,
					M360BillingObjRepo.M360BillingInvoice.memberPayments_xpath).getText();
			String memberTotalDueUI = getElement(LocatorTypes.xpath,
					M360BillingObjRepo.M360BillingInvoice.memberTotalDue_xpath).getText().trim();

			// validating member total due
			if (Double.parseDouble(memberPaymentAmt) < 0) {
				memberTotalDue = Double.parseDouble(memberInvoiceUI) + Double.parseDouble(memberPaymentAmt);
			} else {
				memberTotalDue = Double.parseDouble(memberInvoiceUI);
			}
			if (Double.parseDouble(memberTotalDueUI) == (memberTotalDue)) {
				reportPass("Member Total Due amount matches");
				flag1 = true;
			} else {
				reportFail("Member Total Due amount does not matches");
				flag1 = false;
			}

			// validating member Payment amount
			if (Double.parseDouble(memberPaymentAmt) == totalPaymentAmt) {
				reportPass("Member Payment amount matches with UI");
				flag2 = true;
			} else {
				reportFail("Member Payment amount does not matches with UI");
				flag2 = false;
			}
			if (flag1 == true && flag2 == true) {
				flag = true;
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("M360BillingInvoicePage validateMemberSummary failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}

	public String getAdjustmentInvoiceDetails() {
		String bankCode = null;
		try {
			List<WebElement> invoiceDetailsList = getElements(LocatorTypes.xpath,
					M360BillingObjRepo.M360BillingInvoice.invoiceDetailslist_xpath);
			for (int i = 2; i < invoiceDetailsList.size(); i++) {
				if (getElement(LocatorTypes.xpath,
						M360BillingObjRepo.M360BillingInvoice.invoiceDetailslist_xpath + "[" + i + "]/td[3]").getText()
								.equalsIgnoreCase("Premium Received")) {
					getElement(LocatorTypes.xpath,
							M360BillingObjRepo.M360BillingInvoice.invoiceDetailslist_xpath + "[" + i + "]/td[3]")
									.click();
					bankCode = getElement(LocatorTypes.xpath,
							M360BillingObjRepo.M360BillingInvoice.invoiceDetailsBankCode_xpath).getText();
					reportPass("Premium Received Invoice found");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("No more invoices");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return bankCode;
	}

	public boolean validateAdjustUnAppliedInvoiceAmount(String banckCode, String invoiceNbr,
			String unAppliedfinalAmount) {
		boolean flag = true;
		try {
			getElement(LocatorTypes.xpath, M360BillingObjRepo.M360BillingInvoice.invoiceAdjustment_xpath).click();
			getElement(LocatorTypes.xpath,
					M360BillingObjRepo.M360BillingInvoice.invoiceDetailsAdjustmentBanckCode_xpath).sendKeys(banckCode);
			getElement(LocatorTypes.xpath,
					M360BillingObjRepo.M360BillingInvoice.invoiceDetailsAdjustmentInvoiceNbr_xpath)
							.sendKeys(invoiceNbr);
			Select adjustFunctionDropDown = new Select(getElement(LocatorTypes.xpath,
					M360BillingObjRepo.M360BillingInvoice.invoiceAdjustmentFunction_xpath));
			adjustFunctionDropDown.selectByVisibleText("REF - Refund");
			Select adjustRefundReasonDropDown = new Select(getElement(LocatorTypes.xpath,
					M360BillingObjRepo.M360BillingInvoice.invoiceAdjustmentRefundReason_xpath));
			adjustRefundReasonDropDown.selectByVisibleText("02 - Overpayment");
			unAppliedfinalAmount = unAppliedfinalAmount.trim();
			unAppliedfinalAmount = unAppliedfinalAmount.substring(1, unAppliedfinalAmount.length());
			getElement(LocatorTypes.xpath, M360BillingObjRepo.M360BillingInvoice.invoiceAdjustmentDetailAmt_xpath)
					.clear();
			getElement(LocatorTypes.xpath, M360BillingObjRepo.M360BillingInvoice.invoiceAdjustmentDetailAmt_xpath)
					.sendKeys(unAppliedfinalAmount);
			getElement(LocatorTypes.xpath, M360BillingObjRepo.M360BillingInvoice.invoiceAdjustmentUpdateDtlsBtn_xpath)
					.click();
			reportPass("Refund Initiated");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Refund not Initiated");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}
}
