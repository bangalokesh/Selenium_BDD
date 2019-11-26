package managers;

import org.openqa.selenium.WebDriver;

import org.openqa.selenium.support.PageFactory;

import EnrollmentFiles.CreateFileFromLayout;
import EnrollmentFiles.CreateTRRFileFromTable;
import pageclasses.BasePage;
import pageclasses.brokerPortal.ApplicationTrackerPage;
import pageclasses.brokerPortal.BrokerLoginPage;
import pageclasses.brokerPortal.CommissionReportPage;
import pageclasses.callidus.CallidusAgentPage;
import pageclasses.callidus.CallidusCustomerPage;
import pageclasses.callidus.CallidusHomePage;
import pageclasses.callidus.CallidusLoginPage;
import pageclasses.guidingCare.GuidingCareComplaintsPage;
import pageclasses.guidingCare.GuidingCareDecision;
import pageclasses.guidingCare.GuidingCareLoginPage;
import pageclasses.guidingCare.GuidingCareMemberDetailsPage;
import pageclasses.guidingCare.GuidingCarePage;
import pageclasses.guidingCare.GuidingCarePreAuthorization;
import pageclasses.guidingCare.GuidingCarePreAuthorizationActivity;
import pageclasses.guidingCare.GuidingCareProviderPage;
import pageclasses.healthX.HealthXAdminPage;
import pageclasses.healthX.HealthXLoginPage;
import pageclasses.healthX.HealthXMemberPage;
import pageclasses.healthX.HealthXProviderPage;
import pageclasses.hrp.HRPClaimsPage;
import pageclasses.hrp.HRPHomePage;
import pageclasses.hrp.HRPLoginPage;
import pageclasses.hrp.HRPMemberSearch;
import pageclasses.hrp.HRPMemberViewDetails;
import pageclasses.hrp.HRPProviderSearch;
import pageclasses.hrp.HRPServiceRequestCreate;
import pageclasses.hrp.HRPSupplierSearch;
import pageclasses.hrp.HRPSupplierViewDetails;
import pageclasses.hrp.HRPWorkBasketPage;
import pageclasses.m360.M360HomePage;
import pageclasses.m360.M360LoginPage;
import pageclasses.m360billing.CreatePaymentFile;
import pageclasses.m360billing.M360BillingInvoicePage;
import pageclasses.m360billing.M360MemberPaymentsPage;
import pageclasses.m360membership.M360EligibilityInquiryPage;
import pageclasses.m360membership.M360LetterRequestPage;
import pageclasses.m360membership.M360LetterReviewPage;
import pageclasses.m360membership.M360MAEnrollPage;
import pageclasses.m360membership.M360MemberPage;
import pageclasses.m360membership.Member_ID_Card;
import pageclasses.m360membership.OECMemberEnrollment;
import processes.BrokerPortalProcess;
import processes.EncounterDetailsProcess;
import processes.GuidingCareProviderProcess;
import processes.HRPClaimsValidationProcess;
import processes.HRPCreateServiceRequestProcess;
import processes.HRPMemberValidationProcess;
import processes.HRPPGRProviderValidationProcess;
import processes.HRPWorkbasketProcess;
import processes.HealthXProcess;
import processes.M360BillingMemPaymentProcess;
import processes.M360EnrollmentProcess;
import processes.ProviderProcess;
import x12.EligibilityAdvise.EligibilityFileCreation;
import x12.claims.Claim835FileValidation;
import x12.claims.ClaimFileCreation;
import x12.encounters.EncounterData;
import x12.encounters.EncounterDetailsPage;
import x12.encounters.EncountersHomePage;
import x12.enrollments.EnrollmentFileGenerator;
import x12.enums.ClaimType;

public class PageManager {

	private WebDriver driver;

