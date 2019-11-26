package pageobjects;

public class GuidingCareObjRepo {

	public class GuidingCareLogin {
		public static final String userName_id = "UserName";
		public static final String password_id = "Password";
		public static final String submitButton_id = "btnLogin";
	}

	public class GuidingCareLogout {
		public static final String logoutDropdownArrow_xpaath = "//div[@class='logBox']/div[@class='gearmenu']";
		public static final String logoutLink_id = "lnkLogout";
	}

	public class GuidingCareLandingPage {
		public static final String searchMember_id = "txtSearch";

		public static final String memberName_id = "//input[@type='button']";
		public static final String memName_id = "txtSearch";
		public static final String button_xpath = "//*[@id='memberSerach']/div/div/div/div[3]/div[1]/input";
		public static final String memName_xpath = "//*[@id=\'txtSearch\']";

		public static final String MBISearchLink_xpath = "//*[@id='memberSerach']/div/div/div/div[1]/span/span";
		public static final String dropDownMBI_xpath = "//*[@id='selectSearch_listbox']/li[3]";
		public static final String searchDropdownLink_xpath = "//*[@id='memberSerach']/div/div/div/div[1]/span/span/span[2]/span";
		public static final String searchArrow_xpath = "//*[@id=\"memberSerach\"]/div/div/div/div[1]/span/span/span[2]";
		public static final String searchList_xpath = "//*[@id=\"selectSearch_listbox\"]/li";
		public static final String homePageMemberInfo_xpath = "//*[@id=\"lnkMemberInfo\"]";
		public static final String popuUpWindowLabel_xpath = "//div[@id='popUpDialog1']/div[@id='OpenSearchResultForm']/div[1]/label";
		public static final String closePopUpWindow_xpath = "//div[@id='popUpDialog1']/div[1]/div/a[@id='closePopUp']";
		public static final String popUpTableData_xpath = "//*[@id=\"GridQuickSearch\"]/div[2]/table";

		public static final String MBISearch_id = "txtSearch";
		public static final String memberID_id = "txtSearch";
		public static final String dropDownMemberID_xpath = "//*[@id='selectSearch_listbox']/li[5]";
		public static final String memberIDSearchLink_xpath = "//*[@id='memberSerach']/div/div/div/div[1]/span/span";

		public static final String searchDropdown_xpath = "//*[@id=\"memberSerach\"]/div/div/div/div[1]/span/span/span[1]";

		public static final String authorizationsLink_id = "btnClickAuth";
		public static final String authorizationsLink_xpath = "//*[@id='btnClickAuth']/i";
		public static final String AuthIDTextField_id = "txtASAuthId";
		public static final String AuthSearchButton_id = "btnPatientAuthorizationSearch";
		public static final String AuthResult_xpath = "//*[@id='DivAuthSummary']/div/div[2]/table/tbody/tr/td[1]/a";

		public static final String careGiverTab_id = "lnkCareGivers";
		public static final String careTeamTab_id = "careTeamTab";
		public static final String eligiblityTab_id = "lnkMemberEligibility";
		public static final String searchDropdownArrow_id = "searchAdvanceId";
		public static final String providerSearchLink_id = "lnkProviderSearch";
		public static final String returnToHomePage_id = "btnCC";
		public static final String memberSearchLink_id = "lnkMemberSearch";

		public static final String memberDetailsIcon_id = "ImgGotoCareRecord";
		public static final String inPatient_id = "imgInpatient";
		public static final String outPatient_id = "imgOutPatient";
		public static final String pharmacy_id = "imgPharmacy";
		public static final String HCBS_id = "imgHCServices";

	}

	public class GuidingCareMember {

		// CareGiver Tab
		public static final String firstCheckbox_xpath = "//input[contains(@id,'ChkBoxItem')]";
		public static final String viewButton_xpath = "//*[@id ='BtnView']";

