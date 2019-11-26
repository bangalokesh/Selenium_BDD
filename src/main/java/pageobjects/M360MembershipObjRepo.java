package pageobjects;

public class M360MembershipObjRepo {

	public class M360Login {
		public static final String userid_name = "User_id";
		public static final String password_name = "User_pwd";
		public static final String signin_name = "submit";
	}

	public class M360LandingPage {
		public static final String app_className = "app-button";
		public static final String app_linkText = "WiPro M360 UAT";
		public static final String m360Header_xpath = "//*[@id='td1']/a";
		public static final String m360EligHeader_xpath = "//*[@id='td1']/div/a[1]";
		public static final String m360HomeFrame_xpath = "/html/frameset/frame";
	}

	public class M360MedicareSupportPage {
		public static final String memNewSearch_name = "newEnrollSearch";
		public static final String memberHeader_xpath = "//form[@name='eemApplForm']/table/tbody/tr[1]/td/table/tbody/tr[2]/td/table[2]/tbody/tr/td[9]";
		public static final String medicareID_name = "searchHicNo";
		public static final String supplementalID_name = "searchSupplementalId";
		public static final String memberID_name = "searchMemberId";
		public static final String ssn_name = "searchSSN";
		public static final String lastName_name = "searchLastName";
		public static final String effectiveMonth_name = "searchEffectiveMonthFrmt";
		public static final String memberSearchGoButton_id = "butGoGif";
		public static final String memberDetails_xpath = "//*[@id='tblSearchResultsList']/tbody/tr[2]/td";
		public static final String memberHicID_xpath = "/html/body/form/table[3]/tbody/tr[1]/td/table[2]/tbody/tr[2]/td[4]";
		public static final String enrollment_tab_xpath = "//*[@id='enrlTab_a']";
		public static final String memEnrollmentDetails_xpath = "//*[@id='enrl0']/td";
	}

	public class M360EligIquiryPage {
		public static final String medicare_id = "strHICNbr";
		public static final String lastName_name = "qryLastName";
		public static final String dob_name = "qryBirthDate";
		public static final String queryButton_name = "btn_Submit";
		public static final String resetButton_name = "btn_Reset";
		public static final String exitButton_name = "btn_exit";
		public static final String eligTable_xpath = "//*[@id='eligResults']/tbody/tr[4]/td/table[2]/tbody/tr/td/table/tbody/tr[2]/td/table/tbody/tr/td/table";
		public static final String esrd_xpath = "//*[@id=\"eligResults\"]/tbody/tr[4]/td/table[2]/tbody/tr/td/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr[5]/td/table/tbody/tr[2]/td[5]/div";
		public static final String medicaid_xpath = "//*[@id=\"eligResults\"]/tbody/tr[4]/td/table[2]/tbody/tr/td/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr[5]/td/table/tbody/tr[3]/td[5]/div";
		public static final String memID_ID = "strHICNbr";
		public static final String name = "//*[@id=\"eligResults\"]/tbody/tr[4]/td/table[1]/tbody/tr[1]/td/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr[2]/td[3]/div";
		public static final String gender = "//*[@id=\"eligResults\"]/tbody/tr[4]/td/table[1]/tbody/tr[1]/td/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr[3]/td[3]/div";
		public static final String state = "//*[@id=\"eligResults\"]/tbody/tr[4]/td/table[1]/tbody/tr[1]/td/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr[4]/td[3]/div";
		public static final String dob = "//*[@id=\"eligResults\"]/tbody/tr[4]/td/table[1]/tbody/tr[1]/td/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr[2]/td[6]/div";
		public static final String county = "//*[@id=\"eligResults\"]/tbody/tr[4]/td/table[1]/tbody/tr[1]/td/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr[4]/td[6]/div";
		public static final String planID = "//*[@id=\"eligResults\"]/tbody/tr[6]/td/table[1]/tbody/tr/td/table/tbody/tr[2]/td/table/tbody/tr[2]/td[3]/div";
		public static final String plan_enrollment = "//*[@id=\"eligResults\"]/tbody/tr[6]/td/table[1]/tbody/tr/td/table/tbody/tr[2]/td/table/tbody/tr[2]/td[5]/div";
		public static final String medicarePartA = "//*[@id=\"eligResults\"]/tbody/tr[4]/td/table[2]/tbody/tr/td/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr[1]/td/table/tbody/tr[1]/td[3]/div";
		public static final String medicarePartB = "//*[@id=\"eligResults\"]/tbody/tr[4]/td/table[2]/tbody/tr/td/table/tbody/tr[2]/td/table/tbody/tr/td/table/tbody/tr[1]/td/table/tbody/tr[2]/td[3]/div";
	}

