package processes;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import managers.PageManager;
import pageclasses.BasePage;
import utils.Const;
import utils.Dbconn;

public class HRPMemberValidationProcess extends BasePage {

	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(HRPMemberValidationProcess.class.getName());
	Dbconn db = new Dbconn();
	PageManager pm = new PageManager();

	public HRPMemberValidationProcess() {
		String appName = Const.HRPPATH;
		driver = getWinDriver(appName);
	}

	public void validateHRPMember() {
		try {
			String query = "select M1.SupplementalID, " + "RE1.ID, " + "RE1.ProductPlanName, " + "M1.MEDICAREID, "
					+ "RE1.DRIVERTYPE, " + "RE1.CoverageDate, " + "M1.firstname, " + "M1.middleInitial, "
					+ "m1.lastname, " + "m1.birthdate, " + "m1.ssn, " + "m1.effectivemonth, " + "m2.PlanID, "
					+ "m2.PBPID, " + "m2.PartAStartDate, " + "m2.PartBStartDate, " + "m2.PartDStartDate, "
					+ "m2.PCPStartDate, " + "m2.ApplicationDate, " + "m2.ApplicationSignatureDate, " + "AD.Address1, "
					+ "AD.City, " + "AD.StateCode, " + "AD.ZipCode, " + "C1.CountyName, " + "RE1.PCPNPI, "
					+ "RE1.PCPDoctorName " + "from [VelocityTestAutomation].[dbo].[test_data_readytoenroll] RE1 "
					+ "join [VelocityTestAutomation].[dbo].[member_demographic] M1 on RE1.MedCareID = M1.MedicareID "
					+ "join [VelocityTestAutomation].[dbo].[member_enrollment] M2 on M1.MedicareID = M2.MedicareID "
					+ "join [VelocityTestAutomation].[dbo].[Address_details] AD on RE1.AddressID= AD.ID "
					+ "join [VelocityTestAutomation].[dbo].[countyMapping] C1 on AD.county = C1.countyName "
					+ "where runmode = 'Y' and [ApplicationStatus] in ('EAPRV') " + "and HRPMemValStatus is null "
					+ "and CMSEnrollmentStatus ='CMSAPRV';";

			List<HashMap<String, String>> list = db.getListOfHashMapsFromResultSet(query);
			for (HashMap<String, String> row : list) {
				HashMap<String, String> temptestData = new HashMap<String, String>();
				for (String key : row.keySet()) {
					temptestData.put(key, row.get(key));
				}
				setrowTestData(temptestData);
				boolean searchflag = pm.getHrpMemSearchPage().searchMember();
				if (searchflag == true) {
					boolean viewflag = pm.getHrpMemSearchPage().viewMember();
					if (viewflag == true) {
						boolean generalFlag = pm.getHrpMemViewDetails().validateGeneralDetails();
						boolean contactFlag = pm.getHrpMemViewDetails().validateContactDetails();
						boolean benefitFlag = pm.getHrpMemViewDetails().validateBenefitPlanDetails();
						boolean medicareFlag = pm.getHrpMemViewDetails().validateMedicareDetails();
						boolean providerFlag = pm.getHrpMemViewDetails().validateProviderChoiceDetails();
						if (generalFlag == true && contactFlag == true && benefitFlag == true && medicareFlag == true
								&& providerFlag == true) {
							reportPass("HRP Validation passed for MedicareID: " + temptestData.get("MEDICAREID"));
							db.updateReadyToEnroll("HRPMemValStatus", "PASSED");
						} else {
							reportFail("HRP Validation failed for MedicareID: " + temptestData.get("MEDICAREID"));
							db.updateReadyToEnroll("HRPMemValStatus", "FAILED");
						}
					} else {
						reportFail("HRP Validation failed for MedicareID: " + temptestData.get("MEDICAREID")
								+ ". Member in Workbench");
						db.updateReadyToEnroll("HRPMemValStatus", "FAILED");
					}
					pm.getHrpMemSearchPage().clickMainSearch();
				} else {
					reportFail("HRP Validation failed for MedicareID: " + temptestData.get("MEDICAREID")
							+ ". Member not found in search");
					db.updateReadyToEnroll("HRPMemValStatus", "FAILED");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Test Failed in HRP Member Validation process validateHRPMember method");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void executeTestsForCallidus() {
		try {
			String query = "select M1.SupplementalID,RE1.ID,M1.MEDICAREID,RE1.AGENCYID,RE1.AGENTID,RE1.DRIVERTYPE from [VelocityTestAutomation].[dbo].[test_data_readytoenroll] RE1 "
					+ "join [VelocityTestAutomation].[dbo].[member_demographic] M1 on RE1.MedCareID = M1.MedicareID "
					+ "where runmode = 'y' " + "and [CalidusValStatus] in ('None','') "
					+ "and MemValStatus is not null " + "and MemValStatus !=''";
			List<HashMap<String, String>> list = db.getListOfHashMapsFromResultSet(query);
			for (HashMap<String, String> row : list) {
				HashMap<String, String> temptestData = new HashMap<String, String>();
				for (String key : row.keySet()) {
					temptestData.put(key, row.get(key));
				}
				setrowTestData(temptestData);
				pm.getCallidusHomePage().callidusValidateCustomerAgent();
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
			String query = "SELECT re.[ID], re.[TCID], re.[MemberLName], re.[MedCareID], re.[ElectionType], re.[AgentType]\r\n"
					+ " ,re.[AgencyID], re.[DOB], re.[AddressID], re.[AgentID], re.[NPI], re.[ApplicationID]\r\n"
					+ " ,re.[ApplicationDate], re.[CoverageDate], re.[ApplicationType], re.[ApplicationStatus], re.[EnrollmentType], re.[PCPNPI], re.[PCPDoctorName]\r\n"
					+ " , ad.[AddressType], ad.[Address1], ad.[Address2], ad.[Address3], ad.[City], ad.[StateName]\r\n"
					+ " , ad.[StateCode], ad.[ZipCode], ad.[County],ad.[CountyCode],ad.[HomePhone],ad.[CellPhone],ad.[WorkPhone]\r\n"
					+ ", pd.[GroupName], pd.[GroupID],pd.[PlanName],pd.[ProductID],pd.[PlanID],pd.[PBPID],pd.[PBPSegmentID]\r\n"
					+ ",pd.[Designation],pd.[NewPrefix],pd.[OldPrefix], agt.[AgentTIN], agt.[AgentName], agt.[AgentType], agt.[AgentPhone], agt.[AgentEmail]\r\n"
					+ ",agy.[AgencyTIN], agy.[AgencyName], agy.[AgencyType], agy.[AgencyPhone], agy.[AgencyEmail]\r\n"
					+ "FROM [dbo].[test_data_readytoenroll] re, [dbo].[address_details] ad, [dbo].[product_details] pd, [dbo].[agent] agt, [dbo].[agency] agy\r\n"
					+ " where re.[RunMode] ='Y'\r\n"
					+ " and (re.[ApplicationStatus] != 'None' AND re.[ApplicationStatus] != '')  \r\n"
					+ " and (re.[MemValStatus] = '' OR re.[MemValStatus] = null OR re.[MemValStatus] = 'none' OR re.[MemValStatus] = 'None') \r\n"
					+ " and re.[AddressID] = ad.[ID]\r\n" + " and re.[ProductPlanID] = pd.[PlanID]\r\n"
					+ " and re.[ProductPBPID] = pd.[PBPID]\r\n" + " and re.[ProductSegmentID] = pd.[PBPSegmentID]"
					+ " and re.[AgencyID] = agy.[AgencyTIN]"
					+ " and (re.[MemValStatus] = 'None' OR re.[MemValStatus] = null OR re.[MemValStatus] = '')"
					+ " and re.[AgentID] = agt.[AgentTIN];";
			List<HashMap<String, String>> list = db.getListOfHashMapsFromResultSet(query);
			for (HashMap<String, String> row : list) {
				HashMap<String, String> temptestData = new HashMap<String, String>();
				for (String key : row.keySet()) {
					temptestData.put(key, row.get(key));
				}
				setrowTestData(temptestData);
				boolean flag1 = pm.getM360memPage().searchMember(temptestData.get("MedCareID"));
				boolean flag2 = pm.getM360memPage().validateMemberDemographicDetails();
				boolean flag3 = pm.getM360memPage().validateMemberAddressDetails();
				boolean flag4 = pm.getM360memPage().validateEnrollmentDetails();
				boolean flag5 = pm.getM360memPage().validateMemberAgentDetails();
				boolean flag6 = pm.getM360memPage().validatePCPDetails();
				if (flag1 == true && flag2 == true && flag3 == true && flag4 == true && flag5 == true && flag6 == true)
					db.updateReadyToEnroll("MemValStatus", "PASSED");
				else
					db.updateReadyToEnroll("MemValStatus", "FAILED");
			}
			pm.getM360memPage().closeMemberPage();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Test Failed in M360EnrollmentProcess executeTestMemberValidation method");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

}
