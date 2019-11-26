package pageobjects;

public class HealthXObjRepo {

	public class HealthXAdminPortalLogin {
		public static final String username_id = "username";
		public static final String password_name = "password";
		public static final String loginButton_id = "loginButton";
	}
	
	public class HealthXPortalAdminPortalPage {
		public static final String loggedInAsProviderOrAdmin_xpath = "/html/body/header/nav/span";
		public static final String logOut_xpath = "/html/body/header/nav/ul/li[3]/a";
		public static final String eligibilityAndBenefitsTab_xpath = "//*[@id=\"hxUserMenu\"]/li[2]/a";
		public static final String userManagerProviderPortalTab_xpath = "//*[@id=\"hxUserMenu\"]/li[3]/a";
		public static final String userManagerMemberPortalTab_xpath = "//*[@id=\"hxUserMenu\"]/li[4]/a";
	}
	
	public class HealthXProviderPortalPage {
		public static final String providerCreateAnAccount_xpath = "//*[@id=\"ContentItem100_1\"]/div[2]/fieldset/span/a[2]";
		public static final String providerlicenseAgreementAcceptCheckBox_id = "field_87";
		public static final String providerlicenseAgreementAgreeButton_id = "button_1";
		
		public static final String healthXheaderName_xpath = "/html/body/header/p";
		public static final String providerHomeTab_xpath = "//*[@id=\"hxUserMenu\"]/li[1]/a";
		public static final String claimsAndPaymentTab_xpath = "//*[@id=\"hxUserMenu\"]/li[3]/a";
		public static final String AuthorizationsAndReferralsTab_xpath = "//*[@id=\"hxUserMenu\"]/li[4]/a";
	}
	
	public class HealthXMemberPortalPage {
		public static final String memberCreateAnAccount_xpath = "//*[@id=\"ContentItem0_2\"]/div/fieldset/div[1]/div[5]/div[2]/a";
		public static final String memberlicenseAgreementAcceptCheckBox_id = "field_87";
		public static final String loggedInAsMember_className = "wideOnly";
		public static final String logOut_xpath = "//*[@id=\"header-main\"]/div[2]/div/div[2]/div/nav/ul/li[2]/a/span[2]";
	}
	
	public class HealthXRegistrationInfo {
		
		public static final String providerRegistrationInfoTin_id = "field_11";
		public static final String providerRegistrationInfoContactName_id = "field_35";
		public static final String providerRegistrationInfoContactPhone_id = "field_36";
		public static final String providerRegistrationInfoNpi_id = "field_30";
		public static final String providerRegistrationInfoPracticePhone_id = "field_10";
		public static final String providerRegistrationInfoPracticeAddress_id = "field_76";
		public static final String providerRegistrationInfoPracticeFax_id = "field_84";
		public static final String providerRegistrationInfoPracticeName_id = "field_37";
		public static final String providerRegistrationInfoPrimarySpecialtyDropDown_id = "field_38";
		public static final String providerRegistrationInfoAddedProviders_id = "field_34";
		public static final String providerRegistrationInfoAddButton_id = "button6";
		
		public static final String providerRegistrationInfoErrorMessage_className = "errorMessage";
		
		public static final String registrationInfoFirstName_id = "field_2";
		public static final String registrationInfoLastName_id = "field_3";
		public static final String memberRegistrationInfoMemberID_id = "field_41";
		public static final String memberRegistrationInfoDateOfBirth_id = "field_4";
		
