# Notification Service

*Description:* Notification Service, listens to a RabbitMQ for a message

*Author:* Tarun Pal Singh

*Design:* 
* It is a Microservice based on Java
* It is a Java service (Java 1.8)
* It listens to a Queue (Rabbit MQ)
* If there is a message, it logs it and sends a email to the student Id(Email)