		public static final String cg_firstName_xpath = "//*[@id='divViewName'][1]/table/tbody/tr/td[3]";
		public static final String cg_lastName_xpath = "//*[@id='divViewName'][2]/table/tbody/tr/td[3]";
		public static final String cg_middleName_xpath = "//*[@id='divViewName'][3]/table/tbody/tr/td[3]";
		public static final String cg_DOB_xpath = "//*[@id='divViewName'][4]/table/tbody/tr/td[3]";
		public static final String cg_Gender_xpath = "//*[@id='divViewName'][5]/table/tbody/tr/td[3]";
		public static final String cg_relation_xpath = "//*[@id='divViewName'][8]/table/tbody/tr/td[3]";
		public static final String cg_homePhone_xpath = "//*[@id='divViewName'][11]/table/tbody/tr/td[3]";
		public static final String cg_cellPhone_xpath = "//*[@id='divViewName'][12]/table/tbody/tr/td[3]";
		public static final String cg_primaryEmail_xpath = "//*[@id='divViewName'][13]/table/tbody/tr/td[3]";
		public static final String cg_fax_xpath = "//*[@id='divViewName'][16]/table/tbody/tr/td[3]";
		public static final String cg_address_xpath = "//*[@id='divViewName'][17]/table/tbody/tr/td[3]";
		public static final String cg_city_xpath = "//*[@id='divViewName'][20]/table/tbody/tr/td[3]";
		public static final String cg_state_xpath = "//*[@id='divViewName'][19]/table/tbody/tr/td[3]";
		public static final String cg_county_xpath = "//*[@id='divViewName'][21]/table/tbody/tr/td[3]";
		public static final String cg_zipcode_xpath = "//*[@id='divViewName'][18]/table/tbody/tr/td[3]";
		public static final String cg_isPrimary_xpath = "//*[@id='divViewName'][25]/table/tbody/tr/td[3]";
		public static final String cg_alternatePhone_xpath = "//*[@id='divViewName'][15]/table/tbody/tr/td[3]";
		public static final String cg_alternateEmail_xpath = "//*[@id='divViewName'][14]/table/tbody/tr/td[3]";
		public static final String cg_primaryLanguage_xpath = "//*[@id='divViewName'][9]/table/tbody/tr/td[3]";
		public static final String cg_closePopUp_id = "closePopUp";

		// CareTeam
		public static final String ct_startDate_xpath = "//*[@id=\"careTeamGrid\"]/div[2]/table/tbody/tr/td[14]";
		public static final String ct_EndDate_xpath = "//*[@id=\"careTeamGrid\"]/div[2]/table/tbody/tr/td[15]";
		public static final String ct_PCP_xpath = "//*[@id=\"careTeamGrid\"]/div[2]/table/tbody/tr/td[5]/div";

	}

	public class GuidingCareProvider {
		public static final String providerCodeTextField_id = "txtProviderID";
		public static final String searchButton_id = "btnSearch";
		public static final String networkNameTextField_id = "ddlNetworkName";
	}

	public class GuidingCareProvDetails {
		public static final String providerLink_xpath = "//*[@id='gvFindProvider']/div[2]/table/tbody/tr/td[3]/a";
		public static final String providerList_xpath = "//*[@id='gvFindProvider']/div[2]/table/tbody/tr";
		public static final String provInfoHeader_xpath = "//*[@id='popUpDialog4']/div[1]";
		public static final String provGender_xpath = "//*[@id='popUpDialog4']/div[2]/div[1]/div[4]/div[1]/span[1]";
		public static final String acceptPatients_xpath = "//*[@id='popUpDialog4']/div[2]/div[1]/div[4]/div[3]/span";
		public static final String lob1_xpath = "//*[@id='provEnrollmentDetails']/div[2]/table/tbody/tr[1]/td[1]/div/span";
		public static final String lob2_xpath = "//*[@id='provEnrollmentDetails']/div[6]/table/tbody/tr[1]/td[1]/div/span";
		public static final String benefitNetworks1_xpath = "//*[@id='provEnrollmentDetails']/div[2]/table/tbody/tr[1]/td[2]/div/span";
		public static final String benefitNetworks2_xpath = "//*[@id='provEnrollmentDetails']/div[6]/table/tbody/tr[1]/td[2]/div/span";
		public static final String languageSpoken_xpath = "//*[@id='popUpDialog4']/div[2]/div[1]/div[4]/div[2]";
		public static final String speciality_xpath = "//*[@id='popUpDialog4']/div[2]/div[1]/div[3]";
		public static final String closePopup_xpath = "//a[@id='closePopUpUM']";
		public static final String providerID_xpath = "//*[@id=\"gvFindProvider\"]/div[2]/table/tbody/tr/td[5]";
		public static final String benefitNetworks_id = "provEnrollmentDetails";
		public static final String benefitNetworks_xpath = "//*[@id=\"provEnrollmentDetails\"]/div";
	}

