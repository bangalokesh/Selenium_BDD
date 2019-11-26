package processes;

import org.openqa.selenium.WebDriverException;

import managers.PageManager;
import pageclasses.BasePage;
import utils.Const;
import utils.Dbconn;

public class HRPCreateServiceRequestProcess extends BasePage {
	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(HRPPGRProviderValidationProcess.class.getName());
	Dbconn db = new Dbconn();
	PageManager pm = new PageManager();

	public void createServiceRequests() {
		try {
			for (int i = 0; i < Const.Number_of_service_requests; i++) {
				pm.getHRPServiceRequestCreate().navigateToServiceRequest();
				pm.getHRPServiceRequestCreate().serviceRequestPractitioner();
				pm.getHRPServiceRequestCreate().navigateToServiceRequest();
				pm.getHRPServiceRequestCreate().serviceRequestMember();
				pm.getHRPServiceRequestCreate().navigateToServiceRequest();
				pm.getHRPServiceRequestCreate().serviceRequestSupplier();
			}
		} catch (WebDriverException e) {
			e.printStackTrace();
			winDriver.quit();
		}

		catch (Exception e) {
			e.printStackTrace();
		}

	}
}