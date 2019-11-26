package x12.enums;

public enum ClaimLoops {

	Loop1000A("1000A"), Loop1000B("1000B");
	
	public final String name;
	
	ClaimLoops(String name){
		this.name = name;
	}
}
