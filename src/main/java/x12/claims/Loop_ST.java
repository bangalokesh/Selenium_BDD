package x12.claims;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import utils.Dbconn;
import x12.FileDataStructure;
import x12.Header;
import x12.Loop;
import x12.Mapping;
import x12.Segment;
import x12.enums.ClaimType;
import x12.enums.FileType;

public class Loop_ST {

	private Segment ISA_Segment;
	private Segment GS_Segment;

	private IEA IEA_Segment;

	private int transactions;
	private String interchangeControlNumber, groupControlNumber;
	
	private Map<String, String> staticData;
	
	private Dbconn db;
	private Header header;
	private Loop loop1000A, loop1000B;
	private Loop loop2000A, Loop_2010AA;
	private Loop loop2000B, Loop_2010BA, Loop_2010BB;
	private Loop_2300 loop2300;
	private SE trailer;

	private String claimCode;
	protected List<Loop> loop2400;
	private String patientNumber;
	private String claimNumber;
	private double totalClaimAmount;
	
	private String testID;

	public Loop_ST(ClaimType type, String testID, String isaControlNumber, Map<String, String> staticData, List<HashMap<String, String>> claimLineData, FileDataStructure dataHolder) {
		this.testID = testID;
		this.db = new Dbconn();
		this.staticData = staticData;
		this.interchangeControlNumber = getControlNumber();
		this.groupControlNumber = getControlNumber();
		
		Map<String, String> isaData = dataHolder.getSegments(dataHolder.getLoopData("ISA")).get("ISA");
		isaData.put("ISA_ISA13", interchangeControlNumber);
		
		Map<String, String> gsData = dataHolder.getSegments(dataHolder.getLoopData("GS")).get("GS");
		gsData.put("GS_GS06", groupControlNumber);
		
		ISA_Segment = new Segment("ISA", isaData);
		GS_Segment = new Segment("GS", gsData);
		
		IEA_Segment = new IEA();
		if(this.staticData.get("GE01") != null) {
			this.transactions = Integer.parseInt(this.staticData.get("GE01").trim()); 
		} else {
			this.transactions = 0; //this value is different for Eligibility file and should come from the DB
		}
		
		header = new Header(FileType.Claim, isaControlNumber, staticData);
		
		loop1000A = new Loop("1000A", dataHolder.getSegments(dataHolder.getLoopData("1000A")));
		loop1000A.addSegments("NM1");
		loop1000A.addSegments("PER");
		
		loop1000B = new Loop("1000B", dataHolder.getSegments(dataHolder.getLoopData("1000B")));
		loop1000B.addSegments("NM1");
		
		loop2000A = new Loop("2000A", dataHolder.getSegments(dataHolder.getLoopData("2000A")));
		this.Loop_2010AA = new Loop("2010AA", dataHolder.getSegments(dataHolder.getLoopData("2010AA")));
		this.Loop_2010AA.addSegments("NM1");
		this.Loop_2010AA.addSegments("N3");
		this.Loop_2010AA.addSegments("N4");
		this.Loop_2010AA.addSegments("REF");
		loop2000A.addLoop(Loop_2010AA);
		
		loop2000B = new Loop("2000B", dataHolder.getSegments(dataHolder.getLoopData("2000B")));
		loop2000B.addSegments("HL");
		loop2000B.addSegments("SBR");
		this.Loop_2010BA = new Loop("2010BA", dataHolder.getSegments(dataHolder.getLoopData("2010BA")));
		this.Loop_2010BA.addSegments("NM1");
		this.Loop_2010BA.addSegments("N3");
		this.Loop_2010BA.addSegments("N4");
		this.Loop_2010BA.addSegments("DMG");
		this.Loop_2010BB = new Loop("2010BB", dataHolder.getSegments(dataHolder.getLoopData("2010BB")));
		this.Loop_2010BB.addSegments("NM1");
		loop2000B.addLoop(Loop_2010BA);
		loop2000B.addLoop(Loop_2010BB);
		
		this.totalClaimAmount = 0;
		this.loop2400 = new LinkedList<Loop>();
		this.claimCode = type.codeInt;
		this.claimNumber = generateNumber();
		this.patientNumber = generateNumber();
		
		
		
		processClaims(dataHolder, claimLineData);
		
		dataHolder.addElement("2300_CLM01", this.patientNumber);
		dataHolder.addElement("2300_CLM02", fixDecimals(""+totalClaimAmount));
		dataHolder.addElement("2300_REF02", this.claimNumber);
		dataHolder.addElement("2300_NTE02", patientNumber + this.claimNumber);
		
		loop2300 = new Loop_2300(testID, type, claimLineData, dataHolder);

		trailer = new SE(header.getSTControlNumber());
		
	}
	
	private String fixDecimals(String raw) {
		if (!raw.contains("."))
			return raw;

		int length = raw.length();
		for (int i = raw.length() - 1; i >= 0; i--) {
			if (raw.charAt(i) == '.')
				return raw.substring(0, i);
			else if (raw.charAt(i) == '0')
				length--;
			else
				return raw.substring(0, length);
		}

		return raw;
	}
	
