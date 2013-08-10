package helper;

import helper.db.framework.BONECP;
import helper.db.framework.DBCP;
import helper.db.framework.DB_Framwork;
import helper.db.framework.DB_Regular;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class CNST {
	private static final Properties prop;

	//数据库相关字段
	public static final String DRIVER;
	public static final String DB_URL;
	public static final String USER;
	public static final String PASS;

	//用来控制Msg类-->从而控制在运行程序时显示何种信息
	public static final Boolean DEBUG_MSG;
	public static final Boolean USER_MSG;

	public static Charset CHAR_ENCODING;

	//导出标准输入输出流的地址
	//public static final String STDOUT;
	//public static final String STDERR;

	//为了Debug方便，控制JUnit时间间隔
	public static final int INTERVAL_JUNIT;
	//关闭数据库的线程等待多长时间关闭数据库连接
	public static final int INTERVAL_DBSHUTDOWN;

	public static final DB_Framwork dbf;

	static {
		prop = new Properties();
		FileInputStream fis;
		try {
			File ppxml = FileIO.findSiblingResource(CNST.class, "properties.xml");
			fis = new FileInputStream(ppxml);
			prop.loadFromXML(fis);
		} catch (Exception e) {
			e.printStackTrace();
		}
		//initPropertyFile();
		//initDebug();

		if (Boolean.parseBoolean(prop.getProperty("REDIRECT"))) {
			FileIO.redirect(prop.getProperty("STDOUT"), prop.getProperty("STDERR"));
		}

		INTERVAL_JUNIT = Integer.parseInt(prop.getProperty("INTERVAL_JUNIT"));
		INTERVAL_DBSHUTDOWN = Integer.parseInt(prop.getProperty("INTERVAL_DBSHUTDOWN"));
		DEBUG_MSG = Boolean.parseBoolean(prop.getProperty("DEBUG_MSG"));
		USER_MSG = Boolean.parseBoolean(prop.getProperty("USER_MSG"));

		switch (prop.getProperty("CHAR_ENCODING")) {
		case "UTF-8":
			CHAR_ENCODING = StandardCharsets.UTF_8;
		}

		String dbNow = prop.getProperty("DB_NOW");
		DRIVER = prop.getProperty(dbNow + "_DRIVER");
		DB_URL = prop.getProperty(dbNow + "_URL");
		USER = prop.getProperty(dbNow + "_USER");
		PASS = prop.getProperty(dbNow + "_PASS");

		switch (prop.getProperty("DB_FRAMEWORK")) {
		case "DB_REGULAR":
			dbf = DB_Regular.INST;
			break;
		case "BONECP":
			dbf = BONECP.INST;
			break;
		case "DBCP":
			dbf = DBCP.INST;
			break;
		default:
			dbf = null;
			break;
		}
	}

	public static String getSQL(String sqlFile) {
		return prop.getProperty(sqlFile);
	}

}