	public class M360MAEnroll {
		public static final String M360_xpath = "//*[@id='td1']";
		public static final String maNewMemApl_xpath = "//*[@value='MA - New Member Appl']";
		public static final String appDate_id = "appldatepicker";
		public static final String coverageDate_id = "reqdatepicker";
		public static final String enrolFName_name = "mbrFirstName";
		public static final String enrolMName_name = "mbrMiddleName";
		public static final String enrolLName_name = "mbrLastName";
		public static final String medCareId_name = "mbrHicNbr";
		public static final String eligCheck_name = "btnCheck";
		public static final String primaryAdd_name = "perAdd1";
		public static final String primaryAdd2_name = "perAdd2";
		public static final String primaryAdd3_name = "perAdd3";
		public static final String homePhone_name = "perPhone";
		public static final String workPhone_name = "perWorkPhone";
		public static final String cellPhone_name = "perCell";
		public static final String faxPhone_name = "perFax";
		public static final String email_name = "mbrEmail";
		public static final String zipcode_name = "perZip5";
		public static final String city_id = "select2-perCity-container"; // "perCity"; // *[@id="perCity"]
		public static final String enrollingProdQuestMark_xpath = "//*[@id='rxInfo']/table/tbody/tr/td/table/tbody/tr[1]/td[4]/input[5]";
		public static final String planID_name = "searchPlan";
		public static final String pbpID_name = "searchPbp";
		public static final String segmentID_name = "searchSegment";
		public static final String prodSearch_name = "btnProdSearch";
		public static final String prodSearchResult_xpath = "//*[@name=\"prodInfo\"]";
		public static final String prdSubmit_name = "btnProdSubmit";
		public static final String pcpQuestMark_name = "helpPCP";
		public static final String pcpSearchSelect_xpath = "//*[@id='data']/tbody/tr[6]/td[1]/input";
		public static final String pcpSubmit_name = "btnPcpSubmit";
		public static final String electionType_xpath = "//*[@id='eleType']";
		public static final String appReceivedDate_name = "receiptDate";
		public static final String appSign_name = "signDt";
		public static final String brokerCommAgency_name = "helpAgent";
		public static final String agencyID_name = "searchAgencyId";
		public static final String agencySearch_name = "btnAgencySearch";
		public static final String brokerAgentType_xpath = "//*[@name='brokerType']";
		public static final String brokerAgentName_xpath = "//*[@name='brokAgentId']";
		public static final String brokerCommAgencySubmit_name = "btnAgencySubmit";
		public static final String agencySearchResult_xpath = "//*[@name='agencyInfo']";
		public static final String brokerDate_name = "agentDt";
		public static final String validateApplication_xpath = "//*[@value='Validate Application']";
		public static final String updateApplication_xpath = "//*[@value='Update']";
		public static final String applicationID_name = "applId";
		public static final String applicationType_xpath = "//*[contains(@id, 'select2-applType')]";
		public static final String applicationDate_xpath = "//div[@id=\"contents\"]/table/tbody/tr[5]/td/table/tbody/tr[1]/td[7]/input";
		public static final String currentStatus_name = "currStatus";
		public static final String applicationStatus_xpath = "//*[@id=\"select2-applStatusList-container\"]";
		public static final String brokerName_xpath = "//*[@id=\"broker\"]/table/tbody/tr/td/table/tbody/tr[2]/td[2]/span[3]/span[1]/span";
		public static final String productName_xpath = "//*[@id=\"rxInfo\"]/table/tbody/tr/td/table/tbody/tr[1]/td[4]/input[4]";
		public static final String close_xpath = "//*[@id=\"eemApplForm\"]/table[1]/tbody/tr[2]/td/table[2]/tbody/tr/td[1]/a";
		public static final String brokerAgent_xpath = "//*[starts-with(@id, 'select2-brokAgentId')]";// "//*[@id=\"broker\"]/table/tbody/tr/td/table/tbody/tr[2]/td[2]/span[3]/span[1]/span/span[2]";
		public static final String searchAgentID_xpath = "/html/body/span/span/span[1]/input";// "//*[@class=\"select2-search
																								// select2-search--dropdown\"]/input";
		public static final String searchOfficeCode_name = "searchOffCd";
		public static final String officeSearch_name = "btnPcpSearch";
		public static final String selectPCP_xpath = "//*[@name=\"pcpInfo\"]";
		public static final String npiID_xpath = "//*[@id=\"data\"]/tbody/tr[1]/td[2]";
		public static final String pcpDoctor_xpath = "//*[@id=\"data\"]/tbody/tr[1]/td[4]";
		public static final String enrolDOB_name = "mbrBirthDt";
		public static final String appIDSearch = "//*[@id=\"eemSearch\"]/tbody/tr[1]/td/table/tbody/tr/td[6]/input";
		public static final String goButton = "btnGoGif";
		public static final String applicationStatus_select = "//*[@id='applStatusList']";
		public static final String pcpReset_name = "btnReset";
		public static final String npiFirstRadioBtn_xpath = "//*[@id=\"data\"]/tbody/tr[1]/td[1]/input";
		public static final String HICNum_name = "displayHic";
		public static final String rxID_name = "mbrRxId";
		public static final String supplementalID_name = "altMbrId";
		public static final String ssn_name = "mbrSsn";
		public static final String memberID_name = "mbrId";
		public static final String gender_xpath = "//*[@id=\"personalInfo\"]/table/tbody/tr/td/table/tbody/tr[2]/td[6]/span/span[1]/span";
		public static final String paymentOption_xpath = "//*[@id=\"premiumInfo\"]/table/tbody/tr/td/table/tbody/tr/td[2]/span/span[1]/span";
		public static final String lisStartDate_id = "lisStDtPicker";
		public static final String lisEndDate_id = "lisEndDtPicker";
		public static final String lisCopay_xpath = "//*[@id=\"lisInfo\"]/table/tbody/tr/td/table/tbody/tr/td[6]/span/span[1]/span";
		public static final String lisPercent_xpath = "//*[@id=\"lisInfo\"]/table/tbody/tr/td/table/tbody/tr/td[8]/span/span[1]/span";
		public static final String npiNumber_xpath = "//*[@id=\"data\"]/tbody/tr/td[2]";
		public static final String cmaProductName_xpath = "//*[@id=\"rxInfo\"]/table/tbody/tr/td/table/tbody/tr[4]/td[4]/input[4]";
		public static final String agentHelp_xpath = "//*[@id=\"broker\"]/table/tbody/tr/td/table/tbody/tr[1]/td[2]/input[3]";
		public static final String agentField_name = "searchAgentId";
		public static final String agentType_name = "searchAgentType";
		public static final String agentIDCheck_xpath = "//*[@name='brokAgentId']";
		public static final String agencyIDCheck_name = "commAgencyId";
		public static final String LocationId = "searchLocId";
	}

