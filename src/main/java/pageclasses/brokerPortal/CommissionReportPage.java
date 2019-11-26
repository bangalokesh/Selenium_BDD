package pageclasses.brokerPortal;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import enums.LocatorTypes;
import pageclasses.BasePage;
import pageobjects.CommissionReportObjRepo;
import utils.Dbconn;

public class CommissionReportPage extends BasePage {

	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(CommissionReportPage.class.getName());
	Dbconn db = new Dbconn();

	public CommissionReportPage() {
		driver = getWebDriver();
	}

	public void navigateCommissionReportPage() {
		Actions actions = new Actions(driver);
		actions.moveToElement(getElement(LocatorTypes.xpath, CommissionReportObjRepo.HomePage.reportingTab_xpath))
				.click(getElement(LocatorTypes.xpath, CommissionReportObjRepo.HomePage.commissionReport_xpath)).build()
				.perform();
		getElement(LocatorTypes.id, CommissionReportObjRepo.HomePage.runReportbtn_id).click();
	}

	public HashMap<String, String> searchCommissionReportPage() {
		String medicare = null;
		HashMap<String, String> testDataMap = new HashMap<String, String>();
		List<WebElement> headers = getElements(LocatorTypes.className,
				CommissionReportObjRepo.MedicareCommisssionReport.medicareHeaders_className);
		for (int i = 0; i < headers.size(); i++) {
			if (headers.get(i).getText().equals("Medicare Advantage")) {
				medicare = headers.get(i).getText();
				try {

					List<WebElement> subscribers = getElements(LocatorTypes.xpath,
							CommissionReportObjRepo.MedicareCommisssionReport.subscriberList_xpath);
					for (int j = 2; j <= subscribers.size() - 2; j++) {

						List<WebElement> subscribersColumns = getElements(LocatorTypes.xpath,
								CommissionReportObjRepo.MedicareCommisssionReport.subscriberList_xpath + "[" + j
										+ "]/td");
						testDataMap.put("Broker Name ", subscribersColumns.get(0).getText().trim());
						testDataMap.put("TMGID " + j, subscribersColumns.get(1).getText().trim());
						testDataMap.put("Subscriber Name " + j, subscribersColumns.get(2).getText().trim());
						testDataMap.put("Fixed Amount " + j, subscribersColumns.get(7).getText().trim());
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
				continue;
			}
		}
		System.out.println(medicare);
		if (medicare.equals("Medicare Advantage")) {
			System.out.println("Medicare Advantage table is present for this broker id");
		} else {
			System.out.println("Medicare Advantage table is not present for this broker id");
		}
		return testDataMap;
	}

	public void validateMedicareCommissionReport() {
		HashMap<String, String> testDataMem = searchCommissionReportPage();
		String query = "select SupplementalID from member_demographic";
		int count = 0;
		try {
			List<WebElement> subscribers = getElements(LocatorTypes.xpath,
					CommissionReportObjRepo.MedicareCommisssionReport.subscriberList_xpath);
			List<HashMap<String, String>> list = db.getListOfHashMapsFromResultSet(query);

			for (HashMap<String, String> row : list) {
				HashMap<String, String> temptestData = new HashMap<String, String>();
				for (String key : row.keySet()) {
					temptestData.put(key, row.get(key));
				}
				for (int j = 2; j <= subscribers.size() - 2; j++) {
					if (// testDataMem.get("Broker Name
						// ").equalsIgnoreCase(temptestData.get("SubscriberID"))&&
					testDataMem.get("TMGID " + j).equals(temptestData.get("SupplementalID"))) {
						count++;
					}
				}
			}
			if (count > 0) {
				reportPass("Broker commission report has supplementalID ");
			} else {
				reportFail("Broker commission report does not have supplementalID's ");
			}

		} catch (IOException e) {
			e.printStackTrace();
			try {
				reportFail("Broker Commission report validateMembers Failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
