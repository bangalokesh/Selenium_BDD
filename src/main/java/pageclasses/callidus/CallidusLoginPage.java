package pageclasses.callidus;

import java.io.IOException;

import enums.LocatorTypes;
import managers.PropertyFileReader;
import pageclasses.BasePage;
import pageobjects.CallidusObjRepo;

public class CallidusLoginPage extends BasePage {

	public CallidusLoginPage() {
		driver = getWebDriver();
	}

	public void login() {
		try {
			String uri = PropertyFileReader.getCallidusUrl().replace("\"", "");
			String userid = PropertyFileReader.getCallidusUserId().replace("\"", "");
			String password = decodeEncryptedString("Callidus");
			driver.navigate().to(uri);
			wait(2);
			if (driver.getTitle().contains("BCBSAZ ICM - Management")) {
				System.out.println(driver.getTitle());
				reportPass("Callidus Login Successful");
			} else {

				getElement(LocatorTypes.name, CallidusObjRepo.CallidusLogin.userid_name).sendKeys(userid);
				getElement(LocatorTypes.name, CallidusObjRepo.CallidusLogin.password_name).sendKeys(password);
				getElement(LocatorTypes.id, CallidusObjRepo.CallidusLogin.login_id).click();

				if (driver.getTitle().contains("BCBSAZ ICM - Management"))
					reportPass("Callidus Login Successful");
				else
					reportFail("Callidus Login Not Successful");
			}

		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Callidus - login failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

}