	public class M360MemberPage {
		// Member Page Tabs --
		public static final String pageCannotDisplay_xpath = "/html/body";
		public static final String unExpectedResponse = "/html/body/table/tbody/tr[1]/td/table/tbody/tr/td";
		public static final String close_link = "Close";
		public static final String close_tab_xpath = "//*[@name='eemEnrollForm']/table[1]/tbody/tr[2]/td/table[2]/tbody/tr/td[1]/a";
		public static final String M360_xpath = "//*[@id='td1']";
		public static final String demographicsTab_id = "demoTab_a";
		public static final String enrollmentTab_id = "enrlTab_a";
		public static final String addressTab_id = "addrTab_a";
		public static final String trrTab_id = "trrTab_a";
		public static final String pcpTab_id = "pcpTab_a";
		public static final String accretionTab_id = "accTab_a";
		public static final String LISTab_id = "lisTab_a";
		public static final String LEPTab_id = "lepTab_a";
		public static final String COBTab_id = "cobTab_a";
		public static final String medicare_id_xpath = "//*[@id=\"eemEnrollSearchDiv\"]/tbody/tr/td/table/tbody/tr[1]/td[4]/input";

		public static final String memberID_xpath = "//table[@id='tblSearchResultsList']/tbody/tr[2]/td[1]";
		public static final String supplementalID_xpath = "//table[@id='tblSearchResultsList']/tbody/tr[2]/td[3]";
		// Demographics Data
		public static final String member_xpath = "//*[@id=\"demographic\"]/table/tbody/tr[1]/td/table/tbody/tr[1]/td/table/tbody/tr";
		public static final String hicNumber_xpath = "//*[@name='eemEnrollForm']/table[3]/tbody/tr[1]/td/table[2]/tbody/tr[2]/td[4]";// "/html/body/form/table[3]/tbody/tr[1]/td/table[2]/tbody/tr[2]/td[4]";
		public static final String salutation = "//*[@id=\"demographic\"]/table/tbody/tr[1]/td/table/tbody/tr[1]/td/table/tbody/tr[2]/td[2]/select";// "//span[@class='select2-selection
																																					// select2-selection--single']";//"select2-displayDemographic.prefix-7r-container";
		public static final String firstName_xpath = "//*[@id=\"demographic\"]/table/tbody/tr[1]/td/table/tbody/tr[1]/td/table/tbody/tr[2]/td[2]/input[1]";
		public static final String middleName_xpath = "//*[@id=\"demographic\"]/table/tbody/tr[1]/td/table/tbody/tr[1]/td/table/tbody/tr[2]/td[2]/input[2]";
		public static final String lastName_xpath = "//*[@id=\"demographic\"]/table/tbody/tr[1]/td/table/tbody/tr[1]/td/table/tbody/tr[2]/td[2]/input[3]";
		public static final String suffixName_xpath = "//*[@id=\"demographic\"]/table/tbody/tr[1]/td/table/tbody/tr[1]/td/table/tbody/tr[2]/td[2]/input[4]";
		public static final String effectiveMonth_xpath = "//*[@id=\"demographic\"]/table/tbody/tr[1]/td/table/tbody/tr[1]/td/table/tbody/tr[1]/td[3]";
		public static final String status_xpath = "//*[@id=\"select2-displayDemographic.memberStatus-n8-container\"]";
		public static final String member_id_xpath = "//*[@id=\"demographic\"]/table/tbody/tr[1]/td/table/tbody/tr[1]/td/table/tbody/tr[4]/td[2]";
		public static final String ssn_xpath = "//*[@id=\"demographic\"]/table/tbody/tr[1]/td/table/tbody/tr[1]/td/table/tbody/tr[4]/td[5]/input";
		public static final String medicare_id_output_xpath = "//*[@id=\"demographic\"]/table/tbody/tr[1]/td/table/tbody/tr[1]/td/table/tbody/tr[4]/td[8]";
		public static final String gender_xpath = " //*[@id=\"demographic\"]/table/tbody/tr[1]/td/table/tbody/tr[1]/td/table/tbody/tr[6]/td[2]/select";
		public static final String dateOfBirth_xpath = "//*[@id=\"demographic\"]/table/tbody/tr[1]/td/table/tbody/tr[1]/td/table/tbody/tr[6]/td[5]/input";// input[@id='dp1563487211148']";//*[@id=\"dp1563389206888\"]";
		public static final String dateOfdeath_xpath = "//*[@id=\"dp1563389206889\"]";
		public static final String alternateCorrespondenceMethod_xpath = "//*[@id=\"select2-displayDemographic.altCorrespondenceInd-ra-container\"]";
		public static final String maritalStatus_xpath = "//*[@id=\"select2-displayDemographic.maritalStatus-hg-container\"]";
		public static final String race_xpath = "//*[@id=\"select2-displayDemographic.raceCd-dx-container\"]";
		public static final String languageCode_xpath = "//*[@id=\"select2-displayDemographic.languageCd-4z-container\"]";
		public static final String spouseWork_xpath = "//*[@id=\"select2-displayDemographic.spouseWorkInd-7p-container\"]";
		public static final String medicareID_xpath = "//*[@id=\"tblSearchResultsList\"]/tbody/tr[2]/td[2]";