	private String generateNumber() {
		Random r = new Random();
		char[] num = new char[10];
		for (int i = 0; i < 3; i++)
			num[i] = (char) ('a' + r.nextInt(26));
		for (int i = 3; i < 10; i++)
			num[i] = (char) ('0' + r.nextInt(10));
		return new String(num);
	}

	public void processClaims(FileDataStructure dataHolder, List<HashMap<String, String>> claimLineData) {
		int count = 1;
		for (Map<String, String> claim : claimLineData) {
			this.totalClaimAmount += (Double.parseDouble(claim.get("2400_SV" + this.claimCode + "02")));
			dataHolder.addElement("2400_REF02", claimNumber + count);
			dataHolder.addElement("2400_LX01", ""+count);
			Loop loop = new Loop("2400", dataHolder.getNext2400Map());
			loop.addSegments("LX");
			loop.addSegments("SV" + this.claimCode);
			loop.addSegments("DTP");
			loop.addSegments("REF");
			this.loop2400.add(loop);
			count++;
			
			String query = "UPDATE [dbo].[test_data_claimline]\n" + "SET ClaimLineNumber = '" + (claimNumber + count) + "'\n"
					+ "WHERE ID = '" + dataHolder.getElement("2400_ID00") + "'";
			db.executeQuery(query);
		}
	}
	
	public int getSegmentCount() {
		return 
			   header.getSegmentCount() 
			 + loop1000A.getSegmentCount() 
			 + loop1000B.getSegmentCount() 
			 + loop2000A.getSegmentCount() 
			 + loop2000B.getSegmentCount()
			 + loop2300.getSegmentCount()
			 + getLoop2400SegmentCount() + 1;
	}
	
	private int getLoop2400SegmentCount() {
		int count = 0;
		for(Loop l : loop2400)
			count += l.getSegmentCount();
		return count;
	}

	public void updateTable() {
		String query = "UPDATE [dbo].[test_data_readytoclaims]\n" + "SET PatientNumber = '" + this.patientNumber
				+ "',\n" + "	ClaimNumber = '" + this.claimNumber + "' \n" +
				"WHERE TestCaseID = '" + this.testID + "'";
		db.executeQuery(query);
	}

	@Override
	public String toString() {
		StringBuilder loop = new StringBuilder();
		loop.append(header.toString());
		loop.append("\r\n");
		loop.append(loop1000A);
		loop.append("\r\n");
		loop.append(loop1000B);
		loop.append("\r\n");
		loop.append(loop2000A.toString());
		loop.append("\r\n");
		loop.append(loop2000B.toString());
		loop.append("\r\n");
		loop.append(loop2300.toString());
		loop.append("\r\n");
		for (Loop l2400 : loop2400) {
			loop.append(l2400.toString());
			loop.append("\r\n");
		}
		loop.append(trailer.toString());
		return loop.toString();
	}

	private class SE extends Segment implements Mapping {
		private String controlNumber;

		public SE(String ST_ControlNumber) {
			super("SE");
			this.controlNumber = ST_ControlNumber;
			setValues(getMap());
		}

		@Override
		public Map<String, String> getMap() {
			Map<String, String> map = new HashMap<String, String>();
			map.put("SE01", "" + getSegmentCount()); // number of segments
			map.put("SE02", this.controlNumber); // ST02
			return map;
		}
	}

	private int randBetween(int start, int end) {
		return start + (int) Math.round(Math.random() * (end - start));
	}
	
	private String getControlNumber() {
		String controlNumber = "";
		try {
			controlNumber = randBetween(100000000, 999999999) + "";

		} catch (Exception e) {
			e.printStackTrace();
		}
		return controlNumber;
	}
	
	public String getControlHeaders() {
		StringBuilder fileData = new StringBuilder();
		fileData.append(ISA_Segment.toString());
		fileData.append("\r\n");
		fileData.append(GS_Segment.toString());
		fileData.append("\r\n");
		return fileData.toString();
	}

	public String getControlTrailers() {
		StringBuilder fileData = new StringBuilder();
		fileData.append(new GE(transactions).toString());
		fileData.append("\r\n");
		fileData.append(IEA_Segment.toString());
		fileData.append("\r\n");
		return fileData.toString();
	}

	private class GE extends Segment implements Mapping {

		private int transactions;

		public GE(int transactions) {
			super("GE");
			this.transactions = transactions;
			setValues(getMap());
		}

		@Override
		public Map<String, String> getMap() {
			Map<String, String> map = new HashMap<String, String>();
			map.put("GE01", "" + this.transactions);
			map.put("GE02", groupControlNumber);
			return map;
		}
	}

	private class IEA extends Segment implements Mapping {

		public IEA() {
			super("IEA");
			setValues(getMap());
		}

		@Override
		public Map<String, String> getMap() {
			Map<String, String> map = new HashMap<String, String>();
			map.put("IEA01", "1");
			map.put("IEA02", interchangeControlNumber);
			return map;
		}
	}
}
