package helper.db;

import helper.Msg;

import java.util.ArrayList;
import java.util.Scanner;
/**
 * 这个类对SQL代码段做相应的分析
 * @author Administrator
 *
 */
public final class SQL {
	private SQL(){
		
	}

	public static String produceMius(String userInput, String answer) {
		userInput = userInput.replaceAll(";", "");
		String selectList = SQL.getSelectList(answer);
		Msg.debugMsg(SQL.class, "User Input sql is: " + userInput);
		Msg.debugMsg(SQL.class, "select list is: " + selectList);
		String newSQL = null;
		if (userInput.toLowerCase().contains("where")) {
			// this is when user input is "select * from * where *"
			newSQL = answer + " and " + selectList + " not in (" + userInput + ");";

		} else {
			// this is when user input is "select * from *"
			newSQL = answer + " where " + selectList + " not in (" + userInput + ");";
		}

		Msg.debugMsg(SQL.class, "Set Minus sql is: " + newSQL);
		return newSQL;
	}

	public static String getSelectList(String SQL) {
		// 这个变量如果是全局变量，就会影响到第一个JUnit测试后面的测试
		// 这就是为什么JUnit测试单独测试都可以
		// 但是一放到一起就不行了
		boolean start = false;
		Scanner sc = new Scanner(SQL);
		ArrayList<String> selectList = new ArrayList<String>();
		String str = null;
		while (sc.hasNext()) {
			str = sc.next().replaceAll(",$", "").trim();
			//Msg.debugMsg(SQL.class, "str=" + str + "]");
			if (!start) {
				if (str.equals("select")) {
					start = true;
				}
				continue;
			}
			if (str.equals("from")) {
				break;
			}
			//Msg.debugMsg(SQL.class, "add[" + str + "]");
			selectList.add(str);

		}
		sc.close();
		String result = selectList.toString().replace('[', '(').replace(']', ')');
		return result;
	}
}
