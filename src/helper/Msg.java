package helper;

public class Msg {
	public static void debugMsg(Class<?> c, String msg) {
		if (CNST.DEBUG_MSG) {
			System.out.printf("DEBUG: [%-20s]  %s\n", c, msg);
		}
	}

	public static void debugSep() {
		if (CNST.DEBUG_MSG)
			System.out.println("DEBUG: *********************************************");
	}

	public static void userMsgLn(String msg) {
		if (CNST.USER_MSG) {
			System.out.println(msg);
		}
	}

	public static void userMsgF(String format, Object... args) {
		if (CNST.USER_MSG) {
			System.out.printf(format, args);
		}
	}

	public static void userSep(int numberOfColumns, char chr) {
		if (CNST.USER_MSG)
			for (int i = 0; i < 21 * numberOfColumns; i++) {
				System.out.print(chr);
			}
		System.out.println();
	}

}
