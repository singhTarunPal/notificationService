package com.bits.notification.util;

import java.nio.charset.Charset;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.mail.Authenticator;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

import com.aspose.email.MailAddress;
import com.aspose.email.MailMessage;
import com.aspose.email.SmtpClient;

public class OutlookEmailUtil {

	private static String host = "smtp-mail.outlook.com";
	private static String sslPort = "465";
	private static String port = "465";
	private static String username = "singh.tarunpal@outlook.com";
	private static String password = "tarunpal@12345";

	private static final Logger LOGGER = LogManager.getLogger(OutlookEmailUtil.class);

	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	public static void sendEmail(String toMailId, String emailBody) {

		if (toMailId == null || emailBody == null) {
			return;
		}

		if (toMailId.length() == 0 || emailBody.length() == 0) {
			return;
		}

		LOGGER.info("SSLEmail Started for MailId: " + toMailId + " ; for body: " + emailBody);

		Properties props = getProperties();

		Authenticator auth = new Authenticator() {
			// override the getPasswordAuthentication method
			protected PasswordAuthentication getPasswordAuthentication() {
				return new PasswordAuthentication(username, password);
			}
		};

		Session session = Session.getDefaultInstance(props, auth);
		LOGGER.info("Email session created ");

		StringBuffer subject = new StringBuffer();
		StringBuffer body = new StringBuffer();

		subject.append(IConstants.APP_NAME);
		body.append(emailBody);

		ExecutorService emailExecutor = Executors.newSingleThreadExecutor();
		emailExecutor.execute(new Runnable() {
			@Override
			public void run() {
				sendEmail(session, toMailId, subject.toString(), body.toString());
			}
		});
		emailExecutor.shutdown();
	}

	private static Properties getProperties() {
		Properties props = new Properties();
		props.put("mail.smtp.host", host); // SMTP Host
		props.put("mail.smtp.socketFactory.port", sslPort); // SSL Port
		props.put("mail.smtp.socketFactory.class", "javax.net.ssl.SSLSocketFactory"); // SSL Factory Class
		props.put("mail.smtp.starttls.enable", "true");
		props.put("mail.smtp.auth", "true"); // Enabling SMTP Authentication
		props.put("mail.smtp.port", port); // SMTP Port
		return props;
	}

	/**
	 * Utility method to send simple HTML email
	 * 
	 * @param session
	 * @param toEmail
	 * @param subject
	 * @param body
	 */
	public static void sendEmail(Session session, String toEmail, String subject, String body) {
		MailMessage message = new MailMessage();
		LOGGER.info("Started mail sending to: "+ toEmail);

		// Set subject of the message, body and sender information
		message.setSubject(subject);
		message.setHtmlBody(body);
		message.setFrom(new MailAddress(username, "Tarun | Sender", false));

		// Specify encoding
		message.setBodyEncoding(Charset.forName("US-ASCII"));

		// Add To recipients and CC recipients
		message.getTo().addItem(new MailAddress(toEmail, "Recipient 1", false));
		//message.getCC().addItem(new MailAddress("cc1@domain.com", "Recipient 3", false));

		// Create an instance of SmtpClient Class
		SmtpClient client = new SmtpClient();

		// Specify your mailing host server, Username, Password, Port
		client.setHost(host);
		client.setUsername(username);
		client.setPassword(password);
		client.setPort(587);

		try {
			// Client.Send will send this message
			client.send(message);
		    LOGGER.info(subject + " ||| Email sent successfully to: " + toEmail );
		}

		catch (Exception ex) {
			ex.printStackTrace();
			LOGGER.error("Email could not be sent to: " + toEmail);
		}
		finally {
			client.close();
		}
	}

}
