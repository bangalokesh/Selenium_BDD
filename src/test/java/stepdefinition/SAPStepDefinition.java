package stepdefinition;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import EnrollmentFiles.CreateFileFromLayout;
import cucumber.api.java.en.Given;
import junit.framework.Assert;
import managers.PageManager;
import pageclasses.BasePage;
import utils.ApiUtils;
import utils.Const;

public class SAPStepDefinition {

	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(SAPStepDefinition.class.getName());

	PageManager pm = new PageManager();
	BasePage bp = pm.getBasePage();
	CreateFileFromLayout createFile = new CreateFileFromLayout();

	@Given("I Login to SAP")
	public void sapLogin() {
		try {
			if (BasePage.isContinueExecution()) {
				logger.info("Start Logging in to SAP");

			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@SuppressWarnings("deprecation")
	@Given("I read the file and validate in API")
	public void I_read_the_file_and_validate_in_API() {
		if (BasePage.isContinueExecution()) {
			logger.info("Start Logging file test in java");
			try {
				List<HashMap<String, String>> file = createFile.readFileFromLayout(Const.SAPConfigProperties,
						Const.SAPDataFile, ";");
				for (HashMap<String, String> row : file) {
					HashMap<String, String> apiRow = new HashMap<String, String>();
					System.out.println(row.get("d:srcsyscd").toString());
					String xml = ApiUtils.apiGetRequest(
							"https://bslswissrevm.birlasoft.com:8004/sap/opu/odata/sap/YENTITY_MAPPING_SRV/MappingTableSet?$filter=srcsyscd%20eq%20%27"
									+ row.get("d:srcsyscd").toString() + "%27&sap-client=100",
							"Basic bXNhcXVpYjpXZWxjb21lMg==");
					apiRow.put("d:srcsyscd", row.get("d:srcsyscd").toString());
					apiRow.put("d:lesrcid", getValueOfTag("d:lesrcid", xml));
					apiRow.put("d:leid", getValueOfTag("d:leid", xml));
					apiRow.put("d:validfrom", getValueOfTag("d:validfrom", xml).replaceAll("-", ""));
					apiRow.put("d:validto", getValueOfTag("d:validto", xml).replaceAll("-", ""));

					boolean b = bp.compareHashMaps(row, apiRow);
					if (b)
						logger.info("Data in file and table matched");
					else
						logger.info("Data in file and table did not match");

				}
			} catch (Exception e) {
				e.printStackTrace();
				Assert.assertTrue("Exception - Connection failure", false);;
			}
		}
	}

	private String getValueOfTag(String tag, String xml) {
		System.out.println(tag);
		System.out.println(xml);
		String startTag = "<" + tag + ">";
		String closeTag = "</" + tag + ">";
		return xml.substring(xml.indexOf(startTag) + startTag.length(), xml.indexOf(closeTag));
	}
}
