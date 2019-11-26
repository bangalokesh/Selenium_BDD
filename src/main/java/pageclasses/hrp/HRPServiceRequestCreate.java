package pageclasses.hrp;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import enums.LocatorTypes;
import io.appium.java_client.windows.WindowsDriver;
import managers.PropertyFileReader;
import pageclasses.BasePage;
import pageobjects.HRPObjRepo;

import utils.Dbconn;

public class HRPServiceRequestCreate extends BasePage {

	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(HRPServiceRequestCreate.class.getName());
	Dbconn db = new Dbconn();

	public HRPServiceRequestCreate(WindowsDriver<WebElement> winDriver) {

	}

	public void navigateToServiceRequest() {
		try {
			WebDriverWait wait = new WebDriverWait(winDriver, 10);
			wait.until(ExpectedConditions.elementToBeClickable(By.name("Others")));
			getWinElement(LocatorTypes.name, HRPObjRepo.HRPDockLeft.others_name).click();
			List<WebElement> e = getWinElements(LocatorTypes.id,
					HRPObjRepo.HRPServiceRequestCreateDetails.createServiceRequest_id);
			e.get(7).click();
			Set<String> winHandles = winDriver.getWindowHandles();
			for (String winHandle : winHandles) {
				winDriver.switchTo().window(winHandle);
			}

			wait(2);
			logger.info("service request window open");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("failure to open service request page");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void serviceRequestMember() {
		try {
			String userid = PropertyFileReader.getHrpUserId().replace("\"", "");
			List<String> methods = requestAndResourceMethods();
			int randomNum = getRandomNumber(0, 14);
			Integer randomMemId = getRandomNumber(0, 20);
			Set<String> winHandles = winDriver.getWindowHandles();
			String parentWindow = winDriver.getWindowHandle();
			String childWindow = null;
			for (String winHandle : winHandles) {
				if (!winHandle.equals(parentWindow)) {
					childWindow = winHandle;
					winDriver.switchTo().window(winHandle);
				}
			}
			winDriver.manage().window().maximize();

			List<WebElement> e = getWinElements(LocatorTypes.name,
					HRPObjRepo.HRPServiceRequestCreateDetails.rerquestMedium_name);
			e.get(1).sendKeys(methods.get(randomNum).trim() + Keys.ENTER);
			e.get(2).sendKeys(methods.get(randomNum).trim() + Keys.ENTER);
			e.get(0).sendKeys("Member" + Keys.ENTER);
			getWinElement(LocatorTypes.id, HRPObjRepo.HRPServiceRequestCreateDetails.memberSearch_id).click();
			getWinElement(LocatorTypes.id, HRPObjRepo.HRPServiceRequestCreateDetails.practitionerSearch_id)
					.sendKeys("L" + Keys.ENTER);
			getWinElement(LocatorTypes.name, HRPObjRepo.HRPServiceRequestCreateDetails.lookupLastName_name)
					.sendKeys("%%" + Keys.ENTER + Keys.ENTER);
			getWinElement(LocatorTypes.name,
					HRPObjRepo.HRPServiceRequestCreateDetails.memberid_name + randomMemId.toString()).click();
			Actions action = new Actions(winDriver);
			action.doubleClick(getWinElement(LocatorTypes.name,
					HRPObjRepo.HRPServiceRequestCreateDetails.memberid_name + randomMemId.toString())).build()
					.perform();
			List<WebElement> l = getWinElements(LocatorTypes.name, HRPObjRepo.HRPCreateIssue.scrollDown_name);
			System.out.println("scroll bar " + l.size());
			if (l.size() > 0) {
				action.clickAndHold(getWinElement(LocatorTypes.name, HRPObjRepo.HRPCreateIssue.scrollDown_name)).build()
						.perform();
				wait(1);
				action.release();
			}
			action.moveToElement(getWinElement(LocatorTypes.name, HRPObjRepo.HRPCreateIssue.addIssue_name)).click()
					.build().perform();
			List<WebElement> e1 = getWinElements(LocatorTypes.name, HRPObjRepo.HRPCreateIssue.issueName_name);
			e1.get(7).sendKeys("Members" + Keys.ENTER + Keys.TAB + "General Inquiry" + Keys.ENTER);
			e1.get(6).sendKeys("Low" + Keys.ENTER);
			e1.get(10).sendKeys("hcc_super_user" + Keys.TAB + userid + Keys.ENTER);
			getWinElement(LocatorTypes.name, HRPObjRepo.HRPCreateIssue.summary_name).sendKeys(getRandomString());
			action.clickAndHold(getWinElement(LocatorTypes.name, HRPObjRepo.HRPCreateIssue.scrollDown_name)).build()
					.perform();
			action.release();
			e1.get(11).sendKeys("Previous Member ID" + Keys.TAB + getRandomLong(10));
			action.moveToElement(getWinElement(LocatorTypes.name, HRPObjRepo.HRPCreateIssue.save_name)).click().build()
					.perform();
			winDriver.switchTo().window(parentWindow);
			wait(3);
			updateRequest();
			String requestId = getWinElement(LocatorTypes.id, HRPObjRepo.HRPCreateIssue.request_id).getText().trim();
			reportPass("Service request created for Member with request Id :" + requestId);
			logger.info("created service request for Member");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("failure to create a service request for Member");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void serviceRequestPractitioner() {
		try {
			String userid = PropertyFileReader.getHrpUserId().replace("\"", "");
			List<String> methods = requestAndResourceMethods();
			int randomNum = getRandomNumber(0, 14);
			Integer randomPractitionerId = getRandomNumber(0, 20);
			Set<String> winHandles = winDriver.getWindowHandles();
			String parentWindow = winDriver.getWindowHandle();
			String childWindow = null;
			for (String winHandle : winHandles) {
				if (!winHandle.equals(parentWindow)) {
					childWindow = winHandle;
					winDriver.switchTo().window(winHandle);
				}
			}
			winDriver.manage().window().maximize();

			List<WebElement> e = getWinElements(LocatorTypes.name,
					HRPObjRepo.HRPServiceRequestCreateDetails.rerquestMedium_name);
			e.get(1).sendKeys(methods.get(randomNum).trim() + Keys.ENTER);
			e.get(2).sendKeys(methods.get(randomNum).trim() + Keys.ENTER);
			e.get(0).sendKeys("Practitioner" + Keys.ENTER);
			getWinElement(LocatorTypes.id, HRPObjRepo.HRPServiceRequestCreateDetails.practitionerSearch_id).click();
			getWinElement(LocatorTypes.id, HRPObjRepo.HRPServiceRequestCreateDetails.practitionerSearch_id)
					.sendKeys("L" + Keys.ENTER);
			getWinElement(LocatorTypes.name, HRPObjRepo.HRPServiceRequestCreateDetails.lookupLastName_name)
					.sendKeys("%%" + Keys.ENTER + Keys.ENTER);
			getWinElement(LocatorTypes.name,
					HRPObjRepo.HRPServiceRequestCreateDetails.practitionerid_name + randomPractitionerId.toString())
							.click();
			Actions action = new Actions(winDriver);
			action.doubleClick(getWinElement(LocatorTypes.name,
					HRPObjRepo.HRPServiceRequestCreateDetails.practitionerid_name + randomPractitionerId)).build()
					.perform();
			List<WebElement> l = getWinElements(LocatorTypes.name, HRPObjRepo.HRPCreateIssue.scrollDown_name);
			if (l.size() > 0) {
				action.clickAndHold(getWinElement(LocatorTypes.name, HRPObjRepo.HRPCreateIssue.scrollDown_name)).build()
						.perform();
				wait(1);
				action.release();
			}
			// Issue creation starts here
			action.moveToElement(getWinElement(LocatorTypes.name, HRPObjRepo.HRPCreateIssue.addIssue_name)).click()
					.build().perform();
			List<WebElement> e1 = getWinElements(LocatorTypes.name, HRPObjRepo.HRPCreateIssue.issueName_name);
			e1.get(7).sendKeys("Members" + Keys.ENTER + Keys.TAB + "General Inquiry" + Keys.ENTER);
			e1.get(6).sendKeys("Low" + Keys.ENTER);
			e1.get(10).sendKeys("hcc_super_user" + Keys.TAB + userid + Keys.ENTER);// environment variable
			getWinElement(LocatorTypes.name, HRPObjRepo.HRPCreateIssue.summary_name).sendKeys(getRandomString());
			action.clickAndHold(getWinElement(LocatorTypes.name, HRPObjRepo.HRPCreateIssue.scrollDown_name)).build()
					.perform();
			action.release();
			e1.get(11).sendKeys("Previous Member ID" + Keys.TAB + getRandomLong(10));
			action.moveToElement(getWinElement(LocatorTypes.name, HRPObjRepo.HRPCreateIssue.save_name)).click().build()
					.perform();

			winDriver.switchTo().window(parentWindow);
			wait(3);
			updateRequest();
			String requestId = getWinElement(LocatorTypes.id, HRPObjRepo.HRPCreateIssue.request_id).getText().trim();
			reportPass("Service request created for Practitioner with request Id :" + requestId);
			logger.info("created service request for Practitioner");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("failure to create a service request for Practitioner");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void serviceRequestSupplier() {
		try {
			String userid = PropertyFileReader.getHrpUserId().replace("\"", "");
			List<String> methods = requestAndResourceMethods();
			int randomNum = getRandomNumber(0, 14);
			Integer randomSupplierId = getRandomNumber(0, 20);
			Set<String> winHandles = winDriver.getWindowHandles();
			String parentWindow = winDriver.getWindowHandle();
			String childWindow = null;
			for (String winHandle : winHandles) {
				if (!winHandle.equals(parentWindow)) {
					childWindow = winHandle;
					winDriver.switchTo().window(winHandle);
				}
			}
			winDriver.manage().window().maximize();

			List<WebElement> e = getWinElements(LocatorTypes.name,
					HRPObjRepo.HRPServiceRequestCreateDetails.rerquestMedium_name);
			e.get(1).sendKeys(methods.get(randomNum).trim() + Keys.ENTER);
			e.get(2).sendKeys(methods.get(randomNum).trim() + Keys.ENTER);
			e.get(0).sendKeys("Supplier" + Keys.ENTER);
			getWinElement(LocatorTypes.id, HRPObjRepo.HRPServiceRequestCreateDetails.practitionerSearch_id).click();
			getWinElement(LocatorTypes.id, HRPObjRepo.HRPServiceRequestCreateDetails.practitionerSearch_id)
					.sendKeys("L" + Keys.ENTER);
			getWinElement(LocatorTypes.name, HRPObjRepo.HRPServiceRequestCreateDetails.supplierName_name)
					.sendKeys("%%" + Keys.ENTER + Keys.ENTER);
			getWinElement(LocatorTypes.name,
					HRPObjRepo.HRPServiceRequestCreateDetails.supplierId_name + randomSupplierId.toString()).click();
			Actions action = new Actions(winDriver);
			action.doubleClick(getWinElement(LocatorTypes.name,
					HRPObjRepo.HRPServiceRequestCreateDetails.supplierId_name + randomSupplierId.toString())).build()
					.perform();
			List<WebElement> l = getWinElements(LocatorTypes.name, HRPObjRepo.HRPCreateIssue.scrollDown_name);
			if (l.size() > 0) {
				action.clickAndHold(getWinElement(LocatorTypes.name, HRPObjRepo.HRPCreateIssue.scrollDown_name)).build()
						.perform();
				wait(1);
				action.release();
			}
			action.moveToElement(getWinElement(LocatorTypes.name, HRPObjRepo.HRPCreateIssue.addIssue_name)).click()
					.build().perform();
			List<WebElement> e1 = getWinElements(LocatorTypes.name, HRPObjRepo.HRPCreateIssue.issueName_name);
			e1.get(7).sendKeys("Member Authorized" + Keys.ENTER + Keys.TAB + "Care Management" + Keys.ENTER + Keys.TAB
					+ "Drug Management" + Keys.TAB);
			e1.get(6).sendKeys("Low" + Keys.ENTER);
			e1.get(10).sendKeys("hcc_super_user" + Keys.TAB + userid + Keys.ENTER);
			getWinElement(LocatorTypes.name, HRPObjRepo.HRPCreateIssue.summary_name).sendKeys(getRandomString());
			action.clickAndHold(getWinElement(LocatorTypes.name, HRPObjRepo.HRPCreateIssue.scrollDown_name)).build()
					.perform();
			action.release();
			e1.get(11).sendKeys("Previous Member ID" + Keys.TAB + getRandomLong(10));
			action.moveToElement(getWinElement(LocatorTypes.name, HRPObjRepo.HRPCreateIssue.save_name)).click().build()
					.perform();
			winDriver.switchTo().window(parentWindow);
			wait(3);
			updateRequest();
			String requestId = getWinElement(LocatorTypes.id, HRPObjRepo.HRPCreateIssue.request_id).getText().trim();
			reportPass("Service request created for Supplier with request Id :" + requestId);
			logger.info("created service request for Supplier");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("failure to create a service request for Supplier");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void updateRequest() {
		HashMap<String, String> testDataMap = new HashMap<String, String>();
		testDataMap.put("issueId",
				getWinElement(LocatorTypes.id, HRPObjRepo.HRPCreateIssue.issueID_id).getText().trim());
		testDataMap.put("requestId",
				getWinElement(LocatorTypes.id, HRPObjRepo.HRPCreateIssue.request_id).getText().trim());

		String insertQuery = "INSERT INTO VelocityTestAutomation.dbo.test_data_readytoservice\r\n"
				+ "([TCID],[DriverType],[AppName],[Environment],[IssueID],[RequestID])\r\n" + "VALUES (" + 19
				+ ", 'CHROME', 'HRP', 'UAT', '" + testDataMap.get("issueId") + "', '" + testDataMap.get("requestId")
				+ "');";
		db.sqlUpdate(insertQuery);
	}

	public List<String> requestAndResourceMethods() {
		ArrayList<String> methods = new ArrayList<String>();
		methods.add("External Transfer");
		methods.add("Inbound Email");
		methods.add("Inbound Fax");
		methods.add("Inbound Letter");
		methods.add("Inbound Phone");
		methods.add("Inbound Portal");
		methods.add("Inbound Translator Service Phone");
		methods.add("Internal Transfer");
		methods.add("Outbound Email");
		methods.add("Outbound Fax");
		methods.add("Outbound Letter");
		methods.add("Outbound Phone");
		methods.add("Outbound Portal");
		methods.add("Outbound Translator Service Phone");
		methods.add("Walk-in");
		return methods;
	}
}
