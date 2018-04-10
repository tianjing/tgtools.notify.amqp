package tgtools.notify.rabbitmq.service;

import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.ChannelAwareMessageListener;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import tgtools.exceptions.APPErrorException;
import tgtools.notify.rabbitmq.core.Constants;
import tgtools.util.LogHelper;
import tgtools.util.StringUtil;

import java.text.MessageFormat;

/**
 * @author 田径
 * @Title
 * @Description
 * @date 15:17
 */
public class RabbitMqService {

    protected RabbitAdmin mRabbitAdmin;
    protected Queue mTimeoutQueue;
    protected Exchange mClientSystemExchange;
    protected Exchange mServerSystemExchange;
    public RabbitMqService(RabbitAdmin pRabbitAdmin) {
        mRabbitAdmin = pRabbitAdmin;
    }

    public Queue getTimeoutQueue() {
        return mTimeoutQueue;
    }

    public void setTimeoutQueue(Queue pTimeoutQueue) {
        mTimeoutQueue = pTimeoutQueue;
    }

    public Exchange getClientSystemExchange() {
        return mClientSystemExchange;
    }

    public void setClientSystemExchange(Exchange pClientSystemExchange) {
        mClientSystemExchange = pClientSystemExchange;
    }

    public Exchange getServerSystemExchange() {
        return mServerSystemExchange;
    }

    public void setServerSystemExchange(Exchange pServerSystemExchange) {
        mServerSystemExchange = pServerSystemExchange;
    }

    public void initDeclare() {
        initQueue();
        initExchange();
    }

    protected void initQueue() {
        if (null != mTimeoutQueue) {
            mRabbitAdmin.declareQueue(mTimeoutQueue);
        }
    }

    protected void initExchange() {
        if (null != mClientSystemExchange) {
            mRabbitAdmin.declareExchange(mClientSystemExchange);
        }
        if (null != mServerSystemExchange) {
            mRabbitAdmin.declareExchange(mServerSystemExchange);
        }
    }

    public RabbitAdmin getRabbitAdmin() {
        return mRabbitAdmin;
    }

    public void setRabbitAdmin(RabbitAdmin pRabbitAdmin) {
        mRabbitAdmin = pRabbitAdmin;
    }

    /**
     * 获取用户队列名称
     *
     * @param pLoginName
     *
     * @return
     */
    public String getUserQueueName(String pLoginName) {
        return Constants.QUEUE_USER + "." + pLoginName;
    }

    /**
     * 创建用户队列
     *
     * @param pLoginName
     */
    public void createUserQueue(String pLoginName) {
        if (StringUtil.isNullOrEmpty(pLoginName)) {
            return;
        }
        String queueName = getUserQueueName(pLoginName);

        try {
            Queue sendQueue = new Queue(queueName, true, false, false, Constants.QUEUE_ARGS);
            mRabbitAdmin.declareQueue(sendQueue);

            Binding binding = BindingBuilder.bind(sendQueue).to(mClientSystemExchange).with(StringUtil.EMPTY_STRING).noargs();
            mRabbitAdmin.declareBinding(binding);

        } catch (Exception e) {
            LogHelper.error(StringUtil.EMPTY_STRING, "创建用户队列失败；原因：" + e.getMessage(), "RabbitMqService.CreateUserQueue", e);
        }
    }

    /**
     * 发消息给用户
     *
     * @param pLoginName
     * @param pMessage
     */
    public void sendToUserMessage(String pLoginName, Message pMessage) {
        mRabbitAdmin.getRabbitTemplate().send(StringUtil.EMPTY_STRING,getUserQueueName(pLoginName), pMessage);
    }

    /**
     * 发消息给用户
     *
     * @param pLoginName
     * @param pMessage
     */
    public void sendToUserMessage(String pLoginName, String pMessage) {
        mRabbitAdmin.getRabbitTemplate().convertAndSend(StringUtil.EMPTY_STRING,getUserQueueName(pLoginName), pMessage);
    }

    /**
     * 发送信息到 Constants.EXCHANGE_CLIENT_SYSTEM
     *
     * @param pMessage
     */
    public void sendToClientMessage(Message pMessage) {
        mRabbitAdmin.getRabbitTemplate().send(Constants.EXCHANGE_CLIENT_SYSTEM, StringUtil.EMPTY_STRING, pMessage);
    }

