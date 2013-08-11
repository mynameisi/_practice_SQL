//package EarlierVersionPractices;
//
//import helper.FileIO;
//
//import java.io.File;
//import java.io.FileInputStream;
//import java.nio.charset.Charset;
//import java.nio.charset.StandardCharsets;
//import java.util.Properties;
//
//public class CNST_static_Field_Version {
//
//	private static Properties prop = null;
//
//	//数据库相关字段
//	public static String DRIVER = null;
//	public static String DB_URL = null;
//	public static String USER = null;
//	public static String PASS = null;
//	
//	//用来控制Msg类-->从而控制在运行程序时显示何种信息
//	public static Boolean DEBUG_MSG = null;
//	public static Boolean USER_MSG = null;
//	
//	public static Charset CHAR_ENCODING = null;
//	
//	//导出标准输入输出流的地址
//	public static String STDOUT=null;
//	public static String STDERR=null;
//	
//	//为了Debug方便，控制JUnit时间间隔
//	public static int INTERVAL_JUNIT=0;
//	//关闭数据库的线程等待多长时间关闭数据库连接
//	public static int INTERVAL_DBSHUTDOWN=0;
//
//	static {
//		if (prop == null) {
//			prop = new Properties();
//			FileInputStream fis;
//			try {
//				File ppxml=FileIO.findSiblingResource(CNST_static_Field_Version.class, "properties.xml");
//				fis = new FileInputStream(ppxml);
//				prop.loadFromXML(fis);
//			} catch (Exception e) {
//				e.printStackTrace();
//			}
//		}
//		if(Boolean.parseBoolean(prop.getProperty("REDIRECT"))){
//			STDOUT = prop.getProperty("STDOUT");
//			STDERR = prop.getProperty("STDERR");
//			FileIO.redirect(STDOUT,STDERR);
//		}
//		
//		INTERVAL_JUNIT = Integer.parseInt(prop.getProperty("INTERVAL_JUNIT"));
//		INTERVAL_DBSHUTDOWN= Integer.parseInt(prop.getProperty("INTERVAL_DBSHUTDOWN"));
//		
//		DEBUG_MSG = DEBUG_MSG != null ? DEBUG_MSG : Boolean.parseBoolean(prop.getProperty("DEBUG_MSG"));
//		USER_MSG = USER_MSG != null ? USER_MSG : Boolean.parseBoolean(prop.getProperty("USER_MSG"));
//		switch (prop.getProperty("CHAR_ENCODING")) {
//		case "UTF-8":
//			CHAR_ENCODING = StandardCharsets.UTF_8;
//		}
//	
//		String dbNow=prop.getProperty("DB_NOW");
//		DRIVER = (DRIVER != null ? DRIVER : prop.getProperty(dbNow+"_DRIVER"));
//		DB_URL = (DB_URL != null ? DB_URL : prop.getProperty(dbNow+"_URL"));
//		USER = (USER != null ? USER : prop.getProperty(dbNow+"_USER"));
//		PASS = (PASS != null ? PASS : prop.getProperty(dbNow+"_PASS"));
//
//	}
//
//	public static String getSQL(String sqlFile) {
//		return prop.getProperty(sqlFile);
//	}
//
//}
