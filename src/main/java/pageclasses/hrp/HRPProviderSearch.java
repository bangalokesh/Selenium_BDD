package pageclasses.hrp;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.aventstack.extentreports.Status;

import enums.LocatorTypes;
import io.appium.java_client.windows.WindowsDriver;
import managers.PageManager;
import pageclasses.BasePage;
import pageobjects.HRPObjRepo;
import utils.Dbconn;

public class HRPProviderSearch extends BasePage {

	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(HRPProviderSearch.class.getName());
	Dbconn db = new Dbconn();
	PageManager pm = new PageManager();

	public HRPProviderSearch(WindowsDriver<WebElement> winDriver) {

	}

	public HashMap<String, String> searchProvider(ResultSet testData) {
		HashMap<String, String> searchResultData = new HashMap<String, String>();
		try {
			wait(2);
			getWinElement(LocatorTypes.name, HRPObjRepo.HRPTobBar.clear_name).click();
			WebElement w = getWinElement(LocatorTypes.name, HRPObjRepo.HRPProviderSearch.NPI);
			w.sendKeys(testData.getString("NPI") + Keys.ENTER);
			String resultCount = getWinElement(LocatorTypes.name, HRPObjRepo.HRPProviderSearch.statusBar).getText();
			String[] result = resultCount.split(":");
			int count = Integer.parseInt(result[1].trim());
			if (count == 1) {
				String name = getWinElement(LocatorTypes.name,
						HRPObjRepo.HRPProviderSearch.providerNameOutput + (count - 1)).getText();
				String npi = getWinElement(LocatorTypes.name, HRPObjRepo.HRPProviderSearch.npiOutput + (count - 1))
						.getText();
				String practitionerId = getWinElement(LocatorTypes.name,
						HRPObjRepo.HRPProviderSearch.practitionerOutput + (count - 1)).getText();
				String primarySpecialty = getWinElement(LocatorTypes.name,
						HRPObjRepo.HRPProviderSearch.primarySpecialty + (count - 1)).getText();
				String supplierId = getWinElement(LocatorTypes.name, HRPObjRepo.HRPProviderSearch.supplierID + "0")
						.getText();
				String supplierLocation = getWinElement(LocatorTypes.name,
						HRPObjRepo.HRPProviderSearch.supplierLocation + (count - 1)).getText();
				logger.info("search result Provider Name = " + name + npi);

				searchResultData.put("Name", name);
				searchResultData.put("PractitionerId", practitionerId);
				searchResultData.put("NPI", npi);
				searchResultData.put("primarySpecialty", primarySpecialty);
				searchResultData.put("supplierId", supplierId);
				searchResultData.put("supplierLocation", supplierLocation);
				reportPass("Search Provider");
				if (supplierId == null || supplierId.isEmpty() || supplierLocation == null
						|| supplierLocation.isEmpty())
					test.log(Status.WARNING, "Supplier ID is null in Provider Search Result");
			}
			Actions a = new Actions(winDriver);
			a.click(getWinElement(LocatorTypes.name, HRPObjRepo.HRPTobBar.searchFor_name)).sendKeys("s").build()
					.perform();
			PageManager pm = new PageManager();
			HashMap<String, String> supplierData = pm.getHRPSupplierSearchPage().searchSupplierByTaxID(testData);
			if (supplierData != null) {
				searchResultData.put("Supp_SupplierId", supplierData.get("SupplierID"));
				searchResultData.put("Supp_SupplierName", supplierData.get("SupplierName"));
				searchResultData.put("Supp_Address", supplierData.get("Address"));
				searchResultData.put("Supp_City", supplierData.get("City"));
				searchResultData.put("Supp_State", supplierData.get("State"));
				searchResultData.put("Supp_ZipCode", supplierData.get("ZipCode"));
				reportPass("Search Supplier");
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Failure in Searching Member at HRPMemberSearch Page");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return searchResultData;
	}

	/*
	 * public void searchProvider() { try { WebElement w =
	 * getWinElement(LocatorTypes.name, HRPObjRepo.HRPProviderSearch.NPI);
	 * w.sendKeys(testData.get("NPI") + Keys.ENTER); String name =
	 * getWinElement(LocatorTypes.name,
	 * HRPObjRepo.HRPProviderSearch.providerNameOutput +"0").getText(); String
	 * practitionerId = getWinElement(LocatorTypes.name,
	 * HRPObjRepo.HRPProviderSearch.practitionerOutput +"0").getText(); String npi =
	 * getWinElement(LocatorTypes.name, HRPObjRepo.HRPProviderSearch.npiOutput
	 * +"0").getText(); String primarySpecialty = getWinElement(LocatorTypes.name,
	 * HRPObjRepo.HRPProviderSearch.primarySpecialty +"0").getText();
	 * logger.info("search result Provider Name = " + name + practitionerId + npi +
	 * primarySpecialty ); if(npi.contains(testData.get("NPI"))){
	 * reportPass("Provider Search successful" + "search result Member Name = " +
	 * name); } getWinElement(LocatorTypes.name,
	 * HRPObjRepo.HRPProviderSearch.providerNameOutput +"0").click();
	 * getWinElement(LocatorTypes.name,
	 * HRPObjRepo.HRPProviderSearch.providerNameOutput +"0").sendKeys(Keys.ENTER);
	 * wait(10); Set<String> winHandles = winDriver.getWindowHandles();
	 * System.out.println(winHandles.size()); for(String winHandle : winHandles){
	 * winDriver.switchTo().window(winHandle); break; } } catch (Exception e) {
	 * e.printStackTrace(); try {
	 * reportFail("Failure in Searching Member at HRPMemberSearch Page"); } catch
	 * (IOException e1) { e1.printStackTrace(); } }
	 * 
	 * }
	 */

	public void viewMember() {
		try {
			Set<String> winHandles = winDriver.getWindowHandles();
			for (String winHandle : winHandles) {
				winDriver.switchTo().window(winHandle);
				getWinElement(LocatorTypes.name, HRPObjRepo.HRPMemberSearch.view_name).click();
				getWinElement(LocatorTypes.name, HRPObjRepo.HRPMemberSearch.memNameSearch_name + "0")
						.sendKeys(Keys.ENTER);
				break;
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Failure in Searching Member at HRPMemberSearch Page");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public Connection getAccessDbCon() {
		Connection connection = null;
		try {
			Class.forName("net.ucanaccess.jdbc.UcanaccessDriver");
			String msAccDB = "C:\\Users\\lbanga01\\Git\\AccessDB\\velocity_automation.mdb";
			String dbURL = "jdbc:ucanaccess://" + msAccDB;
			connection = DriverManager.getConnection(dbURL);
		} catch (Exception cnfex) {
			System.out.println("Problem in loading or " + "registering MS Access JDBC driver");
			cnfex.printStackTrace();
		}
		return connection;
	}

	public void validateProviders() {
		Connection connection = null;
		Statement statement = null;
		ResultSet resultSet = null;
		boolean flag = true;
		try {
			connection = getAccessDbCon();
			statement = connection.createStatement();
			resultSet = statement.executeQuery("SELECT * FROM Provider_Extract_No_Duplicate_NPI WHERE STATUS is null");
			while (resultSet.next()) {
				test = extent.createTest(resultSet.getString("NPI"));
				HashMap<String, String> tempProviders = searchProvider(resultSet);
				String name = resultSet.getString("LAST_NAME").trim() + ", " + resultSet.getString("FIRST_NAME").trim();
				String address = resultSet.getString("ADDRESS") + " " + resultSet.getString("SUITE").trim();

				if (!(name.trim().equals(tempProviders.get("Name").toUpperCase().trim()))) {
					System.out.println(
							name.trim() + " is not equal to " + tempProviders.get("Name").toUpperCase().trim());
					test.log(Status.FAIL, name + " is not equal to " + tempProviders.get("Name"));
					flag = false;
				}
				if (tempProviders.get("Supp_Address") != null) {
					if (!(address.trim().equalsIgnoreCase(tempProviders.get("Supp_Address").toUpperCase().trim()))) {
						System.out.println(address.trim() + " is not equal to "
								+ tempProviders.get("Supp_Address").toUpperCase().trim());
						test.log(Status.FAIL, address + " is not equal to " + tempProviders.get("Supp_Address"));
						flag = false;
					}
				} else {
					test.log(Status.FAIL, "Supplier Data not found");
					flag = false;
				}
				if (!tempProviders.get("supplierId").contains("Supplier row") && tempProviders.get("supplierId") != null
						&& tempProviders.get("Supp_SupplierName") != null) {
					if (!(tempProviders.get("Supp_SupplierName").trim()
							.equalsIgnoreCase(tempProviders.get("supplierId").trim()))) {
						System.out.println(tempProviders.get("Supp_SupplierName").trim() + " is not equal to "
								+ tempProviders.get("supplierId").trim());
						test.log(Status.FAIL, tempProviders.get("Supp_SupplierName") + " is not equal to "
								+ tempProviders.get("supplierId"));
						flag = false;
					}
				}
				if (!(resultSet.getString("PRIMARY_SPECIALTY").trim()
						.equalsIgnoreCase(tempProviders.get("primarySpecialty").trim()))) {
					System.out.println(resultSet.getString("PRIMARY_SPECIALTY").trim() + " is not equal to "
							+ tempProviders.get("primarySpecialty").trim());
					test.log(Status.FAIL, resultSet.getString("PRIMARY_SPECIALTY") + " is not equal to "
							+ tempProviders.get("primarySpecialty"));
					flag = false;
				}
				if (tempProviders.get("Supp_City") != null) {
					if (!resultSet.getString("CITY").trim()
							.equalsIgnoreCase((tempProviders.get("Supp_City").toUpperCase().trim()))) {
						System.out.println(resultSet.getString("CITY").trim() + " is not equal to "
								+ tempProviders.get("Supp_City").toUpperCase().trim());
						test.log(Status.FAIL,
								resultSet.getString("CITY") + " is not equal to " + tempProviders.get("Supp_City"));
						flag = false;
					}
				}
				if (tempProviders.get("Supp_ZipCode") != null) {
					if (!resultSet.getString("ZIP").trim()
							.equalsIgnoreCase((tempProviders.get("Supp_ZipCode").trim()))) {
						System.out.println(resultSet.getString("ZIP").trim() + " is not equal to "
								+ tempProviders.get("Supp_ZipCode").trim());
						test.log(Status.FAIL,
								resultSet.getString("ZIP") + " is not equal to " + tempProviders.get("Supp_ZipCode"));
						flag = false;
					}
				}
				if (tempProviders.get("Supp_State") != null) {
					if (!resultSet.getString("ST").trim().equalsIgnoreCase((tempProviders.get("Supp_State").trim()))) {
						System.out.println(resultSet.getString("ST").trim() + " is not equal to "
								+ tempProviders.get("Supp_State").trim());
						test.log(Status.FAIL,
								resultSet.getString("ST") + " is not equal to " + tempProviders.get("Supp_State"));
						flag = false;
					}
				}

				if (flag == true) {
					updateResults(resultSet.getInt("ID"), "PASSED");
				} else {
					updateResults(resultSet.getInt("ID"), "FAILED");
				}
				flushTest();
			}
		} catch (SQLException e) {
			e.printStackTrace();
			try {
				winDriver.close();
				reportFail("HRPProviderSearch validateProviders Failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		} finally {
			try {
				if (null != connection) {
					resultSet.close();
					statement.close();
					connection.close();
				}
			} catch (SQLException s) {
				s.printStackTrace();
			}
		}
	}

	public void updateResults(int id, String status) {
		Connection connection = null;
		Statement statement = null;
		try {
			connection = getAccessDbCon();
			statement = connection.createStatement();
			String query = "UPDATE Provider_Extract_No_Duplicate_NPI SET STATUS = '" + status + "' WHERE ID = " + id;
			statement.executeUpdate(query);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean getProviderDetails() {
		try {
			String npi = null, primarySpecialty = null;
			Set<String> winHandles = winDriver.getWindowHandles();
			String parentWindow = winDriver.getWindowHandle();
			String childWindow = null;
			boolean flag = true;

			for (String winHandle : winHandles) {
				if (!winHandle.equals(parentWindow)) {
					childWindow = winHandle;
					winDriver.switchTo().window(winHandle);
				}
			}
			HashMap<String, String> providerDetails = new HashMap<String, String>();
			getWinElement(LocatorTypes.name, HRPObjRepo.HRPTobBar.clear_name).click();
			WebElement w = getWinElement(LocatorTypes.name, HRPObjRepo.HRPProviderSearch.NPI);
			w.sendKeys(testData.get("NPI") + Keys.ENTER);

			// No Search Result in HRP
			if (isWinElementPresent(LocatorTypes.name, HRPObjRepo.HRPOkAlert.ok_name)) {

				getWinElement(LocatorTypes.name, HRPObjRepo.HRPOkAlert.ok_name).click();

				// For closing window
				Actions a = new Actions(winDriver);
				a.keyDown(Keys.ALT).sendKeys("o").keyUp(Keys.ALT).perform();

				String query = " UPDATE [VelocityTestAutomation].[dbo].[PGR_HRP_Data] SET [STATUS] = 'ProvNotFound' where [ID] = '"
						+ testData.get("ID") + "';";
				db.sqlUpdate(query);
				reportFail("Provider not found in HRP NPI: " + testData.get("NPI"));
				return false;
			}
			String resultCount = getWinElement(LocatorTypes.name, HRPObjRepo.HRPProviderSearch.statusBar).getText();
			String[] result = resultCount.split(":");
			int count = Integer.parseInt(result[1].trim());
			if (count == 1) {
//				npi = getWinElement(LocatorTypes.name, HRPObjRepo.HRPProviderSearch.npiOutput + (count - 1)).getText();
//				primarySpecialty = getWinElement(LocatorTypes.name,
//						HRPObjRepo.HRPProviderSearch.primarySpecialty + (count - 1)).getText();
//				primarySpecialty.replaceAll(", ", "-");
			} else
				reportFail("Provider search Result is more than one in HRP NPI: " + HRPObjRepo.HRPProviderSearch.NPI);

			winHandles = winDriver.getWindowHandles();

			// Click on View button
			List<WebElement> list_view = getWinElements(LocatorTypes.name, HRPObjRepo.HRPProviderSearch.view_name);
			if (list_view.size() == 1)
				list_view.get(0).click();
			else
				list_view.get(1).click();
			winDriver.switchTo().window(parentWindow);
			// Retrieve provider name
			String name = getWinElement(LocatorTypes.id, HRPObjRepo.HRPProviderViewDetails.personName_id).getText();
			npi = getWinElement(LocatorTypes.id, HRPObjRepo.HRPProviderViewDetails.npi_id).getText();
			primarySpecialty = getWinElement(LocatorTypes.name, HRPObjRepo.HRPProviderViewDetails.taxonomy_name)
					.getText();

			reportPass("Name information is extracted from screen");

			providerDetails.put("NAME", name);
			providerDetails.put("NPI", npi);
			providerDetails.put("PrimarySpeciality", primarySpecialty);
			providerDetails.put("CATEGORY", "Individual");
			providerDetails.put("BenefitNetwork", getBenefitNetworkName());

			getWinElement(LocatorTypes.name, HRPObjRepo.HRPProviderViewDetails.rolesTab_name).click();

			if (!isWinElementPresent(LocatorTypes.name, "Row 1")) {
				reportFail("Roles are not present for practioner NPI :" + testData.get("NPI"));
				return false;
			}

			for (int i = 1; isWinElementPresent(LocatorTypes.name, "Row " + i); i++) { // Flow to retrieve address
																						// details for type PCP

				String roleType = getWinElement(LocatorTypes.name,
						HRPObjRepo.HRPProviderViewDetails.roleType_name + (i - 1)).getText();
				if (roleType.equalsIgnoreCase("PCP")) {
					providerDetails.put("PrimaryIndicator", "1");
					try {
						getWinElement(LocatorTypes.name, HRPObjRepo.HRPProviderViewDetails.roleNameRow_name + (i - 1))
								.click();
						practitionerDetailsPCPToDB(providerDetails);

						String insertSuccessQuery = "UPDATE [dbo].[hrp_pgr_provider_supplier_details] SET Screen_Scan_Status = 'Success' WHERE NPI = '"
								+ testData.get("NPI") + "'";
						db.sqlUpdate(insertSuccessQuery);
					} catch (Exception e) {
						e.printStackTrace();
						return false;
					}
				} else { // Retrieve address details for type non PCP
					providerDetails.put("PrimaryIndicator", "0");
					getWinElement(LocatorTypes.name, HRPObjRepo.HRPProviderViewDetails.supplierName_name + (i - 1))
							.click();
					pm.getHRPSupplierViewDetailsPage().getSupplierLocationDetails(providerDetails);
				}

			}
		} catch (Exception e) {
			e.printStackTrace();
			if (isWinElementPresent(LocatorTypes.name, HRPObjRepo.HRPTobBar.quit_name)) {
				getWinElement(LocatorTypes.name, HRPObjRepo.HRPTobBar.quit_name).click();
				return false;
			} else {
				winDriver.quit();
				return false;
			}

		}
		return true;

	}

	public String getBenefitNetworkName() {
		StringBuilder benefitNetworkName = new StringBuilder();
		try {
			if (!isWinElementPresent(LocatorTypes.xpath,
					HRPObjRepo.HRPProviderViewDetails.benifitNetwork1_xpath + "[1]")) {

				reportFail("No Benefit Networks present in HRP for practitioner NPI" + testData.get("NPI"));
			}

			for (int i = 1; isWinElementPresent(LocatorTypes.xpath,
					HRPObjRepo.HRPProviderViewDetails.benifitNetwork1_xpath + "[" + i + "]"); i++) {
				String value = getWinElement(LocatorTypes.xpath,
						HRPObjRepo.HRPProviderViewDetails.benifitNetwork1_xpath + "[" + i + "]" + "/Pane").getText();
				if (!value.isEmpty()) {
					benefitNetworkName.append(value);
					benefitNetworkName.append(",");
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return benefitNetworkName.toString();

	}

	public void practitionerDetailsPCPToDB(HashMap<String, String> providerDetails) {
		wait(2);

		String County = getWinElement(LocatorTypes.id, HRPObjRepo.HRPProviderViewDetails.county_id).getText();
		String Address = getWinElement(LocatorTypes.id, HRPObjRepo.HRPProviderViewDetails.address_id).getText();
		String Suite = getWinElement(LocatorTypes.id, HRPObjRepo.HRPProviderViewDetails.address2_id).getText();
		String ZipCode = getWinElement(LocatorTypes.id, HRPObjRepo.HRPProviderViewDetails.zipCode_id).getText();
		String City = getWinElement(LocatorTypes.id, HRPObjRepo.HRPProviderViewDetails.city_id).getText();
		String State = getWinElement(LocatorTypes.id, HRPObjRepo.HRPProviderViewDetails.state_id).getText();
		String TaxID = getWinElement(LocatorTypes.xpath, HRPObjRepo.HRPProviderViewDetails.taxID_xpath).getText()
				.replace("-", "");

		try {
			reportPass("Data extracted from HRP Screen");
		} catch (IOException e) {
			e.printStackTrace();
		}
		providerDetails.put("County", County);
		providerDetails.put("Address", Address);
		providerDetails.put("Suite", Suite);
		providerDetails.put("ZipCode", ZipCode);
		providerDetails.put("City", City);
		providerDetails.put("State", State);
		providerDetails.put("TaxID", TaxID);
		db.insert_HRP_ProviderSupplierDetailsToDB(providerDetails);

		getWinElement(LocatorTypes.name, HRPObjRepo.HRPTobBar.back_name).click();
		wait(2);
	}

}