	public class GuidingCareMemberDetails {
		public static final String searchByXBU_id = "ddlIndexName";
		public static final String memberSearch_id = "txtIndexID";
		public static final String memberSearchbtn_id = "btnFindCareMemberSearch";
		public static final String membersearchHyperLink_xpath = "//*[@id=\"MemAdvSearchGrid\"]/div[2]/table/tbody/tr/td[2]/a";
		public static final String membeDetailsTab_id = "MemberDetailsTab";
		public static final String memberName_xpath = "//div[@id='divViewName']";
		public static final String gender_xpath = "//div[@id='divViewGender']";
		public static final String dateOfBirth_xpath = "//div[@id='divViewDOB']";
		public static final String altruistaID_xpath = "//div[@id='divViewClientPatientId']";
		public static final String primaryLanguage_xpath = "//div[@id='divViewPrimaryLanguage']";
		public static final String preferredWrittenLanguage_xpath = "//div[@id='divViewWrittenLanguage']";
		public static final String primaryPhone_xpath = "//*[@id='divViewPhone']/table/tbody/tr/td[2]";
		public static final String cellPhone_xpath = "//*[@id='divViewCellPhone']";
		public static final String alternatePhone_xpath = "//*[@id='divViewAltPhone']";
		public static final String primaryEmail_xpath = "//*[@id='divViewPrimaryEmail']";
		public static final String ethnicity_xpath = "//*[@id='EthnicityName']";

		public static final String addressDetailsList_xpath = "//*[@id='MemberDetailsAddressGrid']/div[2]/table/tbody/tr";
		public static final String phoneNumberList_xpath = "//*[@id='PhoneNumberDetailsGrid']/div[2]/table/tbody/tr";

		public static final String healthInsuranceClaimNumber_xpath = "//*[@id=\'MemberIdentifiersDiv\']/div[8]";
		public static final String xbuNumber_xpath = "//*[@id=\'MemberIdentifiersDiv\']/div[10]";
		public static final String medicareBeneficiaryID_xpath = "//*[@id=\'MemberIdentifiersDiv\']/div[6]";
		public static final String m360ID_xpath = "//*[@id=\'MemberIdentifiersDiv\']/div[12]";

		public static final String phoneNumbersTab_id = "divViewMemberPhoneNumbersGridHeader";
		public static final String memberIdentifiersTab_id = "divViewMemberIdentifierHeader";
		public static final String addressesTab_id = "divViewMemberDetailsAddressGridHeader";
		public static final String address_xpath = "//*[@id='MemberDetailsAddressGrid']/div[2]/table/tbody/tr/td[2]";
		public static final String city_xpath = "//*[@id='MemberDetailsAddressGrid']/div[2]/table/tbody/tr/td[3]";
		public static final String state_xpath = "//*[@id='MemberDetailsAddressGrid']/div[2]/table/tbody/tr/td[4]";
		public static final String county_xpath = "//*[@id='MemberDetailsAddressGrid']/div[2]/table/tbody/tr/td[5]";
		public static final String zip_xpath = "//*[@id='MemberDetailsAddressGrid']/div[2]/table/tbody/tr/td[6]";
		public static final String addressType_xpath = "//*[@id='MemberDetailsAddressGrid']/div[2]/table/tbody/tr/td[7]";
		public static final String isPrimary_xpath = "//*[@id='MemberDetailsAddressGrid']/div[2]/table/tbody/tr/td[8]";

		// Eligibility Tab
		public static final String eligibilityTab_id = "lnkMemberEligibility";
		public static final String LOB_xpath = "//*[@id='divFullViewData']/div/div[1]/div/div[2]/div[1]/span[2]";
		public static final String LOBCode_xpath = "//*[@id='divFullViewData']/div/div[1]/div/div[2]/div[1]/span[4]";
		public static final String benefitNetworks_xpath = "//*[@id='divFullViewData']/div/div[2]/div/div/span[2]";
		public static final String benefitNetworksCode_xpath = "//*[@id='divFullViewData']/div/div[2]/div/div/span[4]";
		public static final String eligibilityID_xpath = "//*[@id='divFullViewData']/div/div[5]/span[2]";
		public static final String startDate_xpath = "//*[@id='divFullViewData']/div/div[1]/div/div[2]/div[2]/span[4]";
		public static final String endDate_xpath = "//*[@id='divFullViewData']/div/div[1]/div/div[2]/div[2]/span[6]";
		public static final String benifitPlan_xpath = "//*[@id='divFullViewData']/div/div[3]/div/div/span[2]";
		public static final String benifitPlanCode_xpath = "//*[@id='divFullViewData']/div/div[3]/div/div/span[4]";

