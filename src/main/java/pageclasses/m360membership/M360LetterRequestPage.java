package pageclasses.m360membership;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import enums.LocatorTypes;
import managers.PageManager;
import pageclasses.BasePage;
import pageobjects.M360MembershipObjRepo;
import utils.Dbconn;

public class M360LetterRequestPage extends BasePage {

	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(M360LetterRequestPage.class.getName());
	PageManager pm = new PageManager();
	Dbconn db = new Dbconn();

	public M360LetterRequestPage() {
		driver = getWebDriver();
	}

	public void closeWindow() {
		try {
			getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360LetterRequest.CloseTab_xpath).click();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Closing of window failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void navigateToLetterRequestPage() {
		try {
			getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360LetterRequest.letterRequestTab_xpath).click();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("M360MemberPage navigate to letter request page failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void navigateToApplicationEntryPage() {
		try {
			getElement(LocatorTypes.xpath,
					M360MembershipObjRepo.M360LetterRequest.applicationEntryOnLetterRequestTab_xpath).click();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("M360MemberPage navigate to application entry page failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public boolean searchLetterRequestMemberId(String memberId) {
		boolean flag = false;
		try {
			flag = isElementPresent(LocatorTypes.xpath,
					M360MembershipObjRepo.M360LetterRequest.letterRequestSourceDropdown_xpath);
			if (flag) {
				getElement(LocatorTypes.xpath,
						M360MembershipObjRepo.M360LetterRequest.letterRequestSourceDropdown_xpath).click();
				getElement(LocatorTypes.xpath,
						M360MembershipObjRepo.M360LetterRequest.letterRequestSourceDropdownSearchField_xpath)
								.sendKeys("Member");
				getElement(LocatorTypes.xpath,
						M360MembershipObjRepo.M360LetterRequest.letterRequestSourceDropdownSearchField_xpath)
								.sendKeys(Keys.ENTER);
				getElement(LocatorTypes.xpath,
						M360MembershipObjRepo.M360LetterRequest.letterRequestMemberIdTextField_xpath).clear();
				getElement(LocatorTypes.xpath,
						M360MembershipObjRepo.M360LetterRequest.letterRequestMemberIdTextField_xpath)
								.sendKeys(memberId);
				getElement(LocatorTypes.id, M360MembershipObjRepo.M360LetterRequest.memberSearchGoButton_id).click();
			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			try {
				reportFail("M360MemberPage letter request member search failed - " + memberId);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}

	public boolean tableItemResult(int nodeCount) {
		boolean isClosed = false;
		try {
			List<WebElement> letterRequestList = getElements(LocatorTypes.xpath,
					M360MembershipObjRepo.M360LetterRequest.letterRequestTblSearchResultsList_xpath);
			if (letterRequestList.get(nodeCount).getText().toUpperCase().contains("CLOSED") && (letterRequestList
					.get(nodeCount).getText().toUpperCase().contains("OEV LETTER")
					|| letterRequestList.get(nodeCount).getText().toUpperCase().contains("CONFIRM ENROLLMENT"))) {
				isClosed = true;
			} else {
				isClosed = false;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return isClosed;
	}

	public int selectTableSearchResultsList(int nodeCount) {
		int listSize = 0;
		try {
			List<WebElement> letterRequestList = getElements(LocatorTypes.xpath,
					M360MembershipObjRepo.M360LetterRequest.letterRequestTblSearchResultsList_xpath);
			listSize = letterRequestList.size();
			if (letterRequestList.get(nodeCount).getText().toUpperCase().contains("CLOSED")) {
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

	public HashMap<String, String> getLetterRequestDetails() {
		HashMap<String, String> memberLetterRequest = new HashMap<String, String>();
		try {
			String letterRequestFirstName = getElement(LocatorTypes.id,
					M360MembershipObjRepo.M360LetterRequest.letterRequestFirstName_id).getText();
			String letterRequestLastName = getElement(LocatorTypes.id,
					M360MembershipObjRepo.M360LetterRequest.letterRequestLastName_id).getText();
			String letterRequestPlan = getElement(LocatorTypes.xpath,
					M360MembershipObjRepo.M360LetterRequest.letterRequestPlan_xpath).getText();
			String letterRequestPBP = getElement(LocatorTypes.xpath,
					M360MembershipObjRepo.M360LetterRequest.letterRequestPBP_xpath).getText();
			String letterRequestDesignation = getElement(LocatorTypes.xpath,
					M360MembershipObjRepo.M360LetterRequest.letterRequestDesignation_xpath).getText();
			String letterRequestEffectiveDate = getElement(LocatorTypes.xpath,
					M360MembershipObjRepo.M360LetterRequest.letterRequestEffectiveDate_xpath).getText();
			letterRequestEffectiveDate = getDateMMDDYYYYInSqlFormat(letterRequestEffectiveDate);
			memberLetterRequest.put("FirstName", letterRequestFirstName);
			memberLetterRequest.put("LastName", letterRequestLastName);
			memberLetterRequest.put("PlanID", letterRequestPlan);
			memberLetterRequest.put("PBPID", letterRequestPBP);
			memberLetterRequest.put("PlanDesignation", letterRequestDesignation);
			memberLetterRequest.put("EffectiveStartDate", letterRequestEffectiveDate);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Retrieval of letter request data failed.");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return memberLetterRequest;
	}

	public boolean validateLetterRequestDetails() {
		boolean flag = false;
		int counter = 0;
		try {
			int listSize = 0;
			boolean isClosed;
			String firstNameValue = getElement(LocatorTypes.id,
					M360MembershipObjRepo.M360LetterRequest.letterRequestFirstName_id).getText();
			if (firstNameValue.trim().equals("")) {
				flag = false;
			} else {
				listSize = selectTableSearchResultsList(0);
				for (int indexCount = 0; indexCount < listSize; indexCount++) {
					isClosed = tableItemResult(indexCount);
					if (indexCount == listSize - 1 || counter == 2) {
						break;
					} else {
						if (isClosed == true) {
							counter++;
							selectTableSearchResultsList(indexCount);
							HashMap<String, String> memLetterRequestDetails = getLetterRequestDetails();
							flag = compareHashMaps(memLetterRequestDetails, testData);
							if (flag) {
								reportPass("Validation of letter request data is successful.\r\n" + "UI: "
										+ memLetterRequestDetails);
							} else {
								reportFail("Validation of letter request data failed.\r\n" + "UI: "
										+ memLetterRequestDetails + "\r\n" + "DB: " + testData);
								break;
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
				reportFail("validateLetterRequestDetails method failed. TestData: " + testData);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}
}
