package br.ucs.lasis.core.mail;

import java.io.File;
import java.util.List;
import java.util.Properties;

import javax.activation.DataHandler;
import javax.activation.DataSource;
import javax.activation.FileDataSource;
import javax.mail.BodyPart;
import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Multipart;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeBodyPart;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeMultipart;

import br.ucs.lasis.core.enums.MailTypeEnum;

public class SendMail {

	private String host;
	private String port; 
	private String user;
	private String password;

	public String getHost() {
		return host;
	}

	public void setHost(String host) {
		this.host = host;
	}

	public String getPort() {
		return port;
	}

	public void setPort(String port) {
		this.port = port;
	}

	public String getUser() {
		return user;
	}

	public void setUser(String user) {
		this.user = user;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public SendMail(String host, String port, String user, String password) {
		this.host = host;
		this.port = port;
		this.user = user;
		this.password = password;
	}

	public SendMail(String host, String port) {
		
		this.host = host;
		this.port = port;
	}

	private void sendMessage(MailTypeEnum type, String from, String to, String subject, String textOrHTMLMessage, List<String> attachments) throws AddressException, MessagingException {

		// Setup mail server
		Properties properties = new Properties();
		
		if (this.host != null) {
			properties.setProperty("mail.smtp.host", this.host);
		}
		
		if (this.port != null) {
			properties.setProperty("mail.smtp.port", this.port);
		}
		
		if (this.user != null) {
			properties.setProperty("mail.smtp.user", this.user);
		}
		
		if (this.password != null) {
			properties.setProperty("mail.smtp.password", this.password);
		}

		// Get the default Session object.
		Session session = Session.getDefaultInstance(properties);
		MimeMessage message = new MimeMessage(session);

		// Set From: header field of the header.
		message.setFrom(new InternetAddress(from));

		// Set To: header field of the header.
		message.addRecipient(Message.RecipientType.TO, new InternetAddress(to));

		// Set Subject: header field
		message.setSubject(subject);

		if (attachments != null) {

			// Create a multipart message
			Multipart multipart = new MimeMultipart();

			BodyPart messageBodyPartText = new MimeBodyPart();

			if (MailTypeEnum.TEXT.equals(type)) {
				messageBodyPartText.setText(textOrHTMLMessage);
			} else {
				messageBodyPartText.setContent(textOrHTMLMessage, "text/html");
			}

			multipart.addBodyPart(messageBodyPartText);

			// Attachment
			for (String attach : attachments) {

				// Create the message part
				BodyPart messageBodyPart = new MimeBodyPart();

				System.out.println(attach);
				
				DataSource source = new FileDataSource(attach);
				messageBodyPart.setDataHandler(new DataHandler(source));
				
				String fileName = attach.substring(attach.lastIndexOf(File.separator) + 1);
				
				messageBodyPart.setFileName(fileName);
				
				
				multipart.addBodyPart(messageBodyPart);

			}

			message.setContent(multipart);
		} else {

			if (MailTypeEnum.TEXT.equals(type)) {
				// Now set the actual message
				message.setText(textOrHTMLMessage);
			} else {
				// Send the actual HTML message, as big as you like
				message.setContent(textOrHTMLMessage, "text/html");
			}
		}

		
		// Send message
		Transport.send(message);

	}

	public void sendTextMessage(String from, String to, String subject, String textMessage) throws AddressException, MessagingException {
		this.sendMessage(MailTypeEnum.TEXT, from, to, subject, textMessage, null);
	}

	public void sendTextMessage(String from, String to, String subject, String textMessage, List<String> attachments) throws AddressException, MessagingException {
		this.sendMessage(MailTypeEnum.TEXT, from, to, subject, textMessage, attachments);
	}

	public void sendHTMLMessage(String from, String to, String subject, String htmlMessage) throws AddressException, MessagingException {
		this.sendMessage(MailTypeEnum.HTML, from, to, subject, htmlMessage, null);
	}

	public void sendHTMLMessage(String from, String to, String subject, String htmlMessage, List<String> attachments) throws AddressException, MessagingException {
		this.sendMessage(MailTypeEnum.HTML, from, to, subject, htmlMessage, attachments);
	}

// PARA TESTE	
//	public static void main(String[] args) throws AddressException, MessagingException {
//		SendMail mail = new SendMail("192.168.1.244", "25");
//		mail.sendHTMLMessage("email@keyworks.com.br", "email@keyworks.com.br", "Teste de email html", "<h1>Mensagem de teste HTML</h1>");
//	}

}