		public static final String additionalInformationTab_id = "divViewAdditionalInformationHeader";
		public static final String relationshipCode_xpath = "//*[@id='AdditionalInfoDiv']/div[98]";
		public static final String personCode_xpath = "//*[@id='AdditionalInfoDiv']/div[96]";
		public static final String clientName_xpath = "//*[@id=AdditionalInfoDiv]/div[6]";
	}

	public class PreAuthorization {
		// *[@id="UmDdAuthType"]
		public static final String network_xpath = "//*[@id='divFullViewData']/div/div[2]/div[1]/div/span[2]";
		public static final String authTypeDropdown_id = "UmDdAuthType";
		public static final String authTypeDropdownSelectedText_xpath = "//*[@id='AuthInit_2_1']/div[2]/div/div[3]/table/tbody/tr/td[2]/span/span/span[1]";
		public static final String authTypeDropdownArrowButton_xpath = "//*[@id='AuthInit_2_1']/div[2]/div/div[3]/table/tbody/tr/td[2]/span/span/span[2]/span";
		public static final String authTypeNoRecordsFound_xpath = "//*[@id=\"MemEligMain_FullView\"]/div/center/div/span";
		public static final String authTypeDropdownTextField_xpath = "(//*[@id=\"UmDdAuthType-list\"]/span/input)[4]";;
		public static final String saveAndNext_id = "BtnSave";
		public static final String facilityProvNPIDropdown_xpath = "//*[@id ='DIV_22_UF_PROV_FACILITY_NAME']/table/tbody/tr/td/div[2]/select";
		public static final String admittingProvNPIDropdown_xpath = "//*[@id ='DIV_22_UF_PROV_ADMITTED_NAME_NAME']/table/tbody/tr/td/div[2]/select";
		public static final String facilityProvNameDropdown_xpath = "(//*[@id='DIV_22_UF_PROV_FACILITY_NAME'])/table/tbody/tr/td/div[2]/select";
		public static final String admittingProvNameDropdown_xpath = "(//*[@id='DIV_22_UF_PROV_ADMITTED_NAME_NAME'])/table/tbody/tr/td/div[2]/select";
		public static final String referredToProvNPIDropdown_xpath = "//*[@id ='DIV_22_UF_PROV_REFFTO_NAME']/table/tbody/tr/td/div[2]/select";
		public static final String facilityProvNameText_xpath = "(//*[@id='TextSsProviderName'])[1]";
		public static final String admittingProvNameText_xpath = "(//*[@id='TextSsProviderName'])[2]";
		public static final String referredbyProvNameText_xpath = "(//*[@id='TextSsProviderName'])[2]";
		public static final String referredToProvNameText_xpath = "(//*[@id='TextSsProviderName'])[3]";
		public static final String facilityFax_xpath = "(//*[@id='TextFax'])[1]";
		public static final String admittingFax_xpath = "(//*[@id='TextFax'])[2]";
		public static final String referredbyFax_xpath = "(//*[@id='TextFax'])[2]";
		public static final String referredToFax_xpath = "(//*[@id='TextFax'])[3]";
		public static final String notificationDate_id = "TextNotiDate";
		public static final String admissionDate_id = "TextAdmDate";
		public static final String authDetailDropdown_xpath = "//*[@id='DIV_23_UF_AUTH_PRIORITY']/div/div[2]/span[1]/span/span[1]";
		public static final String diagnosisDescription_xpath = "(//*[@id = 'TextSsCodeDesc'])[1]";
		public static final String procedureDescription_xpath = "(//*[@id = 'TextSsCodeDesc'])[2]";
		public static final String diagnosis_xpath1 = "//div[@id='AuthIcdCode']["; // concat diagnosis_xpath1 +
																					// rownumber
																					// +diagnosisAddButton_xpath2
		public static final String diagnosisAddButton_xpath2 = "]/table/tbody/tr/td[6]/input[@id='BtnIcdAdd']";
		public static final String diagnosisCode_xpath2 = "]/table/tbody/tr/td[5]/div[@id='DIV_24_UF_CODES_ADMISSION_CODE']/descendant::input[@id='TextSsCode']";
		public static final String procedure_xpath1 = "//div[@id='AuthCptCode'][";
		public static final String procedureAddButton_xpath2 = "]/div/div[2]/div[10]/div/table/tbody/tr/td/input[@id='BtnCptAdd']";
		public static final String procedureCode_xpath2 = "]/div/div[2]/div/div/div[2]/descendant::input[@id='TextSsCode']";
		public static final String procedureCode_xpath2_2 = "]/div/div[2]/div/div/div[2]/descendant::input[@id='TextSsCode']";
		public static final String prodecureModifier_xpath2 = "]/div/div[2]/div[2]/div/div[2]/table/tbody/tr/td/div";
		public static final String procedureUnitTypeDropdownArrow_xpath = "]/div/div[2]/div[4]/div/div[2]/span/span/span[2]";
		public static final String procedureUnitTypeCurrentText_xpath = "]/div/div[2]/div[4]/div/div[2]/span/span/span[1]";
		public static final String procedureFromDate_xpath = "]/div/div[2]/div[5]/div/div[2]/input";
		public static final String procedureToDate_xpath2 = "]/div/div[2]/div[6]/div/div[2]/input";
		public static final String requestedDays_xpath2 = "]/div/div[2]/div[7]/div/div[2]/input";
		public static final String unitTypeDropdown_id = "UmDdUnitType";
		public static final String fromDate_xpath = "(//*[@id='TextFromDate'])[1]";
		public static final String toDate_xpath = "(//*[@id='TextToDate'])[1]";
		public static final String reqDays_id = "TextReq";
		public static final String saveAndDecide_id = "BtnSaveDecide";
		public static final String dueDate_id = "LbldueDate";
		public static final String saveFinal_xpath = "(//*[@id='BtnSaveFinal'])[1]";
		public static final String preAuthID_id = "BtnAuth_View";
		public static final String decisionStatus_xpath = "//*[@id=\"GA_ADS_Row_0\"]/td[3]";
		public static final String auth_priorityDropdown_xpath = "//*[@id='DIV_23_UF_AUTH_PRIORITY']/div/div[2]/span[1]/span/span[2]/span";
		public static final String auth_priorityText_xpath = "//*[@id='DIV_23_UF_AUTH_PRIORITY']/div/div[2]/span[1]/span/span[1]";

