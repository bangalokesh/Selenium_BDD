package processes;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.WebElement;

import enums.LocatorTypes;
import managers.PageManager;
import pageclasses.BasePage;
import pageobjects.HRPObjRepo;
import utils.Dbconn;

/**
 * @author tneha01 TestCase : Validate Claims
 */
public class HRPClaimsValidationProcess extends BasePage {

	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(HRPClaimsValidationProcess.class.getName());
	Dbconn db = new Dbconn();
	PageManager pm = new PageManager();

	public void validateClaims() {

		try {
			String query = "select *  from test_data_readytoclaims  where ClaimNumber = '39020192840000500' and (HRPValidationStatus = 'FAIL' OR HRPValidationStatus is NULL);";

			List<HashMap<String, String>> list = db.getListOfHashMapsFromResultSet(query);
			for (HashMap<String, String> row : list) {
				HashMap<String, String> temptestData = new HashMap<String, String>();
				for (String key : row.keySet()) {
					temptestData.put(key, row.get(key));
				}
				setrowTestData(temptestData);
				test = extent.createTest("HRP Claims Validation of ClaimNumber: " + temptestData.get("ClaimNumber"));
				HashMap<String, String> headerDeaitls = new HashMap<String, String>();
				pm.getHRPClaimsPage().navigateToClaimsSearch();
				pm.getHRPClaimsPage().searchClaim(temptestData.get("ClaimNumber"));
				headerDeaitls = pm.getHRPClaimsPage().claimHeadrDetails();
				System.out.println("header detials: " + headerDeaitls);
				System.out.println("test data: " + temptestData);
				boolean headerValidation = compareHashMaps(headerDeaitls, temptestData);
				if (headerValidation == true) {
					reportPass("Header details Validation passed for ClaimNumber: " + temptestData.get("ClaimNumber"));
				} else {
					reportFail("Header details Validation passed for ClaimNumber: " + temptestData.get("ClaimNumber"));
				}

				// Validate Lines tab
				String linesDataQuery = "select cl.[ID]\r\n" + "      ,cl.[TestCaseID]\r\n"
						+ "      ,cl.[CPTCodeID]\r\n" + "      ,cl.[Amount]\r\n"
						+ "      ,cl.[UnitOrBasisforMeasurementCode]\r\n" + "      ,cl.[Quantity]\r\n"
						+ "      ,cl.[DateTimeFromPeriod]\r\n" + "      ,cl.[DateTimeToPeriod]\r\n"
						+ "      ,cl.[ClaimLineNumber]\r\n"
						+ "  FROM [VelocityTestAutomation].[dbo].[test_data_readytoclaims] rc join\r\n"
						+ "  [VelocityTestAutomation].[dbo].[test_data_claimline] cl ON\r\n"
						+ "  rc.TESTCASEID = cl.TESTCASEID\r\n" + "  where rc.TESTCASEID = '"
						+ temptestData.get("TestCaseID") + "';";
				List<HashMap<String, String>> listOfLines = db.getListOfHashMapsFromResultSet(linesDataQuery);

				int lineCounter = 1;
				boolean linesValidation = false;
				for (HashMap<String, String> line : listOfLines) {
					HashMap<String, String> tempTestData2 = new HashMap<String, String>();
					for (String key2 : line.keySet()) {
						tempTestData2.put(key2, line.get(key2));
					}
					setrowTestData(tempTestData2);
					// get all lines from UI
					getWinElement(LocatorTypes.name, HRPObjRepo.HRPClaims.linesTab_name).click();
					List<WebElement> listOfLinesUI = getWinElements(LocatorTypes.name,
							HRPObjRepo.HRPClaims.noOfLineRecords_name);
					int totalLinesOnUI = listOfLinesUI.size();
					listOfLinesUI.get(totalLinesOnUI - lineCounter).click();
					linesValidation = pm.getHRPClaimsPage().validateLinesTab();
					listOfLinesUI.get(totalLinesOnUI - lineCounter).click();
					lineCounter++;
				}

				if (linesValidation == true) {
					reportPass(
							"All lines - Validations are passed for Claimnumber: " + temptestData.get("ClaimNumber"));
				} else {
					reportFail(
							"All lines - Validations are Failed for Claimnumber: " + temptestData.get("ClaimNumber"));
				}
				flushTest();
				if (headerValidation == true && linesValidation == true) {
					db.updateReadyToClaims("HRPValidationStatus", "PASS");
				} else {
					db.updateReadyToClaims("HRPValidationStatus", "FAIL");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Test Failed in HRP claim Validation process ");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

}
