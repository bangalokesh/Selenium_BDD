package pageclasses.sap;

import java.io.IOException;
import java.sql.SQLException;
import java.util.List;

import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;

import enums.LocatorTypes;
import managers.PageManager;
import pageclasses.BasePage;
import pageclasses.m360membership.M360MemberPage;
import utils.Dbconn;

public class SAPLogin extends BasePage{
	
	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(M360MemberPage.class.getName());
	PageManager pm = new PageManager();
	Dbconn db = new Dbconn();
	
	public SAPLogin() {
		driver = getWebDriver();
	}
	
	
	
	public void loginToSAP() throws IOException, SQLException {
		getWebDriver();
		driver.navigate().to("https://sapdemo.birlasoft.com:1443/sap/bc/ui5_ui5/ui2/ushell/shells/abap/FioriLaunchpad.html?sap-client=100&sap-language=EN");
		driver.findElement(By.id("USERNAME_FIELD-inner")).sendKeys("LOKESHB");
		driver.findElement(By.id("PASSWORD_FIELD-inner")).sendKeys("Corp1@User");
		driver.findElement(By.id("LOGIN_LINK")).click();
		logger.info("Login Successful");
		wait(10);
		elementVisibleWait(LocatorTypes.xpath, "//a[@id='sf']//span[@class='sapUshellShellHeadItmCntnt']", 10);
		driver.findElement(By.xpath("//a[@id='sf']//span[@class='sapUshellShellHeadItmCntnt']")).click();
		elementClickableWait(LocatorTypes.id, "searchFieldInShell-select-labelText", 10);
		wait(2);
		while(!(driver.findElement(By.id("searchFieldInShell-select-labelText")).getText().trim().equalsIgnoreCase("All"))) {
			wait(2);
		}	
		driver.findElement(By.id("searchFieldInShell-input-inner")).sendKeys("Create Sales Order");
		if(driver.findElement(By.id("searchFieldInShell-button-img")).isEnabled()){
			driver.findElement(By.id("searchFieldInShell-button-img")).click();
		}
		while(!driver.getTitle().trim().contains("Search for")) {
			wait(2);
		}
		if(driver.getTitle().contains("Search for")) {
			logger.info("Search Results Page Displayed");
		}
		List<WebElement> searchItems = driver.findElements(By.xpath("//*[@id='AppSearchContainerResultListItem']/div"));
		outerloop:
		for (WebElement element: searchItems) {
			if(element.getText().contains("Create Sales Orders")) {
				element.click();
				break outerloop;
			}
		}
		while(!driver.getTitle().trim().equalsIgnoreCase("Create Sales Documents")) {
			wait(2);
		}
		if(driver.getTitle().trim().equalsIgnoreCase("Create Sales Documents")){
			System.out.println(driver.getTitle().trim());	
			logger.info("Start Creating Sales Order");
		}
		
		
	} 
	
	public void createSalesOrder() {
		driver.switchTo().frame("application-SalesDocument-create");
		driver.findElement(By.id("M0:46:::2:22")).sendKeys("BV");	
		driver.findElement(By.id("M0:46:::5:22")).sendKeys("1810");
		driver.findElement(By.id("M0:46:::6:22")).sendKeys("10");
		driver.findElement(By.id("M0:46:::7:22")).sendKeys("00");
		driver.findElement(By.id("M0:46:::8:22")).sendKeys("180");
		driver.findElement(By.id("M0:46:::9:22")).sendKeys("180");
		driver.findElement(By.id("M0:50::btn[0]")).click();
		
		wait(5);
		
		//SO Detail Screen
		//driver.switchTo().frame("application-SalesDocument-create");
		driver.findElement(By.id("M0:46:1:1::0:17")).sendKeys("18100001");
		driver.findElement(By.id("M0:46:1::3:17")).sendKeys(""+getRandomNumber());
		driver.findElement(By.id("M0:46:1::3:56")).sendKeys("27.04.2020");
		driver.findElement(By.id("M0:46:2:3B256:2:1::0:3")).click();
		
		//Material Details
		
		
		//javaScriptClick(driver.findElement(By.xpath("//input[@id='tbl3042[1,2]_c")));
		driver.findElement(By.xpath("//*[@id=\"M0:46:2:3B256:2\"]/div[2]/table/tbody/tr[2]/td/div/div[2]/table/tbody/tr[1]/td[3]/div[1]/span[1]/input[1]")).sendKeys("FG-101");
		driver.findElement(By.xpath("//*[@id=\\\"M0:46:2:3B256:2\\\"]/div[2]/table/tbody/tr[2]/td/div/div[2]/table/tbody/tr[1]/td[5]/div[1]/span[1]/input[1]")).sendKeys("10");
		
		driver.findElement(By.id("M0:50::btn[11]")).click();
		
		
		/*JavascriptExecutor jse = (JavascriptExecutor)driver;
		jse.executeScript("document.getElementsByName('body')[0].setAttribute('type', 'text');");
		driver.findElement(By.xpath("//input[@name='body']")).clear();
		driver.findElement(By.xpath("//input[@name='body']")).sendKeys("Ripon: body text");
		
		//*[@id="tbl3270[1,2]_c"]
		//*[@id="tbl329[1,2]"]
		//*[@id="tbl329[1,2]"]/div
		//*[@id="tbl329[1,2]_c-r"]
		//*[@id="tbl329[1,2]_c"]
		
		                                                                                                                                                                                                                                 //*[@id="M0:46:2:3B256:2"]/div[2]/table/tbody/tr[2]/td/div/div[2]/table/tbody/tr[1]/td[3]/div[1]/span[1]/input[1]
		
		/html[1]/body[1]/table[1]/tbody[1]/tr[1]/td[1]/div[1]/form[1]/table[1]/tbody[1]/tr[2]/td[1]/div[1]/div[2]/table[1]/tbody[1]/tr[3]/td[1]/div[1]/table[1]/tbody[1]/tr[1]/td[1]/div[1]/div[1]/div[1]/div[2]/div[1]/div[2]/table[1]/tbody[1]/tr[2]/td[1]/div[1]/div[2]/table[1]/tbody[1]/tr[1]/td[3]/div[1]/span[1]/input[1]
		
		*/
		
	}
	
	@Test
	public void testSOCreation() throws IOException, SQLException {
		loginToSAP();
		createSalesOrder();
	}

}
