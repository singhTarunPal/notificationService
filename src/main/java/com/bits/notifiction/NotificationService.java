package com.bits.notifiction;

import java.nio.charset.StandardCharsets;

import javax.annotation.PostConstruct;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.bits.util.EmailUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;



@SpringBootApplication
@EnableAutoConfiguration
public class NotificationService {

	private final static String QUEUE_NAME = "lib_notif_queue";
	private static final Logger LOGGER = LogManager.getLogger(NotificationService.class);
	
	public static void main(String[] args) {
		SpringApplication.run(NotificationService.class, args);
	}
	
	 @PostConstruct
	    public void init() throws Exception{

			ConnectionFactory factory = new ConnectionFactory();
			factory.setHost("localhost");
			Connection connection = factory.newConnection();
			Channel channel = connection.createChannel();

			channel.queueDeclare(QUEUE_NAME, true, false, false, null);
			LOGGER.info(" [*] Waiting for messages. To exit press CTRL+C");

			DeliverCallback deliverCallback = (consumerTag, delivery) -> {
				String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
				LOGGER.info(" [x] Received message:'" + message + "'");
				try {
					String[] tokens = message.split("#");
					EmailUtil.sendEmail(tokens[0], tokens[1]);
				} catch (Exception e) {
					LOGGER.info("Error occured while extracting email Id or the Email body");
				}
			};
			channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
			});
		
	    }	
}