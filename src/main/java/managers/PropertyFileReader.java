package managers;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;

import utils.Const;

public class PropertyFileReader {

	private static String m360Url;
	private static String m360UserId;
	private static String m360Password;
	private static String healthRulesPayor;
	private static String hrpUserId;
	private static String hrpPassword;
	private static String pCS;
	private static String providerGoldenRecordUrl;
	private static String cRM;
	private static String callidusUrl;
	private static String callidusUserId;
	private static String callidusPassword;
	private static String guidingCareUserId;
	private static String guidingCarePassword;
	private static String guidingCareUrl;
	private static String guidingCareIntakeUserId;
	private static String guidingCareIntakePassword;
	private static String guidingCareUMSupervisorUserId;
	private static String guidingCareUMSupervisorePassword;
	private static String guidingCareUMClinicalReviewerUserId;
	private static String guidingCareUMClinicalReviewerPassword;
	private static String guidingCarePhysicianReviewerUserId;
	private static String guidingCarePhysicianReviewerPassword;
	private static String guidingCareUMTechUserId;
	private static String guidingCareUMTechPassword;
	private static String oracleFinancial;
	private static String aZBlueMedicareUrl;
	private static String clearstoneUrl;
	private static String healthXMemberPortalUrl;
	private static String healthXMemberPortalUserId;
	private static String healthXMemberPortalPassword;
	private static String healthXProviderPortalUrl;
	private static String healthXProviderPortalUserId;
	private static String healthXProviderPortalPassword;
	private static String healthXAdminPortalUrl;
	private static String healthXAdminPortalUserId;
	private static String healthXAdminPortalPassword;
	private static String appTracker;
	private static String tPSCash;
	private static String dbUserId;
	private static String dbPassword;
	private static String hrpAppName;
	private static String brokerPortalUrl;
	private static String brokerPortalPassword;
	private static JSONObject jsonRoot;
	private static ObjectMapper objectMapper = new ObjectMapper();
	@SuppressWarnings("unused")
	private static Map<String, String> myMap = new HashMap<String, String>();
	private static JsonNode envNode;