		public static final String registrationInfoUserName_id = "field_12";
		public static final String registrationInfoEmailAddress_id = "field_17";
		public static final String registrationInfoConfirmEmailAddress_id = "field_18";
		public static final String registrationInfoPassword_id = "field_21";
		public static final String registrationInfoConfirmPassword_id = "field_22";
		public static final String registrationInfoSecuryQ_1_Dropdown_id = "field_23";
		public static final String registrationInfoSecuryQ_1_TextField_id = "fieldExtra_23";
		public static final String registrationInfoSecuryQ_2_Dropdown_id = "field_24";
		public static final String registrationInfoSecuryQ_2_TextField_id = "fieldExtra_24";
		public static final String registrationInfoSecuryQ_3_Dropdown_id = "field_25";
		public static final String registrationInfoSecuryQ_3_TextField_id = "fieldExtra_25";
		public static final String registrationInfoNextButton_id = "button_1";
		public static final String registrationInfoFinishButton_id = "button_2";
		
		public static final String registrationConfirmMemberInfo_className = "successMessage";
		public static final String registrationConfirmProviderInfo_xpath = "//*[@id=\"stepContent\"]/table/tbody";
		public static final String registrationMemberInfo_className = "signUpInfo";
		public static final String registrationMemberInfoYourName_xpath = "//*[@id=\"stepContent\"]/div[2]/span[2]";
		public static final String registrationMemberInfoAddress_xpath = "//*[@id=\"stepContent\"]/div[2]/span[4]";
		public static final String registrationMemberInfoCity_xpath = "//*[@id=\"stepContent\"]/div[2]/span[6]";
		public static final String registrationMemberInfoState_xpath = "//*[@id=\"stepContent\"]/div[2]/span[8]";
		public static final String registrationMemberInfoZip_xpath = "//*[@id=\"stepContent\"]/div[2]/span[10]";
		
		public static final String registrationAccountInfo_className = "accountInfo";
		public static final String registrationAccountInfoUserName_xpath = "//*[@id=\"stepContent\"]/div[3]/span[2]";
		public static final String registrationAccountInfoEmailAddress_xpath = "//*[@id=\"stepContent\"]/div[3]/span[4]";
	}
	
	public class EligibilityAndBenefits {
		public static final String memberIDInputTextArea_className = "memberIDInput";
		public static final String firstNameInputTextField_className = "firstNameInput";
		public static final String lastNameInputTextField_className = "lastNameInput";
		public static final String dateOfBirthInputTextField_className = "dobInput hasDatepicker";
		public static final String eligibilityAndBenefitsSearchButton_id = "ctl00_MainContent_uxEligControl_uxSearch";
		public static final String eligibilityNameSelect_xpath = "//td[text()='*']//parent::tr//a";
		public static final String eligibilityProviderNPI_Select_xpath = "//*[@id=\"ctl00_MainContent_uxOrgProviderIDList\"]";
		
		public static final String eligibilityAndBenefitsSearchResultRow_xpath = "//*[@id=\"ctl00_MainContent_uxEligControl_uxListGrid\"]/tbody/tr";
		public static final String eligibilityProviderNPI_SelectMessageLabel_xpath = "//*[@id=\"ctl00_MainContent_uxEligControl_uxMessageLabel\"]";
		
		/*Subscriber Information*/
		public static final String subscriberMemberName_xpath = "//*[@id=\"ctl00_MainContent_uxTabPanel\"]/div/div[2]/div/div[5]/div/table[1]/tbody/tr[1]/td[2]";
		public static final String subscriberMemberID_xpath = "//*[@id=\"ctl00_MainContent_uxTabPanel\"]/div/div[2]/div/div[5]/div/table[1]/tbody/tr[2]/td[2]";
		public static final String subscriberEffectiveDate_xpath = "//*[@id=\"ctl00_MainContent_uxTabPanel\"]/div/div[2]/div/div[5]/div/table[1]/tbody/tr[3]/td[2]";
		public static final String subscriberTerminationDate_xpath = "//*[@id=\"ctl00_MainContent_uxTabPanel\"]/div/div[2]/div/div[5]/div/table[1]/tbody/tr[4]/td[2]";
		public static final String subscriberGroup_xpath = "//*[@id=\"ctl00_MainContent_uxTabPanel\"]/div/div[2]/div/div[5]/div/table[1]/tbody/tr[1]/td[4]";
		public static final String subscriberPlan_xpath = "//*[@id=\"ctl00_MainContent_uxTabPanel\"]/div/div[2]/div/div[5]/div/table[1]/tbody/tr[2]/td[4]";
		public static final String subscriberStatus_xpath = "//*[@id=\"ctl00_MainContent_uxTabPanel\"]/div/div[2]/div/div[5]/div/table[1]/tbody/tr[3]/td[4]";
		public static final String subscriberQMB_xpath = "//*[@id=\"ctl00_MainContent_uxTabPanel\"]/div/div[2]/div/div[5]/div/table[1]/tbody/tr[4]/td[4]";
		
