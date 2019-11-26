package pageobjects;

import managers.PropertyFileReader;
import utils.Const;

public class HRPObjRepo {

	public class HRPLogin {
		public static final String userName_className = "WindowsForms10.EDIT.app.0.293a9a3_r30_ad1";
		public static final String userNameTwo_className = "WindowsForms10.EDIT.app.0.293a9a3_r36_ad1";
		public static final String password_className = "WindowsForms10.EDIT.app.0.293a9a3_r30_ad1";
		public static final String passwordTwo_className = "WindowsForms10.EDIT.app.0.293a9a3_r36_ad1";
		public static final String environment_id = "lookUpEditServers";
		public static final String environment_select = "Row 6";
		public static final String login_name = "Login";
		public static final String close_id = "simpleButtonClose";
		public static final String loginForm = "LoginForm";
		public static final String ok_name = "&OK";
		public static final String uname_id = "textEditUserName";
		public static final String pwd_id = "textEditPassword";
		public static final String uname_xpath = "//Window[@Name='HealthRules Manager Login']/Pane[2]/Edit";
		public static final String pwd_xpath = "//Window[@Name='HealthRules Manager Login']/Pane[3]/Edit";

	}

	public class HRPDockLeft {
		public static final String member_name = "Members";
		public static final String claims_name = "Claims";
		public static final String favorites_name = "Favorites";
		public static final String others_name = "Others";
		public static final String myWork_name = "My Work";
		public static final String financial_name = "Financials";
		public static final String provider_name = "Providers";
		public static final String funding_name = "Funding";
		public static final String utilization_name = "Utilization";
		public static final String admin_name = "Administration";
	}

	public class HRPTobBar {
		public static final String close_name = "Close";
		public static final String save_name = "Save";
		public static final String clear_name = "Clear";
		public static final String searchBar_name = "Search Bar";
		public static final String searchFor_name = "Search For";
		public static final String back_name = "Back";
		public static final String quit_name = "&Quit";

	}

	public class HRPHomeMemberSearch {
		public static final String memSearch_id = "SimpleButtonSearch";
	}

	public class HRPMemberSearch {
		// Input Text Fields
		public static final String memberId_name = "Member ID";
		public static final String otherId_name = "Other ID";
		public static final String subsId_name = "Subscription ID";
		public static final String hicn_name = "HICN/RRB";
		public static final String medBenID_name = "Medicare Beneficiary ID";
		public static final String lastName_name = "Last Name";
		public static final String firstName_name = "First Name";
		// Buttons
		public static final String search_name = "Search";
		public static final String clear_name = "Clear";
		public static final String searchErrorText_name = "This search found no data.";

		// SearchOutput
		public static final String memNameSearch_name = "Member Name row ";
		public static final String memIDSearch_name = "ID row ";
		public static final String view_name = "View";
		public static final String viewErrorText_name = "This member has not been successfully created so you will need to view it from the workbasket.";
	}

	public class HRPProviderSearch {
		// Input Text Fields
		public static final String NPI = "Practitioner ID/NPI";
		public static final String lastName = "Last Name";
		public static final String firstName = "First Name";

		// Search Output
		public static final String firstRowOutput = "Row ";
		public static final String firstRow_name = "Row 1";

		public static final String address_name = "Pay To Address row 0";
		public static final String providerNameOutput = "Name row ";
		public static final String npiOutput = "NPI row ";
		public static final String practitionerOutput = "Practitioner ID row ";
		public static final String primarySpecialty = "Primary Specialty row ";
		public static final String supplierID = "Supplier row ";
		public static final String supplierLocation = "Supplier Location row ";
		public static final String view_name = "View";
		// public static final String view_xpath="//"
		public static final String statusBar = "Search Results Status Bar";
	}

