package pageclasses.m360billing;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.WebElement;

import com.aventstack.extentreports.Status;

import enums.LocatorTypes;
import managers.PageManager;
import pageclasses.BasePage;
import pageclasses.m360membership.M360MemberPage;
import pageobjects.M360BillingObjRepo;

import utils.Dbconn;

public class M360MemberPaymentsPage extends BasePage {

	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(M360MemberPage.class.getName());
	PageManager pm = new PageManager();
	Dbconn db = new Dbconn();

	public M360MemberPaymentsPage() {
		driver = getWebDriver();
	}

	public void navigateToMemberPaymentDetails() {
		getElement(LocatorTypes.xpath, M360BillingObjRepo.M360ManagePayment.memPaymentsTab_xpath).click();

	}

	public void getMember(String medicareID) {
		getElement(LocatorTypes.xpath, M360BillingObjRepo.M360ManagePayment.resetBtn_xpath).click();
		getElement(LocatorTypes.xpath, M360BillingObjRepo.M360ManagePayment.medId_xpath).sendKeys(medicareID);
		getElement(LocatorTypes.id, M360BillingObjRepo.M360ManagePayment.goBtn_id).click();
	}

	public void searchInvoiceList() {
		boolean flag = false;
		try {
			List<WebElement> membersList = getElements(LocatorTypes.xpath,
					M360BillingObjRepo.MemberPaymentDetails.memberlist_xpath);

			if (membersList.size() > 0) {
				for (int i = 0; i < membersList.size(); i++) {
					membersList.get(i).click();
					flag = validateMemberDetials();
					if (flag == true) {
						continue;
					}
				}
				if (flag == true) {
					reportPass("Invoice : " + testData.get("InvoiceNumber") + " is found");
					db.updateReadyToBill("PayValidationStatus", "PASSED");
				} else {
					reportFail("Invoice : " + testData.get("InvoiceNumber") + " is not found");
				}
			} else {
				reportFail("Invoice is not found on the screen");
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Invoice is not found on the screen");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

	}

	public HashMap<String, String> getBillingInvoiceDetails() {
		HashMap<String, String> testDataMap = new HashMap<String, String>();
		try {

			testDataMap.put("memberId",
					getElement(LocatorTypes.xpath, M360BillingObjRepo.MemberPaymentDetails.memId_xpath).getText()
							.trim());
			testDataMap.put("Name", getElement(LocatorTypes.xpath, M360BillingObjRepo.MemberPaymentDetails.name_xpath)
					.getText().trim().toUpperCase());
			testDataMap.put("medicareId",
					getElement(LocatorTypes.xpath, M360BillingObjRepo.MemberPaymentDetails.medicareId_xpath).getText()
							.trim());
			testDataMap.put("paySource",
					getElement(LocatorTypes.xpath, M360BillingObjRepo.MemberPaymentDetails.paySource_xpath).getText()
							.substring(0, 1).trim());
			testDataMap.put("payIndicator",
					getElement(LocatorTypes.xpath, M360BillingObjRepo.MemberPaymentDetails.payPostedIndicator_xpath)
							.getText().trim());
			testDataMap.put("payAmount",
					getElement(LocatorTypes.xpath, M360BillingObjRepo.MemberPaymentDetails.payAmount_xpath).getText()
							.trim());

			testDataMap.put("checkNum",
					getElement(LocatorTypes.xpath, M360BillingObjRepo.MemberPaymentDetails.checkNum_xpath).getText()
							.trim());
			testDataMap.put("checkDate",
					getDateMMDDYYYYInSqlFormat(
							getElement(LocatorTypes.xpath, M360BillingObjRepo.MemberPaymentDetails.checkDate_xpath)
									.getText().trim()));

			testDataMap.put("dueDate",
					getDateMMDDYYYYInSqlFormat(
							getElement(LocatorTypes.xpath, M360BillingObjRepo.MemberPaymentDetails.dueDate_xpath)
									.getText().trim()));

			List<WebElement> invoiceList = getElements(LocatorTypes.xpath,
					M360BillingObjRepo.MemberPaymentDetails.invoiceList_xpath);

			for (int i = 2; i <= invoiceList.size(); i++) {

				List<WebElement> invoiceColumns = getElements(LocatorTypes.xpath,
						M360BillingObjRepo.MemberPaymentDetails.invoiceList_xpath + "[" + i + "]/td");
				testDataMap.put("invoiceNumber" + i, invoiceColumns.get(0).getText().trim());
				testDataMap.put("invoiceType" + i, invoiceColumns.get(1).getText().trim());

			}

			logger.info(testDataMap);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Getting member payment details failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return testDataMap;
	}

	public boolean validateMemberDetials() {
		boolean flag = true;

		HashMap<String, String> testDataMem = getBillingInvoiceDetails();
		List<WebElement> invoiceList = getElements(LocatorTypes.xpath,
				M360BillingObjRepo.MemberPaymentDetails.invoiceList_xpath);

		try {

			for (int i = 2; i <= invoiceList.size(); i++) {
				if (testData.get("InvoiceNumber").trim()
						.equalsIgnoreCase(testDataMem.get("invoiceNumber" + i).trim())) {
					if (!testData.get("MemberID").trim().equalsIgnoreCase(testDataMem.get("memberId").trim())) {
						test.log(Status.FAIL, "MemberID does not match - " + testData.get("MemberID").trim() + " -- "
								+ testDataMem.get("memberId").trim());
						flag = false;
					}
					if (!testData.get("MedicareID").trim().equalsIgnoreCase(testDataMem.get("medicareId").trim())) {
						test.log(Status.FAIL, "MedicareID does not match - " + testData.get("MedicareID").trim()
								+ " -- " + testDataMem.get("medicareId").trim());
						flag = false;
					}
					if (!testDataMem.get("Name").trim().contains(testData.get("MemberLName").trim().toUpperCase())) {
						test.log(Status.FAIL, "MemberLName does not match - " + testData.get("MemberLName").trim()
								+ " -- " + testDataMem.get("Name").trim());
						flag = false;
					}
					if (!testData.get("PaymentSource").trim().equalsIgnoreCase(testDataMem.get("paySource").trim())) {
						test.log(Status.FAIL, "PaymentSource does not match - " + testData.get("PaymentSource").trim()
								+ " -- " + testDataMem.get("paySource").trim());
						flag = false;
					}
					if (!testData.get("PaymentPostedIndicator").trim()
							.equalsIgnoreCase(testDataMem.get("payIndicator").trim())) {
						test.log(Status.FAIL,
								"PaymentPostedIndicator does not match - "
										+ testData.get("PaymentPostedIndicator").trim() + " -- "
										+ testDataMem.get("payIndicator").trim());
						flag = false;
					}
					if (!testData.get("PaymentAmount").trim().equalsIgnoreCase(testDataMem.get("payAmount").trim())) {
						test.log(Status.FAIL, "PaymentAmount does not match - " + testData.get("PaymentAmount").trim()
								+ " -- " + testDataMem.get("payAmount").trim());
						flag = false;
					}
					if (!testData.get("CheckNumber").trim().equalsIgnoreCase(testDataMem.get("checkNum").trim())) {
						test.log(Status.FAIL, "CheckNumber does not match - " + testData.get("CheckNumber").trim()
								+ " -- " + testDataMem.get("checkNum").trim());
						flag = false;
					}
					if (!testData.get("CheckDate").trim().equalsIgnoreCase(testDataMem.get("checkDate").trim())) {
						test.log(Status.FAIL, "CheckDate does not match - " + testData.get("CheckDate").trim() + " -- "
								+ testDataMem.get("checkDate").trim());
						flag = false;
					}
					if (!testData.get("InvoiceNumber").trim()
							.equalsIgnoreCase(testDataMem.get("invoiceNumber" + i).trim())) {
						test.log(Status.FAIL, "InvoiceNumber does not match - " + testData.get("InvoiceNumber").trim()
								+ " -- " + testDataMem.get("invoiceNumber" + i).trim());
						flag = false;
					}
					if (!testData.get("InvoiceType").trim()
							.equalsIgnoreCase(testDataMem.get("invoiceType" + i).trim())) {
						test.log(Status.FAIL, "InvoiceType does not match - " + testData.get("InvoiceType").trim()
								+ " -- " + testDataMem.get("invoiceType" + i).trim());
						flag = false;
					}
					if (!testData.get("InvoiceDueDate").trim().equalsIgnoreCase(testDataMem.get("dueDate").trim())) {
						test.log(Status.FAIL, "InvoiceDueDate does not match - " + testData.get("InvoiceDueDate").trim()
								+ " -- " + testDataMem.get("dueDate").trim());
						flag = false;
					}
					if (flag == true) {
						db.updateReadyToBill("PayValidationStatus", "PASSED");
						reportPass("Member payment detials are validated for medicare :" + testDataMem);
					} else {
						reportFail("Member payment details Not Matched, screen data: " + testDataMem
								+ " did not match enrollment data in tables: " + testData);
					}

				}

			}

		} catch (IOException e) {
			e.printStackTrace();
			try {
				reportFail("No Member payment details to validate");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		return flag;
	}

}
