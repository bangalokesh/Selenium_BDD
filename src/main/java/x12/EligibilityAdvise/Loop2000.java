package x12.EligibilityAdvise;

import java.util.HashMap;
import java.util.Map;

import x12.Mapping;
import x12.Segment;
import x12.enums.ClaimDataPoint;

public class Loop2000 {

	private HL hl;
	private Loop2100 loop_2100;
	private Map<String, String> staticData;
	private int segmentCount;
	
	public int getSegmentCount() {
		return segmentCount;
	}

	public Loop2000(Map<String, String> data, int hlCounter) {
		this.staticData = data;
		hl = new HL(hlCounter+"");
		loop_2100 = new Loop2100();	
		this.segmentCount = 2;
	}

	public String toString() {
		StringBuilder loop = new StringBuilder();
		loop.append("\n");
		loop.append(hl);		
		loop.append(loop_2100);	
		return loop.toString();
	}
	
	private class Loop2100 {
		private NM1 nm1;
		public Loop2100() {
			nm1 = new NM1();
		}
		public String toString() {
			StringBuilder loop = new StringBuilder();
			loop.append("\n");
			loop.append(nm1);
			return loop.toString();
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
				map.put("NM103", staticData.get("2100_NM103"));
				map.put("NM104", staticData.get("2100_NM104"));
				map.put("NM105", staticData.get("2100_NM105"));
				map.put("NM106", staticData.get("2100_NM106"));
				map.put("NM107", staticData.get("2100_NM107"));
				map.put("NM108", staticData.get("2100_NM108"));
				map.put("NM109", staticData.get("2100_NM109"));
				return map;
			}
		}
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

}