	public static class HRPProviderViewDetails {
		public static final String statusBar = "Search Results Status Bar";
		public static final String generalPane = "General";
		public static final String benefitPanel = "repeaterPanelBenefitNetworks";
		public static final String benefitNetworksClass = "WindowsForms10.Window.b.app.0.293a9a3_r36_ad1";
		public static final String beneficiaryName = "Name";

		public static final String beneficiaryNameLabel_name = "Name:";

		public static final String npi_id = "autoEditNPI";
		public static final String personName_id = "personNameView";
		public static final String speciality_name = "Specialty row 0";
		public static final String taxonomy_name = "Code row 0";
		public static final String benifitNetwork1_xpath = "//Window[@Name='HealthRules Manager - "
				+ PropertyFileReader.getHrpUserId().replace("\"", "") + "    (" + Const.HRPEnvironment
				+ ")']/Pane[@Name ='panelControlDetail']/Pane[@Name='']/Tab/Pane/Pane/Pane/Pane[@Name='repeaterPanelBenefitNetworks']/Pane";
		public static final String benifitNetwork2_xpath = "/Pane[@Name='subEntityPanelBenefitNetwork']/Pane";

		// Roles Tab
		public static final String rolesTab_name = "Roles";

		public static final String roleNameRow_name = "Role Name row ";
		public static final String roleType_name = "Role Type row ";
		public static final String supplierName_name = "Supplier Name row ";

		// Provider Address Locations
		public static final String address_id = "autoEditAddress";
		public static final String address2_id = "autoEditAddress2";
		public static final String city_id = "autoEditCityName";
		public static final String state_id = "autoEditState";
		public static final String county_id = "textEditCounty";
		public static final String zipCode_id = "autoEditZipCode";
		public static final String taxID_xpath = "//Window[@Name='HealthRules Manager - "
				+ PropertyFileReader.getHrpUserId().replace("\"", "") + "    (" + Const.HRPEnvironment
				+ ")']/Pane[@Name ='panelControlDetail']/Pane[@Name='']/Tab/Pane/Pane/Pane/Pane[@Name='headerGroupControlSupplier']/Pane[@Name='subEntityPanelSupplier']/Pane[@Name='subEntityPanelOrgInfo']/Pane[@Name='subEntityPanelTaxEntity']/Pane";

	}

	public class HRPSupplierSearch {
		public static final String taxID_id = "textEditTaxID";
		public static final String view_name = "View";
		public static final String address_name = "Pay To Address row ";
		public static final String supplierID_name = "Supplier ID row ";
		public static final String supplierName_name = "Supplier Name row ";
		public static final String supplierNPI_name = "Supplier ID/NPI";

	}

	public static class HRPSupplierViewDetails {
		public static final String contactTab_name = "Contact";
		// Contact Tab Details
		public static final String address_className = "WindowsForms10.Window.b.app.0.293a9a3_r36_ad1";
		public static final String city_className = "WindowsForms10.EDIT.app.0.293a9a3_r36_ad1";
		public static final String state_className = "WindowsForms10.Window.8.app.0.293a9a3_r36_ad1";
		public static final String zipcode_className = "WindowsForms10.EDIT.app.0.293a9a3_r36_ad1";
		public static final String county_className = "WindowsForms10.EDIT.app.0.293a9a3_r36_ad1";

		public static final String lineDown_name = "Line Down";

		public static final String supplierName_id = "autoEditPrimaryName";
		public static final String supplierNPI_id = "autoEditNPI";

		public static final String supplierID_id = "autoEditSupplierID";
		public static final String taxID_xpath = "//Window[@Name='HealthRules Manager - "
				+ PropertyFileReader.getHrpUserId().replace("\"", "") + "    (" + Const.HRPEnvironment
				+ ")']/Pane[@Name ='panelControlSummary']/Pane[@Name='']/Pane[@Name='entityPanelSupplierDetailSummary']/Pane[@Name='subEntityPanelOrganization']/Pane[@Name='subEntityPanelTaxEntity']/Pane";
		public static final String primarySpeciality_Name = "Classification row 0";

