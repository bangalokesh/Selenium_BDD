package pageclasses.hrp;

import java.io.IOException;
import java.util.List;
import java.util.Set;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.NoSuchWindowException;
import org.openqa.selenium.WebDriverException;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import enums.LocatorTypes;
import io.appium.java_client.windows.WindowsDriver;
import pageclasses.BasePage;
import pageobjects.HRPObjRepo;

public class HRPHomePage extends BasePage {

	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(HRPHomePage.class.getName());

	public HRPHomePage(WindowsDriver<WebElement> winDriver) {

	}

	public void navigateMemberSearch() {
		try {
			WebDriverWait wait = new WebDriverWait(winDriver, 10);
			wait.until(ExpectedConditions.elementToBeClickable(By.name("Members")));
			getWinElement(LocatorTypes.name, HRPObjRepo.HRPDockLeft.member_name).click();
			List<WebElement> e = getWinElements(LocatorTypes.id, HRPObjRepo.HRPHomeMemberSearch.memSearch_id);
			e.get(0).click();
			logger.info("search window open");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("failure in search member at HRPHomePage");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public boolean searchProvider() {
		try {
			getWinElement(LocatorTypes.name, HRPObjRepo.HRPDockLeft.provider_name).click();
			List<WebElement> e = getWinElements(LocatorTypes.id, HRPObjRepo.HRPHomeMemberSearch.memSearch_id);
			e.get(0).click();
			// winDriver.manage().window().maximize();
			logger.info("search window open");
		}  catch(NoSuchWindowException e) {
			e.printStackTrace();
			killHRP();
			winDriver.quit();
			return false;
		} catch(NullPointerException e) {
			killHRP();
			e.printStackTrace();			
			winDriver.quit();
			return false;
		} catch(WebDriverException e) {
			killHRP();
			e.printStackTrace();
			winDriver.quit();
			return false;
		} catch (Exception e) {
			try {
			Set<String> winHandles = winDriver.getWindowHandles();
			
			for (String winHandle : winHandles) {
				if ((winDriver.getTitle().contains("Application Error"))) {
					winDriver.quit();
					killHRP();
					return false;
				}
			}}catch(Exception e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			try {
				reportFail("failure in search member at HRPHomePage");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return true;
	}

	public void killHRP() {
		try {
		Runtime.getRuntime().exec("taskkill /F /IM HealthEdge.Manager.exe /T");
		} catch(Exception e) {
			e.printStackTrace();
		}
		
		
	}

	public boolean searchPractioner() {
		try {
			Actions a = new Actions(winDriver);
			a.keyDown(Keys.CONTROL).sendKeys(String.valueOf('\u0066')).keyUp(Keys.CONTROL).perform();
			wait(2);
			getWinElement(LocatorTypes.name, HRPObjRepo.HRPTobBar.searchFor_name).sendKeys("p");
			//a.click(getWinElement(LocatorTypes.name, HRPObjRepo.HRPTobBar.searchFor_name)).sendKeys("p").build().perform();
		} catch(NoSuchWindowException e) {
			e.printStackTrace();
			killHRP();
			winDriver.quit();
			return false;
		} catch(NoSuchElementException e) {
			e.printStackTrace();
			killHRP();
			winDriver.quit();
			return false;
		} catch(NullPointerException e) {
			killHRP();
			e.printStackTrace();			
			winDriver.quit();
			return false;
		} catch(WebDriverException e) {
			killHRP();
			e.printStackTrace();
			winDriver.quit();
			return false;
		} catch (Exception e) {
			try {
			Set<String> winHandles = winDriver.getWindowHandles();
			
			for (String winHandle : winHandles) {
				if ((winDriver.getTitle().contains("Application Error"))) {
					winDriver.quit();
					killHRP();
					return false;
				}
			}}catch(Exception e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			try {
				reportFail("failure in search member at HRPHomePage");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return true;
	}


	public void searchSupplier() {
		try {
			getWinElement(LocatorTypes.name, HRPObjRepo.HRPDockLeft.provider_name).click();
			List<WebElement> e = getWinElements(LocatorTypes.id, HRPObjRepo.HRPHomeMemberSearch.memSearch_id);
			e.get(1).click();
			logger.info("Supplier search window open");
			
		} catch(NoSuchWindowException e) {
			e.printStackTrace();			
			winDriver.quit();
			
		} catch(NullPointerException e) {
			e.printStackTrace();			
			winDriver.quit();
		}
		
		catch (Exception e) {
			
			Set<String> winHandles = winDriver.getWindowHandles();
			
			for (String winHandle : winHandles) {
				if ((winDriver.getTitle().contains("Application Error"))) {
					winDriver.quit();
				}
			}
			e.printStackTrace();
			try {
				reportFail("failure in search supplier at HRPHomePage");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public boolean searchSupplierNew() {
		try {
			Actions a = new Actions(winDriver);
			a.keyDown(Keys.CONTROL).sendKeys(String.valueOf('\u0066')).keyUp(Keys.CONTROL).perform();
			
			//a.click(getWinElement(LocatorTypes.name, HRPObjRepo.HRPTobBar.searchFor_name)).sendKeys("s").build()
			//.perform();
			getWinElement(LocatorTypes.name, HRPObjRepo.HRPTobBar.searchFor_name).sendKeys("s");
			
		} catch(NoSuchWindowException e) {
			e.printStackTrace();
			killHRP();
			winDriver.quit();
			return false;
			
		} catch(NoSuchElementException e) {
			e.printStackTrace();
			killHRP();
			winDriver.quit();
			return false;
			
		}catch(NullPointerException e) {
			killHRP();
			e.printStackTrace();			
			winDriver.quit();
			return false;
			
		} catch(WebDriverException e) {
			killHRP();
			e.printStackTrace();
			winDriver.quit();
			return false;
			
		} catch (Exception e) {
			try {
			Set<String> winHandles = winDriver.getWindowHandles();
			
			for (String winHandle : winHandles) {
				if ((winDriver.getTitle().contains("Application Error"))) {
					winDriver.quit();
					killHRP();
					return false;
				}
			}}catch(Exception e1) {
				e1.printStackTrace();
			}
			e.printStackTrace();
			try {
				reportFail("failure in search member at HRPHomePage");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return true;
	}
	public void closeSearch() {
		try {
			getWinElement(LocatorTypes.name, HRPObjRepo.HRPTobBar.close_name).click();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Failure in closing search window");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}

	}

}
