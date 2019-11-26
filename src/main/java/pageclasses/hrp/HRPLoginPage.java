package pageclasses.hrp;

import java.io.IOException;
import java.util.Set;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import com.aventstack.extentreports.Status;

import enums.LocatorTypes;
import io.appium.java_client.windows.WindowsDriver;
import managers.PropertyFileReader;
import pageclasses.BasePage;
import pageobjects.HRPObjRepo;
import utils.Const;

public class HRPLoginPage extends BasePage {

	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(HRPLoginPage.class.getName());

	public HRPLoginPage(WindowsDriver<WebElement> winDriver) {
		if(winDriver == null)
			getWinDriver(Const.HRPPATH);
	}

	public void hrpLogin() {
		try {
			
			String userid = PropertyFileReader.getHrpUserId().replace("\"", "");
			String password = decodeEncryptedString("HRP");
			WebElement uid = getWinElement(LocatorTypes.xpath, HRPObjRepo.HRPLogin.uname_xpath);
			WebElement pwd = getWinElement(LocatorTypes.xpath, HRPObjRepo.HRPLogin.pwd_xpath);
			WebElement login_button = getWinElement(LocatorTypes.name, HRPObjRepo.HRPLogin.login_name);
			
			if(isWinElementPresent(LocatorTypes.id, HRPObjRepo.HRPLogin.uname_id)) {
			uid.clear();
			uid.sendKeys(userid);
			
			pwd.clear();
			pwd.sendKeys(password);
			}
			
			if(uid.getText().isEmpty()) {
				uid.clear();
				uid.sendKeys(userid);
				
				pwd.clear();
				pwd.sendKeys(password);
			}
			wait(1);
			WebElement we = getWinElement(LocatorTypes.id, HRPObjRepo.HRPLogin.environment_id);
			we.sendKeys(Const.HRPEnvironment + Keys.ENTER);
			wait(1);
			login_button.click();
			
			wait(45);
			if(isWinElementPresent(LocatorTypes.id, HRPObjRepo.HRPLogin.uname_id)) {
				String errorMsg1 = getWinElement(LocatorTypes.id, HRPObjRepo.HRPLogin.loginForm).getText();
				if (errorMsg1.contains("Invalid user name or password.")) {
					uid.clear();
					uid.sendKeys(userid);
					wait(2);
					pwd.clear();
					pwd.sendKeys(password);
					wait(3);
					login_button.click();
					wait(45);
				}
			}
		

			Set<String> winHandles = winDriver.getWindowHandles();
//			while (winHandles.size() == 0) {
//				if (!winDriver.getTitle().contains("HealthRules Manager")) {
//					String errorMsg = getWinElement(LocatorTypes.id, HRPObjRepo.HRPLogin.loginForm).getText();
//					if (errorMsg.contains("Invalid user name or password.")) {
//						getWinElement(LocatorTypes.id, HRPObjRepo.HRPLogin.pwd_id).sendKeys(password);
//						getWinElement(LocatorTypes.name, HRPObjRepo.HRPLogin.login_name).click();
//					}
//				}
//				wait(2);
//				winHandles = winDriver.getWindowHandles();
//			}
			if (isWinElementPresent(LocatorTypes.name, HRPObjRepo.HRPLogin.ok_name)) {
				getWinElement(LocatorTypes.name, HRPObjRepo.HRPLogin.ok_name).click();
			}


			winHandles = winDriver.getWindowHandles();
			outerloop: for (String winHandle : winHandles) {
				winDriver.switchTo().window(winHandle);
				winDriver.manage().window().maximize();
				if (winDriver.getTitle().contains("HealthRules Manager")) {
					if (isWinElementPresent(LocatorTypes.id, HRPObjRepo.HRPLogin.close_id)) {
						getWinElement(LocatorTypes.id, HRPObjRepo.HRPLogin.close_id).click();
					}
					test.log(Status.INFO,"login successful");
					break outerloop;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("failure in login at HRPLoginPage");
				takeScreenShotAndAttach("login Failure", System.getProperty("user.dir") + "//reports//htmlReports//Screenshots//");
				
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

		public void killHRP() {
		try {
		Runtime.getRuntime().exec("taskkill /F /IM HealthEdge.Manager.exe /T");
		} catch(Exception e) {
			e.printStackTrace();
		}
		}
		

}
