package x12.enums;

public enum ClaimDataPoint {
	
	StaticData("StaticData"),
	BillingProviderData("BillingProviderData"),
	MemberData("MemberData"),
	ClaimData("ClaimData"),
	RefProvider("RefProvider"),
	RenProvider("RenProvider"),
	AttendingProvider("AttendingProvider");
	
	public final String value;
	
	ClaimDataPoint(String value) {
		this.value = value;
	}
	
}
