package pageclasses.hrp;

import java.io.IOException;
import java.util.HashMap;

import org.openqa.selenium.By;
import org.openqa.selenium.Keys;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

import com.aventstack.extentreports.Status;

import enums.LocatorTypes;
import io.appium.java_client.windows.WindowsDriver;
import managers.PageManager;
import pageclasses.BasePage;
import pageobjects.HRPObjRepo;
import utils.Dbconn;

/**
 * @author tneha01 TestCase : Validate Claims
 */
public class HRPClaimsPage extends BasePage {

	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(HRPClaimsPage.class.getName());
	Dbconn db = new Dbconn();
	PageManager pm = new PageManager();

	public HRPClaimsPage(WindowsDriver<WebElement> winDriver) {

	}

	public void navigateToClaimsSearch() {
		try {
			WebDriverWait wait = new WebDriverWait(winDriver, 10);
			wait.until(ExpectedConditions.elementToBeClickable(By.name("Claims")));
			getWinElement(LocatorTypes.name, HRPObjRepo.HRPDockLeft.claims_name).click();
			getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.claimsSearch_id).click();

			wait(2);
			logger.info("claim search window open");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("failure to open claim search page");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void searchClaim(String claimNumber) {
		try {

			getWinElement(LocatorTypes.name, HRPObjRepo.HRPClaims.advanced_name).click();
			getWinElement(LocatorTypes.name, HRPObjRepo.HRPClaims.claimNumber_name).sendKeys(claimNumber + Keys.ENTER);

			Actions action = new Actions(winDriver);
			action.doubleClick(getWinElement(LocatorTypes.name, HRPObjRepo.HRPClaims.claimsResult_name)).build()
					.perform();
			wait(2);
			logger.info("claim search results open");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("No results found");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public HashMap<String, String> claimHeadrDetails() {
		HashMap<String, String> headerDeaitls = new HashMap<String, String>();
		try {
			headerDeaitls.put("PatientNumber", getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.patientAccountNum_id).getText().trim().replaceFirst("^0+(?!$)", ""));
			headerDeaitls.put("MedicareID", getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.submittedSubscriberID_id).getText().trim());
			headerDeaitls.put("ClaimNumber", getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.externalClaimID_id).getText().trim());
//			headerDeaitls.put("SubmitterNM1IdentificationCode", getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.submitterID_id).getText().trim());
			headerDeaitls.put("SubmitterNM1NameLastOrganizationName", getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.submitterName_id).getText().trim());
			
			headerDeaitls.put("memberName", getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.memberName_id).getText().trim());
			headerDeaitls.put("memberID", getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.memberID_id).getText().trim());
			headerDeaitls.put("memberBirthDate", getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.memberBirthDate_id).getText().trim());
			headerDeaitls.put("memberGender", getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.memberGender_id).getText().trim());
			headerDeaitls.put("relationship", getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.relationship_id).getText().trim());
			headerDeaitls.put("IDPrefix", getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.IDPrefix_id).getText().trim());
			
			getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.openSubscriberDropdown_id).click();
			headerDeaitls.put("subscriberName", getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.subscriberName_id).getText().trim());
			headerDeaitls.put("subscriberID", getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.subscriberID_id).getText().trim());
			headerDeaitls.put("subscriberIDPrefix", getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.subscriberIDPrefix_id).getText().trim());
			headerDeaitls.put("address", getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.address_id).getText().trim());
			headerDeaitls.put("subscriberBirthDate", getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.subscriberBirthDate_id).getText().trim());
			headerDeaitls.put("subscriberGender", getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.subscriberGender_id).getText().trim());
			headerDeaitls.put("subscriberCity", getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.subscriberCity_id).getText().trim());
			headerDeaitls.put("subscriberState", getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.subscriberState_id).getText().trim());
			headerDeaitls.put("subscriberZipCode", getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.subscriberZipCode_id).getText().trim());
			headerDeaitls.put("subscriberGroupNumber", getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.subscriberGroupNumber_id).getText().trim());
			
			headerDeaitls.put("providerSupplierName", getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.providerSupplierName_id).getText().trim());
			headerDeaitls.put("providerSupplierID", getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.providerSupplierID_id).getText().trim());
			
			getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.openSupplierDropdown_id).click();
			headerDeaitls.put("supplierName", getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.supplierName_id).getText().trim());
			headerDeaitls.put("supplierNPI", getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.supplierNPI_id).getText().trim());
			headerDeaitls.put("supplierID", getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.supplierID_id).getText().trim());
			headerDeaitls.put("supplierTaxID", getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.supplierTaxID_id).getText().trim());
			headerDeaitls.put("supplierAddress", getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.supplierAddress_id).getText().trim());
			headerDeaitls.put("supplierCity", getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.supplierCity_id).getText().trim());
			headerDeaitls.put("supplierZipCode", getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.supplierZipCode_id).getText().trim());
			headerDeaitls.put("supplierCountry", getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.supplierCountry_id).getText().trim());
			
			headerDeaitls.put("physicianName", getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.physicianName_id).getText().trim());
			headerDeaitls.put("physicianID", getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.physicianID_id).getText().trim());
			
			headerDeaitls.put("locationName", getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.locationName_id).getText().trim());
			headerDeaitls.put("locationID", getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.locationID_id).getText().trim());
			
			getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.openRenderingServiceAddress_id).click();
	
			headerDeaitls.put("renderingFacilityAddress", getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.renderingFacilityAddress_id).getText().trim());
			headerDeaitls.put("renderingFacilityCity", getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.renderingFacilityCity_id).getText().trim());
			headerDeaitls.put("renderingFacilityZipCode", getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.renderingFacilityZipCode_id).getText().trim());
			headerDeaitls.put("renderingFacilityCountry", getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.renderingFacilityCountry_id).getText().trim());
			
			headerDeaitls.put("submittedCarges", getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.submittedCarges_id).getText().trim());
			headerDeaitls.put("billedAmount", getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.billedAmount_id).getText().trim());
			headerDeaitls.put("allowedAmount", getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.allowedAmount_id).getText().trim());
			headerDeaitls.put("totalDeductible", getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.totalDeductible_id).getText().trim());
			headerDeaitls.put("coPayment", getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.coPayment_id).getText().trim());
			headerDeaitls.put("coInsurance", getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.coInsurance_id).getText().trim());
			headerDeaitls.put("memberPaidAmount", getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.memberPaidAmount_id).getText().trim());
			headerDeaitls.put("nonCoveredAmount", getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.nonCoveredAmount_id).getText().trim());
			headerDeaitls.put("memberAmount", getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.memberAmount_id).getText().trim());
			headerDeaitls.put("memberPaidToProvider", getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.memberPaidToProvider_id).getText().trim());
			
			headerDeaitls.put("primaryDiagnosisCode", getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.primaryDiagnosisCode_id).getText().trim());
			headerDeaitls.put("primaryDiagnosisDescription", getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.primaryDiagnosisDescription_id).getText().trim());
			headerDeaitls.put("primaryDiagnosisCodetype", getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.primaryDiagnosisCodetype_id).getText().trim());
			
			headerDeaitls.put("releaseSignature", getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.releaseSignature_id).getText().trim());
			headerDeaitls.put("assignmentAccepted", getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.assignmentAccepted_id).getText().trim());
			headerDeaitls.put("benefitsAssigned", getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.benefitsAssigned_id).getText().trim());
		}catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Retrieval of Header tab details failed");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}	
		
		return headerDeaitls;
	}

	public HashMap<String, String> getLinesData() {

		HashMap<String, String> dataUI = new HashMap<String, String>();
		String fromDate = getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.fromDate_id).getText().trim();
		String toDate = getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.toDate_id).getText().trim();
		String pos = getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.pos_id).getText().trim();
		String emg = getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.checkboxEMGStatus_id).getText().trim();
		String cpt = getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.cpt_id).getText().trim();
		String modifierOne = getWinElement(LocatorTypes.xpath, HRPObjRepo.HRPClaims.modifier1_xpath).getText().trim();
		String modifierTwo = getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.modifierCode2_id).getText().trim();
		String modifierThree = getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.modifierCode3_id).getText().trim();
		String modifierFour = getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.modifierCode4_id).getText().trim();
		String modifierFive = getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.modifierCode5_id).getText().trim();
		String charges = getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.charges_id).getText().trim();
		String uos = getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.uos_id).getText().trim();
		String minutes = getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.minutes_id).getText().trim();
		String epsdt = getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.checkboxEPSDT_id).getText().trim();
		String allowedAmt = getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.allowedAmt_id).getText().trim();
		String paidAmt = getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.paidAmt_id).getText().trim();
		String nonCovered = getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.nonCovered_id).getText().trim();
		String memberResponsibilty = getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.memberResponsibility_id)
				.getText().trim();
		String deductible = getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.deductible_id).getText().trim();
		String copayment = getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.copayment_id).getText().trim();
		String coinsurance = getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.coinsurance_id).getText().trim();
		String balanceBilled = getWinElement(LocatorTypes.id, HRPObjRepo.HRPClaims.balanceBilled_id).getText().trim();
		dataUI.put("fromDate", fromDate);
		dataUI.put("toDate", toDate);
		dataUI.put("pos", pos);
		dataUI.put("emg", emg);
		dataUI.put("cpt", cpt);
		dataUI.put("modifier1", modifierOne);
		dataUI.put("modifier2", modifierTwo);
		dataUI.put("modifier3", modifierThree);
		dataUI.put("modifier4", modifierFour);
		dataUI.put("modifier5", modifierFive);
		dataUI.put("charges", charges);
		dataUI.put("uos", uos);
		dataUI.put("minutes", minutes);
		dataUI.put("epsdt", epsdt);
		dataUI.put("allowedAmt", allowedAmt);
		dataUI.put("paidAmt", paidAmt);
		dataUI.put("nonCovered", nonCovered);
		dataUI.put("memberResponsibilty", memberResponsibilty);
		dataUI.put("deductible", deductible);
		dataUI.put("copayment", copayment);
		dataUI.put("coinsurance", coinsurance);
		dataUI.put("balanceBilled", balanceBilled);
		System.out.println(dataUI);
		return dataUI;

	}

	public boolean validateLinesTab() {
		boolean flag = true;
		try {
				HashMap<String, String> dataUI = getLinesData();
				String fromDateDB = testData.get("DateTimeFromPeriod").trim();
				String toDateDB = testData.get("DateTimeToPeriod").trim();
				String cptCode = testData.get("CPTCODEID").trim();
				String quantity = testData.get("QUANTITY").trim();
				String unitOfQuantity = testData.get("UnitOrBasisforMeasurementCode").trim();

				// validate fromDate
				fromDateDB = fromDateDB.substring(0, 2) + "/" + fromDateDB.substring(2, 4) + "/"
						+ fromDateDB.substring(4, 8);
				System.out.println("fromdateDB-" + fromDateDB);
				if (!compareDatesInDifferentFormat(dataUI.get("fromDate"), fromDateDB).equalsIgnoreCase("0")) {
					test.log(Status.FAIL, "fromDate in DB:" + fromDateDB + " is not equal to fromDate on UI: "
							+ dataUI.get("fromDate"));
					flag = false;
				}

				// if toDate is not present in DB then todate is same as from date
				if (toDateDB == "" || toDateDB == null) {
					toDateDB = fromDateDB;
				} else {
					toDateDB = toDateDB.substring(0, 2) + "/" + toDateDB.substring(2, 4) + "/"
							+ toDateDB.substring(4, 8);
					System.out.println("toDateDB-" + toDateDB);
				}
				// validate todate
				if (!compareDatesInDifferentFormat(dataUI.get("toDate"), toDateDB).equalsIgnoreCase("0")) {
					test.log(Status.FAIL,
							"toDate in DB: " + toDateDB + " is not equal to todate on UI: " + dataUI.get("toDate"));
					flag = false;
				}

				String[] modifierCodes = cptCode.split(":");
				cptCode = modifierCodes[0];
				// validate CPT code
				if (!cptCode.trim().equalsIgnoreCase(dataUI.get("cpt").trim())) {
					test.log(Status.FAIL,
							"CPT Code in DB : " + cptCode + " is not equal to CPT code on UI:" + dataUI.get("cpt"));
					flag = false;
				}

				// validate all modifier codes
				for (int j = 1; j < modifierCodes.length; j++) {
					if (!modifierCodes[j].trim().equalsIgnoreCase(dataUI.get("modifier" + j).trim())) {
						test.log(Status.FAIL,
								"Modifier code[" + j + "] in DB:" + modifierCodes[j].trim()
										+ " is not equal to Modifier code[" + j + "] on UI:"
										+ dataUI.get("modifier" + j).trim());
						flag = false;
					}
				}
				// validate charges
				String chargesUI = dataUI.get("charges");
				chargesUI = chargesUI.substring(1, chargesUI.length());
				System.out.println("chargesUI" + chargesUI);
				if (chargesUI.equalsIgnoreCase(testData.get("Amount"))) {
					test.log(Status.FAIL, "Quantity in Minutes in DB: " + quantity
							+ " does not match with Quanity on UI:" + dataUI.get("minutes"));
					flag = false;
				}

				// validate Quantity in Minutes or Units
				if (unitOfQuantity.equalsIgnoreCase("MJ")) {
					if (!quantity.equalsIgnoreCase(dataUI.get("minutes"))) {
						test.log(Status.FAIL, "Quantity in Minutes in DB: " + quantity
								+ " does not match with Quanity on UI:" + dataUI.get("minutes"));
						flag = false;
					}
				} else if (unitOfQuantity.equalsIgnoreCase("UN")) {
					if (!quantity.equalsIgnoreCase(dataUI.get("uos"))) {
						test.log(Status.FAIL, "Quantity in Unit in DB: " + quantity
								+ " does not match with Quanity on UI:" + dataUI.get("units"));
						flag = false;
					}
				}

			if (flag == true) {
				reportPass("Validation of Lines tab is Passed for Claim Line Number: " + testData.get("ClaimLineNumber"));
			}else {
				reportFail("Validation of Lines tab is Failed for Claim Line Number: " + testData.get("ClaimLineNumber"));
			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			try {
				reportFail("Validation of Lines tab is Failed for Claim Line Number: " + testData.get("ClaimLineNumber"));
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}

}
