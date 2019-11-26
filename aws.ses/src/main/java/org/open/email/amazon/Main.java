package org.open.email.amazon;

import java.io.FileNotFoundException;
import java.io.IOException;

import javax.mail.MessagingException;

public class Main {

	public static void main (String[] args) throws FileNotFoundException, IOException, PropertyNotInitializedException, SendException, MessagingException, AttachmentException {
		AmazonProperties.initializeProperties("/home/pranav/Documents/Work Info/GITHUB/Java/aws.ses/src/main/resources/amazon.properties");

		/*
		 * without attachment
		 */
		AmazonEmailObject a= new AmazonEmailObject("pranavsrinivasdutta@gmail.com");
		a.setHtmlBody("<b>This is the BOLD TEXT</b>");
		a.setSubject("This is an unwanted subject");
		a.setTextBody("We'll see where the text comes.");
		a.setCcs(new String[] {"pranavsrinivasdutta@gmail.com"});
		a.setBccs(new String[] {"pranavsrinivasdutta@gmail.com"});
		System.out.println(a.send("topranavsai@gmail.com", "shadowed.repository@gmail.com"));
		
		/*
		 * with attachment
		 */
		AmazonRawEmailObject z= new AmazonRawEmailObject("shadowed.repository@gmail.com");
		z.setHtmlBody("<b>This Email is without Attachment</b>");
		z.setSubject("This is an Attachment Subject.");
		z.setTextBody("This is the text body");
		z.attachFile("/home/pranav/Pictures/goku.jpg");
		z.setCcs(new String[] {"pranavsrinivasdutta@gmail.com"});
		z.setBccs(new String[] {"pranavsrinivasdutta@gmail.com"});
		
		System.out.println(z.send("pranavsrinivasdutta@gmail.com", "topranavsai@gmail.com"));

	}

}
