package pageclasses;

import java.awt.AWTException;
import java.awt.Robot;
import java.awt.event.KeyEvent;
import java.io.BufferedReader;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.security.SecureRandom;
import java.security.spec.KeySpec;
import java.sql.SQLException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.PBEKeySpec;
import javax.crypto.spec.SecretKeySpec;

import org.apache.http.HttpResponse;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.client.methods.HttpPatch;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.ContentType;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.HttpClientBuilder;
import org.apache.http.util.EntityUtils;
import org.apache.pdfbox.pdmodel.PDDocument;
import org.apache.pdfbox.text.PDFTextStripper;
import org.apache.pdfbox.text.PDFTextStripperByArea;
import org.json.JSONArray;
import org.json.JSONObject;
import org.junit.Assert;
import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoAlertPresentException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.ExtentReports;
import com.aventstack.extentreports.ExtentTest;
import com.aventstack.extentreports.Status;
import com.aventstack.extentreports.reporter.ExtentHtmlReporter;
import com.aventstack.extentreports.reporter.configuration.ResourceCDN;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.google.common.io.Files;

import enums.DriverType;
import enums.LocatorTypes;
import enums.LoggingStatus;
import io.appium.java_client.windows.WindowsDriver;
import managers.DriverManager;
import managers.PropertyFileReader;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import utils.Const;
import utils.Dbconn;

public class BasePage extends CommonMethods {
	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(BasePage.class.getName());

	private static int tcid = 0;
	private static int testDataid = 0;
	private static boolean continueExecution = true;
	private static String runName = null;
	private static int runId = 0;
	private static String reportPath = null;
	public static HashMap<String, String> testData = new HashMap<String, String>();
	private static HashMap<String, Integer> testResultIDs = new HashMap<String, Integer>();
	public static ExtentReports extent = getInstance();
	public static ExtentTest test;
	public static ExtentHtmlReporter htmlReporter;
	public static WebDriver driver;
	public static WindowsDriver<WebElement> winDriver;
	public static String driverType;
	public static JavascriptExecutor js = (JavascriptExecutor) driver;

	public static String getWebDriverType() {
		return driverType;
	}

	public static void setWebDriverType(String driverType) {
		BasePage.driverType = driverType;
	}

	public void setTcid(int id) {
		BasePage.tcid = id;
	}

	public static int getTcid() {
		return tcid;
	}

	public static boolean isContinueExecution() {
		return continueExecution;
	}

	public void setContinueExecution(boolean continueExecution) {
		BasePage.continueExecution = continueExecution;
	}

	public static String getRunName() {
		return runName;
	}