		// Enrollment Data
		public static final String enrollmentGroup_array_xpath = "//*[@id=\"tblEnrollmentList\"]/tbody/tr";
		public static final String enrollmentLatestRecord_xpath = "//*[@id=\"tblEnrollmentList\"]/tbody/tr[2]";
		public static final String enrollmentStatus_id = "dispEnrollment.enrollStatus";
		public static final String enrollmentReason_id = "dispEnrollment.enrollReasonCd";
		public static final String enrollmentStartDate_id = "dispEnrollment.effStartDateFrmt";
		public static final String enrollmentEndDate_id = "dispEnrollment.effEndDateFrmt";
		public static final String enrollmentOverride_id = "dispEnrollment.overrideInd";
		public static final String groupName_id = "dispEnrollment.groupName";
		public static final String group_id_id = "dispEnrollment.grpId";
		public static final String productName_id = "dispEnrollment.productName";
		public static final String product_id_id = "dispEnrollment.productId";
		public static final String plan_id_id = "dispEnrollment.planId";
		public static final String pbp_id_id = "dispEnrollment.pbpId";
		public static final String segment_id = "dispEnrollment.pbpSegmentId";
		public static final String type_id = "dispEnrollment.planType";
		public static final String designation_id = "dispEnrollment.planDesignation";
		public static final String source_id = "dispEnrollment.enrollSource";
		public static final String trigger_txn_xpath = "//*[@id=\"tblEnrollmentDisplay\"]/tbody/tr[8]/td[4]"; // This
																												// might
																												// be an
																												// image???
		public static final String electionType_id = "dispEnrollment.electionTypeDesc";
		public static final String sepReason_id = "dispEnrollment.sepReasonDesc";
		public static final String sepDate_id = "dispEnrollment.sepElectionDateFrmt";
		public static final String receivedDate_id = "dispEnrollment.receivedDateFrmt";
		public static final String cancellationReason_id = "dispEnrollment.cancellationReasonDesc";
		public static final String bypassEdits_xpath = "//*[@id=\"tblEnrollmentDisplay\"]/tbody/tr[11]/td[2]"; // This
																												// might
																												// be an
																												// image???
		public static final String disReason_id = "dispEnrollment.disReasonDesc";
		public static final String supplemental_id_id = "dispEnrollment.supplementalId";
		public static final String rx_id_id = "dispEnrollment.rxId";
		public static final String applicationDate_id = "dispEnrollment.applicationDateFrmt";
		public static final String signatureDate_id = "dispEnrollment.signatureDateFrmt";
		public static final String applicationReceivedDate_id = "dispEnrollment.receiptDateFormat";