	private BasePage basePage;
	private HRPHomePage hrpHomePage;
	private HRPLoginPage hrpLoginPage;
	private HRPMemberSearch hrpMemSearchPage;
	private HRPMemberViewDetails hrpMemViewDetails;
	private HRPProviderSearch hrpProviderSearch;
	private HRPSupplierSearch hrpSupplierSearch;
	private HRPSupplierViewDetails hrpSupplierViewDetails;
	private M360LoginPage m360LoginPage;
	private M360MemberPage m360MemPage;
	private M360EligibilityInquiryPage m360ElgInquiryPage;
	private CallidusLoginPage callidusLoginPage;
	private CallidusHomePage callidusHomePage;
	private CallidusCustomerPage callidusCustomerPage;
	private CallidusAgentPage callidusAgentPage;
	private M360MAEnrollPage m360MAEnrollPage;
	private M360EnrollmentProcess m360EnrollmentProcess;
	private BrokerLoginPage brokerLoginPage;
	private ApplicationTrackerPage applicationTrackerPage;
	private CommissionReportPage commissionreportPage;
	private BrokerPortalProcess brokerPortalProcess;
	private M360LetterRequestPage m360letterRequestPage;
	private M360LetterReviewPage m360letterReviewPage;
	private HRPMemberValidationProcess getHRPMemberValidationProcess;
	private M360MemberPaymentsPage m360MemberPaymentsPage;
	private M360BillingMemPaymentProcess m360BillingMemPaymentProcess;
	private OECMemberEnrollment oecMemberEnrollmentPage;
	private CreateFileFromLayout createFileFromLayoutPage;
	private CreatePaymentFile createPaymentFile;
	private M360BillingInvoicePage m360BillingInvoicePage;
	private CreateTRRFileFromTable createTRRFileFromTable;
	private M360HomePage m360HomePage;
	private GuidingCareLoginPage guidingCareLoginPage;
	private GuidingCarePage guidingCarePage;
	private GuidingCareProviderPage guidingCareProviderPage;
	private GuidingCareProviderProcess guidingCareProviderProcess;
	private ProviderProcess providerProcess;
	private GuidingCareMemberDetailsPage guidingCareMemberDetails;
	private Member_ID_Card member_id_card;
	private HRPPGRProviderValidationProcess hrp_pgr_providerProcess;
	private CreateTRRFileFromTable trr_file_data;
	private EncountersHomePage encountersHomePage;
	private EncounterDetailsPage encounterDetailsPage;
	private GuidingCarePreAuthorization guidingCarePreAuthorization;
	private EncounterData encounterData;
	private EncounterDetailsProcess encounterDetailsProcess;
	private HRPServiceRequestCreate hrpServiceRequestCreate;
	private HRPCreateServiceRequestProcess hrpCreateServiceRequestProcess;
	private GuidingCareComplaintsPage guidingCareComplaintsPage;
	private GuidingCareDecision guidingCareDecision;
	private GuidingCarePreAuthorizationActivity guidingCarePreAuthorizationActivity;
	private HRPClaimsValidationProcess hrpClaimsValidationProcess;
	private HRPClaimsPage hrpClaimsPage;
	private HealthXLoginPage healthXLoginPage;
	private HealthXAdminPage healthXAdminPage;
	private HealthXProviderPage healthXProviderPage;
	private HealthXMemberPage healthXMemberPage;
	private HealthXProcess healthXProcess;
	private HRPWorkbasketProcess hrpWorkBasketProcess;
	private HRPWorkBasketPage hrpWorkBasketPage;
	private Claim835FileValidation claimFile835;
	private ClaimFileCreation claimFile837;
	private EnrollmentFileGenerator enrollmentFileGen;
	private EligibilityFileCreation createEligibilityCheckFile;

	public PageManager() {
		PageFactory.initElements(driver, this);
	}

	public M360BillingMemPaymentProcess getM360BillingMemPaymentProcess() {
		return (m360BillingMemPaymentProcess == null)
				? m360BillingMemPaymentProcess = new M360BillingMemPaymentProcess()
				: m360BillingMemPaymentProcess;
	}

	public HRPCreateServiceRequestProcess getHRPCreateServiceRequestProcess() {
		return (hrpCreateServiceRequestProcess == null)
				? hrpCreateServiceRequestProcess = new HRPCreateServiceRequestProcess()
				: hrpCreateServiceRequestProcess;
	}

	public HRPServiceRequestCreate getHRPServiceRequestCreate() {
		return (hrpServiceRequestCreate == null)
				? hrpServiceRequestCreate = new HRPServiceRequestCreate(BasePage.winDriver)
				: hrpServiceRequestCreate;
	}

	public BrokerLoginPage getBrokerLoginPage() {
		return (brokerLoginPage == null) ? brokerLoginPage = new BrokerLoginPage() : brokerLoginPage;
	}

