package pageclasses.guidingCare;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import com.aventstack.extentreports.Status;

import enums.LocatorTypes;
import managers.PageManager;
import pageclasses.BasePage;
import pageobjects.GuidingCareObjRepo;
import utils.AccessDbconn;
import utils.Dbconn;

public class GuidingCareProviderPage extends BasePage {

	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(GuidingCareProviderPage.class.getName());
	PageManager pm = new PageManager();
	Dbconn db = new Dbconn();
	AccessDbconn accessDb = new AccessDbconn();

	public GuidingCareProviderPage() {
		driver = getWebDriver();
	}

	public boolean searchProviderCode(String providerCode, String networkName) {
		boolean flag = false;
		try {
			zoomInZoomOut("100%");
			getElement(LocatorTypes.id, GuidingCareObjRepo.GuidingCareProvider.providerCodeTextField_id).clear();
			getElement(LocatorTypes.id, GuidingCareObjRepo.GuidingCareProvider.providerCodeTextField_id)
					.sendKeys(providerCode.replace(' ', '%'));
			Select network = new Select(
					getElement(LocatorTypes.id, GuidingCareObjRepo.GuidingCareProvider.networkNameTextField_id));
			network.selectByVisibleText(networkName);
			wait(2);
			getElement(LocatorTypes.id, GuidingCareObjRepo.GuidingCareProvider.searchButton_id).click();

			boolean searchResult = isElementPresent(LocatorTypes.xpath,
					GuidingCareObjRepo.GuidingCareProvDetails.providerList_xpath);
			if (searchResult == true) {
				flag = true;
				reportPass("Search result found in GuidingCare provider for " + providerCode);
			} else {
				flag = false;
				reportFail("No search results found in GuidingCare provider for " + providerCode);
			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			try {
				reportFail("GuidingCare provider search failed.");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}

	public HashMap<String, String> emptyHashMapForGCProvider() {
		HashMap<String, String> testHashMap = new HashMap<String, String>();

		testHashMap.put("Provider Name", "");
		testHashMap.put("Provider Type", "");
		testHashMap.put("Provider NPI", "");
		testHashMap.put("Tax ID", "");
		testHashMap.put("Address", "");
		testHashMap.put("Phone", "");
		testHashMap.put("StartDate", "");
		testHashMap.put("EndDate", "");
		testHashMap.put("Provider Gender", "");
		testHashMap.put("Speciality", "");
		testHashMap.put("Accepting Patients", "");
		testHashMap.put("LanguageSpoken", "");
		testHashMap.put("benefitNetwork", "");

		return testHashMap;
	}

	public HashMap<String, String> retrieveProviderDetails(String benNetwork) {
		HashMap<String, String> testDataMap = new HashMap<String, String>();
		try {
			Actions actions = new Actions(driver);
			wait(5);
			boolean providerCodeExists = isElementPresent(LocatorTypes.xpath,
					GuidingCareObjRepo.GuidingCareProvDetails.providerID_xpath);
			if (providerCodeExists == true) {
				String providerCodeUI = "";
				/*
				 * Try 20 attempts to make sure the providerCode from the UI and DB match If
				 * unable to retrieve matching providerCode after 20 attempts, set values to
				 * empty
				 */
				for (int i = 1; i <= 20; i++) {
					providerCodeUI = getElement(LocatorTypes.xpath,
							GuidingCareObjRepo.GuidingCareProvDetails.providerID_xpath).getText().trim();
					if (!providerCodeUI.equals(testData.get("PROVIDER_ID").trim())) {
						wait(2);
					} else if (i == 20) {
						testDataMap = emptyHashMapForGCProvider();
						return testDataMap;
					} else {
						break;
					}
				}
			} else {
				testDataMap = emptyHashMapForGCProvider();
				return testDataMap;
			}

			List<WebElement> providerInfo = getElements(LocatorTypes.xpath,
					GuidingCareObjRepo.GuidingCareProvDetails.providerList_xpath + "[1]/td");
			testDataMap.put("Provider Name", providerInfo.get(2).getText().trim());
			testDataMap.put("Provider Type", providerInfo.get(3).getText().trim());
			testDataMap.put("Provider NPI", providerInfo.get(5).getText().trim());
			testDataMap.put("Tax ID", providerInfo.get(6).getText().trim());
			testDataMap.put("Address", providerInfo.get(7).getText().trim());
			testDataMap.put("Phone", providerInfo.get(8).getText().trim());
			testDataMap.put("StartDate", providerInfo.get(11).getText().trim());
			testDataMap.put("EndDate", providerInfo.get(12).getText().trim());

			elementVisibleWait(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareProvDetails.providerLink_xpath, 30);
			actions.moveToElement(
					getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareProvDetails.providerLink_xpath))
					.click().perform();
			elementVisibleWait(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareProvDetails.provInfoHeader_xpath, 30);

			String path = ExtentReportScreenshot();
			test.addScreenCaptureFromPath(path);

			zoomInZoomOut("80%");

			boolean genderExists = isElementPresent(LocatorTypes.xpath,
					GuidingCareObjRepo.GuidingCareProvDetails.provGender_xpath);
			if (genderExists == true) {
				testDataMap.put("Provider Gender",
						getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareProvDetails.provGender_xpath)
								.getText().trim());
			} else {
				testDataMap.put("Provider Gender", "");
			}

			boolean specialityExists = isElementPresent(LocatorTypes.xpath,
					GuidingCareObjRepo.GuidingCareProvDetails.speciality_xpath);
			if (specialityExists) {
				testDataMap.put("Speciality",
						getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareProvDetails.speciality_xpath)
								.getText().trim());
			} else {
				testDataMap.put("Speciality", "");
			}

			boolean acceptingPatientsExists = isElementPresent(LocatorTypes.xpath,
					GuidingCareObjRepo.GuidingCareProvDetails.acceptPatients_xpath);
			if (acceptingPatientsExists) {
				testDataMap.put("Accepting Patients",
						getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareProvDetails.acceptPatients_xpath)
								.getText().trim());
			} else {
				testDataMap.put("Accepting Patients", "");
			}

			boolean languageSpokenExists = isElementPresent(LocatorTypes.xpath,
					GuidingCareObjRepo.GuidingCareProvDetails.languageSpoken_xpath);
			if (languageSpokenExists == true) {
				testDataMap.put("LanguageSpoken",
						getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareProvDetails.languageSpoken_xpath)
								.getText().trim());
			} else {
				testDataMap.put("LanguageSpoken", "");
			}

