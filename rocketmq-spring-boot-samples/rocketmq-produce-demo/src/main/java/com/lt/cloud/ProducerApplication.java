package com.lt.cloud;

import java.io.UnsupportedEncodingException;

import org.apache.rocketmq.client.exception.MQBrokerException;
import org.apache.rocketmq.client.exception.MQClientException;
import org.apache.rocketmq.client.producer.DefaultMQProducer;
import org.apache.rocketmq.client.producer.SendCallback;
import org.apache.rocketmq.client.producer.SendResult;
import org.apache.rocketmq.common.message.Message;
import org.apache.rocketmq.remoting.common.RemotingHelper;
import org.apache.rocketmq.remoting.exception.RemotingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
@RestController
public class ProducerApplication
{
    public static void main( String[] args )
    {
        SpringApplication.run(ProducerApplication.class, args);
    }
    @Autowired
    private DefaultMQProducer producer;
    @RequestMapping("/sync")
    public void syncProducer() throws UnsupportedEncodingException, MQClientException, RemotingException, MQBrokerException, InterruptedException {
    	System.out.println("namesrv:"+producer.getNamesrvAddr()+",group:"+producer.getProducerGroup());
    	for(int i=0;i<100;i++) {
    		Message message=new Message("TopicTest", "TagA", 
    				("Hello RocketMQ" + i).getBytes(RemotingHelper.DEFAULT_CHARSET));
    		SendResult sendResult=producer.send(message);
    		System.out.printf("%s%n",sendResult);
    	}
    	
    }
    @RequestMapping("/asyc")
    public void asyncProducer() throws UnsupportedEncodingException, MQClientException, RemotingException, InterruptedException {
    	for(int i=0;i<100;i++) {
    		final int index=i;
    		Message msg=new Message("TopicTest", "TagA", "OrderID188", "Hello world".getBytes(RemotingHelper.DEFAULT_CHARSET));
    		producer.send(msg, new SendCallback() {
				
				@Override
				public void onSuccess(SendResult sendResult) {
					
					System.out.printf("%-10d OK %s %n", index,
                            sendResult.getMsgId());
				}
				
				@Override
				public void onException(Throwable e) {
					 System.out.printf("%-10d Exception %s %n", index, e);
                     e.printStackTrace();
					
				}
			});
    	}
    }
    /**
     * One-way transmission is used for cases requiring moderate reliability, such as log collection.
     * @throws UnsupportedEncodingException 
     * @throws InterruptedException 
     * @throws RemotingException 
     * @throws MQClientException 
     */
    @RequestMapping("/oneway")
    public void onewayProducer() throws UnsupportedEncodingException, MQClientException, RemotingException, InterruptedException {
    	for (int i = 0; i < 100; i++) {
            //Create a message instance, specifying topic, tag and message body.
            Message msg = new Message("TopicTest" /* Topic */,
                "TagA" /* Tag */,
                ("Hello RocketMQ " +
                    i).getBytes(RemotingHelper.DEFAULT_CHARSET) /* Message body */
            );
            //Call send message to deliver message to one of brokers 不带确认直接发送，信息可能丢失，但是速度快
            producer.sendOneway(msg);
        }
        //Shut down once the producer instance is not longer in use.
    }

}