		/*PCP Information*/
		public static final String pcpName_xpath = "//*[@id=\"ctl00_MainContent_uxTabPanel\"]/div/div[2]/div/div[5]/div/table[2]/tbody/tr[1]/td[2]";
		public static final String pcpEffectiveDate_xpath = "//*[@id=\"ctl00_MainContent_uxTabPanel\"]/div/div[2]/div/div[5]/div/table[2]/tbody/tr[2]/td[2]";
		public static final String pcpPhone_xpath = "//*[@id=\"ctl00_MainContent_uxTabPanel\"]/div/div[2]/div/div[5]/div/table[2]/tbody/tr[1]/td[4]";
		public static final String pcpTerminationDate_xpath = "//*[@id=\"ctl00_MainContent_uxTabPanel\"]/div/div[2]/div/div[5]/div/table[2]/tbody/tr[2]/td[4]";
		
		/*CoPayments & Accumulators*/
		public static final String copaymentsAndAccumulatorsTableBody_xpath = "//*[@id=\"ctl00_MainContent_uxTabPanel\"]/div/div[2]/div/div[5]/div/table[3]/tbody";
		public static final String pcpOfficeVisit_xpath = "//td[contains (text(),'PCP Office Visit:')]//parent::tr//td[2]";
		public static final String specialistOfficeVisit_xpath = "//td[contains (text(),'Specialist Office Visit:')]//parent::tr//td[4]";
		public static final String mentalHealthOfficeVisit_xpath = "//td[contains (text(),'Mental Health Office Visit:')]//parent::tr//td[2]";
		public static final String physicalTherapy_xpath = "//td[contains (text(),'Physical Therapy:')]//parent::tr//td[4]";
		public static final String laboratoryServices_xpath = "//td[contains (text(),'Laboratory Services:')]//parent::tr//td[2]";
		public static final String XRay_xpath = "//td[contains (text(),'X-Ray (With or without contrast):')]//parent::tr//td[4]";
		public static final String CTMRIMRASPECT_xpath = "//td[contains (text(),'CT, MRI, MRA, SPECT:')]//parent::tr//td[2]";
		public static final String inpatientHospitalCoverage_xpath = "//td[contains (text(),'Inpatient Hospital Coverage:')]//parent::tr//td[4]";
		public static final String outpatientServicesHospital_xpath = "//td[contains (text(),'Outpatient Services (Hospital):')]//parent::tr//td[2]";
		public static final String outpatientServicesAmbulatorySurgeryCenter_xpath = "//td[contains (text(),'Outpatient Services*: (Ambulatory Surgery Center)')]//parent::tr//td[4]";
		
		public static final String membersearchHyperLink_xpath = "//*[@id='ctl00_MainContent_uxEligControl_uxListGrid']/tbody/tr[3]/td[1]/a";
		public static final String EligibilityAndBenefitsTab_xpath = "//*[@id=\"hxUserMenu\"]/li[2]/a";
		public static final String searchList_xpath= "//*[@id='ctl00_MainContent_uxEligControl_uxListGrid']/tbody/tr";
	}
	
	public class ClaimsAndPayments {
		public static final String claimsAndPaymentsProviderNPI_Select_xpath = "//*[@id=\"ctl00_MainContent_uxOrgProviderIDList\"]";
		public static final String claimsAndPaymentsProviderNPI_SelectMessageLabel_xpath = "//*[@id=\"ctl00_MainContent_uxClaimControl_uxMessageLabel\"]";
		
