# Notification Service

*Description:* Notification Service, listens to a RabbitMQ for a message

*Author:* Tarun Pal Singh

*Design:* 
* It is a Microservice based on Java
* It is a Java service (Java 1.8)
* It listens to a Queue (Rabbit MQ)
* If there is a message, it logs it and sends a email to the student Id(Email)

*Steps of Execution:* 
* Run RabbitMQ - C:\RabbitMQServer\rabbitmq_server-3.9.13\sbin\rabbitmq-server.bat
* Open project as Maven import in Eclipse - Run project (Spring Boot) -NotoficationService.java
* MQ runs at: http://localhost:15672/#/queues
* Message Format: <toEmailId>#<MessageBody>
