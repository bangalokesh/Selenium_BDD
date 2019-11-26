package x12;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

public class Loop {

	private int segmentCount;
	private Map<String, Map<String, String>> segmentData;
	
	private List<Segment> segments;
	private List<Loop> childLoops;
	
	
	public Loop(String loopName, Map<String, Map<String, String>> data) {
		this.segmentData = data;
		this.segments = new LinkedList<Segment>();
		this.childLoops = new LinkedList<Loop>();
		this.segmentCount = 0;
	}
	
	public void addSegments(String segmentName, String segmentCode) {
		this.segments.add(new Segment(segmentName, this.segmentData.get(segmentCode)));
		this.segmentCount ++;
	}
	
	public void addSegments(String segmentName) {
		this.segments.add(new Segment(segmentName, this.segmentData.get(segmentName)));
		this.segmentCount ++;
	}
	
	
	public int getSegmentCount() {
		return this.segmentCount;
	}
	
	public void addLoop(Loop loop) {
		this.childLoops.add(loop);
		this.segmentCount += loop.getSegmentCount();
	}
	
	public String toString() {
		StringBuilder s = new StringBuilder();
		for(Segment seg : segments) {
			s.append(seg);
			s.append("\r\n");
		}
		for(Loop loop : this.childLoops) {
			s.append(loop);
			s.append("\r\n");
		}
		return s.toString().trim();
	}
}