		//For full table rows, tr[2] to tr[11]
		//public static final String claimsAndPaymentsProvider_claimNumberLink_xpath = "//*[@id=\"ctl00_MainContent_uxClaimControl_uxListGrid\"]/tbody/tr[2]/td[1]/a";
		public static final String claimsAndPaymentsProvider_claimNumberLink_xpath = "//*[@id=\"ctl00_MainContent_uxClaimControl_uxListGrid\"]/tbody/tr"; //[index]/td[1]/a"
		public static final String claimsAndPaymentsProvider_navigateForwardFromFirstPage_xpath = "//*[@id=\"ctl00_MainContent_uxTabPanel\"]/div/div[2]/div/table/tbody/tr/td/a[1]";
		public static final String claimsAndPaymentsProvider_navigateForward_xpath = "//*[@id=\"ctl00_MainContent_uxTabPanel\"]/div/div[2]/div/table/tbody/tr/td/a[3]";
		public static final String claimsAndPaymentsProvider_lastPage_xpath = "//*[@id=\"ctl00_MainContent_uxTabPanel\"]/div/div[2]/div/table/tbody/tr/td/img[1]";
		
		public static final String claimsAndPaymentsProvider_backToSearchResults_xpath = "//*[@id=\"ctl00_MainContent_uxClaimControl_uxBackToSearchLink\"]";
		
		//Claim Number Information
		public static final String claimsAndPaymentsProvider_claimNumber_xpath = "//*[@id=\"ctl00_MainContent_uxTabPanel\"]/div/div[2]/div/div[6]/div/div[1]/div/h3";
		public static final String claimsAndPaymentsProvider_memberName_xpath = "//*[@id=\"ctl00_MainContent_uxTabPanel\"]/div/div[2]/div/div[6]/div/div[1]/div/table/tbody/tr[1]/td[2]";
		public static final String claimsAndPaymentsProvider_memberID_xpath = "//*[@id=\"ctl00_MainContent_uxTabPanel\"]/div/div[2]/div/div[6]/div/div[1]/div/table/tbody/tr[2]/td[2]";
		public static final String claimsAndPaymentsProvider_plan_xpath = "//*[@id=\"ctl00_MainContent_uxTabPanel\"]/div/div[2]/div/div[6]/div/div[1]/div/table/tbody/tr[3]/td[2]";
		public static final String claimsAndPaymentsProvider_group_xpath = "//*[@id=\"ctl00_MainContent_uxTabPanel\"]/div/div[2]/div/div[6]/div/div[1]/div/table/tbody/tr[4]/td[2]";
		public static final String claimsAndPaymentsProvider_negotiatedAmount_xpath = "//*[@id=\"ctl00_MainContent_uxTabPanel\"]/div/div[2]/div/div[6]/div/div[1]/div/table/tbody/tr[5]/td[2]";
		public static final String claimsAndPaymentsProvider_totalCharges_xpath = "//*[@id=\"ctl00_MainContent_uxTabPanel\"]/div/div[2]/div/div[6]/div/div[1]/div/table/tbody/tr[6]/td[2]";
		public static final String claimsAndPaymentsProvider_dateOfService_xpath = "//*[@id=\"ctl00_MainContent_uxTabPanel\"]/div/div[2]/div/div[6]/div/div[1]/div/table/tbody/tr[1]/td[4]";
		public static final String claimsAndPaymentsProvider_serviceProvider_xpath = "//*[@id=\"ctl00_MainContent_uxTabPanel\"]/div/div[2]/div/div[6]/div/div[1]/div/table/tbody/tr[2]/td[4]";
		public static final String claimsAndPaymentsProvider_npi_xpath = "//*[@id=\"ctl00_MainContent_uxTabPanel\"]/div/div[2]/div/div[6]/div/div[1]/div/table/tbody/tr[3]/td[4]";
		public static final String claimsAndPaymentsProvider_claimStatus_xpath = "//*[@id=\"ctl00_MainContent_uxTabPanel\"]/div/div[2]/div/div[6]/div/div[1]/div/table/tbody/tr[4]/td[4]";
		public static final String claimsAndPaymentsProvider_planPaid_xpath = "//*[@id=\"ctl00_MainContent_uxTabPanel\"]/div/div[2]/div/div[6]/div/div[1]/div/table/tbody/tr[5]/td[4]";
		
