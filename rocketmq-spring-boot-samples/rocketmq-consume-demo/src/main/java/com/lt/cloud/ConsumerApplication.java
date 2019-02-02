package com.lt.cloud;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.spring.annotation.RocketMQMessageListener;
import org.apache.rocketmq.spring.core.RocketMQListener;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class ConsumerApplication 
{	
	private static final Logger LOGGER=LoggerFactory.getLogger(ConsumerApplication.class);
    public static void main( String[] args )
    {
        SpringApplication.run(ConsumerApplication.class, args);
    }
    
    @Value("${rocketmq.consumer.groupName}")
    private static String CONSUMERGROUP;
    
    
    @Service
    @RocketMQMessageListener(topic="TopicTest",selectorExpression="*",consumerGroup="consumer")
    public class Myconsumer1 implements RocketMQListener<String>{

		@Override
		public void onMessage(String message) {
			LOGGER.info("received message:{}",message);
			
		}
    	
    }
    
    
    
}
