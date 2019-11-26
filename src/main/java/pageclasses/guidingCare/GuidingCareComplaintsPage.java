package pageclasses.guidingCare;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;

import enums.LocatorTypes;
import managers.PageManager;
import pageclasses.BasePage;
import pageobjects.GuidingCareObjRepo;

/**
 * @author tneha01 Test Case: To Validate all type of Complaints for given Plan
 */
public class GuidingCareComplaintsPage extends BasePage {
	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(GuidingCareComplaintsPage.class.getName());
	PageManager pm = new PageManager();

	public GuidingCareComplaintsPage() {
		driver = getWebDriver();
	}

	public boolean checkPresenceofLOB(String planId) {
		boolean flag = false;
		try {
			elementVisibleWait(LocatorTypes.id, GuidingCareObjRepo.GuidingCareComplaints.eligibiltyLOB_id, 30);
			String lobText = getElement(LocatorTypes.id, GuidingCareObjRepo.GuidingCareComplaints.eligibiltyLOB_id)
					.getText();
			logger.info("Lobtext: " + lobText);
			if (lobText.contains(planId)) {
				flag = true;
			}

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return flag;
	}

	public boolean navigateToComplaintsPage(String medicareID, String complaintType) {
		boolean navToComplPage = false;
		try {
			zoomInZoomOut("100%");
			wait(5);
			elementClickableWait(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareComplaints.complaintTab_xpath, 10);
			javaScriptClick(
					getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareComplaints.complaintTab_xpath));

			elementClickableWait(LocatorTypes.id, GuidingCareObjRepo.GuidingCareComplaints.addComplaintsButton_id, 30);
			javaScriptClick(
					getElement(LocatorTypes.id, GuidingCareObjRepo.GuidingCareComplaints.addComplaintsButton_id));

			elementClickableWait(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareComplaints.complaintTypeDD_xpath,
					10);
			getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareComplaints.complaintTypeDD_xpath).click();
			wait(1);
			List<WebElement> list = getElements(LocatorTypes.xpath,
					GuidingCareObjRepo.GuidingCareComplaints.complaintTypeDDList_xpath);
			boolean flag = selectFromDropDownList(list, complaintType);

			if (!flag) {
				reportFail("Unable to find '" + complaintType + "' option in search List");
				navToComplPage = false;
				return navToComplPage;
			}

			elementVisibleWait(LocatorTypes.id, GuidingCareObjRepo.GuidingCareComplaints.authSource_id, 10);
			if (isElementPresent(LocatorTypes.id, GuidingCareObjRepo.GuidingCareComplaints.authSource_id)) {
				navToComplPage = true;
				reportPass("Navigation to Complaints page is successful");
			} else {
				navToComplPage = false;
				reportFail("Navigation to Complaints page failed");
			}

		} catch (Exception e) {
			navToComplPage = false;
			e.printStackTrace();
			try {
				reportFail("Navigation to Complaints page failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

		return navToComplPage;
	}

	public String createComplaint(String medicareID, HashMap<String, String> dbRecord, String complaintType) {
		String complaintID = "";
		try {

			List<WebElement> list;
			// try clicking on naviateBack() button
			pm.getGuidingCareComplaintsPage().navigateToComplaintsPage(medicareID, complaintType);
			zoomInZoomOut("100%");
			wait(6);

			// enter type of cases
			getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareComplaints.typeOfCaseArrow_xpath).click();
			wait(1);
			list = getElements(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareComplaints.typeOfCaseList_xpath);
			selectFromDropDownList(list, "Concurrent");

			// enter received date
			String[] dbReceiveddateTime = dbRecord.get("RECEIVED_DATETIME").trim().split(":");
			elementClickableWait(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareComplaints.dateReceivedText_xpath,
					20);
			getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareComplaints.dateReceivedText_xpath).click();
			elementClickableWait(LocatorTypes.id, GuidingCareObjRepo.GuidingCareComplaints.dateHours_id, 20);
			getElement(LocatorTypes.id, GuidingCareObjRepo.GuidingCareComplaints.dateHours_id)
					.sendKeys(dbReceiveddateTime[0]);
			getElement(LocatorTypes.id, GuidingCareObjRepo.GuidingCareComplaints.dateMinutes_id)
					.sendKeys(dbReceiveddateTime[1]);
			getElement(LocatorTypes.id, GuidingCareObjRepo.GuidingCareComplaints.dateSeconds_id)
					.sendKeys(dbReceiveddateTime[2]);
			getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareComplaints.btnDone_xpath).click();

			// enter complaint class
			elementClickableWait(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareComplaints.complaintClassArrow_xpath,
					20);
			getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareComplaints.complaintClassArrow_xpath).click();
			wait(2);
			list = getElements(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareComplaints.complaintClassList_xpath);
			selectFromDropDownList(list, dbRecord.get("COMPLAINT_CLASS"));