		// Address Data Tab
		public static final String addressType_array_id = "addr";
		public static final String addressType_array_xpath = "//*[@id=\"tblAddressList\"]/tbody/tr";
		public static final String addressType_id = "dispAddress.addrTypeDesc";
		public static final String addressStartDate_id = "dispAddress.effStartDateFrmt";
		public static final String addressEndDate_id = "dispAddress.effEndDateFrmt";
		public static final String addressOverride_id = "dispAddress.overrideInd";
		public static final String addressLineOne_id = "dispAddress.address1";
		public static final String addressLineTwo_id = "dispAddress.address2";
		public static final String addressLineThree_id = "dispAddress.address3";
		public static final String city_id = "dispAddress.city";
		public static final String state_id = "dispAddress.stateAbbr";
		public static final String zipCode_id = "dispAddress.zipCdFrmt";
		public static final String country_id = "dispAddress.countryCd";
		public static final String county_id = "dispAddress.countyName";
		public static final String homePhoneNumber_id = "dispAddress.homePhoneNbr";
		public static final String cellPhoneNumber_id = "dispAddress.cellPhoneNbr";
		public static final String workPhoneNumber_id = "dispAddress.workPhoneNbr";
		public static final String faxNumber_id = "dispAddress.faxNbr";

		// PCP Data
		public static final String pcpNumber_array_xpath = "//*[@id=\"tblPcpList\"]/tbody/tr";
		public static final String pcpStartDate_id = "dispPcpInfo.effStartDateFrmt";
		public static final String pcpEndDate_id = "dispPcpInfo.effEndDateFrmt";
		public static final String pcpOverride_id = "dispPcpInfo.overrideInd";
		public static final String pcpNumber_id = "dispPcpInfo.pcpNbr";
		public static final String pcpLocation_id = "dispPcpInfo.locationId";
		public static final String doctorName_id = "dispPcpInfo.doctorName";
		public static final String clinicName_id = "dispPcpInfo.clinicName";
		public static final String lineOfBusiness_id = "dispPcpInfo.lineOfBusiness";
		public static final String doctorAddress_id = "dispPcpInfo.doctorAddress";
		public static final String doctorCity_id = "dispPcpInfo.doctorCity";
		public static final String doctorState_id = "dispPcpInfo.doctorState";
		public static final String doctorZip_id = "dispPcpInfo.doctorZipFrmt";