	public CommissionReportPage getCommissionReportPage() {
		return (commissionreportPage == null) ? commissionreportPage = new CommissionReportPage()
				: commissionreportPage;
	}

	public M360MemberPaymentsPage getM360MemberPaymentsPage() {
		return (m360MemberPaymentsPage == null) ? m360MemberPaymentsPage = new M360MemberPaymentsPage()
				: m360MemberPaymentsPage;
	}

	public ApplicationTrackerPage getApplicationTrackerPage() {
		return (applicationTrackerPage == null) ? applicationTrackerPage = new ApplicationTrackerPage()
				: applicationTrackerPage;
	}

	public BrokerPortalProcess getBrokerPortalProcess() {
		return (brokerPortalProcess == null) ? brokerPortalProcess = new BrokerPortalProcess() : brokerPortalProcess;
	}

	public BasePage getBasePage() {
		return (basePage == null) ? basePage = new BasePage() : basePage;
	}

	public HRPHomePage getHrpHomePage() {
		return (hrpHomePage == null) ? hrpHomePage = new HRPHomePage(BasePage.winDriver) : hrpHomePage;
	}

	public HRPLoginPage getLoginPage() {
		return (hrpLoginPage == null) ? hrpLoginPage = new HRPLoginPage(BasePage.winDriver) : hrpLoginPage;
	}

	public HRPMemberSearch getHrpMemSearchPage() {
		return (hrpMemSearchPage == null) ? hrpMemSearchPage = new HRPMemberSearch(BasePage.winDriver)
				: hrpMemSearchPage;
	}

	public HRPMemberViewDetails getHrpMemViewDetails() {
		return (m360LoginPage == null) ? hrpMemViewDetails = new HRPMemberViewDetails(BasePage.winDriver)
				: hrpMemViewDetails;
	}

	public M360LoginPage getM360LoginPage() {
		return (m360LoginPage == null) ? m360LoginPage = new M360LoginPage() : m360LoginPage;
	}

	public M360MemberPage getM360memPage() {
		return (m360MemPage == null) ? m360MemPage = new M360MemberPage() : m360MemPage;
	}

	public M360EligibilityInquiryPage getm360ElgInquiryPage() {
		return (m360ElgInquiryPage == null) ? m360ElgInquiryPage = new M360EligibilityInquiryPage()
				: m360ElgInquiryPage;
	}

	public HRPProviderSearch getHRPProviderSearchPage() {
		return (hrpProviderSearch == null) ? hrpProviderSearch = new HRPProviderSearch(BasePage.winDriver)
				: hrpProviderSearch;
	}

	public HRPSupplierSearch getHRPSupplierSearchPage() {
		return (hrpSupplierSearch == null) ? hrpSupplierSearch = new HRPSupplierSearch(BasePage.winDriver)
				: hrpSupplierSearch;
	}

	public HRPSupplierViewDetails getHRPSupplierViewDetailsPage() {
		return (hrpSupplierViewDetails == null) ? hrpSupplierViewDetails = new HRPSupplierViewDetails()
				: hrpSupplierViewDetails;
	}

	public M360MAEnrollPage getm360MAEnrollPage() {
		return (m360MAEnrollPage == null) ? m360MAEnrollPage = new M360MAEnrollPage() : m360MAEnrollPage;
	}

	public M360EnrollmentProcess getm360EnrollmentProcess() {
		return (m360EnrollmentProcess == null) ? m360EnrollmentProcess = new M360EnrollmentProcess()
				: m360EnrollmentProcess;
	}

	public CallidusLoginPage getCallidusLoginPage() {
		return (callidusLoginPage == null) ? callidusLoginPage = new CallidusLoginPage() : callidusLoginPage;
	}

	public CallidusHomePage getCallidusHomePage() {
		return (callidusHomePage == null) ? callidusHomePage = new CallidusHomePage() : callidusHomePage;
	}

	public CallidusCustomerPage getCallidusCustomerPage() {
		return (callidusCustomerPage == null) ? callidusCustomerPage = new CallidusCustomerPage()
				: callidusCustomerPage;
	}

