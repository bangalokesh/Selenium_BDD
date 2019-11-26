package pageclasses.m360membership;

import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import com.aventstack.extentreports.Status;

import enums.LocatorTypes;
import managers.PageManager;
import pageclasses.BasePage;
import pageclasses.CommonMethods;
import pageclasses.m360.M360LoginPage;
import pageobjects.M360MembershipObjRepo;
import utils.AccessDbconn;
import utils.Dbconn;

public class M360MemberPage extends BasePage {
	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(M360MemberPage.class.getName());
	PageManager pm = new PageManager();
	Dbconn db = new Dbconn();
	AccessDbconn accessDb = new AccessDbconn();
	CommonMethods commonMethods = new CommonMethods();

	public M360MemberPage() {
		driver = getWebDriver();
	}

	public boolean validateMemberDemographicDetails() {
		boolean flag = false;
		try {
			HashMap<String, String> testDataMem = getDemographicsDetails();
			if (!testData.get("DOB").isEmpty()) {
				HashMap<String, String> memDemoData = new HashMap<String, String>();
				HashMap<String, String> testDataDemo = new HashMap<String, String>();

				memDemoData.put("MEMBERBIRTHDATE", testDataMem.get("MEMBERBIRTHDATE"));
				memDemoData.put("MEMBERLASTNAME", testDataMem.get("MEMBERLASTNAME"));
				testDataDemo.put("MEMBERBIRTHDATE", getDateInYYYYMMDD(testData.get("DOB")));
				testDataDemo.put("MEMBERLASTNAME", testData.get("MemberLName").trim());

				flag = compareHashMaps(memDemoData, testDataDemo);

				if (flag)
					reportPass("Member (Demographics) Verified " + memDemoData);
				else {
					reportFail("Member (Demographics) Verification failed, data on screen: " + testDataMem
							+ " did not match enrollment data in tables: " + testData);
				}

				String selectQuery = "SELECT * FROM [VelocityTestAutomation].[dbo].[member_demographic] WHERE [MedicareID] = '"
						+ testData.get("MedCareID") + "';";
				ResultSet rs = db.executeSelectQuery(selectQuery);
				String[] temp_date = testDataMem.get("CMSEFFECTIVEMONTH").split("/");
				testDataMem.put("CMSEFFECTIVEMONTH", temp_date[0] + "/01/" + temp_date[1]);

				if (rs.next()) {
					if (rs.getString("MedicareID").equalsIgnoreCase(testData.get("MedCareID"))) {
						String updateQuery = "UPDATE [VelocityTestAutomation].[dbo].[member_demographic]\r\n"
								+ "SET [Prefix] = '" + testDataMem.get("MEMBERPREFIX") + "', [FirstName] = '"
								+ testDataMem.get("MEMBERFIRSTNAME").trim() + "', [MiddleInitial] = '"
								+ testDataMem.get("MEMBERMIDDLEINITIAL").trim() + "', [LastName] = '"
								+ testDataMem.get("MEMBERLASTNAME").trim() + "', [Suffix] = '"
								+ testDataMem.get("MEMBERSUFFIX").trim() + "', [EffectiveMonth] = '"
								+ getDateMMDDYYYYInSqlFormat(testDataMem.get("CMSEFFECTIVEMONTH").trim())
								+ "', [SSN] = '" + testDataMem.get("MEMBERSSN").trim() + "', [Gender] = ' "
								+ testDataMem.get("MEMBERGENDER").trim() + "', [BirthDate] = '"
								+ getDateStringInSqlFormat(testDataMem.get("MEMBERBIRTHDATE").trim())
								+ "',[MemberID] = '" + testDataMem.get("MEMBERID").trim() + "',[MedicareID] = '"
								+ testData.get("MedCareID").trim() + "'\r\n" + "WHERE [MedicareID] = '"
								+ testData.get("MedCareID") + "';";
						db.sqlUpdate(updateQuery);
					}
				} else {
					String insertQuery = "INSERT INTO VelocityTestAutomation.dbo.member_demographic\r\n"
							+ "([MedicareID], [Prefix], [FirstName], [MiddleInitial], [LastName], [Suffix], [EffectiveMonth], [SSN], [Gender], [BirthDate], [MemberID])\r\n"
							+ "VALUES ('" + testData.get("MedCareID").trim() + "', '"
							+ testDataMem.get("MEMBERPREFIX").trim() + "', '"
							+ testDataMem.get("MEMBERFIRSTNAME").trim() + "', '"
							+ testDataMem.get("MEMBERMIDDLEINITIAL").trim() + "', '"
							+ testDataMem.get("MEMBERLASTNAME").trim() + "', '" + testDataMem.get("MEMBERSUFFIX").trim()
							+ "', '" + getDateMMDDYYYYInSqlFormat(testDataMem.get("CMSEFFECTIVEMONTH").trim()) + "', '"
							+ testDataMem.get("MEMBERSSN").trim() + "', '" + testDataMem.get("MEMBERGENDER").trim()
							+ "', '" + getDateStringInSqlFormat(testDataMem.get("MEMBERBIRTHDATE").trim()) + "', '"
							+ testDataMem.get("MEMBERID").trim() + "');";
					db.sqlUpdate(insertQuery);
				}
			} else {
				flag = false;
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
			flag = false;
			e.printStackTrace();
			try {
				reportFail("M360MemberPage validateMembers Failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}

	public boolean validateMemberAddressDetails() {
		boolean flag = true;
		try {
			navigateToAddressTab();
			HashMap<String, String> memAddressDetails = getAddressDetails();
			HashMap<String, String> testDataAddress = new HashMap<String, String>();
			/*
			 * testDataAddress.put("ADDRESS", testData.get("Address1").toUpperCase() +
			 * testData.get("Address2").toUpperCase() +
			 * testData.get("Address3").toUpperCase());
			 */
			testDataAddress.put("ADDRESSTYPE", testData.get("AddressType").toUpperCase());
			testDataAddress.put("CITY", testData.get("City").toUpperCase());
			testDataAddress.put("STATE", testData.get("StateCode").toUpperCase());
			testDataAddress.put("ZIPCODE", testData.get("ZipCode").substring(0, 5));
			// testDataAddress.put("HOMEPHONE", testData.get("HomePhone"));
			testDataAddress.put("WORKPHONE", testData.get("WorkPhone"));
			testDataAddress.put("CELLPHONE", testData.get("CellPhone"));

			/*
			 * memAddressDetails.put("ADDRESS", memAddressDetails.get("ADDRESS1") +
			 * memAddressDetails.get("ADDRESS2") + memAddressDetails.get("ADDRESS3"));
			 */
			memAddressDetails.replace("ZIPCODE", memAddressDetails.get("ZIPCODE").substring(0, 5));
			memAddressDetails.replace("HOMEPHONE", memAddressDetails.get("HOMEPHONE").replace("-", ""));
			memAddressDetails.replace("WORKPHONE", memAddressDetails.get("WORKPHONE").replace("-", ""));
			memAddressDetails.replace("CELLPHONE", memAddressDetails.get("CELLPHONE").replace("-", ""));
			memAddressDetails.remove("COUNTYCODE");
			memAddressDetails.remove("ADDRESS1");
			memAddressDetails.remove("ADDRESS2");
			memAddressDetails.remove("ADDRESS3");
			memAddressDetails.remove("HOMEPHONE");

			flag = compareHashMaps(memAddressDetails, testDataAddress);

			if (flag) {
				reportPass("Member (Address) Verified: " + memAddressDetails);
			} else {
				reportFail("Member (Address) Not Matched, screen data: " + memAddressDetails
						+ " did not match enrollment data in tables: " + testDataAddress);
			}
		} catch (IOException e) {
			if (getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MemberPage.pageCannotDisplay_xpath).getText()
					.equalsIgnoreCase("The page cannot be displayed because an internal server error has occurred.")
					|| getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MemberPage.unExpectedResponse).getText()
							.equals("Unexpected Response")
					|| getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MemberPage.unExpectedResponse).getText()
							.contains("The following session is not valid")) {
				driver.quit();
			}
			flag = false;
			e.printStackTrace();
			try {
				reportFail("Member Address Not Matched. DB Test Data: " + testData);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}

	public boolean validateMemberDemographicData(ResultSet resultSet, String suppID) {
		boolean flag = true;
		try {
			HashMap<String, String> testDataDemo = new HashMap<String, String>();
			HashMap<String, String> memDataDemo = getMemberDetails(suppID);

			testDataDemo.put("MEMBERPREFIX", resultSet.getString("MEMBERPREFIX"));
			testDataDemo.put("MEMBERFIRSTNAME", resultSet.getString("MEMBERFIRSTNAME"));
			testDataDemo.put("MEMBERMIDDLEINITIAL", resultSet.getString("MEMBERMIDDLEINITIAL"));
			testDataDemo.put("MEMBERLASTNAME", resultSet.getString("MEMBERLASTNAME"));
			testDataDemo.put("MEMBERSSN", resultSet.getString("MEMBERSSN"));
			testDataDemo.put("MEMBERGENDER", resultSet.getString("MEMBERGENDER"));
			testDataDemo.put("MEMBERBIRTHDATE", resultSet.getString("MEMBERBIRTHDATE"));

			compareHashMaps(memDataDemo, testDataDemo);

			if (flag)
				reportPass("Member Verified " + memDataDemo);
			else
				reportFail("Member Verification failed " + memDataDemo);
		} catch (SQLException | IOException e) {
			e.printStackTrace();
			try {
				reportFail("M360MemberPage validateMemberDemographicData Failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}

	public void navigateToMemberDetails() {
		try {
			getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360LandingPage.m360Header_xpath).click();
			Set<String> winHandles = driver.getWindowHandles();
			for (String winHandle : winHandles) {
				System.out.println(driver.getCurrentUrl());
				if (driver.getCurrentUrl().contains("eemAction.do?method=initialize")) {
					getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MedicareSupportPage.memberHeader_xpath)
							.click();
					break;
				} else
					driver.switchTo().window(winHandle);
			}
			if (driver.getCurrentUrl().contains("eemAction.do?method=initialize")) {
				driver.manage().window().maximize();
				System.out.println(driver.getCurrentUrl());
				getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MedicareSupportPage.memberHeader_xpath)
						.click();
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("M360MemberPage navigateToMemberDetails failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public boolean searchMember(String suppID) {
		boolean flag = true;
		try {
			getElement(LocatorTypes.name, M360MembershipObjRepo.M360MedicareSupportPage.memNewSearch_name).click();
			getElement(LocatorTypes.name, M360MembershipObjRepo.M360MedicareSupportPage.medicareID_name)
					.sendKeys(suppID);
			getElement(LocatorTypes.id, M360MembershipObjRepo.M360MedicareSupportPage.memberSearchGoButton_id).click();
			if (getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MedicareSupportPage.memberDetails_xpath)
					.getText().trim().equalsIgnoreCase("No Data")) {
				test.log(Status.FAIL, "Member Not Found");
				return false;
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
			flag = false;
			e.printStackTrace();
			try {
				reportFail("M36MemberPage getMemberDetails failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}

	public HashMap<String, String> getMemberDetails(String suppID) {
		HashMap<String, String> memberDemographicData = new HashMap<String, String>();
		try {
			getElement(LocatorTypes.name, M360MembershipObjRepo.M360MedicareSupportPage.memNewSearch_name).click();
			getElement(LocatorTypes.name, M360MembershipObjRepo.M360MedicareSupportPage.supplementalID_name)
					.sendKeys(suppID);
			getElement(LocatorTypes.id, M360MembershipObjRepo.M360MedicareSupportPage.memberSearchGoButton_id).click();
			memberDemographicData = getDemographicsDetails();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("M36MemberPage getMemberDetails failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return memberDemographicData;
	}

	public void enterMedicareID(String medicareID) {
		try {
			getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MemberPage.medicare_id_xpath).sendKeys(medicareID);

			reportPass("Entered Medicare ID data successfully");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Enter Medicare ID data Info failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public HashMap<String, String> getDemographicsDetails() {
		HashMap<String, String> memberDemographic = new HashMap<String, String>();
		try {
			if (isElementPresent(LocatorTypes.xpath, M360MembershipObjRepo.M360MemberPage.hicNumber_xpath)) {

				String HICNO = getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MemberPage.hicNumber_xpath)
						.getText();
				Select sal = new Select(
						getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MemberPage.salutation));
				String MEMBERPREFIX = sal.getFirstSelectedOption().getText();
				String MEMBERFIRSTNAME = getElement(LocatorTypes.xpath,
						M360MembershipObjRepo.M360MemberPage.firstName_xpath).getAttribute("value");
				String MEMBERMIDDLEINITIAL = getElement(LocatorTypes.xpath,
						M360MembershipObjRepo.M360MemberPage.middleName_xpath).getAttribute("value");
				String MEMBERLASTNAME = getElement(LocatorTypes.xpath,
						M360MembershipObjRepo.M360MemberPage.lastName_xpath).getAttribute("value");
				String MEMBERSUFFIX = getElement(LocatorTypes.xpath,
						M360MembershipObjRepo.M360MemberPage.suffixName_xpath).getAttribute("value");
				String CMSEFFECTIVEMONTH = getElement(LocatorTypes.xpath,
						M360MembershipObjRepo.M360MemberPage.effectiveMonth_xpath).getText();
				String MEMBERSSN = getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MemberPage.ssn_xpath)
						.getAttribute("value");
				String MEDICARENUMBER = getElement(LocatorTypes.xpath,
						M360MembershipObjRepo.M360MemberPage.medicare_id_output_xpath).getText();
				String MEMBERBIRTHDATE = getElement(LocatorTypes.xpath,
						M360MembershipObjRepo.M360MemberPage.dateOfBirth_xpath).getAttribute("value");
				Date dob = new SimpleDateFormat("MM/dd/yyyy").parse(MEMBERBIRTHDATE);
				MEMBERBIRTHDATE = new SimpleDateFormat("yyyyMMdd").format(dob);
				Select gen = new Select(
						getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MemberPage.gender_xpath));
				String MEMBERGENDER = getGenderInitial(gen.getFirstSelectedOption().getText());
				MEMBERSSN = MEMBERSSN.replace("-", "");
				String MEMBERID = getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MemberPage.member_id_xpath)
						.getText();

				if (MEMBERPREFIX.trim().equalsIgnoreCase("Select")) {
					MEMBERPREFIX = "";
				}

				memberDemographic.put("HICNO", HICNO);
				memberDemographic.put("MEMBERPREFIX", MEMBERPREFIX);
				memberDemographic.put("MEMBERFIRSTNAME", MEMBERFIRSTNAME);
				memberDemographic.put("MEMBERMIDDLEINITIAL", MEMBERMIDDLEINITIAL);
				memberDemographic.put("MEMBERLASTNAME", MEMBERLASTNAME);
				memberDemographic.put("MEMBERSUFFIX", MEMBERSUFFIX);
				memberDemographic.put("CMSEFFECTIVEMONTH", CMSEFFECTIVEMONTH);
				memberDemographic.put("MEMBERSSN", removZeroFromFront(MEMBERSSN));
				memberDemographic.put("MEDICARENUMBER", MEDICARENUMBER);
				memberDemographic.put("MEMBERGENDER", MEMBERGENDER);
				memberDemographic.put("MEMBERBIRTHDATE", MEMBERBIRTHDATE);
				memberDemographic.put("MEMBERID", MEMBERID);
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Retrieval of Demographics data failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return memberDemographic;
	}

	public HashMap<String, String> getEnrollmentDetails() {
		HashMap<String, String> memberEnrollment = new HashMap<String, String>();
		try {
			getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MemberPage.enrollmentLatestRecord_xpath).click();

			String enrollmentStatus = getElement(LocatorTypes.id,
					M360MembershipObjRepo.M360MemberPage.enrollmentStatus_id).getText();
			String enrollmentReason = getElement(LocatorTypes.id,
					M360MembershipObjRepo.M360MemberPage.enrollmentReason_id).getText();
			String enrollmentStartDate = getElement(LocatorTypes.id,
					M360MembershipObjRepo.M360MemberPage.enrollmentStartDate_id).getText();
			String enrollmentEndDate = getElement(LocatorTypes.id,
					M360MembershipObjRepo.M360MemberPage.enrollmentEndDate_id).getText();
			String enrollmentGroupName = getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.groupName_id)
					.getText();
			String enrollmentGroupID = getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.group_id_id)
					.getText();
			String enrollmentProductName = getElement(LocatorTypes.id,
					M360MembershipObjRepo.M360MemberPage.productName_id).getText();
			String enrollmentProductID = getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.product_id_id)
					.getText();
			String enrollmentPlanID = getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.plan_id_id)
					.getText();
			String enrollmentPBP_ID = getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.pbp_id_id)
					.getText();
			String enrollmentSegment = getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.segment_id)
					.getText();
			String enrollmentType = getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.type_id).getText();
			String enrollmentDesignation = getElement(LocatorTypes.id,
					M360MembershipObjRepo.M360MemberPage.designation_id).getText();
			String enrollmentElectionType = getElement(LocatorTypes.id,
					M360MembershipObjRepo.M360MemberPage.electionType_id).getText();
			String enrollmentCancellationReason = getElement(LocatorTypes.id,
					M360MembershipObjRepo.M360MemberPage.cancellationReason_id).getText();
			String enrollmentDisReason = getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.disReason_id)
					.getText();
			String enrollmentSupplementalID = getElement(LocatorTypes.id,
					M360MembershipObjRepo.M360MemberPage.supplemental_id_id).getText();
			String enrollmentRxID = getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.rx_id_id)
					.getText();
			String enrollmentApplicationDate = getElement(LocatorTypes.id,
					M360MembershipObjRepo.M360MemberPage.applicationDate_id).getText();
			String enrollmentSignatureDate = getElement(LocatorTypes.id,
					M360MembershipObjRepo.M360MemberPage.signatureDate_id).getText();
			memberEnrollment.put("ENROLLMENTSTATUS", enrollmentStatus.trim());
			memberEnrollment.put("ENROLLMENTREASON", enrollmentReason.trim());
			memberEnrollment.put("ENROLLMENTSTARTDATE", enrollmentStartDate.trim());
			memberEnrollment.put("ENROLLMENTENDDATE", enrollmentEndDate.trim());
			memberEnrollment.put("ENROLLMENTGROUPNAME", enrollmentGroupName.trim());
			memberEnrollment.put("ENROLLMENTGROUPID", enrollmentGroupID.trim());
			memberEnrollment.put("ENROLLMENTPRODUCTNAME", enrollmentProductName.trim());
			memberEnrollment.put("ENROLLMENTPRODUCTID", enrollmentProductID.trim());
			memberEnrollment.put("ENROLLMENTPLANID", enrollmentPlanID.trim());
			memberEnrollment.put("ENROLLMENTPBPID", enrollmentPBP_ID.trim());
			memberEnrollment.put("ENROLLMENTSEGMENT", enrollmentSegment.trim());
			memberEnrollment.put("ENROLLMENTTYPE", enrollmentType.trim());
			memberEnrollment.put("ENROLLMENTDESIGNATION", enrollmentDesignation.trim());
			memberEnrollment.put("ENROLLMENTELECTIONTYPE", enrollmentElectionType.trim());
			memberEnrollment.put("ENROLLMENTCANCELLATIONREASON", enrollmentCancellationReason.trim());
			memberEnrollment.put("ENROLLMENTDISREASON", enrollmentDisReason.trim());
			memberEnrollment.put("ENROLLMENTSUPPLEMENTALID", enrollmentSupplementalID.trim());
			memberEnrollment.put("ENROLLMENTRXID", enrollmentRxID.trim());
			memberEnrollment.put("ENROLLMENTAPPLICATIONDATE", enrollmentApplicationDate.trim());
			memberEnrollment.put("ENROLLMENTSIGNATUREDATE", enrollmentSignatureDate.trim());
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Retrieval of Enrollment data failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return memberEnrollment;
	}

	public boolean validateEnrollmentDetails() {
		boolean flag = false;
		String enrollmentStartDate = null;
		@SuppressWarnings("unused")
		String applicationDate = null;
		try {
			navigateToEnrollmentTab();
			HashMap<String, String> memEnrollDetails = getEnrollmentDetails();
			HashMap<String, String> memberEnrollData = new HashMap<String, String>();
			HashMap<String, String> enrollData = new HashMap<String, String>();

			if (!testData.get("CoverageDate").isEmpty())
				enrollmentStartDate = getDateInMMDDYYYY(testData.get("CoverageDate"));
			if (!testData.get("ApplicationDate").isEmpty())
				applicationDate = getDateInMMDDYYYY(testData.get("ApplicationDate"));

			if (testData.get("PlanID").trim().equalsIgnoreCase("H0302")
					&& getDateFromString(enrollmentStartDate).compareTo(getDateFromString("7/31/2019")) <= 0) {
				memberEnrollData.put("OldPrefix", memEnrollDetails.get("ENROLLMENTSUPPLEMENTALID").substring(0, 3));
				enrollData.put("OldPrefix", testData.get("OldPrefix"));
			} else {
				memberEnrollData.put("NewPrefix", memEnrollDetails.get("ENROLLMENTSUPPLEMENTALID").substring(0, 3));
				enrollData.put("NewPrefix", testData.get("NewPrefix"));
			}

			memberEnrollData.put("ENROLLMENTSTARTDATE", memEnrollDetails.get("ENROLLMENTSTARTDATE"));
			memberEnrollData.put("ENROLLMENTGROUPNAME", memEnrollDetails.get("ENROLLMENTGROUPNAME").toUpperCase());
			memberEnrollData.put("ENROLLMENTGROUPID", memEnrollDetails.get("ENROLLMENTGROUPID").toUpperCase());
			memberEnrollData.put("ENROLLMENTPRODUCTNAME", memEnrollDetails.get("ENROLLMENTPRODUCTNAME").toUpperCase());
			memberEnrollData.put("ENROLLMENTPRODUCTID", memEnrollDetails.get("ENROLLMENTPRODUCTID"));
			memberEnrollData.put("ENROLLMENTPLANID", memEnrollDetails.get("ENROLLMENTPLANID"));
			memberEnrollData.put("ENROLLMENTPBPID", memEnrollDetails.get("ENROLLMENTPBPID"));
			memberEnrollData.put("ENROLLMENTSEGMENT", memEnrollDetails.get("ENROLLMENTSEGMENT"));
			memberEnrollData.put("ENROLLMENTELECTIONTYPE",
					memEnrollDetails.get("ENROLLMENTELECTIONTYPE").toUpperCase());

			enrollData.put("ENROLLMENTSTARTDATE", enrollmentStartDate);
			enrollData.put("ENROLLMENTGROUPNAME", testData.get("GroupName").toUpperCase());
			enrollData.put("ENROLLMENTGROUPID", testData.get("GroupID").toUpperCase());
			enrollData.put("ENROLLMENTPRODUCTNAME", testData.get("PlanName").toUpperCase());
			enrollData.put("ENROLLMENTPRODUCTID", testData.get("ProductID"));
			enrollData.put("ENROLLMENTPLANID", testData.get("PlanID"));
			enrollData.put("ENROLLMENTPBPID", testData.get("PBPID"));
			enrollData.put("ENROLLMENTSEGMENT", testData.get("PBPSegmentID"));
			enrollData.put("ENROLLMENTELECTIONTYPE", testData.get("ElectionType").toUpperCase());

			flag = compareHashMaps(memberEnrollData, enrollData);
			if (flag) {
				reportPass("Enrollment Record Validation Passed " + memberEnrollData);
			} else {
				reportFail("Enrollment Record Validation Failed, screen output: " + memberEnrollData
						+ " does not match enroll data in tables: " + enrollData);
			}

			String selectQuery = "SELECT * FROM [VelocityTestAutomation].[dbo].[member_enrollment] WHERE [MedicareID] = '"
					+ testData.get("MedCareID") + "';";
			ResultSet rs = db.executeSelectQuery(selectQuery);
			if (rs.next()) {
				if (rs.getString("MedicareID").equalsIgnoreCase(testData.get("MedCareID"))) {
					String updateQuery = "UPDATE [VelocityTestAutomation].[dbo].[member_enrollment]\r\n"
							+ "SET [MedicareID] = '" + testData.get("MedCareID") + "', \r\n" + "[SupplementalID] = '"
							+ memEnrollDetails.get("ENROLLMENTSUPPLEMENTALID").trim() + "', \r\n"
							+ "[EffectiveStartDate] = '"
							+ getDateMMDDYYYYInSqlFormat(memEnrollDetails.get("ENROLLMENTSTARTDATE")).trim() + "',\r\n"
							+ "[GroupID] = '" + memEnrollDetails.get("ENROLLMENTGROUPID").trim() + "',\r\n"
							+ "[ProductID] = '" + memEnrollDetails.get("ENROLLMENTPRODUCTID").trim() + "',\r\n"
							+ "[EnrollStatus] = '" + memEnrollDetails.get("ENROLLMENTSTATUS").trim() + "',\r\n"
							+ "[EnrollReasonCode] = '" + memEnrollDetails.get("ENROLLMENTREASON").trim() + "',\r\n"
							+ "[PlanID] = '" + memEnrollDetails.get("ENROLLMENTPLANID").trim() + "',\r\n"
							+ "[PBPID] = '" + memEnrollDetails.get("ENROLLMENTPBPID").trim() + "',\r\n"
							+ "[PBPSegmentID] = '" + memEnrollDetails.get("ENROLLMENTSEGMENT").trim() + "',\r\n"
							+ "[PlanDesignation] = '" + memEnrollDetails.get("ENROLLMENTDESIGNATION").trim() + "',\r\n"
							+ "[ElectionType] = '" + memEnrollDetails.get("ENROLLMENTELECTIONTYPE").trim() + "',\r\n"
							+ "[RXID] = '" + memEnrollDetails.get("ENROLLMENTRXID").trim() + "',\r\n"
							+ "[ApplicationDate] = '" + testData.get("ApplicationDate") + "',\r\n"
							+ "[ApplicationSignatureDate] = '" + testData.get("ApplicationDate") + "',\r\n"
							+ "[CancellationReason] = '" + memEnrollDetails.get("ENROLLMENTCANCELLATIONREASON").trim()
							+ "',\r\n" + "[DisEnrollmentReason] = '"
							+ memEnrollDetails.get("ENROLLMENTDISREASON").trim() + "'\r\n" + "WHERE [MedicareID] = '"
							+ testData.get("MedCareID") + "';";
					db.sqlUpdate(updateQuery);
				}
			} else {
				String insertQuery = "INSERT INTO [VelocityTestAutomation].[dbo].[member_enrollment]\r\n"
						+ " ([MedicareID],[SupplementalID],[EffectiveStartDate],[GroupID],[ProductID]\r\n"
						+ " ,[EnrollStatus],[EnrollReasonCode],[PlanID],[PBPID],[PBPSegmentID],[PlanDesignation]\r\n"
						+ " ,[ElectionType],[RXID],[ApplicationDate],[ApplicationSignatureDate],[CancellationReason]\r\n"
						+ " ,[DisEnrollmentReason]) VALUES \r\n" + " ('" + testData.get("MedCareID") + "', '"
						+ memEnrollDetails.get("ENROLLMENTSUPPLEMENTALID").trim() + "'\r\n" + ", '"
						+ testData.get("CoverageDate").trim() + "'\r\n" + ", '"
						+ memEnrollDetails.get("ENROLLMENTGROUPID").trim() + "'\r\n" + ", '"
						+ memEnrollDetails.get("ENROLLMENTPRODUCTID").trim() + "'\r\n" + ", '"
						+ memEnrollDetails.get("ENROLLMENTSTATUS").trim() + "'\r\n" + ", '"
						+ memEnrollDetails.get("ENROLLMENTREASON").trim() + "'\r\n" + ", '"
						+ memEnrollDetails.get("ENROLLMENTPLANID").trim() + "'\r\n" + ", '"
						+ memEnrollDetails.get("ENROLLMENTPBPID").trim() + "'\r\n" + ", '"
						+ memEnrollDetails.get("ENROLLMENTSEGMENT").trim() + "'\r\n" + ", '"
						+ memEnrollDetails.get("ENROLLMENTDESIGNATION").trim() + "'\r\n" + ", '"
						+ memEnrollDetails.get("ENROLLMENTELECTIONTYPE").trim() + "'\r\n" + ", '"
						+ memEnrollDetails.get("ENROLLMENTRXID").trim() + "'\r\n" + ", '"
						+ testData.get("ApplicationDate") + "', '" + testData.get("ApplicationDate") + "'\r\n" + ", '"
						+ memEnrollDetails.get("ENROLLMENTCANCELLATIONREASON").trim() + "'\r\n" + ", '"
						+ memEnrollDetails.get("ENROLLMENTDISREASON").trim() + "')";
				db.sqlUpdate(insertQuery);
			}
			String updateReadyToEnroll = "Update [VelocityTestAutomation].[dbo].[test_data_readytoenroll] set [ApplicationStatus] = '"
					+ memEnrollDetails.get("ENROLLMENTSTATUS").trim() + "', [CMSEnrollmentStatus] = '"
					+ memEnrollDetails.get("ENROLLMENTREASON").trim() + "' WHERE [MedCareID] = '"
					+ testData.get("MedCareID") + "';";
			db.sqlUpdate(updateReadyToEnroll);
			String updateDemo = "Update [VelocityTestAutomation].[dbo].[member_demographic] set [SupplementalID] = '"
					+ memEnrollDetails.get("ENROLLMENTSUPPLEMENTALID").trim() + "' WHERE [MedicareID] = '"
					+ testData.get("MedCareID") + "';";
			db.sqlUpdate(updateDemo);
		} catch (Exception e) {
			if (getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MemberPage.pageCannotDisplay_xpath).getText()
					.equalsIgnoreCase("The page cannot be displayed because an internal server error has occurred.")
					|| getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MemberPage.unExpectedResponse).getText()
							.equals("Unexpected Response")
					|| getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MemberPage.unExpectedResponse).getText()
							.contains("The following session is not valid")) {
				driver.quit();
			}
			flag = false;
			e.printStackTrace();
			try {
				reportFail("validateEnrollmentDetails method failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}

	public HashMap<String, String> getAddressDetails() {
		HashMap<String, String> memberAddress = new HashMap<String, String>();
		try {
			Integer count = 0;
			List<WebElement> addressTypeList = getElements(LocatorTypes.xpath,
					M360MembershipObjRepo.M360MemberPage.addressType_array_xpath);

			while (count < addressTypeList.size()) {
				if (addressTypeList.get(count).getText().toUpperCase().contains("PRIM")) {
					addressTypeList.get(count).click();
					break;
				} else {
					count++;
				}
			}
			String ADDRESSTYPE = getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.addressType_id)
					.getText();
			String ADDRESS1 = getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.addressLineOne_id)
					.getText();
			String ADDRESS2 = getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.addressLineTwo_id)
					.getText();
			String ADDRESS3 = getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.addressLineThree_id)
					.getText();
			String CITY = getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.city_id).getText();
			String STATE = getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.state_id).getText();
			String ZIPCODE = getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.zipCode_id).getText();
			String COUNTYCODE = getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.county_id).getText();
			String HOMEPHONE = getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.homePhoneNumber_id)
					.getText();
			String CELLPHONE = getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.cellPhoneNumber_id)
					.getText();
			String WORKPHONE = getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.workPhoneNumber_id)
					.getText();

			if (ADDRESSTYPE.toUpperCase().contains("PRIMARY")) {
				ADDRESSTYPE = "PRIM";
			}

			memberAddress.put("ADDRESSTYPE", ADDRESSTYPE.substring(0, 4).toUpperCase());
			memberAddress.put("ADDRESS1", ADDRESS1);
			memberAddress.put("ADDRESS2", ADDRESS2);
			memberAddress.put("ADDRESS3", ADDRESS3);
			memberAddress.put("CITY", CITY);
			memberAddress.put("STATE", STATE);
			memberAddress.put("ZIPCODE", ZIPCODE);
			memberAddress.put("COUNTYCODE", COUNTYCODE);
			memberAddress.put("HOMEPHONE", HOMEPHONE);
			memberAddress.put("WORKPHONE", WORKPHONE);
			memberAddress.put("CELLPHONE", CELLPHONE);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Retrieval of Address data failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return memberAddress;
	}

	public boolean validatePCPDetails() {
		boolean flag = true;
		try {
			if (!testData.get("PCPNPI").trim().isEmpty() && testData.get("PCPNPI").trim() != "none") {
				navigateToPCPTab();
				HashMap<String, String> pcpData = getPCPDetails();
				HashMap<String, String> testDataPCP = new HashMap<String, String>();
				testDataPCP.put("PCPNUMBER", testData.get("PCPNPI").trim());
				testDataPCP.put("PCPDOCTORNAME", testData.get("PCPDoctorName").trim());
				pcpData.remove("PCPSTARTDATE");
				pcpData.remove("PCPENDDATE");
				pcpData.remove("PCPOVERRIDE");
				pcpData.remove("PCPLOCATION");
				pcpData.remove("PCPCLINICNAME");
				pcpData.remove("PCPLINEOFBUSINESS");
				pcpData.remove("PCPDOCTORADDRESS");
				pcpData.remove("PCPDOCTORCITY");
				pcpData.remove("PCPDOCTORSTATE");
				pcpData.remove("PCPDOCTORZIP");

				flag = compareHashMaps(pcpData, testDataPCP);
				if (flag) {
					reportPass("PCP Data Validated : " + pcpData);
				} else {
					reportFail("PCP Data Mismatch. \r\n UI Data: " + pcpData + "\r\n DB Table Data: " + testDataPCP);
				}
			} else {
				test.log(Status.WARNING, "Enrollment Data for PCP missing in readytoenroll data table");
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
			flag = false;
			e.printStackTrace();
			try {
				reportFail("Validation of PCP Details Failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}

	public HashMap<String, String> getPCPDetails() {
		HashMap<String, String> memberPCP = new HashMap<String, String>();
		try {
			String pcpStartDate = getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.pcpStartDate_id)
					.getText();
			String pcpEndDate = getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.pcpEndDate_id)
					.getText();
			String pcpOverride = getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.pcpOverride_id)
					.getText();
			String pcpNumber = getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.pcpNumber_id).getText();
			String pcpLocation = getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.pcpLocation_id)
					.getText();
			String pcpDoctorName = getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.doctorName_id)
					.getText();
			String pcpClinicName = getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.clinicName_id)
					.getText();
			String pcpLineOfBusiness = getElement(LocatorTypes.id,
					M360MembershipObjRepo.M360MemberPage.lineOfBusiness_id).getText();
			String pcpDoctorAddress = getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.doctorAddress_id)
					.getText();
			String pcpDoctorCity = getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.doctorCity_id)
					.getText();
			String pcpDoctorState = getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.doctorState_id)
					.getText();
			String pcpDoctorZip = getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.doctorZip_id)
					.getText();

			String query = "UPDATE VelocityTestAutomation.dbo.member_enrollment SET PCPStartDate ='" + pcpStartDate
					+ "' " + " where MedicareID = '" + testData.get("MedCareID") + "'";
			db.sqlUpdate(query);

			memberPCP.put("PCPSTARTDATE", pcpStartDate);
			memberPCP.put("PCPENDDATE", pcpEndDate);
			memberPCP.put("PCPOVERRIDE", pcpOverride);
			memberPCP.put("PCPNUMBER", pcpNumber);
			memberPCP.put("PCPLOCATION", pcpLocation);
			memberPCP.put("PCPDOCTORNAME", pcpDoctorName);
			memberPCP.put("PCPCLINICNAME", pcpClinicName);
			memberPCP.put("PCPLINEOFBUSINESS", pcpLineOfBusiness);
			memberPCP.put("PCPDOCTORADDRESS", pcpDoctorAddress);
			memberPCP.put("PCPDOCTORCITY", pcpDoctorCity);
			memberPCP.put("PCPDOCTORSTATE", pcpDoctorState);
			memberPCP.put("PCPDOCTORZIP", pcpDoctorZip);

		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Retrieval of PCP data failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return memberPCP;
	}

	public HashMap<String, String> getAgentDetails() {
		HashMap<String, String> testDataMap = new HashMap<String, String>();
		try {
			testDataMap.put("agentPlanId",
					getElement(LocatorTypes.id, M360MembershipObjRepo.M360Agent.agentPlanId_id).getText());
			testDataMap.put("agentStartDate",
					getElement(LocatorTypes.id, M360MembershipObjRepo.M360Agent.agentStartDate_id).getText());
			testDataMap.put("agentEndDate",
					getElement(LocatorTypes.id, M360MembershipObjRepo.M360Agent.agentEndDate_id).getText());
			testDataMap.put("agentOverrideId",
					getElement(LocatorTypes.id, M360MembershipObjRepo.M360Agent.agentOverride_id).getText());
			testDataMap.put("agencyType",
					getElement(LocatorTypes.id, M360MembershipObjRepo.M360Agency.agencytype_id).getText());
			testDataMap.put("agencyName",
					getElement(LocatorTypes.id, M360MembershipObjRepo.M360Agency.agencyname_id).getText());
			testDataMap.put("agencyTin",
					getElement(LocatorTypes.id, M360MembershipObjRepo.M360Agency.agencytin_id).getText());
			testDataMap.put("agencyPhone",
					getElement(LocatorTypes.id, M360MembershipObjRepo.M360Agency.agencyphone_id).getText());
			testDataMap.put("agencyEmail",
					getElement(LocatorTypes.id, M360MembershipObjRepo.M360Agency.agencyemail_id).getText());
			testDataMap.put("agentType",
					getElement(LocatorTypes.id, M360MembershipObjRepo.M360Agent.agenttype_id).getText());
			testDataMap.put("agentName",
					getElement(LocatorTypes.id, M360MembershipObjRepo.M360Agent.agentname_id).getText());
			testDataMap.put("agentTin",
					getElement(LocatorTypes.id, M360MembershipObjRepo.M360Agent.agenttin_id).getText());
			testDataMap.put("agentPhone",
					getElement(LocatorTypes.id, M360MembershipObjRepo.M360Agent.agentphone_id).getText());
			testDataMap.put("agentEmail",
					getElement(LocatorTypes.id, M360MembershipObjRepo.M360Agent.agentemail_id).getText());
			if (testDataMap.get("agentEndDate").equals("99/99/9999")) {
				testDataMap.put("agentEndDate", "09/09/9999");
			}
			logger.info(testDataMap);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("M360AgentPage getAgentDetails failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return testDataMap;
	}

	public boolean validateMemberAgentDetails() {
		boolean flag = true;
		try {
			navigateToAgentTab();
			HashMap<String, String> memAgentDetails = getAgentDetails();
			HashMap<String, String> agentData = new HashMap<String, String>();

			agentData.put("agencyEmail", testData.get("AgencyEmail").toUpperCase());
			agentData.put("agentName", testData.get("AgentName").toUpperCase());
			agentData.put("agencyTin", testData.get("AgencyTIN"));
			agentData.put("agentType", testData.get("AgentType").toUpperCase());
			agentData.put("agentPhone", testData.get("AgentPhone"));
			agentData.put("agentEmail", testData.get("AgentEmail").toUpperCase());
			agentData.put("agencyName", testData.get("AgencyName").toUpperCase());
			agentData.put("agencyPhone", testData.get("AgencyPhone"));
			agentData.put("agencyType", testData.get("AgencyType").toUpperCase());

			memAgentDetails.remove("agentPlanId");
			memAgentDetails.remove("agentEndDate");
			memAgentDetails.remove("agentOverrideId");
			memAgentDetails.remove("agentStartDate");
			memAgentDetails.remove("agentTin");

			flag = compareHashMaps(memAgentDetails, agentData);

			if (flag) {
				reportPass("Agent & Agency Details Verified " + memAgentDetails);
			} else {
				reportFail("Agent & Agency Details Verification failed, screen data: " + memAgentDetails
						+ " does not match DB table data: \r\n" + agentData);
			}
		} catch (IOException e) {
			if (getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MemberPage.pageCannotDisplay_xpath).getText()
					.equalsIgnoreCase("The page cannot be displayed because an internal server error has occurred.")
					|| getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MemberPage.unExpectedResponse).getText()
							.equals("Unexpected Response")
					|| getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MemberPage.unExpectedResponse).getText()
							.contains("The following session is not valid")) {
				driver.quit();
			}
			flag = false;
			e.printStackTrace();
			try {
				reportFail("Agent & Agency Details Verification failed due to exception: " + e.getMessage());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}

	public void navigateToDemographicsTab() {
		try {
			getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.demographicsTab_id).click();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Clicking on Demographics tab failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void navigateToEnrollmentTab() {
		try {
			getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.enrollmentTab_id).click();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Clicking on Enrollment tab failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void navigateToAddressTab() {
		try {
			getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.addressTab_id).click();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Clicking on Address tab failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void navigateToPCPTab() {
		try {
			getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.pcpTab_id).click();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Clicking on PCP tab failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void closeMemberPage() {
		try {
			String parentWindow = null;

			String childWindow = driver.getWindowHandle();
			Set<String> winHandles = driver.getWindowHandles();

			for (String winHandle : winHandles) {
				if (!winHandle.equalsIgnoreCase(childWindow)) {
					driver.switchTo().window(winHandle);
					parentWindow = driver.getWindowHandle();
				}
			}
			driver.switchTo().window(childWindow);
			driver.close();
			driver.switchTo().window(parentWindow);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Closing of window failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void navigateToAgentTab() {
		try {
			getElement(LocatorTypes.id, M360MembershipObjRepo.M360Agent.agentTab_id).click();

		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("M360MemberPage navigateToMemberDetails failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void navigateToAccretionTab() {
		try {
			getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.accretionTab_id).click();

		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("M360MemberPage navigateToAccretion tab failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public HashMap<String, String> getAccretionDetails() {
		HashMap<String, String> temp = new HashMap<String, String>();
		try {
			temp.put("memberID", getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MemberPage.memberID_xpath)
					.getText().trim());
			temp.put("supplementalID",
					getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MemberPage.supplementalID_xpath).getText()
							.trim());
			temp.put("primaryBin",
					getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.primaryBin_id).getText().trim());
			temp.put("primaryPCN",
					getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.primaryPCN_id).getText().trim());
			temp.put("primaryRXID",
					getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.primaryRXId_id).getText().trim());
			temp.put("secondaryBin",
					getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.secondaryBin_id).getText().trim());
			temp.put("primaryRXGroup",
					getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.primaryRXGroup_id).getText()
							.trim());
			temp.put("secondaryPCN",
					getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.secondaryPCN_id).getText().trim());
			temp.put("secondaryRXID", getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.secondaryRXId_id)
					.getText().trim());
			temp.put("createUserId",
					getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.createUserId_id).getText().trim());
			temp.put("dOB", getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.dob_id).getText().trim());
			temp.put("partCAmount",
					getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.partCAmount_id).getText().trim());
			temp.put("partDAmount",
					getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.partDAmount_id).getText().trim());
			temp.put("electionType",
					getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.ElectionType_id).getText().trim());
			temp.put("priorCommercial",
					getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.priorCommercial_id).getText()
							.trim());
			temp.put("disEnrollement",
					getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.disEnrollement_id).getText()
							.trim());
			temp.put("totalUncoveredMonths",
					getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.totalUncoveredMonths_id).getText()
							.trim());
			temp.put("premimumWithHoldOption",
					getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.premiumWithHoldOption_id).getText()
							.trim());
			temp.put("creditableCoverageFlag",
					getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.creditableCoverageFlag_id)
							.getText().trim());
			temp.put("partDOptOut",
					getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.partDOptOut).getText().trim());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return temp;
	}

	public boolean insertUpdateAccretionDetailsDB(HashMap<String, String> temp) {
		boolean flag = true;
		try {
			if (!db.checkExistingRecordInTable("member_accretion_details", "MedicareID", testData.get("MedCareID"))) {
				String insertQuery = "INSERT INTO [VelocityTestAutomation].[dbo].[member_accretion_details]"
						+ "([MedicareID]" + ",[SupplementalID]" + ",[MemberID]" + ",[PrimaryBin]" + ",[PrimaryPCN]"
						+ ",[PrimaryRXID]" + ",[SecondaryBin]" + ",[PrimaryRxGroup]" + ",[SecondaryPCN]"
						+ ",[SecondaryRXID]" + ",[CreateUserId]" + ",[DOB]" + ",[PartCAmount]" + ",[PartDAmount]"
						+ ",[ElectionType]" + ",[PriorCommercialOvrInd]" + ",[DisEnrollmentReason]"
						+ ",[TotalUncoveredMonths]" + ",[PremiumWithholdOption]" + ",[CreditableCoverageFalg]"
						+ ",[PartDOptOutInd])" + "     VALUES " + "('" + testData.get("MedCareID") + "','"
						+ temp.get("supplementalID") + "','" + temp.get("memberID") + "','" + temp.get("primaryBin")
						+ "','" + temp.get("primaryPCN") + "','" + temp.get("primaryRXID") + "','"
						+ temp.get("secondaryBin") + "','" + temp.get("primaryRXGroup") + "','"
						+ temp.get("secondaryPCN") + "','" + temp.get("secondaryRXID") + "','"
						+ temp.get("createUserId") + "','" + temp.get("dOB") + "','" + temp.get("partCAmount") + "','"
						+ temp.get("partDAmount") + "','" + temp.get("electionType") + "','"
						+ temp.get("priorCommercial") + "','" + temp.get("disEnrollement") + "','"
						+ temp.get("totalUncoveredMonths") + "','" + temp.get("premimumWithHoldOption") + "','"
						+ temp.get("creditableCoverageFlag") + "','" + temp.get("partDOptOut") + "');";

				db.sqlUpdate(insertQuery);
			} else {

				String updateQuery = "UPDATE [dbo].[member_accretion_details]" + "   SET [SupplementalID] = '"
						+ temp.get("supplementalID") + "',[MemberID] = '" + temp.get("memberID") + "',[PrimaryBin] ='"
						+ temp.get("primaryBin") + "',[PrimaryPCN] = '" + temp.get("primaryPCN") + "',[PrimaryRXID] = '"
						+ temp.get("primaryRXID") + "',[SecondaryBin] = '" + temp.get("secondaryBin")
						+ "',[PrimaryRxGroup] = '" + temp.get("primaryRXGroup") + "',[SecondaryPCN] = '"
						+ temp.get("secondaryPCN") + "',[SecondaryRXID] = '" + temp.get("secondaryRXID")
						+ "',[CreateUserId] ='" + temp.get("createUserId") + "',[DOB] = '" + temp.get("dOB")
						+ "',[PartCAmount] = '" + temp.get("partCAmount") + "',[PartDAmount] = '"
						+ temp.get("partDAmount") + "',[ElectionType] = '" + temp.get("electionType")
						+ "',[PriorCommercialOvrInd] = '" + temp.get("priorCommercial") + "',[DisEnrollmentReason] = '"
						+ temp.get("disEnrollement") + "',[TotalUncoveredMonths] = '" + temp.get("totalUncoveredMonths")
						+ "',[PremiumWithholdOption] = '" + temp.get("premimumWithHoldOption")
						+ "',[CreditableCoverageFalg] = '" + temp.get("creditableCoverageFlag")
						+ "',[PartDOptOutInd] = '" + temp.get("partDOptOut") + "'" + " WHERE [MedicareID] = '"
						+ testData.get("MedCareID") + "'";
				db.sqlUpdate(updateQuery);
			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}
		return flag;
	}

	public void memberAccretionDetails() {
		navigateToAccretionTab();
		boolean flag = insertUpdateAccretionDetailsDB(getAccretionDetails());

		try {
			if (flag == true)
				reportPass("Accretion Details are captured successfully");
			else
				reportFail("Capturing Accretion Details failed");
		} catch (IOException e) {
			if (getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MemberPage.pageCannotDisplay_xpath).getText()
					.equalsIgnoreCase("The page cannot be displayed because an internal server error has occurred.")
					|| getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MemberPage.unExpectedResponse).getText()
							.equals("Unexpected Response")
					|| getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MemberPage.unExpectedResponse).getText()
							.contains("The following session is not valid")) {
				driver.quit();
			}
			e.printStackTrace();
		}

	}

	public void memberLIS_LEPDetails() {
		HashMap<String, String> tempLIS, tempLEP = new HashMap<String, String>();

		navigateToLISTab();
		tempLIS = getLISDetails();
		navigateToLEPTab();
		tempLEP = getLEPDetails();

		boolean flag = insertLISLEPDetailsToDB(tempLIS, tempLEP);

		try {
			if (flag == true)
				reportPass("LIS LEP Details are captured successfully");
			else
				reportFail("Capturing LIS LEP Details failed");
		} catch (IOException e) {
			if (getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MemberPage.pageCannotDisplay_xpath).getText()
					.equalsIgnoreCase("The page cannot be displayed because an internal server error has occurred.")
					|| getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MemberPage.unExpectedResponse).getText()
							.equals("Unexpected Response")
					|| getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MemberPage.unExpectedResponse).getText()
							.contains("The following session is not valid")) {
				driver.quit();
			}
			e.printStackTrace();
		}

	}

	public void memberCOBDetails() {
		HashMap<String, String> temp = new HashMap<String, String>();
		navigateToCOBTab();
		temp = getCOBDetails();
		boolean flag = insertUpdateCOBDetailsToDB(temp);

		try {
			if (flag == true)
				reportPass("COB Details are captured successfully");
			else
				reportFail("Capturing COB Details failed");
		} catch (IOException e) {
			if (getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MemberPage.pageCannotDisplay_xpath).getText()
					.equalsIgnoreCase("The page cannot be displayed because an internal server error has occurred.")
					|| getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MemberPage.unExpectedResponse).getText()
							.equals("Unexpected Response")
					|| getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MemberPage.unExpectedResponse).getText()
							.contains("The following session is not valid")) {
				driver.quit();
			}
			e.printStackTrace();
		}
	}

	public boolean insertLISLEPDetailsToDB(HashMap<String, String> tempLISDetails,
			HashMap<String, String> tempLEPDetails) {
		boolean flag = true;
		try {
			if (!db.checkExistingRecordInTable("member_LIS_LEP_details", "MedicareID", testData.get("MedCareID"))) {
				String insertQuery = "INSERT INTO [VelocityTestAutomation].[dbo].[member_LIS_LEP_details]"
						+ "([MedicareID] ,[SupplementalID]" + ",[MemberID]" + ",[SubsidySource]" + ",[LISOverride]"
						+ ",[CoPay]" + ",[LICBAE]" + ",[LISPercent]" + ",[LISBAE]" + ",[LISAmount]" + ",[SPAPAmt]"
						+ ",[LESOverride]" + ",[AttestationLockIndicator]" + ",[LESType]" + ",[UncoveredMonths]"
						+ ",[LEPAmount]" + ",[LEPWaivedAmt]" + ",[BypassEdits]" + ",[TriggerTXN73])" + "     VALUES"
						+ "('" + testData.get("MedCareID") + "','" + tempLISDetails.get("supplementalID") + "','"
						+ tempLISDetails.get("memberID") + "','" + tempLISDetails.get("subsidySource") + "','"
						+ tempLISDetails.get("lisOverride") + "','" + tempLISDetails.get("coPay") + "','"
						+ tempLISDetails.get("licBAE") + "','" + tempLISDetails.get("percent") + "','"
						+ tempLISDetails.get("LISBAE") + "','" + tempLISDetails.get("LISAmt") + "','"
						+ tempLISDetails.get("spapAmt") + "','" + tempLEPDetails.get("lesOverride") + "','"
						+ tempLEPDetails.get("attestLockIndicator") + "','" + tempLEPDetails.get("lepType") + "','"
						+ tempLEPDetails.get("noOfUncoveredMonths") + "','" + tempLEPDetails.get("lepAmt") + "','"
						+ tempLEPDetails.get("lepWaived") + "','" + tempLEPDetails.get("bypassEdits") + "','"
						+ tempLEPDetails.get("triggerTRX73") + "');";

				db.sqlUpdate(insertQuery);
			} else {
				String updateQuery = "UPDATE [VelocityTestAutomation].[dbo].[member_LIS_LEP_details]"
						+ "   SET [SupplementalID] ='" + tempLISDetails.get("supplementalID") + "',[MemberID] ='"
						+ tempLISDetails.get("memberID") + "',[SubsidySource] ='" + tempLISDetails.get("subsidySource")
						+ "',[LISOverride] ='" + tempLISDetails.get("lisOverride") + "',[CoPay] ='"
						+ tempLISDetails.get("coPay") + "',[LICBAE] ='" + tempLISDetails.get("licBAE")
						+ "',[LISPercent] ='" + tempLISDetails.get("percent") + "',[LISBAE] ='"
						+ tempLISDetails.get("LISBAE") + "',[LISAmount] ='" + tempLISDetails.get("LISAmt")
						+ "',[SPAPAmt] ='" + tempLISDetails.get("spapAmt") + "',[LESOverride] ='"
						+ tempLEPDetails.get("lesOverride") + "',[AttestationLockIndicator] ='"
						+ tempLEPDetails.get("attestLockIndicator") + "',[LESType] ='" + tempLEPDetails.get("lepType")
						+ "',[UncoveredMonths] ='" + tempLEPDetails.get("noOfUncoveredMonths") + "',[LEPAmount] ='"
						+ tempLEPDetails.get("lepAmt") + "',[LEPWaivedAmt] ='" + tempLEPDetails.get("lepWaived")
						+ "',[BypassEdits] ='" + tempLEPDetails.get("bypassEdits") + "',[TriggerTXN73] ='"
						+ tempLEPDetails.get("triggerTRX73") + "' WHERE MedicareID='" + testData.get("MedCareID") + "'";

				db.sqlUpdate(updateQuery);
			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}

		return flag;
	}

	public void navigateToLISTab() {
		try {
			getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.LISTab_id).click();

		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("M360MemberPage navigateToLISTab failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void navigateToLEPTab() {
		try {
			getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.LEPTab_id).click();

		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("M360MemberPage navigateToLEPTab failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void navigateToCOBTab() {
		try {
			getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.COBTab_id).click();

		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("M360MemberPage navigateToCOBTab failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public HashMap<String, String> getLISDetails() {

		HashMap<String, String> temp = new HashMap<String, String>();

		try {
			temp.put("memberID", getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MemberPage.memberID_xpath)
					.getText().trim());
			temp.put("supplementalID",
					getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MemberPage.supplementalID_xpath).getText()
							.trim());
			temp.put("subsidySource", getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.subsidySource_id)
					.getText().trim());
			temp.put("lisOverride",
					getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.lis_overRide_id).getText().trim());
			temp.put("coPay",
					getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.coPay_id).getText().trim());
			temp.put("licBAE",
					getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.licBAE_id).getText().trim());
			temp.put("percent",
					getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.percent_id).getText().trim());
			temp.put("LISBAE",
					getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.LISBAE_id).getText().trim());
			temp.put("LISAmt",
					getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.LISAmt_id).getText().trim());
			temp.put("spapAmt",
					getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.spapAmt_id).getText().trim());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return temp;

	}

	public HashMap<String, String> getLEPDetails() {

		HashMap<String, String> temp = new HashMap<String, String>();

		try {
			temp.put("lesOverride",
					getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.les_override_id).getText().trim());
			temp.put("attestLockIndicator",
					getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.attest_lock_indicator_id).getText()
							.trim());
			temp.put("lepType",
					getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.lepType_id).getText().trim());
			temp.put("noOfUncoveredMonths",
					getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.noOfUncoveredMonths_id).getText()
							.trim());
			temp.put("lepAmt",
					getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.lepAmt_id).getText().trim());
			temp.put("lepWaived",
					getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.lepWaived_id).getText().trim());
			temp.put("bypassEdits",
					getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.bypassEdits_id).getText().trim());
			temp.put("triggerTRX73",
					getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.triggerTRX73_id).getText().trim());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return temp;

	}

	public HashMap<String, String> getCOBDetails() {

		HashMap<String, String> temp = new HashMap<String, String>();
		try {
			temp.put("memberID", getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MemberPage.memberID_xpath)
					.getText().trim());
			temp.put("supplementalID",
					getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MemberPage.supplementalID_xpath).getText()
							.trim());
			temp.put("COBType",
					getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.COBType_id).getText().trim());
			temp.put("cobOverride",
					getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.cob_override_id).getText().trim());
			temp.put("OHI", getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.OHI_id).getText().trim());
			temp.put("rxGrp",
					getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.rxGrp_id).getText().trim());
			temp.put("rxName",
					getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.rxName_id).getText().trim());
			temp.put("rxID",
					getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.rxID_id).getText().trim());
			temp.put("rxBIN",
					getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.rxBIN_id).getText().trim());
			temp.put("rxPCN",
					getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.rxPCN_id).getText().trim());
		} catch (Exception e) {
			e.printStackTrace();
		}

		return temp;

	}

	public boolean insertUpdateCOBDetailsToDB(HashMap<String, String> temp) {
		boolean flag = true;

		try {
			if (!db.checkExistingRecordInTable("member_COB_details", "MedicareID", testData.get("MedCareID"))) {
				String insertQuery = "INSERT INTO [VelocityTestAutomation].[dbo].[member_COB_details]" + "([MemberID]"
						+ ",[SupplementalID]" + ",[MedicareID]" + ",[COBType]" + ",[COBOverride]" + ",[OHI]"
						+ ",[RXGroup]" + ",[RXName]" + ",[RXID]" + ",[RXBIN]" + ",[RXPCN])" + "     VALUES" + "('"
						+ temp.get("memberID") + "','" + temp.get("supplementalID") + "','" + testData.get("MedCareID")
						+ "','" + temp.get("COBType") + "','" + temp.get("cobOverride") + "','" + temp.get("OHI")
						+ "','" + temp.get("rxGrp") + "','" + temp.get("rxName") + "','" + temp.get("rxID") + "','"
						+ temp.get("rxBIN") + "','" + temp.get("rxPCN") + "');";

				db.sqlUpdate(insertQuery);
			} else {
				String updateQuery = "UPDATE [VelocityTestAutomation].[dbo].[member_COB_details]"
						+ "   SET [MemberID] ='" + temp.get("memberID") + "',[SupplementalID] ='"
						+ temp.get("supplementalID") + "',[COBType] ='" + temp.get("COBType") + "',[COBOverride] ='"
						+ temp.get("cobOverride") + "',[OHI] ='" + temp.get("OHI") + "',[RXGroup] ='"
						+ temp.get("rxGrp") + "',[RXName] ='" + temp.get("rxName") + "',[RXID] ='" + temp.get("rxID")
						+ "',[RXBIN] ='" + temp.get("rxBIN") + "',[RXPCN] ='" + temp.get("rxPCN")
						+ "' WHERE [MedicareID] ='" + testData.get("MedCareID") + "'";
				db.sqlUpdate(updateQuery);
			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
		}

		return flag;
	}

	public void getDSInfo() {
		try {
			getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MemberPage.dsInfoTab_xpath).click();
			String partAStartDate = getElement(LocatorTypes.xpath,
					M360MembershipObjRepo.M360MemberPage.partAStartDate_xpath).getText().trim();
			String partBStartDate = getElement(LocatorTypes.xpath,
					M360MembershipObjRepo.M360MemberPage.partBStartDate_xpath).getText().trim();
			String partDStartDate = getElement(LocatorTypes.xpath,
					M360MembershipObjRepo.M360MemberPage.partDStartDate_xpath).getText().trim();
			String query = "UPDATE VelocityTestAutomation.dbo.member_enrollment SET PartAStartDate ='" + partAStartDate
					+ "', " + "PartBStartDate = '" + partBStartDate + "', " + "PartDStartDate = '" + partDStartDate
					+ "'" + "where MedicareID = '" + testData.get("MedCareID") + "'";
			db.sqlUpdate(query);
			reportPass("Capture DSInfo details successful");
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
				reportFail("Capture DSInfo details failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	@SuppressWarnings("null")
	public HashMap<String, String> getDSInformation() {
		HashMap<String, String> dsInfo = null;
		try {
			getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MemberPage.dsInfoTab_xpath).click();
			String partAStartDate = getElement(LocatorTypes.xpath,
					M360MembershipObjRepo.M360MemberPage.partAStartDate_xpath).getText().trim();
			String partBStartDate = getElement(LocatorTypes.xpath,
					M360MembershipObjRepo.M360MemberPage.partBStartDate_xpath).getText().trim();
			String partDStartDate = getElement(LocatorTypes.xpath,
					M360MembershipObjRepo.M360MemberPage.partDStartDate_xpath).getText().trim();
			dsInfo.put("PARTASTART", partAStartDate);
			dsInfo.put("PARTBSTART", partBStartDate);
			dsInfo.put("PARTDSTART", partDStartDate);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Capture DSInfo details failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return dsInfo;
	}

	public void updateResults(int id, String status) {
		Connection connection = null;
		Statement statement = null;
		try {
			connection = accessDb.getConnection();
			statement = connection.createStatement();
			String query = "UPDATE MembershipDemo_20190710150325 SET FILLER_2 = '" + status + "' WHERE ID = " + id;
			statement.executeUpdate(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void validateMemberData() {
		pm.getM360LoginPage().login();
		navigateToMemberDetails();
		Connection connection = null;
		Statement statement = null;
		Statement addStatement = null;
		ResultSet resultSet = null;
		ResultSet resultSetAdd = null;
		boolean flag = true;
		try {
			connection = accessDb.getConnection();
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM MembershipDemo_20190710150325 WHERE FILLER_2 is null;");
			while (resultSet.next()) {
				String memSuppID = resultSet.getString("MEMBERID");
				test = extent.createTest(memSuppID);
				flag = validateMemberDemographicData(resultSet, memSuppID);
				navigateToAddressTab();
				addStatement = connection.createStatement();
				resultSetAdd = addStatement
						.executeQuery("SELECT * FROM MembershipAddress_20190710150327 WHERE ((MEMBERID= '" + memSuppID
								+ "') AND (ADDRESSTYPE = 'PRIM') AND (EFFECTIVEENDDATE = '99991231'));");
				resultSetAdd.next();
				flag = validateMemberAddressData(resultSetAdd);
				if (flag)
					updateResults(resultSet.getInt("ID"), "PASSED");
				else
					updateResults(resultSet.getInt("ID"), "FAILED");
				flushTest();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				reportFail("M360MemberPage validateMembers Failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} finally {
			try {
				if (null != connection) {
					resultSet.close();
					statement.close();
					connection.close();
				}
			} catch (SQLException s) {
				s.printStackTrace();
			}
		}
	}

	public boolean validateMemberAddressData(ResultSet resultSet) {
		boolean flag = true;
		try {
			HashMap<String, String> memDataAddress = getAddressDetails();
			HashMap<String, String> testDataAddress = new HashMap<String, String>();

			testDataAddress.put("ADDRESSTYPE", resultSet.getString("ADDRESSTYPE").trim());
			/*
			 * testDataAddress.put("ADDRESS", testData.get("Address1").toUpperCase() +
			 * testData.get("Address2").toUpperCase() +
			 * testData.get("Address3").toUpperCase());
			 */
			testDataAddress.put("CITY", resultSet.getString("CITY").trim());
			testDataAddress.put("STATE", resultSet.getString("STATE").trim());
			testDataAddress.put("ZIPCODE", resultSet.getString("ZIPCODE").trim());
			testDataAddress.put("HOMEPHONE", resultSet.getString("HOMEPHONE").trim());
			testDataAddress.put("CELLPHONE", resultSet.getString("CELLPHONE").trim());
			testDataAddress.put("WORKPHONE", resultSet.getString("WORKPHONE").trim());
			testDataAddress.put("FAXNUMBER", resultSet.getString("FAXNUMBER").trim());

			/*
			 * memDataAddress.put("ADDRESS", memDataAddress.get("ADDRESS1") +
			 * memDataAddress.get("ADDRESS2") + memDataAddress.get("ADDRESS3"));
			 */
			memDataAddress.remove("ADDRESS1");
			memDataAddress.remove("ADDRESS2");
			memDataAddress.remove("ADDRESS3");
			memDataAddress.remove("COUNTYCODE");

			flag = compareHashMaps(memDataAddress, testDataAddress);
			if (flag)
				reportPass("Member Verified " + memDataAddress);
			else
				reportFail("Member Verification failed. \r\n UI Data: " + memDataAddress + "\r\n Table DB Data: "
						+ testDataAddress);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("M360MemberPage validateMemberAddressData Failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}

	public void validateMemDemo(HashMap<String, String> memberDemoData) {
		String query = null;
		boolean flag = false;
		try {
			HashMap<String, String> testDataMemDemo = new HashMap<String, String>();

			testDataMemDemo.put("MEMBERPREFIX", testData.get("MEMBERPREFIX").trim().toUpperCase());
			testDataMemDemo.put("MEMBERFIRSTNAME", testData.get("MEMBERFIRSTNAME").trim().toUpperCase());
			testDataMemDemo.put("MEMBERMIDDLEINITIAL", testData.get("MEMBERMIDDLEINITIAL").trim().toUpperCase());
			testDataMemDemo.put("MEMBERLASTNAME", testData.get("MEMBERLASTNAME").trim().toUpperCase());
			testDataMemDemo.put("MEMBERGENDER", testData.get("MEMBERGENDER").trim().toUpperCase());
			testDataMemDemo.put("MEMBERBIRTHDATE", testData.get("MEMBERBIRTHDATE").trim().toUpperCase());
			memberDemoData.remove("HICNO");
			memberDemoData.remove("CMSEFFECTIVEMONTH");
			memberDemoData.remove("MEDICARENUMBER");
			memberDemoData.remove("MEMBERSUFFIX");
			memberDemoData.remove("MEMBERID");
			memberDemoData.remove("MEMBERSSN");

			flag = compareHashMaps(memberDemoData, testDataMemDemo);

			if (flag) {
				query = "UPDATE MembershipDemo SET FILLER = 'PASSED' WHERE ID = "
						+ Integer.parseInt(testData.get("ID"));
				test.log(Status.PASS, "Member Demographic Details Passed: " + testDataMemDemo);
			} else {
				query = "UPDATE MembershipDemo SET FILLER = 'FAILED' WHERE ID = "
						+ Integer.parseInt(testData.get("ID"));
				test.log(Status.FAIL, "Member Demographic Details Failed. Screen Data = " + memberDemoData
						+ "Table DB Data = " + testDataMemDemo);
			}
		} catch (Exception e) {
			query = "UPDATE MembershipDemo SET FILLER = 'EXCEPTION' WHERE ID = " + Integer.parseInt(testData.get("ID"))
					+ ";";
			test.log(Status.FATAL, "Member Demographic Details Exception occoured in data = " + memberDemoData);
			e.printStackTrace();
			if (getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MemberPage.pageCannotDisplay_xpath).getText()
					.equalsIgnoreCase("The page cannot be displayed because an internal server error has occurred.")) {
				driver.quit();
			}
		} finally {
			accessDb.updateDBTestData(query);
		}
	}

	public void validateEnrollData(HashMap<String, String> enrollData) {
		String query = null;
		boolean flag = false;
		try {
			HashMap<String, String> memberEnrollment = new HashMap<String, String>();

			memberEnrollment.put("ENROLLMENTSTATUS", testData.get("ENROLLSTATUS").trim());
			memberEnrollment.put("ENROLLMENTREASON", testData.get("ENROLLREASONCODE").trim());
			memberEnrollment.put("ENROLLMENTSTARTDATE",
					getDateInMMDDYYYYFROMYYYYMMDD(testData.get("EFFECTIVESTARTDATE").trim()));
			memberEnrollment.put("ENROLLMENTGROUPID", testData.get("GROUPID").trim());
			memberEnrollment.put("ENROLLMENTPRODUCTID", testData.get("PRODUCTID").trim());
			memberEnrollment.put("ENROLLMENTPLANID", testData.get("PLANID").trim());
			memberEnrollment.put("ENROLLMENTPBPID", testData.get("PBPID").trim());
			memberEnrollment.put("ENROLLMENTSEGMENT", testData.get("PBPSEGMENTID").trim());
			memberEnrollment.put("ENROLLMENTDESIGNATION", testData.get("PLANDESIGNATION").trim());
			// memberEnrollment.put("ENROLLMENTELECTIONTYPE",
			// testData.get("ELECTIONTYPE").trim());
			memberEnrollment.put("ENROLLMENTSUPPLEMENTALID", testData.get("SUPPLEMENTALID").trim());
			memberEnrollment.put("ENROLLMENTRXID", testData.get("RXID").trim());
			memberEnrollment.put("ENROLLMENTAPPLICATIONDATE",
					getDateInMMDDYYYYFROMYYYYMMDD(testData.get("APPLICATIONDATE").trim()));
			memberEnrollment.put("ENROLLMENTSIGNATUREDATE",
					getDateInMMDDYYYYFROMYYYYMMDD(testData.get("SIGNATUREDATE").trim()));

			enrollData.remove("ENROLLMENTGROUPNAME");
			enrollData.remove("ENROLLMENTPRODUCTNAME");
			enrollData.remove("ENROLLMENTCANCELLATIONREASON");
			enrollData.remove("ENROLLMENTDISREASON");
			enrollData.remove("ENROLLMENTTYPE");
			enrollData.remove("ENROLLMENTENDDATE");
			enrollData.remove("ENROLLMENTELECTIONTYPE");

			flag = compareHashMaps(enrollData, memberEnrollment);

			if (flag) {
				query = "UPDATE MembershipEnrollment SET FILLER = 'PASSED' WHERE ID = "
						+ Integer.parseInt(testData.get("ENR_ID"));
				test.log(Status.PASS, "Member Enrollment Details Passed: " + memberEnrollment);
			} else {
				query = "UPDATE MembershipEnrollment SET FILLER = 'FAILED' WHERE ID = "
						+ Integer.parseInt(testData.get("ENR_ID"));
				test.log(Status.FAIL, "Member Enrollment Details Failed. Screen Data = " + enrollData
						+ "Table DB Data = " + memberEnrollment);
			}
			accessDb.updateDBTestData(query);
		} catch (Exception e) {
			query = "UPDATE MembershipEnrollment SET FILLER = 'EXCEPTION' WHERE ID = "
					+ Integer.parseInt(testData.get("ENR_ID"));
			test.log(Status.FATAL, "Member Enrollment Details Exception occoured in data = " + enrollData);
			e.printStackTrace();
			if (getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MemberPage.pageCannotDisplay_xpath).getText()
					.equalsIgnoreCase("The page cannot be displayed because an internal server error has occurred.")) {
				driver.quit();
			}
		}
	}

	public void validateAddressData(HashMap<String, String> addressData) {
		String query = null;
		boolean flag = false;
		HashMap<String, String> memberAddress = new HashMap<String, String>();
		try {
			memberAddress.put("ADDRESSTYPE", testData.get("ADDRESSTYPE").trim().toUpperCase());
			// memberAddress.put("ADDRESS1", testData.get("ADDRESS1").trim().toUpperCase());
			memberAddress.put("ADDRESS2", testData.get("ADDRESS2").trim().toUpperCase());
			memberAddress.put("ADDRESS3", testData.get("ADDRESS3").trim().toUpperCase());
			memberAddress.put("CITY", testData.get("CITY").trim().toUpperCase());
			memberAddress.put("STATE", testData.get("STATE").trim().toUpperCase());
			memberAddress.put("ZIPCODE", testData.get("ZIPCODE").trim().toUpperCase().replace("-", ""));
			memberAddress.put("HOMEPHONE", testData.get("HOMEPHONE").trim().toUpperCase());
			memberAddress.put("WORKPHONE", testData.get("WORKPHONE").trim().toUpperCase());
			memberAddress.put("CELLPHONE", testData.get("CELLPHONE").trim().toUpperCase());
			addressData.remove("COUNTYCODE");
			addressData.remove("ADDRESS1");
			addressData.replace("ZIPCODE", addressData.get("ZIPCODE").replace("-", ""));

			flag = compareHashMaps(addressData, memberAddress);
			if (flag) {
				test.log(Status.PASS, "Member Address Details Passed: " + memberAddress);
				query = "UPDATE MembershipAddress SET FILLER = 'PASSED' WHERE ID = "
						+ Integer.parseInt(testData.get("ADD_ID"));
			} else {
				test.log(Status.FAIL, "Member Address Details Failed. \r\n Screen Data = " + addressData
						+ "\r\n Table DB Data = " + memberAddress);
				query = "UPDATE MembershipAddress SET FILLER = 'FAILED' WHERE ID = "
						+ Integer.parseInt(testData.get("ADD_ID"));
			}
		} catch (Exception e) {
			query = "UPDATE MembershipAddress SET FILLER = 'EXCEPTION' WHERE ID = "
					+ Integer.parseInt(testData.get("ADD_ID"));
			test.log(Status.FATAL, "Member Address Details Exception occoured in data = " + addressData);
			e.printStackTrace();
			if (getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MemberPage.pageCannotDisplay_xpath).getText()
					.equalsIgnoreCase("The page cannot be displayed because an internal server error has occurred.")) {
				driver.quit();
			}
		} finally {
			accessDb.updateDBTestData(query);
		}
	}

	public void validateAgentData(HashMap<String, String> agentData) {
		String query = null;
		boolean flag = false;
		HashMap<String, String> memberAgent = new HashMap<String, String>();
		try {
			memberAgent.put("agencyName", testData.get("AGENCYNAME"));
			memberAgent.put("agencyTin", testData.get("AGENCYTIN"));
			agentData.remove("agentName");
			agentData.remove("agentTin");
			agentData.remove("agentPhone");
			agentData.remove("agentEmail");
			agentData.remove("agentEndDate");
			agentData.remove("agentOverrideId");
			agentData.remove("agentType");
			agentData.remove("agentName");
			agentData.remove("agentTin");
			agentData.remove("agentPhone");
			agentData.remove("agentEmail");
			agentData.remove("agentEndDate");
			agentData.remove("agencyPhone");
			agentData.remove("agencyEmail");
			agentData.remove("agentPlanId");
			agentData.remove("agencyPhone");
			agentData.remove("agencyType");
			agentData.remove("agentStartDate");
			// agentData.remove("agencyTin");

			flag = compareHashMaps(agentData, memberAgent);

			if (flag) {
				query = "UPDATE MembershipAgent SET FILLER = 'PASSED' WHERE ID = " + testData.get("AGT_ID");
				test.log(Status.PASS, "Member Agent Details Passed: " + memberAgent);
			} else {
				query = "UPDATE MembershipAgent SET FILLER = 'FAILED' WHERE ID = " + testData.get("AGT_ID");
				test.log(Status.FAIL, "Member Agent Details Failed. \r\n Screen Data = " + agentData
						+ " \r\n Table DB Data = " + memberAgent);
			}
		} catch (Exception e) {
			e.printStackTrace();
			query = "UPDATE MembershipAgent SET FILLER = 'EXCEPTION' WHERE ID = " + testData.get("AGT_ID");
			test.log(Status.FATAL, "Member Agent Details Exception occoured in data = " + agentData);
			if (getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MemberPage.pageCannotDisplay_xpath).getText()
					.equalsIgnoreCase("The page cannot be displayed because an internal server error has occurred.")) {
				driver.quit();
			}
		} finally {
			accessDb.updateDBTestData(query);
		}
	}

	public void validatePCPData(HashMap<String, String> pcpData) {
		String query = null;
		boolean flag = false;
		HashMap<String, String> memberPCP = new HashMap<String, String>();
		try {
			memberPCP.put("PCPDOCTORNAME", testData.get("PCPNAME").toUpperCase());
			memberPCP.put("PCPDOCTORADDRESS", testData.get("PCPADDRESS").toUpperCase());
			memberPCP.put("PCPDOCTORCITY", testData.get("PCPCITY").toUpperCase());
			memberPCP.put("PCPDOCTORSTATE", testData.get("PCPSTATE").toUpperCase());
			memberPCP.put("PCPDOCTORZIP", testData.get("PCPZIP"));
			pcpData.remove("PCPENDDATE");
			pcpData.remove("PCPOVERRIDE");
			pcpData.remove("PCPNUMBER");
			pcpData.remove("PCPLOCATION");
			pcpData.remove("PCPCLINICNAME");
			pcpData.remove("PCPLINEOFBUSINESS");
			pcpData.remove("PCPSTARTDATE");

			flag = compareHashMaps(pcpData, memberPCP);

			if (flag) {
				query = "UPDATE MembershipPCP SET FILLER = 'PASSED' WHERE ID = "
						+ Integer.parseInt(testData.get("PCP_ID"));
				test.log(Status.PASS, "Member PCP Details Passed: " + memberPCP);
			} else {
				query = "UPDATE MembershipPCP SET FILLER = 'FAILED' WHERE ID = "
						+ Integer.parseInt(testData.get("PCP_ID"));
				test.log(Status.FAIL, "Member PCP Details Failed. \r\n Screen Data = " + pcpData
						+ " \r\n Table DB Data = " + memberPCP);
			}
		} catch (Exception e) {
			query = "UPDATE MembershipPCP SET FILLER = 'EXCEPTION' WHERE ID = "
					+ Integer.parseInt(testData.get("PCP_ID"));
			test.log(Status.FATAL, "Member PCP Details Exception occoured in data = " + pcpData);
			e.printStackTrace();
			if (getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MemberPage.pageCannotDisplay_xpath).getText()
					.equalsIgnoreCase("The page cannot be displayed because an internal server error has occurred.")) {
				driver.quit();
			}
		} finally {
			accessDb.updateDBTestData(query);
		}
	}

	public void validateCOBData(HashMap<String, String> cobData) {
		String query = null;
		boolean flag = false;
		HashMap<String, String> memberCOB = new HashMap<String, String>();
		try {
			memberCOB.put("memberID", testData.get("memberID").trim());
			memberCOB.put("supplementalID", testData.get("supplementalID").trim());
			memberCOB.put("cobOverride", testData.get("cobOverride").trim());
			memberCOB.put("OHI", testData.get("OHI").trim());
			memberCOB.put("COBType", testData.get("COBType").trim());
			memberCOB.put("rxGrp", testData.get("rxGrp").trim());
			memberCOB.put("rxName", testData.get("rxName").trim());
			memberCOB.put("rxID", testData.get("rxID").trim());
			memberCOB.put("rxBIN", testData.get("rxBIN").trim());
			memberCOB.put("rxPCN", testData.get("rxPCN").trim());

			flag = compareHashMaps(cobData, memberCOB);

			if (flag) {
				query = "UPDATE MembershipEnrollment SET FILLER_2 = 'PASSED' WHERE ID = "
						+ Integer.parseInt(testData.get("ENR_ID"));
			} else {
				query = "UPDATE MembershipEnrollment SET FILLER_2 = 'FAILED' WHERE ID = "
						+ Integer.parseInt(testData.get("ENR_ID"));
			}
			accessDb.updateDBTestData(query);
		} catch (Exception e) {
			e.printStackTrace();
			if (getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MemberPage.pageCannotDisplay_xpath).getText()
					.equalsIgnoreCase("The page cannot be displayed because an internal server error has occurred.")) {
				driver.quit();
			}
		}
	}

	public void validateLEPData(HashMap<String, String> lepData) {
		String query = null;
		boolean flag = false;
		DecimalFormat df = new DecimalFormat("####0.00");
		HashMap<String, String> memberLEP = new HashMap<String, String>();
		try {
			if (!testData.get("LEP_ID").isEmpty()) {
				if (!testData.get("LEPAMOUNT").isEmpty())
					memberLEP.put("lepAmt", df.format(Double.parseDouble(testData.get("LEPAMOUNT"))));
				if (!testData.get("NUMBEROFUNCOVEREDMONTHS").isEmpty())
					memberLEP.put("noOfUncoveredMonths", testData.get("NUMBEROFUNCOVEREDMONTHS").trim());
				if (!testData.get("LEPWAIVEDAMOUNT").isEmpty())
					memberLEP.put("lepWaived", df.format(Double.parseDouble(testData.get("LEPWAIVEDAMOUNT"))));
				lepData.remove("lesOverride");
				lepData.remove("attestLockIndicator");
				lepData.remove("lepType");
				lepData.remove("bypassEdits");
				lepData.remove("triggerTRX73");

				flag = compareHashMaps(lepData, memberLEP);

				if (flag) {
					query = "UPDATE MEMLEP SET FILLER = 'PASSED' WHERE ID = " + Integer.parseInt(testData.get("LEP_ID"))
							+ ";";
					test.log(Status.PASS, "Member LEP Details Passed: " + memberLEP);
				} else {
					query = "UPDATE MEMLEP SET FILLER = 'FAILED' WHERE ID = " + Integer.parseInt(testData.get("LEP_ID"))
							+ ";";
					test.log(Status.FAIL, "Member LEP Details Failed. \r\n Screen Data = " + lepData
							+ " \r\n Table DB Data = " + memberLEP);
				}
			}
		} catch (Exception e) {
			query = "UPDATE MEMLEP SET FILLER = 'EXCEPTION' WHERE ID = " + Integer.parseInt(testData.get("LEP_ID"))
					+ ";";
			test.log(Status.FATAL, "Member LEP Details Exception occoured in data = " + lepData);
			e.printStackTrace();
			if (getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MemberPage.pageCannotDisplay_xpath).getText()
					.equalsIgnoreCase("The page cannot be displayed because an internal server error has occurred.")) {
				driver.quit();
			}
		} finally {
			if (!testData.get("LEP_ID").isEmpty())
				accessDb.updateDBTestData(query);
		}
	}

	public void validateLISData(HashMap<String, String> lisData) {
		String query = null;
		boolean flag = false;
		HashMap<String, String> memberLIS = new HashMap<String, String>();
		DecimalFormat df = new DecimalFormat("####0.00");
		try {
			if (!testData.get("LIS_ID").isEmpty()) {
				if (!testData.get("LISSUBSIDYSOURCE").isEmpty())
					memberLIS.put("subsidySource", testData.get("LISSUBSIDYSOURCE").trim().toUpperCase());
				if (!testData.get("LISCOPAYCODE").isEmpty())
					memberLIS.put("coPay", testData.get("LISCOPAYCODE").trim());
				if (!testData.get("LISPERCENTCODE").isEmpty())
					memberLIS.put("percent", testData.get("LISPERCENTCODE").trim());
				if (!testData.get("LISAMOUNT").isEmpty())
					memberLIS.put("LISAmt", df.format(Double.parseDouble(testData.get("LISAMOUNT").trim())));
				if (!testData.get("LISSPAPAMT").isEmpty())
					memberLIS.put("spapAmt", df.format(Double.parseDouble(testData.get("LISSPAPAMT").trim())));
				lisData.remove("lisOverride");
				lisData.remove("licBAE");
				lisData.remove("LISBAE");
				lisData.remove("memberID");
				lisData.remove("supplementalID");
				if (!lisData.get("subsidySource").trim().isEmpty())
					lisData.replace("subsidySource", lisData.get("subsidySource").trim().substring(0, 1));
				if (!lisData.get("coPay").trim().isEmpty())
					lisData.replace("coPay", lisData.get("coPay").trim().substring(0, 1));

				flag = compareHashMaps(lisData, memberLIS);

				if (flag) {
					query = "UPDATE MEMLIS SET FILLER = 'PASSED' WHERE ID = "
							+ Integer.parseInt(testData.get("LIS_ID"));
					test.log(Status.PASS, "Member LIS Details Passed: " + memberLIS);
				} else {
					query = "UPDATE MEMLIS SET FILLER = 'FAILED' WHERE ID = "
							+ Integer.parseInt(testData.get("LIS_ID"));
					test.log(Status.FAIL, "Member LIS Details Failed. \r\n Screen Data = " + lisData
							+ " \r\n Table DB Data = " + memberLIS);
				}
			}
		} catch (Exception e) {
			query = "UPDATE MEMLIS SET FILLER = 'EXCEPTION' WHERE ID = " + Integer.parseInt(testData.get("LIS_ID"));
			test.log(Status.FATAL, "Member LIS Details Exception occoured in data = " + lisData);
			e.printStackTrace();
			if (getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MemberPage.pageCannotDisplay_xpath).getText()
					.equalsIgnoreCase("The page cannot be displayed because an internal server error has occurred.")) {
				driver.quit();
			}
		} finally {
			if (!testData.get("LIS_ID").isEmpty())
				accessDb.updateDBTestData(query);
		}
	}

	private void validateDisenrollment(HashMap<String, String> record) {
		try {
			searchMember(record.get("BeneficiaryID"));
			navigateToEnrollmentTab();
			HashMap<String, String> enrollmentData = getEnrollmentDetails();
			navigateToTRRTab();
			Map<String, String> TRRData = this.getTRRData();
			boolean passing = true;

			if (!enrollmentData.get("ENROLLMENTSTATUS").equals("DAPRV")) {
				passing = false;
				test.log(Status.FAIL, "Failed - Expected: DAPRV, actual: " + enrollmentData.get("ENROLLMENTSTATUS"));
			}

			if (!TRRData.get("ReplyCode").equals(record.get("TransactionalReplyCode"))
					|| !TRRData.get("TransactionCode").equals(record.get("TransactionCode"))) {
				passing = false;
				test.log(Status.FAIL, "No record Found");
			}

			if (!enrollmentData.get("ENROLLMENTSTARTDATE").equals(TRRData.get("TransactionDate"))
					|| !getDateInYYMMDD(enrollmentData.get("ENROLLMENTSTARTDATE").replaceAll("/", ""))
							.equals(record.get("EffectiveDate"))) {
				passing = false;
				test.log(Status.FAIL, "No record Found");
			}

			if (passing)
				reportPass("Passed - Data Matched" + TRRData.toString());
			else
				reportFail("Failed - Data does not match as expected" + TRRData.toString());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void validateDisenrollment() {
		try {
			M360LoginPage log = new M360LoginPage();
			log.login();
			String query = "SELECT BeneficiaryID, EffectiveDate, TransactionalReplyCode, TransactionCode FROM dbo.trr_file_data "
					+ "WHERE ModificationSubType = '01351' or ModificationSubType = '01451' or ModificationSubType = '01551'";
			List<HashMap<String, String>> tableVal = db.getListOfHashMapsFromResultSet(query);
			System.out.println(tableVal.size());
			navigateToMemberDetails();
			for (HashMap<String, String> record : tableVal) {
				validateDisenrollment(record);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// LASTNAME, FIRSTNAME MIDDLEINITIAL
	private void validateNameChange(HashMap<String, String> memberData) {
		navigateToMemberDetails();
		searchMember(memberData.get("BeneficiaryID"));
		HashMap<String, String> demographicsData = getDemographicsDetails();

		String firstname = demographicsData.get("MEMBERFIRSTNAME");
		String middleinit = demographicsData.get("MEMBERMIDDLEINITIAL");
		String lastname = demographicsData.get("MEMBERLASTNAME");
		String nameondemographicstab = lastname.trim().toLowerCase() + ", " + firstname.trim().toLowerCase() + " "
				+ middleinit.trim().toLowerCase();

		navigateToTRRTab();
		Map<String, String> data = this.getTRRData();
		String namestr = memberData.get("UpdatedData");
		if (namestr.trim().toLowerCase().equals(data.get("After").trim().toLowerCase())
				&& namestr.trim().toLowerCase().equals(nameondemographicstab)) {
			try {
				reportPass("Passed - Name Verified");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				reportFail("Failed - Name mismatch");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void validateNameChange() {
		String query = "SELECT BeneficiaryID, UpdatedData FROM dbo.trr_file_data WHERE ModificationSubType LIKE '023%'";
		List<HashMap<String, String>> MedicareIDs = db.getListOfHashMapsFromResultSet(query);
		for (HashMap<String, String> MedicareID : MedicareIDs)
			validateNameChange(MedicareID);
	}

	private void validateBeneficiaryIDChange(String memberID) {
		navigateToMemberDetails();
		searchMember(memberID);
		HashMap<String, String> demographicsData = getDemographicsDetails();
		String medicareID = demographicsData.get("MEDICARENUMBER");
		navigateToTRRTab();
		Map<String, String> data = this.getTRRData();

		if (memberID.trim().toLowerCase().equals(data.get("After").trim().toLowerCase())
				&& memberID.trim().toLowerCase().equals(medicareID.trim().toLowerCase())) {
			try {
				reportPass("Passed - Name Verified");
			} catch (IOException e) {
				e.printStackTrace();
			}
		} else {
			try {
				reportFail("Failed - Name mismatch");
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	public void validateBeneficiaryIDChange() {
		try {
			String query = "SELECT UpdatedData FROM dbo.trr_file_data WHERE ModificationSubType LIKE '022%'";
			List<HashMap<String, String>> MedicareIDs = db.getListOfHashMapsFromResultSet(query);
			for (HashMap<String, String> MedicareID : MedicareIDs)
				validateBeneficiaryIDChange(MedicareID.get("UpdatedData"));
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void navigateToTRRTab() {
		try {
			getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.trrTab_id).click();

		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("M360MemberPage navigateToTRR tab failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public Map<String, String> getTRRData() {
		Map<String, String> data = new HashMap<String, String>();
		try {
			String transactionCode = getElement(LocatorTypes.xpath,
					M360MembershipObjRepo.M360MemberPage.transactionCode_xpath).getText();
			String replyCode = getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MemberPage.replyCode).getText();
			String txn_eff_date = getElement(LocatorTypes.xpath,
					M360MembershipObjRepo.M360MemberPage.txn_eff_date_xpath).getText();
			String changedData = getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MemberPage.changed_xpath)
					.getText();

			data.put("TransactionDate", txn_eff_date);
			data.put("TransactionCode", transactionCode);
			data.put("ReplyCode", replyCode);
			data.put("After", changedData);

		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Retrieval of TRR data failed");
			} catch (Exception e1) {
				e1.printStackTrace();
			}
		}
		return data;
	}

	public void navigateToLettersTab() {
		try {
			getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.lettersTab_id).click();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("M360MemberPage: Navigate to letters tab failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public HashMap<String, String> getLetterData() {
		HashMap<String, String> memberLetterData = new HashMap<String, String>();
		try {
			String medicareID = getElement(LocatorTypes.xpath,
					M360MembershipObjRepo.M360MemberPage.letterMedicareID_xpath).getText().trim();
			String letterName = getElement(LocatorTypes.id, M360MembershipObjRepo.M360MemberPage.letterName_id)
					.getText();
			String letterReviewTableFrame = getElement(LocatorTypes.xpath,
					M360MembershipObjRepo.M360LetterReview.letterReviewTableFrame_xpath).getText();
			String firstName = "";
			String middleName = "";
			String lastName = "";
			String planName = "";
			String memberID = "";
			String rxGroup = "";
			String rxBIN = "";
			String rxPCN = "";
			int beginIndex;

			String[] tableArray = letterReviewTableFrame.split("\\r?\\n");

			beginIndex = tableArray[2].lastIndexOf("first name");
			firstName = tableArray[2].substring(beginIndex, tableArray[2].length()).trim();
			firstName = firstName.replace("first name", "");
			firstName = firstName.trim();

			beginIndex = tableArray[3].lastIndexOf("MIDDLE NAME");
			middleName = tableArray[3].substring(beginIndex, tableArray[3].length()).trim();
			middleName = middleName.replace("MIDDLE NAME", "");
			middleName = middleName.trim();

			beginIndex = tableArray[4].lastIndexOf("last name");
			lastName = tableArray[4].substring(beginIndex, tableArray[4].length()).trim();
			lastName = lastName.replace("last name", "");
			lastName = lastName.trim();

			beginIndex = letterReviewTableFrame.lastIndexOf("PLAN NAME");
			planName = letterReviewTableFrame.substring(beginIndex, letterReviewTableFrame.length());
			planName = planName.replace("PLAN NAME", "");
			planName = planName.trim();
			String[] planNameSplit = planName.split("\\r?\\n");
			planName = planNameSplit[0];

			if (letterReviewTableFrame.contains("MEMBER ID")) {
				beginIndex = letterReviewTableFrame.lastIndexOf("MEMBER ID");
				memberID = letterReviewTableFrame.substring(beginIndex, letterReviewTableFrame.length());
				memberID = memberID.replace("MEMBER ID", "");
				memberID = memberID.trim();
				String[] memberIDSplit = memberID.split("\\r?\\n");
				memberID = memberIDSplit[0];
			} else {
				beginIndex = letterReviewTableFrame.lastIndexOf("member id");
				memberID = letterReviewTableFrame.substring(beginIndex, letterReviewTableFrame.length());
				memberID = memberID.replace("member id", "");
				memberID = memberID.trim();
				String[] memberIDSplit = memberID.split("\\r?\\n");
				memberID = memberIDSplit[0];
			}
			if (letterReviewTableFrame.contains("RXGRP")) {
				beginIndex = letterReviewTableFrame.lastIndexOf("RXGRP");
				rxGroup = letterReviewTableFrame.substring(beginIndex, letterReviewTableFrame.length());
				rxGroup = rxGroup.replace("RXGRP", "");
				rxGroup = rxGroup.trim();
				String[] rxGroupSplit = rxGroup.split("\\r?\\n");
				rxGroup = rxGroupSplit[0];
			}
			if (letterReviewTableFrame.contains("RXBIN")) {
				beginIndex = letterReviewTableFrame.lastIndexOf("RXBIN");
				rxBIN = letterReviewTableFrame.substring(beginIndex, letterReviewTableFrame.length());
				rxBIN = rxBIN.replace("RXBIN", "");
				rxBIN = rxBIN.trim();
				String[] rxBINSplit = rxBIN.split("\\r?\\n");
				rxBIN = rxBINSplit[0];
			}
			if (letterReviewTableFrame.contains("RXPCN")) {
				beginIndex = letterReviewTableFrame.lastIndexOf("RXPCN");
				rxPCN = letterReviewTableFrame.substring(beginIndex, letterReviewTableFrame.length());
				rxPCN = rxPCN.replace("RXPCN", "");
				rxPCN = rxPCN.trim();
				String[] rxPCNSplit = rxPCN.split("\\r?\\n");
				rxPCN = rxPCNSplit[0];
			}
			memberLetterData.put("LetterName", letterName);
			memberLetterData.put("FirstName", firstName);
			memberLetterData.put("MiddleInitial", middleName);
			memberLetterData.put("LastName", lastName);
			memberLetterData.put("ContractNumber", planName);
			memberLetterData.put("PartDRxID", memberID);
			memberLetterData.put("BeneficiaryID", medicareID);
			memberLetterData.put("PartDRxGroup", rxGroup);
			memberLetterData.put("PartDRxBIN", rxBIN);
			memberLetterData.put("PartDRxPCN", rxPCN);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("M360MemberPage unable to retrieve letter data from member results.");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return memberLetterData;
	}

	public int selectTableSearchResultsList(int nodeCount) {
		int listSize = 0;
		try {
			List<WebElement> letterTableList = getElements(LocatorTypes.xpath,
					M360MembershipObjRepo.M360MemberPage.letterRow_xpath);

			listSize = letterTableList.size();
			letterTableList.get(nodeCount).click();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail(
						"M360MemberPage unable to click on letter item in the table search results for Letters tab.");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return listSize;
	}

	public boolean tableItemResult(int nodeCount, String letterDesc) {
		boolean isLetterNameValid = false;
		try {
			List<WebElement> letterTableList = getElements(LocatorTypes.xpath,
					M360MembershipObjRepo.M360MemberPage.letterRow_xpath);
			if (letterTableList.get(nodeCount).getText().toUpperCase().contains(letterDesc)) {
				isLetterNameValid = true;
			} else {
				isLetterNameValid = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail(
						"M360MemberPage unable to retrieve letter item in the table search results for Letters tab.");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return isLetterNameValid;
	}

	public boolean validateLetterInfo(String medicareID, String letterDesc) {
		boolean flag = false;
		boolean isMemberAvailable = false;
		try {
			isMemberAvailable = searchMember(medicareID);
			if (isMemberAvailable) {
				navigateToLettersTab();
				int listSize = 0;
				boolean isLetterNameValid;
				boolean elementPresence = isElementPresent(LocatorTypes.id,
						M360MembershipObjRepo.M360MemberPage.letterRowFirstItem_id);
				if (elementPresence == false) {
					flag = false;
					test.log(Status.INFO, "No results found for Medicare ID " + medicareID);
				} else {
					listSize = selectTableSearchResultsList(0);
					outerloop: for (int indexCount = 0; indexCount < listSize; indexCount++) {
						isLetterNameValid = tableItemResult(indexCount, letterDesc);
						if (indexCount == listSize - 1) {
							break;
						} else {
							if (isLetterNameValid == true) {
								selectTableSearchResultsList(indexCount);
								HashMap<String, String> memValidateLetters = getLetterData();
								test.log(Status.INFO, "" + memValidateLetters);
								flag = compareHashMaps(memValidateLetters, testData);
								break outerloop;
							}
						}
					}
				}
			} else {
				flag = false;
			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			try {
				reportFail("M360MemberPage: validation process of letters failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}

	public boolean searchMemberBySupplementalID(String suppID) {
		boolean flag = true;
		try {
			getElement(LocatorTypes.name, M360MembershipObjRepo.M360MedicareSupportPage.memNewSearch_name).click();
			getElement(LocatorTypes.name, M360MembershipObjRepo.M360MedicareSupportPage.supplementalID_name)
					.sendKeys(suppID);
			getElement(LocatorTypes.id, M360MembershipObjRepo.M360MedicareSupportPage.memberSearchGoButton_id).click();
			if (getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MedicareSupportPage.memberDetails_xpath)
					.getText().trim().equalsIgnoreCase("No Data")) {
				logger.info("Member Not Found");
				return false;
			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			if (getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MemberPage.pageCannotDisplay_xpath).getText()
					.equalsIgnoreCase("The page cannot be displayed because an internal server error has occurred.")
					|| getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MemberPage.unExpectedResponse).getText()
							.equals("Unexpected Response")
					|| getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MemberPage.unExpectedResponse).getText()
							.contains("The following session is not valid")) {
				driver.quit();
			}
			try {
				reportFail("M36MemberPage searchMemberBySupplementalID failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}

	public String getMedicareID() {
		String medicareID = "";
		try {
			boolean isMedicareIDPresent = isElementPresent(LocatorTypes.xpath,
					M360MembershipObjRepo.M360MemberPage.medicareID_xpath);
			if (isMedicareIDPresent) {
				medicareID = getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360MemberPage.medicareID_xpath)
						.getText().trim();
			} else {
				medicareID = "";
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Retrieval of MedicareID failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return medicareID;
	}
}
