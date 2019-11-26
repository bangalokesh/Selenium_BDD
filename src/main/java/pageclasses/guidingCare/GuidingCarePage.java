package pageclasses.guidingCare;

import java.io.IOException;

import org.openqa.selenium.interactions.Actions;

import enums.LocatorTypes;
import managers.PageManager;
import pageclasses.BasePage;
import pageobjects.GuidingCareObjRepo;
import utils.Dbconn;

public class GuidingCarePage extends BasePage {

	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(GuidingCarePage.class.getName());
	PageManager pm = new PageManager();
	Dbconn db = new Dbconn();

	public GuidingCarePage() {
		driver = getWebDriver();
	}

	public void navigateToProviderPage() {
		try {
			wait(2);
			elementVisibleWait(LocatorTypes.id, GuidingCareObjRepo.GuidingCareLandingPage.searchDropdownArrow_id, 30);
			Actions action = new Actions(driver);
			action.moveToElement(
					getElement(LocatorTypes.id, GuidingCareObjRepo.GuidingCareLandingPage.searchDropdownArrow_id))
					.click(getElement(LocatorTypes.id, GuidingCareObjRepo.GuidingCareLandingPage.providerSearchLink_id))
					.build().perform();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Navigation to Provider page failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void returnToHomePage() {
		try {
			getElement(LocatorTypes.id, GuidingCareObjRepo.GuidingCareLandingPage.returnToHomePage_id).click();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Navigation to home page failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void navigateToMemberPage() {
		try {
			wait(4);
			elementVisibleWait(LocatorTypes.id, GuidingCareObjRepo.GuidingCareLandingPage.searchDropdownArrow_id, 30);
			Actions action = new Actions(driver);
			action.moveToElement(
					getElement(LocatorTypes.id, GuidingCareObjRepo.GuidingCareLandingPage.searchDropdownArrow_id))
					.click(getElement(LocatorTypes.id, GuidingCareObjRepo.GuidingCareLandingPage.memberSearchLink_id))
					.build().perform();
			wait(2);
			elementVisibleWait(LocatorTypes.id, GuidingCareObjRepo.GuidingCareMemberDetails.memberSearch_id, 40);
			reportPass("Navigated to Member page successfully");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Navigation to Member page failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void logout() {
		try {
			getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareLogout.logoutDropdownArrow_xpaath).click();
			getElement(LocatorTypes.id, GuidingCareObjRepo.GuidingCareLogout.logoutLink_id).click();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
