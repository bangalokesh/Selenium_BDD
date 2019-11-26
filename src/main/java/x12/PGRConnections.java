package x12;

import java.util.HashMap;

import utils.Dbconn;

public class PGRConnections {

	private Dbconn dbaccess;
	
	public PGRConnections() {
		this.dbaccess = new Dbconn();
	}
	
	// This query is to get Member PCP from ReadyToEnroll table i.e. In Network PCP.
	public HashMap<String, String> getMemberPCP(String medicareID, String planID, String pbpID) {
		String query = "select PCPNPI AS PCP from [VelocityTestAutomation].[dbo].[test_data_readytoenroll] where MedcareID = '"
				+ medicareID + "';";
		HashMap<String, String> pcpNPI = dbaccess.getResultSet(query);
		if(pcpNPI.get("PCP").isEmpty()) {
			return getPcpInNetwork(planID, pbpID);
		}
		return getProviderData(pcpNPI.get("PCP"));
//		HashMap<String, String> temp = new HashMap<String, String>();
//		temp.put("LAST_NAME", "PREFERRED HOMECARE DME");
//		temp.put("FIRST_NAME", "");
//		temp.put("NPI", "1477594877");		
//		return temp;
	}
	
	// This query is to get IN Network PCP based on plan, pbp
	public HashMap<String, String> getPcpInNetwork(String planID, String pbpID) {
		String planQuery = "Select Designation AS program from [dbo].[product_details] where PlanID = '" + planID
				+ "' and PBPID = '" + pbpID + "';";
		HashMap<String, String> planCode = dbaccess.getResultSet(planQuery);
		String pcpQuery = "SELECT TOP 1 pt.Name AS CATEGORY, prov.ProvLastNM AS LAST_NAME, prov.ProvFirstNm AS FIRST_NAME, prov.ProvMidNm AS MID, dt.DegTypeDesc AS DEGREE\r\n"
				+ " , pract.FedrlTaxIDNum AS TAX_ID, prov.ProvNPIID AS NPI, pl.ProvLocAltID AS ProvLoc, l.[Std Mail Address Line 1] AS [ADDRESS], l.LocAddrLine2Nm AS SUITE, l.[Std Mail City] AS CITY\r\n"
				+ " , l.[Std Mail State] AS ST, l.[Std Mail ZIP] AS ZIP, l.CntyNm AS CNTY, pg.ProgDesc AS Network,pg.ProgCd as Program\r\n"
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
				+ "and l.[Std Mail Address Line 1] NOT LIKE '%BOX%' \r\n" + "ORDER BY  prov.ProvNPIID;";

		return dbaccess.getResultSetTwo(pcpQuery);
	}
	
	public HashMap<String, String> getProviderData(String NPI) {

		String query = "SELECT TOP 1 \r\n" 
				+ "pt.Name AS CATEGORY, \r\n"
				+ "prov.ProvLastNM AS LAST_NAME, "
				+ "prov.ProvFirstNm AS FIRST_NAME, "
				+ "prov.ProvMidNm AS MID, [ProvSuffixNm] AS SUFFIX, \r\n"
				+ "pract.FedrlTaxIDNum AS TAX_ID, "
				+ "prov.ProvNPIID AS NPI, \r\n"
				+ "l.[Std Mail Address Line 1] AS [ADDRESS], "
				+ "l.[Std Mail City] AS CITY\r\n"
				+ ", l.[Std Mail State] AS ST, "
				+ "l.[Std Mail ZIP] AS ZIP,\r\n" 
				+ "SPC.SPEC as [SPECIALTY CODE],\r\n"
				+ "TX.Code AS PRV03 \r\n" 
				+ "FROM [Profisee].[data].[tProvider] prov\r\n"
				+ "INNER JOIN [Profisee].[SPF].[PracticeParticipant] pp\r\n" + "       ON pp.ProvFK = prov.Code\r\n"
				+ "\r\n" + "INNER JOIN [Profisee].[data].[tPractice] pract\r\n"
				+ "       ON pract.Code = pp.PractcFK\r\n" + "\r\n"
				+ "INNER JOIN [Profisee].[data].[tDG_ProviderType] pt\r\n" + "       ON pt.Code = prov.ProvTypeFK\r\n"
				+ "\r\n" + "INNER JOIN [Profisee].[SPF].[ProviderLocation] pl\r\n"
				+ "       ON pl.PractcPrtcptFK = PP.PractcPrtcptPK\r\n" + "\r\n" 
				+ "INNER JOIN Profisee.data.tDG_LocationType lt\r\n" + "       ON lt.ID = pl.LocTypeFK\r\n" + "\r\n"
				+ "INNER JOIN [Profisee].[data].[tLocation] l\r\n" + "       ON l.Code = pl.LocFK\r\n" + "\r\n"
				+ "INNER JOIN [Profisee].[SPF].[PracticeParticipantSpecialty] ps\r\n"
				+ "       ON ps.PractcPrtcptFK = PP.PractcPrtcptPK\r\n"
				+ "INNER JOIN [Profisee].[data].[vDG_HealthcareSpecialty] hs  \r\n"
				+ "    ON hs.Code = ps.HlthcrSpcltyFK\r\n" + "Inner Join [Profisee].[data].[tDG_TaxonomyCode] TX\r\n"
				+ "       on TX.Code=hs.TaxonomyFK\r\n" + "Inner Join [ProviderMasterStage].[PIMS].[SPEC] SPC\r\n"
				+ "on SPC.Taxonomy=TX.MasterCd\r\n" + "where   \r\n" + "              prov.ProvNPIID = '" + NPI
				+ "' \r\n" + "                       and pl.ProvLocEndDt = '2999-12-31'\r\n"
				+ "and l.[Std Mail Address Line 1] NOT LIKE '%BOX%' \r\n"
				+ "                           ORDER BY  prov.ProvNPIID\r\n";
		HashMap<String, String> data = new HashMap<String, String>();
		data.putAll(dbaccess.getResultSetTwo(query));
		data.put("PCPID", NPI);
		return data;
	}
	
}