	public CallidusAgentPage getCallidusAgentPage() {
		return (callidusAgentPage == null) ? callidusAgentPage = new CallidusAgentPage() : callidusAgentPage;
	}

	public M360LetterRequestPage getM360LetterRequestPage() {
		return (m360letterRequestPage == null) ? m360letterRequestPage = new M360LetterRequestPage()
				: m360letterRequestPage;
	}

	public M360LetterReviewPage getM360LetterReviewPage() {
		return (m360letterReviewPage == null) ? m360letterReviewPage = new M360LetterReviewPage()
				: m360letterReviewPage;
	}

	public HRPMemberValidationProcess getHRPMemberValidationProcess() {
		return (getHRPMemberValidationProcess == null)
				? getHRPMemberValidationProcess = new HRPMemberValidationProcess()
				: getHRPMemberValidationProcess;
	}

	public OECMemberEnrollment getoecMemberEnrollmentPage() {
		return (oecMemberEnrollmentPage == null) ? oecMemberEnrollmentPage = new OECMemberEnrollment()
				: oecMemberEnrollmentPage;
	}

	public CreateFileFromLayout getCreateFileFromLayout() {
		return (createFileFromLayoutPage == null) ? createFileFromLayoutPage = new CreateFileFromLayout()
				: createFileFromLayoutPage;
	}

	public CreatePaymentFile getCreateBillFromTable() {
		return (createPaymentFile == null) ? createPaymentFile = new CreatePaymentFile() : createPaymentFile;
	}

	public M360BillingInvoicePage getM360BillingInvoicePage() {
		return (m360BillingInvoicePage == null) ? m360BillingInvoicePage = new M360BillingInvoicePage()
				: m360BillingInvoicePage;
	}

	public CreateTRRFileFromTable createTRRFileFromTable() {
		return (createTRRFileFromTable == null) ? createTRRFileFromTable = new CreateTRRFileFromTable()
				: createTRRFileFromTable;
	}

	public M360HomePage getM360HomePage() {
		return (m360HomePage == null) ? m360HomePage = new M360HomePage() : m360HomePage;
	}

	public GuidingCareLoginPage getGuidingCareLoginPage() {
		return (guidingCareLoginPage == null) ? guidingCareLoginPage = new GuidingCareLoginPage()
				: guidingCareLoginPage;
	}

	public GuidingCarePage getGuidingCarePage() {
		return (guidingCarePage == null) ? guidingCarePage = new GuidingCarePage() : guidingCarePage;
	}

	public GuidingCareProviderPage getGuidingCareProviderPage() {
		return (guidingCareProviderPage == null) ? guidingCareProviderPage = new GuidingCareProviderPage()
				: guidingCareProviderPage;
	}

	public GuidingCareProviderProcess getGuidingCareProviderProcess() {
		return (guidingCareProviderProcess == null) ? guidingCareProviderProcess = new GuidingCareProviderProcess()
				: guidingCareProviderProcess;
	}

	public ProviderProcess getproviderProcess() {
		return (providerProcess == null) ? providerProcess = new ProviderProcess() : providerProcess;
	}

	public GuidingCareMemberDetailsPage getguidingCareMemberDetails() {
		return (guidingCareMemberDetails == null) ? guidingCareMemberDetails = new GuidingCareMemberDetailsPage()
				: guidingCareMemberDetails;
	}

	public Member_ID_Card getVerifyMemberIDCard() {
		return (member_id_card == null) ? member_id_card = new Member_ID_Card() : member_id_card;
	}

	public HRPPGRProviderValidationProcess getHRPPGRProviderProcess() {
		return (hrp_pgr_providerProcess == null) ? hrp_pgr_providerProcess = new HRPPGRProviderValidationProcess()
				: hrp_pgr_providerProcess;
	}

	public CreateTRRFileFromTable getTrrFileData() {
		return (trr_file_data == null) ? trr_file_data = new CreateTRRFileFromTable() : trr_file_data;
	}

	public EncountersHomePage getEncountersHomePage() {
		return (encountersHomePage == null) ? encountersHomePage = new EncountersHomePage() : encountersHomePage;
	}

	public EncounterDetailsPage getEncountersDetailsPage() {
		return (encounterDetailsPage == null) ? encounterDetailsPage = new EncounterDetailsPage()
				: encounterDetailsPage;
	}