		//Claim Details
		public static final String claimsAndPaymentsProvider_claimDetailRow_xpath = "//*[@id=\"ctl00_MainContent_uxTabPanel\"]/div/div[2]/div/div[6]/div/div[3]/div/table/tbody/tr";
		
		public static final String claimsAndPaymentsProvider_claimDetailDescription_xpath = "//*[@id=\"ctl00_MainContent_uxTabPanel\"]/div/div[2]/div/div[6]/div/div[3]/div/table/tbody/tr/td[1]";
		public static final String claimsAndPaymentsProvider_claimDetailCptCode_xpath = "//*[@id=\"ctl00_MainContent_uxTabPanel\"]/div/div[2]/div/div[6]/div/div[3]/div/table/tbody/tr/td[2]";
		public static final String claimsAndPaymentsProvider_claimDetailMod1_xpath = "//*[@id=\"ctl00_MainContent_uxTabPanel\"]/div/div[2]/div/div[6]/div/div[3]/div/table/tbody/tr/td[3]";
		public static final String claimsAndPaymentsProvider_claimDetailMod2_xpath = "//*[@id=\"ctl00_MainContent_uxTabPanel\"]/div/div[2]/div/div[6]/div/div[3]/div/table/tbody/tr/td[4]";
		public static final String claimsAndPaymentsProvider_claimDetailCharges_xpath = "//*[@id=\"ctl00_MainContent_uxTabPanel\"]/div/div[2]/div/div[6]/div/div[3]/div/table/tbody/tr/td[5]";
		public static final String claimsAndPaymentsProvider_claimDetailNegotiatedAmount_xpath = "//*[@id=\"ctl00_MainContent_uxTabPanel\"]/div/div[2]/div/div[6]/div/div[3]/div/table/tbody/tr/td[6]";
		public static final String claimsAndPaymentsProvider_claimDetailMemberCopay_xpath = "//*[@id=\"ctl00_MainContent_uxTabPanel\"]/div/div[2]/div/div[6]/div/div[3]/div/table/tbody/tr/td[7]";
		public static final String claimsAndPaymentsProvider_claimDetailPlanPaid_xpath = "//*[@id=\"ctl00_MainContent_uxTabPanel\"]/div/div[2]/div/div[6]/div/div[3]/div/table/tbody/tr/td[8]";
		
		public static final String claimsAndPaymentsProvider_claimDetailTotalCharges_xpath = "//*[@id=\"ctl00_MainContent_uxTabPanel\"]/div/div[2]/div/div[6]/div/div[3]/div/table/tfoot/tr/td[2]";
		public static final String claimsAndPaymentsProvider_claimDetailTotalNegotiatedAmount_xpath = "//*[@id=\"ctl00_MainContent_uxTabPanel\"]/div/div[2]/div/div[6]/div/div[3]/div/table/tfoot/tr/td[3]";
		public static final String claimsAndPaymentsProvider_claimDetailTotalMemberCopay_xpath = "//*[@id=\"ctl00_MainContent_uxTabPanel\"]/div/div[2]/div/div[6]/div/div[3]/div/table/tfoot/tr/td[4]";
		public static final String claimsAndPaymentsProvider_claimDetailTotalPlanPaid_xpath = "//*[@id=\"ctl00_MainContent_uxTabPanel\"]/div/div[2]/div/div[6]/div/div[3]/div/table/tfoot/tr/td[5]";
	}

