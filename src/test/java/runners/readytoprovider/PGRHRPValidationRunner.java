package runners.readytoprovider;

import org.junit.AfterClass;
import org.junit.runner.RunWith;

import cucumber.api.CucumberOptions;
import cucumber.api.junit.Cucumber;
import pageclasses.BasePage;

@RunWith(Cucumber.class)
@CucumberOptions(
		tags= " @hrp_pgr_validation, @59919",
		features="src\\test\\resources\\features",
		glue="stepdefinition",
		monochrome = true,
		plugin = { "pretty", "html:reports/cucumber-reports", "json:reports/cucumber-reports/Cucumber.json", "junit:reports/cucumber-reports/Cucumber.xml", "com.aventstack.extentreports.cucumber.adapter.ExtentCucumberAdapter:reports/cucumber-reports/report.html" }
	)


public class PGRHRPValidationRunner {
	@AfterClass
	public static void writeExtentReport() {
		
		BasePage bp = new BasePage();
		bp.endOfTestRun();
		System.out.println("Ending Test");
	}
}
