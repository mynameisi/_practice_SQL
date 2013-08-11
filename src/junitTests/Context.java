package junitTests;

import helper.db.framework.BONECP;
import helper.db.framework.DBCP;
import helper.db.framework.DBFrameWork;
import helper.db.framework.DB_REGULAR;
import helper.io.FileIO;

import java.io.File;
import java.io.FileInputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class Context {
	private static final Properties prop;

	//用来控制Msg类-->从而控制在运行程序时显示何种信息
	public static final Boolean DEBUG_MSG;
	public static final Boolean USER_MSG;

	//导出标准输入输出流的地址
	//public static final String STDOUT;
	//public static final String STDERR;

	//为了Debug方便，控制JUnit时间间隔
	public static final int INTERVAL_JUNIT;
	//关闭数据库的线程等待多长时间关闭数据库连接
	public static final int INTERVAL_DBSHUTDOWN;

	//use this interface approach
	//1. it shields the user from doing anything to the database
	//2. it enables this resource to be globally available
	private static final DBFrameWork myDB;

	public static DBFrameWork getMyDB() {
		return myDB;
	}

	private static final Logger logger = LoggerFactory.getLogger(Context.class);
	static {
		prop = new Properties();
		FileInputStream fis;
		try {
			File ppxml = FileIO.findRootResource(Context.class, "properties.xml");
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

		/*
		 * The following codes sets up the pool
		 */
		String dbNow = prop.getProperty("DB_NOW");
		String DRIVER = prop.getProperty(dbNow + "_DRIVER");
		String URL = prop.getProperty(dbNow + "_URL");
		String USER = prop.getProperty(dbNow + "_USER");
		String PASS = prop.getProperty(dbNow + "_PASS");

		
		String f=prop.getProperty("DB_FRAMEWORK");
		logger.info("STARTING DB Framwork ["+f+"]");
		switch (f) {
		case "DB_REGULAR":
			myDB = new DB_REGULAR(DRIVER, URL, USER, PASS);
			break;
		case "BONECP":
			myDB = new BONECP(DRIVER, URL, USER, PASS);
			break;
		case "DBCP":
			myDB = new DBCP(DRIVER, URL, USER, PASS);
			break;
		default:
			myDB = null;
			break;
		}
		if(dbNow.equals("HSQLDB_IN_MEM")){
			myDB.batchUpdate(FileIO.findRootResource(Context.class, "createDB.sql"));;
		}
		logger.info("DB Framwork ["+f+"] started");
	}

	//	public static String getSQL(String sqlFile) {
	//		return prop.getProperty(sqlFile);
	//	}

}
