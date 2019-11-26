package x12.enrollments;

import java.util.HashMap;
import java.util.Map;

import x12.FileDataStructure;
import x12.Header;
import x12.Loop;
import x12.Mapping;
import x12.Segment;
import x12.enums.FileType;

public class Loop_ST {
	
	private Header header;
	private Loop loop1000A, loop1000B, loop1000C;
	private Loop loop2000, loop2100A, loop2100C;
	private Loop loop2300, loop2310;
	
	public Loop_ST(Map<String, String> staticData, String interchangeControlNumber, FileDataStructure eds) {
		this.header = new Header(FileType.Enrollment, interchangeControlNumber, staticData);
		
		loop1000A = new Loop("1000A", eds.getSegments(eds.getLoopData("1000A")));
	    loop1000B = new Loop("1000B", eds.getSegments(eds.getLoopData("1000B")));
		loop1000C = new Loop("1000C", eds.getSegments(eds.getLoopData("1000C")));
		
		loop2000 = new Loop("2000", eds.getSegments(eds.getLoopData("2000")));
		loop2000.addSegments("INS");
		loop2000.addSegments("REF", "REF1");
		loop2000.addSegments("REF", "REF2");
		loop2000.addSegments("REF", "REF3");
		loop2000.addSegments("REF", "REF4");
		loop2000.addSegments("DTP", "DTP1");
		loop2000.addSegments("DTP", "DTP2");
		loop2000.addSegments("DTP", "DTP3");
		loop2100A = new Loop("2100A", eds.getSegments(eds.getLoopData("2100A")));
		loop2100A.addSegments("NM1");
		loop2100A.addSegments("N3");
		loop2100A.addSegments("N4");
		loop2100A.addSegments("DMG");
		loop2100A.addSegments("LUI");
		loop2100C = new Loop("2100C", eds.getSegments(eds.getLoopData("2100C")));
		loop2100C.addSegments("NM1");
		loop2100C.addSegments("N3");
		loop2100C.addSegments("N4");
		loop2000.addLoop(loop2100A);
		loop2000.addLoop(loop2100C);
		
		loop2300 = new Loop("2300", eds.getSegments(eds.getLoopData("2300")));
		loop2300.addSegments("HD", "HD");
		loop2300.addSegments("DTP", "DTP1");
		loop2300.addSegments("DTP", "DTP2");
		loop2300.addSegments("REF", "REF1");
		loop2300.addSegments("REF", "REF2");
		loop2310 = new Loop("2310", eds.getSegments(eds.getLoopData("2310")));
		loop2310.addSegments("LX", "LX");
		loop2310.addSegments("NM1", "NM1");
		loop2310.addSegments("N3", "N3");
		loop2310.addSegments("N4", "N4");
		loop2300.addLoop(loop2310);
	}
	
	public String toString() {
		StringBuilder writer = new StringBuilder();
		writer.append(header.toString());
		writer.append("\r\n");
		writer.append(loop1000A.toString());
		writer.append("\r\n");
		writer.append(loop1000B.toString());
		writer.append("\r\n");
		writer.append(loop1000C.toString());
		writer.append("\r\n");
		
		writer.append(loop2000.toString());
		writer.append("\r\n");
		
		writer.append(loop2300.toString());
		writer.append("\r\n");
		writer.append((new SE(header.getSTControlNumber(), (3 
                + loop1000A.getSegmentCount() 
                + loop1000B.getSegmentCount() 
                + loop1000C.getSegmentCount() 
                + loop2000.getSegmentCount() 
                + loop2300.getSegmentCount()))
					).toString());
		return writer.toString();
	}
	
	private class SE extends Segment implements Mapping {
		private String controlNumber;
		private int segmentCount;

		public SE(String ST_ControlNumber, int segmentCount) {
			super("SE");
			this.segmentCount = segmentCount;
			this.controlNumber = ST_ControlNumber;
			setValues(getMap());
		}

		@Override
		public Map<String, String> getMap() {
			Map<String, String> map = new HashMap<String, String>();
			map.put("SE01", "" + segmentCount); // number of segments
			map.put("SE02", this.controlNumber); // ST02
			return map;
		}
	}
}
