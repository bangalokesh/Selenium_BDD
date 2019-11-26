package pageclasses.brokerPortal;

import java.io.IOException;

import enums.LocatorTypes;
import managers.PropertyFileReader;
import pageclasses.BasePage;
import pageobjects.BrokerPortalObjRepo;
import utils.Dbconn;

public class BrokerLoginPage extends BasePage {

	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(BrokerLoginPage.class.getName());
	Dbconn db = new Dbconn();

	public BrokerLoginPage() {
		driver = getWebDriver();
	}

	public void login(String userId) {
		try {

			String uri = PropertyFileReader.getBrokerPortalUrl().replace("\"", "");
			String userid = userId;
			String password = decodeString(PropertyFileReader.getBrokerPortalPassword().replace("\"", ""));
			driver.navigate().to(uri);
			if (driver.getTitle().contains("BCBSAZ Broker Home Page")) {
				System.out.println(driver.getTitle());
				reportPass("Broker Portal Login Successful");
			} else {
				getElement(LocatorTypes.xpath, BrokerPortalObjRepo.BrokerPortalLogin.userid_xpath).sendKeys(userid);
				getElement(LocatorTypes.xpath, BrokerPortalObjRepo.BrokerPortalLogin.password_xpath).sendKeys(password);
				getElement(LocatorTypes.xpath, BrokerPortalObjRepo.BrokerPortalLogin.signin_xpath).click();
				if (driver.getTitle().contains("BCBSAZ Broker Home Page"))
					reportPass("Broker page Login Successful");
				else
					reportFail("Broker page Login Failed");
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("BrokerPortal - login failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

}
