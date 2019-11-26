package x12.claims;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.imsweb.x12.Loop;
import com.imsweb.x12.Segment;
import com.imsweb.x12.reader.X12Reader;
import com.imsweb.x12.reader.X12Reader.FileType;


class Structure{
	Map<String, String> claim;
	List<Map<String, String>> lxClm;
	
	public Structure(Map<String, String> claim, List<Map<String, String>> lxCLM) {
		this.claim = claim;
		this.lxClm = lxCLM;
	}
	
	public Map<String, String> getClaim(){
		return this.claim;
	}
	
	public List<Map<String, String>> getLXClm(){
		return this.lxClm;
	}
	
	public String toString() {
		StringBuilder s = new StringBuilder();
		s.append(claim.toString());
		s.append("\n");
		for(Map<String, String> m : lxClm) {
			s.append("\t" + m.toString() + "\n");
		}
		return s.toString();
	}
}

public class FileParser835 {
	public List<Structure> get835FileData(File file) {
		X12Reader reader;

		try {
			// put folder name, and add multiple files to the folder.
			// read the files and get the subscribers
			reader = new X12Reader(FileType.ANSI835_5010_X221, file);

			List<Loop> ISALoops = reader.getLoops();
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File("out.txt")));

			List<Structure> data = new LinkedList<Structure>();
//			// for every ISA Loop, extract the ST Loop
			for (Loop ISALoop : ISALoops) {
				List<Loop> ST_LOOPs = ISALoop.findLoop("ST_LOOP");

//				// for every ST Loop, there is a HEADER DETAIL pair
//				// get the DETAIL Loop
				for (Loop ST_LOOP : ST_LOOPs) {
					List<Loop> details = ST_LOOP.findLoop("DETAIL");
					for (Loop detail : details) {
						Loop loop2100 = detail.getLoop("2100");
						List<Loop> loop2110 = detail.findLoop("2110");

						Map<String, String> claim = new HashMap<String, String>();
						
						Segment clp = loop2100.getSegment("CLP");
						claim.put("CLP01", clp.getElement("CLP01").toString());
						claim.put("CLP02", clp.getElement("CLP02").toString());
						claim.put("CLP03", clp.getElement("CLP03").toString());
						claim.put("CLP04", clp.getElement("CLP04").toString());
						claim.put("CLP05", clp.getElement("CLP05").toString());
						claim.put("CLP07", clp.getElement("CLP07").toString());
						List<Segment> nm1 = loop2100.findSegment("NM1");
						for (Segment seg : nm1) {
							if (seg.getElement("NM101").toString().equals("QC")) {
								claim.put("NM1QC03", seg.getElement("NM103").toString());
								claim.put("NM1QC04", seg.getElement("NM104").toString());
								claim.put("NM1QC09", seg.getElement("NM109").toString());
							}
							if(seg.getElement("NM101").toString().equals("IL")) {
								claim.put("NM1IL03", seg.getElement("NM103").toString());
								claim.put("NM1IL04", seg.getElement("NM104").toString());
								claim.put("NM1IL09", seg.getElement("NM109").toString());
							}
						}
						
						List<Map<String, String>> subData = new LinkedList<Map<String, String>>();
						for(Loop l2110 : loop2110) {
							Map<String, String> clmLX = new HashMap<String, String>();
							Segment svc = l2110.getSegment("SVC");
							clmLX.put("SVC01", svc.getElement("SVC01").toString());
							clmLX.put("SVC02", svc.getElement("SVC02").toString());
							clmLX.put("SVC05", svc.getElement("SVC05").toString());
							
							List<Segment> cas = l2110.findSegment("CAS");
							for (Segment seg : cas) {
								if (seg.getElement("CAS01").toString().equals("PR")) {
									clmLX.put("CASPR01", seg.getElement("CAS01").toString());
									clmLX.put("CASPR03", seg.getElement("CAS03").toString());
								}
								if(seg.getElement("CAS01").toString().equals("CO")) {
									clmLX.put("CASCO01", seg.getElement("CAS01").toString());
									clmLX.put("CASCO03", seg.getElement("CAS03").toString());
								}
							}
							
							clmLX.put("REF02", l2110.getSegment("REF").getElement("REF02").toString());
							clmLX.put("AMT02", l2110.getSegment("AMT").getElement("AMT02").toString());
							clmLX.put("DTM02", l2110.getSegment("DTM").getElement("DTM02").toString());
							subData.add(clmLX);
						}
						
						data.add(new Structure(claim, subData));
					}
				}
			}

			writer.close();
			return data;
		} catch (IOException e) {
			e.printStackTrace();
		}

		return null;
	}

	public static void main(String[] args) {
		FileParser835 e = new FileParser835();
		List<Structure> data = e.get835FileData(new File("835 wPR Amt_Copay.txt"));
		System.out.println(data.toString());
		for(Structure s : data) {
			Map<String, String> transaction = s.getClaim();
			List<Map<String, String>> claims = s.getLXClm();
			
		}
	}
}
