package processes;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import com.aventstack.extentreports.Status;

import enums.LocatorTypes;
import managers.PageManager;
import pageclasses.BasePage;
import pageobjects.GuidingCareObjRepo;
import utils.AccessDbconn;
import utils.Dbconn;

public class GuidingCareProviderProcess extends BasePage {

	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(GuidingCareProviderProcess.class.getName());
	Dbconn db = new Dbconn();
	PageManager pm = new PageManager();
	AccessDbconn accessDb = new AccessDbconn();

	public String getGuidingCareProviderQuery(String benefitNetwork) {
		String guidingCareProviderQuery = "SELECT \r\n" + "PROVIDER_ID \r\n" + ",BENEFIT_NETWORK \r\n"
				+ ",PROVIDER_TYPE \r\n" + ",FACILITY_NAME \r\n" + ",PROVIDER_NAME \r\n" + ",GENDER \r\n"
				+ ",TAX_ID_VALUE \r\n" + ",NPI_VALUE \r\n" + ",SPECIALTY_DESC \r\n" + ",LANGUAGE_SPOKEN_DESC \r\n"
				+ ",Telephone \r\n" + ",AcceptingNewPatients \r\n" + ",AddressData \r\n" + ",LOB_value \r\n"
				+ ",EffectiveDate \r\n" + ",TermDate \r\n"
				+ "FROM [VelocityTestAutomation].[dbo].[GuidingCare_Provider] \r\n" + "WHERE BENEFIT_NETWORK = '"
				+ benefitNetwork + "' \r\n" + "AND (STATUS IS NULL) \r\n" + "ORDER BY EffectiveDate DESC;";

		return guidingCareProviderQuery;
	}

	public void executeGC_ProviderMA_BlueAdvantage_P3_HMO() {
		String query = getGuidingCareProviderQuery("MA BlueAdvantage P3 HMO");
		validateGuidingCareProvider(query, "MA BlueAdvantage P3 HMO");
	}

	public void executeGC_ProviderMA_BlueAdvantage_HMO() {
		String query = getGuidingCareProviderQuery("MA BlueAdvantage HMO");
		validateGuidingCareProvider(query, "MA BlueAdvantage HMO");
	}

	public void executeGC_ProviderMA_BlueJourney_PPO_Maricopa_Pima() {
		String query = getGuidingCareProviderQuery("MA BlueJourney PPO Maricopa Pima");
		validateGuidingCareProvider(query, "MA BlueJourney PPO Maricopa Pima");
	}

	public void executeGC_ProviderMA_BluePathway_P3_HMO() {
		String query = getGuidingCareProviderQuery("MA BluePathway P3 HMO");
		validateGuidingCareProvider(query, "MA BluePathway P3 HMO");
	}

	public void executeGC_ProviderMA_BluePathway_Maricopa_HMO() {
		String query = getGuidingCareProviderQuery("MA BluePathway Maricopa HMO");
		validateGuidingCareProvider(query, "MA BluePathway Maricopa HMO");
	}

