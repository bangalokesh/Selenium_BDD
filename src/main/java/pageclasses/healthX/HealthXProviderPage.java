package pageclasses.healthX;

import java.io.IOException;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import com.aventstack.extentreports.Status;

import enums.LocatorTypes;
import managers.PageManager;
import pageclasses.BasePage;
import pageobjects.HealthXObjRepo;
import utils.Dbconn;

public class HealthXProviderPage extends BasePage {

	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(HealthXProviderPage.class.getName());
	PageManager pm = new PageManager();
	Dbconn db = new Dbconn();

	public HealthXProviderPage() {
		driver = getWebDriver();
	}
	
	public boolean createProviderAccountNavigation() {
		boolean firstNameFieldExists = false;
		boolean flag = false;
		try {
			getElement(LocatorTypes.xpath, HealthXObjRepo.HealthXProviderPortalPage.providerCreateAnAccount_xpath).click();
			elementVisibleWait(LocatorTypes.id, HealthXObjRepo.HealthXProviderPortalPage.providerlicenseAgreementAcceptCheckBox_id, 10);
			wait(2);
			getElement(LocatorTypes.id, HealthXObjRepo.HealthXProviderPortalPage.providerlicenseAgreementAcceptCheckBox_id).click();
			wait(2);
			getElement(LocatorTypes.id, HealthXObjRepo.HealthXProviderPortalPage.providerlicenseAgreementAgreeButton_id).click();
			firstNameFieldExists = isElementPresent(LocatorTypes.id, 
						HealthXObjRepo.HealthXRegistrationInfo.registrationInfoFirstName_id);
			if(firstNameFieldExists == false) {
				flag = false;
				reportFail("Navigation to Create Provider Contact Info page failed");
			} else {
				flag = true;
			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			try {
				reportFail("createProviderAccountNavigation method resulted in an exception!");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}
	
	public boolean createProviderContactInfo() {
		boolean errorMessageExists = false;
		boolean addedProviders = false;
		boolean flag = false;
		String errorMessage = "";
		String firstName = "";
		String lastName = "";
		String tin = "";
		String contactName = "";
		String contactPhone = "";
		String npi = "";
		String practicePhone = "";
		String practiceAddress = "";
		String practiceFax = "";
		String practiceName = "";
		String primarySpecialty = "";
		long longPracticeFax = 0;
		try {
			firstName = testData.get("FIRST NAME");
			lastName = testData.get("LAST NAME");
			tin = testData.get("TAX ID");
			contactName = testData.get("FIRST NAME") + " " + testData.get("LAST NAME");
			long longContactPhone = new BigDecimal(testData.get("PHONE")).longValueExact();
			contactPhone = Long.toString(longContactPhone);
			npi = testData.get("NPI");
			long longPracticePhone = new BigDecimal(testData.get("PHONE")).longValueExact();
			practicePhone = Long.toString(longPracticePhone);
			if (testData.get("SUITE") == null || testData.get("SUITE").equals("")) {
				practiceAddress = testData.get("PRIMARY ADDRESS") + ", " + testData.get("CITY") + ", "
						+ testData.get("ST") + " " + testData.get("ZIP");
			} else {
				practiceAddress = testData.get("PRIMARY ADDRESS") + " " + testData.get("SUITE") + ", "
						+ testData.get("CITY") + ", " + testData.get("ST") + " " + testData.get("ZIP");
			}
			if(testData.get("FAX") == null || testData.get("FAX").equals("")) {
				longPracticeFax = new BigDecimal(testData.get("PHONE")).longValueExact();
				practiceFax = Long.toString(longPracticeFax);
			} else {
				longPracticeFax = new BigDecimal(testData.get("FAX")).longValueExact();
				practiceFax = Long.toString(longPracticeFax);
			}
			practiceName = testData.get("GROUP NAME");
			primarySpecialty = testData.get("PRIMARY SPECIALTY");
			
			elementVisibleWait(LocatorTypes.id, HealthXObjRepo.HealthXRegistrationInfo.registrationInfoFirstName_id, 10);
			getElement(LocatorTypes.id, HealthXObjRepo.HealthXRegistrationInfo.registrationInfoFirstName_id)
					.sendKeys(firstName);
			getElement(LocatorTypes.id, HealthXObjRepo.HealthXRegistrationInfo.registrationInfoLastName_id)
					.sendKeys(lastName);
			getElement(LocatorTypes.id, HealthXObjRepo.HealthXRegistrationInfo.providerRegistrationInfoTin_id)
					.sendKeys(tin);
			getElement(LocatorTypes.id, HealthXObjRepo.HealthXRegistrationInfo.providerRegistrationInfoContactName_id)
					.sendKeys(contactName);
			getElement(LocatorTypes.id, HealthXObjRepo.HealthXRegistrationInfo.providerRegistrationInfoContactPhone_id)
					.sendKeys(getPhoneInUS_StandardFormat(contactPhone));
			getElement(LocatorTypes.id, HealthXObjRepo.HealthXRegistrationInfo.providerRegistrationInfoNpi_id)
					.sendKeys(npi);
			getElement(LocatorTypes.id, HealthXObjRepo.HealthXRegistrationInfo.providerRegistrationInfoPracticePhone_id)
					.sendKeys(getPhoneInUS_StandardFormat(practicePhone));
			getElement(LocatorTypes.id,
					HealthXObjRepo.HealthXRegistrationInfo.providerRegistrationInfoPracticeAddress_id)
							.sendKeys(practiceAddress);
			getElement(LocatorTypes.id, HealthXObjRepo.HealthXRegistrationInfo.providerRegistrationInfoPracticeFax_id)
					.sendKeys(getPhoneInUS_StandardFormat(practiceFax));
			getElement(LocatorTypes.id, HealthXObjRepo.HealthXRegistrationInfo.providerRegistrationInfoPracticeName_id)
					.sendKeys(practiceName);
			getElement(LocatorTypes.id,
					HealthXObjRepo.HealthXRegistrationInfo.providerRegistrationInfoPrimarySpecialtyDropDown_id).click();
			getElement(LocatorTypes.id,
					HealthXObjRepo.HealthXRegistrationInfo.providerRegistrationInfoPrimarySpecialtyDropDown_id)
							.sendKeys(primarySpecialty);
			getElement(LocatorTypes.id,
					HealthXObjRepo.HealthXRegistrationInfo.providerRegistrationInfoPrimarySpecialtyDropDown_id)
							.sendKeys(Keys.ENTER);
			getElement(LocatorTypes.id, HealthXObjRepo.HealthXRegistrationInfo.providerRegistrationInfoAddButton_id)
					.click();
			
			addedProviders = isElementPresent(LocatorTypes.id, 
					HealthXObjRepo.HealthXRegistrationInfo.providerRegistrationInfoAddedProviders_id);
			if(addedProviders == false) {
				flag = false;
				errorMessageExists = isElementPresent(LocatorTypes.className, 
						HealthXObjRepo.HealthXRegistrationInfo.providerRegistrationInfoErrorMessage_className);
				if(errorMessageExists) {
					errorMessage = getElement(LocatorTypes.className, 
							HealthXObjRepo.HealthXRegistrationInfo.providerRegistrationInfoErrorMessage_className).getText();
					reportFail("Provider contact info registration has failed! " + errorMessage);
				} else {
					reportFail("Provider contact info registration has failed!");
				}
			} else {
				flag = true;
				elementVisibleWait(LocatorTypes.id, HealthXObjRepo.HealthXRegistrationInfo.registrationInfoNextButton_id, 10);
				getElement(LocatorTypes.id, HealthXObjRepo.HealthXRegistrationInfo.registrationInfoNextButton_id).click();
				wait(1);
				getElement(LocatorTypes.id, HealthXObjRepo.HealthXRegistrationInfo.registrationInfoNextButton_id).click();
			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			try {
				reportFail("createProviderContactInfo method resulted in an exception!");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}
	
	public void navigateToEligibilityAndBenefitsTab() {
		try {
			getElement(LocatorTypes.xpath, HealthXObjRepo.EligibilityAndBenefits.EligibilityAndBenefitsTab_xpath).click();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("ProviderPage navigateToEligibilityAndBenefitsTab failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public void navigateToClaimsAndPaymentTab() {
		try {
			getElement(LocatorTypes.xpath, HealthXObjRepo.HealthXProviderPortalPage.claimsAndPaymentTab_xpath).click();
			boolean claimsAndPaymentsDropdownExists = isElementPresent(LocatorTypes.xpath, HealthXObjRepo.ClaimsAndPayments.claimsAndPaymentsProviderNPI_Select_xpath);
			if(!claimsAndPaymentsDropdownExists) {
				reportFail("failure to navigate to Claims and Payment Tab search page.");
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
	
	public boolean selectClaimsProvider() {
		boolean flag = false;
		try {
			getElement(LocatorTypes.xpath, HealthXObjRepo.ClaimsAndPayments.claimsAndPaymentsProviderNPI_Select_xpath).click();
			getElement(LocatorTypes.xpath, HealthXObjRepo.ClaimsAndPayments.claimsAndPaymentsProviderNPI_Select_xpath).sendKeys(testData.get("NPI"));
			getElement(LocatorTypes.xpath, HealthXObjRepo.ClaimsAndPayments.claimsAndPaymentsProviderNPI_Select_xpath).sendKeys(Keys.ENTER);
			elementVisibleWait(LocatorTypes.xpath, HealthXObjRepo.ClaimsAndPayments.claimsAndPaymentsProviderNPI_SelectMessageLabel_xpath, 10);
			String messageLabel = getElement(LocatorTypes.xpath, HealthXObjRepo.ClaimsAndPayments.claimsAndPaymentsProviderNPI_SelectMessageLabel_xpath).getText();
			if(messageLabel.contains("found")) {
				flag = true;
			} else {
				flag = false;
				reportFail("Unable to select NPI in Claims and Payments dropdown.");
			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			try {
				reportFail("Exception in selectClaimsProvider method!");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}
	
	public int getNumberOfClaims() {
		int numberOfClaims = 0;
		try {
			String messageLabel = getElement(LocatorTypes.xpath, 
					HealthXObjRepo.ClaimsAndPayments.claimsAndPaymentsProviderNPI_SelectMessageLabel_xpath).getText();
			if(messageLabel.contains("claims found.") && !messageLabel.contains("No ")) {
				messageLabel = messageLabel.replace("claims found.", "");
			} else if(messageLabel.contains("claim found.") && !messageLabel.contains("No ")) {
				messageLabel = messageLabel.replace("claim found.", "");
			} else if(messageLabel.contains("No")){
				messageLabel = "0";
			}
			messageLabel = messageLabel.trim();
			numberOfClaims = Integer.parseInt(messageLabel);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Exception in getNumberOfClaims method!");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return numberOfClaims;
	}
	
	public HashMap<String, String> getClaimsAndPaymentsInformation() {
		HashMap<String, String> claimsAndPaymentsInfo = new HashMap<String, String>();
		String claimNumber;
		String memberName;
		String memberID;
		String planName;
		String groupName;
		String totalCharges;
		String dateOfService;
		String serviceProvider;
		String npi;
		String claimStatus;
		String planPaid;
		try {
			claimNumber = getElement(LocatorTypes.xpath, 
					HealthXObjRepo.ClaimsAndPayments.claimsAndPaymentsProvider_claimNumber_xpath).getText();
			claimNumber = claimNumber.replace("Claim # ", "");
			memberName = getElement(LocatorTypes.xpath, 
					HealthXObjRepo.ClaimsAndPayments.claimsAndPaymentsProvider_memberName_xpath).getText();
			memberID = getElement(LocatorTypes.xpath, 
					HealthXObjRepo.ClaimsAndPayments.claimsAndPaymentsProvider_memberID_xpath).getText();
			planName = getElement(LocatorTypes.xpath, 
					HealthXObjRepo.ClaimsAndPayments.claimsAndPaymentsProvider_plan_xpath).getText();
			groupName = getElement(LocatorTypes.xpath, 
					HealthXObjRepo.ClaimsAndPayments.claimsAndPaymentsProvider_group_xpath).getText();
			totalCharges = getElement(LocatorTypes.xpath, 
					HealthXObjRepo.ClaimsAndPayments.claimsAndPaymentsProvider_totalCharges_xpath).getText();
			totalCharges = totalCharges.replace("$", "");
			totalCharges = totalCharges.replace(",", "");
			dateOfService = getElement(LocatorTypes.xpath, 
					HealthXObjRepo.ClaimsAndPayments.claimsAndPaymentsProvider_dateOfService_xpath).getText();
			serviceProvider = getElement(LocatorTypes.xpath, 
					HealthXObjRepo.ClaimsAndPayments.claimsAndPaymentsProvider_serviceProvider_xpath).getText();
			npi = getElement(LocatorTypes.xpath, 
					HealthXObjRepo.ClaimsAndPayments.claimsAndPaymentsProvider_npi_xpath).getText();
			claimStatus = getElement(LocatorTypes.xpath, 
					HealthXObjRepo.ClaimsAndPayments.claimsAndPaymentsProvider_claimStatus_xpath).getText();
			planPaid = getElement(LocatorTypes.xpath, 
					HealthXObjRepo.ClaimsAndPayments.claimsAndPaymentsProvider_planPaid_xpath).getText();
			planPaid = planPaid.replace("$", "");
			planPaid = planPaid.replace(",", "");
			
			claimsAndPaymentsInfo.put("ClaimNumber", claimNumber);
			claimsAndPaymentsInfo.put("MemberName", memberName);
			claimsAndPaymentsInfo.put("MemberID", memberID);
			claimsAndPaymentsInfo.put("PlanName", planName);
			claimsAndPaymentsInfo.put("GroupName", groupName);
			claimsAndPaymentsInfo.put("TotalCharges", totalCharges);
			claimsAndPaymentsInfo.put("DateOfService", dateOfService);
			claimsAndPaymentsInfo.put("ServiceProvider", serviceProvider);
			claimsAndPaymentsInfo.put("NPI", npi);
			claimsAndPaymentsInfo.put("ClaimStatus", claimStatus);
			claimsAndPaymentsInfo.put("PlanPaid", planPaid);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Exception in getClaimsAndPaymentsInformation method!");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return claimsAndPaymentsInfo;
	}
	
	public HashMap<String, String> getClaimDetailsTableInfo(int row) {
		HashMap<String,String> claimDetailsTableInfo = new HashMap<String,String>();
		String memberID;
		String claimDetailsCptCode;
		String claimDetailsMod1;
		String claimDetailsMod2;
		String claimDetailsCharges;
		String claimDetailsPlanPaid;
		try{
			memberID = getElement(LocatorTypes.xpath, 
					HealthXObjRepo.ClaimsAndPayments.claimsAndPaymentsProvider_memberID_xpath).getText();
			claimDetailsCptCode = getElement(LocatorTypes.xpath,
					HealthXObjRepo.ClaimsAndPayments.claimsAndPaymentsProvider_claimDetailRow_xpath + "[" + row
							+ "]/td[2]").getText();
			claimDetailsMod1 = getElement(LocatorTypes.xpath,
					HealthXObjRepo.ClaimsAndPayments.claimsAndPaymentsProvider_claimDetailRow_xpath + "[" + row
							+ "]/td[3]").getText();
			claimDetailsMod2 = getElement(LocatorTypes.xpath,
					HealthXObjRepo.ClaimsAndPayments.claimsAndPaymentsProvider_claimDetailRow_xpath + "[" + row
							+ "]/td[4]").getText();
			claimDetailsCharges = getElement(LocatorTypes.xpath,
					HealthXObjRepo.ClaimsAndPayments.claimsAndPaymentsProvider_claimDetailRow_xpath + "[" + row
							+ "]/td[5]").getText();
			claimDetailsCharges = claimDetailsCharges.replace("$", "");
			claimDetailsCharges = claimDetailsCharges.replace(",", "");
			claimDetailsPlanPaid = getElement(LocatorTypes.xpath,
					HealthXObjRepo.ClaimsAndPayments.claimsAndPaymentsProvider_claimDetailRow_xpath + "[" + row
							+ "]/td[8]").getText();
			claimDetailsPlanPaid = claimDetailsPlanPaid.replace("$", "");
			claimDetailsPlanPaid = claimDetailsPlanPaid.replace(",", "");
			
			claimDetailsTableInfo.put("MemberID", memberID);
			claimDetailsTableInfo.put("CPTCodeID", claimDetailsCptCode);
			claimDetailsTableInfo.put("Mod1", claimDetailsMod1);
			claimDetailsTableInfo.put("Mod2", claimDetailsMod2);
			claimDetailsTableInfo.put("Charges", claimDetailsCharges);
			claimDetailsTableInfo.put("PlanPaid", claimDetailsPlanPaid);
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
	
	public boolean processMemberClaimNumbers(){
		HashMap<String, String> claimsAndPaymentsInfo = new HashMap<String, String>();
		boolean flag = false;
		int falseValidationCounter = 0;
		int numberOfClaims;
		int claimLinkIndex = 2;
		try {
			numberOfClaims = getNumberOfClaims(); 
			if(numberOfClaims == 0) {
				test.log(Status.WARNING, "No claims have been found for Provider NPI: " + testData.get("NPI"));
			} else {
				for(int index = 1; index <= numberOfClaims; index++) {
					if(numberOfClaims >= 10 && claimLinkIndex <= 11) {
						getElement(LocatorTypes.xpath, HealthXObjRepo.ClaimsAndPayments.claimsAndPaymentsProvider_claimNumberLink_xpath 
								+ "[" + claimLinkIndex + "]/td[1]/a").click();
						
					} else if(numberOfClaims > 10 && claimLinkIndex > 11) {
						claimLinkIndex = 2;
						boolean navigateForwardExists = isElementPresent(LocatorTypes.xpath, 
								HealthXObjRepo.ClaimsAndPayments.claimsAndPaymentsProvider_navigateForwardFromFirstPage_xpath);
						if(navigateForwardExists) {
							getElement(LocatorTypes.xpath, 
									HealthXObjRepo.ClaimsAndPayments.claimsAndPaymentsProvider_navigateForwardFromFirstPage_xpath).click();
						} else {
							navigateForwardExists = isElementPresent(LocatorTypes.xpath, 
									HealthXObjRepo.ClaimsAndPayments.claimsAndPaymentsProvider_navigateForward_xpath);
							if(navigateForwardExists) {
								getElement(LocatorTypes.xpath, 
										HealthXObjRepo.ClaimsAndPayments.claimsAndPaymentsProvider_navigateForward_xpath).click();
							}
						}
						getElement(LocatorTypes.xpath, HealthXObjRepo.ClaimsAndPayments.claimsAndPaymentsProvider_claimNumberLink_xpath 
								+ "[" + claimLinkIndex + "]/td[1]/a").click();
					} else {
						getElement(LocatorTypes.xpath, HealthXObjRepo.ClaimsAndPayments.claimsAndPaymentsProvider_claimNumberLink_xpath 
								+ "[" + claimLinkIndex + "]/td[1]/a").click();
					}
					
					claimsAndPaymentsInfo = getClaimsAndPaymentsInformation();
					flag = validateClaimsAndPayments(claimsAndPaymentsInfo);
					
					if(flag == false) {
						falseValidationCounter++;
					}
					navigateBack();
					claimLinkIndex++;
				}
				if(falseValidationCounter == 0 && flag == true) {
					flag = true;
					reportPass("Validation for Claims information in HealthX Provider is successful for Provider NPI: " + testData.get("NPI"));
				} else {
					flag = false;
					reportFail("Validation for Claims information in HealthX Provider has failed for Provider NPI: " + testData.get("NPI"));
				}
			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			try {
				reportFail("Exception in processMemberClaimNumbers method!");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}
	
	public double getClaimDetailCharges(int row) {
		double charges = 0;
		String claimDetailsCptCode;
		HashMap<String, String> temptestData = new HashMap<String, String>();
		try {
			claimDetailsCptCode = getElement(LocatorTypes.xpath,
					HealthXObjRepo.ClaimsAndPayments.claimsAndPaymentsProvider_claimDetailRow_xpath + "[" + row
							+ "]/td[2]").getText();
			
			String query = "SELECT TOP 1 cl.CPTCodeID, cl.Amount\r\n" + 
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
					charges = 0;
				} else {
					charges = Double.parseDouble(temptestData.get("Amount"));
				}
			} else {
				charges = 0;
			}
		} catch (Exception e) {
			charges = 0;
			e.printStackTrace();
			try {
				reportFail("Exception in getClaimDetailCharges method!");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return charges;
	}
	
	public double getClaimDetailPlanPaid(int row) {
		double planPaid = 0;
		String claimDetailsCptCode;
		HashMap<String, String> temptestData = new HashMap<String, String>();
		try {
			claimDetailsCptCode = getElement(LocatorTypes.xpath,
					HealthXObjRepo.ClaimsAndPayments.claimsAndPaymentsProvider_claimDetailRow_xpath + "[" + row
							+ "]/td[2]").getText();
			
			String query = "SELECT TOP 1 cl.CPTCodeID, rtc.ClaimPaymentAmount\r\n" + 
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
				if(temptestData.get("ClaimPaymentAmount") == null || temptestData.get("ClaimPaymentAmount").equals("")) {
					planPaid = 0;
				} else {
					planPaid = Double.parseDouble(temptestData.get("ClaimPaymentAmount"));
				}
			} else {
				planPaid = 0;
			}
		} catch (Exception e) {
			planPaid = 0;
			e.printStackTrace();
			try {
				reportFail("Exception in getClaimDetailPlanPaid method!");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return planPaid;
	}
	
	public boolean validateClaimsAndPayments(HashMap<String, String> claimsAndPaymentsUI) {
		boolean flag = false;
		int count = 0;
		HashMap<String, String> claimsDetailsTableInfo = new HashMap<String, String>();
		String providerClaimQuery;
		String fullName;
		String serviceProvider;
		String groupName;
		String claimStatus;
		String claimNumber;
		String dateOfService;
		double totalClaimCharges = 0;
		double totalPlanPaid = 0;
		HashMap<String, String> testDataClaimsInfo = new HashMap<String, String>();
		try {
			List<WebElement> claimDetailsList = getElements(LocatorTypes.xpath, 
					HealthXObjRepo.ClaimsAndPayments.claimsAndPaymentsProvider_claimDetailRow_xpath);
			for(int row = 1; row <= claimDetailsList.size(); row++) {
				claimsDetailsTableInfo = getClaimDetailsTableInfo(row);
				totalClaimCharges += getClaimDetailCharges(row);
				totalPlanPaid += getClaimDetailPlanPaid(row);
				flag = validateClaimsDetailsTableInfo(claimsDetailsTableInfo);
				if(flag == false) {
					count++;
				}
			}
			
			providerClaimQuery = "SELECT TOP 1 md.FirstName, md.MiddleInitial, md.LastName, md.SupplementalID, "
					+ "re.ProductPBPID, re.ProductPlanID, pd.PlanName, mad.PrimaryRxGroup, \r\n"
					+ "cl.CPTCodeID, cl.Amount, cl.DateTimeFromPeriod,\r\n"
					+ "rtc.ClaimNumber, rtc.BillingProviderNPI, \r\n"
					+ "rtc.Validation835ClaimStatus, rtc.ClaimPaymentAmount, rtc.PatientResponsibilityAmount\r\n"
					+ "FROM [dbo].[member_demographic] md\r\n"
					+ "INNER JOIN [dbo].[test_data_readytoenroll] re\r\n"
					+ "ON md.MedicareID = re.MedCareID\r\n"
					+ "INNER JOIN [dbo].[member_accretion_details] mad \r\n"
					+ "ON md.SupplementalID = mad.SupplementalID \r\n"
					+ "INNER JOIN [dbo].[test_data_readytoclaims] rtc \r\n"
					+ "ON md.MedicareID = rtc.MedicareID\r\n"
					+ "INNER JOIN [dbo].[test_data_claimline] cl\r\n"
					+ "ON SUBSTRING(cl.ClaimLineNumber, 1, (LEN(cl.ClaimLineNumber) - 1)) = rtc.ClaimNumber \r\n"
					+ "INNER JOIN [dbo].[product_details] pd\r\n"
					+ "ON re.ProductPBPID = pd.PBPID\r\n"
					+ "AND re.ProductPlanID = pd.PlanID \r\n"
					+ "WHERE md.SupplementalID = '" + claimsAndPaymentsUI.get("MemberID") + "';";
			
			serviceProvider = testData.get("LAST NAME") + ", " + testData.get("FIRST NAME");
			
			List<HashMap<String, String>> list = db.getListOfHashMapsFromResultSet(providerClaimQuery);
			if (list.size() > 0) {
				for (HashMap<String, String> row : list) {
					HashMap<String, String> temptestData = new HashMap<String, String>();
					for (String key : row.keySet()) {
						temptestData.put(key, row.get(key));
					}
					if (temptestData.get("MiddleInitial") == null || temptestData.get("MiddleInitial").equals("")) {
						fullName = temptestData.get("FirstName") + " " + temptestData.get("LastName");
					} else {
						fullName = temptestData.get("FirstName") + " " + temptestData.get("MiddleInitial") + " "
								+ temptestData.get("LastName");
					}
					if (temptestData.get("Validation835ClaimStatus") == null
							|| temptestData.get("Validation835ClaimStatus").equals("")) {
						claimStatus = "";
					} else {
						claimStatus = temptestData.get("Validation835ClaimStatus");
					}
					if (temptestData.get("DateTimeFromPeriod") == null
							|| temptestData.get("DateTimeFromPeriod").equals("")) {
						dateOfService = "";
					} else {
						dateOfService = temptestData.get("DateTimeFromPeriod");
						dateOfService = getDateMMDDYYYYFROMMDDYYYY(dateOfService);
					}
					if (temptestData.get("ClaimNumber") == null
							|| temptestData.get("ClaimNumber").equals("")) {
						claimNumber = "";
					} else {
						claimNumber = temptestData.get("ClaimNumber");
						if(!isNumeric(claimNumber)) {
							claimNumber = claimNumber.replace(claimNumber.substring(0, 3), "");
						}
					}
					groupName = temptestData.get("PrimaryRxGroup") + " - " + temptestData.get("ProductPlanID");

					testDataClaimsInfo.put("ClaimNumber", claimNumber);
					testDataClaimsInfo.put("MemberName", fullName);
					testDataClaimsInfo.put("MemberID", temptestData.get("SupplementalID"));
					testDataClaimsInfo.put("PlanName", temptestData.get("ProductPBPID"));
					testDataClaimsInfo.put("GroupName", groupName);
					testDataClaimsInfo.put("TotalCharges", Double.toString(totalClaimCharges));
					testDataClaimsInfo.put("DateOfService", dateOfService);
					testDataClaimsInfo.put("ServiceProvider", serviceProvider);
					testDataClaimsInfo.put("NPI", testData.get("NPI"));
					testDataClaimsInfo.put("ClaimStatus", claimStatus);
					testDataClaimsInfo.put("PlanPaid", Double.toString(totalPlanPaid));
				}
			} else {
				testDataClaimsInfo.put("ClaimNumber", "");
				testDataClaimsInfo.put("MemberName", "");
				testDataClaimsInfo.put("MemberID", "");
				testDataClaimsInfo.put("PlanName", "");
				testDataClaimsInfo.put("GroupName", "");
				testDataClaimsInfo.put("TotalCharges", "");
				testDataClaimsInfo.put("DateOfService", "");
				testDataClaimsInfo.put("ServiceProvider", serviceProvider);
				testDataClaimsInfo.put("NPI", testData.get("NPI"));
				testDataClaimsInfo.put("ClaimStatus", "");
				testDataClaimsInfo.put("PlanPaid", "");
			}
			flag = compareHashMaps(claimsAndPaymentsUI, testDataClaimsInfo);
			if(count > 0 || flag == false) {
				flag = false;
			} else {
				flag = true;
			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			try {
				reportFail("Exception in validateClaimsAndPayments method!");
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
					testDataClaimDetails.put("Mod1", mod1);
					testDataClaimDetails.put("Mod2", mod2);
					testDataClaimDetails.put("Charges", temptestData.get("Amount"));
					testDataClaimDetails.put("PlanPaid", temptestData.get("ClaimPaymentAmount"));
				}
			} else {
				testDataClaimDetails.put("MemberID", claimsDetailsTableInfo.get("MemberID"));
				testDataClaimDetails.put("CPTCodeID", "");
				testDataClaimDetails.put("Mod1", "");
				testDataClaimDetails.put("Mod2", "");
				testDataClaimDetails.put("Charges", "");
				testDataClaimDetails.put("PlanPaid", "");
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

	public void searchMemberId(String MemberID) {
		try {
			getElement(LocatorTypes.xpath, HealthXObjRepo.EligibilityAndBenefits.memberIDInputTextArea_className)
					.sendKeys(testData.get("MemberID").trim());
			getElement(LocatorTypes.id, HealthXObjRepo.EligibilityAndBenefits.eligibilityAndBenefitsSearchButton_id).click();
			
			String NameSelect=HealthXObjRepo.EligibilityAndBenefits.eligibilityNameSelect_xpath;
			NameSelect = NameSelect.replace("*", MemberID);
			getElement(LocatorTypes.xpath, NameSelect).click();
			reportPass("MemberID search passed");
	    } catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("ProviderPage MemberID Search failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public boolean searchMemberByProviderNPI_Dropdown(String providerNPI) {
		boolean flag = false;
		try {
			getElement(LocatorTypes.xpath, HealthXObjRepo.EligibilityAndBenefits.eligibilityProviderNPI_Select_xpath).click();
			wait(1);
			getElement(LocatorTypes.xpath, HealthXObjRepo.EligibilityAndBenefits.eligibilityProviderNPI_Select_xpath).sendKeys(providerNPI);
			wait(1);
			getElement(LocatorTypes.xpath, HealthXObjRepo.EligibilityAndBenefits.eligibilityProviderNPI_Select_xpath).sendKeys(Keys.ENTER);
			wait(2);
			boolean messageLabelPresent = isElementPresent(LocatorTypes.xpath, HealthXObjRepo.EligibilityAndBenefits.eligibilityProviderNPI_SelectMessageLabel_xpath);
			if(messageLabelPresent) {
				String messageLabelText = getElement(LocatorTypes.xpath, HealthXObjRepo.EligibilityAndBenefits.eligibilityProviderNPI_SelectMessageLabel_xpath).getText();
				if(messageLabelText.contains("No patients found")) {
					flag = false;
					test.log(Status.WARNING, "No patients found for NPI ID " +  testData.get("NPI"));
				} else {
					flag = true;
				}
				/*
				 * Search through member IDs
				 */
			}
	    } catch (Exception e) {
			flag = false;
	    	e.printStackTrace();
			try {
				reportFail("ProviderPage MemberID search by Provider NPI failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}
	
	public HashMap<String, String> getSubscriberDetails() {
		HashMap<String, String> SubscriberDetails = new HashMap<String, String>();
		try {
			String member = getElement(LocatorTypes.xpath, HealthXObjRepo.EligibilityAndBenefits.subscriberMemberName_xpath).getText().trim();
			String group = getElement(LocatorTypes.xpath, HealthXObjRepo.EligibilityAndBenefits.subscriberGroup_xpath).getText().trim();
			String memberID = getElement(LocatorTypes.xpath, HealthXObjRepo.EligibilityAndBenefits.subscriberMemberID_xpath).getText().trim();
			String plan = getElement(LocatorTypes.xpath, HealthXObjRepo.EligibilityAndBenefits.subscriberPlan_xpath).getText().trim();
			
			SubscriberDetails.put("MEMBERNAME", member.trim());
			SubscriberDetails.put("Group", group.trim());
			SubscriberDetails.put("MemberID", memberID.trim());
			SubscriberDetails.put("Plan",plan);
			logger.info(SubscriberDetails);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Retrieval of Subscriber Details failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return SubscriberDetails;
	}

	public boolean validateSubscriberDetails(HashMap<String, String> subscriberData) {
		boolean flag = false;
		String fullName;
		String group;
		String plan;
		try {
			HashMap<String, String> testDataSubscriberDetails = new HashMap<String, String>();
			if (testData.get("MiddleInitial") != null || !testData.get("MiddleInitial").isEmpty()) {
				fullName = testData.get("LastName") + ", " + testData.get("FirstName") + " "
						+ testData.get("MiddleInitial");
			} else {
				fullName = testData.get("LastName") + ", " + testData.get("FirstName");
			}
			
			String productPlanID = testData.get("ProductPlanID");
			String planName = testData.get("PlanName");
			String productPBPID = testData.get("ProductPBPID");
			
			if(productPlanID == null) {
				productPlanID = "";
			}
			if(planName == null) {
				planName = "";
			}
			if(productPBPID == null) {
				productPBPID = "";
			}
			
			group = productPlanID + " - " + planName;
			plan = planName + "(" + productPBPID + ")";
			
			testDataSubscriberDetails.put("MEMBERNAME", fullName.toUpperCase().trim());
			testDataSubscriberDetails.put("Group", group.trim());
			testDataSubscriberDetails.put("MemberID", testData.get("MemberID").trim());
			testDataSubscriberDetails.put("Plan", plan);
			flag = compareHashMaps(subscriberData, testDataSubscriberDetails);
			if(flag) {
				reportPass("Validation for Subscriber Details in HealthX Provider is successful for Provider NPI: " + testData.get("NPI"));
			} else {
				reportFail("Validation for Subscriber Details in HealthX Provider has failed for Provider NPI: " + testData.get("NPI"));
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
	
	public HashMap<String, String> getPCPInformation() {
		HashMap<String, String> PCPInformation = new HashMap<String, String>();
		try {
			String pcpName = getElement(LocatorTypes.xpath, HealthXObjRepo.EligibilityAndBenefits.pcpName_xpath).getText();
			PCPInformation.put("PCPname", pcpName);
			logger.info(PCPInformation);
			reportPass("getPCPInformation - Passed");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Retrieval of PCPInformation failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return PCPInformation;
	}

	public boolean validatePCPInformation(HashMap<String, String> PCPInformationData) {
		boolean flag = false;
		try {
			flag = compareHashMaps(PCPInformationData, testData);
			if(flag) {
				reportPass("Validation for PCP information in HealthX Provider is successful for Provider NPI: " + testData.get("NPI"));
			} else {
				reportFail("Validation for PCP information in HealthX Provider has failed for Provider NPI: " + testData.get("NPI"));
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
	
	public HashMap<String, String> getCoPaymentsAndAccumulators() {
		HashMap<String, String> CoPaymentsAndAccumulators = new HashMap<String, String>();
		try {
			String pcpOfficeVisit = getElement(LocatorTypes.xpath, HealthXObjRepo.EligibilityAndBenefits.pcpOfficeVisit_xpath).getText();
			String specialistOfficeVisit = getElement(LocatorTypes.xpath, HealthXObjRepo.EligibilityAndBenefits.specialistOfficeVisit_xpath).getText();
			String mentalHealthOfficeVisit = getElement(LocatorTypes.xpath, HealthXObjRepo.EligibilityAndBenefits.mentalHealthOfficeVisit_xpath).getText();
			String physicalTherapy = getElement(LocatorTypes.xpath, HealthXObjRepo.EligibilityAndBenefits.physicalTherapy_xpath).getText();
			String laboratoryServices = getElement(LocatorTypes.xpath, HealthXObjRepo.EligibilityAndBenefits.laboratoryServices_xpath).getText();
			String xRay = getElement(LocatorTypes.xpath, HealthXObjRepo.EligibilityAndBenefits.XRay_xpath).getText();
			String CTMRIMRASPECT = getElement(LocatorTypes.xpath, HealthXObjRepo.EligibilityAndBenefits.CTMRIMRASPECT_xpath).getText();
			String inpatientHospitalCoverage = getElement(LocatorTypes.xpath, HealthXObjRepo.EligibilityAndBenefits.inpatientHospitalCoverage_xpath).getText();
			String outpatientServicesHospital = getElement(LocatorTypes.xpath, HealthXObjRepo.EligibilityAndBenefits.outpatientServicesHospital_xpath).getText();
			String outpatientServicesAmbulatorySurgeryCenter = getElement(LocatorTypes.xpath, HealthXObjRepo.EligibilityAndBenefits.outpatientServicesAmbulatorySurgeryCenter_xpath).getText();
			
			pcpOfficeVisit = pcpOfficeVisit.replace("$", "");
			specialistOfficeVisit = specialistOfficeVisit.replace("$", "");
			mentalHealthOfficeVisit = mentalHealthOfficeVisit.replace("$", "");
			physicalTherapy = physicalTherapy.replace("$", "");
			laboratoryServices = laboratoryServices.replace("$", "");
			xRay = xRay.replace("$", "");
			CTMRIMRASPECT = CTMRIMRASPECT.replace("$", "");
			inpatientHospitalCoverage = inpatientHospitalCoverage.replace("$", "");
			outpatientServicesHospital = outpatientServicesHospital.replace("$", "");
			outpatientServicesAmbulatorySurgeryCenter = outpatientServicesAmbulatorySurgeryCenter.replace("$", "");
			
			CoPaymentsAndAccumulators.put("PCPOfficeVisit", pcpOfficeVisit);
			CoPaymentsAndAccumulators.put("SpecialistOfficeVisit", specialistOfficeVisit);
			CoPaymentsAndAccumulators.put("MentalHealthOfficeVisit", mentalHealthOfficeVisit);
			CoPaymentsAndAccumulators.put("PhysicalTherapy", physicalTherapy);
			CoPaymentsAndAccumulators.put("LaboratoryServices", laboratoryServices);
			CoPaymentsAndAccumulators.put("XRay", xRay);
			CoPaymentsAndAccumulators.put("CTMRIMRASPECT", CTMRIMRASPECT);
			CoPaymentsAndAccumulators.put("InpatientHospitalCoverage", inpatientHospitalCoverage);
			CoPaymentsAndAccumulators.put("OutpatientServicesHospital", outpatientServicesHospital);
			CoPaymentsAndAccumulators.put("OutpatientServicesAmbulatorySurgeryCenter", outpatientServicesAmbulatorySurgeryCenter);
			logger.info(CoPaymentsAndAccumulators);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Retrieval of CoPaymentsAndAccumulators failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return CoPaymentsAndAccumulators;
	}

	public boolean validateCoPaymentsAndAccumulators(HashMap<String, String> CoPaymentsAndAccumulatorsData) {
		boolean flag = false;
		try {
			flag = compareHashMaps(CoPaymentsAndAccumulatorsData, testData);
			if(flag) {
				reportPass("Validation for CoPayments and Accumulators data in HealthX Provider is successful for Provider NPI: " + testData.get("NPI"));
			} else {
				reportFail("Validation for CoPayments and Accumulators data in HealthX Provider has failed for Provider NPI: " + testData.get("NPI"));
			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			try {
				reportFail("validateCoPaymentsAndAccumulators failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}
	
}
