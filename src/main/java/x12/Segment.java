package x12;

import java.util.Arrays;
import java.util.Map;
import java.util.Random;

public class Segment {

	private String[] values;
	private String segmentCode;

	public Segment(String segmentCode) {
		this.segmentCode = segmentCode;
	}
	
	public String getSegmentCode() {
		return this.segmentCode;
	}
	
	public Segment(String segmentCode, Map<String, String> segmentData) {
		this.segmentCode = segmentCode;
		setValues(segmentData);
	}

	public String getValue(int index) {
		return values[index];
	}
	
	public void replaceValue(int index, String value) {
		this.values[index] = value;
	}
	
	public void setValues(Map<String, String> map) {
		String[] keys = new String[map.size()];
		keys = map.keySet().toArray(keys);
		Arrays.sort(keys);
		values = new String[keys.length];
		if (segmentCode.equals("N4")) {
			String zip = map.get("N403");
			if(zip.length() == 5)
				map.replace("N403", map.get("N403") + ((new Random()).nextInt(8999) + 1000));
		}
		for (int i = 0; i < keys.length; i++)
			values[i] = map.get(keys[i]);
	}

	public void setValues(String[] values) {
		this.values = values;
	}

	public String toString() {
		StringBuilder segment = new StringBuilder();
		segment.append(segmentCode);
		for (String value : values) {
			segment.append("*");
			if (value == null)
				segment.append("");
			else
				segment.append(value);
		}
		segment.append("~");
		return segment.toString();
	}
}
