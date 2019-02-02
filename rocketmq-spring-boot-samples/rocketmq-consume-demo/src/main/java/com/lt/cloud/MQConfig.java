package com.lt.cloud;

import java.util.List;

import org.apache.rocketmq.client.consumer.DefaultMQPushConsumer;
import org.apache.rocketmq.client.consumer.MessageSelector;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyContext;
import org.apache.rocketmq.client.consumer.listener.ConsumeConcurrentlyStatus;
import org.apache.rocketmq.client.consumer.listener.MessageListenerConcurrently;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.common.message.MessageExt;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

//@Configuration
public class MQConfig {
	public static final Logger LOGGER=LoggerFactory.getLogger(MQConfig.class);
	@Value("${rocketmq.consumer.groupName}")
	private String consumerGroup;
	@Value("${rocketmq.consumer.namesrvAddr}")
	private String consumerNamestrAddr;
	
	@Value("${rocketmq.consumer.topic}")
	private String consumerTopic;
	@Value("${rocketmq.consumer.topic.selector}")
	private String messageSelector;
	@Bean
	public DefaultMQPushConsumer consumer() throws MQClientException {
		DefaultMQPushConsumer consumer=new DefaultMQPushConsumer(consumerGroup);
		consumer.setNamesrvAddr(consumerNamestrAddr);
		consumer.subscribe(consumerTopic, messageSelector);
		consumer.registerMessageListener(new MessageListenerConcurrently() {
			
			@Override
			public ConsumeConcurrentlyStatus consumeMessage(List<MessageExt> msgs, ConsumeConcurrentlyContext context) {
				System.out.printf("%s Receive New Messages: %s %n", Thread.currentThread().getName(), msgs);
				return ConsumeConcurrentlyStatus.CONSUME_SUCCESS;
			}
		});
		consumer.start();
		LOGGER.info("RocketMQ consumer is start ! groupName:[%s],namesrvAddr:[%s]"
				,consumer.getConsumerGroup(),consumer.getNamesrvAddr());
		return consumer;
	}
}
