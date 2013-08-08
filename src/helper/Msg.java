package helper;

public class Msg {
	public static void msg(Class<?> c, String msg) {
		if (CNST.OK_MSG) {
			System.out.printf("[%-20s]  %s\n", c, msg);
		}
	}
	
	public static void mmsg(String msg){
		System.out.println(msg);
	}
	
	public static void msep(int numberOfColumns, char chr){
		for (int i = 0; i < 21 * numberOfColumns; i++) {
			System.out.print(chr);
		}
		System.out.println();
	}

	public static void sep() {
		if (CNST.OK_MSG)
			System.out.println("*********************************************");
	}
}