		public static final String taxonomy_Name = "Code row 0";
		// public static final String benefitNetwork_xpath =
		// "//Window[@Name='HealthRules Manager -
		// "+PropertyFileReader.getHrpUserId().replace("\"", "")+"
		// ("+Const.HRPEnvironment+")']/Pane[@Name
		// ='panelControlSummary']/Pane[@Name='']/Pane[@Name='entityPanelSupplierDetailSummary']/Pane[@Name='subEntityPanelOrganization']/Pane[@Name='subEntityPanelTaxEntity']/Pane";
		public static final String benefitNetwork1_xpath = "//Window[@Name='HealthRules Manager - "
				+ PropertyFileReader.getHrpUserId().replace("\"", "") + "    (" + Const.HRPEnvironment
				+ ")']/Pane[@Name ='panelControlDetail']/Pane[@Name='']/Tab/Pane/Pane/Pane/Pane[@Name='repeaterPanelBenefitNetworks']/Pane";
		public static final String benefitNetwork2_xpath = "/Pane[@Name='subEntityPanelBenefitNetwork']/Pane";
		public static final String locationsTab_name = "Locations";
		public static final String locationNameRow_name = "Location Name row ";

		public static final String address_id = "autoEditAddress";
		public static final String address2_id = "autoEditAddress2";
		public static final String city_id = "autoEditCityName";
		public static final String state_id = "autoEditState";
		public static final String county_id = "textEditCounty";
		public static final String zipCode_id = "autoEditZipCode";
		public static final String phoneNumber = "autoEditPhoneNumber";
		public static final String phoneAreaCode = "autoEditPhoneAreaCode";
	}

	public class HRPMemberViewDetails {
		public static final String benefitPlan_name = "Benefit Plans row 1";
		public static final String editMember_name = "Open for Edit";
		public static final String primaryName_id = "personNameView";
		public static final String accountName_id = "hyperLinkEditHyperLinkId";
		public static final String activeDate_id = "textEditStatusChangedDate";
		public static final String DOB_name = "//*[@Name='panelControlAutoEdit']/pane";
		public static final String general_className = "WindowsForms10.EDIT.app.0.293a9a3_r36_ad1";
		public static final String general1_className = "WindowsForms10.Window.b.app.0.293a9a3_r36_ad1";
	}

	public class HRPMemberSpecifyAsOFDate {
		public static final String changeEffectiveDate_name = "Specify As of Date:";
		public static final String changeProcessClaimDate_name = "Do Not Reprocess Claims";
		public static final String reasonCode_name = "Reason Code:";
		public static final String comment_name = "Comment:";
		public static final String submit_name = "&OK";
		public static final String cancel_name = "&CANCEL";
	}

	public class HRPMemberEditSuscriptionBankAccounts {
		public static final String origEffectiveDate_name = "Orig. Effective Date:";
		public static final String bankAccountsTab_name = "Bank Accounts";
		public static final String paymentType_name = "Payment Type:";
	}

	public class HRPMemberReviewRequired {
		public static final String yes_name = "&Yes";
		public static final String no_name = "&No";
	}

	public class HRPOkAlert {
		public static final String noDataFoundWindow_name = "No Data Found";
		public static final String ok_name = "&OK";
	}

	public class HRPContactDetails {
		public static final String address_className = "WindowsForms10.EDIT.app.0.293a9a3_r36_ad1";
		public static final String state_className = "WindowsForms10.Window.b.app.0.293a9a3_r36_ad1";

	}

	public class HRPBenefitPlanDetails {
		public static final String benefitTab_name = "Benefit Plans";
		public static final String benefitPlan_name = "Benefit Plans row 1";

	}

