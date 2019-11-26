package x12;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;

public class FileDataStructure {

	private Set<String> loopCodes;
	private Map<String, Map<String, String>> loopClassifiedData;
	
	private Queue<HashMap<String, String>> loop2400s;
	
	private List<HashMap<String, String>> transactionRecords;
	
	public FileDataStructure() {
		this.loopCodes = new HashSet<String>();
		this.transactionRecords = new ArrayList<HashMap<String, String>>();
		this.loopClassifiedData = new HashMap<String, Map<String, String>>();
		this.loop2400s = new LinkedList<HashMap<String, String>>();
	}
	
	public void setTransactionRecords(List<HashMap<String, String>> records) {
		this.transactionRecords = records;
	}
	
	public Map<String, String> getNextTransaction(){
		return this.transactionRecords.remove(0);
	}
	
	
	
	/*
	 * Takes a Map and passes data loopwise to fileData.
	 * Separates out loop code and uses that as a key in file data
	 * to store it
	 * */
	public void addTableData(Map<String, String> data) {
		Object[] keys = data.keySet().toArray();
		
		for(Object key : keys) {
			String loop = key.toString().split("_")[0];
			if(!loopCodes.contains(loop)) {
				loopCodes.add(loop);
				loopClassifiedData.put(loop, new HashMap<String, String>());
			}
			String value = data.get(key);
			loopClassifiedData.get(loop).put(key.toString(), value);
		}
	 }
	
	public String getElement(String key) {
		return this.loopClassifiedData.get(key.split("_")[0]).get(key);
	}
	
	public void addElement(String key, String value) {
		String loop = key.toString().split("_")[0];
		if(!loopCodes.contains(loop)) {
			loopCodes.add(loop);
			loopClassifiedData.put(loop, new HashMap<String, String>());
		}
		loopClassifiedData.get(loop).put(key.toString(), value);
	}
	
	/*
	 * Uses Loop Data to classify segments out of it.
	 * Returns a Map of Maps where each Map represents a Segment
	 */
	public Map<String, Map<String, String>> getSegments(Map<String, String> loopData){
		Map<String, Map<String, String>> segments = new HashMap<String, Map<String, String>>();
		
		Object[] keys = loopData.keySet().toArray();
		
		for(Object key : keys) {
			if(!key.toString().contains("_"))
				continue;
			String segmentFullCode = key.toString().split("_")[1];
			String segmentCode = segmentFullCode.substring(0, segmentFullCode.length()-2);
			if(!segments.containsKey(segmentCode))
				segments.put(segmentCode, new HashMap<String, String>());
			
			segments.get(segmentCode).put(segmentFullCode, loopData.get(key.toString()));
		}
		return segments;
	}
	
	/*
	 * Specific to 2400 loops, it takes the 2400 loop data
	 * and adds to a separate list of maps
	 */
	public void add2400Loops(List<HashMap<String, String>> data) {
		this.loop2400s.addAll(data);
	}
	
	/*
	 * Adds all the static data to the 2400 loop map
	 * and returns the ready map
	 */
	public Map<String, Map<String, String>> getNext2400Map(){
		Map<String, String> loopData = this.loop2400s.poll();
		this.addTableData(loopData);
		return this.getSegments(this.getLoopData("2400"));
	}
	
	public Map<String, String> getLoopData(String key){
		return loopClassifiedData.get(key);
	}
	
	public String toString() {
		StringBuilder s = new StringBuilder();

		for(String key : loopClassifiedData.keySet()) {
			s.append(key);
			Map<String, Map<String, String>> loopData = getSegments(loopClassifiedData.get(key));
			for(String keyy : loopData.keySet()) {
				s.append("\t\t" + keyy + "\t");
				s.append(loopData.get(keyy));
				s.append("\n");
			}
			s.append("\n");
		}
		return s.toString();
	}
}
