package pageobjects;

public class CallidusObjRepo {

	public class CallidusLogin {

		public static final String userid_name = "UsrSet_UserId";
		public static final String password_name = "UsrSet_UserPswd";
		public static final String login_id = "login-button-button";
	}

	public class CallidusCommon {
		public static final String tablerow1_xpath = "//*[@id='gridview-1010-record-0']";
		public static final String tablerow1Column2_xpath = "//*[@id='gridview-1010-record-0']/td[2]/div";
		public static final String tablerow1Column3_xpath = "//*[@id='gridview-1010-record-0']/td[3]/div";
		public static final String tablerow1Column5_xpath = "//*[@id='gridview-1010-record-0']/td[5]/div";
		public static final String tablerow1Column6_xpath = "//*[@id='gridview-1010-record-0']/td[6]/div";
		public static final String tablerow2Column2_xpath = "//*[@id='gridview-1010-record-1']/td[2]/div";
		public static final String tablerow2Column16_xpath = "//*[@id='gridview-1010-record-0']/td[16]/div";
		public static final String searchbutton_id = "Search";
		public static final String clearButton_id = "Clear";

	}

	public class CallidusHomePageMenuBar {
		// Menu Bar Links
		public static final String producers_xpath = "//div[@class='a3mMenuTabs']//a[contains(@id,'Producers')]";
		public static final String customers_xpath = "//div[@class='a3mMenuTabs']//a[contains(@id,'Customers')]";
	}

	public class CallidusCustomerPage {
		// Customer Page search fields
		public static final String customerId_id = "fld_Customer_CustomerId";
		public static final String searchbutton_id = "Search";
		public static final String clearCustomer_id = "CustomerClear";
		// Customer demographics fields
		public static final String customerNameId = "fld_Customer_Name";

		// Customer Page Left Side Links
		public static final String policieslink_id = "mtm_Customers_Customers_CustomerSFI_CustomerDFI_CustomerCustPolicySFI";
		public static final String productOwnership_id = "mtm_Customers_Customers_CustomerSFI_CustomerDFI_CustomerMatchSearchTable";
		public static final String transactionHistoryLink_id = "mtm_Customers_Customers_CustomerSFI_CustomerDFI_BrokerHistoryCustomerSFI";
		public static final String cmsCycleYear_id = "mtm_Customers_Customers_ExtCrossRefDynamicSFIMARxCycleYear";
		public static final String producerOwnershipLink_id = "mtm_Customers_Customers_CustomerSFI_CustomerDFI_CustomerMatchSearchTable";

		public static final String row1Checkbox_xpath = "//tr[contains(@id, 'record-0')]/td[1]";
		public static final String row1CustID_xpath = "//tr[contains(@id, 'record-0')]/td[2]/div/a";

		public static final String customerName_name = "Customer_Name";
		// Customer Demographics details in Policies Screen

		public static final String custFirstName_name = "CustPolicy_HolderFirstName";
		public static final String custMiddleIni_name = "CustPolicy_HolderMiddleInitial";
		public static final String custLastName_name = "CustPolicy_HolderLastName";
		public static final String custDOB_name = "CustPolicy_HolderDOB";
		public static final String custSSN_name = "CustPolicy_HolderSSN";

		// Customer Address Details
		public static final String custStreetAddress_name = "CustPolicy_HolderStreet";
		public static final String custCity_name = "CustPolicy_HolderCity";
		public static final String custState_name = "CustPolicy_HolderState";
		public static final String custZipCode_name = "CustPolicy_HolderZip";
		public static final String custCountyName_name = "CustPolicy_CountyName";
		public static final String custCountyCode_name = "CustPolicy_CountyCode";
		public static final String custPhoneNum_name = "CustPolicy_HolderPhone";

		// Customer Plan Details
		public static final String MBI_name = "CustPolicy_HICN";
		public static final String effectiveDate_name = "CustPolicy_DatEff";
		public static final String terminationDate_name = "CustPolicy_DatExp";
		public static final String contractNumber_name = "CustPolicy_ContractNbr";
		public static final String planID_name = "CustPolicy_PBP";
		public static final String productName_name = "CustPolicy_ProCode";
		public static final String planType_name = "CustPolicy_PlanType";
		public static final String applicationSignDate_name = "CustPolicy_AppSignedDate";
		public static final String applicationReceivedDate_name = "CustPolicy_AppRcvDate";

		// AgentDetails

		public static final String producerType_row1_xpath = "//tr[contains(@id, 'record-0')]/td[6]/div";
		public static final String producerType_row2_xpath = "//tr[contains(@id, 'record-1')]/td[6]/div";

	}

	public class CallidusAgentPage {

		public static final String processPeriodDropdown_name = "BrokerHistory_BillPeriod";
		public static final String premiumPeriodDropdown_name = "BrokerHistory_PremPeriod";
		public static final String BrokerType_name = "BrokerHistory_RecType";
		public static final String sortByPremPerdHeader_id = "BrokerHistoryCustomerSFI_col_0-titleEl";
		public static final String sortByPremPeriodDropdownArrow_xpath = "//*[@id='BrokerHistoryCustomerSFI_col_0-triggerEl'] ";
		public static final String sortByPremPeriodDescending_xpath = "//*[@id='menuitem-1015-textEl']";
		public static final String contractType_xpath = "(//*[@class='detailForm'])[5]/tbody/tr[3]/td[4]";
		public static final String commission_xpath = "(//table[@class=\"detailForm\"])[2]/tbody/tr[4]/td[2]";
		public static final String TIN_xpath = "(//table[@class='detailForm'])[1]/tbody/tr[5]/td[4]";
		public static final String producerType_xpath = "(//*[@class='detailForm'])[5]/tbody/tr[4]/td[2]";

		// AgencyAgent DEtails

		public static final String name_xpath = "//table[@class='detailForm'][1]/tbody/tr[5]/td[2]";
		public static final String phone_xpath = "(//table[@class='detailForm'])[2]/tbody/tr[2]/td[2]";
		public static final String email_xpath = "(//table[@class='detailForm'])[2]/tbody/tr[3]/td[4]";

	}

	public class CallidusCMSCycleYear {
		public static final String mbi_xpath = "//input[@id='fld_ExtCrossRef_FValue1']";

	}
	
	public class CallidusProducerOwnershipPage{
		public static final String exppirationDate = "(//table[@class='detailForm'])[1]/tbody/tr[2]/td[4]";
	}
}
