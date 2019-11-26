package x12.enrollments;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import pageclasses.CommonMethods;
import utils.Const;
import utils.Dbconn;
import x12.FileDataStructure;
import x12.Loop_ISA;

public class EnrollmentFileGenerator {

	private Dbconn dbaccess;
	
	private Map<String, String> staticData;
	private Loop_ISA isa_loop;
	
	private FileDataStructure eds;
	
	private String groupC, groupD, groupE;

	public EnrollmentFileGenerator() {
		this.dbaccess = new Dbconn();
		
		eds = new FileDataStructure();
		eds.addTableData(getSenderReceiver());
		eds.addTableData(getBrokerMemberProviderStaticData());
		
		this.staticData = getISAControlData();
		this.isa_loop = new Loop_ISA(this.staticData);
	}
	
	public void createEnrollmentFile(String filename) {
		try {
			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
					Const.EnrollmentFilePath + filename + CommonMethods.getCurrentDateTime() + ".txt")));
			writer.append(this.isa_loop.getControlHeaders());
			
			List<String> records = new LinkedList<String>();
			BufferedReader groupA = new BufferedReader(new FileReader(new File(filename)));
			String medIDA;
			String medQuery = "(";
			while((medIDA = groupA.readLine()) != null) {
				medIDA = medIDA.replaceAll(",", "");
				medQuery += "'" + medIDA + "',";
				medIDA = medIDA.trim();
				records.add(medIDA);
			}
			medQuery = medQuery.substring(0, medQuery.length()-1);
			medQuery += ")";
			groupA.close();
			if(filename.equals("enrollmentInputC.txt"))
				this.groupC = medQuery;
			if(filename.equals("enrollmentInputD.txt"))
				this.groupD = medQuery;
			if(filename.equals("enrollmentInputE.txt"))
				this.groupE = medQuery;
			
			List<HashMap<String, String>> CVTMemberData = getCVTMemberData(medQuery);
			
			int memberCounter = 0;
			Map<String, String> member;
			
			Map<String, String> pcpData;
			
			Loop_ST stloop;
			for(String record : records) {
				eds.addTableData(getBrokerAgent());
			
				if(memberCounter >= CVTMemberData.size())
					break;
				member = CVTMemberData.get(memberCounter);
				String planID = member.get("PLANID_ENROLLMENT");
				String pbpID =  member.get("PBPID");
				
				eds.addTableData(member);
				
				pcpData = getPcpInNetwork(planID, pbpID);
				eds.addTableData(pcpData);
				
				memberCounter++;

				stloop = new Loop_ST(staticData, isa_loop.getInterchangeControlNumber(), eds);
				writer.append(stloop.toString());
				writer.append("\r\n");
			}
			this.isa_loop.setTransactionCount(records.size());
			writer.append(this.isa_loop.getControlTrailers());
			writer.append("\r\n");
			writer.close();
		} catch(Exception e) {
			e.printStackTrace();
		}
	}
	
	private List<HashMap<String, String>> getCVTMemberData(String medQuery) {
		String query = "SELECT  " +
				"       [dbo].[CVT_DSINFO].[EFFECTIVESTARTDATE] AS [2000_DTP103]\r\n" + 
				"      ,[dbo].[CVT_MemberData].[MEMBERID] AS '2000_REF102'\r\n" + 
				"      ,[dbo].[CVT_MemberData].[MEMBERID] AS '2300_REF102'\r\n" + 
				"      ,[dbo].[CVT_MemberData].[MEMBERID] AS '2100A_NM109'\r\n" + 
				"	   ,[dbo].[CVT_MemberData].[MEDICARENUMBER] AS '2000_REF302'\r\n" + 
				"      ,[dbo].[CVT_MemberData].[MEMBERLASTNAME] AS '2100A_NM103'\r\n" + 
//				"      ,[dbo].[CVT_MemberData].[MEMBERLASTNAME] AS '2100C_NM103'\r\n" + 
				"      ,[dbo].[CVT_MemberData].[MEMBERFIRSTNAME] AS '2100A_NM104'\r\n" + 
//				"      ,[dbo].[CVT_MemberData].[MEMBERFIRSTNAME] AS '2100C_NM104'\r\n" + 
				"      ,[dbo].[CVT_MemberData].[MEMBERMIDDLEINITIAL] AS '2100A_NM105'\r\n" + 
//				"      ,[dbo].[CVT_MemberData].[MEMBERMIDDLEINITIAL] AS '2100C_NM105'\r\n" + 
				"      ,[dbo].[CVT_MemberData].[MEMBERPREFIX] AS '2100A_NM106'\r\n" + 
//				"      ,[dbo].[CVT_MemberData].[MEMBERPREFIX] AS '2100C_NM106'\r\n" + 
				"      ,[dbo].[CVT_MemberData].[MEMBERSUFFIX] AS '2100A_NM107'" +
//				"      ,[dbo].[CVT_MemberData].[MEMBERSUFFIX] AS '2100C_NM107'" +
				"      ,[dbo].[CVT_MemberData].[HOMEPHONE] AS '2100A_PER02'" +
				"      ,[dbo].[CVT_MemberData].[ADDRESS1] AS '2100A_N301'" +
				"      ,[dbo].[CVT_MemberData].[CITY] AS '2100A_N401'" +
				"      ,[dbo].[CVT_MemberData].[STATE] AS '2100A_N402'" +
				"      ,[dbo].[CVT_MemberData].[ZIPCODE] AS '2100A_N403'" +
				"      ,[dbo].[CVT_MemberData].[ADDRESS1] AS '2100C_N301'" +
				"      ,[dbo].[CVT_MemberData].[CITY] AS '2100C_N401'" +
				"      ,[dbo].[CVT_MemberData].[STATE] AS '2100C_N402'" +
				"      ,[dbo].[CVT_MemberData].[ZIPCODE] AS '2100C_N403'" +
				"      ,[dbo].[CVT_MemberData].[PLANID_ENROLLMENT]" +
				"      ,[dbo].[CVT_MemberData].[PBPID]" +
				"      ,[dbo].[CVT_MemberData].[MEMBERBIRTHDATE] AS '2100A_DMG02'" +
				"      ,[dbo].[CVT_MemberData].[MEMBERGENDER] AS '2100A_DMG03'" +
				"	   ,[dbo].[product_details].[GroupID] AS '2000_REF202'\r\n" + 
				"	   ,[dbo].[product_details].[ProductID] AS '2300_REF202'\r\n" + 
				"FROM " +
				"[VelocityTestAutomation].[dbo].[CVT_MemberData],  \r\n" + 
				"				[VelocityTestAutomation].[dbo].[product_details],\r\n" + 
				"				[VelocityTestAutomation].[dbo].[CVT_DSINFO]\r\n" + 
				"		   WHERE [VelocityTestAutomation].[dbo].[CVT_MemberData].PRODUCTID = [VelocityTestAutomation].[dbo].[product_details].ProductID and\r\n" + 
				"		         [VelocityTestAutomation].[dbo].[CVT_MemberData].MEMBERID = [VelocityTestAutomation].[dbo].[CVT_DSINFO].[MEMBER_ID]\r\n" + 
				"	 and [VelocityTestAutomation].[dbo].[CVT_DSINFO].[DSCODE] = 'MBI' "/*and [VelocityTestAutomation].[dbo].[CVT_MemberData].[Track] is Null*/+" and [VelocityTestAutomation].[dbo].[CVT_MemberData].MEDICARENUMBER in 	" + medQuery;
//				+ "[VelocityTestAutomation].[dbo].[CVT_MemberData],  [VelocityTestAutomation].[dbo].[product_details]\r\n" + 
//				"WHERE [VelocityTestAutomation].[dbo].[CVT_MemberData].PRODUCTID = [VelocityTestAutomation].[dbo].[product_details].ProductID";
		
		List<HashMap<String, String>> data = dbaccess.getListOfHashMapsFromResultSet(query);
		for(Map<String, String> map : data) {
			map.put("2000_DTP203", "20190101");
			map.put("2000_DTP303", "20181228");
			map.put("2300_HD01", "021");
			map.put("2300_HD03", "HLT");
			map.put("2300_HD04", map.get("PBPID") + "N");
			map.put("2300_HD05", "EMP");
			map.put("2300_DTP103", "20190101");
			map.put("2300_DTP203", "20181228");
			map.put("2000_REF401", "DX");
			map.put("2000_REF402", "000");
		}
		return data;
//		return dbaccess.getListOfHashMapsFromResultSet(query);
	}

	private Map<String, String> getISAControlData(){
		String query = "SELECT 	 [ISA01] AS [ISA_ISA01]\r\n" + 
				"      		  	,[ISA03] AS [ISA_ISA03]\r\n" + 
				"      			,[ISA05] AS [ISA_ISA05]\r\n" + 
				"      			,[ISA06] AS [ISA_ISA06]\r\n" + 
				"      			,[ISA07] AS [ISA_ISA07]\r\n" + 
				"      			,[ISA08] AS [ISA_ISA08]\r\n" + 
				"      			,[ISA11] AS [ISA_ISA11]\r\n" + 
				"      			,[ISA12] AS [ISA_ISA12]\r\n" + 
				"      			,[ISA14] AS [ISA_ISA14]\r\n" + 
				"      			,[ISA15] AS [ISA_ISA15]\r\n" + 
				"      			,[ISA16] AS [ISA_ISA16]\r\n" + 
				"      			,[GS01] AS [GS_GS01]\r\n" + 
				"      			,[GS02] AS [GS_GS02]\r\n" + 
				"      			,[GS03] AS [GS_GS03]\r\n" + 
				"      			,[GS07] AS [GS_GS07]\r\n" + 
				"      			,[GS08] AS [GS_GS08]\r\n" + 
				"      ,[ST01]\r\n" + 
				"      ,[ST03]\r\n" + 
				"      ,[BGN01]\r\n" + 
				"      ,[BGN05]\r\n" + 
				"      ,[BGN06]\r\n" + 
				"      ,[BGN07]\r\n" + 
				"      ,[BGN08]\r\n" + 
				"  FROM [VelocityTestAutomation].[dbo].[834_header_data]";
		return dbaccess.getListOfHashMapsFromResultSet(query).get(0);
	}
	
	private Map<String, String> getBrokerMemberProviderStaticData(){
		String query = "SELECT TOP 1000 [1000C_N101]\r\n" + 
				"      ,[1000C_N103]\r\n" + 
				"      ,[2000_INS01]\r\n" + 
				"      ,[2000_INS02]\r\n" + 
				"      ,[2000_INS03]\r\n" + 
				"      ,[2000_INS04]\r\n" + 
				"      ,[2000_INS05]\r\n" + 
				"      ,[2000_INS06]\r\n" + 
				"      ,[2000_INS07]\r\n" + 
				"      ,[2000_INS08]\r\n" + 
				"      ,[2000_REF101]\r\n" + 
				"      ,[2000_REF201]\r\n" + 
				"      ,[2000_REF301]\r\n" + 
				"      ,[2000_DTP101]\r\n" + 
				"      ,[2000_DTP102]\r\n" + 
				"      ,[2000_DTP201]\r\n" + 
				"      ,[2000_DTP202]\r\n" + 
				"      ,[2000_DTP301]\r\n" + 
				"      ,[2000_DTP302]\r\n" + 
				"      ,[2100A_NM101]\r\n" + 
				"      ,[2100A_NM102]\r\n" + 
				"      ,[2100A_NM108]\r\n" + 
//				"      ,[2100A_NM108] AS '2100C_NM108'\r\n" + 
				"      ,[2100A_PER01]\r\n" + 
				"      ,[2100A_PER03]\r\n" + 
				"      ,[2100A_DMG01]\r\n" + 
				"      ,[2100_LUI01] AS '2100A_LUI01'\r\n" + 
				"      ,[2100_LUI02] AS '2100A_LUI02'\r\n" + 
				"      ,[2100C_NM101]\r\n" + 
				"      ,[2100C_NM102]\r\n" + 
				"      ,[2300_DTP101]\r\n" + 
				"      ,[2300_DTP102]\r\n" + 
				"      ,[2300_DTP201]\r\n" + 
				"      ,[2300_DTP202]\r\n" + 
				"      ,[2300_REF101]\r\n" + 
				"      ,[2300_REF201]\r\n" + 
				"      ,[2310_LX01]\r\n" + 
				"      ,[2310_NM101]\r\n" + 
				"      ,[2310_NM102]\r\n" + 
				"      ,[2310_NM108]\r\n" + 
				"      ,[2310_NM110]\r\n" + 
				"  FROM [VelocityTestAutomation].[dbo].[834_broker_member_provider_static_data]";
		Map<String, String> data = dbaccess.getListOfHashMapsFromResultSet(query).get(0);
		data.put("2300_HD02", "");
		return data;
	}
	
	public Map<String, String> getSenderReceiver(){
		String query = "SELECT [1000A_N101]\r\n" + 
				"      ,[1000A_N102]\r\n" + 
				"      ,[1000A_N103]\r\n" + 
				"      ,[1000A_N104]\r\n" + 
				"      ,[1000B_N101]\r\n" + 
				"      ,[1000B_N102]\r\n" + 
				"      ,[1000B_N103]\r\n" + 
				"      ,[1000B_N104]\r\n" + 
				"  FROM [VelocityTestAutomation].[dbo].[834_Sender_Receiver]";
		return dbaccess.getListOfHashMapsFromResultSet(query).get(0);
	}
	
	public Map<String, String> getBrokerAgent(){
		String query = "SELECT \r\n" + 
				"       [AgentTIN] AS '1000C_N104' \r\n" + 
				"      ,[AgentName] AS '1000C_N102' \r\n" + 
				"  FROM [VelocityTestAutomation].[dbo].[agent]";
		List<HashMap<String, String>> data = dbaccess.getListOfHashMapsFromResultSet(query);
		int count = data.size();
		int index = (new Random()).nextInt(count);
		return data.get(index);
	}
	
