package x12.claims;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import pageclasses.BasePage;
import pageclasses.CommonMethods;
import utils.Const;
import utils.Dbconn;
import x12.FileDataStructure;
//import x12.Loop;
import x12.Loop_ISA;
import x12.enums.ClaimType;

public class ClaimFileCreation extends BasePage {

	private Dbconn dbaccess;
	private Loop_ISA isa_loop;
	private Map<String, String> staticData;
	private FileDataStructure dataHolder;
	private ClaimType claimType;

	public ClaimFileCreation(ClaimType type) {
		this.dbaccess = new Dbconn();
		this.claimType = type;
		this.staticData = getStaticData();
		this.dataHolder = new FileDataStructure();
		this.dataHolder.addTableData(staticData);
		this.isa_loop = new Loop_ISA(staticData);
	}

//	private Loop readXML(String filename) {
//		 File file = new File(filename);
//		 DocumentBuilderFactory dbf = DocumentBuilderFactory.newInstance();
//		 try {
//			DocumentBuilder db = dbf.newDocumentBuilder();
//			Document document = db.parse(file);
//			document.getDocumentElement().normalize();
//			
//			NodeList nList = document.getElementsByTagName("Loop");
//				Node node = nList.item(0);
//				Loop loop = printLoop(node);
//				return loop;
//		} catch (ParserConfigurationException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (SAXException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		} catch (IOException e) {
//			// TODO Auto-generated catch block
//			e.printStackTrace();
//		}
//		 return null;
//	}
	
//	private Loop printLoop(Node node) {
//		if(node == null)
//			return null;
//		if(node.getNodeName().equals("Loop")){
//			
//			Element elem = (Element) node;
//			Loop loop = new Loop();
//			NodeList nl = elem.getChildNodes();
//			for(int i=0; i<nl.getLength(); i++) {
//				String nodeName = nl.item(i).getNodeName();
//				if(nodeName.equals("ID")) {
//					loop.setLoopID(nl.item(i).getTextContent());
//				} else if(nodeName.equals("Segment")) {
////					System.out.println("Segment here is "+ nl.item(i).getTextContent());
//					loop.addSegment(nl.item(i).getTextContent());
//				} else if(nodeName.equals("Loop")) {
//					Loop child = printLoop(nl.item(i));
//					loop.addLoop(child);
//				}
//			}
//			return loop;
//		}
//		return null;
//	}
	