	public class HealthXPortalAdminPortal_UserManagerPage {
		/*User Manager - Provider Portal*/
		public static final String userManagerUserNameTextField_id = "username";
		public static final String userManagerFirstNameTextField_id = "firstname";
		public static final String userManagerLastNameTextField_id = "lastname";
		public static final String userManagerEmailAddressTextField_id = "emailaddress";
		public static final String userManagerRemoteUserIdTextField_id = "remoteuserid";
		public static final String userManagerUserTypeDropdown_id = "usertype";
		public static final String userManagerUniqueIdTextField_id = "combinedid";
		public static final String userManagerGroupNumberTextField_id = "groupnumber";
		public static final String userManagerTinTextField_id = "tin";
		public static final String userManagerNpiTextField_id = "npi";
		public static final String userManagerOrgProviderIdTextField_id = "orgproviderid";
		public static final String userManagerUserIdTextField_id = "userid";
		public static final String userManagerSessionIdTextField_id = "sessionid";
		public static final String userManagerSearchButton_xpath = "//*[@id=\"userSearchContent\"]/div/div/div/button[1]";
		
		public static final String imitateButton_className = "jsImitate";
		public static final String imitateButton_xpath = "//*[starts-with(@id, 'table-')]/tbody/tr[1]/td[1]/span/button";
		public static final String imitateUserDropdown_id = "website";
		public static final String imitateUserButton_name = "imitateButton";
		public static final String imitateModeField_id = "imitateMessage";
	}
	
	public class HealthXPortalMemberPortal_UserManagerPage{
		public static final String coverageAndBenefits_xpath = "//*[@id=\'hxUserMenu\']/li[2]/a";
		public static final String medicalTab_xpath = "//*[@id=\'hxUserMenu\']/li[2]/ul/li[1]";
		public static final String claimsTab_xpath = "//*[@id=\'hxUserMenu\']/li[3]/a";
		
		//Member Information
		public static final String memberName_xpath = "//*[@id=\'personalInfo\']/tbody/tr[1]/td[2]";
		public static final String planName_xpath = "//*[@id=\'personalInfo\']/tbody/tr[1]/td[4]";
		public static final String memberId_xpath = "//*[@id=\'personalInfo\']/tbody/tr[2]/td[2]";
		public static final String effectiveDate_xpath = "//*[@id=\'personalInfo\']/tbody/tr[2]/td[4]";
		public static final String status_xpath = "//*[@id=\'personalInfo\']/tbody/tr[3]/td[2]";
		public static final String healthPlanId_xpath = "//*[@id=\'personalInfo\']/tbody/tr[4]/td[4]";
		public static final String serviceTypes_xpath = "//*[@id=\'personalInfo\']/tbody/tr[5]/td[2]";
		public static final String RxBIN_xpath = "//*[@id=\'personalInfo\']/tbody/tr[5]/td[4]";
		public static final String RxPCN_xpath = "//*[@id=\'personalInfo\']/tbody/tr[6]/td[2]";
		public static final String RxGRP_xpath = "//*[@id=\'personalInfo\']/tbody/tr[6]/td[4]";
		
		//PCP Information
		public static final String pcpName_xpath = "//*[@id=\'contactInfo\']/tbody/tr[1]/td[2]";
		public static final String pcpPhone_xpath = "//*[@id=\'contactInfo\']/tbody/tr[1]/td[4]";
		public static final String pcpEffectiveDate_xpath = "//*[@id=\'contactInfo\']/tbody/tr[3]/td[2]";
		public static final String pcpTerminationDate_xpath = "//*[@id=\'contactInfo\']/tbody/tr[3]/td[4]";
		
