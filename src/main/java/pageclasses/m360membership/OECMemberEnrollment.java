package pageclasses.m360membership;

import java.io.IOException;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import managers.PageManager;
import pageclasses.BasePage;
import pageclasses.CommonMethods;
import utils.Dbconn;

public class OECMemberEnrollment extends BasePage {
	static CallableStatement stmt = null;
	static Dbconn db = new Dbconn();
	PageManager pm = new PageManager();

	public void createOECRecordInDB() {
		String query = "SELECT rn.* FROM [dbo].[test_data_readytoenroll] rn  \r\n"
				+ "WHERE [RunMode] ='Y' AND [MemValStatus] IN ('None','') AND EnrollmentType = 'OEC' AND ID > 340 order by ID desc;";
		try {
			List<HashMap<String, String>> list = db.getListOfHashMapsFromResultSet(query);
			for (HashMap<String, String> row : list) {
				HashMap<String, String> temptestData = new HashMap<String, String>();
				for (String key : row.keySet()) {
					temptestData.put(key, row.get(key));
				}
				setrowTestData(temptestData);
				try {
					Map<String, String> userData = pm.getm360ElgInquiryPage().getMemberEligibilityDetails();
					if (!userData.get("ApplicantFirstName").isEmpty()) {
						insertOECMemberEnrollmentData(testData.get("MedCareID"), userData);
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}
			}

		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Test Failed in M360EnrollmentProcess executeTestsForEnrollment method");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void insertOECMemberEnrollmentData(String medicareID, Map<String, String> userData) {
		Connection con = Dbconn.getConnection();
		try {
			getAddressData();

			String coverageDate = getNextMonth().replace("/", "");
			String enrollmentyear = null;
			if (testData.get("CoverageDate").isEmpty()) {
				coverageDate = getNextMonth().replace("/", "");
				enrollmentyear = coverageDate.substring(0, 4);
			} else {
				coverageDate = getDateInMMDDYYYY(testData.get("CoverageDate")).replace("/", "");
				enrollmentyear = coverageDate.substring(4);
			}
			stmt = con.prepareCall(
					"{call OECFileCreation_insert(?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?,?)}");

			stmt.setString(1, getConfirmationNumber());
			String sDate = getDateInMMDDYYYY(CommonMethods.getCurrentDate()).replace("/", "");
			String applicationDate = getDateInYYYYMMDD(CommonMethods.getCurrentDate());
			stmt.setString(2, sDate); // SubmitDate
			stmt.setString(3, testData.get("ProductPlanID")); // ContractID
			stmt.setString(4, testData.get("ProductPBPID")); // PlanID
			stmt.setString(5, testData.get("ProductSegmentID")); // SegmentID
			stmt.setString(6, userData.get("ApplicantTitle")); // ApplicantTitle
			stmt.setString(7, userData.get("ApplicantFirstName")); // ApplicantFirstName
			stmt.setString(8, userData.get("ApplicantMiddleInitial")); // ApplicantMiddleInitial
			stmt.setString(9, userData.get("ApplicantLastName")); // ApplicantLastName
			stmt.setString(10, userData.get("dob")); // ApplicantBirthDate
			stmt.setString(11, userData.get("ApplicantGender")); // ApplicantGender

			stmt.setString(12, testData.get("Address1")); // ApplicantAddress1
			stmt.setString(13, testData.get("Address2")); // ApplicantAddress2
			stmt.setString(14, testData.get("Address3")); // ApplicantAddress3
			stmt.setString(15, testData.get("City")); // ApplicantCity
			stmt.setString(16, testData.get("County")); // ApplicantCounty
			stmt.setString(17, testData.get("StateCode")); // ApplicantState
			stmt.setString(18, testData.get("ZipCode")); // ApplicantZip
			stmt.setString(19, ""); // ApplicantPhone

			stmt.setString(20, ""); // ApplicantEmailAddress
			stmt.setString(21, medicareID); // MedicareNumber
			stmt.setString(22, ""); // ApplicantSSN
			stmt.setString(23, ""); // MailingAddress1
			stmt.setString(24, ""); // MailingAddress2
			stmt.setString(25, ""); // MailingAddress3
			stmt.setString(26, ""); // MailingCity
			stmt.setString(27, ""); // MailingState
			stmt.setString(28, ""); // MailingZip

			stmt.setString(29, userData.get("medicarePartA"));// MedicarePartA
			stmt.setString(30, userData.get("medicarePartB")); // MedicarePartB

			stmt.setString(31, ""); // EmergencyContact
			stmt.setString(32, ""); // EmergencyPhone
			stmt.setString(33, ""); // EmergencyRelationship
			stmt.setString(43, ""); // AuthorizedRepName
			stmt.setString(49, ""); // AuthorizedRepRelationship
			stmt.setString(34, "No"); // PremiumDeducted //NULL
			stmt.setString(69, ""); // PremiumWithhold //
			stmt.setString(35, ""); // PremiumSource
			stmt.setString(36, "No"); // OtherCoverage
			stmt.setString(37, ""); // OtherCoverageName
			stmt.setString(38, ""); // OtherCoverageID
			stmt.setString(39, "No"); // LongTerm
			stmt.setString(40, ""); // LongTermName
			stmt.setString(41, ""); // LongTermAddress
			stmt.setString(42, ""); // LongTermPhone
			stmt.setString(44, ""); // AuthorizedRepAddress
			stmt.setString(45, ""); // AuthorizedRepCity
			stmt.setString(46, ""); // AuthorizedRepState
			stmt.setString(47, ""); // AuthorizedRepZip
			stmt.setString(48, ""); // AuthorizedRepPhone
			stmt.setString(50, ""); // Language

			if (userData.get("esrd").isEmpty() || userData.get("esrd").equalsIgnoreCase("N")
					|| userData.get("esrd").equalsIgnoreCase("NO"))
				stmt.setString(51, "No");
			else
				stmt.setString(51, "Yes"); // ESRD varchar
			if (userData.get("stateMedicaid").isEmpty() || userData.get("stateMedicaid").equalsIgnoreCase("N")
					|| userData.get("stateMedicaid").equalsIgnoreCase("NO"))
				stmt.setString(52, "No");
			else
				stmt.setString(52, "Yes"); // StateMedicaid

			stmt.setString(53, "No"); // WorkStatus
			stmt.setString(54, testData.get("PCPDoctorName")); // PrimaryCarePhysician readyToEnroll table
			stmt.setString(55, ""); // OtherCoverageGroup
			stmt.setString(56, testData.get("AgentID")); // AgentID
			stmt.setString(57, "" + CommonMethods.getCurrentTimeStampVal()); // SubmitTime
			stmt.setString(58, ""); // PartDSubAppInd
			stmt.setString(59, ""); // DeemedInd
			stmt.setString(60, ""); // SubsidyPercentage 000, 025,050,075,100
			stmt.setString(61, ""); // DeemedReasonCode
			stmt.setString(62, ""); // LISCopayLevelID
			stmt.setString(63, ""); // DeemedCopayLevelID
			stmt.setString(64, ""); // PartDOptOutSwitch
			if (testData.get("ApplicationType").contains("NMA"))
				stmt.setString(65, "NEW"); // SEPReasonCode
			else if (testData.get("ApplicationType").contains("CMA"))
				stmt.setString(65, "MOV"); // SEPReasonCode
			stmt.setString(66, ""); // SEPCMSReasonCODE
			stmt.setString(67, "Yes"); // PremiumDirectPay
			stmt.setString(68, enrollmentyear); // EnrollmentPlanYear
			stmt.setString(69, coverageDate); // RequestedEffectiveDate
			stmt.setString(70, ""); // PremiumWithHold
			stmt.setString(71, ""); // AccountHolderName
			stmt.setString(72, ""); // AccountType
			stmt.setString(73, ""); // BankName
			stmt.setString(74, ""); // BankRoutingNumber
			stmt.setString(75, ""); // BankAccountNumber
			stmt.setString(76, applicationDate); // Dateenrolleesignedpaperapplication

			if (!testData.get("AgentID").isEmpty() && testData.get("AgentID") != null
					&& !testData.get("AgentID").equalsIgnoreCase("none")) {
				stmt.setString(77, applicationDate); // DateAgentSigned
			}

			stmt.setString(78, ""); // InformationInAnotherFormatType
			stmt.setString(79, "N"); // CurrentPCP
			stmt.setString(80, testData.get("OECType")); // OECType
			stmt.execute();

			db.updateReadyToEnroll("ApplicationDate", getDateStringInSqlFormat(getDateInYYMMDDFROMDDMMYYYY(sDate)));
			if (testData.get("CoverageDate").isEmpty())
				db.updateReadyToEnroll("CoverageDate",
						getDateStringInSqlFormat(getDateInYYMMDDFROMDDMMYYYY(coverageDate)));
			db.updateReadyToEnroll("AddressID", testData.get("AddressID"));
			db.updateReadyToEnroll("ApplicationStatus", "READY");
			db.updateReadyToEnroll("DOB", getDateInSqlFormatFromMMDDYYY(userData.get("dob")));
		} catch (SQLException e) {
			e.printStackTrace();
		} finally {
			try {
				stmt.close();
				con.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}

	public static String getConfirmationNumber() {
		String confNumber = null;
		try {
			if (testData.get("OECType") == null || testData.get("OECType").isEmpty()
					|| testData.get("OECType").equalsIgnoreCase("CMS")) {
				confNumber = "" + CommonMethods.getRandomLong(14);
				getRandomString(14, false);
			} else {
				String OECType = testData.get("OECType");
				confNumber = OECType + CommonMethods.getRandomLong(11);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return confNumber;
	}

	public void deleteFromOECFileCreation() {
		try {
			String query = "DELETE FROM [VelocityTestAutomation].[dbo].[OECFileCreation]";
			db.sqlUpdate(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void getAddressData() {
		HashMap<String, String> addressData = db.getResultSet(
				"select * from [dbo].[address_details] " + "where [ZipCode] = '" + testData.get("ZipCode") + "';");
		testData.put("Address1", addressData.get("Address1"));
		testData.put("Address2", addressData.get("Address2"));
		testData.put("Address3", addressData.get("Address3"));
		testData.put("City", addressData.get("City"));
		testData.put("County", addressData.get("County"));
		testData.put("StateCode", addressData.get("StateCode"));
		testData.put("AddressID", addressData.get("ID"));
	}

}
