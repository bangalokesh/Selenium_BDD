package pageclasses.hrp;

import java.io.IOException;
import java.sql.ResultSet;
import java.util.HashMap;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;

import enums.LocatorTypes;
import io.appium.java_client.windows.WindowsDriver;
import pageclasses.BasePage;
import pageobjects.HRPObjRepo;

public class HRPSupplierSearch extends BasePage {

	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(HRPSupplierSearch.class.getName());

	public HRPSupplierSearch(WindowsDriver<WebElement> winDriver) {

	}

	public HashMap<String, String> searchSupplierByTaxID(ResultSet testData) {
		HashMap<String, String> searchResult = new HashMap<String, String>();
		try {
			wait(3);
			WebElement w = getWinElement(LocatorTypes.id, HRPObjRepo.HRPSupplierSearch.taxID_id);
			w.sendKeys(testData.getString("TAX_ID") + Keys.ENTER);
			wait(5);
			if (isWinElementPresent(LocatorTypes.name, HRPObjRepo.HRPOkAlert.noDataFoundWindow_name)) {
				reportFail("Supplier Tax ID Not Found");
				getWinElement(LocatorTypes.name, HRPObjRepo.HRPOkAlert.ok_name).sendKeys(Keys.ENTER);
			} else {
				String[] tempaddress = getWinElement(LocatorTypes.name, HRPObjRepo.HRPSupplierSearch.address_name + "0")
						.getText().split(",");
				searchResult.put("SupplierID",
						getWinElement(LocatorTypes.name, HRPObjRepo.HRPSupplierSearch.supplierID_name + "0").getText());
				searchResult.put("SupplierName",
						getWinElement(LocatorTypes.name, HRPObjRepo.HRPSupplierSearch.supplierName_name + "0")
								.getText());
				searchResult.put("Address", tempaddress[0]);
				searchResult.put("City", tempaddress[1]);
				searchResult.put("State", tempaddress[2]);
				searchResult.put("ZipCode", tempaddress[3]);
			}
			Actions a = new Actions(winDriver);
			a.click(getWinElement(LocatorTypes.name, HRPObjRepo.HRPTobBar.searchFor_name)).sendKeys("p").build()
					.perform();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				winDriver.close();
				reportFail("Failure in Searching Supplier at HRPSupplierSearch Window");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return searchResult;
	}

	public void searchSupplierByNPI() {

	}
}
