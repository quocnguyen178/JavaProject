package springmvc_example.config;

import java.io.IOException;
import java.util.Properties;

import org.apache.log4j.Logger;


public class XeConfig {
	private static Logger logger = Logger.getLogger(XeConfig.class);
	private String content;
	private String request;
	private Properties loadConfiguration(){
		Properties props = new Properties();
		try {
			props.load(XeConfig.class.getClassLoader().getResourceAsStream("requestbody.properties"));
		} catch (IOException e) {
			logger.error("Error occurs when attempting to load the resource file.", e);
		}
		
		return props;
		
	}
	public XeConfig() {
		Properties props = loadConfiguration();
		setContent(props.getProperty("xeserver"));
		setRequest(props.getProperty("request"));
	}
	public static Logger getLogger() {
		return logger;
	}
	public static void setLogger(Logger logger) {
		XeConfig.logger = logger;
	}
	public String getContent() {
		return content;
	}
	public void setContent(String content) {
		this.content = content;
	}
	public String getRequest() {
		return request;
	}
	public void setRequest(String request) {
		this.request = request;
	}
	
	
	
}
