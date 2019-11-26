package pageclasses.brokerPortal;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import enums.LocatorTypes;
import managers.PageManager;
import pageclasses.BasePage;
import pageobjects.ApplicationTrackerObjRepo;

import utils.Dbconn;

public class ApplicationTrackerPage extends BasePage {

	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(ApplicationTrackerPage.class.getName());
	Dbconn db = new Dbconn();
	PageManager pm;

	public ApplicationTrackerPage() {
		driver = getWebDriver();
	}

	public void applicationTrackerPage() {

		Actions actions = new Actions(driver);
		actions.moveToElement(getElement(LocatorTypes.xpath, ApplicationTrackerObjRepo.HomePage.planTools_xpath))
				.click(getElement(LocatorTypes.xpath, ApplicationTrackerObjRepo.HomePage.appTracker_xpath)).build()
				.perform();
		getElement(LocatorTypes.xpath, ApplicationTrackerObjRepo.AppTrackerPage.appType_xpath).click();
		getElement(LocatorTypes.id, ApplicationTrackerObjRepo.AppTrackerPage.addTypes_id).click();
		getElement(LocatorTypes.xpath, ApplicationTrackerObjRepo.AppTrackerPage.lastDays_xpath).click();
		getElement(LocatorTypes.id, ApplicationTrackerObjRepo.AppTrackerPage.searchButton_id).click();

	}

	public HashMap<String, String> applicationStatusData() {
		HashMap<String, String> testDataMap = new HashMap<String, String>();
		try {

			List<WebElement> subscribers = getElements(LocatorTypes.xpath,
					ApplicationTrackerObjRepo.AppTrackerStatus.subscriberList_xpath);
			for (int i = 1; i <= subscribers.size(); i++) {

				List<WebElement> subscribersColumns = getElements(LocatorTypes.xpath,
						ApplicationTrackerObjRepo.AppTrackerStatus.subscriberList_xpath + "[" + i + "]/td");
				testDataMap.put("NPN " + i, subscribersColumns.get(0).getText().trim());
				testDataMap.put("Application Type " + i, subscribersColumns.get(1).getText().trim());
				testDataMap.put("Contract holder name " + i, subscribersColumns.get(3).getText().trim());
				testDataMap.put("Plan Name " + i, subscribersColumns.get(6).getText().trim());
				testDataMap.put("Status ID " + i, subscribersColumns.get(7).getText().trim());
			}

			logger.info(testDataMap);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Broker application getMemberDetails failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return testDataMap;
	}

	public void validateAppStatus(String agentId) {
		List<WebElement> subscribers = getElements(LocatorTypes.xpath,
				ApplicationTrackerObjRepo.AppTrackerStatus.subscriberList_xpath);
		HashMap<String, String> testDataMem = applicationStatusData();
		int count = 0;
		try {
			String query = "SELECT AgentID, ApplicationType, MemberLName, ProductPlanName, ApplicationStatus FROM test_data_readytoenroll JOIN agent on test_data_readytoenroll.AgentID=agent.AgentTIN where AgentID ="
					+ agentId;
			List<HashMap<String, String>> list = db.getListOfHashMapsFromResultSet(query);

			for (HashMap<String, String> row : list) {
				HashMap<String, String> temptestData = new HashMap<String, String>();
				for (String key : row.keySet()) {
					temptestData.put(key, row.get(key));
				}
				for (int i = 1; i <= subscribers.size(); i++) {
					if (testDataMem.get("NPN " + i).equalsIgnoreCase(temptestData.get("AgentID"))
							&& testDataMem.get("Application Type " + i)
									.equalsIgnoreCase(temptestData.get("ApplicationType"))
							&& testDataMem.get("Contract holder name " + i).contains(temptestData.get("MemberLName"))
							&& testDataMem.get("Plan Name " + i).equalsIgnoreCase(temptestData.get("ProductPlanName"))
							&& testDataMem.get("Status ID " + i)
									.equalsIgnoreCase(temptestData.get("ApplicationStatus"))) {
						count++;
					}
				}
			}
			if (count > 0) {
				reportPass("Application Status Verified for this agentID " + agentId + " :" + testDataMem);
			} else {
				reportFail("Application Status Not Verified for this agentID " + agentId + " :" + testDataMem);
			}

		} catch (IOException e) {
			e.printStackTrace();
			try {
				reportFail("Application tracker validation Failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
