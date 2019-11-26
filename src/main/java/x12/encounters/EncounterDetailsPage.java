package x12.encounters;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;

import com.aventstack.extentreports.Status;

import enums.LocatorTypes;
import managers.PageManager;
import pageclasses.BasePage;
import pageobjects.EncountersObjRepo;

public class EncounterDetailsPage extends BasePage {

	PageManager pm = new PageManager();
	
	public ArrayList<String> addClaimType() {
		ArrayList<String> claimTypeList = new ArrayList<String>();
		try {
			claimTypeList.add("Professional");
			claimTypeList.add("Institutional");
			claimTypeList.add("DME");
		} catch(Exception e) {
			e.printStackTrace();
		}
		return claimTypeList;
	}
	
	public int removeClaimType(String claimType, ArrayList<String> claimTypeList) {
		int arraySize = claimTypeList.size();
		try {
			if(arraySize > 0) {
				claimTypeList.remove(claimType);
			}
			arraySize = claimTypeList.size();
		} catch(Exception e) {
			e.printStackTrace();
		}
		return arraySize;
	}
	
	public boolean searchClaimNumber(String claimNumber, String claimType, String submitterID) {
		boolean flag = false;
		boolean subscriberButtonExists = false;
		ArrayList<String> claimTypeList;
		int claimTypeSize = 0;
		int arraySize;
		try {
			claimTypeList = addClaimType();
			arraySize = claimTypeList.size();
			for(int index = 0; index < arraySize; index++) {
				getElement(LocatorTypes.xpath, EncountersObjRepo.EncounterDetails.submitterIdDropdown_xpath).click();
				getElement(LocatorTypes.xpath, EncountersObjRepo.EncounterDetails.submitterIdTextField_xpath).sendKeys(submitterID);
				getElement(LocatorTypes.xpath, EncountersObjRepo.EncounterDetails.submitterIdTextField_xpath).sendKeys(Keys.ENTER);
				
				getElement(LocatorTypes.xpath, EncountersObjRepo.EncounterDetails.claimTypeDropdown_xpath).click();
				getElement(LocatorTypes.className, EncountersObjRepo.EncounterDetails.claimTypeTextField_class).sendKeys(claimType);
				getElement(LocatorTypes.className, EncountersObjRepo.EncounterDetails.claimTypeTextField_class).sendKeys(Keys.ENTER);
				
				getElement(LocatorTypes.name, EncountersObjRepo.EncounterDetails.claimNumberTextField_name).clear();
				getElement(LocatorTypes.name, EncountersObjRepo.EncounterDetails.claimNumberTextField_name).sendKeys(claimNumber);
				getElement(LocatorTypes.id, EncountersObjRepo.EncounterDetails.goButton_id).click();
				subscriberButtonExists = isElementPresent(LocatorTypes.id, EncountersObjRepo.EncounterDetails.subscriberExpandButton_id);
				
				if(subscriberButtonExists) {
					flag = true;
					if(index > 0) {
						test.log(Status.WARNING, "Discrepancy for Claim Type between file and UI on Claim Number "
								+  claimNumber + " - UI Claim Type: " + claimType);
					}
					if(claimTypeList.size() > 0) {
						claimTypeList.clear();
					}
					break;
				} else {
					flag = false;
					claimTypeSize = removeClaimType(claimType, claimTypeList);
					if (claimTypeSize > 0) {
						claimType = claimTypeList.get(0);
					} else {
						break;
					}
				}
			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			try {
				reportFail("Claim number search in Encounter Details page has failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}
	
	public int selectClaimLineTableList(int nodeCount) {
		int listSize = 0;
		try {
			List<WebElement> claimLineList = getElements(LocatorTypes.xpath, EncountersObjRepo.EncounterDetails.claimLineTable_xpath);
			listSize = claimLineList.size();
			claimLineList.get(nodeCount).click();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Encounters Page unable to click on claim line item, in the table.");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return listSize;
	}
	
	public String claimLineTableRow(int nodeCount) {
		String claimLineRow = "";
		try {
			List<WebElement> claimLineList = getElements(LocatorTypes.xpath,
					EncountersObjRepo.EncounterDetails.claimLineTable_xpath);
			claimLineRow = claimLineList.get(nodeCount).getText();
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Encounters Page unable to retrieve row data on claim line item, in the table.");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return claimLineRow;
	}
	
	public HashMap<String, String> getEncounterDetailsData(){
		HashMap<String, String> encounterData = new HashMap<String, String>();
		try {
			String diagPtr1 = "";
			String diagPtr2 = "";
			String diagPtr3 = "";
			String diagPtr4 = "";
			String subscriberName = "";
			String medicareID = "";
			String taxID = "";
			String subscriberDiv = "";
			String totalClaimChargeAmount = "";
			String placeOfService = "";
			String claimFreqTypeCode = "";
			String processStatus = "";
			String npi = "";
			double chargeAmount;
			List<String> diagPtrAll = new LinkedList<String>();
			List<String> diagnosisCodes = new LinkedList<String>();
			String claimLineRow = "";
			String claimStatus = getElement(LocatorTypes.xpath,
					EncountersObjRepo.EncounterDetails.statusTableRowData_xpath).getText();
			String plusMinusSubscriberSrc = getElement(LocatorTypes.id,
					EncountersObjRepo.EncounterDetails.subscriberExpandButton_id).getAttribute("src");
			if (plusMinusSubscriberSrc.contains("Plus")) {
				getElement(LocatorTypes.id, EncountersObjRepo.EncounterDetails.subscriberExpandButton_id).click();
			}
			subscriberDiv = getElement(LocatorTypes.id, EncountersObjRepo.EncounterDetails.subscriber_div_id)
					.getText();
			if (subscriberDiv.trim().equalsIgnoreCase("NO DATA")) {
				subscriberName = "";
				medicareID = "";
			} else {
				subscriberName = getElement(LocatorTypes.xpath,
						EncountersObjRepo.EncounterDetails.subscriber_name_xpath).getText();
				medicareID = getElement(LocatorTypes.xpath,
						EncountersObjRepo.EncounterDetails.subscriber_medicareID_xpath).getText();
			}

			String plusMinusClaimSrc = getElement(LocatorTypes.id,
					EncountersObjRepo.EncounterDetails.claimExpandButton_id).getAttribute("src");
			if (plusMinusClaimSrc.contains("Plus")) {
				getElement(LocatorTypes.id, EncountersObjRepo.EncounterDetails.claimExpandButton_id).click();
			}
			totalClaimChargeAmount = getElement(LocatorTypes.xpath,
					EncountersObjRepo.EncounterDetails.claim_totalClaimChargeAmount_xpath).getText();
			if (totalClaimChargeAmount.contains("$")) {
				totalClaimChargeAmount = totalClaimChargeAmount.replace("$", "");
			}
			chargeAmount = Double.parseDouble(totalClaimChargeAmount);
			totalClaimChargeAmount = Double.toString(chargeAmount);
			placeOfService = getElement(LocatorTypes.xpath,
					EncountersObjRepo.EncounterDetails.claim_placeOfService_xpath).getText();
			claimFreqTypeCode = getElement(LocatorTypes.xpath,
					EncountersObjRepo.EncounterDetails.claim_claimFreqTypeCode_xpath).getText();
			processStatus = getElement(LocatorTypes.xpath,
					EncountersObjRepo.EncounterDetails.claim_processStatus_xpath).getText();
			String plusMinusProviderSrc = getElement(LocatorTypes.id,
					EncountersObjRepo.EncounterDetails.providerExpandButton_id).getAttribute("src");
			if (plusMinusProviderSrc.contains("Plus")) {
				getElement(LocatorTypes.id, EncountersObjRepo.EncounterDetails.providerExpandButton_id).click();
			}

			boolean npiExists = isElementPresent(LocatorTypes.xpath,
					EncountersObjRepo.EncounterDetails.provider_npi_xpath);
			if (npiExists) {
				npi = getElement(LocatorTypes.xpath, 
						EncountersObjRepo.EncounterDetails.provider_npi_xpath).getText();
				taxID = getElement(LocatorTypes.xpath, 
						EncountersObjRepo.EncounterDetails.provider_affilTaxID_xpath).getText();
			} else {
				npi = "";
				taxID = "";
			}

			boolean claimCodeButtonExists = isElementPresent(LocatorTypes.xpath,
					EncountersObjRepo.EncounterDetails.claimCodesExpandButton_xpath);
			if (claimCodeButtonExists) {
				String plusMinusClaimCodesSrc = getElement(LocatorTypes.xpath,
						EncountersObjRepo.EncounterDetails.claimCodesExpandButton_xpath).getAttribute("src");
				if (plusMinusClaimCodesSrc.contains("Plus")) {
					getElement(LocatorTypes.xpath, EncountersObjRepo.EncounterDetails.claimCodesExpandButton_xpath)
							.click();
					wait(1);
					getElement(LocatorTypes.id, EncountersObjRepo.EncounterDetails.diagnosisCodesExpandButton_id)
							.click();
				} else {
					getElement(LocatorTypes.id, EncountersObjRepo.EncounterDetails.diagnosisCodesExpandButton_id)
							.click();
				}

				List<WebElement> diagnosesCodesList = getElements(LocatorTypes.xpath,
						EncountersObjRepo.EncounterDetails.diagnosisCodeRow_xpath);

				String formatDiagCodes = "";
				String[] formatDiagCodesArray;

				for (int i = 0; i < diagnosesCodesList.size(); i++) {
					formatDiagCodes = diagnosesCodesList.get(i).getText().trim();
					formatDiagCodesArray = formatDiagCodes.split("   ");
					formatDiagCodes = "(" + formatDiagCodesArray[0] + ", " + formatDiagCodesArray[1] + ")";
					diagnosisCodes.add(formatDiagCodes);
				}
			}

			String plusMinusClaimLineSrc = getElement(LocatorTypes.id,
					EncountersObjRepo.EncounterDetails.claimLineExpandButton_id).getAttribute("src");
			if (plusMinusClaimLineSrc.contains("Plus")) {
				getElement(LocatorTypes.id, EncountersObjRepo.EncounterDetails.claimLineExpandButton_id).click();
			}
			int listSize = 0;
			listSize = selectClaimLineTableList(0);
			for (int indexCount = 0; indexCount < listSize; indexCount++) {
				selectClaimLineTableList(indexCount);
				claimLineRow = claimLineTableRow(indexCount);
				logger.info(claimLineRow);
				diagPtr1 = getElement(LocatorTypes.id,
						EncountersObjRepo.EncounterDetails.claimLineDetailDiagcdPtr1_id).getText().trim();
				diagPtr2 = getElement(LocatorTypes.id,
						EncountersObjRepo.EncounterDetails.claimLineDetailDiagcdPtr2_id).getText().trim();
				diagPtr3 = getElement(LocatorTypes.id,
						EncountersObjRepo.EncounterDetails.claimLineDetailDiagcdPtr3_id).getText().trim();
				diagPtr4 = getElement(LocatorTypes.id,
						EncountersObjRepo.EncounterDetails.claimLineDetailDiagcdPtr4_id).getText().trim();

				if (!diagPtr4.equals("")) {
					diagPtrAll.add(diagPtr1 + ":" + diagPtr2 + ":" + diagPtr3 + ":" + diagPtr4);
				} else if (diagPtr4.equals("") && !diagPtr3.equals("")) {
					diagPtrAll.add(diagPtr1 + ":" + diagPtr2 + ":" + diagPtr3);
				} else if (diagPtr4.equals("") && diagPtr3.equals("") && !diagPtr2.equals("")) {
					diagPtrAll.add(diagPtr1 + ":" + diagPtr2);
				} else if (diagPtr4.equals("") && diagPtr3.equals("") && diagPtr2.equals("") && !diagPtr1.equals("")) {
					diagPtrAll.add(diagPtr1);
				} else {
					diagPtrAll.add("");
				}
			}
			encounterData.put("SubscriberName", subscriberName);
			// encounterData.put("MedicareID", medicareID);
			encounterData.put("claimStatus", claimStatus.trim());
			encounterData.put("processStatus", processStatus);
			encounterData.put("TotalClaimChargeAmt", totalClaimChargeAmount);
			encounterData.put("NPI", npi);
			encounterData.put("TaxID", taxID);
			encounterData.put("PlaceOfService", placeOfService);
			encounterData.put("ClaimFreqTypeCode", claimFreqTypeCode);
			encounterData.put("DiagnosisCodes", diagnosisCodes.toString());
			encounterData.put("DiagnosisPointers", diagPtrAll.toString());
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Attempted retrieval of encounter details data has failed.");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return encounterData;
	}
	
	public boolean validateEncounterDetails(HashMap<String, String> encounterDetailsFromFile) {
		boolean flag = false;
		int counter = 0;
		try {
			HashMap<String, String> encounterDetailsFromUI = getEncounterDetailsData();
			if(!encounterDetailsFromUI.get("claimStatus").trim().equalsIgnoreCase(encounterDetailsFromUI.get("processStatus"))) {
				flag = false;
				counter++;
				test.log(Status.FAIL, "The Claim Status and Process Status are NOT EQUAL: " 
						+ encounterDetailsFromUI.get("claimStatus").trim().toUpperCase() + " | " 
						+ encounterDetailsFromUI.get("processStatus").trim().toUpperCase());
			}
			logger.info("File - " + encounterDetailsFromFile);
			logger.info("UI   - " + encounterDetailsFromUI);
			encounterDetailsFromUI.remove("claimStatus");
			encounterDetailsFromUI.remove("processStatus");
			flag = compareHashMaps(encounterDetailsFromUI, encounterDetailsFromFile);
			if(flag == false || counter > 0) {
				flag = false;
			} else {
				flag = true;
			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			try {
				reportFail("Validation process of Encounters failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}
}
