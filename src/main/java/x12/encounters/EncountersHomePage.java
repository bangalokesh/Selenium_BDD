package x12.encounters;

import java.io.IOException;
import java.util.Set;

import org.openqa.selenium.interactions.Actions;

import enums.LocatorTypes;
import pageclasses.BasePage;
import pageobjects.EncountersObjRepo;

public class EncountersHomePage extends BasePage {

	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(EncountersHomePage.class.getName());

	public EncountersHomePage() {
		driver = getWebDriver();
	}

	public void navigateToEncounters() {
		try {
			elementVisibleWait(LocatorTypes.id, EncountersObjRepo.Encounters.RiskManagementField_id, 30);
			Actions action = new Actions(driver);
			action.moveToElement(getElement(LocatorTypes.id, EncountersObjRepo.Encounters.RiskManagementField_id))
					.click(getElement(LocatorTypes.xpath, EncountersObjRepo.Encounters.encountersLink_xpath))
					.build().perform();
			Set<String> winHandles = driver.getWindowHandles();
			for (String winHandle : winHandles) {
				logger.info(driver.getCurrentUrl());
				if (driver.getCurrentUrl().contains("hpeAction.do?method=initialize")) {
					driver.manage().window().maximize();
					break;
				} else {
					driver.switchTo().window(winHandle);
					driver.manage().window().maximize();
				}
			}
			boolean encounterDetailsLinkExists = isElementPresent(LocatorTypes.className,
					EncountersObjRepo.Encounters.encountersDetail_class);
			if (encounterDetailsLinkExists) {
				reportPass("Medicare Support Services navigate to Encounters page is successful");
			} else {
				reportFail("Medicare Support Services navigate to Encounters page failed");
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Medicare Support Services navigate to Encounters page failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void navigateToEncounterDetails() {
		try {
			getElement(LocatorTypes.className, EncountersObjRepo.Encounters.encountersDetail_class).click();
			boolean claimNumberExists = isElementPresent(LocatorTypes.name,
					EncountersObjRepo.EncounterDetails.claimNumberTextField_name);
			if (claimNumberExists) {
				reportPass("EncountersHomePage navigate to Encounter Details page is successful");
			} else {
				reportFail("EncountersHomePage navigate to Encounter Details page failed");
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("EncountersHomePage navigate to Encounter Details page failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
