package helper;

import java.util.ArrayList;
import java.util.Scanner;

public class SQL {

	public static String produceMius(String yourAnswer, String answer) {
		yourAnswer = yourAnswer.replaceAll(";", "");
		String selectList = SQL.getSelectList(answer);
		Msg.msg(SQL.class, "Your sql is: " + yourAnswer);
		Msg.msg(SQL.class, "select list is: " + selectList);
		String newSQL = answer + "and " + selectList + " not in (" + yourAnswer
				+ ");";
		return newSQL;
	}

	public static String getSelectList(String SQL) {
		//这个变量如果是全局变量，就会影响到第一个JUnit测试后面的测试
		//这就是为什么JUnit测试单独测试都可以
		//但是一放到一起就不行了
		boolean start = false;
		Scanner sc = new Scanner(SQL);
		ArrayList<String> selectList = new ArrayList<String>();
		String str = null;
		while (sc.hasNext()) {
			str = sc.next().replaceAll(",$", "").trim();
			Msg.msg(SQL.class, "str=" + str + "]");
			if (!start) {
				if (str.equals("select")) {
					start = true;
				}
				continue;
			}
			if (str.equals("from")) {
				break;
			}
			Msg.msg(SQL.class, "add[" + str + "]");
			selectList.add(str);

		}
		sc.close();
		String result = selectList.toString().replace('[', '(')
				.replace(']', ')');
		return result;
	}
}
