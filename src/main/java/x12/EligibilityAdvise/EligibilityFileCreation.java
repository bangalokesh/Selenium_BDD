package x12.EligibilityAdvise;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import pageclasses.BasePage;
import pageclasses.CommonMethods;
import utils.Const;
import utils.Dbconn;
import x12.Header;
import x12.Loop_ISA;
import x12.Mapping;
import x12.PGRConnections;
import x12.Segment;
import x12.enums.FileType;

public class EligibilityFileCreation extends BasePage {
	public Dbconn dbaccess;
	private Loop_ISA isa_loop;
	private Header st_header;
	private SE trailer;
	private HashMap<String, String> staticData;
	private List<HashMap<String, String>> senderReceiver;
	private int fileCount;

	public EligibilityFileCreation() {
		this.dbaccess = new Dbconn();
		senderReceiver = getAllSenderReceivers();
		fileCount = 0;
	}

	public static void main(String[] args) {
		EligibilityFileCreation a = new EligibilityFileCreation();
		a.createEligibilityFile();
	}

	public void createEligibilityFile() {
		try {
			// get number of medicare ids
			String queryGetMedicareIDs = "SELECT D.[SupplementalID] AS SupplementalID\n"
					+"		, D.[MedicareID] AS MEDICARE_ID \n	"
					+ "      ,D.[MemberID] AS MEMBER_ID\n"
					+ "      ,D.[LastName] AS MEMBER_LAST_NAME\n" + "      ,D.[FirstName] AS MEMBER_FIRST_NAME\n"
					+",D.[MiddleInitial] AS MEMBER_MIDDLE_NAME"
					+ "      ,D.[BirthDate] AS MEMBER_BIRTH_DATE\n" + "      ,D.[Gender] AS MEMBER_GENDER\n"
					+ "	  ,E.[PlanID] AS PLAN_ID\n" + "      ,E.[PBPID] AS PBP_ID\n"
					+ "  FROM [VelocityTestAutomation].[dbo].[member_demographic] D\n"
					+ "  inner join [VelocityTestAutomation].[dbo].[member_enrollment] E\n"
					+ "  ON D.[MedicareID] = E.[MedicareID]\n" + "  WHERE D.[MedicareID] IN\n"
					+ "  (SELECT MIN([MedicareID]) FROM [VelocityTestAutomation].[dbo].[member_enrollment] \n"
					+ "  WHERE [PlanID] <> 'R6592' \n" + "  AND NOT([PlanID] = 'H6936' AND [PBPID] = '001')\n"
					+ "  GROUP BY [PlanID],[PBPID])";
			List<HashMap<String, String>> listOfMembers = dbaccess.getListOfHashMapsFromResultSet(queryGetMedicareIDs);
			List<HashMap<String, String>> senderReceiverList;


			/**************************** Create Realtime file ******************************/
			//BlueExchange files
			senderReceiverList = getSenderReceiver("Realtime", "Blue Exchange");			
			createRealTimeFile(listOfMembers.get(0), "Realtime", senderReceiverList.get(0));

			// Create Non-Blue exchange Realtime files - one for each member
			senderReceiverList = getSenderReceiver("Realtime", "Non Blue Exchange");
			for (int i=0; i<listOfMembers.size(); i++) {
				HashMap<String, String> member = listOfMembers.get(i);
				createRealTimeFile(member, "Realtime", senderReceiverList.get(i));
			}
			
			/************************* Create Batch files ***********************************/
			//BlueExchange files
			senderReceiverList = getSenderReceiver("Batch", "Blue Exchange");
			createBatchFile(listOfMembers, senderReceiverList.get(0));
			
			// Create Non-Blue exchange Batch file
			senderReceiverList = getSenderReceiver("Batch", "Non Blue Exchange");
			createBatchFile(listOfMembers, senderReceiverList.get(getRandomNumber(0, senderReceiverList.size()-1)));
			
			
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private HashMap<String, String> populateReceiverData(HashMap<String, String> memberData) {
		PGRConnections pgrData = new PGRConnections();
		HashMap<String, String> memberPCP = pgrData.getMemberPCP(memberData.get("MEDICARE_ID"), memberData.get("PLAN_ID"), memberData.get("PBP_ID"));
		HashMap<String, String> temp = new HashMap<String, String>();
		for (String key: memberPCP.keySet()) {
			if(key.equalsIgnoreCase("LAST_NAME"))
				temp.put("2100_NM103", memberPCP.get(key));
			if(key.equalsIgnoreCase("FIRST_NAME")) {
				String firstName = memberPCP.get(key);
				if(firstName == null || (firstName.length()==0)) {
					temp.put("2100_NM102", "2");
					temp.put("2100_NM104", "");
				} else {
					temp.put("2100_NM102", "1");
					temp.put("2100_NM104", firstName);
				}
			}
			if(key.equalsIgnoreCase("NPI"))
				temp.put("2100_NM109", memberPCP.get(key));	
		}
		return temp;
	}

	public void createRealTimeFile(HashMap<String, String> memberData, String mode, HashMap<String, String> senderReceiverData) {
		this.fileCount++;
		BufferedWriter writer;
		try {
			staticData = getStaticData(senderReceiverData);
			isa_loop = new Loop_ISA(this.staticData);
			st_header = new Header(FileType.Eligibility, getControlNumber(), this.staticData);
			
			writer = new BufferedWriter(new FileWriter(
					new File(Const.EligibilityFilePath + "Eligibilty_RealTime_" + this.fileCount + "_"+ CommonMethods.getCurrentDateTime() + ".txt")));
			int hlCounter = 0;
			// Application Sender’s Code
			this.isa_loop.addStaticData("GS06", getControlNumber());
			writer.append(isa_loop.getControlHeaders());
			writer.append(st_header.toString());

			List<HashMap<String, String>> loopData = get2000LoopData();
			HashMap<String, String> payerData = null;
			HashMap<String, String> receiverData = null;
			HashMap<String, String> subscriberData = null;
			for (HashMap<String, String> row : loopData) {
				if (row.get("IDENTIFIER").equalsIgnoreCase("PAYER"))
					payerData = row;
				else if (row.get("IDENTIFIER").equalsIgnoreCase("RECEIVER"))
					receiverData = row;
				else if (row.get("IDENTIFIER").equalsIgnoreCase("SUBSCRIBER"))
					subscriberData = row;
			}
			payerData.put("2100_NM109", staticData.get("ISA08"));
			receiverData.putAll(populateReceiverData(memberData));
			List<String> eq01List = getEQ();
			subscriberData.put("EQ01", eq01List.get(randBetween(0, eq01List.size() - 1)));
			subscriberData.put("SupplementalID", memberData.get("SupplementalID"));
			subscriberData.putAll(staticData);
			Loop2000 loopA = new Loop2000(payerData, ++hlCounter);
			Loop2000 loopB = new Loop2000(receiverData, ++hlCounter);
			writer.append(loopA.toString());
			writer.append(loopB.toString());
			AdditionalSegments additionalSegs = new AdditionalSegments(subscriberData, memberData, ++hlCounter);
			writer.append(additionalSegs.toString());

			int noOflines = 1 + st_header.getSegmentCount() + loopA.getSegmentCount() + loopB.getSegmentCount()
					+ additionalSegs.getSegmentCount();
			trailer = new SE(noOflines + "", staticData.get("SE02"));
			writer.append(trailer.toString());
			writer.append("\n");
			writer.append(this.isa_loop.getControlTrailers());
			writer.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	public void createBatchFile(List<HashMap<String, String>> members, HashMap<String, String> senderReceiverData) {
		this.fileCount++;
		BufferedWriter writer;
		try {
			staticData = getStaticData(senderReceiverData);
			isa_loop = new Loop_ISA(this.staticData);
			st_header = new Header(FileType.Eligibility, getControlNumber(), this.staticData);
			
			writer = new BufferedWriter(new FileWriter(
					new File(Const.EligibilityFilePath + "Eligibilty_Batch_" + this.fileCount + "_" + CommonMethods.getCurrentDateTime() + ".txt")));
			int hlCounter = 0;
			// Application Sender’s Code
			this.isa_loop.addStaticData("GS06", getControlNumber());
			writer.append(isa_loop.getControlHeaders());
			writer.append(st_header.toString());

			List<HashMap<String, String>> loopData = get2000LoopData();
			HashMap<String, String> payerData = null;
			HashMap<String, String> receiverData = null;
			HashMap<String, String> subscriberData = null;
			for (HashMap<String, String> row : loopData) {
				if (row.get("IDENTIFIER").equalsIgnoreCase("PAYER"))
					payerData = row;
				else if (row.get("IDENTIFIER").equalsIgnoreCase("RECEIVER"))
					receiverData = row;
				else if (row.get("IDENTIFIER").equalsIgnoreCase("SUBSCRIBER"))
					subscriberData = row;
			}
			payerData.put("2100_NM109", staticData.get("ISA08"));
			receiverData.putAll(populateReceiverData(members.get(0)));
			Loop2000 loopA = new Loop2000(payerData, ++hlCounter);
			Loop2000 loopB = new Loop2000(receiverData, ++hlCounter);
			writer.append(loopA.toString());
			writer.append(loopB.toString());
			int batchSegments = 0;
			for (HashMap<String, String> memberData : members) {
				subscriberData.put("SupplementalID", memberData.get("SupplementalID"));
				subscriberData.putAll(staticData);
				subscriberData.putAll(getEQandHI());
				AdditionalSegments additionalSegs = new AdditionalSegments(subscriberData, memberData,
						++hlCounter);
				writer.append(additionalSegs.toString());
				batchSegments = batchSegments + additionalSegs.getSegmentCount();
			}
			writer.append("\r\n");
			int noOflines = 1 + st_header.getSegmentCount() + loopA.getSegmentCount() + loopB.getSegmentCount()
					+ batchSegments;
			trailer = new SE(noOflines + "", staticData.get("SE02"));
			writer.append(trailer.toString());
			writer.append("\r\n");
			writer.append(this.isa_loop.getControlTrailers());
			writer.close();

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

	}

	public List<HashMap<String, String>> getSenderReceiver(String mode, String name) {		
		List<HashMap<String, String>> bluexSR = new ArrayList<HashMap<String,String>>();
		List<HashMap<String, String>> nonBluexSR = new ArrayList<HashMap<String,String>>();
		for(HashMap<String, String> row : this.senderReceiver) {
			if(row.get("Mode").trim().equalsIgnoreCase(mode)) {			
				if(row.get("Name").trim().equalsIgnoreCase("Blue Exchange")) {
					bluexSR.add(row);
				} else {
					nonBluexSR.add(row);
				}
			}
		}
		if(name.equalsIgnoreCase("Blue Exchange")) {
			return bluexSR;
		} else {
			//generate list of 9 sender receivers for 9 members
			List<HashMap<String, String>> temp = new ArrayList<HashMap<String,String>>();
			for(int i=1; i<=9; i++) {				
				int x = randBetween(1, nonBluexSR.size()-1);
				temp.add(nonBluexSR.get(x));
			}			
			return temp;
		}			
	}
	
	private List<HashMap<String, String>> getAllSenderReceivers() {
		String senderRecQuery = "SELECT [ISA06]\n" + 
				"      ,[ISA08]\n" + 
				"      ,[Name]\n" + 
				"      ,[Transaction]\n" + 
				"      ,[Mode]\n" + 
				"  FROM [VelocityTestAutomation].[dbo].[270_SenderReceiverDetails]";
		List<HashMap<String, String>> list = dbaccess.getListOfHashMapsFromResultSet(senderRecQuery);
		return list;
	}

	public HashMap<String, String> getStaticData(HashMap<String, String> senderReceiverData) {
		String query = "SELECT TOP 1  [ISA01] AS [ISA_ISA01]\n" 
							+ "      ,[ISA03] AS [ISA_ISA03]\n" 
							+ "      ,[ISA05] AS [ISA_ISA05]\n" 
							+ "      ,[ISA07] AS [ISA_ISA07]\n"
							+ "      ,[ISA11] AS [ISA_ISA11]\n" 
							+ "      ,[ISA12] AS [ISA_ISA12]\n" 
							+ "      ,[ISA14] AS [ISA_ISA14]\n" 
							+ "      ,[ISA15] AS [ISA_ISA15]\n" 
							+ "      ,[ISA16] AS [ISA_ISA16]\n"
							+ "      ,[GS01] AS [GS_GS01]\n" 
							+ "      ,[GS07] AS [GS_GS07]\n" 
							+ "      ,[GS08] AS [GS_GS08]\n" 
							
							+ "      ,[ST01]\n" + "      ,[ST03]\n"
				+ "      ,[BHT01]\n" + "      ,[BHT02]\n" + "      ,[DMG01]\n" + "      ,[SE01]\n" + "      ,[GE01]\n"
				+ "      ,[IEA01]\n" + "      ,[DTP01]\n" + "      ,[DTP02]\n" + "      ,[SE02]\n" + "	  ,[HI01-01]\n" 
				+ "	     ,[EQ02-01]\n" + "	  ,[EQ05]" 	+ "  FROM [VelocityTestAutomation].[dbo].[270Static_Data]";
		HashMap<String, String> data = dbaccess.getResultSet(query);
		data.put("ISA_ISA06", senderReceiverData.get("ISA06"));
		data.put("ISA_ISA08", senderReceiverData.get("ISA08"));
		data.put("GS_GS02", senderReceiverData.get("ISA06"));
		data.put("GS_GS03", senderReceiverData.get("ISA08"));
		return data;
	}
	public List<String> getEQ() {
		String queryEQ = "SELECT [EQ01]\n" + "FROM [VelocityTestAutomation].[dbo].[270EQ_Segment_data]";
		List<String> eq01List = dbaccess.getListFromQuery(queryEQ);
		return eq01List;
	}

	public HashMap<String, String> getEQandHI() {
		String query = "SELECT [EQ03]\n" + 
				"      ,[HI01]\n" + 
				"  FROM [VelocityTestAutomation].[dbo].[270EQ_Segment_data]"
				+ "where [HI01] is not NULL";
		List<HashMap<String, String>> list = dbaccess.getListOfHashMapsFromResultSet(query);
		HashMap<String, String> h = new HashMap<String, String>();
		int index = getRandomNumber(0, list.size()-1);
		h.put("EQ02-02", list.get(index).get("EQ03").trim());
		String hI01 = list.get(index).get("HI01").trim();
		if(hI01.contains(".")) {
			hI01 = hI01.substring(0, hI01.indexOf("."));
		}
		h.put("HI01-02", hI01);
		return h;
	}
	
	public List<HashMap<String, String>> get2000LoopData() {
		String query = "SELECT [IDENTIFIER]\n" + "      ,[2000_HL02]\n" + "      ,[2000_HL03]\n"
				+ "      ,[2000_HL04]\n" + "      ,[2100_NM101]\n" + "      ,[2100_NM102]\n" + "      ,[2100_NM108]\n"
				+ "      ,[2100_NM109]\n" + "      ,[2100_NM103]\n"
				+ "  FROM [VelocityTestAutomation].[dbo].[270Identifier_Data]";
		List<HashMap<String, String>> h = dbaccess.getListOfHashMapsFromResultSet(query);
		return h;
	}

	private class SE extends Segment implements Mapping {
		private String se01;
		private String se02;

		public SE(String se01, String se02) {
			super("SE");
			this.se01 = se01;
			this.se02 = se02;
			setValues(getMap());
		}

		@Override
		public Map<String, String> getMap() {
			Map<String, String> map = new HashMap<String, String>();
			map.put("SE01", se01); // number of segments
			map.put("SE02", se02); // ST02
			return map;
		}
	}

}
