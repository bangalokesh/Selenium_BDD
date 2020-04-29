package managers;

import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.firefox.FirefoxOptions;
import org.openqa.selenium.firefox.FirefoxProfile;
import org.openqa.selenium.ie.InternetExplorerDriver;
import org.openqa.selenium.ie.InternetExplorerOptions;
import org.openqa.selenium.remote.CapabilityType;
import org.openqa.selenium.remote.DesiredCapabilities;

import enums.DriverType;
import io.appium.java_client.windows.WindowsDriver;
import utils.Const;

public class DriverManager {

	public static WebDriver driver;
	public static WindowsDriver<WebElement> winDriver;

	@SuppressWarnings("static-access")
	public DriverManager(WebDriver driver, WindowsDriver<WebElement> winDriver) {
		this.driver = driver;
		this.winDriver = winDriver;
	}

	public static WebDriver createLocalWebDriver(String driverType) {
		try {
			switch (driverType) {
			case "FIREFOX":
				System.setProperty("webdriver.gecko.driver",
						System.getProperty("user.dir") + "//drivers//geckodriver.exe");
				FirefoxOptions options = new FirefoxOptions().setProfile(new FirefoxProfile());
				options.setLegacy(true);
				driver = new FirefoxDriver(options);
				break;
			case "CHROME":
				ChromeOptions cOptions = new ChromeOptions();
				//cOptions.setHeadless(true);
				cOptions.setCapability(CapabilityType.SUPPORTS_APPLICATION_CACHE, false);
				cOptions.setCapability(CapabilityType.SUPPORTS_NETWORK_CONNECTION, true);
				System.setProperty("webdriver.chrome.driver",
						System.getProperty("user.dir") + "//drivers//chromedriver.exe");
				driver = new ChromeDriver(cOptions);
				driver.get("chrome://settings/clearBrowserData");
				break;
			case "INTERNETEXPLORER":
				System.setProperty("webdriver.ie.driver",
						System.getProperty("user.dir") + "//drivers//IEDriverServer.exe");
				InternetExplorerOptions ieOptions = new InternetExplorerOptions();
				ieOptions.setCapability(InternetExplorerDriver.IE_ENSURE_CLEAN_SESSION, true);
				ieOptions.setCapability(InternetExplorerDriver.ENABLE_ELEMENT_CACHE_CLEANUP, true);
				ieOptions.setCapability(InternetExplorerDriver.NATIVE_EVENTS, false);
				ieOptions.setCapability(CapabilityType.SUPPORTS_JAVASCRIPT, true);
				ieOptions.setCapability(CapabilityType.ACCEPT_SSL_CERTS, false);
				ieOptions.setCapability(InternetExplorerDriver.REQUIRE_WINDOW_FOCUS, true);
				ieOptions.setCapability(InternetExplorerDriver.IGNORE_ZOOM_SETTING, true);
				ieOptions.setCapability(InternetExplorerDriver.INTRODUCE_FLAKINESS_BY_IGNORING_SECURITY_DOMAINS, true);
				ieOptions.setCapability("ignoreProtectedModeSettings", true);
				driver = new InternetExplorerDriver(ieOptions);
				break;
			}
			driver.manage().timeouts().implicitlyWait(Integer.parseInt(Const.IMPLICITWAITTIME), TimeUnit.SECONDS);
			driver.manage().window().maximize();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return driver;
	}

	public static WindowsDriver<WebElement> createLocalWindowsDriver(DriverType driverType, String appName) {
		try {
			DesiredCapabilities appCapabilities = new DesiredCapabilities();
			appCapabilities.setCapability("deviceName", Const.DEVICENAME);
			appCapabilities.setCapability("app", appName);
			winDriver = new WindowsDriver<WebElement>(new URL("http://" + Const.WINLOCALIP + ":4723/wd/hub"),
					appCapabilities);
			winDriver.manage().timeouts().implicitlyWait(Integer.parseInt(Const.IMPLICITWAITTIME), TimeUnit.SECONDS);
		} catch (Exception e) {
			e.printStackTrace();
		}
		return winDriver;

	}

}
