package processes;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.WebDriverException;

import com.aventstack.extentreports.Status;

import managers.PageManager;
import pageclasses.BasePage;
import utils.Dbconn;

public class HRPPGRProviderValidationProcess extends BasePage {

	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(HRPPGRProviderValidationProcess.class.getName());
	Dbconn db = new Dbconn();
	PageManager pm = new PageManager();

	public void validatePGRProvider(String programCode) {
		try {
			boolean flag = true;
			int counter = 0;
			String query = query_provider_PGR(programCode);
			List<HashMap<String, String>> list = db.getListOfHashMapsFromResultSet(query);
			logger.info(list.size());
			outerloop: for (HashMap<String, String> row : list) {
				counter++;
				HashMap<String, String> temptestData = new HashMap<String, String>();
				for (String key : row.keySet()) {
					temptestData.put(key, row.get(key));
				}

				setrowTestData(temptestData);

				test = extent.createTest("HRP PGR Provider Validation " + temptestData.get("NPI"));

				String checkForExitingRecordQuery = "SELECT COUNT(0) as count FROM [dbo].[hrp_pgr_provider_supplier_details] WHERE NPI = '"
						+ temptestData.get("NPI") + "' and [SCREEN_SCAN_STATUS] = 'Success'";

				if (!db.checkForExistingRecord(checkForExitingRecordQuery)) {

					boolean flag1 = pm.getHrpHomePage().searchPractioner();
					if (flag1 == false) {

						test.log(Status.FATAL, "Exception in HRP Record");
						flushTest();
						break outerloop;

					}

					if (!pm.getHRPProviderSearchPage().getProviderDetails()) {
						flag = false;
					}
				}
				if (flag)

					pm.getHRPPGRProviderProcess().validateProviderSupplierData(temptestData);

				test.log(Status.INFO, "Provider Record " + (counter) + "validated out of " + list.size() + " records");
				flushTest();

			}

		} catch (WebDriverException e) {
			e.printStackTrace();
			winDriver.quit();
		}

		catch (Exception e) {
			e.printStackTrace();
		}

	}

	public void validateProviderSupplierData(HashMap<String, String> tempHashMap) {

		String benefitNetworkNameHRP = null;
		String categoryHRP = null;
		String PGRname = null, degree = null;
		switch (tempHashMap.get("Network")) {
		case "MedicareAdvantage HMO": // MDH
			benefitNetworkNameHRP = "MA BlueAdvantage P3 HMO,MA BlueAdvantage HMO";
			break;
		case "MA BlueInspire Mariocpa Pima PPO": // MPP //MA BlueInspire Mariocpa Pima PPO
			benefitNetworkNameHRP = "MA BlueJourney PPO Maricopa Pima";
			break;
		case "MA BlueInspire Statewide PPO": // MPM
			benefitNetworkNameHRP = "MA BlueJourney PPO Statewide";
			break;
		case "MA BlueVista Maricopa HMO": // MMH
			benefitNetworkNameHRP = "MA BluePathway Maricopa HMO";
			break;
		case "MA BlueVista Pima HMO": // MPH
			benefitNetworkNameHRP = "MA BluePathway P3 HMO";
			break;

		}

		if (tempHashMap.get("CATEGORY").equalsIgnoreCase("Individual")) {
			categoryHRP = "Individual";
			degree = db.returnRecordFromTablePRFDB("[Profisee].[data].[tDG_DegreeType]", "[MasterCd]", "[name]",
					tempHashMap.get("DEGREE"));
			if (tempHashMap.get("MID") == null || tempHashMap.get("MID").isEmpty()) {
				PGRname = (tempHashMap.get("FIRST_NAME") + " " + tempHashMap.get("LAST_NAME") + " " + degree)
						.toUpperCase();
			} else {
				PGRname = (tempHashMap.get("FIRST_NAME") + " " + tempHashMap.get("MID") + " "
						+ tempHashMap.get("LAST_NAME") + " " + degree).toUpperCase();
			}
			tempHashMap.put("NAME", PGRname);
		} else {
			categoryHRP = "Supplier";
			tempHashMap.put("NAME", tempHashMap.get("LAST_NAME"));
		}

		tempHashMap.put("CATEGORY_HRP", categoryHRP);

		tempHashMap.put("BENEFITNETWORK_HRP", benefitNetworkNameHRP);

		boolean flag = db.rowMatchedHRPPGR(tempHashMap);

		if (flag) {
			try {
				String query = " UPDATE [VelocityTestAutomation].[dbo].[PGR_HRP_Data] SET [STATUS] = 'Pass' where [ID] = '"
						+ tempHashMap.get("ID") + "';";
				db.sqlUpdate(query);
				// test.log(Status.PASS,"PGR HRP " + categoryHRP + " Validation Passed for PGR
				// Data:" + testData);
				reportPass("PGR HRP " + categoryHRP + " Validation Passed for PGR Data:" + testData);

			} catch (Exception e) {
				e.printStackTrace();
			}
		} else {
			try {
				String query = " UPDATE [VelocityTestAutomation].[dbo].[PGR_HRP_Data] SET [STATUS] = 'Fail' where [ID] = '"
						+ tempHashMap.get("ID") + "';";
				db.sqlUpdate(query);
				test.log(Status.FAIL, "PGR HRP " + categoryHRP + " Validation Passed for PGR Data:" + testData);
				reportFail("PGR HRP " + categoryHRP + " Validation Failed for PGR Data:" + testData);
			} catch (Exception e1) {
				e1.printStackTrace();
			}

		}
	}

