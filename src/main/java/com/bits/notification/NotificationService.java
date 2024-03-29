package com.bits.notification;

import java.nio.charset.StandardCharsets;

import javax.annotation.PostConstruct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;

import com.bits.notification.util.OutlookEmailUtil;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import com.rabbitmq.client.DeliverCallback;

@SpringBootApplication
@EnableAutoConfiguration
@PropertySource({"classpath:application.properties"})
@ComponentScan(basePackages = "com.bits.notification.*")
public class NotificationService {

	private final static String QUEUE_NAME = "lib_notif_queue";
	
	
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
			System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

			DeliverCallback deliverCallback = (consumerTag, delivery) -> {
				String message = new String(delivery.getBody(), StandardCharsets.UTF_8);
				System.out.println(" [x] Received message:'" + message + "'");
				try {
					String[] tokens = message.split("#");
					OutlookEmailUtil.sendEmail(tokens[0], tokens[1]);
				} catch (Exception e) {
					System.out.println("Error occured while extracting email Id or the Email body");
				}
			};
			channel.basicConsume(QUEUE_NAME, true, deliverCallback, consumerTag -> {
			});
		
	    }	
}