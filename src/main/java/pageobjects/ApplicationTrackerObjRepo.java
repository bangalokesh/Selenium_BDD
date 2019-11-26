package pageobjects;

public class ApplicationTrackerObjRepo {

	public class HomePage{
		public static final String planTools_xpath = "//*[@id=\'mainMegaNav\']/li[1]/span";
		public static final String appTracker_xpath = "//*[@id=\'mainMegaNav\']/li[1]/div/div/ul/li[2]/a";
		
	}
	
	public class AppTrackerPage{
		public static final String appType_xpath = "//*[@id=\'SelectedAppType_chosen\']/ul/li/input";
		public static final String addTypes_id	= "AddAllAppTypes";
		public static final String lastDays_xpath = "//*[@id=\'l180days\']";
		public static final String searchButton_id = "btnSearch";
		public static final String agendID_xpath = "//*[@id=\'ApplicationTracking\']/div[4]/div/div[2]/div[1]";
	}
	
	public class AppTrackerStatus{
		
		public static final String subscriberList_xpath = "//*[@id=\'AppTrackingResult\']/tbody/tr";

	}
}