	public GuidingCarePreAuthorization getGuidingCarePreAuthorization() {
		return (guidingCarePreAuthorization == null) ? guidingCarePreAuthorization = new GuidingCarePreAuthorization()
				: guidingCarePreAuthorization;
	}

	public EncounterData getEncounterDataPage() {
		return (encounterData == null) ? encounterData = new EncounterData() : encounterData;
	}

	public EncounterDetailsProcess getEncounterDetailsProcess() {
		return (encounterDetailsProcess == null) ? encounterDetailsProcess = new EncounterDetailsProcess()
				: encounterDetailsProcess;
	}

	public GuidingCareComplaintsPage getGuidingCareComplaintsPage() {
		return (guidingCareComplaintsPage == null) ? guidingCareComplaintsPage = new GuidingCareComplaintsPage()
				: guidingCareComplaintsPage;
	}

	public GuidingCareDecision getguidingCareDecision() {
		return guidingCareDecision == null ? guidingCareDecision = new GuidingCareDecision() : guidingCareDecision;
	}

	public GuidingCarePreAuthorizationActivity getGuidingCarePreAuthorizationActivity() {
		return (guidingCarePreAuthorizationActivity == null)
				? guidingCarePreAuthorizationActivity = new GuidingCarePreAuthorizationActivity()
				: guidingCarePreAuthorizationActivity;
	}

	public HRPClaimsValidationProcess getHRPClaimsValidationProcess() {
		return (hrpClaimsValidationProcess == null) ? hrpClaimsValidationProcess = new HRPClaimsValidationProcess()
				: hrpClaimsValidationProcess;
	}

	public HRPClaimsPage getHRPClaimsPage() {
		return (hrpClaimsPage == null) ? hrpClaimsPage = new HRPClaimsPage(BasePage.winDriver) : hrpClaimsPage;
	}
	
	public HealthXLoginPage getHealthXLoginPage() {
		return (healthXLoginPage == null) ? healthXLoginPage = new HealthXLoginPage() : healthXLoginPage;
	}
	
	public HealthXAdminPage getHealthXAdminPage() {
		return (healthXAdminPage == null) ? healthXAdminPage = new HealthXAdminPage() : healthXAdminPage;
	}
	
	public HealthXProviderPage getHealthXProviderPage() {
		return (healthXProviderPage == null) ? healthXProviderPage = new HealthXProviderPage() : healthXProviderPage;
	}
	
	public HealthXMemberPage getHealthXMemberPage() {
		return (healthXMemberPage == null) ? healthXMemberPage = new HealthXMemberPage() : healthXMemberPage;
	}
	
	public HealthXProcess getHealthXProcess() {
		return (healthXProcess == null) ? healthXProcess = new HealthXProcess() : healthXProcess;
	}
	
	public HRPWorkbasketProcess getHRPWorkbasketProcess() {
		return (hrpWorkBasketProcess == null) ? hrpWorkBasketProcess = new HRPWorkbasketProcess() : hrpWorkBasketProcess;
	}
	
	public HRPWorkBasketPage getHRPWorkBasketPage() {
		return (hrpWorkBasketPage == null) ? hrpWorkBasketPage = new HRPWorkBasketPage(BasePage.winDriver) : hrpWorkBasketPage;
	}
	
	public Claim835FileValidation getClaim835FileValidation() {
		return (claimFile835 == null) ? claimFile835 = new Claim835FileValidation() : claimFile835;
	}
	
	public ClaimFileCreation getClaimFileCreationProf() {
		return (claimFile837 == null) ? claimFile837 = new ClaimFileCreation(ClaimType.Professional) : claimFile837;
	}
	
	public ClaimFileCreation getClaimFileCreationInst() {
		return (claimFile837 == null) ? claimFile837 = new ClaimFileCreation(ClaimType.Institutional) : claimFile837;
	}
	
	public EnrollmentFileGenerator getEnrollmentFile() {
		return (enrollmentFileGen == null) ? enrollmentFileGen = new EnrollmentFileGenerator() : enrollmentFileGen;
	}
	
	public EligibilityFileCreation getEligibilityCheckFile() {
		return (createEligibilityCheckFile == null) ? createEligibilityCheckFile = new EligibilityFileCreation() : createEligibilityCheckFile;
	}
	
}
