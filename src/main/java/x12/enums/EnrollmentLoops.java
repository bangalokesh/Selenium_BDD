package x12.enums;

public enum EnrollmentLoops {

	Loop1000A("1000A"), Loop1000B("1000B"), Loop1000C("1000C"),
	Loop2100A("2100A"), Loop2100C("2100C"),
	Loop2300("2300"), Loop2310("2310"), Loop2320("2320");
	
	public final String name;
	
	EnrollmentLoops(String name){
		this.name = name;
	}
}