			boolean lobPlan1Exists = isElementPresent(LocatorTypes.xpath,
					GuidingCareObjRepo.GuidingCareProvDetails.lob1_xpath);

			if (lobPlan1Exists == true) {
				List<WebElement> benefitNetworks = getElements(LocatorTypes.xpath,
						GuidingCareObjRepo.GuidingCareProvDetails.benefitNetworks_xpath);

				outerloop: for (int i = 0; i < benefitNetworks.size(); i++) {
					String bn = benefitNetworks.get(i).getText();
					if (bn.contains(benNetwork)) {
						testDataMap.put("benefitNetwork", bn);
						break outerloop;
					} else {
						testDataMap.put("benefitNetwork", "");
					}
				}
			} else {
				testDataMap.put("benefitNetwork", "");
			}

			WebElement el = getElement(LocatorTypes.xpath, GuidingCareObjRepo.GuidingCareProvDetails.closePopup_xpath);
			JavascriptExecutor exec = (JavascriptExecutor) driver;
			exec.executeScript("arguments[0].click()", el);
			// driver.findElement(By.cssSelector("a[id='closePopUpUM']")).click();
			// getElement(LocatorTypes.xpath,
			// GuidingCareObjRepo.GuidingCareProvDetails.closePopup_xpath).click();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("GuidingCare application retrieveProviderDetails failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return testDataMap;
	}

	public List<Integer> findWord(String text, String word) {
		List<Integer> indexList = new ArrayList<Integer>();

		int index = 0;
		while (index != -1) {
			index = text.indexOf(word, index);
			if (index != -1) {
				indexList.add(index);
				index++;
			}
		}
		return indexList;
	}

	public void updateRecordFromProviderTable(String status) {
		String updateProviderData = "";

		String SelectProviderCode = "SELECT PROVIDER_ID \r\n"
				+ " FROM [VelocityTestAutomation].[dbo].[GuidingCare_Provider] \r\n" + " WHERE PROVIDER_ID = " + "'"
				+ testData.get("PROVIDER_ID") + "' \r\n" + " AND BENEFIT_NETWORK = " + "'"
				+ testData.get("BENEFIT_NETWORK") + "';";

		boolean provider = db.isResultSetEmpty(SelectProviderCode);

		if (provider == true) {
			updateProviderData = "UPDATE [VelocityTestAutomation].[dbo].[GuidingCare_Provider] \r\n" + " SET STATUS = "
					+ "'" + status + "'" + "\r\n" + " WHERE PROVIDER_ID = " + "'" + testData.get("PROVIDER_ID")
					+ "' \r\n" + " AND BENEFIT_NETWORK = " + "'" + testData.get("BENEFIT_NETWORK") + "';";
			db.sqlUpdate(updateProviderData);
		}
	}

	public void insertOrUpdate_Row_From_Provider_Validation_Table(String columnName) {
		String providerID_BenNetwork = testData.get("PROVIDER_ID").trim() + "_"
				+ testData.get("BENEFIT_NETWORK").trim();

		String SelectProviderCode = "SELECT ID \r\n"
				+ " FROM [VelocityTestAutomation].[dbo].[GuidingCare_Provider_Validation]\r\n" + " WHERE PROVIDER_ID = "
				+ "'" + providerID_BenNetwork + "';";

		boolean provider = db.isResultSetEmpty(SelectProviderCode);

		if (provider == false) {
			String updateProviderData = "UPDATE [VelocityTestAutomation].[dbo].[GuidingCare_Provider_Validation]\r\n"
					+ "SET " + columnName + " = 'FALSE'\r\n" + "WHERE PROVIDER_ID = " + "'" + providerID_BenNetwork
					+ "';";
			db.sqlUpdate(updateProviderData);
		} else {
			String insertProviderData = "INSERT INTO [VelocityTestAutomation].[dbo].[GuidingCare_Provider_Validation]\r\n"
					+ "(PROVIDER_ID," + columnName + ")\r\n" + "VALUES (" + "'" + providerID_BenNetwork + "',"
					+ "'FALSE'" + ");";
			db.sqlUpdate(insertProviderData);
		}
	}

	public void delete_Row_From_Provider_Validation_Table() {
		String providerID_BenNetwork = testData.get("PROVIDER_ID").trim() + "_"
				+ testData.get("BENEFIT_NETWORK").trim();

		String SelectProviderCode = "SELECT ID \r\n"
				+ " FROM [VelocityTestAutomation].[dbo].[GuidingCare_Provider_Validation]\r\n" + "WHERE PROVIDER_ID = "
				+ "'" + providerID_BenNetwork + "';";

		boolean provider = db.isResultSetEmpty(SelectProviderCode);

		if (provider == true) {
			String deleteProviderData = "DELETE FROM [VelocityTestAutomation].[dbo].[GuidingCare_Provider_Validation]\r\n"
					+ "WHERE PROVIDER_ID = " + "'" + providerID_BenNetwork + "';";
			db.sqlUpdate(deleteProviderData);
		}
	}

	public boolean validateProviderDetails(String benNetwork) {
		boolean flag = true;
		try {
			HashMap<String, String> testDataMem = retrieveProviderDetails(benNetwork);
			HashMap<String, String> srcProviderTestData = new HashMap<String, String>();
			HashMap<String, String> targetProviderTestData = new HashMap<String, String>();

			logger.info("DB Info - \t" + testData);
			logger.info("UI Info - \t" + testDataMem);

			// Provider & Facility Name
			srcProviderTestData.put("PROVIDER_NAME", testDataMem.get("Provider Name"));
			if (testDataMem.get("Provider Type").trim().equals("Supplier"))
				targetProviderTestData.put("FACILITY_NAME", testData.get("FACILITY_NAME").trim());
			else
				targetProviderTestData.put("PROVIDER_NAME", testData.get("PROVIDER_NAME").trim());

			// Provider NPI
			targetProviderTestData.put("NPI_VALUE", testData.get("NPI_VALUE").trim());
			if (testDataMem.get("Provider NPI").trim().contains(testData.get("NPI_VALUE").trim())) {
				srcProviderTestData.put("NPI_VALUE", testData.get("NPI_VALUE").trim());
			} else {
				srcProviderTestData.put("NPI_VALUE", testDataMem.get("Provider NPI"));
			}

			// Provider Type
			targetProviderTestData.put("PROVIDER_TYPE", testData.get("PROVIDER_TYPE").trim());
			srcProviderTestData.put("PROVIDER_TYPE", testDataMem.get("Provider Type"));

			// Provider NPI
			targetProviderTestData.put("NPI_VALUE", testData.get("NPI_VALUE").trim());
			if (testDataMem.get("Provider NPI").trim().contains(testData.get("NPI_VALUE").trim())) {
				srcProviderTestData.put("NPI_VALUE", testData.get("NPI_VALUE").trim());
			} else {
				srcProviderTestData.put("NPI_VALUE", testDataMem.get("Provider NPI"));
			}

			// TAX ID
			srcProviderTestData.put("TAX_ID_VALUE", testDataMem.get("Tax ID"));
			targetProviderTestData.put("TAX_ID_VALUE", testData.get("TAX_ID_VALUE").trim());

			// Languages Spoken
			String languagesSpoken = testDataMem.get("LanguageSpoken");
			if (languagesSpoken.contains("Languages Spoken")) {
				languagesSpoken = languagesSpoken.replace("Languages Spoken", "").trim();
				String[] langSplitUI = trimStringArray(languagesSpoken.split(";|\\r?\\n"));
				String[] langSplitDB = trimStringArray(testData.get("LANGUAGE_SPOKEN_DESC").split("[,;]"));
				srcProviderTestData.put("LANGUAGE_SPOKEN", Arrays.toString(langSplitUI));
				targetProviderTestData.put("LANGUAGE_SPOKEN", Arrays.toString(langSplitDB));
			} else {
				srcProviderTestData.put("LANGUAGE_SPOKEN", testDataMem.get("LanguageSpoken").trim());
				targetProviderTestData.put("LANGUAGE_SPOKEN", testData.get("LANGUAGE_SPOKEN_DESC").trim());
			} // what to do if one is null and other one is blank? do we need to add separate
				// condition?

			// Gender
			String gender = testDataMem.get("Provider Gender");
			targetProviderTestData.put("GENDER", testData.get("GENDER").trim());

			if (testDataMem.get("Provider Type").equals("Practitioner")) {
				if (gender.contains("FEMALE") || gender.contains("female")) {
					gender = "F";
				} else if (gender.contains("MALE") || gender.contains("male")) {
					gender = "M";
				}
			}
			srcProviderTestData.put("GENDER", gender);

			// Specialty
			String speciality = testDataMem.get("Speciality");

			speciality = removePrefix(speciality, "Practitioner").trim();
			speciality = removePrefix(speciality, "Supplier").trim();
			speciality = removePrefix(speciality, "Speciality").trim();
			speciality = speciality.replace("amp;", "").trim();
			if (testData.get("SPECIALTY_DESC").contains(",") || testData.get("SPECIALTY_DESC").contains("^")) {
				String[] specialtySplitUI = trimStringArray(speciality.split("\\r?\\n"));
				String[] specialtySplitDB = trimStringArray(testData.get("SPECIALTY_DESC").split("[,^]"));
				srcProviderTestData.put("SPECIALTY_DESC", Arrays.toString(specialtySplitUI));
				targetProviderTestData.put("SPECIALTY_DESC", Arrays.toString(specialtySplitDB));
			} else {
				targetProviderTestData.put("SPECIALTY_DESC", testData.get("SPECIALTY_DESC").trim());
				srcProviderTestData.put("SPECIALTY_DESC", speciality);
			}

			// Accepting Patients
			String acceptingNewPatients = testDataMem.get("Accepting Patients").trim();
			if (acceptingNewPatients.contains("True") || acceptingNewPatients.contains("Yes")) {
				acceptingNewPatients = "1";
			} else if (acceptingNewPatients.contains("False") || acceptingNewPatients.contains("No")) {
				acceptingNewPatients = "0";
			}
			targetProviderTestData.put("ACCEPTING_NEW_PATIENTS", testData.get("AcceptingNewPatients").trim());
			srcProviderTestData.put("ACCEPTING_NEW_PATIENTS", acceptingNewPatients);

			// Benefit Network & LOB Info
			String bn = testDataMem.get("benefitNetwork");
			String networkName = "";
			String lobName = "";
			int beginIndex;
			int endIndex;

			if (bn == null || bn.equals("")) {
				networkName = "";
				lobName = "";
			}
			if (bn.contains("LOB") && bn.contains("BENEFIT NETWORKS")) {
				List<Integer> lobTextList = findWord(bn, "LOB");
				List<Integer> benefitNetworksTextList = findWord(bn, "BENEFIT NETWORKS");
				beginIndex = bn.indexOf("LOB");
				if (lobTextList.get(0) < benefitNetworksTextList.get(0)) {
					endIndex = benefitNetworksTextList.get(0);
				} else {
					endIndex = benefitNetworksTextList.get(1);
				}

				lobName = bn.substring(beginIndex, endIndex);
				lobName = lobName.replace("LOB", "");
				lobName = lobName.trim();
				logger.info(lobName);
			}
			if (bn.contains("BENEFIT NETWORKS")) {
				beginIndex = bn.lastIndexOf("BENEFIT NETWORKS");
				if (bn.contains("Contract Start Date")) {
					endIndex = bn.indexOf("Contract Start Date");
				} else {
					endIndex = bn.indexOf("Contract End Date");
				}
				networkName = bn.substring(beginIndex, endIndex);
				networkName = networkName.replace("BENEFIT NETWORKS", "");
				networkName = networkName.trim();
				logger.info(networkName);
			}

			srcProviderTestData.put("LOB_VALUE", lobName);
			srcProviderTestData.put("BENEFIT_NETWORK", networkName);
			targetProviderTestData.put("LOB_VALUE", testData.get("LOB_value").trim());
			targetProviderTestData.put("BENEFIT_NETWORK", testData.get("BENEFIT_NETWORK").trim());

			// Start Date
			targetProviderTestData.put("EFFECTIVE_DATE", testData.get("EffectiveDate").trim());
			String formattedDate1 = getDateMMDDYYYYInAlternateSqlFormat(testDataMem.get("StartDate").trim());
			srcProviderTestData.put("EFFECTIVE_DATE", formattedDate1);

			// End Date
			targetProviderTestData.put("TERM_DATE", testData.get("TermDate").trim());
			String formattedDate2 = getDateMMDDYYYYInAlternateSqlFormat(testDataMem.get("EndDate").trim());
			srcProviderTestData.put("TERM_DATE", formattedDate2);

			// Phone
			String telephone = testDataMem.get("Phone");
			if (telephone == null || telephone.trim().equals("N/A")) {
				srcProviderTestData.put("TELEPHONE", "");
			} else {
				srcProviderTestData.put("TELEPHONE", telephone);
			}
			targetProviderTestData.put("TELEPHONE", testData.get("Telephone").trim());

			// Address Data
			srcProviderTestData.put("ADDRESS_DATA", testDataMem.get("Address").trim());
			targetProviderTestData.put("ADDRESS_DATA", testData.get("AddressData").trim());

			// compare Hashmaps
			compareHashMaps(srcProviderTestData, targetProviderTestData);

		} catch (Exception e) {
			e.printStackTrace();
			flag = false;
			try {
				reportFail("Provider details are not validated");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}

	public String[] trimStringArray(String[] array) {

		for (int i = 0; i < array.length; i++) {
			array[i] = array[i].trim();
		}
		return array;

	}

	public String removePrefix(String s, String prefix) {
		if (s != null && prefix != null && s.startsWith(prefix)) {
			return s.substring(prefix.length());
		}
		return s;
	}

	public boolean compareHashMaps(HashMap<String, String> map1, HashMap<String, String> map2) {
		boolean flag = true;
		try {
			if (map1 == null || map2 == null)
				flag = false;
			else {
				for (String ch1 : map1.keySet()) {
					if (!map1.get(ch1).equalsIgnoreCase(map2.get(ch1))) {
						insertOrUpdate_Row_From_Provider_Validation_Table(ch1);
						test.log(Status.FAIL, map1.get(ch1) + "is not equal to " + map2.get(ch1));
						flag = false;
					}
				}
			}
			if (flag) {
				reportPass("Guiding Care Provider matching values- \n" + map1);
			} else {
				reportFail("Guiding Care Provider unmatched values \n UI values-\n" + map1 + "\n DB value - \n" + map2);
			}

		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("GuidingCare provider search failed.");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}
}