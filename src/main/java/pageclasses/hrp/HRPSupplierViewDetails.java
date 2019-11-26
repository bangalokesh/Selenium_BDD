package pageclasses.hrp;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import com.aventstack.extentreports.Status;

import enums.LocatorTypes;
import pageclasses.BasePage;
import pageobjects.HRPObjRepo;
import utils.Dbconn;

public class HRPSupplierViewDetails extends BasePage {

	Dbconn db = new Dbconn();

	public String[] viewSupplierContactDetails() {

		getWinElement(LocatorTypes.id, HRPObjRepo.HRPSupplierViewDetails.contactTab_name).click();
		String[] tempContactDetails = null;
		return tempContactDetails;
	}

	public boolean getSupplierDetails() {
		try {
			Set<String> winHandles = winDriver.getWindowHandles();
			String parentWindow = winDriver.getWindowHandle();
			String childWindow = null;

			for (String winHandle : winHandles) {
				if (!winHandle.equals(parentWindow)) {
					winDriver.switchTo().window(winHandle);
					childWindow = winHandle;
				}
			}
			HashMap<String, String> SupplierDetails = new HashMap<String, String>();

			getWinElement(LocatorTypes.name, HRPObjRepo.HRPTobBar.clear_name).click();
			WebElement w = getWinElement(LocatorTypes.name, HRPObjRepo.HRPSupplierSearch.supplierNPI_name);
			w.sendKeys(testData.get("NPI") + Keys.ENTER);
			wait(2);
			// If Search Result is zero
			if (isWinElementPresent(LocatorTypes.name, HRPObjRepo.HRPOkAlert.ok_name)) {

				getWinElement(LocatorTypes.name, HRPObjRepo.HRPOkAlert.ok_name).click();
				// For closing window
				Actions a = new Actions(winDriver);
				a.keyDown(Keys.ALT).sendKeys("o").keyUp(Keys.ALT).perform();

				String query = " UPDATE [VelocityTestAutomation].[dbo].[PGR_HRP_Data] SET [STATUS] = 'SupplNotFound' where [ID] = '"
						+ testData.get("ID") + "';";
				db.sqlUpdate(query);
				reportFail("Provider not found in HRP NPI: " + testData.get("NPI"));
				return false;
			}
			List<WebElement> list_view = getWinElements(LocatorTypes.name, HRPObjRepo.HRPProviderSearch.view_name);
			if (list_view.size() == 1)
				list_view.get(0).click();
			else
				list_view.get(1).click();
			winDriver.switchTo().window(parentWindow);

			wait(2);
			try {
				reportPass("Supplier Name Information extracted from screen");
			} catch (IOException e) {
				e.printStackTrace();
			}

			SupplierDetails.put("NAME",
					getWinElement(LocatorTypes.id, HRPObjRepo.HRPSupplierViewDetails.supplierName_id).getText());
			SupplierDetails.put("PrimarySpeciality",
					getWinElement(LocatorTypes.name, HRPObjRepo.HRPSupplierViewDetails.taxonomy_Name).getText());
			SupplierDetails.put("Degree", "Not Applicable");
			SupplierDetails.put("CATEGORY", "Supplier");
			SupplierDetails.put("BenefitNetwork", returnBenefitNetworkName());
			getSupplierLocationDetails(SupplierDetails);

		} catch (NoSuchWindowException e) {
			e.printStackTrace();
			winDriver.quit();
		} catch (NullPointerException e) {
			e.printStackTrace();
			winDriver.quit();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return true;
	}

	public String returnBenefitNetworkName() {
		StringBuilder benefitNetworkName = new StringBuilder();
		try {
			if (!isWinElementPresent(LocatorTypes.xpath,
					HRPObjRepo.HRPSupplierViewDetails.benefitNetwork1_xpath + "[1]")) {
				try {
					reportFail("No Benefit Network present in HRP for supplier NPI - " + testData.get("NPI"));
				} catch (IOException e) {
					e.printStackTrace();
				}
			}

			for (int i = 1; isWinElementPresent(LocatorTypes.xpath,
					HRPObjRepo.HRPSupplierViewDetails.benefitNetwork1_xpath + "[" + i + "]"); i++) {
				String value = getWinElement(LocatorTypes.xpath, HRPObjRepo.HRPSupplierViewDetails.benefitNetwork1_xpath
						+ "[" + i + "]" + HRPObjRepo.HRPSupplierViewDetails.benefitNetwork2_xpath).getText();
				benefitNetworkName.append(value);
				benefitNetworkName.append(",");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return benefitNetworkName.toString();
	}

	public void getSupplierLocationDetails(HashMap<String, String> tempDetails) {
		try {
			String TaxID = getWinElement(LocatorTypes.xpath, HRPObjRepo.HRPSupplierViewDetails.taxID_xpath).getText()
					.replace("-", "");
			String supplierID = getWinElement(LocatorTypes.id, HRPObjRepo.HRPSupplierViewDetails.supplierID_id)
					.getText();
			String supplierNPI = null;

			if (isWinElementPresent(LocatorTypes.id, HRPObjRepo.HRPSupplierViewDetails.supplierNPI_id)) {
				supplierNPI = getWinElement(LocatorTypes.id, HRPObjRepo.HRPSupplierViewDetails.supplierNPI_id)
						.getText();
			} else {
				supplierNPI = "";
				test.log(Status.FAIL, "Supplier NPI is empty for Supplier ID");
			}

			tempDetails.put("TaxID", TaxID);
			tempDetails.put("SUPPL_NPI", supplierNPI);
			tempDetails.put("SUPPL_ID", supplierID);
			String query = "SELECT COUNT(0) as count FROM [dbo].[hrp_non_pcp_supplier_screen_data] WHERE [SUPPLIER_ID] = '"
					+ tempDetails.get("SUPPL_ID") + "' and [SCREEN_SCAN_STATUS] = 'Success'";

			if (!db.checkForExistingRecord(query)) { // go to HRP screen if data is not present in
														// hrp_non_pcp_supplier_screen_data
				try {
					getWinElement(LocatorTypes.name, HRPObjRepo.HRPSupplierViewDetails.locationsTab_name).click();
					wait(1);

					if (!isWinElementPresent(LocatorTypes.name, "Row 1")) {
						reportFail("No Supplier locations found in HRP for " + testData.get("NPI"));
					}
				} catch (Exception e1) {
					e1.printStackTrace();
				}

				try {
					getSupplierLocationFromScreen(tempDetails);

					String updateAsSuccess = "UPDATE [dbo].[hrp_non_pcp_supplier_screen_data] SET Screen_Scan_Status = 'Success' WHERE [SUPPLIER_ID] = '"
							+ tempDetails.get("SUPPL_ID") + "'";
					db.sqlUpdate(updateAsSuccess);

					String insertSuccessQuery = "UPDATE [dbo].[hrp_pgr_provider_supplier_details] SET Screen_Scan_Status = 'Success' WHERE NPI = '"
							+ testData.get("NPI") + "'";
					db.sqlUpdate(insertSuccessQuery);

				} catch (Exception e1) {
//				String updateAsNotSuccess = "UPDATE [dbo].[hrp_non_pcp_supplier_screen_data] SET Screen_Scan_Status = 'UnSuccessful' WHERE [SUPPLIER_ID] = '"+tempDetails.get("SUPPL_ID")+"'" ; 
//				db.sqlUpdate(updateAsNotSuccess);		
					e1.printStackTrace();
				}
			} else { // get data from hrp_non_pcp_supplier_screen_data
				getSupplierLocationFromDB(tempDetails);

				String insertSuccessQuery = "UPDATE [dbo].[hrp_pgr_provider_supplier_details] SET Screen_Scan_Status = 'Success' WHERE NPI = '"
						+ testData.get("NPI") + "'";
				db.sqlUpdate(insertSuccessQuery);
			}

			getWinElement(LocatorTypes.name, HRPObjRepo.HRPTobBar.back_name).click();
			wait(2);
		} catch (Exception e2) {
			e2.printStackTrace();
		}

	}

	public void getSupplierLocationFromDB(HashMap<String, String> tempDetails) {

		String query = "SELECT * FROM [dbo].[hrp_non_pcp_supplier_screen_data] WHERE SUPPLIER_ID = '"
				+ tempDetails.get("SUPPL_ID") + "' and [SCREEN_SCAN_STATUS] = 'Success'";

		List<HashMap<String, String>> list = db.getListOfHashMapsFromResultSet(query);
		logger.info(list.size());
		for (HashMap<String, String> row : list) {
			HashMap<String, String> supplierData = tempDetails;
			HashMap<String, String> dbRecord = new HashMap<String, String>();
			for (String key : row.keySet()) {
				dbRecord.put(key, row.get(key));
			}
			supplierData.put("County", dbRecord.get("COUNTY"));
			supplierData.put("Address", dbRecord.get("ADDRESS"));
			supplierData.put("Suite", dbRecord.get("SUITE"));
			supplierData.put("ZipCode", dbRecord.get("ZIPCODE"));
			supplierData.put("City", dbRecord.get("CITY"));
			supplierData.put("State", dbRecord.get("STATE"));
			supplierData.put("PhoneNum", dbRecord.get("PHONENUMBER"));

			db.insert_HRP_ProviderSupplierDetailsToDB(supplierData);
		}

	}

	public void getSupplierLocationFromScreen(HashMap<String, String> tempDetails) {

		String link_name = null;
		int samAddresslinkCounter = 1;
		for (int i = 1; isWinElementPresent(LocatorTypes.name, "Row " + i); i++) {

			WebElement locationNameRow = getWinElement(LocatorTypes.name,
					HRPObjRepo.HRPSupplierViewDetails.locationNameRow_name + (i - 1));

			link_name = locationNameRow.getText();

			String query = "select count(0) as count from [dbo].[hrp_non_pcp_supplier_screen_data] where supplier_ID = "
					+ "'" + tempDetails.get("SUPPL_ID") + "' and ADDRESS_LINK = '" + link_name
					+ "' and screen_scan_status is null";
			if (!db.checkForExistingRecord(query)) {
				getWinElement(LocatorTypes.name, HRPObjRepo.HRPSupplierViewDetails.locationNameRow_name + (i - 1))
						.click();
				wait(2);

				String County = getWinElement(LocatorTypes.id, HRPObjRepo.HRPSupplierViewDetails.county_id).getText();
				String Address = getWinElement(LocatorTypes.id, HRPObjRepo.HRPSupplierViewDetails.address_id).getText();
				String Suite = getWinElement(LocatorTypes.id, HRPObjRepo.HRPSupplierViewDetails.address2_id).getText();
				String ZipCode = getWinElement(LocatorTypes.id, HRPObjRepo.HRPSupplierViewDetails.zipCode_id).getText();
				String City = getWinElement(LocatorTypes.id, HRPObjRepo.HRPSupplierViewDetails.city_id).getText();
				String State = getWinElement(LocatorTypes.id, HRPObjRepo.HRPSupplierViewDetails.state_id).getText();

				List<WebElement> phoneNumbers = getWinElements(LocatorTypes.id,
						HRPObjRepo.HRPSupplierViewDetails.phoneNumber);
				List<WebElement> areaCodes = getWinElements(LocatorTypes.id,
						HRPObjRepo.HRPSupplierViewDetails.phoneAreaCode);
				int j = 0;
				StringBuilder phoneNumBuilder = new StringBuilder();
				for (WebElement phoneNumber : phoneNumbers) {

					String phoneNum = phoneNumber.getText();
					String code = areaCodes.get(j).getText();

					phoneNum = code + phoneNum;

					phoneNumBuilder.append(phoneNum);
					phoneNumBuilder.append(",");

				}

				try {
					reportPass("Data extracted from HRP Screen");
				} catch (IOException e) {
					e.printStackTrace();
				}

				tempDetails.put("County", County);
				tempDetails.put("Address", Address);
				tempDetails.put("Suite", Suite);
				tempDetails.put("ZipCode", ZipCode);
				tempDetails.put("City", City);
				tempDetails.put("State", State);
				tempDetails.put("PhoneNumber", phoneNumBuilder.toString());
				tempDetails.put("SameAddressCounter", Integer.toString(samAddresslinkCounter));
				tempDetails.put("AddressLinkName", link_name);

				db.insert_HRP_Non_PCP_Supplier_Screen_Data_To_DB(tempDetails);

				db.insert_HRP_ProviderSupplierDetailsToDB(tempDetails);

				getWinElement(LocatorTypes.name, HRPObjRepo.HRPTobBar.back_name).click();
				wait(2);

			} else {
				String SameAddressCounter_updateQuery = "update [dbo].[hrp_non_pcp_supplier_screen_data] set SAME_ADDRESS_COUNTER = SAME_ADDRESS_COUNTER +1 where supplier_ID = "
						+ "'" + tempDetails.get("SUPPL_ID") + "' and ADDRESS_LINK = '" + link_name
						+ "' and screen_scan_status is null";

				db.sqlUpdate(SameAddressCounter_updateQuery);
			}
			if (i > 13) {
				if (isWinElementPresent(LocatorTypes.name, HRPObjRepo.HRPSupplierViewDetails.lineDown_name)) {
					WebElement we = getWinElement(LocatorTypes.name, HRPObjRepo.HRPSupplierViewDetails.lineDown_name);
					for (int j = 1; j <= (i - 13); j++) {
						we.click();
					}

				}
			}
		}
	}
}