    /**
     * 发送信息到 Constants.EXCHANGE_CLIENT_SYSTEM
     *
     * @param pMessage
     */
    public void convertAndSendToClientMessage(String pMessage) {
        mRabbitAdmin.getRabbitTemplate().convertAndSend(Constants.EXCHANGE_CLIENT_SYSTEM, StringUtil.EMPTY_STRING, pMessage);
    }

    /**
     * 发送信息到 Constants.EXCHANGE_SERVER_SYSTEM
     *
     * @param pMessage
     */
    public void sendToServerMessage(Message pMessage) {
        mRabbitAdmin.getRabbitTemplate().send(Constants.EXCHANGE_SERVER_SYSTEM, StringUtil.EMPTY_STRING, pMessage);
    }

    /**
     * 发送信息到 Constants.EXCHANGE_SERVER_SYSTEM
     *
     * @param pMessage
     */
    public void convertAndSendToServerMessage(String pMessage) {
        mRabbitAdmin.getRabbitTemplate().convertAndSend(Constants.EXCHANGE_SERVER_SYSTEM, StringUtil.EMPTY_STRING, pMessage);
    }

    /**
     * 创建用户监听 （自动应答）
     *
     * @param pLoginName       用户登录名称（或唯一标识）
     * @param pMessageListener 消息处理
     */
    public SimpleMessageListenerContainer createUserConsumer(String pLoginName, ChannelAwareMessageListener pMessageListener) {
        return createUserConsumer(pLoginName, pMessageListener, AcknowledgeMode.AUTO);
    }

    /**
     * 创建用户监听
     *
     * @param pLoginName       用户登录名称（或唯一标识）
     * @param pMessageListener 消息处理
     * @param pAcknowledgeMode 应答模式
     */
    public SimpleMessageListenerContainer createUserConsumer(String pLoginName, ChannelAwareMessageListener pMessageListener, AcknowledgeMode pAcknowledgeMode) {
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer();
        container.setConnectionFactory(mRabbitAdmin.getRabbitTemplate().getConnectionFactory());
        container.setQueueNames(getUserQueueName(pLoginName));
        container.setMessageListener(pMessageListener);
        container.setAcknowledgeMode(pAcknowledgeMode);
        return container;
    }

    /**
     * 发消息给用户
     *
     * @param pExchange   exchange
     * @param pRoutingKey routingKey
     * @param pMessage    message
     */
    public void sendMessage(String pExchange, String pRoutingKey, String pMessage) {
        mRabbitAdmin.getRabbitTemplate().convertAndSend(pExchange, pRoutingKey, pMessage);
    }

    /**
     * @param pExchange   exchange
     * @param pRoutingKey routingKey
     * @param pMessage    message
     */
    public void sendMessage(String pExchange, String pRoutingKey, Message pMessage) {
        mRabbitAdmin.getRabbitTemplate().send(pExchange, pRoutingKey, pMessage);
    }


    /**
     * 启动用户监听
     *
     * @param pLoginName       用户登录名称（或唯一标识）
     * @param pMessageListener 消息处理
     * @param pAcknowledgeMode 应答模式
     *
     * @throws APPErrorException
     */
    public void createAndStartUserConsumer(String pLoginName, ChannelAwareMessageListener pMessageListener, AcknowledgeMode pAcknowledgeMode) throws APPErrorException {
        SimpleMessageListenerContainer container = createUserConsumer(pLoginName, pMessageListener, pAcknowledgeMode);
        try {
            container.start();
        } catch (Throwable ex) {
            throw new APPErrorException(MessageFormat.format("启动rabbitmq消费者失败；Name：{0};原因：{1}", pLoginName, ex.getMessage()), ex);
        }
    }

    /**
     * 启动用户监听  （自动应答）
     *
     * @param pLoginName       用户登录名称（或唯一标识）
     * @param pMessageListener 消息处理
     *
     * @throws APPErrorException
     */
    public void createAndStartUserConsumer(String pLoginName, ChannelAwareMessageListener pMessageListener) throws APPErrorException {
        SimpleMessageListenerContainer container = createUserConsumer(pLoginName, pMessageListener);
        try {
            container.start();
        } catch (Throwable ex) {
            throw new APPErrorException(MessageFormat.format("启动rabbitmq消费者失败；Name：{0};原因：{1}", pLoginName, ex.getMessage()), ex);
        }
    }

}
