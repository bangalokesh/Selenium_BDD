package x12.claims;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import com.aventstack.extentreports.Status;

import pageclasses.BasePage;
import utils.Dbconn;

public class Claim835FileValidation extends BasePage{
	static Dbconn db = new Dbconn();
	
	public void claimFileValidation() throws IOException {
		FileParser835 e = new FileParser835();
		List<Structure> data = e.get835FileData(new File("835 wPR Amt_Copay.txt"));
		System.out.println(data.toString());
		for(Structure s : data) {
			Map<String, String> transaction = s.getClaim();
			List<Map<String, String>> claims = s.getLXClm();
			  
			HashMap<String, String> transactionFileHashMap = new HashMap<String, String>();
			transactionFileHashMap.put("PatientNumber", transaction.get("CLP01"));
			transactionFileHashMap.put("Status", transaction.get("CLP02"));
			transactionFileHashMap.put("PatientResponsibilityAmount", transaction.get("CLP05"));
			transactionFileHashMap.put("ClaimPaymentAmount", transaction.get("CLP03"));
			transactionFileHashMap.put("ClaimNumber", transaction.get("CLP07"));
			transactionFileHashMap.put("MedicareID", transaction.get("NM1QC09"));
			transactionFileHashMap.put("LastName", transaction.get("NM1QC03"));
			transactionFileHashMap.put("FirstName", transaction.get("NM1QC04"));
			transactionFileHashMap.put("subscriberLastName", transaction.get("NM1IL03"));
			transactionFileHashMap.put("subscriberFirstName", transaction.get("NM1IL04"));
			transactionFileHashMap.put("subscriberMedicareID", transaction.get("NM1QC09"));
			
			String query = "SELECT rc.ID, rc.ClaimNumber, rc.PatientNumber, rc.MedicareID, rc.TestCaseID, rc.Status, rc.ClaimPaymentAmount, rc.PatientResponsibilityAmount, md.LastName, md.FirstName\r\n" + 
					"FROM test_data_readytoclaims rc JOIN member_demographic md\r\n" + 
					 "ON rc.MedicareID = md.MedicareID WHERE ClaimNumber = '" + transactionFileHashMap.get("ClaimNumber") + "' AND Validation835ClaimStatus = 'NULL';";
					//"ON rc.MedicareID = md.MedicareID WHERE ClaimNumber = '" + transactionFileHashMap.get("ClaimNumber") + "';";
			
			HashMap<String, String> transactionDBValue = db.getResultSet(query);
			if (transactionDBValue.size() < 1) {
				return;
			}
			
			test = extent.createTest("HRP 835 Claims File Validation for Claim Number: " + transaction.get("CLP07"));
			HashMap<String, String> transactionDBHashMap = new HashMap<>();
			String testCaseID = "";
			int ID = -1;
			transactionDBHashMap.put("PatientResponsibilityAmount", transactionDBValue.get("PatientResponsibilityAmount"));
			transactionDBHashMap.put("ClaimPaymentAmount", transactionDBValue.get("ClaimPaymentAmount"));
			transactionDBHashMap.put("ClaimNumber", transactionDBValue.get("ClaimNumber"));
			transactionDBHashMap.put("PatientNumber", transactionDBValue.get("PatientNumber"));
			transactionDBHashMap.put("MedicareID", transactionDBValue.get("MedicareID"));
			transactionDBHashMap.put("Status", transactionDBValue.get("Status"));
			transactionDBHashMap.put("LastName", transactionDBValue.get("LastName"));
			transactionDBHashMap.put("FirstName", transactionDBValue.get("FirstName"));
			transactionDBHashMap.put("subscriberLastName", transactionDBValue.get("LastName"));
			transactionDBHashMap.put("subscriberFirstName", transactionDBValue.get("FirstName"));
			transactionDBHashMap.put("subscriberMedicareID", transactionDBValue.get("MedicareID"));
			
			testCaseID = transactionDBValue.get("TestCaseID");
			ID = Integer.parseInt(transactionDBValue.get("ID"));
			
			if (compareHashMaps(transactionDBHashMap, transactionFileHashMap)) {
				test.log(Status.PASS, "835 claim level data passed : " + transactionFileHashMap);
				reportPass("835 File validation passed: " + transactionFileHashMap);
				db.updateDBTestData(
						"UPDATE [VelocityTestAutomation].[dbo].[test_data_readytoclaims]\r\n" + "SET Validation835ClaimStatus = 'PASSED' \r\n"
								+ "WHERE \r\n" + "ID = " + ID);
			}else {
				test.log(Status.FAIL, "835 claim level data Not Matched: <br>- Database = " + transactionDBHashMap + "<br>File = " + transactionFileHashMap);
				reportFail("835 File validation Not Matched: <br>- Database = " + transactionDBHashMap + "<br>File = " + transactionFileHashMap);
								
				db.updateDBTestData(
						"UPDATE [VelocityTestAutomation].[dbo].[test_data_readytoclaims]\r\n" + "SET Validation835ClaimStatus = 'FAILED' \r\n"
								+ "WHERE \r\n" + "ID = " + ID);
			}
			
			claimLineValidation(claims, testCaseID);
			
			flushTest();
		}
	}

	private void claimLineValidation(List<Map<String, String>> claims, String testCaseID) throws IOException {
		for(Map<String, String> claim: claims) {
			HashMap<String, String> claimLineFileHashMap = new HashMap<>();
			String CPTCode[]= claim.get("SVC01").split("\\{");
			claimLineFileHashMap.put("CPTCodeID", CPTCode[1]);
			claimLineFileHashMap.put("ClaimLineNumber", claim.get("REF02"));
			claimLineFileHashMap.put("ClaimLinePaymentAmount", claim.get("CASPR03"));
			
			String claimLineQuery = "SELECT \r\n" + 
					"  cl.TestCaseID, cl.CPTCodeID, cl.Amount, cl.Quantity, cl.ClaimLineNumber\r\n" + 
					"FROM \r\n" + 
					"  test_data_claimline cl\r\n" + 
					"WHERE \r\n" + 					
					"  cl.TestCaseID = '" + testCaseID + "' and cl.ClaimLineNumber = '" + claim.get("REF02") + "' ;";
			HashMap<String, String> claimLineDBValue = db.getResultSet(claimLineQuery);
			if (claimLineDBValue.size() < 1) {
				continue;
			}
			
			HashMap<String, String> claimLineDBHashMap = new HashMap<>();
			claimLineDBHashMap.put("CPTCodeID", claimLineDBValue.get("CPTCodeID"));
			claimLineDBHashMap.put("ClaimLineNumber", claimLineDBValue.get("ClaimLineNumber"));
			claimLineDBHashMap.put("ClaimLinePaymentAmount", claimLineDBValue.get("Amount"));
			
			if (compareHashMaps(claimLineDBHashMap, claimLineFileHashMap)) {
				test.log(Status.PASS, "835 claim line level data passed: " + claimLineFileHashMap);
				reportPass("835 claim line level data passed: " + claimLineFileHashMap);
			}else {
				test.log(Status.FAIL, "835 claim line level data Not Matched: <br>- Database = " + claimLineDBHashMap + "<br>File = " + claimLineFileHashMap);
				reportFail("835 claim line level data Not Matched: <br>- Database = " + claimLineDBHashMap + "<br>File = " + claimLineFileHashMap);
			}
		}
	}

	@Test
	public void Test() throws IOException {
		claimFileValidation();
	}
}
