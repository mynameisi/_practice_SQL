package helper;

import java.io.File;
import java.io.FileInputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Properties;

public enum CNST {
	INST;
	private Properties prop = null;

	//数据库相关字段
	public String DRIVER = null;
	public String DB_URL = null;
	public String USER = null;
	public String PASS = null;

	//用来控制Msg类-->从而控制在运行程序时显示何种信息
	public Boolean DEBUG_MSG = null;
	public Boolean USER_MSG = null;

	public Charset CHAR_ENCODING = null;

	//导出标准输入输出流的地址
	public String STDOUT = null;
	public String STDERR = null;

	//为了Debug方便，控制JUnit时间间隔
	public int INTERVAL_JUNIT = 0;
	//关闭数据库的线程等待多长时间关闭数据库连接
	public int INTERVAL_DBSHUTDOWN = 0;

	CNST() {
		prop = new Properties();
		FileInputStream fis;
		try {
			File ppxml = FileIO.findSiblingResource(CNST.class, "properties.xml");
			fis = new FileInputStream(ppxml);
			prop.loadFromXML(fis);
		} catch (Exception e) {
			e.printStackTrace();
		}

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

	}

	public String getSQL(String sqlFile) {
		return prop.getProperty(sqlFile);
	}

}