	public void validateGuidingCareProvider(String guidingCareProviderQuery, String benNetwork) {
		try {
			List<HashMap<String, String>> list = db.getListOfHashMapsFromResultSet(guidingCareProviderQuery);
			int counter = 0;
			for (HashMap<String, String> row : list) {
				counter++;
				HashMap<String, String> temptestData = new HashMap<String, String>();
				for (String key : row.keySet()) {
					temptestData.put(key, row.get(key));
				}
				setrowTestData(temptestData, counter);
				test = extent.createTest("Guiding Care Provider Validation test for " + temptestData.get("PROVIDER_ID")
						+ "_" + temptestData.get("BENEFIT_NETWORK"));
				boolean flag1 = pm.getGuidingCareProviderPage().searchProviderCode(temptestData.get("PROVIDER_ID"),
						temptestData.get("BENEFIT_NETWORK"));
				Boolean flag2 = true;
				if (flag1 == true) {
					flag2 = pm.getGuidingCareProviderPage().validateProviderDetails(benNetwork);
				}
				if (flag1 == true && flag2 == true)
					reportPass("Guiding Care provider validation is successful");
				else
					reportFail("Guiding Care provider validation failed");
				flushTest();
			}
			pm.getGuidingCarePage().returnToHomePage();
			wait(5);
			closeBrowser();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Test Failed in validateGuidingCareProvider method");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void validateMemberDataInGuidingCare() {
		pm.getGuidingCarePage().navigateToMemberPage();
		try {
			String query = "SELECT TOP 1000 MembershipDemo.ID AS MEM_ID, MembershipDemo.MEMBERID, MembershipDemo.EMERGENCYNAME, MembershipDemo.EMERGENCYRELATIONSHIP, "
					+ "MembershipDemo.MEMBERFIRSTNAME, MembershipDemo.MEMBERMIDDLEINITIAL, MembershipDemo.MEMBERLASTNAME, MembershipDemo.EMERGENCYPHONE, "
					+ "MembershipDemo.MEMBERBIRTHDATE, MembershipDemo.MEMBERGENDER, MembershipDemo.EMERGENCYEMAIL, MembershipAddress.ID AS ADD_ID, "
					+ "MembershipAddress.ADDRESSTYPE, MembershipAddress.ADDRESS1, MembershipAddress.ADDRESS2, MembershipAddress.COUNTYCODE, "
					+ "MembershipAddress.CITY, MembershipAddress.STATE, MembershipAddress.ZIPCODE, MembershipAddress.HOMEPHONE, "
					+ "MembershipEnrollment.ID AS ENR_ID, "
					+ "MembershipEnrollment.EFFECTIVESTARTDATE, MembershipEnrollment.EFFENDDATE, MembershipEnrollment.GROUPID, "
					+ "MembershipEnrollment.PLANID, MembershipEnrollment.PBPID, "
					+ "MembershipPCP.ID AS PCP_ID, MembershipPCP.EFFECTIVESTARTDATE AS PCPSTARTDATE, MembershipPCP.EFFECTIVEENDDATE AS PCPENDDATE "
					+ "FROM (((MembershipDemo INNER JOIN MembershipAddress ON MembershipDemo.MEMBERID = MembershipAddress.MEMBERID) "
					+ "INNER JOIN MembershipEnrollment ON MembershipDemo.MEMBERID = MembershipEnrollment.MEMBERID) "
					+ "INNER JOIN MembershipPCP ON MembershipDemo.MEMBERID = MembershipPCP.MEMBERID) "
					+ "WHERE (MembershipDemo.ID > 18770 AND MembershipDemo.ID < 23771 AND ((MembershipDemo.FILLER) Is Null) AND ((MembershipAddress.ADDRESSTYPE)=\"PRIM\") AND ((MembershipEnrollment.EFFENDDATE)=\"99991231\") "
					+ "AND ((MembershipPCP.EFFECTIVEENDDATE)=\"20191231\") AND ((MembershipAddress.EFFECTIVEENDDATE)=\"99991231\"))"
					+ "ORDER BY MembershipDemo.MEMBERID;";

			List<HashMap<String, String>> list = accessDb.getListOfHashMapsFromResultSet(query);

			for (HashMap<String, String> row : list) {
				HashMap<String, String> temptestData = new HashMap<String, String>();
				for (String key : row.keySet()) {
					temptestData.put(key, row.get(key));
				}
				setrowTestData(temptestData, Integer.parseInt(temptestData.get("MEM_ID")));
				test = extent.createTest("M360 Member Validation test for " + testData.get("MEMBERID"));
				boolean memberIDFound = pm.getguidingCareMemberDetails()
						.navigateAndSearchByXBU(testData.get("MEMBERID"));
				if (memberIDFound == true) {
					HashMap<String, String> personalData = pm.getguidingCareMemberDetails().getPersonalDetails();
					pm.getguidingCareMemberDetails().validatePersonalDetails(personalData);
					HashMap<String, String> addressData = pm.getguidingCareMemberDetails().getAddressDetails();
					pm.getguidingCareMemberDetails().validateAddressDetails(addressData);
					HashMap<String, String> memberIdentifiersData = pm.getguidingCareMemberDetails()
							.getMemberIdentifiers();
					pm.getguidingCareMemberDetails().validateMemberIdentifiers(memberIdentifiersData);
					HashMap<String, String> eligibilitydetailsData = pm.getguidingCareMemberDetails()
							.getEligibilitydetails();
					pm.getguidingCareMemberDetails().validateEligibilitydetails(eligibilitydetailsData);
					HashMap<String, String> careGiverData = pm.getguidingCareMemberDetails().getCareGiverDetails();
					if (careGiverData.get("flag").trim().equalsIgnoreCase("true")) {
						test.log(Status.INFO, "MEMBERID " + testData.get("MEMBERID") + " has a care giver ");
						pm.getguidingCareMemberDetails().validateCareGiver(careGiverData);
					} else {
						test.log(Status.INFO, "MEMBERID " + testData.get("MEMBERID") + " has no care giver ");
					}
					HashMap<String, String> careTeamData = pm.getguidingCareMemberDetails().getCareTeamDetails();
					if (careTeamData.get("flag").trim().equalsIgnoreCase("true")) {
						test.log(Status.INFO, "MEMBERID " + testData.get("MEMBERID") + " has a care team ");
						pm.getguidingCareMemberDetails().validateCareTeam(careTeamData);
					} else {
						test.log(Status.INFO, "MEMBERID " + testData.get("MEMBERID") + " has no care team ");
					}
				} else {
					test.log(Status.FAIL, "MEMBERID " + testData.get("MEMBERID") + " is not found in Guiding Care ");
					pm.getGuidingCarePage().navigateToMemberPage();
				}
				flushTest();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void createPreauthorizationActivity() {
		try {
			String query = "SELECT [ID]\r\n" + "      ,[MedicareID]\r\n" + "      ,[DecisionStatusToBe]\r\n"
					+ "      ,[PatientType]\r\n" + "      ,[PreAuthType]\r\n" + "      ,[ActivityType]\r\n"
					+ "      ,[DecisionRole]\r\n"
					+ "  FROM [VelocityTestAutomation].[dbo].[guidingCare_Preauthorization_Activity] where MedicareID is not null and [status]  is null";

			List<HashMap<String, String>> list = db.getListOfHashMapsFromResultSet(query);
			int counter = 0;
			for (HashMap<String, String> row : list) {
				counter++;
				HashMap<String, String> temptestData = new HashMap<String, String>();
				for (String key : row.keySet()) {
					temptestData.put(key, row.get(key));
				}
				setrowTestData(temptestData, counter);
				test = extent.createTest("Creating Preauthorization for member " + testData.get("MedicareID"));

				boolean memberIDFound = pm.getguidingCareMemberDetails().searchMBI(testData.get("MedicareID"));

				if (memberIDFound == true) {

					if (testData.get("PatientType").equals("In Patient")) {
						pm.getGuidingCarePreAuthorization().preauthorization_InPatient();
					} else if (testData.get("PatientType").equals("Out Patient")) {
						pm.getGuidingCarePreAuthorization().preauthorization_OutPatient();
					}
					pm.getGuidingCarePreAuthorizationActivity().createPreAuthActivity(testData.get("ActivityType"),
							testData.get("DecisionRole")); // P2P Review, UM 1st Attempt, UM2nd Attempt, UM Lack of Info

					// pm.getGuidingCarePreAuthorizationActivity().completePreAuthActivity(authID);

					// logout
				}
				test.log(Status.INFO, "Creating " + counter + " record out of " + list.size());
				flushTest();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void decisionPreAuthorizationActivity() {
		try {
			String query = "SELECT [ID],[PreAuth_ID]\r\n" + "      ,[MedicareID]\r\n"
					+ "      ,[DecisionStatusToBe]\r\n" + "      ,[DecisionRole]\r\n"
					+ "  FROM [VelocityTestAutomation].[dbo].[guidingCare_Preauthorization_Activity] where [status] = 'Pending' and MedicareID is not null";

			List<HashMap<String, String>> list = db.getListOfHashMapsFromResultSet(query);
			int counter = 0;
			for (HashMap<String, String> row : list) {
				counter++;
				HashMap<String, String> temptestData = new HashMap<String, String>();
				for (String key : row.keySet()) {
					temptestData.put(key, row.get(key));
				}
				setrowTestData(temptestData);
				test = extent.createTest("Decision PreAuth " + testData.get("MedicareID"));

				pm.getGuidingCareLoginPage().loginAsRole(testData.get("DecisionRole"));
				boolean memberIDFound = pm.getguidingCareMemberDetails().searchMBI(testData.get("MedicareID"));
				if (memberIDFound == true) {
					pm.getguidingCareDecision().navigateToActivitiesPage();
					pm.getGuidingCarePreAuthorizationActivity().completePreAuthActivity(testData.get("PreAuth_ID"));
					pm.getguidingCareDecision().navigateToDecisionsPage();
					pm.getguidingCareDecision().DecisionPreauthApprove();
				}
			}
			test.log(Status.INFO, "Creating " + counter + " record out of " + list.size());
			flushTest();

		} catch (Exception e) {

		}
	}

	public List<String> getAllLOBplanIds() {
		String query = "select DISTINCT PLANID FROM [VelocityTestAutomation].[dbo].[member_enrollment]";
		List<String> planList = db.getListFromQuery(query);
		return planList;
	}

	public List<String> selectMemberforPlan(String planID) {
		String query = "select MEDICAREID FROM [VelocityTestAutomation].[dbo].[member_enrollment]\r\n"
				+ "  where PLANID = '" + planID + "'";
		List<String> memberList = db.getListFromQuery(query);
		return memberList;
	}

	public void validateComplaints(String planID) {
		try {
			// all complaint types
			String allComplaintTypes[] = { "Grievance", "Appeal", "QOC" };
			List<String> memberList = selectMemberforPlan(planID);
			String medicareID = null;
			boolean isLOBpresent = false;
			boolean navToComplaintsPage = false;
			boolean isMemberPresent = false;

			for (String member : memberList) {
				// navigate to create new complaint page
				isMemberPresent = pm.getguidingCareMemberDetails().searchMBI(member);
				if (isMemberPresent) {
					navToComplaintsPage = pm.getGuidingCareComplaintsPage().navigateToComplaintsPage(member,
							allComplaintTypes[1]);
					if (navToComplaintsPage) {
						isLOBpresent = pm.getGuidingCareComplaintsPage().checkPresenceofLOB(planID);
						if (isLOBpresent) {
							medicareID = member;
							break;
						}
					}
				} else {
					reportFail("Failed to navigate to Complaints page");
				}

			}
			if (medicareID != null) {
				for (int i = 0; i < allComplaintTypes.length; i++) {
					String complaintType = allComplaintTypes[i];
					logger.info("Member ID-" + medicareID);
					String queryAllCompltTypes = "select * from [VelocityTestAutomation].[dbo].[GuidingCare_ComplaintsTAT]\r\n"
							+ "  where LOB LIKE '" + planID + "%' and\r\n" + "  COMPLAINT_TYPE = '" + complaintType
							+ "'";
					List<HashMap<String, String>> listAllcomplaints = db
							.getListOfHashMapsFromResultSet(queryAllCompltTypes);

					// Create and Validate All complaints for given complaint type
					for (HashMap<String, String> row : listAllcomplaints) {
						String complaintID = "";
						String status = "";
						test = extent.createTest("Vallidate " + complaintType + "-type Complaint for Plan-"
								+ row.get("LOB") + " of type " + row.get("COMPLAINT_CLASS") + " - "
								+ row.get("COMPLAINT_CATEGORY"));
						// boolean complaint_created = false;
						if (!isElementPresent(LocatorTypes.xpath,
								GuidingCareObjRepo.GuidingCareComplaints.goToCareRecord_xpath)) {
							navigateBack();
						}
						wait(3);
						elementClickableWait(LocatorTypes.xpath,
								GuidingCareObjRepo.GuidingCareComplaints.goToCareRecord_xpath, 30);
						getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareComplaints.goToCareRecord_xpath)
								.click();
						complaintID = pm.getGuidingCareComplaintsPage().createComplaint(medicareID, row, complaintType);
						if (complaintID.length() > 0) {
							status = pm.getGuidingCareComplaintsPage().validateTAT(row);
						} else {
							reportFail("Complaint is not created successfully");
							status = "FAIL";
						}

						String updateQuery = "UPDATE [VelocityTestAutomation].[dbo].[GuidingCare_ComplaintsTAT]\r\n"
								+ "SET STATUS = '" + status + "',\r\n" + "COMPLAINT_ID = '" + complaintID + "'\r\n"
								+ "where ID = '" + row.get("ID") + "'";
						db.updateDBTestData(updateQuery);
						flushTest();
					}

					// Create Standard appeal and then Expedited it and validate TAT - for Appeal
					// and Grievance only
					if (complaintType.equalsIgnoreCase("Appeal") || complaintType.equalsIgnoreCase("Grievance")) {
						String queryExpdite = "select * from [VelocityTestAutomation].[dbo].[GuidingCare_ComplaintsTAT]\r\n"
								+ "  where LOB LIKE '" + planID + "%' and\r\n" + "  COMPLAINT_TYPE = '" + complaintType
								+ "' and\r\n" + "  COMPLAINT_CLASS LIKE '%Expedited%'";
						List<HashMap<String, String>> listExpdt = db.getListOfHashMapsFromResultSet(queryExpdite);
						for (HashMap<String, String> expdtRow : listExpdt) {
							String complaintID = "";
							String status = "";
							test = extent.createTest("Vallidate " + complaintType + "-type Complaint for Plan-"
									+ expdtRow.get("LOB") + " of type " + expdtRow.get("COMPLAINT_CLASS") + " - "
									+ expdtRow.get("COMPLAINT_CATEGORY"));
							// get standard complaint data of same complaint category and create standard
							// complaint first
							String[] comp_class = expdtRow.get("COMPLAINT_CLASS").split(" ");
							String queryStd = "SELECT TOP 1 [ID]\r\n" + "      ,[LOB]\r\n"
									+ "      ,[COMPLAINT_TYPE]\r\n" + "      ,[COMPLAINT_CLASS]\r\n"
									+ "      ,[COMPLAINT_CATEGORY]\r\n" + "      ,[TAT_TIME]\r\n"
									+ "      ,[TAT_TIME_TYPE]\r\n"
									+ "      ,[RECEIVED_DATETIME] from [VelocityTestAutomation].[dbo].[GuidingCare_ComplaintsTAT]\r\n"
									+ "  where LOB LIKE '" + planID + "%' and\r\n" + "  COMPLAINT_TYPE = '"
									+ complaintType + "' and\r\n" + "  COMPLAINT_CLASS = '" + comp_class[0] + " "
									+ comp_class[1] + " Standard'";
							boolean expediteComplntCreated = false;
							HashMap<String, String> stdRow = db.getResultSet(queryStd);
							if (!isElementPresent(LocatorTypes.xpath,
									GuidingCareObjRepo.GuidingCareComplaints.goToCareRecord_xpath)) {
								navigateBack();
							}
							wait(3);
							elementClickableWait(LocatorTypes.xpath,
									GuidingCareObjRepo.GuidingCareComplaints.goToCareRecord_xpath, 30);
							getElement(LocatorTypes.xpath,
									GuidingCareObjRepo.GuidingCareComplaints.goToCareRecord_xpath).click();
							complaintID = pm.getGuidingCareComplaintsPage().createComplaint(medicareID, stdRow,
									complaintType);
							if (complaintID.length() > 0) {
								status = pm.getGuidingCareComplaintsPage().validateTAT(stdRow);
								if (status.equalsIgnoreCase("PASS")) {
									expediteComplntCreated = pm.getGuidingCareComplaintsPage()
											.expediteExistingStandardComplaint(medicareID, expdtRow);
									if (expediteComplntCreated) {
										status = pm.getGuidingCareComplaintsPage().validateTAT(expdtRow);
									} else {
										reportFail("Complaint is not created successfully");
										status = "FAIL";
									}
								}
							} else {
								reportFail("Complaint is not created successfully");
								status = "FAIL";
							}
							// pm.getGuidingCareComplaintsPage().validateTAT(expdtRow);
							String updateQuery = "UPDATE [VelocityTestAutomation].[dbo].[GuidingCare_ComplaintsTAT]\r\n"
									+ "SET STD_THEN_EXPEDITE_STATUS = '" + status + "',\r\n"
									+ "STD_EXPEDITE_COMPLAINTID = '" + complaintID + "'\r\n" + "where ID = '"
									+ expdtRow.get("ID") + "'";
							db.updateDBTestData(updateQuery);
							flushTest();
						}
					} // end of expedited complaint

				} // end of for loop - all complaint types
			} else {
				test.log(Status.FAIL, "No member enrolled for LOB(Plan) -" + planID);
			}

		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Complaint is not created successfully");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

	}

	public void createPreAuthorizationForClaims() {
		String query = "select c.TestCaseID,c.MedicareID,c.ClaimType, c.HealthCareServiceLocationInformation, c.BillingProviderNPI,c.HealthCareCodeInformationPrimary, c.HealthCareCodeInformationSecondary,c.RenderingProviderNPI,c.AttendingProviderNPI\r\n"
				+ "  from [test_data_readytoclaims] c where c.MedicareID is not null and where PreAuthIndicator = 'Y'";

		List<HashMap<String, String>> list = db.getListOfHashMapsFromResultSet(query);
		int counter = 0;
		for (HashMap<String, String> row : list) {
			counter++;
			HashMap<String, String> temptestData = new HashMap<String, String>();
			for (String key : row.keySet()) {
				temptestData.put(key, row.get(key));
			}
			setrowTestData(temptestData, counter);
			test = extent.createTest("Creating Preauthorization for member " + testData.get("MedicareID"));
			boolean memberIDFound = pm.getguidingCareMemberDetails().searchMBI(testData.get("MedicareID"));

			if (memberIDFound == true) {
				pm.getGuidingCarePreAuthorization().createPreauthForClaims();
			}

			flushTest();
		}
	}

}