		// Accretion Tab
		public static final String primaryBin_id = "dispAccretion.primBin";
		public static final String primaryPCN_id = "dispAccretion.primPcn";
		public static final String primaryRXId_id = "dispAccretion.primRxId";
		public static final String secondaryBin_id = "dispAccretion.secBin";
		public static final String primaryRXGroup_id = "dispAccretion.primeRxGrp";
		public static final String secondaryPCN_id = "dispAccretion.secPcn";
		public static final String secondaryRXId_id = "dispAccretion.secRx";
		public static final String createUserId_id = "dispAccretion.createUserId";
		public static final String dob_id = "dispAccretion.birthDate";
		public static final String partCAmount_id = "dispAccretion.prtCAmt";
		public static final String partDAmount_id = "dispAccretion.prtDAmt";
		public static final String ElectionType_id = "dispAccretion.electionType";
		public static final String priorCommercial_id = "dispAccretion.priorCommercialOvrInd";
		public static final String disEnrollement_id = "dispAccretion.disEnrollReason";
		public static final String totalUncoveredMonths_id = "dispAccretion.uncoveredMonths";
		public static final String premiumWithHoldOption_id = "dispAccretion.premiumHoldOption";
		public static final String creditableCoverageFlag_id = "dispAccretion.credCovflag";
		public static final String partDOptOut = "dispAccretion.prtDOutInd";

		// LIS Tab

		public static final String subsidySource_id = "dispLisInfo.subsidySourceDesc";
		public static final String lis_overRide_id = "dispLisInfo.overrideInd";
		public static final String coPay_id = "dispLisInfo.liCoPayDesc";
		public static final String licBAE_id = "dispLisInfo.licBaeInd";
		public static final String percent_id = "dispLisInfo.lisPctCd";
		public static final String LISBAE_id = "dispLisInfo.lisBaeInd";
		public static final String LISAmt_id = "dispLisInfo.lisAmt";
		public static final String spapAmt_id = "dispLisInfo.spapAmt";

		// LES Tab
		public static final String les_override_id = "dispLepInfo.overrideInd";
		public static final String attest_lock_indicator_id = "dispLepInfo.attstLockInd";
		public static final String lepType_id = "dispLepInfo.lepType";
		public static final String noOfUncoveredMonths_id = "dispLepInfo.nbrUncovMonths";
		public static final String lepAmt_id = "dispLepInfo.lepAmt";
		public static final String lepWaived_id = "dispLepInfo.lepWaivedAmt";
		public static final String bypassEdits_id = "dispEnrollment.editOverrideInd";
		public static final String triggerTRX73_id = "dispEnrollment.trg73Ind";

		// COB Tab
		public static final String COBType_id = "cobView.cobDesc";
		public static final String cob_override_id = "cobView.override";
		public static final String OHI_id = "cobView.ohiDesc";
		public static final String rxGrp_id = "cobView.rxGrp";
		public static final String rxName_id = "cobView.rxName";
		public static final String rxID_id = "cobView.rxId";
		public static final String rxBIN_id = "cobView.rxBin";
		public static final String rxPCN_id = "cobView.rxPcn";

		// DSInfo Data --Priya
		public static final String dsInfoTab_xpath = "//*[@id=\"dsTab_a\"]";
		public static final String partAStartDate_xpath = "//*[@id=\"dsi2\"]/td[3]";
		public static final String partBStartDate_xpath = "//*[@id=\"dsi3\"]/td[3]";
		public static final String partDStartDate_xpath = "//*[@id=\"dsi4\"]/td[3]";

