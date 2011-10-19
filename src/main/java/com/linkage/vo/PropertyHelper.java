package com.linkage.vo;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * @author nvadugu
 *
 */
public class PropertyHelper {
	
	static Logger logger;
	static{
		logger = LoggerFactory.getLogger(PropertyHelper.class);
	}
	
	
	public static Properties getProperites() {		
		Properties props = new Properties();        
        try {
        	InputStream inputStream = PropertyHelper.class.getResourceAsStream("/graphdb.properties");
			props.load(inputStream);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			logger.error("Error loading properties");
			e.printStackTrace();
		}
		
        return props;
	}
	
	public static String getPropertyValue(String key) {		
		Properties props = getProperites();	
        return props.getProperty(key);
	}

}
