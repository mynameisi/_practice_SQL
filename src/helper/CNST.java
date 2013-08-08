package helper;

import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public class CNST {

	private static Properties prop = null;

	public static String DRIVER = null;
	public static String DB_URL = null;
	public static String USER = null;
	public static String PASS = null;
	
	public static Boolean DEBUG_MSG = null;
	public static Boolean USER_MSG = null;
	
	public static Charset CHAR_ENCODING = null;

	static {
		if (prop == null) {
			prop = new Properties();
			FileInputStream fis;
			try {
				fis = new FileInputStream(CNST.class.getResource("properties.xml").getFile());
				prop.loadFromXML(fis);
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		DEBUG_MSG = DEBUG_MSG != null ? DEBUG_MSG : Boolean.parseBoolean(prop.getProperty("DEBUG_MSG"));
		USER_MSG = USER_MSG != null ? USER_MSG : Boolean.parseBoolean(prop.getProperty("USER_MSG"));
		switch (prop.getProperty("CHAR_ENCODING")) {
		case "UTF-8":
			CHAR_ENCODING = StandardCharsets.UTF_8;
		}
		// the following switching on String in a Java 7 new feature
		switch (prop.getProperty("DB_NOW")) {
		case "MySQL":
			DRIVER = (DRIVER != null ? DRIVER : prop.getProperty("MYSQL_DRIVER"));
			DB_URL = (DB_URL != null ? DB_URL : prop.getProperty("MYSQL_URL"));
			USER = (USER != null ? USER : prop.getProperty("MYSQL_USER"));
			PASS = (PASS != null ? PASS : prop.getProperty("MYSQL_PASS"));
			break;
		case "Oracle":
			break;
		case "PostgreSQL":
			break;
		}

	}

	public static String getSQL(String sqlFile) {
		return prop.getProperty(sqlFile);
	}

}