		// TRR Tab Data
		public static final String transactionCode_xpath = "//*[@id=\"displayTrrLog.transactionCode\"]";
		public static final String replyCode = "//*[@id=\"displayTrrLog.transReplyDisp\"]";
		public static final String name_xpath = "/html/body/form/table[3]/tbody/tr[1]/td/table[2]/tbody/tr[2]/td[8]";
		public static final String txn_eff_date_xpath = "//*[@id=\"displayTrrLog.effectiveDateFrmt\"]";
		public static final String changed_xpath = "//*[@id=\"displayTrrLog.relatedFieldAfter\"]";
		public static final String updatePerformed_xPath = "//*[@id=\"displayTrrLog.updateYN\"]";

		// Letter Tab Data
		public static final String lettersTab_id = "ltrTab_a";
		public static final String letterRow_xpath = "//*[@id=\"tblLetterList\"]/tbody/tr";
		public static final String letterRowFirstItem_id = "ltr0";
		public static final String letterMedicareID_xpath = "//*[@id=\"tblSearchResultsList\"]/tbody/tr[2]/td[2]";

		public static final String letterName_id = "dispLetter.letterName";
		public static final String letterDescription_id = "dispLetter.letterDesc";
		public static final String letterRequestDate_id = "dispLetter.requestDate";
		public static final String letterOriginalMailDate_id = "dispLetter.origMailDate";
		public static final String letterStatus_id = "dispLetter.recordStatus";
		public static final String letterLastMailDate_id = "dispLetter.lastMailDate";
		public static final String letterPrintRequestDate_id = "dispLetter.printRequestDate";
		public static final String letterPrintDate_id = "dispLetter.printDate";
		public static final String letterReprintDate_id = "date";
		public static final String letterResponseDueDate_id = "dispLetter.responseDueDate";
		public static final String letterResponseDate_id = "dispLetter.responseDate";
	}

	public class M360Agent {
		public static final String medicare_id = "strHICNbr";
		public static final String demographicsTab_id = "demoTab_a";
		public static final String agentTab_id = "agentTab_a";
		public static final String agenttype_id = "agentView.agentTypeDesc";
		public static final String agentname_id = "agentView.agentName";
		public static final String agenttin_id = "agentView.agentTIN";
		public static final String agentphone_id = "agentView.agentPhone";
		public static final String agentemail_id = "agentView.agentEmail";
		public static final String agentPlanId_id = "agentView.planId";
		public static final String agentStartDate_id = "agentView.frmtEffStartDate";
		public static final String agentEndDate_id = "agentView.frmtEffEndDate";
		public static final String agentOverride_id = "agentView.overrideInd";

	}

	public class M360Agency {
		public static final String agencytype_id = "agentView.agencyTypeDesc";
		public static final String agencyname_id = "agentView.agencyName";
		public static final String agencytin_id = "agentView.agencyTIN";
		public static final String agencyphone_id = "agentView.agencyPhone";
		public static final String agencyemail_id = "agentView.agencyEmail";
	}

	public class M360LetterReview {
		public static final String letterReviewTab_xpath = "/html/body/form/table/tbody/tr[1]/td/table/tbody/tr[2]/td/table[2]/tbody/tr/td[13]/a";
		public static final String letterReviewSourceDropdown_xpath = "//*[starts-with(@id, 'select2-searchType-') and contains(@id, 'container')]";
		public static final String letterReviewSourceDropdownSearchField_xpath = "/html/body/span/span/span[1]/input";
		public static final String letterReviewMemberIdTextField_xpath = "//*[@id=\"eemLetterReviewSearchDiv\"]/tbody/tr/td/table/tbody/tr[1]/td[3]/input";
		public static final String memberSearchGoButton_xpath = "//*[@id=\"eemLetterReviewSearchDiv\"]/tbody/tr/td/table/tbody/tr[1]/td[6]/input";
		public static final String letterReviewSupplementalId_xpath = "//*[@id=\"tblLetterReview\"]/tbody/tr[1]/td[6]";
		public static final String letterReviewStatus_xpath = "//*[@id=\"tblLetterReview\"]/tbody/tr[1]/td[8]/select";
		public static final String letterReviewLetter_xpath = "//*[@id=\"tblLetterReview\"]/tbody/tr[2]/td[2]";
		public static final String letterReviewPrintRequestDate_name = "displayCorrMbr.printRequestDate";
		public static final String letterReviewOrigMailDate_name = "displayCorrMbr.origMailDate";
		public static final String letterReviewLastMailDate_name = "displayCorrMbr.lastMailDate";
		public static final String letterReviewBatch_xpath = "//*[@id=\"tblLetterReview\"]/tbody/tr[3]/td[8]";
		public static final String letterReviewDatePrinted_name = "displayCorrMbr.printDate";
		public static final String letterReviewReprintDate_name = "displayCorrMbr.reprintDate";
		public static final String letterReviewResponseDueDate_name = "displayCorrMbr.responseDueDate";
		public static final String letterReviewResponseDate_name = "displayCorrMbr.responseDate";
		public static final String letterReviewCreateTime_xpath = "//*[@id=\"tblLetterReview\"]/tbody/tr[6]/td[2]";
		public static final String letterReviewTableFrame_xpath = "//*[@id=\"letterDisplayVarData\"]/table/tbody";
		public static final String letterReviewTblSearchResultsList_xpath = "//*[@id=\"tblSearchResultsList\"]/tbody/tr";
		public static final String letterNameOEVLetter_xpath = "//*[@id=\"tblSearchResultsList\"]/tbody/tr[2]/td[5]";
		public static final String letterNameConfirmEnrollment_xpath = "//*[@id=\"tblSearchResultsList\"]/tbody/tr[3]/td[5]";
		public static final String applicationEntryOnLetterReviewTab_xpath = "/html/body/form/table[1]/tbody/tr[2]/td/table[2]/tbody/tr/td[3]/a";
	}

