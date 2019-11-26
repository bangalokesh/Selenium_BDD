package EnrollmentFiles;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.sql.Statement;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import org.junit.Test;

import pageclasses.BasePage;
import utils.Const;
import utils.Dbconn;

public class CreateFileFromLayout extends BasePage {

	Dbconn db = new Dbconn();

	private ResultSet getResults(String query) {
		try {
			Connection conn = Dbconn.getConnection();
			Statement st = conn.createStatement();
			return st.executeQuery(query);
		} catch (Exception e) {
			e.printStackTrace();
			return null;
		}
	}

	public List<Map<String, String>> readFileFromLayout(String propertiesFile, String dataFilePath, String delimiter)
			throws IOException {

		List<Map<String, String>> fileData = new LinkedList<Map<String, String>>();
		BufferedReader dataFile = new BufferedReader(new FileReader(new File(dataFilePath)));

		Map<String, Integer> properties = loadProperties(propertiesFile);
		String line;
		while ((line = dataFile.readLine()) != null) {

			String[] data = line.split(delimiter);
			Map<String, String> record = new LinkedHashMap<String, String>();
			int index = 0;
			for (String key : properties.keySet()) {
				if (index < data.length) {
					record.put(key, data[index]);
					index++;
				} else {
					record.put(key, null);
				}
			}
			fileData.add(record);
		}
		dataFile.close();
		return fileData;
	}

	private String getTrailer(int count, String app) {
		count = count + 2;
		String formatDateTime = getRandomTimeToday();
		// return "TRAILER" + (formatDateTime) + new
		// DecimalFormat("0000000000").format(count);
		return "TRLR" + app + (formatDateTime) + new DecimalFormat("0000000000").format(count);
	}

	public void createFileForLayout(String pathToPropertiesFile, String query, String target, char delimiter)
			throws IOException, SQLException {

		ResultSet rs = getResults(query);
		ResultSetMetaData rsmd = rs.getMetaData();

		int columnsNumber = rsmd.getColumnCount();
		List<String> columnName = new ArrayList<String>();
		for (int i = 1; i < columnsNumber; i++) {
			columnName.add(rsmd.getColumnName(i));
		}

		BufferedWriter writer = new BufferedWriter(new FileWriter(new File(target)));
		StringBuilder s = new StringBuilder();

		Map<String, Integer> properties = loadProperties(pathToPropertiesFile);

		for (String head : properties.keySet()) {
			if (columnName.contains(head)) {
				s.append(head);
				s.append(delimiter);
			}
		}
		s.append("\n");
		writer.append(s.toString());
		s = new StringBuilder();

		int count = 0;
		while (rs.next()) {

			for (String property : properties.keySet()) {
				if (columnName.contains(property)) {
					String value = rs.getObject(property) == null ? "" : rs.getObject(property).toString();
					if (value.length() > properties.get(property)) {
						value = value.substring(0, properties.get(property));
					}
					System.out.println(property);
					s.append(value.trim());
					s.append(delimiter);
				}
			}
			s.append("\r\n");
			writer.append(s.toString());
			s = new StringBuilder();
			count++;
		}
		if (target.contains("EEMT.OEC.UPLOAD.AON"))
			writer.append(this.getTrailer(count, "AON"));
		else if (target.contains("EEMT.OEC.UPLOAD.EHT"))
			writer.append(this.getTrailer(count, "EHT"));
		else if (target.contains("EEMT.OEC.UPLOAD.HPO"))
			writer.append(this.getTrailer(count, "HPT"));
		else if (target.contains("EEMT.OEC.UPLOAD.EXT"))
			writer.append(this.getTrailer(count, "EXT"));
		else if (target.contains("EEMT.OEC.UPLOAD.CLROL2019"))
			writer.append(this.getTrailer(count, "C19"));
		else if (target.contains("EEMT.OEC.UPLOAD.CLROL2020"))
			writer.append(this.getTrailer(count, "C20"));
		writer.append("\r\n");
		writer.close();
	}

