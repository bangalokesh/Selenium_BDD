package runners.readytoprovider;

import org.junit.AfterClass;
import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import pageclasses.BasePage;

@RunWith(Cucumber.class)
@CucumberOptions(
		//@VerifyPGRinM360MDHMMH
		tags= "@VerifyPGRinM360MPPMPH", 
		features="src\\test\\resources\\features",
		glue="stepdefinition",
		monochrome = true,
		plugin = { "pretty", "html:reports/cucumber-reports", "json:reports/cucumber-reports/Cucumber.json", "junit:reports/cucumber-reports/Cucumber.xml", "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:reports/cucumber-reports/report.html" }
	)
public class PGRM360ValidationRunner {

	@AfterClass
	public static void writeExtentReport() {
		
		BasePage bp = new BasePage();
		bp.endOfTestRun();
		System.out.println("Ending Verify PGR in M360 Test");
	}
}
