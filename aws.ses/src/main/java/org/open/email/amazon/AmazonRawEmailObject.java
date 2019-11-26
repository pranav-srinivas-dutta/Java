package org.open.email.amazon;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import org.open.email.Attachable;
import org.open.email.Sendable;

import com.amazonaws.services.simpleemail.model.RawMessage;
import com.amazonaws.services.simpleemail.model.SendRawEmailRequest;

public class AmazonRawEmailObject extends AmazonEmailObject implements Sendable, Attachable {
	
	private Session session;
	private MimeMessage message;
	private MimeMultipart messageBody;
	private MimeMultipart attachmentBody;
	private MimeBodyPart attachment;
	private boolean attachmentPresent;

	public AmazonRawEmailObject(String from) throws AddressException, MessagingException {
		super(from);
		session= Session.getDefaultInstance(new Properties());
		message= new MimeMessage(session);
		message.setFrom(new InternetAddress(from));
		messageBody= new MimeMultipart("alternative");
		
		attachmentBody= new MimeMultipart("mixed");
		attachment= new MimeBodyPart();
		attachmentPresent= false;
	}
	
	@Override
	public void setSubject(String subject) {
		super.setSubject(subject);
		if (subject == null)
			subject= "";
		
		try {
			System.out.println("Setting subject");
			this.message.setSubject(subject, "UTF-8");
			System.out.println("Set subject");
		} catch (MessagingException e) {
			e.printStackTrace();
		}
	}

	@Override
	public String send(String... recipients) throws SendException {
		String recivers= this.convertArrayToCSV(recipients);
		String ccs= this.convertArrayToCSV(this.ccs);
		String bccs= this.convertArrayToCSV(this.bccs);
		
		String messageId= "";
		
		try {
			
			this.message.setRecipients(Message.RecipientType.TO, InternetAddress.parse(recivers.toString()));
			
			if (this.ccs != null) {
				this.message.setRecipients(Message.RecipientType.CC, InternetAddress.parse(ccs));
			}
			
			if (this.bccs != null) {
				this.message.setRecipients(Message.RecipientType.BCC, InternetAddress.parse(bccs));
			}
			
			MimeBodyPart wrap = new MimeBodyPart();
			wrap.setContent(messageBody);
			message.setContent(attachmentBody);
			
			if (attachmentPresent) {
				attachmentBody.addBodyPart(wrap);
				attachmentBody.addBodyPart(attachment);
			}
		} catch (MessagingException e) {
			e.printStackTrace();
			throw new SendException("Error Sending Email.");
		}
		
		if (attachmentPresent) {			
			ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
			try {
				message.writeTo(outputStream);
			} catch (IOException | MessagingException e) {
				e.printStackTrace();
				throw new SendException("Error Sending Email.");
			}
			RawMessage rawMessage = 
					new RawMessage(ByteBuffer.wrap(outputStream.toByteArray()));

			SendRawEmailRequest rawEmailRequest= new SendRawEmailRequest(rawMessage);
			messageId= CLIENT.sendRawEmail(rawEmailRequest).getMessageId();
		} else {
			messageId= super.send(recipients);
		}
		
		
		return messageId;
	}
	
	private String convertArrayToCSV(String[] strArr) {
		StringBuilder recivers= new StringBuilder("");
		for (int i= 0; i < strArr.length; i++) {
			recivers.append(strArr[i]);
			if (i != (strArr.length - 1))
				recivers.append(",");
		}
		return recivers.toString();
	}

	@Override
	public void attachFile(String path) throws AttachmentException {
        DataSource fds = new FileDataSource(path);
        try {
			attachment.setDataHandler(new DataHandler(fds));
			attachment.setFileName(fds.getName());
			attachmentPresent= true;
		} catch (MessagingException e) {
			e.printStackTrace();
			throw new AttachmentException("Unable to attach File.");
		}
		
	}
	
	@Override
	public void setTextBody(String textBody) throws MessagingException {
		super.setTextBody(textBody);
		MimeBodyPart textPart = new MimeBodyPart();
        textPart.setContent(textBody, "text/plain; charset=UTF-8");
        
        this.messageBody.addBodyPart(textPart);
	}
	
	@Override
	public void setHtmlBody(String htmlBody) throws MessagingException {
		super.setHtmlBody(htmlBody);
		
		MimeBodyPart htmlPart = new MimeBodyPart();
        htmlPart.setContent(htmlBody,"text/html; charset=UTF-8");
        
        this.messageBody.addBodyPart(htmlPart);
		
	}

	public MimeMultipart getMessageBody() {
		return messageBody;
	}

	public boolean isAttachmentPresent() {
		return attachmentPresent;
	}

	public void setAttachmentPresent(boolean attachmentPresent) {
		this.attachmentPresent = attachmentPresent;
	}

}
