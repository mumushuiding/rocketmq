package com.lt.cloud;

import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;


@Configuration
public class MQProducerConfig {
	public static final Logger LOGGER=LoggerFactory.getLogger(MQProducerConfig.class);
	@Value("${rocketmq.producer.groupName}")
    private String groupName;
	@Value("${rocketmq.producer.namesrvAddr}")
	private String namesrvAddr;
	/**
     * 消息发送超时时间，默认3秒
     */
    @Value("${rocketmq.producer.sendMsgTimeout}")
    private Integer sendMsgTimeout;
    /**
     * 消息发送失败重试次数，默认2次
     */
    @Value("${rocketmq.producer.retryTimesWhenSendFailed}")
    private Integer retryTimesWhenSendFailed;
    
    @Bean
    public DefaultMQProducer producer(){
        DefaultMQProducer producer=new DefaultMQProducer(this.groupName);
        producer.setNamesrvAddr(this.namesrvAddr);
        producer.setRetryTimesWhenSendFailed(retryTimesWhenSendFailed);
        try {
			producer.start();
			LOGGER.info(String.format("RocketMQ producer is start ! groupName:[%s],namesrvAddr:[%s]"
                    , producer.getProducerGroup(), producer.getNamesrvAddr()));
		} catch (MQClientException e) {
			e.printStackTrace();
		}
		return producer;
    }
}
