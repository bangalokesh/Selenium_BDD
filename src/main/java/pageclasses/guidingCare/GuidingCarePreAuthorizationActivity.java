package pageclasses.guidingCare;

import java.io.IOException;
import java.sql.SQLException;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import enums.LocatorTypes;
import managers.PageManager;
import managers.PropertyFileReader;
import pageclasses.BasePage;
import pageobjects.GuidingCareObjRepo;

public class GuidingCarePreAuthorizationActivity extends BasePage {

	PageManager pm = new PageManager();
	GuidingCarePage gcp = new GuidingCarePage();
	GuidingCareMemberDetailsPage gcm = new GuidingCareMemberDetailsPage();

	public void openAuthActivityWindow() {
		getElement(LocatorTypes.xpath,
				GuidingCareObjRepo.PreAuthorizationActivity.actionWindowDropdownArrowButton_xpath).click();
		getElement(LocatorTypes.id, GuidingCareObjRepo.PreAuthorizationActivity.addAuthActivity_id).click();
	}

	public void createPreAuthActivity(String activityType, String role) {

		try {
			openAuthActivityWindow();

			Set<String> winHandles = driver.getWindowHandles();
			for (String winHandle : winHandles) {
				if (driver.getTitle().contains("AUTHORIZATION ACTIVITY"))
					break;
				else
					driver.switchTo().window(winHandle);
			}

			WebElement dropdownArrow;
			WebElement dropdownText;
			wait(2);

			// ActivityType
			dropdownArrow = getElement(LocatorTypes.xpath,
					GuidingCareObjRepo.PreAuthorizationActivity.activityTypeDropdownArrowButton_xpath);
			dropdownText = getElement(LocatorTypes.xpath,
					GuidingCareObjRepo.PreAuthorizationActivity.activityTypeDropdownSelectedText_xpath);
			selectDropdownValue(activityType, dropdownArrow, dropdownText);
			wait(1);

			// Priority
			dropdownArrow = getElement(LocatorTypes.xpath,
					GuidingCareObjRepo.PreAuthorizationActivity.priorityDropdownArrowButton_xpath);
			dropdownText = getElement(LocatorTypes.xpath,
					GuidingCareObjRepo.PreAuthorizationActivity.priorityDropdownSelectedText_xpath);
			selectDropdownValue("Low", dropdownArrow, dropdownText);
			wait(1);

			// Find Provider dd
			// {UmDdActProvider}

			getElement(LocatorTypes.id, GuidingCareObjRepo.PreAuthorizationActivity.dueDate_id)
					.sendKeys(getTimeStamp());

			// AssignedTo
			dropdownArrow = getElement(LocatorTypes.xpath,
					GuidingCareObjRepo.PreAuthorizationActivity.assignedToDropdownArrowButton_xpath);
			dropdownText = getElement(LocatorTypes.xpath,
					GuidingCareObjRepo.PreAuthorizationActivity.assignedToDropdownSelectedText_xpath);

			// selectDropdownValue("Physician Reviewer", dropdownArrow, dropdownText);

			switch (role) {

			case "Physician Reviewer":
				role = PropertyFileReader.getGuidingCarePhysicianReviewerUserId().replace("\"", "").substring(2);
				break;

			case "ClinicalReviewer":
				role = PropertyFileReader.getGuidingCareUMClinicalReviewerUserId().replace("\"", "").substring(2);
				break;

			case "Supervisor":
				role = PropertyFileReader.getGuidingCareUMSupervisorUserId().replace("\"", "").substring(1);
				break;
			}

			selectDropdownValueContains(role, dropdownArrow, dropdownText);

			String assignedTo = driver
					.findElement(
							By.xpath(GuidingCareObjRepo.PreAuthorizationActivity.assignedToDropdownSelectedText_xpath))
					.getText();

			// Comments
			// getElement(LocatorTypes.id,
			// GuidingCareObjRepo.PreAuthorizationActivity.comments).sendKeys(activityType +
			// " assigned to : " + " " + Keys.TAB + Keys.ENTER); //assignedTo);
			getElement(LocatorTypes.id, GuidingCareObjRepo.PreAuthorizationActivity.comments)
					.sendKeys(activityType + " assigned to: " + assignedTo + Keys.TAB + Keys.ENTER); // assignedTo);

			wait(2);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Navigate to Activities Tab
	public void openActvitiesWindow() throws IOException, SQLException {
		wait(2);
		Actions action = new Actions(driver);
		action.moveToElement(
				getElement(LocatorTypes.xpath, GuidingCareObjRepo.PreAuthorizationActivity.activitiesTab_xpath)).click()
				.build().perform();
		wait(2);
		getElement(LocatorTypes.xpath, GuidingCareObjRepo.PreAuthorizationActivity.activitiesTab_xpath).click();
	}

	public void completePreAuthActivity(String preAuth_Number) throws IOException, SQLException {
		wait(1);
		openActvitiesWindow();
		wait(1);

		try {
			int tableRows = driver
					.findElements(
							By.xpath(GuidingCareObjRepo.PreAuthorizationActivity.authorizationActivitiesTable_xpath))
					.size();
			if (tableRows < 2) {
				return;
			}

			if (tableRows == 2) {
				try {
					String noRecordsMessage = getElement(LocatorTypes.xpath,
							GuidingCareObjRepo.PreAuthorizationActivity.noRecordsMessage_xpath).getText();
					if (noRecordsMessage.trim().equalsIgnoreCase("No Records Found")) {
						return;
					}
				} catch (Exception e) {
				}
			}

			String status = "";
			for (int i = 2; i <= tableRows; i++) {
				String activityType_xpath = GuidingCareObjRepo.PreAuthorizationActivity.authorizationActivity_xpath
						+ "[" + i + "]/td[2] ";
				String activityType = driver.findElement(By.xpath(activityType_xpath)).getText();

				// Activity already completed
				String status_word_xpath = GuidingCareObjRepo.PreAuthorizationActivity.authorizationActivity_xpath + "["
						+ i + "]/td[5] ";
				elementVisibleWait(LocatorTypes.xpath, status_word_xpath, 5);
				status = driver.findElement(By.xpath(status_word_xpath)).getText();
				if (status.equalsIgnoreCase("Completed") || activityType.equalsIgnoreCase("MD Review Completed")) {
					continue;
				}

				String thumbsUp_xpath = GuidingCareObjRepo.PreAuthorizationActivity.authorizationActivity_xpath + "["
						+ i + "]/td[15]";
				try {
					if (activityType.equalsIgnoreCase("P2P Review") && !status.equalsIgnoreCase("Completed")) {
						try {
							getElement(LocatorTypes.xpath, thumbsUp_xpath).click();
							WebElement dropdownArrow = getElement(LocatorTypes.xpath,
									GuidingCareObjRepo.PreAuthorizationActivity.decision_buttonArrow_xpath);
							WebElement dropdownText = getElement(LocatorTypes.xpath,
									GuidingCareObjRepo.PreAuthorizationActivity.decision_button_text);
							selectDropdownValue("Approved", dropdownArrow, dropdownText);
							elementVisibleWait(LocatorTypes.id,
									GuidingCareObjRepo.PreAuthorizationActivity.saveAndComplete_id, 5);
							getElement(LocatorTypes.id, GuidingCareObjRepo.PreAuthorizationActivity.saveAndComplete_id)
									.click();

							reportPass("Validation Passed for Pre Auth ID: " + testData.get("PreAuth_ID")
									+ " and Pre Auth activity type: " + activityType
									+ ". Current Status is: Completed");
							return;
						} catch (Exception e) {
							reportFail("Validation Failed for Pre Auth ID: " + testData.get("PreAuth_ID")
									+ " and Pre Auth activity type: " + activityType + ". Current Status is: "
									+ status);
						}
					} else {
						getElement(LocatorTypes.xpath, thumbsUp_xpath).click();
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
				System.out.println("Number of rows: " + tableRows);
				wait(2);

				try {
					elementVisibleWait(LocatorTypes.xpath, status_word_xpath, 10);
					status = driver.findElement(By.xpath(status_word_xpath)).getText();
				} catch (Exception e) {
					e.printStackTrace();
				}

				if (status.equals("Completed")) {
					reportPass("Validation Passed for Pre Auth ID: " + testData.get("PreAuth_ID")
							+ " and Pre Auth activity type: " + activityType + ". Current Status is: " + status);
				} else {
					reportFail("Validation Failed for Pre Auth ID: " + testData.get("PreAuth_ID")
							+ " and Pre Auth activity type: " + activityType + ". Current Status is: " + status);
				}

				wait(10);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}
}
