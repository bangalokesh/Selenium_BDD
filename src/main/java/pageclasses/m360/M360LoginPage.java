package pageclasses.m360;

import java.io.IOException;

import com.aventstack.extentreports.Status;

import enums.LocatorTypes;
import managers.PropertyFileReader;
import pageclasses.BasePage;
import pageobjects.M360MembershipObjRepo;

public class M360LoginPage extends BasePage{
	
	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(M360LoginPage.class.getName());
	
	public M360LoginPage() {
		driver = getWebDriver();
	}
	
	public void login() {
		try {
			String uri = PropertyFileReader.getM360Url().replace("\"", "");
			String userid = PropertyFileReader.getM360UserId().replace("\"", "");
			String password = decodeEncryptedString("M360");
			driver.navigate().to(uri);
			wait(2);
			if(driver.getTitle().contains("AZBLUE - EVAL")) {
				test.log(Status.INFO, "Navigated to M360");
			} else {
				getElement(LocatorTypes.name, M360MembershipObjRepo.M360Login.userid_name).sendKeys(userid);
				getElement(LocatorTypes.name, M360MembershipObjRepo.M360Login.password_name).sendKeys(password);
				getElement(LocatorTypes.name, M360MembershipObjRepo.M360Login.signin_name).click();
			}
			driver.switchTo().frame(getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360LandingPage.m360HomeFrame_xpath));
			if(driver.getTitle().contains("Support"))
				reportPass("M360 Login Successful");
			else {
				reportFail("M360 Login fialed");
				setContinueExecution(false);
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("M360LoginPage - login failed exception");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

}
