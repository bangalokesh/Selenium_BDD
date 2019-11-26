package processes;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;

import com.aventstack.extentreports.Status;

import managers.PageManager;
import pageclasses.BasePage;
import processes.M360EnrollmentProcess;
import utils.Dbconn;

public class ProviderProcess extends BasePage {

	public static final org.apache.log4j.Logger logger = org.apache.log4j.Logger
			.getLogger(M360EnrollmentProcess.class.getName());
	Dbconn db = new Dbconn();
	PageManager pm = new PageManager();

	public String queryProviderMethod(String progCd) {
		String queryProvider = " SELECT [ID],[CATEGORY],[LAST NAME] AS LAST_NAME,[FIRST NAME] AS FIRST_NAME,[MID], CONCAT([PRIMARY ADDRESS], [SUITE]) AS ADDRESS, [CITY],[ST],[ZIP],[CNTY], [TAX ID] AS TAX_ID,  [NPI1] AS NPI , [MDH], [SPEC] AS PRIMARY_SPECIALTY \r\n"
				+ " FROM [VelocityTestAutomation].[dbo].[BCBSAZProv_ActiveOnly]\r\n" + " WHERE " + progCd + " = "
				+ progCd;

		return queryProvider;
	}

	public void executeMDH_Validation() {
		try {
			String query = queryProviderMethod("MDH");
			validateProvider(query, "MDH");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Test Failed in ProviderProcess executeMDH_Validation method");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void executeMHT_Validation() {
		try {
			String query = queryProviderMethod("MHT");
			validateProvider(query, "MHT");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Test Failed in ProviderProcess executeMHT_Validation method");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void executeMMH_Validation() {
		try {
			String query = queryProviderMethod("MMH");
			validateProvider(query, "MMH");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Test Failed in ProviderProcess executeMMH_Validation method");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void executeMPH_Validation() {
		try {
			String query = queryProviderMethod("MPH");
			validateProvider(query, "MPH");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Test Failed in ProviderProcess executeMPH_Validation method");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void executeMPM_Validation() {
		try {
			String query = queryProviderMethod("MPM");
			validateProvider(query, "MPM");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Test Failed in ProviderProcess executeMPM_Validation method");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void executeMPP_Validation() {
		try {
			String query = queryProviderMethod("MPP");
			validateProvider(query, "MPP");
		} catch (Exception e) {
			e.printStackTrace();
			try {
				reportFail("Test Failed in ProviderProcess executeMPP_Validation method");
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
	}

	public void executeProviderValidation() {
		executeMDH_Validation();
		executeMPP_Validation();
		executeMPM_Validation();
		executeMHT_Validation();
		executeMMH_Validation();
		executeMPH_Validation();
	}

	public boolean validateProvider(String query, String type) {
		boolean flag = true;
		try {
			List<HashMap<String, String>> list = db.getListOfHashMapsFromResultSet(query);
			logger.info(list.size());
			for (HashMap<String, String> row : list) {
				HashMap<String, String> temptestData = new HashMap<String, String>();
				for (String key : row.keySet()) {
					temptestData.put(key, row.get(key));
				}
				flag = true;
				logger.info(temptestData);

				test = extent.createTest(
						"Provider Validation test for " + temptestData.get("TAX_ID") + "_" + temptestData.get("NPI"));
				String providerInVelocityDB = "SELECT DISTINCT pt.Name AS CATEGORY, prov.ProvLastNM AS LAST_NAME, prov.ProvFirstNm AS FIRST_NAME, prov.ProvMidNm AS MID, dt.DegTypeDesc AS DEGREE\r\n"
						+ "                           , pract.FedrlTaxIDNum AS TAX_ID, prov.ProvNPIID AS NPI, CONCAT (l.LocAddrLine1Nm, l.LocAddrLine2Nm) AS ADDRESS, l.LocAddrCityNm AS CITY\r\n"
						+ "                           , l.StateNm AS ST, l.LocAddrZipCd AS ZIP, l.CntyNm AS CNTY, --ptn.ProvPhnNum AS PHONE, \r\n"
						+ "						   hs.MasterCd, SPC.SPEC AS PRIMARY_SPECIALTY, pg.ProgDesc AS Network,pg.ProgCd as Program,SR.Name\r\n"
						+ "FROM [Profisee].[data].[tProvider] prov\r\n"
						+ "INNER JOIN [Profisee].[SPF].[PracticeParticipant] pp\r\n"
						+ "       ON pp.ProvFK = prov.Code\r\n" + "\r\n"
						+ "INNER JOIN [Profisee].[data].[tPractice] pract\r\n"
						+ "       ON pract.Code = pp.PractcFK\r\n" + "\r\n"
						+ "INNER JOIN [profisee].[SPF].[PracticeParticipantProgramContract] pppc\r\n"
						+ "       ON pppc.PractcPrtcptFK = PP.PractcPrtcptPK\r\n" + "\r\n"
						+ "INNER JOIN ProviderMasterHub.HUB.vProgram pg\r\n" + "       ON pg.ProgPK = pppc.ProgFK\r\n"
						+ "\r\n" + "INNER JOIN [Profisee].[data].[tDG_ProviderType] pt\r\n"
						+ "       ON pt.Code = prov.ProvTypeFK\r\n" + "\r\n"
						+ "INNER JOIN ProviderMasterHub.HUB.vDegreeType dt\r\n"
						+ "       ON dt.DegTypePK = prov.DegTypeFK\r\n" + "\r\n"
						+ "INNER JOIN [Profisee].[SPF].[ProviderLocation] pl\r\n"
						+ "       ON pl.PractcPrtcptFK = PP.PractcPrtcptPK\r\n" + "\r\n"
						+ "INNER JOIN Profisee.data.tDG_LocationType lt\r\n" + "       ON lt.ID = pl.LocTypeFK\r\n"
						+ "\r\n" + "INNER JOIN [Profisee].[data].[tLocation] l\r\n" + "       ON l.Code = pl.LocFK\r\n"
						+ "\r\n" + "INNER JOIN [Profisee].[SPF].[PracticeParticipantSpecialty] ps\r\n"
						+ "      ON ps.PractcPrtcptFK = PP.PractcPrtcptPK\r\n" + "\r\n"
						+ "INNER JOIN [Profisee].[data].[vDG_HealthcareSpecialty] hs  \r\n"
						+ "       ON hs.Code = ps.HlthcrSpcltyFK\r\n" + "\r\n"
						+ " Inner Join [Profisee].[data].[tDG_TaxonomyCode] TX \r\n"
						+ " on TX.Code=hs.TaxonomyFK \r\n"
						+ "Inner Join [ProviderMasterStage].[PIMS].[SPEC] SPC \r\n"
						 + " on SPC.Taxonomy=TX.MasterCd  \r\n" 
						//+ "INNER JOIN [Profisee].[SPF].[ProviderTelephoneNumber] ptn\r\n"
						//+ "       ON ptn.PractcPrtcptFK = PP.PractcPrtcptPK --AND ptn.PhnNumTypeFK = pl.LocTypeFK\r\n"
						+ "\r\n" + "INNER JOIN [Profisee].[data].[tBenefitNetwork] bn\r\n"
						+ "       ON bn.Program = pg.ProgCd\r\n"
						+ "Inner join [Profisee].[data].[tDG_SourceSystem] SR\r\n" + "ON prov.SrcSysCd = SR.ID\r\n"
						+ "\r\n" + "where pg.ProgCd = '" + type + "' and SR.Name = 'PIMS' AND\r\n" + "pt.Name = '"
						+ temptestData.get("CATEGORY") + "' AND pract.FedrlTaxIDNum = '" + temptestData.get("TAX_ID")
						+ "'\r\n" + "AND prov.ProvNPIID = '" + temptestData.get("NPI") + "'\r\n"
						+ "AND CONCAT (l.LocAddrLine1Nm, l.LocAddrLine2Nm) = '" + temptestData.get("ADDRESS") + "'\r\n"
						+ "	\r\n" + "ORDER BY \r\n" + "       prov.ProvNPIID";

				logger.info(providerInVelocityDB);

				HashMap<String, String> veloProviderRow = db.getResultSetTwo(providerInVelocityDB);
				logger.info(veloProviderRow);

				if (veloProviderRow.isEmpty()) {
					test.log(Status.FATAL, "Row not found for Provider Validation Table " + temptestData);
					db.updateProvider(type + "_VAL_STATUS", "FATAL", Integer.parseInt(temptestData.get("ID")));
				} else {

					if (!temptestData.get("CATEGORY").equalsIgnoreCase(veloProviderRow.get("CATEGORY").trim())) {
						test.log(Status.FAIL, "Category mismatch - " + temptestData.get("CATEGORY").trim() + " -- "
								+ veloProviderRow.get("CATEGORY").trim());
						flag = false;
					}

					if (!temptestData.get("LAST_NAME").equalsIgnoreCase(veloProviderRow.get("LAST_NAME").trim())) {
						test.log(Status.FAIL, "LAST_NAME mismatch - " + temptestData.get("LAST_NAME").trim() + " -- "
								+ veloProviderRow.get("LAST_NAME").trim());
						flag = false;
					}

					if (!temptestData.get("FIRST_NAME").equalsIgnoreCase(veloProviderRow.get("FIRST_NAME").trim())) {
						test.log(Status.FAIL, "FIRST_NAME mismatch - " + temptestData.get("FIRST_NAME").trim() + " -- "
								+ veloProviderRow.get("FIRST_NAME").trim());
						flag = false;
					}

					if (!temptestData.get("MID").equalsIgnoreCase(veloProviderRow.get("MID").trim())) {
						test.log(Status.FAIL, "MID mismatch - " + temptestData.get("MID").trim() + " -- "
								+ veloProviderRow.get("MID").trim());
						flag = false;
					}

					if (!temptestData.get("ADDRESS").equalsIgnoreCase(veloProviderRow.get("ADDRESS").trim())) {
						test.log(Status.FAIL, "ADDRESS mismatch - " + temptestData.get("ADDRESS").trim() + " -- "
								+ veloProviderRow.get("ADDRESS").trim());
						flag = false;
					}

					if (!temptestData.get("CITY").equalsIgnoreCase(veloProviderRow.get("CITY").trim())) {
						test.log(Status.FAIL, "CITY mismatch - " + temptestData.get("CITY").trim() + " -- "
								+ veloProviderRow.get("CITY").trim());
						flag = false;
					}

					if (!temptestData.get("ST").equalsIgnoreCase(veloProviderRow.get("ST").trim())) {
						test.log(Status.FAIL, "ST mismatch - " + temptestData.get("ST").trim() + " -- "
								+ veloProviderRow.get("ST").trim());
						flag = false;
					}

					if (!temptestData.get("ZIP").equalsIgnoreCase(veloProviderRow.get("ZIP").trim())) {
						test.log(Status.FAIL, "ZIP mismatch - " + temptestData.get("ZIP").trim() + " -- "
								+ veloProviderRow.get("ZIP").trim());
						flag = false;
					}

					if (!temptestData.get("PRIMARY_SPECIALTY")
							.equalsIgnoreCase(veloProviderRow.get("PRIMARY_SPECIALTY").trim())) {
						test.log(Status.FAIL,
								"PRIMARY_SPECIALTY mismatch - " + temptestData.get("PRIMARY_SPECIALTY").trim() + " -- "
										+ veloProviderRow.get("PRIMARY_SPECIALTY").trim());
						flag = false;
					}

					if (flag == true) {
						db.updateProvider(type + "_VAL_STATUS", "PASSED", Integer.parseInt(temptestData.get("ID")));
						test.log(Status.PASS, "Record Matched -- " + veloProviderRow);
					} else {
						db.updateProvider(type + "_VAL_STATUS", "FAILED", Integer.parseInt(temptestData.get("ID")));
						test.log(Status.FAIL, "Record NOT Matched -- " + veloProviderRow + " ---- " + temptestData);
					}
				}

				flushTest();
			}
		} catch (Exception e) {
			flag = false;
			e.printStackTrace();
			try {
				reportFail("Exception when attempting to validate " + type);
			} catch (IOException e1) {
				e1.printStackTrace();
			}
		}
		return flag;
	}
}
