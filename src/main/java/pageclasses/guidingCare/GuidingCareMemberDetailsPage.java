package pageclasses.guidingCare;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.aventstack.extentreports.Status;

import enums.LocatorTypes;
import managers.PageManager;
import pageclasses.BasePage;
import pageobjects.GuidingCareObjRepo;
import utils.AccessDbconn;
import utils.Dbconn;

public class GuidingCareMemberDetailsPage extends BasePage {

	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(GuidingCareMemberDetailsPage.class.getName());
	PageManager pm = new PageManager();
	Dbconn db = new Dbconn();
	AccessDbconn accessDb = new AccessDbconn();

	public GuidingCareMemberDetailsPage() {
		driver = getWebDriver();
	}

	public void naviagteToCareGiverTab() {
		try {
			elementVisibleWait(LocatorTypes.id, GuidingCareObjRepo.GuidingCareLandingPage.careGiverTab_id, 10);
			getElement(LocatorTypes.id, GuidingCareObjRepo.GuidingCareLandingPage.careGiverTab_id).click();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void naviagteToCareTeamTab() {
		try {
			elementVisibleWait(LocatorTypes.id, GuidingCareObjRepo.GuidingCareLandingPage.careTeamTab_id, 15);
			getElement(LocatorTypes.id, GuidingCareObjRepo.GuidingCareLandingPage.careTeamTab_id).click();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean navigateAndSearchByXBU(String XBU) {
		boolean flag = true;
		try {
			if (isElementPresent(LocatorTypes.id, GuidingCareObjRepo.GuidingCareMemberDetails.memberSearch_id)) {
				flag = searchByXBU(XBU);
				reportPass("GuidingCare XBU search is successful - navigateAndSearchByXBU");
			} /*
				 * else { pm.getGuidingCarePage().navigateToMemberPage(); flag =
				 * searchByXBU(XBU); }
				 */
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			try {
				reportFail("Guiding care XBU search failed - navigateAndSearchByXBU");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}

	public boolean searchByXBU(String XBU) {
		boolean flag = false;
		try {
			elementClickableWait(LocatorTypes.id, GuidingCareObjRepo.GuidingCareMemberDetails.searchByXBU_id, 15);
			Select s = new Select(
					getElement(LocatorTypes.id, GuidingCareObjRepo.GuidingCareMemberDetails.searchByXBU_id));
			s.selectByVisibleText("XBU number");
			getElement(LocatorTypes.id, GuidingCareObjRepo.GuidingCareMemberDetails.memberSearch_id).sendKeys(XBU);
			elementClickableWait(LocatorTypes.id, GuidingCareObjRepo.GuidingCareMemberDetails.memberSearchbtn_id, 15);
			getElement(LocatorTypes.id, GuidingCareObjRepo.GuidingCareMemberDetails.memberSearchbtn_id).click();
			if (isElementPresent(LocatorTypes.xpath,
					GuidingCareObjRepo.GuidingCareMemberDetails.membersearchHyperLink_xpath)) {
				getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareMemberDetails.membersearchHyperLink_xpath)
						.click();
				isElementPresent(LocatorTypes.id, GuidingCareObjRepo.GuidingCareMemberDetails.membeDetailsTab_id);
				flag = true;
			} else {
				flag = false;
				reportFail("Member is not found in Guiding Care");
			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			try {
				reportFail("Guiding care XBU search failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}

	public HashMap<String, String> getCareGiverDetails() {
		HashMap<String, String> careGiverDetails = new HashMap<String, String>();
		String name = testData.get("EMERGENCYNAME");
		if (!name.isEmpty()) {
			naviagteToCareGiverTab();
			try {
				wait(3);
				if (isElementPresent(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareMember.firstCheckbox_xpath)) {
					getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareMember.firstCheckbox_xpath).click();
					getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareMember.viewButton_xpath).click();
					String fName = getElement(LocatorTypes.xpath,
							GuidingCareObjRepo.GuidingCareMember.cg_firstName_xpath).getText().trim();
					String lName = getElement(LocatorTypes.xpath,
							GuidingCareObjRepo.GuidingCareMember.cg_lastName_xpath).getText().trim();
//			String mName = getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareMember.cg_middleName_xpath).getText().trim();
//			String DOB = getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareMember.cg_DOB_xpath).getText().trim();
//			String gender = getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareMember.cg_Gender_xpath).getText().trim();
					String relation = getElement(LocatorTypes.xpath,
							GuidingCareObjRepo.GuidingCareMember.cg_relation_xpath).getText().trim();
					String homePhone = getElement(LocatorTypes.xpath,
							GuidingCareObjRepo.GuidingCareMember.cg_homePhone_xpath).getText().trim();
//			String cellPhone = getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareMember.cg_cellPhone_xpath).getText().trim();
//			String alternatePhone = getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareMember.cg_alternatePhone_xpath).getText().trim();
//			String primaryEmail = getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareMember.cg_primaryEmail_xpath).getText().trim();
//			String alternateEmail = getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareMember.cg_alternateEmail_xpath).getText().trim();
//			String fax = getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareMember.cg_fax_xpath).getText().trim();
//			String address = getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareMember.cg_address_xpath).getText().trim();
//			String city = getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareMember.cg_city_xpath).getText().trim();
//			String state = getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareMember.cg_state_xpath).getText().trim();
//			String county = getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareMember.cg_county_xpath).getText().trim();
//			String zipcode = getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareMember.cg_zipcode_xpath).getText().trim();
//			String isPrimary = getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareMember.cg_isPrimary_xpath).getText().trim();
//			String primaryLanguage = getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareMember.cg_primaryLanguage_xpath).getText().trim();
					getElement(LocatorTypes.id, GuidingCareObjRepo.GuidingCareMember.cg_closePopUp_id).click();
					careGiverDetails.put("cgFName", fName);
					careGiverDetails.put("cgLName", lName);
					careGiverDetails.put("cgRelation", relation);
					careGiverDetails.put("cgHomePhone", homePhone);
					careGiverDetails.put("flag", "true");
					reportPass("getCareGiverDetails - Passed");
				} else {
					reportFail(testData.get("MEMBERID")
							+ " has Care giver in DB and not in Guiding Care - getCareGiverDetails - failed");
					careGiverDetails.put("flag", "false");
				}
			} catch (Exception e) {
				e.printStackTrace();
				try {
					reportFail("getCareGiverDetails - failed");
				} catch (IOException e1) {
					e1.printStackTrace();
				}
			}
		} else {
			careGiverDetails.put("flag", "false");
			test.log(Status.INFO, "MEMBERID " + testData.get("MEMBERID") + " has no care giver in DB ");
		}
		return careGiverDetails;
	}

	public void validateCareGiver(HashMap<String, String> careGiverData) {
		String query = null;
		try {
			HashMap<String, String> testDataCareGiver = new HashMap<String, String>();
			String fullName = testData.get("EMERGENCYNAME").toUpperCase();
			String[] lName = fullName.trim().split(",");
			String fName = lName[1].trim();
			testDataCareGiver.put("cgLName", lName[0].trim());
			testDataCareGiver.put("cgFName", fName);
			String relation = validatecareGiverRelationship(testData.get("EMERGENCYRELATIONSHIP").trim());
			testDataCareGiver.put("cgRelation", "Emergency Contact-" + relation.trim());
			testDataCareGiver.put("cgHomePhone", testData.get("EMERGENCYPHONE").trim());
			testDataCareGiver.put("flag", "true");
			if (testDataCareGiver.equals(careGiverData)) {
				query = "UPDATE MembershipDemo SET FILLER = 'PASSED' WHERE ID = " + testData.get("PCP_ID");
				reportPass("validateCareGiver - Passed");
			} else {
				query = "UPDATE MembershipDemo SET FILLER = 'FAILED' WHERE ID = " + testData.get("PCP_ID");
				reportFail("validateCareGiver - failed. Fields from DB: " + testDataCareGiver
						+ ". Fields from Application: " + careGiverData);
			}
			accessDb.updateDBTestData(query);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("validateCareGiver - failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public HashMap<String, String> getCareTeamDetails() {
		naviagteToCareTeamTab();
		HashMap<String, String> careTeamDetails = new HashMap<String, String>();
		try {
			if (isElementPresent(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareMember.ct_startDate_xpath)) {
				String PCPStartDate = getElement(LocatorTypes.xpath,
						GuidingCareObjRepo.GuidingCareMember.ct_startDate_xpath).getText().trim();
				String PCPEndDate = getElement(LocatorTypes.xpath,
						GuidingCareObjRepo.GuidingCareMember.ct_EndDate_xpath).getText().trim();
				// String PCP = getElement(LocatorTypes.xpath,
				// GuidingCareObjRepo.GuidingCareMember.ct_PCP_xpath).getText().trim();
				careTeamDetails.put("ctStartDate", getDateInYYMMDD(PCPStartDate));
				careTeamDetails.put("ctEndDate", getDateInYYMMDD(PCPEndDate));
				careTeamDetails.put("flag", "true");
				reportPass("getCareTeamDetails - Passed");
			} else {
				careTeamDetails.put("flag", "false");
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("getCareTeamDetails - failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return careTeamDetails;

	}

	public void validateCareTeam(HashMap<String, String> careTeamData) {
		String query = null;
		try {
			HashMap<String, String> testDataCareTeam = new HashMap<String, String>();
			testDataCareTeam.put("ctStartDate", testData.get("PCPSTARTDATE").trim());
			testDataCareTeam.put("ctEndDate", testData.get("PCPENDDATE").trim());
			testDataCareTeam.put("flag", "true");
			if (testDataCareTeam.equals(careTeamData)) {
				query = "UPDATE MembershipPCP SET FILLER = 'PASSED' WHERE ID = " + testData.get("PCP_ID");
				reportPass("validateCareTeam - Passed");
			} else {
				query = "UPDATE MembershipPCP SET FILLER = 'FAILED' WHERE ID = " + testData.get("PCP_ID");
				reportFail("validateCareTeam - failed. Fields from DB: " + testDataCareTeam
						+ ". Fields from Application: " + careTeamData);
			}
			accessDb.updateDBTestData(query);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("validateCareTeam - failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void navigateToMBIPage() {
		try {
			elementVisibleWait(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareLandingPage.MBISearchLink_xpath, 15);
			getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareLandingPage.MBISearchLink_xpath).click();
			elementVisibleWait(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareLandingPage.dropDownMBI_xpath, 30);
			getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareLandingPage.dropDownMBI_xpath).click();
			reportPass("Navigation to MBI page is successful");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Navigation to MBI page failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public boolean searchMBI(String MBI) {
		wait(2);
		boolean flag = true;
		try {
			elementClickableWait(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareLandingPage.searchArrow_xpath, 30);
			getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareLandingPage.searchArrow_xpath).click();
			wait(3);
			List<WebElement> list = getElements(LocatorTypes.xpath,
					GuidingCareObjRepo.GuidingCareLandingPage.searchList_xpath);
			int i = 0;
			System.out.println(list.size());
			for (i = 0; i < list.size(); i++) {
				System.out.println(list.get(i).getText().trim());
				if (list.get(i).getText().trim().equalsIgnoreCase("MBI")) {
					list.get(i).click();
					break;
				}
			}
			if (i == list.size()) {
				flag = false;
				reportFail("Guiding care MBI search failed");
				return flag;
			}
			getElement(LocatorTypes.id, GuidingCareObjRepo.GuidingCareLandingPage.MBISearch_id).clear();
			getElement(LocatorTypes.id, GuidingCareObjRepo.GuidingCareLandingPage.MBISearch_id).sendKeys(MBI);
			elementClickableWait(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareLandingPage.button_xpath, 15);
			getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareLandingPage.button_xpath).click();

			if (isElementPresent(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareLandingPage.popUpTableData_xpath)) {
				String tableData = getElement(LocatorTypes.xpath,
						GuidingCareObjRepo.GuidingCareLandingPage.popUpTableData_xpath).getText().trim();
				if (tableData.length() > 0 || (tableData == null)) {
					logger.info("Cannot find this Member");
					reportFail("Cannot find this Member");
					getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareLandingPage.closePopUpWindow_xpath)
							.click();
				}
			}

			if (isElementPresent(LocatorTypes.xpath,
					GuidingCareObjRepo.GuidingCareLandingPage.homePageMemberInfo_xpath)) {
				flag = true;
				reportPass("Guiding Care MBI search is successful");
			}

		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			try {
				reportFail("Guiding care MBI search failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}

	public void navigateToMemberIDPage() {
		try {
			elementVisibleWait(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareLandingPage.memberIDSearchLink_xpath,
					15);
			getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareLandingPage.memberIDSearchLink_xpath).click();
			elementVisibleWait(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareLandingPage.dropDownMemberID_xpath,
					15);
			getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareLandingPage.dropDownMemberID_xpath).click();
			reportPass("Navigation to MemberId page is successful");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Navigation to MemberId page failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public boolean searchMemberID(String MemberID) {
		boolean flag = true;
		try {
			getElement(LocatorTypes.id, GuidingCareObjRepo.GuidingCareLandingPage.memberID_id).sendKeys(MemberID);
			elementClickableWait(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareLandingPage.button_xpath, 30);
			getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareLandingPage.button_xpath).click();
			flag = true;
			reportPass("GuidingCare MemberId search is successful.");
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			try {
				reportFail("Guiding care MemberId search failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}

	public HashMap<String, String> getPersonalDetails() {
		HashMap<String, String> personalDetails = new HashMap<String, String>();
		try {
			elementVisibleWait(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareMemberDetails.memberName_xpath, 30);
			String[] memberName = getElement(LocatorTypes.xpath,
					GuidingCareObjRepo.GuidingCareMemberDetails.memberName_xpath).getText().split(":");
			String[] gender = getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareMemberDetails.gender_xpath)
					.getText().split(":");
			String[] dateOfBirth = getElement(LocatorTypes.xpath,
					GuidingCareObjRepo.GuidingCareMemberDetails.dateOfBirth_xpath).getText().split(":");
//				String []altruistaID = getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareMemberDetails.altruistaID_xpath).getText().split(":");
//				String []primaryLanguage = getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareMemberDetails.primaryLanguage_xpath).getText().split(":");
//				String []preferredWrittenLanguage = getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareMemberDetails.preferredWrittenLanguage_xpath).getText().split(":");
			StringBuilder primaryPhone = new StringBuilder(
					getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareMemberDetails.primaryPhone_xpath)
							.getText());
			StringBuilder phone = primaryPhone.deleteCharAt(3).deleteCharAt(6);
//				String[] cellPhone = getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareMemberDetails.cellPhone_xpath).getText().split(":");
//				String[] alternatePhone = getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareMemberDetails.alternatePhone_xpath).getText().split(":");
//				String[] primaryEmail = getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareMemberDetails.primaryEmail_xpath).getText().split(":");
//				String ethnicity = getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareMemberDetails.ethnicity_xpath).getText().trim();
			personalDetails.put("MEMBERNAME", memberName[1].trim());
			personalDetails.put("gender", getGenderInitial(gender[1].trim()));
			personalDetails.put("dateOfBirth", getDateInYYYYMMDDfromMMMMMddYYYY(dateOfBirth[1].trim()));
			personalDetails.put("primaryPhone", phone.toString());
			logger.info(personalDetails);
			reportPass("getPersonalDetails - Passed");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Retrieval of Personal details failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return personalDetails;
	}

	public void validatePersonalDetails(HashMap<String, String> personalData) {
		String query = null;
		try {
			HashMap<String, String> testDataPersonalDetails = new HashMap<String, String>();
			if (!testData.get("MEMBERMIDDLEINITIAL").isEmpty()) {
				String fullName = testData.get("MEMBERFIRSTNAME") + " " + testData.get("MEMBERMIDDLEINITIAL") + " "
						+ testData.get("MEMBERLASTNAME");
				testDataPersonalDetails.put("MEMBERNAME", fullName.toUpperCase().trim());
			} else {
				String fullName = testData.get("MEMBERFIRSTNAME") + " " + testData.get("MEMBERLASTNAME");
				testDataPersonalDetails.put("MEMBERNAME", fullName.toUpperCase().trim());
			}
			testDataPersonalDetails.put("gender", testData.get("MEMBERGENDER").trim());
			testDataPersonalDetails.put("dateOfBirth", testData.get("MEMBERBIRTHDATE").trim());
			testDataPersonalDetails.put("primaryPhone", testData.get("HOMEPHONE").trim());
			if (testDataPersonalDetails.equals(personalData)) {
				query = "UPDATE MembershipDemo SET FILLER = 'PASSED' WHERE ID = " + testData.get("MEM_ID");
				reportPass("validatePersonalDetails - Passed");
			} else {
				query = "UPDATE MembershipDemo SET FILLER = 'FAILED' WHERE ID = " + testData.get("MEM_ID");
				reportFail("validatePersonalDetails - failed. Fields from DB: " + testDataPersonalDetails
						+ ". Fields from Application: " + personalData);
			}
			accessDb.updateDBTestData(query);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("validatePersonalDetails - failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public HashMap<String, String> getAddressDetails() {
		navigateToAddressesTab();
		HashMap<String, String> addressDetails = new HashMap<String, String>();
		try {
			elementVisibleWait(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareMemberDetails.addressDetailsList_xpath,
					30);
			List<WebElement> addressDetailsList = getElements(LocatorTypes.xpath,
					GuidingCareObjRepo.GuidingCareMemberDetails.addressDetailsList_xpath);
			wait(2);
			for (int i = 1; i <= addressDetailsList.size(); i++) {

				List<WebElement> addressDetailsColumns = getElements(LocatorTypes.xpath,
						GuidingCareObjRepo.GuidingCareMemberDetails.addressDetailsList_xpath + "[" + i + "]/td");
				String addressType = addressDetailsColumns.get(6).getText().trim();
				if (addressType.equalsIgnoreCase("Primary Address")) {
					addressDetails.put("address", addressDetailsColumns.get(1).getText().trim());
					addressDetails.put("city", addressDetailsColumns.get(2).getText().trim());
					addressDetails.put("state", addressDetailsColumns.get(3).getText().trim());
					// addressDetails.put("county", addressDetailsColumns.get(4).getText().trim());
					addressDetails.put("zip", addressDetailsColumns.get(5).getText().trim());
					// addressDetails.put("addressType",
					// addressDetailsColumns.get(6).getText().trim());
					break;
				}
			}
			logger.info(addressDetails);
			reportPass("getAddressDetails - Passed");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Retrieval of Address details failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return addressDetails;
	}

	public void validateAddressDetails(HashMap<String, String> addressData) {
		String query = null;
		try {
			HashMap<String, String> testDataAddressDetails = new HashMap<String, String>();
			if (testData.get("ADDRESS2").isEmpty()) {
				testDataAddressDetails.put("address", testData.get("ADDRESS1").toUpperCase().trim());
			} else {
				testDataAddressDetails.put("address", testData.get("ADDRESS1").toUpperCase().trim() + " "
						+ testData.get("ADDRESS2").toUpperCase().trim());
			}
			testDataAddressDetails.put("city", testData.get("CITY").toUpperCase().trim());
			testDataAddressDetails.put("state", testData.get("STATE").toUpperCase().trim());
			// testDataAddressDetails.put("county", testData.get("COUNTYCODE").trim());
			testDataAddressDetails.put("zip", testData.get("ZIPCODE").trim());
			// testDataAddressDetails.put("addressType",
			// testData.get("ADDRESSTYPE").toUpperCase().trim());
			if (testDataAddressDetails.equals(addressData)) {
				query = "UPDATE MembershipAddress SET FILLER = 'PASSED' WHERE ID = " + testData.get("ADD_ID");
				reportPass("validateAddressDetails - Passed");
			} else {
				query = "UPDATE MembershipAddress SET FILLER = 'FAILED' WHERE ID = " + testData.get("ADD_ID");
				reportFail("validateAddressDetails - failed. Fields from DB: " + testDataAddressDetails
						+ ". Fields from Application: " + addressData);
			}
			accessDb.updateDBTestData(query);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("validateAddressDetails - failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public HashMap<String, String> getMemberIdentifiers() {
		navigateToMemberIdentifiersTab();
		HashMap<String, String> memberIdentifiers = new HashMap<String, String>();
		try {
//			String healthInsuranceClaimNumber = getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareMemberDetails.healthInsuranceClaimNumber_xpath).getText();
			elementVisibleWait(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareMemberDetails.xbuNumber_xpath, 10);
			String xbuNumber = getElement(LocatorTypes.xpath,
					GuidingCareObjRepo.GuidingCareMemberDetails.xbuNumber_xpath).getText().trim();
//			String medicareBeneficiaryID = getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareMemberDetails.medicareBeneficiaryID_xpath).getText();
//			String m360ID = getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareMemberDetails.m360ID_xpath).getText();
			memberIdentifiers.put("xbuNumber", xbuNumber);
			logger.info(memberIdentifiers);
			reportPass("getMemberIdentifiers - Passed");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Retrieval of Member Identifiers failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return memberIdentifiers;
	}

	public void validateMemberIdentifiers(HashMap<String, String> memberIdentifiersData) {
		String query = null;
		try {
			HashMap<String, String> testDataMemberIdentifiers = new HashMap<String, String>();
			testDataMemberIdentifiers.put("xbuNumber", testData.get("MEMBERID").trim());

			if (testDataMemberIdentifiers.equals(memberIdentifiersData)) {
				query = "UPDATE MembershipDemo SET FILLER = 'PASSED' WHERE ID = " + testData.get("MEM_ID");
				reportPass("validateMemberIdentifiers - Passed");
			} else {
				query = "UPDATE MembershipDemo SET FILLER = 'FAILED' WHERE ID = " + testData.get("MEM_ID");
				reportFail("validateMemberIdentifiers - failed. Fields from DB: " + testDataMemberIdentifiers
						+ ". Fields from Application: " + memberIdentifiersData);
			}
			accessDb.updateDBTestData(query);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("validateMemberIdentifiers failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public HashMap<String, String> getEligibilitydetails() {
		navigateToEligibilityTab();
		HashMap<String, String> eligibilitydetails = new HashMap<String, String>();

		try {
			elementVisibleWait(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareMemberDetails.LOB_xpath, 10);
			String LOB = getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareMemberDetails.LOB_xpath).getText()
					.toUpperCase();
			String LOBCode = getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareMemberDetails.LOBCode_xpath)
					.getText();
//				String benefitNetworks = getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareMemberDetails.benefitNetworks_xpath).getText();
//				String benefitNetworksCode = getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareMemberDetails.benefitNetworksCode_xpath).getText();
//				String eligibilityID = getElement(LocatorTypes.xpath,GuidingCareObjRepo.GuidingCareMemberDetails.eligibilityID_xpath).getText();
			String startDate = getElement(LocatorTypes.xpath,
					GuidingCareObjRepo.GuidingCareMemberDetails.startDate_xpath).getText();
			// String endDate = getElement(LocatorTypes.xpath,
			// GuidingCareObjRepo.GuidingCareMemberDetails.endDate_xpath).getText();
//				String benefitPlan = getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareMemberDetails.benifitPlan_xpath).getText();
			String benefitPlanCode = getElement(LocatorTypes.xpath,
					GuidingCareObjRepo.GuidingCareMemberDetails.benifitPlanCode_xpath).getText();

			eligibilitydetails.put("LOB", LOB);
			eligibilitydetails.put("LOBCode", LOBCode);
			eligibilitydetails.put("startDate", getDateInYYMMDD(startDate));
			// eligibilitydetails.put("endDate", getDateInYYYYMMDDfromMMDDYYYY(endDate));
			eligibilitydetails.put("benefitPlanCode", benefitPlanCode);
			logger.info(eligibilitydetails);
			reportPass("getEligibilitydetails -  Passed");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Retrieval of Eligibility Details failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return eligibilitydetails;
	}

	public void validateEligibilitydetails(HashMap<String, String> eligibilitydetailsData) {
		String query = null;
		try {
			HashMap<String, String> testDataEligibilitydetails = new HashMap<String, String>();

			testDataEligibilitydetails.put("LOB", testData.get("GROUPID").trim());
			testDataEligibilitydetails.put("LOBCode", testData.get("PLANID").trim());
			testDataEligibilitydetails.put("startDate", testData.get("EFFECTIVESTARTDATE").trim());
			// testDataEligibilitydetails.put("endDate", testData.get("EFFENDDATE").trim());
			testDataEligibilitydetails.put("benefitPlanCode", testData.get("PBPID").trim());

			if (testDataEligibilitydetails.equals(eligibilitydetailsData)) {
				query = "UPDATE MembershipEnrollment SET FILLER = 'PASSED' WHERE ID = " + testData.get("ENR_ID");
				reportPass("validateEligibilitydetails - Passed");
			} else {
				query = "UPDATE MembershipEnrollment SET FILLER = 'FAILED' WHERE ID = " + testData.get("ENR_ID");
				reportFail("validateEligibilitydetails - failed. Fields from DB: " + testDataEligibilitydetails
						+ ". Fields from Application: " + eligibilitydetailsData);
			}
			accessDb.updateDBTestData(query);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("validateEligibilitydetails - failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void navigateToMemberIdentifiersTab() {
		try {
			elementVisibleWait(LocatorTypes.id, GuidingCareObjRepo.GuidingCareMemberDetails.memberIdentifiersTab_id,
					15);
			getElement(LocatorTypes.id, GuidingCareObjRepo.GuidingCareMemberDetails.memberIdentifiersTab_id).click();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Clicking on Member Identifiers tab failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void navigateToAddressesTab() {
		try {
			elementVisibleWait(LocatorTypes.id, GuidingCareObjRepo.GuidingCareMemberDetails.addressesTab_id, 15);
			getElement(LocatorTypes.id, GuidingCareObjRepo.GuidingCareMemberDetails.addressesTab_id).click();
			reportPass("Navigated to Address tab failed");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Clicking on Addresses tab failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void navigateToEligibilityTab() {
		try {
			elementVisibleWait(LocatorTypes.id, GuidingCareObjRepo.GuidingCareMemberDetails.eligibilityTab_id, 15);
			getElement(LocatorTypes.id, GuidingCareObjRepo.GuidingCareMemberDetails.eligibilityTab_id).click();
			reportPass("Navigated to Eligibility tab failed");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Clicking on Eligibility tab failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public String validatecareGiverRelationship(String relation) {
		String relationship = null;
		try {
			switch (relation) {
			case "ATY": {
				relationship = "Attorney";
				break;
			}
			case "DAD": {
				relationship = "Father";
				break;
			}
			case "DEP": {
				relationship = "Dependent";
				break;
			}
			case "EMP": {
				relationship = "Employee";
				break;
			}
			case "FCH": {
				relationship = "Foster Child";
				break;
			}
			case "FSP": {
				relationship = "Foster Parent";
				break;
			}
			case "GCH": {
				relationship = "Grandchild";
				break;
			}
			case "GFA": {
				relationship = "Grandfather";
				break;
			}
			case "GMA": {
				relationship = "Grandmother";
				break;
			}
			case "GPR": {
				relationship = "Grandparent";
				break;
			}
			case "GRD": {
				relationship = "Guardian";
				break;
			}
			case "LIF": {
				relationship = "Life Partner";
				break;
			}
			case "MOM": {
				relationship = "Mother";
				break;
			}
			case "NCH": {
				relationship = "Natural Child";
				break;
			}
			case "NN": {
				relationship = "Niece/Nephew";
				break;
			}
			case "OTH": {
				relationship = "Other";
				break;
			}
			case "PAL": {
				relationship = "Friend";
				break;
			}
			case "PHY": {
				relationship = "Physician";
				break;
			}
			case "POA": {
				relationship = "Power of Attorney";
				break;
			}
			case "REL": {
				relationship = "Relative (other)";
				break;
			}
			case "SCH": {
				relationship = "Step Child";
				break;
			}
			case "SIB": {
				relationship = "Sibling (sister/brother)";
				break;
			}
			case "SPS": {
				relationship = "Spouse";
				break;
			}
			case "STP": {
				relationship = "Step Parent";
				break;
			}
			case "UNK": {
				relationship = "Unknown";
				break;
			}
			}
			reportPass("Relationship values obtained successfully");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Getting Relationship values failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return relationship;

	}

}
