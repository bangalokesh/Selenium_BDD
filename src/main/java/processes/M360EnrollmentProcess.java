package processes;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import com.aventstack.extentreports.Status;

import enums.LocatorTypes;
import managers.PageManager;
import pageclasses.BasePage;
import pageobjects.M360MembershipObjRepo;
import utils.AccessDbconn;
import utils.Dbconn;

public class M360EnrollmentProcess extends BasePage {

	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(M360EnrollmentProcess.class.getName());
	Dbconn db = new Dbconn();
	AccessDbconn accessDb = new AccessDbconn();
	PageManager pm = new PageManager();

	public M360EnrollmentProcess() {
		driver = getWebDriver();
	}

	public void executeTestsForEnrollment() {
		try {
			String query = "select * from [dbo].[test_data_readytoenroll] where [RunMode] ='Y' and [ApplicationStatus] in ('None','') and EnrollmentType = 'Online' \r\n"
					+ "  and ApplicationType = 'NMA' and (MedCareID is not Null OR MedCareID != 'None' OR MedCareID != '') ORDER BY ID DESC";
			List<HashMap<String, String>> list = db.getListOfHashMapsFromResultSet(query);
			for (HashMap<String, String> row : list) {
				HashMap<String, String> temptestData = new HashMap<String, String>();
				for (String key : row.keySet()) {
					temptestData.put(key, row.get(key));
				}
				setrowTestData(temptestData);
				test = extent.createTest("M360 Online Member Enrollment  test for " + testData.get("MedCareID"));
				pm.getm360MAEnrollPage().processEnrollOnline();
				flushTest();
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
				reportFail("Test Failed in M360EnrollmentProcess executeTestsForEnrollment method");
				db.updateReadyToEnroll("ApplicationStatus", "Exception");
				executeTestsForEnrollment();
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void executeTestsForCallidus() {
		try {
			String query = "select M1.SupplementalID," + "RE1.ID," + "M1.MEDICAREID," + "RE1.DRIVERTYPE,"
					+ "M1.firstname," + "M1.middleInitial," + "m1.lastname," + "m1.birthdate," + "m1.ssn,"
					+ "m1.effectivemonth," + "m2.PlanID as ContarctNumber," + "m2.PlanDesignation as PlanType,"
					+ "m2.PBPID," + "m2.ApplicationDate," + "m2.ApplicationSignatureDate," + "AD.Address1," + "AD.City,"
					+ "AD.StateCode," + "AD.ZipCode, " + "C1.CountyName," + "C1.CountyID," + "AD.HomePhone,"
					+ "A1.AgentName," + "A1.AgentTIN, " + "A1.AgentPhone, " + "A1.AgentEmail, " + "A2.AgencyTIN, "
					+ "A2.AgencyName," + "A2.AgencyPhone, " + "A2.AgencyEmail "
					+ "from [VelocityTestAutomation].[dbo].[test_data_readytoenroll] RE1 "
					+ "join [VelocityTestAutomation].[dbo].[member_demographic] M1 on RE1.MedCareID = M1.MedicareID "
					+ "join [VelocityTestAutomation].[dbo].[member_enrollment] M2 on M1.MedicareID = M2.MedicareID "
					+ "join [VelocityTestAutomation].[dbo].[Address_details] AD on RE1.AddressID= AD.ID "
					+ "join [VelocityTestAutomation].[dbo].[countyMapping] C1 on AD.county = C1.countyName "
					+ "join [VelocityTestAutomation].[dbo].[agent] A1 on A1.[AgentTIN] = RE1.AgentID and A1.[AgencyTIN] = RE1.AgencyID "
					+ "join [VelocityTestAutomation].[dbo].[agency] A2 on A2.[AgencyTIN] = RE1.AgencyID "
					+ "where runmode = 'y' and [CalidusValStatus] in ('None','') "
					+ "and MemValStatus is not null and MemValStatus !='';";

			List<HashMap<String, String>> list = db.getListOfHashMapsFromResultSet(query);
			for (HashMap<String, String> row : list) {
				HashMap<String, String> temptestData = new HashMap<String, String>();
				for (String key : row.keySet()) {
					temptestData.put(key, row.get(key));
				}
				setrowTestData(temptestData);
				test = extent
						.createTest("Callidus Member, Agent and Commission test for " + testData.get("MEDICAREID"));
				pm.getCallidusHomePage().callidusValidateCustomerAgent();
				flushTest();
			}

		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Test Failed in class " + getClass().toString() + " method executeTestsForCallidus");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void executeTestsForCallidusOnAccessDB() {
		try {
			String query = "SELECT top 5 MembershipDemo.MEMBERID as SupplementalID, MembershipDemo.MEMBERLASTNAME, MembershipDemo.MEMBERMIDDLEINITIAL as middleInitial, "
					+ "MembershipDemo.MEMBERFIRSTNAME as firstname, MembershipDemo.MEMBERBIRTHDATE, MembershipDemo.CMSEFFECTIVEMONTH, MembershipEnrollment.PLANID as ContarctNumber, MembershipEnrollment.PLANDESIGNATION as PlanType,"
					+ "MembershipEnrollment.PBPID, MembershipEnrollment.APPLICATIONDATE as ApplicationDate, MembershipEnrollment.SIGNATUREDATE as ApplicationSignatureDate,"
					+ "MembershipAddress.ADDRESS1, MembershipAddress.CITY as City, MembershipAddress.STATE, MembershipAddress.ZIPCODE, MembershipAddress.HOMEPHONE, MembershipAgent.AGENTNAME, MembershipAgent.AGENTID,"
					+ "MembershipAgent.AGENCYNAME as AgencyName, MembershipAgent.AGENCYID\r\n"
					+ "FROM MembershipAgent, (MembershipAddress INNER JOIN MembershipDemo ON MembershipAddress.ID = MembershipDemo.ID) INNER JOIN MembershipEnrollment ON MembershipAddress.ID = MembershipEnrollment.ID;";
			List<HashMap<String, String>> list = accessDb.getListOfHashMapsFromResultSet(query);

			for (HashMap<String, String> row : list) {
				HashMap<String, String> temptestData = new HashMap<String, String>();
				for (String key : row.keySet()) {
					temptestData.put(key, row.get(key));
				}

				setrowTestData(temptestData);
				test = extent
						.createTest("Callidus Member, Agent and Commission test for " + testData.get("SupplementalID"));
				pm.getCallidusHomePage().callidusValidateCustomerAgent_AccessDB();

				flushTest();
			}

		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Test Failed in class " + getClass().toString() + " method executeTestsForCallidus");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void executeTestMemberValidation() {

		try {
			String query = "SELECT DISTINCT re.[ID], re.[TCID], re.[MemberLName], re.[MedCareID], re.[ElectionType], re.[AgentType]\r\n"
					+ ",re.[AgencyID], re.[DOB], re.[AddressID], re.[AgentID], re.[PCPNPI], re.[ApplicationID]\r\n"
					+ ",re.[ApplicationDate], re.[CoverageDate], re.[ApplicationType], re.[ApplicationStatus], re.[EnrollmentType], re.[PCPNPI], re.[PCPDoctorName]\r\n"
					+ ", ad.[AddressType], ad.[Address1], ad.[Address2], ad.[Address3], ad.[City], ad.[StateName]\r\n"
					+ ", ad.[StateCode], ad.[ZipCode], ad.[County],ad.[CountyCode],ad.[HomePhone],ad.[CellPhone],ad.[WorkPhone]\r\n"
					+ ", pd.[GroupName], pd.[GroupID],pd.[PlanName],pd.[ProductID],pd.[PlanID],pd.[PBPID],pd.[PBPSegmentID]\r\n"
					+ ",pd.[Designation],pd.[NewPrefix],pd.[OldPrefix], agt.[AgentTIN] AS AgentID, agt.[AgentTrueTIN] AS AgentTIN , agt.[AgentName], agt.[AgentType], agt.[AgentPhone], agt.[AgentEmail]\r\n"
					+ ",agy.[AgencyTIN] AS AgencyID, agy.[AgencyTrueTIN] AS AgencyTIN, agy.[AgencyName], agy.[AgencyType], agy.[AgencyPhone], agy.[AgencyEmail]\r\n"
					+ "FROM [dbo].[test_data_readytoenroll] re, [dbo].[address_details] ad, [dbo].[product_details] pd, [dbo].[agent] agt, [dbo].[agency] agy\r\n"
					+ "where re.[RunMode] ='Y'\r\n"
					+ "and (re.[ApplicationStatus] != 'None' AND re.[ApplicationStatus] != '' AND re.[ApplicationStatus] != 'EPEND') \r\n"
					+ "AND (CMSEnrollmentStatus = '' OR CMSEnrollmentStatus = 'None' OR CMSEnrollmentStatus = 'none' OR CMSEnrollmentStatus is NULL OR CMSEnrollmentStatus = 'CMSSUBMIT') \r\n"
					+ "and re.[AddressID] = ad.[ID] and re.[ProductPlanID] = pd.[PlanID]\r\n"
					+ "and re.[ProductPBPID] = pd.[PBPID] and re.[ProductSegmentID] = pd.[PBPSegmentID]\r\n"
					+ "and re.[AgencyID] = agy.[AgencyTIN]\r\n" + "and agy.[AgencyTIN] = agt.[AgencyTIN]\r\n"
					+ "and re.[AgentID] = agt.[AgentTIN] \r\n"
					+ "and (re.ApplicationID != 'None' AND re.ApplicationID != '' AND re.ApplicationID is NOT NULL AND re.ApplicationID != 'NULL' AND re.ApplicationID != 'null')\r\n"
					+ "ORDER BY re.ID;";
			List<HashMap<String, String>> list = db.getListOfHashMapsFromResultSet(query);
			for (HashMap<String, String> row : list) {
				HashMap<String, String> temptestData = new HashMap<String, String>();
				for (String key : row.keySet()) {
					temptestData.put(key, row.get(key));
				}
				setrowTestData(temptestData);
				test = extent.createTest("M360 Member Validation test for " + testData.get("MedCareID"));
				boolean flag1 = pm.getM360memPage().searchMember(temptestData.get("MedCareID"));
				if (flag1) {
					boolean flag2 = pm.getM360memPage().validateMemberDemographicDetails();
					boolean flag3 = pm.getM360memPage().validateMemberAddressDetails();
					boolean flag4 = pm.getM360memPage().validateEnrollmentDetails();
					boolean flag5 = pm.getM360memPage().validateMemberAgentDetails();
					boolean flag6 = pm.getM360memPage().validatePCPDetails();
					pm.getM360memPage().getDSInfo();
					pm.getM360memPage().memberAccretionDetails();
					pm.getM360memPage().memberLIS_LEPDetails();
					pm.getM360memPage().memberCOBDetails();
					if (flag2 == true && flag3 == true && flag4 == true && flag5 == true && flag6 == true)
						db.updateReadyToEnroll("MemValStatus", "PASSED");
					else
						db.updateReadyToEnroll("MemValStatus", "FAILED");
				}
				flushTest();
			}
			pm.getM360memPage().closeMemberPage();
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
				reportFail("Test Failed in M360EnrollmentProcess executeTestMemberValidation method");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void executeTestLetterValidation() {
		try {
			pm.getM360HomePage().navigateToMThreeSixty();
			String query = "SELECT re.[ID], re.[TCID], re.[MedCareID]\r\n"
					+ " ,md.[FirstName], md.[LastName], md.[MiddleInitial], md.[SupplementalID], md.[MemberID], md.[LanguageIndicator]\r\n"
					+ " ,me.[PlanID], me.[PBPID], me.[PlanDesignation], me.[EffectiveStartDate], me.[RXID]\r\n"
					+ " ,ad.[PrimaryBin], ad.[PrimaryPCN], ad.[PrimaryRxGroup]\r\n"
					+ " FROM [dbo].[test_data_readytoenroll] re, [dbo].[member_demographic] md, [dbo].[member_enrollment] me, [dbo].[member_accretion_details] ad\r\n"
					+ " WHERE re.[RunMode] ='Y'\r\n" + " AND re.[MedCareID] = md.[MedicareID]\r\n"
					+ " AND re.[MedCareID] = me.[MedicareID]\r\n" + " AND re.[MedCareID] = ad.[MedicareID]\r\n"
					+ " AND (re.[LetterStatus] IS NULL OR re.[LetterStatus] = '' OR re.[LetterStatus] = 'FAILED')\r\n"
					+ " AND re.[ApplicationStatus] = 'EAPRV'\r\n" + " AND re.[CMSEnrollmentStatus] = 'CMSAPRV';";
			List<HashMap<String, String>> list = db.getListOfHashMapsFromResultSet(query);
			for (HashMap<String, String> row : list) {
				HashMap<String, String> temptestData = new HashMap<String, String>();
				for (String key : row.keySet()) {
					temptestData.put(key, row.get(key));
				}
				setrowTestData(temptestData);
				test = extent.createTest("M360 Enrollment Letter Validation test for " + testData.get("MedCareID"));
				pm.getM360LetterRequestPage().navigateToLetterRequestPage();
				boolean flag1 = pm.getM360LetterRequestPage().searchLetterRequestMemberId(temptestData.get("MemberID"));
				boolean flag2 = pm.getM360LetterRequestPage().validateLetterRequestDetails();
				pm.getM360LetterRequestPage().navigateToApplicationEntryPage();
				pm.getM360LetterReviewPage().navigateToLetterReviewPage();
				boolean flag3 = pm.getM360LetterReviewPage().searchLetterReviewMemberId(temptestData.get("MemberID"));
				boolean flag4 = pm.getM360LetterReviewPage().validateLetterReviewDetails();
				pm.getM360LetterReviewPage().navigateToApplicationEntryPage();
				if (flag1 == true && flag2 == true && flag3 == true && flag4 == true) {
					db.updateReadyToEnroll("LetterStatus", "PASSED");
					reportFail("Letter validation failed");
				} else {
					db.updateReadyToEnroll("LetterStatus", "FAILED");
					reportFail("Letter validation failed");
				}
				flushTest();
			}
			pm.getM360LetterRequestPage().closeWindow();
			driver.quit();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Test Failed in M360EnrollmentProcess executeTestLetterValidation method");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	/*
	 * public void executeTestsForChange() { try { String query = "select * from " +
	 * "[dbo].[test_data_readytoenroll] " +
	 * "where [RunMode] ='Y' and [ApplicationStatus] is null or [ApplicationStatus] in ('None','') "
	 * + "and EnrollmentType = 'Online' " + "and ApplicationType = 'CMA' " +
	 * "and (MedCareID is not Null OR MedCareID != 'None' OR MedCareID != '')";
	 * List<HashMap<String, String>> list =
	 * db.getListOfHashMapsFromResultSet(query); for (HashMap<String, String> row :
	 * list) { HashMap<String, String> temptestData = new HashMap<String, String>();
	 * for (String key : row.keySet()) { temptestData.put(key, row.get(key)); }
	 * setrowTestData(temptestData); test =
	 * extent.createTest("M360 Online Member Change test for " +
	 * testData.get("MedCareID")); pm.getm360MAEnrollPage().processCMAOnline();
	 * 
	 * flushTest(); } } catch (Exception e) { e.printStackTrace(); try {
	 * reportFail("Test Failed in M360EnrollmentProcess executeTestsForChange method"
	 * ); } catch (IOException e1) { e1.printStackTrace(); } } }
	 */

	/*
	 * Member Validation of Access DB Extract
	 */
	public void validateMemberDataMigration() {

		String updateQuery = null;
		try {
			String query = "SELECT TOP 10000 MembershipDemo.ID, MembershipDemo.MEMBERID, MembershipDemo.MEMBERPREFIX, MembershipDemo.MEMBERFIRSTNAME, MembershipDemo.MEMBERMIDDLEINITIAL, MembershipDemo.MEMBERLASTNAME, MembershipDemo.MEMBERSSN, MembershipDemo.MEMBERBIRTHDATE, MembershipDemo.MEMBERGENDER, MembershipAddress.ID AS ADD_ID, MembershipAddress.ADDRESSTYPE, MembershipAddress.ADDRESS1, MembershipAddress.ADDRESS2, MembershipAddress.ADDRESS3, MembershipAddress.CITY, MembershipAddress.STATE, MembershipAddress.ZIPCODE, MembershipAddress.HOMEPHONE, MembershipAddress.WORKPHONE, MembershipAddress.CELLPHONE, MembershipEnrollment.ID AS ENR_ID, MembershipEnrollment.EFFECTIVESTARTDATE, MembershipEnrollment.EFFENDDATE, MembershipEnrollment.GROUPID, MembershipEnrollment.PRODUCTID, MembershipEnrollment.SUPPLEMENTALID, MembershipEnrollment.ENROLLSTATUS, MembershipEnrollment.ENROLLREASONCODE, MembershipEnrollment.PLANID, MembershipEnrollment.PBPID, MembershipEnrollment.PBPSEGMENTID, MembershipEnrollment.PLANDESIGNATION, MembershipEnrollment.ELECTIONTYPE, MembershipEnrollment.RXID, MembershipEnrollment.APPLICATIONDATE, MembershipEnrollment.SIGNATUREDATE, MembershipAgent.ID AS AGT_ID, MembershipAgent.EFFECTIVESTARTDATE AS AGENCYSTARTDATE, MembershipAgent.AGENCYNAME, MembershipAgent.AGENCYTIN, MembershipAgent.FILLER AS AGENCY_FILLER, MembershipPCP.ID AS PCP_ID, MembershipPCP.EFFECTIVEENDDATE AS PCPENDDATE, MembershipPCP.PCPNAME, MembershipPCP.EFFECTIVESTARTDATE AS PCPSTARTDATE, MembershipPCP.PCPZIP, MembershipPCP.PCPADDRESS, MembershipPCP.PCPCITY, MembershipPCP.PCPSTATE, MEMLEP.NUMBEROFUNCOVEREDMONTHS, MEMLEP.ID AS LEP_ID, MEMLEP.LEPAMOUNT, MEMLEP.LEPWAIVEDAMOUNT, MEMLIS.ID AS LIS_ID, MEMLIS.LISSUBSIDYSOURCE, MEMLIS.LISCOPAYCODE, MEMLIS.LISPERCENTCODE, MEMLIS.LISAMOUNT, MEMLIS.LISSPAPAMT, MembershipDemo.FILLER AS DEM_FILLER, MembershipEnrollment.FILLER AS ENR_FILLER, MembershipAgent.FILLER AS AGT_FILLER, MembershipPCP.FILLER AS PCP_FILLER, MEMLEP.FILLER AS LEP_FILLER, MEMLIS.FILLER AS LIS_FILLER, MembershipAddress.FILLER AS ADD_FILLER\r\n"
					+ "FROM (((((MembershipDemo LEFT JOIN MembershipAddress ON MembershipDemo.MEMBERID = MembershipAddress.MEMBERID) LEFT JOIN MembershipEnrollment ON MembershipDemo.MEMBERID = MembershipEnrollment.MEMBERID) LEFT JOIN MembershipAgent ON MembershipDemo.MEMBERID = MembershipAgent.MEMBER_ID) LEFT JOIN MembershipPCP ON MembershipDemo.MEMBERID = MembershipPCP.MEMBERID) LEFT JOIN MEMLEP ON MembershipDemo.MEMBERID = MEMLEP.MEMBERID) LEFT JOIN MEMLIS ON MembershipDemo.MEMBERID = MEMLIS.MEMBER_ID\r\n"
					+ "WHERE (((MembershipAddress.ADDRESSTYPE)=\"PRIM\") AND ((MembershipEnrollment.EFFENDDATE)=\"99991231\") AND ((MembershipPCP.EFFECTIVEENDDATE)=\"20191231\") AND ((MembershipDemo.FILLER) Is Null) AND ((MembershipAddress.EFFECTIVEENDDATE)=\"99991231\")) AND MembershipDemo.ID <= 40000\r\n"
					+ "ORDER BY MembershipDemo.MEMBERID;";
			List<HashMap<String, String>> list = accessDb.getListOfHashMapsFromResultSet(query);
			pm.getM360LoginPage().login();
			pm.getM360memPage().navigateToMemberDetails();
			for (HashMap<String, String> row : list) {
				HashMap<String, String> temptestData = new HashMap<String, String>();
				for (String key : row.keySet()) {
					temptestData.put(key, row.get(key));
				}
				setrowTestData(temptestData);
				test = extent.createTest("M360 Member Validation test for " + testData.get("MEMBERID"));
				HashMap<String, String> memberDemoData = pm.getM360memPage().getMemberDetails(testData.get("MEMBERID"));
				pm.getM360memPage().navigateToAddressTab();
				HashMap<String, String> addressData = pm.getM360memPage().getAddressDetails();
				pm.getM360memPage().validateAddressData(addressData);
				pm.getM360memPage().navigateToEnrollmentTab();
				HashMap<String, String> enrollData = pm.getM360memPage().getEnrollmentDetails();
				pm.getM360memPage().validateEnrollData(enrollData);
				pm.getM360memPage().navigateToAgentTab();
				HashMap<String, String> agentData = pm.getM360memPage().getAgentDetails();
				pm.getM360memPage().validateAgentData(agentData);
				pm.getM360memPage().navigateToPCPTab();
				HashMap<String, String> pcpData = pm.getM360memPage().getPCPDetails();
				pm.getM360memPage().validatePCPData(pcpData);
				pm.getM360memPage().navigateToLEPTab();
				HashMap<String, String> lepData = pm.getM360memPage().getLEPDetails();
				pm.getM360memPage().validateLEPData(lepData);
				pm.getM360memPage().navigateToLISTab();
				HashMap<String, String> lisData = pm.getM360memPage().getLISDetails();
				pm.getM360memPage().validateLISData(lisData);
				pm.getM360memPage().navigateToDemographicsTab();
				pm.getM360memPage().validateMemDemo(memberDemoData);
			}
		} catch (Exception e) {
			updateQuery = "UPDATE MembershipDemo SET FILLER = 'EXCEPTION' WHERE ID = "
					+ Integer.parseInt(testData.get("ID")) + ";";
			test.log(Status.FATAL, "Member Demographic Details Exception occoured in data = " + testData);
			accessDb.updateDBTestData(updateQuery);
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

	public void validatePGRToM360PCPProcess(String programCode) {
		try {
			String planQuery = "select  top 1 PlanID,PBPID,PBPSegmentId from [dbo].[product_details] "
					+ "where designation = '" + programCode + "'";
			HashMap<String, String> plan = db.getResultSet(planQuery);
			String query = PGR_M360_query(programCode);
			List<HashMap<String, String>> list = db.getListOfHashMapsFromProviderDB(query);
			// List<HashMap<String, String>> list =
			// db.getListOfHashMapsFromResultSet(query);
			pm.getm360MAEnrollPage().navigateMANewMemApl();
			pm.getm360MAEnrollPage().eligibilityCheckForMember();
			pm.getm360MAEnrollPage().retrievePlanInformation(plan.get("PlanID"), plan.get("PBPID"),
					plan.get("PBPSegmentId"));
			pm.getm360MAEnrollPage().openPCPWindow();

			for (Map<String, String> dbValues : list) {
				test = extent.createTest("PGR to M360 Provider Validation for NPI: " + dbValues.get("NPI")
						+ " Office Location: " + dbValues.get("ProvLoc") + " Program Code: " + programCode);
				pm.getm360MAEnrollPage().validate_PGR_M360_PCP_Data(dbValues);

				flushTest();
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

	private String PGR_M360_query(String programCode) {

		String query = "SELECT DISTINCT \r\n"
				+ "    pt.Name AS CATEGORY, prov.ProvLastNM AS LAST_NAME, prov.ProvFirstNm AS FIRST_NAME, prov.ProvMidNm AS MID, dt.DegTypeDesc AS DEGREE\r\n"
				+ "   , pract.FedrlTaxIDNum AS TAX_ID, prov.ProvNPIID AS NPI, pl.ProvLocAltID AS ProvLoc, l.[Std Mail Address Line 1] AS [ADDRESS], Concat(l.[Std Mail Address Line 2],l.[Std Suite Name],' ',l.[Std Suite Number]) AS SUITE, l.[Std Mail City] AS CITY\r\n"
				+ "   ,l.[Std Mail State] AS ST, l.[Std Mail ZIP] AS ZIP, l.CntyNm AS CNTY, pg.ProgDesc AS Network,pg.ProgCd as Program\r\n"
				+ "  FROM \r\n" + "    [Profisee].[data].[tProvider] prov\r\n"
				+ "  INNER JOIN [Profisee].[SPF].[PracticeParticipant] pp \r\n" + "    ON pp.ProvFK = prov.Code\r\n"
				+ "  INNER JOIN [Profisee].[data].[tPractice] pract \r\n" + "    ON pract.Code = pp.PractcFK  \r\n"
				+ "  INNER JOIN [profisee].[SPF].[PracticeParticipantProgramContract] pppc \r\n"
				+ "    ON pppc.PractcPrtcptFK = PP.PractcPrtcptPK  \r\n"
				+ "  INNER JOIN ProviderMasterHub.HUB.vProgram pg \r\n" + "    ON pg.ProgPK = pppc.ProgFK  \r\n"
				+ "  INNER JOIN [Profisee].[data].[tDG_ProviderType] pt \r\n"
				+ "       ON pt.Code = prov.ProvTypeFK\r\n" + "  INNER JOIN ProviderMasterHub.HUB.vDegreeType dt \r\n"
				+ "    ON dt.DegTypePK = prov.DegTypeFK  \r\n"
				+ "  INNER JOIN [Profisee].[SPF].[ProviderLocation] pl \r\n"
				+ "    ON pl.PractcPrtcptFK = PP.PractcPrtcptPK  \r\n"
				+ "  INNER JOIN Profisee.data.tDG_LocationType lt \r\n" + "    ON lt.ID = pl.LocTypeFK  \r\n"
				+ "  INNER JOIN [Profisee].[data].[tLocation] l \r\n" + "    ON l.Code = pl.LocFK  \r\n"
				+ "  INNER JOIN [Profisee].[SPF].[PracticeParticipantSpecialty] ps \r\n"
				+ "    ON ps.PractcPrtcptFK = PP.PractcPrtcptPK  \r\n"
				+ "  INNER JOIN [Profisee].[data].[vDG_HealthcareSpecialty] hs \r\n"
				+ "    ON hs.Code = ps.HlthcrSpcltyFK  \r\n"
				+ "  INNER JOIN [Profisee].[data].[tBenefitNetwork] bn \r\n" + "    ON bn.Program = pg.ProgCd\r\n"
				+ "  INNER JOIN [Profisee].[data].[tDG_SourceSystem] SR \r\n" + "    ON prov.SrcSysCd = SR.ID\r\n"
				+ "  WHERE\r\n" + "    pt.Name in  ('Individual') and pg.ProgCd = '" + programCode + "'\r\n"
				+ "       and  pppc.PriCarePhyscnInd = 1 and pppc.ProvAccptNewPatInd = 1 and prov.ProvNPIID > '1083086680'\r\n"
				+ "          and lt.name in ('Primary Address','Office Address')\r\n"
				+ "                and pl.ProvLocEndDt > GETDATE() ";

		/*
		 * String query = "SELECT *\r\n" +
		 * "  FROM [VelocityTestAutomation].[dbo].[pgr_m360_data]\r\n" +
		 * "  WHERE Program = '" + programCode + "' AND STATUS is NULL \r\n" +
		 * "  ORDER BY NPI ";
		 */
		return query;
	}

	public String getTRR_LetterQuery(String modSubType) {
		String query = "SELECT trr.[BeneficiaryID], trr.[FirstName], trr.[LastName], trr.[MiddleInitial], \r\n"
				+ " trr.[ContractNumber], trr.[LetterName], trr.[PartDRxID], trr.[PartDRxGroup], trr.[PartDRxBIN], trr.[PartDRxPCN] \r\n"
				+ " FROM [dbo].[trr_file_data] trr \r\n" + " WHERE RunMode = 'Y' \r\n"
				+ " AND trr.[ModificationSubType] = " + "'" + modSubType + "';";
		return query;
	}

	public void validateCompletedEnrollmentRequest() {
		try {
			String letterDesc = "COMPLETED ENROLLMENT REQUEST AND TO CONFIRM ENROLLMENT";
			String query = getTRR_LetterQuery("01161");
			executeMemberLetter(query, letterDesc);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Test Failed in validateCompletedEnrollmentRequest method");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void validateNoticeForDisenrollmentOOAStatus() {
		try {
			String letterDesc = "NOTICE FOR DISENROLLMENT DUE TO CONFIRMATION OF OOA";
			String query = getTRR_LetterQuery("01351");
			executeMemberLetter(query, letterDesc);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Test Failed in validateNoticeForDisenrollmentOOAStatus method");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void validateConfirmVoluntaryDisenrollmentFollowingTRR() {
		try {
			String letterDesc = "CONFIRM VOLUNTARY DISENROLLMENT FOLLOWING RECEIPT OF TRR";
			String query = getTRR_LetterQuery("01451");
			executeMemberLetter(query, letterDesc);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Test Failed in validateConfirmVoluntaryDisenrollmentFollowingTRR method");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void validateNotificationPlanPremiumAmountDueForReinstatement() {
		try {
			String letterDesc = "NOTIFICATION OF PLAN PREMIUM AMOUNT DUE FOR REINSTATEMENT";
			String query = getTRR_LetterQuery("01551");
			executeMemberLetter(query, letterDesc);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Test Failed in validateNotificationPlanPremiumAmountDueForReinstatement method");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void validateDisemrollDueToLossOfPartAB() {
		try {
			String letterDesc = "DISEMROLL DUE TO LOSS OF PART A/B";
			String query = getTRR_LetterQuery("01851+19701+08101");
			executeMemberLetter(query, letterDesc);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Test Failed in validateDisemrollDueToLossOfPartAB method");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void validateMAModelNoticeResearchPotentialOutOfAreaStatus() {
		try {
			String letterDesc = "MA MODEL NOTICE TO RESERARCH POTENTIAL OUT OF AREA STATUS";
			String query = getTRR_LetterQuery("08501+15401+30501");
			executeMemberLetter(query, letterDesc);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Test Failed in validateMAModelNoticeResearchPotentialOutOfAreaStatus method");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void validateModelNoticeDisenrollmentDueToDeath() {
		try {
			String letterDesc = "MODEL NOTICE OF DISENROLLMENT DUE TO DEATH";
			String query = getTRR_LetterQuery("01851+09001");
			executeMemberLetter(query, letterDesc);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Test Failed in validateModelNoticeDisenrollmentDueToDeath method");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void executeMemberLetter(String memLetterQuery, String letterDesc) {
		try {
			List<HashMap<String, String>> medicareIDs = db.getListOfHashMapsFromResultSet(memLetterQuery);
			int counter = 0;
			for (HashMap<String, String> record : medicareIDs) {
				counter++;
				HashMap<String, String> temptestData = new HashMap<String, String>();
				for (String key : record.keySet()) {
					temptestData.put(key, record.get(key));
				}
				setrowTestData(temptestData, counter);
				test = extent.createTest("Member Letter Validation test for " + temptestData.get("BeneficiaryID"));
				boolean flag = pm.getM360memPage().validateLetterInfo(temptestData.get("BeneficiaryID"), letterDesc);
				if (flag == true) {
					reportPass("Validation for member letters is successful for " + temptestData.get("BeneficiaryID"));
				} else {
					reportFail("Validation for member letters has failed for " + temptestData.get("BeneficiaryID"));
				}
				flushTest();
			}
		} catch (IOException e) {
			e.printStackTrace();
			try {
				reportFail("Test Failed in validateMemberLetter method");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void updateCVT_MemberDataMedicareID() {
		String medicareID;
		String updateQuery;
		boolean memberSearchResult = false;
		try {
			String query = "SELECT MEMBERID FROM [VelocityTestAutomation].[dbo].[CVT_MemberData] \r\n"
					+ "WHERE (MEDICARENUMBER = '' OR MEDICARENUMBER IS NULL) \r\n" + "AND (ID >= 1 AND ID <= 13000);";
			List<HashMap<String, String>> list = db.getListOfHashMapsFromResultSet(query);
			for (HashMap<String, String> memberRow : list) {
				medicareID = "";
				updateQuery = "";
				memberSearchResult = false;
				memberSearchResult = pm.getM360memPage().searchMemberBySupplementalID(memberRow.get("MEMBERID"));
				if (memberSearchResult) {
					medicareID = pm.getM360memPage().getMedicareID();
					if (!medicareID.equals("") || medicareID != null) {
						updateQuery = "UPDATE [VelocityTestAutomation].[dbo].[CVT_MemberData] \r\n"
								+ "SET MEDICARENUMBER = " + "'" + medicareID + "'" + " \r\n" + "WHERE MEMBERID = " + "'"
								+ memberRow.get("MEMBERID") + "';";
						db.sqlUpdate(updateQuery);
					}
				}
			}
			pm.getM360memPage().closeMemberPage();
			driver.quit();
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
