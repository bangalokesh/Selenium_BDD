package processes;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import managers.PageManager;
import pageclasses.BasePage;
import x12.encounters.EncounterData;
import x12.encounters.EncounterData.Subscriber;


public class EncounterDetailsProcess extends BasePage {

	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(EncounterDetailsProcess.class.getName());
	PageManager pm = new PageManager();

	public List<String> getServiceLineItems(String formatServiceLine, Subscriber subscriberData,
			List<String> serviceLineItems) {
		int startIndex;
		int endIndex;
		String[] formatServiceLineArray;
		try {
			formatServiceLineArray = subscriberData.getServiceLines().toString().split("LX");
			if (!serviceLineItems.isEmpty()) {
				serviceLineItems.clear();
			}
			for (int i = 0; i < formatServiceLineArray.length; i++) {
				if (formatServiceLineArray[i].contains("SV107")) {
					startIndex = formatServiceLineArray[i].indexOf("SV107=");
					endIndex = formatServiceLineArray[i].indexOf("}");
					formatServiceLine = formatServiceLineArray[i].substring(startIndex, endIndex);
					formatServiceLine = formatServiceLine.replace("SV107=", "");
					serviceLineItems.add(formatServiceLine);
				} else {
					continue;
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Test Failed in getServiceLineItems method");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return serviceLineItems;
	}

	public String getEntityName(Subscriber subscriberData) {
		String fullName = "";
		try {
			if (subscriberData.getEntityTypeQualifier().equals("1")) {
				if (!subscriberData.getMiddleInitial().trim().equals("")) {
					fullName = subscriberData.getFirstName() + " " + subscriberData.getMiddleInitial() + " "
							+ subscriberData.getLastName();
				} else {
					fullName = subscriberData.getFirstName() + " " + subscriberData.getLastName();
				}
			} else {
				fullName = subscriberData.getOrganisationName();
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Test Failed in getEntityName method");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return fullName;
	}

	public String getLongClaimTypeValue(Subscriber subscriberData) {
		String claimType = "";
		try {
			if (subscriberData.getClaimType().equals("PT")) {
				claimType = "Professional";
			} else if (subscriberData.getClaimType().equals("IT")) {
				claimType = "Institutional";
			} else if (subscriberData.getClaimType().equals("ET")) {
				claimType = "DME";
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Test Failed in getLongClaimTypeValue method");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return claimType;
	}

	public void processAndValidateEncountersDetails(String claimNumber, String claimType, String submitterID,
			HashMap<String, String> encounterFileData) {
		try {
			boolean flag1 = pm.getEncountersDetailsPage().searchClaimNumber(claimNumber, claimType, submitterID);
			if (flag1) {
				boolean flag2 = pm.getEncountersDetailsPage().validateEncounterDetails(encounterFileData);
				if (flag2) {
					reportPass("Search for encounter data is successful for " + claimNumber);
				} else {
					reportFail("Validation for encounter data failed for " + claimNumber);
				}
			} else {
				reportFail("Validation for encounter data failed for " + claimNumber
						+ ", unable to search for claim number.");
			}
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Test Failed in processAndValidateEncountersDetails method");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void executeEncounterDetails() {
		try {
			int counter = 0;
			String claimType = "";
			String claimNumber = "";
			String submitterID = "";
//			String medicareID = "";
			String fullName = "";
			String formatServiceLine = "";
			String totalClaimChargeAmount = "";
			String npi = "";
			String taxID = "";
			String placeOfService = "";
			String claimFreqTypeCode = "";
			String diagnosisCodes = "";
			String diagnosisPointers = "";
			double chargeAmount;
			List<String> serviceLineItems = new LinkedList<String>();
			List<String> serviceLineItemsOutput = new LinkedList<String>();
			HashMap<String, String> encounterFileData = new HashMap<String, String>();
			EncounterData encounterData = new EncounterData();
			List<Subscriber> data = encounterData.getEncounterData();
			for (Subscriber subscriberData : data) {
				counter++;
				claimType = getLongClaimTypeValue(subscriberData);
				serviceLineItemsOutput = getServiceLineItems(formatServiceLine, subscriberData, serviceLineItems);
				/*if (subscriberData.getSubscriberPrimaryIdentifier() == null) {
					medicareID = "";
				} else {
					medicareID = subscriberData.getSubscriberPrimaryIdentifier();
				}*/
				claimNumber = subscriberData.getClaimNumber();
				submitterID = subscriberData.getSubmitterID();
				chargeAmount = Double.parseDouble(subscriberData.getClaimAmount());
				totalClaimChargeAmount = Double.toString(chargeAmount);
				npi = subscriberData.getNPI();
				taxID = subscriberData.getTaxID();
				placeOfService = subscriberData.getPlaceOfServiceCode();
				claimFreqTypeCode = subscriberData.getClaimFrequencyCode();
				diagnosisCodes = subscriberData.getDiagnosisCodes().toString();
				diagnosisPointers = serviceLineItemsOutput.toString();
				fullName = getEntityName(subscriberData);
				encounterFileData.put("SubscriberName", fullName);
				// encounterFileData.put("MedicareID", medicareID);
				encounterFileData.put("TotalClaimChargeAmt", totalClaimChargeAmount);
				encounterFileData.put("NPI", npi);
				encounterFileData.put("TaxID", taxID);
				encounterFileData.put("PlaceOfService", placeOfService);
				encounterFileData.put("ClaimFreqTypeCode", claimFreqTypeCode);
				encounterFileData.put("DiagnosisCodes", diagnosisCodes);
				encounterFileData.put("DiagnosisPointers", diagnosisPointers);

				setrowTestData(encounterFileData, counter);
				test = extent.createTest("Encounter Validation test for claim number: " + claimNumber + "-" + claimType
						+ "-" + submitterID);

				processAndValidateEncountersDetails(claimNumber, claimType, submitterID, encounterFileData);
				flushTest();
			}
			driver.close();
			closeBrowser();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Test Failed in executeEncounterDetails method");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}
}
