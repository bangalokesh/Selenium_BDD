package x12.claims;

import java.util.Map;

import x12.enums.ClaimDataPoint;

import java.util.HashMap;
import java.util.List;

public class ClaimsDataStructure {

	private String ISAControlNumber;
	private String testID;
	private Map<String, String> staticData;
	private Map<String, String> billingProviderData;
	private Map<String, String> memberData;
	private Map<String, String> claimData;
	private List<HashMap<String, String>> claimLineData;
	private Map<String, String> refProvider;
	private Map<String, String> renProvider;
	private Map<String, String> attendingProvider;
	
	private Map<ClaimDataPoint, Map<String, String>> loop2000A;
	private Map<ClaimDataPoint, Map<String, String>> loop2000B;
	private Map<ClaimDataPoint, Map<String, String>> loop2300;
	
	public String getISAControlNumber() {
		return ISAControlNumber;
	}
	public void setISAControlNumber(String iSAControlNumber) {
		ISAControlNumber = iSAControlNumber;
	}
	public String getTestID() {
		return testID;
	}
	public void setTestID(String testID) {
		this.testID = testID;
	}
	public Map<String, String> getBillingProviderData() {
		return billingProviderData;
	}
	public void setStaticData(Map<String, String> staticData) {
		this.staticData = staticData;
	}
	public Map<String, String> getStaticData(){
		return this.staticData;
	}
	public void setBillingProviderData(Map<String, String> billingProviderData) {
		this.billingProviderData = billingProviderData;
	}
	public Map<String, String> getMemberData() {
		return memberData;
	}
	public void setMemberData(Map<String, String> memberData) {
		this.memberData = memberData;
	}
	public Map<String, String> getClaimData() {
		return claimData;
	}
	public void setClaimData(Map<String, String> claimData) {
		this.claimData = claimData;
	}
	public List<HashMap<String, String>> getClaimLineData() {
		return claimLineData;
	}
	public void setClaimLineData(List<HashMap<String, String>> claimLineData) {
		this.claimLineData = claimLineData;
	}
	public Map<String, String> getRefProvider() {
		return refProvider;
	}
	public void setRefProvider(Map<String, String> refProvider) {
		this.refProvider = refProvider;
	}
	public Map<String, String> getRenProvider() {
		return renProvider;
	}
	public void setRenProvider(Map<String, String> renProvider) {
		this.renProvider = renProvider;
	}
	
	public void setAttendingProvider(HashMap<String, String> map) {
		this.attendingProvider = map;
	}
	
	public Map<String, String> getAttendingProvider(){
		return this.attendingProvider;
	}
	
	public Map<ClaimDataPoint, Map<String, String>> getLoop2000A(){
		loop2000A = new HashMap<ClaimDataPoint, Map<String, String>>();
		loop2000A.put(ClaimDataPoint.StaticData, this.staticData);
		loop2000A.put(ClaimDataPoint.BillingProviderData, this.billingProviderData);
		return loop2000A;
	}
	
	public Map<ClaimDataPoint, Map<String, String>> getLoop2000B(){
		loop2000B = new HashMap<ClaimDataPoint, Map<String, String>>();
		loop2000B.put(ClaimDataPoint.StaticData, this.staticData);
		loop2000B.put(ClaimDataPoint.MemberData, this.memberData);
		return loop2000B;
	}
	
	public Map<ClaimDataPoint, Map<String, String>> getLoop2300(){
		loop2300 = new HashMap<ClaimDataPoint, Map<String, String>>();
		loop2300.put(ClaimDataPoint.StaticData, this.staticData);
		loop2300.put(ClaimDataPoint.ClaimData, this.claimData);
		loop2300.put(ClaimDataPoint.RefProvider, this.refProvider);
		loop2300.put(ClaimDataPoint.RenProvider, this.renProvider);
		loop2300.put(ClaimDataPoint.AttendingProvider, this.attendingProvider);
		return loop2300;
	}

}
