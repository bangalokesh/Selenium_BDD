package pageclasses.guidingCare;

import java.io.IOException;

import com.aventstack.extentreports.Status;

import enums.LocatorTypes;
import managers.PropertyFileReader;
import pageclasses.BasePage;
import pageobjects.GuidingCareObjRepo;

public class GuidingCareLoginPage extends BasePage {

	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(GuidingCareLoginPage.class.getName());

	public GuidingCareLoginPage() {
		driver = getWebDriver();
	}

	public void login() {
		try {
			String uri = PropertyFileReader.getGuidingCareUrl().replace("\"", "");
			String userid = PropertyFileReader.getGuidingCareUserId().replace("\"", "");
			String password = decodeEncryptedString("guidingCareUrl");
			driver.navigate().to(uri);
			wait(2);
			if (driver.getTitle().trim().equals("..:: Altruista Health ::..")) {
				test.log(Status.INFO, "Navigated to Guiding Care Page");
				reportPass("Guiding Care login successful");
			} else {
				getElement(LocatorTypes.id, GuidingCareObjRepo.GuidingCareLogin.userName_id).sendKeys(userid);
				getElement(LocatorTypes.id, GuidingCareObjRepo.GuidingCareLogin.password_id).sendKeys(password);
				getElement(LocatorTypes.id, GuidingCareObjRepo.GuidingCareLogin.submitButton_id).click();

				if (!driver.getCurrentUrl().contains("Login"))
					reportPass("Guiding Care login successful");
				else {
					reportFail("Guiding Care login failed");
					setContinueExecution(false);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Guiding Care - login failed exception");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void login(String userid, String password) {
		try {
			String uri = PropertyFileReader.getGuidingCareUrl().replace("\"", "");
			driver.navigate().to(uri);
			wait(2);
			if (driver.getTitle().trim().equals("..:: Altruista Health ::..")) {
				test.log(Status.INFO, "Navigated to Guiding Care Page");
				reportPass("Guiding Care login successful");
			} else {
				getElement(LocatorTypes.id, GuidingCareObjRepo.GuidingCareLogin.userName_id).sendKeys(userid);
				getElement(LocatorTypes.id, GuidingCareObjRepo.GuidingCareLogin.password_id).sendKeys(password);
				getElement(LocatorTypes.id, GuidingCareObjRepo.GuidingCareLogin.submitButton_id).click();

				if (!driver.getCurrentUrl().contains("Login"))
					reportPass("Guiding Care login successful");
				else {
					reportFail("Guiding Care login failed");
					setContinueExecution(false);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Guiding Care - login failed exception");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void loginAsUMIntake() {

		try {
			String userid = PropertyFileReader.getGuidingCareIntakeUserId().replace("\"", "");
			String password = decodeEncryptedString("guidingCareUMIntake");
			login(userid, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loginAsUMSupervisor() {

		try {
			String userid = PropertyFileReader.getGuidingCareUMSupervisorUserId().replace("\"", "");
			String password = decodeEncryptedString("guidingCareUMSupervisor");
			login(userid, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loginAsUMClinicalReviewer() {

		try {
			String userid = PropertyFileReader.getGuidingCareUMClinicalReviewerUserId().replace("\"", "");
			String password = decodeEncryptedString("guidingCareUMClinicalReviewer");
			login(userid, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loginAsPhysicianReviewer() {

		try {
			String userid = PropertyFileReader.getGuidingCarePhysicianReviewerUserId().replace("\"", "");
			String password = decodeEncryptedString("guidingCarePhysicianReviewer");
			login(userid, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loginAsUMTech() {

		try {
			String userid = PropertyFileReader.getGuidingCareUMTechUserId().replace("\"", "");
			String password = decodeEncryptedString("guidingCareUMTech");
			login(userid, password);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void loginAsRole(String role) {

		switch (role) {
		case "Physician Reviewer":
			loginAsPhysicianReviewer();
			break;

		case "Clinical Reviewer":
			loginAsUMClinicalReviewer();
			break;

		case "Supervisor":
			loginAsUMSupervisor();
			break;

		case "UMIntake":
			loginAsUMIntake();
			break;

		case "UMTech":
			loginAsUMTech();
		}

	}

}