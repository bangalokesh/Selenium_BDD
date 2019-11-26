package processes;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import managers.PageManager;
import pageclasses.BasePage;
import utils.Dbconn;

public class M360BillingMemPaymentProcess extends BasePage {

	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(M360EnrollmentProcess.class.getName());
	Dbconn db = new Dbconn();
	PageManager pm = new PageManager();

	public M360BillingMemPaymentProcess() {
		driver = getWebDriver();
	}

	public void executeMemberPaymentsValidation() {

		try {

			String query = "select ID, MemberID, MedicareID, MemberLName, PaymentSource, PaymentPostedIndicator, PaymentAmount, CheckNumber, CheckDate, InvoiceNumber, InvoiceType, InvoiceDueDate from VelocityTestAutomation.dbo.test_data_readytobill where PayFileGenerated = 'Y' and PayValidationStatus is null ;";
			List<HashMap<String, String>> list = db.getListOfHashMapsFromResultSet(query);
			HashMap<String, String> temptestData = new HashMap<String, String>();

			for (HashMap<String, String> row : list) {
				for (String key : row.keySet()) {
					temptestData.put(key, row.get(key));
				}
				setrowTestData(temptestData);
				test = extent.createTest("Billing Invoice Test for " + testData.get("MedicareID"));
				pm.getM360MemberPaymentsPage().getMember(temptestData.get("MedicareID").trim());
				pm.getM360MemberPaymentsPage().searchInvoiceList();
				flushTest();
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Test Failed in BillingMemPaymentProcess executeMemberPaymentValidation method");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void executeBillingInvoice() {
		try {
			String query = " SELECT enr.[ID]\r\n" + "      ,enr.[TCID]\r\n" + "      ,enr.[DriverType]\r\n"
					+ "      ,enr.[MemberLName]\r\n" + "      ,enr.[MedCareID]\r\n" + "      ,dem.[MemberID]\r\n"
					+ "      ,dem.[SupplementalID]\r\n" + "      ,enr.[RunMode]\r\n" + "      ,enr.[Environment]\r\n"
					+ "      ,enr.[ProductPlanID]\r\n" + "      ,enr.[ProductPBPID]\r\n"
					+ "      ,enr.[ProductSegmentID]\r\n" + "      ,enr.[ApplicationDate]\r\n"
					+ "      ,enr.[CoverageDate]\r\n" + "      ,enr.[ApplicationType]\r\n"
					+ "      ,enr.[ProductPlanName]\r\n" + "      ,enr.[ApplicationStatus]\r\n"
					+ "      ,enr.[CMSEnrollmentStatus]\r\n" + "      ,enr.[MemValStatus]\r\n"
					+ "      ,enr.[CalidusValStatus]\r\n" + "      ,enr.[BillingStatus]\r\n"
					+ "  FROM [VelocityTestAutomation].[dbo].[test_data_readytoenroll] enr, [dbo].[member_demographic] dem\r\n"
					+ "  WHERE enr.[RunMode] = 'Y' AND\r\n" + "  enr.[ApplicationStatus] = 'EAPRV' AND\r\n"
					+ "  enr.[CMSEnrollmentStatus] = 'CMSAPRV' AND\r\n" + "  enr.[BillingStatus] is Null AND\r\n"
					+ "  enr.[MemValStatus] != 'DECEASED' AND\r\n" + "  enr.[MedCareID] = dem.[MedicareID];\r\n" + "";
			List<HashMap<String, String>> list = db.getListOfHashMapsFromResultSet(query);
			HashMap<String, String> temptestData = new HashMap<String, String>();
			for (HashMap<String, String> row : list) {
				for (String key : row.keySet()) {
					temptestData.put(key, row.get(key));
				}
				setrowTestData(temptestData);
				test = extent.createTest("Billing Invoice Test for " + testData.get("MedCareID"));
				pm.getM360BillingInvoicePage().searchMedicareId();
				pm.getM360BillingInvoicePage().getInvoiceList();

				flushTest();
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Test Failed in BillingMemPaymentProcess executeMemberPaymentValidation method");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void executeBillingInvoiceValidation() {
		try {
			String query = " SELECT enr.[ID]\r\n" + "      ,enr.[TCID]\r\n" + "      ,enr.[DriverType]\r\n"
					+ "      ,enr.[MemberLName]\r\n" + "      ,enr.[MedCareID]\r\n" + "      ,dem.[MemberID]\r\n"
					+ "      ,dem.[SupplementalID]\r\n" + "      ,enr.[RunMode]\r\n" + "      ,enr.[Environment]\r\n"
					+ "      ,enr.[ProductPlanID]\r\n" + "      ,enr.[ProductPBPID]\r\n"
					+ "      ,enr.[ProductSegmentID]\r\n" + "      ,enr.[ApplicationDate]\r\n"
					+ "      ,enr.[CoverageDate]\r\n" + "      ,enr.[ApplicationType]\r\n"
					+ "      ,enr.[ProductPlanName]\r\n" + "      ,enr.[ApplicationStatus]\r\n"
					+ "      ,enr.[CMSEnrollmentStatus]\r\n" + "      ,enr.[MemValStatus]\r\n"
					+ "      ,enr.[CalidusValStatus]\r\n" + "      ,enr.[BillingStatus]\r\n"
					+ "      ,enr.[CoverageDate]\r\n" + "      ,pd.[2020PartCPremium]\r\n"
					+ "      ,pd.[2020PartDPremium]\r\n" + "      ,pd.[2020TotalPremium]\r\n"
					+ "      ,pd.[2020PartDLIS025]\r\n" + "      ,pd.[2020PartDLIS050]\r\n"
					+ "      ,pd.[2020PartDLIS075]\r\n" + "      ,pd.[2020PartDLIS100]\r\n"
					+ "      ,pd.[2019PartCPremium]\r\n" + "      ,pd.[2019PartDPremium]\r\n"
					+ "      ,pd.[2019TotalPremium]\r\n" + "      ,pd.[2019PartDLIS025]\r\n"
					+ "      ,pd.[2019PartDLIS050]\r\n" + "      ,pd.[2019PartDLIS075]\r\n"
					+ "      ,pd.[2019PartDLIS100]\r\n" + "      ,lep.[LISPercent]\r\n"
					+ "  FROM [VelocityTestAutomation].[dbo].[test_data_readytoenroll] enr, [dbo].[member_demographic] dem, [dbo].[product_details] pd, [dbo].[member_LIS_LEP_details] lep\r\n"
					+ "  WHERE enr.[RunMode] = 'Y' AND\r\n" + "  enr.[ApplicationStatus] = 'EAPRV' AND\r\n"
					+ "  enr.[CMSEnrollmentStatus] = 'CMSAPRV' AND\r\n" + "  enr.[BillingStatus] is Null AND\r\n"
					+ "  enr.[MemValStatus] != 'DECEASED' AND\r\n" + "  enr.[MedCareID] = dem.[MedicareID] AND\r\n"
					+ "  enr.[MedCareID] = lep.[MedicareID] AND\r\n" + "  enr.[ProductPlanName] = pd.[PlanName];\r\n"
					+ "";
			List<HashMap<String, String>> list = db.getListOfHashMapsFromResultSet(query);
			HashMap<String, String> temptestData = new HashMap<String, String>();
			for (HashMap<String, String> row : list) {
				for (String key : row.keySet()) {
					temptestData.put(key, row.get(key));
				}
				setrowTestData(temptestData);
				test = extent.createTest("Billing Invoice Test for " + testData.get("MedCareID"));
				pm.getM360BillingInvoicePage().searchMedicareId();
				pm.getM360BillingInvoicePage().getInvoiceList();
				boolean flag = pm.getM360BillingInvoicePage().getOpenInvoiceDetails();
				if (flag == true) {
					reportPass("Billing invoice Validation passed for MedicareID: " + testData.get("MedCareID"));
					db.updateReadyToEnroll("BillingStatus", "PASSED");
				} else {
					reportFail("Billing invoice Validation failed for MedicareID: " + testData.get("MedCareID"));
					db.updateReadyToEnroll("BillingStatus", "FAILED");
				}
				flushTest();
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Test Failed in BillingMemPaymentProcess executeMemberPaymentValidation method");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void executeBillingInvoiceUnappliedCashValidation() {
		try {
			String query = " SELECT enr.[ID]\r\n" + "      ,enr.[TCID]\r\n" + "      ,enr.[DriverType]\r\n"
					+ "      ,enr.[MemberLName]\r\n" + "      ,enr.[MedCareID]\r\n" + "      ,dem.[MemberID]\r\n"
					+ "      ,dem.[SupplementalID]\r\n" + "      ,enr.[RunMode]\r\n" + "      ,enr.[Environment]\r\n"
					+ "      ,enr.[ProductPlanID]\r\n" + "      ,enr.[ProductPBPID]\r\n"
					+ "      ,enr.[ProductSegmentID]\r\n" + "      ,enr.[ApplicationDate]\r\n"
					+ "      ,enr.[CoverageDate]\r\n" + "      ,enr.[ApplicationType]\r\n"
					+ "      ,enr.[ProductPlanName]\r\n" + "      ,enr.[ApplicationStatus]\r\n"
					+ "      ,enr.[CMSEnrollmentStatus]\r\n" + "      ,enr.[MemValStatus]\r\n"
					+ "      ,enr.[CalidusValStatus]\r\n" + "      ,enr.[BillingStatus]\r\n"
					+ "      ,enr.[CoverageDate]\r\n" + "      ,rtb.[PaymentAmount]\r\n"
					+ "      ,rtb.[RefundIndicator]\r\n"
					+ "  FROM [VelocityTestAutomation].[dbo].[test_data_readytoenroll] enr, [dbo].[member_demographic] dem, [dbo].[test_data_readytobill] rtb \r\n"
					+ "  WHERE enr.[RunMode] = 'Y' AND\r\n" + "  enr.[ApplicationStatus] = 'EAPRV' AND\r\n"
					+ "  enr.[CMSEnrollmentStatus] = 'CMSAPRV' AND\r\n" + "  enr.[BillingStatus] is Null AND\r\n"
					+ "  rtb.[PayFileGenerated] = 'Y' AND\r\n" + "  rtb.[PayValidationStatus] is Null AND\r\n"
					+ "  enr.[MemValStatus] != 'DECEASED' AND\r\n" + "  enr.[MedCareID] = dem.[MedicareID] AND\r\n"
					+ "  rtb.[PaymentAmount] > rtb.[InvoiceAmount];\r\n" + "";
			List<HashMap<String, String>> list = db.getListOfHashMapsFromResultSet(query);
			HashMap<String, String> temptestData = new HashMap<String, String>();
			for (HashMap<String, String> row : list) {
				for (String key : row.keySet()) {
					temptestData.put(key, row.get(key));
				}
				setrowTestData(temptestData);
				test = extent.createTest("Billing Invoice Test for " + testData.get("MedCareID"));
				pm.getM360BillingInvoicePage().searchMedCareAndInvoiceType();
				boolean flag = pm.getM360BillingInvoicePage().getUnAppliedInvoiceDetails();
				if (flag == true) {
					reportPass("Billing invoice Validation passed for MedicareID: " + testData.get("MedCareID"));
					db.updateReadyToEnroll("BillingStatus", "PASSED");
				} else {
					reportFail("Billing invoice Validation failed for MedicareID: " + testData.get("MedCareID"));
					db.updateReadyToEnroll("BillingStatus", "FAILED");
				}
				flushTest();
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail(
						"Test Failed in BillingMemPaymentProcess executeBillingInvoiceUnappliedCashValidation method");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
