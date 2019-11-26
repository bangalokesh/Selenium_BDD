package pageclasses.healthX;

import java.io.IOException;
import java.util.HashMap;
import java.util.Set;

import org.openqa.selenium.Keys;

import enums.LocatorTypes;
import managers.PageManager;
import pageclasses.BasePage;
import pageobjects.HealthXObjRepo;
import utils.Dbconn;

public class HealthXAdminPage extends BasePage {

	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(HealthXAdminPage.class.getName());
	PageManager pm = new PageManager();
	Dbconn db = new Dbconn();

	public HealthXAdminPage() {
		driver = getWebDriver();
	}
	
	public void navigateToProviderTab() {
		boolean isUserNameFieldVisible = false;
		try {
			getElement(LocatorTypes.xpath, HealthXObjRepo.HealthXPortalAdminPortalPage.userManagerProviderPortalTab_xpath).click();
			isUserNameFieldVisible = isElementPresent(LocatorTypes.id, 
					HealthXObjRepo.HealthXPortalAdminPortal_UserManagerPage.userManagerFirstNameTextField_id);
			if(isUserNameFieldVisible == false) {
				reportFail("Navigation to Provider page failed");
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("navigateToProviderPage method resulted in an exception!");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public void navigateToMemberTab() {
		boolean isUserNameFieldVisible = false;
		try {
			getElement(LocatorTypes.xpath, HealthXObjRepo.HealthXPortalAdminPortalPage.userManagerMemberPortalTab_xpath).click();
			isUserNameFieldVisible = isElementPresent(LocatorTypes.id, 
					HealthXObjRepo.HealthXPortalAdminPortal_UserManagerPage.userManagerFirstNameTextField_id);
			if(isUserNameFieldVisible == false) {
				reportFail("Navigation to Member page failed");
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("navigateToMemberPage method resulted in an exception!");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public void navigateToEligibilityAndBenefitsPage() {
		boolean isMemberIDFieldVisible = false;
		try {
			getElement(LocatorTypes.xpath, HealthXObjRepo.HealthXPortalAdminPortalPage.eligibilityAndBenefitsTab_xpath).click();
			isMemberIDFieldVisible = isElementPresent(LocatorTypes.id, 
					HealthXObjRepo.EligibilityAndBenefits.memberIDInputTextArea_className);
			if(isMemberIDFieldVisible == false) {
				reportFail("Navigation to Eligibility and Benefits page failed");
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("navigateToEligibilityAndBenefitsTab method resulted in an exception!");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public boolean searchUserManager(String userName) {
		boolean flag = false;
		boolean isImitateButtonVisible = false;
		try {
			getElement(LocatorTypes.id, HealthXObjRepo.HealthXPortalAdminPortal_UserManagerPage.userManagerUserNameTextField_id).sendKeys(userName);
			//getElement(LocatorTypes.id, HealthXObjRepo.HealthXPortalAdminPortal_UserManagerPage.userManagerFirstNameTextField_id).sendKeys(firstName);
			//getElement(LocatorTypes.id, HealthXObjRepo.HealthXPortalAdminPortal_UserManagerPage.userManagerLastNameTextField_id).sendKeys(lastName);
			//getElement(LocatorTypes.id, HealthXObjRepo.HealthXPortalAdminPortal_UserManagerPage.userManagerNpiTextField_id).sendKeys(npi);
			getElement(LocatorTypes.xpath, HealthXObjRepo.HealthXPortalAdminPortal_UserManagerPage.userManagerSearchButton_xpath).click();
			
			isImitateButtonVisible = isElementPresent(LocatorTypes.className, 
					HealthXObjRepo.HealthXPortalAdminPortal_UserManagerPage.imitateButton_className);
			if(isImitateButtonVisible == false) {
				flag = false;
				reportFail("User manager search failed!");
			} else {
				flag = true;
			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			try {
				reportFail("searchUserManager method resulted in an exception!");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}
	
	public void imitateUserManager(String portalType) {
		boolean isImitateModeFieldVisible = false;
		try {
			getElement(LocatorTypes.className, HealthXObjRepo.HealthXPortalAdminPortal_UserManagerPage.imitateButton_className).click();
			wait(1);
			getElement(LocatorTypes.id, HealthXObjRepo.HealthXPortalAdminPortal_UserManagerPage.imitateUserDropdown_id).click();
			wait(1);
			getElement(LocatorTypes.id, HealthXObjRepo.HealthXPortalAdminPortal_UserManagerPage.imitateUserDropdown_id).sendKeys(portalType);
			wait(2);
			getElement(LocatorTypes.id, HealthXObjRepo.HealthXPortalAdminPortal_UserManagerPage.imitateUserDropdown_id).sendKeys(Keys.ENTER);
			wait(2);
			getElement(LocatorTypes.name, HealthXObjRepo.HealthXPortalAdminPortal_UserManagerPage.imitateUserButton_name).click();
			
			Set<String> winHandles = driver.getWindowHandles();
			for (String winHandle : winHandles) {
				System.out.println(driver.getTitle());
				if (driver.getTitle().contains("Provider Portal") || driver.getTitle().contains("Member Portal")) {
					wait(1);
					driver.manage().window().maximize();
					break;
				} else
					driver.switchTo().window(winHandle);
			}
			
			isImitateModeFieldVisible = isElementPresent(LocatorTypes.id, 
					HealthXObjRepo.HealthXPortalAdminPortal_UserManagerPage.imitateModeField_id);
			if(isImitateModeFieldVisible == false) {
				reportFail("Imitate User Manager navigation failed!");
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("imitateUserManager method resulted in an exception!");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public boolean searchMemberID(String memberID) {
		boolean flag = false;
		boolean areMemberSearchResultsVisible = false;
		try {
			getElement(LocatorTypes.className, HealthXObjRepo.EligibilityAndBenefits.memberIDInputTextArea_className).sendKeys(memberID);
			getElement(LocatorTypes.id, HealthXObjRepo.EligibilityAndBenefits.eligibilityAndBenefitsSearchButton_id).click();
			
			areMemberSearchResultsVisible = isElementPresent(LocatorTypes.xpath, 
					HealthXObjRepo.EligibilityAndBenefits.eligibilityAndBenefitsSearchResultRow_xpath);
			if(areMemberSearchResultsVisible == false) {
				flag = false;
				reportFail("Member ID search failed!");
			} else {
				flag = true;
			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			try {
				reportFail("searchMemberID method resulted in an exception!");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}
	
	public String createUserName(String firstName, String lastName) {
		String userName = firstName.toLowerCase().charAt(0) + lastName.toLowerCase() + Integer.toString(getRandomNumber(1, 1000));
		return userName;
	}
	
	public String createEmailAddress(String userName) {
		String emailAddress = userName.toLowerCase() + "@azblue.com"; //remove the .toLowerCase() from the username
		return emailAddress;
	}
	
	/*userType is either 'Provider' or 'Member'*/
	public HashMap<String, String> createLoginAccountDetails(String firstName, String lastName, String userType) {
		boolean isLoggedIn = false;
		HashMap<String, String> credentialInfo = new HashMap<String, String>();
		try {
			String userName = createUserName(firstName, lastName);
			String emailAddress = createEmailAddress(userName);
			String password = "ABC123!@#"; //getRandomString(10, true); //password will be the same for every added member
			String securityQuestionOne = "Who is your favorite writer?";
			String securityQuestionTwo = "What was the name of your first pet?";
			String securityQuestionThree = "What is your favorite cartoon character?";
			String securityAnswerOne = "Ernest Hemingway";
			String securityAnswerTwo = "Fido";
			String securityAnswerThree = "Mickey Mouse";
			
			elementVisibleWait(LocatorTypes.id, HealthXObjRepo.HealthXRegistrationInfo.registrationInfoUserName_id, 10);
			getElement(LocatorTypes.id, HealthXObjRepo.HealthXRegistrationInfo.registrationInfoUserName_id).sendKeys(userName);
			getElement(LocatorTypes.id, HealthXObjRepo.HealthXRegistrationInfo.registrationInfoEmailAddress_id).sendKeys(emailAddress);
			getElement(LocatorTypes.id, HealthXObjRepo.HealthXRegistrationInfo.registrationInfoConfirmEmailAddress_id).sendKeys(emailAddress);
			getElement(LocatorTypes.id, HealthXObjRepo.HealthXRegistrationInfo.registrationInfoPassword_id).sendKeys(password);
			getElement(LocatorTypes.id, HealthXObjRepo.HealthXRegistrationInfo.registrationInfoConfirmPassword_id).sendKeys(password);
			
			getElement(LocatorTypes.id, HealthXObjRepo.HealthXRegistrationInfo.registrationInfoSecuryQ_1_Dropdown_id).click();
			getElement(LocatorTypes.id, HealthXObjRepo.HealthXRegistrationInfo.registrationInfoSecuryQ_1_Dropdown_id).sendKeys(securityQuestionOne);
			getElement(LocatorTypes.id, HealthXObjRepo.HealthXRegistrationInfo.registrationInfoSecuryQ_1_Dropdown_id).sendKeys(Keys.ENTER);
			getElement(LocatorTypes.id, HealthXObjRepo.HealthXRegistrationInfo.registrationInfoSecuryQ_1_TextField_id).sendKeys(securityAnswerOne);
			
			getElement(LocatorTypes.id, HealthXObjRepo.HealthXRegistrationInfo.registrationInfoSecuryQ_2_Dropdown_id).click();
			getElement(LocatorTypes.id, HealthXObjRepo.HealthXRegistrationInfo.registrationInfoSecuryQ_2_Dropdown_id).sendKeys(securityQuestionTwo);
			getElement(LocatorTypes.id, HealthXObjRepo.HealthXRegistrationInfo.registrationInfoSecuryQ_2_Dropdown_id).sendKeys(Keys.ENTER);
			getElement(LocatorTypes.id, HealthXObjRepo.HealthXRegistrationInfo.registrationInfoSecuryQ_2_TextField_id).sendKeys(securityAnswerTwo);
			
			getElement(LocatorTypes.id, HealthXObjRepo.HealthXRegistrationInfo.registrationInfoSecuryQ_3_Dropdown_id).click();
			getElement(LocatorTypes.id, HealthXObjRepo.HealthXRegistrationInfo.registrationInfoSecuryQ_3_Dropdown_id).sendKeys(securityQuestionThree);
			getElement(LocatorTypes.id, HealthXObjRepo.HealthXRegistrationInfo.registrationInfoSecuryQ_3_Dropdown_id).sendKeys(Keys.ENTER);
			getElement(LocatorTypes.id, HealthXObjRepo.HealthXRegistrationInfo.registrationInfoSecuryQ_3_TextField_id).sendKeys(securityAnswerThree);
			
			getElement(LocatorTypes.id, HealthXObjRepo.HealthXRegistrationInfo.registrationInfoNextButton_id).click();
			
			if(userType.toUpperCase().contains("MEMBER")) {
				elementVisibleWait(LocatorTypes.className, HealthXObjRepo.HealthXRegistrationInfo.registrationConfirmMemberInfo_className, 10);
				wait(1);
				getElement(LocatorTypes.id, HealthXObjRepo.HealthXRegistrationInfo.registrationInfoFinishButton_id).click();
				isLoggedIn = isElementPresent(LocatorTypes.className, HealthXObjRepo.HealthXMemberPortalPage.loggedInAsMember_className);
				if(isLoggedIn == false) {
					reportFail("Unable to confirm the account creation for member.");
				} else {
					credentialInfo.put("UserName", userName);
					credentialInfo.put("Password", password);
				}
			} else {
				elementVisibleWait(LocatorTypes.xpath, HealthXObjRepo.HealthXRegistrationInfo.registrationConfirmProviderInfo_xpath, 10);
				wait(1);
				getElement(LocatorTypes.id, HealthXObjRepo.HealthXRegistrationInfo.registrationInfoFinishButton_id).click();
				isLoggedIn = isElementPresent(LocatorTypes.xpath, HealthXObjRepo.HealthXPortalAdminPortalPage.loggedInAsProviderOrAdmin_xpath);
				if(isLoggedIn == false) {
					reportFail("Unable to confirm the account creation for provider.");
				} else {
					credentialInfo.put("UserName", userName);
					credentialInfo.put("Password", password);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("createLoginAccountDetails method resulted in an exception!");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return credentialInfo;
	}
}
