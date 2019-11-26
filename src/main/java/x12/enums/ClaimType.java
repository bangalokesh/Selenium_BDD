package x12.enums;

public enum ClaimType {

	Professional("1", "P", "Professional_Batch_Claims_"),
	Institutional("2", "I", "Institutional_Batch_Claims_");
	
	public final String codeInt, codeString, filename;
	
	ClaimType(String code1, String code2, String filename) {
		this.codeInt = code1;
		this.codeString = code2;
		this.filename = filename;
	}
}
