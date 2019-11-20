package org.open.nosql.dynamodb;

import java.io.FileInputStream;
import java.util.Properties;

@SuppressWarnings("serial")
public class DynamoProperties extends Properties {
	
	private static DynamoProperties properties;
	
	private DynamoProperties (String propertiesPath) throws Exception {
		this.load(new FileInputStream(propertiesPath));
	}
	
	public static synchronized DynamoProperties initializeDynamoProperties(String propertiesPath) throws Exception {
		if (properties == null) {
			properties= new DynamoProperties(propertiesPath);
		}
		return properties;
	}

}
