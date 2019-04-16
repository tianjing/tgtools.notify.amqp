package tgtools.notify.rabbitmq.websocket;


import com.rabbitmq.client.Channel;
import org.springframework.amqp.core.AcknowledgeMode;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.*;
import org.springframework.amqp.rabbit.listener.api.*;
import tgtools.notify.rabbitmq.service.RabbitMqService;
import tgtools.util.LogHelper;

import java.io.Closeable;
import java.io.IOException;

/**
 * @author 田径
 * @Title
 * @Description
 * @date 19:07
 */

public class ConsumerMap implements Closeable {
    protected java.util.concurrent.ConcurrentHashMap<String, AbstractMessageListenerContainer> mContainers = new java.util.concurrent.ConcurrentHashMap<String, AbstractMessageListenerContainer>();
    protected AbstractClientWebSocketHandler mWebSocketHandler;
    protected RabbitMqService mRabbitMqService;

    public ConsumerMap(AbstractClientWebSocketHandler pWebSocketHandler) {
        mWebSocketHandler = pWebSocketHandler;
        if (null != mWebSocketHandler) {
            mRabbitMqService = new RabbitMqService(mWebSocketHandler.getRabbitAdmin());
        }
    }
    public boolean hasConsumer(String pLoginName)
    {
        return mContainers.containsKey(pLoginName);
    }
    public boolean hasConsumer(AbstractMessageListenerContainer pContainer)
    {
        return mContainers.containsValue(pContainer);
    }
    public AbstractMessageListenerContainer getConsumer(String pLoginName)
    {
        return mContainers.get(pLoginName);
    }

    public void createConsumer(String pLoginName) {
        try {
            SimpleMessageListenerContainer container = mRabbitMqService.createUserConsumer(pLoginName, new MessageListenerImpl(pLoginName), AcknowledgeMode.MANUAL);
            container.start();
            mContainers.put(pLoginName, container);
            LogHelper.info("", "createConsumer name:" + pLoginName, "ConsumerMap");
        } catch (Exception e) {
            LogHelper.error("", "createConsumer 出错；原因:" +e.toString(), "ConsumerMap",e);
        }

    }

    public void removeConsumer(String pLoginName) {
        if (mContainers.containsKey(pLoginName)) {
            AbstractMessageListenerContainer container = mContainers.get(pLoginName);
            if (null != container) {
                mContainers.remove(pLoginName);
                container.stop();
                container.destroy();
                LogHelper.info("", "destroyConsumer name:" + pLoginName, "ConsumerMap");
                container = null;
            }
        }
    }

    @Override
    public void close() {
        for (AbstractMessageListenerContainer item : mContainers.values()) {
            try {
                item.stop();
                item.destroy();
            } catch (Exception e) {
            }
        }
        LogHelper.info("", "destroyAllConsumer" , "ConsumerMap");
        mContainers.clear();
        mContainers=null;
    }


    public class MessageListenerImpl implements ChannelAwareMessageListener {
        protected String mLoginName;

        public MessageListenerImpl(String pLoginName) {
            mLoginName = pLoginName;
        }

        @Override
        public void onMessage(Message pMsg, Channel pChannel)  {

            try {
                System.out.println(mLoginName + " onMessage:" + new String(pMsg.getBody(), "UTF-8"));
                ConsumerMap.this.mWebSocketHandler.sendMessage(mLoginName, new String(pMsg.getBody(), "UTF-8"));
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