	public class M360LetterRequest {
		public static final String letterRequestTab_xpath = "/html/body/form/table/tbody/tr[1]/td/table/tbody/tr[2]/td/table[2]/tbody/tr/td[11]/a";
		public static final String letterRequestTblSearchResultsList_xpath = "//*[@id=\"tblSearchResultsList\"]/tbody/tr";
		public static final String letterRequestSourceDropdown_xpath = "//*[starts-with(@id, 'select2-searchType-') and contains(@id, 'container')]";
		public static final String letterRequestSourceDropdownSearchField_xpath = "/html/body/span/span/span[1]/input";
		public static final String letterRequestMemberIdTextField_xpath = "//*[@id=\"eemLetterRequestSearchDiv\"]/table/tbody/tr[1]/td[4]/input";
		public static final String memberSearchGoButton_id = "btnGoGif";
		public static final String letterRequestButton_name = "newLtrReq";
		public static final String letterRequestFirstName_id = "displayLetterReq.mbrFName";
		public static final String letterRequestLastName_id = "displayLetterReq.mbrLName";
		public static final String letterRequestType_id = "displayLetterReq.triggerTypeDesc";
		public static final String letterRequestStatus_id = "displayLetterReq.triggerStatusDesc";
		public static final String letterRequestPlan_xpath = "//*[@id=\"letterReqDisplay\"]/table/tbody/tr[1]/td/table/tbody/tr[4]/td[2]";
		public static final String letterRequestPBP_xpath = "//*[@id=\"letterReqDisplay\"]/table/tbody/tr[1]/td/table/tbody/tr[4]/td[6]";
		public static final String letterRequestDesignation_xpath = "//*[@id=\"letterReqDisplay\"]/table/tbody/tr[1]/td/table/tbody/tr[4]/td[8]";
		public static final String letterRequestEffectiveDate_xpath = "//*[@id=\"letterReqDisplay\"]/table/tbody/tr[1]/td/table/tbody/tr[4]/td[10]";
		public static final String letterRequestDescription_xpath = "//*[@id=\"letterReqDisplay\"]/table/tbody/tr[1]/td/table/tbody/tr[7]/td[2]";
		public static final String CloseTab_xpath = "/html/body/form/table/tbody/tr[1]/td/table/tbody/tr[2]/td/table[2]/tbody/tr/td[1]/a";
		public static final String applicationEntryOnLetterRequestTab_xpath = "/html/body/form/table[3]/tbody/tr/td[3]/a";
	}

	public class M360CMA {
		public static final String maContractChange_xpath = "//*[@value='MA - Contract Chg Form']";
		public static final String CMAenrollingProdQuestMark_xpath = "//*[@id=\"rxInfo\"]/table/tbody/tr/td/table/tbody/tr[4]/td[4]/input[5]";

	}
}
