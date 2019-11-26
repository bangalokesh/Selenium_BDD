package pageclasses.callidus;

import java.io.IOException;
import java.util.HashMap;

import com.aventstack.extentreports.Status;

import enums.LocatorTypes;
import pageclasses.BasePage;
import pageobjects.CallidusObjRepo;

public class CallidusCustomerPage extends BasePage {

	public CallidusCustomerPage() {

	}

	public boolean searchCustomer() {
		boolean flag = false;
		try {
			test.log(Status.INFO, testData.get("SupplementalID"));
			getElement(LocatorTypes.xpath, CallidusObjRepo.CallidusHomePageMenuBar.customers_xpath).click();
			getElement(LocatorTypes.id, CallidusObjRepo.CallidusCustomerPage.clearCustomer_id).click();
			wait(1);
			getElement(LocatorTypes.id, CallidusObjRepo.CallidusCustomerPage.customerId_id)
					.sendKeys(testData.get("SupplementalID").substring(3));
			getElement(LocatorTypes.id, CallidusObjRepo.CallidusCommon.searchbutton_id).click();

			if (!isElementPresent(LocatorTypes.id, CallidusObjRepo.CallidusCustomerPage.policieslink_id)) {
				reportFail("Customer not found in callidus application" + testData.get("SupplementalID"));
				flag = false;
			} else {
				flag = true;
			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			try {
				reportFail("Error while searching for customer." + testData.get("SupplementalID"));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}

	public HashMap<String, String> returnCustomerDemographics() {
		HashMap<String, String> tempCustomerDetails = new HashMap<String, String>();
		try {
			getElement(LocatorTypes.id, CallidusObjRepo.CallidusCustomerPage.policieslink_id).click();
			if (getElement(LocatorTypes.xpath, CallidusObjRepo.CallidusCustomerPage.row1CustID_xpath).getText()
					.contains(testData.get("SupplementalID").substring(3))) {
				getElement(LocatorTypes.xpath, CallidusObjRepo.CallidusCustomerPage.row1Checkbox_xpath).click();
				getElement(LocatorTypes.id, CallidusObjRepo.CallidusCustomerPage.searchbutton_id).click();

				tempCustomerDetails.put("firstname",
						getElement(LocatorTypes.name, CallidusObjRepo.CallidusCustomerPage.custFirstName_name)
								.getAttribute("value"));
				tempCustomerDetails.put("middleInitial",
						getElement(LocatorTypes.name, CallidusObjRepo.CallidusCustomerPage.custMiddleIni_name)
								.getAttribute("value"));
				tempCustomerDetails.put("lastname",
						getElement(LocatorTypes.name, CallidusObjRepo.CallidusCustomerPage.custLastName_name)
								.getAttribute("value"));
				tempCustomerDetails.put("birthdate",
						getElement(LocatorTypes.name, CallidusObjRepo.CallidusCustomerPage.custDOB_name)
								.getAttribute("value"));
				tempCustomerDetails.put("MEDICAREID",
						getElement(LocatorTypes.name, CallidusObjRepo.CallidusCustomerPage.MBI_name)
								.getAttribute("value"));
				tempCustomerDetails.put("effectivemonth",
						getElement(LocatorTypes.name, CallidusObjRepo.CallidusCustomerPage.effectiveDate_name)
								.getAttribute("value"));
				tempCustomerDetails.put("ContarctNumber",
						getElement(LocatorTypes.name, CallidusObjRepo.CallidusCustomerPage.contractNumber_name)
								.getAttribute("value"));
				tempCustomerDetails.put("PBPID",
						getElement(LocatorTypes.name, CallidusObjRepo.CallidusCustomerPage.planID_name)
								.getAttribute("value"));
				tempCustomerDetails.put("ApplicationSignatureDate",
						getElement(LocatorTypes.name, CallidusObjRepo.CallidusCustomerPage.applicationSignDate_name)
								.getAttribute("value"));
				tempCustomerDetails.put("ApplicationDate",
						getElement(LocatorTypes.name, CallidusObjRepo.CallidusCustomerPage.applicationReceivedDate_name)
								.getAttribute("value"));
				tempCustomerDetails.put("Address1",
						getElement(LocatorTypes.name, CallidusObjRepo.CallidusCustomerPage.custStreetAddress_name)
								.getAttribute("value"));
				tempCustomerDetails.put("City",
						getElement(LocatorTypes.name, CallidusObjRepo.CallidusCustomerPage.custCity_name)
								.getAttribute("value"));
				tempCustomerDetails.put("StateCode",
						getElement(LocatorTypes.name, CallidusObjRepo.CallidusCustomerPage.custState_name)
								.getAttribute("value"));
				tempCustomerDetails.put("ZipCode",
						getElement(LocatorTypes.name, CallidusObjRepo.CallidusCustomerPage.custZipCode_name)
								.getAttribute("value"));
				tempCustomerDetails.put("CountyID",
						getElement(LocatorTypes.name, CallidusObjRepo.CallidusCustomerPage.custCountyCode_name)
								.getAttribute("value"));
				tempCustomerDetails.put("HomePhone",
						getElement(LocatorTypes.name, CallidusObjRepo.CallidusCustomerPage.custPhoneNum_name)
								.getAttribute("value"));
			} else {
				test.log(Status.ERROR,
						"Unable to retrieve Customer Demographics on customer " + testData.get("SupplementalID"));
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Exception while retrieving Customer Demographics on customer "
						+ testData.get("SupplementalID"));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return tempCustomerDetails;
	}

	public boolean validateCustomerDemographics() {
		HashMap<String, String> tempCustomerDetails = returnCustomerDemographics();
		boolean flag = false;
		try {
			String testDataBirthDate = getDateInMMDDYYYY(testData.get("birthdate"));
			String testDataEffectiveMonth = getDateInMMDDYYYY(testData.get("effectivemonth"));
			String testDataApplicationDate = getDateInMMDDYYYY(testData.get("ApplicationDate"));
			String testDataAppSignatureDate = getDateInMMDDYYYY(testData.get("ApplicationSignatureDate"));

			testData.put("birthdate", testDataBirthDate);
			testData.put("effectivemonth", testDataEffectiveMonth);
			testData.put("ApplicationDate", testDataApplicationDate);
			testData.put("ApplicationSignatureDate", testDataAppSignatureDate);

			flag = compareHashMaps(testData, tempCustomerDetails);
			if (flag) {
				reportPass("Callidus Member Verified " + tempCustomerDetails);
			} else {
				reportFail("Callidus Member Verification failed screen: " + tempCustomerDetails + "\r\n db details: "
						+ testData);
			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			try {
				reportFail("validateCustomerDemographics method failed. TestData: " + testData);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}

	public boolean validateCustomerDemographics_accessDB() {
		HashMap<String, String> tempCustomerDetails = returnCustomerDemographics();
		boolean flag = false;
		try {
			String testDataBirthDate = getDateInMMDDYYYY(testData.get("birthdate"));
			String testDataEffectiveMonth = getDateInMMDDYYYY(testData.get("effectivemonth"));
			String testDataApplicationDate = getDateInMMDDYYYY(testData.get("ApplicationDate"));
			String testDataAppSignatureDate = getDateInMMDDYYYY(testData.get("ApplicationSignatureDate"));

			testData.put("birthdate", testDataBirthDate);
			testData.put("effectivemonth", testDataEffectiveMonth);
			testData.put("ApplicationDate", testDataApplicationDate);
			testData.put("ApplicationSignatureDate", testDataAppSignatureDate);

			tempCustomerDetails.remove("MEDICAREID");
			flag = compareHashMaps(testData, tempCustomerDetails);
			if (flag) {
				reportPass("Callidus Member Verified " + tempCustomerDetails);
			} else {
				reportFail("Callidus Member Verification failed screen: " + tempCustomerDetails + "\r\n db details: "
						+ testData);
			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			try {
				reportFail("validateCustomerDemographics_accessDB method failed. TestData: " + testData);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}
}
