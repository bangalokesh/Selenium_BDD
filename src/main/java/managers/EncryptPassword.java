package managers;

import pageclasses.BasePage;

public class EncryptPassword {

	@SuppressWarnings("static-access")
	public static void main(String[] args) {
		BasePage bp = new BasePage();
		bp.encrypt("", "HRP");
		System.out.println(bp.decodeEncryptedString("HRP"));
	}
}