	@SuppressWarnings({ "unchecked" })
	public static void getProperty() {
		try {
			byte[] mapData = Files.readAllBytes(Paths.get(Const.ENVPROPERTIES));
			myMap = objectMapper.readValue(mapData, HashMap.class);
			JsonNode rootNode = objectMapper.readTree(mapData);
			envNode = rootNode.path(Const.ENV);
			JsonNode m360 = envNode.get("M360");
			JsonNode HRP = envNode.get("HRP");
			JsonNode DB = envNode.get("Database");
			JsonNode callidus = envNode.get("Callidus");
			JsonNode brokerPortal = envNode.get("BrokerPortal");
			JsonNode guidingCare = envNode.get("guidingCareUrl");
			JsonNode guidingCareIntake = envNode.get("guidingCareUMIntake");
			JsonNode guidingCareUMSupervisor = envNode.get("guidingCareUMSupervisor");
			JsonNode guidingCareUMClinicalReviewer = envNode.get("guidingCareUMClinicalReviewer");
			JsonNode guidingCarePhysicianReviewer = envNode.get("guidingCarePhysicianReviewer");
			JsonNode guidingCareUMTech = envNode.get("guidingCareUMTech");
			JsonNode healthXAdminPortal = envNode.get("healthXAdminPortalUrl");
			JsonNode healthXProviderPortal = envNode.get("healthXProviderPortalUrl");
			JsonNode healthXMemberPortal = envNode.get("healthXMemberPortalUrl");
			
			setM360Url(m360.get("url").toString());
			setM360UserId(m360.get("userId").toString());
			setm360Password(m360.get("password").toString());

			setHrpUserId(HRP.get("userId").toString());
			setHrpPassword(HRP.get("password").toString());
			setHRPAppName(HRP.get("appName").toString());

			setDbUserId(DB.get("userId").toString());
			setDbPassword(DB.get("password").toString());

			setCallidusUrl(callidus.get("url").toString());
			setCallidusUserId(callidus.get("userId").toString());
			setCallidusPassword(callidus.get("password").toString());
			setBrokerPortalUrl(brokerPortal.get("url").toString());
			setBrokerPortalPassword(brokerPortal.get("password").toString());

			setGuidingCareUrl(guidingCare.get("url").toString());
			setGuidingCareUserId(guidingCare.get("userId").toString());
			setGuidingCarePassword(guidingCare.get("password").toString());
			
			setGuidingCareIntakeUserId(guidingCareIntake.get("userId").toString());
			setGuidingCareIntakePassword(guidingCareIntake.get("password").toString());
			
			setGuidingCareUMSupervisorUserId(guidingCareUMSupervisor.get("userId").toString());
			setGuidingCareUMSupervisorePassword(guidingCareUMSupervisor.get("password").toString());
			
			setGuidingCareUMClinicalReviewerUserId(guidingCareUMClinicalReviewer.get("userId").toString());
			setGuidingCareUMClinicalReviewerPassword(guidingCareUMClinicalReviewer.get("password").toString());
			
			setGuidingCarePhysicianReviewerUserId(guidingCarePhysicianReviewer.get("userId").toString());
			setGuidingCarePhysicianReviewerPassword(guidingCarePhysicianReviewer.get("password").toString());
			
			setGuidingCareUMTechUserId(guidingCareUMTech.get("userId").toString());
			setGuidingCareUMTechPassword(guidingCareUMTech.get("password").toString());
			
			setHealthXAdminPortalUrl(healthXAdminPortal.get("url").toString());
			setHealthXAdminPortalUserId(healthXAdminPortal.get("userId").toString());
			setHealthXAdminPortalPassword(healthXAdminPortal.get("password").toString());
			
			setHealthXProviderPortalUrl(healthXProviderPortal.get("url").toString());
			setHealthXProviderPortalUserId(healthXProviderPortal.get("userId").toString());
			setHealthXProviderPortalPassword(healthXProviderPortal.get("password").toString());
			
			setHealthXMemberPortalUrl(healthXMemberPortal.get("url").toString());
			setHealthXMemberPortalUserId(healthXMemberPortal.get("userId").toString());
			setHealthXMemberPortalPassword(healthXMemberPortal.get("password").toString());
			
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public static String getGuidingCareIntakeUserId() {
		if (guidingCareIntakeUserId == null)
			getProperty();
		return guidingCareIntakeUserId;
	}

	public static void setGuidingCareIntakeUserId(String guidingCareIntakeUserId) {
		PropertyFileReader.guidingCareIntakeUserId = guidingCareIntakeUserId;
	}

	public static String getGuidingCareIntakePassword() {
		if (guidingCareIntakePassword == null)
			getProperty();
		return guidingCareIntakePassword;
	}

	public static void setGuidingCareIntakePassword(String guidingCareIntakePassword) {
		PropertyFileReader.guidingCareIntakePassword = guidingCareIntakePassword;
	}

	public static String getGuidingCareUMSupervisorUserId() {
		if (guidingCareUMSupervisorUserId == null)
			getProperty();
		return guidingCareUMSupervisorUserId;
	}

	public static void setGuidingCareUMSupervisorUserId(String guidingCareUMSupervisorUserId) {
		PropertyFileReader.guidingCareUMSupervisorUserId = guidingCareUMSupervisorUserId;
	}

	public static String getGuidingCareUMSupervisorePassword() {
		if (guidingCareUMSupervisorePassword == null)
			getProperty();
		return guidingCareUMSupervisorePassword;
	}

	public static void setGuidingCareUMSupervisorePassword(String guidingCareUMSupervisorePassword) {
		PropertyFileReader.guidingCareUMSupervisorePassword = guidingCareUMSupervisorePassword;
	}

	public static String getGuidingCareUMClinicalReviewerUserId() {
		if (guidingCareUMClinicalReviewerUserId == null)
			getProperty();
		return guidingCareUMClinicalReviewerUserId;
	}

	public static void setGuidingCareUMClinicalReviewerUserId(String guidingCareUMClinicalReviewerUserId) {
		PropertyFileReader.guidingCareUMClinicalReviewerUserId = guidingCareUMClinicalReviewerUserId;
	}

	public static String getGuidingCareUMClinicalReviewerPassword() {
		if (guidingCareUMClinicalReviewerPassword == null)
			getProperty();
		return guidingCareUMClinicalReviewerPassword;
	}

	public static void setGuidingCareUMClinicalReviewerPassword(String guidingCareUMClinicalReviewerPassword) {
		PropertyFileReader.guidingCareUMClinicalReviewerPassword = guidingCareUMClinicalReviewerPassword;
	}

	public static String getGuidingCarePhysicianReviewerUserId() {
		if (guidingCarePhysicianReviewerUserId == null)
			getProperty();
		return guidingCarePhysicianReviewerUserId;
	}

	public static void setGuidingCarePhysicianReviewerUserId(String guidingCarePhysicianReviewerUserId) {
		PropertyFileReader.guidingCarePhysicianReviewerUserId = guidingCarePhysicianReviewerUserId;
	}

	public static String getGuidingCarePhysicianReviewerPassword() {
		if (guidingCarePhysicianReviewerPassword == null)
			getProperty();
		return guidingCarePhysicianReviewerPassword;
	}

	public static void setGuidingCarePhysicianReviewerPassword(String guidingCarePhysicianReviewerPassword) {
		PropertyFileReader.guidingCarePhysicianReviewerPassword = guidingCarePhysicianReviewerPassword;
	}

	public static String getGuidingCareUMTechUserId() {
		if (guidingCareUMTechUserId == null)
			getProperty();
		return guidingCareUMTechUserId;
	}

	public static void setGuidingCareUMTechUserId(String guidingCareUMTechUserId) {
		PropertyFileReader.guidingCareUMTechUserId = guidingCareUMTechUserId;
	}

	public static String getGuidingCareUMTechPassword() {
		if (guidingCareUMTechPassword == null)
			getProperty();
		return guidingCareUMTechPassword;
	}

	public static void setGuidingCareUMTechPassword(String guidingCareUMTechPassword) {
		PropertyFileReader.guidingCareUMTechPassword = guidingCareUMTechPassword;
	}

	public static String getBrokerPortalUrl() {
		if (brokerPortalUrl == null)
			getProperty();
		return brokerPortalUrl;
	}

	public static void setBrokerPortalUrl(String brokerPortalUrl) {
		PropertyFileReader.brokerPortalUrl = brokerPortalUrl;
	}

	public static String getBrokerPortalPassword() {
		if (brokerPortalPassword == null)
			getProperty();
		return brokerPortalPassword;
	}

	public static void setBrokerPortalPassword(String brokerPortalPassword) {
		PropertyFileReader.brokerPortalPassword = brokerPortalPassword;
	}

	public static String getClearstoneUrl() {
		if (clearstoneUrl == null)
			getProperty();
		return clearstoneUrl;
	}

	public static void setClearstoneUrl(String clearstoneUrl) {
		PropertyFileReader.clearstoneUrl = clearstoneUrl;
	}

	public static String getM360Url() {
		if (m360Url == null)
			getProperty();
		return m360Url;
	}

	public static void setM360Url(String m360Url) {
		PropertyFileReader.m360Url = m360Url;
	}

	public static String getM360UserId() {
		if (m360UserId == null)
			getProperty();
		return m360UserId;
	}

	public static void setM360UserId(String m360UserId) {
		PropertyFileReader.m360UserId = m360UserId;
	}

	public static String getm360Password() {
		if (m360Password == null)
			getProperty();
		return m360Password;
	}

	public static void setm360Password(String m360Password) {
		PropertyFileReader.m360Password = m360Password;
	}

	public static String getHrpUserId() {
		if (hrpUserId == null)
			getProperty();
		return hrpUserId;
	}

	public static void setHrpUserId(String hrpUserId) {
		PropertyFileReader.hrpUserId = hrpUserId;
	}

	public static String getHrpPassword() {
		if (hrpPassword == null)
			getProperty();
		return hrpPassword;
	}

	public static void setHrpPassword(String hrpPassword) {
		PropertyFileReader.hrpPassword = hrpPassword;
	}

	public static String getHealthRulesPayor() {
		if (healthRulesPayor == null)
			getProperty();
		return healthRulesPayor;
	}

	public static void setHealthRulesPayor(String healthRulesPayor) {
		PropertyFileReader.healthRulesPayor = healthRulesPayor;
	}

	public static String getpCS() {
		if (pCS == null)
			getProperty();
		return pCS;
	}

	public static void setpCS(String pCS) {
		PropertyFileReader.pCS = pCS;
	}

	public static String getProviderGoldenRecordUrl() {
		if (providerGoldenRecordUrl == null)
			getProperty();
		return providerGoldenRecordUrl;
	}

	public static void setProviderGoldenRecordUrl(String providerGoldenRecordUrl) {
		PropertyFileReader.providerGoldenRecordUrl = providerGoldenRecordUrl;
	}

	public static String getcRM() {
		if (cRM == null)
			getProperty();
		return cRM;
	}

	public static void setcRM(String cRM) {
		PropertyFileReader.cRM = cRM;
	}

	public static String getCallidusUrl() {
		if (callidusUrl == null)
			getProperty();
		return callidusUrl;
	}

	public static void setCallidusUrl(String callidusUrl) {
		PropertyFileReader.callidusUrl = callidusUrl;
	}

	public static String getCallidusUserId() {
		if (callidusUserId == null)
			getProperty();
		return callidusUserId;
	}

	public static void setCallidusUserId(String callidusUserId) {
		PropertyFileReader.callidusUserId = callidusUserId;
	}

	public static String getCallidusPassword() {
		if (callidusPassword == null)
			getProperty();
		return callidusPassword;
	}

	public static void setCallidusPassword(String callidusPassword) {
		PropertyFileReader.callidusPassword = callidusPassword;
	}

	public static String getGuidingCareUrl() {
		if (guidingCareUrl == null)
			getProperty();
		return guidingCareUrl;
	}

	public static void setGuidingCareUrl(String guidingCareUrl) {
		PropertyFileReader.guidingCareUrl = guidingCareUrl;
	}

	public static String getGuidingCareUserId() {
		if (guidingCareUserId == null)
			getProperty();
		return guidingCareUserId;
	}

	public static void setGuidingCareUserId(String guidingCareUserId) {
		PropertyFileReader.guidingCareUserId = guidingCareUserId;
	}

	public static String getGuidingCarePassword() {
		if (guidingCarePassword == null)
			getProperty();
		return guidingCarePassword;
	}

	public static void setGuidingCarePassword(String guidingCarePassword) {
		PropertyFileReader.guidingCarePassword = guidingCarePassword;
	}

	public static String getOracleFinancial() {
		if (oracleFinancial == null)
			getProperty();
		return oracleFinancial;
	}

	public static void setOracleFinancial(String oracleFinancial) {
		PropertyFileReader.oracleFinancial = oracleFinancial;
	}

	public static String getaZBlueMedicareUrl() {
		if (aZBlueMedicareUrl == null)
			getProperty();
		return aZBlueMedicareUrl;
	}

	public static void setaZBlueMedicareUrl(String aZBlueMedicareUrl) {
		PropertyFileReader.aZBlueMedicareUrl = aZBlueMedicareUrl;
	}

	public static String getHealthXMemberPortalUrl() {
		if (healthXMemberPortalUrl == null)
			getProperty();
		return healthXMemberPortalUrl;
	}

	public static void setHealthXMemberPortalUrl(String healthXMemberPortalUrl) {
		PropertyFileReader.healthXMemberPortalUrl = healthXMemberPortalUrl;
	}

	public static String getHealthXProviderPortalUrl() {
		if (healthXProviderPortalUrl == null)
			getProperty();
		return healthXProviderPortalUrl;
	}

	public static void setHealthXProviderPortalUrl(String healthXProviderPortalUrl) {
		PropertyFileReader.healthXProviderPortalUrl = healthXProviderPortalUrl;
	}

	public static String getAppTracker() {
		if (appTracker == null)
			getProperty();
		return appTracker;
	}

	public static void setAppTracker(String appTracker) {
		PropertyFileReader.appTracker = appTracker;
	}

	public static String gettPSCash() {
		if (tPSCash == null)
			getProperty();
		return tPSCash;
	}

	public static void settPSCash(String tPSCash) {
		PropertyFileReader.tPSCash = tPSCash;
	}

	public static String getDbUserId() {
		if (dbUserId == null)
			getProperty();
		return dbUserId;
	}

	public static void setDbUserId(String dbUserId) {
		PropertyFileReader.dbUserId = dbUserId;
	}

	public static String getDbPassword() {
		if (dbPassword == null)
			getProperty();
		return dbPassword;
	}

	public static void setDbPassword(String dbPassword) {
		PropertyFileReader.dbPassword = dbPassword;
	}

	public static String getHRPAppName() {
		if (hrpAppName == null)
			getProperty();
		return hrpAppName;
	}

	public static void setHRPAppName(String appName) {
		PropertyFileReader.hrpAppName = appName;
	}
	
	public static String getHealthXAdminPortalUrl() {
		if (healthXAdminPortalUrl == null)
			getProperty();
		return healthXAdminPortalUrl;
	}

	public static void setHealthXAdminPortalUrl(String healthXAdminPortalUrl) {
		PropertyFileReader.healthXAdminPortalUrl = healthXAdminPortalUrl;
	}

	public static String getHealthXAdminPortalUserId() {
		if (healthXAdminPortalUserId == null)
			getProperty();
		return healthXAdminPortalUserId;
	}

	public static void setHealthXAdminPortalUserId(String healthXAdminPortalUserId) {
		PropertyFileReader.healthXAdminPortalUserId = healthXAdminPortalUserId;
	}

	public static String getHealthXAdminPortalPassword() {
		if (healthXAdminPortalPassword == null)
			getProperty();
		return healthXAdminPortalPassword;
	}

	public static void setHealthXAdminPortalPassword(String healthXAdminPortalPassword) {
		PropertyFileReader.healthXAdminPortalPassword = healthXAdminPortalPassword;
	}
	
	public static String getHealthXProviderPortalUserId() {
		if (healthXProviderPortalUserId == null)
			getProperty();
		return healthXProviderPortalUserId;
	}

	public static void setHealthXProviderPortalUserId(String healthXProviderPortalUserId) {
		PropertyFileReader.healthXProviderPortalUserId = healthXProviderPortalUserId;
	}

	public static String getHealthXProviderPortalPassword() {
		if (healthXProviderPortalPassword == null)
			getProperty();
		return healthXProviderPortalPassword;
	}

	public static void setHealthXProviderPortalPassword(String healthXProviderPortalPassword) {
		PropertyFileReader.healthXProviderPortalPassword = healthXProviderPortalPassword;
	}

	public static String getHealthXMemberPortalUserId() {
		if (healthXMemberPortalUserId == null)
			getProperty();
		return healthXMemberPortalUserId;
	}

	public static void setHealthXMemberPortalUserId(String healthXMemberPortalUserId) {
		PropertyFileReader.healthXMemberPortalUserId = healthXMemberPortalUserId;
	}

	public static String getHealthXMemberPortalPassword() {
		if (healthXMemberPortalPassword == null)
			getProperty();
		return healthXMemberPortalPassword;
	}

	public static void setHealthXMemberPortalPassword(String healthXMemberPortalPassword) {
		PropertyFileReader.healthXMemberPortalPassword = healthXMemberPortalPassword;
	}

	public String[] getUserIDPwd(String appName) {

		String[] temp = new String[2];
		try {
			byte[] mapData = Files.readAllBytes(Paths.get(Const.ENVPROPERTIES));
			JsonNode rootNode = objectMapper.readTree(mapData);
			envNode = rootNode.path(Const.ENV);
			temp[0] = envNode.get(appName).get("userId").toString();
			temp[1] = envNode.get(appName).get("password").toString();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return temp;
	}

	@SuppressWarnings({ "unchecked" })
	public static void setPwdHashInJson(Map<String, String> tempEncryptedData) {
		objectMapper = new ObjectMapper();
		FileWriter file = null;
		byte[] mapData;
		try {
			mapData = Files.readAllBytes(Paths.get(Const.ENVPROPERTIES));
			myMap = objectMapper.readValue(mapData, HashMap.class);
			jsonRoot = new JSONObject(new String(mapData));
			jsonRoot.getJSONObject(tempEncryptedData.get("Environment")).getJSONObject(tempEncryptedData.get("AppName"))
					.put("password", tempEncryptedData.get("passwordHash"));

			Object newObject = objectMapper.readValue(jsonRoot.toString(), Object.class);
			String p = objectMapper.writerWithDefaultPrettyPrinter().writeValueAsString(newObject);
			file = new FileWriter(Const.ENVPROPERTIES);
			file.write(p);
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception r) {
			r.printStackTrace();
		} finally {
			try {
				if (file != null) {
					file.flush();
					file.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}

		}
	}

}
