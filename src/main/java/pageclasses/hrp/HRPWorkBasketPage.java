package pageclasses.hrp;


import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import enums.LocatorTypes;
import io.appium.java_client.windows.WindowsDriver;
import managers.PageManager;
import pageclasses.BasePage;
import pageobjects.HRPObjRepo;
import utils.Dbconn;

public class HRPWorkBasketPage extends BasePage{

	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(HRPWorkBasketPage.class.getName());
	Dbconn db = new Dbconn();
	PageManager pm = new PageManager();
	
	public HRPWorkBasketPage(WindowsDriver<WebElement> winDriver) {}
	
	public void assignToGroup() {
		
		getWinElement(LocatorTypes.name, HRPObjRepo.HRPWorkBasket.assign_name).click();
		getWinElement(LocatorTypes.name, HRPObjRepo.HRPWorkBasket.assignGroup_name).click();
		getWinElement(LocatorTypes.name, HRPObjRepo.HRPWorkBasket.assignGroup_name).sendKeys("hcc_super_user" + Keys.ENTER);
		getWinElement(LocatorTypes.name, HRPObjRepo.HRPWorkBasket.assignOKBtn_name).click();
		
		//take ownership
		getWinElement(LocatorTypes.name, HRPObjRepo.HRPWorkBasket.takeOwnership_name).click();
		
	}

	public void approveMember() {
		getWinElement(LocatorTypes.name, HRPObjRepo.HRPWorkBasket.openForEdit_name).click();
		
		
	}

	

	
}
