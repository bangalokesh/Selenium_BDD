package utils;

import enums.Environments;

public class Const {

	public static final String LOG4JPROPERTIES = System.getProperty("user.dir") + "//src//test//resources//PropertyFiles//log4j.properties";
	
	public static final String REPORT_PATH = System.getProperty("user.dir") + "//reports//htmlReports//";
	
	public static final String ENVPROPERTIES = System.getProperty("user.dir") + "//src//test//resources//PropertyFiles//Environment.json";

	public static final String ENV = Environments.UAT.toString();
	
	public static final String EXTENTCONFIG = System.getProperty("user.dir") + "//src//test//resources//PropertyFiles//extent-config.xml";

	public static final String DEVICENAME = "TEST123SYSTEM";
	
	public static final String WINLOCALIP = "127.0.0.1";
	
	public static final String IMPLICITWAITTIME = "20";
	
	public static final String TESTPLANID = null;//"37542";//
	
	public static final String TESTSUITEID = null;//"47883";//
	
	public static final Integer RUNID = 0;//
	
	public static final String OECProperties = System.getProperty("user.dir") + "//src//test//resources//PropertyFiles//OECLayout.properties";

	public static final String OECCallCenterProperties = System.getProperty("user.dir") + "//src//test//resources//PropertyFiles//OECCallCenterLayout.properties";
	
	public static final String OECCLRProperties = System.getProperty("user.dir") + "//src//test//resources//PropertyFiles//OECWebPortalLayout.properties";
	
	public static final String OECAONProperties = System.getProperty("user.dir") + "//src//test//resources//PropertyFiles//OECAONLayout.properties";
	
	public static final String SAPConfigProperties = System.getProperty("user.dir") + "//src//test//resources//PropertyFiles//SAPConfig.properties";
	
	public static final String SAPDataFile = System.getProperty("user.dir") + "//data//SAP_File_Data//File_Data_Interim_Table.txt";
 	
	public static final String AccessDBPath = "C:\\Users\\lbanga01\\Git\\AccessDB_Lokesh\\velocity_automation.mdb";

	public static final String OECFILEPATH = System.getProperty("user.dir") + "//reports//oec_files//";
	
	public static final String AZUREAPIPATH = "https://dev.azure.com/AZBlue/OneAZBlue/_apis"; 
	
	public static final String AzureKey = "Basic bGJhbmdhQGRlbG9pdHRlLmNvbTo0NHQ2Y3FvNWM3enhyN3ZrYmF6cnJqbmpta3p2YTZnc2x1eHl2aGF4eXVpamR2Yng1eXZx";

	public static final String HRPPATH = "";
	
	public static final String HRPEnvironment = "UAT (BCAZ)";

	public static final String PaymentFilePath = System.getProperty("user.dir") + "//reports//paymentFile//";
	
	public static final String TRR_PROPERTIES_FILE = System.getProperty("user.dir") +  "//src//test//resources//PropertyFiles//TRRLayout.properties";
	
	public static final String TRR_FILEPATH = System.getProperty("user.dir") + "//reports//TRR_Files//";
	
	public static final String MEM_ID_00_PROPERTY_FILE = System.getProperty("user.dir") +  "//src//test//resources//PropertyFiles//Member_ID_Card_00.properties";
	
	public static final String MEM_ID_01_PROPERTY_FILE = System.getProperty("user.dir") +  "//src//test//resources//PropertyFiles//Member_ID_Card_01.properties";
	
	public static final String ID_Card_Member_Extract = System.getProperty("user.dir") +  "//data//Member_ID_Card//";
	
	public static final String ID_Card_Member_Extract_Archive = System.getProperty("user.dir") +  "//data//Member_ID_Card_Archive//";
	
	public static final String ClaimFilePath = System.getProperty("user.dir") +  "//data//ClaimFile//";
	
	public static final String EligibilityFilePath = System.getProperty("user.dir") +  "//data//EligibilityFile//";
	
	public static final String EnrollmentFilePath = System.getProperty("user.dir") + "//data//EnrollmentFile//";
	
	public static final String tessDataMaster = System.getProperty("user.dir") + "//src//main//resources//tessdata-master//";
	
	public static final int Number_of_service_requests = 1;
	
	//----------------------------------------------------
	public static final String SAPDBQuery1 = "select ZSRC_SYS_CD, ZLE_SRC_ID, ZLE_ID, ZVALID_FROM, ZVALID_TO from saptest.sapdata";
	
	public static final String SAPDBQuery2 = "SELECT ZSRC_SYS_CD, ZLE_SRC_ID, ZLE_ID, ZVALID_FROM, ZVALID_TO FROM saptest.sapdata WHERE ZSRC_SYS_CD = ";


}