		public static final String saveAndNextAuthBasics_xpath = "//*[@id='AuthInit_2_3']/div[2]/div/div[4]/div[2]/input";
		public static final String saveAndNextDiagnosisAndServiceCodes_xpath = "//*[@id='AuthInit_2_4']/div[2]/div[4]/div[3]/input";
		public static final String saveAndNextFinacialDetails_xpath = "//*[@id='AuthInit_2_5']/div[2]/div/div[4]/div[2]/input";
		public static final String saveAndNextAdditionalDetails_xpath = "//*[@id='AuthInit_2_6']/div[2]/div/div[8]/div[2]/input";
		public static final String saveAndNextAdditionalDetails_outpatient_xpath = "//*[@id='AuthInit_2_6']/div[2]/div/div[7]/div[2]/input";
		public static final String saveButton_xpath = "//*[@id='Accordion_Group_2']/div[10]/div[8]/input[1]";

		public static final String preAuthEntryLink_class = "AuthEntry";
		public static final String applicationStatusDropdownArrow_xpath = "(//*[@id=\"Accordion_Group_2\"]/div[10]/div[4]/div/div[2]/span/span/span[2]/span)[1]";
		public static final String applicationStatusText_xpath = "(//*[@id=\"Accordion_Group_2\"]/div[10]/div[4]/div/div[2]/span/span/span[1])[1]";
	}

