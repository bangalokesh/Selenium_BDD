package pageclasses.m360;

import java.io.IOException;
import java.util.Set;

import org.openqa.selenium.interactions.Actions;

import enums.LocatorTypes;
import pageclasses.BasePage;
import pageobjects.M360BillingObjRepo;
import pageobjects.M360MembershipObjRepo;

public class M360HomePage extends BasePage {

	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(M360HomePage.class.getName());

	public M360HomePage() {
		driver = getWebDriver();
	}

	public void navigateToEligInquiry() {
		(new Actions(driver))
				.moveToElement(getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360LandingPage.m360Header_xpath))
				.click(getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360LandingPage.m360EligHeader_xpath))
				.build().perform();

		for (String winHandle : driver.getWindowHandles()) {
			driver.switchTo().window(winHandle);
		}
	}

	public void navigateToMThreeSixty() {
		try {
			getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360LandingPage.m360Header_xpath).click();
			Set<String> winHandles = driver.getWindowHandles();
			for (String winHandle : winHandles) {
				System.out.println(driver.getCurrentUrl());
				if (driver.getCurrentUrl().contains("eemAction.do?method=initialize")) {
					driver.manage().window().maximize();
					break;
				} else
					driver.switchTo().window(winHandle);
			}
			reportPass("M360MemberPage navigate to M360 page is successful");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("M360MemberPage navigate to M360 page failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void navigateToM360BillingHome() {
		try {
			getElement(LocatorTypes.xpath, M360BillingObjRepo.M360ManagePayment.menuBilling_xpath).click();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("navigateToM360BillingHome: " + e.getMessage());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

	}

}