		//CoPayment information
		public static final String pcpOfficeVisit_xpath = "//*[@id='eligView']/table[3]/tbody/tr[1]/td[2]";
		public static final String speacialistOfficeVisit_xpath = "//*[@id='eligView']/table[3]/tbody/tr[1]/td[4]";
		public static final String emergencyRoom_xpath = "//*[@id='eligView']/table[3]/tbody/tr[2]/td[2]";
		public static final String mentalHealthOfficeVisit_xpath = "//*[@id='eligView']/table[3]/tbody/tr[2]/td[4]";
		public static final String physicalTherapy_xpath = "//*[@id='eligView']/table[3]/tbody/tr[3]/td[2]";
		public static final String laboratoryServices_xpath = "//*[@id='eligView']/table[3]/tbody/tr[3]/td[4]";
		public static final String xray_xpath = "//*[@id='eligView']/table[3]/tbody/tr[4]/td[2]";
		public static final String ctMriMra_xpath = "//*[@id='eligView']/table[3]/tbody/tr[4]/td[4]";
		public static final String inpatientHospitalCoverage_xpath = "//*[@id='eligView']/table[3]/tbody/tr[5]/td[2]";
		public static final String outPatientServicesHospital_xpath = "//*[@id='eligView']/table[3]/tbody/tr[5]/td[4]";
		public static final String outPatientServicesAmbulatorySurgeryCenter_xpath = "//*[@id='eligView']/table[3]/tbody/tr[6]/td[2]";
		
		
		//Maximum Out of Pocket
		public static final String moopLimitMaximumAmount_xpath = "//*[@id='eligView']/table[4]/tbody/tr[1]/td[3]";
		
		//Claims information
		public static final String claimsRows_xpath = "//*[@id=\'claimResults\']/table/tbody/tr";
		public static final String claimsResults_id = "claimResults";
		public static final String claimsNumberOfResultsFound_xpath = "//*[@id=\"claimResults\"]/h4";
		public static final String claimsCount_className = "claims_count";
		public static final String claimNumberLink_xpath = "//*[@id=\"claimResults\"]/table/tbody/tr";
		
		public static final String claimNumberText_xpath = "//*[@id=\"claimDetails\"]/h3[1]";
		public static final String claimMemberName_xpath = "//*[@id=\"claimDetails\"]/div[2]/table/tbody/tr[1]/td[2]";
		public static final String claimMemberID_xpath = "//*[@id=\"claimDetails\"]/div[2]/table/tbody/tr[2]/td[2]";
		public static final String claimPlanName_xpath = "//*[@id=\"claimDetails\"]/div[2]/table/tbody/tr[3]/td[2]";
		public static final String claimBilledAmount_xpath = "//*[@id=\"claimDetails\"]/div[2]/table/tbody/tr[4]/td[2]";
		public static final String claimApprovedAmount_xpath = "//*[@id=\"claimDetails\"]/div[2]/table/tbody/tr[5]/td[2]";
		public static final String claimPlanShare_xpath = "//*[@id=\"claimDetails\"]/div[2]/table/tbody/tr[6]/td[2]";
		public static final String claimDateOfService_xpath = "//*[@id=\"claimDetails\"]/div[2]/table/tbody/tr[1]/td[4]";
		public static final String claimServiceProvider_xpath = "//*[@id=\"claimDetails\"]/div[2]/table/tbody/tr[2]/td[4]";
		public static final String claimMemberResponsibility_xpath = "//*[@id=\"claimDetails\"]/div[2]/table/tbody/tr[6]/td[4]";
		
		public static final String claimDetailsList_xpath = "//*[@id=\"claimDetails\"]/div[3]/table[2]/tbody/tr";
		
		public static final String claimDetailsTotalChargesFooter = "//*[@id=\"ctl00_MainContent_uxTabPanel\"]/div/div[2]/div/div[6]/div/div[3]/div/table/tfoot/tr/td[2]";
		public static final String claimDetailsTotalPlanPaidFooter = "//*[@id=\"ctl00_MainContent_uxTabPanel\"]/div/div[2]/div/div[6]/div/div[3]/div/table/tfoot/tr/td[5]";
	}

	
}
