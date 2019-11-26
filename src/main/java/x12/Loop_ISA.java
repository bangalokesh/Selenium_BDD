package x12;

import java.util.HashMap;
import java.util.Map;

import pageclasses.BasePage;

public class Loop_ISA extends BasePage {

	private ISA ISA_Segment;
	private GS GS_Segment;

	private IEA IEA_Segment;

	private int transactions;
	private String interchangeControlNumber, groupControlNumber;

	private Map<String, String> staticData;

	public Loop_ISA(Map<String, String> staticData) {
		this.interchangeControlNumber = getControlNumber();
		this.groupControlNumber = getControlNumber();
		this.staticData = staticData;
		ISA_Segment = new ISA();
		GS_Segment = new GS();
		IEA_Segment = new IEA();
		if(this.staticData.get("GE01") != null) {
			this.transactions = Integer.parseInt(this.staticData.get("GE01").trim()); 
		} else {
			this.transactions = 0; //this value is different for Eligibility file and should come from the DB
		}
		
	}

	public void addStaticData(String key, String value) {
		this.staticData.put(key, value);
	}
	
	public Map<String, String> getStaticData() {
		return staticData;
	}
	
	public int getTransactions() {
		return this.transactions;
	}

	public void setTransactionCount(int size) {
		this.transactions = size;
	}

	public String getInterchangeControlNumber() {
		return this.interchangeControlNumber;
	}

	private String getGroupControlNumber() {
		return this.groupControlNumber;
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
		fileData.append(new GE(getTransactions()).toString());
		fileData.append("\r\n");
		fileData.append(IEA_Segment.toString());
		fileData.append("\r\n");
		return fileData.toString();
	}

	private class ISA extends Segment implements Mapping {

		public ISA() {
			super("ISA");
			setValues(getMap());
		}

//		@Override
//		public Map<String, String> getMap() {
//			Map<String, String> map = new HashMap<String, String>();
//			map.put("ISA01", staticData.get("ISA01"));
//			map.put("ISA02", "          ");
//			map.put("ISA03", staticData.get("ISA03"));
//			map.put("ISA04", "          ");
//			map.put("ISA05", staticData.get("ISA05"));
//			map.put("ISA06", fixSpaces(staticData.get("ISA06"), 15));
//			map.put("ISA07", staticData.get("ISA07"));
//			map.put("ISA08", fixSpaces(staticData.get("ISA08"), 15));
//			map.put("ISA09", getCurrentDateYYMMDD());
//			map.put("ISA10", getCurrentTimeStampHHMM());
//			map.put("ISA11", staticData.get("ISA11"));
//			map.put("ISA12", staticData.get("ISA12"));
//			map.put("ISA13", getInterchangeControlNumber());
//			map.put("ISA14", staticData.get("ISA14"));
//			map.put("ISA15", staticData.get("ISA15"));
//			map.put("ISA16", staticData.get("ISA16"));
//			return map;
//		}
		@Override
		public Map<String, String> getMap() {
			Map<String, String> map = new HashMap<String, String>();
			map.put("ISA01", staticData.get("ISA_ISA01"));
			map.put("ISA02", "          ");
			map.put("ISA03", staticData.get("ISA_ISA03"));
			map.put("ISA04", "          ");
			map.put("ISA05", staticData.get("ISA_ISA05"));
			map.put("ISA06", fixSpaces(staticData.get("ISA_ISA06"), 15));
			map.put("ISA07", staticData.get("ISA_ISA07"));
			map.put("ISA08", fixSpaces(staticData.get("ISA_ISA08"), 15));
			map.put("ISA09", getCurrentDateYYMMDD());
			map.put("ISA10", getCurrentTimeStampHHMM());
			map.put("ISA11", staticData.get("ISA_ISA11"));
			map.put("ISA12", staticData.get("ISA_ISA12"));
			map.put("ISA13", getInterchangeControlNumber());
			map.put("ISA14", staticData.get("ISA_ISA14"));
			map.put("ISA15", staticData.get("ISA_ISA15"));
			map.put("ISA16", staticData.get("ISA_ISA16"));
			return map;
		}
	}

	private class GS extends Segment implements Mapping {

		public GS() {
			super("GS");
			setValues(getMap());
		}

//		@Override
//		public Map<String, String> getMap() {
//			Map<String, String> map = new HashMap<String, String>();
//			map.put("GS01", staticData.get("GS01"));
//			map.put("GS02", staticData.get("GS02"));
//			map.put("GS03", staticData.get("GS03"));
//			map.put("GS04", getCurrentDateCCYYMMDD());
//			map.put("GS05", getCurrentTimeStampHHMM());
//			map.put("GS06", getGroupControlNumber());
//			map.put("GS07", staticData.get("GS07"));
//			map.put("GS08", staticData.get("GS08"));
//			return map;
//		}
		
		@Override
		public Map<String, String> getMap() {
			Map<String, String> map = new HashMap<String, String>();
			map.put("GS01", staticData.get("GS_GS01"));
			map.put("GS02", staticData.get("GS_GS02"));
			map.put("GS03", staticData.get("GS_GS03"));
			map.put("GS04", getCurrentDateCCYYMMDD());
			map.put("GS05", getCurrentTimeStampHHMM());
			map.put("GS06", getGroupControlNumber());
			map.put("GS07", staticData.get("GS_GS07"));
			map.put("GS08", staticData.get("GS_GS08"));
			return map;
		}
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
			map.put("GE02", getGroupControlNumber());
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
			map.put("IEA02", getInterchangeControlNumber());
			return map;
		}
	}
}
