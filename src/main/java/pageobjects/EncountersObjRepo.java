package pageobjects;

public class EncountersObjRepo {

	public class Encounters{
		public static final String RiskManagementField_id = "td5";
		public static final String encountersLink_xpath = "//*[@id=\"td5\"]/div/a[2]";
		public static final String encountersDetail_class = "optionButtonHpe";
		public static final String encountersDetail_xpath = "/html/body/form/table/tbody/tr[4]/td/table/tbody/tr/td[1]/table/tbody/tr/td[3]/input";
	}
	
	public class EncounterDetails {
		
		public static final String claimTypeDropdown_xpath = "//*[starts-with(@id, 'select2-searchDetailEncType-') and contains(@id, 'container')]";
		public static final String claimTypeDropdown_name = "searchDetailEncType";
		
		public static final String submitterIdDropdown_xpath = "//*[starts-with(@id, 'select2-searchDetailSubmitterId-') and contains(@id, 'container')]";
		public static final String submitterIdTextField_xpath = "/html/body/span/span/span[1]/input";
		
		public static final String claimNumberTextField_name = "searchDetailClaimRefNbr";
		public static final String goButton_id = "btnGoGif";
		public static final String statusTableRowData_xpath = "//*[@id=\"profListTbl\"]/tbody/tr[2]/td[5]";
		public static final String claimTypeTextField_class = "select2-search__field";
		
		public static final String subscriberExpandButton_id = "img1";
		public static final String claimExpandButton_id = "img2";
		public static final String providerExpandButton_id = "img4";
		public static final String claimCodesExpandButton_xpath = "//*[@id=\"profClaimDiv\"]/table[2]/tbody/tr[1]/td/table/tbody/tr[2]/td/img[1]";
		public static final String diagnosisCodesExpandButton_id = "img12";
		
		public static final String subscriber_div_id = "profSubscriberDiv";
		public static final String subscriber_name_xpath = "//*[@id=\"subscriberDisplay\"]/table/tbody/tr[1]/td/table/tbody/tr[1]/td[2]";
		public static final String subscriber_medicareID_xpath = "//*[@id=\"subscriberDisplay\"]/table/tbody/tr[1]/td/table/tbody/tr[1]/td[6]";
		public static final String subscriber_hicn_xpath = "//*[@id=\"subscriberDisplay\"]/table/tbody/tr[1]/td/table/tbody/tr[1]/td[4]";
		
		public static final String claim_totalClaimChargeAmount_xpath = "//*[@id=\"claimDisplay\"]/table/tbody/tr/td/table/tbody/tr[1]/td[4]";
		public static final String claim_placeOfService_xpath = "//*[@id=\"claimDisplay\"]/table/tbody/tr/td/table/tbody/tr[2]/td[8]";
		public static final String claim_claimFreqTypeCode_xpath = "//*[@id=\"claimDisplay\"]/table/tbody/tr/td/table/tbody/tr[3]/td[8]";
		public static final String claim_processStatus_xpath = "//*[@id=\"claimDisplay\"]/table/tbody/tr/td/table/tbody/tr[1]/td[6]";
		
		public static final String provider_npi_xpath = "//*[@id=\"prvDisplay\"]/table/tbody/tr[1]/td/table[2]/tbody/tr[2]/td[6]";
		public static final String provider_affilTaxID_xpath = "//*[@id=\"prvDisplay\"]/table/tbody/tr[1]/td/table[2]/tbody/tr[4]/td[4]";
		
		public static final String diagnosisTableList_id = "profDiagCodeTbodyDisp";
		public static final String diagnosisCodeRow_xpath = "//*[@id=\"profDiagCodeTbodyDisp\"]/tr";
		public static final String claimLineExpandButton_id = "profClmLineImgToggle";
		public static final String claimLineTable_xpath = "//*[@id=\"tblProfClaimLineList\"]/tbody/tr";
		public static final String claimLineDetailDiagcdPtr1_id = "liDiagcdPtr1";
		public static final String claimLineDetailDiagcdPtr2_id = "liDiagcdPtr2";
		public static final String claimLineDetailDiagcdPtr3_id = "liDiagcdPtr3";
		public static final String claimLineDetailDiagcdPtr4_id = "liDiagcdPtr4";
		
	}
	
}
