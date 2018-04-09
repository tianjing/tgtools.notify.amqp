package tgools.notify.rabbitmq.websocket;


import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.listener.AbstractMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.stereotype.Component;
import tgools.notify.rabbitmq.service.RabbitMqService;
import tgtools.util.LogHelper;

import java.io.IOException;

/**
 * @author 田径
 * @Title
 * @Description
 * @date 19:07
 */
@Component
public class ConsumerMap extends java.util.concurrent.ConcurrentHashMap<String, AbstractMessageListenerContainer> {
    public ConsumerMap(WsClientFactory pWsClientFactory)
    {
        mWsClientFactory=pWsClientFactory;
    }
    protected RabbitMqService mRabbitMqService = new RabbitMqService();
    protected WsClientFactory mWsClientFactory;

    public void createConsumer(String pLoginName) {
        try {
            SimpleMessageListenerContainer container = mRabbitMqService.createUserConsumer(pLoginName, new MessageListenerImpl(pLoginName), AcknowledgeMode.MANUAL);
            container.start();
            put(pLoginName, container);
            LogHelper.info("", "createConsumer name:" + pLoginName, "ConsumerMap");
        } catch (Exception e) {
            e.printStackTrace();
        }

    }


    public class MessageListenerImpl implements ChannelAwareMessageListener {
        private String mLoginName;

        public MessageListenerImpl(String pLoginName) {
            mLoginName = pLoginName;
        }

        @Override
        public void onMessage(Message pMsg, Channel pChannel) throws Exception {
            try {
                System.out.println(mLoginName + " onMessage:" + new String(pMsg.getBody(), "UTF-8"));
                ConsumerMap.this.mWsClientFactory.sendMessage(mLoginName, new String(pMsg.getBody(), "UTF-8"));
            } catch (Exception e) {
                LogHelper.error("", "onMessage Error", "MessageListenerImpl.onMessage", e);
            }

            try {
                pChannel.basicAck(pMsg.getMessageProperties().getDeliveryTag(), false);
            } catch (IOException e) {
                LogHelper.error("", "消息 ack 出错", "MessageListenerImpl.onMessage", e);
            }
        }

    }

}
