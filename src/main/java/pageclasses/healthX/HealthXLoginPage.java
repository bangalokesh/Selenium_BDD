package pageclasses.healthX;

import java.io.IOException;

import com.aventstack.extentreports.Status;

import enums.LocatorTypes;
import managers.PropertyFileReader;
import pageclasses.BasePage;
import pageobjects.HealthXObjRepo;

public class HealthXLoginPage extends BasePage {

public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(HealthXLoginPage.class.getName());
	
	public HealthXLoginPage() {
		driver = getWebDriver();
	}
	
	public void loginAsAdmin() {
		try {
			boolean isLoggedIn = false;
			String url = PropertyFileReader.getHealthXAdminPortalUrl().replace("\"", "");
			String userid = PropertyFileReader.getHealthXAdminPortalUserId().replace("\"", "");
			String password = decodeEncryptedString("healthXAdminPortalUrl");
			driver.navigate().to(url);
			
			isLoggedIn = isElementPresent(LocatorTypes.xpath, HealthXObjRepo.HealthXPortalAdminPortalPage.loggedInAsProviderOrAdmin_xpath);
			if(isLoggedIn) {
				test.log(Status.INFO, "Navigated to HealthX Admin Portal");
			} else {
				getElement(LocatorTypes.id, HealthXObjRepo.HealthXAdminPortalLogin.username_id).sendKeys(userid);
				getElement(LocatorTypes.name, HealthXObjRepo.HealthXAdminPortalLogin.password_name).sendKeys(password);
				getElement(LocatorTypes.id, HealthXObjRepo.HealthXAdminPortalLogin.loginButton_id).click();
			}
			
			isLoggedIn = isElementPresent(LocatorTypes.xpath, HealthXObjRepo.HealthXPortalAdminPortalPage.loggedInAsProviderOrAdmin_xpath);
			if(isLoggedIn)
				reportPass("HealthX Admin Login Successful");
			else {
				reportFail("HealthX Admin Login Failed");
				setContinueExecution(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("HealthXLoginPage loginAsAdmin - login failed exception");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public void loginAsProvider(String userid, String password) {
		try {
			boolean isLoggedIn = false;
			String url = PropertyFileReader.getHealthXProviderPortalUrl().replace("\"", "");
			driver.navigate().to(url);
			
			isLoggedIn = isElementPresent(LocatorTypes.id, HealthXObjRepo.HealthXAdminPortalLogin.username_id);
			if(!isLoggedIn) {
				test.log(Status.INFO, "Navigated to HealthX Provider Portal");
			} else {
				getElement(LocatorTypes.id, HealthXObjRepo.HealthXAdminPortalLogin.username_id).sendKeys(userid);
				getElement(LocatorTypes.name, HealthXObjRepo.HealthXAdminPortalLogin.password_name).sendKeys(password);
				getElement(LocatorTypes.id, HealthXObjRepo.HealthXAdminPortalLogin.loginButton_id).click();
			}
			
			isLoggedIn = isElementPresent(LocatorTypes.xpath, HealthXObjRepo.HealthXPortalAdminPortalPage.loggedInAsProviderOrAdmin_xpath);
			if(isLoggedIn)
				reportPass("HealthX Provider Login Successful");
			else {
				reportFail("HealthX Provider Login Failed");
				setContinueExecution(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("HealthXLoginPage loginAsProvider - login failed exception");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public void loginAsMember(String userid, String password) {
		try {
			boolean isLoggedIn = false;
			String url = PropertyFileReader.getHealthXMemberPortalUrl().replace("\"", "");
			driver.navigate().to(url);
			
			isLoggedIn = isElementPresent(LocatorTypes.id, HealthXObjRepo.HealthXAdminPortalLogin.username_id);
			if(!isLoggedIn) {
				test.log(Status.INFO, "Navigated to HealthX Member Portal");
			} else {
				getElement(LocatorTypes.id, HealthXObjRepo.HealthXAdminPortalLogin.username_id).sendKeys(userid);
				getElement(LocatorTypes.name, HealthXObjRepo.HealthXAdminPortalLogin.password_name).sendKeys(password);
				getElement(LocatorTypes.id, HealthXObjRepo.HealthXAdminPortalLogin.loginButton_id).click();
			}
			
			isLoggedIn = isElementPresent(LocatorTypes.className, HealthXObjRepo.HealthXMemberPortalPage.loggedInAsMember_className);
			if(isLoggedIn)
				reportPass("HealthX Member Login Successful");
			else {
				reportFail("HealthX Member Login Failed");
				setContinueExecution(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("HealthXLoginPage loginAsMember - login failed exception");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public void launchMemberPortalPage() {
		try {
			String url = PropertyFileReader.getHealthXMemberPortalUrl().replace("\"", "");
			driver.navigate().to(url);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("HealthXLoginPage launchMemberPortalPage - Unable to launch Member Portal Web page.");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public void launchProviderPortalPage() {
		try {
			String url = PropertyFileReader.getHealthXProviderPortalUrl().replace("\"", "");
			driver.navigate().to(url);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("HealthXLoginPage launchProviderPortalPage - Unable to launch Provider Portal Web page.");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public void launchAdminPortalPage() {
		try {
			String url = PropertyFileReader.getHealthXAdminPortalUrl().replace("\"", "");
			driver.navigate().to(url);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("HealthXLoginPage launchAdminPortalPage - Unable to launch Admin Portal Web page.");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
	
	public void logoutAdminPortal() {
		try {
			elementVisibleWait(LocatorTypes.xpath, HealthXObjRepo.HealthXPortalAdminPortalPage.logOut_xpath, 10);
			getElement(LocatorTypes.xpath, HealthXObjRepo.HealthXPortalAdminPortalPage.logOut_xpath).click();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	public void logoutMemberPortal() {
		try {
			elementVisibleWait(LocatorTypes.xpath, HealthXObjRepo.HealthXMemberPortalPage.logOut_xpath, 10);
			getElement(LocatorTypes.xpath, HealthXObjRepo.HealthXMemberPortalPage.logOut_xpath).click();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