	public void create837ClaimsFile() {
		try {

			BufferedWriter writer = new BufferedWriter(new FileWriter(new File(
					Const.ClaimFilePath + this.claimType.filename + CommonMethods.getCurrentDateTime() + ".txt")));
			BufferedWriter errors = new BufferedWriter(new FileWriter(new File("prof_errors.txt")));
			writer.append(isa_loop.getControlHeaders());
			String interchangeControlNumber = isa_loop.getInterchangeControlNumber();

			// number of claims
			String noOfClaimsQuery = "select Distinct top 1 TestCaseID from [dbo].[test_data_readytoclaims]  where RunMode = 'Y' and Status is NULL and ClaimType = '"
					+ this.claimType.codeString + "' order by TestCaseID";

			List<HashMap<String, String>> listOfClaims = dbaccess.getListOfHashMapsFromResultSet(noOfClaimsQuery);
			dataHolder.setTransactionRecords(listOfClaims);
			isa_loop.setTransactionCount(listOfClaims.size());

			for (HashMap<String, String> claim : listOfClaims) {

				String testID = claim.get("TestCaseID");
//				System.out.println("working on count \t" + count + " testID \t" + testID);
				Map<String, String> memberData = getMemberData(testID);
				if(memberData.containsKey("ERROR")) {
					System.out.println("FAIL");
					errors.append(testID + "\n");
					errors.append(memberData.get("ERROR"));
					errors.append("\n\n============================\n\n");
					continue;
				}
				this.dataHolder.addTableData(memberData);

				Map<String, String> provider = getBillingProviderData(testID, memberData.get("MedicareID"));
				this.dataHolder.addTableData(provider);

				Map<String, String> claimData = getClaimData(testID);
				dataHolder.addTableData(claimData);
				
				List<HashMap<String, String>> claimLineData = getClaimLineData(testID, claimData.get("2400_SV100"));
				dataHolder.add2400Loops(claimLineData);
				getClaimsDTPSegments(claimData, claimLineData);
				getClaimsHISegments(claimData);
				if (this.claimType == ClaimType.Institutional) {
					HashMap<String, String> attendingProvider = getAttendingProvider(testID);
					dataHolder.addTableData(attendingProvider);
				}

				if (!claimData.get("2310A_NM109").isEmpty()) {
					Map<String, String> refProviderData = getProviderData("2310A", claimData.get("2310A_NM109"));
					dataHolder.addTableData(refProviderData);

				}
				if (!claimData.get("2310B_NM109").isEmpty()) {
					Map<String, String> renProviderData = getProviderData("2310B", claimData.get("2310B_NM109"));
					dataHolder.addTableData(renProviderData);
				}

				Loop_ST st_loop = new Loop_ST(this.claimType, testID, interchangeControlNumber, this.staticData,
						claimLineData, dataHolder);
				writer.append( st_loop.toString());
				
				st_loop.updateTable();
				writer.append("\n");
			}
			writer.append(isa_loop.getControlTrailers());
			writer.close();
			errors.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	// Get static Data for the file
	private HashMap<String, String> getStaticData() {
		String type = this.claimType.codeInt;
		String query = "SELECT [ClaimType]\r\n" + "      ,[ISAAuthorInfoQualifier] 			  		AS ISA_ISA01\r\n"
				+ "      ,[ISAAuthorInfo] 					  		AS ISA_ISA02\r\n"
				+ "      ,[ISASecurityInfoQualifier] 		  		AS ISA_ISA03\r\n"
				+ "      ,[ISASecurityInfo] 				  		AS ISA_ISA04\r\n"
				+ "      ,[ISAInterchangeSenderIDQualifier]   		AS ISA_ISA05\r\n"
				+ "      ,[ISAInterChangeSenderID] 			  		AS ISA_ISA06\r\n"
				+ "      ,[ISAInterchangeReceiverIDQualifier] 		AS ISA_ISA07\r\n"
				+ "      ,[ISAInterChangeReceiverID] 		  		AS ISA_ISA08\r\n"
				+ "      ,[ISARepetitionSeparator] 			  		AS ISA_ISA11\r\n"
				+ "      ,[ISAInterchangeControlVersionNum]   		AS ISA_ISA12\r\n"
				+ "      ,[ISAAckRequested] 				  		AS ISA_ISA14\r\n"
				+ "      ,[ISAInterchangeUsasgeIndicator] 	  		AS ISA_ISA15\r\n"
				+ "      ,[ISAComponentElementSeparator] 	  		AS ISA_ISA16\r\n"
				+ "      ,[GSFunctionalIdentifierCode] 		  		AS GS_GS01\r\n"
				+ "      ,[GSApplicationSenderCode] 		  		AS GS_GS02\r\n"
				+ "      ,[GSApplicationReceiverCode] 		  		AS GS_GS03\r\n"
				+ "      ,[GSResponsibleAgencyCode] 		  		AS GS_GS07\r\n"
				+ "      ,[GSVersionReleaseIndustryIdentifierCode] 	AS GS_GS08\r\n"
				+ "      ,[STTransactionSetIdentifierCode] 			AS ST01\r\n"
				+ "      ,[STImplementationConventionReference] 	AS ST03\r\n"
				+ "      ,[BHTHierarchicalStructureCode] 			AS BHT01\r\n"
				+ "      ,[BHTTransactionTypeCode] 					AS BHT06\r\n"
				+ "      ,[SubmitterNM1EntityIdentifierCode] 		AS [1000A_NM101]\r\n"
				+ "      ,[SubmitterNM1EntityTypeQualifier] 		AS [1000A_NM102]\r\n"
				+ "      ,[SubmitterNM1NameLastOrganizationName] 	AS [1000A_NM103]\r\n"
				+ "      ,[SubmitterNM1NameFirst] 					AS [1000A_NM104]\r\n"
				+ "      ,[SubmitterNM1NameMiddle] 					AS [1000A_NM105]\r\n"
				+ "      ,[SubmitterNM1NamePrefix] 					AS [1000A_NM106]\r\n"
				+ "      ,[SubmitterNM1NameSuffix] 					AS [1000A_NM107]\r\n"
				+ "      ,[SubmitterNM1IdentificationCodeQualifier] AS [1000A_NM108]\r\n"
				+ "      ,[SubmitterNM1IdentificationCode] 			AS [1000A_NM109]\r\n"
				+ "      ,[SubmitterPERContactFunctionCode]  		AS [1000A_PER01]\r\n"
				+ "      ,[SubmitterPERName] 						AS [1000A_PER02]\r\n"
				+ "      ,[SubmitterPERCommunicationNumQualifier1] 	AS [1000A_PER03]\r\n"
				+ "      ,[SubmitterPERCommunicationNumber1] 		AS [1000A_PER04]\r\n"
				+ "      ,[SubmitterPERCommunicationNumQualifier2] 	AS [1000A_PER05]\r\n"
				+ "      ,[SubmitterPERCommunicationNumber2] 		AS [1000A_PER06]\r\n"
				+ "      ,[SubmitterPERCommunicationNumQualifier3] 	AS [1000A_PER07]\r\n"
				+ "      ,[SubmitterPERCommunicationNumber3] 		AS [1000A_PER08]\r\n"
				+ "      ,[ReceiverNM1EntityIdentifierCode] 		AS [1000B_NM101]\r\n"
				+ "      ,[ReceiverNM1EntityTypeQualifier] 			AS [1000B_NM102]\r\n"
				+ "      ,[ReceiverNM1NameLastOrganizationName] 	AS [1000B_NM103]\r\n"
				+ "      ,[ReceiverNameFirst]  						AS [1000B_NM104]\r\n"
				+ "      ,[ReceiverNameMiddle] 						AS [1000B_NM105]\r\n"
				+ "      ,[ReceiverNamePrefix] 						AS [1000B_NM106]\r\n"
				+ "      ,[ReceiverNameSuffix]			 			AS [1000B_NM107]\r\n"
				+ "      ,[ReceiverNM1IdentificationCodeQualifier] 	AS [1000B_NM108]\r\n"
				+ "      ,[ReceiverNM1IdentificationCode] 			AS [1000B_NM109]\r\n"
				+ "      ,[HLProviderHierarchicalIDNumber] 			AS [2000A_HL01]\r\n"
				+ "      ,[HLProviderHierarchicalParentIDNumber] 	AS [2000A_HL02]\r\n"
				+ "      ,[HLProviderHierarchicalLevelCode] 		AS [2000A_HL03]\r\n"
				+ "      ,[HLProviderHierarchicalChildCode] 		AS [2000A_HL04]\r\n"
				+ "      ,[BillingProviderNM1EntityIdentifierCode] 	AS [2010AA_NM101]\r\n"
				+ "      ,[BillingProviderNM1IdentificationCodeQualifier] 		AS [2010AA_NM108]\r\n"
				+ "      ,[BillingProviderNM1IdentificationCodeQualifier] 		AS [2300_NM108]\r\n"
				+ "      ,[BillingProviderREFReferenceIdentificationQualifier] 	AS [2010AA_REF01]\r\n"
				+ "      ,[HLSubscriberHierarchicalParentIDNumber] 	AS [2000B_HL02]\r\n"
				+ "      ,[HLSubscriberHierarchicalLevelCode] 		AS [2000B_HL03]\r\n"
				+ "      ,[HLSubscriberHierarchicalChildCode] 		AS [2000B_HL04]\r\n"
				+ "      ,[SBRIndividualRelationshipCode] 			AS [2000B_SBR02]\r\n"
				+ "      ,[SBRName] 								AS [2000B_SBR04]\r\n"
				+ "      ,[SubscriberNM1EntityIdentifierCode] 		AS [2010BA_NM101]\r\n"
				+ "      ,[SubscriberNM1IdentificationCodeQualifier] 			AS [2010BA_NM108]\r\n"
				+ "      ,[SubscriberDMGDateTimePeriodFormatQualifier] 			AS [2010BA_DMG01]\r\n"
				+ "      ,[PayerNM1EntityIdentifierCode] 			AS [2010BB_NM101]\r\n"
				+ "      ,[PayerNM1EntityTypeQualifier] 			AS [2010BB_NM102]\r\n"
				+ "      ,[PayerNM1NameLastOrganizationName]	 	AS [2010BB_NM103]\r\n"
				+ "      ,[PayerNM1NameFirst] 						AS [2010BB_NM104]\r\n"
				+ "      ,[PayerNM1NameMiddle] 						AS [2010BB_NM105]\r\n"
				+ "      ,[PayerNM1NamePrefix] 						AS [2010BB_NM106]\r\n"
				+ "      ,[PayerNM1NameSuffix] 						AS [2010BB_NM107]\r\n"
				+ "      ,[PayerNM1IdentificationCodeQualifier] 	AS [2010BB_NM108]\r\n"
				+ "      ,[PayerNM1IdentificationCode] 				AS [2010BB_NM109]\r\n"
				+ "      ,[ATTProviderNM1EntityTypeQualifier] 		AS [2300_NM101] \r\n"
				+ "      ,[CLMClaimFilingIndicatorCode] 			AS [2300_CLM03]\r\n"
				+ "      ,[CLMNonInstitutionalClaimTypeCode] 		AS [2300_CLM04]\r\n"
				+ "      ,[CLMYesNoConditionResponseCode1] 			AS [2300_CLM06]\r\n"
				+ "      ,[CLMProviderAcceptAssignmentCode] 		AS [2300_CLM07]\r\n"
				+ "      ,[CLMYesNoConditionResponseCode2] 			AS [2300_CLM08]\r\n"
				+ "      ,[CLMReleaseInformationCode] 				AS [2300_CLM09]\r\n"
				+ "      ,[CLMREFReferenceIdentificationQualifier] 	AS [2300_REF01]\r\n"
				+ "      ,[CLMNTENoteReferenceCode] 				AS [2300_NTE01]\r\n"
				+ "      ,[REFProviderNM1EntityIdentifierCode] 				AS [2310A_NM109]\r\n"
				+ "      ,[REFProviderNM1EntityIdentifierCode] 				AS [2310B_NM109]\r\n"
				+ "      ,[REFProviderNM1IdentificationCodeQualifier]		AS [2420F_NM108]\r\n"
				+ "      ,[RENProviderNM1EntityTypeQualifier]  				AS [2420A_NM101]\r\n"
				+ "      ,[RENProviderNM1IdentificationCodeQualifier] 		AS [2420A_NM108]\r\n"
				+ "      ,[ClaimLineSV1FacilityCodeValue] 					AS [2400_SV" + claimType.codeInt + "05]\r\n"
				+ "      ,[ClaimLineSV1ServiceTypeCode] 					AS [2400_SV" + claimType.codeInt + "06]\r\n"
				+ "      ,[ClaimLineSV1CompositeDiagnosisCodePointer] 		AS [2400_SV" + claimType.codeInt + "07]\r\n"
				+ "      ,[ClaimLineDTPDateTimeQualifier] 					AS [2400_DTP01]\r\n"
				+ "      ,[ClaimLineDTPDateTimePeriodFormatQualifier] 		AS [2400_DTP02]\r\n"
				+ "      ,[ClaimLineREFReferenceIdentificationQualifier] 	AS [2400_REF01]\r\n"
				+ "      ,[CLM_DTP_Institutional_Qualifier1] AS [2300_DTP101]\r\n"
				+ "      ,[CLM_DTP_Institutional_Qualifier2] AS [2300_DTP201]\r\n"
				+ "      ,[CLM_DTP_Institutional_Qualifier3] AS [2300_DTP301]\r\n"
				+ "      ,[CLM_DTP_Institutional_FormatQualifier1] AS [2300_DTP102]\r\n"
				+ "      ,[CLM_DTP_Institutional_FormatQualifier2] AS [2300_DTP202]\r\n"
				+ "      ,[CLM_DTP_Institutional_FormatQualifier3] AS [2300_DTP302] \r\n"
				+ " 	 ,[CL101] AS [2300_CL101]\r\n" + "		 ,[CL102] AS [2300_CL102]\r\n"
				+ "		 ,[CL103] AS [2300_CL103]\r\n"
				+ "		 ,[PreAuthREFIdentificationQual] 	AS [2300_REF201]\r\n"
				+ "      ,[PayerRespSeqNoCode] 				AS [2000B_SBR01]\r\n"
				+ "      , [SBRClaimFilingIndicatorCode] 	AS [2000B_SBR09]\r\n"
				+ "      , [SBRInsuranceTypeCode] 			AS [2000B_SBR05] \r\n"
				+ "  FROM [VelocityTestAutomation].[dbo].[static_claim_data] WHERE ID = " + type + "\r\n";
		List<HashMap<String, String>> list = dbaccess.getListOfHashMapsFromResultSet(query);
		if (list.isEmpty())
			return null;
		Map<String, String> data = list.get(0);
		data.put("ISA_ISA02", "          ");
		data.put("ISA_ISA04", "          ");
		data.put("ISA_ISA06", fixSpaces(data.get("ISA_ISA06"), 15));
		data.put("ISA_ISA08", fixSpaces(data.get("ISA_ISA08"), 15));
		data.put("ISA_ISA09", getCurrentDateYYMMDD());
		data.put("ISA_ISA10", getCurrentTimeStampHHMM());

		data.put("GS_GS04", getCurrentDateCCYYMMDD());
		data.put("GS_GS05", getCurrentTimeStampHHMM());
		return list.get(0);
	}

	// in ready to claims table create three columns - contracted, taxonomyCd,
	// referringProviderIndicator
	private Map<String, String> getBillingProviderData(String testID, String memberDataMedicareID) {
		String contracted, referringProviderIndicator, taxonomyCd;

		String query = "SELECT [PlanID], [PBPID], [MedicareID], Contracted, ReferringProviderIndicator, TaxonomyCd FROM test_data_readytoclaims WHERE TestCaseID = '"
				+ testID + "'";
		HashMap<String, String> data = dbaccess.getListOfHashMapsFromResultSet(query).get(0);
		contracted = data.get("Contracted");
		referringProviderIndicator = data.get("ReferringProviderIndicator");
		taxonomyCd = data.get("TaxonomyCd");

		String medicareID = data.get("MedicareID").isEmpty() ? memberDataMedicareID : data.get("MedicareID");
		String planID = data.get("PlanID");
		String pbpID = data.get("PBPID");

		Map<String, String> providerData = null;

		if (this.claimType == ClaimType.Institutional) {
			providerData = (contracted != null && contracted.equals("Y")) ? getInNetworkHospital(planID, pbpID)
					: getOutOfNetworkHospital(planID, pbpID);
			query = "UPDATE test_data_readytoclaims\r\n" + "SET MedicareID = '" + medicareID + "',\r\n"
					+ "	BillingProviderNPI = '" + providerData.get("2010AA_NM109") + "'";

			if (referringProviderIndicator != null && referringProviderIndicator.equals("Y"))
				query += ", ReferringProviderNPI = '" + getMemberPCP(medicareID, planID, pbpID).get("PCPID") + "' ";
			query += "WHERE TestCaseID = '" + testID + "'";
			dbaccess.executeQuery(query);
//			return providerData;
		} else {
			if (!referringProviderIndicator.equals("Y")) {

				if (contracted != null && contracted.equals("Y") && taxonomyCd.isEmpty()) {
					providerData = getMemberPCP(medicareID, planID, pbpID); // get this from getMemberData
				} else if (contracted != null && contracted.equals("Y") && !taxonomyCd.isEmpty()) {
					providerData = getSpecialistInNetwork("2010AA", planID, pbpID, taxonomyCd);
				} else {
					providerData = getPcpOutOfNetwork(planID, pbpID); // planID and PBPID
				}
				
				HashMap<String, String> supplierData = getSupplierData("2010AA", providerData.get("2010AA_NM109"));
				

				query = "UPDATE test_data_readytoclaims\r\n" + "SET MedicareID = " + formatArgument(medicareID) + ",\r\n"
						+ "	BillingProviderNPI = " + formatArgument(supplierData.get("2010AA_NM109")) + " ,\r\n"
						+ " RenderingProviderNPI = " + formatArgument(providerData.get("2010AA_NM109")) + " \r\n"
						+ "WHERE TestCaseID = " + formatArgument(testID);
				dbaccess.executeQuery(query);
				return supplierData;
			} else if (referringProviderIndicator.equals("Y")) {
				if (contracted.equals("Y")) {
					providerData = getSpecialistInNetwork("2010AA", planID, pbpID, taxonomyCd);
				} else {
					providerData = getSpecialistoutOfNetwork("2010AA", planID, pbpID, taxonomyCd);
				}
				
				HashMap<String, String> supplierData = getSupplierData("2010AA", providerData.get("2010AA_NM109"));
				
				query = "UPDATE test_data_readytoclaims\r\n" + "SET MedicareID = " + formatArgument(medicareID)
						+ ",\r\n" + "	BillingProviderNPI = " + formatArgument(supplierData.get("2010AA_NM109"))
						+ ",\r\n" + "	ReferringProviderNPI = "
						+ formatArgument(getMemberPCP(medicareID, planID, pbpID).get("PCPID")) + " \r\n"
						+ " RenderingProviderNPI = " + formatArgument(providerData.get("2010AA_NM109")) + " \r\n"
						+ "WHERE TestCaseID = " + formatArgument(testID);
				dbaccess.executeQuery(query);
				return supplierData;
			}
		}
		if (providerData.get("2010AA_NM104").isEmpty())
			providerData.put("2010AA_NM102", "2");
		else
			providerData.put("2010AA_NM102", "1");

		providerData.put("2010AA_NM106", "");
		return providerData;
	}

	private String formatArgument(String arg) {
		return arg == null ? null : "'" + arg + "'";
	}

	private Map<String, String> getMemberData(String tcid) {
		String testcaseQuery = "Select [PlanID], [PBPID], [Gender], [MedicareID] FROM\r\n"
				+ "[dbo].[test_data_readytoclaims]\r\n" + "WHERE \r\n" + "TestCaseID = '" + tcid + "' AND\r\n"
				+ "HealthcareCodeInformationPrimary is not NULL";

		HashMap<String, String> member = dbaccess.getResultSet(testcaseQuery);

		String query = null;
		if (!member.get("MedicareID").isEmpty()) {
			query = "SELECT   member.[SupplementalID]    AS [2010BA_NM109]\r\n"
					+ "      ,member.[LastName]      AS [2010BA_NM103]\r\n"
					+ "      ,member.[FirstName]     AS [2010BA_NM104]\r\n"
					+ "      ,member.[MiddleInitial] AS [2010BA_NM105]\r\n"
					+ "      ,member.[Suffix]        AS [2010BA_NM107]\r\n"
					+ "	     ,address.[Address1]	 AS [2010BA_N301]\r\n"
					+ "	     ,address.[City] 		 AS [2010BA_N401]\r\n"
					+ "	     ,address.[StateCode] 	 AS [2010BA_N402]\r\n"
					+ "	     ,address.[ZipCode]		 AS [2010BA_N403]\r\n"
					+ "	     ,member.[Gender] 		 AS [2010BA_DMG03]\r\n"
					+ "	     ,member.[BirthDate] 	 AS [2010BA_DMG02]\r\n"
					+ "	     ,enr.GroupID 			 AS [2000B_SBR03]\r\n" 
					+ "      ,member.[MedicareID] \r\n"
					+ "  FROM \r\n"
					+ "  [VelocityTestAutomation].[dbo].[member_demographic] member, \r\n"
					+ "  [dbo].[address_details] address, [dbo].[test_data_readytoenroll] rte,\r\n"
					+ "  [dbo].[member_enrollment] enr\r\n" + "  WHERE rte.[PCPNPI] is Not NULL and \r\n"
					+ "	member.[MedicareID] = rte.[MedCareID] and\r\n"
					+ "	member.[MedicareID] = enr.[MedicareID] and\r\n" + "	rte.[AddressID] = address.[ID] and\r\n"
					+ "	member.[MedicareID] ='" + member.get("MedicareID") + "'";
		} else {
			String countofRecordsQuery = null;
			HashMap<String, String> noOfRecords = new HashMap<String, String>();
			int randomNum = 0;
			if (member.get("Gender").isEmpty()) {

				countofRecordsQuery = "SELECT count(*) AS COUNT \r\n" + "  FROM \r\n"
						+ "  [VelocityTestAutomation].[dbo].[member_demographic] member, \r\n"
						+ "  [dbo].[address_details] address, [dbo].[test_data_readytoenroll] rte,\r\n"
						+ "  [dbo].[member_enrollment] enr\r\n" + "  WHERE rte.[PCPNPI] is Not NULL and \r\n"
						+ "	member.[MedicareID] = rte.[MedCareID] and\r\n"
						+ "	member.[MedicareID] = enr.[MedicareID] and\r\n" + "	rte.[AddressID] = address.[ID] and\r\n"
						+ "	enr.PlanID = '" + member.get("PlanID") + "' and\r\n" + "	enr.PBPID = '"
						+ member.get("PBPID") + "' and member.WorkStream = 'ClaimsAutomation'";
				noOfRecords = dbaccess.getResultSet(countofRecordsQuery);

				if(noOfRecords.get("COUNT").toString().equals("0")) {
					noOfRecords = new HashMap<String, String>();
					noOfRecords.put("ERROR", countofRecordsQuery);
					return noOfRecords;
				}

				randomNum = getRandomNumber(1, Integer.parseInt(noOfRecords.get("COUNT")));
				query = "WITH MyTable AS\r\n" + "(\r\n" + "SELECT "

						+ "       member.[SupplementalID]    AS [2010BA_NM109]\r\n"
						+ "      ,member.[LastName]      AS [2010BA_NM103]\r\n"
						+ "      ,member.[FirstName]     AS [2010BA_NM104]\r\n"
						+ "      ,member.[MiddleInitial] AS [2010BA_NM105]\r\n"
						+ "      ,member.[Suffix]        AS [2010BA_NM107]\r\n"
						+ "	     ,address.[Address1]	 AS [2010BA_N301]\r\n"
						+ "	     ,address.[City] 		 AS [2010BA_N401]\r\n"
						+ "	     ,address.[StateCode] 	 AS [2010BA_N402]\r\n"
						+ "	     ,address.[ZipCode]		 AS [2010BA_N403]\r\n"
						+ "	     ,member.[Gender] 		 AS [2010BA_DMG03]\r\n"
						+ "	     ,member.[BirthDate] 	 AS [2010BA_DMG02]\r\n"
						+ "	     ,enr.GroupID 			 AS [2000B_SBR03]\r\n"
						+ "      ,member.[MedicareID] \r\n"
						+ "	  , ROW_NUMBER() OVER( ORDER BY rte.[MedcareID]) AS RowNumber\r\n" + "  FROM \r\n"
						+ "  [VelocityTestAutomation].[dbo].[member_demographic] member, \r\n"
						+ "  [dbo].[address_details] address, [dbo].[test_data_readytoenroll] rte,\r\n"
						+ "  [dbo].[member_enrollment] enr\r\n" + "  WHERE rte.[PCPNPI] is Not NULL and \r\n"
						+ "	member.[MedicareID] = rte.[MedCareID] and\r\n"
						+ "	member.[MedicareID] = enr.[MedicareID] and\r\n" + "	rte.[AddressID] = address.[ID] and\r\n"
						+ "	enr.PlanID = '" + member.get("PlanID") + "' and\r\n" + "	enr.PBPID = '"
						+ member.get("PBPID") + "' and member.WorkStream = 'ClaimsAutomation') \r\n" + " Select \r\n"
						+ "  [2010BA_NM109], [2010BA_NM103], [2010BA_NM104], [2010BA_NM105], [2010BA_NM107], [2010BA_N301], [2010BA_N401], [2010BA_N402], [2010BA_N403], [2010BA_DMG02], [2010BA_DMG03], [2000B_SBR03], [MedicareID]\r\n"
						+ "  FROM \r\n" + "  MyTable WHERE \r\n" + "	RowNumber = " + randomNum + ";";
			} else {
				countofRecordsQuery = "SELECT count(*) AS COUNT \r\n" + "  FROM \r\n"
						+ "  [VelocityTestAutomation].[dbo].[member_demographic] member, \r\n"
						+ "  [dbo].[address_details] address, [dbo].[test_data_readytoenroll] rte,\r\n"
						+ "  [dbo].[member_enrollment] enr\r\n" + "  WHERE rte.[PCPNPI] is Not NULL and \r\n"
						+ "	member.[MedicareID] = rte.[MedCareID] and\r\n"
						+ "	member.[MedicareID] = enr.[MedicareID] and\r\n" + "	rte.[AddressID] = address.[ID] and\r\n"
						+ "	enr.PlanID = '" + member.get("PlanID") + "' and\r\n" + "	enr.PBPID = '"
						+ member.get("PBPID") + "' and member.WorkStream = 'ClaimsAutomation' and \r\n" + "   member.[Gender] = '" + member.get("Gender") + "';";
				noOfRecords = dbaccess.getResultSet(countofRecordsQuery);
				if(noOfRecords.get("COUNT").toString().equals("0")) {
					noOfRecords = new HashMap<String, String>();
					noOfRecords.put("ERROR", countofRecordsQuery);
					return noOfRecords;
				}

				randomNum = getRandomNumber(1, Integer.parseInt(noOfRecords.get("COUNT")));

				query = "WITH MyTable AS\r\n" + "(\r\n" + "SELECT "
						+ "       member.[SupplementalID]    AS [2010BA_NM109]\r\n"
						+ "      ,member.[LastName]      AS [2010BA_NM103]\r\n"
						+ "      ,member.[FirstName]     AS [2010BA_NM104]\r\n"
						+ "      ,member.[MiddleInitial] AS [2010BA_NM105]\r\n"
						+ "      ,member.[Suffix]        AS [2010BA_NM107]\r\n"
						+ "	     ,address.[Address1]	 AS [2010BA_N301]\r\n"
						+ "	     ,address.[City] 		 AS [2010BA_N401]\r\n"
						+ "	     ,address.[StateCode] 	 AS [2010BA_N402]\r\n"
						+ "	     ,address.[ZipCode]		 AS [2010BA_N403]\r\n"
						+ "	     ,member.[Gender] 		 AS [2010BA_DMG03]\r\n"
						+ "	     ,member.[BirthDate] 	 AS [2010BA_DMG02]\r\n"
						+ "	     ,enr.GroupID 			 AS [2000B_SBR03]\r\n"
						+ "      ,member.[MedicareID] \r\n"
						+ "	  , ROW_NUMBER() OVER( ORDER BY rte.[MedcareID]) AS RowNumber\r\n" + "  FROM \r\n"
						+ "  [VelocityTestAutomation].[dbo].[member_demographic] member, \r\n"
						+ "  [dbo].[address_details] address, [dbo].[test_data_readytoenroll] rte,\r\n"
						+ "  [dbo].[member_enrollment] enr\r\n" + "  WHERE rte.[PCPNPI] is Not NULL and \r\n"
						+ "	member.[MedicareID] = rte.[MedCareID] and\r\n"
						+ "	member.[MedicareID] = enr.[MedicareID] and\r\n" + "	rte.[AddressID] = address.[ID] and\r\n"
						+ "	enr.PlanID = '" + member.get("PlanID") + "' and\r\n" + "	enr.PBPID = '"
						+ member.get("PBPID") + "' and \r\n" + "   member.[Gender] = '" + member.get("Gender")
						+ "' and member.WorkStream = 'ClaimsAutomation') \r\n" + " Select \r\n"
						+ "  [2010BA_NM109], [2010BA_NM103], [2010BA_NM104], [2010BA_NM105], [2010BA_NM107], [2010BA_N301], [2010BA_N401], [2010BA_N402], [2010BA_N403], [2010BA_DMG02], [2010BA_DMG03], [2000B_SBR03],[MedicareID]\r\n"
						+ "  FROM \r\n" + "  MyTable WHERE \r\n" + "	RowNumber = " + randomNum + ";";
			}
		}
		Map<String, String> memberData = dbaccess.getResultSet(query);
		memberData.put("2000B_HL01", "1");
		memberData.put("2000B_SBR06", "");
		memberData.put("2000B_SBR07", "");
		memberData.put("2000B_SBR08", "");

		if (memberData.get("2010BA_NM104") == null)
			memberData.put("2010BA_NM102", "2");
		else
			memberData.put("2010BA_NM102", "1");
		memberData.put("2010BA_NM106", "");
		if (memberData.get("2010BA_DMG02") != null) {
			memberData.put("2010BA_DMG02", getDateInYYYYMMDD(memberData.get("2010BA_DMG02")));
		}
		return memberData;
	}

	private HashMap<String, String> getProviderData(String loopCode, String NPI) {

		String query = "SELECT TOP 1 \r\n" +

				"pt.Name AS CATEGORY, \r\n" 
				+ "prov.ProvLastNM 				AS [" + loopCode + "_NM103], \r\n"
				+ "prov.ProvFirstNm 			AS [" + loopCode + "_NM104], \r\n"
				+ "prov.ProvMidNm 				AS [" + loopCode + "_NM105], \r\n"
				+ "[ProvSuffixNm] 				AS [" + loopCode + "_NM107], \r\n"
				+ "pract.FedrlTaxIDNum 			AS [2010AA_REF02], \r\n" 
				+ "prov.ProvNPIID 				AS [" + loopCode + "_NM109], \r\n" + "l.[Std Mail Address Line 1] 	AS [2010AA_N301], \r\n"
				+ "l.[Std Mail City] 			AS [2010AA_N401], \r\n"
				+ "l.[Std Mail State] 			AS [2010AA_N402], \r\n"
				+ "l.[Std Mail ZIP]				AS [2010AA_N403], \r\n" + "SPC.SPEC as [SPECIALTY CODE] \r\n"

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
		if(loopCode.startsWith("2310")) {
			if (data.get(loopCode+"_NM104").isEmpty())
				data.put(loopCode+"_NM102", "2");
			else
				data.put(loopCode+"_NM102", "1");
			data.put(loopCode+"_NM106", "");
			data.put(loopCode+"_NM108", "XX");
		}
		return data;
	}

	public HashMap<String, String> getClaimData(String tcid) {
		String claimQuery = "Select " + "[HealthCareServiceLocationInformation]	AS [2300_CLM05],"
				+ "[HealthCareCodeInformationPrimary] 		AS [2300_HI101],"
				+ "[HealthCareCodeInformationSecondary] 	AS [2300_HI201],"
				+ "[HealthCareCodeInformationAdmission] 	AS [2300_HI301],"
				+ "[PreAuthIndicator]						AS [2300_REF200],"
				+ "[PreAuthNumber]							AS [2300_REF202],"
				+ "[ReferringProviderNPI] 					AS [2310A_NM109],"
				+ "[RenderingProviderNPI] 					AS [2310B_NM109],"
				+ "[SV201ProductServiceID] 					AS [2400_SV200] \r\n" + "FROM\r\n"
				+ "[dbo].[test_data_readytoclaims]\r\n" + "WHERE \r\n" + "TestCaseID = '" + tcid + "' AND\r\n"
				+ "BillingProviderNPI is not NULL";

		return dbaccess.getResultSet(claimQuery);
	}

	private String fixDecimals(String raw) {
		if (!raw.contains("."))
			return raw;

		int length = raw.length();
		for (int i = raw.length() - 1; i >= 0; i--) {
			if (raw.charAt(i) == '.')
				return raw.substring(0, i);
			else if (raw.charAt(i) == '0')
				length--;
			else
				return raw.substring(0, length);
		}

		return raw;
	}

	private String fixDate(String date) {
		if (date.length() != 8)
			return date;
		return date.substring(4, 8) + date.substring(0, 4);
	}

	private boolean isInPatient(String val) {
		return val.split(":")[0].equals("21") || val.split(":")[0].equals("31") || val.split(":")[0].equals("51")
				|| val.split(":")[0].equals("61");
	}

	private void getClaimsDTPSegments(Map<String, String> claimData, List<HashMap<String, String>> claimLineData) {
		Map<String, String> data = new HashMap<String, String>();
		List<String> to = new ArrayList<String>();
		List<String> from = new ArrayList<String>();
		for (HashMap<String, String> map : claimLineData) {
			if (map.get("DateTimeToPeriod") != null && map.get("DateTimeToPeriod").length() == 8)
				to.add(map.get("DateTimeToPeriod"));
			if (map.get("DateTimeFromPeriod") != null && map.get("DateTimeFromPeriod").length() == 8)
				from.add(map.get("DateTimeFromPeriod"));
		}
		to.addAll(from);
		if (!to.isEmpty())
			Collections.sort(to);

		if (isInPatient(claimData.get("2300_CLM05")))
			data.put("2300_DTP103", CommonMethods.getCurrentTimeStampHHMM());

		if (to.isEmpty())
			return;

		data.put("2300_DTP203", fixDate(to.get(0)) + "-" + fixDate(to.get(to.size() - 1)));

		if (isInPatient(claimData.get("2300_CLM05"))) {
			data.put("2300_DTP303", fixDate(to.get(0)));
		}

		this.dataHolder.addTableData(data);
	}

	private String getHI02Format(String head, String raw) {
		StringBuilder str = new StringBuilder();
		String[] split = raw.split(",");
		for (String val : split) {
			str.append(head + ":");
			if (val.trim().contains(".")) {
				str.append(val.trim().replace(".", ""));
			} else
				str.append(val.trim());
			str.append("*");
		}
		return str.toString().substring(0, str.length() - 1);
	}

	private void getClaimsHISegments(Map<String, String> claimData) {
		Map<String, String> data = new HashMap<String, String>();
		if (this.claimType == ClaimType.Institutional) {

			if (isInPatient(claimData.get("2300_CLM05"))) {
				String hi01 = getHI02Format("ABK", claimData.get("2300_HI101"));
				hi01 += ":::::::Y";
				data.put("2300_HI101", hi01);
			} else
				data.put("2300_HI101", getHI02Format("ABK", claimData.get("2300_HI101")));
			if (!claimData.get("2300_HI201").isEmpty()) {
				data.put("2300_HI201", getHI02Format("ABF", claimData.get("2300_HI201")));
			}
			if (!claimData.get("2300_HI101").isEmpty()) {
				data.put("2300_HI301", getHI02Format("ABJ", claimData.get("2300_HI101")));
			}

		} else {
			data.put("2300_HI101", getHI02Format("ABK", claimData.get("2300_HI101")));
			if (!claimData.get("2300_HI201").isEmpty())
				data.put("2300_HI102", getHI02Format("ABF", claimData.get("2300_HI201")));
		}
		this.dataHolder.addTableData(data);
	}

	public List<HashMap<String, String>> getClaimLineData(String tcid, String cvtData) {
		String code = this.claimType == ClaimType.Professional ? "1" : "2";
		String claimLineQuery = "Select " + "[ID] 			AS [2400_ID00], " 
		                                  + "[CPTCodeID] 	AS [2400_SV" + code + "01], " 
		                                  + "[Amount]		AS [2400_SV" + code + "02], "
		                                  + "[UnitOrBasisforMeasurementCode] 	AS [2400_SV" + code + "03], " 
		                                  + "[Quantity]		AS [2400_SV" + code + "04]" 
		                                  + ",[DateTimeFromPeriod], [DateTimeToPeriod]\r\n" + "FROM\r\n"
				+ "[dbo].[test_data_claimline]\r\n" + "WHERE \r\n" + "TestCaseID = '" + tcid + "' ";

		List<HashMap<String, String>> data = dbaccess.getListOfHashMapsFromResultSet(claimLineQuery);
		for (Map<String, String> map : data) {

			// SV1/SV2 Segment
			if (this.claimType == ClaimType.Institutional)
				map.put("2400_SV200", cvtData);
			map.put("2400_SV" + code + "01", "HC:" + map.get("2400_SV" + code + "01"));
			map.put("2400_SV" + code + "02", fixDecimals(map.get("2400_SV" + code + "02")));
			map.put("2400_SV" + code + "04", fixDecimals(map.get("2400_SV" + code + "04")));
			if (this.claimType == ClaimType.Professional) {
				map.put("2400_SV" + code + "05", "");
				map.put("2400_SV" + code + "06", "");
				map.put("2400_SV" + code + "07", "1");
			}

			// DTP Segment
			map.put("2400_DTP01", "472");
			if (map.get("DateTimeToPeriod").isEmpty() && map.get("DateTimeToPeriod").length() != 8) {
				map.put("2400_DTP02", "D8");
				map.put("2400_DTP03", fixDate(map.get("DateTimeFromPeriod")));

			} else {
				map.put("2400_DTP02", "RD8");
				map.put("2400_DTP03",
						fixDate(map.get("DateTimeFromPeriod")) + "-" + fixDate(map.get("DateTimeToPeriod")));
			}
		}
		return data;
	}

	// This query is to get Member PCP from ReadyToEnroll table i.e. In Network PCP.
	public HashMap<String, String> getMemberPCP(String medicareID, String planID, String pbpID) {
		String query = "select PCPNPI AS PCP from [VelocityTestAutomation].[dbo].[test_data_readytoenroll] where MedcareID = '"
				+ medicareID + "';";
		HashMap<String, String> pcpNPI = dbaccess.getResultSet(query);
		if (pcpNPI.get("PCP").isEmpty()) {
			return getPcpInNetwork(planID, pbpID);
		}
		return getProviderData("2010AA", pcpNPI.get("PCP"));
	}

	// This query is to get IN Network Specialist based on plan, pbp and taxonomy
	// code
	public HashMap<String, String> getSpecialistInNetwork(String loopCode, String planID, String pbpID,
			String taxonomyCd) {
		String planQuery = "Select Designation as program from [dbo].[product_details] where PlanID = '" + planID
				+ "' and PBPID = '" + pbpID + "';";
		HashMap<String, String> planCode = dbaccess.getResultSet(planQuery);

		/* provider */
		String specialistQuery = "SELECT TOP 1\r\n" + "pt.Name	 	   	   		   AS CATEGORY, \r\n"
				+ "prov.ProvLastNM    		   AS [" + loopCode + "_NM103], \r\n" + "prov.ProvFirstNm    		   AS ["
				+ loopCode + "_NM104], \r\n" + "prov.ProvMidNm      		   AS [" + loopCode + "_NM105], \r\n"
				+ "[ProvSuffixNm] 	   		   AS [" + loopCode + "_NM107], \r\n" + "pract.FedrlTaxIDNum 		   AS ["
				+ loopCode + "_REF02], \r\n" + "prov.ProvNPIID 	   		   AS [" + loopCode + "_NM109], \r\n"
				+ "l.[Std Mail Address Line 1] AS [" + loopCode + "_N301], \r\n" + "l.[Std Mail City] 		   AS ["
				+ loopCode + "_N401], \r\n" + "l.[Std Mail State] 	  	   AS [" + loopCode + "_N402], \r\n"
				+ "l.[Std Mail ZIP] 		   AS [" + loopCode + "_N403],\r\n"
				+ "SPC.SPEC 				   AS [SPECIALTY CODE],\r\n"
				+ "TX.MasterCd 				   AS [TAXANOMY CODE],\r\n" + "pg.ProgCd 				   AS Program\r\n"

				+ "FROM [Profisee].[data].[tProvider] prov\r\n"
				+ "INNER JOIN [Profisee].[SPF].[PracticeParticipant] pp ON pp.ProvFK = prov.Code\r\n"
				+ "INNER JOIN [Profisee].[data].[tPractice] pract ON pract.Code = pp.PractcFK\r\n"
				+ "INNER JOIN [Profisee].[data].[tDG_ProviderType] pt ON pt.Code = prov.ProvTypeFK\r\n"
				+ "INNER JOIN [Profisee].[SPF].[ProviderLocation] pl ON pl.PractcPrtcptFK = PP.PractcPrtcptPK\r\n"
				+ "INNER JOIN Profisee.data.tDG_LocationType lt ON lt.ID = pl.LocTypeFK\r\n"
				+ "INNER JOIN [Profisee].[data].[tLocation] l ON l.Code = pl.LocFK\r\n"
				+ "INNER JOIN [Profisee].[SPF].[PracticeParticipantSpecialty] ps ON ps.PractcPrtcptFK = PP.PractcPrtcptPK\r\n"
				+ "INNER JOIN [Profisee].[data].[vDG_HealthcareSpecialty] hs  ON hs.Code = ps.HlthcrSpcltyFK\r\n"
				+ "Inner Join [Profisee].[data].[tDG_TaxonomyCode] TX ON TX.Code=hs.TaxonomyFK\r\n"
				+ "Inner Join [ProviderMasterStage].[PIMS].[SPEC] SPC ON SPC.Taxonomy=TX.MasterCd\r\n"
				+ "INNER JOIN [profisee].[SPF].[PracticeParticipantProgramContract] pppc ON pppc.PractcPrtcptFK = PP.PractcPrtcptPK\r\n"
				+ "INNER JOIN ProviderMasterHub.HUB.vProgram pg ON pg.ProgPK = pppc.ProgFK\r\n"
				+ "INNER JOIN [Profisee].[data].[tBenefitNetwork] bn ON bn.Program = pg.ProgCd\r\n" + "where   \r\n"
				+ "prov.ProvFirstNm IS NOT NULL and pl.ProvLocEndDt = '2999-12-31' and pg.ProgCd =  '"
				+ planCode.get("program") + "' and TX.MasterCd = '" + taxonomyCd + "'\r\n"
				+ "and l.[Std Mail Address Line 1] NOT LIKE '%BOX%' \r\n" + "ORDER BY  prov.ProvNPIID";
		return dbaccess.getResultSetTwo(specialistQuery);
	}

	// This query is to get OUT OF Network Specialist based on plan, pbp and
	// taxonomy code
	public HashMap<String, String> getSpecialistoutOfNetwork(String loopCode, String planID, String pbpID,
			String taxonomyCd) {
		String planQuery = "Select Designation as program from [dbo].[product_details] where PlanID = '" + planID
				+ "' and PBPID = '" + pbpID + "';";
		HashMap<String, String> planCode = dbaccess.getResultSet(planQuery);

		/* provider */
		String specialistQuery = "SELECT TOP 1\r\n"

				+ "pt.Name	 	   	   		   AS CATEGORY, \r\n" + "prov.ProvLastNM    		   AS [" + loopCode
				+ "_NM103], \r\n" + "prov.ProvFirstNm    		   AS [" + loopCode + "_NM104], \r\n"
				+ "prov.ProvMidNm      		   AS [" + loopCode + "_NM105], \r\n" + "[ProvSuffixNm] 	   		   AS ["
				+ loopCode + "_NM107], \r\n" + "pract.FedrlTaxIDNum 		   AS [" + loopCode + "_REF02], \r\n"
				+ "prov.ProvNPIID 	   		   AS [" + loopCode + "_NM109], \r\n" + "l.[Std Mail Address Line 1] AS ["
				+ loopCode + "_N301], \r\n" + "l.[Std Mail City] 		   AS [" + loopCode + "_N401], \r\n"
				+ "l.[Std Mail State] 	  	   AS [" + loopCode + "_N402], \r\n" + "l.[Std Mail ZIP] 		   AS ["
				+ loopCode + "_N403],\r\n" + "SPC.SPEC 				   AS [SPECIALTY CODE],\r\n"
				+ "TX.MasterCd 				   AS [TAXANOMY CODE],\r\n" + "pg.ProgCd 				   AS Program\r\n"

				+ "FROM [Profisee].[data].[tProvider] prov\r\n"
				+ "INNER JOIN [Profisee].[SPF].[PracticeParticipant] pp ON pp.ProvFK = prov.Code\r\n"
				+ "INNER JOIN [Profisee].[data].[tPractice] pract ON pract.Code = pp.PractcFK\r\n"
				+ "INNER JOIN [Profisee].[data].[tDG_ProviderType] pt ON pt.Code = prov.ProvTypeFK\r\n"
				+ "INNER JOIN [Profisee].[SPF].[ProviderLocation] pl ON pl.PractcPrtcptFK = PP.PractcPrtcptPK\r\n"
				+ "INNER JOIN Profisee.data.tDG_LocationType lt ON lt.ID = pl.LocTypeFK\r\n"
				+ "INNER JOIN [Profisee].[data].[tLocation] l ON l.Code = pl.LocFK\r\n"
				+ "INNER JOIN [Profisee].[SPF].[PracticeParticipantSpecialty] ps ON ps.PractcPrtcptFK = PP.PractcPrtcptPK\r\n"
				+ "INNER JOIN [Profisee].[data].[vDG_HealthcareSpecialty] hs  ON hs.Code = ps.HlthcrSpcltyFK\r\n"
				+ "Inner Join [Profisee].[data].[tDG_TaxonomyCode] TX ON TX.Code=hs.TaxonomyFK\r\n"
				+ "Inner Join [ProviderMasterStage].[PIMS].[SPEC] SPC ON SPC.Taxonomy=TX.MasterCd\r\n"
				+ "INNER JOIN [profisee].[SPF].[PracticeParticipantProgramContract] pppc ON pppc.PractcPrtcptFK = PP.PractcPrtcptPK\r\n"
				+ "INNER JOIN ProviderMasterHub.HUB.vProgram pg ON pg.ProgPK = pppc.ProgFK\r\n"
				+ "INNER JOIN [Profisee].[data].[tBenefitNetwork] bn ON bn.Program = pg.ProgCd\r\n" + "where   \r\n"
				+ "prov.ProvFirstNm IS NOT NULL and pl.ProvLocEndDt = '2999-12-31' and pg.ProgCd !=  '"
				+ planCode.get("program") + "' and TX.MasterCd = '" + taxonomyCd + "'\r\n"
				+ "and l.[Std Mail Address Line 1] NOT LIKE '%BOX%' \r\n"
				+ "and prov.ProvNPIID not in (SELECT prov.ProvNPIID FROM [Profisee].[data].[tProvider] prov\r\n"
				+ "INNER JOIN [Profisee].[SPF].[PracticeParticipant] pp ON pp.ProvFK = prov.Code\r\n"
				+ "INNER JOIN [profisee].[SPF].[PracticeParticipantProgramContract] pppc ON pppc.PractcPrtcptFK = PP.PractcPrtcptPK\r\n"
				+ "INNER JOIN ProviderMasterHub.HUB.vProgram pg ON pg.ProgPK = pppc.ProgFK\r\n" + "where pg.ProgCd =  '"
				+ planCode.get("program") + "'\r\n" + ")\r\n" + "ORDER BY  prov.ProvNPIID";
		return dbaccess.getResultSetTwo(specialistQuery);
	}

	// This query is to get IN Network PCP based on plan, pbp
	public HashMap<String, String> getPcpInNetwork(String planID, String pbpID) {
		String planQuery = "Select Designation AS program from [dbo].[product_details] where PlanID = '" + planID
				+ "' and PBPID = '" + pbpID + "';";
		HashMap<String, String> planCode = dbaccess.getResultSet(planQuery);

		/* provider */
		String pcpQuery = "SELECT TOP 1 " + "pt.Name	 	   	   		   AS CATEGORY, \r\n"
				+ "prov.ProvLastNM    		   AS [2010AA_NM103], \r\n"
				+ "prov.ProvFirstNm    		   AS [2010AA_NM104], \r\n"
				+ "prov.ProvMidNm      		   AS [2010AA_NM105], \r\n" + " dt.DegTypeDesc AS DEGREE,\r\n"
				+ "pract.FedrlTaxIDNum 		   AS [2010AA_REF02], \r\n"
				+ "prov.ProvNPIID 	   		   AS [2010AA_NM109], \r\n" + "pl.ProvLocAltID AS ProvLoc, "
				+ "l.LocAddrLine2Nm AS SUITE, " + "l.[Std Mail Address Line 1] AS [2010AA_N301], \r\n"
				+ "l.[Std Mail City] 		   AS [2010AA_N401], \r\n"
				+ "l.[Std Mail State] 	  	   AS [2010AA_N402], \r\n"
				+ "l.[Std Mail ZIP] 		   AS [2010AA_N403],\r\n" + "l.CntyNm AS CNTY," + " pg.ProgDesc AS Network,"
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
				+ "and l.[Std Mail Address Line 1] NOT LIKE '%BOX%' \r\n" + "ORDER BY  prov.ProvNPIID;";

		return dbaccess.getResultSetTwo(pcpQuery);
	}

	// This query is to get OUT OF Network PCP based on plan, pbp
	public HashMap<String, String> getPcpOutOfNetwork(String planID, String pbpID) {
		String planQuery = "Select Designation AS program from [dbo].[product_details] where PlanID = '" + planID
				+ "' and PBPID = '" + pbpID + "';";
		HashMap<String, String> planCode = dbaccess.getResultSet(planQuery);
		String pcpQuery = "SELECT TOP 1 "

				+ "pt.Name	 	   	   		   AS CATEGORY, \r\n"
				+ "prov.ProvLastNM    		   AS [2010AA_NM103], \r\n"
				+ "prov.ProvFirstNm    		   AS [2010AA_NM104], \r\n"
				+ "prov.ProvMidNm      		   AS [2010AA_NM105], \r\n" + "dt.DegTypeDesc 			   AS DEGREE, \r\n"
				+ "pract.FedrlTaxIDNum 		   AS [2010AA_REF02], \r\n"
				+ "prov.ProvNPIID 	   		   AS [2010AA_NM109], \r\n" + "pl.ProvLocAltID 			   AS ProvLoc, "
				+ "l.LocAddrLine2Nm 		   AS SUITE, " + "l.[Std Mail Address Line 1] AS [2010AA_N301], \r\n"
				+ "l.[Std Mail City] 		   AS [2010AA_N401], \r\n"
				+ "l.[Std Mail State] 	  	   AS [2010AA_N402], \r\n"
				+ "l.[Std Mail ZIP] 		   AS [2010AA_N403],\r\n" + "l.CntyNm AS CNTY, " + "pg.ProgDesc AS Network,"
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
				+ " WHERE pt.Name in  ('Individual') AND pg.ProgCd != '" + planCode.get("program") + "'\r\n"
				+ " AND  pppc.PriCarePhyscnInd = 1 AND pppc.ProvAccptNewPatInd = 1\r\n"
				+ "and l.[Std Mail Address Line 1] NOT LIKE '%BOX%' \r\n"
				+ " AND prov.ProvNPIID not in (SELECT prov.ProvNPIID \r\n"
				+ "FROM [Profisee].[data].[tProvider] prov\r\n"
				+ "INNER JOIN [Profisee].[SPF].[PracticeParticipant] pp ON pp.ProvFK = prov.Code\r\n"
				+ "INNER JOIN [profisee].[SPF].[PracticeParticipantProgramContract] pppc ON pppc.PractcPrtcptFK = PP.PractcPrtcptPK\r\n"
				+ "INNER JOIN ProviderMasterHub.HUB.vProgram pg ON pg.ProgPK = pppc.ProgFK\r\n" + "WHERE pg.ProgCd = '"
				+ planCode.get("program") + "')\r\n" + "ORDER BY  prov.ProvNPIID;";

		return dbaccess.getResultSetTwo(pcpQuery);
	}

	// This query is to get out of Network Hospital for Institutional Claims
	public HashMap<String, String> getOutOfNetworkHospital(String planID, String pbpID) {
		String planQuery = "Select Designation AS program from [dbo].[product_details] where PlanID = '" + planID
				+ "' and PBPID = '" + pbpID + "';";
		HashMap<String, String> planCode = dbaccess.getResultSet(planQuery);

		/* provider */
		String hospitalQuery = "SELECT \r\n" + "TOP 1\r\n"

				+ "pt.Name	 	   	   		   AS CATEGORY, \r\n"
				+ "prov.ProvLastNM    		   AS [2010AA_NM103], \r\n"
				+ "prov.ProvFirstNm    		   AS [2010AA_NM104], \r\n"
				+ "prov.ProvMidNm      		   AS [2010AA_NM105], \r\n"
				+ "[ProvSuffixNm] 	   		   AS [2010AA_NM107], \r\n"
				+ "pract.FedrlTaxIDNum 		   AS [2010AA_REF02], \r\n"
				+ "prov.ProvNPIID 	   		   AS [2010AA_NM109], \r\n"
				+ "l.[Std Mail Address Line 1] AS [2010AA_N301], \r\n"
				+ "l.[Std Mail City] 		   AS [2010AA_N401], \r\n"
				+ "l.[Std Mail State] 	  	   AS [2010AA_N402], \r\n"
				+ "l.[Std Mail ZIP] 		   AS [2010AA_N403],\r\n"
				+ "SPC.SPEC 				   AS [SPECIALTY CODE],\r\n"
				+ "TX.MasterCd 				   AS [TAXANOMY CODE],\r\n" + "pg.ProgCd 				   AS Program\r\n"

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
				+ "on SPC.Taxonomy=TX.MasterCd\r\n" + "\r\n"
				+ "INNER JOIN [profisee].[SPF].[PracticeParticipantProgramContract] pppc\r\n"
				+ "       ON pppc.PractcPrtcptFK = PP.PractcPrtcptPK\r\n"
				+ "INNER JOIN ProviderMasterHub.HUB.vProgram pg\r\n" + "       ON pg.ProgPK = pppc.ProgFK\r\n"
				+ "INNER JOIN [Profisee].[data].[tBenefitNetwork] bn\r\n" + "       ON bn.Program = pg.ProgCd\r\n"
				+ "where   \r\n" + "prov.ProvFirstNm IS NULL and pt.Name = 'Hospital'\r\n"
				+ "and pl.ProvLocEndDt = '2999-12-31'\r\n" + "and pg.ProgCd !=  '" + planCode.get("program") + "'\r\n"
				+ "and l.[Std Mail Address Line 1] NOT LIKE '%BOX%' \r\n" + "and prov.ProvNPIID not in (SELECT \r\n"
				+ "prov.ProvNPIID \r\n" + "FROM [Profisee].[data].[tProvider] prov\r\n"
				+ "INNER JOIN [Profisee].[SPF].[PracticeParticipant] pp\r\n" + "       ON pp.ProvFK = prov.Code\r\n"
				+ "INNER JOIN [profisee].[SPF].[PracticeParticipantProgramContract] pppc\r\n"
				+ "       ON pppc.PractcPrtcptFK = PP.PractcPrtcptPK\r\n"
				+ "INNER JOIN ProviderMasterHub.HUB.vProgram pg\r\n" + "       ON pg.ProgPK = pppc.ProgFK\r\n"
				+ "where pg.ProgCd =  '" + planCode.get("program") + "'\r\n" + ")\r\n" + "ORDER BY  prov.ProvNPIID; ";

		return dbaccess.getResultSetTwo(hospitalQuery);
	}

	// This query is to get In Network Hospital for Institutional Claims
	public HashMap<String, String> getInNetworkHospital(String planID, String pbpID) {
		String planQuery = "Select Designation AS program from [dbo].[product_details] where PlanID = '" + planID
				+ "' and PBPID = '" + pbpID + "';";
		HashMap<String, String> planCode = dbaccess.getResultSet(planQuery);
		String hospitalQuery = "SELECT \r\n" + "TOP 1\r\n"

		/* provider */
				+ "pt.Name	 	   	   		   AS CATEGORY, \r\n"
				+ "prov.ProvLastNM    		   AS [2010AA_NM103], \r\n"
				+ "prov.ProvFirstNm    		   AS [2010AA_NM104], \r\n"
				+ "prov.ProvMidNm      		   AS [2010AA_NM105], \r\n"
				+ "[ProvSuffixNm] 	   		   AS [2010AA_NM107], \r\n"
				+ "pract.FedrlTaxIDNum 		   AS [2010AA_REF02], \r\n"
				+ "prov.ProvNPIID 	   		   AS [2010AA_NM109], \r\n"
				+ "l.[Std Mail Address Line 1] AS [2010AA_N301], \r\n"
				+ "l.[Std Mail City] 		   AS [2010AA_N401], \r\n"
				+ "l.[Std Mail State] 	  	   AS [2010AA_N402], \r\n"
				+ "l.[Std Mail ZIP] 		   AS [2010AA_N403],\r\n"
				+ "SPC.SPEC 				   AS [SPECIALTY CODE],\r\n"
				+ "TX.MasterCd 				   AS [TAXANOMY CODE],\r\n" + "pg.ProgCd 				   AS Program\r\n"

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
				+ "on SPC.Taxonomy=TX.MasterCd\r\n" + "\r\n"
				+ "INNER JOIN [profisee].[SPF].[PracticeParticipantProgramContract] pppc\r\n"
				+ "       ON pppc.PractcPrtcptFK = PP.PractcPrtcptPK\r\n"
				+ "INNER JOIN ProviderMasterHub.HUB.vProgram pg\r\n" + "       ON pg.ProgPK = pppc.ProgFK\r\n"
				+ "INNER JOIN [Profisee].[data].[tBenefitNetwork] bn\r\n" + "       ON bn.Program = pg.ProgCd\r\n"
				+ "where   \r\n" + "prov.ProvFirstNm IS NULL and pt.Name = 'Hospital'\r\n"
				+ "and pl.ProvLocEndDt = '2999-12-31'\r\n" + "and pg.ProgCd =  '" + planCode.get("program") + "'\r\n"
				+ "and l.[Std Mail Address Line 1] NOT LIKE '%BOX%' \r\n" + "ORDER BY  prov.ProvNPIID";
		return dbaccess.getResultSetTwo(hospitalQuery);
	}

	private HashMap<String, String> getAttendingProvider(String testID) {
		String query = "SELECT [PlanID], [PBPID], Contracted, TaxonomyCd FROM test_data_readytoclaims WHERE TestCaseID = '"
				+ testID + "'";
		HashMap<String, String> data = dbaccess.getListOfHashMapsFromResultSet(query).get(0);
		String contracted = data.get("Contracted");
		String planID = data.get("PlanID");
		String pbpID = data.get("PBPID");
		String taxonomyCd = data.get("TaxonomyCd");

		HashMap<String, String> provData = new HashMap<String, String>();
		if (!contracted.isEmpty() && contracted.equals("Y")) {
			provData = getSpecialistInNetwork("2300", planID, pbpID, taxonomyCd);
		} else {
			provData = getSpecialistoutOfNetwork("2300", planID, pbpID, taxonomyCd);
		}

		if (provData.get("2300_NM104").isEmpty())
			provData.put("2300_NM102", "2");
		else
			provData.put("2300_NM102", "1");
		
		String npi = provData.get("2300_NM109");
		query = "UPDATE test_data_readytoclaims \r\n" + "SET AttendingProviderNPI = '" + npi + "' \r\n"
				+ "WHERE TestCaseID = '" + testID + "'";
		dbaccess.executeQuery(query);
		return provData;
	}
	
	//get supplier data from provider database
	
	public HashMap<String, String> getSupplierData(String loopCode, String NPI){
		
		String query = "SELECT TOP 1                      \r\n" + 
				"pt.Name AS CATEGORY, pract.PractcNm AS [" +  loopCode +  "_NM103],                       \r\n" + 
				"'' AS [" +  loopCode +  "_NM104],                         \r\n" + 
				"'' AS [" +  loopCode +  "_NM105], \r\n" + 
				"'' AS [" +  loopCode +  "_NM106],                        \r\n" + 
				"'' AS [" +  loopCode +  "_NM107],                         \r\n" +
				"'XX' AS [" +  loopCode +  "_NM108],                        \r\n" + 
				" pract.FedrlTaxIDNum  AS [" +  loopCode +  "_REF02], \r\n" + 
				" pract.OrgPractcNPIId AS [" +  loopCode +  "_NM109], \r\n" + 
				" l.[Std Mail Address Line 1] AS [" +  loopCode +  "_N301],  l.[Std Mail City]  AS [" +  loopCode +  "_N401],                        \r\n" + 
				"  l.[Std Mail State]  AS [" +  loopCode +  "_N402], l.[Std Mail ZIP]  AS [" +  loopCode +  "_N403] \r\n" + 
				"  FROM [Profisee].[data].[tProvider] prov INNER JOIN [Profisee].[SPF].[PracticeParticipant] pp       ON pp.ProvFK = prov.Code                        \r\n" + 
				"  INNER JOIN [Profisee].[data].[tPractice] pract                                   ON pract.Code = pp.PractcFK                       \r\n" + 
				"  INNER JOIN [Profisee].[data].[tDG_ProviderType] pt     ON pt.Code = prov.ProvTypeFK                         \r\n" + 
				"  INNER JOIN [Profisee].[SPF].[ProviderLocation] pl                                ON pl.PractcPrtcptFK = PP.PractcPrtcptPK                         \r\n" + 
				"  INNER JOIN Profisee.data.tDG_LocationType lt ON lt.ID = pl.LocTypeFK                          \r\n" + 
				"  INNER JOIN [Profisee].[data].[tLocation] l       ON l.Code = pl.LocFK                         \r\n" + 
				"  where   \r\n" + 
				"  prov.ProvNPIID = '" + NPI + "'\r\n" + 
				"  and pl.ProvLocEndDt = '2999-12-31'                          \r\n" + 
				"  and l.[Std Mail Address Line 1] NOT LIKE '%BOX%'                                                     \r\n" + 
				"  ORDER BY  prov.ProvNPIID;";
		HashMap<String, String> supplierData = dbaccess.getResultSetTwo(query);
		if(supplierData.get("2010AA_NM104").isEmpty())
			supplierData.put("2010AA_NM102", "2");
		else
			supplierData.put("2010AA_NM102", "1");
		return supplierData;
	}

	public static void main(String[] args) {
		ClaimFileCreation cfc = new ClaimFileCreation(ClaimType.Professional);
		//Loop loop = cfc.readXML("institutional_structure.xml");
		//System.out.println(loop.toString());
		cfc.create837ClaimsFile();
	}
}