	public class PreAuthorizationActivity {
		public static final String actionWindowDropdownArrowButton_xpath = "//*[@id='divUMActions']/i";
		public static final String addAuthActivity_id = "anchAddUMAct";
		public static final String activityTypeDropdownArrowButton_xpath = "//*[@id='AuthActivityForm']/div[4]/div[4]/span[1]/span/span[2]/span";
		public static final String activityTypeDropdownSelectedText_xpath = "//*[@id='AuthActivityForm']/div[4]/div[4]/span[1]/span/span[1]";
		public static final String priorityDropdownArrowButton_xpath = "//*[@id='AuthActivityForm']/div[5]/div[4]/span[1]/span/span[2]/span";
		public static final String priorityDropdownSelectedText_xpath = "//*[@id='AuthActivityForm']/div[5]/div[4]/span[1]/span/span[1]";
		public static final String assignedToDropdownArrowButton_xpath = "//*[@id='divUmActCareStaff']/div[4]/span[1]/span/span[2]/span";
		public static final String assignedToDropdownSelectedText_xpath = "//*[@id='divUmActCareStaff']/div[4]/span[1]/span/span[1]";
		public static final String pre_auth_id_xpath = "//*[@id='AuthActivityForm']/div[3]/div[4]";
		public static final String activitiesTab_xpath = "//*[@id=\"AuthLeftTab_6\"]/div/i";
		public static final String authorizationActivitiesTable_xpath = "//div[contains(@class,'ItemMT5')]/div/div/div[@class='slimScrollDiv']/div[contains(@class, 'SlimScroll')]/table[@class = 'GlbGrid']/tbody/tr";
		public static final String noRecordsMessage_xpath = "//*[@id='UmAuthActivities']/div/div[2]/div/table/tbody/tr[2]/td";
		public static final String dueDate_id = "UmTxtActDueDate";
		public static final String comments = "UmActComReasons";
		public static final String loggedInUser_xpath = "//*[@id=\"lblLoginName\"]";
		public static final String authorizationActivity_xpath = "//div[contains(@class,'ItemMT5')]/div/div/div[@class='slimScrollDiv']/div[contains(@class, 'SlimScroll')]/table[@class = 'GlbGrid']/tbody/tr";

		public static final String decision_buttonArrow_xpath = "//*[@id=\"AuthActivityViewForm\"]/table/tbody/tr[14]/td[2]/span[1]/span/span[2]/span";
		public static final String decision_button_text = "//*[@id=\"AuthActivityViewForm\"]/table/tbody/tr[14]/td[2]/span[1]/span/span[1]";
		public static final String saveAndComplete_id = "BtnAuthViewSaveComplete";
	}

	public class GuidingCarePreAuthDecisions {
		public static final String decisionStatus_xpath = "//tr[@id='GA_ADS_Row_0']/td[3]";
		public static final String decisionStatusDropdown_id = "UmDdDecStatus";
		public static final String decisionStatusCodeDropdown_id = "UmDdDecStatusCodes";
		public static final String decisionApprovedDays_id = "TextDecAppr";
		public static final String decisionRequestedDays_xpath = "//tr[@class='DecisionSelect Data']/td[11]";
		public static final String saveButton_xpath = "//div[@id='DecBtnSave']/input[@id='BtnSave']";
		public static final String desicionTabLink_class = "AuthDecision";
		public static final String activitiesLink_class = "AuthActivities";
		public static final String decisionStatusDropdown_xpath1 = "//tr[@id = 'GA_ADS_Row_"; // tr[@id =
																								// 'GA_ADS_Row_"+rowNumber+"']/following-sibling::tr/td[3]/select[@id='UmDdDecStatus']";
																								// //row number starts
																								// with zero
		public static final String descisionStatusDropdown_xpath2 = "']/following-sibling::tr/td[3]/select[@id='UmDdDecStatus']";
		public static final String decisionStatusCodeDropdown_xpath2 = "']/following-sibling::tr/td[4]/select[@id='UmDdDecStatusCodes']";
		public static final String decisionApprovedDays_xpath2 = "']/following-sibling::tr/td[12]/input[@id='TextDecAppr']";
		public static final String decisionStatusEdit_xpath2 = "']/td[3]";
	}