	public void validatePGRSupplier(String programCode) {
		try {
			String query = query_supplier_PGR(programCode);
			boolean flag = true;
			int counter = 0;
			List<HashMap<String, String>> list = db.getListOfHashMapsFromResultSet(query);
			logger.info(list.size());
			outerloop: for (HashMap<String, String> row : list) {
				counter++;
				HashMap<String, String> temptestData = new HashMap<String, String>();
				for (String key : row.keySet()) {
					temptestData.put(key, row.get(key));
				}

				setrowTestData(temptestData);

				test = extent.createTest("HRP PGR Supplier Validation " + temptestData.get("NPI"));

				String checkForExitingRecordQuery = "SELECT COUNT(0) as count FROM [dbo].[hrp_pgr_provider_supplier_details] WHERE NPI = '"
						+ temptestData.get("NPI") + "' and [SCREEN_SCAN_STATUS] = 'Success'";

				if (!db.checkForExistingRecord(checkForExitingRecordQuery)) {
					if (!db.checkForExistingRecord(checkForExitingRecordQuery)) {
						boolean flag1 = pm.getHrpHomePage().searchSupplierNew();
						if (flag1 == false) {

							test.log(Status.FATAL, "Exception in HRP Record");
							flushTest();
							break outerloop;

						}
						if (!pm.getHRPSupplierViewDetailsPage().getSupplierDetails()) {
							reportFail("Supplier not found in HRP NPI: " + temptestData.get("NPI"));
							flag = false;
						}
					}
					if (flag)
						pm.getHRPPGRProviderProcess().validateProviderSupplierData(temptestData);
					test.log(Status.INFO,
							"Supplier Record " + (counter) + "validated out of " + list.size() + " records");
					flushTest();
				}
			}
		} catch (Exception e) {

		}

	}

