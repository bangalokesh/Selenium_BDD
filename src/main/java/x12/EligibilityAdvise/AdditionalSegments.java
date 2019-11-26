package x12.EligibilityAdvise;

import java.util.HashMap;
import java.util.Map;

import pageclasses.BasePage;
import x12.Mapping;
import x12.Segment;

public class AdditionalSegments extends BasePage {
	
	private HL hl1;
	private TRN trn1;
	private NM1 nm1;
	private DMG dmg1;
	private DTP dtp1;
	private HI hi1;
	private EQ eq1;
	Map<String, String> staticData;
	Map<String, String> memberData;
	private int segmentCount;
	
	public int getSegmentCount() {
		return segmentCount;
	}

	public AdditionalSegments(Map<String, String> staticData, Map<String, String> memberData, int hlCounter) {
		this.staticData = staticData;
		this.memberData = memberData;
		hl1 = new HL(hlCounter+"");
		trn1 = new TRN();
		nm1 = new NM1();
		dmg1 = new DMG();
		dtp1 = new DTP();
		eq1 = new EQ();
		this.segmentCount = 6;
		if(staticData.get("EQ01") == null || staticData.get("EQ01").equalsIgnoreCase(""))
			hi1 = new HI();
	}
	
	public String toString() {
		StringBuilder loop = new StringBuilder();
		loop.append("\n");
		loop.append(hl1);
		loop.append("\n");
		loop.append(trn1);
		loop.append("\n");
		loop.append(nm1);
		loop.append("\n");
		loop.append(dmg1);
		loop.append("\n");
		loop.append(dtp1);
		loop.append("\n");
		if(hi1 != null) {
			loop.append(hi1);
			loop.append("\n");
		}
		loop.append(eq1);
		
		return loop.toString();
	}
	
	private class HL extends Segment implements Mapping {
		String hlCounter;
		public HL(String hlCounter) {
			super("HL");
			this.hlCounter = hlCounter;
			setValues(getMap());
		}
		@Override
		public Map<String, String> getMap() {
			Map<String, String> map = new HashMap<String, String>();
			map.put("HL01", hlCounter);
			map.put("HL02", staticData.get("2000_HL02"));
			map.put("HL03", staticData.get("2000_HL03"));
			map.put("HL04", staticData.get("2000_HL04"));
			return map;
		}
	}
	
	private class NM1 extends Segment implements Mapping {
		public NM1() {
			super("NM1");
			setValues(getMap());
		}

		@Override
		public Map<String, String> getMap() {
			Map<String, String> map = new HashMap<String, String>();
			map.put("NM101", staticData.get("2100_NM101"));
			map.put("NM102", staticData.get("2100_NM102"));
			map.put("NM103", memberData.get("MEMBER_LAST_NAME"));
			map.put("NM104", memberData.get("MEMBER_FIRST_NAME"));
			map.put("NM105", memberData.get("MEMBER_MIDDLE_NAME"));
			map.put("NM106", staticData.get("2100_NM106"));
			map.put("NM107", staticData.get("2100_NM107"));
			map.put("NM108", staticData.get("2100_NM108"));			
			map.put("NM109", staticData.get("SupplementalID"));
			return map;
		}
	}
	
	private class TRN extends Segment implements Mapping{
		public TRN() {
			super("TRN");
			setValues(getMap());
		}
		@Override
		public Map<String, String> getMap() {
			Map<String, String> map = new HashMap<String, String>();
			map.put("TRN01", "1");
			map.put("TRN02", getControlNumber());
			map.put("TRN03", getControlNumber());
			return map;
		}
		
	}
	
	private class DMG extends Segment implements Mapping{
		public DMG() {
			super("DMG");
			setValues(getMap());
		}
		@Override
		public Map<String, String> getMap() {
			Map<String, String> map = new HashMap<String, String>();
			map.put("DMG01", staticData.get("DMG01"));
			map.put("DMG02", memberData.get("MEMBER_BIRTH_DATE").replace("-", ""));
			map.put("DMG03", memberData.get("MEMBER_GENDER"));
			return map;
		}
		
	}
	
	private class DTP extends Segment implements Mapping{
		public DTP() {
			super("DTP");
			setValues(getMap());
		}
		@Override
		public Map<String, String> getMap() {
			Map<String, String> map = new HashMap<String, String>();
			map.put("DTP01", staticData.get("DTP01"));
			map.put("DTP02", staticData.get("DTP02"));
			map.put("DTP03", getCurrentDateCCYYMMDD() + "-" + getDateAfterDaysinCCYYMMDD(2));
			return map;
		}
		
	}
	
	private class EQ extends Segment implements Mapping{
		public EQ() {
			super("EQ");
			setValues(getMap());
		}
		@Override
		public Map<String, String> getMap() {
			Map<String, String> map = new HashMap<String, String>();
			String eq01 = staticData.get("EQ01");
			if(eq01 == null || eq01.equalsIgnoreCase("")) {
				map.put("EQ02", staticData.get("EQ02-01") + staticData.get("EQ02-02"));
				map.put("EQ03", staticData.get("EQ03"));
				map.put("EQ04", staticData.get("EQ04"));
				map.put("EQ05", staticData.get("EQ05"));
			}
			else 
				map.put("EQ01", staticData.get("EQ01"));
			return map;
		}
		
	}
	
	private class HI extends Segment implements Mapping{
		public HI() {
			super("HI");
			setValues(getMap());
		}
		@Override
		public Map<String, String> getMap() {
			Map<String, String> map = new HashMap<String, String>();
			map.put("HI01", staticData.get("HI01-01") + staticData.get("HI01-02"));
			return map;
		}
		
	}

}
