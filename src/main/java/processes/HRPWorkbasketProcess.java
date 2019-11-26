package processes;

import java.util.HashMap;
import java.util.List;

import org.openqa.selenium.Keys;
import org.openqa.selenium.interactions.Actions;

import enums.LocatorTypes;
import managers.PageManager;
import pageclasses.BasePage;
import pageobjects.HRPObjRepo;
import utils.Const;
import utils.Dbconn;

public class HRPWorkbasketProcess extends BasePage{

	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(HRPWorkbasketProcess.class.getName());
	Dbconn db = new Dbconn();
	PageManager pm = new PageManager();
	
	public HRPWorkbasketProcess() {
		String appName = Const.HRPPATH;
		driver = getWinDriver(appName);
	}
	
	public void clearWorkBasket() {	
		getWinElement(LocatorTypes.name, HRPObjRepo.HRPWorkBasket.workbasketDropDown_name).click();
		Actions action = new Actions(driver);
		action.sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_DOWN)
		.sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_DOWN)
		.sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_DOWN)
		.sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_DOWN)
		.sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_DOWN)
		.sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ARROW_DOWN).sendKeys(Keys.ENTER).build().perform();
		//.moveToElement(getWinElement(LocatorTypes.name, HRPObjRepo.HRPWorkBasket.enrollReviewRepair_name)).click()
		
		//click on member
		getWinElement(LocatorTypes.name, HRPObjRepo.HRPWorkBasket.subscriber0_name).click();
		pm.getHRPWorkBasketPage().assignToGroup();
		
		
	}
	
	
//	public void createWorkBasket() {
//		String query = "     select M1.SupplementalID, RE1.ID, RE1.ProductPlanName, M1.MEDICAREID,\r\n" + 
//				"	 M1.firstname, M1.middleInitial, m1.lastname, m1.birthdate, m1.ssn,\r\n" + 
//				"     m1.effectivemonth, m2.PlanID, m2.PBPID, RE1.PCPNPI\r\n" + 
//				"	 from [VelocityTestAutomation].[dbo].[test_data_readytoenroll] RE1 	 join [VelocityTestAutomation].[dbo].[member_demographic] M1 \r\n" + 
//				"	 on RE1.MedCareID = M1.MedicareID \r\n" + 
//				"	 join [VelocityTestAutomation].[dbo].[member_enrollment] M2 \r\n" + 
//				"	 on M1.MedicareID = M2.MedicareID \r\n" + 
//				"	 where runmode = 'Y' and [ApplicationStatus] in ('EAPRV') and HRPMemValStatus is null and CMSEnrollmentStatus ='CMSAPRV' and M1.WorkStream = 'Enrollment';";
//		
//		List<HashMap<String, String>> list = db.getListOfHashMapsFromResultSet(query);
//		for (HashMap<String, String> row : list) {
//			HashMap<String, String> temptestData = new HashMap<String, String>();
//			for (String key : row.keySet()) {
//				temptestData.put(key, row.get(key));
//			}
//			setrowTestData(temptestData);
//			boolean searchflag = pm.getHrpMemSearchPage().searchMember();
//			if (searchflag == true) {
//				pm.getHRPWorkBasketPage().assignToGroup();
//				pm.getHRPWorkBasketPage().approveMember();
//			}
//			
//		}
//		
//	}
	

	
	
}