			// enter complaint category
			getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareComplaints.complaintCategoryArrow_xpath)
					.click();
			wait(1);
			list = getElements(LocatorTypes.xpath,
					GuidingCareObjRepo.GuidingCareComplaints.complaintCategoryList_xpath);
			boolean classFound = selectFromDropDownList(list, dbRecord.get("COMPLAINT_CATEGORY"));
			if (!classFound) {
				// try once more to click the class name
				getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareComplaints.complaintCategoryArrow_xpath)
						.click();
				wait(3);
				list = getElements(LocatorTypes.xpath,
						GuidingCareObjRepo.GuidingCareComplaints.complaintCategoryList_xpath);
				classFound = selectFromDropDownList(list, dbRecord.get("COMPLAINT_CATEGORY"));
				if (!classFound) {
					reportFail("Expected class-" + dbRecord.get("COMPLAINT_CATEGORY") + " is NOT Found on the screen, "
							+ "Complaint cannot be created witht this data");
					return complaintID;
				}
			}

			// enter sub category
			if (!complaintType.equalsIgnoreCase("QOC")) {
				getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareComplaints.complaintSubcategoryArrow_xpath)
						.click();
				wait(2);
				list = getElements(LocatorTypes.xpath,
						GuidingCareObjRepo.GuidingCareComplaints.complaintSubcategoryList_xpath);
				selectFromDropDownList(list, "Behavioral Health");
			}

			// enter communicationType
			getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareComplaints.communicationTypeArrow_xpath)
					.click();
			wait(1);
			list = getElements(LocatorTypes.xpath,
					GuidingCareObjRepo.GuidingCareComplaints.communicationTypeList_xpath);
			selectFromDropDownList(list, "Email");

			// enter intakeDept
			getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareComplaints.intakeDeptArrow_xpath).click();
			wait(1);
			list = getElements(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareComplaints.intakeDeptList_xpath);
			selectFromDropDownList(list, "Claims");

			// enter responsibleDept
			getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareComplaints.responsibleDeptArrow_xpath).click();
			wait(1);
			list = getElements(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareComplaints.responsibleDeptList_xpath);
			selectFromDropDownList(list, "Claims");

			// enter whoIntiated
			getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareComplaints.whoIntiatedArrow_xpath).click();
			list = getElements(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareComplaints.whoIntiatedList_xpath);
			selectFromDropDownList(list, "Member");

			test.addScreenCaptureFromPath("./Screenshots/" + ExtentReportScreenshot());
			// submit the complaint
			getElement(LocatorTypes.id, GuidingCareObjRepo.GuidingCareComplaints.btnSave_id).click();

			// validate if Complaint is created successfully
			String successMsg = "";
			elementVisibleWait(LocatorTypes.id, GuidingCareObjRepo.GuidingCareComplaints.successPopUpMessage_id, 10);
			if (isElementPresent(LocatorTypes.id, GuidingCareObjRepo.GuidingCareComplaints.successPopUpMessage_id)) {
				successMsg = getElement(LocatorTypes.id,
						GuidingCareObjRepo.GuidingCareComplaints.successPopUpMessage_id).getText().trim();
			}
			logger.info("Actual Success Message-'" + successMsg
					+ "'\n Expected Success Message- 'Complaint details added successfully.'");

			if (successMsg.trim().equalsIgnoreCase("Complaint details added successfully.")) {
				reportPass("Complaint created for plan " + dbRecord.get("LOB") + " of type "
						+ dbRecord.get("COMPLAINT_CLASS") + " - " + dbRecord.get("COMPLAINT_CATEGORY")
						+ "is created successfully");
				complaintID = getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareComplaints.complaintID_xpath)
						.getAttribute("value").trim();
				complaintID = complaintID.substring(complaintID.indexOf("(") + 1, complaintID.indexOf(")"));
				logger.info("Complaint ID: " + complaintID);
			} else {
				reportFail("Complaint created for plan " + dbRecord.get("LOB") + " of type "
						+ dbRecord.get("COMPLAINT_CLASS") + " - " + dbRecord.get("COMPLAINT_CATEGORY")
						+ "is NOT created successfully");
				reportFail("Actual Success Message-'" + successMsg
						+ "'\n Expected Success Message- 'The complaint has been updated.'");
			}

		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Complaint is not created successfully");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return complaintID;
	}

	// Validate TAT
	public String validateTAT(HashMap<String, String> dbRecord) {
		String status = "";
		try {
			boolean flag1 = true;
			wait(3);
			elementClickableWait(LocatorTypes.id, GuidingCareObjRepo.GuidingCareComplaints.btnExpandComplaintDetails_id,
					20);
			getElement(LocatorTypes.id, GuidingCareObjRepo.GuidingCareComplaints.btnExpandComplaintDetails_id).click();
			wait(2);
			// validate that received date is current date
			elementVisibleWait(LocatorTypes.id, GuidingCareObjRepo.GuidingCareComplaints.dateReceived_id, 30);
			String receivedDateTxt = getElement(LocatorTypes.id,
					GuidingCareObjRepo.GuidingCareComplaints.dateReceived_id).getAttribute("value").trim();
			String dateUI = receivedDateTxt.split(" ")[0];
			String dateCurrent = getCurrentDate().split(" ")[0];
			logger.info("Actual Received Date-" + dateUI + " Expected Received Date-" + dateCurrent);
			if (!compareDatesInDifferentFormat(dateUI, dateCurrent).equalsIgnoreCase("0"))
				flag1 = false;

			// validate that due date is after TAT_TIME days/hours from received date
			String dueDateTxt = getElement(LocatorTypes.id, GuidingCareObjRepo.GuidingCareComplaints.hiddendateDue_id)
					.getAttribute("value").trim();
			String expectedDueDate = getDateAfterDays(receivedDateTxt, dbRecord.get("TAT_TIME"),
					dbRecord.get("TAT_TIME_TYPE"));
			logger.info("Actual Due Date-" + dueDateTxt + " Expected Due Date-" + expectedDueDate);
			logger.info("time" + dbRecord.get("TAT_TIME") + " time type-" + dbRecord.get("TAT_TIME_TYPE"));
			if (!dueDateTxt.equalsIgnoreCase(expectedDueDate))
				flag1 = false;

			// To forcefully change property of a hidden element and access it
			WebElement daysUntilDecision = getElement(LocatorTypes.xpath,
					GuidingCareObjRepo.GuidingCareComplaints.daysUntilDecision_xpath);
			String daysUntilDecisionTxt = enableElementAndGetText(daysUntilDecision);

			String expectedDaysUntilDecision = compareDatesInDifferentFormat(dateCurrent,
					expectedDueDate.split(" ")[0]);
			logger.info("Actual daysUntilDecision-" + daysUntilDecisionTxt + " Expected daysUntilDecision-"
					+ expectedDaysUntilDecision);
			if (!daysUntilDecisionTxt.equalsIgnoreCase(expectedDaysUntilDecision))
				flag1 = false;

			((JavascriptExecutor) driver).executeScript("scroll(0,100)");
			test.addScreenCaptureFromPath("./Screenshots/" + ExtentReportScreenshot());
			if (flag1) {
				status = "PASS";
				logger.info("Complaint TAT Validation for plan " + dbRecord.get("LOB") + " of type "
						+ dbRecord.get("COMPLAINT_CLASS") + " - " + dbRecord.get("COMPLAINT_CATEGORY")
						+ " has expected Turnaround Time(TAT)");
				reportPass("Complaint TAT Validation for plan " + dbRecord.get("LOB") + " of type "
						+ dbRecord.get("COMPLAINT_CLASS") + " - " + dbRecord.get("COMPLAINT_CATEGORY")
						+ " has expected Turnaround Time(TAT)");
			} else {
				status = "FAIL";
				logger.info("Complaint TAT Validation for plan " + dbRecord.get("LOB") + " of type "
						+ dbRecord.get("COMPLAINT_CLASS") + " - " + dbRecord.get("COMPLAINT_CATEGORY")
						+ " does NOT have expected Turnaround Time(TAT)");
				reportFail("Complaint TAT Validation for plan " + dbRecord.get("LOB") + " of type "
						+ dbRecord.get("COMPLAINT_CLASS") + " - " + dbRecord.get("COMPLAINT_CATEGORY")
						+ " does NOT have expected Turnaround Time(TAT)");
				reportFail("Actual Due Date-" + dueDateTxt + "\n Expected Due Date-" + expectedDueDate + "\n"
						+ "Actual daysUntilDecision-" + daysUntilDecisionTxt + "\n Expected daysUntilDecision-"
						+ expectedDaysUntilDecision);
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Complaint-TAT is not validated successfully");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			status = "FAIL";
		}
		logger.info("Status: " + status);
		return status;
	}

	public boolean expediteExistingStandardComplaint(String medicareID, HashMap<String, String> dbRecord) {
		boolean complaint_created = false;
		try {

			wait(3);
			List<WebElement> list;
			// enter complaint class
			elementClickableWait(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareComplaints.complaintClassArrow_xpath,
					20);
			getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareComplaints.complaintClassArrow_xpath).click();
			wait(1);
			list = getElements(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareComplaints.complaintClassList_xpath);
			selectFromDropDownList(list, dbRecord.get("COMPLAINT_CLASS"));

			// enter complaint category
			getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareComplaints.complaintCategoryArrow_xpath)
					.click();
			wait(2);
			list = getElements(LocatorTypes.xpath,
					GuidingCareObjRepo.GuidingCareComplaints.complaintCategoryList_xpath);
			selectFromDropDownList(list, dbRecord.get("COMPLAINT_CATEGORY"));

			test.addScreenCaptureFromPath("./Screenshots/" + ExtentReportScreenshot());
			// submit the complaint
			getElement(LocatorTypes.id, GuidingCareObjRepo.GuidingCareComplaints.btnSave_id).click();

			// validate if Complaint is created successfully
			String successMsg = "";
			boolean flag1 = false;
			if (isElementPresent(LocatorTypes.id, GuidingCareObjRepo.GuidingCareComplaints.successPopUpMessage_id)) {
				successMsg = getElement(LocatorTypes.id,
						GuidingCareObjRepo.GuidingCareComplaints.successPopUpMessage_id).getText().trim();
				logger.info("Actual Success Message-'" + successMsg
						+ "'\n Expected Success Message- 'The complaint has been updated.'");
				if (successMsg.equalsIgnoreCase("The complaint has been updated."))
					flag1 = true;
			}

			if (flag1) {
				complaint_created = true;
				reportPass("Complaint Expedited for plan " + dbRecord.get("LOB") + " of type "
						+ dbRecord.get("COMPLAINT_CLASS") + " - " + dbRecord.get("COMPLAINT_CATEGORY")
						+ "is created successfully");
			} else {
				complaint_created = false;
				reportFail("Complaint NOT Expedited for plan " + dbRecord.get("LOB") + " of type "
						+ dbRecord.get("COMPLAINT_CLASS") + " - " + dbRecord.get("COMPLAINT_CATEGORY")
						+ "is NOT created successfully");
				reportFail("Actual Success Message-'" + successMsg
						+ "'\n Expected Success Message- 'The complaint has been updated.'");
			}

		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Complaint is not created successfully");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return complaint_created;
	}

	public String getDateAfterDays(String oldDate, String time, String time_type) {
		try {
			SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm:ss");
			Calendar cal = Calendar.getInstance();
			cal.setTime(sdf.parse(oldDate));
			if (time_type.equalsIgnoreCase("DAYS"))
				cal.add(Calendar.DATE, Integer.parseInt(time) - 1); // including current day
			else if (time_type.equalsIgnoreCase("HOURS"))
				cal.add(Calendar.HOUR, Integer.parseInt(time));

			String newDate = sdf.format(cal.getTime());
			return newDate;

		} catch (ParseException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return "";
	}

	public boolean selectFromDropDownList(List<WebElement> list, String textTobeSelected) {
		boolean flag = false;
		int i = 0;
		for (i = 0; i < list.size(); i++) {
			if (list.get(i).getText().trim().equalsIgnoreCase(textTobeSelected)) {
				list.get(i).click();
				flag = true;
				break;
			}
		}
		if (i == list.size()) {
			flag = false;
		}
		return flag;
	}

}