	public String query_provider_PGR(String programCode) {

		String query = "SELECT [ID] ,[CATEGORY] ,[LAST_NAME], [FIRST_NAME]  ,[MID] ,[DEGREE] ,[TAX_ID] ,[NPI] ,[ADDRESS] ,[SUITE]"
				+ " ,[CITY] ,[ST] ,[ZIP],[CNTY]  ,[PRIMARY_SPECIALTY] ,[Network]  ,[Program], [PRIMARYCAREINDI]"
				+ "	  FROM [VelocityTestAutomation].[dbo].[PGR_HRP_Data] where program in ('MDH','MPH','MPP','MMH') and [STATUS] is null"
				+ " and NPI = '1194245746'";
		// + " and CATEGORY = 'Individual' and NPI between 1154587939 and 1326056599";

		return query;
	}

	public String query_supplier_PGR(String programCode) {

		String query = "SELECT [ID] ,[CATEGORY] ,[LAST_NAME], [FIRST_NAME]  ,[MID] ,[DEGREE] ,[TAX_ID] ,[NPI] ,[ADDRESS] ,[SUITE]"
				+ " ,[CITY] ,[ST] ,[ZIP],[CNTY]  ,[PRIMARY_SPECIALTY] ,[Network]  ,[Program], [PRIMARYCAREINDI]"
				+ "	  FROM [VelocityTestAutomation].[dbo].[PGR_HRP_Data] where program in ('MDH','MPH','MPP','MMH') and category <> 'Individual' and [STATUS] is null"
				+ " and CATEGORY  <> 'Individual' and NPI between 1154587939 and 1326056599";

		return query;
	}

	public void validateHRP_PGR(String programCode) {

		try {
			validatePGRProvider(programCode);
			// validatePGRSupplier(programCode);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Test Failed in HRPPGRProviderValidationProcess validateHRP_PGR method");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void validationProcessOnExitsingHRPData(String programCode) {
		try {
			int counter = 0;
			String getNPIFromHRPTableQuery = "select distinct(NPI) from [dbo].[hrp_pgr_provider_supplier_details] where npi in ('1003018649',\r\n"
					+ "'1003019688',\r\n" + "'1003063173',\r\n" + "'1003068727',\r\n" + "'1003090390',\r\n"
					+ "'1164613675',\r\n" + "'1164630679',\r\n" + "'1164646857',\r\n" + "'1588649198',\r\n"
					+ "'1588650220',\r\n" + "'1588661151',\r\n" + "'1588674832',\r\n" + "'1588681324',\r\n"
					+ "'1588685879',\r\n" + "'1760872550',\r\n" + "'1760925069',\r\n" + "'1962456590',\r\n"
					+ "'1962462630',\r\n" + "'1962469791'\r\n" + ") order by NPI asc";
			List<String> npis = db.getListFromQuery(getNPIFromHRPTableQuery);

			for (String npi : npis) {

				String getPGRRecordForValidationQuery = "  select [ID] ,[CATEGORY] ,[LAST_NAME], [FIRST_NAME]  ,[MID] ,[DEGREE] ,[TAX_ID] ,[NPI] ,[ADDRESS] ,[SUITE] "
						+ "			      ,[CITY] ,[ST] ,[ZIP],[CNTY]  ,[PRIMARY_SPECIALTY] ,[Network]  ,[Program], [PRIMARYCAREINDI] from pgr_hrp_data where NPI = '"
						+ npi + "'";
//								+ " and [status] is null " +
//						" and program = 'MPP' "+
//						"				  order by NPI asc\r\n" ;

				List<HashMap<String, String>> list = db.getListOfHashMapsFromResultSet(getPGRRecordForValidationQuery);
				logger.info(list.size());
				for (HashMap<String, String> row : list) {
					counter++;
					HashMap<String, String> temptestData = new HashMap<String, String>();
					for (String key : row.keySet()) {
						temptestData.put(key, row.get(key));
					}

					setrowTestData(temptestData);

					test = extent.createTest("HRP PGR Supplier Validation " + temptestData.get("NPI"));

					pm.getHRPPGRProviderProcess().validateProviderSupplierData(temptestData);
					test.log(Status.INFO,
							"Provider Record " + (counter) + "validated out of " + list.size() + " records");
					flushTest();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

	}

}
