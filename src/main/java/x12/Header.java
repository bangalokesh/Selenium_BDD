package x12;

import java.util.HashMap;
import java.util.Map;

import pageclasses.BasePage;
import x12.enums.FileType;

public class Header extends BasePage {

	private ST ST_Segment;
	private BHT BHT_Segment;
	private BGN BGN_Segment;

	private int segmentCount;
	private FileType fileType;
	private Map<String, String> staticData;
	private String STControlNumber, ISAControlNumber, BGNControlNumber;

	public Header(FileType fileType, String ISAControlNumber, Map<String, String> staticData) {
		this.fileType = fileType;
		this.staticData = staticData;		
		this.ISAControlNumber = ISAControlNumber;
		
		if(this.fileType == FileType.Eligibility) {
			this.STControlNumber  = getEligibilitySTControlNumber();
		} else {
			this.STControlNumber = getControlNumber();
		}
		
		ST_Segment = new ST();
		this.segmentCount = 1;
		
		if(this.fileType == FileType.Claim || this.fileType== FileType.Eligibility)
			BHT_Segment = new BHT();
		else if(this.fileType == FileType.Enrollment) {
			BGNControlNumber = getControlNumber();
			BGN_Segment = new BGN();
		}
		this.segmentCount ++;
	}

	public int getSegmentCount() {
		return this.segmentCount;
	}

	public String getSTControlNumber() {
		return this.STControlNumber;
	}
	
	public String getBGNControlNumber() {
		return this.BGNControlNumber;
	}
	
	public String getEligibilitySTControlNumber() {
		return "0001";
	}

	public String toString() {
		StringBuilder str = new StringBuilder();
		str.append(ST_Segment);
		str.append("\r\n");
		if(this.fileType == FileType.Claim || this.fileType == FileType.Eligibility) {
			str.append(BHT_Segment);
		} else if(this.fileType == FileType.Enrollment) {
			str.append(BGN_Segment.toString());
		}
		return str.toString();
	}

	private class ST extends Segment implements Mapping {

		public ST() {
			super("ST");
			setValues(getMap());
		}

		@Override
		public Map<String, String> getMap() {
			Map<String, String> map = new HashMap<String, String>();
			map.put("ST01", staticData.get("ST01"));
			if(fileType== FileType.Eligibility) {
				map.put("ST02", staticData.get("SE02"));
			} else {
				map.put("ST02", getSTControlNumber()); // transaction set control number - 9 bytes
			}			
			map.put("ST03", staticData.get("ST03"));
			return map;
		}
	}

	private class BHT extends Segment implements Mapping {

		public BHT() {
			super("BHT");
			setValues(getMap());
		}

		@Override
		public Map<String, String> getMap() {
			Map<String, String> map = new HashMap<String, String>();
			map.put("BHT01", staticData.get("BHT01"));
			if(fileType== FileType.Eligibility) {
				map.put("BHT02", staticData.get("BHT02"));
			} else {
				map.put("BHT02", "00"); // if HEALTH CARE SERVICE LOCATION INFORMATION third part is 01 then it should
				// be 00 else it should be 18
			}
			map.put("BHT03", ISAControlNumber); // same as ISA Control Number
			map.put("BHT04", getCurrentDateCCYYMMDD()); // same date as date in GS Loop
			map.put("BHT05", getCurrentTimeStampHHMM()); // same time in GS Loop
			map.put("BHT06", staticData.get("BHT06"));
			return map;
		}
	}

	private class BGN extends Segment implements Mapping {

		public BGN() {
			super("BGN");
			setValues(getMap());
		}

		@Override
		public Map<String, String> getMap() {
			Map<String, String> map = new HashMap<String, String>();
			map.put("BGN01", staticData.get("BGN01"));
			map.put("BGN02", getBGNControlNumber());
			map.put("BGN03", getCurrentDateCCYYMMDD());
			map.put("BGN04", getCurrentTimeStampHHMM());
			map.put("BGN05", staticData.get("BGN05"));
			map.put("BGN06", staticData.get("BGN06"));
			map.put("BGN07", staticData.get("BGN07"));
			map.put("BGN08", staticData.get("BGN08"));
			return map;
		}
	}
}
