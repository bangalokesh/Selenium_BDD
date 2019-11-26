package x12.claims;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import x12.FileDataStructure;
import x12.Loop;
import x12.Segment;
import x12.enums.ClaimType;

public class Loop_2300 {

	private Segment clm;
	private List<Segment> dateTimes;
	private Segment cl1;
	
	private Segment ref;
	private Segment nte;
	
	private List<Segment> hiFields;
	
	protected Segment ref2;
	private Segment nm1;
	
	private Loop_2310 loop_2310;
	protected List<Loop> loop2400;

	protected String testID;
	protected int segmentCount;
	private ClaimType claimType;
	
	private Map<String, Map<String, String>> segmentData;
	private FileDataStructure dataHolder;

	public Loop_2300(String testID, ClaimType claimType, List<HashMap<String, String>> claimLineData,
			 FileDataStructure dataHolder) {
		
		this.testID = testID;
		this.claimType = claimType;
		this.segmentData = dataHolder.getSegments(dataHolder.getLoopData("2300"));
		this.dataHolder = dataHolder;
		loop2400 = new LinkedList<Loop>();

		clm = new Segment("CLM", this.segmentData.get("CLM"));
		this.segmentCount = 1;

		if(this.claimType == ClaimType.Institutional) {
			this.dateTimes = new LinkedList<Segment>();
			this.dateTimes.add(new Segment("DTP", segmentData.get("DTP1")));
			this.dateTimes.add(new Segment("DTP", segmentData.get("DTP2")));
			this.dateTimes.add(new Segment("DTP", segmentData.get("DTP3")));
			this.cl1 = new Segment("CL1", segmentData.get("CL1"));
			segmentCount += 4;
		} else {
			this.dateTimes = new LinkedList<Segment>();
			if (isInPatient(segmentData.get("CLM").get("CLM05"))) {
				this.dateTimes.add(new Segment("DTP", segmentData.get("DTP3")));
				segmentCount++;
				segmentData.remove("DTP3");
			}
		}
		
		
		
		ref = new Segment("REF", segmentData.get("REF"));
		nte = new Segment("NTE", segmentData.get("NTE"));
		this.segmentCount += 2;
		
		this.hiFields = new LinkedList<Segment>();
		if(this.claimType == ClaimType.Institutional) {
			hiFields.add(new Segment("HI", this.segmentData.get("HI1")));
			hiFields.add(new Segment("HI", this.segmentData.get("HI2")));
			hiFields.add(new Segment("HI", this.segmentData.get("HI3")));
			this.nm1 = new Segment("NM1", this.segmentData.get("NM1"));
			this.segmentCount += 4;
		} else {
			hiFields.add(new Segment("HI", segmentData.get("HI1")));
			this.segmentCount ++;
		}
		
		if (segmentData.get("REF2").get("2300_REF200") != null) {
			ref2 = new Segment("REF", segmentData.get("REF2"));
			this.segmentCount++;
		}
		
		String key1 = dataHolder.getSegments(dataHolder.getLoopData("2310A")).get("NM1").get("NM109");
		String key2 = dataHolder.getSegments(dataHolder.getLoopData("2310B")).get("NM1").get("NM109");
		loop_2310 = new Loop_2310(key1, key2);
	}
	
	private boolean isInPatient(String val) {
		return val.split(":")[0].equals("21") || val.split(":")[0].equals("31") || val.split(":")[0].equals("51")
				|| val.split(":")[0].equals("61");
	}

	public int getSegmentCount() {
		return this.segmentCount;
	}

	public String toString() {
		StringBuilder loop = new StringBuilder();
		loop.append(clm);
		loop.append("\r\n");
		
		if(this.claimType == ClaimType.Institutional) {
			for (Segment dtp : dateTimes) {
				loop.append(dtp);
				loop.append("\r\n");
			}
			if (cl1 != null) {
				loop.append(cl1);
				loop.append("\r\n");
			}
		}
		
		loop.append(ref);
		loop.append("\r\n");
		loop.append(nte);
		loop.append("\r\n");
		
		for (Segment hi : hiFields) {
			loop.append(hi);
			loop.append("\r\n");
		}
		
		if(this.claimType == ClaimType.Institutional) {
			loop.append(nm1);
			loop.append("\r\n");
		}
		
		loop.append(loop_2310.toString());
		
		return loop.toString().trim();
	}

	protected class Loop_2310 {

		private Segment Loop_2310A, Loop_2310B;

		public Loop_2310(String referringNPI, String renderingNPI) {
			if (referringNPI != null && !referringNPI.isEmpty()
					&& !segmentData.get("CLM").get("CLM05").split(":")[0].equals("21")) {
				Map<String, String> nm1A = dataHolder.getSegments(dataHolder.getLoopData("2310A")).get("NM1");
				nm1A.put("NM101", "DN");
				nm1A.put("NM102", "1");
				Loop_2310A = new Segment("NM1", nm1A);
				segmentCount++;
			}
			if (renderingNPI != null) {
				Map<String, String> nm1B = dataHolder.getSegments(dataHolder.getLoopData("2310B")).get("NM1");
				nm1B.put("NM101", "82");
				nm1B.put("NM102", "1");
				Loop_2310B = new Segment("NM1", nm1B);
				
				segmentCount++;
			}
		}

		@Override
		public String toString() {
			StringBuilder loop = new StringBuilder();
			if (Loop_2310A != null)
				loop.append(Loop_2310A.toString() + "\n");
			if (Loop_2310B != null)
				loop.append(Loop_2310B.toString() + "\n");
			return loop.toString();
		}
	}
}