	public class HRPMedicareDetails {
		public static final String medicareTab_name = "Medicare";
		public static final String MBI_name = "headerGroupControlMedicareHICN";
		public static final String medicare_className = "WindowsForms10.EDIT.app.0.293a9a3_r36_ad1";
		public static final String partAStartDate_name = "repeaterPanelPartADateRanges";
		public static final String partBStartDate_name = "repeaterPanelPartBDateRanges";
		public static final String partDStartDate_name = "repeaterPanelPartDDateRanges";
		public static final String startDates_className = "WindowsForms10.EDIT.app.0.293a9a3_r36_ad1";

	}

	public class HRPProviderChoiceDetails {
		public static final String providerChoiceTab_name = "Provider Choice";
		public static final String providerEffectiveDate_name = "Effective Date row ";
		public static final String providerType_name = "Provider Type row ";
		public static final String providerName_name = "Provider Name row ";
		public static final String providerSelectionType_name = "Selection Type row ";
		public static final String NPI_name = "Practitioner Role row ";
	}

	public static class HRPServiceRequestCreateDetails {
		public static final String closePopupwindow_id = "simpleButtonClose";
		public static final String createServiceRequest_className = "WindowsForms10.Window.b.app.0.293a9a3_r36_ad1";
		public static final String createServiceRequest_id = "SimpleButtonCreate";
		public static final String rerquestMedium_name = "Open";
		public static final String requestMediumname_name = "ShortName";
		public static final String requestMedium_xpath = "//Window[@Name ='Create Service Request - "
				+ PropertyFileReader.getHrpUserId().replace("\"", "") + "    (" + Const.HRPEnvironment
				+ ")']/Pane[@Name='paneBottom']/Tab[@Name='']/Pane[@Name='General']/Pane[@Name='']/Tab[@Name='']/Pane[@Name='General']/Pane[@Name='']/Pane[@Name='entityPanelServiceRequestIssue']/Pane[@Name='headerGroupControlServiceRequest']/Pane[@Name='entityPanelServiceRequest']/Pane[@Name='autoSizingPanelTop']/Pane[@Name='panelControlTop']/Pane[4]/Edit";
		public static final String firstName_name = "First Name:";
		public static final String lastName_name = "Last Name:";
		public static final String address_name = "Address:";
		public static final String city_name = "City:";
		public static final String zipCode_name = "Zip/Postal Code:";
		public static final String county_name = "County:";
		public static final String memberSearch_id = "referenceMenuButton";
		public static final String lookup_id = "257";
		public static final String lookupLastName_name = "Last Name";
		public static final String lookupsearchbtn_id = "simpleButtonSearch";
		public static final String resultsPopup_name = "&OK";
		public static final String lookupOkbtn_id = "simpleButtonOK";
		public static final String practitionerSearch_id = "referenceMenuButton";
		public static final String practitionerid_name = "Practitioner ID row ";
		public static final String memberid_name = "ID row ";
		public static final String supplierName_name = "Supplier Name";
		public static final String supplierId_name = "Supplier ID row ";

	}

	public class HRPCreateIssue {
		public static final String addIssue_name = "Add New";
		public static final String issueName_name = "Open";
		public static final String issue_id = "LabelControlCaption";
		public static final String summary_name = "Summary:";
		public static final String scrollDown_name = "Line Down";
		public static final String save_name = "Save";
		public static final String issueID_id = "autoEditIssueId";
		public static final String request_id = "autoEditServiceRequestId";
	}

	public static class HRPClaims {
		public static final String claimsSearch_id = "SimpleButtonSearch";
		public static final String advanced_name = "Advanced";
		public static final String claimNumber_name = "Claim Number";
		public static final String search_name = "Search";
		public static final String claimsResult_name = "Claim ID row 0";