	public void generateOEC_CMS_File() throws IOException, SQLException {
		String query = "SELECT * FROM [VelocityTestAutomation].[dbo].[OECFileCreation] WHERE OECType = 'CMS'";
		String target = Const.OECFILEPATH + "EEMT.OEC.UPLOAD.OEC." + getCurrentDateTimeStampVal() + ".TXT";
		String properties = Const.OECProperties;
		char delimeter = '\t';
		createFileForLayout(properties, query, target, delimeter);
	}

	public void generateOEC_EH_File() throws IOException, SQLException {
		String query = "SELECT * FROM [VelocityTestAutomation].[dbo].[OECFileCreation] WHERE OECType = 'EH'";
		String target = Const.OECFILEPATH + "EEMT.OEC.UPLOAD.EHT." + getCurrentDateTimeStampVal() + ".TXT";
		String properties = Const.OECCallCenterProperties;
		char delimeter = '\t';
		createFileForLayout(properties, query, target, delimeter);
	}

	public void generateOEC_AON_File() throws IOException, SQLException {
		String query = "SELECT * FROM [VelocityTestAutomation].[dbo].[OECFileCreation] WHERE OECType = 'AON'";
		String target = Const.OECFILEPATH + "EEMT.OEC.UPLOAD.AON." + getCurrentDateTimeStampVal() + ".TXT";
		String properties = Const.OECAONProperties;
		char delimeter = '\t';
		createFileForLayout(properties, query, target, delimeter);
	}

	public void generateOEC_HPT_File() throws IOException, SQLException {
		String query = "SELECT * FROM [VelocityTestAutomation].[dbo].[OECFileCreation] WHERE OECType = 'HPT'";
		String target = Const.OECFILEPATH + "EEMT.OEC.UPLOAD.HPO." + getCurrentDateTimeStampVal() + ".TXT";
		String properties = Const.OECCallCenterProperties;
		char delimeter = '\t';
		createFileForLayout(properties, query, target, delimeter);
	}

	public void generateOEC_CLR_2019_File() throws IOException, SQLException {
		String query = "SELECT * FROM [VelocityTestAutomation].[dbo].[OECFileCreation] WHERE OECType = 'CLR' AND [EnrollmentPlanYear] = '2019'";
		String target = Const.OECFILEPATH + "EEMT.OEC.UPLOAD.CLROL2019." + getCurrentDateTimeStampVal() + ".TXT";
		String properties = Const.OECCLRProperties;
		char delimeter = '\t';
		createFileForLayout(properties, query, target, delimeter);
	}

	public void generateOEC_CLR_2020_File() throws IOException, SQLException {
		String query = "SELECT * FROM [VelocityTestAutomation].[dbo].[OECFileCreation] WHERE OECType = 'CLR' AND [EnrollmentPlanYear] = '2020'";
		String target = Const.OECFILEPATH + "EEMT.OEC.UPLOAD.CLROL2020." + getCurrentDateTimeStampVal() + ".TXT";
		String properties = Const.OECCLRProperties;
		char delimeter = '\t';
		createFileForLayout(properties, query, target, delimeter);
	}

	public void generateOEC_EXT_File() throws IOException, SQLException {
		String query = "SELECT * FROM [VelocityTestAutomation].[dbo].[OECFileCreation] WHERE OECType = 'EXT'";
		String target = Const.OECFILEPATH + "EEMT.OEC.UPLOAD.EXT." + getCurrentDateTimeStampVal() + ".TXT";
		String properties = Const.OECCallCenterProperties;
		char delimeter = '\t';
		createFileForLayout(properties, query, target, delimeter);
	}

	@Test
	public void executeCMS() {
		try {
			generateOEC_CMS_File();
			generateOEC_EXT_File();
			generateOEC_CLR_2019_File();
			generateOEC_CLR_2020_File();
			generateOEC_HPT_File();
			generateOEC_EH_File();
			generateOEC_AON_File();

		} catch (IOException | SQLException e) {
			e.printStackTrace();
		}
	}
}
