package com.bits.util;

import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import javax.mail.Authenticator;
import javax.mail.Message;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.context.annotation.Bean;
import org.springframework.context.support.PropertySourcesPlaceholderConfigurer;

public class EmailUtil {

	
	private static String host="smtp.gmail.com";
    private static String sslPort = "465";
    private static String port="465";
    private static String username="lib.system.tarunpalsingh@gmail.com";
    private static String password="tarunpal@12345";
    
    private static final Logger LOGGER = LogManager.getLogger(EmailUtil.class);
	
	@Bean
	public static PropertySourcesPlaceholderConfigurer propertyConfigInDev() {
		return new PropertySourcesPlaceholderConfigurer();
	}

	public static void sendEmail(String toMailId, String emailBody) {
		
		if(toMailId == null ||emailBody ==null) {
			return;
		}
		
		if(toMailId.length() == 0 || emailBody.length() == 0) {
			return;
		}

		LOGGER.info("SSLEmail Started for MailId: " + toMailId + " ; for body: "+ emailBody);

		Properties props = getProperties();

		Authenticator auth = new Authenticator() {
			//override the getPasswordAuthentication method
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
		props.put("mail.smtp.host", host); //SMTP Host
		props.put("mail.smtp.socketFactory.port", sslPort); //SSL Port
		props.put("mail.smtp.socketFactory.class",
				"javax.net.ssl.SSLSocketFactory"); //SSL Factory Class
		props.put("mail.smtp.starttls.enable","true"); 
		props.put("mail.smtp.auth", "true"); //Enabling SMTP Authentication
		props.put("mail.smtp.port", port); //SMTP Port
		return props;
	}

		/**
	 * Utility method to send simple HTML email
	 * @param session
	 * @param toEmail
	 * @param subject
	 * @param body
	 */
	public static void sendEmail(Session session, String toEmail, String subject, String body){
		try
	    {
		  LOGGER.info("Started mail sending to: "+ toEmail);
		  MimeMessage message = new MimeMessage(session);    
          message.addRecipient(Message.RecipientType.TO,new InternetAddress(toEmail));    
          message.setSubject(subject);    
          message.setText(body);    
          //send message  
          Transport.send(message);    
          System.out.println("message sent successfully");    

	         LOGGER.info(subject + " ||| Email sent successfully to: " + toEmail );

	    }
	    catch (Exception e) {
	    	e.printStackTrace();
	    	LOGGER.error("Email could not be sent to: " + toEmail);
	    }
	}


}