	public class GuidingCareComplaints {
		public static final String memberSearchtext = "//*[@id=\"txtSearch\"]";
		public static final String memberSearchBtn = "//*[@id=\"memberSerach\"]/div/div/div/div[3]/div[1]/input";
		public static final String searchedMemberRadioBtn = "//tbody[@role = 'rowgroup']/tr[1]/td[1]/input";
		public static final String searchedMemberFirstName = "//tbody[@role = 'rowgroup']/tr[1]/td[5]";
		public static final String searchedMemberLastName = "//tbody[@role = 'rowgroup']/tr[1]/td[4]";
		public static final String goToCareRecord_xpath = "//*[@id=\"ImgGotoCareRecord\"]";
		public static final String complaintTab_xpath = "/html/body/div[16]/div[2]/div[1]/table/tbody/tr[5]/td/div[5]/div[2]/div/div/ul/li[4]/div";
		public static final String addComplaintsButton_id = "AddComplaint";
		public static final String btnNewComplaints_xpath = "//*[@id=\"btnNewCompl\"]";
		public static final String complaintTypeDD_xpath = "//*[@id=\"Accordion_Group_2\"]/div[1]/div[2]/div[1]/span[5]/span";
		public static final String complaintTypeDDList_xpath = "/html/body/div/div/ul[@id=\"AgDdlComplaintType_listbox\"]/li";
		public static final String dateReceivedText_xpath = "//*[@id=\"TextDateReceived\"]";
		public static final String dateReceived_id = "TextDateReceived";
		public static final String hiddenReceivedDate_id = "HdnComplaintReceivedDate";
		public static final String dateHours_id = "txtDateHours";
		public static final String dateMinutes_id = "txtDateMinutes";
		public static final String dateSeconds_id = "txtDateSeconds";
		public static final String btnDone_xpath = "//input[@class ='clsPopulateTime btn-default']";
		public static final String hiddendateDue_id = "HdnComplaintDueDate";
		public static final String daysUntilDecision_xpath = "//*[@id=\"TextDaysUntilDecision\"]";
		public static final String authSource_id = "AgDdAuthSource";
		public static final String typeOfCaseArrow_xpath = "//*[@id=\"DIV_21_UF_CD_TYPE_OF_CASE\"]/span[1]/span";
		public static final String typeOfCaseList_xpath = "//*[@id=\"AgDdTypeOfCase_listbox\"]/li";
		public static final String eligibiltyLOB_id = "MemEligMain_MiniView";
		public static final String complaintClassArrow_xpath = "//*[@id=\"DIV_21_UF_CD_COMPLAINT_CLASS\"]/span[1]";
		public static final String complaintClassList_xpath = "//*[@id=\"AgDdComplpaintClass_listbox\"]/li";
		public static final String complaintCategoryArrow_xpath = "//*[@id=\"DIV_21_UF_CD_COMPLAINT_CATEGORY\"]/span[1]";
		public static final String complaintCategoryList_xpath = "//*[@id=\"AgDdComplpaintCategory_listbox\"]/li";
		public static final String complaintSubcategoryArrow_xpath = "//*[@id=\"DIV_21_UF_CD_COMPLAINT_SUB_CATEGORY\"]/span[1]";
		public static final String complaintSubcategoryList_xpath = "(/html/body/div/div/ul[@id=\"AgDdComplpaintSubCategory_listbox\"])/li";
		public static final String communicationTypeArrow_xpath = "//*[@id=\"DIV_21_UF_CD_COMMUNICATION_TYPE\"]/span[1]";
		public static final String communicationTypeList_xpath = "//*[@id=\"AgDdCommunicationType_listbox\"]/li";
		public static final String intakeDeptArrow_xpath = "//*[@id=\"DIV_21_UF_CD_INTAKE_DEPT\"]/span[1]";
		public static final String intakeDeptList_xpath = "//*[@id=\"AgDdIntakeDepartment_listbox\"]/li";
		public static final String responsibleDeptArrow_xpath = "//*[@id=\"DIV_21_UF_CD_RESPONSIBLE_DEPT\"]/span[1]";
		public static final String responsibleDeptList_xpath = "//*[@id=\"AgDdResponsibleDepartment_listbox\"]/li";
		public static final String whoIntiatedArrow_xpath = "//*[@id=\"DIV_21_UF_CD_WHO_INTIATED_APPEALS\"]/span[1]";
		public static final String whoIntiatedList_xpath = "//*[@id=\"AgDdInitiatedAppeals_listbox\"]/li";
		public static final String complaintAgainst_id = "AgDdComplaintAgainst";
		public static final String complaintAgainstDetails_id = "TextComplaintAgainst";
		public static final String btnSave_id = "BtnSave";
		public static final String btnExpandComplaintDetails_id = "BtnAct";
		public static final String successPopUpMessage_id = "ModalPopup_ContentMessage";
		public static final String createdDate_id = "TextCreatedDate";
		public static final String complaintID_xpath = "//*[@id=\"BtnComplaint_View\"]";
	}

}