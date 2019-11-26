package org.open.email.amazon;

import javax.mail.MessagingException;

import org.open.email.EmailObject;
import org.open.email.Sendable;

import com.amazonaws.regions.Regions;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailService;
import com.amazonaws.services.simpleemail.AmazonSimpleEmailServiceClientBuilder;
import com.amazonaws.services.simpleemail.model.Body;
import com.amazonaws.services.simpleemail.model.Content;
import com.amazonaws.services.simpleemail.model.Destination;
import com.amazonaws.services.simpleemail.model.Message;
import com.amazonaws.services.simpleemail.model.SendEmailRequest;

public class AmazonEmailObject extends EmailObject implements Sendable {

	protected static AmazonSimpleEmailService CLIENT;
	protected static AmazonProperties AMAZONPROPERTIES;
	
	protected String htmlBody;
	protected String textBody;
	protected String subject;
	protected String[] ccs;
	protected String[] bccs;
	
	private SendEmailRequest request;

	static {
		try {
			AMAZONPROPERTIES= AmazonProperties.getAmazonProperties();
			CLIENT= AmazonSimpleEmailServiceClientBuilder.standard()
					.withRegion(Regions.fromName(AMAZONPROPERTIES.getProperty(AmazonProperties.getAwsRegion()))).build();
		} catch (PropertyNotInitializedException e) {
			e.printStackTrace();
		}

	}

	public AmazonEmailObject (String from) {
		super(from);
		request = new SendEmailRequest();
	}

	@Override
	public String send(String... recipients) throws SendException {
		this.request.withDestination(new Destination().withToAddresses(recipients))
		.withMessage(new Message()
				.withBody(new Body()
						.withHtml(new Content()
								.withCharset("UTF-8").withData(htmlBody))
						.withText(new Content()
								.withCharset("UTF-8").withData(textBody)))
				.withSubject(new Content()
						.withCharset("UTF-8").withData(subject)))
		.withSource(this.from);
		
		if (ccs != null) {
			this.request.withDestination(new Destination().withCcAddresses(ccs));
		}
		
		if (bccs != null) {
			this.request.withDestination(new Destination().withBccAddresses(bccs));
		}
		
		return CLIENT.sendEmail(request).getMessageId();
	}

	public static AmazonSimpleEmailService getClient() {
		return CLIENT;
	}

	public String getHtmlBody() {
		return htmlBody;
	}

	public void setHtmlBody(String htmlBody) throws MessagingException {
		this.htmlBody = htmlBody;
	}

	public String getTextBody() {
		return textBody;
	}

	public void setTextBody(String textBody) throws MessagingException {
		this.textBody = textBody;
	}

	public String getSubject() {
		return subject;
	}

	public void setSubject(String subject) {
		this.subject = subject;
	}

	public static AmazonProperties getAMAZONPROPERTIES() {
		return AMAZONPROPERTIES;
	}

	public String[] getCcs() {
		return ccs;
	}

	public void setCcs(String[] ccs) {
		this.ccs = ccs;
	}

	public String[] getBccs() {
		return bccs;
	}

	public void setBccs(String[] bccs) {
		this.bccs = bccs;
	}

}
