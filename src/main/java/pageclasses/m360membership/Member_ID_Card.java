package pageclasses.m360membership;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;

import org.apache.commons.io.FileUtils;

import com.aventstack.extentreports.Status;

import pageclasses.BasePage;
import utils.Const;
import utils.Dbconn;

public class Member_ID_Card extends BasePage {
	Dbconn dbaccess = new Dbconn();
	boolean member_exists;

	public void Member_ID_Validation() {

		File[] files = getSampleFiles();
		if (files == null || files.length < 1) {
			logger.info("There are no Member ID Card Files to validate. So did not run any tests.");
			test = extent.createTest("Sample Member ID Card Validation Files are not available");
			test.log(Status.FAIL, "There are no Member ID Card Files to validate. So did not run any tests.");
			return;
		}

		String query = "SELECT md.FirstName + ' '+ md.MiddleInitial +' '+ md.LastName AS FullName, rn.MemberLName, ad.Address1 + ' '+ ad.Address2+' '+ ad.Address3 AS Address, ad.City, ad.StateCode, ad.ZipCode, pd.OldPrefix, rn.ProductPlanID, rn.ProductPBPID, md.FirstName, md.LastName, md.MiddleInitial, md.Suffix, md.SupplementalID, rn.PCPDoctorName, pd.PlanName, pd.NewPrefix FROM test_data_readytoenroll rn JOIN member_demographic md ON rn.MedCareID = md.MedicareID JOIN address_details ad ON rn.AddressID = ad.ID JOIN product_details pd ON rn.ProductPlanID = pd.PlanID AND rn.ProductPBPID = pd.PBPID WHERE rn.RunMode='Y' AND rn.MemValStatus != ''";
		List<HashMap<String, String>> rs = dbaccess.getListOfHashMapsFromResultSet(query);
		for (HashMap<String, String> dbValues : rs) {
			member_exists = false;
			test = extent.createTest(
					"Member ID Card Validation Report for enrollment ID: " + dbValues.get("SupplementalID"));
			generateMemberPropertyHashValues(dbValues.get("SupplementalID"), dbValues);
		}

		flushTest();

		if (!rs.isEmpty()) {
			moveFilesToArchive(Const.ID_Card_Member_Extract, Const.ID_Card_Member_Extract_Archive);
		}
	}

	public void generateMemberPropertyHashValues(String enrolleeID, HashMap<String, String> dbValues) {
		HashMap<String, String> member_Map_00 = null;
		File[] files = getSampleFiles();

		outerloop: for (File file : files) {
			try {
				BufferedReader dataFile = new BufferedReader(new FileReader(new File(file.getPath())));
				HashMap<String, String> properties = loadProperties(Const.MEM_ID_00_PROPERTY_FILE, "00");
				member_Map_00 = new LinkedHashMap<String, String>();

				String line_00;
				while ((line_00 = dataFile.readLine()) != null) {
					String line_01 = dataFile.readLine();

					for (String key : properties.keySet()) {
						String[] pos = properties.get(key).split(" ");
						int start = Integer.parseInt(pos[0]);
						int end = Integer.parseInt(pos[1]);
						String memberValue = line_00.substring(start, end);

						member_Map_00.put(key, memberValue);
					}

					HashMap<String, String> member_Map_01 = new LinkedHashMap<String, String>();
					member_Map_01 = build_01_Map(line_01);

					member_Map_00.putAll(member_Map_01);

					if (member_Map_00.get("SupplementalID").trim().equals(enrolleeID)) {
						member_exists = true;
						validate_Member_ID_Values(member_Map_00, enrolleeID, dbValues, file.getName());
						break outerloop;
					}
				}

				dataFile.close();
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

		if (member_exists == false) {
			test.log(Status.FAIL, "Member ID: " + enrolleeID + " does not exist in all sample files");
		}
	}

	public HashMap<String, String> build_01_Map(String line) {
		HashMap<String, String> member_Map = new LinkedHashMap<String, String>();
		HashMap<String, String> properties_2 = loadProperties(Const.MEM_ID_01_PROPERTY_FILE, "01");
		for (String key : properties_2.keySet()) {
			String[] pos = properties_2.get(key).split(" ");
			int start = Integer.parseInt(pos[0]);
			int end = Integer.parseInt(pos[1]);
			String memberValue = line.substring(start, end);
			member_Map.put(key, memberValue);
		}

		return member_Map;
	}

	public HashMap<String, String> loadProperties(String filePath, String value) {
		HashMap<String, String> ID_Map = new LinkedHashMap<String, String>();

		try {
			BufferedReader fileReader = new BufferedReader(new FileReader(new File(filePath)));
			String line;
			while ((line = fileReader.readLine()) != null) {
				String header = line.split("=")[0].trim();
				String startEnd = line.split("=")[1].trim();
				ID_Map.put(header.trim(), startEnd.trim());
			}
			fileReader.close();
		} catch (IOException e) {
			System.err.println("Error in property file - cannot load properties.");
		}

		return ID_Map;
	}

	public void validate_Member_ID_Values(HashMap<String, String> member_Map, String enrolleeID,
			HashMap<String, String> dbValues, String fileName) throws IOException {
		boolean flag = true;
		HashMap<String, String> property = member_Map;
		try {
			if (compareHashMaps(property, dbValues)) {
				reportPass("Member ID Matched: - " + property);
				flag = true;
			} else {
				reportFail("Member ID Not Matched: - Database NP = " + dbValues + "\n Member ID Card Extract = "
						+ property);
				flag = false;
			}
		} catch (Exception e) {
			logger.info("Error: " + e.getMessage());
		}
	}

	public File[] getSampleFiles() {
		File folder = new File(Const.ID_Card_Member_Extract);
		File[] files = folder.listFiles();

		return files;
	}

	public void moveFilesToArchive(String source, String target) {
		File destFile = new File(target);
		File folder = new File(source);
		File[] files = folder.listFiles();
		if (files == null || files.length < 1) {
			logger.info("No Files to move from: " + source);
			return;
		}

		for (File file : files) {
			try {
				File f = new File(target + file.getName());
				if (f.exists()) {
					f.delete();
				}

				FileUtils.moveFileToDirectory(file, destFile, true);
			} catch (Exception e) {
				logger.info("Problem moving file to " + target + " folder");
			}
		}
	}
}