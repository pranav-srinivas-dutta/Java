package org.open.email.amazon;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Base64;
import java.util.Properties;

import com.sun.mail.iap.ByteArray;

@SuppressWarnings("serial")
public class AmazonProperties extends Properties {
	private static AmazonProperties amazonProperties;
	
	private static final String AWS_ACCESS_KEY= "aws.accessKeyId";
	private static final String AWS_SECRET_KEY= "aws.secretKey";
	private static final String AWS_REGION= "aws.region";

	private AmazonProperties(String propertiesPath) throws FileNotFoundException, IOException {
		this.load(new FileInputStream(propertiesPath));
		String accessKey= (String) this.get(AWS_ACCESS_KEY);
		String secretKey= (String) this.get(AWS_SECRET_KEY);
		
		System.setProperty(AWS_ACCESS_KEY, new String (Base64.getDecoder().decode(accessKey.getBytes())));
		System.setProperty(AWS_SECRET_KEY, new String (Base64.getDecoder().decode(secretKey.getBytes())));
	}
	
	public static synchronized void initializeProperties(String propertiesPath) throws FileNotFoundException, IOException {
		if (amazonProperties == null)
			amazonProperties= new AmazonProperties(propertiesPath);
	}

	public static AmazonProperties getAmazonProperties() throws PropertyNotInitializedException {
		if (amazonProperties == null)
			throw new PropertyNotInitializedException("Properties not Initialized.");
		return amazonProperties;
	}
	
	public static String getAwsAccessKey() {
		return AWS_ACCESS_KEY;
	}

	public static String getAwsSecretKey() {
		return AWS_SECRET_KEY;
	}

	public static String getAwsRegion() {
		return AWS_REGION;
	}

	public static void main(String [] args) {
		byte[] accessKey= Base64.getEncoder().encode("".getBytes());
		System.out.println(new String(accessKey));
		
	}
}
