package x12.encounters;

import java.io.File;
import java.io.IOException;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import com.imsweb.x12.Element;
import com.imsweb.x12.Loop;
import com.imsweb.x12.Segment;
import com.imsweb.x12.reader.X12Reader;
import com.imsweb.x12.reader.X12Reader.FileType;

import pageclasses.CommonMethods;

// ST_LOOP - one transaction
public class EncounterData {

	public static final String foldername = "data//Ready_to_Encounter_Data";

	// Details of Provider, Subscriber itself and
	// A list of all the service lines [LX Fields]
	public class Subscriber {
		private String NPI, TaxID;
		private String EntityTypeQualifier;
		private String firstName, lastName, middleInitial;
		private String organisationName;
		private String subscriberPrimaryIdentifier;
		private String claimAmount;
		private String placeOfServiceCode, claimFrequencyCode;
		private String claimNumber;
		private List<Diagnosis> diagnosisCodes;
		private List<ServiceLine> serviceLines;
		private String claimType;
		private String submitterID;

		public Subscriber(String NPI, String TaxID) {
			this.NPI = NPI;
			this.TaxID = TaxID;
			diagnosisCodes = new LinkedList<Diagnosis>();
			serviceLines = new LinkedList<ServiceLine>();
		}

		public Subscriber() {
			diagnosisCodes = new LinkedList<Diagnosis>();
			serviceLines = new LinkedList<ServiceLine>();
		}

		public class Diagnosis {
			private String diagnosisTypeCode, diagnosisCode;

			public String getDiagnosisTypeCode() {
				return diagnosisTypeCode;
			}

			public void setDiagnosisTypeCode(String diagnosisTypeCode) {
				this.diagnosisTypeCode = diagnosisTypeCode;
			}

			public String getDiagnosisCode() {
				return diagnosisCode;
			}

			public void setDiagnosisCode(String diagnosisCode) {
				this.diagnosisCode = diagnosisCode;
			}

			public Diagnosis(String type, String code) {
				this.diagnosisCode = code;
				this.diagnosisTypeCode = type;
			}

			public String toString() {
				return "(" + this.diagnosisTypeCode + ", " + this.diagnosisCode + ")";
			}
		}

		public class ServiceLine {
			private int ServiceLineNumber; // LX Field numbers
			private Map<String, String> professionalService; // SV1 fields - map

			public ServiceLine(Element element, List<Element> elements) {
				this.professionalService = new LinkedHashMap<String, String>();
				this.ServiceLineNumber = Integer.parseInt(element.getValue());
				this.setProfessionalServices(elements);
			}

			public void setProfessionalServices(List<Element> raw) {
				for (Element elem : raw) {
					professionalService.put(elem.getId(), elem.getValue());
				}
			}

			public int getServiceLineNumber() {
				return this.ServiceLineNumber;
			}

			public Map<String, String> getProfessionalService() {
				return this.professionalService;
			}

			public String toString() {
				return "\n\t\tLX" + this.ServiceLineNumber + " : SV1 " + this.professionalService.toString();
			}
		}

		public void setEntityTypeQualifier(String EntityTypeQualifier) {
			this.EntityTypeQualifier = EntityTypeQualifier;
		}

		public String getEntityTypeQualifier() {
			return this.EntityTypeQualifier;
		}

		public void setName(String firstName, String lastName, String middleInitial) {
			this.firstName = firstName;
			this.lastName = lastName;
			this.middleInitial = middleInitial;
		}

		public String getFirstName() {
			return this.firstName;
		}

		public String getMiddleInitial() {
			return this.middleInitial;
		}

		public String getLastName() {
			return this.lastName;
		}

		public void setOrganisationName(String name) {
			this.organisationName = name;
		}

		public String getOrganisationName() {
			return this.organisationName;
		}

		public void setSubscriberPrimaryIdentifier(String SubscriberPrimaryIdentifier) {
			this.subscriberPrimaryIdentifier = SubscriberPrimaryIdentifier;
		}

		public String getSubscriberPrimaryIdentifier() {
			return this.subscriberPrimaryIdentifier;
		}

		public String getClaimAmount() {
			return this.claimAmount;
		}

		public void setClaimAmount(String amount) {
			this.claimAmount = amount;
		}

		public void setPlaceOfServiceCode(String code) {
			this.placeOfServiceCode = code;
		}

		public String getPlaceOfServiceCode() {
			return placeOfServiceCode;
		}

		public void setClaimFrequencyCode(String code) {
			this.claimFrequencyCode = code;
		}

		public String getClaimFrequencyCode() {
			return claimFrequencyCode;
		}

		// input format - [HI*ABK:M545*ABF:M5136*ABF:M6281]
		// split based on *, for every code, get the type,code pair and append to the
		// list.
		public void addDiagnosisCodes(String raw) {
			String[] blocks = raw.split("\\*");
			for (int i = 1; i < blocks.length; i++) {
				this.diagnosisCodes.add(new Diagnosis(blocks[i].split(":")[0], blocks[i].split(":")[1]));
			}
		}

		public List<Diagnosis> getDiagnosisCodes() {
			return this.diagnosisCodes;
		}

		public void addServiceLines(Element element, List<Element> elements) {
			this.serviceLines.add(new ServiceLine(element, elements));
		}

		public List<ServiceLine> getServiceLines() {
			return this.serviceLines;
		}

		public String getClaimNumber() {
			return this.claimNumber;
		}

		public void setClaimNumber(String number) {
			this.claimNumber = number;
		}

		public String getNPI() {
			return this.NPI;
		}

