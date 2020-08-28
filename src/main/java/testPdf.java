import java.util.HashMap;

import org.junit.Test;

public class testPdf {
	
	
	public void validatePDF(HashMap<String, String> data, String fileData ) {
		for (String ch1 : data.keySet()) {
			if (fileData.contains(data.get(ch1).trim())) {
				System.out.println(data.get(ch1).trim() + ": exist in file");
			} else {
				System.out.println(data.get(ch1).trim() + ": does not exist in file");
			}
		}
	}
	
	@Test
	public void testMethod() {
		
		String fileData = "hello world 1bcd march feb blue";
		
		HashMap<String, String> data = new HashMap<String, String>();

		data.put("A", "hello");
		data.put("B", "march");
		data.put("C", "feb");
		data.put("D", "blue");
		data.put("E", "1bcd");
		data.put("G", "remappingFunction");
		
		validatePDF(data, fileData ) ;
		
	}
	

}
