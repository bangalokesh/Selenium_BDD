package pageclasses.hrp;

import java.io.IOException;
import java.util.List;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import enums.LocatorTypes;
import io.appium.java_client.windows.WindowsDriver;
import pageclasses.BasePage;
import pageobjects.HRPObjRepo;

public class HRPMemberSearch extends BasePage {

	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(HRPMemberSearch.class.getName());

	public HRPMemberSearch(WindowsDriver<WebElement> winDriver) {

	}

	public boolean searchMember() {
		boolean flag = true;
		try {
			getWinElement(LocatorTypes.name, HRPObjRepo.HRPTobBar.clear_name).click();
			WebElement w = getWinElement(LocatorTypes.name, HRPObjRepo.HRPMemberSearch.memberId_name);
			StringBuilder str = new StringBuilder(testData.get("SupplementalID").trim());
			StringBuilder SupplementalID = str.deleteCharAt(0).deleteCharAt(0).deleteCharAt(0);
			w.sendKeys(SupplementalID.toString() + Keys.TAB);
			if (getWinElement(LocatorTypes.name, HRPObjRepo.HRPMemberSearch.search_name).isEnabled())
				w.sendKeys(Keys.ENTER);
			int counter = 0;
			outerloop: while (counter < 5) {
				if (!isWinElementPresent(LocatorTypes.name, HRPObjRepo.HRPMemberSearch.searchErrorText_name)) {
					wait(2);
					counter++;
				} else {
					getWinElement(LocatorTypes.name, HRPObjRepo.HRPLogin.ok_name).click();
					flag = false;
					break outerloop;
				}
			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			try {
				winDriver.close();
				reportFail("Failure in Searching Member at HRPMemberSearch Page");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;

	}

	public boolean viewMember() {
		boolean flag = true;
		try {
			List<WebElement> result = getWinElements(LocatorTypes.name, HRPObjRepo.HRPMemberSearch.view_name);
			result.get(1).click();
			int counter = 0;
			outerloop: while (counter < 5) {
				if (!isWinElementPresent(LocatorTypes.name, HRPObjRepo.HRPMemberSearch.viewErrorText_name)) {
					wait(2);
					counter++;
				} else {
					getWinElement(LocatorTypes.name, HRPObjRepo.HRPLogin.ok_name).click();
					flag = false;
					break outerloop;
				}
			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			try {
				winDriver.close();
				reportFail("Failure in Searching Member at HRPMemberSearch Page");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}

	public void clickMainSearch() {
		try {
			if (isWinElementPresent(LocatorTypes.name, HRPObjRepo.HRPMemberSearch.search_name)) {
				getWinElement(LocatorTypes.name, HRPObjRepo.HRPMemberSearch.search_name).click();
			}
			reportPass("Clicked main search ");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Failure in clicking main search ");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
