package EnrollmentFiles;

import java.io.BufferedWriter;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import org.joda.time.LocalDateTime;

import pageclasses.BasePage;
import utils.Const;
import utils.Dbconn;

public class CreateTRRFileFromTable extends BasePage {

	private Dbconn dbaccess = new Dbconn();
	private String TRR_FILEPATH = Const.TRR_FILEPATH + "TRR_ENROLLMENT_Files_" + getRunName();

	static final int LINE_WIDTH = 500;
	String TRR_DISENROLLMENT_FILEPATH = Const.TRR_FILEPATH + "TRR_DISENROLLMENT_MODIFICATION_Files_" + getRunName();
	static final String QUERY_ON_TRR_TABLE = "SELECT * FROM dbo.trr_file_data WHERE RunMode = 'Y'";

	private void createEnrollmentFile(String filename, List<HashMap<String, String>> data,
			LinkedHashMap<String, Integer> properties) throws IOException {
		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(filename)));

		for (HashMap<String, String> record : data) {
			String line = "";
			for (String key : properties.keySet()) {
				if (key.equals("PartDLateEnrollmentPenaltyAmount")
						|| key.equals("PartDLateEnrollmentPenaltyWaivedAmount")
						|| key.equals("PartDLateEnrollmentPenaltySubsidyAmount")
						|| key.equals("LowIncomePartDPremiumSubsidyAmount")
						|| key.equals("DeMinimisDifferentialAmount"))
					line += " 0000.00";

				else if (record.get(key) == null || record.get(key).trim().equals("")) {
					line += (fixSpaces("", properties.get(key)));
				} else {
					line += (fixSpaces(record.get(key), properties.get(key)));
				}
			}

			if (record.get("TransactionalReplyCode").equals("022")) {
				line = changeBeneficiaryID(line, record.get("Identifier"));
			}

			if (record.get("TransactionalReplyCode").equals("023")) {
				line = changeName(line, record.get("Identifier"));
			}
			writer.append(line.substring(0, 499));
			writer.append("\r\n");
		}
		writer.close();
	}

	private String changeBeneficiaryID(String line, String identifier) {
		char[] chars = line.toCharArray();
		for (int i = 0; i < 12; i++) {
			chars[i + 85] = chars[i];
		}
		Random gen = new Random();
		char[] newID = new char[12];
		boolean[] isNum = new boolean[] { true, false, true, true, false, false, true, false, false, true, true };
		for (int i = 0; i < 11; i++) {
			if (isNum[i]) {
				chars[i] = (char) ('0' + gen.nextInt(10));
				newID[i] = chars[i];
			} else {
				chars[i] = (char) ('A' + gen.nextInt(26));
				newID[i] = chars[i];
			}
		}

		String query = "UPDATE dbo.trr_file_data SET UpdatedData = '" + new String(newID) + "' WHERE Ientifier = "
				+ identifier;
		dbaccess.executeQuery(query);
		return new String(chars);
	}

	private String changeName(String line, String identifier) {
		char[] chars = line.toCharArray();
		Random gen = new Random();
		String newName = "";
		for (int i = 12; i < 32; i++) {
			if (i == 24)
				newName += ", ";
			if (i == 31)
				newName += " ";

			chars[i] = (char) ('A' + gen.nextInt(26));
			newName += chars[i];
		}

		String query = "UPDATE dbo.trr_file_data SET UpdatedData = '" + new String(newName) + "' WHERE Ientifier = "
				+ identifier;
		dbaccess.executeQuery(query);
		return new String(chars);
	}

	private String getSystemAssignedTransactionID() {
		Random generator = new Random();
		String retVal = "-";
		for (int i = 0; i < 10; i++) {
			retVal += generator.nextInt(10);
		}
		return retVal;
	}

	private String getPlanAssignedTransactionTrackingID() {
		Random generator = new Random();
		String retVal = "E";
		for (int i = 0; i < 11; i++) {
			retVal += generator.nextInt(10);
		}
		return retVal;
	}

	private String getEffectiveDate(String date) {
		if (date == null || date.trim().length() == 0) {
			String newdate = getNextMonth().trim().replaceAll("/", "");
			return newdate.substring(4, 8) + newdate.substring(0, 2) + newdate.substring(2, 4);
		}
		return date.trim().replaceAll("-", "");
	}

	private String getTodaysDate() {
		DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
		LocalDate localDate = LocalDate.now();
		return dtf.format(localDate);
	}

	public void populateDataBase() {
		String query = "SELECT \r\n" + "dbo.member_demographic.MedicareID as BeneficiaryID,\r\n"
				+ "dbo.member_demographic.LastName as LastName, \r\n"
				+ "dbo.member_demographic.FirstName as FirstName,\r\n"
				+ "dbo.member_demographic.MiddleInitial as MiddleInitial, \r\n"
				+ "dbo.member_demographic.Gender as Gender,\r\n" + "dbo.member_demographic.BirthDate as DOB, \r\n"
				+ "dbo.test_data_readytoenroll.ProductPlanID as ContractNumber,\r\n"
				+ "dbo.countyMapping.CountyID as CountyCode,\r\n"
				+ "dbo.test_data_readytoenroll.CoverageDate as EffectiveDate, \r\n"
				+ "dbo.test_data_readytoenroll.ProductPBPID as PBPID,\r\n"
				+ "dbo.test_data_readytoenroll.ApplicationDate as ApplicationDate,\r\n"
				+ "dbo.test_data_readytoenroll.ProductSegmentID as SegmentNumber,\r\n"
				+ "dbo.member_accretion_details.PrimaryBIN as PartDRxBIN,\r\n"
				+ "dbo.member_accretion_details.PrimaryPCN as PartDRxPCN,\r\n"
				+ "dbo.member_accretion_details.PrimaryRXID as PartDRxID,\r\n"
				+ "dbo.member_accretion_details.SecondaryBin as SecondaryRxBIN,\r\n"
				+ "dbo.member_accretion_details.SecondaryPCN as SecondaryRxPCN,\r\n"
				+ "dbo.member_accretion_details.ElectionType as ElectionTypeCode,\r\n"
				+ "dbo.member_accretion_details.PrimaryRxGroup as PartDRxGroup,\r\n"
				+ "dbo.member_accretion_details.DisEnrollmentReason as DisenrollmentReasonCode,\r\n"
				+ "dbo.member_accretion_details.TotalUncoveredMonths as SubmittedNumberOfUncoveredMonths,\r\n"
				+ "dbo.member_accretion_details.PremiumWithholdOption as PremiumWithholdOption,\r\n"
				+ "dbo.member_accretion_details.CreditableCoverageFalg as CreditableCoverageFlag \r\n" + "FROM ((((\r\n"
				+ "dbo.member_demographic JOIN dbo.member_enrollment ON dbo.member_demographic.MedicareID = dbo.member_enrollment.MedicareID) \r\n"
				+ "FULL OUTER JOIN dbo.test_data_readytoenroll ON dbo.member_demographic.MedicareID = dbo.test_data_readytoenroll.MedCareID) \r\n"
				+ "FULL OUTER JOIN dbo.address_details ON dbo.address_details.ID = dbo.test_data_readytoenroll.AddressID)\r\n"
				+ "FULL OUTER JOIN dbo.countyMapping ON dbo.countyMapping.CountyName = dbo.address_details.County) \r\n"
				+ "FULL OUTER JOIN dbo.member_accretion_details ON dbo.member_enrollment.MedicareID = dbo.member_accretion_details.MedicareID \r\n"
				+ "WHERE dbo.test_data_readytoenroll.RunMode = 'Y' and dbo.test_data_readytoenroll.ApplicationStatus = 'EPEND' and CMSEnrollmentStatus = 'CMSSUBMIT'";

		String updateQuery = "UPDATE dbo.trr_file_data SET RunMode = 'N' WHERE RunMode = 'Y'";
		dbaccess.sqlUpdate(updateQuery);

		List<HashMap<String, String>> data = dbaccess.getListOfHashMapsFromResultSet(query);
		LinkedList<String> keys = new LinkedList<String>();
		if (data.isEmpty())
			return;
		for (String key : data.get(0).keySet())
			keys.add(key);
		if (keys.contains("Gender"))
			keys.remove("Gender");

		String dbColumns = "";
		for (String key : keys) {
			dbColumns += key + ", ";
		}

		String hardCodedColumns = "ModificationType, RunMode, PlanAssignedTransactionTrackingID, SystemAssignedTransactionTrackingID, "
				+ "PartCBeneficiaryPremium, PartDBeneficiaryPremium, OutOfAreaFlag, UIUserOrganizationDesignation, "
				+ "UIInitiatedChangeFlag, WAIndicator, DisabilityIndicator, HospiceIndicator, "
				+ "Institutional_NHC_HCBSIndicator, RecordType, StateCode, TransactionalReplyCode, "
				+ "TransactionCode, EnrollmentSourceCode, CumulativeNumberOfUncoveredMonths, ProcessingTimestamp, "
				+ "PartDLateEnrollmentPenaltyAmount, PartDLateEnrollmentPenaltyWaivedAmount, "
				+ "PartDLateEnrollmentPenaltySubsidyAmount, LowIncomePartDPremiumSubsidyAmount, "
				+ "DeMinimisDifferentialAmount, TRCShortName, TransactionDate, Gender, SourceID";
		String hardCodedValues = "'ENROLLED', 'Y', '" + getPlanAssignedTransactionTrackingID() + "', '"
				+ getSystemAssignedTransactionID()
				+ "', '00000.00', '00039.00', 'N', '01', '0', '0', '0', '0', '0', 'T', '03', '011', '61', 'B', '000', '"
				+ new SimpleDateFormat("HH.mm.ss.SSSSSS").format(new Date())
				+ "', ' 0000.00', ' 0000.00', ' 0000.00', ' 0000.00', ' 0000.00', 'ENROLL ACCEPTED', ";

		hardCodedValues += "'" + getTodaysDate() + "', ";

		dbColumns += hardCodedColumns;

		for (HashMap<String, String> record : data) {
			String dbValues = "";
			for (String key : keys) {
				if (key.equals("DOB") || key.toLowerCase().contains("date")) {
					if (key.equals("EffectiveDate")) {
						dbValues += "'" + getEffectiveDate(record.get(key)) + "', ";
					} else if (key.equals("ApplicationDate")) {
						dbValues += "'" + getTodaysDate() + "', ";
					} else
						dbValues += "'" + record.get(key).trim().replaceAll("-", "") + "', ";
				} else {
					dbValues += "'" + record.get(key).trim() + "', ";
				}
			}
			dbValues += hardCodedValues;
			String gender = "" + getGenderCode(record.get("Gender"));
			dbValues += gender + ", ";
			dbValues += "'" + record.get("ContractNumber") + "'";

			query = "INSERT INTO dbo.trr_file_data (" + dbColumns + ") VALUES (" + dbValues + ")";
			dbaccess.updateDBTestData(query);
		}
	}

	public void createEnrollmentTRRFileFromLayout() {
		String query = "SELECT * FROM dbo.trr_file_data WHERE RunMode = 'Y'";
		List<HashMap<String, String>> data = dbaccess.getListOfHashMapsFromResultSet(query);

		LinkedHashMap<String, Integer> properties;
		try {
			properties = (LinkedHashMap<String, Integer>) loadProperties(Const.TRR_PROPERTIES_FILE);

			List<String> planIDs = getPlanIDs(0);
			Map<String, List<HashMap<String, String>>> collection = new HashMap<String, List<HashMap<String, String>>>();
			for (String id : planIDs)
				collection.put(id, new LinkedList<HashMap<String, String>>());

			for (HashMap<String, String> record : data) {
				List<HashMap<String, String>> list = collection.get(record.get("ContractNumber"));
				list.add(record);
				collection.put(record.get("ContractNumber"), list);
			}

			for (String id : planIDs)
				createEnrollmentFile(TRR_FILEPATH + id, collection.get(id), properties);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getMonthEndDate() {
		Calendar c = Calendar.getInstance();
		int res = c.getActualMaximum(Calendar.DATE);
		LocalDateTime date = LocalDateTime.now();
		return "" + date.getYear() + "" + new DecimalFormat("00").format(date.getMonthOfYear()) + "" + res;
	}

	private void createDisEnrollmentFile(String filepath, List<HashMap<String, String>> data,
			LinkedHashMap<String, Integer> properties) {
		BufferedWriter writer;
		try {
			writer = new BufferedWriter(new FileWriter(new File(filepath)));

			for (HashMap<String, String> record : data) {
				String line = "";
				for (String key : properties.keySet()) {

					if (key.equals("PartDLateEnrollmentPenaltyAmount")
							|| key.equals("PartDLateEnrollmentPenaltyWaivedAmount")
							|| key.equals("PartDLateEnrollmentPenaltySubsidyAmount")
							|| key.equals("LowIncomePartDPremiumSubsidyAmount")
							|| key.equals("DeMinimisDifferentialAmount"))
						line += " 0000.00";

					else if (key.equals("TransactionalReplyCode"))
						line += getTransactionalReplyCode(record.get("ModificationSubType"));
					else if (key.equals("TransactionCode"))
						line += getTransactionCode(record.get("ModificationSubType"));
					else if (key.equals("TRCShortName")) {
						line += fixSpaces(record.get("ModificationType"), properties.get(key));
					}

					else if (key.equals("DynamicTRCValue") || key.equals("EffectiveDate")) {
						line += fixSpaces(getMonthEndDate(), properties.get(key));
					}

					else if (record.get(key) == null || record.get(key).trim().equals("")) {
						line += (fixSpaces("", properties.get(key)));
					} else {
						line += (fixSpaces(record.get(key), properties.get(key)));
					}
				}
				writer.append(line);
				writer.append("\r\n");
				List<String> additionalLines = generateAdditionalLines(line, record.get("ModificationSubType"));
				for (String myLine : additionalLines) {
					writer.append(myLine);
					writer.append("\r\n");
				}
			}
			writer.close();
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private String getTransactionalReplyCode(String code) {
		if (code == null)
			code = "00000";
		while (code.length() != 5) {
			code = "0" + code;
		}
		return code.substring(0, 3);
	}

	private String getTransactionCode(String code) {
		if (code == null)
			code = "00000";
		while (code.length() != 5) {
			code = "0" + code;
		}
		return code.substring(3, 5);
	}

	private List<String> generateAdditionalLines(String line, String TRCCode) {
		List<String> additionalLines = new ArrayList<String>();

		String code1 = null, code2 = null, name1 = null, name2 = null;
		if (TRCCode.equals("19701") || TRCCode.equals("8101") || TRCCode.equals("08101") || TRCCode.equals("7901")
				|| TRCCode.equals("07901") || TRCCode.equals("09001") || TRCCode.equals("9001")) {
			code1 = "01851";
			name1 = "AUTO DISENROLL ";
		} else if (TRCCode.equals("08501") || TRCCode.equals("8501")) {
			code1 = "30501";
			name1 = "ZIP CD CHANGE  ";
			code2 = "15401";
			name2 = "OUT OF AREA    ";
		} else if (TRCCode.equals("30501")) {
			code1 = "08501";
			name1 = "NEW SCC        ";
			code2 = "15401";
			name2 = "OUT OF AREA    ";
		} else if (TRCCode.equals("29573")) {
			code1 = "17801";
			name1 = "PNLTY RESCINDED";
		}

		if (code1 != null) {
			char[] line1 = line.toCharArray();
			for (int i = 56; i < 61; i++) {
				line1[i] = code1.charAt(i - 56);
			}
			for (int i = 365; i < 380 || (i - 365) > name1.length(); i++) {
				line1[i] = name1.charAt(i - 365);
			}
			additionalLines.add(new String(line1));
		}

		if (code2 != null) {
			char[] line2 = line.toCharArray();
			for (int i = 56; i < 61; i++) {
				line2[i] = code2.charAt(i - 56);
			}
			for (int i = 365; i < 380 || (i - 365) > name2.length(); i++) {
				line2[i] = name2.charAt(i - 365);
			}
			if (code2.equals("15401")) {
				line2[133] = 'Y';
			}
			additionalLines.add(new String(line2));
		}

		return additionalLines;
	}

	private List<String> getPlanIDs(int flag) {
		String query = "";
		if (flag == 1)
			query = "select distinct ContractNumber from trr_file_data where RunMode = 'Y'";
		else if (flag == 0)
			query = "select distinct ContractNumber from trr_file_data";
		List<HashMap<String, String>> IDs = dbaccess.getListOfHashMapsFromResultSet(query);
		List<String> ids = new LinkedList<String>();
		for (HashMap<String, String> map : IDs)
			ids.add(map.get("ContractNumber"));
		return ids;
	}

	public void createTRRDisenrollmentFile() {
		String query = "SELECT * FROM dbo.trr_file_data WHERE RunMode = 'Y'";
		List<HashMap<String, String>> data = dbaccess.getListOfHashMapsFromResultSet(query);

		List<String> planIDs = getPlanIDs(1);
		Map<String, List<HashMap<String, String>>> collection = new HashMap<String, List<HashMap<String, String>>>();
		for (String id : planIDs)
			collection.put(id, new LinkedList<HashMap<String, String>>());

		for (HashMap<String, String> record : data) {
			List<HashMap<String, String>> list = collection.get(record.get("ContractNumber"));
			list.add(record);
			collection.put(record.get("ContractNumber"), list);
		}

		LinkedHashMap<String, Integer> properties;
		properties = (LinkedHashMap<String, Integer>) loadProperties(Const.TRR_PROPERTIES_FILE);

		for (String planID : planIDs) {
			createDisEnrollmentFile(TRR_DISENROLLMENT_FILEPATH + planID, collection.get(planID), properties);
		}

	}

}