		// Lines Tab
		public static final String linesTab_name = "Lines";
		public static final String noOfLineRecords_name = "headerGroupControlLine";
		public static final String fromDate_id = "autoEditServiceStartDate";
		public static final String toDate_id = "autoEditServiceEndDate";
		public static final String pos_id = "autoEditPOS";
		public static final String checkboxEMGStatus_id = "autoEditEmergencyStatus";
		public static final String cpt_id = "autoEditService";
		public static final String modifierCode1_id = "autoEditModifierCode1";
		public static final String modifierCode2_id = "autoEditModifierCode2";
		public static final String modifierCode3_id = "autoEditModifierCode3";
		public static final String modifierCode4_id = "autoEditModifierCode4";
		public static final String modifierCode5_id = "autoEditModifierCode5";
		public static final String charges_id = "autoEditBilledAmount";
		public static final String uos_id = "autoEditUOS";
		public static final String minutes_id = "autoEditMinutes";
		public static final String checkboxEPSDT_id = "autoEditEpsdtFamilyPlan";
		// pricing sub-tab
		public static final String allowedAmt_id = "autoEditBaseAdjustedAllowedAmount";
		public static final String paidAmt_id = "autoEditBasePaidAmount";
		public static final String nonCovered_id = "autoEditBaseNonCoveredMemberExcess";
		public static final String memberResponsibility_id = "autoEditBaseMemberResponsibilityAmount";
		public static final String deductible_id = "autoEditBaseDeductibleAmount";
		public static final String copayment_id = "autoEditBaseCoPayAmount";
		public static final String coinsurance_id = "autoEditBaseCoInsuranceAmount";
		public static final String balanceBilled_id = "autoEditBalanceBillMemberExcess";

		public static final String modifier1_xpath = "//Window[@Name ='HealthRules Manager - "
				+ PropertyFileReader.getHrpUserId().replace("\"", "") + "    (" + Const.HRPEnvironment
				+ ")']/Pane[@Name='panelControlDetail']/Pane/Tab/Pane[1]/Pane[1]/Pane/Pane/Pane/Pane/Pane/Pane[@Name='panelBottom']/"
				+ "Pane[@Name='autoSizingPanelDetailTop']/Pane[@Automationid='repeatableSubEntityPanelModifierCode1']/Pane[@Name='Delete']/Pane[@Name='panelControlAutoEdit']/Edit";
//				+ "/Pane[@Automationid='Claim']/Tab[@Automationid='tabControlClaim']/Pane[@Name='Lines']/Pane[@Automationid='claimLines']/"
//				+ "Pane[@Name='entityPanel']/Pane[@Name='repeaterClaimLines']/Pane[@Automationid='ClaimLine']/Pane[@Name='headerGroupControlLine']/Pane[@Automationid='ClaimLinePanel']/"
//				

		// Header Tab
		public static final String patientAccountNum_id = "autoEditPatientAccountNumber";
		public static final String submittedSubscriberID_id = "autoEditSubmittedSubscriberID";
		public static final String externalClaimID_id = "autoEditExternalClaimNumber";
		public static final String submitterID_id = "autoEditSubmitterID";
		public static final String submitterName_id = "autoEditSubmitterName";

		// Member
		public static final String memberName_id = "subEntityPanelLegalName";
		public static final String memberID_id = "autoEditID";
		public static final String memberBirthDate_id = "autoEditBirthDate";
		public static final String memberGender_id = "subEntityPanelMemberIle1";
		public static final String relationship_id = "subEntityPanelRelationshipToSubscriber";
		public static final String IDPrefix_id = "entityPanelConsolidatedClaim";

		// submitted subscriber information
		public static final String openSubscriberDropdown_id = "headerGroupControlSubmittedSubscriberInformation";
		public static final String subscriberName_id = "autoEditSubmittedSubscriberName";
		public static final String subscriberID_id = "autoEditSubmittedSubscriberID";
		public static final String subscriberIDPrefix_id = "textEditSubmittedIDPrefix";
		public static final String address_id = "autoEditSubmittedSubscriberAddress";
		public static final String subscriberBirthDate_id = "autoEditSubmittedSubscriberBirthDate";
		public static final String subscriberGender_id = "autoEditSubmittedSubscriberGender";
		public static final String subscriberCity_id = "autoEditSubmittedSubscriberCityName";
		public static final String subscriberState_id = "autoEditSubmittedSubscriberState";
		public static final String subscriberZipCode_id = "autoEditSubmittedSubscriberZipCode";
		public static final String subscriberGroupNumber_id = "autoEditSubmittedSubscriberGroupNumber";