	public static void setRunName() {
		try {
			String fileName = getCurrentDateTime();
			runName = "Test_Execution_" + fileName + "_Run_No_" + getRunId();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("invokeDriver: " + e.getMessage());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public static int getRunId() {
		return runId;
	}

	public static void setRunId(String testPlanId, String testSuiteId) {
		runId = createRun(testPlanId, testSuiteId);
	}

	public static void setTest(String scenarioName, String testName) {
		test = extent.createTest(tcid + "_" + scenarioName + "_" + testName);
	}

	public ExtentTest getTest() {
		return test;
	}

	public static String getReportPath() {
		return reportPath;
	}

	public static void setReportPath() {
		new File(Const.REPORT_PATH + "//" + runName + "//screenshots").mkdirs();
		reportPath = Const.REPORT_PATH + "//" + runName + "//report.html";
	}

	public static HashMap<String, String> getTestData() {
		return testData;
	}

	public static void setTestData(HashMap<String, String> testData) {
		try {
			Dbconn db = new Dbconn();
			BasePage.testData = db.selectTestDataFromTable(getTcid());
			setTestDataid(Integer.parseInt(testData.get("ID")));
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("setTestData: " + e.getMessage());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

	}

	public static void setrowTestData(HashMap<String, String> testData) {
		try {
			BasePage.testData = null;
			BasePage.testData = testData;
			setTestDataid(Integer.parseInt(testData.get("ID")));
			getTestData();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("setTestData: " + e.getMessage());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public static void setrowTestData(HashMap<String, String> testData, int id) {
		try {
			BasePage.testData = null;
			BasePage.testData = testData;
			setTestDataid(id);
			getTestData();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("setTestData: " + e.getMessage());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	// Checking if driverType == null
	public static WebDriver getWebDriver() {
		try {
			if (driver == null) {
				if (driverType == null || driverType.isEmpty() || driverType.contains("WINAPP")) {
					driver = DriverManager.createLocalWebDriver("CHROME");
				} else {
					driver = DriverManager.createLocalWebDriver(driverType.toUpperCase().trim());
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("getWebDriver: " + e.getMessage());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return driver;
	}

	public static WindowsDriver<WebElement> getWinDriver(String appName) {
		try {
			if (winDriver == null) {
				winDriver = DriverManager.createLocalWindowsDriver(DriverType.WINAPP, appName);
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("getWinDriver: " + e.getMessage());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return winDriver;
	}

	public static void invokeDriver() {
		try {
			driverType = testData.get("DriverType");
			if (driverType.equalsIgnoreCase("WINAPP")) {
				String appName = Const.HRPPATH;
				getWinDriver(appName);
			} else {
				getWebDriver();
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("invokeDriver: " + e.getMessage());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void moveFiles(String srcPath, String targetPath) {
		try {
			java.nio.file.Files.move(new java.io.File(srcPath).toPath(), new java.io.File(targetPath).toPath(),
					java.nio.file.StandardCopyOption.ATOMIC_MOVE, java.nio.file.StandardCopyOption.REPLACE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
			try {
				reportFail("moveFiles: " + e.getMessage());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public static ExtentReports getInstance() {
		try {
			if (extent == null) {
				setRunName();
				if (Const.TESTPLANID != null && Const.TESTSUITEID != null && Const.RUNID == 0) {
					setRunId(Const.TESTPLANID, Const.TESTSUITEID);
					getTestResults(runId);
				} else
					runId = Const.RUNID;

				setReportPath();
				htmlReporter = new ExtentHtmlReporter(getReportPath());
				htmlReporter.config().setResourceCDN(ResourceCDN.EXTENTREPORTS);
				htmlReporter.loadXMLConfig(Const.EXTENTCONFIG);
				extent = new ExtentReports();
				extent.attachReporter(htmlReporter);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return extent;
	}

	public void flushTest() {
		try {
			if (extent != null) {
				extent.flush();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void endOfTestRun() {
		try {
			if (extent != null) {
				extent.flush();
				htmlReporter.loadConfig(Const.EXTENTCONFIG);
			}
			if (Const.TESTPLANID != null && Const.TESTSUITEID != null) {
				File f = new File(Const.REPORT_PATH);
				File destZipFile = getLatestFilefromDir(f); // String folder = Const.REPORT_PATH +
				zipDir(Const.REPORT_PATH, destZipFile.getAbsolutePath(), destZipFile.getAbsolutePath() + ".zip");
				destZipFile = getLatestFilefromDir(f);
				addAttachmentToRun(runId, destZipFile.getAbsolutePath());
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// *********************************Navigating
	// Methods***********************************************

	public void navigate(String url) {
		try {
			driver.navigate().to(url);
			logger.info("Test = " + test + " Navigating to " + url);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("navigate: " + e.getMessage());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void goToUrl(String url) {
		try {
			driver.get(url);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("goToUrl: " + e.getMessage());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		logger.info("Test = " + test + " Navigating to " + url);
	}

	public void navigateBack() {
		try {
			driver.navigate().back();
			logger.info("Navigating Back");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("navigateBack: " + e.getMessage());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void navigateForward() {
		try {
			driver.navigate().forward();
			logger.info("Navigating Forward");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("navigateForward: " + e.getMessage());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void pageRefresh() {
		try {
			driver.navigate().refresh();
			logger.info("Refresh the page");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("pageRefresh: " + e.getMessage());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	// *****************************************Get Current Page
	// URL***********************************************************
	public String getCurrentUrl() {
		String url = null;
		try {
			url = driver.getCurrentUrl();
			logger.info("Current url = " + url);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("getCurrentUrl: " + e.getMessage());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return url;
	}

	// ***************************************UTILITY
	// FUNCTIONS**********************************************
	// *********************************Get Element
	// Method***************************************************
	// *********************************Get Element
	// Method***************************************************
	public WebElement getElement(LocatorTypes locatorType, String locatorkey) {
		WebElement e = null;
		try {
			if (locatorType.toString().endsWith("id")) {
				e = driver.findElement(By.id(locatorkey));
			} else if (locatorType.toString().endsWith("name")) {
				e = driver.findElement(By.name(locatorkey));
			} else if (locatorType.toString().endsWith("xpath")) {
				e = driver.findElement(By.xpath(locatorkey));
			} else if (locatorType.toString().endsWith("cssSelector")) {
				e = driver.findElement(By.cssSelector(locatorkey));
			} else if (locatorType.toString().endsWith("linkText")) {
				e = driver.findElement(By.linkText(locatorkey));
			} else if (locatorType.toString().endsWith("partialLinkText")) {
				e = driver.findElement(By.partialLinkText(locatorkey));
			} else if (locatorType.toString().endsWith("tagName")) {
				e = driver.findElement(By.tagName(locatorkey));
			} else if (locatorType.toString().endsWith("className")) {
				e = driver.findElement(By.className(locatorkey));
			}
			logger.info("Test = " + test + " Getting Element: " + locatorkey);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return e;
	}

	public WebElement getWinElement(LocatorTypes locatorType, String locatorkey) {
		WebElement e = null;
		try {
			if (locatorType.toString().endsWith("id")) {
				e = winDriver.findElementByAccessibilityId(locatorkey);
			} else if (locatorType.toString().endsWith("name")) {
				e = winDriver.findElement(By.name(locatorkey));
			} else if (locatorType.toString().endsWith("xpath")) {
				e = winDriver.findElement(By.xpath(locatorkey));
			} else if (locatorType.toString().endsWith("cssSelector")) {
				e = winDriver.findElement(By.cssSelector(locatorkey));
			} else if (locatorType.toString().endsWith("linkText")) {
				e = winDriver.findElement(By.linkText(locatorkey));
			} else if (locatorType.toString().endsWith("partialLinkText")) {
				e = winDriver.findElement(By.partialLinkText(locatorkey));
			} else if (locatorType.toString().endsWith("tagName")) {
				e = winDriver.findElement(By.tagName(locatorkey));
			} else if (locatorType.toString().endsWith("className")) {
				e = winDriver.findElement(By.className(locatorkey));
			}
			logger.info("Test = " + test + " Getting Element: " + locatorkey);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return e;
	}

	//// **********************************Get Windows Elements
	//// Method*************************************************
	public List<WebElement> getWinElements(LocatorTypes locatorType, String locatorkey) {
		List<WebElement> e = null;
		try {
			if (locatorType.toString().endsWith("id")) {
				e = winDriver.findElementsByAccessibilityId(locatorkey);
			} else if (locatorType.toString().endsWith("name")) {
				e = winDriver.findElements(By.name(locatorkey));
			} else if (locatorType.toString().endsWith("xpath")) {
				e = winDriver.findElements(By.xpath(locatorkey));
			} else if (locatorType.toString().endsWith("cssSelector")) {
				e = winDriver.findElements(By.cssSelector(locatorkey));
			} else if (locatorType.toString().toString().endsWith("linkText")) {
				e = winDriver.findElements(By.linkText(locatorkey));
			} else if (locatorType.toString().endsWith("partialLinkText")) {
				e = winDriver.findElements(By.partialLinkText(locatorkey));
			} else if (locatorType.toString().endsWith("tagName")) {
				e = winDriver.findElements(By.tagName(locatorkey));
			} else if (locatorType.toString().endsWith("className")) {
				e = winDriver.findElements(By.className(locatorkey));
			}
			logger.info("Test = " + test + " Getting Elements: " + locatorkey);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return e;
	}

	// **********************************Get Elements
	// Method*************************************************
	public List<WebElement> getElements(LocatorTypes locatorType, String locatorkey) {
		List<WebElement> e = null;
		try {
			if (locatorType.toString().endsWith("id")) {
				e = driver.findElements(By.id(locatorkey));
			} else if (locatorType.toString().endsWith("name")) {
				e = driver.findElements(By.name(locatorkey));
			} else if (locatorType.toString().endsWith("xpath")) {
				e = driver.findElements(By.xpath(locatorkey));
			} else if (locatorType.toString().endsWith("cssSelector")) {
				e = driver.findElements(By.cssSelector(locatorkey));
			} else if (locatorType.toString().toString().endsWith("linkText")) {
				e = driver.findElements(By.linkText(locatorkey));
			} else if (locatorType.toString().endsWith("partialLinkText")) {
				e = driver.findElements(By.partialLinkText(locatorkey));
			} else if (locatorType.toString().endsWith("tagName")) {
				e = driver.findElements(By.tagName(locatorkey));
			} else if (locatorType.toString().endsWith("className")) {
				e = driver.findElements(By.className(locatorkey));
			}
			logger.info("Test = " + test + " Getting Elements: " + locatorkey);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		return e;
	}

	// ************************************Is Element Present
	// Method*****************************************
	public boolean isElementPresent(LocatorTypes locatorType, String locatorkey) {
		List<WebElement> e = null;
		try {
			if (locatorType.toString().endsWith("id")) {
				e = driver.findElements(By.id(locatorkey));
			} else if (locatorType.toString().endsWith("name")) {
				e = driver.findElements(By.name(locatorkey));
			} else if (locatorType.toString().endsWith("xpath")) {
				e = driver.findElements(By.xpath(locatorkey));
			} else if (locatorType.toString().endsWith("cssSelector")) {
				e = driver.findElements(By.cssSelector(locatorkey));
			} else if (locatorType.toString().endsWith("linkText")) {
				e = driver.findElements(By.linkText(locatorkey));
			} else if (locatorType.toString().endsWith("partialLinkText")) {
				e = driver.findElements(By.partialLinkText(locatorkey));
			} else if (locatorType.toString().endsWith("tagName")) {
				e = driver.findElements(By.tagName(locatorkey));
			} else if (locatorType.toString().endsWith("className")) {
				e = driver.findElements(By.className(locatorkey));
			}
			logger.info("Test = " + test + " Verifying if element is present: " + locatorkey);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		if (e.size() == 0)
			return false;
		else
			return true;
	}

	// ************************************Is Windows Element Present
	// Method*****************************************
	public boolean isWinElementPresent(LocatorTypes locatorType, String locatorkey) {
		List<WebElement> e = null;
		try {
			if (locatorType.toString().endsWith("id")) {
				e = winDriver.findElementsByAccessibilityId(locatorkey);
			} else if (locatorType.toString().endsWith("name")) {
				e = winDriver.findElements(By.name(locatorkey));
			} else if (locatorType.toString().endsWith("xpath")) {
				e = winDriver.findElements(By.xpath(locatorkey));
			} else if (locatorType.toString().endsWith("cssSelector")) {
				e = winDriver.findElements(By.cssSelector(locatorkey));
			} else if (locatorType.toString().endsWith("linkText")) {
				e = winDriver.findElements(By.linkText(locatorkey));
			} else if (locatorType.toString().endsWith("partialLinkText")) {
				e = winDriver.findElements(By.partialLinkText(locatorkey));
			} else if (locatorType.toString().endsWith("tagName")) {
				e = winDriver.findElements(By.tagName(locatorkey));
			} else if (locatorType.toString().endsWith("className")) {
				e = winDriver.findElements(By.className(locatorkey));
			}
			logger.info("Test = " + test + " Verifying if element is present: " + locatorkey);
		} catch (Exception exception) {
			exception.printStackTrace();
		}
		if (e.size() == 0)
			return false;
		else
			return true;
	}

	// **********************************Get Element Size
	// Method*********************************************
	public int getElementsSize(LocatorTypes locatorType, String locatorkey) {
		int s = 0;
		List<WebElement> e = getElements(locatorType, locatorkey);
		try {
			s = e.size();
			logger.info("Getting element size " + locatorkey + " = " + s);
		} catch (Exception exception) {
			exception.printStackTrace();
			try {
				reportFail("getElementsSize: " + exception.getMessage());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
			logger.info("Not able to find size of Element " + locatorkey);
		}
		return s;
	}

	// **************************************** Send Keys
	// ****************************************************
	public void sendKey(LocatorTypes locatorType, String locatorkey, Keys selectkey) throws IOException, SQLException {
		try {
			logger.info("Sending Key - " + locatorkey);
			WebElement e = getElement(locatorType, locatorkey);
			e.sendKeys(selectkey);
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("sendKey: " + e.getMessage());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	// ****************************** Explicit Wait Element Visible
	// ******************************************
	public void elementVisibleWait(LocatorTypes locatorType, String locatorKey, int wait)
			throws IOException, SQLException {
		WebDriverWait d = new WebDriverWait(driver, wait);
		try {
			if (locatorType.toString().endsWith("xpath")) {
				d.until(ExpectedConditions.visibilityOfElementLocated(By.xpath(locatorKey)));
			} else if (locatorType.toString().endsWith("id")) {
				d.until(ExpectedConditions.visibilityOfElementLocated(By.id(locatorKey)));
			} else if (locatorType.toString().endsWith("className")) {
				d.until(ExpectedConditions.visibilityOfElementLocated(By.className(locatorKey)));
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}

	public void elementClickableWait(LocatorTypes locatorType, String locatorKey, int wait)
			throws IOException, SQLException {
		WebDriverWait d = new WebDriverWait(driver, wait);
		try {
			if (locatorType.toString().endsWith("xpath")) {
				d.until(ExpectedConditions.elementToBeClickable(By.xpath(locatorKey)));
			} else if (locatorType.toString().endsWith("id")) {
				d.until(ExpectedConditions.elementToBeClickable(By.id(locatorKey)));
			} else if (locatorType.toString().endsWith("className")) {
				d.until(ExpectedConditions.elementToBeClickable(By.className(locatorKey)));
			}
		} catch (Exception e1) {
			e1.printStackTrace();
		}

	}

	public boolean verifyIfAlertisPresent(int wait) {
		WebDriverWait wdwait = new WebDriverWait(driver, 0);
		if (wdwait.until(ExpectedConditions.alertIsPresent()) == null)
			return false;
		else
			return true;
	}

	public void dismissAlert() {
		driver.switchTo().alert().dismiss();
	}

	public void acceptAlert() {
		driver.switchTo().alert().accept();
	}

	// **************************************** Close Browser
	// ************************************************
	public void closeDrivers() {
		try {
			closeBrowser();
			closeWindowsDriver();
		} catch (Exception e) {
			try {
				reportFail("sendKey: " + e.getMessage());
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	// ***************************************** Logging
	// *****************************************************
	public static void logInfo(LoggingStatus logStatus, String stepKeyword, String details) {
		try {
			if (logStatus.equals(LoggingStatus.INFO)) {
				test.info(stepKeyword + ": - " + details);
				logger.info(LoggingStatus.INFO + details);
			} else if (logStatus.equals(LoggingStatus.FAIL)) {
				test.fail(stepKeyword + ": - " + details);
				logger.info(LoggingStatus.FAIL + details);
			} else if (logStatus.equals(LoggingStatus.PASS)) {
				test.pass(stepKeyword + ": - " + details);
				logger.info(LoggingStatus.PASS + details);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ***************************************** Logging
	// *****************************************************
	public void logInfo(String logStatus, String stepKeyword, String details) {
		try {
			if (logStatus.equalsIgnoreCase("INFO")) {
				test.info(stepKeyword + ": - " + details);
				logger.info(LoggingStatus.INFO + details);
			} else if (logStatus.equalsIgnoreCase("FAIL")) {
				test.fail(stepKeyword + ": - " + details);
				logger.info(LoggingStatus.FAIL + details);
			} else if (logStatus.equalsIgnoreCase("PASS")) {
				test.pass(stepKeyword + ": - " + details);
				logger.info(LoggingStatus.PASS + details);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// **********************************Reporting Pass
	// Method***********************************************
	public void reportPass(String stepKeyword) throws IOException {
		try {
			if (driver != null) {
				String path = ExtentReportScreenshot();
				logInfo(LoggingStatus.PASS, stepKeyword, "test passed");
				test.addScreenCaptureFromPath("./Screenshots/" + path);
			} else if (winDriver != null) {
				String path = ExtentReportScreenshot();
				logInfo(LoggingStatus.PASS, stepKeyword, "test passed");
				test.addScreenCaptureFromPath("./Screenshots/" + path);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// **********************************Reporting Fail
	// Method***********************************************
	public static void reportFail(String stepKeyword) throws IOException {
		try {
			if (driver != null) {
				String path = ExtentReportScreenshot();
				logInfo(LoggingStatus.FAIL, stepKeyword, "test failed");
				test.addScreenCaptureFromPath("./Screenshots/" + path);
			} else if (winDriver != null) {
				String path = ExtentReportScreenshot();
				logInfo(LoggingStatus.FAIL, stepKeyword, "test failed");
				test.addScreenCaptureFromPath("./Screenshots/" + path);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String ExtentReportScreenshot() {
		String path = null;
		try {
			Date d = new Date();
			String screenshotFile = d.toString().replace(":", "_").replace(" ", "_") + ".png";
			path = Const.REPORT_PATH + runName + "//screenshots//" + tcid + screenshotFile;
			if (driver != null) {
				File file = ((TakesScreenshot) driver).getScreenshotAs(OutputType.FILE);
				Files.copy(file, new File(path));
			} else if (winDriver != null) {
				File file = ((TakesScreenshot) winDriver).getScreenshotAs(OutputType.FILE);
				Files.copy(file, new File(path));
			}
			path = tcid + screenshotFile;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return path;
	}

	// ***********************************Close
	// Drivers*******************************************************
	public static void closeBrowser() {
		try {
			if (driver != null) {
				driver.quit();
				driver = null;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static void closeWindowsDriver() {
		try {
			if (winDriver != null) {
				winDriver.quit();
				winDriver = null;
				Runtime.getRuntime().exec("taskkill /F /IM HealthEdge.Manager.exe /T");
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void setRunModeToNo() {
		Dbconn db = new Dbconn();
		try {
			String updateQuery = "update VelocityTestAutomation.dbo.test_case set RunMode = 'N' where id = '"
					+ testData.get("ID") + "'";
			db.sqlUpdate(updateQuery);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public boolean returnMapEquals(HashMap<String, String> a, HashMap<String, String> b) {
		boolean flag = false;
		try {
			flag = (a.equals(b)) ? true : false;
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	public void wait(int seconds) {
		try {
			Thread.sleep(seconds * 1000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	}

	public static String decodeString(String password) {
		String dStr = null;
		try {
			Base64.Decoder decoder = Base64.getDecoder();
			dStr = new String(decoder.decode(password));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return dStr;
	}

	public static String decodeEncryptedString(String AppName) {
		String password = null;
		try {
			HashMap<String, String> tempEncrypt = new HashMap<String, String>();
			HashMap<String, String> temp = new HashMap<String, String>();
			Map<String, String> tempDecrypt = new HashMap<String, String>();
			Dbconn dbcon = new Dbconn();
			PropertyFileReader propFileReader = new PropertyFileReader();

			tempEncrypt.put("UserID", propFileReader.getUserIDPwd(AppName)[0].replace("\"", ""));
			tempDecrypt.put("passwordHash", propFileReader.getUserIDPwd(AppName)[1].replace("\"", ""));
			tempEncrypt.put("Environment", Const.ENV);
			tempEncrypt.put("AppName", AppName);
			temp = dbcon.returnSecretKeySalt(tempEncrypt);
			tempDecrypt.put("salt", temp.get("salt"));
			tempDecrypt.put("secretKey", temp.get("secretKey"));
			password = decrypt(tempDecrypt);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return password;
	}

	public static String decrypt(Map<String, String> encodedData) {
		String salt = encodedData.get("salt");
		String secretKey = encodedData.get("secretKey");
		String strToDecrypt = encodedData.get("passwordHash");
		try {
			byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
			IvParameterSpec ivspec = new IvParameterSpec(iv);
			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			KeySpec spec = new PBEKeySpec(secretKey.toCharArray(), salt.getBytes(), 65536, 128);
			SecretKey tmp = factory.generateSecret(spec);
			SecretKeySpec secretKeySpec = new SecretKeySpec(tmp.getEncoded(), "AES");
			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5PADDING");
			cipher.init(Cipher.DECRYPT_MODE, secretKeySpec, ivspec);
			return new String(cipher.doFinal(Base64.getDecoder().decode(strToDecrypt)));
		} catch (Exception e) {
			System.out.println("Error while decrypting: " + e.toString());
		}
		return null;
	}

	public static void encrypt(String strToEncrypt, String appName) {
		Dbconn dbconn = new Dbconn();
		PropertyFileReader propFileReader = new PropertyFileReader();
		Map<String, String> encryptedData = new HashMap<String, String>();

		SecureRandom secretKeyGenerator = new SecureRandom();
		byte[] secretKey = new byte[22];
		secretKeyGenerator.nextBytes(secretKey);

		byte[] salt = new byte[40];
		SecureRandom saltGenerator = new SecureRandom();
		saltGenerator.nextBytes(salt);

		encryptedData.put("secretKey", Base64.getEncoder().encodeToString(secretKey));
		encryptedData.put("salt", Base64.getEncoder().encodeToString(salt));

		try {
			byte[] iv = { 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0, 0 };
			IvParameterSpec ivspec = new IvParameterSpec(iv);

			SecretKeyFactory factory = SecretKeyFactory.getInstance("PBKDF2WithHmacSHA256");
			KeySpec spec = new PBEKeySpec(encryptedData.get("secretKey").toCharArray(),
					encryptedData.get("salt").getBytes(), 65536, 128);
			SecretKey tmp = factory.generateSecret(spec);
			SecretKeySpec secretKeySpec = new SecretKeySpec(tmp.getEncoded(), "AES");

			Cipher cipher = Cipher.getInstance("AES/CBC/PKCS5Padding");
			cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec, ivspec);

			encryptedData.put("passwordHash",
					Base64.getEncoder().encodeToString(cipher.doFinal(strToEncrypt.getBytes("UTF-8"))));

			encryptedData.put("Environment", Const.ENV);
			encryptedData.put("UserId", propFileReader.getUserIDPwd(appName)[0].replace("\"", ""));
			encryptedData.put("AppName", appName);

			dbconn.insertEncryptDataToDB(encryptedData);
			PropertyFileReader.setPwdHashInJson(encryptedData);

		} catch (Exception e) {
			System.out.println("Error while encrypting: " + e.toString());
		}
		// return null;
	}

	// *********AZURE DEVOPS INTEGRATION METHODS*********************************

	// ********Get Test Points from test plan and test suite in Azure DevOps*******
	public static ArrayList<Integer> getTestPoints(String testPlanID, String suiteID) {
		ArrayList<Integer> points = new ArrayList<Integer>();
		try {
			String uri = Const.AZUREAPIPATH + "/test/Plans/" + testPlanID + "/Suites/" + suiteID
					+ "/points/?api-version=5.0";
			HttpGet httpget = new HttpGet(uri);
			httpget.setHeader("Content-Type", "application/json");
			httpget.setHeader("Authorization",
					"Basic bGJhbmdhOnBmYzd2Y3l2aWNoZGJwdnM3dmpkZ243bHA3cDU1d2VpbDR2NDN2dnZjZGlhY2I1eHR2ZmE=");
			HttpClient client = HttpClientBuilder.create().build();
			HttpResponse response = client.execute(httpget);
			String json = EntityUtils.toString(response.getEntity());
			JSONObject jsonObj = new JSONObject(json);
			JSONArray ja_data = jsonObj.getJSONArray("value");
			int length = ja_data.length();
			for (int i = 0; i < length; i++) {
				JSONObject jObj = ja_data.getJSONObject(i);
				int id = jObj.getInt("id");
				points.add(id);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return points;
	}

	// ********Create New Test Run in Azure DevOps*******
	public static int createRun(String testPlanId, String testSuiteId) {
		int runId = 0;
		try {
			ArrayList<Integer> testPoints = getTestPoints(testPlanId, testSuiteId);
			String uri = Const.AZUREAPIPATH + "/test/runs?api-version=5.1-preview.3";
			HttpPost httppost = new HttpPost(uri);
			httppost.setHeader("Content-Type", "application/json");
			httppost.setHeader("Authorization",
					"Basic bGJhbmdhOnBmYzd2Y3l2aWNoZGJwdnM3dmpkZ243bHA3cDU1d2VpbDR2NDN2dnZjZGlhY2I1eHR2ZmE=");
			ObjectMapper mapper = new ObjectMapper();
			String jsonDataString = "{\"name\": \"Velocity Test Automation Run\",\"plan\": {\"id\": \"" + testPlanId
					+ "\"}}";
			JsonNode listNode = mapper.valueToTree(testPoints);
			JSONArray arr = new JSONArray(listNode.toString());
			JSONObject requestObject = new JSONObject(jsonDataString);
			requestObject.put("pointIds", arr);
			String requestBody = requestObject.toString();
			httppost.setEntity(new StringEntity(requestBody, ContentType.APPLICATION_JSON));
			HttpClient client = HttpClientBuilder.create().build();
			HttpResponse response = client.execute(httppost);
			String jsonResponse = EntityUtils.toString(response.getEntity());
			JSONObject jsonObj = new JSONObject(jsonResponse);
			runId = jsonObj.getInt("id");
			logger.info("New Run id created in Azure DevOps: " + runId);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return runId;
	}

	// ********Get Test Results from test run in Azure DevOps*******
	public static HashMap<String, Integer> getTestResults(int runId) {
		try {
			String uri = Const.AZUREAPIPATH + "/test/Runs/" + runId + "/results?api-version=5.1-preview.6";
			HttpGet httpget = new HttpGet(uri);
			httpget.setHeader("Content-Type", "application/json");
			httpget.setHeader("Authorization",
					"Basic bGJhbmdhOnBmYzd2Y3l2aWNoZGJwdnM3dmpkZ243bHA3cDU1d2VpbDR2NDN2dnZjZGlhY2I1eHR2ZmE=");
			HttpClient client = HttpClientBuilder.create().build();
			HttpResponse response = client.execute(httpget);
			String json = EntityUtils.toString(response.getEntity());
			JSONObject jsonObj = new JSONObject(json);
			JSONArray ja_data = jsonObj.getJSONArray("value");
			int length = ja_data.length();
			for (int i = 0; i < length; i++) {
				JSONObject jObj = ja_data.getJSONObject(i);
				int testResultId = jObj.getInt("id");
				JSONObject testCase = jObj.getJSONObject("testCase");
				String testCaseId = testCase.getString("id");
				testResultIDs.put(testCaseId, testResultId);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return testResultIDs;
	}

	// ********Update Test Results from test run in Azure DevOps*******
	public void updateTestResult(String state, String comment, String outcome, int testCaseId) {
		try {
			getTestResults(runId);
			int testResultId = testResultIDs.get(testCaseId + "");
			String uri = Const.AZUREAPIPATH + "/test/Runs/" + runId + "/results?api-version=5.1-preview.6";
			HttpPatch httppost = new HttpPatch(uri);
			httppost.setHeader("Content-Type", "application/json");
			httppost.setHeader("Authorization",
					"Basic bGJhbmdhOnBmYzd2Y3l2aWNoZGJwdnM3dmpkZ243bHA3cDU1d2VpbDR2NDN2dnZjZGlhY2I1eHR2ZmE=");
			JSONArray arr = new JSONArray();
			JSONObject requestObject = new JSONObject();
			requestObject.toJSONArray(arr);
			requestObject.put("id", testResultId);
			requestObject.put("outcome", outcome);
			requestObject.put("comment", comment);
			requestObject.put("state", state);
			arr.put(requestObject);
			String requestBody = arr.toString();
			httppost.setEntity(new StringEntity(requestBody, ContentType.APPLICATION_JSON));
			HttpClient client = HttpClientBuilder.create().build();
			HttpResponse response = client.execute(httppost);
			String jsonResponse = EntityUtils.toString(response.getEntity());
			JSONObject jsonObj = new JSONObject(jsonResponse);
			JSONArray ja_data = jsonObj.getJSONArray("value");
			int length = ja_data.length();
			for (int i = 0; i < length; i++) {
				JSONObject jObj = ja_data.getJSONObject(i);
				Assert.assertTrue(jObj.getString("outcome").equalsIgnoreCase(outcome));
				Assert.assertTrue(jObj.getInt("id") == (testResultId));
			}
			logger.info("Test Result updated for run: " + runId + " Test ResultID = " + testResultId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ********Add Attachment to Test Run in Azure DevOps*******

	public void addAttachmentToRun(int runId, String filePath) {
		try {
			String dateTime = CommonMethods.getCurrentDateTime();
			String stream = encodeFileToString(filePath);
			String uri = Const.AZUREAPIPATH + "/test/Runs/" + runId + "/attachments?api-version=5.1-preview.1";
			HttpPost httppost = new HttpPost(uri);
			httppost.setHeader("Content-Type", "application/json");
			httppost.setHeader("Authorization",
					"Basic bGJhbmdhOnBmYzd2Y3l2aWNoZGJwdnM3dmpkZ243bHA3cDU1d2VpbDR2NDN2dnZjZGlhY2I1eHR2ZmE=");
			JSONObject requestObject = new JSONObject();
			requestObject.put("attachmentType", "GeneralAttachment");
			requestObject.put("fileName", runId + "_attachment_" + dateTime);
			requestObject.put("stream", stream);
			String requestBody = requestObject.toString();
			httppost.setEntity(new StringEntity(requestBody, ContentType.APPLICATION_JSON));
			HttpClient client = HttpClientBuilder.create().build();
			HttpResponse response = client.execute(httppost);
			if (response.toString().contains("200 OK"))
				logger.info("Attached Report to Run: " + runId);
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// ***************************encode a file***********************************
	public String encodeFileToString(String filepath) {
		File originalFile = new File(filepath);
		String encodedBase64 = null;
		try {
			FileInputStream fileInputStreamReader = new FileInputStream(originalFile);
			byte[] bytes = new byte[(int) originalFile.length()];
			fileInputStreamReader.read(bytes);
			encodedBase64 = new String(org.apache.commons.codec.binary.Base64.encodeBase64(bytes));
			fileInputStreamReader.close();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return encodedBase64;
	}

	// ******************************Zip File
	// Methods**********************************************

	public void zipDir(String path, String srcFolder, String destZipFile) throws Exception {
		ZipOutputStream zip = null;
		FileOutputStream fileWriter = null;
		fileWriter = new FileOutputStream(destZipFile);
		zip = new ZipOutputStream(fileWriter);
		addFolderToZip(path, srcFolder, zip);
		zip.flush();
		zip.close();
	}

	private void addFileToZip(String path, String srcFile, ZipOutputStream zip) {
		try {
			File folder = new File(srcFile);
			if (folder.isDirectory()) {
				addFolderToZip(path, srcFile, zip);
			} else {
				byte[] buf = new byte[1024];
				int len;
				FileInputStream in = new FileInputStream(srcFile);
				zip.putNextEntry(new ZipEntry(path + "/" + folder.getName()));
				while ((len = in.read(buf)) > 0) {
					zip.write(buf, 0, len);
				}
				in.close();
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private void addFolderToZip(String path, String srcFolder, ZipOutputStream zip) throws Exception {
		try {
			File folder = new File(srcFolder);
			for (String fileName : folder.list()) {
				if (path.equals("")) {
					addFileToZip(folder.getName(), srcFolder + "/" + fileName, zip);
				} else {
					addFileToZip(folder.getAbsolutePath(), srcFolder + "/" + fileName, zip);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static int getTestDataid() {
		return testDataid;
	}

	public static void setTestDataid(int testDataid) {
		BasePage.testDataid = testDataid;
	}

	public String fixSpaces(String string, int length) {
		if (string == null)
			string = "";
		string = string.trim();
		char[] data = new char[length];
		for (int i = 0; i < Math.min(string.length(), length); i++) {
			data[i] = string.charAt(i);
		}
		if (string.length() < length) {
			for (int i = string.length(); i < length; i++) {
				data[i] = ' ';
			}
		}
		return new String(data);
	}

	public Map<String, Integer> loadProperties(String filepath) {

		Map<String, Integer> mymap = new LinkedHashMap<String, Integer>();
		try {
			BufferedReader fileReader = new BufferedReader(new FileReader(new File(filepath)));
			String line;
			while ((line = fileReader.readLine()) != null) {
				String header = line.split("=")[0].trim();
				int length = Integer.parseInt(line.split("=")[1].trim());
				mymap.put(header, length);
			}
			fileReader.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return mymap;
	}

	public boolean waitForAlert() {
		try {
			WebDriverWait w = new WebDriverWait(driver, 30);
			w.until(ExpectedConditions.alertIsPresent());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			return false;
		}
	}

	public boolean isAlertPresent() {
		try {
			driver.switchTo().alert();
			return true;
		} catch (NoAlertPresentException Ex) {
			return false;
		}
	}

	public boolean compareHashMaps(HashMap<String, String> map1, HashMap<String, String> m360_PCP) {
		boolean flag = true;
		if (map1 == null || m360_PCP == null)
			return false;

		for (String ch1 : map1.keySet()) {
			if (!map1.get(ch1).trim().equalsIgnoreCase(m360_PCP.get(ch1).trim())) {
				test.log(Status.FAIL, ch1 + ": " + map1.get(ch1) + " is not equal to " + m360_PCP.get(ch1));
				flag = false;
			}
		}
		return flag;
	}

	public void zoomInZoomOut(String value) {
		JavascriptExecutor js = (JavascriptExecutor) driver;
		js.executeScript("document.body.style.zoom='" + value + "'");
	}

	// code for selecting values from dropdown in Guiding Care Application
	public void selectDropdownValue(String dropdownToBeText, WebElement dropdownArrowButton,
			WebElement dropdownCurrentText) {
		try {
			Actions a = new Actions(driver);
			for (int i = 0; i < 40; i++) {
				if (dropdownToBeText.equals(dropdownCurrentText.getText())) {
					break;
				} else {
					a.clickAndHold(dropdownArrowButton).sendKeys(Keys.DOWN).build().perform();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void selectDropdownValueContains(String dropdownToBeText, WebElement dropdownArrowButton,
			WebElement dropdownCurrentText) {
		try {
			Actions a = new Actions(driver);
			for (int i = 0; i < 40; i++) {
				if (dropdownCurrentText.getText().contains(dropdownToBeText)) {
					break;
				} else {
					a.clickAndHold(dropdownArrowButton).sendKeys(Keys.DOWN).build().perform();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void javaScriptClick(WebElement element) {
		JavascriptExecutor executor = (JavascriptExecutor) driver;
		executor.executeScript("arguments[0].click();", element);
	}

	public String getControlNumber() {
		String controlNumber = "";
		try {
			controlNumber = randBetween(100000000, 999999999) + "";

		} catch (Exception e) {
			e.printStackTrace();
		}
		return controlNumber;
	}

	public String compareDatesInDifferentFormat(String dateUI, String dateExpected) {
		SimpleDateFormat sdf[] = new SimpleDateFormat[] { new SimpleDateFormat("MM/dd/yyyy"),
				new SimpleDateFormat("yyyy-MM-dd") };

		Date date1 = parse(dateUI, sdf);
		Date date2 = parse(dateExpected, sdf);
		logger.info("Comparing following dates-" + date1 + "  " + date2);
		if (date1 == null || date2 == null) {
			logger.info("Null dates cannot be compared");
			return "Dates to be comapred have null value";
		}
		long difference = (date2.getTime() - date1.getTime()) / 86400000;
		logger.info("Difference in dates is-" + Math.abs(difference));
		return Math.abs(difference) + "";
	}

	public static Date parse(String value, DateFormat... formatters) {
		Date date = null;
		for (DateFormat formatter : formatters) {
			try {
				date = formatter.parse(value);
				break;
			} catch (ParseException e) {
			}
		}
		return date;
	}

	// To forcefully change property of a hidden element to enabled and access it
	public String enableElementAndGetText(WebElement element) {
		String text = "";
		((JavascriptExecutor) driver).executeScript("arguments[0].disabled = 'enabled';", element);
		text = element.getAttribute("value").trim();
		return text;
	}

	/// PDF Reader & Image Reader and validation methods
	/// -------------------------------------------------------------

	/**
	 * reads the pdf and returns the string
	 * 
	 * @param filepath
	 * @return pdf text as string
	 * @throws InvalidPasswordException
	 * @throws IOException
	 */
	public static String pdfReader(String filepath) {
		String pdfText = null;
		try {
			PDDocument document = PDDocument.load(new File(filepath));
			document.getClass();
			if (!document.isEncrypted()) {
				PDFTextStripperByArea stripper = new PDFTextStripperByArea();
				stripper.setSortByPosition(true);
				PDFTextStripper tStripper = new PDFTextStripper();
				String pdfFileInText = tStripper.getText(document);
				String lines[] = pdfFileInText.split("\\r?\\n");
				for (String line : lines) {
					pdfText = pdfText + line;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return pdfText;
	}

	/**
	 * reads the image and returns the string
	 * 
	 * @param inputFilePath
	 * @param tessdataMasterPath
	 * @return image text as string
	 * @throws TesseractException
	 */
	public static String readImageReturnText(String inputFilePath, String tessdataMasterPath) {
		String imageText = null;
		try {
			Tesseract tesseract = new Tesseract();
			tesseract.setDatapath(Const.tessDataMaster);
			imageText = tesseract.doOCR(new File(inputFilePath));
		} catch (Exception e) {
			e.printStackTrace();
		}
		return imageText;
	}

	public static boolean validatePDForImage(String pdfText, HashMap<String, String> verificationList) {
		boolean flag = true;
		try {
			if (verificationList == null)
				return false;

			for (String ch1 : verificationList.keySet()) {
				if (!pdfText.contains(verificationList.get(ch1))) {
					test.log(Status.FAIL, ch1 + ": " + verificationList.get(ch1) + " is not in " + pdfText);
					flag = false;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return flag;
	}

	public void saveFileWindows(String text) {
		try {
			Robot robot = new Robot();
			Actions a = new Actions(driver);
			robot.keyPress(KeyEvent.VK_DOWN);
			robot.keyRelease(KeyEvent.VK_DOWN);
			a.sendKeys(text);
			robot.keyPress(KeyEvent.VK_ENTER);
			robot.keyRelease(KeyEvent.VK_ENTER);
		} catch (AWTException e) {
			e.printStackTrace();
		}
	}

}
