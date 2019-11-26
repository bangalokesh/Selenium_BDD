package pageclasses.guidingCare;

import java.io.IOException;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import enums.LocatorTypes;
import pageclasses.BasePage;
import pageobjects.GuidingCareObjRepo;
import utils.Dbconn;

public class GuidingCareDecision extends BasePage {
	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(GuidingCareLoginPage.class.getName());

	public GuidingCareDecision() {
		driver = getWebDriver();
	}

	Dbconn db = new Dbconn();

	public void navigateToDecisionsPage() {

		try {
			wait(4);
			elementVisibleWait(LocatorTypes.className,
					GuidingCareObjRepo.GuidingCarePreAuthDecisions.desicionTabLink_class, 10);
			getElement(LocatorTypes.className, GuidingCareObjRepo.GuidingCarePreAuthDecisions.desicionTabLink_class)
					.click();
			;
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void DecisionPreauthApprove() {

		try {
			elementVisibleWait(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCarePreAuthDecisions.decisionStatus_xpath,
					10);
			getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCarePreAuthDecisions.decisionStatus_xpath).click();

			elementVisibleWait(LocatorTypes.id,
					GuidingCareObjRepo.GuidingCarePreAuthDecisions.decisionStatusDropdown_id, 10);
			Select status = new Select(getElement(LocatorTypes.id,
					GuidingCareObjRepo.GuidingCarePreAuthDecisions.decisionStatusDropdown_id));
			status.selectByVisibleText(testData.get("DecisionStatusToBe"));
			elementVisibleWait(LocatorTypes.id,
					GuidingCareObjRepo.GuidingCarePreAuthDecisions.decisionStatusCodeDropdown_id, 10);
			Select statusCode = new Select(getElement(LocatorTypes.id,
					GuidingCareObjRepo.GuidingCarePreAuthDecisions.decisionStatusCodeDropdown_id));
			statusCode.selectByIndex(1);// selectByVisibleText("Approved-Medical Necessity Met");
			elementVisibleWait(LocatorTypes.xpath,
					GuidingCareObjRepo.GuidingCarePreAuthDecisions.decisionRequestedDays_xpath, 10);
			String daysRequested = getElement(LocatorTypes.xpath,
					GuidingCareObjRepo.GuidingCarePreAuthDecisions.decisionRequestedDays_xpath).getAttribute("title");

			getElement(LocatorTypes.id, GuidingCareObjRepo.GuidingCarePreAuthDecisions.decisionApprovedDays_id)
					.sendKeys(daysRequested);

			getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCarePreAuthDecisions.saveButton_xpath).click();

			elementVisibleWait(LocatorTypes.xpath, GuidingCareObjRepo.PreAuthorization.decisionStatus_xpath, 10);

			String decisionStatus = getElement(LocatorTypes.xpath,
					GuidingCareObjRepo.PreAuthorization.decisionStatus_xpath).getText();

			updateDecisionStatusToDB(decisionStatus);

			reportPass("Decision status is updated for authID:" + testData.get("PreAuth_ID"));
		} catch (Exception e) {
			try {
				reportFail("Decision status update failed for authID:" + testData.get("PreAuth_ID"));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
		}
	}

	public void updateDecisionStatusToDB(String decisionStatus) {
		String query = "update [VelocityTestAutomation].[dbo].[guidingCare_Preauthorization_Activity] "
				+ "set status = '" + decisionStatus + "' where preauth_id ='" + testData.get("PreAuth_ID") + "'";
		db.sqlUpdate(query);
	}

	public void navigateToActivitiesPage() {
		try {
			elementClickableWait(LocatorTypes.id, GuidingCareObjRepo.GuidingCareLandingPage.authorizationsLink_id, 10);

			WebElement authorizationLink = getElement(LocatorTypes.xpath,
					GuidingCareObjRepo.GuidingCareLandingPage.authorizationsLink_xpath);

			JavascriptExecutor ex = (JavascriptExecutor) driver;
			ex.executeScript("arguments[0].click();", authorizationLink);

			elementClickableWait(LocatorTypes.id, GuidingCareObjRepo.GuidingCareLandingPage.AuthIDTextField_id, 10);
			getElement(LocatorTypes.id, GuidingCareObjRepo.GuidingCareLandingPage.AuthIDTextField_id)
					.sendKeys(testData.get("PreAuth_ID"));
			elementClickableWait(LocatorTypes.id, GuidingCareObjRepo.GuidingCareLandingPage.AuthSearchButton_id, 10);
			getElement(LocatorTypes.id, GuidingCareObjRepo.GuidingCareLandingPage.AuthSearchButton_id).click();

			if (isElementPresent(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareLandingPage.AuthResult_xpath)) {
				WebElement resultLink = getElement(LocatorTypes.xpath,
						GuidingCareObjRepo.GuidingCareLandingPage.AuthResult_xpath);
				ex.executeScript("arguments[0].click();", resultLink);
			} else
				return;
			wait(4);
			elementVisibleWait(LocatorTypes.className,
					GuidingCareObjRepo.GuidingCarePreAuthDecisions.activitiesLink_class, 10);
			getElement(LocatorTypes.className, GuidingCareObjRepo.GuidingCarePreAuthDecisions.activitiesLink_class)
					.click();
			;
		} catch (Exception e) {
			e.printStackTrace();
		}

	}
}