		public void setNPI(String npi) {
			this.NPI = npi;
		}

		public String getTaxID() {
			return this.TaxID;
		}

		public void setTaxID(String taxID) {
			this.TaxID = taxID;
		}

		public void setClaimType(String type) {
			this.claimType = type;
		}

		public String getClaimType() {
			return this.claimType;
		}

		public void setSubmitterID(String submitterID) {
			this.submitterID = submitterID;
		}

		public String getSubmitterID() {
			return this.submitterID;
		}

		public String toString() {
			StringBuilder s = new StringBuilder();
			if (getEntityTypeQualifier().equals("1"))
				s.append("\nSubscriber Name: " + getLastName() + ", " + getFirstName() + " " + getMiddleInitial());
			else
				s.append("Organisation Name: " + getOrganisationName());
			s.append("\nSubscriber Primary Identifier : " + getSubscriberPrimaryIdentifier());
			s.append("\nNPI & TaxID: " + this.NPI + ", " + this.TaxID);
			s.append("\nClaim Amount: " + this.claimAmount);
			s.append("\nClaim Number: " + this.claimNumber);
			s.append("\nSubmitterID: " + this.submitterID);
			s.append("\nPlace of Service Code: " + this.placeOfServiceCode);
			s.append("\nClaim Frequency Code: " + this.claimFrequencyCode);
			s.append("\nDiagnosis Codes: " + this.diagnosisCodes.toString());
			s.append("\nService Lines: " + this.serviceLines.toString());
			s.append("\nClaim Type: " + this.claimType);
			return s.toString();
		}

	}

	private List<Subscriber> getEncounterData(File file) {
		X12Reader reader;
		List<Subscriber> subscriberDetail = new LinkedList<Subscriber>();

		try {

			// put folder name, and add multiple files to the folder.
			// read the files and get the subscribers
			reader = new X12Reader(FileType.ANSI837_5010_X222, file);

			String filename = file.getName().substring(8, 10);

			String prefixFilename = file.getName().substring(0, 7);

			List<Loop> ISALoops = reader.getLoops();

			// for every ISA Loop, extract the ST Loop
			for (Loop ISALoop : ISALoops) {
				List<Loop> ST_LOOPs = ISALoop.findLoop("ST_LOOP");

				// for every ST Loop, there is a HEADER DETAIL pair
				// get the DETAIL Loop
				for (Loop ST_LOOP : ST_LOOPs) {
					List<Loop> details = ST_LOOP.findLoop("DETAIL");

					// in every DETAIL Loop, there is-
					// 2010AA Loop for NPI and TaxID
					// 2000B Loops for subscribers
					for (Loop detail : details) {
						Loop providerDetails = detail.getLoop("2010AA");

						String NPI = providerDetails.getElement("NM1", "NM109");
						String TaxID = providerDetails.getElement("REF", "REF02");

						// for every subscriber in 2000B Loop, extract the details
						List<Loop> subscribers = detail.findLoop("2000B");
						for (Loop subscriberRecord : subscribers) {
							Subscriber record = new Subscriber(NPI, TaxID);

							Loop loopBA = subscriberRecord.getLoop("2010BA");

							// Subscriber Details
							record.setEntityTypeQualifier(loopBA.getElement("NM1", "NM102"));
							if (loopBA.getElement("NM1", "NM101").equals("IL")) {
								if (loopBA.getElement("NM1", "NM102").equals("1")) {
									// person
									record.setName(loopBA.getElement("NM1", "NM104"), loopBA.getElement("NM1", "NM103"),
											loopBA.getElement("NM1", "NM105"));

								} else if (loopBA.getElement("NM1", "NM102").equals("2")) {
									// non person
									record.setOrganisationName(loopBA.getElement("NM1", "NM103"));
								}
							}
							record.setSubscriberPrimaryIdentifier(loopBA.getElement("NM1", "NM109"));

							// get the claims loop 2300
							Loop claimLoop = subscriberRecord.getLoop("2300");
							record.setClaimAmount(claimLoop.getElement("CLM", "CLM02"));
							record.setClaimNumber(claimLoop.getElement("CLM", "CLM01"));

							// Place of Service and Claim Frequency TypeCode
							String service_claim[] = claimLoop.getElement("CLM", "CLM05").split(":");
							record.setPlaceOfServiceCode(service_claim[0]);
							record.setClaimFrequencyCode(service_claim[2]);

							// Get the Diagnosis Codes
							Segment HI_Codes = claimLoop.getSegment("HI");
							record.addDiagnosisCodes(HI_Codes.toString());

							List<Loop> LX_Loops = claimLoop.findLoop("2400");
							for (Loop lx : LX_Loops) {
								Segment lxField = lx.getSegment("LX");
								Segment sv1Field = lx.getSegment("SV1");
								record.addServiceLines(lxField.getElement("LX01"), sv1Field.getElements());
							}

							record.setClaimType(filename);
							record.setSubmitterID(prefixFilename);
							subscriberDetail.add(record);
						}
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
		return subscriberDetail;
	}

	public List<Subscriber> getEncounterData() {
		File[] files = CommonMethods.getAllFilesfromDir(new File(foldername));
		List<Subscriber> list = new LinkedList<Subscriber>();
		for (File file : files) {
			list.addAll(getEncounterData(file));
		}
		return list;
	}

	public static void main(String[] args) {
		EncounterData e = new EncounterData();
		List<Subscriber> data = e.getEncounterData();

		for (Subscriber s : data) {
			System.out.println(s.toString()); // medicareID
		}
	}
}
