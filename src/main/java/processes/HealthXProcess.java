package processes;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.junit.Test;

import managers.PageManager;
import pageclasses.BasePage;
import utils.Dbconn;

public class HealthXProcess extends BasePage {

	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(HealthXProcess.class.getName());
	Dbconn db = new Dbconn();
	PageManager pm = new PageManager();

	public HealthXProcess() {
		driver = getWebDriver();
	}
	
	public void executeTestsForProvider(boolean setAdmin) {
		try {
			String query = "SELECT hxpd.NPI, hxpd.UserName, hxpd.Password, \r\n"
					+ "[bpao].[FIRST NAME], [bpao].[LAST NAME], [bpao].[TAX ID], bpao.NPI1, bpao.PHONE, bpao.FAX, \r\n"
					+ "[bpao].[PRIMARY ADDRESS], bpao.SUITE, bpao.CITY, bpao.ST, bpao.ZIP, [bpao].[GROUP NAME], [bpao].[PRIMARY SPECIALTY] \r\n"
					+ "FROM [dbo].[HealthX_ProviderData] hxpd \r\n"
					+ "INNER JOIN  [dbo].[BCBSAZProv_ActiveOnly] bpao \r\n"
					+ "ON hxpd.NPI = bpao.NPI1 \r\n"
					+ "WHERE [bpao].CATEGORY = 'Individual' \r\n"
					+ "AND RunMode = 'Y';";
			List<HashMap<String, String>> list = db.getListOfHashMapsFromResultSet(query);
			for (HashMap<String, String> row : list) {
				HashMap<String, String> temptestData = new HashMap<String, String>();
				for (String key : row.keySet()) {
					temptestData.put(key, row.get(key));
				}
				setrowTestData(temptestData);
				test = extent.createTest("HealthX Provider validation test for " + testData.get("NPI"));
				if (testData.get("UserName") == null || testData.get("UserName").equals("")) {
					boolean flagCreateProvider = createProvider();
					if (flagCreateProvider) {
						validateProviderInfo();
						pm.getHealthXLoginPage().logoutAdminPortal();
					}
				} else if (setAdmin) {
					processProviderAsAdmin();
					driver.close();
				} else {
					pm.getHealthXLoginPage().loginAsProvider(testData.get("UserName"), testData.get("Password"));
					validateProviderInfo();
					pm.getHealthXLoginPage().logoutAdminPortal();
				}
				flushTest();
			}
			driver.quit();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Exception in HealthXProcess executeTestsForProvider method");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	@Test
	public void executeTestsForMember(boolean setAdmin) {
		try {
			String query = "SELECT hxmd.MemberID, hxmd.UserName, hxmd.Password, \r\n" + 
					"md.FirstName, md.LastName, md.MiddleInitial, md.BirthDate, \r\n" + 
					"mad.PrimaryBin, mad.PrimaryPCN, mad.PrimaryRxGroup, \r\n" + 
					"pd.PlanName, re.PCPDoctorName,  re.ProductPlanID, re.ProductPBPID,\r\n" + 
					"hcp.PCPOfficeVisit, hcp.SpecialistOfficeVisit, hcp.EmergencyRoom, hcp.MentalHealthOfficeVisit,\r\n" + 
					"hcp.PhysicalTherapy, hcp.LaboratoryServices, hcp.XRay, hcp.CT_MRI_MRA_SPECT, hcp.InpatientHospitalCoverage,\r\n" + 
					"hcp.OutpatientServicesHospital, hcp.OutpatientServicesASC, hcp.MaximumOutOfPocket, hcp.County, \r\n" +
					"cl.CPTCodeID, cl.Amount, cl.DateTimeFromPeriod,\r\n" + 
					"rtc.ClaimNumber, rtc.BillingProviderNPI, \r\n" + 
					"rtc.Validation835ClaimStatus, rtc.ClaimPaymentAmount, rtc.PatientResponsibilityAmount \r\n" + 
					"FROM [dbo].[HealthX_MemberData] hxmd \r\n" + 
					"INNER JOIN [dbo].[member_demographic] md \r\n" + 
					"ON hxmd.MemberID = md.SupplementalID \r\n" + 
					"INNER JOIN [dbo].[member_accretion_details] mad \r\n" + 
					"ON md.SupplementalID = mad.SupplementalID \r\n" + 
					"INNER JOIN [dbo].[test_data_readytoenroll] re \r\n" + 
					"ON md.MedicareID = re.MedCareID \r\n" + 
					"INNER JOIN [dbo].[test_data_readytoclaims] rtc\r\n" + 
					"ON md.MedicareID = rtc.MedicareID \r\n" +
					"INNER JOIN [dbo].[test_data_claimline] cl\r\n" + 
					"ON SUBSTRING(cl.ClaimLineNumber, 1, (LEN(cl.ClaimLineNumber) - 1)) = rtc.ClaimNumber \r\n" + 
					"INNER JOIN [dbo].[product_details] pd \r\n" + 
					"ON re.[ProductPlanID] = pd.PlanID \r\n" + 
					"AND re.[ProductPBPID] = pd.PBPID \r\n" + 
					"INNER JOIN [HealthX_CoPayments] hcp\r\n" + 
					"ON pd.PlanID = hcp.PlanID\r\n" + 
					"AND pd.PBPID = hcp.PBPID\r\n" + 
					"WHERE hxmd.RunMode = 'Y'\r\n";
					//"AND hxmd.MemberID LIKE 'XBU%'";
			List<HashMap<String, String>> list = db.getListOfHashMapsFromResultSet(query);
			for (HashMap<String, String> row : list) {
				HashMap<String, String> temptestData = new HashMap<String, String>();
				for (String key : row.keySet()) {
					temptestData.put(key, row.get(key));
				}
				setrowTestData(temptestData);
				test = extent.createTest("HealthX Member validation test for " + testData.get("MemberID"));
				if (testData.get("UserName") == null || testData.get("UserName").equals("")) {
					boolean flagCreateMember = createMember();
					if (flagCreateMember) {
						validateMemberInfo();
						pm.getHealthXLoginPage().logoutMemberPortal();
					}
				} else if (setAdmin) {
					processMemberAsAdmin();
					//driver.close();
				} else {
					pm.getHealthXLoginPage().loginAsMember(testData.get("UserName"), testData.get("Password"));
					validateMemberInfo();
					pm.getHealthXLoginPage().logoutMemberPortal();
				}
				flushTest();
			}
			driver.quit();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Exception in HealthXProcess executeTestsForMember method");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public boolean createProvider() {
		boolean flag = false;
		try {
			pm.getHealthXLoginPage().launchProviderPortalPage();
			boolean flag1 = pm.getHealthXProviderPage().createProviderAccountNavigation();
			boolean flag2 = pm.getHealthXProviderPage().createProviderContactInfo();
			if(flag1 == false || flag2 == false) {
				flag = false;
			} else {
				HashMap<String, String> credentialInfo = pm.getHealthXAdminPage().createLoginAccountDetails(
						testData.get("FIRST NAME"), testData.get("LAST NAME"), "Provider");
				if(!credentialInfo.isEmpty()) {
					String updateQuery = "UPDATE [dbo].[HealthX_ProviderData] \r\n" 
							+ "SET UserName = " + "'" + credentialInfo.get("UserName") + "'" 
							+ ", Password = " + "'" + credentialInfo.get("Password") + "'\r\n" 
							+ "WHERE NPI = " + "'" + testData.get("NPI") + "';";
					db.sqlUpdate(updateQuery);
					credentialInfo.clear();
					flag = true;
				} else {
					flag = false;
				}
			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			try {
				reportFail("Exception in HealthXProcess createProvider method.");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}
	
	public boolean createMember() {
		boolean flag = false;
		try {
			pm.getHealthXLoginPage().launchMemberPortalPage();
			boolean flag1 = pm.getHealthXMemberPage().createMemberAccountNavigation();
			boolean flag2 = pm.getHealthXMemberPage().createMemberAccountDetails();
			if(flag1 == false || flag2 == false) {
				flag = false;
			} else {
				HashMap<String, String> credentialInfo = pm.getHealthXAdminPage().createLoginAccountDetails(
						testData.get("FirstName"), testData.get("LastName"), "Member");
				if(!credentialInfo.isEmpty()) {
					String updateQuery = "UPDATE [dbo].[HealthX_MemberData] \r\n" 
							+ "SET UserName = " + "'" + credentialInfo.get("UserName") + "'" 
							+ ", Password = " + "'" + credentialInfo.get("Password") + "'\r\n" 
							+ "WHERE MemberID = " + "'" + testData.get("MemberID") + "';";
					db.sqlUpdate(updateQuery);
					credentialInfo.clear();
					flag = true;
				} else {
					flag = false;
				}
			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			try {
				reportFail("Exception in HealthXProcess createMember method.");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}
	
	public void processProviderAsAdmin() {
		try {
			pm.getHealthXLoginPage().loginAsAdmin();
			pm.getHealthXAdminPage().navigateToProviderTab();
			boolean flagSearchUserManager = pm.getHealthXAdminPage().searchUserManager(testData.get("UserName"));
			if(flagSearchUserManager) {
				pm.getHealthXAdminPage().imitateUserManager("Provider Portal");
				validateProviderInfo();
				pm.getHealthXLoginPage().logoutAdminPortal();
			} else {
				reportFail("Unable to search provider. \r\n UserName: " + testData.get("UserName") + " \r\n NPI: " + testData.get("NPI"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Exception in HealthXProcess processProviderAsAdmin method!");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public void processMemberAsAdmin() {
		try {
			pm.getHealthXLoginPage().loginAsAdmin();
			pm.getHealthXAdminPage().navigateToMemberTab();
			boolean flagSearchUserManager = pm.getHealthXAdminPage().searchUserManager(testData.get("UserName"));
			if(flagSearchUserManager) {
				pm.getHealthXAdminPage().imitateUserManager("Member Portal");
				validateMemberInfo();
				pm.getHealthXLoginPage().logoutMemberPortal();
			} else {
				reportFail("Unable to search member. \r\n UserName: " + testData.get("UserName") + " \r\n MemberID: " + testData.get("MemberID"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Exception in HealthXProcess processMemberAsAdmin method!");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public void validateMemberInfo() {
		try {
			pm.getHealthXMemberPage().navigateToBenefitsTab();
			boolean flag1 = pm.getHealthXMemberPage().validateMemberInfoDetails();
			boolean flag2 = pm.getHealthXMemberPage().validatePCPInformation();
			boolean flag3 = pm.getHealthXMemberPage().validateCoPayInformation();
			boolean flag4 = pm.getHealthXMemberPage().validateMaximumOutOfPocketInformation();
			pm.getHealthXMemberPage().navigateToClaimsTab();
			boolean flag5 = pm.getHealthXMemberPage().processClaimsInformation();
			if(flag1 && flag2 && flag3 && flag4 && flag5) {
				reportPass("Validation for member in HealthX is successful for member ID: " + testData.get("MemberID"));
			} else {
				reportFail("Validation for member in HealthX has failed for member ID: " + testData.get("MemberID"));
			}
		}  catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Exception in validateMemberInfo method for member ID: " + testData.get("MemberID"));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public void validateProviderInfo() {
		boolean flag1 = false;
		boolean flag2 = false;
		boolean flag3 = false;
		boolean flag4 = false;
		boolean flag5 = false;
		boolean flagSearchByProviderNPI = false;
		try {
			pm.getHealthXProviderPage().navigateToEligibilityAndBenefitsTab();
			flagSearchByProviderNPI = pm.getHealthXProviderPage().searchMemberByProviderNPI_Dropdown(testData.get("NPI"));
			if(flagSearchByProviderNPI) {
				/*
				 * 1. It is possible that a query will need to be in each validation method.
				 * 2. If this is the case, we will query the patient of the provider, using the MemberID
				 * 3. This will be done for all the members for the provider (so maybe use a for loop for the number of members)
				 * 4. Use a count variable, if at least one validation is invalid, increment the count variable
				 * 5. This count variable will be used to flag the validation as false, where 0 = true and any positive number = false
				 */
				HashMap<String, String> UIsubscriberDetails = pm.getHealthXProviderPage().getSubscriberDetails();
				flag1 = pm.getHealthXProviderPage().validateSubscriberDetails(UIsubscriberDetails);
				HashMap<String, String> UIpcpDetails = pm.getHealthXProviderPage().getPCPInformation();
				flag2 = pm.getHealthXProviderPage().validatePCPInformation(UIpcpDetails);
				HashMap<String, String> UIcopaymentsAndAccumulators = pm.getHealthXProviderPage().getCoPaymentsAndAccumulators();
				flag3 = pm.getHealthXProviderPage().validateCoPaymentsAndAccumulators(UIcopaymentsAndAccumulators);
			}
			pm.getHealthXProviderPage().navigateToClaimsAndPaymentTab();
			flagSearchByProviderNPI = pm.getHealthXProviderPage().selectClaimsProvider();
			if(flagSearchByProviderNPI) {
				flag4 =  pm.getHealthXProviderPage().processMemberClaimNumbers();
			}
			/*
			 * Other validation methods will go here (Authorizations & Referrals)
			 */
			if(flag1 && flag2 && flag3 && flagSearchByProviderNPI) {
				reportPass("Provider validation is successful for: \r\n UserName: " + testData.get("UserName") + " \r\n NPI: " + testData.get("NPI"));
			} else {
				if(!flagSearchByProviderNPI && flag4 && flag5) {
					/*
					 * Have another if statement for the claims and authorizations and check if they are validated
					 */
					reportPass("Provider validation is sucessful, but no patients found for: "
							+ "\r\n UserName: " + testData.get("UserName") + " \r\n NPI: " + testData.get("NPI"));
				} else {
					reportFail("Provider validation has failed for: \r\n UserName: " + testData.get("UserName") + " \r\n NPI: " + testData.get("NPI"));
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Exception in validateProviderInfo method for NPI ID: " + testData.get("NPI"));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
