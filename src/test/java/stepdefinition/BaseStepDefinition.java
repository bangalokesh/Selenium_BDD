package stepdefinition;

import cucumber.api.java.en.And;
import cucumber.api.java.en.Given;
import managers.PageManager;
import pageclasses.BasePage;

public class BaseStepDefinition {
	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger.getLogger(BaseStepDefinition.class.getName());
	
	PageManager pm = new PageManager();
	BasePage bp = pm.getBasePage();
	
  @Given("^I Open Browser$")
  public void openBrowser() throws Throwable {
	  try {
		  if(BasePage.isContinueExecution()) {
			  logger.info("Opening Browser");
		  }
	  }catch(Exception e){
		  e.printStackTrace();
	  }
  }
  
  @Given("I navigate to")
  public void navigate() throws Throwable {
	  try {
		  if(BasePage.isContinueExecution()) {
			  logger.info("Navigating");
			  pm.getM360HomePage().navigateToMThreeSixty();
			  pm.getM360HomePage().navigateToM360BillingHome();
		  }
	  } catch(Exception e){
		  e.printStackTrace();
	  }
  }
  
  @And("Set RunMode to N as Test is completed")
  public void setRunModeToN() throws Throwable {
	  try {
		  bp.setRunModeToNo();
	  } catch(Exception e){
		  e.printStackTrace();
	  }
  }
}