//	public List<HashMap<String, String>> getDynamicData(){
//		String query = "SELECT " + 
//				"       [2000_DTP103]\r\n" + 
//				"      ,[2000_DTP203]\r\n" + 
//				"      ,[2000_DTP303]\r\n" + 
//				"      ,[2300_HD01]\r\n" + 
//				"      ,[2300_HD03]\r\n" + 
//				"      ,[2300_HD04]\r\n" + 
//				"      ,[2300_HD05]\r\n" + 
//				"      ,[2300_DTP103]\r\n" + 
//				"      ,[2300_DTP203]\r\n" +  
//				"      ,[2310_NM109]\r\n" + 
//				"      ,[2320_COB01]\r\n" + 
//				"      ,[2320_COB02]\r\n" + 
//				"      ,[2320_COB03]\r\n" + 
//				"      ,[2320_REF101]\r\n" + 
//				"      ,[2320_REF102]\r\n" + 
//				"      ,[2320_REF201]\r\n" + 
//				"      ,[2320_REF202]\r\n" + 
//				"      ,[2320_DTP101]\r\n" + 
//				"      ,[2320_DTP102]\r\n" + 
//				"      ,[2320_DTP103]\r\n" + 
//				"      ,[2320_DTP201]\r\n" + 
//				"      ,[2320_DTP202]\r\n" + 
//				"      ,[2320_DTP203]\r\n" + 
//				"  FROM [VelocityTestAutomation].[dbo].[834_member_info]";
//		return dbaccess.getListOfHashMapsFromResultSet(query);
//	}
	
	public List<HashMap<String, String>> getDynamicData(){
		String query = "SELECT " + 
				
				"       [2000_DTP203]\r\n" + 
				"      ,[2000_DTP303]\r\n" + 
				"      ,[2300_HD01]\r\n" + 
				"      ,[2300_HD03]\r\n" + 
				"      ,[2300_HD04]\r\n" + 
				"      ,[2300_HD05]\r\n" + 
				"      ,[2300_DTP103]\r\n" + 
				"      ,[2300_DTP203]\r\n" +  
				"      ,[2310_NM109]\r\n" + 
				"      ,[2320_COB01]\r\n" + 
				"      ,[2320_COB02]\r\n" + 
				"      ,[2320_COB03]\r\n" + 
				"      ,[2320_REF101]\r\n" + 
				"      ,[2320_REF102]\r\n" + 
				"      ,[2320_REF201]\r\n" + 
				"      ,[2320_REF202]\r\n" + 
				"      ,[2320_DTP101]\r\n" + 
				"      ,[2320_DTP102]\r\n" + 
				"      ,[2320_DTP103]\r\n" + 
				"      ,[2320_DTP201]\r\n" + 
				"      ,[2320_DTP202]\r\n" + 
				"      ,[2320_DTP203]\r\n" + 
				"  FROM [VelocityTestAutomation].[dbo].[834_member_info]";
		return dbaccess.getListOfHashMapsFromResultSet(query);
	}

	
	public HashMap<String, String> getPcpInNetwork(String planID, String pbpID) {
        String planQuery = "Select Designation AS program from [dbo].[product_details] where PlanID = '" + planID
                     + "' and PBPID = '" + pbpID + "';";
        HashMap<String, String> planCode = dbaccess.getResultSet(planQuery);
        String pcpQuery = "SELECT TOP 1 "
        		+ "pt.Name AS CATEGORY, "
//        		+ "prov.ProvLastNM AS '2310_NM103', "
//        		+ "prov.ProvFirstNm AS '2310_NM104', "
//        		+ "prov.ProvMidNm AS '2310_NM105', "
        		+ "dt.DegTypeDesc AS DEGREE, "
        		+ "pract.FedrlTaxIDNum AS TAX_ID, "
        		+ "prov.ProvNPIID AS '2310_NM109', "
        		+ "pl.ProvLocAltID AS ProvLoc, "
        		+ "l.[Std Mail Address Line 1] AS '2310_N301', "
        		+ "l.LocAddrLine2Nm AS SUITE, "
        		+ "l.[Std Mail City] AS '2310_N401', "
                + "l.[Std Mail State] AS '2310_N402', "
                + "l.[Std Mail ZIP] AS '2310_N403', "
                + "l.CntyNm AS CNTY, "
                + "pg.ProgDesc AS Network,"
                + "pg.ProgCd as Program\r\n"
                     + " FROM [Profisee].[data].[tProvider] prov\r\n"
                     + " INNER JOIN [Profisee].[SPF].[PracticeParticipant] pp ON pp.ProvFK = prov.Code\r\n"
                     + " INNER JOIN [Profisee].[data].[tPractice] pract ON pract.Code = pp.PractcFK\r\n"
                     + " INNER JOIN [profisee].[SPF].[PracticeParticipantProgramContract] pppc ON pppc.PractcPrtcptFK = PP.PractcPrtcptPK\r\n"
                     + " INNER JOIN ProviderMasterHub.HUB.vProgram pg ON pg.ProgPK = pppc.ProgFK\r\n"
                     + " INNER JOIN [Profisee].[data].[tDG_ProviderType] pt  ON pt.Code = prov.ProvTypeFK\r\n"
                     + " INNER JOIN ProviderMasterHub.HUB.vDegreeType dt ON dt.DegTypePK = prov.DegTypeFK\r\n"
                     + " INNER JOIN [Profisee].[SPF].[ProviderLocation] pl ON pl.PractcPrtcptFK = PP.PractcPrtcptPK \r\n"
                     + " INNER JOIN Profisee.data.tDG_LocationType lt ON lt.ID = pl.LocTypeFK \r\n"
                     + " INNER JOIN [Profisee].[data].[tLocation] l   ON l.Code = pl.LocFK \r\n"
                     + " INNER JOIN [Profisee].[SPF].[PracticeParticipantSpecialty] ps ON ps.PractcPrtcptFK = PP.PractcPrtcptPK\r\n"
                     + " INNER JOIN [Profisee].[data].[vDG_HealthcareSpecialty] hs ON hs.Code = ps.HlthcrSpcltyFK\r\n"
                     + " INNER JOIN [Profisee].[data].[tBenefitNetwork] bn ON bn.Program = pg.ProgCd\r\n"
                     + " INNER JOIN [Profisee].[data].[tDG_SourceSystem] SR ON prov.SrcSysCd = SR.ID\r\n"
                     + " WHERE pt.Name in  ('Individual') AND pg.ProgCd = '" + planCode.get("program") + "'\r\n"
                     + " AND  pppc.PriCarePhyscnInd = 1 AND pppc.ProvAccptNewPatInd = 1\r\n"
                     + "and l.[Std Mail Address Line 1] NOT LIKE '%BOX%' \r\n"
                     + "ORDER BY  prov.ProvNPIID;";

        HashMap<String, String> data = dbaccess.getResultSetTwo(pcpQuery);
        data.put("2310_NM103", "");
        data.put("2310_NM104", "");
        data.put("2310_NM105", "");
        data.put("2310_NM106", "");
        data.put("2310_NM107", "");
        return data;
	}
	
	public static void main(String[] args) {
		EnrollmentFileGenerator gen = new EnrollmentFileGenerator();
		gen.createEnrollmentFile("enrollmentInputC.txt");
		System.out.println("C Complete");
		
//		gen.createEnrollmentFile("enrollmentInputD.txt");
//		System.out.println("D Complete");
//		
//		gen.createEnrollmentFile("enrollmentInputE.txt");
//		System.out.println("E Complete");
		
//		gen.updateTrackinDBString(gen.groupC);
//		gen.updateTrackinDBString(gen.groupD);
//		gen.updateTrackinDBString(gen.groupE);
		
	}
	
	public void updateTrackinDBString (String groupName) {
		String query =  "Update [VelocityTestAutomation].[dbo].[CVT_MemberData] set Track = CVT" + getCurrentDate() 
		+ "	WHERE [VelocityTestAutomation].[dbo].[CVT_MemberData].PRODUCTID = [VelocityTestAutomation].[dbo].[product_details].ProductID and\r\n" + 
				"		         [VelocityTestAutomation].[dbo].[CVT_MemberData].MEMBERID = [VelocityTestAutomation].[dbo].[CVT_DSINFO].[MEMBER_ID]\r\n" + 
				"	 and [VelocityTestAutomation].[dbo].[CVT_DSINFO].[DSCODE] = 'MBI' and [VelocityTestAutomation].[dbo].[CVT_MemberData].[Track] is Null and [VelocityTestAutomation].[dbo].[CVT_MemberData].MEDICARENUMBER in 	" + groupName;
	
		dbaccess.executeQuery(query);
	}
	
	public String getCurrentDate() {
		SimpleDateFormat sdf = new SimpleDateFormat("yyMMdd");
		String currentDate = sdf.format(new Date());
		return currentDate;
	}
}
