package pageobjects;

public class CommissionReportObjRepo {
	
	public class HomePage{
		public static final String reportingTab_xpath = "//*[@id=\'mainMegaNav\']/li[2]/span"; 
		public static final String commissionReport_xpath = "//*[@id=\'mainMegaNav\']/li[2]/div/div/ul/li[2]/a";
		public static final String runReportbtn_id = "btnRunReport";
	}
	
	public class MedicareCommisssionReport{
		public static final String medicareHeaders_className = "tableHeader";
		public static final String subscriberList_xpath = "//*[@id=\'MedicareCommissionResult\']/tbody/tr";
	}
}
