package pageclasses.m360membership;

import java.io.IOException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.support.ui.Select;

import enums.LocatorTypes;
import managers.PageManager;
import pageclasses.BasePage;
import pageobjects.M360MembershipObjRepo;
import utils.Dbconn;

public class M360LetterReviewPage extends BasePage {

	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(M360LetterReviewPage.class.getName());
	PageManager pm = new PageManager();
	Dbconn db = new Dbconn();

	public M360LetterReviewPage() {
		driver = getWebDriver();
	}

	public void navigateToLetterReviewPage() {
		try {
			getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360LetterReview.letterReviewTab_xpath).click();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("M360MemberPage navigate to Letter Review page failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void navigateToApplicationEntryPage() {
		try {
			getElement(LocatorTypes.xpath,
					M360MembershipObjRepo.M360LetterReview.applicationEntryOnLetterReviewTab_xpath).click();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("M360MemberPage navigate to application entry page failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public boolean searchLetterReviewMemberId(String memberId) {
		boolean flag = false;
		try {
			flag = isElementPresent(LocatorTypes.xpath,
					M360MembershipObjRepo.M360LetterReview.letterReviewSourceDropdown_xpath);
			if (flag) {
				getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360LetterReview.letterReviewSourceDropdown_xpath)
						.click();
				getElement(LocatorTypes.xpath,
						M360MembershipObjRepo.M360LetterReview.letterReviewSourceDropdownSearchField_xpath)
								.sendKeys("Member");
				getElement(LocatorTypes.xpath,
						M360MembershipObjRepo.M360LetterReview.letterReviewSourceDropdownSearchField_xpath)
								.sendKeys(Keys.ENTER);
				getElement(LocatorTypes.xpath,
						M360MembershipObjRepo.M360LetterReview.letterReviewMemberIdTextField_xpath).clear();
				getElement(LocatorTypes.xpath,
						M360MembershipObjRepo.M360LetterReview.letterReviewMemberIdTextField_xpath).sendKeys(memberId);
				getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360LetterReview.memberSearchGoButton_xpath)
						.click();
			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			try {
				reportFail("M360MemberPage letter review member search failed - " + memberId);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}

	public void selectTableSearchResultsList() {
		try {
			Integer count = 0;
			String letterReviewItem = "";
			DateTimeFormatter dtf1 = DateTimeFormatter.ofPattern("MM/dd/yyyy");
			DateTimeFormatter dtf2 = DateTimeFormatter.ofPattern("yyyy-MM-dd");

			List<WebElement> letterReviewList = getElements(LocatorTypes.xpath,
					M360MembershipObjRepo.M360LetterReview.letterReviewTblSearchResultsList_xpath);
			while (count < letterReviewList.size()) {
				int index;
				letterReviewItem = letterReviewList.get(count).getText();
				if (letterReviewItem.contains("/20")) {
					index = letterReviewItem.indexOf("/20");
					letterReviewItem = letterReviewItem.substring(index - 5, index + 5).trim();
					LocalDate itemDate = LocalDate.parse(letterReviewItem, dtf1);
					LocalDate currentDate = LocalDate.parse(getCurrentDate(), dtf2);
					if (currentDate.minusDays(30).compareTo(itemDate) < 0) {
						letterReviewList.get(count).click();
						break;
					}
				} else {
					count++;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("M360MemberPage unable to click on item in the table search results for Letter Review.");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public HashMap<String, String> getLetterReviewDetails(int nodeCount) {
		HashMap<String, String> memberLetterReview = new HashMap<String, String>();
		try {
			int startIndexString;
			int underscoreIndex;
			String letterName = "";

			List<WebElement> letterNames = getElements(LocatorTypes.xpath,
					M360MembershipObjRepo.M360LetterReview.letterReviewTblSearchResultsList_xpath);

			letterName = letterNames.get(nodeCount).getText();
			startIndexString = letterName.indexOf("/20");
			underscoreIndex = letterName.indexOf("_");
			letterName = letterName.substring(startIndexString + 6, underscoreIndex).trim();

			Select status = new Select(
					getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360LetterReview.letterReviewStatus_xpath));
			String letterReviewStatus = status.getFirstSelectedOption().getText();
			String letterReviewTableFrame = getElement(LocatorTypes.xpath,
					M360MembershipObjRepo.M360LetterReview.letterReviewTableFrame_xpath).getText();
			String firstName = "";
			String middleName = "";
			String lastName = "";
			String planName = "";
			String memberId = "";
			String rxid = "";
			String rxgrp = "";
			String rxbin = "";
			String rxpcn = "";
			String effectiveEnrollmentDate = "";

			int beginIndex;
			String[] tableArray = letterReviewTableFrame.split("\\r?\\n");

			beginIndex = tableArray[2].lastIndexOf("  ");
			firstName = tableArray[2].substring(beginIndex, tableArray[2].length()).trim();

			beginIndex = tableArray[3].lastIndexOf("  ");
			middleName = tableArray[3].substring(beginIndex, tableArray[3].length()).trim();

			beginIndex = tableArray[4].lastIndexOf("  ");
			lastName = tableArray[4].substring(beginIndex, tableArray[4].length()).trim();

			if (!letterReviewTableFrame.toUpperCase().contains("UNCOVERED MONTH")) {
				beginIndex = tableArray[10].lastIndexOf("  ");
				planName = tableArray[10].substring(beginIndex, tableArray[10].length()).trim();
			}

			if (letterReviewTableFrame.contains("PCP") && letterReviewTableFrame.toUpperCase().contains("SPECIALIST")) {
				if (!letterReviewTableFrame.toUpperCase().contains("IN NETWORK COPAY")) {
					beginIndex = tableArray[15].lastIndexOf("  ");
					memberId = tableArray[15].substring(beginIndex, tableArray[15].length()).trim();
				} else {
					beginIndex = tableArray[17].lastIndexOf("  ");
					memberId = tableArray[17].substring(beginIndex, tableArray[17].length()).trim();
				}
			} else if (letterReviewTableFrame.toUpperCase().contains("RXID")) {
				beginIndex = tableArray[11].lastIndexOf("  ");
				memberId = tableArray[11].substring(beginIndex, tableArray[11].length()).trim();

				beginIndex = tableArray[12].lastIndexOf("  ");
				rxid = tableArray[12].substring(beginIndex, tableArray[12].length()).trim();

				beginIndex = tableArray[13].lastIndexOf("  ");
				rxgrp = tableArray[13].substring(beginIndex, tableArray[13].length()).trim();

				beginIndex = tableArray[14].lastIndexOf("  ");
				rxbin = tableArray[14].substring(beginIndex, tableArray[14].length()).trim();

				beginIndex = tableArray[15].lastIndexOf("  ");
				rxpcn = tableArray[15].substring(beginIndex, tableArray[15].length()).trim();

				beginIndex = tableArray[16].lastIndexOf("  ");
				effectiveEnrollmentDate = tableArray[16].substring(beginIndex, tableArray[16].length()).trim();
				effectiveEnrollmentDate = getLetterEffectiveDateInYYYYMMDD(effectiveEnrollmentDate);
			}
			memberLetterReview.put("letterName", letterName);
			memberLetterReview.put("letterReviewStatus", letterReviewStatus);
			memberLetterReview.put("FirstName", firstName);
			memberLetterReview.put("MiddleInitial", middleName);
			memberLetterReview.put("LastName", lastName);
			memberLetterReview.put("PlanID", planName);
			memberLetterReview.put("SupplementalID", memberId);
			memberLetterReview.put("RXID", rxid);
			memberLetterReview.put("PrimaryRxGroup", rxgrp);
			memberLetterReview.put("PrimaryBin", rxbin);
			memberLetterReview.put("PrimaryPCN", rxpcn);
			memberLetterReview.put("EffectiveStartDate", effectiveEnrollmentDate);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Retrieval of letter review data failed.");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return memberLetterReview;
	}

	public boolean tableItemResult(int nodeCount) {
		boolean isCompleted = false;
		try {
			List<WebElement> letterRequestList = getElements(LocatorTypes.xpath,
					M360MembershipObjRepo.M360LetterReview.letterReviewTblSearchResultsList_xpath);

			String letterReviewTableFrame = getElement(LocatorTypes.xpath,
					M360MembershipObjRepo.M360LetterReview.letterReviewTableFrame_xpath).getText();
			if (letterRequestList.get(nodeCount).getText().toUpperCase().contains("COMPLETED")
					&& (letterRequestList.get(nodeCount).getText().toUpperCase().contains("OEV LETTER")
							|| letterRequestList.get(nodeCount).getText().toUpperCase().contains("CONFIRM ENROLLMENT"))
					&& !(letterRequestList.get(nodeCount).getText().toUpperCase().contains("EXTRACTED"))
					&& letterReviewTableFrame.toUpperCase().contains("LANGUAGE CODE  ENG")) {
				isCompleted = true;
			} else {
				isCompleted = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isCompleted;
	}

	public int selectTableSearchResultsList(int nodeCount) {
		int listSize = 0;
		try {
			List<WebElement> letterRequestList = getElements(LocatorTypes.xpath,
					M360MembershipObjRepo.M360LetterReview.letterReviewTblSearchResultsList_xpath);

			listSize = letterRequestList.size();
			if (letterRequestList.get(nodeCount).getText().toUpperCase().contains("COMPLETED")) {
				letterRequestList.get(nodeCount).click();
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("M360MemberPage unable to click on item in the table search results for Letter Request.");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return listSize;
	}

	public String getLetterEffectiveDateInYYYYMMDD(String date) {
		try {
			String[] splitdate = date.split(",");
			String modifiedDate = splitdate[0].trim() + splitdate[1];
			Date finalDate = new SimpleDateFormat("MMM dd yyyy").parse(modifiedDate);
			date = new SimpleDateFormat("yyyy-MM-dd").format(finalDate);
		} catch (ParseException e) {
			e.printStackTrace();
		}
		return date;
	}

	public boolean validateLetterReviewDetails() {
		boolean flag = false;
		try {
			int listSize = 0;
			boolean isCompleted;
			boolean elementPresence = isElementPresent(LocatorTypes.xpath,
					M360MembershipObjRepo.M360LetterReview.letterReviewTableFrame_xpath);
			if (elementPresence == false) {
				flag = false;
			} else {
				listSize = selectTableSearchResultsList(0);
				for (int indexCount = 0; indexCount < listSize; indexCount++) {
					isCompleted = tableItemResult(indexCount);
					if (indexCount == listSize - 1) {
						break;
					} else {
						if (isCompleted == true) {
							selectTableSearchResultsList(indexCount);
							HashMap<String, String> memLetterReviewDetails = getLetterReviewDetails(indexCount);
							flag = compareHashMaps(memLetterReviewDetails, testData);
							if (flag) {
								reportPass("Validation of letter review data is successful.\r\n" + "UI: "
										+ memLetterReviewDetails.get("letterName") + " - " + memLetterReviewDetails);
							} else {
								reportFail("Validation of letter review data failed.\r\n" + "UI: "
										+ memLetterReviewDetails.get("letterName") + " - " + memLetterReviewDetails
										+ "\r\n" + "DB: " + testData);
							}
						} else {
							flag = false;
						}
					}
				}
			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			try {
				reportFail("validateLetterReviewDetails method failed. Test Data: " + testData);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}
}
