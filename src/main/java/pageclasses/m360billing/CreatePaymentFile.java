package pageclasses.m360billing;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;

import pageclasses.BasePage;
import pageclasses.CommonMethods;
import utils.Const;
import utils.Dbconn;

public class CreatePaymentFile extends BasePage {
	static Dbconn db = new Dbconn();
	String paymentType = "LEC";

	private Map<String, String> getWF_FileHeader(String type) {
		String header = "";
		String recordType = "H";
		String custAccountNumber = "HCF0241";
		char paymentSource;
		String fileDate = "";
		String fileBatchID = "";
		String bankID = "";

		paymentSource = paymentType.charAt(new Random().nextInt(3));

		String currentDate = CommonMethods.getCurrentDate();
		fileDate = currentDate.replace("-", "");
		bankID = type.equals("Medisun") ? "WF01" : "WF02";

		String fullDate = CommonMethods.getCurrentTimeStampVal().toString();
		String datePart = fullDate.substring(0, 4);
		String timePart = fullDate.substring(11, 18).replace(":", "");
		fileBatchID = Character.toString(paymentSource) + datePart + timePart;
		header = recordType + custAccountNumber + paymentSource + fileDate + fileBatchID + bankID;

		Map<String, String> retVal = new HashMap<String, String>();
		retVal.put("Header", header);
		retVal.put("PaymentSource", "" + paymentSource);
		retVal.put("FileDate", currentDate);
		return retVal;
	}

	private String getWF_FileTrailer(String totalPayment, int totalRecords) {

		String trailer = "";
		String recordType = "T";
		String custAccountNumber = "HCF0241";
		String fileDate = "";
		String fileRecordCount = new DecimalFormat("000000000").format(totalRecords);

		fileDate = CommonMethods.getCurrentDate().replace("-", "");
		trailer = recordType + custAccountNumber + fileDate + totalPayment + fileRecordCount;
		return trailer;
	}

	private List<String> createDetailRecord(String type) {
		String query = "select dbo.test_data_readytobill.ID as ID, SupplementalID, InvoiceDueDate, InvoiceNumber, PaymentAmount, InvoiceAmount from ((dbo.test_data_readytoenroll join dbo.test_data_readytobill "
				+ "on dbo.test_data_readytoenroll.MedCareID = dbo.test_data_readytobill.MedicareID) "
				+ "join dbo.product_details on ((dbo.test_data_readytoenroll.ProductPlanID = dbo.product_details.PlanID) and "
				+ "(dbo.test_data_readytoenroll.ProductPBPID = dbo.product_details.PBPID) and "
				+ "(dbo.test_data_readytoenroll.ProductSegmentID = dbo.product_details.PBPSegmentID))) where InvoiceStatus = 'Open' and GroupID = '"
				+ type + "'";

		List<String> detailRecord = new ArrayList<String>();
		try {
			List<HashMap<String, String>> data = db.getListOfHashMapsFromResultSet(query);
			Map<String, String> myHeader = getWF_FileHeader(type);
			detailRecord.add(myHeader.get("Header"));
			String paymentSource = myHeader.get("PaymentSource");
			String line;
			double totalPayment = 0;

			for (HashMap<String, String> record : data) {
				line = "";
				line += "D";
				line += fixSpaces("", 15);
				line += fixSpaces(record.get("SupplementalID").trim(), 15);
				line += fixSpaces("", 8);
				line += fixSpaces(record.get("InvoiceDueDate").trim().replaceAll("-", ""), 8);
				line += fixSpaces(record.get("InvoiceNumber").trim(), 12);
				double finalPayment;
				if (record.get("PaymentAmount") == null || record.get("PaymentAmount").equals("0.00")) {
					line += new DecimalFormat("0000000.00").format(Double.parseDouble(record.get("InvoiceAmount")));
					totalPayment += Double.parseDouble(record.get("InvoiceAmount"));
					finalPayment = Double.parseDouble(record.get("InvoiceAmount"));
				} else {
					line += new DecimalFormat("0000000.00").format(Double.parseDouble(record.get("PaymentAmount")));
					totalPayment += Double.parseDouble(record.get("PaymentAmount"));
					finalPayment = Double.parseDouble(record.get("PaymentAmount"));
				}

				String updateQuery = "UPDATE VelocityTestAutomation.dbo.test_data_readytobill SET PaymentSource = '"
						+ paymentSource + "'," + " PaymentAmount = '" + finalPayment + "', CheckDate = '"
						+ myHeader.get("FileDate") + "'," + " CheckNumber = '" + record.get("InvoiceNumber")
						+ "', PayFileGenerated = 'Y' WHERE ID = '" + record.get("ID") + "'";
				db.updateDBTestData(updateQuery);
				detailRecord.add(line);
			}

			detailRecord
					.add(getWF_FileTrailer(new DecimalFormat("00000000000.00").format(totalPayment), data.size() + 2));
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Test Failed in CreateBillFromTable createDetailRecord method");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return detailRecord;
	}

	public void createPaymentFile() throws IOException {
		String paymentFilePath = Const.PaymentFilePath + "paymentFile_" + getRunName() + ".txt"; 
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(paymentFilePath)));
		List<String> records = createDetailRecord("Medisun");

		for (String record : records) {
			writer.append(fixSpaces(record, 79));
			writer.append("\n");
		}

		records = createDetailRecord("BCBSAZ");
		for (String record : records) {
			writer.append(fixSpaces(record, 79));
			writer.append("\n");
		}
		writer.close();
	}
}
