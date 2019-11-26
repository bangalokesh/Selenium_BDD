package pageclasses.m360membership;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.openqa.selenium.interactions.Actions;

import enums.LocatorTypes;
import pageclasses.BasePage;
import pageobjects.M360MembershipObjRepo;

public class M360EligibilityInquiryPage extends BasePage {

	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(M360EligibilityInquiryPage.class.getName());

	public M360EligibilityInquiryPage() {
		driver = getWebDriver();
	}

	public void eligibilityCheck() {
		try {
			Actions action = new Actions(driver);
			action.moveToElement(getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360LandingPage.m360Header_xpath))
					.click(getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360LandingPage.m360EligHeader_xpath))
					.build().perform();
			Set<String> winHandles = driver.getWindowHandles();
			for (String winHandle : winHandles) {
				if (driver.getTitle().contains("Eligibility Inquiry"))
					break;
				else
					driver.switchTo().window(winHandle);
			}
			getElement(LocatorTypes.id, M360MembershipObjRepo.M360EligIquiryPage.medicare_id).sendKeys("1AD1RX4TV27");
			getElement(LocatorTypes.name, M360MembershipObjRepo.M360EligIquiryPage.lastName_name).sendKeys("MCCAMMACK");
			getElement(LocatorTypes.name, M360MembershipObjRepo.M360EligIquiryPage.queryButton_name).click();
			String a = getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360EligIquiryPage.eligTable_xpath)
					.getText();
			System.out.println(a);
			reportPass("Member Search Successful");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("M360LoginPage - login failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void searchMemberEnquiry() {
		try {
			getElement(LocatorTypes.name, M360MembershipObjRepo.M360EligIquiryPage.resetButton_name).click();
			getElement(LocatorTypes.id, M360MembershipObjRepo.M360EligIquiryPage.memID_ID)
					.sendKeys(testData.get("MedCareID"));
			getElement(LocatorTypes.name, M360MembershipObjRepo.M360EligIquiryPage.lastName_name)
					.sendKeys(testData.get("MemberLName"));
			getElement(LocatorTypes.name, M360MembershipObjRepo.M360EligIquiryPage.queryButton_name).click();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public Map<String, String> getMemberEligibilityDetails() {
		try {
			searchMemberEnquiry();
			Map<String, String> properties = new HashMap<String, String>();
			if (isAlertPresent()) {
				acceptAlert();
				return properties;
			}

			String name = getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360EligIquiryPage.name).getText();
			String memberLastname = name.split(",")[0].trim();
			String firstName = name.split(" ")[1].trim();
			String middleInitial = name.split(" ").length == 3 ? name.split(" ")[2] : null;
			String gender = getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360EligIquiryPage.gender).getText();
			gender = (gender.contains("f") || gender.contains("F")) ? "F"
					: ((gender.contains("u") || gender.contains("U")) ? "U" : "M");

			String applicantTitle = gender.equals("F") ? "Ms." : (gender.equals("U") ? null : "Mr.");

			String state = getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360EligIquiryPage.state).getText();
			String[] buffer;
			buffer = state.split("-");
			state = buffer[buffer.length - 1].trim();

			String dob = getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360EligIquiryPage.dob).getText().trim()
					.replace("/", "");

			String county = getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360EligIquiryPage.county).getText();
			buffer = county.split("-");
			county = buffer[buffer.length - 1].trim();

			String planID = getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360EligIquiryPage.planID).getText();
			String plan_enrollment = getElement(LocatorTypes.xpath,
					M360MembershipObjRepo.M360EligIquiryPage.plan_enrollment).getText().trim().replaceAll("/", "");
			String medicarePartA = getDateInMMYYYY(
					getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360EligIquiryPage.medicarePartA).getText()
							.trim());
			String medicarePartB = getDateInMMYYYY(
					getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360EligIquiryPage.medicarePartB).getText()
							.trim());

			String esrd = getElement(LocatorTypes.xpath, M360MembershipObjRepo.M360EligIquiryPage.esrd_xpath).getText()
					.trim();
			String stateMedicaid = getElement(LocatorTypes.xpath,
					M360MembershipObjRepo.M360EligIquiryPage.medicaid_xpath).getText().trim();

			properties.put("ApplicantFirstName", firstName);
			properties.put("ApplicantMiddleInitial", middleInitial);
			properties.put("ApplicantLastName", memberLastname);

			properties.put("ApplicantGender", gender);
			properties.put("ApplicantTitle", applicantTitle);

			properties.put("state", state);
			properties.put("dob", dob);
			properties.put("county", county);
			properties.put("planID", planID);
			properties.put("plan_enrollment", plan_enrollment);
			properties.put("medicarePartA", medicarePartA);
			properties.put("medicarePartB", medicarePartB);
			properties.put("esrd", esrd);
			properties.put("stateMedicaid", stateMedicaid);
			return properties;

		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}
}