		// Provider
		public static final String providerSupplierName_id = "autoEditName";
		public static final String providerSupplierID_id = "autoEditId";

		// Submitted supplier information
		public static final String openSupplierDropdown_id = "submittedSupplierInfo";
		public static final String supplierName_id = "autoEditSubmittedSupplierInfoFullName";
		public static final String supplierNPI_id = "autoEditSubmittedSupplierInfoNPI";
		public static final String supplierID_id = "autoEditSubmittedSupplierInfoID";
		public static final String supplierTaxID_id = "autoEditSubmittedSupplierInfoTaxID";
		public static final String supplierAddress_id = "autoEditSubmittedSupplierInfoAddress";
		public static final String supplierCity_id = "autoEditSubmittedSupplierInfoCityName";
		public static final String supplierZipCode_id = "autoEditSubmittedSupplierInfoZipCode";
		public static final String supplierCountry_id = "autoEditSubmittedSupplierInfoCountry";

		// Referring Physician
		public static final String physicianName_id = "subEntityPanelILE";
		public static final String physicianID_id = "autoEditReferringPhysicianID";

		// Rendering Facility
		public static final String locationName_id = "autoEditRenderingFacilityLocation";
		public static final String locationID_id = "autoEditRenderingFacilityLocationID";

		// Rendering Service Address
		public static final String openRenderingServiceAddress_id = "renderedServicesAddressControl";
		public static final String renderingFacilityAddress_id = "autoEditAddress";
		public static final String renderingFacilityCity_id = "autoEditCityName";
		public static final String renderingFacilityZipCode_id = "autoEditZipCode";
		public static final String renderingFacilityCountry_id = "autoEditCountry";

		// Totals
		public static final String submittedCarges_id = "textEditClaimLevelSubmittedCharges";
		public static final String billedAmount_id = "textEditBilledAmount";
		public static final String allowedAmount_id = "autoEditAllowedAmount";
		public static final String totalDeductible_id = "autoEditHeaderDeductible";
		public static final String coPayment_id = "autoEditHeaderCoPay";
		public static final String coInsurance_id = "autoEditHeaderCoInsurance";
		public static final String memberPaidAmount_id = "autoEditMemberPaidAmount";
		public static final String nonCoveredAmount_id = "autoEditNonCoveredMemberExcess";
		public static final String memberAmount_id = "autoEditMemberAmount";
		public static final String memberPaidToProvider_id = "autoEditMemberPaidToProvider";

		// Primary Diagnosis
		public static final String primaryDiagnosisCode_id = "codeAutoEditPrincipalDiagnosis";
		public static final String primaryDiagnosisDescription_id = "autoEditPrincipalDiagnosisDescription";
		public static final String primaryDiagnosisCodetype_id = "autoEditPrincipalDiagCodeType";

		// Additional Information
		public static final String releaseSignature_id = "autoEditInfoReleased";
		public static final String assignmentAccepted_id = "nullableCheckEditAssignmentAcceptance";
		public static final String benefitsAssigned_id = "nullableCheckEditBenefitAssignment";

	}
	
	public class HRPWorkBasket {
		public static final String subscriber0_name = "Subscriber row 0";
		public static final String workbasketDropDown_name = "Workbasket";
		public static final String enrollReviewRepair_name = "Enrollment Review & Repair";
		public static final String assign_name	 = "Assign";
		public static final String assignGroup_name = "ACL_HRP_PROD_ER_Enrollment_Manager";
		public static final String assignOKBtn_name = "&OK";
		public static final String takeOwnership_name = "Take Ownership";
		public static final String openForEdit_name = "Open for Edit";
	}

}
