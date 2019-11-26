package pageclasses.healthX;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.aventstack.extentreports.Status;

import enums.LocatorTypes;
import managers.PageManager;
import pageclasses.BasePage;
import pageobjects.HealthXObjRepo;
import utils.Dbconn;

public class HealthXMemberPage extends BasePage {

	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(HealthXMemberPage.class.getName());
	PageManager pm = new PageManager();
	Dbconn db = new Dbconn();

	public HealthXMemberPage() {
		driver = getWebDriver();
	}
	
	public boolean createMemberAccountNavigation() {
		boolean memberIDFieldExists = false;
		boolean flag = false;
		boolean errorMessageExists = false;
		String errorMessage;
		try {
			getElement(LocatorTypes.xpath, HealthXObjRepo.HealthXMemberPortalPage.memberCreateAnAccount_xpath).click();
			elementVisibleWait(LocatorTypes.id, HealthXObjRepo.HealthXMemberPortalPage.memberlicenseAgreementAcceptCheckBox_id, 10);
			getElement(LocatorTypes.id, HealthXObjRepo.HealthXMemberPortalPage.memberlicenseAgreementAcceptCheckBox_id)
					.click();
			wait(1);
			getElement(LocatorTypes.id, HealthXObjRepo.HealthXRegistrationInfo.registrationInfoNextButton_id).click();
			
			memberIDFieldExists = isElementPresent(LocatorTypes.id,
					HealthXObjRepo.HealthXRegistrationInfo.memberRegistrationInfoMemberID_id);
			if (memberIDFieldExists == false) {
				flag = false;
				errorMessageExists = isElementPresent(LocatorTypes.className,
						HealthXObjRepo.HealthXRegistrationInfo.providerRegistrationInfoErrorMessage_className);
				if(errorMessageExists) {
					errorMessage = getElement(LocatorTypes.className, HealthXObjRepo.HealthXRegistrationInfo.providerRegistrationInfoErrorMessage_className).getText();
					reportFail("Error Message in the UI: " + errorMessage);
				}
				reportFail("Navigation to Create Member Account page failed");
			} else {
				flag = true;
			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			try {
				reportFail("createMemberAccountNavigation method resulted in an exception!");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}

	public boolean createMemberAccountDetails() {
		boolean flag = false;
		boolean userNameFieldExists = false;
		try {
			String memberID = testData.get("MemberID");
			String firstName = testData.get("FirstName");
			String lastName = testData.get("LastName");
			String dateOfBirth = testData.get("BirthDate");
			
			if(memberID.startsWith("M2K")) {
				memberID = memberID.replace("M2K", "");
			}
			
			elementVisibleWait(LocatorTypes.id, HealthXObjRepo.HealthXRegistrationInfo.memberRegistrationInfoMemberID_id, 10);
			getElement(LocatorTypes.id, HealthXObjRepo.HealthXRegistrationInfo.memberRegistrationInfoMemberID_id)
					.sendKeys(memberID);
			getElement(LocatorTypes.id, HealthXObjRepo.HealthXRegistrationInfo.registrationInfoFirstName_id)
					.sendKeys(firstName);
			getElement(LocatorTypes.id, HealthXObjRepo.HealthXRegistrationInfo.registrationInfoLastName_id)
					.sendKeys(lastName);
			getElement(LocatorTypes.id, HealthXObjRepo.HealthXRegistrationInfo.memberRegistrationInfoDateOfBirth_id)
					.sendKeys(getDateInMMDDYYYY(dateOfBirth));
			getElement(LocatorTypes.id, HealthXObjRepo.HealthXRegistrationInfo.registrationInfoNextButton_id).click();
			
			userNameFieldExists = isElementPresent(LocatorTypes.id,
					HealthXObjRepo.HealthXRegistrationInfo.registrationInfoUserName_id);
			if (userNameFieldExists == false) {
				flag = false;
				reportFail("Navigation to Create Login Account page failed");
			} else {
				flag = true;
			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			try {
				reportFail("createMemberAccountDetails method resulted in an exception!");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}
	public void navigateToBenefitsTab() {
		try {
			Actions action = new Actions(driver);
			action.moveToElement(getElement(LocatorTypes.xpath,
					HealthXObjRepo.HealthXPortalMemberPortal_UserManagerPage.coverageAndBenefits_xpath)).build().perform();
			getElement(LocatorTypes.xpath, HealthXObjRepo.HealthXPortalMemberPortal_UserManagerPage.medicalTab_xpath).click();
			logger.info("Medical coverage and benefits page open");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("failure to open Medical coverage and benefits page");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public void navigateToClaimsTab() {
		try {
			getElement(LocatorTypes.xpath, HealthXObjRepo.HealthXPortalMemberPortal_UserManagerPage.claimsTab_xpath).click();
			boolean claimResultsExists = isElementPresent(LocatorTypes.id, HealthXObjRepo.HealthXPortalMemberPortal_UserManagerPage.claimsResults_id);
			if(!claimResultsExists) {
				reportFail("Failure to open claim search page!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Exception in navigateToClaimsTab method!");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public HashMap<String, String> getMemberInformationDetails() {
		HashMap<String, String> memberInfo = new HashMap<String, String>();
		try {
			elementVisibleWait(LocatorTypes.xpath, HealthXObjRepo.HealthXPortalMemberPortal_UserManagerPage.memberName_xpath, 10);
			String memberName = getElement(LocatorTypes.xpath,
					HealthXObjRepo.HealthXPortalMemberPortal_UserManagerPage.memberName_xpath).getText().trim();
			String planName = getElement(LocatorTypes.xpath,
					HealthXObjRepo.HealthXPortalMemberPortal_UserManagerPage.planName_xpath).getText().trim();
			String memberId = getElement(LocatorTypes.xpath,
					HealthXObjRepo.HealthXPortalMemberPortal_UserManagerPage.memberId_xpath).getText().trim();
			String RxBIN = getElement(LocatorTypes.xpath,
					HealthXObjRepo.HealthXPortalMemberPortal_UserManagerPage.RxBIN_xpath).getText().trim();
			String RxGRP = getElement(LocatorTypes.xpath,
					HealthXObjRepo.HealthXPortalMemberPortal_UserManagerPage.RxGRP_xpath).getText().trim();
			String RxPCN = getElement(LocatorTypes.xpath,
					HealthXObjRepo.HealthXPortalMemberPortal_UserManagerPage.RxPCN_xpath).getText().trim();

			memberInfo.put("MEMBERNAME", memberName);
			memberInfo.put("Plan", planName);
			memberInfo.put("MemberID", memberId);
			memberInfo.put("RxBIN", RxBIN);
			memberInfo.put("RxGRP", RxGRP);
			memberInfo.put("RxPCN", RxPCN);

			logger.info(memberInfo);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("getting Member information details - failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return memberInfo;
	}
	
	public boolean validateMemberInfoDetails() {
		boolean flag = false;
		String fullName;
		String plan;
		int count = 0;
		try {
			HashMap<String, String> memberInfo = getMemberInformationDetails();
			HashMap<String, String> testDataMemberInfoDetails = new HashMap<String, String>();
		
			fullName = testData.get("FirstName") + " " + testData.get("LastName");
			String planName = testData.get("PlanName");
			String productPBPID = testData.get("ProductPBPID");
			String memberID = testData.get("MemberID");;
			
			/*if planName has one type of format, use that format, else, use the different format
			 * Examples: 	2020 Blue Medicare Advantage Classic (008) - Pima - Blue Medicare Advantage Classic
			 * 				2020 BluePathway Plan 2 (003) - Maricopa - BluePathway
			 * 				2020 Blue Medicare Advantage Standard (009) - Pima - Blue Medicare Advantage Standard
			 * 				2020 Blue Medicare Advantage Classic (006) - Maric - Blue Medicare Advantage Classic
			 * 				001 - Blue Medicare Advantage Plus (HMO)
			 * */
			if(planName == null) {
				planName = "";
			}
			if(productPBPID == null) {
				productPBPID = "";
			}
			if(memberID.startsWith("M2K")) {
				memberID = memberID.replace("M2K", "");
			}
			
			plan = planName;
			
			if(memberInfo.get("Plan").contains("2020")) {
				plan = plan.replace("(HMO)", "");
				plan = plan.replace("(PPO)", "");
				plan = plan.trim();
			}
			if(!memberInfo.get("Plan").contains(plan)) {
				flag = false;
				count++;
			}
			//plan = "2020 " + planName + "(" + productPBPID + ")" + " - " + [COUNTY NAME] + " - " + planName;
			testDataMemberInfoDetails.put("MEMBERNAME", fullName.toUpperCase().trim());
			testDataMemberInfoDetails.put("MemberID", memberID.trim());
			//testDataMemberInfoDetails.put("Plan", plan.trim());
			testDataMemberInfoDetails.put("RxBIN", testData.get("PrimaryBin").trim());
			testDataMemberInfoDetails.put("RxGRP", testData.get("PrimaryRxGroup").trim());
			testDataMemberInfoDetails.put("RxPCN", testData.get("PrimaryPCN").trim());
			memberInfo.remove("Plan");
			flag = compareHashMaps(memberInfo, testDataMemberInfoDetails);
			
			if(flag == true && count == 0) {
				flag = true;
				reportPass("Validation for member information in HealthX Member is successful for member ID: " + testData.get("MemberID"));
			} else {
				flag = false;
				reportFail("Validation for member information in HealthX Member has failed for member ID: " + testData.get("MemberID"));
			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			try {
				reportFail("validateSubscriberDetails - failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}
	
	public HashMap<String, String> getPCPInformationDetails() {
		HashMap<String, String> pcpInfo = new HashMap<String, String>();
		try {
			String pcpName = getElement(LocatorTypes.xpath,
					HealthXObjRepo.HealthXPortalMemberPortal_UserManagerPage.pcpName_xpath).getText().trim();
			pcpInfo.put("PCPDoctorName", pcpName);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("getting PCP information details - failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return pcpInfo;
	}
	
	public boolean validatePCPInformation() {
		boolean flag = false;
		try {
			HashMap<String, String> pcpInfo = getPCPInformationDetails();
			flag = compareHashMaps(pcpInfo, testData);
			
			if(flag) {
				reportPass("Validation for PCP information in HealthX Member is successful for member ID: " + testData.get("MemberID"));
			} else {
				reportFail("Validation for PCP information in HealthX Member has failed for member ID: " + testData.get("MemberID"));
			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			try {
				reportFail("validatePCPInformation failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}
	
	public HashMap<String, String> getCoPaymentInformationDetails() {
		HashMap<String, String> coPayInfo = new HashMap<String, String>();
		try {
				String pcpOfficeVisit = getElement(LocatorTypes.xpath, HealthXObjRepo.HealthXPortalMemberPortal_UserManagerPage.pcpOfficeVisit_xpath).getText().trim();
				String specialistOfficeVisit = getElement(LocatorTypes.xpath, HealthXObjRepo.HealthXPortalMemberPortal_UserManagerPage.speacialistOfficeVisit_xpath).getText().trim();
				String emergencyRoom = getElement(LocatorTypes.xpath, HealthXObjRepo.HealthXPortalMemberPortal_UserManagerPage.emergencyRoom_xpath).getText().trim();
				String mentalHealthOfficeVisit = getElement(LocatorTypes.xpath, HealthXObjRepo.HealthXPortalMemberPortal_UserManagerPage.mentalHealthOfficeVisit_xpath).getText().trim();
				String physicalTherapy = getElement(LocatorTypes.xpath, HealthXObjRepo.HealthXPortalMemberPortal_UserManagerPage.physicalTherapy_xpath).getText().trim();
				String laboratoryServices = getElement(LocatorTypes.xpath, HealthXObjRepo.HealthXPortalMemberPortal_UserManagerPage.laboratoryServices_xpath).getText().trim();
				String xRay = getElement(LocatorTypes.xpath, HealthXObjRepo.HealthXPortalMemberPortal_UserManagerPage.xray_xpath).getText().trim();
				String CTMRIMRASPECT = getElement(LocatorTypes.xpath, HealthXObjRepo.HealthXPortalMemberPortal_UserManagerPage.ctMriMra_xpath).getText().trim();
				String inpatientHospitalCoverage = getElement(LocatorTypes.xpath, HealthXObjRepo.HealthXPortalMemberPortal_UserManagerPage.inpatientHospitalCoverage_xpath).getText().trim();
				String outPatientServicesHospital = getElement(LocatorTypes.xpath, HealthXObjRepo.HealthXPortalMemberPortal_UserManagerPage.outPatientServicesHospital_xpath).getText().trim();
				String outpatientServicesAmbulatorySurgeryCenter = getElement(LocatorTypes.xpath, HealthXObjRepo.HealthXPortalMemberPortal_UserManagerPage.outPatientServicesAmbulatorySurgeryCenter_xpath).getText();
				
				pcpOfficeVisit = pcpOfficeVisit.replace("$", "");
				specialistOfficeVisit = specialistOfficeVisit.replace("$", "");
				emergencyRoom = emergencyRoom.replace("$", "");
				mentalHealthOfficeVisit = mentalHealthOfficeVisit.replace("$", "");
				physicalTherapy = physicalTherapy.replace("$", "");
				laboratoryServices = laboratoryServices.replace("$", "");
				xRay = xRay.replace("$", "");
				CTMRIMRASPECT = CTMRIMRASPECT.replace("$", "");
				inpatientHospitalCoverage = inpatientHospitalCoverage.replace("$", "");
				outPatientServicesHospital = outPatientServicesHospital.replace("$", "");
				outpatientServicesAmbulatorySurgeryCenter = outpatientServicesAmbulatorySurgeryCenter.replace("$", "");
				
				coPayInfo.put("PCPOfficeVisit", pcpOfficeVisit);
				coPayInfo.put("SpecialistOfficeVisit", specialistOfficeVisit);
				coPayInfo.put("EmergencyRoom", emergencyRoom);
				coPayInfo.put("MentalHealthOfficeVisit", mentalHealthOfficeVisit);
				coPayInfo.put("PhysicalTherapy", physicalTherapy);
				coPayInfo.put("LaboratoryServices", laboratoryServices);
				coPayInfo.put("XRay", xRay);
				coPayInfo.put("CT_MRI_MRA_SPECT", CTMRIMRASPECT);
				coPayInfo.put("InpatientHospitalCoverage", inpatientHospitalCoverage);
				coPayInfo.put("OutpatientServicesHospital", outPatientServicesHospital);
				coPayInfo.put("OutpatientServicesASC", outpatientServicesAmbulatorySurgeryCenter);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("getting PCP information details - failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return coPayInfo;
	}
	
	public boolean validateCoPayInformation() {
		boolean flag = false;
		try {
			HashMap<String, String> coPayInfo = getCoPaymentInformationDetails();
			flag = compareHashMaps(coPayInfo, testData);
			if(flag) {
				reportPass("Validation for CoPay information in HealthX Member is successful for member ID: " + testData.get("MemberID"));
			} else {
				reportFail("Validation for CoPay information in HealthX Member has failed for member ID: " + testData.get("MemberID"));
			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			try {
				reportFail("validatePCPInformation failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}
	
	public HashMap<String, String> getMaxOutOfPocketInformation(){
		HashMap<String, String> moopInfo = new HashMap<String, String>();
		try {
			String moopLimitMaxAmount = getElement(LocatorTypes.xpath, HealthXObjRepo.HealthXPortalMemberPortal_UserManagerPage.moopLimitMaximumAmount_xpath).getText();
			moopLimitMaxAmount = moopLimitMaxAmount.replace("$", "");
			moopLimitMaxAmount = moopLimitMaxAmount.replace(",", "");
			moopInfo.put("MaximumOutOfPocket", moopLimitMaxAmount);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("getMaxOutOfPocketInformation failed!");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return moopInfo;
	}
	
	public boolean validateMaximumOutOfPocketInformation() {
		boolean flag = false;
		try {
			HashMap<String, String> moopInfo = getMaxOutOfPocketInformation();
			flag = compareHashMaps(moopInfo, testData);
			if(flag) {
				reportPass("Validation for Maximum Out of Pocket information in HealthX Member is successful for member ID: " + testData.get("MemberID"));
			} else {
				reportFail("Validation for Maximum Out of Pocket information in HealthX Member has failed for member ID: " + testData.get("MemberID"));
			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			try {
				reportFail("validateMaximumOutOfPocketInformation failed!");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}
	
	
	public int getClaimsCount(){
		int numberOfResults = 0;
		try {
			String numberOfClaimResultsText = getElement(LocatorTypes.xpath, 
					HealthXObjRepo.HealthXPortalMemberPortal_UserManagerPage.claimsNumberOfResultsFound_xpath).getText();
			if(numberOfClaimResultsText.contains("No claims have been found.")) {
				numberOfResults = 0;
			} else {
				numberOfClaimResultsText = getElement(LocatorTypes.className, 
						HealthXObjRepo.HealthXPortalMemberPortal_UserManagerPage.claimsCount_className).getText();
				numberOfResults = Integer.parseInt(numberOfClaimResultsText);
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Exception in getClaimsCount method!");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return numberOfResults;
	}
	
	public HashMap<String, String> getClaimsInformation(){
		HashMap<String, String> claimsInformation = new HashMap<String, String>();
		
		String claimNumber;
		String memberName;
		String memberID;
		String planName;
		String billedAmount;
		String approvedAmount;
		String planShare;
		String dateOfService;
		String serviceProvider;
		String memberResponsibility;
		try {
			claimNumber = getElement(LocatorTypes.xpath, 
					HealthXObjRepo.HealthXPortalMemberPortal_UserManagerPage.claimNumberText_xpath).getText();
			claimNumber = claimNumber.replace("Claim #", "");
			memberName = getElement(LocatorTypes.xpath, 
					HealthXObjRepo.HealthXPortalMemberPortal_UserManagerPage.claimMemberName_xpath).getText();
			memberID = getElement(LocatorTypes.xpath, 
					HealthXObjRepo.HealthXPortalMemberPortal_UserManagerPage.claimMemberID_xpath).getText();
			planName = getElement(LocatorTypes.xpath, 
					HealthXObjRepo.HealthXPortalMemberPortal_UserManagerPage.claimPlanName_xpath).getText();
			billedAmount = getElement(LocatorTypes.xpath, 
					HealthXObjRepo.HealthXPortalMemberPortal_UserManagerPage.claimBilledAmount_xpath).getText();
			billedAmount = billedAmount.replace("$", "");
			billedAmount = billedAmount.replace(",", "");
			approvedAmount = getElement(LocatorTypes.xpath, 
					HealthXObjRepo.HealthXPortalMemberPortal_UserManagerPage.claimApprovedAmount_xpath).getText();
			approvedAmount = approvedAmount.replace("$", "");
			approvedAmount = approvedAmount.replace(",", "");
			planShare = getElement(LocatorTypes.xpath, 
					HealthXObjRepo.HealthXPortalMemberPortal_UserManagerPage.claimPlanShare_xpath).getText();
			planShare = planShare.replace("$", "");
			planShare = planShare.replace(",", "");
			dateOfService = getElement(LocatorTypes.xpath, 
					HealthXObjRepo.HealthXPortalMemberPortal_UserManagerPage.claimDateOfService_xpath).getText();
			serviceProvider = getElement(LocatorTypes.xpath, 
					HealthXObjRepo.HealthXPortalMemberPortal_UserManagerPage.claimServiceProvider_xpath).getText();
			memberResponsibility = getElement(LocatorTypes.xpath, 
					HealthXObjRepo.HealthXPortalMemberPortal_UserManagerPage.claimMemberResponsibility_xpath).getText();
			memberResponsibility = memberResponsibility.replace("$", "");
			memberResponsibility = memberResponsibility.replace(",", "");
			
			claimsInformation.put("ClaimNumber", claimNumber);
			claimsInformation.put("MemberName", memberName);
			claimsInformation.put("MemberID", memberID);
			claimsInformation.put("PlanName", planName);
			claimsInformation.put("BilledAmount", billedAmount);
			claimsInformation.put("ApprovedAmount", approvedAmount);
			claimsInformation.put("PlanShare", planShare);
			claimsInformation.put("DateOfService", dateOfService);
			claimsInformation.put("ServiceProvider", serviceProvider);
			claimsInformation.put("MemberResponsibility", memberResponsibility);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Exception in getClaimsInformation method!");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return claimsInformation;
	}
	
	public HashMap<String, String> getClaimDetailsTableInfo(int row) {
		HashMap<String,String> claimDetailsTableInfo = new HashMap<String,String>();
		String memberID;
		String claimDetailsCptCode;
		//String claimDetailsMod1;
		//String claimDetailsMod2;
		String claimDetailsBilledAmount;
		String claimDetailsApprovedAmount;
		String claimDetailsPlanShare;
		//String claimDetailsCoPay;
		try{
			memberID = getElement(LocatorTypes.xpath, HealthXObjRepo.HealthXPortalMemberPortal_UserManagerPage.claimMemberID_xpath).getText();
			claimDetailsCptCode = getElement(LocatorTypes.xpath, 
					HealthXObjRepo.HealthXPortalMemberPortal_UserManagerPage.claimDetailsList_xpath + "[" + row + "]/td[2]").getText();
			/*claimDetailsMod1 = getElement(LocatorTypes.xpath,
					HealthXObjRepo.ClaimsAndPayments.claimsAndPaymentsProvider_claimDetailRow_xpath + "[" + row
							+ "]/td[3]").getText();
			claimDetailsMod2 = getElement(LocatorTypes.xpath,
					HealthXObjRepo.ClaimsAndPayments.claimsAndPaymentsProvider_claimDetailRow_xpath + "[" + row
							+ "]/td[4]").getText();*/
			claimDetailsBilledAmount = getElement(LocatorTypes.xpath, 
					HealthXObjRepo.HealthXPortalMemberPortal_UserManagerPage.claimDetailsList_xpath + "[" + row + "]/td[3]").getText();
			claimDetailsBilledAmount = claimDetailsBilledAmount.replace("$", "");
			claimDetailsBilledAmount = claimDetailsBilledAmount.replace(",", "");
			claimDetailsApprovedAmount = getElement(LocatorTypes.xpath, 
					HealthXObjRepo.HealthXPortalMemberPortal_UserManagerPage.claimDetailsList_xpath + "[" + row + "]/td[4]").getText();
			claimDetailsApprovedAmount = claimDetailsApprovedAmount.replace("$", "");
			claimDetailsApprovedAmount = claimDetailsApprovedAmount.replace(",", "");
			claimDetailsPlanShare = getElement(LocatorTypes.xpath, 
					HealthXObjRepo.HealthXPortalMemberPortal_UserManagerPage.claimDetailsList_xpath + "[" + row + "]/td[5]").getText();
			claimDetailsPlanShare = claimDetailsPlanShare.replace("$", "");
			claimDetailsPlanShare = claimDetailsPlanShare.replace(",", "");
			
			claimDetailsTableInfo.put("MemberID", memberID);
			claimDetailsTableInfo.put("CPTCodeID", claimDetailsCptCode);
			//claimDetailsTableInfo.put("Mod1", claimDetailsMod1);
			//claimDetailsTableInfo.put("Mod2", claimDetailsMod2);
			claimDetailsTableInfo.put("BilledAmount", claimDetailsBilledAmount);
			claimDetailsTableInfo.put("ApprovedAmount", claimDetailsApprovedAmount);
			claimDetailsTableInfo.put("PlanShare", claimDetailsPlanShare);
			//claimDetailsTableInfo.put("CoPay", claimDetailsCoPay);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Exception in getClaimDetailsTableInfo method!");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return claimDetailsTableInfo;
	}
	
	public double getClaimDetailBilledAmount(int row) {
		double billedAmount = 0;
		String claimDetailsCptCode;
		HashMap<String, String> temptestData = new HashMap<String, String>();
		try {
			claimDetailsCptCode = getElement(LocatorTypes.xpath, 
					HealthXObjRepo.HealthXPortalMemberPortal_UserManagerPage.claimDetailsList_xpath + "[" + row + "]/td[2]").getText();
			
			String query = "SELECT TOP 1 cl.Amount\r\n" + 
					" FROM [dbo].[test_data_claimline] cl\r\n" + 
					" INNER JOIN [dbo].[test_data_readytoclaims] rtc\r\n" + 
					" ON SUBSTRING(cl.ClaimLineNumber, 1, (LEN(cl.ClaimLineNumber) - 1)) = rtc.ClaimNumber\r\n" + 
					" WHERE cl.CPTCodeID LIKE '" + claimDetailsCptCode + "%'";
			List<HashMap<String, String>> list = db.getListOfHashMapsFromResultSet(query);
			if(list.size() > 0) {
				for (HashMap<String, String> record : list) {
					for (String key : record.keySet()) {
						temptestData.put(key, record.get(key));
					}
				}
				if(temptestData.get("Amount") == null || temptestData.get("Amount").equals("")) {
					billedAmount = 0;
				} else {
					billedAmount = Double.parseDouble(temptestData.get("Amount"));
				}
			} else {
				billedAmount = 0;
			}
		} catch (Exception e) {
			billedAmount = 0;
			e.printStackTrace();
			try {
				reportFail("Exception in getClaimDetailBilledAmount method!");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return billedAmount;
	}
	
	public double getClaimDetailPlanShare(int row) {
		double planShare = 0;
		String claimDetailsCptCode;
		HashMap<String, String> temptestData = new HashMap<String, String>();
		try {
			claimDetailsCptCode = getElement(LocatorTypes.xpath, 
					HealthXObjRepo.HealthXPortalMemberPortal_UserManagerPage.claimDetailsList_xpath + "[" + row + "]/td[2]").getText();
			
			String query = "SELECT TOP 1 rtc.ClaimPaymentAmount\r\n" + 
					" FROM [dbo].[test_data_claimline] cl\r\n" + 
					" INNER JOIN [dbo].[test_data_readytoclaims] rtc\r\n" + 
					" ON SUBSTRING(cl.ClaimLineNumber, 1, (LEN(cl.ClaimLineNumber) - 1)) = rtc.ClaimNumber\r\n" + 
					" WHERE cl.CPTCodeID LIKE '" + claimDetailsCptCode + "%'";
			List<HashMap<String, String>> list = db.getListOfHashMapsFromResultSet(query);
			if(list.size() > 0) {
				for (HashMap<String, String> record : list) {
					for (String key : record.keySet()) {
						temptestData.put(key, record.get(key));
					}
				}
				if(temptestData.get("Amount") == null || temptestData.get("Amount").equals("")) {
					planShare = 0;
				} else {
					planShare = Double.parseDouble(temptestData.get("Amount"));
				}
			} else {
				planShare = 0;
			}
		} catch (Exception e) {
			planShare = 0;
			e.printStackTrace();
			try {
				reportFail("Exception in getClaimDetailPlanShare method!");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return planShare;
	}
	
	public double getClaimDetailCoPay(int row) {
		double coPay = 0;
		String claimDetailsCptCode;
		HashMap<String, String> temptestData = new HashMap<String, String>();
		try {
			claimDetailsCptCode = getElement(LocatorTypes.xpath, 
					HealthXObjRepo.HealthXPortalMemberPortal_UserManagerPage.claimDetailsList_xpath + "[" + row + "]/td[2]").getText();
			
			String query = "SELECT TOP 1 rtc.PatientResponsibilityAmount\r\n" + 
					" FROM [dbo].[test_data_claimline] cl\r\n" + 
					" INNER JOIN [dbo].[test_data_readytoclaims] rtc\r\n" + 
					" ON SUBSTRING(cl.ClaimLineNumber, 1, (LEN(cl.ClaimLineNumber) - 1)) = rtc.ClaimNumber\r\n" + 
					" WHERE cl.CPTCodeID LIKE '" + claimDetailsCptCode + "%'";
			List<HashMap<String, String>> list = db.getListOfHashMapsFromResultSet(query);
			if(list.size() > 0) {
				for (HashMap<String, String> record : list) {
					for (String key : record.keySet()) {
						temptestData.put(key, record.get(key));
					}
				}
				if(temptestData.get("Amount") == null || temptestData.get("Amount").equals("")) {
					coPay = 0;
				} else {
					coPay = Double.parseDouble(temptestData.get("Amount"));
				}
			} else {
				coPay = 0;
			}
		} catch (Exception e) {
			coPay = 0;
			e.printStackTrace();
			try {
				reportFail("Exception in getClaimDetailCoPay method!");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return coPay;
	}
	
	public boolean processClaimsInformation() {
		HashMap<String, String> memberClaimsInfo = new HashMap<String, String>();
		boolean flag = false;
		int falseValidationCounter = 0;
		int numberOfClaims = 0;
		int claimLinkIndex = 1;
		try {
			numberOfClaims = getClaimsCount();
			if(numberOfClaims == 0) {
				test.log(Status.WARNING, "No claims have been found for in the UI for Member ID: " + testData.get("MemberID"));
				flag = false;
				// Need to come back to the flag. There are cases where we might not supposed to retrieve any claims for a member
			} else {
				for(int index = 1; index <= numberOfClaims; index++) {
					getElement(LocatorTypes.xpath,
							HealthXObjRepo.HealthXPortalMemberPortal_UserManagerPage.claimsRows_xpath + "["
									+ claimLinkIndex + "]/td[1]/span/a").click();
					memberClaimsInfo = getClaimsInformation();
					flag = validateClaimsInformation(memberClaimsInfo);
					if(flag == false) {
						falseValidationCounter++;
					}
					getElement(LocatorTypes.xpath, HealthXObjRepo.HealthXPortalMemberPortal_UserManagerPage.claimsTab_xpath).click();
					elementVisibleWait(LocatorTypes.id, HealthXObjRepo.HealthXPortalMemberPortal_UserManagerPage.claimsResults_id, 10);
					claimLinkIndex++;
				}
			}
			if(falseValidationCounter == 0 && flag == true) {
				flag = true;
				reportPass("Validation for Claims information in HealthX Member is successful for member ID: " + testData.get("MemberID"));
			} else {
				flag = false;
				reportFail("Validation for Claims information in HealthX Member has failed for member ID: " + testData.get("MemberID"));
			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			try {
				reportFail("Exception in processClaimsInformation method!");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}
	
	public boolean validateClaimsInformation(HashMap<String,String> memberClaimsInfo) {
		boolean flag = false;
		HashMap<String, String> testDataClaimsInfo = new HashMap<String, String>();
		HashMap<String, String> temptestData = new HashMap<String, String>();
		HashMap<String,String> uiClaimDetailsInfo = new HashMap<String, String>();
		String memberClaimQuery;
		String fullName;
		String lastName;
		String firstName = "";
		String providerNameSplit[];
		String firstNameSplit[];
		String providerName = "";
		String fullPlanName;
		String pbpID;
		String planName;
		int count = 0;
		double totalBilledAmount = 0;
		double totalApprovedAmount = 0;
		double totalPlanShare = 0;
		double totalMemberResponsibility = 0;
		try {
			if(testData.get("ProductPBPID") != null || !testData.get("ProductPBPID").equals("")) {
				pbpID = testData.get("ProductPBPID");
			} else {
				pbpID = "";
			}
			if(testData.get("PlanName") != null || !testData.get("PlanName").equals("")) {
				planName = testData.get("PlanName");
			} else {
				planName = "";
			}
			fullPlanName = pbpID + " - " + planName;
			
			if(testData.get("MiddleInitial") == null || testData.get("MiddleInitial").equals("")){
				fullName = testData.get("FirstName") + " " + testData.get("LastName");
			} else {
				fullName = testData.get("FirstName") + " " 
						+ testData.get("MiddleInitial") + " " 
						+ testData.get("LastName");
			}
			
			lastName = memberClaimsInfo.get("ServiceProvider");
			if(lastName.contains(",")) {
				providerNameSplit = lastName.split(",");
				/*There might be issues with last names and first names with more than one name*/
				lastName = providerNameSplit[0];
				lastName = lastName.replace(",", "");
				firstName = providerNameSplit[1].trim();
				firstNameSplit = firstName.split(" ");
				firstName = firstNameSplit[0];
			}
			
			/*Query for INDIVIDUAL provider*/
			memberClaimQuery = "SELECT TOP 1 bpao.[CATEGORY], bpao.[LAST NAME], bpao.[FIRST NAME], \r\n"
					+ "bpao.[MID], bpao.[SUFFIX], bpao.[GROUP NAME]\r\n"
					+ "FROM [dbo].[BCBSAZProv_ActiveOnly] bpao\r\n"
					+ "WHERE bpao.[LAST NAME] = '" + lastName + "'\r\n"
					+ "AND bpao.[FIRST NAME] LIKE '" + firstName + "%'";

			List<HashMap<String, String>> list = db.getListOfHashMapsFromResultSet(memberClaimQuery);
			if(list.isEmpty()) {
				/*Query for NON-INDIVIDUAL provider*/
				memberClaimQuery = "SELECT TOP 1 bpao.[CATEGORY], bpao.[LAST NAME], bpao.[FIRST NAME], \r\n"
						+ "bpao.[MID], bpao.[SUFFIX], bpao.[GROUP NAME]\r\n"
						+ "FROM [dbo].[BCBSAZProv_ActiveOnly] bpao\r\n"
						+ "WHERE bpao.[LAST NAME] LIKE '" + lastName.trim() + "%';";
				list = db.getListOfHashMapsFromResultSet(memberClaimQuery);
			}
			
			for (HashMap<String, String> row : list) {
				for (String key : row.keySet()) {
					temptestData.put(key, row.get(key));
				}
				if(!temptestData.get("CATEGORY").equals("Individual")) {
					providerName = temptestData.get("LAST NAME");
				} else {
					if(temptestData.get("MID") == null || temptestData.get("MID").equals("")) {
						providerName = temptestData.get("LAST NAME") + ", " + temptestData.get("FIRST NAME");
					} else {
						providerName = temptestData.get("LAST NAME") + ", " + temptestData.get("FIRST NAME") + " " + temptestData.get("MID");
					}
				}
			}
			
			List<WebElement> claimDetailsList = getElements(LocatorTypes.xpath, 
					HealthXObjRepo.HealthXPortalMemberPortal_UserManagerPage.claimDetailsList_xpath);
			for(int row = 1; row <= claimDetailsList.size(); row++) {
				uiClaimDetailsInfo = getClaimDetailsTableInfo(row);
				totalBilledAmount = getClaimDetailBilledAmount(row);
				totalPlanShare = getClaimDetailPlanShare(row);
				totalMemberResponsibility = getClaimDetailCoPay(row);
				flag = validateClaimsDetailsTableInfo(uiClaimDetailsInfo);
				if(flag == false) {
					count++;
				}
			}
			
			totalApprovedAmount = totalBilledAmount - totalMemberResponsibility;
			
			testDataClaimsInfo.put("ClaimNumber", "");
			testDataClaimsInfo.put("MemberName", fullName);
			testDataClaimsInfo.put("MemberID", testData.get("MemberID"));
			testDataClaimsInfo.put("PlanName", fullPlanName);
			testDataClaimsInfo.put("BilledAmount", Double.toString(totalBilledAmount));
			testDataClaimsInfo.put("ApprovedAmount", Double.toString(totalApprovedAmount));
			testDataClaimsInfo.put("PlanShare", Double.toString(totalPlanShare));
			testDataClaimsInfo.put("DateOfService", "");
			testDataClaimsInfo.put("ServiceProvider", providerName);
			testDataClaimsInfo.put("MemberResponsibility", Double.toString(totalMemberResponsibility));
			flag = compareHashMaps(memberClaimsInfo, testDataClaimsInfo);
			
			if(count > 0 || flag == false) {
				flag = false;
			} else {
				flag = true;
			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			try {
				reportFail("Exception in validateClaimsInformation method!");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}
	
	public boolean validateClaimsDetailsTableInfo(HashMap<String,String> claimsDetailsTableInfo){
		boolean flag = false;
		HashMap<String, String> testDataClaimDetails = new HashMap<String,String>();
		String cptCodeID = "";
		String[] cptCodeIDSplit;
		String mod1 = "";
		String mod2 = "";
		String claimsDetailsQuery;
		try {
			claimsDetailsQuery = "SELECT TOP 1 md.SupplementalID, \r\n"
					+ "cl.CPTCodeID, cl.Amount, cl.DateTimeFromPeriod,\r\n"
					+ "rtc.ClaimNumber, rtc.BillingProviderNPI, \r\n"
					+ "rtc.Validation835ClaimStatus, rtc.ClaimPaymentAmount, rtc.PatientResponsibilityAmount\r\n"
					+ "FROM [dbo].[member_demographic] md\r\n"
					+ "INNER JOIN [dbo].[test_data_readytoclaims] rtc \r\n"
					+ "ON md.MedicareID = rtc.MedicareID\r\n"
					+ "INNER JOIN [dbo].[test_data_claimline] cl\r\n"
					+ "ON SUBSTRING(cl.ClaimLineNumber, 1, (LEN(cl.ClaimLineNumber) - 1)) = rtc.ClaimNumber \r\n"
					+ "WHERE md.SupplementalID = '" + claimsDetailsTableInfo.get("MemberID") + "';";
			
			List<HashMap<String, String>> list = db.getListOfHashMapsFromResultSet(claimsDetailsQuery);
			if (list.size() > 0) {
				for (HashMap<String, String> row : list) {
					HashMap<String, String> temptestData = new HashMap<String, String>();
					for (String key : row.keySet()) {
						temptestData.put(key, row.get(key));
					}
					if (temptestData.get("CPTCodeID") != null && temptestData.get("CPTCodeID").contains(":")) {
						cptCodeID = temptestData.get("CPTCodeID");
						cptCodeIDSplit = cptCodeID.split(":");
						switch (cptCodeIDSplit.length) {
						case 2:
							cptCodeID = cptCodeIDSplit[0];
							mod1 = cptCodeIDSplit[1];
							mod2 = "";
							break;
						case 3:
							cptCodeID = cptCodeIDSplit[0];
							mod1 = cptCodeIDSplit[1];
							mod2 = cptCodeIDSplit[2];
							break;
						}
					} else if (temptestData.get("CPTCodeID") == null
							|| temptestData.get("CPTCodeID").trim().equals("")) {
						cptCodeID = "";
						mod1 = "";
						mod2 = "";
					} else {
						cptCodeID = temptestData.get("CPTCodeID");
						mod1 = "";
						mod2 = "";
					}
					testDataClaimDetails.put("MemberID", claimsDetailsTableInfo.get("MemberID"));
					testDataClaimDetails.put("CPTCodeID", cptCodeID);
					//testDataClaimDetails.put("Mod1", mod1);
					//testDataClaimDetails.put("Mod2", mod2);
					testDataClaimDetails.put("BilledAmount", temptestData.get(""));
					testDataClaimDetails.put("ApprovedAmount", temptestData.get(""));
					testDataClaimDetails.put("PlanShare", temptestData.get(""));
					//testDataClaimDetails.put("CoPay", temptestData.get(""));
				}
			} else {
				testDataClaimDetails.put("MemberID", claimsDetailsTableInfo.get("MemberID"));
				testDataClaimDetails.put("CPTCodeID", "");
				//testDataClaimDetails.put("Mod1", "");
				//testDataClaimDetails.put("Mod2", "");
				testDataClaimDetails.put("BilledAmount", "");
				testDataClaimDetails.put("ApprovedAmount", "");
				testDataClaimDetails.put("PlanShare", "");
				//testDataClaimDetails.put("CoPay", "");
			}
			flag = compareHashMaps(claimsDetailsTableInfo, testDataClaimDetails);
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			try {
				reportFail("Exception in validateClaimsDetailsTableInfo method!");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}
}
